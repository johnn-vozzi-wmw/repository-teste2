package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.FornecedorRep;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FornecedorRepDbxDao;

public class FornecedorRepService extends CrudService {

    private static FornecedorRepService instance;
    
    private FornecedorRepService() {
        //--
    }
    
    public static FornecedorRepService getInstance() {
        if (instance == null) {
            instance = new FornecedorRepService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return FornecedorRepDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {

    }
    
    public boolean possuiFornRep() throws SQLException {
    	FornecedorRep filter = new FornecedorRep();
    	filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(FornecedorRep.class);
    	return countByExample(filter) > 0;
    }

}