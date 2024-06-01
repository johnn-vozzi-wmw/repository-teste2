package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SugestaoVendaRepDbxDao;
import totalcross.util.Vector;

public class SugestaoVendaRepService extends CrudService {

    private static SugestaoVendaRepService instance;

    private SugestaoVendaRepService() {
        //--
    }

    public static SugestaoVendaRepService getInstance() {
        if (instance == null) {
            instance = new SugestaoVendaRepService();
        }
        return instance;
    }

    protected CrudDao getCrudDao() {
        return SugestaoVendaRepDbxDao.getInstance();
    }

    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public Vector findAllSugestoesVendaRepLogado(String cdEmpresa) throws SQLException {
		SugestaoVendaRep sugestaoVendaRep = new SugestaoVendaRep();
		sugestaoVendaRep.cdEmpresa = ValueUtil.isNotEmpty(cdEmpresa) ? cdEmpresa : SessionLavenderePda.cdEmpresa;
		sugestaoVendaRep.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		return findAllByExample(sugestaoVendaRep);
	}

	public int countSugestoesVendaRepBySugestaoVenda(String cdEmpresa, String cdSugestaoVenda) throws SQLException {
		SugestaoVendaRep sugestaoVendaRep = new SugestaoVendaRep();
		sugestaoVendaRep.cdEmpresa = ValueUtil.isNotEmpty(cdEmpresa) ? cdEmpresa : SessionLavenderePda.cdEmpresa;
		sugestaoVendaRep.cdSugestaoVenda = cdSugestaoVenda;
		return countByExample(sugestaoVendaRep);
	}

}