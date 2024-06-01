package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Concorrente extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPCONCORRENTE";

    public static String CONCORRENTE_NMCOLUNA_CDCONCORRENTE = "CDCONCORRENTE";
    public static String CONCORRENTE_NMCOLUNA_DSCONCORRENTE = "DSCONCORRENTE";

    public String cdEmpresa;
    public String cdConcorrente;
    public String dsConcorrente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Concorrente) {
            Concorrente concorrente = (Concorrente) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, concorrente.cdEmpresa) &&
                ValueUtil.valueEquals(cdConcorrente, concorrente.cdConcorrente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdConcorrente);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdConcorrente;
	}

	public String getDsDomain() {
		return dsConcorrente == null ? "" : dsConcorrente;
	}
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
}