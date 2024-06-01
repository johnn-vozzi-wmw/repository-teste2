package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class InsumoProduto extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPINSUMOPRODUTO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdInsumo;
    public String cdProduto;
    public String dsInsumo;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof InsumoProduto) {
            InsumoProduto insumoProduto = (InsumoProduto) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, insumoProduto.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, insumoProduto.cdRepresentante) &&
                ValueUtil.valueEquals(cdInsumo, insumoProduto.cdInsumo) &&
                ValueUtil.valueEquals(cdProduto, insumoProduto.cdProduto);
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
        primaryKey.append(cdProduto);
        return primaryKey.toString();
    }

}