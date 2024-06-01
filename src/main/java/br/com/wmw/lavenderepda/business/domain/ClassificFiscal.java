package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ClassificFiscal extends LavendereBaseDomain {

	public static String TABLE_NAME = "TBLVPCLASSIFICFISCAL";

	public String cdEmpresa;
	public String cdRepresentante;
    public String cdClassificFiscal;
    public String dsClassificFiscal;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ClassificFiscal) {
            ClassificFiscal classificFiscal = (ClassificFiscal) obj;
            return
            ValueUtil.valueEquals(cdEmpresa, classificFiscal.cdEmpresa) &&
            ValueUtil.valueEquals(cdRepresentante, classificFiscal.cdRepresentante) &&
            ValueUtil.valueEquals(cdClassificFiscal, classificFiscal.cdClassificFiscal);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer strBuffer = new StringBuffer();
        strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdClassificFiscal);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdClassificFiscal;
	}

	public String getDsDomain() {
		return dsClassificFiscal;
	}

}