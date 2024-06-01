package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class InfoEntregaPed extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPINFOENTREGAPED";

    public String cdEmpresa;
    public String cdRepresentante;
    public String flOrigemPedido;
    public String nuPedido;
    public Date dtEmissao;
    public String cdCliente;
    public String dsInfoEntrega;
    //-- Não Persistentes
    public String nmCliente;
    public Cliente cliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof InfoEntregaPed) {
            InfoEntregaPed infoEntregaPed = (InfoEntregaPed) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, infoEntregaPed.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, infoEntregaPed.cdRepresentante) &&
                ValueUtil.valueEquals(flOrigemPedido, infoEntregaPed.flOrigemPedido) &&
                ValueUtil.valueEquals(nuPedido, infoEntregaPed.nuPedido);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(flOrigemPedido);
        primaryKey.append(";");
        primaryKey.append(nuPedido);
        return primaryKey.toString();
    }

}