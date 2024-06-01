package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ContratoProdEst;
import br.com.wmw.lavenderepda.business.domain.ContratoProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ContratoProdutoDbxDao;

public class ContratoProdutoService extends CrudService {

    private static ContratoProdutoService instance;

    private ContratoProdutoService() {
        //--
    }

    public static ContratoProdutoService getInstance() {
        if (instance == null) {
            instance = new ContratoProdutoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ContratoProdutoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void convertForecastToItemPedido(ContratoProduto contratoProduto, Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	convertContratoProdutoToItemPedido(contratoProduto, pedido, itemPedido);
		itemPedido.setQtItemFisico(contratoProduto.qtProdutoContrato);
	}

	public void convertGradeToItemPedido(ContratoProduto contratoProduto, ContratoProdEst contratoProdEst, Pedido pedido, ItemPedido itemPedido) throws SQLException {
    	convertContratoProdutoToItemPedido(contratoProduto, pedido, itemPedido);
		itemPedido.setQtItemFisico((contratoProduto.qtProdutoContrato -  contratoProdEst.qtEstoqueAtual));
	}

	protected void convertContratoProdutoToItemPedido(ContratoProduto contratoProduto, Pedido pedido, ItemPedido itemPedido) throws SQLException {
		itemPedido.cdProduto = contratoProduto.cdProduto;
		itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(itemPedido.cdTabelaPreco, itemPedido.cdProduto, itemPedido.cdUfClientePedido);
		if (itemTabelaPreco != null) {
			itemPedido.vlBaseItemTabelaPreco = itemTabelaPreco.vlUnitario;
			itemPedido.vlBaseItemPedido = itemPedido.vlBaseItemTabelaPreco;
			itemPedido.vlItemPedido = itemPedido.vlBaseItemPedido;
		}
	}
}