package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemNfDevolucao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfDevolucaoPdbxDao;
import totalcross.util.Vector;

public class ItemNfDevolucaoService extends CrudService {
	
	private static ItemNfDevolucaoService instance;
	
	private ItemNfDevolucaoService() {
    }

	public static ItemNfDevolucaoService getInstance() {
		if (instance == null) {
			instance = new ItemNfDevolucaoService();
		}
		return instance;
	}
	
	@Override
	public void validate(BaseDomain domain) {
	}

	@Override
	protected CrudDao getCrudDao() {
		return new ItemNfDevolucaoPdbxDao(ItemNfDevolucao.TABLE_NAME);
	}
	
	public Vector getItensTituloFinanceiro(BaseDomain filter) throws SQLException {
		return ItemNfDevolucaoPdbxDao.getInstance().findAllByExample(filter);
	}

}
