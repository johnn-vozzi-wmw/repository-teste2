package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemNotaFiscal;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNotaFiscalDao;
import totalcross.util.Vector;

public class ItemNotaFiscalService extends CrudService {
	
	private static ItemNotaFiscalService instance;
	
	public static ItemNotaFiscalService getInstance() {
		if (instance == null) {
			instance = new ItemNotaFiscalService();
	    }
	    return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	@Override
	protected CrudDao getCrudDao() {
		return ItemNotaFiscalDao.getInstance();
	}

	public Vector findItemNotaFiscalList(String cdEmpresa, String cdSerie, String nuNotaFiscal) throws SQLException {
		ItemNotaFiscal itemNotaFiscalFilter = new ItemNotaFiscal(cdEmpresa, cdSerie, nuNotaFiscal);
		itemNotaFiscalFilter.sortAtributte = "NUSEQUENCIAITEM";
		itemNotaFiscalFilter.sortAsc = "S";
		return findAllByExample(itemNotaFiscalFilter);
	}

}
