package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import br.com.wmw.lavenderepda.business.domain.MotivoPendenciaJust;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotivoPendenciaJustDbxDao;

import java.sql.SQLException;

public class MotivoPendenciaJustService extends CrudService {

    private static MotivoPendenciaJustService instance;
    
    private MotivoPendenciaJustService() { }
    
    public static MotivoPendenciaJustService getInstance() {
        if (instance == null) {
            instance = new MotivoPendenciaJustService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return MotivoPendenciaJustDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {}

    public MotivoPendenciaJust findByMotivoPendenciaJust(String cdMotivoPendencia, String cdMotivoPendenciaJust) throws SQLException {
        MotivoPendenciaJust motivoPendenciaJustFilter = new MotivoPendenciaJust(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(MotivoPendencia.class), cdMotivoPendencia, cdMotivoPendenciaJust);
        return (MotivoPendenciaJust) findByRowKey(motivoPendenciaJustFilter.getRowKey());
    }
}