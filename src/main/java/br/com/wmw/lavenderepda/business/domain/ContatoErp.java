package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ContatoErp extends Contato {

    public static final String TABLE_NAME = "TBLVPCONTATOERP";

    public String cdContatoRelacionado;
    public String flDefault;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ContatoErp) {
            ContatoErp contatoerp = (ContatoErp) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, contatoerp.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, contatoerp.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, contatoerp.cdCliente) &&
                ValueUtil.valueEquals(cdContato, contatoerp.cdContato);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuilder strBuffer = new StringBuilder();
    	strBuffer.append(cdEmpresa);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
    	strBuffer.append(";");
    	strBuffer.append(cdCliente);
    	strBuffer.append(";");
    	strBuffer.append(cdContato);
        return strBuffer.toString();
    }

}