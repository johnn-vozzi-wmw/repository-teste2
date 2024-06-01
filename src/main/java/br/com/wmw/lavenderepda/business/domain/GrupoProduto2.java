package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class GrupoProduto2 extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPGRUPOPRODUTO2";

    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String dsGrupoProduto2;
    public String flBloqueiaVendaPorMeta;
    
    //Nao Persistentes
    public Produto produtoFilter;
    public GrupoProdTipoPed grupoProdTipoPedFilter;
    public Restricao restricaoFilter;
    public boolean filterByGrupoProdutoExistente;
    public boolean addCteRestricao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoProduto2) {
            GrupoProduto2 grupoProduto2 = (GrupoProduto2) obj;
            return
                ValueUtil.valueEquals(cdGrupoProduto1, grupoProduto2.cdGrupoProduto1) &&
                ValueUtil.valueEquals(cdGrupoProduto2, grupoProduto2.cdGrupoProduto2);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdGrupoProduto1);
    	strBuffer.append(";");
    	strBuffer.append(cdGrupoProduto2);
        return strBuffer.toString();
    }

    @Override
	public String getCdDomain() {
		return cdGrupoProduto2;
	}

    @Override
	public String getDsDomain() {
		return dsGrupoProduto2;
	}

}