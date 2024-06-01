package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoPagtoCli extends BaseDomain {

	public static String TABLE_NAME = "TBLVPTIPOPAGTOCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdTipoPagamento;
    public String cdCliente;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoPagtoCli) {
            TipoPagtoCli tipoPagtoCli = (TipoPagtoCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoPagtoCli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tipoPagtoCli.cdRepresentante) &&
                ValueUtil.valueEquals(cdTipoPagamento, tipoPagtoCli.cdTipoPagamento) &&
                ValueUtil.valueEquals(cdCliente, tipoPagtoCli.cdCliente);
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
        primaryKey.append(cdTipoPagamento);
        primaryKey.append(";");
        primaryKey.append(cdCliente);
        return primaryKey.toString();
    }

}