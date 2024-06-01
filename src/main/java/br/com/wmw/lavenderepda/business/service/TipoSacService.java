package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TipoSac;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoSacDao;

public class TipoSacService extends CrudService {

    private static TipoSacService instance;
    
    private TipoSacService() {
        //--
    }
    
    public static TipoSacService getInstance() {
        if (instance == null) {
            instance = new TipoSacService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoSacDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public String getDsTipoSac(String cdTipoSac) throws SQLException {
    	TipoSac tipoSac = new TipoSac();
    	tipoSac.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	tipoSac.cdTipoSac = cdTipoSac;
    	return findColumnByRowKey(tipoSac.getRowKey(), "DSTIPOSAC");
    }

}