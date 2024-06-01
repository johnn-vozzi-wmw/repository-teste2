package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ClasseValorDao;

public class ClasseValorService extends CrudService {

    private static ClasseValorService instance;

    public static ClasseValorService getInstance() {
        if (instance == null) {
            instance = new ClasseValorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ClasseValorDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}


}