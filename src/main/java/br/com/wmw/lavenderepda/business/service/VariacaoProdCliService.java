package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdCli;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VariacaoProdCliDbxDao;

public class VariacaoProdCliService extends CrudService {

    private static VariacaoProdCliService instance;
    
    private VariacaoProdCliService() {
        //--
    }
    
    public static VariacaoProdCliService getInstance() {
        if (instance == null) {
            instance = new VariacaoProdCliService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VariacaoProdCliDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }
    
    public double getVlVariacaoPrecoProdCli(ItemPedido itemPedido) throws SQLException {
    	VariacaoProdCli variacaoProdCliFilter = new VariacaoProdCli(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdProduto, itemPedido.pedido.cdCliente);
    	return ValueUtil.getDoubleValue(findColumnByRowKey(variacaoProdCliFilter.getRowKey(), "vlPctVariacao"));
    }

}