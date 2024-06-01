package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PontExtItemPed;
import br.com.wmw.lavenderepda.business.domain.PontExtPed;
import br.com.wmw.lavenderepda.business.service.PontExtItemPedService;
import br.com.wmw.lavenderepda.business.service.PontExtPedService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoMovimentacaoExtratoPontuacaoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelPontuacaoItemExtratoRepresentanteForm extends LavendereCrudListForm {
	
	private Date currentDate;
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador lvPontuacao;
	private LabelTotalizador lvFaixaDias;
	
	private EditDate edDtInicial;
	private EditDate edDtFinal;
	private LabelName lbItem;
	private LabelName lbDtInicio;
	private LabelName lbDtFim;
	
	private TipoMovimentacaoExtratoPontuacaoComboBox tipoMovimentacaoExtratoPontuacaoComboBox;
	private LabelName lbTpMovimentacao;
	private PontExtPed pontExtPed;
	
	private int imageSize;
	private Vector listItens;
	private int nuColumnsList = 10;
	private int itensPerLineList = 2;
	private int nuRowsList = 5;
	
	private double vlTotalPontuacaoRealizado; 
	private double vlTotalPontuacaoBase; 
	
	public RelPontuacaoItemExtratoRepresentanteForm(PontExtPed pontExtPed) throws SQLException {
		super(MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_TITULO, pontExtPed.nuPedido));
		this.pontExtPed = pontExtPed;
		currentDate = DateUtil.getCurrentDate();
		singleClickOn = false;
		loadImageSize();
        initializeListContainer();
        initializeEditsAndLabels();
        initializeComboBoxes();
        configListContainer("CDPRODUTO, DTEMISSAO, HREMISSAO");
	}

	private void loadImageSize() {
		imageSize = (UiUtil.getLabelPreferredHeight() * nuRowsList) - (HEIGHT_GAP_BIG * 3);	
	}

	private void initializeListContainer() {
		listContainer = new GridListContainer(nuColumnsList, itensPerLineList, false);
		listContainer.setColsSort(new String[][]{
			{Messages.EXTRATO_PONTUACAO_ORDER_DATA, "CDPRODUTO, DTEMISSAO, HREMISSAO"},
			{Messages.EXTRATO_PONTUACAO_ORDER_VLPONTUACAOBASE, "CDPRODUTO, VLPONTUACAOBASE"},
			{Messages.EXTRATO_PONTUACAO_ORDER_VLPONTUACAOREALIZADO, "CDPRODUTO, VLPONTUACAOREALIZADO"}
		});
		Util.prepareDefaultImage(imageSize, true);
		listContainer.showLeftImage(Util.getDefaultNoImage(imageSize));
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(5, RIGHT);
		listContainer.setColPosition(7, RIGHT);
		listContainer.setColPosition(9, RIGHT);
		sessaoTotalizadores = new SessionTotalizerContainer();
		barBottomContainer.setVisible(false);
	}

	private void initializeEditsAndLabels() {
        lbTpMovimentacao = new LabelName(Messages.EXTRATO_PONTUACAO_TIPO_MOVIMENTACAO);
        lbItem = new LabelName(Messages.EXTRATO_PONTUACAO_ITEM_PRODUTO);
        lbDtInicio = new LabelName(Messages.EXTRATO_PONTUACAO_DATA_INICIO);
        lbDtFim = new LabelName(Messages.EXTRATO_PONTUACAO_DATA_FIM);
        lvPontuacao = new LabelTotalizador("999999999,999");
        lvFaixaDias = new LabelTotalizador("999999999,999");
        edDtInicial = new EditDate();
        edDtFinal = new EditDate();
	}
	
	private void initializeComboBoxes() throws SQLException {
		tipoMovimentacaoExtratoPontuacaoComboBox = new TipoMovimentacaoExtratoPontuacaoComboBox();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		PontExtItemPed pontExtItemPed = new PontExtItemPed();
		pontExtItemPed.cdEmpresa = pontExtPed.cdEmpresa;
		pontExtItemPed.cdRepresentante = pontExtPed.cdRepresentante;
		pontExtItemPed.flOrigemPedido = pontExtPed.flOrigemPedido;
		pontExtItemPed.nuPedido = pontExtPed.nuPedido;
		pontExtItemPed.flTipoLancamento = tipoMovimentacaoExtratoPontuacaoComboBox.getValue();
		pontExtItemPed.dsTipoLancamento = edFiltro.getValue();
		pontExtItemPed.dtEmissaoInicialFilter = edDtInicial.getValue();
		pontExtItemPed.dtEmissaoFinalFilter = edDtFinal.getValue();
		return pontExtItemPed;
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return listItens = PontExtItemPedService.getInstance().findAllByExample(domain);
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return PontExtPedService.getInstance();
	}
	
    @Override
    protected String[] getItem(Object domain) throws SQLException {
    	PontExtItemPed pontExtItemPed = (PontExtItemPed) domain;
        final int pontuacaoColor = PontuacaoConfigService.getInstance().getPontuacaoColor(pontExtItemPed.vlPontuacaoRealizado, pontExtItemPed.vlPontuacaoBase, true, true, true);
        if (!pontExtItemPed.isTipoLancamentoCancelamento()) {
        	listContainer.setColColor(1, pontuacaoColor);
        } else {
        	listContainer.setColColor(1, Color.brighter(Color.BLACK, 100));
        }
        listContainer.setColFontSize(9, listContainer.getFontSizeSecundaryItens(), true);
        String[] s = new String[nuColumnsList];
        s[0] = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_LANCAMENTO, pontExtItemPed.cdProduto);
        s[1] = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_PONTUACAO, getVlPontosLista(pontExtItemPed));
        s[2] = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_DESCRICAO, pontExtItemPed.dsTipoLancamento);
        s[3] = ValueUtil.VALOR_NI;
        s[4] = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_QUANTIDADE, StringUtil.getStringValueToInterface(pontExtItemPed.qtItemFisico));
        s[5] = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_SALDO_PARCIAL, StringUtil.getStringValueToInterface(pontExtItemPed.vlSaldoParcial, LavenderePdaConfig.nuCasasDecimaisPontuacao));
        s[6] = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_PESO, StringUtil.getStringValueToInterface(pontExtItemPed.vlPesoPontuacao));
        s[7] = MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_FAIXA_PRECO, new String[] {StringUtil.getStringValueToInterface(pontExtItemPed.vlFatorCorrecaoFaixaPreco), StringUtil.getStringValueToInterface(pontExtItemPed.vlPctFaixaPrecoPontuacao)});
        s[8] = pontExtItemPed.dtEmissao + Messages.EXTRATO_PONTUACAO_SEPARADOR_DATA_HORA + pontExtItemPed.hrEmissao;
        s[9] = pontExtItemPed.getDsTipoLancamento();
        return s;
    }

	private String getVlPontosLista(PontExtItemPed pontExtItemPed) {
		return PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(pontExtItemPed.vlPontuacaoRealizado, pontExtItemPed.vlPontuacaoBase, pontExtItemPed.isTipoLancamentoNovoPedido() && !LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato, true);
	}
    
    @Override
    protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
    	PontExtItemPed pontExtItemPed = (PontExtItemPed) domain;
		c.setImage(Util.getImageForProdutoList(pontExtItemPed.produto, imageSize, true));
		c.setToolTip(pontExtItemPed.dsTipoLancamento);
    }
    
    @Override
    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
    	updateTotalizadores();
    }

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, lbTpMovimentacao, getLeft(), getNextY());
		UiUtil.add(this, tipoMovimentacaoExtratoPontuacaoComboBox, getLeft(), getNextY());
		
		UiUtil.add(this, lbDtInicio, getLeft(), getNextY());
		UiUtil.add(this, edDtInicial, getLeft(), getNextY());	
		UiUtil.add(this, lbDtFim, edDtInicial.getX2() + WIDTH_GAP_BIG, lbDtInicio.getY());
		UiUtil.add(this, edDtFinal, edDtInicial.getX2() + WIDTH_GAP_BIG, edDtInicial.getY());
		
		UiUtil.add(this, lbItem, getLeft(), AFTER);
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
		
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(sessaoTotalizadores, lvPontuacao, CENTER + 3, TOP, PREFERRED, PREFERRED);
		UiUtil.add(sessaoTotalizadores, lvFaixaDias, RIGHT - listContainer.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
		sessaoTotalizadores.resizeHeight();
    	sessaoTotalizadores.resetSetPositions();
		sessaoTotalizadores.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
		
		UiUtil.add(this, listContainer, LEFT, edFiltro.getY2() + HEIGHT_GAP_BIG, FILL, FILL - sessaoTotalizadores.getHeight());
	}

	private void updateTotalizadores() {
		atualizaValoresSomatorios();
		lvPontuacao.setValue(MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_TOTALIZADOR_PONTUACAO, PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(vlTotalPontuacaoRealizado, vlTotalPontuacaoBase, true, true)));
		lvPontuacao.setForeColor(PontuacaoConfigService.getInstance().getPontuacaoColor(vlTotalPontuacaoRealizado, vlTotalPontuacaoBase, true, true, true));
		lvFaixaDias.setValue(MessageUtil.getMessage(Messages.EXTRATO_PONTUACAO_ITEM_FAIXA_DIAS, new String[] {StringUtil.getStringValueToInterface(getDiasMediosFromItem()), StringUtil.getStringValueToInterface(pontExtPed.qtDiasMedios), getMessageDiasMedios(pontExtPed.qtDiasMedios)}));
		sessaoTotalizadores.reposition();
	}
	
	private void atualizaValoresSomatorios() {
		vlTotalPontuacaoRealizado = vlTotalPontuacaoBase = 0;
		if (ValueUtil.isEmpty(listItens)) return;
		int size = listItens.size();
		for (int i = 0; i < size; i++) {
			PontExtItemPed pontExtItemPed = (PontExtItemPed) listItens.items[i];
			vlTotalPontuacaoRealizado += pontExtItemPed.vlPontuacaoRealizado;
			if (!LavenderePdaConfig.ocultaValorPontuacaoBaseExtrato) {
				vlTotalPontuacaoBase += pontExtItemPed.vlPontuacaoBase;
			}
		}
	}
	
	private double getDiasMediosFromItem() {
		return ValueUtil.isNotEmpty(listItens) ? ((PontExtItemPed) listItens.items[0]).vlFatorCorrecaoFaixaDias : 0;
	}

	private String getMessageDiasMedios(int qtDiasMedios) {
		return (qtDiasMedios > 1) ? Messages.EXTRATO_PONTUACAO_ITEM_FAIXA_DIAS_DIAS : Messages.EXTRATO_PONTUACAO_ITEM_FAIXA_DIAS_DIA;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED :
			if (event.target == tipoMovimentacaoExtratoPontuacaoComboBox) {
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
	
}
