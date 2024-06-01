package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.TelaService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TelaLavendereDbxDao;

public class TelaLavendereService extends TelaService {

    private TelaLavendereService() {
        super();
    }

    public static TelaService getInstance() {
        if (instance == null) {
            instance = new TelaLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return TelaLavendereDbxDao.getInstance();
    }

}