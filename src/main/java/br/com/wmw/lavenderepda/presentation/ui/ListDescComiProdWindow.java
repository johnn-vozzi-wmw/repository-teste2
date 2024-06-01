package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescComiFaixa;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DescComiFaixaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class ListDescComiProdWindow extends WmwWindow {

	private Pedido pedido;
	private ItemPedido itemPedido;
	private BaseGridEdit grid;
	private LabelContainer sessionProduto;
	public DescComiFaixa descComiProd;
	public boolean selecionouItemGrid;
	public boolean inModeSelection = true;
	public int gridSize;

	public ListDescComiProdWindow(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		super(Messages.DESCCOMIPROD_TITULO_CADASTRO);
		this.pedido = pedido;
		this.itemPedido = itemPedido;
		sessionProduto = new LabelContainer(this.itemPedido.getProduto().toString());
		//--
		makeUnmovable();
		setRect(8, 8);
	}

	private Vector getDomainList() throws SQLException {
		return DescComiFaixaService.getInstance().findAllByProduto(pedido.cdCliente, SessionLavenderePda.usuarioPdaRep.cdRepresentante, pedido.cdCondicaoPagamento, pedido.getCliente().cdRamoAtividade, pedido.getCliente().cdCidadeComercial, itemPedido.getProduto().cdProduto);
	}

	public void list() throws java.sql.SQLException {
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			Vector domainList = getDomainList();
			listSize = domainList.size();
			//--
			clearGrid();
			if (listSize > 0) {
				String[][] gridItems;
				String[] item = getItem(domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItem(domainList.items[i]);
				}
				item = null;
				//--
				grid.setItems(gridItems);
			}
			gridSize = grid.size();
			domainList = null;
		} finally {
			msg.unpop();
		}
	}

	public void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}

	//@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		DescComiFaixa desccomiprod = (DescComiFaixa) domain;
		String[] item = {
				StringUtil.getStringValue(desccomiprod.rowKey),
				StringUtil.getStringValueToInterface(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? ValueUtil.getIntegerValue(desccomiprod.qtItem) : desccomiprod.qtItem),
				StringUtil.getStringValueToInterface(desccomiprod.vlPctDesconto),
				StringUtil.getStringValueToInterface(desccomiprod.vlPctComissao) };
		return item;
	}

	//@Override
	protected String getSelectedRowKey() {
		String[] item = grid.getSelectedItem();
		return item[0];
	}

	//@Override
	protected DescComiFaixa getSelectedDomainOnGrid() {
		DescComiFaixa desccomiprod = new DescComiFaixa();
		String[] item = grid.getSelectedItem();
		desccomiprod.rowKey = item[0];
		desccomiprod.qtItem = ValueUtil.getDoubleValue(item[1]);
		desccomiprod.vlPctDesconto = ValueUtil.getDoubleValue(item[2]);
		desccomiprod.vlPctComissao = ValueUtil.getDoubleValue(item[3]);
		return desccomiprod;
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		UiUtil.add(this, sessionProduto, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, -0, LEFT),
			new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, -25, CENTER),
			new GridColDefinition(Messages.DESCONTO_LABEL_VLPCTDESCONTO, -40, CENTER),
			new GridColDefinition(Messages.DESCCOMI_LABEL_VLPCTCOMISSAO, -35, CENTER) };
		grid = UiUtil.createGridEdit(gridColDefiniton);
		UiUtil.add(this, grid);
		grid.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
		list();
		selectDescAtualNaGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void selectDescAtualNaGrid() {
		if (itemPedido.descComissaoProd != null) {
			int size = grid.size();
			for (int i = 0; i < size; i++) {
				String cellText = grid.getCellText(i, 0);
				if (itemPedido.descComissaoProd.rowKey.equals(cellText)) {
					grid.setSelectedIndex(i);
					descComiProd = itemPedido.descComissaoProd;
					break;
				}
			}
		}
	}

	protected void btFecharClick() throws SQLException {
		if ((descComiProd != null) || !inModeSelection) {
			selecionouItemGrid = false;
			unpop();
		} else {
			UiUtil.showWarnMessage(Messages.DESCCOMI_OBRIGATORIO_SEM_DESC);
		}
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case PenEvent.PEN_UP: {
				if (event.target instanceof BaseGridEdit) {
					if (inModeSelection && grid.dispararAcaoClickUnico && (grid.getSelectedIndex() != -1)) {
						grid.dispararAcaoClickUnico = false;
						selecionouItemGrid = true;
						descComiProd = getSelectedDomainOnGrid();
						unpop();
					}
				}
				break;
			}
		}
	}

	public void popup() {
		if (gridSize > 0) {
			super.popup();
		}
	}

}