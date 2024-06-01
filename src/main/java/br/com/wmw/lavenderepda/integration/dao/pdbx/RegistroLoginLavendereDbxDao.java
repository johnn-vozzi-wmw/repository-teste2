package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.RegistroLoginDbxDao;
import br.com.wmw.lavenderepda.business.domain.RegistroLoginLavendere;

public class RegistroLoginLavendereDbxDao extends RegistroLoginDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new RegistroLoginLavendere();
	}

	public RegistroLoginLavendereDbxDao() {
		super(RegistroLoginLavendere.TABLE_NAME);
	}

	public static RegistroLoginDbxDao getInstance() {
		if (instance == null) {
			instance = new RegistroLoginLavendereDbxDao();
		}
		return instance;
	}

}
