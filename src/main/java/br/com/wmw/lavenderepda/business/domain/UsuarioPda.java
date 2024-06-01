package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class UsuarioPda extends BaseDomain {

    public static final String TABLE_NAME = "TBLVPUSUARIOPDA";

	public static final String TIPOPDA_POCKET = "POCKET";
	public static final String TIPOPDA_SIMULADOR = "SIMULADOR";
	public static final String TIPOPDA_ANDROID = "ANDROID";

    public int nuHoraDiferencaServidor;
    public int nuHoraDiferencaHrVerao;

    public Representante representante;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioPda) {
            UsuarioPda usuarioPda = (UsuarioPda) obj;
            return
                ValueUtil.valueEquals(cdUsuario, usuarioPda.cdUsuario);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return cdUsuario;
    }

}