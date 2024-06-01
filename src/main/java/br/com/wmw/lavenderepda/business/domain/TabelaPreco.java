package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class TabelaPreco extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPTABELAPRECO";

	public static final int CDTABELAPRECO_CESTAPROMOCIONAL = 9999999;
	public static final String CDTABELAPRECO_VALOR_ZERO = "0";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTabelaPreco;
    public String dsTabelaPreco;
    public double vlPctComissao;
    public String flAcessaOutrasTab;
    public String flAcessivelOutrasTab;
    public String flDescontoQtdAuto;
    public String flEspecial;
    public double vlPctMaxDescAdicionalItem;
    public double qtMinValor;
    public int cdListaTabelaPreco;
    public int cdColunaTabelaPreco;
    public String flTroca;
    public String flPromocional;
    public String flBonificacao;
    public String dsMsgAlerta;
    public String cdLocalEstoque;
    public double qtPesoMin;
    public String cdVerba;
    public double qtMinProduto;
    public double qtMinPedido;
    public double qtMinGrade1;
    public double qtMinGrade2;
    public Date dtInicial;
    public Date dtFinal;
    public String flPermiteDesconto;
    public String flIndicaDtEntregaManual;
    public String flDevolucaoEstoque;
    public String flIgnoraIndiceCondTipoPagto;
    public String cdTabelaPrecoPrincipal;
    public String flBloqueiaDesc2;
    public boolean usaTabelaPrecoApenasCatalogo;
    public double qtPesoAcumuladoAtual;
    public double qtValorMinAcumulado;
    public double vlIndiceComissaoRepInterno;
    public String flDMaxVlBaseItemPed;
    public String flBloqueiaNegociacao;
    
    //Nao persistentes
    public CondPagtoTabPreco condPagtoTabPrecoFilter;
    public TabelaPrecoCli tabelaPrecoCliFilter;
    public TabelaPrecoRep tabelaPrecoRepFilter;
    public TabelaPrecoReg tabelaPrecoRegFilter;
    public TabelaPrecoSeg tabelaPrecoSegFilter;
    public TabPrecTipoPedido tabPrecTipoPedidoFilter;
    public String cdTabelaPrecoPedidoFilter;
    public String cdTabelaPrecoClienteFilter;
    public String notFlAcessivelOutrasTab;
    public String notFlTroca;
    public String notFlBonificacao;
    public String notFlEspecial;
    public String[] cdTabelaPrecoList;
    public boolean filterByHierarquiaTabelasPreco;
    public boolean filterByCdListaTabelaPreco;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TabelaPreco) {
            TabelaPreco tabelaPreco = (TabelaPreco) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tabelaPreco.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tabelaPreco.cdRepresentante) &&
                ValueUtil.valueEquals(cdTabelaPreco, tabelaPreco.cdTabelaPreco);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
        return strBuffer.toString();
    }

    public boolean isFlAcessaOutrasTab() {
    	return !ValueUtil.VALOR_NAO.equals(flAcessaOutrasTab);
    }

    public boolean isFlAcessivelOutrasTab() {
    	return !ValueUtil.VALOR_NAO.equals(flAcessivelOutrasTab);
    }

    @Override
	public String getCdDomain() {
		return cdTabelaPreco;
	}

    @Override
	public String getDsDomain() {
		return dsTabelaPreco;
	}

	public boolean isAplicaDescQtdeAuto() {
		return ValueUtil.VALOR_SIM.equals(flDescontoQtdAuto);
	}
	
	public boolean isEspecial() {
		return ValueUtil.VALOR_SIM.equals(this.flEspecial);
	}
	
	public boolean isPermiteDesconto() {
		return !ValueUtil.VALOR_NAO.equals(flPermiteDesconto);
	}
	
	public boolean isPermiteIndicarDataEntregaManual() {
		return ValueUtil.getBooleanValue(flIndicaDtEntregaManual);
	}
	
	public boolean isIgnoraIndiceCondTipoPagto() {
		return ValueUtil.getBooleanValue(flIgnoraIndiceCondTipoPagto);
	}
	public boolean isBloqueiaDesc2() {
		return ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flBloqueiaDesc2);
	}

	public boolean isUsaDescontoMaximoPorVlBaseItemPedido() {
    	return ValueUtil.getBooleanValue(flDMaxVlBaseItemPed);
	}
	
	public boolean isBloqueiaCampoNegociacao() {
		return ValueUtil.getBooleanValue(flBloqueiaNegociacao);
	}

}