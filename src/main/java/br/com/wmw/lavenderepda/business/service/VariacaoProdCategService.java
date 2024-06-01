package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdCateg;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VariacaoProdCategDbxDao;

public class VariacaoProdCategService extends CrudService {

    private static VariacaoProdCategService instance;

    private VariacaoProdCategService() {
        //--
    }

    public static VariacaoProdCategService getInstance() {
        if (instance == null) {
            instance = new VariacaoProdCategService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VariacaoProdCategDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public double getVlVariacaoPrecoProduto(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.pedido.getCliente().cdCategoria == null) return 0d;
		VariacaoProdCateg varicaoProdCategoriaFilter = new VariacaoProdCateg();
		varicaoProdCategoriaFilter.cdEmpresa = itemPedido.cdEmpresa;
		varicaoProdCategoriaFilter.cdRepresentante = itemPedido.cdRepresentante;
		varicaoProdCategoriaFilter.cdProduto = itemPedido.cdProduto;
		varicaoProdCategoriaFilter.cdCategoria = itemPedido.pedido.getCliente().cdCategoria;
		return ValueUtil.getDoubleValue(findColumnByRowKey(varicaoProdCategoriaFilter.getRowKey(), "vlPctVariacao"));
	}

}