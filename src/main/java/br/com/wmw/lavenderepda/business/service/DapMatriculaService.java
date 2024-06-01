package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DapMatriculaDbxDao;
import totalcross.util.Date;

public class DapMatriculaService extends CrudService {
	
	private static DapMatriculaService instance;
	
	public static DapMatriculaService getInstance() {
		if (instance == null) {
			instance = new DapMatriculaService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException { }

	@Override
	protected CrudDao getCrudDao() {
		return DapMatriculaDbxDao.getInstance();
	}

	public boolean isMatriculaInvalida(Date dtValidade) {
		return dtValidade != null && DateUtil.isAfter(DateUtil.getCurrentDate(), dtValidade);
	}

}
