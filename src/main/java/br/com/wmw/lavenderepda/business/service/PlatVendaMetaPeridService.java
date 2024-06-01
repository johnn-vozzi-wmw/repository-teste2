package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlatVendaMetaPeridDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PlatVendaMetaPeridService extends CrudService {
	
	private static PlatVendaMetaPeridService instance;

    public static PlatVendaMetaPeridService getInstance() {
        if (instance == null) {
            instance = new PlatVendaMetaPeridService();
        }
        return instance;
    }
    
	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return PlatVendaMetaPeridDbxDao.getInstance();
	}

	public String getDsPeriodoBtData(Date data, String cdRepresentante) throws SQLException {
		return PlatVendaMetaPeridDbxDao.getInstance().findDsPeriodoByDataAndRepresentante(data, cdRepresentante);
	}
	
	public Vector getAllGroupBy() throws SQLException {
		return PlatVendaMetaPeridDbxDao.getInstance().findAllGroupBy();
	}
	
}
