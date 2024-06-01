package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ComboCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ComboClienteDao;

public class ComboClienteService extends CrudService {
	
	private static ComboClienteService instance;
	
	public static ComboClienteService getInstance() {
		if (instance == null) {
			instance = new ComboClienteService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return ComboClienteDao.getInstance();
	}
	
	public boolean isPossuiComboCliente() throws SQLException {
		ComboCliente filter = new ComboCliente();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		return countByExample(filter) > 0;
	}

}
