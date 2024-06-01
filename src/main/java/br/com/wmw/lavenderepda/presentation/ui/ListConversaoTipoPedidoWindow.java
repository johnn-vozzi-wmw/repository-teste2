package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class ListConversaoTipoPedidoWindow extends LavendereWmwListWindow {
	
	protected ButtonPopup btLimpar;
	protected EditFiltro edFiltro;
	protected Pedido pedido;
	public TipoPedido tipoPedidoSelecionado;
	
	public ListConversaoTipoPedidoWindow(Pedido pedido) throws SQLException {
		super(Messages.CAD_COVERSAO_TIPO_PEDIDO);
		constructorListContainer();
		btLimpar = new ButtonPopup("  " + FrameworkMessages.BOTAO_LIMPAR + "  ");
		edFiltro = new EditFiltro("999999999", 50);
		singleClickOn = true;
		this.pedido = pedido;
		setDefaultRect();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Vector tipoPedList = TipoPedidoService.getInstance().getTipoPedidoList((TipoPedido) domain);
		if(pedido.isConverteTipoPedidoReplicacao()) {
			for(int i=0; i < tipoPedList.size(); i++) {
				if(pedido.getTipoPedido().equals(tipoPedList.items[i])) {
					tipoPedList.removeElement(tipoPedList.items[i]);
					break;
				}
			}
		}
		return tipoPedList;
				
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		TipoPedido tipoPedido = (TipoPedido) domain;
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(tipoPedido.cdTipoPedido));
		item.addElement(StringUtil.getStringValue(" - " + tipoPedido.dsTipoPedido));
		return (String[]) item.toObjectArray();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return TipoPedidoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		TipoPedido filter = new TipoPedido();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		filter.dsTipoPedido = edFiltro.getText();
		return filter;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName("Filtro"), edFiltro, getLeft(), getNextY());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case EditIconEvent.PRESSED:
			if (event.target == edFiltro) {
				list();
			}
			break;
		case KeyEvent.SPECIAL_KEY_PRESS: 
			if (event.target == edFiltro && ((KeyEvent)event).isActionKey()) {
				list();
				if (listContainer != null && listContainer.size() == 0) {
					edFiltro.requestFocus();
				}
			} 
			break;
		}
	}
	
	private void constructorListContainer() {
    	configListContainer("CDTIPOPEDIDO");
		listContainer = new GridListContainer(2, 2);
		String[][] matriz = new String[2][2];
		matriz[0][0] = Messages.CODIGO;
		matriz[0][1] = "CDTIPOPEDIDO";
		matriz[1][0] = Messages.DESCRICAO;
		matriz[1][1] = "DSTIPOPEDIDO";
		listContainer.setColsSort(matriz);
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
	}
	
	public void singleClickInList() throws SQLException {
		TipoPedido tipoPedido = (TipoPedido) getCrudService().findByRowKey(getSelectedRowKey());
		validateTipoPedidoSelecionado(tipoPedido);
	}
	
	private void validateTipoPedidoSelecionado(TipoPedido tipoPedido) throws SQLException {
		if (tipoPedido != null) {
			if (!pedido.isConverteTipoPedidoReplicacao() && ValueUtil.valueEquals(pedido.getTipoPedido().flIgnoraControleVerba,"N") && ValueUtil.valueEquals(tipoPedido.flIgnoraControleVerba,"S")) {
				UiUtil.showErrorMessage(Messages.CAD_COVERSAO_TIPO_PEDIDO_ERRO_CONVERTER_TIPO_PEDIDO_VERBA);
				return;
			}
			if (ValueUtil.valueEquals(tipoPedido.flBonificacao,"S")) {
				UiUtil.showErrorMessage(Messages.CAD_COVERSAO_TIPO_PEDIDO_ERRO_CONVERTER_TIPO_PEDIDO_BONIFICACAO);
				return;
			}
			if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.CAD_COVERSAO_TIPO_PEDIDO_CONFIRMA_CONVERCAO, tipoPedido.dsTipoPedido))){
				tipoPedidoSelecionado = tipoPedido;
				unpop();
			}
			return;
		}
	}
	
}
