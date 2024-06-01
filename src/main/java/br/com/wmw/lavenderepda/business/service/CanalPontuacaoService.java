package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CanalPontuacaoPdbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SegmentoPontuacaoPdbxDao;
import totalcross.util.Vector;

public class CanalPontuacaoService extends CrudService {

	private static CanalPontuacaoService instance;

	private CanalPontuacaoService() {}
	@Override public void validate(BaseDomain domain) throws java.sql.SQLException {}
	@Override protected CrudDao getCrudDao() { return SegmentoPontuacaoPdbxDao.getInstance(); }
	public static CanalPontuacaoService getInstance() { return instance == null ? instance = new CanalPontuacaoService() : instance; }

	public Vector findAllCanalInPontExtPed() throws SQLException {
		return CanalPontuacaoPdbxDao.getInstance().findAllCanalInPontExtPed();
	}

}