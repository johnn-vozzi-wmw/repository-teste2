package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotivoColeta extends LavendereBaseDomain {
	
	public static final String TABLE_NAME = "TBLVPMOTIVOCOLETA";

    public String cdMotivo;
    public String dsMotivo;
    public String flEncerraAutomatico;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoColeta) {
            MotivoColeta motivoColeta = (MotivoColeta) obj;
            return
                ValueUtil.valueEquals(cdMotivo, motivoColeta.cdMotivo);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdMotivo);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdMotivo;
	}

	@Override
	public String getDsDomain() {
		return dsMotivo;
	}

}