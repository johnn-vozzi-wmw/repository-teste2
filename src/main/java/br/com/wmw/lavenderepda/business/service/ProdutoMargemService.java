package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoMargem;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoMargemDbxDao;

public class ProdutoMargemService extends CrudService {

    private static ProdutoMargemService instance;
    
    private ProdutoMargemService() {
        //--
    }
    
    public static ProdutoMargemService getInstance() {
        if (instance == null) {
            instance = new ProdutoMargemService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoMargemDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    	//--
    }
    
    public ProdutoMargem getProdutoMargem(final Produto produto, String cdRamoAtividade) throws SQLException {
    	if (produto != null && cdRamoAtividade != null) {
    		ProdutoMargem produtoMargemFilter = new ProdutoMargem();
    		produtoMargemFilter.cdEmpresa = produto.cdEmpresa;
    		produtoMargemFilter.cdRepresentante = produto.cdRepresentante;
    		produtoMargemFilter.cdProduto = produto.cdProduto;
    		produtoMargemFilter.cdRamoAtividade = cdRamoAtividade;
    		return (ProdutoMargem) findByRowKey(produtoMargemFilter.getRowKey());
    	}
    	return null;
    }

}