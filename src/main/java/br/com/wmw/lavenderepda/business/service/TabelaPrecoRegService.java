package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoRegDbxDao;

public class TabelaPrecoRegService extends CrudService {

    private static TabelaPrecoRegService instance;

    private TabelaPrecoRegService() {
        //--
    }

    public static TabelaPrecoRegService getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoRegService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TabelaPrecoRegDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

}