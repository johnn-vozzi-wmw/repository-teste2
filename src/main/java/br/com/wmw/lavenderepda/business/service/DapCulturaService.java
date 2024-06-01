package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.DapCultura;
import br.com.wmw.lavenderepda.business.domain.DapLaudo;
import br.com.wmw.lavenderepda.business.domain.DapMatricula;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DapCulturaDbxDao;

public class DapCulturaService extends CrudService {
	
	private static DapCulturaService instance;
	
	public static DapCulturaService getInstance() {
		if (instance == null) {
			instance = new DapCulturaService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return DapCulturaDbxDao.getInstance();
	}

	public boolean isNuSequenciaValido(DapCultura dapCultura, DapMatricula dapMatricula) throws SQLException {
		DapLaudo dapLaudo = new DapLaudo();
		dapLaudo.cdEmpresa = dapCultura.cdEmpresa;
		dapLaudo.cdSafra = dapCultura.cdSafra;
		dapLaudo.cdCliente = dapCultura.cdCliente;
		dapLaudo.cdDapMatricula = dapCultura.cdDapMatricula;
		dapLaudo.cdDapCultura = dapCultura.cdDapCultura;
		int nextNuSeqLaudo = DapLaudoService.getInstance().findMaxKey(dapLaudo, "NUSEQLAUDO") + 1;
		return nextNuSeqLaudo <= dapMatricula.nuMaxSeqLaudo;
	}

}
