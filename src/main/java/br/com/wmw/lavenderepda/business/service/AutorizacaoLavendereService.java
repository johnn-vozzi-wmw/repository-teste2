package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.AutorizacaoService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AutorizacaoLavendereDbxDao;

public class AutorizacaoLavendereService extends AutorizacaoService {

    private AutorizacaoLavendereService() {
        super();
    }

    public static AutorizacaoService getInstance() {
        if (instance == null) {
            instance = new AutorizacaoLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return AutorizacaoLavendereDbxDao.getInstance();
    }

}