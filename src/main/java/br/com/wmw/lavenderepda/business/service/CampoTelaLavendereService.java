package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.CampoTelaService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CampoTelaLavendereDbxDao;

public class CampoTelaLavendereService extends CampoTelaService {

    private CampoTelaLavendereService() {
        super();
    }

    public static CampoTelaService getInstance() {
        if (instance == null) {
            instance = new CampoTelaLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return CampoTelaLavendereDbxDao.getInstance();
    }

}