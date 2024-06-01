package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.NotaCredito;
import br.com.wmw.lavenderepda.business.service.NotaCreditoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.ListContainerEvent;
import totalcross.util.IntVector;
import totalcross.util.Vector;

public class ListNotaCreditoWindow extends LavendereWmwListWindow {
	
	private ButtonPopup  btConfirmar;
	
	private NotaCredito notaCreditoFilter;
	private Vector notaCreditoSelecionadaList;
	private boolean cancelaPedido;
	private LabelValue lbInformacao;

	public ListNotaCreditoWindow(String cdEmpresa, String cdRepresentante, String cdCliente) {
		super(Messages.NOTAS_CREDITO);
		notaCreditoFilter = new NotaCredito(cdEmpresa, cdRepresentante, cdCliente);
		listContainer = new GridListContainer(3, 2);
		listContainer.setCheckable(true);
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColTotalizerRight(2, Messages.VALOR_NOTA_CREDITO_SELECIONADO);
		listContainer.setColsSort(new String[][]{{Messages.NOTA_CREDITO_LABEL_NUMERO, "CDNOTACREDITO"}, {Messages.PEDIDO_LABEL_VLTOTALPEDIDO, "VLNOTACREDITO"}});
		listContainer.setUseSortMenu(true);
		configListContainer("CDNOTACREDITO");
		listContainer.atributteSortSelected  = sortAtributte;
		listContainer.sortAsc = sortAsc;
		lbInformacao = new LabelValue(Messages.NOTA_CREDITO_LABEL_TITULO, CENTER);
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		btFechar.setText(Messages.BOTAO_CANCELAR);
		setDefaultRect();
		atualizaTotalizadores();
		notaCreditoSelecionadaList = new Vector();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getCrudService().findAllByExample(domain);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		NotaCredito notaCredito = (NotaCredito) domain;
		 String[] item = {
	        		Messages.NOTA_CREDITO_LABEL_NUMERO + " " + StringUtil.getStringValue(notaCredito.cdNotaCredito),
	                " ",
	                Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(notaCredito.vlNotaCredito)};
		return item;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return NotaCreditoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return notaCreditoFilter;
	}
	
	@Override
	public void screenResized() {
		super.screenResized();
		lbInformacao.setText(MessageUtil.quebraLinhas(lbInformacao.getText(), width - 20));
		lbInformacao.reposition();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	protected void onFormStart() throws SQLException {
		lbInformacao.setText(MessageUtil.quebraLinhas(lbInformacao.getText(), width - 20));
    	UiUtil.add(this, lbInformacao, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}
	
	@Override
	protected void addButtons() {
		addButtonPopup(btConfirmar);
		super.addButtons();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == listContainer.ckCheckAll) {
					atualizaTotalizadores();
				} else if (event.target == btConfirmar) {
					confirmarClick();
				}  else if (event.target == listContainer.popupMenuOrdenacao) {
					atualizaTotalizadores();
				}
				break;
			}
			case ListContainerEvent.RIGHT_IMAGE_CLICKED_EVENT : {
				atualizaTotalizadores();
				break;
			}
		}
	}
	
	@Override
	protected void btFecharClick() throws SQLException {
		getNotaCreditoSelecionadaList().removeAllElements();
		if (LavenderePdaConfig.utilizaNotasCreditoEObrigaSelecaoDeNotaCredito()) {
			cancelaPedido = UiUtil.showConfirmYesNoMessage(Messages.SELECIONE_NOTA_CREDITO_CANCELAR);
			if (!cancelaPedido) return;
		} 
		super.btFecharClick();
	}
	
	private void confirmarClick() throws SQLException {
		carregaNotaCreditoSeleciona();
		if (ValueUtil.isEmpty(getNotaCreditoSelecionadaList())) {
			UiUtil.showErrorMessage(Messages.SELECIONE_NOTA_CREDITO);
		} else {
			unpop();
		}
	}
	
	private void carregaNotaCreditoSeleciona() throws SQLException {
		getNotaCreditoSelecionadaList().removeAllElements();
		int[] checkedItens = listContainer.getCheckedItens();
		int size = checkedItens.length;
		for (int i = 0; i < size; i++) {
			int posicaoItemSelecionado = checkedItens[i];
			Container container = listContainer.getContainer(posicaoItemSelecionado);
			BaseListContainer.Item item = ((BaseListContainer.Item)container);
			NotaCredito notaCreditoSeleciona = (NotaCredito) NotaCreditoService.getInstance().findByRowKey(item.id);
			getNotaCreditoSelecionadaList().addElement(notaCreditoSeleciona);
		}
	}

	private void atualizaTotalizadores() {
		IntVector itensComChecked = listContainer.checkedItens;
		BaseListContainer baseListContainer = listContainer.getBaseListContainer();
		int size = baseListContainer.size();
		for (int i = 0; i < size; i++) {
			Container container = listContainer.getContainer(i);
			BaseListContainer.Item item = ((BaseListContainer.Item)container);
			item.ignoreTotalizer = !itensComChecked.contains(i);
		}
		screenResized();
	}

	protected Vector getNotaCreditoSelecionadaList() {
		return notaCreditoSelecionadaList;
	}

	protected boolean isCancelaPedido() {
		return cancelaPedido;
	}

}
