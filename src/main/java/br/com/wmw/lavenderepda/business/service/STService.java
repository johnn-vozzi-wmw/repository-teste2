package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.business.domain.TributacaoVlBase;

public class STService {

	private static STService instance;

    private STService() {
        //--
    }

    public static STService getInstance() {
        if (instance == null) {
            instance = new STService();
        }
        return instance;
    }

    public void aplicaSTItemPedido(ItemPedido itemPedido, Tributacao tributacao, TributacaoVlBase tributacaoVlBase) throws SQLException {
    	if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
    		aplicaSTItemPedidoPersonalizado(itemPedido, LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? itemPedido.tributacaoConfig : itemPedido.getTributacaoConfigItem(), tributacao, tributacaoVlBase, getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacaoVlBase));
    	} else {
    		aplicaSTItemPedidoNormal(itemPedido, tributacao, tributacaoVlBase);
    	}
    }

	public double getVlBaseIcmsRetidoCalcRetido(ItemPedido itemPedido, TributacaoVlBase tributacaoVlBase) throws SQLException {
		if (!LavenderePdaConfig.usaUnidadeAlternativa) {
			return tributacaoVlBase.vlBaseIcmsRetidoCalcRetido;
		}
		return getVlBaseByVlUnidadeAlternativa(itemPedido, tributacaoVlBase.vlBaseIcmsRetidoCalcRetido);
	}

    protected void aplicaSTItemPedidoPersonalizado(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao, TributacaoVlBase tributacaoVlBase, double vlBaseIcmsRetidoCalcRetido) throws SQLException {
		boolean tributacaoConfigurada = tributacaoConfig != null;
		tributacaoConfigurada &= tributacao != null;

    	if (tributacaoConfigurada && tributacaoConfig.isCalculaSt()) {
			calculaSt(itemPedido, tributacaoConfig, tributacao, vlBaseIcmsRetidoCalcRetido);
		} else if (tributacaoConfigurada && tributacaoConfig.isCalculaSt2()) {
			calculaSt2(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, vlBaseIcmsRetidoCalcRetido);
		} else {
			itemPedido.vlTotalBaseStItem = 0;
			itemPedido.vlTotalStItem = 0;
			itemPedido.vlPctMargemAgregada = 0;
		}

		if (LavenderePdaConfig.mostraDescAcessoriaCapaPedido) {
			itemPedido.vlDespesaAcessoria = tributacao.vlDespesaAcessoria;
		}
    }

	private void calculaSt(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao, double vlBaseIcmsRetidoCalcRetido) throws SQLException {
    		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos) && tributacao.vlIcmsRetido != 0) {
    			final double vlIcmsRetido = tributacao.vlIcmsRetido;
			itemPedido.vlTotalStItem = vlIcmsRetido * itemPedido.getQtItemFisico();
			itemPedido.vlTotalBaseStItem = vlBaseIcmsRetidoCalcRetido * itemPedido.getQtItemFisico();
    			return;
    		}
    		if (tributacao.vlPctIcmsRetido != 0) {
    			vlBaseIcmsRetidoCalcRetido = getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacaoConfig, tributacao, vlBaseIcmsRetidoCalcRetido);
    			
    			itemPedido.vlPctMargemAgregada = tributacao.vlPctMargemAgregada;
			itemPedido.vlTotalStItem = ValueUtil.round(vlBaseIcmsRetidoCalcRetido * tributacao.vlPctIcmsRetido / 100, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) - itemPedido.vlTotalIcmsItem;
    			if (tributacao.vlPctMinSt > 0) {
    				double vlMinSt = ValueUtil.round(vlBaseIcmsRetidoCalcRetido * tributacao.vlPctMinSt / 100, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
    				if (itemPedido.vlTotalStItem < vlMinSt) {
    					itemPedido.vlTotalStItem = vlMinSt;
    				}
    			}
    		}
    		}

	private void calculaSt2(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao, TributacaoVlBase tributacaoVlBase, double vlBaseIcmsRetidoCalcRetido) throws SQLException {
		if (tributacao.vlPctIcmsRetido != 0) {
			vlBaseIcmsRetidoCalcRetido = getVlBaseIcmsRetidoCalcRetidoSt2(itemPedido, tributacaoConfig, tributacao, tributacaoVlBase, vlBaseIcmsRetidoCalcRetido);
			itemPedido.vlPctMargemAgregada = tributacao.vlPctMargemAgregada;
			itemPedido.vlTotalStItem = ValueUtil.round(vlBaseIcmsRetidoCalcRetido, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
			if (tributacao.vlPctMinSt > 0) {
				double vlMinSt = ValueUtil.round(vlBaseIcmsRetidoCalcRetido * tributacao.vlPctMinSt / 100, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
				if (itemPedido.vlTotalStItem < vlMinSt) {
					itemPedido.vlTotalStItem = vlMinSt;
				}
			}
		}
	}

	public double getVlBaseIcmsRetidoCalcRetidoSt2(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao, TributacaoVlBase tributacaoVlBase, double vlBaseIcmsRetidoCalcRetido) throws SQLException {
		boolean verificaValorItem = tributacaoConfig.isVerificaValorItem() && ValueUtil.round(itemPedido.vlItemPedido) >= ValueUtil.round(vlBaseIcmsRetidoCalcRetido);
		boolean aplicaIndiceCondPagtoVlBaseST = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST();
		double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
		double vlTotalItemPedidoComSeguroST = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseST, vlIndiceCondPagto);

		if ((tributacaoVlBase.isExisteVlBaseIcmsRetidoCalcRetido() || verificaValorItem)
				&& itemPedido.getQtItemFisico() > 0 && itemPedido.vlItemPedido + (itemPedido.vlTotalIpiItem / itemPedido.getQtItemFisico()) <= (vlBaseIcmsRetidoCalcRetido * (tributacao.vlPctDiferenca / 100))) {

			vlBaseIcmsRetidoCalcRetido = vlBaseIcmsRetidoCalcRetido == 0 ? getVlTotalItemComSeguroEFrete(itemPedido, tributacaoConfig, vlTotalItemPedidoComSeguroST) : verificaValorItem ? vlTotalItemPedidoComSeguroST : vlBaseIcmsRetidoCalcRetido;
			vlBaseIcmsRetidoCalcRetido -= tributacao.vlPctReducaoBaseIcmsRetido / 100;
			vlBaseIcmsRetidoCalcRetido *= itemPedido.getQtItemFisico();
			vlBaseIcmsRetidoCalcRetido *= tributacao.vlPctIcmsRetido / 100;
			vlBaseIcmsRetidoCalcRetido -= itemPedido.vlTotalIcmsItem;

    	} else {

			vlBaseIcmsRetidoCalcRetido = getVlTotalItemComSeguroEFrete(itemPedido, tributacaoConfig, vlTotalItemPedidoComSeguroST);
			vlBaseIcmsRetidoCalcRetido += itemPedido.vlTotalIpiItem;
			vlBaseIcmsRetidoCalcRetido -= tributacao.vlPctRepasseIcms / 100;
			vlBaseIcmsRetidoCalcRetido += (vlBaseIcmsRetidoCalcRetido * (tributacao.vlPctMargemAgregada / 100));
			vlBaseIcmsRetidoCalcRetido -= tributacao.vlPctReducaoBaseIcmsRetido / 100;
			vlBaseIcmsRetidoCalcRetido *= tributacao.vlPctIcmsRetido / 100;
			vlBaseIcmsRetidoCalcRetido -= itemPedido.vlTotalIcmsItem;
    	}

		return vlBaseIcmsRetidoCalcRetido;
    }
    
    public double getVlBaseIcmsRetidoCalcRetido(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao, double vlBaseIcmsRetidoCalcRetido) throws SQLException {
		if (tributacaoConfig == null) {
			return vlBaseIcmsRetidoCalcRetido;
		}
		boolean verificaValorItem = tributacaoConfig.isVerificaValorItem() && ValueUtil.round(itemPedido.vlItemPedido) >= ValueUtil.round(vlBaseIcmsRetidoCalcRetido);
		double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
		boolean aplicaIndiceCondPagtoVlBaseST = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseST();
		if (vlBaseIcmsRetidoCalcRetido == 0 || verificaValorItem) {
			double vlTotalItemPedidoComSeguroST = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseST, vlIndiceCondPagto);
			vlBaseIcmsRetidoCalcRetido =  vlBaseIcmsRetidoCalcRetido == 0 ? getVlTotalItemComSeguroEFrete(itemPedido, tributacaoConfig, vlTotalItemPedidoComSeguroST) : verificaValorItem ? vlTotalItemPedidoComSeguroST : vlBaseIcmsRetidoCalcRetido;
			vlBaseIcmsRetidoCalcRetido = tributacaoConfig.isAplicaValorIpiNaBaseSt() ? (vlBaseIcmsRetidoCalcRetido + itemPedido.vlTotalIpiItem) : vlBaseIcmsRetidoCalcRetido;
			vlBaseIcmsRetidoCalcRetido -= tributacao.isBaseIcmsRetidoComRepasse() ? calculaRepasseIcms(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctRepasseIcms) : 0; 
			vlBaseIcmsRetidoCalcRetido += ValueUtil.round(calculaMargemAgregada(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctMargemAgregada), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
			vlBaseIcmsRetidoCalcRetido -= calculaReducaoBaseIcms(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctReducaoBaseIcmsRetido);
		} else {
			double vlReducaoSt = 0;
			if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flAplicaReducaoBaseIcmsRetido)) {
				vlReducaoSt = (tributacao.vlPctReducaoBaseIcmsRetido / 100) * vlBaseIcmsRetidoCalcRetido;
			}
			if (aplicaIndiceCondPagtoVlBaseST) {
				vlBaseIcmsRetidoCalcRetido = vlIndiceCondPagto != 0 ? vlBaseIcmsRetidoCalcRetido * vlIndiceCondPagto : vlBaseIcmsRetidoCalcRetido;
			}
			vlBaseIcmsRetidoCalcRetido = (vlBaseIcmsRetidoCalcRetido - vlReducaoSt) * itemPedido.getQtItemFisico();
		}
		return vlBaseIcmsRetidoCalcRetido;
	}

	private double getVlTotalItemComSeguroEFrete(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, double vlTotalItemPedidoComSeguroST) {
		return tributacaoConfig.isAplicaValorFreteNaBaseSt() ? (vlTotalItemPedidoComSeguroST + itemPedido.getVlTotalItemPedidoFrete()) : vlTotalItemPedidoComSeguroST;
	}
    
    protected void aplicaSTItemPedidoNormal(ItemPedido itemPedido, Tributacao tributacao, TributacaoVlBase tributacaoVlBase) throws SQLException {
    	if (tributacao != null && (tributacao.cdTipoRecolhimento == Tributacao.TRIBUTACAO_RETIDO)) {
	        boolean existePrecoBaseIcmsCalcRetido = tributacaoVlBase.isExistePrecoBaseIcmsCalcRetido();
	        boolean existeVlBaseIcmsRetidoCalcRetido = tributacaoVlBase.isExisteVlBaseIcmsRetidoCalcRetido();
	        double vlBaseIcmsCalcRetido = getVlBaseIcmsCalcRetido(itemPedido, tributacaoVlBase, existePrecoBaseIcmsCalcRetido);
	        double vlBaseIcmsRetidoCalcRetido = getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, tributacaoVlBase, existeVlBaseIcmsRetidoCalcRetido);
	        itemPedido.vlDespesaAcessoria = tributacao.vlDespesaAcessoria;
	        itemPedido.vlIcms = calculaIcmsNormal(vlBaseIcmsCalcRetido, tributacao, existePrecoBaseIcmsCalcRetido, itemPedido);
	        if (LavenderePdaConfig.usaCalculoReversoNaST && (itemPedido.isEditandoValorItemST() || itemPedido.isEditandoDescontoST())) {
	        	calculaIcmsRetidoReverso(vlBaseIcmsRetidoCalcRetido, tributacao, itemPedido, tributacaoVlBase);
	        } else {
				itemPedido.vlSt = calculaIcmsRetido(vlBaseIcmsCalcRetido, vlBaseIcmsRetidoCalcRetido, tributacao, itemPedido, tributacaoVlBase);
			}
	        //--
	        if (LavenderePdaConfig.usaCalculoReversoNaST && itemPedido.vlItemPedidoStBase == 0.0) {
	        	itemPedido.vlItemPedidoStBase = itemPedido.vlItemPedido + itemPedido.vlSt;
			}
    	}
    }

	public double getVlBaseIcmsCalcRetido(ItemPedido itemPedido, TributacaoVlBase tributacaoVlBase, boolean existePrecoBaseIcmsCalcRetido) throws SQLException {
		double vlBaseIcmsCalcRetido =  LavenderePdaConfig.usaCalculoReversoNaST && itemPedido.isEditandoValorItemST()  ? itemPedido.vlItemPedidoStReverso : itemPedido.vlItemPedido;
    	if (tributacaoVlBase != null) {
    		if (existePrecoBaseIcmsCalcRetido) {
    			vlBaseIcmsCalcRetido = tributacaoVlBase.vlBaseIcmsCalcRetido;
    		}
    		//--
    		if (LavenderePdaConfig.usaUnidadeAlternativa && (!itemPedido.isCdUnidadeIgualCdUnidadeProduto() && existePrecoBaseIcmsCalcRetido)) {
    			vlBaseIcmsCalcRetido = getVlBaseByVlUnidadeAlternativa(itemPedido, vlBaseIcmsCalcRetido);
    		}	
    	} 
    	return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlBaseIcmsCalcRetido, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlBaseIcmsCalcRetido;
    }

    public double getVlBaseIcmsRetidoCalcRetido(ItemPedido itemPedido, Tributacao tributacao, TributacaoVlBase tributacaoVlBase, boolean existeVlBaseIcmsRetidoCalcRetido) throws SQLException {
    	double vlBaseIcmsRetidoCalcRetido = itemPedido.vlBaseItemPedido;
    	if (tributacaoVlBase != null) {
			if (existeVlBaseIcmsRetidoCalcRetido) {
				vlBaseIcmsRetidoCalcRetido = tributacaoVlBase.getVlBaseIcmsRetidoCalcRetido(tributacao.vlPctDiferenca, itemPedido.vlItemPedido, tributacao.vlPctMargemAgregada);
			}
			if (LavenderePdaConfig.usaUnidadeAlternativa && !itemPedido.isCdUnidadeIgualCdUnidadeProduto() && existeVlBaseIcmsRetidoCalcRetido) {	
				vlBaseIcmsRetidoCalcRetido = getVlBaseByVlUnidadeAlternativa(itemPedido, vlBaseIcmsRetidoCalcRetido);
    		}
    	}
    	vlBaseIcmsRetidoCalcRetido = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlBaseIcmsRetidoCalcRetido, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlBaseIcmsRetidoCalcRetido;
		return calculaVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido);
    }

	protected double calculaVlBaseIcmsRetidoCalcRetido(ItemPedido itemPedido, Tributacao tributacao, double vlBaseIcmsRetidoCalcRetido) {
		double vlPctDesconto = LavenderePdaConfig.usaCalculoReversoNaST && !itemPedido.isEditandoValorItemST() ? itemPedido.vlPctDescontoStReverso : itemPedido.vlPctDesconto;
		if (itemPedido.isRecebeuDescontoPorQuantidade()) {
			vlPctDesconto = itemPedido.vlPctFaixaDescQtd;
		}
		if (tributacao.isBaseIcmsRetidoComDesconto() || (tributacao.isBaseIcmsRetidoComDescontoAcrescimo() && vlPctDesconto != 0)) {
			return ValueUtil.round(vlBaseIcmsRetidoCalcRetido * (1 - (vlPctDesconto / 100)));
		}
		if (tributacao.isBaseIcmsRetidoComAcrescimo() || (tributacao.isBaseIcmsRetidoComDescontoAcrescimo() && itemPedido.vlPctAcrescimo != 0)) {
			return ValueUtil.round(vlBaseIcmsRetidoCalcRetido * (1 + (itemPedido.vlPctAcrescimo / 100)));
		}
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlBaseIcmsRetidoCalcRetido, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlBaseIcmsRetidoCalcRetido;
	}

	public double calculaIpi(Tributacao tributacao, ItemPedido itemPedido, double vlBaseCalculo) throws SQLException {
		double vlBaseCalculoIpi = vlBaseCalculo;
		boolean descontoAplicadoBase = tributacao.isBaseIcmsRetidoComDesconto() || tributacao.isBaseIcmsRetidoComDescontoAcrescimo();
		boolean acrescimoAplicadoBase = tributacao.isBaseIcmsRetidoComAcrescimo() || tributacao.isBaseIcmsRetidoComDescontoAcrescimo();

		if (!descontoAplicadoBase && (tributacao.isBaseIpiComDesconto() || (tributacao.isBaseIpiComDescontoAcrescimo() && itemPedido.vlPctDesconto != 0))) {
			vlBaseCalculoIpi = ValueUtil.round(vlBaseCalculo * (1 - (itemPedido.vlPctDesconto / 100)));
		} else if (!acrescimoAplicadoBase && (tributacao.isBaseIpiComAcrescimo() || (tributacao.isBaseIpiComDescontoAcrescimo() && itemPedido.vlPctAcrescimo != 0))) {
			vlBaseCalculoIpi = ValueUtil.round(vlBaseCalculo * (1 + (itemPedido.vlPctAcrescimo / 100)));
		}

		double vlIpi = tributacao.isUsaIpiCalculado() ? itemPedido.vlIpiItem : itemPedido.getProduto().vlIpi;
		vlIpi = vlIpi != 0 ? vlIpi : vlBaseCalculoIpi * itemPedido.getProduto().vlPctIpi / 100;
		vlIpi = (vlIpi < itemPedido.getProduto().vlMinIpi) ? itemPedido.getProduto().vlMinIpi : vlIpi;
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlIpi, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlIpi;
    }

	public double calculaRepasseIcms(double vlBaseCalculo, double vlPctRepasseIcms) {
		double vlRepasseIcms = vlBaseCalculo * vlPctRepasseIcms / 100;
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlRepasseIcms, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlRepasseIcms;
    }

	public double calculaReducaoBaseIcms(double vlBaseCalculo, double vlPctReducaoBaseIcms) {
		double vlReducaoBaseIcms = vlBaseCalculo * vlPctReducaoBaseIcms / 100;
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlReducaoBaseIcms, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlReducaoBaseIcms;
    }

	public double calculaAliquota(double vlBaseCalculo, double aliquota) {
		double vlAliquotaIcms = vlBaseCalculo * aliquota / 100;
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlAliquotaIcms, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlAliquotaIcms;
	}

	public double calculaReducaoIcms(double vlBaseCalculo, double vlPctReducaoIcms) {
		double vlReducaoIcms = vlBaseCalculo * vlPctReducaoIcms / 100;
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlReducaoIcms, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlReducaoIcms;
	}

	public double calculaMargemAgregada(double vlBaseCalculo, double vlPctMargemAgregada) {
		double vlMargemAgregada = vlBaseCalculo * vlPctMargemAgregada / 100;
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlMargemAgregada, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlMargemAgregada;
	}

    public double calculaIcmsNormal(double vlBaseCalculo, Tributacao tributacao, boolean existePrecoBaseIcmsCalcRetido, ItemPedido itemPedido) throws SQLException {
    	double aliquota = tributacao.vlPctIcms;
    	double vlIcmsNormal = vlBaseCalculo;
    	if (!existePrecoBaseIcmsCalcRetido) {
    		if (tributacao.isAplicaFrete() && !LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
        		double vlFrete = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(itemPedido.vlItemPedidoFrete, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : itemPedido.vlItemPedidoFrete;
    			vlIcmsNormal += vlFrete;
    		}
    		double vlDespesaAcessoria = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(tributacao.vlDespesaAcessoria, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : tributacao.vlDespesaAcessoria;
    		vlIcmsNormal += vlDespesaAcessoria;
    		vlIcmsNormal += tributacao.isAplicaIpiNoIcms() ? calculaIpi(tributacao, itemPedido, vlIcmsNormal) : 0;
    		vlIcmsNormal -= calculaRepasseIcms(vlIcmsNormal, tributacao.vlPctRepasseIcms); 
    		vlIcmsNormal -= calculaReducaoBaseIcms(vlIcmsNormal, tributacao.vlPctReducaoBaseIcms);
    	}
    	vlIcmsNormal = calculaAliquota(vlIcmsNormal, aliquota);
    	if (!existePrecoBaseIcmsCalcRetido) {
    		vlIcmsNormal -= calculaReducaoIcms(vlIcmsNormal, tributacao.vlPctReducaoIcms);
    	}
    	return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? vlIcmsNormal : ValueUtil.round(vlIcmsNormal, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
    }

    protected double calculaIcmsPersonalizado(ItemPedido itemPedido, TributacaoConfig tributacaoConfig, Tributacao tributacao, double vlBaseIcms) throws SQLException {
    	if (tributacaoConfig != null && tributacaoConfig.isCalculaIcms()) {
    		if (ValueUtil.VALOR_SIM.equals(tributacaoConfig.flUtilizaValorFixoImpostos)) {
    			final double vlIcms = tributacao != null ? tributacao.vlIcms : 0;
				itemPedido.vlTotalIcmsItem = vlIcms * itemPedido.getQtItemFisico();
    			itemPedido.vlTotalBaseIcmsItem = vlBaseIcms * itemPedido.getQtItemFisico();;
    			return itemPedido.vlTotalBaseIcmsItem;
    		}
    		double vlPctIcms = getPctIcmsParaCalculoPersonalizado(itemPedido, tributacao);
    		double vlIndiceCondPagto = itemPedido.pedido.getCondicaoPagamento().vlIndiceFinanceiro;
    		boolean aplicaIndiceCondPagtoVlBaseICMS = LavenderePdaConfig.isAplicaIndiceCondPagtoVlBaseICMS();
    		if (vlBaseIcms != 0) {
    			if (aplicaIndiceCondPagtoVlBaseICMS) {
    				vlIndiceCondPagto = vlIndiceCondPagto == 0 ? 1 : vlIndiceCondPagto;
    				vlBaseIcms = vlBaseIcms * vlIndiceCondPagto;
    			}
    			vlBaseIcms = vlBaseIcms * itemPedido.getQtItemFisico();
				if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) itemPedido.vlTotalBaseIcmsItem = vlBaseIcms;
    			itemPedido.vlTotalIcmsItem = ValueUtil.round(vlBaseIcms * vlPctIcms / 100, LavenderePdaConfig.nuCasasDecimaisPisCofinsIcms);
    		} else {
				double vlTotalItemPedidoComSeguroICMS = itemPedido.getVlTotalItemPedidoComSeguro(aplicaIndiceCondPagtoVlBaseICMS, vlIndiceCondPagto);
    			vlBaseIcms = tributacaoConfig.isAplicaValorFreteNaBaseIcms() ? vlTotalItemPedidoComSeguroICMS + itemPedido.getVlTotalItemPedidoFrete() : vlTotalItemPedidoComSeguroICMS;
    			vlBaseIcms = tributacaoConfig.isAplicaValorIpiNaBaseIcms() ? (vlBaseIcms + itemPedido.vlTotalIpiItem) : vlBaseIcms;
    			if (tributacao != null) {
    				vlBaseIcms -= tributacao.isBaseIcmsRetidoComRepasse() ? calculaRepasseIcms(vlBaseIcms, tributacao.vlPctRepasseIcms) : 0;
    				vlBaseIcms -= calculaReducaoBaseIcms(vlBaseIcms, tributacao.vlPctReducaoBaseIcms);
    			}
    			if (tributacaoConfig.isCalculaEDebitaPisCofins() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
    				vlBaseIcms -= itemPedido.vlTotalPisItem + itemPedido.vlTotalCofinsItem;
    			}
    			itemPedido.vlBaseIcmsCalcFecop = vlBaseIcms;
    			vlBaseIcms = ValueUtil.round(vlBaseIcms * vlPctIcms / 100, LavenderePdaConfig.nuCasasDecimaisPisCofinsIcms);
    			itemPedido.vlTotalIcmsItem = ValueUtil.round(vlBaseIcms - calculaReducaoIcms(vlBaseIcms, tributacao != null ? tributacao.vlPctReducaoIcms : 0));
    		}
    	}else {
    		itemPedido.vlTotalIcmsItem = 0;
    		itemPedido.vlTotalBaseIcmsItem = 0;
    	}
    	return itemPedido.vlTotalIcmsItem;
	}

	public double getPctIcmsParaCalculoPersonalizado(ItemPedido itemPedido, Tributacao tributacao) throws SQLException {
		double vlPctIcms = tributacao != null ? tributacao.vlPctIcms : 0;
		if (vlPctIcms == 0) {
			vlPctIcms = Cliente.NU_INSCRICAO_ESTADUAL_ISENTO.equals(itemPedido.pedido.getCliente().nuInscricaoEstadual) ? itemPedido.getProduto().vlPctIcms : itemPedido.pedido.getCliente().vlPctIcms;
		}
		return vlPctIcms;
	}

	private void calculaIcmsRetidoReverso(double vlBaseIcmsRetidoCalcRetido, Tributacao tributacao, ItemPedido itemPedido, TributacaoVlBase tributacaoVlBase) throws SQLException {
		double vlIndiceStReverso = 0.0;
		double vlPctIcms = tributacao.vlPctIcms / 100;
		double vlPctIcmsRetido =  tributacao.vlPctIcmsRetido / 100;
		double vlPctReducaoBaseIcmsRetido = tributacao.vlPctReducaoBaseIcmsRetido / 100;
		double vlPctReducaoBaseIcms = tributacao.vlPctReducaoBaseIcms / 100;
		double vlPctMargemAgregada = tributacao.vlPctMargemAgregada / 100;
		boolean isBaseIcmsRetidoComDesconto = tributacao.isBaseIcmsRetidoComDesconto() || tributacao.isBaseIcmsRetidoComDescontoAcrescimo();
		boolean isCalculouVlStItemComDesconto = false;
		itemPedido.vlPctMargemAgregada = tributacao.vlPctMargemAgregada;
		if (tributacao.isFlMedicamento()) {
						calculaVlStReversoItemComDesconto(tributacao, itemPedido, tributacaoVlBase, itemPedido.vlItemPedidoStReverso);
						isCalculouVlStItemComDesconto = true;
		} else if (tributacaoVlBase.vlBaseIcmsRetidoCalcRetido == 0.0) {
			if (isBaseIcmsRetidoComDesconto) {
				vlIndiceStReverso = 1 - itemPedido.vlItemPedidoStReverso / (1 + (1 + vlPctMargemAgregada) * aplicaReducaoBase(vlPctIcmsRetido, vlPctReducaoBaseIcmsRetido) - aplicaReducaoBase(vlPctIcms, vlPctReducaoBaseIcms)) / itemPedido.vlBaseItemPedido;
					} else {
						vlIndiceStReverso = -(vlBaseIcmsRetidoCalcRetido - itemPedido.vlItemPedidoStReverso / vlPctIcmsRetido) / (1 - 1 / vlPctIcmsRetido) / itemPedido.vlBaseItemPedido + 1;
					}
		} else if (tributacaoVlBase.vlBaseIcmsRetidoCalcRetido > 0.0) {
				vlIndiceStReverso = -((aplicaReducaoBase(vlBaseIcmsRetidoCalcRetido, vlPctReducaoBaseIcmsRetido) - itemPedido.vlItemPedidoStReverso / vlPctIcmsRetido) / (1 - 1 / vlPctIcms - vlPctReducaoBaseIcms)) / itemPedido.vlBaseItemPedido + 1;
			}
		if (!isCalculouVlStItemComDesconto) {
			itemPedido.vlSt = calculaVlStReverso(itemPedido, vlIndiceStReverso);
			itemPedido.vlSt = itemPedido.vlSt < 0 ? 0 : itemPedido.vlSt;
			itemPedido.vlItemPedido = itemPedido.vlItemPedidoStReverso - itemPedido.vlSt;
		}
	}

	private double getVlPctDiferenca(double vlPctDiferenca) {
		return vlPctDiferenca > 0 ? vlPctDiferenca : 0.9d;
	}

	private void calculaVlStReversoItemComDesconto(Tributacao tributacao, ItemPedido itemPedido, TributacaoVlBase tributacaoVlBase, double vlItemPedidoStReverso) throws SQLException {
		double vlItemPedidoStReversoOld = itemPedido.vlItemPedidoStReverso;
		double vlItemPedidoStBase = itemPedido.vlItemPedidoStBase != 0 ? itemPedido.vlItemPedidoStBase : itemPedido.vlItemPedido + itemPedido.vlSt;
		double vlBaseCalculo = ValueUtil.round(itemPedido.vlBaseItemPedido * vlItemPedidoStReverso / vlItemPedidoStBase);
		double vlPctDesconto = ValueUtil.round(ItemPedidoService.getInstance().calculaVlPctDesconto(itemPedido.vlBaseItemPedido, vlBaseCalculo));
		itemPedido.vlItemPedido = vlBaseCalculo;
		itemPedido.vlPctDesconto = itemPedido.vlPctDescontoStReverso = vlPctDesconto;
		itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
		aplicaSTItemPedidoNormal(itemPedido, tributacao, tributacaoVlBase);
		boolean positivo = false;
		boolean negativo = false;
		int i = 0;
		double iterationFactor = Math.pow(10, LavenderePdaConfig.nuCasasDecimais);
		if (iterationFactor > 1000) {
			iterationFactor = 1000;
		}
		double decimalIncrement = 1d / (iterationFactor * 10);
		for (; i < iterationFactor; i++) {
			double newVlItemPedidoStReverso = ValueUtil.round(itemPedido.vlItemPedido + itemPedido.vlSt, LavenderePdaConfig.nuCasasDecimais);
			if (newVlItemPedidoStReverso == vlItemPedidoStReversoOld) {
				break;
			} else if (newVlItemPedidoStReverso < vlItemPedidoStReversoOld) {
				if (!negativo) {
					itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * vlItemPedidoStReverso / (itemPedido.vlItemPedido + itemPedido.vlSt), LavenderePdaConfig.nuCasasDecimais);
				}
				positivo = true;
				negativo = false;
				itemPedido.vlItemPedido += decimalIncrement;
				itemPedido.vlPctDesconto = itemPedido.vlPctDescontoStReverso = ValueUtil.round(ItemPedidoService.getInstance().calculaVlPctDesconto(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido), LavenderePdaConfig.nuCasasDecimais);
			} else if (newVlItemPedidoStReverso > vlItemPedidoStReversoOld) {
				if (!positivo) {
					itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * vlItemPedidoStReverso / (itemPedido.vlItemPedido + itemPedido.vlSt), LavenderePdaConfig.nuCasasDecimais);
				}
				positivo = false;
				negativo = true;
				itemPedido.vlItemPedido += decimalIncrement;
				itemPedido.vlPctDesconto = itemPedido.vlPctDescontoStReverso = ValueUtil.round(ItemPedidoService.getInstance().calculaVlPctDesconto(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido), LavenderePdaConfig.nuCasasDecimais);
			}
			aplicaSTItemPedidoNormal(itemPedido, tributacao, tributacaoVlBase);
		}
	}

	private double calculaVlStReverso(ItemPedido itemPedido, double vlPctDescStRerverso) {
		return ValueUtil.round(itemPedido.vlItemPedidoStReverso - aplicaPctDescStReverso(itemPedido, vlPctDescStRerverso), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
	}

	private double aplicaPctDescStReverso(ItemPedido itemPedido, double vlPctDescStRerverso) {
		return ValueUtil.round(itemPedido.vlBaseItemPedido - itemPedido.vlBaseItemPedido * vlPctDescStRerverso, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
	}

	private double aplicaReducaoBase(double vlBase, double vlPctReducao) {
		return ValueUtil.round(vlBase - (vlBase * vlPctReducao), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
	}

	private double calculaIcmsRetido(double vlBaseIcmsCalcRetido, double vlBaseIcmsRetidoCalcRetido, Tributacao tributacao, ItemPedido itemPedido, TributacaoVlBase tributacaoVlBase) throws SQLException {
		boolean existeVlBaseIcmsRetidoCalcRetido = tributacaoVlBase.isExisteVlBaseIcmsRetidoCalcRetido();
		double repassePct = tributacao.vlPctRepasseIcms;
    	double margemAgregadaPctIcms = itemPedido.vlPctMargemAgregada = tributacao.vlPctMargemAgregada;
    	double reducaoPct = tributacao.vlPctReducaoBaseIcmsRetido;
    	double aliquotaRetido = tributacao.vlPctIcmsRetido;
    	double aliquotaFecop = 0;
    	double vlFecopARecolher = 0;
    	double vlFrete = 0;
    	double vlIpiParaCalculo = 0;
    	double vlIcms = itemPedido.vlIcms;
    	double vlIcmsFinal = vlBaseIcmsRetidoCalcRetido;
    	boolean usaReducaoPct = true;
		boolean usaMargemAgregada = true;
    	if (tributacao.isFlMedicamento() && !tributacao.isPossuiFundoDePobreza()) {
        	if (!existeVlBaseIcmsRetidoCalcRetido) {
        		usaReducaoPct = false;
        	} else {
        		double vlbase = tributacaoVlBase.getVlBaseIcmsRetidoCalcRetido(tributacao.vlPctDiferenca, itemPedido.vlItemPedido, margemAgregadaPctIcms);
				double resultadoPrimeiroCalculoAuxiliar = executaPrimeiroCalculoAuxiliar(vlBaseIcmsCalcRetido, vlbase, tributacao);
        		if (resultadoPrimeiroCalculoAuxiliar <= getVlPctDiferenca(tributacao.vlPctDiferenca)) {
					usaMargemAgregada = false;
					vlIcmsFinal = vlBaseIcmsRetidoCalcRetido = vlbase;
				} else {
					double resultadoSegundoCalculoAuxiliar = executaSegundoCalculoAuxiliar(tributacao, vlBaseIcmsCalcRetido);
					if (resultadoSegundoCalculoAuxiliar > vlBaseIcmsRetidoCalcRetido) {
						usaReducaoPct = false;
						usaMargemAgregada = false;
					} else {
						usaReducaoPct = false;
						vlIcmsFinal = vlBaseIcmsRetidoCalcRetido = getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, null, existeVlBaseIcmsRetidoCalcRetido);
					}
				}
        	}
    	}

    	if (tributacao.isAplicaFrete() && !existeVlBaseIcmsRetidoCalcRetido && !LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos) {
    		vlFrete = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(itemPedido.vlItemPedidoFrete, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : itemPedido.vlItemPedidoFrete;
    		vlIcmsFinal += vlFrete;
    	}
    	if (tributacao.isAplicaIpiNoIcmsRetido() && !existeVlBaseIcmsRetidoCalcRetido) {
    		vlIpiParaCalculo = calculaIpi(tributacao, itemPedido, vlIcmsFinal);
    		vlIcmsFinal += vlIpiParaCalculo;
    	}
    	if (tributacao.isPossuiFundoDePobreza()) {
    		aliquotaFecop = tributacao.vlPctFecop;
    		vlFecopARecolher = FecopService.getInstance().calculaValorFecopARecolher(vlBaseIcmsRetidoCalcRetido, tributacao, vlIcms, vlFrete, margemAgregadaPctIcms, aliquotaRetido, vlIpiParaCalculo, true);	
    	}
    	if (tributacao.isBaseIcmsRetidoComRepasse()) {
    		vlIcmsFinal -= calculaRepasseIcms(vlIcmsFinal, repassePct);
    	}
    	if (usaMargemAgregada) {
    		vlIcmsFinal += calculaMargemAgregada(vlIcmsFinal, margemAgregadaPctIcms);
    	}
    	if (usaReducaoPct) {
    		vlIcmsFinal -= calculaReducaoBaseIcms(vlIcmsFinal, reducaoPct);
    	}
    	vlIcmsFinal = calculaAliquota(vlIcmsFinal, aliquotaRetido + aliquotaFecop); 
    	if (tributacao.vlPctOutorga != 0) {
    		vlIcms = calculaIcmsOutorgado(vlBaseIcmsRetidoCalcRetido, tributacao.vlPctIcms, tributacao.vlPctOutorga);
    	}
    	vlIcmsFinal -=  vlIcms + vlFecopARecolher;
    	if (tributacao.vlPctMinSt != 0) {
			double vlMinSt = calculaIcmsRetidoPctMinSt(vlBaseIcmsRetidoCalcRetido, repassePct, margemAgregadaPctIcms, reducaoPct, tributacao, vlIpiParaCalculo);
			if (vlIcmsFinal < vlMinSt) {
				vlIcmsFinal = vlMinSt;
			}
		}
    	//--
    	if (vlIcmsFinal < 0) {
    		vlIcmsFinal = 0;
    	}
    	return ValueUtil.round(vlIcmsFinal, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
    }

    private double executaSegundoCalculoAuxiliar(Tributacao tributacao, double vlBaseCalculo) {
		return ValueUtil.round(vlBaseCalculo + (vlBaseCalculo * tributacao.vlPctMargemAgregada / 100), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
	}

    private double executaPrimeiroCalculoAuxiliar(double vlBaseIcmsCalcRetido, double vlBaseIcmsRetidoCalcRetido, Tributacao tributacao) {
    	double baseCalculoIcms = ValueUtil.round(vlBaseIcmsCalcRetido - (vlBaseIcmsCalcRetido * tributacao.vlPctReducaoBaseIcms / 100), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
    	double baseCalculoRetido = ValueUtil.round(vlBaseIcmsRetidoCalcRetido - (vlBaseIcmsRetidoCalcRetido * tributacao.vlPctReducaoBaseIcmsRetido / 100), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);

		return baseCalculoRetido > 0 ? baseCalculoIcms / baseCalculoRetido : 0;
	}

	public double calculaIcmsOutorgado(double vlBaseCalculo, double aliquotaNormal, double aliquotaOutorga) {
    	double vlIcmsOutorgado = vlBaseCalculo * ((aliquotaNormal - aliquotaOutorga) / 100);
    	return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlIcmsOutorgado, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlIcmsOutorgado;
    }

    private double calculaIcmsRetidoPctMinSt(double vlBaseCalculo, double repassePct, double margemAgregadaPctIcms, double reducaoPct, Tributacao tributacao, double vlIpi) {
    	double vlPctMinSt = tributacao.vlPctMinSt;
    	double vlFinal = vlBaseCalculo + vlIpi;
    	vlFinal = LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlFinal, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : vlFinal;
    	vlFinal -= calculaRepasseIcms(vlFinal, repassePct);
    	vlFinal += calculaMargemAgregada(vlFinal, margemAgregadaPctIcms);
    	vlFinal -= calculaReducaoIcms(vlFinal, reducaoPct);
    	vlFinal = vlFinal * vlPctMinSt / 100;
    	return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ?  ValueUtil.round(vlFinal, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : ValueUtil.round(vlFinal, 7);
    }

    public void calculaVlTotalPedidoComST(Pedido pedido) {
    	pedido.vlTtPedidoComSt = getVlTotalItensComSt(pedido);
    	if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco) {
			pedido.vlTtPedidoComSt += PedidoService.getInstance().getVlTotalFreteItens(pedido);
		}
    }

    public double getVlTotalItensComSt(final Pedido pedido) {
		double vlTotalItensComSt = 0;
		if (pedido != null) {
			return pedido.vlTotalPedido + getVlTotalStPedido(pedido);
		}
		return vlTotalItensComSt;
	}

    public double getVlTotalStPedido(Pedido pedido) {
    	return getVlTotalStPedido(pedido, LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado);
    }

	private double getVlTotalStPedido(Pedido pedido, boolean tributacaoPersonalizada) {
		double vlTotalSt = 0;
		if (pedido != null && pedido.itemPedidoList != null) {
			int size = pedido.itemPedidoList.size();
			ItemPedido item;
			for (int i = 0; i < size; i++) {
				item = (ItemPedido)pedido.itemPedidoList.items[i];
				if (!item.isItemBonificacao()) {
					vlTotalSt += tributacaoPersonalizada ? item.vlTotalStItem : item.getVlTotalST();
				}
			}
		}
		return vlTotalSt;
	}

	public double getVlAliquotaIcms(Tributacao tributacao, ItemPedido itemPedido) throws SQLException {
		double aliquotaIcms = tributacao != null ? tributacao.vlPctIcms : 0;
		if (aliquotaIcms == 0) {
			aliquotaIcms = Cliente.NU_INSCRICAO_ESTADUAL_ISENTO.equals(itemPedido.pedido.getCliente().nuInscricaoEstadual) ? itemPedido.getProduto().vlPctIcms : itemPedido.pedido.getCliente().vlPctIcms;
		}
		return aliquotaIcms;
	}
	
	private double getVlBaseByVlUnidadeAlternativa(ItemPedido itemPedido, double vlBase) throws SQLException {
		ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
		if (produtoUnidade != null) {
			return calculaVlUnidadeAlternativa(itemPedido, produtoUnidade, vlBase, false);
		}
		return vlBase;
	}
	
	private double calculaVlUnidadeAlternativa(ItemPedido itemPedido, ProdutoUnidade produtoUnidade, double vlBaseCalculo, boolean naoAplicaIndiceVlBaseRentabilidade) throws SQLException {
		double newVlBaseItemPedido = vlBaseCalculo;
		if (produtoUnidade.vlIndiceFinanceiro == 0) {
			produtoUnidade.vlIndiceFinanceiro = 1;
		}
		
		newVlBaseItemPedido = ItemPedidoService.getInstance().aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, newVlBaseItemPedido);
		
		return naoAplicaIndiceVlBaseRentabilidade ? newVlBaseItemPedido : ItemPedidoService.getInstance().aplicaIndiceFinanceiroUnidadeAlternativa(itemPedido, newVlBaseItemPedido, produtoUnidade);
	}
	

}
