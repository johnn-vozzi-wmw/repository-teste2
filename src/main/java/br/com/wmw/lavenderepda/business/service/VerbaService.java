package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ComiRentabilidade;
import br.com.wmw.lavenderepda.business.domain.DescontoGrupo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoBonifCfg;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.Verba;
import br.com.wmw.lavenderepda.business.domain.VerbaGrupoSaldo;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.validation.CondicaoComercialVerbaException;
import br.com.wmw.lavenderepda.business.validation.ValidationVerbaPersonalizadaException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VerbaPdbxDao;
import totalcross.sys.InvalidNumberException;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

public class VerbaService extends CrudService {

    private static VerbaService instance;

    private VerbaService() {
        //--
    }

    public static VerbaService getInstance() {
        if (instance == null) {
            instance = new VerbaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VerbaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    private double getVerbaGeradaItemPedido(ItemPedido itemPedido) throws SQLException {
		ItemPedido itemPedidoBase = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(itemPedido.getRowKey());
		if (usaVerbaItemPedidoPorItemTabPreco(itemPedido.pedido) && itemPedidoBase != null) {
			itemPedido.vlVerbaItemOld = itemPedidoBase.vlVerbaItem;
			itemPedido.vlVerbaItemPositivoOld = itemPedidoBase.vlVerbaItemPositivo;
		}
		return itemPedidoBase != null ?  itemPedidoBase.vlVerbaItem + itemPedidoBase.vlVerbaItemPositivo + itemPedidoBase.vlVerbaGrupoItem : 0;
    }
    
    public void validateSaldo(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba() && !itemPedido.isIgnoraControleVerba() && !itemPedido.ignoraValidacaoVerbaTemp) {
			boolean itemTemVerbaConsumida = itemPedido.vlVerbaItem < 0 || itemPedido.vlVerbaGrupoItem < 0;
			boolean itemTeveVerbaGeradaDiminuida = itemPedido.vlVerbaItemPositivoOld > itemPedido.vlVerbaItemPositivo && LavenderePdaConfig.geraVerbaPositiva;
			boolean usaPendenciaVerbaSaldoGrupo = LavenderePdaConfig.isUsaMotivosPendenciaVerbaDeGrupoExtrapolada() && VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(itemPedido.pedido);
			boolean isItemPendenteVerbaGrupo = false;
			if (itemTemVerbaConsumida || itemTeveVerbaGeradaDiminuida) {
				validateFornecedor(itemPedido);
				
				double vlTolerancia = getVlTolerancia(itemPedido);
				double vlSaldo = getVlSaldo(itemPedido);
				final double vlVerba = getCurrentVlVerba(itemPedido);
				final double vlVerbaOld = getVerbaGeradaItemPedido(itemPedido);
				
				if (isVerbaPersonalizadaGrupo()) {
					final double vlSaldoPedidosAbertos = VerbaGrupoSaldoService.getInstance().getSaldoConsumidoVerbaGrupoPedidosAbertos(itemPedido); 
					vlSaldo += vlSaldoPedidosAbertos;
					vlTolerancia += vlVerbaOld;
				}
				
				double tot = vlSaldo + (vlVerba - vlVerbaOld);
				if (tot * -1 > vlTolerancia) {
					if (vlTolerancia == 0) {
						if (LavenderePdaConfig.geraVerbaPositiva && (itemPedido.vlVerbaItemPositivo > 0 || itemPedido.vlVerbaItemPositivoOld > 0)) { 
						    throw new CondicaoComercialVerbaException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_POSITIVO_ALTERADO, StringUtil.getStringValueToInterface(tot)));
						} else if (usaPendenciaVerbaSaldoGrupo) { 
							isItemPendenteVerbaGrupo = true;
						} else { 
						    if (LavenderePdaConfig.liberaComSenhaPedidoBonificacaoComSaldoVerbaExtrapolado) { 
						        UiUtil.showWarnMessage(Messages.VERBASALDO_SALDO_NEGATIVO); 
						        return; 
						    } else {
						    	if (LavenderePdaConfig.usaSugestaoVendaPorDivisao) {
						    		VerbaGrupoSaldoService.getInstance().estornaVerbaSaldo(itemPedido.pedido, itemPedido, true);
						    	}
						        String[] args = { StringUtil.getStringValueToInterface(vlVerba), StringUtil.getStringValueToInterface(vlSaldo - vlVerbaOld) };   
						        throw new CondicaoComercialVerbaException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_INDISPONIVEL, args));
						    }
						}
					} else {
						if (!(VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(itemPedido.pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor())) {
							itemPedido.vlVerbaItemOld = 0;
							itemPedido.vlVerbaItemPositivoOld = 0;
						}
						if (isVerbaPersonalizadaGrupo()) {
							double vlSaldoErp = VerbaGrupoSaldoService.getInstance().getVlSaldoErpRelatorio(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
							if (vlSaldoErp < 0) {
								vlTolerancia += vlSaldoErp;
							}
						}
						String[] args = { StringUtil.getStringValueToInterface(vlSaldo), StringUtil.getStringValueToInterface(vlTolerancia), StringUtil.getStringValueToInterface(vlSaldo + getCurrentVlVerba(itemPedido)) };
						throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_NEGATIVA_ACIMA_TOLERANCIA_CONFIRMA, args));
					}
				}
    		}
			if (usaPendenciaVerbaSaldoGrupo) {
				ItemPedidoService.getInstance().marcaItemPedidoPorVerbaSaldoGrupoProduto(itemPedido, isItemPendenteVerbaGrupo);
			}
    	}
    	if (!usaVerbaItemPedidoPorItemTabPreco(itemPedido.pedido)) {
    		itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
    		itemPedido.vlVerbaItemPositivoOld = itemPedido.vlVerbaItemPositivo;
    	}
    	itemPedido.ignoraValidacaoVerbaTemp = false;
    }

	public boolean usaVerbaItemPedidoPorItemTabPreco(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && !((VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) && LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor());
	}

    private double getCurrentVlVerba(final ItemPedido itemPedido) {
    	return itemPedido.vlVerbaItem + itemPedido.vlVerbaItemPositivo + itemPedido.vlVerbaGrupoItem;
    }

    public void validateSaldo(Pedido pedido, double oldVlVerbaTotalPedido) throws SQLException {
		if (!pedido.isIgnoraControleVerba() && !pedido.isSimulaControleVerba()) {
			if (isVerbaPersonalizadaGrupo()) {
				for (ItemPedido itemPedido : VectorUtil.iterateOver(pedido.itemPedidoList, ItemPedido.class)) {
					validateSaldo(itemPedido);
				}
			} else if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
				double vlVerbaTotalPedido = 0;
				double vlSaldo = 0;
				int size = pedido.itemPedidoList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					if ((itemPedido.vlVerbaItem < 0) || ((itemPedido.vlVerbaItemPositivo > 0) && (LavenderePdaConfig.geraVerbaPositiva || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco)) && !itemPedido.isIgnoraControleVerba()) {
						vlVerbaTotalPedido += itemPedido.vlVerbaItem + itemPedido.vlVerbaItemPositivo;
						if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
							vlSaldo += VerbaFornecedorService.getInstance().getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdFornecedor);
						} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
							vlSaldo += VerbaGrupoSaldoService.getInstance().getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
						}
					}
				}
				if (LavenderePdaConfig.isUsaVerbaUsuario()) {
					vlSaldo = VerbaUsuarioService.getInstance().getVlSaldo();
				}
				if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
					vlSaldo = VerbaSaldoVigenciaService.getInstance().getVlSaldo();
				}
				if (vlSaldo == 0d) {
					vlSaldo = VerbaSaldoService.getInstance().getVlSaldo(pedido.getCdContaCorrente(), pedido.dtEmissao);
				}
				vlSaldo -= oldVlVerbaTotalPedido;
				if (vlVerbaTotalPedido != 0 && (vlSaldo + vlVerbaTotalPedido < 0)) {
					double vlSaldoRestante = vlSaldo + vlVerbaTotalPedido;
					double vlTolerancia = getVlTolerancia(pedido);
					if (vlSaldoRestante < 0 && vlSaldoRestante * -1 < vlTolerancia && vlTolerancia > 0) {
						if (!UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_NEGATIVA_ABAIXO_TOLERANCIA_CONFIRMA, vlSaldoRestante))) {
							throw new ValidationException(Messages.VERBASALDO_CANCELADO_USUARIO);
						}
					} else {
						String[] args = { StringUtil.getStringValueToInterface(vlVerbaTotalPedido), StringUtil.getStringValueToInterface(vlSaldo) };
						throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_INDISPONIVEL, args));
					}
				}
			}
		}
    }


    public void validateDeleteSaldoPositivo(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba() && !itemPedido.pedido.isSimulaControleVerba()) {
    		if ((itemPedido.vlVerbaItemPositivo > 0 || itemPedido.vlVerbaItemPositivoOld > 0) && LavenderePdaConfig.geraVerbaPositiva) {
    			double vlSaldo = getVlSaldo(itemPedido);
    			double tot = vlSaldo - itemPedido.vlVerbaItemPositivoOld;
    			if (tot < 0) {
    				throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_POSITIVO_INDISPONIVEL, tot));
    			}
    		}
    	}
    }

    public void validateDeleteSaldoPositivo(Pedido pedido) throws SQLException {
    	if (pedido.vlVerbaPedidoPositivo > 0 && !pedido.isIgnoraControleVerba() && !pedido.isSimulaControleVerba()) {
    		double vlSaldo = 0;
    		if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
    			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
    				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
    				vlSaldo += VerbaFornecedorService.getInstance().getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdFornecedor);
    			}
    		} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
    			vlSaldo = VerbaUsuarioService.getInstance().getVlSaldo();
    		} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
                for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
                    ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
                    vlSaldo += VerbaGrupoSaldoService.getInstance().getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
				}
    		} else if (vlSaldo == 0d) {
    			vlSaldo = VerbaSaldoService.getInstance().getVlSaldo(pedido.getCdContaCorrente(), pedido.dtEmissao);
    		}
    		double tot = vlSaldo - pedido.vlVerbaPedidoPositivo - pedido.vlVerbaPedido;
    		if (tot < 0) {
    			throw new ValidationException(MessageUtil.getMessage(Messages.VERBASALDO_MSG_SALDO_POSITIVO_INDISPONIVEL_PEDIDO, tot));
    		}
    	}
    }

    public double getVlSaldo(ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
			return VerbaFornecedorService.getInstance().getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdFornecedor);
    	} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
			return VerbaUsuarioService.getInstance().getVlSaldo();
		} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(itemPedido.pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			if (isVerbaPersonalizadaGrupo()) {
				return VerbaGrupoSaldoService.getInstance().getVlTotalGrupoSaldo(itemPedido);
			}
			return VerbaGrupoSaldoService.getInstance().getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
		} else if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			return VerbaSaldoVigenciaService.getInstance().getVlSaldo() + itemPedido.pedido.getVlVerbaPedidoDisponivel();
    	} else if (!LavenderePdaConfig.usaVerbaPorFaixaMargemContribuicao) {
    		return VerbaSaldoService.getInstance().getVlSaldo(itemPedido.pedido.getCdContaCorrente(), itemPedido.dtEmissaoPedido);
    	}
    	return 0;
    }

    public double getVlTolerancia(final ItemPedido itemPedido) throws SQLException {
		if (isVerbaPersonalizadaGrupo()) {
			return VerbaGrupoSaldoService.getInstance().getVlToleranciaDisponivel(itemPedido);
		}
    	return getVlTolerancia(itemPedido.pedido);
    }
    public double getVlTolerancia(Pedido pedido) throws SQLException {
    	if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
    		return 0;
    	} else {
    		return SessionLavenderePda.usuarioPdaRep.representante.vlToleranciaVerba;
    	}
    }

    private boolean isItemPedidoPersisteVerba(ItemPedido itemPedido) throws SQLException {
    	itemPedido.auxiliarVariaveis.insertingVerba = true;
	    boolean ignoraVerba = itemPedido.pedido.isIgnoraControleVerba() || itemPedido.isIgnoraControleVerba();
	    itemPedido.auxiliarVariaveis.insertingVerba = false;
	    return !ignoraVerba && (itemPedido.vlVerbaItem <= 0 || (itemPedido.vlVerbaItemPositivo >= 0 && LavenderePdaConfig.geraVerbaPositiva));
    }

    public void insertVlSaldo(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	if (isItemPedidoPersisteVerba(itemPedido)) {
    		boolean verbaGerada = false;
    		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
    			verbaGerada = geraVerbaGrupoSaldoPersonalizada(pedido, itemPedido);
    		} else if (!itemPedido.pedido.isSimulaControleVerba()) {
    			if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
    				VerbaFornecedorService.getInstance().insertVlSaldo(itemPedido);
    				verbaGerada = true;
    			} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
    				VerbaUsuarioService.getInstance().insertVlSaldo(itemPedido);
    				verbaGerada = true;
    			} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
    				VerbaGrupoSaldoService.getInstance().insertVlSaldo(pedido, itemPedido, false);
    				verbaGerada = true;
    			} else if (!LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
    				VerbaSaldoService.getInstance().insertVlSaldo(itemPedido.pedido, itemPedido, false);
    				verbaGerada = true;
    			}
    		}

    		if (verbaGerada) {
    			itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
    			itemPedido.vlVerbaItemPositivoOld = itemPedido.vlVerbaItemPositivo;
    			itemPedido.vlVerbaGrupoOld = itemPedido.vlVerbaGrupoItem;
    		}
    	}
    }

    public void updateVlSaldo(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	if (isItemPedidoPersisteVerba(itemPedido)) {
    		if (!itemPedido.pedido.isSimulaControleVerba()) {
    			boolean isVerbaPersonalizadaGrupo = isVerbaPersonalizadaGrupo();
    			if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
    				VerbaFornecedorService.getInstance().updateVlSaldo(itemPedido);
    			} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
    				VerbaUsuarioService.getInstance().updateVlSaldo(itemPedido);
    			} else if ((VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) && (!isVerbaPersonalizadaGrupo || isHouveAlteracao(pedido, itemPedido))) {
    				VerbaGrupoSaldoService.getInstance().updateVlSaldo(pedido, itemPedido, isVerbaPersonalizadaGrupo);
    			} else if (!LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
    				VerbaSaldoService.getInstance().updateVlSaldo(itemPedido);
    			}
    		}
    		itemPedido.vlVerbaItemOld = itemPedido.vlVerbaItem;
    		itemPedido.vlVerbaItemPositivoOld = itemPedido.vlVerbaItemPositivo;
    		itemPedido.vlVerbaGrupoOld = itemPedido.vlVerbaGrupoItem;
    	} 
    }

	private boolean isHouveAlteracao(Pedido pedido, ItemPedido itemPedido) {
		return !itemPedido.flUIChange && !pedido.flUIChange;
	}

    public void deleteVlSaldo(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	deleteVlSaldo(pedido, itemPedido, false);
    }
    
    public void deleteVlSaldo(Pedido pedido, ItemPedido itemPedido, boolean forceDelete) throws SQLException {
    	if (isVerbaPersonalizadaGrupo()) {
    		if (isHouveAlteracao(pedido, itemPedido)) {
    			VerbaGrupoSaldoService.getInstance().deleteVlSaldo(pedido, itemPedido, true);
    		}
    	} else {
    		if (isItemPedidoPersisteVerba(itemPedido) && !itemPedido.pedido.isSimulaControleVerba() || forceDelete) {
    			if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
    				VerbaFornecedorService.getInstance().deleteVlSaldo(itemPedido);
    			} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
    				VerbaUsuarioService.getInstance().deleteVlSaldo(itemPedido);
    			} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
    				VerbaGrupoSaldoService.getInstance().deleteVlSaldo(pedido, itemPedido, false);
    			} else if (!LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
    				VerbaSaldoService.getInstance().deleteVlSaldo(itemPedido);
    			}
    		}
    	}
    }

    public void updateVerbaSaldoItemPedido(Pedido pedido) throws SQLException {
    	if (pedido.getTipoPedido() != null && (pedido.isSimulaControleVerba() || pedido.isIgnoraControleVerba())) {
			return;
		}
    	for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
    		ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
    		if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
    			VerbaFornecedorService.getInstance().updateVerbaSaldoItemPedidoAposEnvioServidor(itemPedido);
    		} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
				VerbaUsuarioService.getInstance().updateVerbaSaldoItemPedidoAposEnvioServidor(itemPedido);
    		} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
    			VerbaGrupoSaldoService.getInstance().updateVerbaGrupoSaldoItemPedido(pedido, itemPedido);
    		} else {
    			if (!LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo() || LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo() && LavenderePdaConfig.apresentaConsumoVerbaDePedidoNaoTransmitido) {
    				VerbaSaldoService.getInstance().updateVerbaSaldoItemPedido(itemPedido);
    			}
    		}
		}

    }

    public void recalculateAndUpdateVerbaPda() throws SQLException {
    	if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
    		acoesRecalculoVerbaPersonalizada();
    	} else {
    		acoesRecalculoVerbaNormal();
    	}
    }

	private void acoesRecalculoVerbaPersonalizada() throws SQLException {
		if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
			VerbaFornecedorService.getInstance().recalculateAndUpdateVerbaFornecedorPda();
		}
		if (LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto() || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			VerbaGrupoSaldoService.getInstance().recalculateAndUpdateVerbaGrupoSaldoPda();
		}
	}

	private void acoesRecalculoVerbaNormal() throws SQLException {
		if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) { 
    		VerbaFornecedorService.getInstance().recalculateAndUpdateVerbaFornecedorPda();
    	} else if (LavenderePdaConfig.isUsaVerbaUsuario()) {
			VerbaUsuarioService.getInstance().recalculateAndUpdateVerbaUsuarioPda();
    	} else if (LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto() || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
    		VerbaGrupoSaldoService.getInstance().recalculateAndUpdateVerbaGrupoSaldoPda();
    	} else if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
    		VerbaSaldoVigenciaService.getInstance().recalculateAndUpdateVerbaPda();
    	} else {
    		VerbaSaldoService.getInstance().recalculateAndUpdateVerbaSaldoPda();
    	}
	}

    public double somaVerbaPositiva(Pedido pedido) throws SQLException {
    	if (!pedido.isIgnoraControleVerba()) {
    		double vlVerba = 0;
    		int size = pedido.itemPedidoList.size();
    		for (int i = 0; i < size; i++) {
    			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
    			vlVerba += itemPedido.vlVerbaItemPositivo;
    		}
    		return vlVerba;
    	}
    	return 0;
    }
    
    public void aplicaVerbaItemPedidoPorFaixaRentabilidadeComissao(Pedido pedido, ItemPedido itemPedido, ComiRentabilidade comiRentabilidadeAtingida) throws SQLException {
    	if (!pedido.isIgnoraControleVerba() && !itemPedido.isOportunidade() && (pedido.getTipoPedido() != null && !pedido.getTipoPedido().isBonificacao()) && itemPedido.isItemVendaNormal() && itemPedido.getProduto().isUtilizaVerba() && !itemPedido.isIgnoraControleVerba()) {
    		boolean possuiDescPromocionalTabPreco = itemPedido.possuiItemTabelaPreco() && itemPedido.getItemTabelaPreco().vlPctDescPromocional > 0;
    		boolean possuiGrupoDescPromocional = DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(itemPedido);
    		boolean possuiGrupoDescPromocionalQtde = DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocionalPorQtde(itemPedido);
    		if (possuiDescPromocionalTabPreco || possuiGrupoDescPromocional || possuiGrupoDescPromocionalQtde) {
    			return;
    		}
    		if (comiRentabilidadeAtingida != null && comiRentabilidadeAtingida.vlPctRentabilidade != 0d) {
    			itemPedido.vlVerbaItem = 0;
    			itemPedido.vlVerbaItemPositivo = 0;
    			itemPedido.vlPctVerba = 0;
    			double vlPctRentabilidadeMinSemTolerancia = itemPedido.getProduto().vlPctMinRentabilidade;
    			double vlPctRentabilidadeMinComTolerancia = ItemPedidoService.getInstance().getVlPctRentabilidadeMinima(itemPedido);
    			double vlTotalItemRentMinSemTolerancia = ValueUtil.round((itemPedido.vlTotalItemPedido * vlPctRentabilidadeMinSemTolerancia) / itemPedido.getVlPctRentabilidadeLiquida());
    			double vlTotalItemRentMinComTolerancia = ValueUtil.round((itemPedido.vlTotalItemPedido * vlPctRentabilidadeMinComTolerancia) / itemPedido.getVlPctRentabilidadeLiquida());
    			
    			if (itemPedido.vlTotalItemPedido < vlTotalItemRentMinSemTolerancia && itemPedido.vlTotalItemPedido >= vlTotalItemRentMinComTolerancia) {
    				itemPedido.vlVerbaItem = itemPedido.vlTotalItemPedido - vlTotalItemRentMinSemTolerancia;
    			}
    			if (LavenderePdaConfig.isMostraFlexPositivoPedido() && comiRentabilidadeAtingida.vlPctVerba > 0) {
    				itemPedido.vlVerbaItemPositivo = ValueUtil.round(itemPedido.vlTotalItemPedido * (comiRentabilidadeAtingida.vlPctVerba / 100));
    				itemPedido.vlPctVerba = comiRentabilidadeAtingida.vlPctVerba;
    			}
    		}
    	}
    }

    public void aplicaVerbaNoItemPedido(ItemPedido itemPedido, Pedido pedido) throws SQLException {
    	if (isPermiteControleVerba(pedido, itemPedido)) {
    		if ((LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) && itemPedido.isItemVendaNormal() && itemPedido.getProduto().isUtilizaVerba()) {
    			aplicaVerbaNormal(itemPedido, pedido);
    			if ((LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) && consomeVerbaGrupoSaldoPrecoLiberadoSenha(itemPedido)) {
    				aplicaVerbaPositiva(itemPedido, pedido);
    			}
    		} else if (LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba()
    				|| (pedido.isPedidoBonificacao() && !LavenderePdaConfig.naoDescontaVerbaEmPedidoBonificacao)
    				|| isPermiteBonificarQualquerProduto(itemPedido)) {
    			if (itemPedido.isItemBonificacao()) {
    				aplicaVerbaNormalBonificacao(itemPedido);
    			} else if (itemPedido.isItemVendaNormal() && !LavenderePdaConfig.informaVerbaManual) {
    				itemPedido.vlVerbaItem = 0;
    			}
    		}
    		if (!pedido.pedidoSimulacao && LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex() && itemPedido.vlBaseFlex == 0) {
    			itemPedido.vlBaseFlex = itemPedido.getItemTabelaPreco().getVlBaseFlex(pedido, itemPedido);
    		}
    	}
    }
    
    private boolean isPermiteBonificarQualquerProduto(final ItemPedido itemPedido) {
    	return !itemPedido.isInserindoItemPoliticaBonificacao && LavenderePdaConfig.isPermiteBonificarQualquerProduto();
    }
    
    private boolean isPermiteControleVerba(Pedido pedido, ItemPedido itemPedido) throws SQLException {
	    if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
	    	if (itemPedido.isFlPrecoLiberadoSenha() || itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizadoOuPendente(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO)) {
				resetVerba(pedido, itemPedido);
				return false;
			}
	    }
    	if (pedido.isIgnoraControleVerba() || itemPedido.isOportunidade()) {
    		return false;
    	}
		if (!itemPedido.isTabelaPrecoOldIgnoraControleVerba() && itemPedido.isIgnoraControleVerba()) {
			resetVerba(pedido, itemPedido);
		}
		if (itemPedido.isIgnoraControleVerba()) {
			return false;
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacao() && ItemPedidoBonifCfgService.getInstance().findItemPedidoBonifCfgByItemPedido(itemPedido) != null) {
			return false;
		}
		return true;
    }

	public void resetVerba(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		VerbaService.getInstance().deleteVlSaldo(pedido, itemPedido, true);
		itemPedido.vlVerbaItem = 0;
		itemPedido.vlVerbaItemPositivo = 0;
		itemPedido.vlVerbaItemOld = 0;
		itemPedido.vlVerbaItemPositivoOld = 0;
	}

	public void aplicaVerbaPorGrupoProdComTolerancia(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	if (pedido != null && itemPedido != null && !pedido.isIgnoraControleVerba() && !itemPedido.isOportunidade() && !itemPedido.isIgnoraControleVerba()) {
    		boolean hasVerbaGrupoSaldo = VerbaGrupoSaldoService.getInstance().hasVerbaGrupoSaldoWeb(itemPedido);
    		if (hasVerbaGrupoSaldo) {
	    		if (itemPedido.isItemVendaNormal() && itemPedido.getProduto().isUtilizaVerba()) {
					calculaVerbaConsumidaPorGrupoProdComTolerancia(itemPedido);
					calculaVerbaGeradaPorGrupoProdComTolerancia(itemPedido);
	    		} else if (LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() || (pedido.isPedidoBonificacao() && !LavenderePdaConfig.naoDescontaVerbaEmPedidoBonificacao)) {
	    			if (itemPedido.isItemBonificacao()) {
	    				calculaVerbaConsumidaBonificacaoPorGrupoProdComTolerancia(itemPedido);
	    			} else if (itemPedido.isItemVendaNormal()) {
	    				itemPedido.vlVerbaItem = 0;
	    			}
	    		}
	    		aplicaCdVerbaGrupoItem(itemPedido);
    		}
    	}
    }
    
    private void calculaVerbaConsumidaPorGrupoProdComTolerancia(ItemPedido itemPedido) {
    	double vlBase = itemPedido.vlBaseItemPedido;
    	if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.descQuantidade != null) {
    		vlBase  = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.descQuantidade.vlPctDesconto / 100)));
    	} 
		itemPedido.vlVerbaItem = ValueUtil.round((itemPedido.vlItemPedido - vlBase) * itemPedido.getQtItemFisico());
		if (itemPedido.vlVerbaItem > 0) {
			itemPedido.vlVerbaItem = 0;
		}
    }
    
    private void calculaVerbaGeradaPorGrupoProdComTolerancia(ItemPedido itemPedido) {
		itemPedido.vlVerbaItemPositivo = ValueUtil.round((itemPedido.vlItemPedido - itemPedido.vlBaseItemPedido) * itemPedido.getQtItemFisico());
		if (itemPedido.vlVerbaItemPositivo < 0) {
			itemPedido.vlVerbaItemPositivo = 0;
		}
    }
    
    private void calculaVerbaConsumidaBonificacaoPorGrupoProdComTolerancia(ItemPedido itemPedido) {
    	itemPedido.vlVerbaItem = ValueUtil.round(itemPedido.vlTotalItemPedido * -1);
		if (itemPedido.vlVerbaItem > 0) {
			itemPedido.vlVerbaItem = 0;
		}
    }
    
    private void aplicaCdVerbaGrupoItem(ItemPedido itemPedido) throws SQLException {
    	if (itemPedido.vlVerbaItem != 0 || itemPedido.vlVerbaItemPositivo != 0) {
			VerbaGrupoSaldo verbaGrupoSaldo = VerbaGrupoSaldoService.getInstance().getVerbaGrupoSaldoWeb(itemPedido);
			if (verbaGrupoSaldo != null) {
				itemPedido.cdVerbaGrupo = verbaGrupoSaldo.cdVerbaGrupo;
			}
		} else {
			itemPedido.cdVerbaGrupo = 0;
		}
    }

    private boolean aplicaVerbaNormalBonificacao(ItemPedido itemPedido) throws SQLException {
    	if (!itemPedido.pedido.isIgnoraControleVerba()
    			|| LavenderePdaConfig.isPermiteBonificarQualquerProduto()
    			|| permiteConsumirVerbaSupervisor(itemPedido.pedido)) {
    		double vlItemCalcVerba = LavenderePdaConfig.consomePrecoMinimoItemBonificado && LavenderePdaConfig.isPermiteBonificarProdutoPedidoUsandoVerba() ? itemPedido.getItemTabelaPreco().vlBase * itemPedido.getQtItemFisico() : itemPedido.vlTotalItemPedido;
    		itemPedido.vlVerbaItem = ValueUtil.round(vlItemCalcVerba * -1);
    		if (itemPedido.vlVerbaItem > 0) {
    			itemPedido.vlVerbaItem = 0;
    		}
    		itemPedido.vlVerbaItemPositivo = 0;
    		return true;
    	}
    	return false;
    }

    private void aplicaVerbaNormal(ItemPedido itemPedido, Pedido pedido) throws SQLException {
    	if (!pedido.isIgnoraControleVerba()) {
    		double vlBase = getVlBaseFlex(pedido, itemPedido);

    		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
    			geraVerbaGrupoSaldoPersonalizada(pedido, itemPedido, calculateVlVerbaItemPedido(itemPedido, vlBase));
    		} else if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			    itemPedido.vlVerbaItem = calculateVlVerbaInterpolacao(itemPedido);
    		} else if (LavenderePdaConfig.usaValorComStCalculoVerba) {
    			itemPedido.vlVerbaItem = calculateVlVerbaItemComSt(itemPedido, vlBase);
    		} else {
    			itemPedido.vlVerbaItem = calculateVlVerbaItemPedido(itemPedido, vlBase);
    		}
    		itemPedido.vlBaseFlex = ValueUtil.round(vlBase);
    		itemPedido.vlVerbaItem = Math.min(itemPedido.vlVerbaItem, 0);
        }
    }

    private double calculateVlVerbaItemComSt(final ItemPedido itemPedido, final double vlBase) {
    	return ValueUtil.round((itemPedido.getVlItemComSt() - vlBase) * itemPedido.getQtItemFisico());
    }

    private double calculateVlVerbaItemPedido(final ItemPedido itemPedido, final double vlBase) {
    	return ValueUtil.round((getVlItemPedidoCalcVerba(itemPedido, vlBase) - vlBase) * itemPedido.getQtItemFisico());
    }

	private double calculateVlVerbaInterpolacao(ItemPedido itemPedido) {
		try {
			// Ajustado para problema no truncamento da verba
			BigDecimal vlEfetivoTotalItem = BigDecimal.valueOf(itemPedido.vlEfetivoTotalItem * 10000);
			BigDecimal desc = BigDecimal.valueOf(itemPedido.vlPctDesconto / 100 * 10000);
			BigDecimal k = BigDecimal.valueOf(10000);
			BigDecimal vlVerba = vlEfetivoTotalItem.multiply(desc).divide(k, 7, BigDecimal.ROUND_HALF_UP).divide(k, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba, BigDecimal.ROUND_FLOOR).negate();
			return vlVerba.doubleValue();
		} catch (ArithmeticException | IllegalArgumentException | InvalidNumberException e) {
			ExceptionUtil.handle(e);
			return 0d;
		}
	}
    
    private double getVlItemPedidoCalcVerba(ItemPedido itemPedido, double vlBase) {
	    if (itemPedido.hasDescProgressivo()) {
		    return vlBase;
	    }
    	if (!LavenderePdaConfig.usaDescontoExtra) return itemPedido.vlItemPedido;
    	return itemPedido.vlItemPedido / (1 - itemPedido.vlPctDesconto2 / 100);
    }
    
    public void aplicaVerbaPositiva(ItemPedido itemPedido, Pedido pedido) throws SQLException {
    	if (!pedido.isIgnoraControleVerba()) {
    		double vlBase;
    		if (LavenderePdaConfig.usaValorBaseDoItemParaVerbaPositiva) {
    			vlBase = itemPedido.vlBaseItemPedido;
    			if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem && pedido.getCliente().isOptanteSimples()) {
    				vlBase = ValueUtil.round(vlBase - itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples);
    			}
    		} else if (LavenderePdaConfig.usaValorBaseItemTabelaPrecoParaVerbaPositiva) {
    			vlBase = itemPedido.vlBaseItemTabelaPreco;
    		} else {
    			vlBase = getVlBaseFlex(pedido, itemPedido);
    		}
    		//--
    		if (LavenderePdaConfig.informaVerbaManual) {
    			double vlBaseItemPedido = itemPedido.vlBaseItemPedido;
    			if (vlBaseItemPedido <= vlBase) {
    				itemPedido.vlVerbaItemPositivo = 0;
    				return;
    			}
    		}
    		//--
    		if (!LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
    			double vlVerbaItemPositivo = 0;
    			if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
    				vlVerbaItemPositivo = calculateVlVerbaItemPedido(itemPedido, vlBase);
    				geraVerbaGrupoSaldoPersonalizada(pedido, itemPedido, vlVerbaItemPositivo);
    			} else {
    				if (LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco && vlBase <= itemPedido.vlItemPedido) {
    					vlVerbaItemPositivo = itemPedido.vlTotalItemPedido * VerbaGrupoProdutoService.getInstance().getVlPctVerba(itemPedido) / 100;
    				} else if (LavenderePdaConfig.usaValorComStCalculoVerba) {
    					vlVerbaItemPositivo = calculateVlVerbaItemComSt(itemPedido, vlBase);
    				} else {
    					vlVerbaItemPositivo = calculateVlVerbaItemPedido(itemPedido, vlBase);
    				}
    				
    				if (LavenderePdaConfig.isUsaDivisaoValorVerbaUsuarioEmpresa()) {
    					double vlPctVerbaDivisaoUsuario = LavenderePdaConfig.usaDivisaoValorVerbaUsuarioEmpresa;
    					itemPedido.vlVerbaItemPositivo = ValueUtil.round(vlVerbaItemPositivo * (vlPctVerbaDivisaoUsuario / 100));
    					itemPedido.vlPctVerbaRateio = vlPctVerbaDivisaoUsuario;
    				} else {
    					itemPedido.vlVerbaItemPositivo = vlVerbaItemPositivo;
    				}
    			}
    		}
    		calculaVerbaComImpostoERentabilidade(itemPedido);
    		itemPedido.vlVerbaItemPositivo = Math.max(itemPedido.vlVerbaItemPositivo, 0);
    	}
    }

    private double getConsumoVlTolerancia(final ItemPedido itemPedido) throws SQLException {
    	if (itemPedido.vlVerbaGrupoItem == 0) return 0;
    	double vlSaldo = VerbaGrupoSaldoService.getInstance().getVlTotalGrupoSaldoTolerancia(itemPedido);
    	vlSaldo += VerbaGrupoSaldoService.getInstance().getSaldoConsumidoVerbaGrupoPedidosAbertos(itemPedido);
    	vlSaldo = vlSaldo < 0 ? itemPedido.vlVerbaGrupoItem : vlSaldo + itemPedido.vlVerbaGrupoItem;
    	return Math.min(vlSaldo, 0);
    }

    private void geraVerbaGrupoSaldoPersonalizada(Pedido pedido, ItemPedido itemPedido, double vlVerbaItem) throws SQLException {
    	if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido)) {
			itemPedido.vlVerbaGrupoItem = vlVerbaItem;
			if (itemPedido.vlToleranciaVerGruSaldoOld > 0) {
				itemPedido.vlToleranciaVerGruSaldo = itemPedido.vlToleranciaVerGruSaldoOld;
				itemPedido.vlToleranciaVerGruSaldoOld = 0;
			} else {
				itemPedido.vlToleranciaVerGruSaldo = getConsumoVlTolerancia(itemPedido);
				itemPedido.vlToleranciaVerGruSaldoOld = itemPedido.vlToleranciaVerGruSaldo;
			}
		}
    }

    private double getVlBaseFlex(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	if (pedido.pedidoSimulacao && !itemPedido.isFazParteKitFechado() && !itemPedido.isRecebeuDescontoPorQuantidade()) {
    		return itemPedido.vlBaseFlex;
    	}
	    if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) && LavenderePdaConfig.usaDescMaxPrecoLiberadoConsomeVerbaGrupoProduto && itemPedido.isFlPrecoLiberadoSenha()) {
	    	if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
	    		return SolAutorizacaoService.getInstance().getVlLiberadoBySolAutorizacao(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
		    } else {
			    return itemPedido.vlItemMinAfterLibPreco;
		    }
	    }
	    if (LavenderePdaConfig.usaValorBaseDoItemParaVerbaNegativa) {
		    return itemPedido.vlBaseItemPedido;
	    }
    	double vlBase = itemPedido.getItemTabelaPreco().getVlBaseFlex(pedido, itemPedido);
		if (LavenderePdaConfig.aplicaDescontoQuantidadeVlBaseFlex && LavenderePdaConfig.isUsaDescontoQtdPorGrupo()) {
			vlBase = getVlBaseFlexBaseadoFaixaDescontoGrupo(vlBase, itemPedido, DescontoGrupoService.getInstance().calcDescQtdeGrupoUnidadeAlternativa(itemPedido));
		}
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			return itemPedido.vlPrecoEfetivoUnitario;
		}
    	return ValueUtil.round(vlBase);
    }

    protected double getVlBaseFlexBaseadoFaixaDescontoGrupo(double vlBase, ItemPedido itemPedido, Vector descGrupoList) throws SQLException {
		double qtItem = 0;
		double vlPctDesconto = 0;
		int size = descGrupoList.size();
		for (int i = 0; i < size; i++) {
			DescontoGrupo descontoGrupo = (DescontoGrupo) descGrupoList.items[i];
			if (itemPedido.getQtItemFisico() >= descontoGrupo.qtItem && descontoGrupo.qtItem > qtItem) {
				qtItem = descontoGrupo.qtItem;
				vlPctDesconto = descontoGrupo.vlPctDesconto;
			}
		}
		if (vlPctDesconto > 0) {
			if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar()) {
				double vlBaseUnidadeElemComDesconto = ValueUtil.round(itemPedido.vlUnidadePadrao * (1 - vlPctDesconto / 100));
				vlBase = ItemPedidoService.getInstance().calculaVlItemByVlElementar(itemPedido, vlBaseUnidadeElemComDesconto);
			} else {
				vlBase = vlBase * (1 - vlPctDesconto / 100);
			}
		}
		return vlBase;
    }

	public void calculaVerbaComImpostoERentabilidade(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado && !itemPedido.pedido.isPedidoCriticoOuConversaoFob()) {
			Tributacao tributacao = itemPedido.getTributacaoItem();
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
			if (tributacao != null && itemTabelaPreco != null) {
				double vlItemComDesconto = ValueUtil.round(itemPedido.vlBaseItemPedido - (itemPedido.vlBaseItemPedido * itemTabelaPreco.vlPctDescPromocional / 100));
				if (itemPedido.getItemPedidoAud().isGeraVerba() && ((itemTabelaPreco.isFlPromocao() && itemPedido.vlItemPedido > vlItemComDesconto) || (!itemTabelaPreco.isFlPromocao() && itemPedido.vlItemPedido > itemPedido.vlBaseItemTabelaPreco))) {
					if (tributacao.vlIcmsRetido != 0) {
						itemPedido.vlVerbaItemPositivo = (itemPedido.vlItemPedido - itemPedido.vlBaseItemPedido) * (1 - tributacao.vlPctPis / 100);
					} else {
						itemPedido.vlVerbaItemPositivo = (itemPedido.vlItemPedido - itemPedido.vlBaseItemPedido) * (1 - (tributacao.vlPctPis + tributacao.vlPctIcms) / 100);
					}
				} else {
					itemPedido.vlVerbaItemPositivo = 0d;
				}
				if ((itemTabelaPreco.isFlPromocao() && itemPedido.vlItemPedido < vlItemComDesconto) || (!itemTabelaPreco.isFlPromocao() && itemPedido.vlItemPedido < itemPedido.vlBaseItemPedido)) {
					double vlVerbaNecessaria;
					double vlVerbaEmpresa = itemTabelaPreco.isFlPromocao() ? itemPedido.getItemPedidoAud().vlVerbaEmpresa : 0d;
					if (tributacao.vlIcmsRetido != 0) {
						vlVerbaNecessaria = (itemPedido.getItemPedidoAud().vlItemPedidoNeutro - itemPedido.vlItemPedido) * (1 - tributacao.vlPctPis / 100) - vlVerbaEmpresa;	
						itemPedido.getItemPedidoAud().vlVerbaNecessaria = vlVerbaNecessaria >= 0 ? vlVerbaNecessaria : 0;
					} else {
						vlVerbaNecessaria = (itemPedido.getItemPedidoAud().vlItemPedidoNeutro - itemPedido.vlItemPedido) * (1 - (tributacao.vlPctPis + tributacao.vlPctIcms) / 100) - vlVerbaEmpresa;	
						itemPedido.getItemPedidoAud().vlVerbaNecessaria = vlVerbaNecessaria >= 0 ? vlVerbaNecessaria : 0;
					}
				} else {
					itemPedido.getItemPedidoAud().vlVerbaNecessaria = 0d;
				}
			}
		}
	}
	
	private void validateFornecedor(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaVerbaSaldoPorFornecedor()) {
			if (ValueUtil.isEmpty(itemPedido.getProduto().cdFornecedor)) {
				throw new ValidationException(Messages.VERBAFORNECEDOR_MSG_PRODUTO_SEM_FORNECEDOR);
			}
			if (!VerbaFornecedorService.getInstance().existeFornecedor(itemPedido)) {
				throw new ValidationException(Messages.VERBAFORNECEDOR_MSG_FORNECEDOR_NAO_EXISTE);
			}
		}
	}
	
	public void consomeVerbaGrupoSaldoPersonalizada(Pedido pedido) throws SQLException {
		double vlVerbaGrupo = 0d;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			boolean consumiuVerbaItem = false;
			String lastValidation = Verba.VERBA_NAO_CONSUMIDA;
			if (itemPedido.vlVerbaGrupoItem != 0) {
				vlVerbaGrupo += itemPedido.vlVerbaGrupoItem;
			}
			double vlVerbaItem = itemPedido.vlVerbaItem;
			double vlVerbaExtrapolada = 0d;
			if (itemPedido.vlVerbaItem < 0) {
				double vlVerbaDisponivel = VerbaGrupoSaldoService.getInstance().getVlVerbaDisponivelValidacaoPersonalizada(itemPedido);
				itemPedido.vlToleranciaVerGruSaldo = 0d;
				vlVerbaGrupo += itemPedido.vlVerbaGrupoItem = vlVerbaItem;
				double diferenca = vlVerbaItem + vlVerbaDisponivel;
				itemPedido.vlToleranciaVerGruSaldo = diferenca < 0 ? diferenca : 0d;
				if (!consumiuVerbaItem) {
					itemPedido.vlVerbaItem -= itemPedido.vlVerbaGrupoItem;
					itemPedido.vlVerbaItem = Math.min(itemPedido.vlVerbaItem, 0d);
				}
				double vlToleranciaErp = VerbaGrupoSaldoService.getInstance().getVlToleranciaErp(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
				double vlSaldo = VerbaGrupoSaldoService.getInstance().getVlSaldo(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.getProduto().cdGrupoProduto1);
				if (vlVerbaItem + vlSaldo + vlToleranciaErp >= 0) {
					VerbaGrupoSaldoService.getInstance().insertVlSaldo(pedido, itemPedido, true);
					lastValidation = Verba.VERBA_COSUMIDA;
					itemPedido.consumiuVerbaGrupoSaldo = true;
				} else {
					vlVerbaExtrapolada = vlVerbaItem + vlSaldo + vlToleranciaErp;
				}
				if (itemPedido.vlVerbaGrupoItem < 0) {
					VerbaGrupoSaldoService.getInstance().insertVlSaldo(pedido, itemPedido, true);
				}
				itemPedido.consumiuVerbaGrupoSaldo = itemPedido.vlVerbaGrupoItem != 0d;
				lastValidation = itemPedido.vlVerbaGrupoItem != 0d ? Verba.VERBA_GRUPO_PRODUTO : lastValidation;
				if (!Verba.VERBA_COSUMIDA.equals(lastValidation)) {
					validaVerbaNegativaPersonalizada(itemPedido, vlVerbaExtrapolada, lastValidation);
				}
				ItemPedidoService.getInstance().updateValuesVerba(itemPedido);
			}
		}
		pedido.vlVerbaGrupo = vlVerbaGrupo;
	}

	private void validaVerbaNegativaPersonalizada(ItemPedido itemPedido, double vlVerbaItem, final String areaException) throws SQLException {
		if (Math.abs(vlVerbaItem) > 1E-7) {
			throw new ValidationVerbaPersonalizadaException(areaException, vlVerbaItem, itemPedido.getProduto().toString(), itemPedido);
		}
	}
	
	public void deleteSaldoVerbaGrupoSaldoPersonalizada(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.consumiuVerbaGrupoSaldo && itemPedido.vlVerbaGrupoItem < 0) {
				VerbaGrupoSaldoService.getInstance().deleteVlSaldo(pedido, itemPedido, true);
				itemPedido.vlVerbaGrupoItem = 0d;
				itemPedido.vlToleranciaVerGruSaldo = 0d;
				itemPedido.consumiuVerbaGrupoSaldo = false;
			}
			aplicaVerbaNoItemPedido(itemPedido, pedido);
			ItemPedidoService.getInstance().updateValuesVerba(itemPedido);
		}
		pedido.vlVerbaGrupo = 0d;
	}
	
	public boolean geraVerbaGrupoSaldoPersonalizada(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		boolean verbaGerada = false;
		if (itemPedido.vlVerbaGrupoItem != 0 && !itemPedido.flUIChange) {
			VerbaGrupoSaldoService.getInstance().insertVlSaldo(pedido, itemPedido, true);
			verbaGerada = true;
		}
		return verbaGerada;
	}
	
	public boolean isVerbaPersonalizadaGrupo() {
		return LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada();
	}
	
	public double getVerbaPersonalizadaPedido(Pedido pedido, boolean consumo) {
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) {
			return 0d;
		}
		boolean isVerbaGrupo = isVerbaPersonalizadaGrupo();
		double vlVerbaPedido = 0d;
		int size = pedido.itemPedidoList.size();

		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (isVerbaGrupo) {
				vlVerbaPedido += !consumo || itemPedido.vlVerbaGrupoItem < 0d ? itemPedido.vlVerbaGrupoItem : 0d;
			}
			vlVerbaPedido += itemPedido.vlVerbaItem + (!consumo ? itemPedido.vlVerbaItemPositivo : 0d);
		}

		return vlVerbaPedido;
	}
	
	public double getVerbaGrupoSaldoPersonalizadaItemPedido(ItemPedido itemPedido,  boolean consumo) {
		double vlVerbaItem = 0d;
		if (isVerbaPersonalizadaGrupo()) {
			vlVerbaItem += !consumo || itemPedido.vlVerbaGrupoItem < 0d ? itemPedido.vlVerbaGrupoItem : 0d;
		}
		vlVerbaItem += itemPedido.vlVerbaItem + (!consumo ? itemPedido.vlVerbaItemPositivo : 0d);
		return vlVerbaItem;
	}

	public boolean consomeVerbaGrupoSaldoPrecoLiberadoSenha(ItemPedido itemPedido) throws SQLException {
    	if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(itemPedido.pedido) && LavenderePdaConfig.usaDescMaxPrecoLiberadoConsomeVerbaGrupoProduto) {
		    return !itemPedido.isFlPrecoLiberadoSenha();
	    } else {
    		return true;
	    }
	}

	private boolean aplicaVerbaBonifCfg(ItemPedido itemPedido, double baseQtBonificacao, double baseVlBonificacao) {
		if (baseQtBonificacao > 0) {
			double qtBonificacao = Math.max(baseQtBonificacao, itemPedido.getQtItemFisico());
			itemPedido.vlVerbaItem = -itemPedido.vlItemPedido * ValueUtil.round(qtBonificacao - baseQtBonificacao, 0);
			return true;
		} else if (baseVlBonificacao > 0) {
			double vlBonificacao = Math.max(baseVlBonificacao, itemPedido.vlTotalItemPedido);
			itemPedido.vlVerbaItem = -vlBonificacao + baseVlBonificacao;
			return true;
		}
		return false;
	}
	
	public boolean updateVerbaBonifCfg(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.isItemBonificacao() && LavenderePdaConfig.isUsaConsumoVerbaSupervisor()) {
			if (aplicaVerbaBonifCfg(itemPedido)) {
				ItemPedidoService.getInstance().updateValuesVerba(itemPedido);
				return true;
			}
		}
		return false;
	}
	
	private boolean aplicaVerbaBonifCfg(ItemPedido itemPedido) throws SQLException {
		if (!aplicaVerbaBonifCfg(itemPedido, itemPedido.qtBonificadoBonifCfg, itemPedido.vlBonificado)) {
			return aplicaVerbaNormalBonificacao(itemPedido);
		}
		return true;
	}
	
	public void aplicaVerbaBonifCfg(ItemPedido itemPedido, ItemPedidoBonifCfg itemBonif) {
		if (itemBonif != null) {
			aplicaVerbaBonifCfg(itemPedido, itemBonif.qtBonificacao, itemBonif.vlBonificacao);
		}
	}

	public boolean permiteConsumirVerbaSupervisor(Pedido pedido) throws SQLException {
		return LavenderePdaConfig.isUsaConsumoVerbaSupervisor() && pedido.isPedidoPendenteBonificacao();
	}

}
