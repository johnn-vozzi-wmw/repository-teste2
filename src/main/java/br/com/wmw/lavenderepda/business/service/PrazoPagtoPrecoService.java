package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.PrazoPagtoPreco;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PrazoPagtoPrecoDbxDao;

public class PrazoPagtoPrecoService extends CrudService {

    private static PrazoPagtoPrecoService instance;

    private PrazoPagtoPrecoService() {
        //--
    }

    public static PrazoPagtoPrecoService getInstance() {
        if (instance == null) {
            instance = new PrazoPagtoPrecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return PrazoPagtoPrecoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public PrazoPagtoPreco getPrazoPagtoPreco(int cdPrazoPagtoPreco) throws SQLException {
    	PrazoPagtoPreco prazoPagtoPrecoFilter = new PrazoPagtoPreco();
    	prazoPagtoPrecoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	prazoPagtoPrecoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	prazoPagtoPrecoFilter.cdPrazoPagtoPreco = cdPrazoPagtoPreco;
    	return (PrazoPagtoPreco) findByRowKey(prazoPagtoPrecoFilter.getRowKey());
    }


}