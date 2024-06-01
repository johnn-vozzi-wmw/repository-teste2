package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdExcec;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VariacaoProdExcecDbxDao;

public class VariacaoProdExcecService extends CrudService {

    private static VariacaoProdExcecService instance;

    private VariacaoProdExcecService() {
        //--
    }

    public static VariacaoProdExcecService getInstance() {
        if (instance == null) {
            instance = new VariacaoProdExcecService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VariacaoProdExcecDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public double getVlVariacaoPrecoProduto(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.pedido.getCliente().cdRegiao == null || itemPedido.pedido.getCliente().cdCategoria == null) return 0d; 
		VariacaoProdExcec variacaoProdExcecFilter = new VariacaoProdExcec();
		variacaoProdExcecFilter.cdEmpresa = itemPedido.cdEmpresa;
		variacaoProdExcecFilter.cdRepresentante = itemPedido.cdRepresentante;
		variacaoProdExcecFilter.cdProduto = itemPedido.cdProduto;
		variacaoProdExcecFilter.cdRegiao = itemPedido.pedido.getCliente().cdRegiao;
		variacaoProdExcecFilter.cdCategoria = itemPedido.pedido.getCliente().cdCategoria;
		return ValueUtil.getDoubleValue(findColumnByRowKey(variacaoProdExcecFilter.getRowKey(), "vlPctVariacao"));
	}



}