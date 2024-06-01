package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.TributacaoVlBase;

public class FecopService {
	
	private static FecopService instance;
	
	private FecopService() { }
	
	public static FecopService getInstance() {
		return instance == null ? instance = new FecopService() : instance;
	}

	private double getValorArredondado(double valor) {
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(valor, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : valor;
	}

	private double calculaRepasse(double vlBaseCalculo, double vlPctRepasseIcms, Tributacao tributacao) {
		if (!tributacao.isBaseIcmsRetidoComRepasse()) {
			return 0;
		}
		double vlRepasse = vlBaseCalculo * vlPctRepasseIcms / 100;
		return getValorArredondado(vlRepasse);
	}

	private double calculaMargemAgregada(double vlBaseCalculo, double margemAgregadaPctIcms) {
		double vlMargemAgregada = vlBaseCalculo * margemAgregadaPctIcms / 100;
		return getValorArredondado(vlMargemAgregada);
	}
	
	private double calculaReducaoBaseIcmsRetido(double vlBaseCalculo, double vlPctReducaoBaseIcmsRetido) {
		double vlReducaoBaseIcmsRetido = vlBaseCalculo * vlPctReducaoBaseIcmsRetido / 100;
		return getValorArredondado(vlReducaoBaseIcmsRetido);
	}
	
	private double calculaAliquotaFecop(double vlBaseCalculo, double vlPctFecop) {
		double vlAliquotaFecop = vlBaseCalculo * vlPctFecop / 100;
		return getValorArredondado(vlAliquotaFecop);
	}
	
	private double calculaAliquotaFecopRecolher(double vlBaseCalculo, double vlPctFecopRecolher) {
		double vlAliquotaFecopRecolher = vlBaseCalculo * vlPctFecopRecolher / 100;
		return getValorArredondado(vlAliquotaFecopRecolher);
	}

	private double getVlBaseIcmsRetidoCalcRetido(ItemPedido itemPedido, Tributacao tributacao, TributacaoVlBase tributacaoVlBase, boolean existeVlBaseIcmsRetidoCalcRetido) throws SQLException {
		return getValorArredondado(STService.getInstance().getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, tributacaoVlBase, existeVlBaseIcmsRetidoCalcRetido));
	}

	private double getValorIcms(ItemPedido itemPedido, Tributacao tributacao, boolean existeVlBaseIcmsCalcRetido, double vlBaseIcmsCalcRetido) throws SQLException {
		double vlBaseIcms = tributacao.cdTipoFecop == 4 ? 0 : vlBaseIcmsCalcRetido;
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			return STService.getInstance().calculaIcmsPersonalizado(itemPedido, itemPedido.tributacaoConfig, tributacao, vlBaseIcms);
		} else {
			return STService.getInstance().calculaIcmsNormal(vlBaseIcms, tributacao, existeVlBaseIcmsCalcRetido, itemPedido);
		}
	}

	private double getVlBaseIpiParaCalculo(ItemPedido itemPedido, Tributacao tributacao, double vlBaseIcmsRetidoCalcRetido) throws SQLException {
		double vlBaseIpiParaCalculo = 0;
		if (tributacao.isAplicaIpiNoIcmsRetido()) {
			Produto produto = itemPedido.getProduto();
			if (produto.vlIpi != 0) {
				vlBaseIpiParaCalculo = produto.vlIpi;
			} else {
				vlBaseIpiParaCalculo = vlBaseIcmsRetidoCalcRetido * produto.vlPctIpi / 100;
			}
			if (vlBaseIpiParaCalculo < produto.vlMinIpi) {
				vlBaseIpiParaCalculo = produto.vlMinIpi;
			}
		}
		return getValorArredondado(vlBaseIpiParaCalculo);
	}

	private double getVlFrete(ItemPedido itemPedido, Tributacao tributacao, boolean tributacaoPersonalizada, boolean existeVlBaseIcmsRetidoCalcRetido) {
		double vlFrete = 0;
		if (!LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos && (tributacao.isAplicaFrete() || !existeVlBaseIcmsRetidoCalcRetido)) {
			if (tributacaoPersonalizada) {
				vlFrete = itemPedido.vlTotalItemPedidoFrete;
			} else {
				vlFrete = itemPedido.vlItemPedidoFrete;
			}
		}
		return getValorArredondado(vlFrete);
	}

	public double aplicaFecopNoItemPedido(ItemPedido itemPedido, Tributacao tributacao, TributacaoVlBase tributacaoVlBase) throws SQLException {
		if (tributacao == null || tributacao.cdTipoRecolhimento != Tributacao.TRIBUTACAO_RETIDO || tributacao.cdTipoFecop == 0) {
			return 0;
		}

		int cdTipoFecop = tributacao.cdTipoFecop;

		boolean consideraST = cdTipoFecop == 1 || cdTipoFecop == 4;
		boolean tributacaoPersonalizada = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado;
		boolean existeVlBaseIcmsCalcRetido = tributacaoVlBase != null && tributacaoVlBase.vlBaseIcmsCalcRetido != 0;
		boolean existeVlBaseIcmsRetidoCalcRetido = tributacaoVlBase != null && tributacaoVlBase.vlBaseIcmsRetidoCalcRetido != 0;

		double vlBaseIcmsRetidoCalcRetido = getVlBaseIcmsRetidoCalcRetido(itemPedido, tributacao, tributacaoVlBase, existeVlBaseIcmsRetidoCalcRetido);
		double vlFrete = getVlFrete(itemPedido, tributacao, tributacaoPersonalizada, existeVlBaseIcmsRetidoCalcRetido);
		double vlBaseIcmsCalcRetido = getValorArredondado(STService.getInstance().getVlBaseIcmsCalcRetido(itemPedido, tributacaoVlBase, existeVlBaseIcmsCalcRetido));
		double valorIcms = consideraST ? getValorIcms(itemPedido, tributacao, existeVlBaseIcmsCalcRetido, vlBaseIcmsCalcRetido) : 0;
		double aliquotaRetido = consideraST ? tributacao.vlPctIcmsRetido : 0;
		double margemAgregadaPctIcms = itemPedido.vlPctMargemAgregada = (cdTipoFecop != 3) ? tributacao.vlPctMargemAgregada : 0;
		double vlIpi = getVlBaseIpiParaCalculo(itemPedido, tributacao, vlBaseIcmsRetidoCalcRetido);

		double vlFecop;
		switch (cdTipoFecop) {
			case 1:
				vlFecop = calculaValorFecop1(vlBaseIcmsRetidoCalcRetido, tributacao, valorIcms, vlFrete, margemAgregadaPctIcms, aliquotaRetido, vlIpi);
				break;

			case 2:
				vlFecop = calculaValorFecop2(vlBaseIcmsRetidoCalcRetido, tributacao, vlFrete, margemAgregadaPctIcms, vlIpi);
				break;

			case 3:
				vlFecop = calculaValorFecop3(vlBaseIcmsRetidoCalcRetido, tributacao, vlFrete, vlIpi);
				break;

			case 4:
				vlFecop = calculaValorFecop4(vlBaseIcmsRetidoCalcRetido, tributacao, itemPedido);
				break;

			default:
				vlFecop = calculaValorFecopARecolher(vlBaseIcmsRetidoCalcRetido, tributacao, valorIcms, vlFrete, margemAgregadaPctIcms, aliquotaRetido, vlIpi, consideraST); //Antigo (PRM303)
				break;
		}

		if (tributacaoPersonalizada) {
			itemPedido.vlTotalFecopItem = vlFecop;
		} else {
			itemPedido.vlFecop = vlFecop;
		}

		return vlFecop;
	}

	public double calculaValorFecop1(double vlBaseCalculoRetido, Tributacao tributacao, double valorIcms, double vlFrete, double margemAgregadaPctIcms, double aliquotaRetido, double vlIpi) {
		//((BASE_CALCULO_RETIDO + VALOR_FRETE + IPI_PCT - REPASSE_PCT + MARGEM_AGREGADA - REDUCAO_PCT) * (ALIQUOTA_RETIDO + ALIQUOTA_FECOP) - VALOR_ICMS) * ALIQUOTA_FECOP_RECOLHER

		double vlFecopARecolher = vlBaseCalculoRetido + vlFrete + vlIpi;
		vlFecopARecolher -= calculaRepasse(vlFecopARecolher, tributacao.vlPctRepasseIcms, tributacao);
		vlFecopARecolher += calculaMargemAgregada(vlBaseCalculoRetido, margemAgregadaPctIcms);
		vlFecopARecolher -= calculaReducaoBaseIcmsRetido(vlFecopARecolher, tributacao.vlPctReducaoBaseIcmsRetido);
		vlFecopARecolher = calculaAliquotaFecop(vlFecopARecolher, aliquotaRetido + tributacao.vlPctFecop);
		vlFecopARecolher -= valorIcms;

		return calculaAliquotaFecopRecolher(vlFecopARecolher, tributacao.vlPctFecopRecolher);
	}

	public double calculaValorFecop2(double vlBaseCalculoRetido, Tributacao tributacao, double vlFrete, double margemAgregadaPctIcms, double vlIpi) {
		//((BASE_CALCULO_RETIDO + VALOR_FRETE + IPI_PCT - REPASSE_PCT + MARGEM_AGREGADA - REDUCAO_PCT) * ALIQUOTA_FECOP_RECOLHER)

		double vlFecopARecolher = vlBaseCalculoRetido + vlFrete + vlIpi;
		vlFecopARecolher -= calculaRepasse(vlFecopARecolher, tributacao.vlPctRepasseIcms, tributacao);
		vlFecopARecolher += calculaMargemAgregada(vlBaseCalculoRetido, margemAgregadaPctIcms);
		vlFecopARecolher -= calculaReducaoBaseIcmsRetido(vlFecopARecolher, tributacao.vlPctReducaoBaseIcmsRetido);

		return calculaAliquotaFecopRecolher(vlFecopARecolher, tributacao.vlPctFecopRecolher);
	}

	public double calculaValorFecop3(double vlBaseCalculoRetido, Tributacao tributacao, double vlFrete, double vlIpi) {
		//((BASE_CALCULO_RETIDO + VALOR_FRETE + IPI_PCT - REPASSE_PCT - REDUCAO_PCT ) * ALIQUOTA_FECOP_RECOLHER)

		double vlFecopARecolher = vlBaseCalculoRetido + vlFrete + vlIpi;
		vlFecopARecolher -= calculaRepasse(vlFecopARecolher, tributacao.vlPctRepasseIcms, tributacao);
		vlFecopARecolher -= calculaReducaoBaseIcmsRetido(vlFecopARecolher, tributacao.vlPctReducaoBaseIcmsRetido);

		return calculaAliquotaFecopRecolher(vlFecopARecolher, tributacao.vlPctFecopRecolher);
	}

	public double calculaValorFecop4(double vlBaseCalculoRetido, Tributacao tributacao, ItemPedido itemPedido) {
		//(BASE_CALCULO_ST * ALIQUOTA_FECOP_RECOLHER) - (BASE_CALCULO_ICMS * ALIQUOTA_FECOP)

		double vlFecop =  (vlBaseCalculoRetido * (tributacao.vlPctFecopRecolher / 100)) - (itemPedido.vlTotalIcmsItem * (tributacao.vlPctFecop / 100));
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlFecop, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : ValueUtil.round(vlFecop, 7);
	}

	//Antigo - Legado 2015 (PRM303)
	public double calculaValorFecopARecolher(double vlBaseCalculoRetido, Tributacao tributacao, double valorIcms, double vlFrete, double margemAgregadaPctIcms, double aliquotaRetido, double vlIpi, boolean isCalculoST) {
		double vlFecopARecolher = vlBaseCalculoRetido + vlFrete + vlIpi;
		vlFecopARecolher -= calculaRepasse(vlFecopARecolher, tributacao.vlPctRepasseIcms, tributacao);
		if (!LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			vlFecopARecolher += calculaMargemAgregada(vlBaseCalculoRetido, margemAgregadaPctIcms);
			vlFecopARecolher -= calculaReducaoBaseIcmsRetido(vlFecopARecolher, tributacao.vlPctReducaoBaseIcmsRetido);
		}
		if (isCalculoST) {
			vlFecopARecolher = calculaAliquotaFecop(vlFecopARecolher, aliquotaRetido + tributacao.vlPctFecop);
			vlFecopARecolher -= valorIcms;
		}
		vlFecopARecolher = calculaAliquotaFecopRecolher(vlFecopARecolher, tributacao.vlPctFecopRecolher);
		return LavenderePdaConfig.usaArredondamentoIndividualCalculoTributacao ? ValueUtil.round(vlFecopARecolher, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi) : ValueUtil.round(vlFecopARecolher, 7);
	}
}
