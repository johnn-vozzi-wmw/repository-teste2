package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.sys.Convert;
import totalcross.ui.event.Event;
import totalcross.ui.font.FontMetrics;
import totalcross.util.Vector;

public class ListItemPedidoAvisoDescAcrMaximoForm extends LavendereCrudListForm {
	
	private Vector domainList;
	private Vector itemPedidoSelecionadoList;
	private EditMemo edDescricao;

	public ListItemPedidoAvisoDescAcrMaximoForm(Vector itemPedidoListDescAcrExtrapolado) {
		super(Messages.REL_ITENS_AGUARDANDO_CONFIRMACAO);
		this.domainList = itemPedidoListDescAcrExtrapolado;
		this.itemPedidoSelecionadoList = new Vector(0);
		edDescricao = new EditMemo("@@@@@@@@@@", 3, 255);
		edDescricao.setText(Messages.REL_MSG_PRODUTOS_AGUARDANDO_CONFIRMACAO_DESC_ACR_MAX);
		edDescricao.setEditable(false);
		edDescricao.transparentBackground = true;
		edDescricao.drawDots = false;
		constructorListContainer();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new ItemPedido();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemPedidoService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, edDescricao, LEFT + WIDTH_GAP, TOP + WIDTH_GAP, FILL - WIDTH_GAP, PREFERRED + 20);
		UiUtil.add(this, listContainer, LEFT, AFTER + WIDTH_GAP, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		//Sem necessidade de evento até o momento.
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		Vector item = new Vector(0);
		item.addElement(LavenderePdaConfig.ocultaColunaCdProduto ? ValueUtil.VALOR_NI : (StringUtil.getStringValue(itemPedido.cdProduto) + " - "));
		item.addElement(StringUtil.getStringValue(itemPedido.getProduto().dsProduto));
		String dsMotivoAdvertencia = Convert.insertLineBreak(width - getCheckBoxWidth(), fm, itemPedido.dsMotivoAdvertencia);
		String[] dsMotivoLinha =  dsMotivoAdvertencia.split("\n");
		for (int i = 0; i < dsMotivoLinha.length; i++) {
			item.addElement(StringUtil.getStringValue(dsMotivoLinha[i]));
			if (i != dsMotivoLinha.length - 1) {
				item.addElement(ValueUtil.VALOR_NI);
			}
		}
		return (String[]) item.toObjectArray();
	}
	
	@Override
	protected Vector getDomainList() throws SQLException {
		return domainList;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getDomainList();
	}

	private void constructorListContainer() {
		GridListContainer gridListContainer = new GridListContainer(5, 2, true, false, false, true);
		gridListContainer.setColPosition(3, LEFT);
		gridListContainer.setUseSortMenu(false);
		gridListContainer.setBarTopSimple();
		listContainer = gridListContainer;
	}
	
	public Vector getItemPedidoSelecionadosList() {
		return this.itemPedidoSelecionadoList;
	}

	public void preencheListaDeItensSelecionados() {
		int size = listContainer.size();
		for (int i = 0; i < size; i++) {
			BaseListContainer.Item c = (Item) listContainer.getContainer(i);
			//isChecked no framework esta ao contrario
			if (!listContainer.isChecked(i)) {
				itemPedidoSelecionadoList.addElement((ItemPedido) c.domain);
			}
		}
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}
	
	@Override
	protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
		super.setPropertiesInRowList(containerItem, domain);
		String dsToolTip = ((ItemPedido) domain).dsMotivoAdvertencia; 
		containerItem.setToolTip(dsToolTip);
	}
	
	private int getCheckBoxWidth() {
		return listContainer.getLayout().getDefaultRightImageW();
	}

}
