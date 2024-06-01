package br.com.wmw.lavenderepda.business.service;


import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ColetaInfosPda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ColetaInfosPdaDbxDao;

public class ColetaInfosPdaService extends CrudService {

    private static ColetaInfosPdaService instance;
    
    private ColetaInfosPdaService() {
        //--
    }
    
    public static ColetaInfosPdaService getInstance() {
        if (instance == null) {
            instance = new ColetaInfosPdaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ColetaInfosPdaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }
    
    public void insertOrUpdateColeta(ColetaInfosPda coleta) throws SQLException {
    	try {
    		insert(coleta);
    	} catch (ValidationException e) {
    		update(coleta);
    	}
    	
    }

}