package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotivoPerda extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPMOTIVOPERDA";

    public String cdEmpresa;
    public String cdRepresentante;
    public String cdMotivoPerda;
    public String dsMotivoPerda;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoPerda) {
            MotivoPerda motivoPerda = (MotivoPerda) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, motivoPerda.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, motivoPerda.cdRepresentante) && 
                ValueUtil.valueEquals(cdMotivoPerda, motivoPerda.cdMotivoPerda);
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
        primaryKey.append(cdMotivoPerda);
        return primaryKey.toString();
    }

	//@Override
	public String getCdDomain() {
		return cdMotivoPerda;
	}

	//@Override
	public String getDsDomain() {
		return dsMotivoPerda;
	}
	
}