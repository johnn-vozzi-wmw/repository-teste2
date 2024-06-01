package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.chart.BaseColumnChart;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BarContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.PontExtPed;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.PontExtPedService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CanalPontuacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.SegmentoPontuacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.chart.Series;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.MouseEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelPontuacaoExtratoRepresentanteForm extends LavendereCrudListForm {
	
	private Date currentDate;
	
	private BaseColumnChart columnChart;
	private PushButtonGroupBase btGraficoLista;
	private BarContainer barTopListContainer;
	public ButtonAction btResize;
	
	private SessionContainer sessaoTotalizadores;
	private LabelTotalizador lvPontuacao;
	private LabelTotalizador lvVlTotalPedidos;
	
	private RepresentanteSupervComboBox representanteSupervComboBox;
	private SegmentoPontuacaoComboBox segmentoPontuacaoComboBox;
	private CanalPontuacaoComboBox canalPontuacaoComboBox;
	
	private EditDate edDtInicial;
	private EditDate edDtFinal;
	
	private LabelName lbDsRepresentante;
	private LabelName lbDtInicio;
	private LabelName lbDtFim;
	private LabelName lbCliente;
	
	private double vlTotalPontuacaoBase;
	private double vlTotalPontuacaoRealizada;
	private double vlTotalPedidos;
	private boolean topFilters;
	private Vector list;
	
	public RelPontuacaoExtratoRepresentanteForm() throws SQLException {
		super(Messages.EXTRATO_PONTUACAO_TITULO);
        currentDate = DateUtil.getCurrentDate();
		singleClickOn = true;
        initializeListContainer();
        initializeEditsAndLabels();
        initializeComboBoxes();
        initializeButtons();   
        configListContainer("DTEMISSAO, HREMISSAO");
	}
	
	private void initializeListContainer() {
		listContainer = new GridListContainer(12, 4, false);
		listContainer.setColPosition(2, RIGHT);
		listContainer.setColPosition(3, BEFORE);
		listContainer.setColPosition(11, RIGHT);
		listContainer.setColsSort(new String[][]{
			{Messages.EXTRATO_PONTUACAO_ORDER_DATA, "DTEMISSAO, HREMISSAO"},
			{Messages.EXTRATO_PONTUACAO_ORDER_VLPONTUACAOBASE, "VLPONTUACAOBASE"},
			{Messages.EXTRATO_PONTUACAO_ORDER_VLPONTUACAOREALIZADO, "VLPONTUACAOREALIZADO"}
		});
		sessaoTotalizadores = new SessionContainer();
		sessaoTotalizadores.setBackColor(ColorUtil.listContainerBarsColor);
		barBottomContainer.setVisible(false);
		barTopListContainer = new BarContainer();
	}

	private void initializeEditsAndLabels() {
		lbDsRepresentante = new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
        lbDtInicio = new LabelName(Messages.EXTRATO_PONTUACAO_DATA_INICIO);
        lbDtFim = new LabelName(Messages.EXTRATO_PONTUACAO_DATA_FIM);
        lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
        edDtInicial = new EditDate();
        edDtFinal = new EditDate();
        edDtInicial.setValue(DateUtil.getFirstDayOfMonth());
        edDtFinal.setValue(currentDate);
        lvPontuacao = new LabelTotalizador("999999999,999");
        lvVlTotalPedidos = new LabelTotalizador("999999999,999");
	}
	
	private void initializeComboBoxes() throws SQLException {
		representanteSupervComboBox = new RepresentanteSupervComboBox();
		representanteSupervComboBox.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
		if (ValueUtil.isEmpty(representanteSupervComboBox.getValue())) {
			representanteSupervComboBox.setSelectedIndex(0);
		}
		segmentoPontuacaoComboBox = new SegmentoPontuacaoComboBox();
		segmentoPontuacaoComboBox.setSelectedIndex(0);
		canalPontuacaoComboBox = new CanalPontuacaoComboBox();
		canalPontuacaoComboBox.setSelectedIndex(0);
	}
	
	private void initializeButtons() {
		btGraficoLista = new PushButtonGroupBase(new String[] {Messages.EXTRATO_PONTUACAO_LISTA, Messages.EXTRATO_PONTUACAO_GRAFICO}, true, 0, -1, 10, 1, true, PushButtonGroup.NORMAL);
		btGraficoLista.setFont(UiUtil.defaultFontSmall);
		btResize = new ButtonAction("");
		btResize.setBorder(BORDER_NONE);
		btResize.transparentBackground = true;
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		PontExtPed pontuacaoExtratoPed = new PontExtPed();
		pontuacaoExtratoPed.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pontuacaoExtratoPed.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? representanteSupervComboBox.getValue() : SessionLavenderePda.getRepresentante().cdRepresentante;
		final String clienteFilter = edFiltro.getValue();
		if (ValueUtil.isNotEmpty(clienteFilter)) {
			pontuacaoExtratoPed.cliente = new Cliente();
			pontuacaoExtratoPed.cliente.cdCliente = pontuacaoExtratoPed.cliente.nmFantasia = pontuacaoExtratoPed.cliente.nmRazaoSocial = clienteFilter;
		}
		pontuacaoExtratoPed.cdSegmento = segmentoPontuacaoComboBox.getValue();
		pontuacaoExtratoPed.cdCanal = canalPontuacaoComboBox.getValue();
		pontuacaoExtratoPed.dtEmissaoInicialFilter = edDtInicial.getValue();
		pontuacaoExtratoPed.dtEmissaoFinalFilter = edDtFinal.getValue();
		return pontuacaoExtratoPed;
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		list = PontExtPedService.getInstance().findAllByExample(domain);
		loadVlBaseRealizadoTotal();
		createGrafico();
		return list;
	}

	private void loadVlBaseRealizadoTotal() {
		if (ValueUtil.isEmpty(list)) return;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			PontExtPed pontExtPed = (PontExtPed) list.items[i];
			if (pontExtPed.isPedidoCancelado()) continue;
			if (!LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato) {
				vlTotalPontuacaoBase += pontExtPed.vlPontuacaoBaseReal;
			}
	    	vlTotalPontuacaoRealizada += pontExtPed.vlPontuacaoRealizadoReal;
	    	vlTotalPedidos += pontExtPed.vlTotalPedido;
		}
	}
	
	@Override
	public void singleClickInList() throws SQLException {
		show(new RelPontuacaoItemExtratoRepresentanteForm((PontExtPed) getSelectedDomain()));
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PontExtPedService.getInstance();
	}
	
	@Override
	public void list() throws SQLException {
		super.list();
		vlTotalPontuacaoBase = 0d;
		vlTotalPontuacaoRealizada = 0d;
		vlTotalPedidos = 0d;
	}
	
    @Override
    protected String[] getItem(Object domain) throws SQLException {
    	PontExtPed pontExtPed = (PontExtPed) domain;
        final int pontuacaoColor = PontuacaoConfigService.getInstance().getPontuacaoColor(pontExtPed.vlPontuacaoRealizadoReal, pontExtPed.vlPontuacaoBaseReal, !LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato, true, true);
        if (!pontExtPed.isPedidoCancelado()) {
        	listContainer.setColColor(0, pontuacaoColor);
        	listContainer.setColColor(2, pontuacaoColor);
        } else {
        	listContainer.setColColor(0, ColorUtil.componentsForeColor);
        	listContainer.setColColor(2, ColorUtil.componentsForeColor);
        }
        
        listContainer.setColFontSize(1, UiUtil.defaultFontSmall.size - 1, true);
        listContainer.setColFontSize(3, UiUtil.defaultFontSmall.size - 1, false);
        listContainer.setColColor(1, Color.brighter(Color.BLACK, 100));
        listContainer.setColColor(3, Color.brighter(Color.BLACK, 150));
        
        String[] s = new String[12];
        s[0] = Messages.EXTRATO_PONTUACAO_PEDIDO_LISTA + pontExtPed.nuPedido;
        s[1] = getMessageStatus(pontExtPed);
        s[2] = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(pontExtPed.vlPontuacaoRealizadoReal, pontExtPed.vlPontuacaoBaseReal, !LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato, true);
        s[3] = getValorAntigoPontuacao(pontExtPed);

        s[4] = pontExtPed.dsTipoLancamento;
        s[5] = ValueUtil.VALOR_NI;
        s[6] = ValueUtil.VALOR_NI;
        s[7] = ValueUtil.VALOR_NI;
        
        s[8] = pontExtPed.dtEmissao + Messages.EXTRATO_PONTUACAO_SEPARADOR_DATA_HORA + pontExtPed.hrEmissao;
        s[9] = ValueUtil.VALOR_NI;
        s[10] = ValueUtil.VALOR_NI;
        s[11] = FrameworkMessages.LABEL_RS + StringUtil.getStringValueToInterface(pontExtPed.vlTotalPedido);
        return s;
    }

	private String getMessageStatus(PontExtPed pontExtPed) {
		if ((pontExtPed.isPedidoCancelado())) {
			return Messages.EXTRATO_PONTUACAO_REP_CANCELADO;
		}
		if (pontExtPed.vlPontuacaoBaseReal == 0 && pontExtPed.vlPontuacaoRealizadoReal == 0) {
			return Messages.EXTRATO_PONTUACAO_REP_DEVOLVIDO;
		}
		return ValueUtil.VALOR_NI;
	}

	private String getValorAntigoPontuacao(PontExtPed pontExtPed) {
		if (!pontExtPed.hasRetornoDiferencaErp) return ValueUtil.VALOR_NI;
		return "(" + PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(pontExtPed.vlPontuacaoRealizado, pontExtPed.vlPontuacaoBase, !LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato, true) + ") ";
	}
    
    private void createGrafico() {
    	if (columnChart != null) remove(columnChart);
    	if (ValueUtil.isEmpty(list)) return;
        columnChart = new BaseColumnChart(Messages.EXTRATO_PONTUACAO_GRAFICO_TITULO, new String[]{Messages.EXTRATO_PONTUACAO_GRAFICO_X_LABEL});
        double maxValue = vlTotalPontuacaoBase > vlTotalPontuacaoRealizada ? vlTotalPontuacaoBase * 1.2 : vlTotalPontuacaoRealizada * 1.2;
        if (!LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato) {
        	columnChart.series.addElement(new Series(Messages.EXTRATO_PONTUACAO_GRAFICO_LABEL_BASE, new double[] {vlTotalPontuacaoBase}, Color.darker(Color.BLUE)));
        }
    	columnChart.series.addElement(new Series(Messages.EXTRATO_PONTUACAO_GRAFICO_LABEL_REALIZADO, new double[] {vlTotalPontuacaoRealizada}, PontuacaoConfigService.getInstance().getPontuacaoColor(vlTotalPontuacaoRealizada, vlTotalPontuacaoBase, !LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato, true, true)));
		columnChart.setYAxis(0, ValueUtil.getIntegerValue(maxValue), 8);
		UiUtil.add(this, columnChart, LEFT, barTopListContainer.getY2(), FILL, FILL - WIDTH_GAP);    	
	}

    @Override
    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
    	updateTotalizadores();
    }

	@Override
	protected void onFormStart() throws SQLException {
    	final boolean isUsuarioSupervisor = SessionLavenderePda.isUsuarioSupervisor();
		if (isUsuarioSupervisor) {
			UiUtil.add(this, lbDsRepresentante, getLeft(), getNextY());
    		UiUtil.add(this, representanteSupervComboBox, getLeft(), getNextY());
    	} 
		UiUtil.add(this, lbDtInicio, getLeft(), getNextY());
		UiUtil.add(this, edDtInicial, getLeft(), AFTER);	
		UiUtil.add(this, lbDtFim, edDtInicial.getX2() + WIDTH_GAP_BIG, lbDtInicio.getY());
		UiUtil.add(this, edDtFinal, edDtInicial.getX2() + WIDTH_GAP_BIG, edDtInicial.getY());
		
		UiUtil.add(this, new LabelName(Messages.EXTRATO_PONTUACAO_SEGMENTO_COMBO), segmentoPontuacaoComboBox, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.EXTRATO_PONTUACAO_CANAL_COMBO), canalPontuacaoComboBox, getLeft(), getNextY());

		UiUtil.add(this, lbCliente, LEFT + WIDTH_GAP_BIG, AFTER);
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
		
		if (!LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato) {
			UiUtil.add(this, btGraficoLista, CENTER, AFTER + HEIGHT_GAP, PREFERRED, UiUtil.getControlPreferredHeight());
		}
		UiUtil.add(this, barTopListContainer, LEFT, AFTER + HEIGHT_GAP - 1, FILL, UiUtil.getListContainerBarTopHeight());
		int btResizeDimension = configureBtResize();
		UiUtil.add(barTopListContainer, btResize, RIGHT - barTopListContainer.getHeight() / 4, CENTER, btResizeDimension, btResizeDimension);	
		
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(sessaoTotalizadores, lvPontuacao, getLeft() + WIDTH_GAP_BIG + WIDTH_GAP + WIDTH_GAP_BIG, SAME, PREFERRED, PREFERRED);
		UiUtil.add(sessaoTotalizadores, lvVlTotalPedidos, RIGHT - listContainer.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
		sessaoTotalizadores.resizeHeight();
    	sessaoTotalizadores.resetSetPositions();
		sessaoTotalizadores.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
		
        UiUtil.add(this, listContainer, LEFT, barTopListContainer.getY2() - UiUtil.getControlPreferredHeight() * 3 / 4, FILL, FILL - sessaoTotalizadores.getHeight());
        visibleState();
	}

	private int configureBtResize() {
		int iconsDefaultWidthHeight = (int)(((double)barTopListContainer.getHeight() / 5) * 4);
		btResize.setImage(getBtResizeImage(iconsDefaultWidthHeight));
		return iconsDefaultWidthHeight;
	}
	
	private Image getBtResizeImage(int imageHeigth) {
		Image resizeImg = UiUtil.getImage("images/resize.png");
		resizeImg.applyColor2(ColorUtil.baseForeColorSystem);
		return UiUtil.getSmoothScaledImage(resizeImg, imageHeigth, imageHeigth);
	}		

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case MouseEvent.PEN_UP :
			if ((event.target instanceof ButtonAction || (event.target instanceof BarContainer && event.target != barTopListContainer)) && event.target != btResize) {
				btGraficoLista.setSelectedIndex(0);
				visibleState();
			} else if (event.target == btGraficoLista) {
				visibleState();
 			} else if (event.target == btResize) {
				if(!topFilters) {
					disableFieldValues();
				} else {
					enableFieldsValues();
				}
				topFilters = !topFilters;
			}
			break;
		case ControlEvent.PRESSED :
			if (event.target == representanteSupervComboBox && ValueUtil.isNotEmpty(representanteSupervComboBox.getValue())) {
				SessionLavenderePda.setRepresentante(((SupervisorRep) representanteSupervComboBox.getSelectedItem()).representante);
				list();
				visibleState();
			} else if (event.target == segmentoPontuacaoComboBox || event.target == canalPontuacaoComboBox) {
				list();
				visibleState();
			}
			break;
		case EditIconEvent.PRESSED :
			if (event.target == edFiltro) {
				list();
				visibleState();
			}
			break;
		case ValueChangeEvent.VALUE_CHANGE: 
			if (event.target == edDtInicial || event.target == edDtFinal) {
				validateDateFilters();
				list();
				visibleState();
			}
			break;
		}
	}
	
	private void validateDateFilters() throws SQLException {
		if (DateUtil.isAfter(edDtInicial.getValue(), edDtFinal.getValue())) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_VALIDACAO_DATA_VIGENCIA, new String[]{Messages.EXTRATO_PONTUACAO_DATA_INICIO, Messages.EXTRATO_PONTUACAO_DATA_FIM}));
			edDtInicial.setValue(DateUtil.getFirstDayOfMonth());
			edDtFinal.setValue(currentDate);
			return;
		}
		if (DateUtil.isAfter(edDtFinal.getValue(), currentDate)) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_VALIDACAO_DATA_VIGENCIA, new String[]{Messages.EXTRATO_PONTUACAO_DATA_FIM, Messages.EXTRATO_PONTUACAO_DATA_ATUAL}));
			edDtFinal.setValue(currentDate);
			return;
		}
		list();
	}
	
	private void updateTotalizadores() {
		final String vlPontuacao = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(vlTotalPontuacaoRealizada, vlTotalPontuacaoBase, !LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato, true);
		lvPontuacao.setValue(Messages.PONTUACAO_ITEM_PEDIDO_TOTALIZADOR + " " + vlPontuacao);
		lvPontuacao.setForeColor(PontuacaoConfigService.getInstance().getPontuacaoColor(vlTotalPontuacaoRealizada, vlTotalPontuacaoBase, true, true, true));
		lvVlTotalPedidos.setValue(Messages.PONTUACAO_ITEM_PEDIDO_TOTALIZADOR_VALOR + " " + FrameworkMessages.LABEL_RS + StringUtil.getStringValueToInterface(vlTotalPedidos));
		sessaoTotalizadores.reposition();
	}
	
	private void disableFieldValues() {
		barTopListContainer.setRect(LEFT, TOP + barTopContainer.getY2() + 1, FILL, UiUtil.getListContainerBarTopHeight());
		if (columnChart != null) columnChart.setRect(LEFT, barTopListContainer.getY2() + HEIGHT_GAP_BIG, FILL, FILL - WIDTH_GAP);
		setVisibleState(false);
	}

	private void enableFieldsValues() {
		barTopListContainer.setRect(LEFT, btGraficoLista.getY2() + HEIGHT_GAP + 1, FILL, UiUtil.getListContainerBarTopHeight());
		if (columnChart != null) columnChart.setRect(LEFT, barTopListContainer.getY2() + HEIGHT_GAP_BIG, FILL, FILL - WIDTH_GAP);
		setVisibleState(true);
	}
	
	private void setVisibleState(boolean state) {
		btGraficoLista.setVisible(state);
		lbDsRepresentante.setVisible(state);
		representanteSupervComboBox.setVisible(state);
		lbDtInicio.setVisible(state);
		edDtInicial.setVisible(state);
		lbDtFim.setVisible(state);
		edDtFinal.setVisible(state);
		lbCliente.setVisible(state);
		edFiltro.setVisible(state);
		btGraficoLista.setVisible(state);
		sessaoTotalizadores.setVisible(state);
	}
	
	@Override
	public void visibleState() {
		super.visibleState();
    	boolean showingChart = btGraficoLista.getSelectedIndex() == 1;
    	if (columnChart != null) columnChart.setVisible(showingChart);
    	listContainer.setVisible(!showingChart);
    	sessaoTotalizadores.setVisible(!showingChart);
        barTopListContainer.setVisible(showingChart);
	}
	
	@Override
	public void reposition() {
		super.reposition();
		if (topFilters) {
			disableFieldValues();
		} else {
			enableFieldsValues();
		}
	}
	
}
