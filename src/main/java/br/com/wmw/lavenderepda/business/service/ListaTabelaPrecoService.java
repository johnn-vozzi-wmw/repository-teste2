package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ListaTabelaPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ListaTabelaPrecoDao;

public class ListaTabelaPrecoService extends CrudService {
	
	private static ListaTabelaPrecoService instance;
	
	public static ListaTabelaPrecoService getInstance() {
		if (instance == null) {
			instance = new ListaTabelaPrecoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return ListaTabelaPrecoDao.getInstance();
	}
	
	public String getDsListaTabelaPreco(String cdEmpresa, String cdRepresentante, int cdListaTabPreco) throws SQLException {
		ListaTabelaPreco filter = new ListaTabelaPreco();
		filter.cdEmpresa = cdEmpresa;
		filter.cdRepresentante = cdRepresentante;
		filter.cdListaTabelaPreco = cdListaTabPreco;
		return findColumnByRowKey(filter.getRowKey(), ListaTabelaPreco.NOME_COLUNA_DSLISTATABELAPRECO);
	}

}
