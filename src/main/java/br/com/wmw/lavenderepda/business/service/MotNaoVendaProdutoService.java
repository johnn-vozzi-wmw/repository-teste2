package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.MotNaoVendaProdutoDao;

public class MotNaoVendaProdutoService extends CrudService {

    private static MotNaoVendaProdutoService instance;
    
    private MotNaoVendaProdutoService() {

    }
    
    public static MotNaoVendaProdutoService getInstance() {
        if (instance == null) {
            instance = new MotNaoVendaProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return MotNaoVendaProdutoDao.getInstance();
    }

	//@Override
	public void validate(BaseDomain domain) throws SQLException {}


}