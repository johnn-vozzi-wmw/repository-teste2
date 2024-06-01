package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class MotivoChurn extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPMOTIVOCHURN";
	public static String CD_SEM_MOTIVO_INFORMADO = "-1";
	public static String DS_SEM_MOTIVO_INFORMADO = "--- Sem motivo informado ---";

    public String cdMotivoChurn;
    public String dsMotivoChurn;
    public Date dtAlteracao;
    public String hrAlteracao;
    
    public MotivoChurn() { }
    
    public MotivoChurn(String cdMotivoChurn) {
		this.cdMotivoChurn =  cdMotivoChurn; 
	}

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof MotivoChurn) {
            MotivoChurn motivochurn = (MotivoChurn) obj;
            return
                ValueUtil.valueEquals(cdMotivoChurn, motivochurn.cdMotivoChurn);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdMotivoChurn);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdMotivoChurn;
	}

	@Override
	public String getDsDomain() {
		return dsMotivoChurn;
	}
	
	public boolean isSemMotivoInformado() {
		return ValueUtil.valueEquals( MotivoChurn.CD_SEM_MOTIVO_INFORMADO, cdMotivoChurn);
	}
	
	@Override
	public String toString() {
		return isSemMotivoInformado() ? dsMotivoChurn : super.toString();
	}
    
}