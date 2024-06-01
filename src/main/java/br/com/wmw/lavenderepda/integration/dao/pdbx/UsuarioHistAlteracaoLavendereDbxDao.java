package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.UsuarioHistAlteracaoDbxDao;
import br.com.wmw.lavenderepda.business.domain.UsuarioHistAlteracaoLavendere;

public class UsuarioHistAlteracaoLavendereDbxDao extends UsuarioHistAlteracaoDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioHistAlteracaoLavendere();
	}

	public UsuarioHistAlteracaoLavendereDbxDao() {
		super(UsuarioHistAlteracaoLavendere.TABLE_NAME);
	}
	
	public static UsuarioHistAlteracaoDbxDao getInstance() {
		if (instance == null) {
			instance = new UsuarioHistAlteracaoLavendereDbxDao();
		}
		return instance;
	}
	
	
}
