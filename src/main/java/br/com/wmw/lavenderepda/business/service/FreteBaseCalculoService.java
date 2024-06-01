package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FreteBaseCalculoPdbxDao;

public class FreteBaseCalculoService extends CrudService {

    private static FreteBaseCalculoService instance;

    private FreteBaseCalculoService() {}
    
    @Override
    public void validate(BaseDomain domain) throws SQLException {}

    public static FreteBaseCalculoService getInstance() {
    	return (instance == null) ? instance = new FreteBaseCalculoService() : instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FreteBaseCalculoPdbxDao.getInstance();
    }
	
}