package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.util.ValueUtil;

public class UsuarioSupervisor extends LavendereBaseDomain {

	public static final String TABLE_NAME = "TBLVPUSUARIOSUPERVISOR";
	
    public String cdEmpresa;
    public String cdSupervisor;
    public String cdUsuarioTelevendas;
    public String nmUsuario;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioSupervisor) {
            UsuarioSupervisor usuarioSupervisor = (UsuarioSupervisor) obj;
            return
                ValueUtil.valueEquals(cdEmpresa, usuarioSupervisor.cdEmpresa) && 
                ValueUtil.valueEquals(cdSupervisor, usuarioSupervisor.cdSupervisor) && 
                ValueUtil.valueEquals(cdUsuarioTelevendas, usuarioSupervisor.cdUsuarioTelevendas) && 
                ValueUtil.valueEquals(cdUsuario, usuarioSupervisor.cdUsuario);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
        StringBuffer primaryKey = new StringBuffer();
        primaryKey.append(cdEmpresa);
        primaryKey.append(";");
        primaryKey.append(cdSupervisor);
        primaryKey.append(";");
        primaryKey.append(cdUsuarioTelevendas);
        primaryKey.append(";");
        primaryKey.append(cdUsuario);
        return primaryKey.toString();
    }

	@Override
	public String getCdDomain() {
		return cdUsuarioTelevendas;
	}

	@Override
	public String getDsDomain() {
		return nmUsuario;
	}

}