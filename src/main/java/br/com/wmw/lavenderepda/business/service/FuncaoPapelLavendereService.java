package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.FuncaoPapelService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FuncaoPapelLavendereDbxDao;

public class FuncaoPapelLavendereService extends FuncaoPapelService {

    private FuncaoPapelLavendereService() {
        super();
    }

    public static FuncaoPapelService getInstance() {
        if (instance == null) {
            instance = new FuncaoPapelLavendereService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return FuncaoPapelLavendereDbxDao.getInstance();
    }

}