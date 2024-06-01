package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class GrupoProdForn extends BaseDomain {

	public static String TABLE_NAME = "TBLVPGRUPOPRODFORN";
	
    public String cdEmpresa;
    public String cdFornecedor;
    public String cdGrupoProduto1;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoProdForn) {
            GrupoProdForn grupoProdForn = (GrupoProdForn) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, grupoProdForn.cdEmpresa) && 
                ValueUtil.valueEquals(cdFornecedor, grupoProdForn.cdFornecedor) && 
                ValueUtil.valueEquals(cdGrupoProduto1, grupoProdForn.cdGrupoProduto1);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdFornecedor);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto1);
        return primaryKey.toString();
    }

}