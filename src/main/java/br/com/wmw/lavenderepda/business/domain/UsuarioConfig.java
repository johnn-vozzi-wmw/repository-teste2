package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.builder.UsuarioConfigBuilder;

public class UsuarioConfig extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPUSUARIOCONFIG";

    public String cdRepresentantePadrao;
    public String nuRamal;
    public double vlPctDescLiberacaoPedido;
    public double vlPctAcrescLiberacaoPedido;
    public String flLiberaItemPendente;
    public String flLiberaOutraOrdem;
    
    public UsuarioConfig() {
    	super();
    }

    public UsuarioConfig(String cdUsuario) {
        super();
        this.cdUsuario = cdUsuario;
    }

    public UsuarioConfig(UsuarioConfigBuilder usuarioConfigBuilder) {
    	this.cdUsuario = usuarioConfigBuilder.cdUsuario;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioConfig) {
            UsuarioConfig usuarioConfig = (UsuarioConfig) obj;
            return ValueUtil.valueEquals(cdUsuario, usuarioConfig.cdUsuario);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdUsuario);
        return primaryKey.toString();
    }

}