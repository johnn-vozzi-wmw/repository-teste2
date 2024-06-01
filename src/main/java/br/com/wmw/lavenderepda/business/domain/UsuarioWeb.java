package br.com.wmw.lavenderepda.business.domain;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.util.ValueUtil;

public class UsuarioWeb extends BaseDomain {

    public static String TABLE_NAME = "TBLVPUSUARIOWEB";

    public String cdUsuarioWeb;
    public String nmUsuarioWeb;
    public String flTipoUsuarioWeb;

    //não persistentes
	public String likeFilter;
	public boolean checkedInRecadoList;

	//Override
    public boolean equals(Object obj) {
        if (obj instanceof UsuarioWeb) {
            UsuarioWeb usuarioweb = (UsuarioWeb) obj;
            return
                ValueUtil.valueEquals(cdUsuarioWeb, usuarioweb.cdUsuarioWeb);
        }
        return false;
    }

    //@Override
    public String getPrimaryKey() {
    	return  cdUsuarioWeb;
    }

}