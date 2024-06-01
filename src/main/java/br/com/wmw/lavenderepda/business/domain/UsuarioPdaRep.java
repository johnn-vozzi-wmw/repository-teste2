package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.util.ValueUtil;

public class UsuarioPdaRep extends BaseDomain {

    public static String TABLE_NAME = "TBLVPUSUARIOPDAREP";

    public String cdRepresentante;
    //--
    public Representante representante;
    public Usuario usuario;

    //Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioPdaRep) {
            UsuarioPdaRep usuariopdarep = (UsuarioPdaRep) obj;
            return
                ValueUtil.valueEquals(cdUsuario, usuariopdarep.cdUsuario) &&
                ValueUtil.valueEquals(cdRepresentante, usuariopdarep.cdRepresentante);
        }
        return false;
    }

    public String getPrimaryKey() {
    	StringBuffer strBuffer = new StringBuffer();
    	strBuffer.append(cdUsuario);
    	strBuffer.append(";");
    	strBuffer.append(cdRepresentante);
        return strBuffer.toString();
    }

}