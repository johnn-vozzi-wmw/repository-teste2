package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DocumentoAnexo;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FichaFinanceiraPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.util.Vector;

public class FichaFinanceiraService extends CrudPersonLavendereService {

    private static FichaFinanceiraService instance;

    private FichaFinanceiraService() {
        //--
    }

    public static FichaFinanceiraService getInstance() {
        if (instance == null) { 
            instance = new FichaFinanceiraService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FichaFinanceiraPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public double getValuesPedidos(Cliente cliente) throws SQLException {
    	return getVlPedidosCreditoAberto(cliente, null, null);
    }
    
	private Vector findClientePedidoList(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
    	return findClientePedidoList(cdEmpresa, cdRepresentante, cdCliente, null);
    }
    
	private Vector findClientePedidoList(String cdEmpresa, String cdRepresentante, String cdCliente, String flCreditoClienteAberto) throws SQLException {
    	Pedido pedidoFilter = new Pedido();
    	pedidoFilter.cdEmpresa = LavenderePdaConfig.usaPedidosTodasEmpresasSaldoCliente ? null : cdEmpresa;
    	pedidoFilter.cdRepresentante = cdRepresentante;
    	pedidoFilter.cdCliente = cdCliente;
    	pedidoFilter.flCreditoClienteAberto = flCreditoClienteAberto; 
    	return PedidoPdbxDao.getInstance().findClientePedidoList(pedidoFilter);
    }

	private boolean isNecessarioValidacaoLimiteCreditoCliente(Cliente cliente, Pedido pedido) throws SQLException {
		if (pedido != null && isParametrosValidacaoLimiteCreditoLigados(pedido) && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			if (ignoraLimiteCreditoCondicaoPagamento(pedido)) {
				return false;
			}
			if (ignoraLimiteCreditoTipoPedido(pedido)) {
				return false;
			}
			if (naoVerificaLimiteCreditoClienteEspecial(cliente)) {
				return false;
			}
			if (pedido.isPedidoNovoCliente()) {
				return false;
			}
			if (pedido.isPedidoBonificacao()) {
				return false;
			}
			if (pedido.isReplicandoPedido && pedido.isReplicandoPedidoAvisoCapaPedido) {
				return false;
			}
			if (isPagamentoPedidoAVista(cliente, pedido)) {
				return false;
			}
			if (isCondicaoETipoPagamentoAVistaQuandoClienteAtrasadoBloqueado(pedido)) {
				return false;
			}
			if (pedido.fechandoPedidoConsignado) {
				return false;
			}
			if (LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() && !pedido.onFechamentoPedido) {
				return false;
			}
			if (pedido.getTipoPagamento().isIgnoraLimiteCredito()) {
				return false;
			}
			return !LavenderePdaConfig.isObrigaAnexoDocClienteLimiteCredExtrapolado() || !DocumentoAnexoService.getInstance().hasAnexoNoPedido(DocumentoAnexo.NM_ENTIDADE_PEDIDO, pedido.getRowKey());
		}		
		return false;
	}

	private boolean isParametrosValidacaoLimiteCreditoLigados(Pedido pedido) {
		return LavenderePdaConfig.controlarLimiteCreditoCliente
				|| LavenderePdaConfig.bloquearLimiteCreditoCliente
				|| isLiberaComSenhaLimiteCreditoAoFecharPedido(pedido)
				|| isLiberaComSenhaLimiteCreditoNaoFechamento(pedido)
				|| LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito();
	}

	private boolean isLiberaComSenhaLimiteCreditoNaoFechamento(Pedido pedido) {
		return LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() && !LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() && !pedido.onFechamentoPedido;
	}

	private boolean isLiberaComSenhaLimiteCreditoAoFecharPedido(Pedido pedido) {
		return LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() && pedido.onFechamentoPedido;
	}

	private boolean isCondicaoETipoPagamentoAVistaQuandoClienteAtrasadoBloqueado(Pedido pedido) throws SQLException {
		return (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() || LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado())
				&& (ValueUtil.VALOR_ZERO.equals(StringUtil.getStringValue(pedido.getCondicaoPagamento().qtDiasMediosPagamento)) && pedido.getTipoPagamento().isTipoPagamentoAVista());
	}

	private boolean isPagamentoPedidoAVista(Cliente cliente, Pedido pedido) throws SQLException {
		return ((ValueUtil.isNotEmpty(pedido.getTipoPagamento().cdTipoPagamento) && pedido.getTipoPagamento().isTipoPagamentoAVista()) || pedido.isPagamentoAVista()) || ValueUtil.VALOR_SIM.equals(cliente.flClienteLiberadoPedidoAVista);
	}

	private boolean naoVerificaLimiteCreditoClienteEspecial(Cliente cliente) {
		return cliente.isEspecial() && !LavenderePdaConfig.verificaLimiteCreditoClienteEspecial;
	}

	private boolean ignoraLimiteCreditoTipoPedido(Pedido pedido) throws SQLException {
		return pedido.getTipoPedido() != null && pedido.getTipoPedido().isIgnoraLimiteCreditoCliente();
	}

	private boolean ignoraLimiteCreditoCondicaoPagamento(Pedido pedido) throws SQLException {
		return pedido.getCondicaoPagamento() != null && pedido.getCondicaoPagamento().isIgnoraLimiteCredito();
	}
	

	public void validateLimCred() throws SQLException {
		validateLimCred(null, null);
	}

	public void validateLimCred(Pedido pedidoTemp, ItemPedido item) throws SQLException {
		Cliente cliente = SessionLavenderePda.getCliente();
		boolean descontado = false;
		if (pedidoTemp != null && LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
			pedidoTemp.flPendenteLimCred = ValueUtil.VALOR_NAO;
		}
		if (isNecessarioValidacaoLimiteCreditoCliente(cliente, pedidoTemp) && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			Rede rede = null;
			pedidoTemp.isReplicandoPedidoAvisoCapaPedido = true;
			if (LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes) {
				rede = RedeService.getInstance().findRedeByCliente(SessionLavenderePda.getCliente());
			}
			double vlSaldo = getVlSaldoCliente(cliente, rede, pedidoTemp, item);
			if (item != null) {
				vlSaldo -= !pedidoTemp.isPedidoConsignado() ? ValueUtil.round(item.getVlTotalItemComTributos() + item.getVlTotalFrete()) : 0;
				descontado = true;
			}
			if (vlSaldo < 0) {
				vlSaldo *= -1;
				vlSaldo += !descontado && item != null && !pedidoTemp.isPedidoConsignado() ? ValueUtil.round(item.getVlTotalItemComTributos() + item.getVlTotalFrete()) : 0;
				String mensagem; 
				if (LavenderePdaConfig.isObrigaAnexoDocClienteLimiteCredExtrapolado()) {
					mensagem = Messages.MSG_LIMITE_CREDITO_CLIENTE_EXTRAPOLADO_SEM_ANEXO;
				} else {
					mensagem = LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes && rede != null ? Messages.MSG_LIMITE_CREDITO_REDE_EXTRAPOLADO : Messages.MSG_LIMITE_CREDITO_CLIENTE_EXTRAPOLADO;
				}
				if (LavenderePdaConfig.bloquearLimiteCreditoCliente
						|| (LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() && pedidoTemp.onFechamentoPedido)
						|| LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito()
						|| (!LavenderePdaConfig.isLiberaComSenhaLimiteCreditoClienteAoFecharPedido() && LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente() && !pedidoTemp.onFechamentoPedido)
						|| LavenderePdaConfig.controlarLimiteCreditoCliente
						|| LavenderePdaConfig.isObrigaAnexoDocClienteLimiteCredExtrapolado()){
					throw new ValidationException(MessageUtil.getMessage(mensagem, vlSaldo));
				} else {
					UiUtil.showInfoMessage(MessageUtil.getMessage(mensagem, vlSaldo));
				}
			}
		}
	}
	
    public double getVlSaldoCliente(Cliente cliente, Rede rede) throws SQLException {
    	return getVlSaldoCliente(cliente, rede, null, null);
    }
    
	public double getVlSaldoCliente(Cliente cliente, Rede rede, Pedido pedidoTemp, ItemPedido item) throws SQLException {
		double vlSaldoCliente;
		double vlSaldoRede;
		double vlSaldo;
		if (rede != null) {
			vlSaldoRede = rede.vlSaldo;
			Vector clienteRedeList = ClienteService.getInstance().findAllClientesRede(rede);
			double vlTotalPedidos = 0;
			int clienteRedeListSize = clienteRedeList.size();
			for (int i = 0; i < clienteRedeListSize; i++) {
				vlTotalPedidos += getVlPedidosCreditoAberto((Cliente)clienteRedeList.items[i], pedidoTemp, item);
			}
			vlSaldo = vlSaldoRede - vlTotalPedidos;
		} else {
			vlSaldoCliente = ValueUtil.getDoubleValue(FichaFinanceiraService.getInstance().getColumnFichaFinanceira(cliente, "VLSALDOCLIENTE"));
			vlSaldo = vlSaldoCliente - getVlPedidosCreditoAberto(cliente, pedidoTemp,  item);
		}
		return ValueUtil.round(vlSaldo);
	}
	
	public double getVlPedidosCreditoAberto(Cliente cliente, Pedido domainPedido, ItemPedido item) throws SQLException {
		double vlTotalPedidos = 0;
		if (cliente.vlTotalPedidosValidateLimiteCredito != -1 && domainPedido != null) {
			vlTotalPedidos = cliente.vlTotalPedidosValidateLimiteCredito;
			if (!LavenderePdaConfig.usaLimiteCreditoRedeCompartilhadoEmpresas) {
			 	vlTotalPedidos += domainPedido.getVlTotalPedidoGeral(item);
			}
			if (item != null) {
				Pedido pedidoTemp = (Pedido) domainPedido.clone();
				pedidoTemp.itemPedidoList = new Vector();
				PedidoService.getInstance().findItemPedidoList(pedidoTemp);
				int size = pedidoTemp.itemPedidoList.size();
				for (int j = 0; j < size; j++) {
					if (item.equals(pedidoTemp.itemPedidoList.items[j]) && !item.isItemTroca() && !item.isItemTrocaRecolher()) {
						ItemPedido itemPedido = (ItemPedido)pedidoTemp.itemPedidoList.items[j];
						vlTotalPedidos -= itemPedido.getVlTotalItemComTributos() + itemPedido.getVlTotalFrete();
					}
				}
			}
		} else {
			Vector pedidoList;
			if (LavenderePdaConfig.usaLimiteCreditoRedeCompartilhadoEmpresas && RedeService.getInstance().findRedeByCliente(cliente) == null) {
				pedidoList = findClientePedidoList(null, cliente.cdRepresentante, cliente.cdCliente, ValueUtil.VALOR_SIM);
			} else {
				pedidoList = findClientePedidoList(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCliente, ValueUtil.VALOR_SIM);
			}
    		if (!ValueUtil.isEmpty(pedidoList)) {
    			Pedido pedido;
    			int size = pedidoList.size();
    			for (int i = 0; i < size; i++) {
    				pedido = (Pedido) pedidoList.items[i];
					if (ignoraLimiteCreditoTipoPedido(pedido) || pedido.isPedidoConsignado() || pedido.isPagamentoAVista()) {
    					continue;
    				}
    					if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido())  {
    						PedidoService.getInstance().findItemPedidoList(pedido);
    						TributosService.getInstance().calculaVlTotalPedidoComTributos(pedido);
    					}
    					if (!((domainPedido != null && (domainPedido.nuPedido.equals(pedido.nuPedido) && (domainPedido.cdEmpresa.equals(pedido.cdEmpresa)) && (domainPedido.cdRepresentante.equals(pedido.cdRepresentante)))) && (item != null) && item.nuPedido.equals(pedido.nuPedido) && (item.cdEmpresa.equals(pedido.cdEmpresa)) && (item.cdRepresentante.equals(pedido.cdRepresentante)))) {
    						vlTotalPedidos += pedido.getVlTotalPedidoGeral(item);
    						if (domainPedido != null && item == null && (domainPedido.nuPedido.equals(pedido.nuPedido) && (domainPedido.cdEmpresa.equals(pedido.cdEmpresa)) && (domainPedido.cdRepresentante.equals(pedido.cdRepresentante))) && !LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes) { 
        						vlTotalPedidos -= pedido.getVlTotalPedidoGeral(item);
    						}
    					} else {
    						vlTotalPedidos += pedido.getVlTotalPedidoGeral();
    						PedidoService.getInstance().findItemPedidoList(pedido);
    						int size2 = pedido.itemPedidoList.size();
    						for (int j = 0; j < size2; j++) {
    							if (item.equals(pedido.itemPedidoList.items[j]) && !item.isItemTroca() && !item.isItemTrocaRecolher()) {
    								vlTotalPedidos -= ((ItemPedido)pedido.itemPedidoList.items[j]).getVlTotalItemComTributos() + ((ItemPedido)pedido.itemPedidoList.items[j]).getVlTotalFrete();
    							}
    						}

    					}
    				}
	    		pedidoList = null;
	    		pedido = null;
    		}
		}
		return vlTotalPedidos;
	}
	
    public String getColumnFichaFinanceira(Cliente cliente, String dsColumn) throws SQLException {
    	FichaFinanceira fichaFinanceiraFilter = new FichaFinanceira();
		fichaFinanceiraFilter.cdEmpresa = cliente.cdEmpresa;
		fichaFinanceiraFilter.cdRepresentante = cliente.cdRepresentante;
		fichaFinanceiraFilter.cdCliente = cliente.cdCliente;
		return findColumnByRowKey(fichaFinanceiraFilter.getRowKey(), dsColumn);
    }
    
	private boolean isNecessarioValidacaoLimiteCreditoConsignadoCliente(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.controlarLimiteCreditoCliente || LavenderePdaConfig.bloquearLimiteCreditoCliente || LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
			if (pedido != null && pedido.getCondicaoPagamento() != null && pedido.getCondicaoPagamento().isIgnoraLimiteCredito()) {
				return false;
			}
			if (pedido != null && pedido.getTipoPedido() != null && pedido.getTipoPedido().isIgnoraLimiteCreditoCliente()) {
				return false;
			}
			if (pedido != null && pedido.getCondicaoPagamento() != null && pedido.getCondicaoPagamento().isIgnoraLimiteCredito()) {
				return false;
			}
			if (pedido != null && pedido.getTipoPedido() != null && pedido.getTipoPedido().isIgnoraLimiteCreditoCliente()) {
				return false;
			}
			return true;
		}
		return false;
	}
	
	public void validaLimiteCreditoConsignado(Pedido pedido, boolean excluindoDevolucaoPedidoConsignado) throws SQLException {
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao() && isNecessarioValidacaoLimiteCreditoConsignadoCliente(pedido)) {
			double vlSaldo = getVlSaldoConsignadoCliente(pedido, pedido.getCliente(), excluindoDevolucaoPedidoConsignado);
			if (vlSaldo < 0) {
				vlSaldo *= -1;
				throw new ValidationException(MessageUtil.getMessage(Messages.MSG_LIMITE_CREDITO_CONSIGNADO_CLIENTE_EXTRAPOLADO, vlSaldo)); 
			}
		}
	}
	
	public double getVlSaldoConsignadoCliente(Pedido pedido, Cliente cliente, boolean excluindoDevolucaoPedidoConsignado) throws SQLException {
		double vlDevolucoes = pedido != null ? pedido.vlTotalDevolucoes : 0;
		return ValueUtil.round(cliente.vlLimiteCreditoConsig - (vlDevolucoes + getVlPedidosConsignadoCreditoAberto(pedido, cliente)));
	}
	
	public double getVlPedidosConsignadoCreditoAberto(Pedido pedidoTemp, Cliente cliente) throws SQLException {
		double vlTotalPedidos = 0;
		if (cliente.vlTotalPedidosValidateLimiteCreditoConsignado != -1 && (pedidoTemp != null)) {
			vlTotalPedidos = cliente.vlTotalPedidosValidateLimiteCreditoConsignado;
			vlTotalPedidos += pedidoTemp.getVlTotalPedidoGeral();
		} else {
    		Vector pedidoList = findClientePedidoList(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCliente);
    		if (ValueUtil.isNotEmpty(pedidoList)) {
    			Pedido pedido;
    			int size = pedidoList.size();
    			for (int i = 0; i < size; i++) {
    				pedido = (Pedido) pedidoList.items[i];
    				if (ignoraLimiteCreditoTipoPedido(pedido) || !pedido.isPedidoConsignado()) {
    					continue;
    				}
    					if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido())  {
    						PedidoService.getInstance().findItemPedidoList(pedido);
    						TributosService.getInstance().calculaVlTotalPedidoComTributos(pedido);
    					}
    					if (!(((pedidoTemp != null) && (pedidoTemp.nuPedido.equals(pedido.nuPedido) && (pedidoTemp.cdEmpresa.equals(pedido.cdEmpresa)) && (pedidoTemp.cdRepresentante.equals(pedido.cdRepresentante)))))) {
    						vlTotalPedidos += pedido.getVlTotalPedidoGeral();
    						if ((pedidoTemp != null) && (pedidoTemp.nuPedido.equals(pedido.nuPedido) && (pedidoTemp.cdEmpresa.equals(pedido.cdEmpresa)) && (pedidoTemp.cdRepresentante.equals(pedido.cdRepresentante)))) {
        						vlTotalPedidos -= pedido.getVlTotalPedidoGeral();
    						}
    					} else {
    						vlTotalPedidos += pedido.getVlTotalPedidoGeral();
    					}
    				}
	    		pedidoList = null;
	    		pedido = null;
    		}
		}
		return vlTotalPedidos;
	}

    public FichaFinanceira getFichaFinanceira(Cliente cliente) throws SQLException {
		FichaFinanceira fichaFinanceiraFilter = new FichaFinanceira();
		fichaFinanceiraFilter.cdEmpresa = cliente.cdEmpresa;
		fichaFinanceiraFilter.cdRepresentante = cliente.cdRepresentante;
		fichaFinanceiraFilter.cdCliente = cliente.cdCliente;
		FichaFinanceira fichaFinanceira = (FichaFinanceira)findByRowKey(fichaFinanceiraFilter.getRowKey());
		if (fichaFinanceira == null) {
			fichaFinanceira = new FichaFinanceira();
		}
		fichaFinanceiraFilter = null;
		return fichaFinanceira;
    }

    public FichaFinanceira getFichaFinanceiraDyn(Cliente cliente) throws SQLException {
    	FichaFinanceira fichaFinanceiraFilter = new FichaFinanceira();
    	fichaFinanceiraFilter.cdEmpresa = cliente.cdEmpresa;
    	fichaFinanceiraFilter.cdRepresentante = cliente.cdRepresentante;
    	fichaFinanceiraFilter.cdCliente = cliente.cdCliente;
    	FichaFinanceira fichaFinanceira = (FichaFinanceira)findByRowKeyDyn(fichaFinanceiraFilter.getRowKey());
    	fichaFinanceiraFilter = null;
    	return fichaFinanceira;
    }

	public Vector findDistinctStatusCliente(String cdRepresentanteFilter) throws SQLException {
		FichaFinanceira fichaFinanceira = new FichaFinanceira();
		fichaFinanceira.cdEmpresa = SessionLavenderePda.cdEmpresa;
		fichaFinanceira.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cdRepresentanteFilter : SessionLavenderePda.getRepresentante().cdRepresentante;
		return FichaFinanceiraPdbxDao.getInstance().findDistinctStatusCliente(fichaFinanceira);
	}
}
