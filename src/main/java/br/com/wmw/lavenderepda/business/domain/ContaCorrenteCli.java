package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ContaCorrenteCli extends BaseDomain {

    public static String TABLE_NAME = "TBLVPCONTACORRENTECLI";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String nuDocumento;
    public Date dtMovimentacao;
    public double vlCredito;
    public double vlDebito;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ContaCorrenteCli) {
            ContaCorrenteCli contaCorrenteCli = (ContaCorrenteCli) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, contaCorrenteCli.cdEmpresa) &&
                ValueUtil.valueEquals(cdRepresentante, contaCorrenteCli.cdRepresentante) &&
                ValueUtil.valueEquals(cdCliente, contaCorrenteCli.cdCliente) &&
                ValueUtil.valueEquals(nuDocumento, contaCorrenteCli.nuDocumento);
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
    	strBuffer.append(nuDocumento);
        return strBuffer.toString();
    }

}