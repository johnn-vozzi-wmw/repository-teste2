package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class EstoqueIndustria extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPESTOQUEINDUSTRIA";

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

    public EstoqueIndustria(){}

    public EstoqueIndustria(String cdEmpresa, String cdRepresentante, String cdProduto) {
        this.cdEmpresa = cdEmpresa;
        this.cdRepresentante = cdRepresentante;
        this.cdProduto = cdProduto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof EstoqueIndustria) {
            EstoqueIndustria estoqueIndustria = (EstoqueIndustria) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, estoqueIndustria.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, estoqueIndustria.cdRepresentante) &&
                ValueUtil.valueEquals(cdInsumo, estoqueIndustria.cdInsumo) &&
                ValueUtil.valueEquals(cdLocalEstoque, estoqueIndustria.cdLocalEstoque) &&
                ValueUtil.valueEquals(flOrigemEstoque, estoqueIndustria.flOrigemEstoque) &&
                ValueUtil.valueEquals(dtEstoque, estoqueIndustria.dtEstoque) &&
                ValueUtil.valueEquals(cdCentroCusto, estoqueIndustria.cdCentroCusto);
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