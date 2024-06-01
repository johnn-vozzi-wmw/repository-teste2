package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.NotaFiscal;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NotaFiscalDao;
import totalcross.util.Vector;

public class NotaFiscalService extends CrudPersonLavendereService {
	
	private static NotaFiscalService instance;
	
	public static NotaFiscalService getInstance() {
		if (instance == null) {
			instance = new NotaFiscalService();
	    }
	    return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	@Override
	protected CrudDao getCrudDao() {
		return NotaFiscalDao.getInstance();
	}

	public Vector findNotaFiscalList(String cdEmpresa, String cdRepresentante, String nuPedido) throws SQLException {
		NotaFiscal notaFiscalFilter = new NotaFiscal(cdEmpresa, cdRepresentante, nuPedido);
		return findAllByExample(notaFiscalFilter);
	}

}
