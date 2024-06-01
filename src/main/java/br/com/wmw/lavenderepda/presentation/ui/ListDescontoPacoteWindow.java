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
import br.com.wmw.lavenderepda.business.domain.DescontoPacote;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pacote;
import br.com.wmw.lavenderepda.business.service.DescontoGrupoService;
import br.com.wmw.lavenderepda.business.service.DescontoPacoteService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.PacoteComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListDescontoPacoteWindow extends WmwWindow {

	public DescontoPacote descontoPacote;
	private Vector domainList;
	private BaseGridEdit grid;
	private LabelValue lvDsProduto, lvDsUnidade, lvDsTabelaPreco;
	private LabelName lbDsPacote, lbDsProduto, lbDsUnidade, lbDsTabelaPreco;
	private ButtonPopup btCancelar, btSemDesc;
	public boolean isCanceled, showBtSemDesconto, inGridClick;
	private ItemPedido itemPedido;
	private PacoteComboBox cbPacotesItem;
	
	@Override
	protected void addBtFechar() {}

	public ListDescontoPacoteWindow(ItemPedido itemPedido, String dsUnidade, final boolean showBtSemDesconto, final boolean inGridClick) throws SQLException {
		super(Messages.DESCONTO_PACOTE_TITULO);
		this.itemPedido = itemPedido;
		this.showBtSemDesconto = showBtSemDesconto;
		this.inGridClick = inGridClick;
		descontoPacote = null;
		scrollable = false;
		initializeComponents(itemPedido, dsUnidade);
		loadDomainList(itemPedido);
		setDefaultRect();
	}

	private void initializeComponents(ItemPedido itemPedido, String dsUnidade) throws SQLException {
		lbDsPacote = new LabelName(Messages.DESCONTO_PACOTE_LABEL_COMBO, RIGHT);
		cbPacotesItem = new PacoteComboBox(itemPedido);
		cbPacotesItem.selectFirstLoad();
		lbDsProduto = new LabelName(Messages.PRODUTO_NOME_ENTIDADE, RIGHT);
		lvDsProduto = new LabelValue(itemPedido.getProduto().dsProduto);
		lbDsUnidade = new LabelName(Messages.METAS_UNIDADE, RIGHT);
		lvDsUnidade = new LabelValue(dsUnidade);
		lbDsTabelaPreco = new LabelName(Messages.TABELAPRECO_NOME_TABELA, RIGHT);
		lvDsTabelaPreco = new LabelValue(TabelaPrecoService.getInstance().getDsTabelaPreco(itemPedido.cdTabelaPreco));
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btSemDesc = new ButtonPopup(Messages.DESCONTOQUANTIDADE_BOTAO_SEM_DESCONTO);
	}

	private void loadDomainList(ItemPedido itemPedido) throws SQLException {
		Pacote pacote = cbPacotesItem.getValue();
		domainList = DescontoPacoteService.getInstance().findAllByExampleByItemPedido(itemPedido, pacote != null ? pacote.cdPacote : null);
	}

	public void initUI() {
		try {
			super.initUI();
			addComponents();
			list();
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	private void addComponents() {
		if (showBtSemDesconto) {
			addButtonPopup(btSemDesc);
		}
		addButtonPopup(btCancelar);
		int widthLabels = lbDsProduto.getPreferredWidth();
		BaseScrollContainer bs = new BaseScrollContainer(true, false);
		
		UiUtil.add(bs, lbDsPacote, getLeft(), getNextY() + HEIGHT_GAP_BIG, widthLabels, UiUtil.getLabelPreferredHeight());
		UiUtil.add(bs, cbPacotesItem, AFTER + WIDTH_GAP, SAME - HEIGHT_GAP_BIG, this.getClientRect().width - widthLabels - (WIDTH_GAP_BIG * 3));
		int heightBs = UiUtil.getLabelPreferredHeight() * 2;
		
		UiUtil.add(bs, lbDsProduto, getLeft(), getNextY(), widthLabels, UiUtil.getLabelPreferredHeight());
		UiUtil.add(bs, lvDsProduto, AFTER + WIDTH_GAP, SAME);
		heightBs += UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
		
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			UiUtil.add(bs, lbDsUnidade, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, widthLabels, UiUtil.getLabelPreferredHeight());
			UiUtil.add(bs, lvDsUnidade, AFTER + WIDTH_GAP, SAME);
			heightBs += UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
		}
		
		if (LavenderePdaConfig.usaTabPrecoDescQuantidadePorPacote) {
			UiUtil.add(bs, lbDsTabelaPreco, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, widthLabels, UiUtil.getLabelPreferredHeight());
			UiUtil.add(bs, lvDsTabelaPreco, AFTER + WIDTH_GAP, SAME);
			heightBs += UiUtil.getLabelPreferredHeight() + HEIGHT_GAP;
		}
		
		UiUtil.add(this, bs, getLeft(), getTop(), FILL - HEIGHT_GAP, heightBs);
		
		prepareGrid();
        UiUtil.add(this, grid, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, FILL - WIDTH_GAP, FILL - footerH);
	}

	private void prepareGrid() {
		GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, -30, LEFT),
            new GridColDefinition(Messages.DESCONTO_LABEL_VLPCTDESCONTO, -30, LEFT),
            new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, -30, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton);
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
				} else if (event.target == cbPacotesItem) {
					loadDomainList(itemPedido);
					list();
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if ((event.target == grid) && (grid.getSelectedIndex() != -1)) {
					descontoPacote = getSelectedDomainOnGrid();
					unpop();
				}
				break;
			}
		}
	}

	public void list() throws java.sql.SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		try {
			msg.popupNonBlocking();
			Vector domainList = this.domainList;
			int listSize = domainList.size();
			clearGrid();
			if (listSize < 1) return;
			String[][] gridItems;
			String[] item = getItem(domainList.items[0]);
			gridItems = new String[listSize][item.length];
			gridItems[0] = item;
			for (int i = 1; i < listSize; i++) {
				gridItems[i] = getItem(domainList.items[i]);
			}
			grid.setItems(gridItems);
		} finally {
			msg.unpop();
		}
	}

	public void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
        DescontoPacote descPacote = (DescontoPacote) domain;
        String[] item = {
                StringUtil.getStringValue(descPacote.rowKey),
                StringUtil.getStringValueToInterface(descPacote.qtItem),
                StringUtil.getStringValueToInterface(descPacote.vlPctDesconto),
                StringUtil.getStringValueToInterface(DescontoGrupoService.getInstance().getVlDescVlBaseItemPedido((ItemPedido) itemPedido.clone(), descPacote.vlPctDesconto))};
        return item;
    }

	protected DescontoPacote getSelectedDomainOnGrid() {
		String[] item = grid.getSelectedItem();
        DescontoPacote descontoPacoteSelected = new DescontoPacote();
        descontoPacoteSelected.rowKey = item[0];
        descontoPacoteSelected.qtItem = ValueUtil.getDoubleValue(item[1]);
        descontoPacoteSelected.vlPctDesconto = ValueUtil.getDoubleValue(item[2]);
        descontoPacoteSelected.vlDesconto = ValueUtil.getDoubleValue(item[3]);
        descontoPacoteSelected.pacote = cbPacotesItem.getValue();
        return descontoPacoteSelected;
	}
}
