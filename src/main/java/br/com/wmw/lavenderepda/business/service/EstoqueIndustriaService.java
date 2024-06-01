package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustria;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EstoqueIndustriaDbxDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class EstoqueIndustriaService extends CrudService {

    private static EstoqueIndustriaService instance;
    
    private EstoqueIndustriaService() { }
    
    public static EstoqueIndustriaService getInstance() {
        if (instance == null) {
            instance = new EstoqueIndustriaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return EstoqueIndustriaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }
    
    public Vector findAllNaoVencidos(EstoqueIndustria estoquePrevisto) throws SQLException {
	   	return EstoqueIndustriaDbxDao.getInstance().findAllNaoVencidos(estoquePrevisto);
	}

}