package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class AreaVenda extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPAREAVENDA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdAreavenda;
    public String dsAreavenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AreaVenda) {
            AreaVenda areavenda = (AreaVenda) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, areavenda.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, areavenda.cdRepresentante) &&
                ValueUtil.valueEquals(cdAreavenda, areavenda.cdAreavenda);
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
    	strBuffer.append(cdAreavenda);
        return  strBuffer.toString();
    }

	public String getCdDomain() {
		return cdAreavenda;
	}

	public String getDsDomain() {
		return dsAreavenda;
	}

}