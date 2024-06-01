package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ItemPedidoErpDifService;
import br.com.wmw.lavenderepda.business.service.PedidoErpDifService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelDiferencasPedidoWindow  extends WmwListWindow {

	private ScrollTabbedContainer tab;
	private Pedido pedido;
	private LabelValue edNuPedidoERP;
	private LabelValue edStatusPedido;
	private EditMemo edDsObPedOrg;
	private EditMemo edDsObPedErp;
	private LabelName lbNuPedidoErp;
	private LabelName lbStatus;
	private ButtonPopup btCriarPedidoDiferenca;
	private Vector itemPedidoErpList;
	public Vector itemPedidoErpDifComCorteList;
	public boolean btCriarPedidoDiferencaClicado;
	
	private StringBuilder infos = new StringBuilder();

	public RelDiferencasPedidoWindow(Pedido pedidoErp) throws SQLException {
		super(Messages.PEDIDO_DIFERENCAS_RELATORIO);
		scrollable = false;
		singleClickOn = true;
		pedido = pedidoErp;
		edNuPedidoERP = new LabelValue();
		lbNuPedidoErp = new LabelName(Messages.REL_DIF_PEDIDO_LABEL_NUPEDIDO_ERP);
		lbStatus = new LabelName(Messages.PEDIDO_LABEL_CDSTATUSPEDIDO);
		edStatusPedido = new LabelValue();
		edDsObPedOrg = new EditMemo("", 3, Pedido.MAX_LENGTH_DS_OBSERVACAO);
		edDsObPedErp = new EditMemo("", 3, Pedido.MAX_LENGTH_DS_OBSERVACAO);
		btCriarPedidoDiferenca = new ButtonPopup(Messages.REL_DIF_BOTAO_CRIAR_PEDIDO_DIFERENCA);
		itemPedidoErpList = ItemPedidoErpDifService.getInstance().findAllByPedidoErp(pedidoErp);
		carregaItemPedidoComCorte();
		btCriarPedidoDiferencaClicado = false;
		if (LavenderePdaConfig.isShowDifItemPedido()) {
			configListContainer();
		}
		setDefaultRect();
	}

	private void configListContainer() {
		int itemCount = 2;
		itemCount += LavenderePdaConfig.isShowDiferencaPedidoQtdItemPedido() ? 2 : 0;
		itemCount += LavenderePdaConfig.isShowDiferencaPedidoObsItemPedido() ? 2 : 0;
		itemCount += LavenderePdaConfig.usaUnidadeAlternativa ? 2 : 0;
		listContainer = new GridListContainer(itemCount, 2);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		if (itemCount > 2) {
			listContainer.setColPosition(3, RIGHT);
		}
		if (itemCount > 4) {
			listContainer.setColPosition(5, RIGHT);
		}
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return itemPedidoErpList;
	}
	
	@Override
	protected void addBtFechar() {
		if (isGeraPedidoDiferenca()) {
			addButtonPopup(btCriarPedidoDiferenca);
		}
		super.addBtFechar();
	}
	
	private boolean isGeraPedidoDiferenca() {
		return !pedido.isPossuiPedidoDiferenca() && ValueUtil.isNotEmpty(itemPedidoErpDifComCorteList);
	}
	
	private void carregaItemPedidoComCorte() {
		if (!LavenderePdaConfig.geraNovoPedidoDiferencas) return;
		itemPedidoErpDifComCorteList = new Vector();
		int size = itemPedidoErpList.size();
		for (int i = 0; i < size; i++) {
			ItemPedidoErpDif itemPedidoErpDif = (ItemPedidoErpDif) itemPedidoErpList.items[i];
			if (itemPedidoErpDif.qtItemfisicoOrg > itemPedidoErpDif.qtItemFisicoErp) {
				itemPedidoErpDifComCorteList.addElement(itemPedidoErpDif);
			}
		}
	}

	@Override
	protected String[] getItem(Object domain) throws java.sql.SQLException {
		ItemPedidoErpDif itemPedidoErpDif = (ItemPedidoErpDif) domain;

		String[] container = new String[8];
		infos.setLength(0);
		
		infos.append(produtoToString(itemPedidoErpDif, " - "));
		container[0] = infos.toString();
		infos.setLength(0);
		
		container[1] = ValueUtil.VALOR_NI;
		int pos = 2;
		if (LavenderePdaConfig.isShowDiferencaPedidoQtdItemPedido()) {
			infos.append(Messages.REL_DIF_PEDIDO_LABEL_QTD_ORIGINAL + ": ");
			infos.append(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface(itemPedidoErpDif.qtItemfisicoOrg, 0) : StringUtil.getStringValueToInterface(itemPedidoErpDif.qtItemfisicoOrg));
			container[pos] = infos.toString();
			infos.setLength(0);
			pos++;
			//--
			infos.append(Messages.REL_DIF_PEDIDO_LABEL_QTD_ATUAL + ": ");
			infos.append(LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? StringUtil.getStringValueToInterface(itemPedidoErpDif.qtItemFisicoErp, 0) : StringUtil.getStringValueToInterface(itemPedidoErpDif.qtItemFisicoErp));
			container[pos] = infos.toString();
			infos.setLength(0);
			pos++;
		}
		if (LavenderePdaConfig.isShowDiferencaPedidoObsItemPedido()) {
			container[pos] = Messages.ITEMPEDIDO_LABEL_DSOBSERVACAO_SHORT + ": ";
			pos++;
			container[pos] = StringUtil.getStringValue(itemPedidoErpDif.dsObservacaoErp);
			pos++;
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			container[pos] = Messages.METAS_UNIDADE + ": ";
			pos++;
			container[pos] = itemPedidoErpDif.cdUnidade != null ? itemPedidoErpDif.cdUnidade : "";
		}
		return container;
	}

	private String produtoToString(ItemPedidoErpDif itemPedidoErpDif, final String separador) throws SQLException {
		StringBuffer strb = new StringBuffer();
		Produto produto = ProdutoService.getInstance().getProduto(itemPedidoErpDif.cdProduto);
		if ((produto == null || ValueUtil.isEmpty(produto.cdProduto) && itemPedidoErpDif.produto != null)) {
			if (!LavenderePdaConfig.ocultaColunaCdProduto) {
				return itemPedidoErpDif.produto.toString();
			} else {
				return itemPedidoErpDif.produto.dsProduto;
			}
		}
		if (!LavenderePdaConfig.ocultaColunaCdProduto) {
			strb.append(produto.cdProduto + separador).append(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto));
		} else {
			strb.append(StringUtil.getStringValue(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto)));
		}
		return strb.toString();
	}

	//@Override
	protected String getSelectedRowKey() {
		return ((BaseListContainer.Item) listContainer.getSelectedItem()).id;
	}

	//@Override
	protected CrudService getCrudService() throws java.sql.SQLException {
		return ItemPedidoErpDifService.getInstance();
	}

	//@Override
	protected BaseDomain getDomainFilter() {
		return new ItemPedidoErpDif();
	}

	//@Override
	protected void onFormStart() throws SQLException {
		int size = 0;
		if (LavenderePdaConfig.isShowDifPedido()) size++;
		if (LavenderePdaConfig.isShowDifItemPedido()) size++;

		String[] tabs = new String[size];
		int pos = 0;
		if (LavenderePdaConfig.isShowDifPedido()) {
			tabs[pos] = Messages.REL_DIF_PEDIDO_LABEL_OBS_PEDIDO; 
			pos++;
		}
		if (LavenderePdaConfig.isShowDifItemPedido()) {
			tabs[pos] = Messages.REL_DIF_PEDIDO_LABEL_ITENS;
		}
		
		tab = new ScrollTabbedContainer(tabs);
		UiUtil.add(this, tab, LEFT, getNextY(), FILL, FILL - footerH);
		Container tabPanel = null;
		if (LavenderePdaConfig.isShowDifPedido()) {
			tabPanel = tab.getContainer(0);
			UiUtil.add(tabPanel, lbNuPedidoErp, edNuPedidoERP, getLeft(), getNextY(), PREFERRED);
			if (LavenderePdaConfig.isShowDiferencaPedidoStatusPedido()) {
				UiUtil.add(tabPanel, lbStatus, edStatusPedido, getLeft(), getNextY(), PREFERRED);
			}
			if (LavenderePdaConfig.isShowDiferencaPedidoObsPedido()) {
				UiUtil.add(tabPanel, new LabelName(Messages.ORIGEMPEDIDO_ORIGINAL), edDsObPedOrg, getLeft(), getNextY(), PREFERRED - WIDTH_GAP_BIG);
				UiUtil.add(tabPanel, new LabelName(Messages.ORIGEMPEDIDO_ERP), edDsObPedErp, getLeft(), getNextY(), PREFERRED - WIDTH_GAP_BIG);
			}
			PedidoErpDif pedidoerpdif = new PedidoErpDif();
			pedidoerpdif.cdEmpresa = pedido.cdEmpresa;
			pedidoerpdif.cdRepresentante = pedido.cdRepresentante;
			pedidoerpdif.nuPedido = pedido.nuPedido;
			pedidoerpdif.flOrigemPedido = pedido.flOrigemPedido;
			pedidoerpdif = (PedidoErpDif)PedidoErpDifService.getInstance().findByRowKey(pedidoerpdif.getRowKey());
			edNuPedidoERP.setValue(pedido.nuPedido);
			if (pedidoerpdif != null) {
				edStatusPedido.setValue(StatusPedidoPdaService.getInstance().getDsStatusPedido(pedidoerpdif.cdStatusPedido));
				edDsObPedOrg.setValue(pedidoerpdif.dsObservacaoorg);
				edDsObPedErp.setValue(pedidoerpdif.dsObservacaoerp);
			} else {
				edStatusPedido.setValue(pedido.statusPedidoPda.dsStatusPedido);
				tab.setActiveTab(0);
			}
		}
		if (LavenderePdaConfig.isShowDifItemPedido()) {
			tabPanel = tab.getContainer(pos);
			UiUtil.add(tabPanel, listContainer, LEFT, TOP + HEIGHT_GAP, FILL, FILL);
		}
	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCriarPedidoDiferenca) {
					btCriarPedidoDiferencaClick();
				}
			}
		}
	}
	
	private void btCriarPedidoDiferencaClick() throws SQLException {
		btCriarPedidoDiferencaClicado = true;
		btFecharClick();
	}
	
	//@Override
	public void setEnabled(boolean enabled) {
		edDsObPedErp.setEnabled(enabled);
		edDsObPedOrg.setEnabled(enabled);
		edDsObPedOrg.drawBackgroundWhenDisabled = true;
		edDsObPedErp.drawBackgroundWhenDisabled = true;
	}
	
	//@Override
	protected void onPopup() {
		super.onPopup();
		setEnabled(false);
	}
	
	//@Override
	public void detalhesClick() throws SQLException {
		RelDiferencasItemPedidoWindow relDiferencasItemPedidoWindow = new RelDiferencasItemPedidoWindow((ItemPedidoErpDif)getSelectedDomain());
		relDiferencasItemPedidoWindow.popup();
	}

}
