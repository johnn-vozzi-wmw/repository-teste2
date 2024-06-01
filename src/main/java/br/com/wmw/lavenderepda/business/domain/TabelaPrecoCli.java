package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class TabelaPrecoCli extends BaseDomain {

    public static String TABLE_NAME = "TBLVPTABELAPRECOCLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdTabelaPreco;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TabelaPrecoCli) {
            TabelaPrecoCli tabelaPrecoCliente = (TabelaPrecoCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, tabelaPrecoCliente.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, tabelaPrecoCliente.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, tabelaPrecoCliente.cdCliente) &&
                ValueUtil.valueEquals(cdTabelaPreco, tabelaPrecoCliente.cdTabelaPreco);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdTabelaPreco);
        return strBuffer.toString();
    }

}