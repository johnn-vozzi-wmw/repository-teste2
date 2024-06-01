package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.LocalEstoque;
import br.com.wmw.lavenderepda.integration.dao.pdbx.LocalEstoqueDbxDao;

public class LocalEstoqueService extends CrudService{

    private static LocalEstoqueService instance;
    
    public LocalEstoqueService() { }
    
    public static LocalEstoqueService getInstance() {
        if (instance == null) {
            instance = new LocalEstoqueService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return LocalEstoqueDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }

	public LocalEstoque findDsLocalEstoque(String cdEmpresa, String cdRepresentante, String cdLocalEstoque) throws SQLException {
		LocalEstoque localEstoqueFilter = new LocalEstoque(cdEmpresa, cdRepresentante, cdLocalEstoque);
		LocalEstoque localEstoqueResult = (LocalEstoque) LocalEstoqueService.getInstance().findByRowKey(localEstoqueFilter.getRowKey());
		return localEstoqueResult;
	}
	
}