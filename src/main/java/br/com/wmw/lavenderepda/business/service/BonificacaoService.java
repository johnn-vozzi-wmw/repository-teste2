package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

public class BonificacaoService {

    private static BonificacaoService instance;

    private BonificacaoService() {
        //--
    }

    public static BonificacaoService getInstance() {
        if (instance == null) {
            instance = new BonificacaoService();
        }
        return instance;
    }
    
    public void validaQtdeItemBonificacao(ItemPedido itemPedido) throws SQLException {
    	//qtItemFaturamento
		if (LavenderePdaConfig.usaConversaoUnidadesMedida && !LavenderePdaConfig.ocultaQtItemFaturamento && itemPedido.qtItemFaturamento == 0) {
			throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + (ValueUtil.isEmpty(itemPedido.getProduto().dsUnidadeFaturamento) ? Messages.SIGLA_UNIDADE : itemPedido.getProduto().dsUnidadeFaturamento));
		}
		//qtItemFisico
		if (itemPedido.getQtItemFisico() == 0) {
			if (LavenderePdaConfig.usaConversaoUnidadesMedida && !ValueUtil.isEmpty(itemPedido.getProduto().dsUnidadeFisica)) {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + itemPedido.getProduto().dsUnidadeFisica);
			} else {
				throw new ValidationException(FrameworkMessages.MSG_VALIDACAO_CAMPO_NAO_INFORMADO + Messages.ITEMPEDIDO_LABEL_QTITEMFISICO);
			}
		}
    }

    public void validateBonificacaoItem(Pedido pedido, ItemPedido itemPedido, boolean inInsertUpdate, boolean deleteItemBoni) throws SQLException {
    	if (LavenderePdaConfig.isPermiteBonificarProduto() && !LavenderePdaConfig.isPermiteBonificarProdutoSemLimitesItensNoPedido()) {
    		if (itemPedido.isItemBonificacao() && inInsertUpdate) {
    			if (pedido.vlTotalPedido == 0) {
    				throw new ValidationException(Messages.PEDIDO_MSG_BONIFICACAO_VLTOTAL_ZERADO);
				}
    			double pctVlTotalBonificacao = (((pedido.vlBonificacaoPedido + itemPedido.vlTotalItemPedido) - itemPedido.vlTotalItemPedidoOld) * 100) / pedido.vlTotalPedido;
    			if (pctVlTotalBonificacao < 0) pctVlTotalBonificacao = pctVlTotalBonificacao * -1;
    			double pctMaxPedidoBonificado = LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble();
    			if (pctVlTotalBonificacao > pctMaxPedidoBonificado) {
    				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_BONIFICACAO_EXTRAPOLADA, new String[] {StringUtil.getStringValueToInterface(pctVlTotalBonificacao), StringUtil.getStringValueToInterface(pctMaxPedidoBonificado)}));
				}
    		} else if (itemPedido.isItemVendaNormal()) {
    			double pctBonificacao = 0;
    			if (inInsertUpdate) {
    				double valorPedido = (pedido.vlTotalPedido + itemPedido.vlTotalItemPedido) - itemPedido.vlTotalItemPedidoOld;
    				if (valorPedido > 0) {
    					pctBonificacao = ((pedido.vlBonificacaoPedido) * 100) / valorPedido;
    				}
    			} else if (!deleteItemBoni){
    				if ((pedido.vlBonificacaoPedido > 0) && ((pedido.vlTotalPedido - itemPedido.vlTotalItemPedido) == 0) && (!LavenderePdaConfig.isPermiteBonificarProdutoSemLimitesItensNoPedido())) {
    					throw new ValidationException(Messages.PEDIDO_MSG_BONIFICACAO_VLTOTAL_ZERADO);
					}
    				double vlTot = pedido.vlTotalPedido - itemPedido.vlTotalItemPedido;
    				if (vlTot != 0) {
    					pctBonificacao = ((pedido.vlBonificacaoPedido) * 100) / vlTot;
    				}
    			}
    			double permiteBonificarProdutoPedido = 0;
				if (LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble() > 0) {
					permiteBonificarProdutoPedido = LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble();
    			}
    			if (pctBonificacao > permiteBonificarProdutoPedido) {
    				throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_BONIFICACAO_EXTRAPOLADA, new String[] {StringUtil.getStringValueToInterface(pctBonificacao), StringUtil.getStringValueToInterface(permiteBonificarProdutoPedido)}));
				}
    		}
    	}
    }

    public void validateBonificacaoPorGrupoBonificacao(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	validateBonificacaoPorGrupoBonificacao(pedido, itemPedido, false);
    }

    public void validateBonificacaoPorGrupoBonificacao(Pedido pedido, ItemPedido itemPedido, boolean onDeleteItem) throws SQLException {
    	if (itemPedido.isItemVendaNormal()) {
    		validateItemVenda(pedido, itemPedido, onDeleteItem);
    	} else if (itemPedido.isItemBonificacao() && !onDeleteItem) {
    		try {
    			validateItemBonificacao(pedido.itemPedidoList, itemPedido);
    		} catch (ValidationException ve) {
				throw new ValidationException(Messages.BONIFICACAO_MSG_BONIFICACAO_VALIDACAO + " \"" + ve.getMessage() + "\"");
			}
    	} else if (itemPedido.isItemBonificacao() && onDeleteItem) {
    		validateDeleteItemBonificacao(pedido, itemPedido);
    	}
    }

    private void validateItemBonificacao(Vector listItensPedido, ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.usaConfigBonificacaoItemPedido()) return;
    	int sizeTotal = listItensPedido.size();
    	ItemPedido[] itensPedido = new ItemPedido[sizeTotal];
    	for (int i = 0; i < sizeTotal; i++) {
    		itensPedido[i] = (ItemPedido)listItensPedido.items[i];
		}
    	ItemPedido.sortAttr = "NUSEQITEMPEDIDO";
    	SortUtil.qsortInt(itensPedido, 0, sizeTotal-1, true);
    	Vector itensValidos = new Vector(0);
    	for (int i = 0; i < itensPedido.length; i++) {
    		ItemPedido itemPedidoExample = itensPedido[i];
    		if (itemPedidoExample.isItemBonificacao()) {
    			if (itemPedido.equals(itemPedidoExample)) {
    				break;
    			} else {
    				itensValidos.removeAllElements();
    				continue;
    			}
    		}
    		itensValidos.addElement(itemPedidoExample);
		}
    	//Regra 3X1
    	int sizeItens = itensValidos.size();
    	boolean validouMultiplo = false;
    	double qtItensVendidosValidos = 0;
    	int qtItensPermitidosABonificar = 0;
    	if ((LavenderePdaConfig.qtItemAVenderParaPermitirBonificarProduto > 0) && (LavenderePdaConfig.qtPermitidaProdutoABonificarAposVenda > 0)) {
    		double vlTtItensVendidosValidos = 0;
    		double vlTtTtabelaItensVendidosValidos = 0;
    		double qtPesoItensVendidosValidos = 0;
    		double vlPctMaxBonificacao = itemPedido.getProduto().vlPctMaxBonificacao;
    		for (int i = 0; i < sizeItens; i++) {
    			ItemPedido itemPedidoExample = (ItemPedido)itensValidos.items[i];
    			if (itemPedidoExample.isItemVendaNormal()) {
    				if (ValueUtil.valueEquals(itemPedido.cdProduto, itemPedidoExample.getProduto().cdProdutoBonificacao)) {
    					qtItensVendidosValidos += itemPedidoExample.getQtItemFisico();
    					vlTtItensVendidosValidos += itemPedidoExample.vlTotalItemPedido;
    					vlTtTtabelaItensVendidosValidos += ( itemPedidoExample.vlBaseItemPedido * itemPedidoExample.getQtItemFisico());
    					qtPesoItensVendidosValidos += itemPedidoExample.getPesoItemPedido();
    					if (vlPctMaxBonificacao > itemPedidoExample.getProduto().vlPctMaxBonificacao) {
    						vlPctMaxBonificacao = itemPedidoExample.getProduto().vlPctMaxBonificacao;
    					}
    				}
    			}
    		}
    		if (qtItensVendidosValidos > 0) {
    			if ((ValueUtil.getIntegerValue(qtItensVendidosValidos) % LavenderePdaConfig.qtItemAVenderParaPermitirBonificarProduto) == 0) {
    				BigDecimal bd = ValueUtil.getBigDecimalValue(qtItensVendidosValidos);
    				int qtBlocosItensVendidos = bd.divide(new BigDecimal(LavenderePdaConfig.qtItemAVenderParaPermitirBonificarProduto), BigDecimal.ROUND_DOWN).intValue();
    				qtItensPermitidosABonificar = qtBlocosItensVendidos*LavenderePdaConfig.qtPermitidaProdutoABonificarAposVenda;
    				if (ValueUtil.round(itemPedido.getQtItemFisico()) <= ValueUtil.round(qtItensPermitidosABonificar)) {
    					double vlPctProporcionalidade = 100 - ((( vlTtItensVendidosValidos / (qtPesoItensVendidosValidos + itemPedido.getPesoItemPedido()) ) / ( vlTtTtabelaItensVendidosValidos / qtPesoItensVendidosValidos )) * 100);
    					if (ValueUtil.round(vlPctMaxBonificacao) >= ValueUtil.round(vlPctProporcionalidade)) {
    						return;
    					} else {
    						throw new ValidationException(MessageUtil.getMessage(Messages.BONIFICACAO_MSG_PCTDESCONTOMAX_VALIDACAO, new String[]{ StringUtil.getStringValueToInterface(vlPctProporcionalidade), StringUtil.getStringValueToInterface(vlPctMaxBonificacao)}));
    					}
    				}
    			} else {
    				validouMultiplo = true;
    			}
    		}
    	}
		//Regra do AutoBonifica (Verifica se o produto a bonificar está inserido no pedido e se ele é auto-bonificável)
		if (!(ValueUtil.VALOR_SIM.equals(itemPedido.getProduto().flAutoBonifica))) {
			for (int i = 0; i < sizeItens; i++) {
				ItemPedido itemPedidoExample = (ItemPedido)itensValidos.items[i];
				if (itemPedidoExample.isItemVendaNormal()) {
					if (ValueUtil.isNotEmpty(itemPedido.getProduto().cdGrupoBonificacao) && itemPedido.getProduto().cdGrupoBonificacao.equals(itemPedidoExample.getProduto().cdGrupoBonificacao)) {
						if (itemPedido.cdProduto.equals(itemPedidoExample.cdProduto)) {
							throw new ValidationException(Messages.BONIFICACAO_MSG_AUTOBONIFICAVEL_VALIDACAO);
						}
					}
				}

			}
		}
		//Regra Peso do Grupo do produto
		double vlTtItensVendidosValidos = 0;
		double vlTtTtabelaItensVendidosValidos = 0;
		double qtPesoItensVendidosValidos = 0;
		double vlPctMaxBonificacao = itemPedido.getProduto().vlPctMaxBonificacao;
		for (int i = 0; i < sizeItens; i++) {
			ItemPedido itemPedidoExample = (ItemPedido)itensValidos.items[i];
			if (ValueUtil.isNotEmpty(itemPedido.getProduto().cdGrupoBonificacao) && itemPedido.getProduto().cdGrupoBonificacao.equals(itemPedidoExample.getProduto().cdGrupoBonificacao)) {
				vlTtItensVendidosValidos += itemPedidoExample.vlTotalItemPedido;
				vlTtTtabelaItensVendidosValidos += ( itemPedidoExample.vlBaseItemPedido * itemPedidoExample.getQtItemFisico());
				qtPesoItensVendidosValidos += itemPedidoExample.getPesoItemPedido();
				if (vlPctMaxBonificacao > itemPedidoExample.getProduto().vlPctMaxBonificacao) {
					vlPctMaxBonificacao = itemPedidoExample.getProduto().vlPctMaxBonificacao;
				}
			}
		}
		if (qtPesoItensVendidosValidos > 0) {
			double vlPctProporcionalidade = 100 - ((( vlTtItensVendidosValidos / ( qtPesoItensVendidosValidos + itemPedido.getPesoItemPedido())) / ( vlTtTtabelaItensVendidosValidos / qtPesoItensVendidosValidos )) * 100);
			if (ValueUtil.round(vlPctMaxBonificacao) >= ValueUtil.round(vlPctProporcionalidade)) {
				return;
			} else {
				throw new ValidationException(MessageUtil.getMessage(Messages.BONIFICACAO_MSG_PCTDESCONTOMAX_VALIDACAO, new String[]{ StringUtil.getStringValueToInterface(vlPctProporcionalidade), StringUtil.getStringValueToInterface(vlPctMaxBonificacao)}));
			}
		}
		//-- Lança excessão de erro de acordo com a validação ocorrida
		boolean validacaoRegraNxM = sizeItens > 0;
		for (int i = 0; i < sizeItens; i++) {
			ItemPedido itemPedidoExample = (ItemPedido)itensValidos.items[i];
			if (!itemPedido.cdProduto.equals(itemPedidoExample.getProduto().cdProdutoBonificacao)) {
				validacaoRegraNxM = false;
			}
		}
		if (validacaoRegraNxM) {
			if (validouMultiplo) {
				throw new ValidationException(MessageUtil.getMessage(Messages.BONIFICACAO_MSG_ITEMBONIFICACAO_MULTIPLO_VALIDACAO,
						new String[]{ StringUtil.getStringValueToInterface(ValueUtil.getIntegerValue(qtItensVendidosValidos)), StringUtil.getStringValueToInterface(LavenderePdaConfig.qtItemAVenderParaPermitirBonificarProduto)}));
			} else {
				throw new ValidationException(MessageUtil.getMessage(Messages.BONIFICACAO_MSG_ITEMBONIFICACAO_NXM_VALIDACAO,
						new String[]{ StringUtil.getStringValueToInterface(ValueUtil.getIntegerValue(itemPedido.getQtItemFisico())), StringUtil.getStringValueToInterface(qtItensPermitidosABonificar),
							StringUtil.getStringValueToInterface(LavenderePdaConfig.qtItemAVenderParaPermitirBonificarProduto), StringUtil.getStringValueToInterface(LavenderePdaConfig.qtPermitidaProdutoABonificarAposVenda)}));
			}
		} else {
			throw new ValidationException(Messages.BONIFICACAO_MSG_ITEMBONIFICACAO_VALIDACAO);
		}
    }

    private void validateItemVenda(Pedido pedido, ItemPedido itemPedido, boolean onDeleteItem) throws SQLException {
		int sizeItens = pedido.itemPedidoList.size();
    	ItemPedido[] itensPedido = new ItemPedido[sizeItens];
    	for (int i = 0; i < sizeItens; i++) {
    		itensPedido[i] = (ItemPedido)pedido.itemPedidoList.items[i];
		}
    	ItemPedido.sortAttr = "NUSEQITEMPEDIDO";
    	SortUtil.qsortInt(itensPedido, 0, sizeItens-1, true);
    	ItemPedido itemPedidoOficial = null;
    	try {
			for (int i = 0; i < sizeItens; i++) {
				ItemPedido itemPedidoExample = itensPedido[i];
				if ((itemPedidoOficial != null) && itemPedidoExample.isItemBonificacao()) {
					try {
						validateItemBonificacao(pedido.itemPedidoList, itemPedidoExample);
					} catch (ValidationException ve) {
						if (onDeleteItem) {
							throw new ValidationException(Messages.BONIFICACAO_MSG_ITEMEXCLUSAO_VALIDACAO + "\"" + ve.getMessage() + "\"");
						} else {
							throw new ValidationException(Messages.BONIFICACAO_MSG_ITEMALTERACAO_VALIDACAO + "\"" + ve.getMessage() + "\"");
						}
					}
					break;
				}
				if (itemPedidoExample.equals(itemPedido)) {
					pedido.itemPedidoList.removeElement(itemPedidoExample);
					if (!onDeleteItem) {
						itemPedido.nuSeqItemPedido = itemPedidoExample.nuSeqItemPedido;
						pedido.itemPedidoList.addElement(itemPedido);
					}
					itemPedidoOficial = itemPedidoExample;
				}
			}
    	} finally {
    		if (itemPedidoOficial != null) {
				if (!onDeleteItem) {
					pedido.itemPedidoList.removeElement(itemPedido);
				}
				pedido.itemPedidoList.addElement(itemPedidoOficial);
    		}
		}
    }

    private void validateDeleteItemBonificacao(Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	int sizeItens = pedido.itemPedidoList.size();
    	ItemPedido[] itensPedido = new ItemPedido[sizeItens];
    	for (int i = 0; i < sizeItens; i++) {
    		itensPedido[i] = (ItemPedido)pedido.itemPedidoList.items[i];
    	}
    	ItemPedido.sortAttr = "NUSEQITEMPEDIDO";
    	SortUtil.qsortInt(itensPedido, 0, sizeItens-1, true);
    	ItemPedido itemPedidoOficial = null;
    	try {
	    	for (int i = 0; i < sizeItens; i++) {
	    		ItemPedido itemPedidoExample = itensPedido[i];
	    		if ((itemPedidoOficial != null) && itemPedidoExample.isItemBonificacao()) {
    				try {
    					validateItemBonificacao(pedido.itemPedidoList, itemPedidoExample);
		    		} catch (ValidationException ve) {
						throw new ValidationException(Messages.BONIFICACAO_MSG_ITEMALTERACAO_VALIDACAO + "\"" + ve.getMessage() + "\"");
					}
	    			break;
	    		}
	    		if (itemPedidoExample.equals(itemPedido)) {
	    			pedido.itemPedidoList.removeElement(itemPedidoExample);
	    			itemPedidoOficial = itemPedidoExample;
	    		}
	    	}
    	} finally {
    		if (itemPedidoOficial != null) {
    			pedido.itemPedidoList.addElement(itemPedidoOficial);
    		}
    	}
    }

}