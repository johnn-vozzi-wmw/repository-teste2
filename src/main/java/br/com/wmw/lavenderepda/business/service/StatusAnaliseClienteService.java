package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.StatusAnaliseCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.StatusAnaliseClienteDbxDao;

public class StatusAnaliseClienteService extends CrudService {

    private static StatusAnaliseClienteService instance = null;
    
    private StatusAnaliseClienteService() {
        //--
    }
    
    public static StatusAnaliseClienteService getInstance() {
        if (instance == null) {
            instance = new StatusAnaliseClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return StatusAnaliseClienteDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public String getDsStatusAnaliseCliente(String cdStatusAnalise) throws SQLException {
		StatusAnaliseCliente statusCliente = findStatusAnaliseCliente(cdStatusAnalise);
		return StringUtil.getStringValue(statusCliente);
	}

	private StatusAnaliseCliente findStatusAnaliseCliente(String cdStatusAnalise) throws SQLException {
		StatusAnaliseCliente statusClienteFilter = new StatusAnaliseCliente();
		statusClienteFilter.cdStatusAnalise = cdStatusAnalise;
		statusClienteFilter = (StatusAnaliseCliente) findByPrimaryKey(statusClienteFilter);
		return statusClienteFilter;
	}
	
	public boolean isStatusAnaliseClientePermiteEdicao(String cdStatusAnalise) throws SQLException {
		StatusAnaliseCliente statusCliente = findStatusAnaliseCliente(cdStatusAnalise);
		return statusCliente != null && ValueUtil.VALOR_SIM.equals(statusCliente.flPermiteEdicao) && ValueUtil.VALOR_NAO.equals(statusCliente.flConclusao); 
	}
}