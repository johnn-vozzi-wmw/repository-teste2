package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class ContatoPda extends Contato {

	public static final String CD_REGISTRO_DEFAULT = "1";

    public String flOrigemContato;
    public String flAcaoAlteracao;
    public String cdRegistro;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ContatoPda) {
            ContatoPda contato = (ContatoPda) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, contato.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, contato.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, contato.cdCliente) &&
                ValueUtil.valueEquals(flOrigemContato, contato.flOrigemContato) &&
                ValueUtil.valueEquals(cdContato, contato.cdContato) &&
                ValueUtil.valueEquals(cdRegistro, contato.cdRegistro);
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
    	strBuffer.append(flOrigemContato);
    	strBuffer.append(";");
    	strBuffer.append(cdContato);
    	strBuffer.append(";");
    	strBuffer.append(cdRegistro);
        return strBuffer.toString();
    }

}