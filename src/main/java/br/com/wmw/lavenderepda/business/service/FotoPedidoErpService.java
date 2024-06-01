package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FotoPedidoErpDbxDao;

public class FotoPedidoErpService extends CrudService {

    private static FotoPedidoErpService instance = null;
    
    private FotoPedidoErpService() {
        //--
    }
    
    public static FotoPedidoErpService getInstance() {
        if (instance == null) {
            instance = new FotoPedidoErpService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FotoPedidoErpDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

}