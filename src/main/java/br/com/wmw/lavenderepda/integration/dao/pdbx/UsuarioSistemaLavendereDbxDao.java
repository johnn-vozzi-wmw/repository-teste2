package br.com.wmw.lavenderepda.integration.dao.pdbx;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.integration.dao.UsuarioSistemaDbxDao;
import br.com.wmw.lavenderepda.business.domain.UsuarioSistemaLavendere;

public class UsuarioSistemaLavendereDbxDao extends UsuarioSistemaDbxDao {


	@Override
	protected BaseDomain getBaseDomainDefault() {
		return new UsuarioSistemaLavendere();
	}

	public UsuarioSistemaLavendereDbxDao() {
		super("TBLVPUSUARIOSISTEMA");
	}

    public static UsuarioSistemaDbxDao getInstance() {
        if (instance == null) {
            instance = new UsuarioSistemaLavendereDbxDao();
        }
        return instance;
    }

}