package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.domain.SerieNfe;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SerieNfeDao;
import totalcross.sql.Types;

public class SerieNfeService extends CrudService {
	
	private static SerieNfeService instance;
	
	public static SerieNfeService getInstance() {
		if (instance == null) {
			instance = new SerieNfeService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return SerieNfeDao.getInstance();
	}
	
	public void updateNuProximoNumero(String cdEmpresa, String cdRepresentante, int nuSerieNfe, int nuNfe) throws SQLException {
		updateNuProximoNumero(cdEmpresa, cdRepresentante, nuSerieNfe, nuNfe, "1");
	}
	
	public void updateNuProximoNumero(String cdEmpresa, String cdRepresentante, int nuSerieNfe, int nuNfe, String cdTipoNota) throws SQLException {
		try {
			SerieNfe serieNfe = new SerieNfe();
			serieNfe.cdEmpresa = cdEmpresa;
			serieNfe.cdRepresentante = cdRepresentante;
			serieNfe.nuSerieNfe = nuSerieNfe;
			serieNfe.cdTipoNota = cdTipoNota;
			serieNfe.nuProximoNumero = nuNfe + 1;
			updateColumn(serieNfe.getRowKey(), SerieNfe.COLUNA_NUPROXIMONUMERO, serieNfe.nuProximoNumero, Types.INTEGER);
		} catch (ApplicationException e) {
			ExceptionUtil.handle(e);
		}
	}

}
