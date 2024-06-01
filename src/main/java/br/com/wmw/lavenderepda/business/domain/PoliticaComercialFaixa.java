package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class PoliticaComercialFaixa extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPPOLITICACOMERCIALFAIXA";

    public String cdEmpresa;
    public String cdPoliticaComercial;
    public double vlPctPoliticaComercial;
    public double vlPctComissao;
    public String cdMotivoPendencia;
    public int nuOrdemLiberacao;

    public PoliticaComercialFaixa() { }
    
    public PoliticaComercialFaixa(String cdEmpresa, String cdPoliticaComercial, double vlPctPoliticaComercial) {
		this.cdEmpresa = cdEmpresa;
		this.cdPoliticaComercial = cdPoliticaComercial;
		this.vlPctPoliticaComercial = vlPctPoliticaComercial;
	}    
    
    public PoliticaComercialFaixa(ItemPedido itemPedido) {
    	this.cdEmpresa = itemPedido.cdEmpresa;
		this.cdPoliticaComercial = itemPedido.cdPoliticaComercial;
		this.vlPctPoliticaComercial = (itemPedido.vlPctDesconto + itemPedido.vlPctFaixaDescQtd) - itemPedido.vlPctAcrescimo; 
	}

	public PoliticaComercialFaixa(String cdMotivoPendencia, int nuOrdemLiberacao) {
		this.cdMotivoPendencia = cdMotivoPendencia;
		this.nuOrdemLiberacao = nuOrdemLiberacao;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof PoliticaComercialFaixa) {
            PoliticaComercialFaixa politicaComercialFaixa = (PoliticaComercialFaixa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, politicaComercialFaixa.cdEmpresa) && 
                ValueUtil.valueEquals(cdPoliticaComercial, politicaComercialFaixa.cdPoliticaComercial) && 
                ValueUtil.valueEquals(vlPctPoliticaComercial, politicaComercialFaixa.vlPctPoliticaComercial);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdPoliticaComercial);
        primaryKey.append(";");
        primaryKey.append(vlPctPoliticaComercial);
        return primaryKey.toString();
    }

}