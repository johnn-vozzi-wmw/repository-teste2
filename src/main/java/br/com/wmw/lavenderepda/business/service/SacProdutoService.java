package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.domain.SacProduto;
import br.com.wmw.lavenderepda.integration.dao.pdbx.SacDao;
import totalcross.util.Vector;

public class SacProdutoService extends CrudService {

    private static SacProdutoService instance;
    
    private SacProdutoService() {
        //--
    }
    
    public static SacProdutoService getInstance() {
        if (instance == null) {
            instance = new SacProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return SacDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
	
	public Vector getListProdutoSac(Sac sacFilter) throws SQLException {
		SacProduto sacProd = new SacProduto();
		sacProd.cdEmpresa = sacFilter.cdEmpresa;
		sacProd.cdSac = sacFilter.cdSac;
		return SacProdutoService.getInstance().findAllByExample(sacProd);
	}
    
}