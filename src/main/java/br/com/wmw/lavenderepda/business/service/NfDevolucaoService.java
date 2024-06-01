package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.NfDevolucaoDao;
import totalcross.util.Vector;

public class NfDevolucaoService extends CrudPersonLavendereService {
	
	private static NfDevolucaoService instance;
	
	private NfDevolucaoService() {
    }
	
	public static NfDevolucaoService getInstance() {
		if (instance == null) {
			instance = new NfDevolucaoService();
	    }
	    return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		return NfDevolucaoDao.getInstance();
	}

	public boolean hasNotaDevolucaoPendente(NfDevolucao nfDevolucaoPendentePendente) throws SQLException {
		Vector nfDevolucaoItens = NfDevolucaoService.getInstance().findAllByExample(nfDevolucaoPendentePendente);
		return ValueUtil.isNotEmpty(nfDevolucaoItens);
	}	
}
