package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RequisicaoServMotivoDbxDao;
import totalcross.util.Vector;

public class RequisicaoServMotivoService extends CrudService {
	
	private static RequisicaoServMotivoService instance;

	public static RequisicaoServMotivoService getInstance() {
		if (instance == null) {
			instance = new RequisicaoServMotivoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		
	}

	@Override
	protected CrudDao getCrudDao() {
		return RequisicaoServMotivoDbxDao.getInstance();
	}

	public Vector findAllByUsoMotivo(String cdTipo) throws SQLException {
		return RequisicaoServMotivoDbxDao.getInstance().findAllByUsoMotivo(cdTipo);
	}

}
