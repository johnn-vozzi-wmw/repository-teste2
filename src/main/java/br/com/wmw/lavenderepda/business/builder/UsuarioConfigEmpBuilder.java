package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.UsuarioConfigEmp;

public class UsuarioConfigEmpBuilder {
	
    public String cdEmpresa;
    public String cdUsuario;
    
    public UsuarioConfigEmpBuilder() {
    	this.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	this.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
    }
    
    public UsuarioConfigEmpBuilder(String cdEmpresa, String cdUsuario) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdUsuario = cdUsuario;
    }
    
    public UsuarioConfigEmp build() {
    	return new UsuarioConfigEmp(this);
    }

}
