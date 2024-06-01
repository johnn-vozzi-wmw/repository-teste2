package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlataformaVendaClienteDbxDao;

public class PlataformaVendaClienteService extends CrudService {

    private static PlataformaVendaClienteService instance;
    
    private Map<String, Boolean> hashCachePossuiPlataformaCliente = new HashMap<String, Boolean>(); 
    
    private PlataformaVendaClienteService() {
        //--
    }
    
    public static PlataformaVendaClienteService getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaClienteService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PlataformaVendaClienteDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }
    
	public boolean validatePlataformaVendaCliente(Cliente cliente, Pedido pedido) throws SQLException {
		if (cliente == null || pedido == null || cliente.cdEmpresa == null || cliente.cdRepresentante == null || pedido.cdPlataformaVenda == null) return false;
		if (!hasPlataformaForThisClient(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCliente)) return true;
		PlataformaVendaCliente plataformaVendaClienteFilter = new PlataformaVendaCliente();
		plataformaVendaClienteFilter.cdEmpresa = cliente.cdEmpresa;
		plataformaVendaClienteFilter.cdRepresentante = cliente.cdRepresentante;
		plataformaVendaClienteFilter.cdCliente = cliente.cdCliente;
		plataformaVendaClienteFilter.cdPlataformaVenda = pedido.cdPlataformaVenda;
		plataformaVendaClienteFilter.cdCentroCusto = pedido.cdCentroCusto;
		return PlataformaVendaClienteService.getInstance().countByExample(plataformaVendaClienteFilter) > 0;
	}
	
	public boolean hasPlataformaForThisClient(String cdEmpresa, String cdRepresentante, String cdCliente) throws SQLException {
		if (hashCachePossuiPlataformaCliente.containsKey(cdCliente)) return hashCachePossuiPlataformaCliente.get(cdCliente);
		boolean possui = PlataformaVendaClienteDbxDao.getInstance().hasPlataformaForThisClient(cdEmpresa, cdRepresentante, cdCliente);
		hashCachePossuiPlataformaCliente.put(cdCliente, possui);
		return possui;
	}

}