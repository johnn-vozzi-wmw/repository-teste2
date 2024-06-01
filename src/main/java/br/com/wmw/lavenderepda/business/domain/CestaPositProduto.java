package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class CestaPositProduto extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCESTAPOSITPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCampanha;
    public String cdCesta;
    public String cdProduto;
    public String cdCliente;
    public double vlMeta;
    public double vlRealizado;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CestaPositProduto) {
        	CestaPositProduto cestapositproduto = (CestaPositProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cestapositproduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, cestapositproduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdCampanha, cestapositproduto.cdCampanha) &&
                ValueUtil.valueEquals(cdCesta, cestapositproduto.cdCesta) &&
                ValueUtil.valueEquals(cdProduto, cestapositproduto.cdProduto) &&
                ValueUtil.valueEquals(cdCliente, cestapositproduto.cdCliente);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCampanha);
    	strBuffer.append(";");
    	strBuffer.append(cdCesta);
    	strBuffer.append(";");
    	strBuffer.append(cdProduto);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
        return  strBuffer.toString();
    }

}