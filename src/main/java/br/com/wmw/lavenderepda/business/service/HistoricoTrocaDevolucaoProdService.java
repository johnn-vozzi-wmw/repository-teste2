package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.HistoricoTrocaDevolucaoProd;
import br.com.wmw.lavenderepda.integration.dao.pdbx.HistoricoTrocaDevolucaoProdPdbxDao;

import java.sql.SQLException;

public class HistoricoTrocaDevolucaoProdService extends CrudPersonLavendereService {

	private static HistoricoTrocaDevolucaoProdService instance;

	public static HistoricoTrocaDevolucaoProdService getInstance() {
		if (instance == null) {
			instance = new HistoricoTrocaDevolucaoProdService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return HistoricoTrocaDevolucaoProdPdbxDao.getInstance();
	}

	public HistoricoTrocaDevolucaoProd findByPrimaryKey(String cdCliente, String cdProduto) throws SQLException {
		HistoricoTrocaDevolucaoProd historicoTrocaDevolucaoProdFilter = new HistoricoTrocaDevolucaoProd();
		historicoTrocaDevolucaoProdFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		historicoTrocaDevolucaoProdFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		historicoTrocaDevolucaoProdFilter.cdCliente = cdCliente;
		historicoTrocaDevolucaoProdFilter.cdProduto = cdProduto;
		historicoTrocaDevolucaoProdFilter= HistoricoTrocaDevolucaoProdPdbxDao.getInstance().findByPrimaryKey(historicoTrocaDevolucaoProdFilter);
		return historicoTrocaDevolucaoProdFilter;
	}
}
