package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TransportadoraPdbxDao;

public class TransportadoraService extends CrudService {

    private static TransportadoraService instance;

    private TransportadoraService() {
        //--
    }

    public static TransportadoraService getInstance() {
        if (instance == null) {
            instance = new TransportadoraService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TransportadoraPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {

    }

    public Transportadora getTransportadora(String cdTransportadora) throws SQLException {
    	Transportadora transportadora = new Transportadora();
    	transportadora.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	transportadora.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Transportadora.class);
    	transportadora.cdTransportadora = cdTransportadora;
    	return (Transportadora) findByRowKey(transportadora.getRowKey());
    }

	public String findPlacaPor(String cdRepresenante) throws SQLException {
		return TransportadoraPdbxDao.getInstance().findPlacaPor(cdRepresenante);
	}
}