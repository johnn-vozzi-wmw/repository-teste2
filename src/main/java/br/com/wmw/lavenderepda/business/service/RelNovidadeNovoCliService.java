package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PeriodoNovidade;
import br.com.wmw.lavenderepda.business.domain.RelNovidadeNovoCli;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelNovidadeNovoCliDbxDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelNovidadeNovoCliService extends CrudService {
	
	private static RelNovidadeNovoCliService instance;
	
	private RelNovidadeNovoCliService() {
		//--
	}
	
	public static RelNovidadeNovoCliService getInstance() {
		if (instance == null) {
			instance = new RelNovidadeNovoCliService();
		}
		return instance;
	}
	
	@Override
	protected CrudDao getCrudDao() {
		return RelNovidadeNovoCliDbxDao.getInstance();
	}
	
	@Override
	public void validate(BaseDomain domain) { }
	
	public Vector findRelNovidadeNovoCli(String dsFiltro, int periodoNovidade) throws SQLException {
		return RelNovidadeNovoCliDbxDao.getInstance().getRegistrosPorTipoNovidade(getFiltro(dsFiltro, null, periodoNovidade)); 
	}
	
	public Vector findAllByExample(String dsFiltro, String cdTipoNovidadeNovo, int cdPeriodo) throws java.sql.SQLException {
		RelNovidadeNovoCli relNovidadeNovoCli = getFiltro(dsFiltro, cdTipoNovidadeNovo, cdPeriodo);
		return findAllByExample(relNovidadeNovoCli);
	}
	
	public RelNovidadeNovoCli getFiltro(String dsFiltro, String cdTipoNovidade, int cdPeriodo) {
		boolean searchStartsWith = false;
		if (LavenderePdaConfig.usaPesquisaInicioString) {
			if (dsFiltro.startsWith("*")) {
				dsFiltro = dsFiltro.substring(1);
			} else {
				searchStartsWith = true;
			}
		}
		StringBuffer strBuffer = new StringBuffer();
		if (!searchStartsWith) {
			strBuffer.append("%");
		}
		strBuffer.append(dsFiltro);
		strBuffer.append("%");
		Date date = null;
		if (PeriodoNovidade.PERIODO_NOVIDADE_CDHOJE == cdPeriodo) {
			date = DateUtil.getCurrentDate();
			date.advance(-1);
		}
		if (PeriodoNovidade.PERIODO_NOVIDADE_CDPARAMETRO == cdPeriodo) {
			date = DateUtil.getCurrentDate();
			date.advance(-LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto - 1);
		}
		RelNovidadeNovoCli relNovidadeNovoCli = (RelNovidadeNovoCli) new RelNovidadeNovoCli();
		relNovidadeNovoCli.cdEmpresa = SessionLavenderePda.cdEmpresa;
		relNovidadeNovoCli.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		relNovidadeNovoCli.dtGeracao = date;
		if (cdTipoNovidade != null) {
			relNovidadeNovoCli.cdTipoNovidade = cdTipoNovidade;
		}
		if (ValueUtil.isNotEmpty(dsFiltro)) {
			relNovidadeNovoCli.cdNovoCliente = strBuffer.toString();
			relNovidadeNovoCli.nmRazaoSocial = strBuffer.toString();
		}
		relNovidadeNovoCli.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
		return relNovidadeNovoCli;
	}
	
}