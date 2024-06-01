package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;

public class TipoRecado {

	public static final int TIPORECADO_CDCAIXA_DE_ENTRADA = 1;
	public static final int TIPORECADO_CDCAIXA_DE_SAIDA = 3;
	public static final int TIPORECADO_CDITENS_ENVIADOS = 4;

	public int cdTipoRecado;
	public String dsTipoRecado;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof TipoRecado) {
        	TipoRecado tipoRecado = (TipoRecado) obj;
            return
                ValueUtil.valueEquals(cdTipoRecado, tipoRecado.cdTipoRecado);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return StringUtil.getStringValue(cdTipoRecado);
    }

    //@Override
    public String toString() {
    	return dsTipoRecado;
    }

}
