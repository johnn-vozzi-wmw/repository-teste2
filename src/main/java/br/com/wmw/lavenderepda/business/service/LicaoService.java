package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LicaoPdbxDao;

public class LicaoService extends CrudService {
	
	private static LicaoService instance;

	@Override
	public void validate(BaseDomain domain) throws SQLException {
		// TODO Auto-generated method stub
		
	}
	
	public static LicaoService getInstance() {
		if(instance == null) {
			instance = new LicaoService();
		}
		
		return instance;
	}

	@Override
	protected CrudDao getCrudDao() {
		// TODO Auto-generated method stub
		return new LicaoPdbxDao();
	}

}
