package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.HistVendaListTabPreco;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.HistVendaListTabPrecoDao;
import totalcross.util.Vector;

public class HistVendaListTabPrecoService extends CrudService {
	
	private static HistVendaListTabPrecoService instance;
	
	public static HistVendaListTabPrecoService getInstance() {
		if (instance == null) {
			instance = new HistVendaListTabPrecoService();
		}
		return instance;
	}

	@Override
	public void validate(BaseDomain domain) throws SQLException {
	}

	@Override
	protected CrudDao getCrudDao() {
		return HistVendaListTabPrecoDao.getInstance();
	}
	
	public Vector findAllDetalhesByExample(HistVendaListTabPreco filter, ItemPedido itemEdicao, int cdListaTabPreco) throws SQLException {
		Vector list = HistVendaListTabPrecoDao.getInstance().findAllDetalhesByExample(filter);
		if (itemEdicao != null && ValueUtil.valueEquals(filter.cdGrupoProduto1, itemEdicao.getProduto().cdGrupoProduto1)) {
			int size = list.size();
			int i = 0;
			for (; i < size; i++) {
				HistVendaListTabPreco hist = (HistVendaListTabPreco) list.items[i];
				if (hist.cdListaTabelaPreco == cdListaTabPreco) {
					ItemPedido item = findItemOriginal(itemEdicao);
					if (item != null) {
						hist.qtRealizado -= item.getQtItemFisico();
						hist.vlRealizado -= item.vlTotalItemPedido;
					}
					hist.qtRealizado += itemEdicao.getQtItemFisico();
					hist.vlRealizado += itemEdicao.vlTotalItemPedido;
					break;
				}
			}
			if (i >= size) {
				HistVendaListTabPreco histEdicao = (HistVendaListTabPreco)filter.clone();
				histEdicao.qtRealizado = itemEdicao.getQtItemFisico();
				histEdicao.vlRealizado = itemEdicao.vlTotalItemPedido;
				histEdicao.cdListaTabelaPreco = cdListaTabPreco;
				histEdicao.dsListaTabelaPreco = ListaTabelaPrecoService.getInstance().getDsListaTabelaPreco(histEdicao.cdEmpresa, histEdicao.cdRepresentante, cdListaTabPreco);
				list.addElement(histEdicao);
			}
		}
		return list;
	}
	
	public Vector findAllHistVendasByExample(HistVendaListTabPreco filter, ItemPedido itemEdicao, int cdListaTabPreco) throws SQLException {
		Vector listTabelasRealizada = findAllByExample(filter);
		filter.media = true;
		Vector listTabelasMedia = findAllByExample(filter);
		int size = listTabelasRealizada.size();
		int index = 0;
		boolean itemEdicaoOnList = false;
		for (int i = 0; i < size; i++) {
			HistVendaListTabPreco hist = (HistVendaListTabPreco) listTabelasRealizada.items[i];
			if ((index = listTabelasMedia.indexOf(listTabelasRealizada.items[i])) >= 0) {
				HistVendaListTabPreco histMedia = (HistVendaListTabPreco) listTabelasMedia.items[index];
				hist.mediaQtRealizado = histMedia.qtRealizado;
				hist.mediaVlRealizado = histMedia.vlRealizado;
			}
			hist.cdListaTabelaPreco = cdListaTabPreco;
			if (itemEdicao != null && ValueUtil.valueEquals(hist.cdGrupoProduto1, itemEdicao.getProduto().cdGrupoProduto1)) {
				ItemPedido item = findItemOriginal(itemEdicao);
				if (item != null) {
					hist.qtRealizado -= item.getQtItemFisico();
					hist.vlRealizado -= item.vlTotalItemPedido;
				}
				hist.vlRealizado += itemEdicao.vlTotalItemPedido;
				hist.qtRealizado += itemEdicao.getQtItemFisico();
				itemEdicaoOnList = true;
			}
		}
		if (!itemEdicaoOnList && itemEdicao != null) {
			filter.grupoProduto1 = GrupoProduto1Service.getInstance().findGrupoProduto1ByItemPedido(itemEdicao);
			filter.qtRealizado = itemEdicao.getQtItemFisico();
			filter.vlRealizado = itemEdicao.vlTotalItemPedido;
			filter.cdGrupoProduto1 = filter.grupoProduto1 != null ? filter.grupoProduto1.cdGrupoproduto1 : null;
			listTabelasRealizada.addElement(filter);
		}
		return listTabelasRealizada;
	}
	
	private ItemPedido findItemOriginal(ItemPedido item) {
		Vector itemPedidoList = item.pedido.itemPedidoList;
		int index = itemPedidoList.indexOf(item);
		return index == -1 ? null : (ItemPedido)itemPedidoList.elementAt(index);
	}

}
