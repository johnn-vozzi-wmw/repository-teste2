package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;

public class UsuarioGrupoProduto extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPUSUARIOGRUPOPRODUTO";

    public String cdEmpresa;
    public String cdUsuarioGrupoProduto;
    public String cdGrupoProduto1;

    public UsuarioGrupoProduto() { super(TABLE_NAME); }

    public UsuarioGrupoProduto(final String cdEmpresa, final String cdUsuarioGrupoProduto, final String cdGrupoProduto1) {
        super(TABLE_NAME);
        this.cdEmpresa = cdEmpresa;
        this.cdUsuarioGrupoProduto = cdUsuarioGrupoProduto;
        this.cdGrupoProduto1 = cdGrupoProduto1;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof UsuarioGrupoProduto)) return false;
        UsuarioGrupoProduto solAutorizacao = (UsuarioGrupoProduto) obj;
        return ValueUtil.valueEquals(cdEmpresa, solAutorizacao.cdEmpresa)
            && ValueUtil.valueEquals(cdUsuarioGrupoProduto, solAutorizacao.cdUsuarioGrupoProduto)
            && ValueUtil.valueEquals(cdGrupoProduto1, solAutorizacao.cdGrupoProduto1);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdUsuarioGrupoProduto);
        primaryKey.append(";");
        primaryKey.append(cdGrupoProduto1);
        return primaryKey.toString();
    }

}