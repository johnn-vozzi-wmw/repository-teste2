package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescQuantPesoPrdBlqDbxDao;
import totalcross.util.Vector;

public class DescQuantPesoPrdBlqService extends CrudService {

    private static DescQuantPesoPrdBlqService instance = null;
    
    private DescQuantPesoPrdBlqService() {
        //--
    }
    
    public static DescQuantPesoPrdBlqService getInstance() {
        if (instance == null) {
            instance = new DescQuantPesoPrdBlqService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescQuantPesoPrdBlqDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }
    
    public Vector findProdutosBloqueados(BaseDomain domain) throws SQLException {
    	return DescQuantPesoPrdBlqDbxDao.getInstance().findProdutosBloqueados(domain);
    	
    }
    
}