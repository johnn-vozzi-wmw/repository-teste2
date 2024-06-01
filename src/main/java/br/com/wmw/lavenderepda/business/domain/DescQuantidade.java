package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import totalcross.util.Date;

public class DescQuantidade extends BaseDomain {

    public static String TABLE_NAME = "TBLVPDESCQUANTIDADE";
    public static String CAMPO_CDDESCONTO = "CDDESCONTO";
	public static final String NMCOLUNA_QTITEM = "QTITEM";
	public static final String NMCOLUNA_VLPCTDESCONTO = "VLPCTDESCONTO";
	public static final String NMCOLUNA_CDTABELAPRECO = "CDTABELAPRECO";
	public static final String NMCOLUNA_VLDESCONTO = "VLDESCONTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String cdProduto;
    public int qtItem;
    public double vlPctDesconto;
    public String cdDesconto;
    public double vlDesconto;
    public Date dtInicialVigencia;
    public Date dtFimVigencia;
    public String flAgrupadorSimilaridade;
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescQuantidade) {
            DescQuantidade descontoQuantidade = (DescQuantidade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descontoQuantidade.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, descontoQuantidade.cdRepresentante) &&
                ValueUtil.valueEquals(cdTabelaPreco, descontoQuantidade.cdTabelaPreco) &&
                ValueUtil.valueEquals(cdProduto, descontoQuantidade.cdProduto) &&
                ValueUtil.valueEquals(qtItem, descontoQuantidade.qtItem);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(qtItem);
        return strBuffer.toString();
    }

    public void setCdDesconto(String cdEmpresa, String cdRepresentante, String cdTabelaPreco, String cdProduto) {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	cdDesconto = strBuffer.toString();
    }

    //@Override
    public String toString() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdTabelaPreco);
    	strBuffer.append(qtItem);
        return strBuffer.toString();
    }

	public double getVlDescVlBaseItemPedido(double vlBaseItemPedido) {
		double vlItemPedidoMaxPermitido = 0;
		if (LavenderePdaConfig.usaDescontoPorQuantidadeValor) {
			vlItemPedidoMaxPermitido = vlBaseItemPedido - vlDesconto;
		} else {
			vlItemPedidoMaxPermitido = vlBaseItemPedido * (1 - (vlPctDesconto / 100));
		}
		vlItemPedidoMaxPermitido = ValueUtil.round(vlItemPedidoMaxPermitido);
		return vlItemPedidoMaxPermitido;
	}
	
	public boolean isFlAgrupadorSimilaridade() {
		return ValueUtil.getBooleanValue(flAgrupadorSimilaridade);
	}
}