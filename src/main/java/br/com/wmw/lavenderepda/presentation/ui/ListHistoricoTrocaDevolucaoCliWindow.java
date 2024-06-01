package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.GridEditEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.HistoricoTrocaDevolucaoCli;
import br.com.wmw.lavenderepda.business.service.HistoricoTrocaDevolucaoCliService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

import java.sql.SQLException;

public class ListHistoricoTrocaDevolucaoCliWindow extends WmwWindow {

	SessionContainer listContainer;
	public SessionContainer sessaoDica;
	public BaseGridEdit grid;
	private ButtonPopup btOk;
	Vector historicoTrocaDevolucaoCliList;
	LabelName labelSubtitulo;
	double seisMeses;
	double tresMeses;
	double trintaDias;
	int diasSemCompras;
	public HistoricoTrocaDevolucaoCli itemFlTotalClienteTrue;

	public ListHistoricoTrocaDevolucaoCliWindow() {
		super(Messages.HISTORICO_CLIENTE_TITULO);
		listContainer = new SessionContainer();
		sessaoDica = new SessionContainer();
		btOk = new ButtonPopup(FrameworkMessages.BOTAO_OK);
		labelSubtitulo = new LabelName(Messages.HISTORIO_SUBTITULO);
		labelSubtitulo.setForeColor(Color.WHITE);
		setDefaultRect();
	}

	@Override
	protected void onWindowExibition() {
		super.onWindowExibition();
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			UiUtil.add(this, sessaoDica, LEFT, TOP, FILL, UiUtil.getBarMenuPreferredHeight());
			UiUtil.add(sessaoDica, labelSubtitulo, LEFT, CENTER, PREFERRED, PREFERRED);
			int oneChar = fm.stringWidth("A");
			GridColDefinition[] gridColDefiniton = {
					new GridColDefinition(Messages.HISTORICO_CLIENTE_PRODUTO, oneChar * 18, LEFT),
					new GridColDefinition(Messages.HISTORICO_PERC_SEISMESES, oneChar * 10, CENTER),
					new GridColDefinition(Messages.HISTORICO_PERC_TRESMESES, oneChar * 10, CENTER),
					new GridColDefinition(Messages.HISTORICO_PERC_TRINTADIAS, oneChar * 10, CENTER),
					new GridColDefinition(Messages.HISTORICO_DIAS_SEM_COMPRAS, oneChar * 10, CENTER)};
			grid = UiUtil.createGridEdit(gridColDefiniton, false);
			UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - footerH);
			carregaGrid();
			addButtonPopup(btOk);
			addButtonPopup(btFechar);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	public void carregaGrid() throws SQLException {
		grid.clear();
		grid.setGridControllable();
		grid.disableSort = false;
		historicoTrocaDevolucaoCliList = HistoricoTrocaDevolucaoCliService.getInstance().findAllHistoricoTrocaDevolucaoCli(SessionLavenderePda.getCliente().cdCliente);
		if (historicoTrocaDevolucaoCliList != null && ValueUtil.isNotEmpty(historicoTrocaDevolucaoCliList)) {
			diasSemCompras = LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo2() ? ((HistoricoTrocaDevolucaoCli) historicoTrocaDevolucaoCliList.items[0]).diasSemCompras : 0;
			int size = historicoTrocaDevolucaoCliList.size();
			HistoricoTrocaDevolucaoCli item;
			for (int i = 0; i < size; i++) {
				item = (HistoricoTrocaDevolucaoCli) historicoTrocaDevolucaoCliList.items[i];
				String[] itemGrid = getItemGrid(item);
				if (!ValueUtil.isEmpty(itemGrid) && itemGrid != null) {
					grid.add(itemGrid);
				}
			}
			grid.gridController.setRowBackColor(Color.BRIGHT, grid.size());
			totalizadorHistoricoCliente();
			grid.add(new String[]{getStringTotalizador(), StringUtil.getStringValueToInterface(seisMeses), StringUtil.getStringValueToInterface(tresMeses), StringUtil.getStringValueToInterface(trintaDias), StringUtil.getStringValueToInterface(diasSemCompras)}, grid.size());
			grid.totalizadorLabel = getStringTotalizador();
		}
	}

	private String[] getItemGrid(HistoricoTrocaDevolucaoCli item) {
		String[] itemGrid = null;
		if (LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo1() || LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo2()) {
			itemGrid = new String[]{StringUtil.getStringValue(item.dsProduto),
					StringUtil.getStringValue(item.percTrocaDevSeisMeses),
					StringUtil.getStringValueToInterface(item.percTrocaDevTresMeses),
					StringUtil.getStringValueToInterface(item.percTrocaDevTrintaDias),
					StringUtil.getStringValueToInterface(item.diasSemCompras)};
		} else if (LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo3() && !ValueUtil.getBooleanValue(item.flTotalCliente)) {
			itemGrid = new String[]{StringUtil.getStringValue(item.dsProduto),
					StringUtil.getStringValue(item.percTrocaDevSeisMeses),
					StringUtil.getStringValueToInterface(item.percTrocaDevTresMeses),
					StringUtil.getStringValueToInterface(item.percTrocaDevTrintaDias),
					StringUtil.getStringValueToInterface(item.diasSemCompras)};
		}
		return itemGrid;
	}

	public void totalizadorHistoricoCliente() {
		int size = historicoTrocaDevolucaoCliList.size();
		HistoricoTrocaDevolucaoCli item;
		for (int i = 0; i < size; i++) {
			item = (HistoricoTrocaDevolucaoCli) historicoTrocaDevolucaoCliList.items[i];
			if (LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo1()) {
				seisMeses += item.percTrocaDevSeisMeses;
				tresMeses += item.percTrocaDevTresMeses;
				trintaDias += item.percTrocaDevTrintaDias;
				diasSemCompras += item.diasSemCompras;
			} else if (LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo2()) {
				seisMeses = retornaMaiorValor(item.percTrocaDevSeisMeses, seisMeses);
				tresMeses = retornaMaiorValor(item.percTrocaDevTresMeses, tresMeses);
				trintaDias = retornaMaiorValor(item.percTrocaDevTrintaDias, trintaDias);
				diasSemCompras = retornaMenorValor(item.diasSemCompras, diasSemCompras);
			} else if (LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo3() && ValueUtil.getBooleanValue(item.flTotalCliente)) {
				itemFlTotalClienteTrue = item;
				seisMeses = item.percTrocaDevSeisMeses;
				tresMeses = item.percTrocaDevTresMeses;
				trintaDias = item.percTrocaDevTrintaDias;
				diasSemCompras = item.diasSemCompras;
			}
		}
	}

	public String getStringTotalizador() {
		if (LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo2()) {
			return Messages.HISTORIO_MAIOR_MENOR_VALOR;
		} else if (LavenderePdaConfig.modoTotalizacaoHistoricoClienteTipo3() && itemFlTotalClienteTrue != null && !ValueUtil.isEmpty(itemFlTotalClienteTrue.dsProduto)) {
			return itemFlTotalClienteTrue.dsProduto;
		} else {
			return Messages.HISTORIO_TOTAL;
		}
	}

	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btOk) {
					unpop();
				}
				break;
			}
			case GridEditEvent.COLUMN_PRESSED: {
				if (event.target == grid) {
					int coluna = ((GridEditEvent) event).nuColumn;
					if (coluna <= 0) {
						grid.qsort(coluna, grid.isAscending());
					}
				}
			}
		}
	}

	@Override
	protected void btFecharClick() {
		new ListHistoricoTrocaDevolucaoCliWindow().unpop();
		return;
	}

	private double retornaMaiorValor(double hist, double valor) {
		return hist > valor ? hist : valor;
	}

	private int retornaMenorValor(int hist, int valor) {
		return hist < valor ? hist : valor;
	}
}
