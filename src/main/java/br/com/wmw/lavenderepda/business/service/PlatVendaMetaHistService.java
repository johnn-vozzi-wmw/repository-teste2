package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlatVendaMetaHistDbxDao;

public class PlatVendaMetaHistService extends CrudService {
	
	private static PlatVendaMetaHistService instance;
	
    public static PlatVendaMetaHistService getInstance() {
        if (instance == null) {
            instance = new PlatVendaMetaHistService();
        }
        return instance;
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return PlatVendaMetaHistDbxDao.getInstance();
	}

}
