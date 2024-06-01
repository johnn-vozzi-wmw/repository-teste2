package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class GrupoProduto4 extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPGRUPOPRODUTO4";

    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public String cdGrupoProduto4;
    public String dsGrupoProduto4;
    
    //Nao Persistentes
    public Produto produtoFilter;
    public Restricao restricaoFilter;
    public boolean filterByGrupoProdutoExistente;
    public boolean addCteRestricao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoProduto4) {
            GrupoProduto4 grupoProduto4 = (GrupoProduto4) obj;
            return
                ValueUtil.valueEquals(cdGrupoProduto1, grupoProduto4.cdGrupoProduto1) && 
                ValueUtil.valueEquals(cdGrupoProduto2, grupoProduto4.cdGrupoProduto2) && 
                ValueUtil.valueEquals(cdGrupoProduto3, grupoProduto4.cdGrupoProduto3) && 
                ValueUtil.valueEquals(cdGrupoProduto4, grupoProduto4.cdGrupoProduto4);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdGrupoProduto1);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto2);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto3);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto4);
        return primaryKey.toString();
    }
    
    @Override
    public String getCdDomain() {
    	return cdGrupoProduto4;
    }
    
    @Override
    public String getDsDomain() {
    	return dsGrupoProduto4;
    }

}