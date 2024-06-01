package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ComiRentabilidade extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPCOMIRENTABILIDADE";
	public static final String NOME_COLUNA_VLPCTRENTABILIDADE = "vlPctRentabilidade";

    public String cdEmpresa;
    public String cdRepresentante;
    public double vlPctRentabilidade;
    public double vlPctComissao;
    public double vlPctVerba;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ComiRentabilidade) {
            ComiRentabilidade comiRentabilidade = (ComiRentabilidade) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, comiRentabilidade.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, comiRentabilidade.cdRepresentante) && 
                ValueUtil.valueEquals(vlPctRentabilidade, comiRentabilidade.vlPctRentabilidade);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(vlPctRentabilidade);
        return primaryKey.toString();
    }

}