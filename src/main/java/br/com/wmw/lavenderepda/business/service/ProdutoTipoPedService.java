package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoTipoPed;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.validation.ProdutoTipoPedidoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ProdutoTipoPedDbxDao;
import totalcross.util.Vector;

public class ProdutoTipoPedService extends CrudService {

    private static ProdutoTipoPedService instance;

    private ProdutoTipoPedService() {
        //--
    }

    public static ProdutoTipoPedService getInstance() {
        if (instance == null) {
            instance = new ProdutoTipoPedService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return ProdutoTipoPedDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public void validateProdutoTipoPedidoChange(Pedido pedido) throws SQLException {
    	TipoPedido tipoPedido = pedido.getTipoPedido();
    	int size = pedido.itemPedidoList.size();
    	ItemPedido itemPedido;
    	ProdutoTipoPed produtoTipoPed;
    	for (int i = 0; i < size; i++) {
    		itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			produtoTipoPed = getProdutoTipoPed(tipoPedido.cdTipoPedido, itemPedido.cdProduto);
    		if (!isProdutoValidoTipoPedido(produtoTipoPed, tipoPedido)) {
    			throw new ProdutoTipoPedidoException(Messages.TIPOPEDIDO_MSG_PRODUTOS_INADEQUADOS);
    		}
		}

    }

    public Vector getItensNaoConformeByTipoPedido(Pedido pedido, TipoPedido tipoPedido) throws SQLException {
    	Vector itensNaoConforme = new Vector();
    	int size = pedido.itemPedidoList.size();
    	ItemPedido itemPedido;
    	ProdutoTipoPed produtoTipoPed;
    	for (int i = 0; i < size; i++) {
    		itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
        	produtoTipoPed = getProdutoTipoPed(tipoPedido.cdTipoPedido, itemPedido.cdProduto);
    		if (!isProdutoValidoTipoPedido(produtoTipoPed, tipoPedido)) {
    			itensNaoConforme.addElement(itemPedido);
    		}
    	}
    	return itensNaoConforme;
    }

    public int countProdutoTipoPed(String cdTipoPedido) throws SQLException {
		ProdutoTipoPed produtoTipoPedFilter = new ProdutoTipoPed();
		produtoTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoTipoPedFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoTipoPedFilter.cdTipoPedido = cdTipoPedido;
		return ProdutoTipoPedService.getInstance().countByExample(produtoTipoPedFilter);
    }

	public ProdutoTipoPed getProdutoTipoPed(String cdTipoPedido, String cdProduto) throws SQLException {
		ProdutoTipoPed produtoTipoPedFilter = new ProdutoTipoPed();
		produtoTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoTipoPedFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoTipoPedFilter.cdTipoPedido = cdTipoPedido;
		produtoTipoPedFilter.cdProduto = cdProduto;
		ProdutoTipoPed produtoTipoPed = (ProdutoTipoPed) ProdutoTipoPedService.getInstance().findByRowKey(produtoTipoPedFilter.getRowKey());
		return produtoTipoPed;
	}

    protected boolean isProdutoValidoTipoPedido(ProdutoTipoPed produtoTipoPed, TipoPedido tipoPedido) throws SQLException {
		if (produtoTipoPed != null) {
			return tipoPedido == null || !tipoPedido.isFlExcecaoProduto();
		} else {
			return tipoPedido == null || tipoPedido.isFlExcecaoProduto() || countProdutoTipoPed(tipoPedido.cdTipoPedido) == 0;
		}
    }

}