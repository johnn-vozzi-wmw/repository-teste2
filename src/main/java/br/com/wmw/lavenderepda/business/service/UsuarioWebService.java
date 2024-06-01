package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.UsuarioWeb;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioWebPdbxDao;

public class UsuarioWebService extends CrudService {

    private static UsuarioWebService instance;

    private UsuarioWebService() {
        //--
    }

    public static UsuarioWebService getInstance() {
        if (instance == null) {
            instance = new UsuarioWebService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return UsuarioWebPdbxDao.getInstance();
    }
    
    public String getNmUsuario(String cdUsuario) throws SQLException {
    	UsuarioWeb usuarioWebFilter = new UsuarioWeb();
    	usuarioWebFilter.cdUsuarioWeb = cdUsuario;
    	return findColumnByRowKey(usuarioWebFilter.getRowKey(), "NMUSUARIOWEB");
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

}