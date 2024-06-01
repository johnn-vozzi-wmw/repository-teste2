package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.EsquemaCorSistemaService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EsquemaCorSistemaLavendereDbxDao;

public class EsquemaCorSistemaLavendereService extends EsquemaCorSistemaService {

    private EsquemaCorSistemaLavendereService() {
        super();
    }

    public static EsquemaCorSistemaService getInstance() {
        if (instance == null) {
            instance = new EsquemaCorSistemaLavendereService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return EsquemaCorSistemaLavendereDbxDao.getInstance();
    }

}