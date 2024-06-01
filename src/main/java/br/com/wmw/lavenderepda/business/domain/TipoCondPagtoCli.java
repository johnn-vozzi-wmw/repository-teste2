package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TipoCondPagtoCli extends BaseDomain {

	public static String TABLE_NAME = "TBLVPTIPOCONDPAGTOCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdCondicaoPagamento;
    public String cdTipoPagamento;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoCondPagtoCli) {
            TipoCondPagtoCli tipoCondPagtoCli = (TipoCondPagtoCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tipoCondPagtoCli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tipoCondPagtoCli.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, tipoCondPagtoCli.cdCliente) &&
                ValueUtil.valueEquals(cdCondicaoPagamento, tipoCondPagtoCli.cdCondicaoPagamento) &&
                ValueUtil.valueEquals(cdTipoPagamento, tipoCondPagtoCli.cdTipoPagamento);
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
        primaryKey.append(cdCliente);
        primaryKey.append(";");
        primaryKey.append(cdCondicaoPagamento);
        primaryKey.append(";");
        primaryKey.append(cdTipoPagamento);
        return primaryKey.toString();
    }

}