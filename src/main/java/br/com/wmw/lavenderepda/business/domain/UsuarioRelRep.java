package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.builder.UsuarioRelRepBuilder;

public class UsuarioRelRep extends LavendereBaseDomain {
	
	public static String TABLE_NAME = "TBLVPUSUARIORELREP";

    public String cdEmpresa;
    public String cdRepresentante;
	public String cdUsuarioRep;
    public String cdSupervisor;
    public String nmUsuario;
    public double vlPctMaxDesconto;
    public double vlPctMaxAcrescimo;
    public double vlPctAlcadaLibLimiteCredito;
    
    public boolean filtraLiberacaoPorUsuarioEAlcada;
    
    public UsuarioRelRep() {
    	super();
    	this.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	this.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    }
    
    public UsuarioRelRep(UsuarioRelRepBuilder usuarioRelRepBuilder) {
    	this.cdEmpresa = usuarioRelRepBuilder.cdEmpresa;
    	this.cdRepresentante = usuarioRelRepBuilder.cdRepresentante;
    	this.cdUsuarioRep = usuarioRelRepBuilder.cdUsuarioRep;
    	this.vlPctMaxDesconto = usuarioRelRepBuilder.vlPctMaxDesconto;
    	this.vlPctMaxAcrescimo = usuarioRelRepBuilder.vlPctMaxAcrescimo;
    }

    public UsuarioRelRep(String cdUsuarioRep, String nmUsuario) {
    	this.cdUsuarioRep = cdUsuarioRep;
    	this.nmUsuario = nmUsuario;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioRelRep) {
            UsuarioRelRep usuariorelrep = (UsuarioRelRep) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, usuariorelrep.cdEmpresa) && 
                ValueUtil.valueEquals(cdRepresentante, usuariorelrep.cdRepresentante) &&
                ValueUtil.valueEquals(cdUsuarioRep, usuariorelrep.cdUsuarioRep); 
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdRepresentante);
        primaryKey.append(";");
        primaryKey.append(cdUsuarioRep);
        return primaryKey.toString();
    }
    
    public String getCdDomain() {
		return cdUsuarioRep;
	}

	public String getDsDomain() {
		return nmUsuario;
	}

}