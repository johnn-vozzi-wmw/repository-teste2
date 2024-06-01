package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class ContatoCrm extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPCONTATOCRM";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdCliente;
    public String cdContato;
    public String nmContato;
    public String nuFone;
    public String dsEmail;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof ContatoCrm) {
            ContatoCrm contatocrm = (ContatoCrm) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, contatocrm.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, contatocrm.cdRepresentante) && 
                ValueUtil.valueEquals(cdCliente, contatocrm.cdCliente) && 
                ValueUtil.valueEquals(cdContato, contatocrm.cdContato);
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
        primaryKey.append(cdContato);
        return primaryKey.toString();
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