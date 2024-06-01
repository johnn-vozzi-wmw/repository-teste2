package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class GrupoProduto1 extends LavendereBaseDomain {

    public static String TABLE_NAME = "TBLVPGRUPOPRODUTO1";

	public static String CD_GRUPO_PRODUTO_VAZIO = "0";

    public String cdGrupoproduto1;
    public String dsGrupoproduto1;
    public String flBloqueiaVendaPorMeta;
    public String cdCanalGrupo;
    public String flComparaDesc;
    public String flValidaInfoCompProduto;
    public String flObrigaCulturaPraga;
    
    //Nao Persistentes
    public Produto produtoFilter;
    public GrupoProdTipoPed grupoProdTipoPedFilter;
    public GrupoProdForn grupoProdFornFilter;
    public Restricao restricaoFilter;
    public boolean filterByGrupoProdutoExistente;
    public boolean addCteRestricao;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoProduto1) {
            GrupoProduto1 grupoproduto1 = (GrupoProduto1) obj;
            return
                ValueUtil.valueEquals(cdGrupoproduto1, grupoproduto1.cdGrupoproduto1);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        return StringUtil.getStringValue(cdGrupoproduto1);
    }

    @Override
	public String getCdDomain() {
		return cdGrupoproduto1;
	}

    @Override
	public String getDsDomain() {
		return dsGrupoproduto1;
	}
	
	@Override
	public String getSortStringValue() {
		return dsGrupoproduto1;
	}
	
	public boolean isComparaDesc() {
		return ValueUtil.VALOR_SIM.equals(flComparaDesc);
	}
	
	public boolean isValidaInfoCompProduto() {
		return !ValueUtil.valueEquals(ValueUtil.VALOR_SIM, flValidaInfoCompProduto);
	}
	
}