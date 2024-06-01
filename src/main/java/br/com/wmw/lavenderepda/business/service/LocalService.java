package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Local;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LocalDbxDao;

public class LocalService extends CrudService {

    private static LocalService instance;
    
    private LocalService() { }
    
    public static LocalService getInstance() {
        if (instance == null) {
            instance = new LocalService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return LocalDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

	public String getDsLocal(String cdEmpresa, String cdRepresentante, String cdLocal) throws SQLException {
		Local localFilter = new Local();
		localFilter.cdEmpresa = cdEmpresa;
		localFilter.cdRepresentante = cdRepresentante;
		localFilter.cdLocal = cdLocal;
		Local localResult = (Local) LocalService.getInstance().findByRowKey(localFilter.getRowKey());
		return localResult != null ? localResult.dsLocal : null;
	}
}