package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.integration.dao.pdbx.CestaProdutoPdbxDao;
import totalcross.util.Vector;

public class CestaProdutoService extends CrudService {

    private static CestaProdutoService instance;

    private CestaProdutoService() {
        //--
    }

    public static CestaProdutoService getInstance() {
        if (instance == null) {
            instance = new CestaProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return CestaProdutoPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public Vector getProdutosInCesta(ProdutoBase domainFilter, String dsFiltro, boolean filterByPrincipioAtivo, String cdFornecedor, boolean onlyStartString) throws SQLException {
    	dsFiltro = dsFiltro.toUpperCase();
    	String prefixo = onlyStartString ? ValueUtil.VALOR_NI : "%";
    	
    	if (ValueUtil.isNotEmpty(dsFiltro)) {
	    	if (!filterByPrincipioAtivo) {
		    	if (!LavenderePdaConfig.usaFiltroSomenteDescricaoProduto) {
		    		if (LavenderePdaConfig.usaFiltraProdutoCodigoExato()) {
		    			domainFilter.cdProduto = dsFiltro;
		    		} else {
		    			domainFilter.cdProdutoLikeFilter = prefixo + dsFiltro + "%";
		    		}
		    	}
		    	domainFilter.dsProduto = prefixo + dsFiltro + "%";
	    	} else {
	    		domainFilter.dsPrincipioAtivo = prefixo + dsFiltro + "%";
	    	}
    	}
    	if (LavenderePdaConfig.usaFiltroFornecedor) {
    		domainFilter.cdFornecedor = cdFornecedor;
    	}
    	if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
    		return ProdutoTabPrecoService.getInstance().findAllByExample(domainFilter);
    	} else {
    		return ProdutoService.getInstance().findAllByExample(domainFilter);
    	}
    }

}