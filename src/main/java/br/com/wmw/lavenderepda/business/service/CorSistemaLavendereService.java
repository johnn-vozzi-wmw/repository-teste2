package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.CorSistemaService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CorSistemaLavendereDbxDao;

public class CorSistemaLavendereService extends CorSistemaService {

    private CorSistemaLavendereService() {
        super();
    }

    public static CorSistemaService getInstance() {
        if (instance == null) {
            instance = new CorSistemaLavendereService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CorSistemaLavendereDbxDao.getInstance();
    }

}