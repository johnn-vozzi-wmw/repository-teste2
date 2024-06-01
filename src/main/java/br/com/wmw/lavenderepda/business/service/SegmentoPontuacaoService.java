package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SegmentoPontuacaoPdbxDao;
import totalcross.util.Vector;

public class SegmentoPontuacaoService extends CrudService {

	private static SegmentoPontuacaoService instance;

	private SegmentoPontuacaoService() {}
	@Override public void validate(BaseDomain domain) throws java.sql.SQLException {}
	@Override protected CrudDao getCrudDao() { return SegmentoPontuacaoPdbxDao.getInstance(); }
	public static SegmentoPontuacaoService getInstance() { return instance == null ? instance = new SegmentoPontuacaoService() : instance; }

	public Vector findAllSegmentoInPontExtPed() throws SQLException {
		return SegmentoPontuacaoPdbxDao.getInstance().findAllSegmentoInPontExtPed();
	}

}