package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PlataformaVendaProduto;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoErro;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PlataformaVendaProdutoDbxDao;
import totalcross.util.Vector;

public class PlataformaVendaProdutoService extends CrudService {

    private static PlataformaVendaProdutoService instance;
    
    private PlataformaVendaProdutoService() {
        //--
    }
    
    public static PlataformaVendaProdutoService getInstance() {
        if (instance == null) {
            instance = new PlataformaVendaProdutoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return PlataformaVendaProdutoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) { }
    
    public Vector findProdutosPlataformaDivergente(Pedido pedido) throws SQLException {
    	Vector erroPlataformaVendaItensList = new Vector();
    	Vector itemPedidoList = pedido.itemPedidoList;
    	int size = itemPedidoList.size();
    	for (int i = 0; i < size; i++) {
    		ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
    		if (isNotExistsProdutoInPlataformaVendaProduto(pedido, itemPedido)) {
				ProdutoErro produtoErro = new ProdutoErro(itemPedido.getProduto(), itemPedido.cdProduto, Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
				erroPlataformaVendaItensList.addElement(produtoErro);
    		}
    	}
    	return erroPlataformaVendaItensList;
    }
    
    public boolean isNotExistsProdutoInPlataformaVendaProduto(Pedido pedido,ItemPedido itemPedido) throws SQLException {
    	return itemPedido != null ? isNotExistsProdutoInPlataformaVendaProduto(pedido, itemPedido.getProduto()) : false;
    }
    
	public boolean isNotExistsProdutoInPlataformaVendaProduto(Pedido pedido, Produto produto) throws SQLException {
		if (produto == null || pedido == null || produto.cdLinha == null || produto.cdMarca == null || produto.cdEmpresa == null || pedido.cdPlataformaVenda == null) {
			return true;
		}
		PlataformaVendaProduto plataformaVendaProdutoFilter = new PlataformaVendaProduto();
		plataformaVendaProdutoFilter.cdEmpresa = produto.cdEmpresa;
		plataformaVendaProdutoFilter.cdPlataformaVenda = pedido.cdPlataformaVenda;
		plataformaVendaProdutoFilter.cdMarca = produto.cdMarca;
		plataformaVendaProdutoFilter.cdLinha = produto.cdLinha;
		return PlataformaVendaProdutoService.getInstance().countByExample(plataformaVendaProdutoFilter) <= 0;
	}

}