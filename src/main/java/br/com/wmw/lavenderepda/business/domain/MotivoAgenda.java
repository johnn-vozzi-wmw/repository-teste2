package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class MotivoAgenda extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPMOTIVOAGENDA";
	
    public String cdMotivoAgenda;
    public String dsMotivoAgenda;
    public String flAgendaVisita;
    public String flTransferenciaAgenda;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoAgenda) {
            MotivoAgenda motivoAgenda = (MotivoAgenda) obj;
            return
                ValueUtil.valueEquals(cdMotivoAgenda, motivoAgenda.cdMotivoAgenda);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdMotivoAgenda);
        return primaryKey.toString();
    }
    
    @Override
    public String getCdDomain() {
    	return cdMotivoAgenda;
    }
    
    @Override
    public String getDsDomain() {
    	return dsMotivoAgenda;
    }

}