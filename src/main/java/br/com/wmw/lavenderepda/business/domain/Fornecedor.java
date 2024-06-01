package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class Fornecedor extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPFORNECEDOR";

    public String cdEmpresa;
    public String cdFornecedor;
    public String nmRazaoSocial;
    public String nmFantasia;
    
    //Nao persistente
    public String cdRepresentante;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof Fornecedor) {
            Fornecedor fornecedor = (Fornecedor) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, fornecedor.cdEmpresa) &&
                ValueUtil.valueEquals(cdFornecedor, fornecedor.cdFornecedor);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdFornecedor);
        return strBuffer.toString();
    }

	public String getCdDomain() {
		return cdFornecedor;
	}

	public String getDsDomain() {
		return nmFantasia;
	}
}