package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TipoNovidade;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TipoNovidadeDao;
import totalcross.util.Hashtable;

public class TipoNovidadeService extends CrudService {

    private static TipoNovidadeService instance;
    private Hashtable cache;

    private TipoNovidadeService() {
    	cache = new Hashtable(2);
    }

    public static TipoNovidadeService getInstance() {
        if (instance == null) {
            instance = new TipoNovidadeService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TipoNovidadeDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) {
    }
    
    @Override
    public void clearCache() throws SQLException {
    	super.clearCache();
    	cache = null;
    }

    public String getDsTipoNovidade(String cdTipoNovidade) throws SQLException {
    	TipoNovidade tipoNovidadeFilter = new TipoNovidade();
    	tipoNovidadeFilter.cdTipoNovidade = cdTipoNovidade;
    	tipoNovidadeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	String dsTipoNovidade = (String) cache.get(tipoNovidadeFilter.getRowKey());
    	if (dsTipoNovidade == null) {
    		dsTipoNovidade = findColumnByRowKey(tipoNovidadeFilter.getRowKey(), "DSTIPONOVIDADE");
    	}
    	if (dsTipoNovidade == null) {
    		cache.put(tipoNovidadeFilter.getRowKey(), cdTipoNovidade);
    		return StringUtil.getStringValue(cdTipoNovidade);
    	} else {
    		cache.put(tipoNovidadeFilter.getRowKey(), dsTipoNovidade);
    		return dsTipoNovidade;
    	}
    }
    
	public boolean isTipoNovidadeAlteracaoPreco(String cdTipoNovidade) {
		return ValueUtil.valueEquals(cdTipoNovidade, TipoNovidade.TIPONOVIDADEPRODUTO_ALTERACAO_PRECO_INTERPOLACAO)
			|| ValueUtil.valueEquals(cdTipoNovidade, TipoNovidade.TIPONOVIDADEPRODUTO_AUMENTO_PRECO)
			|| ValueUtil.valueEquals(cdTipoNovidade, TipoNovidade.TIPONOVIDADEPRODUTO_QUEDA_PRECO);
	}

	public boolean isTipoNovidadeAlteracaoEstoque(String cdTipoNovidade) {
		return ValueUtil.valueEquals(cdTipoNovidade, TipoNovidade.TIPONOVIDADEPRODUTO_ENTRADA_ESTOQUE);
	}

	public boolean isTipoNovidadeEntradaProduto(String cdTipoNovidade) {
		return ValueUtil.valueEquals(cdTipoNovidade, TipoNovidade.TIPONOVIDADEPRODUTO_NOVO_PRODUTO);
	}

}