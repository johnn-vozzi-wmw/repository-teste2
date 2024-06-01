package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class CCCliPorTipo extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCCCLIPORTIPO";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String nuDocumento;
    public double vlQuebra;
    public double vlMortalidade;
    public double vlPeso;
    public Date dtDocumento;
    public String nuPedido;

    //-- Não Persistente
    public boolean usaFindPedidoVazio;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof CCCliPorTipo) {
            CCCliPorTipo cCCliPorTipo = (CCCliPorTipo) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, cCCliPorTipo.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, cCCliPorTipo.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, cCCliPorTipo.cdCliente) &&
                ValueUtil.valueEquals(nuDocumento, cCCliPorTipo.nuDocumento);
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
        primaryKey.append(nuDocumento);
        return primaryKey.toString();
    }

}