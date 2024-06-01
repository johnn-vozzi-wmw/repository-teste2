package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.sql.Types;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CatalogoExterno;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CatalogoExternoDbxDao;
import totalcross.util.Vector;

public class CatalogoExternoService extends CrudService {

	private static CatalogoExternoService instance;

	public static CatalogoExternoService getInstance() {
		if (instance == null) {
			instance = new CatalogoExternoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return CatalogoExternoDbxDao.getInstance();
	}

	public Vector findAllNaoAlterados() throws SQLException {
		return CatalogoExternoDbxDao.getInstance().findAllNaoAlterados(new CatalogoExterno());
	}

	public Vector findAllCatalogosBaixados() throws SQLException {
		CatalogoExterno catalogoExterno = new CatalogoExterno();
		catalogoExterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		catalogoExterno.filtraFlTipoAlteracaoNotNull = true;
		return CatalogoExternoDbxDao.getInstance().findAllByExample(catalogoExterno);
	}

	public void updateFlTipoAlteracaoCatalogoBaixado(CatalogoExterno catalogoExterno) throws SQLException {
		updateColumn(catalogoExterno.rowKey, "FLTIPOALTERACAO", BaseDomain.FLTIPOALTERACAO_INSERIDO, Types.VARCHAR);
	}

}
