package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.FornecedorRep;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.FornecedorPdbxDao;
import totalcross.util.Vector;

public class FornecedorService extends CrudService {

    private static FornecedorService instance;

    private FornecedorService() {
        //--
    }

    public static FornecedorService getInstance() {
        if (instance == null) {
            instance = new FornecedorService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return FornecedorPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector getProdutosAtFornecedor(Vector listProdutos, String cdFornecedor ) {
    	Vector list = new Vector();
    	if (ValueUtil.isNotEmpty(cdFornecedor)) {
    		int size = listProdutos.size();
    		for (int i = 0; i < size; i++) {
    			ProdutoBase produto = (ProdutoBase)listProdutos.items[i];
    			if (cdFornecedor.equals(produto.cdFornecedor)) {
    				list.addElement(produto);
    			}
    		}
    		return list;
    	} else {
    		return listProdutos;
    	}
    }

	public String getDsFornecedor(String cdEmpresa, String cdFornecedor) throws SQLException {
		if (!ValueUtil.isEmpty(cdEmpresa) && !ValueUtil.isEmpty(cdFornecedor)) {
			Fornecedor fornecedorFilter = new Fornecedor();
			fornecedorFilter.cdEmpresa = cdEmpresa;
			fornecedorFilter.cdFornecedor = cdFornecedor;
			fornecedorFilter = (Fornecedor) FornecedorService.getInstance().findByRowKey(fornecedorFilter.getRowKey());
			return fornecedorFilter != null ? fornecedorFilter.toString() : cdFornecedor;
		} else {
			return cdFornecedor;
		}
	}
	
	public Vector findAllForcedorAssociadoRep(Fornecedor filter) throws SQLException {
		if (FornecedorRepService.getInstance().possuiFornRep()) {
			filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(FornecedorRep.class);
		}
		return findAllByExample(filter);
	}
}