package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.DescontoVenda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescontoVendaDbxDao;
import totalcross.util.Vector;

public class DescontoVendaService extends CrudService {

    private static DescontoVendaService instance = null;
    
    private DescontoVendaService() {
        
    }
    
    public static DescontoVendaService getInstance() {
        if (instance == null) {
            instance = new DescontoVendaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return DescontoVendaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) { 
    	
    }
    
    public double getVlPctDescontoVenda(String cdEmpresa, String cdUf, double vlVenda) throws SQLException {
    	if (vlVenda <= 0) {
    		return 0;
    	}
    	DescontoVenda descontoVenda = getDescontoVenda(cdEmpresa, cdUf, vlVenda);
    	return descontoVenda != null ? descontoVenda.vlPctDesconto : 0;
    }
    
    public DescontoVenda getDescontoVenda(String cdEmpresa, String cdUf, double vlVenda) throws SQLException {
    	DescontoVenda descontoVendaFilter = getDescontoVendaBaseFilter(cdEmpresa, cdUf);
    	descontoVendaFilter.vlVendaFilter = vlVenda;
    	Vector descontoVendaList = findAllByExample(descontoVendaFilter, DescontoVenda.NM_COLUNA_VLVENDA);
    	if (ValueUtil.isNotEmpty(descontoVendaList)) {
    		return (DescontoVenda) descontoVendaList.items[descontoVendaList.size() - 1];
    	}
    	return null;
    }
    
    public Vector findDescontoVendaByEmpresaUfCliente(String cdEmpresa, String cdUf) throws SQLException {
    	DescontoVenda descontoVendaFilter = getDescontoVendaBaseFilter(cdEmpresa, cdUf);
    	Vector descontoVendaList = findAllByExample(descontoVendaFilter, DescontoVenda.NM_COLUNA_VLVENDA);
    	return descontoVendaList;
    }

	private DescontoVenda getDescontoVendaBaseFilter(String cdEmpresa, String cdUf) {
		DescontoVenda descontoVendaFilter = new DescontoVenda();
    	descontoVendaFilter.cdEmpresa = cdEmpresa;
    	descontoVendaFilter.cdUf = cdUf;
    	return descontoVendaFilter;
	}
    
}