package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.TributacaoConfig;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TributacaoConfigDao;

public class TributacaoConfigService extends CrudService {

    private static TributacaoConfigService instance;
    
    private TributacaoConfigService() {
        //--
    }
    
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }
    
    public static TributacaoConfigService getInstance() {
        if (instance == null) {
            instance = new TributacaoConfigService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TributacaoConfigDao.getInstance();
    }
    
    public TributacaoConfig getTributacaoConfig(Cliente cliente, Produto produto, TipoPedido tipoPedido) throws SQLException {
    	if (!LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
    		return null;
    	}
    	TributacaoConfig tributacaoConfig = TributacaoConfigDao.getInstance().findTributacaoConfigProduto(cliente, produto, tipoPedido);
    	return tributacaoConfig;
    }

}