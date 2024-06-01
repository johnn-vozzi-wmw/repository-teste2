package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemNfce;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemNfceDbxDao;
import totalcross.util.Vector;

public class ItemNfceService extends CrudService {

    private static ItemNfceService instance = null;
    
    private ItemNfceService() {
        //--
    }
    
    public static ItemNfceService getInstance() {
        if (instance == null) {
            instance = new ItemNfceService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemNfceDbxDao.getInstance();
    }
 
    //@Override
    public void validate(BaseDomain domain) {   }

	public void geraItensNfce(Pedido pedido, Nfce nfce) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			ItemNfce itemNfce = new ItemNfce();
			itemNfce.cdEmpresa = nfce.cdEmpresa;
			itemNfce.cdRepresentante = nfce.cdRepresentante;
			itemNfce.nuPedido = itemPedido.nuPedido;
			itemNfce.flOrigemPedido = "P";
			itemNfce.cdProduto = itemPedido.cdProduto;
			itemNfce.nuSeqProduto = itemPedido.nuSeqProduto;
			itemNfce.qtItemFisico = itemPedido.getQtItemFisico();
			itemNfce.cdUnidade = itemPedido.cdUnidade;
			itemNfce.vlUnitario = itemPedido.vlItemPedido;
			itemNfce.vlTotalItem = itemPedido.vlTotalItemPedido;
			insert(itemNfce);
		}
	}
	
	public void excluirItensNfce(Nfce nfce) throws SQLException {
		deleteAllByExample(getItemNfceFilter(nfce));
	}
	
	private ItemNfce getItemNfceFilter(final Nfce nfce) {
		ItemNfce itemNfceFilter = new ItemNfce();
		itemNfceFilter.cdEmpresa = nfce.cdEmpresa;
		itemNfceFilter.cdRepresentante = nfce.cdRepresentante;
		itemNfceFilter.nuPedido = nfce.nuPedido;
		itemNfceFilter.flOrigemPedido = nfce.flOrigemPedido;
		return itemNfceFilter;
	}

	public Vector getItemNfceList(final Nfce nfce) throws SQLException {
    	if (nfce != null) {
			ItemNfce itemNfceFilter = getItemNfceFilter(nfce);
			return findAllByExample(itemNfceFilter);
    	}
    	return null;
    }
	
	public void insertOrUpdateItemNfceRetorno(ItemNfce itemNfce) throws SQLException {
		if (countByExample(itemNfce) > 0) {
			getCrudDao().update(itemNfce);
		} else {
			getCrudDao().insert(itemNfce);
		}
	}
}