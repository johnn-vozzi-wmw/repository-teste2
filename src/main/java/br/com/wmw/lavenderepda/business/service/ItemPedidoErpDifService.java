package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoErpDifPdbxDao;
import totalcross.util.Vector;

public class ItemPedidoErpDifService extends CrudService {

    private static ItemPedidoErpDifService instance;

    private ItemPedidoErpDifService() {
        //--
    }

    public static ItemPedidoErpDifService getInstance() {
        if (instance == null) {
            instance = new ItemPedidoErpDifService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemPedidoErpDifPdbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void deleteAllByPedido(Pedido pedido) throws SQLException {
    	ItemPedidoErpDif itemPedidoErpDif = new ItemPedidoErpDif();
    	itemPedidoErpDif.cdEmpresa = pedido.cdEmpresa;
    	itemPedidoErpDif.cdRepresentante = pedido.cdRepresentante;
    	itemPedidoErpDif.flOrigemPedido = pedido.flOrigemPedido;
    	itemPedidoErpDif.nuPedido = pedido.nuPedido;
    	deleteAllByExample(itemPedidoErpDif);
    }

    public Vector findAllByPedido(Pedido pedido) throws SQLException {
    	ItemPedidoErpDif itemPedidoErpDif = new ItemPedidoErpDif();
    	itemPedidoErpDif.cdEmpresa = pedido.cdEmpresa;
    	itemPedidoErpDif.cdRepresentante = pedido.cdRepresentante;
    	itemPedidoErpDif.flOrigemPedido = pedido.flOrigemPedido;
    	itemPedidoErpDif.nuPedido = pedido.nuPedido;
    	return findAllByExample(itemPedidoErpDif);
    }
    
    public Vector findAllByPedidoErp(Pedido pedidoErp) throws SQLException {
    	ItemPedidoErpDif itemPedidoErpDif = new ItemPedidoErpDif();
    	itemPedidoErpDif.cdEmpresa = pedidoErp.cdEmpresa;
    	itemPedidoErpDif.cdRepresentante = pedidoErp.cdRepresentante;
    	itemPedidoErpDif.nuPedido = pedidoErp.nuPedido;
    	return ItemPedidoErpDifPdbxDao.getInstance().findAllByPedidoErp(itemPedidoErpDif);
    }
    
    public Vector findAllNaoAlterados() throws java.sql.SQLException {
    	return ItemPedidoErpDifPdbxDao.getInstance().findAllNaoAlterados();
    }

	public void updateItemPedidoDifLidos() {
		try {
			ItemPedidoErpDifPdbxDao.getInstance().updateItemPedidoDifLidos();
		} catch (Throwable e) {
			// Apenas nao atualiza, pois não o encontrou nenhum para atualizar
		}
	}
	
	public boolean verificaItemComDiferenca(ItemPedido itemPedido) throws SQLException {
		ItemPedidoErpDif itemPedidoErpDif = new ItemPedidoErpDif();
		itemPedidoErpDif.cdEmpresa = itemPedido.cdEmpresa;
		itemPedidoErpDif.cdRepresentante = itemPedido.cdRepresentante;
		itemPedidoErpDif.cdProduto = itemPedido.cdProduto;
		itemPedidoErpDif.nuPedido = itemPedido.nuPedido;
		itemPedidoErpDif.flTipoItemPedido = itemPedido.flTipoItemPedido;
		return countByExample(itemPedidoErpDif) > 0;
	}
	
    public Vector getItemPedidoComDiferencas(Vector listTemp) {
    	int listSize = listTemp.size();
    	Vector list = new Vector(listSize);
    	for (int x = 0; x < listSize; x++) {
    		ItemPedido itemPedido = (ItemPedido) listTemp.items[x];
    		if (itemPedido.possuiDiferenca) {
    			list.addElement(itemPedido);
    		}
    	}
    	listTemp = null;
    	return list;
    }
    
    public void updateFlagDiferencaPedidos() throws SQLException {
		Vector itemPedidoErpDifList = findAllNaoAlterados();
		int size = itemPedidoErpDifList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoErpDif itemPedDif = (ItemPedidoErpDif) itemPedidoErpDifList.items[i];
			PedidoService.getInstance().updateFlPossuiDiferenca(itemPedDif.cdEmpresa, itemPedDif.cdRepresentante, itemPedDif.nuPedido);
		}
		updateItemPedidoDifLidos();
	}

}