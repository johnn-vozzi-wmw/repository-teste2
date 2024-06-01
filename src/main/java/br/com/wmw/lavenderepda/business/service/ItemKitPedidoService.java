package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemKitPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoKit;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemKitPedidoDbxDao;
import totalcross.util.Vector;

public class ItemKitPedidoService extends CrudService {

    private static ItemKitPedidoService instance;

    private ItemKitPedidoService() {
        //--
    }

    public static ItemKitPedidoService getInstance() {
        if (instance == null) {
            instance = new ItemKitPedidoService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ItemKitPedidoDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException { }

	protected void setDadosAlteracao(BaseDomain domain) {
		domain.cdUsuario = Session.getCdUsuario();
	}

    public void insert(Vector itemKitPedidoList, int nuSeqProduto, Pedido pedidoAtual, double qtItemFisico) throws SQLException {
    	for (int i = 0; i < itemKitPedidoList.size(); i++) {
    		ItemKitPedido itemKitPedido = (ItemKitPedido)itemKitPedidoList.items[i];
    	    itemKitPedido.nuSeqProduto = nuSeqProduto;
    	    if (pedidoAtual != null) {
    	    	itemKitPedido.nuPedido = pedidoAtual.nuPedido;
    	    	if (LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual()) {
    	    		itemKitPedido.qtItemFisico = itemKitPedido.qtItemFisico * qtItemFisico;
    	    	}
    	    	itemKitPedido.flOrigemPedido = pedidoAtual.flOrigemPedido;
			}
    		super.insert((ItemKitPedido)itemKitPedidoList.items[i]);
		}
    }

    public void update(Vector itemKitPedidoList, double qtItemFisico) throws SQLException {
    	for (int i = 0; i < itemKitPedidoList.size(); i++) {
    		ItemKitPedido itemKitPedido = (ItemKitPedido)itemKitPedidoList.items[i];
    		if (LavenderePdaConfig.isUsaKitBaseadoNoProdutoOcultaQtAtual()) {
    			ProdutoKit produtoKit = ProdutoKitService.getInstance().getProdutoKitByItemKitPedido(itemKitPedido);
    			itemKitPedido.qtItemFisico = produtoKit.qtItemFisico * qtItemFisico;
    		}
			super.update(itemKitPedido);
    	}
    }

    public void delete(Vector itemKitPedidoList) throws SQLException {
    	for (int i = 0; i < itemKitPedidoList.size(); i++) {
    		super.delete((ItemKitPedido)itemKitPedidoList.items[i]);
    	}
    }

    public void findItemKitPedidoList(ItemPedido itemPedido) throws SQLException {
		ItemKitPedido itemKitPedido = new ItemKitPedido();
		itemKitPedido.cdEmpresa = itemPedido.cdEmpresa;
		itemKitPedido.cdRepresentante = itemPedido.cdRepresentante;
		itemKitPedido.flOrigemPedido = itemPedido.flOrigemPedido;
		itemKitPedido.nuPedido = itemPedido.nuPedido;
		itemKitPedido.cdKit = itemPedido.cdProduto;
		itemKitPedido.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemKitPedido.nuSeqProduto = itemPedido.nuSeqProduto;
		itemPedido.itemKitPedidoList = ItemKitPedidoService.getInstance().findAllByExampleUnique(itemKitPedido);
    }

	//@Override
	public Vector findAllByExampleUnique(final BaseDomain domain) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemKitPedido)domain).flOrigemPedido)) {
			return ItemKitPedidoDbxDao.getInstance().findAllByExampleUnique(domain);
		} else {
			return ItemKitPedidoDbxDao.getInstanceErp().findAllByExampleUnique(domain);
		}
	}

	//@Override
	public Vector findAllByExampleSummaryUnique(final BaseDomain domain) throws SQLException {
		if (OrigemPedido.FLORIGEMPEDIDO_PDA.equals(((ItemKitPedido)domain).flOrigemPedido)) {
			return ItemKitPedidoDbxDao.getInstance().findAllByExampleSummaryUnique(domain);
		} else {
			return ItemKitPedidoDbxDao.getInstanceErp().findAllByExampleSummaryUnique(domain);
		}
	}
	
	public double findQtItemKitDescQtdSimilares(ItemPedido itemPedido, String cdProdutoDesc) throws SQLException {
		ItemKitPedido itemKitPedido = new ItemKitPedido();
		itemKitPedido.cdEmpresa = itemPedido.cdEmpresa;
		itemKitPedido.cdRepresentante = itemPedido.cdRepresentante;
		itemKitPedido.flOrigemPedido = itemPedido.flOrigemPedido;
		itemKitPedido.nuPedido = itemPedido.nuPedido;
		itemKitPedido.flTipoItemPedido = itemPedido.flTipoItemPedido;
		itemKitPedido.nuSeqProduto = itemPedido.nuSeqProduto;
		return ItemKitPedidoDbxDao.getInstance().findQtItemKitDescQtdSimilares(itemKitPedido, cdProdutoDesc, itemPedido.cdProduto);
	}


}