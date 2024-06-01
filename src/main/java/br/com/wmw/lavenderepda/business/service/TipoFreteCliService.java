package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoFreteCliDbxDao;

public class TipoFreteCliService extends CrudService {

    private static TipoFreteCliService instance;
    
    private TipoFreteCliService() {
        //--
    }
    
    public static TipoFreteCliService getInstance() {
        if (instance == null) {
            instance = new TipoFreteCliService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return TipoFreteCliDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) throws SQLException {
    	/**/
    }
}