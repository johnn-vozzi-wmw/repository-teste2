package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TabPrecTipoPedidoDbxDao;

public class TabPrecTipoPedidoService extends CrudService {

    private static TabPrecTipoPedidoService instance;
    
    private TabPrecTipoPedidoService() {
        //--
    }
    
    public static TabPrecTipoPedidoService getInstance() {
        if (instance == null) {
            instance = new TabPrecTipoPedidoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return TabPrecTipoPedidoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

}