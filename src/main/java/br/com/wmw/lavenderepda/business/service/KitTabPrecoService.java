package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.KitTabPrecoPdbxDao;
import totalcross.util.Vector;

public class KitTabPrecoService extends CrudService {

    private static KitTabPrecoService instance;

    private KitTabPrecoService() {
        //--
    }

    public static KitTabPrecoService getInstance() {
        if (instance == null) {
            instance = new KitTabPrecoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return KitTabPrecoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public Vector findAllCdTabelaPreco(String cdKit) throws SQLException {
		return KitTabPrecoPdbxDao.getInstance().findAllCdTabelaPreco(cdKit);
	}
    
    

}