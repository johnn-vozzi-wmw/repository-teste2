package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AreaVenda;
import br.com.wmw.lavenderepda.integration.dao.pdbx.AreaVendaPdbxDao;
import totalcross.util.Vector;

public class AreaVendaService extends CrudService {

    private static AreaVendaService instance;

    private AreaVendaService() {
    }

    public static AreaVendaService getInstance() {
        if (instance == null) {
            instance = new AreaVendaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return AreaVendaPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findAllByCdCliente(String cdCliente) throws SQLException {
    	String[] listAreaCliente = AreaVendaClienteService.getInstance().findCdsAreasVendaCliente(cdCliente);
    	int size2 = listAreaCliente.length;
    	Vector areaVendalist = new Vector(size2);
    	for (int i=0; i < size2; i++) {
    		AreaVenda area = new AreaVenda();
    		area.cdEmpresa = SessionLavenderePda.cdEmpresa;
    		area.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
    		area.cdAreavenda = listAreaCliente[i];
    		area = (AreaVenda)findByRowKey(area.getRowKey());
    		if (area != null) {
    			areaVendalist.addElement(area);
    		}
    	}
    	return areaVendalist;
    }

}