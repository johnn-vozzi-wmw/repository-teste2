package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class StatusAtividade extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPSTATUSATIVIDADE";

    public String cdStatusAtividade;
    public String dsStatusAtividade;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public StatusAtividade() {
	}
    
    
    public StatusAtividade(String cdStatusAtividade, String dsStatusAtividade) {
    	super();
		this.cdStatusAtividade = cdStatusAtividade;
		this.dsStatusAtividade = dsStatusAtividade;
	}
    
    //Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusAtividade) {
            StatusAtividade statusatividade = (StatusAtividade) obj;
            return
                ValueUtil.valueEquals(cdStatusAtividade, statusatividade.cdStatusAtividade);
        }
        return false;
    }

    //@Override
	public String getPrimaryKey() {
        return cdStatusAtividade;
    }

	@Override
	public String getCdDomain() {
		return cdStatusAtividade;
	}

	@Override
	public String getDsDomain() {
		return dsStatusAtividade;
	}

}