package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PermissaoSolAut;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PermissaoSolAutDbxDao;
import totalcross.util.Vector;

public class PermissaoSolAutService extends CrudService {

    private static PermissaoSolAutService instance;

    private PermissaoSolAutService() {}
	public static PermissaoSolAutService getInstance() { return (instance == null) ? instance = new PermissaoSolAutService() : instance; }

	@Override protected CrudDao getCrudDao() { return PermissaoSolAutDbxDao.getInstance(); }
	@Override public void validate(BaseDomain domain) {}

	public Vector getBySessionUser() throws SQLException {
    	return findAllByExample(getPermissaoFilterBySession());
	}

	public PermissaoSolAut getBySessionUser(final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum) throws SQLException {
    	return getPermission(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdUsuario, tipoSolicitacaoAutorizacaoEnum, LavendereConfig.CDSISTEMALAVENDEREPDA);
	}

	private PermissaoSolAut getPermission(final String cdEmpresa, final String cdUsuarioPermissao, final TipoSolicitacaoAutorizacaoEnum tipoSolicitacaoAutorizacaoEnum, final int cdSistema) throws SQLException {
		return (PermissaoSolAut) findByRowKey(new PermissaoSolAut(cdEmpresa, cdUsuarioPermissao, tipoSolicitacaoAutorizacaoEnum, cdSistema).getRowKey());
	}

	public PermissaoSolAut getPermissaoFilterBySession() {
    	return new PermissaoSolAut(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdUsuario, null, LavendereConfig.CDSISTEMALAVENDEREPDA);
	}

}