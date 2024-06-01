package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.builder.UsuarioConfigEmpBuilder;

public class UsuarioConfigEmp extends BaseDomain {
	
	public static String TABLE_NAME = "TBLVPUSUARIOCONFIGEMP";

    public String cdEmpresa;
    public double vlPctMaxDesconto;
    public double vlPctMaxAcrescimo;
    public double vlPctAlcadaLibLimiteCredito;
    
    public UsuarioConfigEmp() {
    	super();
    }

    public UsuarioConfigEmp(UsuarioConfigEmpBuilder usuarioConfigEmpBuilder) {
    	this.cdEmpresa = usuarioConfigEmpBuilder.cdEmpresa;
    	this.cdUsuario = usuarioConfigEmpBuilder.cdUsuario;
    }

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioConfigEmp) {
            UsuarioConfigEmp usuarioconfigEmp = (UsuarioConfigEmp) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, usuarioconfigEmp.cdEmpresa) && 
                ValueUtil.valueEquals(cdUsuario, usuarioconfigEmp.cdUsuario);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdUsuario);
        return primaryKey.toString();
    }

}