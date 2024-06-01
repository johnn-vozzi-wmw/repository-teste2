package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class AreaVendaProduto extends BaseDomain {

	public static String TABLE_NAME = "TBLVPAREAVENDAPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdAreavenda;
    public String cdProduto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof AreaVendaProduto) {
            AreaVendaProduto areavendaproduto = (AreaVendaProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, areavendaproduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, areavendaproduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdAreavenda, areavendaproduto.cdAreavenda) &&
                ValueUtil.valueEquals(cdProduto, areavendaproduto.cdProduto);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdAreavenda);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
        return  strBuffer.toString();
    }

}