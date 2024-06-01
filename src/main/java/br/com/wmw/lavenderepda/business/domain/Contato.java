package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class Contato extends LavendereBasePersonDomain {

	public static String TABLE_NAME = "TBLVPCONTATO";

    public Contato() {
		super(TABLE_NAME);
	}

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdContato;
    //Dinamicos
    public String nmContato;
    public String nuFone;
    public String nuCelular;
    public String dsCargo;
    public String flSexo;
    public String dsEmail;
    public String dsObservacao;
    public Date dtAniversario;
	public String flContatoNovoCliente;
    
    public static String sortAttr;

	@Override
	public String getPrimaryKey() {
		return null;
	}
	
	@Override
	public String toString() {
		if ("NMCONTATO".equalsIgnoreCase(sortAttr)) {
			return nmContato != null ? nmContato.toUpperCase() : "";
		}
		return super.toString();
	}
	
	@Override
	public int getSortIntValue() {
		if ("DTANIVERSARIO".equals(sortAttr) && ValueUtil.isNotEmpty(dtAniversario)) {
    		return dtAniversario.getDay();
    	} else {
    		return super.getSortIntValue();
    	}
	}

	@Override
	public String getCdDomain() {
		return cdContato;
	}

	@Override
	public String getDsDomain() {
		return nmContato;
	}

}