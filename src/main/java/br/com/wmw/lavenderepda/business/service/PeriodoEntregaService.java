package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PeriodoEntregaDbxDao;
import totalcross.util.Vector;

public class PeriodoEntregaService extends CrudService {

    private static PeriodoEntregaService instance = null;
    
    private PeriodoEntregaService() {
    }
    
    public static PeriodoEntregaService getInstance() {
        if (instance == null) {
            instance = new PeriodoEntregaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PeriodoEntregaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public Vector findAllPeriodoEntrega(String cdPeriodoEntrega) throws SQLException {
		return PeriodoEntregaDbxDao.getInstance().findAllPeriodoEntrega(cdPeriodoEntrega);
	}
	
	public Vector findPeriodoEntregaCliEndereco(Vector list) throws SQLException {
		return PeriodoEntregaDbxDao.getInstance().findPeriodoEntregaCliEndereco(list);
	}

}