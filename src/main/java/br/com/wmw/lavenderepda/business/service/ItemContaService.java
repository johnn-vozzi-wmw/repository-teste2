package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemContaDao;

public class ItemContaService extends CrudService {

    private static ItemContaService instance;

    public static ItemContaService getInstance() {
        if (instance == null) {
            instance = new ItemContaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemContaDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

}