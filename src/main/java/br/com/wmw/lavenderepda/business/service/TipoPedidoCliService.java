package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoPedidoCliDbxDao;

public class TipoPedidoCliService extends CrudService {

    private static TipoPedidoCliService instance;
    
    private TipoPedidoCliService() {
        //--
    }
    
    public static TipoPedidoCliService getInstance() {
        if (instance == null) {
            instance = new TipoPedidoCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoPedidoCliDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

}