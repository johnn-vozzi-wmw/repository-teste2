package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoRemessa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoRemessaDbxDao;
import totalcross.sql.Types;
import totalcross.util.Vector;

public class ItemPedidoRemessaService extends CrudService {

    private static ItemPedidoRemessaService instance = null;
    
    private ItemPedidoRemessaService() {
        //--
    }
    
    public static ItemPedidoRemessaService getInstance() {
        if (instance == null) {
            instance = new ItemPedidoRemessaService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemPedidoRemessaDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {
    }

	public void insertItemRemessa(ItemPedido itemPedido, Estoque estoque, double qtEstoqueConsumido) throws SQLException {
		ItemPedidoRemessa itemPedidoRemessa = getItemPedidoRemessaByItemPedido(itemPedido);
		itemPedidoRemessa.cdLocalEstoque = estoque.cdLocalEstoque;
		itemPedidoRemessa.qtEstoqueConsumido = qtEstoqueConsumido;
		itemPedidoRemessa.nuNotaRemessa = estoque.nuNotaRemessa;
		itemPedidoRemessa.nuSerieRemessa = estoque.nuSerieRemessa;
		insert(itemPedidoRemessa);
	}

	private ItemPedidoRemessa getItemPedidoRemessaByItemPedido(ItemPedido itemPedido) {
		ItemPedidoRemessa itemPedidoRemessa = new ItemPedidoRemessa();
		itemPedidoRemessa.cdEmpresa = itemPedido.cdEmpresa;
		itemPedidoRemessa.cdRepresentante = itemPedido.cdRepresentante;
		itemPedidoRemessa.flOrigemPedido = itemPedido.flOrigemPedido;
		itemPedidoRemessa.nuPedido = itemPedido.nuPedido;
		itemPedidoRemessa.cdProduto = itemPedido.cdProduto;
		itemPedidoRemessa.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemPedidoRemessa.nuSeqProduto = itemPedido.nuSeqProduto;
		return itemPedidoRemessa;
	}
	
	protected ItemPedidoRemessa getItemPedidoRemessaByItemNfe(ItemNfe itemNfe) {
		ItemPedidoRemessa itemPedidoRemessa = new ItemPedidoRemessa();
		itemPedidoRemessa.cdEmpresa = itemNfe.cdEmpresa;
		itemPedidoRemessa.cdRepresentante = itemNfe.cdRepresentante;
		itemPedidoRemessa.flOrigemPedido = itemNfe.flOrigemPedido;
		itemPedidoRemessa.nuPedido = itemNfe.nuPedido;
		itemPedidoRemessa.cdProduto = itemNfe.cdProduto;
		itemPedidoRemessa.flTipoItemPedido = itemNfe.flTipoItemPedido;
		itemPedidoRemessa.nuSeqProduto = itemNfe.nuSeqProduto;
		return itemPedidoRemessa;
	}
	
	protected Vector findItemPedidoRemessaList(RemessaEstoque remessaEstoque) throws SQLException {
		ItemPedidoRemessa itemPedidoRemessaList = new ItemPedidoRemessa();
		itemPedidoRemessaList.cdEmpresa = remessaEstoque.cdEmpresa;
		itemPedidoRemessaList.cdRepresentante = remessaEstoque.cdRepresentante;
		itemPedidoRemessaList.nuNotaRemessa = remessaEstoque.nuNotaRemessa;
		itemPedidoRemessaList.nuSerieRemessa = remessaEstoque.nuSerieRemessa;
		return findAllByExample(itemPedidoRemessaList);
	}
	
	
	public void deleteItemPedidoRemessa(ItemPedido itemPedido) throws SQLException {
		ItemPedidoRemessa itemPedidoRemessa = getItemPedidoRemessaByItemPedido(itemPedido);
		deleteAllByExample(itemPedidoRemessa);
	}

	public Vector findAllItemPedidoRemessa(ItemPedido itemPedido) throws SQLException {
		ItemPedidoRemessa itemPedidoRemessa = getItemPedidoRemessaByItemPedido(itemPedido);
		return findAllByExample(itemPedidoRemessa);
	}

	public void updateEstoquePdaToERP(Pedido pedido, boolean atualizaEstoqueOrigemERP) throws SQLException {
		ItemPedidoRemessa itemPedidoRemessaFilter = new ItemPedidoRemessa();
		itemPedidoRemessaFilter.cdEmpresa = pedido.cdEmpresa;
		itemPedidoRemessaFilter.cdRepresentante = pedido.cdRepresentante;
		itemPedidoRemessaFilter.flOrigemPedido = pedido.flOrigemPedido;
		itemPedidoRemessaFilter.nuPedido = pedido.nuPedido;
		Vector itemPedidoRemessaList = findAllByExample(itemPedidoRemessaFilter);
		int size = itemPedidoRemessaList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoRemessa itemPedidoRemessa = (ItemPedidoRemessa) itemPedidoRemessaList.items[i];
			Estoque estoque = EstoqueService.getInstance().getEstoqueByItemPedidoRemessa(itemPedidoRemessa, Estoque.FLORIGEMESTOQUE_PDA);
			updateEstoque(itemPedidoRemessa, estoque);
			if (atualizaEstoqueOrigemERP) {
				estoque = EstoqueService.getInstance().getEstoqueByItemPedidoRemessa(itemPedidoRemessa, Estoque.FLORIGEMESTOQUE_ERP);
				updateEstoque(itemPedidoRemessa, estoque);
			}
		}
	}

	private void updateEstoque(ItemPedidoRemessa itemPedidoRemessa, Estoque estoquePda) throws SQLException {
		estoquePda.qtEstoque -= itemPedidoRemessa.qtEstoqueConsumido;
		EstoqueService.getInstance().updateColumn(estoquePda.getRowKey(), "qtEstoque", estoquePda.qtEstoque, Types.DECIMAL);
	}

	
	
	
	
}
