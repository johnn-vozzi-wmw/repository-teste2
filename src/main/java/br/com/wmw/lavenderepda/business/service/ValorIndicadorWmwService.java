package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.business.domain.ValorIndicadorWmw;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ValorIndicadorWmwDbxDao;
import totalcross.util.Vector;

public class ValorIndicadorWmwService extends CrudPersonLavendereService {

    private static ValorIndicadorWmwService instance;
    
    private ValorIndicadorWmwService() {
        //--
    }
    
    public static ValorIndicadorWmwService getInstance() {
        if (instance == null) {
            instance = new ValorIndicadorWmwService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return ValorIndicadorWmwDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }
    
    public Vector findDistinctPeriodoList() throws SQLException {
		return findDistinctPeriodoList(null);
	}

	public Vector findDistinctPeriodoList(String cdIndicador) throws SQLException {
		ValorIndicadorWmw valorIndicadorWmwFilter = new ValorIndicadorWmw();
		valorIndicadorWmwFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		valorIndicadorWmwFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ValorIndicadorWmw.class);
		valorIndicadorWmwFilter.cdIndicador = cdIndicador;
		return ValorIndicadorWmwDbxDao.getInstance().findDistinctPeriodoList(valorIndicadorWmwFilter);
	}

	public Vector findAllValorIndicador(String dsPeriodo, String cdIndicador) throws SQLException {
		ValorIndicador valorIndicadorFilter = new ValorIndicador(SessionLavenderePda.cdEmpresa, null, cdIndicador, dsPeriodo);
		return ValorIndicadorWmwDbxDao.getInstance().findAllValorIndicador(valorIndicadorFilter); 
	}

	public Vector findAllValorIndicadorByRep(String dsPeriodo, String cdRep) throws SQLException {
		ValorIndicador valorIndicadorFilter = new ValorIndicador(SessionLavenderePda.cdEmpresa, cdRep, null, dsPeriodo);
		return ValorIndicadorWmwDbxDao.getInstance().findAllValorIndicador(valorIndicadorFilter); 
	}

	public ValorIndicadorWmw findValorIndicador(String cdIndicador, String dsApuracao) throws SQLException {
		ValorIndicadorWmw valorIndicadorWmwFilter = new ValorIndicadorWmw();
		valorIndicadorWmwFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		valorIndicadorWmwFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(ValorIndicadorWmw.class);
		valorIndicadorWmwFilter.cdIndicador = cdIndicador;
		valorIndicadorWmwFilter.dsApuracao = dsApuracao;
		Vector valorIndicadorWmwList = findAllByExampleDyn(valorIndicadorWmwFilter);
		return ValueUtil.isNotEmpty(valorIndicadorWmwList) ? (ValorIndicadorWmw) valorIndicadorWmwList.items[0] : new ValorIndicadorWmw();
	}
	
}