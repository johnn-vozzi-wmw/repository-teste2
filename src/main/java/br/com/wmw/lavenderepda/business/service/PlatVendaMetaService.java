package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMeta;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlatVendaMetaDbxDao;
import totalcross.util.Vector;

public class PlatVendaMetaService extends CrudService {
	
    private static PlatVendaMetaService instance;
    
    public static PlatVendaMetaService getInstance() {
        if (instance == null) {
            instance = new PlatVendaMetaService();
        }
        return instance;
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return PlatVendaMetaDbxDao.getInstance();
	}

	public Vector getAllByPeriodo(PlatVendaMeta domain, boolean groupedBy) throws SQLException {
		return PlatVendaMetaDbxDao.getInstance().findAllByPeriodo(domain, groupedBy);
	}
	
	public PlatVendaMeta getPlatVendaMeta(int diasAviso, int diasBloqueio) throws SQLException {
		return PlatVendaMetaDbxDao.getInstance().getPlatVendaMeta(diasAviso, diasBloqueio);
	}
	
}
