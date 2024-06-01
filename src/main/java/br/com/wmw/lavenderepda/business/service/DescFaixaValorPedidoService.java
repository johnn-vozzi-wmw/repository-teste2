package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.DescFaixaValorPedidoDbxDao;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemPedidoPdbxDao;

public class DescFaixaValorPedidoService extends CrudService {

    private static DescFaixaValorPedidoService instance;
    
    private DescFaixaValorPedidoService() {
        //--
    }
    
    public static DescFaixaValorPedidoService getInstance() {
        if (instance == null) {
            instance = new DescFaixaValorPedidoService();
        }
        return instance;
    }

    @Override
    protected CrudDao getCrudDao() {
        return DescFaixaValorPedidoDbxDao.getInstance();
    }
 
    @Override
    public void validate(BaseDomain domain) {
    }

	public boolean aplicaDescontoItens(Pedido pedido) throws SQLException {
		double vlPctDescHistoricoVendasPedido = pedido.vlPctDescHistoricoVendas;
		double vlPctDescHistoricoVendasCli = ValueUtil.getDoubleValue(ClienteService.getInstance().findColumnByRowKey(pedido.getCliente().getRowKey(), "VLPCTDESCHISTORICOVENDAS"));
		double vlPctDescontoFaixaValor  = DescFaixaValorPedidoDbxDao.getInstance().findFaixaDescontoPedido(pedido.vlTotalPedido);
		double vlPctMaiorDesconto = vlPctDescHistoricoVendasCli > vlPctDescontoFaixaValor ? vlPctDescHistoricoVendasCli : vlPctDescontoFaixaValor;
		if (vlPctDescHistoricoVendasPedido > vlPctMaiorDesconto) {
			throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_DESCONTO_FAIXA_MAIOR_QUE_MAXIMO_PERMITIDO, vlPctMaiorDesconto));
		} else {
			vlPctMaiorDesconto = vlPctDescHistoricoVendasPedido;
		}
		if (vlPctMaiorDesconto <= 0) return false;
		
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (!itemPedido.getItemTabelaPreco().isFlBloqueiaDesc2()) {
				//Devolve o desconto 5 para aplicar primeiro o 4
				itemPedido.vlItemPedido = itemPedido.vlItemPedido + (itemPedido.vlDesconto3 / itemPedido.getQtItemFisico());
				//--
				itemPedido.vlPctDesconto2 = vlPctMaiorDesconto;
				itemPedido.vlDesconto2 = ItemPedidoService.getInstance().roundDescontoEmCascataRegra2(itemPedido.vlItemPedido - (itemPedido.vlItemPedido * (1 - (vlPctMaiorDesconto / 100))));
				itemPedido.vlItemPedido -= itemPedido.vlDesconto2;
				itemPedido.vlDesconto2 *= itemPedido.getQtItemFisico();
			}
			if (!itemPedido.getItemTabelaPreco().isFlBloqueiaDesc2() && !itemPedido.getItemTabelaPreco().isFlBloqueiaDescCondicao()) {
				//Aplica novamente o desconto 5
				itemPedido.vlDesconto3 = ItemPedidoService.getInstance().roundDescontoEmCascataRegra2(itemPedido.vlItemPedido - (itemPedido.vlItemPedido  * (1 - (itemPedido.vlPctDesconto3 / 100))));
				itemPedido.vlItemPedido = itemPedido.vlItemPedido - itemPedido.vlDesconto3;
				itemPedido.vlDesconto3 *= itemPedido.getQtItemFisico();
			}
			//-Recalcula o item
			itemPedido.vlTotalItemPedido = ValueUtil.round(itemPedido.vlItemPedido * itemPedido.getQtItemFisico());
			ItemPedidoService.getInstance().loadDadosItemPedido(itemPedido, pedido);
			ItemPedidoService.getInstance().calculaMargemContribuicaoItemRegra2(itemPedido);
			ItemPedidoPdbxDao.getInstance().update(itemPedido);
		}
		return true;
	}

	public void removeDescontoReabrirPedido(Pedido pedido) throws SQLException {
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			itemPedido.vlDesconto = 0;
			itemPedido.vlDescontoCondicao = 0;
			itemPedido.vlPctDesconto2 = 0;
			itemPedido.vlDesconto2 = 0;
			itemPedido.vlDesconto3 = 0;
			ItemPedidoService.getInstance().calculate(itemPedido, pedido);
		}
	}

}