package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.HistoricoTrocaDevolucaoCli;
import br.com.wmw.lavenderepda.integration.dao.pdbx.HistoricoTrocaDevolucaoCliPdbxDao;
import totalcross.util.Vector;

import java.sql.SQLException;

public class HistoricoTrocaDevolucaoCliService extends CrudPersonLavendereService {

	private static HistoricoTrocaDevolucaoCliService instance;
	private Vector historicoDevolucaoTrocaCliList;

	public static HistoricoTrocaDevolucaoCliService getInstance() {
		if (instance == null) {
			instance = new HistoricoTrocaDevolucaoCliService();
		}
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return HistoricoTrocaDevolucaoCliPdbxDao.getInstance();
	}

	public Vector findAllHistoricoTrocaDevolucaoCli(String cdCliente) throws SQLException {
		HistoricoTrocaDevolucaoCli historicoTrocaDevolucaoCliFilter = new HistoricoTrocaDevolucaoCli();
		historicoTrocaDevolucaoCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		historicoTrocaDevolucaoCliFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		historicoTrocaDevolucaoCliFilter.cdCliente = cdCliente;
		historicoDevolucaoTrocaCliList = HistoricoTrocaDevolucaoCliPdbxDao.getInstance().findAllHistDevolucaoTrocaCli(historicoTrocaDevolucaoCliFilter);
		return historicoDevolucaoTrocaCliList;
	}
}
