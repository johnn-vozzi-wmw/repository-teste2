package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import totalcross.ui.Control;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelInsercaoItemPedidoOportunidadeWindow extends WmwListWindow {

	private Vector itemList;
	private LabelName lbDescricao;
	private String text = "";
	private boolean showErroInsercaoItem;


	public RelInsercaoItemPedidoOportunidadeWindow(String title, String text, Vector itemList, boolean showErroInsercaoItem) {
		super(title);
		this.itemList = itemList;
		this.text = text;
		this.showErroInsercaoItem = showErroInsercaoItem;
		lbDescricao = new LabelName(text, CENTER);
		setDefaultRect();
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemPedidoService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
		return new ItemPedido();
	}

	//@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return itemList;
	}

	//@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		Object[] itemErro = (Object[]) domain;
		ItemPedido itemPedido = (ItemPedido)itemErro[0];
		if (showErroInsercaoItem) {
			String[] item = {
					StringUtil.getStringValue(itemPedido.cdProduto),
					StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
					StringUtil.getStringValue(itemPedido.getQtItemFisico()),
					StringUtil.getStringValue(itemPedido.vlTotalItemPedido),
					StringUtil.getStringValue(itemErro[1])
			};
			return item;
		} else {
			String[] item = {
					StringUtil.getStringValue(itemPedido.cdProduto),
					StringUtil.getStringValue(itemPedido.getProduto().dsProduto),
					StringUtil.getStringValue(itemPedido.getProduto().cdUnidade),
					StringUtil.getStringValue(itemPedido.getQtItemFisico()),
					StringUtil.getStringValue(itemPedido.vlTotalItemPedido)
			};
			return item;
		}

	}


	//--------------------------------------------------------------

	//@Override
	protected void onFormStart() {
		StringBuffer strBuffer = new StringBuffer();
		String aux = "                                                                                                                                                                        ";
		int ww = fm.stringWidth(aux);
		lbDescricao.setText(MessageUtil.quebraLinhas(text, width - 2));
		UiUtil.add(this, lbDescricao, LEFT, Control.TOP + HEIGHT_GAP, FILL, PREFERRED);
		if (showErroInsercaoItem) {
			GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, 10, Control.LEFT),
				new GridColDefinition(Messages.PRODUTO_LABEL_DSPRODUTO, fm.stringWidth("AAAAAAAAAAAAAAAAAAAA"), Control.LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, fm.stringWidth("9000,00"), Control.LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLTOTAL, fm.stringWidth("9000,00"), Control.LEFT),
				new GridColDefinition(strBuffer.append(Messages.REL_INSERCAO_ERRO).append(aux).toString(), ww, Control.LEFT) };
			grid = UiUtil.createGridEdit(gridColDefiniton);
		} else {
			GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, 10, Control.LEFT),
				new GridColDefinition(Messages.PRODUTO_LABEL_DSPRODUTO, fm.stringWidth("AAAAAAAAAAAAAAAAAAAA"), Control.LEFT),
				new GridColDefinition(Messages.UNIDADE_ABREVIADA, fm.stringWidth("AAAAAA"), Control.LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, fm.stringWidth("9000,00"), Control.LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLTOTAL, fm.stringWidth("9000,00"), Control.LEFT) };
			grid = UiUtil.createGridEdit(gridColDefiniton);
		}
		aux = null;
		UiUtil.add(this, grid);
		grid.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}


	public void screenResized() {
		super.screenResized();
		lbDescricao.setText(MessageUtil.quebraLinhas(text, width - 2));
		lbDescricao.reposition();
		grid.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	protected void onFormEvent(Event event) throws SQLException {

	}

	protected String getSelectedRowKey() {
		return null;
	}


}