package br.com.wmw.lavenderepda.business.builder;

import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;

public class UsuarioRelRepBuilder {
	
    public String cdEmpresa;
    public String cdRepresentante;
    public String cdSupervisor;
    public String cdUsuarioRep;
    public double vlPctMaxAcrescimo;
    public double vlPctMaxDesconto;
    
    public UsuarioRelRepBuilder(String cdEmpresa, String cdRepresentante, String cdUsuarioRep) {
    	this.cdEmpresa = cdEmpresa;
    	this.cdRepresentante = cdRepresentante;
    	this.cdUsuarioRep = cdUsuarioRep;
    }
    
    public UsuarioRelRep build() {
    	return new UsuarioRelRep(this);
    }
    
    public UsuarioRelRepBuilder comVlPctMaxAcrescimo(double vlPctMaxAcrescimo) {
    	this.vlPctMaxAcrescimo = vlPctMaxAcrescimo;
    	return this;
    }
    
    public UsuarioRelRepBuilder comVlPctMaxDesconto(double vlPctMaxDesconto) {
    	this.vlPctMaxDesconto = vlPctMaxDesconto;
    	return this;
    }

}
