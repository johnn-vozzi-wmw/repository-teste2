package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DescQuantidadePesoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow extends WmwWindow {

	private LabelName lbSaldoFlexAtual;
	private LabelName lbPesoAtual;
	private LabelValue vlSaldoFlexAtual;
	private LabelValue vlPesoAtual;
	public CheckBoolean ckCondicaoPagamento;
	public CheckBoolean ckDescontoPeso;
	private ButtonPopup btConfirmar;
	private ButtonPopup btFaixaDescontoPeso;
	private Pedido pedidoOriginal;
	public Pedido pedidoSimulacao;
	public CadPedidoForm cadPedidoForm;
	public boolean continuaFecharPedido;
	private double vlPctDescontoCondicaoPagamento;
	private double vlPctDescontoPeso;
	private SessionContainer containerInformacoesPedido;
	private SessionContainer containerTituloIndicadores;
	
	public CadAplicaDescontoIndiceFinanceiroSaldoFlexNegativoWindow(Pedido pedido) throws SQLException {
		super(Messages.APLICA_DESCONTO_INDICE_FINANCEIRO_SALDOVERBA_NEGATIVO);
		this.pedidoOriginal = pedido;
		criaPedidoSimulacao();
		lbSaldoFlexAtual = new LabelName(Messages.DESCONTO_SALDO_VERBA);
		lbPesoAtual = new LabelName(Messages.ITEMPEDIDO_LABEL_QT_PESOITENS);
		vlSaldoFlexAtual = new LabelValue();
		vlSaldoFlexAtual.useCurrency();
		vlSaldoFlexAtual.setValue(pedido.vlVerbaPedidoPositivo + pedido.vlVerbaPedido);
		vlPesoAtual = new LabelValue(getQtPeso());
		ckCondicaoPagamento = new CheckBoolean(Messages.INDICE_CONDICAO_PAGAMENTO + getVlIndiceCondicaoPagamento(pedido));
	    ckDescontoPeso = new CheckBoolean(Messages.FAIXA_DESCONTO_PESO + getVlIndiceFaixaDescontoPeso());
		btConfirmar = new ButtonPopup(Messages.BT_CONFIRMAR);
		btFaixaDescontoPeso = new ButtonPopup(Messages.BT_FAIXA_DESCONTO_PESO);
		btFechar.setText(Messages.BOTAO_CANCELAR);
		containerInformacoesPedido = new SessionContainer();
		containerInformacoesPedido.setBackColor(ColorUtil.componentsBackColorDark);
		containerTituloIndicadores = new SessionContainer();
		containerTituloIndicadores.setBackColor(ColorUtil.componentsBackColorDark);
		setDefaultRect();
	}
	
	private void criaPedidoSimulacao() {
		this.pedidoSimulacao = (Pedido) pedidoOriginal.clone();
		pedidoSimulacao.pedidoSimulacao = true;
		Vector itemPedidoList = pedidoOriginal.itemPedidoList;
		Vector itemPedidoSimulacaoList = new Vector();
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoOriginal = (ItemPedido) itemPedidoList.items[i];
			ItemPedido itemPedidoClone = (ItemPedido) itemPedidoOriginal.clone();
			itemPedidoClone.vlBaseFlexAnterior = itemPedidoClone.vlBaseFlex;
			itemPedidoSimulacaoList.addElement(itemPedidoClone);
		}
		pedidoSimulacao.itemPedidoList = itemPedidoSimulacaoList;
	}

	private String getQtPeso() {
		Vector itemPedidoList = pedidoOriginal.itemPedidoList;
		int size = itemPedidoList.size();
		double qtPeso = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (!itemPedido.isFazParteKitFechado() && !itemPedido.isRecebeuDescontoPorQuantidade()) {
				qtPeso += itemPedido.qtPeso;
			}
		}
		
		return StringUtil.getStringValueToInterface(qtPeso);
	}

	private String getVlIndiceCondicaoPagamento(Pedido pedido) throws SQLException {
		vlPctDescontoCondicaoPagamento = ValueUtil.round((1 - pedido.getCondicaoPagamento().vlIndiceFinanceiro) * 100);
		return ": " +  StringUtil.getStringValueToInterface(vlPctDescontoCondicaoPagamento) + "%";
	}
	
	private String getVlIndiceFaixaDescontoPeso() throws SQLException {
		vlPctDescontoPeso = DescQuantidadePesoService.getInstance().findVlPctDesconto(pedidoOriginal.cdEmpresa, vlPesoAtual.getDoubleValue());
		return ": " + StringUtil.getStringValueToInterface(vlPctDescontoPeso) + "%";
	}
	
	
	public void initUI() {
		super.initUI();
		UiUtil.add(this, containerInformacoesPedido, LEFT, getNextY(), FILL, UiUtil.getControlPreferredHeight());
		UiUtil.add(containerInformacoesPedido, new LabelName(Messages.INFORMACOES_PEDIDO), CENTER, CENTER);
		UiUtil.add(this, lbSaldoFlexAtual, vlSaldoFlexAtual, getLeft(), getNextY());
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			UiUtil.add(this, lbPesoAtual, vlPesoAtual, getLeft(), getNextY());
		}
		
		UiUtil.add(this, containerTituloIndicadores, LEFT, AFTER + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight());
		UiUtil.add(containerTituloIndicadores, new LabelName(Messages.DESCONTO_APLICAVEIS), CENTER, CENTER);
		
		if (isExibeCkCondicaoPagamento()) {
			UiUtil.add(this, ckCondicaoPagamento, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			UiUtil.add(this, ckDescontoPeso, getLeft(), getNextY());
		}
		addButtonPopup(btConfirmar);
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoVerbaPorPesoPedido()) {
			addButtonPopup(btFaixaDescontoPeso);
		}
		addButtonPopup(btFechar);
	}

	//@Override
	protected void addBtFechar() {
	}

	public void onWindowEvent(final Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btConfirmar) {
				btConfirmarClick();
			} else if (event.target == btFaixaDescontoPeso) {
				btFaixasDescPesoClick();
			} else if (event.target == ckCondicaoPagamento) {
				ckCondicaoPagamentoCheck();
			} else if (event.target == ckDescontoPeso) {
				ckDescontoPesoCheck();
			}
			break;
		}
		}
	}

	private boolean isExibeCkCondicaoPagamento() {
		if (LavenderePdaConfig.isAplicarDescontosIndicesParaSaldoFlexNegativoCondicaoPagamento()) {
			double vlIndiceFinanceiro = 0;
			try {
				vlIndiceFinanceiro = pedidoOriginal.getCondicaoPagamento().vlIndiceFinanceiro;
			} catch (SQLException ex) {
				return false;
			}
			return vlIndiceFinanceiro > 0 && vlIndiceFinanceiro != 1;  
		}
		return false;
		
	}
	
	private double getVlSaldoFlexAtual() {
		return pedidoSimulacao.vlVerbaPedidoPositivo + pedidoSimulacao.vlVerbaPedido;
	}
	
	private void ckCondicaoPagamentoCheck() throws SQLException {
		atualizaSaldoFlex();
	}
	

	private void ckDescontoPesoCheck() throws SQLException {
		atualizaSaldoFlex();
	}

	private void btFaixasDescPesoClick() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		ListDescQuantidadePesoWindow listDescQuantidadePesoWindow;
		try {
			if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
				listDescQuantidadePesoWindow = new ListDescQuantidadePesoWindow(pedidoOriginal);
			} else {
				listDescQuantidadePesoWindow = new ListDescQuantidadePesoWindow();
			}
			listDescQuantidadePesoWindow.setDefaultRect();
		} finally {
			mb.unpop();
		}
		listDescQuantidadePesoWindow.popup();
	}

	public void btConfirmarClick() throws SQLException {
		pedidoSimulacao.vlPctIndiceFinCondPagto = ckCondicaoPagamento.isChecked() ? vlPctDescontoCondicaoPagamento : 0;
		pedidoSimulacao.vlPctDescQuantidadePeso = ckDescontoPeso.isChecked() ? vlPctDescontoPeso : 0;
		ItemPedidoService.getInstance().recalculaVlBaseFlexItens(pedidoSimulacao, true);
		if (pedidoSimulacao.vlVerbaPedido < 0) {
			pedidoSimulacao.flPendente = ValueUtil.VALOR_SIM;
			UiUtil.showWarnMessage(Messages.PEDIDO_NECESSITA_APROVACAO_DA_GERENCIA);
		} else {
			pedidoSimulacao.flPendente = ValueUtil.VALOR_NAO;
			UiUtil.showConfirmMessage(Messages.PEDIDO_APROVADO);
		}
		
		continuaFecharPedido = true;
		unpop();
	}
	
	@Override
	protected void btFecharClick() throws SQLException {
		super.btFecharClick();
		
	}
	
	private void atualizaSaldoFlex() throws SQLException {
		Vector itemPedidoList = pedidoSimulacao.itemPedidoList;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			if (!itemPedido.isFazParteKitFechado() && !itemPedido.isRecebeuDescontoPorQuantidade()) {
				itemPedido.vlBaseFlex = itemPedido.vlBaseFlexAnterior;
				if(ckCondicaoPagamento.isChecked()) {
					double vlDesconto = ValueUtil.round(itemPedido.vlBaseFlex * vlPctDescontoCondicaoPagamento / 100);
					itemPedido.vlBaseFlex -= vlDesconto;
				}
				if(ckDescontoPeso.isChecked()) {
					double vlDesconto = ValueUtil.round(itemPedido.vlBaseFlex * vlPctDescontoPeso / 100);
					itemPedido.vlBaseFlex -= vlDesconto;
				}
			}
		}
		ItemPedidoService.getInstance().recalculaVlBaseFlexItens(pedidoSimulacao, false);
		PedidoService.getInstance().calculaVerbaPositiva(pedidoSimulacao);
		vlSaldoFlexAtual.setValue(getVlSaldoFlexAtual());
	}

	

}
