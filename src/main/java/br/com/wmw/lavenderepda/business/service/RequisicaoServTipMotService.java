package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipMot;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RequisicaoServTipMotDbxDao;
import totalcross.util.Vector;

public class RequisicaoServTipMotService extends CrudService  {
	
    private static RequisicaoServTipMotService instance;

    public static RequisicaoServTipMotService getInstance() {
        if (instance == null) {
            instance = new RequisicaoServTipMotService();
        }
        return instance;
    }

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return RequisicaoServTipMotDbxDao.getInstance();
	}
	
	public Vector findAllTipo(boolean obrigaCliente, boolean obrigaPedido) throws SQLException {
		RequisicaoServTipMot requisicaoServTipMot = new RequisicaoServTipMot();
		requisicaoServTipMot.cdEmpresa = SessionLavenderePda.cdEmpresa;
		requisicaoServTipMot.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RequisicaoServTipMot.class);
		requisicaoServTipMot.obrigaCliente = obrigaCliente;
		requisicaoServTipMot.obrigaPedido = obrigaPedido;
		return RequisicaoServTipMotDbxDao.getInstance().findAllTipo(requisicaoServTipMot);
	}
}
