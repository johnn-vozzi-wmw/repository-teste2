package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescontoGrupo;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.DescontoGrupoService;
import br.com.wmw.lavenderepda.business.service.GrupoDescProdService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListDescQtdeGrupoProdutoWindow extends WmwWindow {

	public DescontoGrupo descontoGrupoProduto;
	private Vector domainList;
	private boolean showBtSemDesconto;
	private BaseGridEdit grid;
	private LabelValue lvDsProduto, lvDsUnidade, lvDsTabelaPreco, lvDsGrupoProduto, lvNuItensGrupoPedido;
	private LabelName lbDsProduto, lbDsUnidade, lbDsTabelaPreco, lbDsGrupoProduto, lbNuItensGrupoPedido;
	private ButtonPopup btCancelar, btSemDesc;
	public boolean isCanceled;
	private ItemPedido itemPedido;

	public ListDescQtdeGrupoProdutoWindow(ItemPedido itemPedido, Vector domainList, String dsUnidade, boolean showBtSemDesconto) throws SQLException {
		super(Messages.DESCONTOGRUPO_NOME_ENTIDADE);
		this.domainList = domainList;
		this.itemPedido = itemPedido;
		descontoGrupoProduto = null;
		this.showBtSemDesconto = showBtSemDesconto;
		scrollable = false;
		initializeComponents(itemPedido, dsUnidade);
		setDefaultRect();
	}

	private void initializeComponents(ItemPedido itemPedido, String dsUnidade) throws SQLException {
		lbDsProduto = new LabelName(Messages.PRODUTO_NOME_ENTIDADE, RIGHT);
		lvDsProduto = new LabelValue(itemPedido.getProduto().dsProduto);
		lbDsUnidade = new LabelName(Messages.METAS_UNIDADE, RIGHT);
		lvDsUnidade = new LabelValue(dsUnidade);
		lbDsTabelaPreco = new LabelName(Messages.TABELAPRECO_NOME_TABELA, RIGHT);
		lvDsTabelaPreco = new LabelValue(TabelaPrecoService.getInstance().getDsTabelaPreco(itemPedido.cdTabelaPreco));
		lbDsGrupoProduto = new LabelName(Messages.GRUPOPRODUTO_GRUPO_PRODUTO, RIGHT);
		lvDsGrupoProduto = new LabelValue(getCdGrupoProduto());
		lbNuItensGrupoPedido = new LabelName(Messages.DESCONTOGRUPO_MSG_QT_MESMO_PEDIDO, RIGHT);
		lvNuItensGrupoPedido = new LabelValue(StringUtil.getStringValueToInterface(getNuItensGrupoPedido()));
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btSemDesc = new ButtonPopup(Messages.DESCONTOQUANTIDADE_BOTAO_SEM_DESCONTO);
	}

	private String getCdGrupoProduto() throws SQLException {
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			return GrupoDescProdService.getInstance().getDsGrupoProduto(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdProduto, itemPedido.getProduto().cdGrupoDescProd);
		}
		return GrupoProduto1Service.getInstance().getDsGrupoProduto(itemPedido.getProduto().cdGrupoProduto1);
	}
	
	private double getNuItensGrupoPedido() throws SQLException {
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			return GrupoDescProdService.getInstance().getQtItensByGrupoProdutoNoPedido(itemPedido.pedido, itemPedido.getProduto().cdGrupoDescProd);
		}
		return GrupoProduto1Service.getInstance().getQtItensByGrupoProdutoNoPedido(itemPedido.pedido, itemPedido.getProduto().cdGrupoProduto1);
	}

	public void initUI() {
		try {
			super.initUI();
			int width = lbNuItensGrupoPedido.getPreferredWidth();
			if (showBtSemDesconto) {
				addButtonPopup(btSemDesc);
			}
			addButtonPopup(btCancelar);
			BaseScrollContainer bs = new BaseScrollContainer(true, false);
			UiUtil.add(bs, lbDsGrupoProduto, LEFT + WIDTH_GAP, TOP + HEIGHT_GAP, width, UiUtil.getLabelPreferredHeight());
			UiUtil.add(bs, lvDsGrupoProduto, AFTER + WIDTH_GAP, SAME);
			int heightBs = UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
			
			UiUtil.add(bs, lbDsProduto, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, width, UiUtil.getLabelPreferredHeight());
			UiUtil.add(bs, lvDsProduto, AFTER + WIDTH_GAP, SAME);
			heightBs += UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
			
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				UiUtil.add(bs, lbDsUnidade, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, width, UiUtil.getLabelPreferredHeight());
				UiUtil.add(bs, lvDsUnidade, AFTER + WIDTH_GAP, SAME);
				heightBs += UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
			}
			
			if (LavenderePdaConfig.usaDescontoQtdeGrupoPorTabelaPreco) {
				UiUtil.add(bs, lbDsTabelaPreco, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, width, UiUtil.getLabelPreferredHeight());
				UiUtil.add(bs, lvDsTabelaPreco, AFTER + WIDTH_GAP, SAME);
				heightBs += UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
			}
			
			UiUtil.add(bs, lbNuItensGrupoPedido, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, width, UiUtil.getLabelPreferredHeight());
			heightBs += UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
			
			UiUtil.add(bs, lvNuItensGrupoPedido, AFTER + WIDTH_GAP, SAME);
			
			UiUtil.add(this, bs, getLeft(), getTop(), FILL - HEIGHT_GAP, heightBs);
			prepareGridEdit();
	        UiUtil.add(this, grid, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, FILL - WIDTH_GAP, FILL - footerH);
	        list();
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	private void prepareGridEdit() {
		GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, -30, LEFT),
            new GridColDefinition(Messages.DESCONTO_LABEL_VLPCTDESCONTO, -30, LEFT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, -30, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton);
	}
	
	@Override
	protected void addBtFechar() {
	}

	public void onWindowEvent(Event event) throws java.sql.SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCancelar) {
					isCanceled = true;
					unpop();
				} else if (event.target == btSemDesc) {
					isCanceled = false;
					unpop();
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if ((event.target == grid) && (grid.getSelectedIndex() != -1)) {
					descontoGrupoProduto = getSelectedDomainOnGrid();
					unpop();
				}
				break;
			}
		}
	}

	public void list() throws java.sql.SQLException {
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			Vector domainList = this.domainList;
			listSize = domainList.size();
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
				grid.setItems(gridItems);
				domainList = null;
			}
		} finally {
			msg.unpop();
		}
	}

	public void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
        DescontoGrupo descGrupoProduto = (DescontoGrupo) domain;
        String[] item = {
                StringUtil.getStringValue(descGrupoProduto.rowKey),
                StringUtil.getStringValueToInterface(descGrupoProduto.qtItem),
                StringUtil.getStringValueToInterface(descGrupoProduto.vlPctDesconto),
                StringUtil.getStringValueToInterface(DescontoGrupoService.getInstance().getVlDescVlBaseItemPedido((ItemPedido) itemPedido.clone(), descGrupoProduto.vlPctDesconto))};
        return item;
    }

	protected DescontoGrupo getSelectedDomainOnGrid() {
        DescontoGrupo descGrupoProduto = new DescontoGrupo();
        String[] item = grid.getSelectedItem();
        descGrupoProduto.rowKey = item[0];
        descGrupoProduto.qtItem = ValueUtil.getDoubleValue(item[1]);
        descGrupoProduto.vlPctDesconto = ValueUtil.getDoubleValue(item[2]);
        descGrupoProduto.vlDesconto = ValueUtil.getDoubleValue(item[3]);
        return descGrupoProduto;
	}
}
