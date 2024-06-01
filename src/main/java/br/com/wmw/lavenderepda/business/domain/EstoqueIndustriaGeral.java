package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class EstoqueIndustriaGeral extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPESTOQUEINDUSTRIAGERAL";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdInsumo;
    public String cdLocalEstoque;
    public String flOrigemEstoque;
    public Date dtEstoque;
    public Double qtEstoque;
    public String cdCentroCusto;

    //nao persistente
    public String cdProduto;

    public EstoqueIndustriaGeral(){}

    public EstoqueIndustriaGeral(String cdEmpresa, String cdProduto) {
        this.cdEmpresa = cdEmpresa;
        this.cdProduto = cdProduto;
    }

    public EstoqueIndustriaGeral(String cdEmpresa, String cdProduto, String cdRepresentante) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdProduto = cdProduto;
    	this.cdRepresentante = cdRepresentante;
	}

	@Override
    public boolean equals(Object obj) {
        if (obj instanceof EstoqueIndustriaGeral) {
            EstoqueIndustriaGeral estoqueIndustriaGeral = (EstoqueIndustriaGeral) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, estoqueIndustriaGeral.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, estoqueIndustriaGeral.cdRepresentante) &&
                ValueUtil.valueEquals(cdInsumo, estoqueIndustriaGeral.cdInsumo) &&
                ValueUtil.valueEquals(cdLocalEstoque, estoqueIndustriaGeral.cdLocalEstoque) &&
                ValueUtil.valueEquals(flOrigemEstoque, estoqueIndustriaGeral.flOrigemEstoque) &&
                ValueUtil.valueEquals(dtEstoque, estoqueIndustriaGeral.dtEstoque) &&
                ValueUtil.valueEquals(cdCentroCusto, estoqueIndustriaGeral.cdCentroCusto)   ;
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdInsumo);
        primaryKey.append(";");
        primaryKey.append(cdLocalEstoque);
        primaryKey.append(";");
        primaryKey.append(flOrigemEstoque);
        primaryKey.append(";");
        primaryKey.append(dtEstoque);
        primaryKey.append(";");
        primaryKey.append(cdCentroCusto);
        return primaryKey.toString();
    }

}