package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.UsuarioPda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioPdaPdbxDao;
import totalcross.util.Vector;

public class UsuarioPdaService extends CrudService {

    private static UsuarioPdaService instance;

    private UsuarioPdaService() {
        //--
    }

    public static UsuarioPdaService getInstance() {
        if (instance == null) {
            instance = new UsuarioPdaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioPdaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public String getCdUsuarioPda() throws SQLException {
		Vector usuarioList = UsuarioPdaService.getInstance().findAll();
		if (ValueUtil.isNotEmpty(usuarioList)) {
			UsuarioPda usuario = (UsuarioPda) usuarioList.items[0];
			return usuario.cdUsuario;
		}
		return "";
	}
	
}