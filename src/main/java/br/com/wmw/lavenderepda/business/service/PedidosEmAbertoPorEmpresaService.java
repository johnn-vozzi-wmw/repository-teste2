package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidosEmAbertoPorEmpresaDbxDao;;

public class PedidosEmAbertoPorEmpresaService extends CrudService {

    private static PedidosEmAbertoPorEmpresaService instance;
    
    private PedidosEmAbertoPorEmpresaService() {
        //--
    }
    
    public static PedidosEmAbertoPorEmpresaService getInstance() {
        if (instance == null) {
            instance = new PedidosEmAbertoPorEmpresaService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PedidosEmAbertoPorEmpresaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {

    }

}