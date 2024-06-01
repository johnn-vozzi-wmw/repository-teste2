package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ProdutoAtributo;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoAtributoPdbxDao;
import totalcross.util.Vector;

public class ProdutoAtributoService extends CrudService {

    private static ProdutoAtributoService instance;

    private ProdutoAtributoService() {
        //--
    }

    public static ProdutoAtributoService getInstance() {
        if (instance == null) {
            instance = new ProdutoAtributoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoAtributoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector findProdutosByAtributos(String cdAtributo, String cdAtributoOpcaoProd, Vector listProdutos) throws SQLException {
    	Vector result = new Vector();
    	ProdutoAtributo produtoAtributoFilter = new ProdutoAtributo();
    	produtoAtributoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	produtoAtributoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	produtoAtributoFilter.cdAtributoProduto = cdAtributo;
    	produtoAtributoFilter.cdAtributoOpcaoProduto = cdAtributoOpcaoProd;
    	Vector produtoAtributoList = ProdutoAtributoPdbxDao.getInstance().findCdProdutosByExample(produtoAtributoFilter);
    	int size = listProdutos.size();
    	for (int i = 0; i < size; i++) {
    		ProdutoBase produto = (ProdutoBase)listProdutos.items[i];
    		if (produtoAtributoList.indexOf(produto.cdProduto) != -1) {
    			result.addElement(produto);
    		}
    	}
    	produtoAtributoList = null;
    	return result;
    }

}