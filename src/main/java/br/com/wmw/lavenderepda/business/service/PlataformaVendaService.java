package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.PlataformaVenda;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaCliente;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlataformaVendaDbxDao;
import totalcross.util.Vector;

public class PlataformaVendaService extends CrudService {

    private static PlataformaVendaService instance;
    
    private PlataformaVendaService() {
        //--
    }
    
    public static PlataformaVendaService getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaService();
        }
        return instance;
    }
    
    @Override
    protected CrudDao getCrudDao() {
        return PlataformaVendaDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {    }
    
    public Vector getListPlataformaVendaByPlataformaVendaCliente(PlataformaVendaCliente filter) throws SQLException {
    	return PlataformaVendaDbxDao.getInstance().findAllPlataformaVendaByPlataformaVendaCliente(filter);
    }

    public Vector getDistinctiListPlataformaVendaByPlataformaVendaCliente(PlataformaVendaCliente filter) throws SQLException {
    	return PlataformaVendaDbxDao.getInstance().findAllDistinctPlataformaVendaByPlataformaVendaCliente(filter);
    }
    
    public PlataformaVenda findPlataformaVenda(String cdEmpresa, String cdPlataformaVenda) throws SQLException {
    	PlataformaVenda plataformaVendaFilter = new PlataformaVenda();
    	plataformaVendaFilter.cdEmpresa = cdEmpresa;
    	plataformaVendaFilter.cdPlataformaVenda = cdPlataformaVenda;
    	PlataformaVenda plataformaVenda = (PlataformaVenda)findByPrimaryKey(plataformaVendaFilter);
    	return plataformaVenda != null ? plataformaVenda : new PlataformaVenda();
    }
    
}