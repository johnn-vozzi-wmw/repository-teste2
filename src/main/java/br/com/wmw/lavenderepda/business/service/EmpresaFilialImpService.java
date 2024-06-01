package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.EmpresaFilialImp;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EmpresaFilialImpDbxDao;

import java.sql.SQLException;

public class EmpresaFilialImpService extends CrudService {

	private static EmpresaFilialImpService instance;

	private EmpresaFilialImpService() {

	}

	public static EmpresaFilialImpService getInstance() {
		if (instance == null) {
			instance = new EmpresaFilialImpService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return EmpresaFilialImpDbxDao.getInstance();
	}

	public EmpresaFilialImp getDefaultFilter() {
		EmpresaFilialImp empresaFilialImp = new EmpresaFilialImp();
		empresaFilialImp.cdEmpresa = SessionLavenderePda.cdEmpresa;
		empresaFilialImp.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		empresaFilialImp.sortAtributte = "cdFilialImp";
		empresaFilialImp.sortAsc = ValueUtil.VALOR_SIM;
		return empresaFilialImp;
	}
}
