package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.domain.SubTipoSac;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SubTipoSacDbxDao;

public class SubTipoSacService extends CrudService {

    private static SubTipoSacService instance;
    
    private SubTipoSacService() {
        //--
    }
    
    public static SubTipoSacService getInstance() {
        if (instance == null) {
            instance = new SubTipoSacService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SubTipoSacDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public String getDsSubTipoSac(Sac sac) throws SQLException {
    	SubTipoSac subTipoSac = new SubTipoSac();
    	subTipoSac.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	subTipoSac.cdTipoSac = sac.cdTipoSac;
    	subTipoSac.cdSubTipoSac = sac.cdSubTipoSac;
    	return findColumnByRowKey(subTipoSac.getRowKey(), "DSSUBTIPOSAC");
    }

}