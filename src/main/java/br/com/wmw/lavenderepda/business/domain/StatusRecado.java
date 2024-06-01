package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class StatusRecado {

	public static final int STATUSRECADO_CDTODOS = 0;
	public static final int STATUSRECADO_CDNAOLIDO = 1;
	public static final int STATUSRECADO_CDLIDO = 2;
	public static final int STATUSRECADO_CDAENVIAR = 3;
	public static final int STATUSRECADO_CDTRANSMITIDO = 4;

	public int cdStatusRecado;
	public String dsStatusRecado;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusRecado) {
        	StatusRecado statusRecado = (StatusRecado) obj;
            return
                ValueUtil.valueEquals(cdStatusRecado, statusRecado.cdStatusRecado);
        }
        return false;
    }

    @Override
    public String toString() {
    	return dsStatusRecado;
    }

}
