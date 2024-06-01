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
import br.com.wmw.lavenderepda.business.domain.RelNovidadeCli;
import br.com.wmw.lavenderepda.integration.dao.pdbx.RelNovidadeCliDao;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelNovidadeCliService extends CrudService {

    private static RelNovidadeCliService instance = null;
    
    private RelNovidadeCliService() {
        //--
    }
    
    public static RelNovidadeCliService getInstance() {
        if (instance == null) {
            instance = new RelNovidadeCliService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return RelNovidadeCliDao.getInstance();
    }

	@Override
	public void validate(BaseDomain domain) { }
	
	public Vector findRelNovidadeCli(String dsFiltro, int periodoNovidade) throws SQLException {
		return RelNovidadeCliDao.getInstance().getRegistrosPorTipoNovidade(getFiltro(dsFiltro, null, periodoNovidade)); 
	}

	public Vector findAllByExample(String dsFiltro, String cdTipoNovidade, int cdPeriodo) throws java.sql.SQLException {
		RelNovidadeCli relNovidadeCliFilter = getFiltro(dsFiltro, cdTipoNovidade, cdPeriodo);
		return findAllByExample(relNovidadeCliFilter);
	}

	public RelNovidadeCli getFiltro(String dsFiltro, String cdTipoNovidade, int cdPeriodo) {
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
		RelNovidadeCli relNovidadeCliFilter = (RelNovidadeCli) new RelNovidadeCli();
		relNovidadeCliFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		relNovidadeCliFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		relNovidadeCliFilter.dtEmissaoRelatorio = date;
		if (cdTipoNovidade != null) {
			relNovidadeCliFilter.cdTipoNovidade = cdTipoNovidade;
		}
		if (ValueUtil.isNotEmpty(dsFiltro)) {
			relNovidadeCliFilter.dsFiltro = strBuffer.toString();
		}
		relNovidadeCliFilter.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
		return relNovidadeCliFilter;
	}
 
}