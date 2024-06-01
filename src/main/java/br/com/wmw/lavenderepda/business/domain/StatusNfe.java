package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class StatusNfe extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPSTATUSNFE";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdStatusNfe;
    public String dsStatusNfe;
    public String flBloqueiaImpressao;

    public boolean equals(Object obj) {
        if (obj instanceof StatusNfe) {
        	StatusNfe statusNfe = (StatusNfe) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, statusNfe.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, statusNfe.cdRepresentante) &&
                ValueUtil.valueEquals(cdStatusNfe, statusNfe.cdStatusNfe);
        }
        return false;
    }

	public String getCdDomain() {
		return cdStatusNfe;
	}

	public String getDsDomain() {
		return dsStatusNfe;
	}

	public String getPrimaryKey() {
		StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdStatusNfe);
        return primaryKey.toString();
	}
	
	public boolean isBloqueiaImpressao() {
		return ValueUtil.valueEquals(flBloqueiaImpressao ,ValueUtil.VALOR_SIM);
	}

	public String toString() {
		StringBuilder description = new StringBuilder();
		description.append(dsStatusNfe);
		description.append(" [").append(StringUtil.getStringValue(cdStatusNfe)).append("]");
		return description.toString();
	}

}
