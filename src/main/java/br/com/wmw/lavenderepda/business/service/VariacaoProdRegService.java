package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.VariacaoProdReg;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VariacaoProdRegDbxDao;

public class VariacaoProdRegService extends CrudService {

    private static VariacaoProdRegService instance;

    private VariacaoProdRegService() {
        //--
    }

    public static VariacaoProdRegService getInstance() {
        if (instance == null) {
            instance = new VariacaoProdRegService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return VariacaoProdRegDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

	public double getVlVariacaoPrecoProduto(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.pedido.getCliente().cdRegiao == null) return 0d;
		VariacaoProdReg varicaoProdRegFilter = new VariacaoProdReg();
		varicaoProdRegFilter.cdEmpresa = itemPedido.cdEmpresa;
		varicaoProdRegFilter.cdRepresentante = itemPedido.cdRepresentante;
		varicaoProdRegFilter.cdProduto = itemPedido.cdProduto;
		varicaoProdRegFilter.cdRegiao = itemPedido.pedido.getCliente().cdRegiao;
		return ValueUtil.getDoubleValue(findColumnByRowKey(varicaoProdRegFilter.getRowKey(), "vlPctVariacao"));
	}

}