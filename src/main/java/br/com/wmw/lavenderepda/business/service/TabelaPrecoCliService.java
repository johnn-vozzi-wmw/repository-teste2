package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabelaPrecoCliPdbxDao;

public class TabelaPrecoCliService extends CrudService {

    private static TabelaPrecoCliService instance;

    private TabelaPrecoCliService() {
        //--
    }

    public static TabelaPrecoCliService getInstance() {
        if (instance == null) {
            instance = new TabelaPrecoCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TabelaPrecoCliPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

}