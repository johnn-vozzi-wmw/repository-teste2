package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.FreteBaseCalculo;
import br.com.wmw.lavenderepda.business.domain.FreteCalculo;
import br.com.wmw.lavenderepda.business.domain.FreteConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FreteCalculoPdbxDao;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;

public class FreteCalculoService extends CrudService {

    private static FreteCalculoService instance;

    private FreteCalculoService() {}
    
    @Override
    public void validate(BaseDomain domain) throws SQLException {}
    
    public static FreteCalculoService getInstance() {
    	return (instance == null) ? instance = new FreteCalculoService() : instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FreteCalculoPdbxDao.getInstance();
    }

	public Vector findAllFreteCalculoByFreteConfig(FreteConfig freteConfig) throws SQLException {
		if (freteConfig == null) return null;
		return FreteCalculoPdbxDao.getInstance().findAllFreteCalculoByFreteConfig(freteConfig);
	}

	public void calculaFreteCalculoList(Pedido pedido, FreteConfig freteConfig, Vector listFreteCalculo) {
		if (ValueUtil.isEmpty(listFreteCalculo)) return;
		if (ValueUtil.isEmpty(freteConfig.listFreteCalculoFormaFrete)) freteConfig.listFreteCalculoFormaFrete = new Vector();
		if (freteConfig.vlPrecoFreteCalculado == null) freteConfig.vlPrecoFreteCalculado = BigDecimal.ZERO;
		
		int size = listFreteCalculo.size();
		HashMap<Integer, FreteCalculo> hashCalculos = new HashMap<Integer, FreteCalculo>();
		BigDecimal vlFreteFinalPedido = BigDecimal.ZERO;
		FreteCalculo freteFaixaEscolhido = null;
		
		for (int i = 0; i < size; i++) {
			FreteCalculo freteCalculo = (FreteCalculo) listFreteCalculo.items[i];
			freteCalculo.valorCalculado = BigDecimal.ZERO;
			BigDecimal vlMinimoFreteCalculo = ValueUtil.toBigDecimal(freteCalculo.vlMinimo);
			BigDecimal valorCalculo = BigDecimal.ZERO;
			if (freteCalculo.valorCalculado == null) freteCalculo.valorCalculado = BigDecimal.ZERO;
			
			valorCalculo = calculaFreteValor(pedido, freteCalculo);
			
			if (freteCalculo.freteEvento.isTipoBaseCalculoCalculado() && ValueUtil.isNotEmpty(freteCalculo.listFreteBaseCalculo)) {
				processaFreteBaseCalculo(hashCalculos, freteCalculo, vlMinimoFreteCalculo);
			}
			freteFaixaEscolhido = processaFreteCalculoFaixa(pedido, freteConfig, hashCalculos, vlFreteFinalPedido, freteFaixaEscolhido, freteCalculo);
			freteCalculo.valorCalculado = freteCalculo.valorCalculado.compareTo(BigDecimal.ZERO) < 1 ? freteCalculo.valorCalculado.add(valorCalculo) : freteCalculo.valorCalculado;
			hashCalculos.put(freteCalculo.cdFreteCalculo, freteCalculo);
			if (!freteCalculo.freteEvento.isTipoBaseCalculoFaixaPeso()) {
				processaFormaPrecoFrete(freteConfig, vlFreteFinalPedido, freteCalculo);
			}
		}
		realizaProcessamentoFaixaEscolhida(pedido, freteConfig, hashCalculos, vlFreteFinalPedido, freteFaixaEscolhido);
	}

	private BigDecimal calculaFreteValor(final Pedido pedido, FreteCalculo freteCalculo) {
		BigDecimal valorCalculo = BigDecimal.ZERO;
		BigDecimal vlTaxa = ValueUtil.toBigDecimal(freteCalculo.vlTaxa);
		BigDecimal vlMinimo = ValueUtil.toBigDecimal(freteCalculo.vlMinimo);
		if (freteCalculo.freteEvento.isTipoBaseCalculoSemBaseCalculo()) {
			valorCalculo = ValueUtil.toBigDecimal(freteCalculo.vlTaxa);
		} else if (freteCalculo.freteEvento.isTipoBaseCalculoValorPedido()) {
			valorCalculo = aplicaIndiceTaxaValor(ValueUtil.toBigDecimal(pedido.vlTotalPedido), vlTaxa, freteCalculo.flTipoTaxa);
		} else if (freteCalculo.freteEvento.isTipoBaseCalculoPesoPedido()) {
			valorCalculo = aplicaIndiceTaxaValor(ValueUtil.toBigDecimal(pedido.qtPeso), vlTaxa, freteCalculo.flTipoTaxa);
		} else if (freteCalculo.freteEvento.isTipoBaseCalculoVolume()) {
			valorCalculo = aplicaIndiceTaxaValor(ValueUtil.toBigDecimal(pedido.vlVolumePedido), vlTaxa, freteCalculo.flTipoTaxa);
		}
		valorCalculo = getValorCalculadoOrValorMinimo(vlMinimo, valorCalculo);
		return valorCalculo;
	}

	private FreteCalculo processaFreteCalculoFaixa(Pedido pedido, FreteConfig freteConfig, HashMap<Integer, FreteCalculo> hashCalculos, BigDecimal vlFreteFinalPedido, FreteCalculo freteFaixaEscolhido, FreteCalculo freteCalculo) { 
		if (freteCalculo.freteEvento.isTipoBaseCalculoFaixaPeso() && ValueUtil.toBigDecimal(pedido.qtPeso).compareTo(ValueUtil.toBigDecimal(freteCalculo.vlFaixaBC)) >= 0) {
			freteFaixaEscolhido = freteCalculo;
		}
		if (!freteCalculo.freteEvento.isTipoBaseCalculoFaixaPeso()) {
			realizaProcessamentoFaixaEscolhida(pedido, freteConfig, hashCalculos, vlFreteFinalPedido, freteFaixaEscolhido);
		}
		return freteFaixaEscolhido;
	}

	private void realizaProcessamentoFaixaEscolhida(Pedido pedido, FreteConfig freteConfig, HashMap<Integer, FreteCalculo> hashCalculos, BigDecimal vlFreteFinalPedido, FreteCalculo freteFaixaEscolhido) {
		if (freteFaixaEscolhido == null) return;
		BigDecimal pesoPedido = ValueUtil.toBigDecimal(pedido.qtPeso);
		BigDecimal vlFaixaBC = ValueUtil.toBigDecimal(freteFaixaEscolhido.vlFaixaBC);
		BigDecimal vlTaxaExcedente = ValueUtil.toBigDecimal(freteFaixaEscolhido.vlTaxaExcedenteFaixaBC);
		BigDecimal valorFaixa = aplicaIndiceTaxaValorFaixa(ValueUtil.toBigDecimal(freteFaixaEscolhido.vlTaxa), pesoPedido, vlTaxaExcedente, freteFaixaEscolhido.flTipoTaxa);
		if (pesoPedido.compareTo(vlFaixaBC) > 0  && freteFaixaEscolhido.vlTaxaExcedenteFaixaBC > 0) {
			BigDecimal difPesoFaixa = pesoPedido.subtract(vlFaixaBC);
			valorFaixa = valorFaixa.add(aplicaIndiceTaxaValorFaixa(vlTaxaExcedente, difPesoFaixa, vlTaxaExcedente, freteFaixaEscolhido.flTipoTaxaExcedendeFaixaBC));
		}
		freteFaixaEscolhido.valorCalculado = valorFaixa == null ? BigDecimal.ZERO : valorFaixa;
		hashCalculos.put(freteFaixaEscolhido.cdFreteCalculo, freteFaixaEscolhido);
		processaFormaPrecoFrete(freteConfig, vlFreteFinalPedido, freteFaixaEscolhido);
		freteFaixaEscolhido = null;
	}
	
	private void processaFormaPrecoFrete(FreteConfig freteConfig, BigDecimal vlFreteFinalPedido, FreteCalculo freteCalculo) {
		if (!freteCalculo.isFormaPrecoFrete()) return;
		vlFreteFinalPedido = vlFreteFinalPedido.add(freteCalculo.valorCalculado == null ? BigDecimal.ZERO : freteCalculo.valorCalculado);
		freteConfig.listFreteCalculoFormaFrete.addElement(freteCalculo);
		freteConfig.vlPrecoFreteCalculado = freteConfig.vlPrecoFreteCalculado.add(vlFreteFinalPedido);
	}

	private BigDecimal aplicaIndiceTaxaValorFaixa(BigDecimal valor, BigDecimal peso, BigDecimal vlIndiceTaxa, String flTipoTaxa) {
		if (valor == null) return BigDecimal.ZERO;
		if (vlIndiceTaxa == null || ValueUtil.isEmpty(flTipoTaxa)) return valor;
		switch (flTipoTaxa) {
			case FreteCalculo.TIPO_TAXA_SEM_TAXA: {
				return valor;
			}
			case FreteCalculo.TIPO_TAXA_FIXO: {
				return valor;
			}
			case FreteCalculo.TIPO_TAXA_INDICE:
			case FreteCalculo.TIPO_TAXA_PERCENTUAL: {
				return peso.multiply(vlIndiceTaxa);
			}
			default: {
				return valor;
			}
		}
	}

	protected BigDecimal aplicaIndiceTaxaValor(BigDecimal valor, BigDecimal vlIndiceTaxa, String flTipoTaxa) {
		if (valor == null) return BigDecimal.ZERO;
		if (vlIndiceTaxa == null || ValueUtil.isEmpty(flTipoTaxa)) return valor;
		switch (flTipoTaxa) {
			case FreteCalculo.TIPO_TAXA_SEM_TAXA: {
				return valor;
			}
			case FreteCalculo.TIPO_TAXA_FIXO: {
				return valor.add(vlIndiceTaxa);
			}
			case FreteCalculo.TIPO_TAXA_INDICE: {
				return valor.multiply(vlIndiceTaxa);
			}
			case FreteCalculo.TIPO_TAXA_PERCENTUAL:
				BigDecimal pct = ValueUtil.toBigDecimal(ValueUtil.toDouble(vlIndiceTaxa) / 100);
				return valor.multiply(pct);
			default: {
				return valor;
			}
		}
	}

	private void processaFreteBaseCalculo(HashMap<Integer, FreteCalculo> hashCalculos, FreteCalculo freteCalculo, BigDecimal vlMinimoFreteCalculo) {
		if (hashCalculos.isEmpty()) return;
		if (freteCalculo.freteEvento.isTipoBaseCalculoCalculadoSomatorio()) {
			freteCalculo.valorCalculado = calculaValorSomatorioFreteBaseCalculo(hashCalculos, freteCalculo, vlMinimoFreteCalculo);
		} else if (freteCalculo.freteEvento.isTipoBaseCalculoCalculadoMaiorValor()) {
			freteCalculo.valorCalculado = calculaMaiorValorFreteBaseCalculo(hashCalculos, freteCalculo, vlMinimoFreteCalculo);
		}
	}

	protected BigDecimal calculaValorSomatorioFreteBaseCalculo(HashMap<Integer, FreteCalculo> hashCalculos, FreteCalculo freteCalculo, BigDecimal vlMinimoFreteCalculo) {
		if (ValueUtil.isEmpty(hashCalculos) || freteCalculo == null) return BigDecimal.ZERO;
		if (ValueUtil.isEmpty(vlMinimoFreteCalculo)) vlMinimoFreteCalculo = BigDecimal.ZERO;
		BigDecimal valorAcumuladoFreteBase = BigDecimal.ZERO;
		BigDecimal resultado = BigDecimal.ZERO;
		int baseSize = freteCalculo.listFreteBaseCalculo.size();
		for (int i = 0; i < baseSize; i++) {
			FreteBaseCalculo freteBaseCalculo = (FreteBaseCalculo) freteCalculo.listFreteBaseCalculo.items[i];
			FreteCalculo freteCalculoHash = hashCalculos.get(freteBaseCalculo.cdFreteCalculoBC);
			if (freteCalculoHash != null && freteCalculoHash.valorCalculado != null) {
				valorAcumuladoFreteBase = valorAcumuladoFreteBase.add(freteCalculoHash.valorCalculado);
			}
		}
		resultado = aplicaIndiceTaxaValor(valorAcumuladoFreteBase, ValueUtil.toBigDecimal(freteCalculo.vlTaxa), freteCalculo.flTipoTaxa);
		return getValorCalculadoOrValorMinimo(vlMinimoFreteCalculo, resultado);
	}
	
	protected BigDecimal calculaMaiorValorFreteBaseCalculo(HashMap<Integer, FreteCalculo> hashCalculos, FreteCalculo freteCalculo, BigDecimal vlMinimoFreteCalculo) {
		if (ValueUtil.isEmpty(hashCalculos) || freteCalculo == null) return BigDecimal.ZERO;
		if (ValueUtil.isEmpty(vlMinimoFreteCalculo)) vlMinimoFreteCalculo = BigDecimal.ZERO;
		BigDecimal resultado = BigDecimal.ZERO;
		int baseSize = freteCalculo.listFreteBaseCalculo.size();
		for (int i = 0; i < baseSize; i++) {
			FreteBaseCalculo freteBaseCalculo = (FreteBaseCalculo) freteCalculo.listFreteBaseCalculo.items[i];
			FreteCalculo freteCalculoHash = hashCalculos.get(freteBaseCalculo.cdFreteCalculoBC);
			if (freteCalculoHash != null && freteCalculoHash.valorCalculado != null) {
				if (resultado.compareTo(freteCalculoHash.valorCalculado) <= 0) {
					resultado = freteCalculoHash.valorCalculado;
				} 
			}
		}
		if (resultado.compareTo(vlMinimoFreteCalculo) <= 0) return vlMinimoFreteCalculo;
		resultado = aplicaIndiceTaxaValor(resultado, ValueUtil.toBigDecimal(freteCalculo.vlTaxa), freteCalculo.flTipoTaxa);
		return getValorCalculadoOrValorMinimo(vlMinimoFreteCalculo, resultado);
	}

	protected BigDecimal getValorCalculadoOrValorMinimo(BigDecimal valorMinimo, BigDecimal valorCalculado) {
		if (valorCalculado == null && valorMinimo != null) return valorMinimo;
		if (valorCalculado != null && valorMinimo == null) return valorCalculado;
		if (valorCalculado == null && valorMinimo == null) return BigDecimal.ZERO;
		return valorCalculado.compareTo(valorMinimo) > 0 ? valorCalculado : valorMinimo;
	}
    
}