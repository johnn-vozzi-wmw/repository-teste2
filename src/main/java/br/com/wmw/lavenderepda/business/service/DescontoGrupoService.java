package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescontoGrupo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.validation.DescAcresMaximoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescontoGrupoDbxDao;
import totalcross.util.Vector;

public class DescontoGrupoService extends CrudService {

    private static DescontoGrupoService instance;

    private DescontoGrupoService() {
        //--
    }

    public static DescontoGrupoService getInstance() {
        if (instance == null) {
            instance = new DescontoGrupoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescontoGrupoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	
    }
    
    public void validatePctMaxDescontoMaiorFaixaDisponivel(ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.usaDescQuantidadePorPacote && ValueUtil.isNotEmpty(itemPedido.cdPacote)) return;
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			if (ValueUtil.isEmpty(itemPedido.getProduto().cdGrupoDescProd)) {
				return;
			}
		} else if (ValueUtil.isEmpty(itemPedido.getProduto().cdGrupoProduto1)) {
			return;
		}
		DescontoGrupo descGrupoProdFilter = new DescontoGrupo();
		descGrupoProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		descGrupoProdFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			descGrupoProdFilter.cdGrupoDescProd = itemPedido.getProduto().cdGrupoDescProd;
			descGrupoProdFilter.cdGrupoProduto1 = "0";
		} else {
			descGrupoProdFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
			descGrupoProdFilter.cdGrupoDescProd = "0";
		}
		if (LavenderePdaConfig.usaDescontoQtdeGrupoPorTabelaPreco) {
			descGrupoProdFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		} else {
			descGrupoProdFilter.cdTabelaPreco = "0";
		}
		double vlPctMaxDesconto = ValueUtil.round(ValueUtil.getDoubleValue(maxByExample(descGrupoProdFilter, "vlPctDesconto")));
		if (!ValueUtil.VALOR_SIM.equals(itemPedido.flPrecoLiberadoSenha) && !ValueUtil.VALOR_SIM.equals(itemPedido.pedido.flPrecoLiberadoSenha) && ValueUtil.round(itemPedido.vlPctDesconto) > vlPctMaxDesconto && !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata) {
			double vlPermitido = itemPedido.vlBaseItemPedido * (1 - vlPctMaxDesconto / 100);
			throw new DescAcresMaximoException(MessageUtil.getMessage(Messages.DESCONTOGRUPO_MSG_ERROR_MAXDESC_FAIXA, new String[] { StringUtil.getStringValueToInterface(ValueUtil.round(itemPedido.vlPctDesconto)), StringUtil.getStringValueToInterface(vlPctMaxDesconto) }), vlPermitido);
		}
    }
    
	public Vector verificaPctMaxDescontoPorGrupoProduto(final Pedido pedido, final boolean lanceException) throws SQLException {
		if (LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido
				|| LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata
				|| LavenderePdaConfig.validaDescMaxMesmoComDescQuantidadeEDescontoGrupo.equals("2")
				|| !TipoPedidoService.getInstance().validaDescontoItem(pedido.getTipoPedido())) {
			return new Vector(0);
		}
		Vector itensNaoConforme = new Vector();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if (ValueUtil.isNotEmpty(itemPedido.cdPacote) || (LavenderePdaConfig.isIgnoraPctMaxDescEmItemDescMaxProdCli() && itemPedido.getDescMaxProdCli() != null)) {
				continue;
			}
			String cdGrupoProduto = LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo() ? itemPedido.getProduto().cdGrupoDescProd : itemPedido.getProduto().cdGrupoProduto1;
			if (ValueUtil.isNotEmpty(cdGrupoProduto) && !itemPedido.isFlPrecoLiberadoSenha() && !itemPedido.pedido.isFlPrecoLiberadoSenha() && hasDescontoGrupoProduto(itemPedido)) {
				double pctMaxDesconto = getPctMaxDescontoPorGrupoEQuantidade(pedido, itemPedido);
				if (ValueUtil.round(itemPedido.vlPctDesconto) > ValueUtil.round(pctMaxDesconto)) {
					if (lanceException) {
						StringBuffer strBuffer = new StringBuffer();
						throw new ValidationException(strBuffer.append(DescontoGrupo.SIGLE_EXCEPTION).append(Messages.DESCONTOGRUPO_MSG_VALIDACAO).toString());
					} else {
						itemPedido.vlPctMaxDescGrupo = pctMaxDesconto;
						itemPedido.qtItemMesmoGrupo = getQtdItensAdicionadosGrupo(cdGrupoProduto, pedido.itemPedidoList);
						itensNaoConforme.addElement(itemPedido);
					}
				}
			}
		}
		return itensNaoConforme;
	}


	public double getPctMaxDescontoPorGrupoEQuantidade(Pedido pedido, final ItemPedido itemPedido) throws SQLException {
		Vector descontoGrupoList = findAllByGrupoProduto(itemPedido);
		int size = descontoGrupoList.size();
		if (size == 0) {
			return 0;
		}
		String cdGrupoProduto;
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			cdGrupoProduto = itemPedido.getProduto().cdGrupoDescProd;
		} else {
			cdGrupoProduto = itemPedido.getProduto().cdGrupoProduto1;
		}
		double qtItensGrupo;
		if (!itemPedido.usaDescontoCascata && !LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip) {
			qtItensGrupo = getQtdItensAdicionadosGrupo(cdGrupoProduto, pedido.itemPedidoList);
		} else {
			qtItensGrupo = getQtdItensAdicionadosGrupo(cdGrupoProduto, pedido.itemPedidoList, itemPedido);
		}
		DescontoGrupo descontoGrupo;
		double pctMaxDesconto = 0;
		for (int j = 0; j < size; j++) {
			descontoGrupo = (DescontoGrupo) descontoGrupoList.items[j];
			if (qtItensGrupo >= descontoGrupo.qtItem) {
				pctMaxDesconto = descontoGrupo.vlPctDesconto;
				break;
			}
		}
		return pctMaxDesconto;
	}

	public double getQtdItensAdicionadosGrupo(String cdGrupoProduto, Vector itemPedidoList) throws SQLException {
		int size = itemPedidoList.size();
		double qtItensGrupo = 0d;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoGrupo = (ItemPedido) itemPedidoList.items[i];
			if (ValueUtil.isNotEmpty(cdGrupoProduto) && cdGrupoProduto.equals(LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo() ? itemPedidoGrupo.getProduto().cdGrupoDescProd : itemPedidoGrupo.getProduto().cdGrupoProduto1)) {
				qtItensGrupo += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedidoGrupo, itemPedidoGrupo.getQtItemFisico());
			}
		}
		return qtItensGrupo;
	}

	public Vector findAllByGrupoProduto(final ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			if (ValueUtil.isEmpty(itemPedido.getProduto().cdGrupoDescProd)) {
				return new Vector(0);
			}
		} else if (itemPedido.getProduto() == null || ValueUtil.isEmpty(itemPedido.getProduto().cdGrupoProduto1)) {
			return new Vector(0);
		}
		DescontoGrupo descGrupoProdFilter = new DescontoGrupo();
		descGrupoProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		descGrupoProdFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			descGrupoProdFilter.cdGrupoDescProd = itemPedido.getProduto().cdGrupoDescProd;
			descGrupoProdFilter.cdGrupoProduto1 = "0";
		} else {
			descGrupoProdFilter.cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
			descGrupoProdFilter.cdGrupoDescProd = "0";
		}
		if (LavenderePdaConfig.usaDescontoQtdeGrupoPorTabelaPreco) {
			descGrupoProdFilter.cdTabelaPreco = itemPedido.cdTabelaPreco;
		} else {
			descGrupoProdFilter.cdTabelaPreco = "0";
		}
		return findAllByExample(descGrupoProdFilter);
	}

	public boolean hasDescontoGrupoProduto(final ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && ValueUtil.isNotEmpty(findAllByGrupoProduto(itemPedido));
	}

	public Vector calcDescQtdeGrupoUnidadeAlternativa(ItemPedido itemPedido) throws SQLException {
		return calcDescQtdeGrupoUnidadeAlternativa(findAllByGrupoProduto(itemPedido), itemPedido);
	}

	public Vector calcDescQtdeGrupoUnidadeAlternativa(Vector descontoQtdeGrupoList, ItemPedido itemPedido) throws SQLException {
 		Vector descQtdeGrupoListFinal = new Vector();
		if (descontoQtdeGrupoList != null) {
			ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
			if (LavenderePdaConfig.usaUnidadeAlternativa && produtoUnidade != null) {
				for (int i = 0; i < descontoQtdeGrupoList.size(); i++) {
					calcDescQtdeGrupoUnidadeAlternativa((DescontoGrupo)descontoQtdeGrupoList.items[i], produtoUnidade);
				}
				//Mostra apenas a faixa com maior percentual de desconto caso a conversão das faixas por unidade resultem na mesma quantidade
				int size = descontoQtdeGrupoList.size();
				for (int i = 0; i < size; i++) {
					DescontoGrupo descQtdeGrupo = (DescontoGrupo) descontoQtdeGrupoList.items[i];
					boolean inseridoVetorFinal = false;
					int sizeFinal = descQtdeGrupoListFinal.size();
					for (int j = 0; j < sizeFinal; j++) {
						DescontoGrupo descQtdeGrupoTemp = (DescontoGrupo) descQtdeGrupoListFinal.items[j];
						if (descQtdeGrupo.qtItem == descQtdeGrupoTemp.qtItem) {
							if (descQtdeGrupo.vlPctDesconto > descQtdeGrupoTemp.vlPctDesconto) {
								descQtdeGrupoListFinal.removeElement(descQtdeGrupoTemp);
							} else {
								inseridoVetorFinal = true;
							}
						}
					}
					if (!inseridoVetorFinal) {
						descQtdeGrupoListFinal.addElement(descQtdeGrupo);
					}
				}
			} else {
				descQtdeGrupoListFinal = descontoQtdeGrupoList;
			}
		}
		return descQtdeGrupoListFinal;
	}

	private void calcDescQtdeGrupoUnidadeAlternativa(DescontoGrupo descQtdeGrupo, ProdutoUnidade produtoUnidade) {
		double qtItem;
		double precisaoDecimal;
		if (produtoUnidade.isMultiplica()) {
			qtItem = descQtdeGrupo.qtItem / produtoUnidade.nuConversaoUnidade;
		} else {
			qtItem = descQtdeGrupo.qtItem * produtoUnidade.nuConversaoUnidade;
		}
		descQtdeGrupo.qtItem = ValueUtil.getIntegerValue(ValueUtil.round(qtItem));
		precisaoDecimal = ValueUtil.round(qtItem) - ValueUtil.getIntegerValue(ValueUtil.round(qtItem));
		if (ValueUtil.round(precisaoDecimal) > 0) {
			descQtdeGrupo.qtItem += 1;
		}
	}

	public double getVlDescVlBaseItemPedido(ItemPedido itemPedido, double vlPctDesconto) throws SQLException {
		if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar()) {
			itemPedido.vlPctDesconto = vlPctDesconto;
			ItemPedidoService.getInstance().calculaValorEmbalagemElementarComDescAcrescimo(itemPedido);
			return ItemPedidoService.getInstance().calculaVlItemByVlElementar(itemPedido, itemPedido.vlEmbalagemElementar);
		}
		double vlItemPedidoComDesconto = 0;
		vlItemPedidoComDesconto = itemPedido.vlBaseItemPedido * (1 - (vlPctDesconto / 100));
		vlItemPedidoComDesconto = ValueUtil.round(vlItemPedidoComDesconto);
		return vlItemPedidoComDesconto;
	}

	protected void calculaVlTotalItemComDesconto(ItemPedido itemPedido, boolean isRetirandoDesconto) {
		itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.vlPctDesconto / 100)));
		if (LavenderePdaConfig.usaDescontoExtra && !LavenderePdaConfig.isConfigDescontosEmCascata() && !isRetirandoDesconto) {
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDesconto2 / 100)));
		}
		itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
	}

	/**
	 * Aplica o desconto do grupo de produtos em cada item pertencente ao grupo.
	 * O desconto aplicado é referente a faixa atingida pela quantidade de itens do grupo, vendidos no pedido.
	 * Caso o item já possua um desconto inserido manualmente, o valor de desconto do grupo não é aplicado ao item.
	 * @throws SQLException 
	 */
	protected void aplicaDescQtdGrupoAuto(Pedido pedido) throws SQLException {
		aplicaDescQtdGrupoAuto(pedido, false);
	}
	
	protected void aplicaDescQtdGrupoAuto(Pedido pedido, boolean forcaDesconto) throws SQLException {
		if (!LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata) {
			Vector itemPedidoList = pedido.itemPedidoList;
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				double vlPctMaxDescGrupo = getPctMaxDescontoPorGrupoEQuantidade(pedido, itemPedido);
				if ((itemPedido.vlPctDesconto == 0 && vlPctMaxDescGrupo != 0) || forcaDesconto) {
					itemPedido.vlPctDesconto = vlPctMaxDescGrupo;
					calculaVlTotalItemComDesconto(itemPedido, false);
					itemPedido.flDescQtdGrupoAplicadoAuto = true;
					pedido.flDescQtdGrupoAplicadoAuto = true;
				}
			}
		}
	}

	protected boolean retiraDescQtdGrupoAuto(Pedido pedido) throws SQLException {
		Vector itemPedidoList = pedido.itemPedidoList;
		int size = itemPedidoList.size();
		boolean atualizouAlgumItem = false;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			double vlPctMaxDescGrupo = getPctMaxDescontoPorGrupoEQuantidade(pedido, itemPedido);
			double vlPctDesc = itemPedido.vlPctDesconto;
			//--
			if (itemPedido.vlPctDesconto != 0) {
				itemPedido.vlPctDesconto = 0;
				calculaVlTotalItemComDesconto(itemPedido, true);
				itemPedido.flDescQtdGrupoAplicadoAuto = false;
				pedido.flDescQtdGrupoAplicadoAuto = false;
			}
			//-- Remove a verba aplicada ao fechar o pedido
			VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
			//-- Devolve o desconto dado na mão e recalcula o valor total do item novamente
			if (vlPctDesc != 0 && vlPctMaxDescGrupo != vlPctDesc) {
				itemPedido.vlPctDesconto = vlPctDesc;
				calculaVlTotalItemComDesconto(itemPedido, false);
			}
			atualizouAlgumItem = true;
		}
		return atualizouAlgumItem;
	}

	public Vector getDescQtdGrupoAplicadoAuto(Pedido pedido) {
		Vector itensDescontoAplicado = new Vector();
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			if (itemPedido.flDescQtdGrupoAplicadoAuto) {
				itensDescontoAplicado.addElement(itemPedido);
			}
		}
		return itensDescontoAplicado;
	}
	
	public double getQtdItensAdicionadosGrupo(String cdGrupoProduto, Vector itemPedidoList, ItemPedido itemPedido) throws SQLException {
		int size = itemPedidoList.size();
		double qtItensGrupo = 0d;
		if (ValueUtil.isNotEmpty(cdGrupoProduto) && cdGrupoProduto.equals(LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo() ? itemPedido.getProduto().cdGrupoDescProd : itemPedido.getProduto().cdGrupoProduto1)) {
			qtItensGrupo += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico());
		}
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoGrupo = (ItemPedido) itemPedidoList.items[i];
			if (itemPedidoGrupo.equals(itemPedido)) {
				continue;
			}
			if (ValueUtil.isNotEmpty(cdGrupoProduto) && cdGrupoProduto.equals(LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo() ? itemPedidoGrupo.getProduto().cdGrupoDescProd : itemPedidoGrupo.getProduto().cdGrupoProduto1)) {
				qtItensGrupo += ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedidoGrupo, itemPedidoGrupo.getQtItemFisico());
			}
		}
		return qtItensGrupo;
	}
	
	public void aplicaDescontoDescGrupoProduto(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		double desconto = getPctMaxDescontoPorGrupoEQuantidade(pedido, itemPedido);
		itemPedido.vlPctDesconto = desconto;
	}

}
