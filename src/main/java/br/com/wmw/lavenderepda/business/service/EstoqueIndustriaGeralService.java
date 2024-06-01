package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.EstoqueIndustriaGeral;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EstoqueIndustriaGeralDbxDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class EstoqueIndustriaGeralService extends CrudService {

    private static EstoqueIndustriaGeralService instance;

    private EstoqueIndustriaGeralService() { }
    
    public static EstoqueIndustriaGeralService getInstance() {
        if (instance == null) {
            instance = new EstoqueIndustriaGeralService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return EstoqueIndustriaGeralDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }
    
    public Vector findAllNaoVencidosSomados(EstoqueIndustriaGeral estoquePrevisto) throws SQLException {
    	return EstoqueIndustriaGeralDbxDao.getInstance().findAllNaoVencidosSomados(estoquePrevisto);
    }

}