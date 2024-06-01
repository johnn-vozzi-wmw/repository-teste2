package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Campanha;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CampanhaPdbxDao;
import totalcross.util.Vector;

public class CampanhaService extends CrudService {

    private static CampanhaService instance;

    private CampanhaService() {
    }

    public static CampanhaService getInstance() {
        if (instance == null) {
            instance = new CampanhaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CampanhaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) {
    }

    public String getDsCampanha(String cdCampanha) throws SQLException {
    	Campanha campanha = new Campanha();
    	campanha.cdCampanha = cdCampanha;
    	campanha.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	campanha.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	campanha.dsCampanha = CampanhaService.getInstance().findColumnByRowKey(campanha.getRowKey(), "DSCAMPANHA");
    	return campanha.toString();
    }

    public Vector findAllCampanhas() throws SQLException {
    	Campanha campanhaFilter = new Campanha();
    	campanhaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	campanhaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	return findAllByExample(campanhaFilter);
    }

}