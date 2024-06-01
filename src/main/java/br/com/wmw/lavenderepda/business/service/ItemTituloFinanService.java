package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemTituloFinan;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemTituloFinanPdbxDao;
import totalcross.util.Vector;

public class ItemTituloFinanService extends CrudService {
	
	private static ItemTituloFinanService instance;
	
	public static ItemTituloFinanService getInstance() {
		if (instance == null) {
			instance = new ItemTituloFinanService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) {
	}

	@Override
	protected CrudDao getCrudDao() {
		return new ItemTituloFinanPdbxDao(ItemTituloFinan.TABLE_NAME);
	}
	
	public Vector getItensTituloFinanceiro(BaseDomain filter) throws SQLException {
		return ItemTituloFinanPdbxDao.getInstance().findAllByExample(filter);
	}

}
