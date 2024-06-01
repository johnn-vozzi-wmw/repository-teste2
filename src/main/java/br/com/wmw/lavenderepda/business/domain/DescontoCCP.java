package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class DescontoCCP extends BaseDomain {

	public static String TABLE_NAME = "TBLVPDESCONTOCCP";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdClassecliente;
    public String cdClasseProduto;
    public double vlPctDesconto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof DescontoCCP) {
            DescontoCCP descontoCCP = (DescontoCCP) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, descontoCCP.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, descontoCCP.cdRepresentante) &&
                ValueUtil.valueEquals(cdClassecliente, descontoCCP.cdClassecliente) &&
                ValueUtil.valueEquals(cdClasseProduto, descontoCCP.cdClasseProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdClassecliente);
    	strBuffer.append(";");
    	strBuffer.append(cdClasseProduto);
        return strBuffer.toString();
    }

}