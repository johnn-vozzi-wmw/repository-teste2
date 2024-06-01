package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class MargemRentabFaixa extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPMARGEMRENTABFAIXA";
	public static String NOME_COLUNA_VLPCTMARGEMRENTAB = "VLPCTMARGEMRENTAB";

    public String cdEmpresa;
    public String cdMargemRentab;
    public double vlPctMargemRentab;
    public String cdMotivoPendencia;
    public int nuOrdemLiberacao;
    public int cdCorFaixa;
    public String dsCorFaixa;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public MargemRentabFaixa() {
    	
    }

    public MargemRentabFaixa(String cdEmpresa, String cdMargemRentab, double vlPctMargemRentab) {
		this.cdEmpresa = cdEmpresa;
		this.cdMargemRentab = cdMargemRentab;
		this.vlPctMargemRentab = vlPctMargemRentab;
		sortAtributte = "VLPCTMARGEMRENTAB";
		sortAsc = ValueUtil.VALOR_NAO;
		limit = 1;
	}

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof MargemRentabFaixa) {
            MargemRentabFaixa margemrentabfaixa = (MargemRentabFaixa) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, margemrentabfaixa.cdEmpresa) && 
                ValueUtil.valueEquals(cdMargemRentab, margemrentabfaixa.cdMargemRentab) && 
                ValueUtil.valueEquals(vlPctMargemRentab, margemrentabfaixa.vlPctMargemRentab);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdMargemRentab);
        primaryKey.append(";");
        primaryKey.append(vlPctMargemRentab);
        return primaryKey.toString();
    }

}