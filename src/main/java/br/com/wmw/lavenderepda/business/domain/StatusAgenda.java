package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class StatusAgenda extends BaseDomain {

    public static final int STATUSAGENDA_TODOS = -1;
	public static final int STATUSAGENDA_CDAVISITAR = 1;
	public static final int STATUSAGENDA_CDNAOPOSITIVADO = 2;
	public static final int STATUSAGENDA_CDPOSITIVADO = 3;
	public static final int STATUSAGENDA_CDREAGENDADO = 4;
	public static final int STATUSAGENDA_CDTRANSFERIDO = 5;

	public static final String STATUSAGENDA_DSAVISITAR = Messages.FILTRO_STATUSAGENDA_DSAVISITAR;
	public static final String STATUSAGENDA_DSNAOPOSITIVADO = Messages.FILTRO_STATUSAGENDA_DSNAOPOSITIVADO;
	public static final String STATUSAGENDA_DSPOSITIVADO = Messages.FILTRO_STATUSAGENDA_DSPOSITIVADO;
	
	public int cdStatusAgenda;
	public String dsStatusAgenda;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusAgenda) {
        	StatusAgenda statusRecado = (StatusAgenda) obj;
            return
                ValueUtil.valueEquals(cdStatusAgenda, statusRecado.cdStatusAgenda);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	return StringUtil.getStringValue(cdStatusAgenda);
    }

    @Override
    public String toString() {
    	return dsStatusAgenda;
    }


}
