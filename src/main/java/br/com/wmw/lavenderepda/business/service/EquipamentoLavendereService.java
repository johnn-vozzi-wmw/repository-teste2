package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.service.EquipamentoService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.EquipamentoLavendere;
import br.com.wmw.lavenderepda.business.domain.EquipamentoUsuarioLavendere;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EquipamentoLavendereDbxDao;
import totalcross.util.Vector;

public class EquipamentoLavendereService extends EquipamentoService {

    private static EquipamentoLavendereService instance;
    
    private EquipamentoLavendereService() {
    }
    
    public static EquipamentoLavendereService getInstance() {
        if (instance == null) {
            instance = new EquipamentoLavendereService();
        }
        return instance;
    }
	
	public Vector getConfigIntegWebTcList() throws SQLException {
		Vector tableList = new Vector();
		tableList.addElement(ConfigIntegWebTcService.getInstance().findByRowKey(EquipamentoLavendere.TABLE_NAME + ";"));
		tableList.addElement(ConfigIntegWebTcService.getInstance().findByRowKey(EquipamentoUsuarioLavendere.TABLE_NAME + ";"));
		return tableList;
	}
	
	@Override
	protected CrudDao getCrudDao() {
		return EquipamentoLavendereDbxDao.getInstance();
	}
	
}