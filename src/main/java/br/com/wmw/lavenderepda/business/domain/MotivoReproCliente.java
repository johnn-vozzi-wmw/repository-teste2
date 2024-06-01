package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class MotivoReproCliente extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPMOTIVOREPROCLIENTE";

    public String cdMotivoReprovacao;
    public String dsMotivoReprovacao;
    public Date dtAlteracao;
    public String hrAlteracao;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoReproCliente) {
            MotivoReproCliente motivoReproCliente = (MotivoReproCliente) obj;
            return
                ValueUtil.valueEquals(cdMotivoReprovacao, motivoReproCliente.cdMotivoReprovacao);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdMotivoReprovacao);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdMotivoReprovacao;
	}

	@Override
	public String getDsDomain() {
		return dsMotivoReprovacao;
	}

}