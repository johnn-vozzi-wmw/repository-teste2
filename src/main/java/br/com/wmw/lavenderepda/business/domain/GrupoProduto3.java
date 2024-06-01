package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class GrupoProduto3 extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPGRUPOPRODUTO3";

    public String cdGrupoProduto1;
    public String cdGrupoProduto2;
    public String cdGrupoProduto3;
    public String dsGrupoProduto3;
    public String flBloqueiaVendaPorMeta;
    
    //Nao Persistentes
    public Produto produtoFilter;
    public GrupoProdTipoPed grupoProdTipoPedFilter;
    public Restricao restricaoFilter;
    public boolean filterByGrupoProdutoExistente;
    public boolean addCteRestricao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoProduto3) {
            GrupoProduto3 grupoProduto3 = (GrupoProduto3) obj;
            return
                ValueUtil.valueEquals(cdGrupoProduto1, grupoProduto3.cdGrupoProduto1) &&
                ValueUtil.valueEquals(cdGrupoProduto2, grupoProduto3.cdGrupoProduto2) &&
                ValueUtil.valueEquals(cdGrupoProduto3, grupoProduto3.cdGrupoProduto3);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdGrupoProduto1);
    	strBuffer.append(";");
    	strBuffer.append(cdGrupoProduto2);
    	strBuffer.append(";");
    	strBuffer.append(cdGrupoProduto3);
        return strBuffer.toString();
    }

    @Override
	public String getCdDomain() {
		return cdGrupoProduto3;
	}

    @Override
	public String getDsDomain() {
		return dsGrupoProduto3;
	}
}