package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConcorrente;
import br.com.wmw.lavenderepda.business.domain.PesquisaMercadoConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PesquisaMercadoConcorrenteDbxDao;

public class PesquisaMercadoConcorrenteService extends CrudService {

	private static PesquisaMercadoConcorrenteService instance;

	private PesquisaMercadoConcorrenteService() {
	}

	public static PesquisaMercadoConcorrenteService getInstance() {
		if (instance == null) {
			instance = new PesquisaMercadoConcorrenteService();
		}
		return instance;
	}


	@Override
	public void validate(BaseDomain domain) throws SQLException {

	}

	@Override
	protected CrudDao getCrudDao() {
		return PesquisaMercadoConcorrenteDbxDao.getInstance();
	}

	public void deleteRegistrosAntigos(PesquisaMercadoConfig pesquisaMercadoConfig) throws SQLException {
		PesquisaMercadoConcorrente pesquisaMercadoConcorrente = new PesquisaMercadoConcorrente();
		pesquisaMercadoConcorrente.cdEmpresa = pesquisaMercadoConfig.cdEmpresa;
		pesquisaMercadoConcorrente.cdPesquisaMercadoConfig = pesquisaMercadoConfig.cdPesquisaMercadoConfig;
		deleteAllByExample(pesquisaMercadoConcorrente);
	}
}
