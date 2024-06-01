package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class SugestaoVendaProd extends BaseDomain {

	public static String TABLE_NAME = "TBLVPSUGESTAOVENDAPROD";

    public String cdEmpresa;
    public String cdSugestaoVenda;
    public String cdProduto;
    public int qtUnidadesVenda;
    public int nuRelevancia;

    //Não Persistente
    public int qtVendida;
    public String dsTooltip;
    public double vlPrecoProduto;
    public Produto produto;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof SugestaoVendaProd) {
            SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, sugestaoVendaProd.cdEmpresa) &&
                ValueUtil.valueEquals(cdSugestaoVenda, sugestaoVendaProd.cdSugestaoVenda) &&
                ValueUtil.valueEquals(cdProduto, sugestaoVendaProd.cdProduto);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdSugestaoVenda);
        primaryKey.append(";");
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }
    
    //@Override
    public int getSortIntValue() {
    	return ValueUtil.getIntegerValue(nuRelevancia);
    }

}