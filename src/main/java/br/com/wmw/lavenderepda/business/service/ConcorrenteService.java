package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Concorrente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConcorrentePdbxDao;

public class ConcorrenteService extends CrudService {

    private static ConcorrenteService instance;

    private ConcorrenteService() {
        //--
    }

    public static ConcorrenteService getInstance() {
        if (instance == null) {
            instance = new ConcorrenteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ConcorrentePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String getDsConcorrente(String cdConcorrente) throws SQLException {
    	if (cdConcorrente == null) {
    		return ValueUtil.VALOR_NI;
    	}
        Concorrente concorrente = new Concorrente();
        concorrente.cdEmpresa = SessionLavenderePda.cdEmpresa;
        concorrente.cdConcorrente = cdConcorrente;
        concorrente.dsConcorrente = StringUtil.getStringValue(ConcorrenteService.getInstance().findColumnByRowKey(concorrente.getRowKey(), "DSCONCORRENTE"));
    	return concorrente.toString();
    }
}