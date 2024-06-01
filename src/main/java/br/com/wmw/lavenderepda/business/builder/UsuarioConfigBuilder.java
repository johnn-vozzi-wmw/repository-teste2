package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.UsuarioConfig;

public class UsuarioConfigBuilder {
	  
	public String cdEmpresa;
    public String cdUsuario;
    
    public UsuarioConfigBuilder() {
    	this.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    }
    
    public UsuarioConfigBuilder(String cdUsuario) {
    	this.cdUsuario = cdUsuario;
    }
    
    public UsuarioConfig build() {
    	return new UsuarioConfig(this);
    }
    
}
