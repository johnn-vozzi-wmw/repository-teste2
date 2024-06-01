package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AreaVendaCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AreaVendaClientePdbxDao;

public class AreaVendaClienteService extends CrudService {

    private static AreaVendaClienteService instance;

    private AreaVendaClienteService() {
    }

    public static AreaVendaClienteService getInstance() {
        if (instance == null) {
            instance = new AreaVendaClienteService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AreaVendaClientePdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public String[] findCdsAreasVendaCliente(String cdCliente) throws SQLException {
    	AreaVendaCliente areaVendaClienteFilter = new AreaVendaCliente();
    	areaVendaClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	areaVendaClienteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(AreaVendaCliente.class);
    	areaVendaClienteFilter.cdCliente = cdCliente;
    	String[] cdsAreaVenda = AreaVendaClientePdbxDao.getInstance().findCdsAreaVendaByExample(areaVendaClienteFilter);
		return cdsAreaVenda != null ? cdsAreaVenda : new String[]{};
    }

}