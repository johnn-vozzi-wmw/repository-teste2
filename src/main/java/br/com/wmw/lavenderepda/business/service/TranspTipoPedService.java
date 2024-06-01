package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.TranspTipoPed;
import br.com.wmw.lavenderepda.integration.dao.pdbx.TranspTipoPedDbxDao;

public class TranspTipoPedService extends CrudService {

    private static TranspTipoPedService instance;

    private TranspTipoPedService() {
        //--
    }

    public static TranspTipoPedService getInstance() {
        if (instance == null) {
            instance = new TranspTipoPedService();
        }
        return instance;
    }

    //@Override
    protected CrudDao getCrudDao() {
        return TranspTipoPedDbxDao.getInstance();
    }

    //@Override
    public void validate(BaseDomain domain) throws java.sql.SQLException {
    }

    public TranspTipoPed getTranspTipoPed(String cdTransportadora, String cdTipoPedido) throws SQLException {
    	TranspTipoPed transpTipoPed = new TranspTipoPed();
    	transpTipoPed.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	transpTipoPed.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TranspTipoPed.class);
    	transpTipoPed.cdTipoPedido = cdTipoPedido;
    	transpTipoPed.cdTransportadora = cdTransportadora;
    	return (TranspTipoPed) findByRowKey(transpTipoPed.getRowKey());
    }

    public double getVlFreteTransportadora(TranspTipoPed transpTipoPed, double vlTotalPedido) {
    	return (transpTipoPed != null && transpTipoPed.vlMinPedidoFreteGratis != 0 && vlTotalPedido < transpTipoPed.vlMinPedidoFreteGratis) ? transpTipoPed.vlFreteMinimo : 0;
    }

    public void recalculateFreteItensPedido(Pedido pedido, boolean onChangeTransp) throws SQLException {
    	TipoPedido tipoPedido = pedido.getTipoPedido();
    	if (tipoPedido == null || tipoPedido.isIgnoraCalculoFrete() || pedido.getTranspTipoPed() == null || !pedido.getTranspTipoPed().isFlCobraFreteAdicionalPorProd() || !onChangeTransp) return;
		if (onChangeTransp || ((pedido.vlTotalPedidoOld < pedido.getTranspTipoPed().vlMinPedidoFreteGratis && pedido.vlTotalPedido >= pedido.getTranspTipoPed().vlMinPedidoFreteGratis)
				|| (pedido.vlTotalPedidoOld >= pedido.getTranspTipoPed().vlMinPedidoFreteGratis && pedido.vlTotalPedido < pedido.getTranspTipoPed().vlMinPedidoFreteGratis))) {
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
				// se recalcula o item(flag do produto)
				if (ValueUtil.VALOR_SIM.equals(itemPedido.getProduto().flAplicaFreteApenasValorMinimo) || onChangeTransp) {
					calculateFreteItemPedido(pedido, itemPedido);
					ItemPedidoService.getInstance().updateItemSimples(itemPedido);
				}
			}
		}
    }

	public void calculateFreteItemPedido(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		TipoPedido tipoPedido = pedido.getTipoPedido();
		if ((tipoPedido != null && tipoPedido.isIgnoraCalculoFrete()) || pedido.getTranspTipoPed() == null) return;
		if (itemPedido.getProduto().vlPctFrete > 0
				&& pedido.getTranspTipoPed().isFlCobraFreteAdicionalPorProd()
				&& (ValueUtil.VALOR_SIM.equals(itemPedido.getProduto().flAplicaFreteApenasValorMinimo)
				&& pedido.vlTotalPedido < pedido.getTranspTipoPed().vlMinPedidoFreteGratis
				|| !ValueUtil.VALOR_SIM.equals(itemPedido.getProduto().flAplicaFreteApenasValorMinimo))) {
			itemPedido.vlFrete = (itemPedido.getProduto().vlPctFrete / 100) * itemPedido.vlTotalItemPedido;
		} else {
			itemPedido.vlFrete = 0;
		}
	}
	
	public boolean isPossuiTranspTipoPedido() throws SQLException {
		TranspTipoPed filter = new TranspTipoPed();
		filter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		filter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(TranspTipoPed.class);
		return countByExample(filter) > 0;
	}

}