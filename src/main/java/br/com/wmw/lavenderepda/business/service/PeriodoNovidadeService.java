package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PeriodoNovidade;
import totalcross.util.Vector;

public class PeriodoNovidadeService extends CrudService{

	private static PeriodoNovidadeService instance;

	//@Override
	public static PeriodoNovidadeService getInstance() {
	       if (instance == null) {
	           instance = new PeriodoNovidadeService();
	       }
	       return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return null;
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	//@Override
	public Vector findAll() throws java.sql.SQLException {
		Vector periodoNovidadeList = new Vector();
		if (LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto != 0) {
			PeriodoNovidade periodoNovidade = new PeriodoNovidade();
			periodoNovidade.cdPeriodoNovidade = PeriodoNovidade.PERIODO_NOVIDADE_CDPARAMETRO;
			periodoNovidade.dsPeriodoNovidade = MessageUtil.getMessage(Messages.RELNOVIDADE_COMBO_OPC_PARAMETRO, LavenderePdaConfig.nuDiasPermanenciaNovidadesProduto);
			periodoNovidadeList.addElement(periodoNovidade);
		} else {
			PeriodoNovidade periodoNovidade = new PeriodoNovidade();
			periodoNovidade.cdPeriodoNovidade = PeriodoNovidade.PERIODO_NOVIDADE_CDTODOS;
			periodoNovidade.dsPeriodoNovidade = PeriodoNovidade.PERIODO_NOVIDADE_NMTODOS;
			periodoNovidadeList.addElement(periodoNovidade);
		}
		//--
		PeriodoNovidade periodoNovidade = new PeriodoNovidade();
		periodoNovidade.cdPeriodoNovidade = PeriodoNovidade.PERIODO_NOVIDADE_CDHOJE;
		periodoNovidade.dsPeriodoNovidade = PeriodoNovidade.PERIODO_NOVIDADE_NMHOJE;
		periodoNovidadeList.addElement(periodoNovidade);
		//--
		return periodoNovidadeList;
	}
}
