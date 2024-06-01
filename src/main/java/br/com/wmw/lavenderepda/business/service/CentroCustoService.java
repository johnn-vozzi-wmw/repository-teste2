package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CentroCusto;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CentroCustoDao;
import totalcross.util.Vector;

public class CentroCustoService extends CrudService {

    private static CentroCustoService instance;

    public static CentroCustoService getInstance() {
        if (instance == null) {
            instance = new CentroCustoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CentroCustoDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain arg0) {
	}
	
	public CentroCusto findCentroCusto(String cdEmpresa, String cdRepresentante, String cdCentroCusto) throws SQLException {
		return findCentroCusto(cdEmpresa, cdRepresentante, cdCentroCusto, null);
	}
	
    public CentroCusto findCentroCusto(String cdEmpresa, String cdRepresentante, String cdCentroCusto, Cliente cliente) throws SQLException {
    	CentroCusto centroCustoFilter = new CentroCusto(cdEmpresa, cdRepresentante, cdCentroCusto);
    	cliente = cliente == null ? SessionLavenderePda.getCliente() : cliente;
    	centroCustoFilter.cdCliente = cliente != null ? cliente.cdCliente : null;
    	centroCustoFilter.ignoreCliente = !PlataformaVendaClienteService.getInstance().hasPlataformaForThisClient(centroCustoFilter.cdEmpresa, cdRepresentante, centroCustoFilter.cdCliente);
    	CentroCusto centroCusto = (CentroCusto)findByPrimaryKey(centroCustoFilter);
    	return centroCusto == null ? new CentroCusto() : centroCusto;
    }	

	public Vector findAllDistinctByExample(CentroCusto centroCustoFilter) throws SQLException {
		return CentroCustoDao.getInstance().findAllDistinctByExample(centroCustoFilter);
	}
	
	public CentroCusto findCentroCustoInCarga(String cdEmpresa, String cdRepresentante, String cdCentroCusto) throws SQLException {
		CentroCusto centroCustoFilter = new CentroCusto(cdEmpresa, cdRepresentante, cdCentroCusto);
		CentroCusto centroCusto = (CentroCusto) findByRowKey(centroCustoFilter.getRowKey());
		return centroCusto == null ? new CentroCusto() : centroCusto;
	}
	

	public Vector findColumnValuesByExampleJoinPlataformaVendaCli(CentroCusto centroCustoFilter, String column) throws SQLException {
		return CentroCustoDao.getInstance().findColumnValuesByExampleJoinPlataformaVendaCli(centroCustoFilter, column);
	}
	
}