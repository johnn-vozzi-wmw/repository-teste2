package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BasePersonDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;

public class PermissaoSolAut extends BasePersonDomain {

	public static final String TABLE_NAME = "TBLVPPERMISSAOSOLAUT";

    public String cdEmpresa;
    public String cdUsuarioPermissao;
    public TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum;
    public int cdSistema;
    public String flSomenteLeitura;
    public String flIgnoraUsuarioGrupoProd;

    public PermissaoSolAut() { super(TABLE_NAME); }

    public PermissaoSolAut(final String cdEmpresa, final String cdUsuarioPermissao, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum, final int cdSistema) {
        super(TABLE_NAME);
        this.cdEmpresa = cdEmpresa;
        this.cdUsuarioPermissao = cdUsuarioPermissao;
        this.tipoSolicitacaoAutorizacaoEnum = tipoSolicitacaoAutorizacaoEnum;
        this.cdSistema = cdSistema;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PermissaoSolAut)) return false;
        PermissaoSolAut solAutorizacao = (PermissaoSolAut) obj;
        return ValueUtil.valueEquals(cdEmpresa, solAutorizacao.cdEmpresa)
            && ValueUtil.valueEquals(cdUsuarioPermissao, solAutorizacao.cdUsuarioPermissao)
            && ValueUtil.valueEquals(tipoSolicitacaoAutorizacaoEnum, solAutorizacao.tipoSolicitacaoAutorizacaoEnum)
            && ValueUtil.valueEquals(cdSistema, solAutorizacao.cdSistema);
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdUsuarioPermissao);
        primaryKey.append(";");
        primaryKey.append(tipoSolicitacaoAutorizacaoEnum.ordinal());
        primaryKey.append(";");
        primaryKey.append(cdSistema);
        return primaryKey.toString();
    }

    public boolean isSomenteLeitura() { return ValueUtil.getBooleanValue(flSomenteLeitura); }
    public boolean isIgnoraUsuarioGrupoProd() { return ValueUtil.getBooleanValue(flIgnoraUsuarioGrupoProd); }

}