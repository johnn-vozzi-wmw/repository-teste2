package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class RentabilidadeFaixa extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPRENTABILIDADEFAIXA";
	public static final String NOMECOLUNA_VLPCTRENTABILIDADE = "VLPCTRENTABILIDADE";

    public String cdEmpresa;
    public double vlPctRentabilidade;
    public String dsFaixa;
    public String flFaixaMinima;
    public String flFaixaIdeal;
    public int vlR;
    public int vlG;
    public int vlB;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof RentabilidadeFaixa) {
            RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, rentabilidadeFaixa.cdEmpresa) && 
                ValueUtil.valueEquals(vlPctRentabilidade, rentabilidadeFaixa.vlPctRentabilidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(vlPctRentabilidade);
        return primaryKey.toString();
    }
    
    public boolean isFaixaMinima() {
    	return ValueUtil.VALOR_SIM.equals(flFaixaMinima);
    }
    
    public boolean isFaixaIdeal() {
    	return ValueUtil.VALOR_SIM.equals(flFaixaIdeal);
    }

}