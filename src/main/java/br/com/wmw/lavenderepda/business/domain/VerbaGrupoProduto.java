package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class VerbaGrupoProduto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPVERBAGRUPOPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdGrupoProduto1;
    public String cdTabelaPreco;
    public double vlPctVerbaPositiva;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VerbaGrupoProduto) {
            VerbaGrupoProduto verbaGrupoProduto = (VerbaGrupoProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, verbaGrupoProduto.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, verbaGrupoProduto.cdRepresentante) && 
                ValueUtil.valueEquals(cdGrupoProduto1, verbaGrupoProduto.cdGrupoProduto1) &&
                ValueUtil.valueEquals(cdTabelaPreco, verbaGrupoProduto.cdTabelaPreco);
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
        primaryKey.append(cdGrupoProduto1);
        primaryKey.append(";");
        primaryKey.append(cdTabelaPreco);
        return primaryKey.toString();
    }

}