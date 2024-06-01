package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TipoLancamento;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoLancamentoDbxDao;

import java.sql.SQLException;

public class TipoLancamentoService extends CrudService {

	private static TipoLancamentoService instance;

	private TipoLancamentoService() {

	}

	public static TipoLancamentoService getInstance() {
		if (instance == null) {
			instance = new TipoLancamentoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain baseDomain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return TipoLancamentoDbxDao.getInstance();
	}

	public TipoLancamento getFilterByCd(String cdTipoLancamento) {
		TipoLancamento tipoLancamento = new TipoLancamento();
		tipoLancamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
		tipoLancamento.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		tipoLancamento.cdTipoLancamento = cdTipoLancamento;
		return tipoLancamento;
	}
}
