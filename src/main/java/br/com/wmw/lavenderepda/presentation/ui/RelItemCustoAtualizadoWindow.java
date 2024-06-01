package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ExpandableContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import totalcross.util.Vector;

public class RelItemCustoAtualizadoWindow extends WmwWindow {

	private Vector listPedido;

	public RelItemCustoAtualizadoWindow(Vector listPedido) {
		super(Messages.TITLE_ITENS_ATUALIZADOS, WmwWindow.MODE_INFO);
		this.listPedido = listPedido;
		makeUnmovable();
		setDefaultRect();
	}

	public void initUI() {
	   try {
		super.initUI();
		if (ValueUtil.isNotEmpty(listPedido)) {
			int size = listPedido.size();
			for (int i = 0; i < size; i++) {
				Pedido pedido = (Pedido) listPedido.items[i];
				if (pedido.atualizouCustoItem) {
					createListItem(Messages.LABEL_PEDIDO + " " + pedido.nuPedido, pedido.itemPedidoList);
				}
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void createListItem(String title, Vector itemPedidoList) throws SQLException {
		ExpandableContainer exp = new ExpandableContainer(title, ColorUtil.infoColor);
		UiUtil.add(this, exp, LEFT, AFTER + 1, FILL);
		if (ValueUtil.isNotEmpty(itemPedidoList)) {
			int size = itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
				if (itemPedido.atualizouCusto) {
					exp.ctMaximizedContainers.setBackColor(ColorUtil.popupBackColor);
					LabelValue lbDescription = new LabelValue();
					lbDescription.autoMultipleLines = true;
					exp.addComponentInMaximizedContainer(lbDescription);
					lbDescription.setMultipleLinesText(Messages.LABEL_ITEM + ": " + itemPedido.getProduto());
				}
   			}
		}
	}

}