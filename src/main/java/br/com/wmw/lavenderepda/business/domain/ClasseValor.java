package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ClasseValor extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPCLASSEVALOR";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdClasseValor;
    public String dsClasseValor;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClasseValor) {
            ClasseValor classeValor = (ClasseValor) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, classeValor.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, classeValor.cdRepresentante) &&
                ValueUtil.valueEquals(cdClasseValor, classeValor.cdClasseValor);
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
        primaryKey.append(cdClasseValor);
        return primaryKey.toString();
    }

	//@Override
	public String getCdDomain() {
		return cdClasseValor;
	}

	//@Override
	public String getDsDomain() {
		return dsClasseValor;
	}

}