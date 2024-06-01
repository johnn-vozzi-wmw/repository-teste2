package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Entrega;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EntregaDbxDao;

public class EntregaService extends CrudService {

    private static EntregaService instance = null;
    
    private EntregaService() {
    }
    
    public static EntregaService getInstance() {
        if (instance == null) {
            instance = new EntregaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return EntregaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public boolean isEntregaVigente(Pedido pedido) throws SQLException {
		Entrega entregaFilter = new Entrega();
		entregaFilter.cdEmpresa = pedido.cdEmpresa;
		entregaFilter.cdRepresentante = pedido.cdRepresentante;
		entregaFilter.cdEntrega = pedido.cdEntrega;
		return EntregaDbxDao.getInstance().findEntregaFechamentoPedido(entregaFilter).size() > 0; 
	}

}