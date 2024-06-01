package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ColecaoStatus;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ColecaoStatusDbxDao;
import totalcross.util.Vector;

public class ColecaoStatusService extends CrudService {

    private static ColecaoStatusService instance;
    
    private ColecaoStatusService() {
        //--
    }
    
    public static ColecaoStatusService getInstance() {
        if (instance == null) {
            instance = new ColecaoStatusService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ColecaoStatusDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {  }
    
    public Vector findAllColecaoStatus(ColecaoStatus colecaoStatus) throws SQLException {
    	return ColecaoStatusDbxDao.getInstance().findAllColecaoStatus(colecaoStatus);
    }

}