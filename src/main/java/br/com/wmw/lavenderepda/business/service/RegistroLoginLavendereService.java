package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.service.RegistroLoginService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RegistroLoginLavendereDbxDao;

public class RegistroLoginLavendereService extends RegistroLoginService {

	public static RegistroLoginLavendereService instance;

	private RegistroLoginLavendereService() {

	}

	public static RegistroLoginLavendereService getInstance() {
		if (instance == null) {
			instance = new RegistroLoginLavendereService();
		}
		return instance;
	}

	protected CrudDao getCrudDao() {
		return RegistroLoginLavendereDbxDao.getInstance();
	}

}
