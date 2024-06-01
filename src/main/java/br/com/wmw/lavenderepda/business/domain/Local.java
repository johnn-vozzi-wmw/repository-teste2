package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Local extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPLOCAL";

	public String cdEmpresa;
	public String cdRepresentante;
    public String cdLocal;
    public String dsLocal;
    public Date dtAlteracao;
    public String hrAlteracao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Local) {
            Local local = (Local) obj;
            return
            	ValueUtil.valueEquals(cdEmpresa, local.cdEmpresa) &&
            	ValueUtil.valueEquals(cdRepresentante, local.cdRepresentante) &&
                ValueUtil.valueEquals(cdLocal, local.cdLocal);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdLocal);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdLocal;
	}

	@Override
	public String getDsDomain() {
		return dsLocal;
	}

}