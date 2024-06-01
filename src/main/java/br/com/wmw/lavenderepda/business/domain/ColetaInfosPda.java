package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.util.ValueUtil;
import totalcross.util.Date;

public class ColetaInfosPda extends BaseDomain {

	public static final String TABLE_NAME = "TBLVPCOLETAINFOSPDA";

    public Date dtSync;
    public int vlPctBateria;
    public int vlPctBateriaInicial;
    public String hrSyncInicial;
    public String hrSync;
	public Date dtAlteracao;
	public String hrAlteracao;
	public String flGpsAtivo;
	
	public ColetaInfosPda() {
		this.cdUsuario = Session.getCdUsuario();
		this.dtSync = new Date();
	}

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ColetaInfosPda) {
            ColetaInfosPda radarcoletarep = (ColetaInfosPda) obj;
            return
                ValueUtil.valueEquals(cdUsuario, radarcoletarep.cdUsuario) && 
                ValueUtil.valueEquals(dtSync, radarcoletarep.dtSync);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdUsuario);
        primaryKey.append(";");
        primaryKey.append(dtSync);
        return primaryKey.toString();
    }

}