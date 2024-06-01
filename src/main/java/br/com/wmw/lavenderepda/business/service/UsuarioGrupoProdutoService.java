package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.UsuarioGrupoProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.UsuarioGrupoProdutoDbxDao;

public class UsuarioGrupoProdutoService extends CrudService {

    private static UsuarioGrupoProdutoService instance;

    private UsuarioGrupoProdutoService() {}
	public static UsuarioGrupoProdutoService getInstance() { return (instance == null) ? instance = new UsuarioGrupoProdutoService() : instance; }

	@Override protected CrudDao getCrudDao() { return UsuarioGrupoProdutoDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

	public boolean sessionUserCanAutorize(final String cdGrupoProduto1) throws SQLException {
    	return userCanAutorize(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdUsuario, cdGrupoProduto1);
	}

	public boolean userCanAutorize(final String cdEmpresa, final String cdUSuario, final String cdGrupoProduto1) throws SQLException {
		return countByExample(new UsuarioGrupoProduto(cdEmpresa, cdUSuario, cdGrupoProduto1)) > 0;
	}

}