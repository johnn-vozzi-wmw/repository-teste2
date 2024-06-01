package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ResourcesWmw;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ResourcesWmwDbxDao;

public class ResourcesWmwService extends CrudService {

    private static ResourcesWmwService instance;
    
    private ResourcesWmwService() {
        
    }
    
    public static ResourcesWmwService getInstance() {
        if (instance == null) {
            instance = new ResourcesWmwService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ResourcesWmwDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    	
    }
    
    public ResourcesWmw getResourcesWmwRelatorioPdf() throws SQLException {
    	return (ResourcesWmw) findByPrimaryKey(new ResourcesWmw(ResourcesWmw.CHAVE_RELATORIO_PDF));
    }
    
}