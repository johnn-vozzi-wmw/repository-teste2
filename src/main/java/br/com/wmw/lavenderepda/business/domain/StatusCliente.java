package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;

public class StatusCliente extends LavendereBaseDomain {

	public static final String STATUSCLIENTE_CDTODOS = "0";
	public static final String STATUSCLIENTE_CDBLOQUEADO = "1";
	public static final String STATUSCLIENTE_CDSEMTITULO = "2";
	public static final String STATUSCLIENTE_CDATRASADO = "3";
	public static final String STATUSCLIENTE_CDCOMTITULOS = "4";
	public static final String STATUSCLIENTE_CDINATIVOS = "5";
	public static final String STATUSCLIENTE_CDEMAVALIACAO = "6";
	public static final String STATUSCLIENTE_CDBLOQUEADOPORATRASO = "7";
	public static final String STATUSCLIENTE_CDPENDENTEINATIVACAO = "8";

	public static final String FLSTATUSCLIENTE_BLOQUEADO = "B";
	public static final String FLSTATUSCLIENTE_ATRASADO = "A";


	public String cdStatusCliente;
	public String dsStatusCliente;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatusCliente) {
            StatusCliente statusCliente = (StatusCliente) obj;
            return cdStatusCliente.equals(statusCliente.cdStatusCliente);
        }
        return false;
    }

    @Override
    public String getPrimaryKey() {
    	return StringUtil.getStringValue(cdStatusCliente);
    }
    
    public static String getDsStatusCliente(String cdStatusCliente) {
    	if (cdStatusCliente.equals(STATUSCLIENTE_CDTODOS)) {
    		return Messages.STATUSCLIENTE_DSTODOS;
    	} else if (cdStatusCliente.equals(STATUSCLIENTE_CDBLOQUEADO)) {
    		return Messages.STATUSCLIENTE_DSBLOQUEADO;
    	} else if (cdStatusCliente.equals(STATUSCLIENTE_CDSEMTITULO)) {
    		return Messages.STATUSCLIENTE_DSSEMTITULO;
    	} else if (cdStatusCliente.equals(STATUSCLIENTE_CDATRASADO)) {
    		return Messages.STATUSCLIENTE_DSATRASADO;
    	} else if (cdStatusCliente.equals(STATUSCLIENTE_CDCOMTITULOS)) {
    		return Messages.STATUSCLIENTE_DSCOMTITULOS;
    	} else if (cdStatusCliente.equals(STATUSCLIENTE_CDINATIVOS)) {
    		return Messages.STATUSCLIENTE_DSINATIVOS;
    	} else if (cdStatusCliente.equals(STATUSCLIENTE_CDEMAVALIACAO)) {
    		return Messages.STATUSCLIENTE_DSEMAVALIACAO;
    	} else if (cdStatusCliente.equals(STATUSCLIENTE_CDBLOQUEADOPORATRASO)) {
    		return Messages.STATUSCLIENTE_DSBLOQUEADOPORATRASO;
		} else if (cdStatusCliente.equals(STATUSCLIENTE_CDPENDENTEINATIVACAO)) {
			return Messages.STATUSCLIENTE_DSPENDENTEINATIVACAO;
		} else {
    		return "";
    	}
    }

	@Override
	public String getCdDomain() {
		return cdStatusCliente;
	}

	@Override
	public String getDsDomain() {
		return dsStatusCliente;
	}
}
