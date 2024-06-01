package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.ItemConsignacao;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ItemConsignacaoPdbxDao;
import totalcross.util.Vector;

public class ItemConsignacaoService extends CrudService {

	private static ItemConsignacaoService instance;

	private ItemConsignacaoService() {
		//--
	}

	public static ItemConsignacaoService getInstance() {
		if (instance == null) {
			instance = new ItemConsignacaoService();
		}
		return instance;
	}

	//@Override
	protected CrudDao getCrudDao() {
		return ItemConsignacaoPdbxDao.getInstance();
	}

	public Vector findItemConsignacaoList(Consignacao consignacao) throws SQLException {
		ItemConsignacao itemConsignacaoFilter = new ItemConsignacao();
		itemConsignacaoFilter.cdEmpresa = consignacao.cdEmpresa;
		itemConsignacaoFilter.cdRepresentante = consignacao.cdRepresentante;
		itemConsignacaoFilter.cdConsignacao = consignacao.cdConsignacao;
		itemConsignacaoFilter.cdCliente = consignacao.cdCliente;
		return findAllByExample(itemConsignacaoFilter);
	}

	public void findVlBaseItemTabelaPreco(ItemConsignacao itemConsignacao) throws SQLException {
		ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(itemConsignacao.cdTabelaPreco, itemConsignacao.cdProduto, SessionLavenderePda.getCliente().dsUfPreco);
		double newVlBaseItemPedido = 0;
		if (itemTabelaPreco != null) {
			newVlBaseItemPedido = itemTabelaPreco.vlUnitario;
			if (LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial) {
				if (SessionLavenderePda.getCliente().isEspecial()) {
					newVlBaseItemPedido = itemTabelaPreco.vlUnitarioEspecial;
				}
			}
		}
		itemConsignacao.vlItem = newVlBaseItemPedido;
	}

	//@Override
	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	public ItemPedido convertItemConsignacaoToItemPedido(ItemConsignacao itemConsignacao, Pedido pedido) throws SQLException {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.pedido = pedido;
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		itemPedido.cdProduto = itemConsignacao.cdProduto;
		itemPedido.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ORIGINAL;
		itemPedido.cdTabelaPreco = itemConsignacao.cdTabelaPreco;
		itemPedido.setQtItemFisico(itemConsignacao.getQtItemVenda());
		itemPedido.vlItemPedido = itemConsignacao.vlItem;
		itemPedido.vlBaseItemPedido = itemConsignacao.vlItem;
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		//--
		ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(itemPedido.cdTabelaPreco, itemPedido.cdProduto, itemPedido.cdUfClientePedido);
		if (itemTabelaPreco != null) {
			itemPedido.vlBaseItemTabelaPreco = itemTabelaPreco.vlUnitario;
		}
		return itemPedido;
	}
}