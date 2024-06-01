package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaProduto extends BaseDomain {

    public static String TABLE_NAME = "TBLVPVERBAPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdVerba;
    public String cdProduto;
    public String flGeraVerba;
    public double vlMultiplo;
    public double vlVerba;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaProduto) {
            VerbaProduto verbaProduto = (VerbaProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaProduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, verbaProduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdVerba, verbaProduto.cdVerba) &&
                ValueUtil.valueEquals(cdProduto, verbaProduto.cdProduto) &&
                ValueUtil.valueEquals(flGeraVerba, verbaProduto.flGeraVerba);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdVerba);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(flGeraVerba);
        return strBuffer.toString();
    }

}