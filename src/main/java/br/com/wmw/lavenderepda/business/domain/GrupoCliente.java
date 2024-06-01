package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class GrupoCliente extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPGRUPOCLIENTE";

    public String cdGrupoCliente;
    public String dsGrupoCliente;
    public String flTipoCadastro;
    public String flRestringeCondPagto;
    public double vlMinPedido;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GrupoCliente) {
            GrupoCliente grupoCliente = (GrupoCliente) obj;
            return
                ValueUtil.valueEquals(cdGrupoCliente, grupoCliente.cdGrupoCliente);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdGrupoCliente);
        return primaryKey.toString();
    }

}