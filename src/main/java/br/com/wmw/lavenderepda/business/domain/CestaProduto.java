package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CestaProduto extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCESTAPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCesta;
    public String cdProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CestaProduto) {
            CestaProduto cestaproduto = (CestaProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cestaproduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, cestaproduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdCesta, cestaproduto.cdCesta) &&
                ValueUtil.valueEquals(cdProduto, cestaproduto.cdProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCesta);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return  strBuffer.toString();
    }

}