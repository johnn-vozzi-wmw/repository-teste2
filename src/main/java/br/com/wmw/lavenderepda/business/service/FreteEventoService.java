package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FreteEventoPdbxDao;

public class FreteEventoService extends CrudService {

    private static FreteEventoService instance;

    private FreteEventoService() {}
    
    @Override
    public void validate(BaseDomain domain) throws SQLException {}

    public static FreteEventoService getInstance() {
    	return (instance == null) ? instance = new FreteEventoService() : instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FreteEventoPdbxDao.getInstance();
    }
	
}