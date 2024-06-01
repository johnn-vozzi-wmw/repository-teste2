package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.UsuarioSistemaService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioSistemaLavendereDbxDao;

public class UsuarioSistemaLavendereService extends UsuarioSistemaService {

    private UsuarioSistemaLavendereService() {
        super();
    }

    public static UsuarioSistemaService getInstance() {
        if (instance == null) {
            instance = new UsuarioSistemaLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return UsuarioSistemaLavendereDbxDao.getInstance();
    }
    
}