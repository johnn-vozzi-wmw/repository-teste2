package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ProdutoComplemento extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPPRODUTOCOMPLEMENTO";
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdProduto;
    public String cdProdutoComplemento;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ProdutoComplemento) {
            ProdutoComplemento produtoComplementar = (ProdutoComplemento) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, produtoComplementar.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, produtoComplementar.cdRepresentante) && 
                ValueUtil.valueEquals(cdProduto, produtoComplementar.cdProduto) && 
                ValueUtil.valueEquals(cdProdutoComplemento, produtoComplementar.cdProdutoComplemento);
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
        primaryKey.append(cdProduto);
        primaryKey.append(";");
        primaryKey.append(cdProdutoComplemento);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdProduto;
	}

	@Override
	public String getDsDomain() {
		return "";
	}

}