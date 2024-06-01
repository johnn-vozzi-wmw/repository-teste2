package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioLan;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.TipoLancamento;
import br.com.wmw.lavenderepda.business.service.DepositoBancarioService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioEstService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioLanService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.TipoLancamentoService;
import br.com.wmw.lavenderepda.print.FechamentoDiarioVendasDetalhadoPrint;
import br.com.wmw.lavenderepda.print.FechamentoDiarioVendasResumidoPrint;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;

public class RelFechamentoDiarioForm extends BaseUIForm {
	
	private SessionContainer containerFiltros;
	private BaseScrollContainer sContainer;
	private LabelName lbDataFechamento;
	private EditDate edDataFechamento;
	private EditText diaSemana;
	private LabelName lbVlTotalVendas;
	private LabelName lbVlTotalReceitas;
	private LabelName lbVlTotalPedidos;
	private LabelName lbVlTotalDinheiroVendas;
	private LabelName lbVlTotalChequeVendas;
	private LabelName lbVlTotalBoletoVendas;
	private LabelName lbVlTotalOutrosVendas;
	private LabelName lbVlTotalDinheiroPagamento;
	private LabelName lbVlTotalChequePagamento;
	private LabelName lbVlTotalBoletoPagamento;
	private LabelName lbVlTotalOutrosPagamento;
	private LabelName lbVlTotalPagamentos;
	private LabelName lbVlTotalBonificacoes;
	private LabelName lbVlTotalCreditosClientes;
	private LabelName lbVlTotalDevolucoes;
	private LabelName lbVlTotalDeposito;
	private LabelName lbVlTotalDescontos;
	private LabelName lbVlTotalLancamentos;
	private LabelName lbMensagem;
	
	private LabelValue lvVlTotalVendas;
	private LabelValue lvVlTotalReceitas;
	private LabelValue lvVlTotalPedidos;
	private LabelValue lvVlTotalDinheiroVendas;
	private LabelValue lvVlTotalChequeVendas;
	private LabelValue lvVlTotalBoletoVendas;
	private LabelValue lvVlTotalOutrosVendas;
	private LabelValue lvVlTotalPagamentos;
	private LabelValue lvVlTotalDinheiroPagamento;
	private LabelValue lvVlTotalChequePagamento;
	private LabelValue lvVlTotalBoletoPagamento;
	private LabelValue lvVlTotalOutrosPagamento;	
	private LabelValue lvVlTotalBonificacoes;
	private LabelValue lvVlTotalCreditoClientes;
	private LabelValue lvVlTotalDescontos;
	private LabelValue lvVlTotalDevolucoes;
	private LabelValue lvVlTotalDeposito;
	private LabelValue lvVlTotalLancamentos;

	private ButtonAction btFinalizarDia;
	private ButtonAction btReImprimirFechamento;
	private ButtonAction btImprimirFinanceiro;
	private ButtonAction btExibirDepositos;
	private ButtonAction btLiberarSenha;
	private ButtonAction btVeiculoQuilometragem;
	private ButtonAction btLancamentos;
	
	private Date dataUltimoFechamentoDiario;

	private HashMap<String, LabelValue> lvVlTotalLancamentoHashMap;

	private FechamentoDiario fechamentoDiarioVeiculo;
	private ScrollTabbedContainer scrollTabbedContainer;
	private BaseGridEdit gridEstoqueProduto;
	private BaseGridEdit gridTotalizadorProduto;
	private BaseGridEdit gridCliente;
	private BaseGridEdit gridTotalizadorCliente;
	private final int INDEX_FECHAMENTO = 0;
	private final int INDEX_CLIENTE = 1;
	private final int INDEX_PRODUTO;

	public RelFechamentoDiarioForm() throws SQLException {
		super(Messages.RELFECHAMENTO_DIARIO_TITULO);
		criaContainer();
		criaCampoData();
		criaCampoDiaSemana();
		criaCampoValores();
		criaLabelMensagemUsuario();
		criaBotoes();
		if (LavenderePdaConfig.usaRelatorioEstoqueProduto() && LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
			INDEX_PRODUTO = 2;
		} else {
			INDEX_PRODUTO = 1;
		}
	}

	@Override
	protected void onFormStart() throws SQLException {
		habilitaAcoesTela();
		UiUtil.add(this, containerFiltros, LEFT, getTop(), FILL, UiUtil.getControlPreferredHeight() + (HEIGHT_GAP * 2));
		UiUtil.add(containerFiltros, lbDataFechamento, LEFT + WIDTH_GAP, CENTER);
		UiUtil.add(containerFiltros, edDataFechamento, AFTER + WIDTH_GAP, CENTER);
		UiUtil.add(containerFiltros, diaSemana, AFTER + WIDTH_GAP, CENTER);
		if (LavenderePdaConfig.usaRelatorioEstoqueProduto() || LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
			UiUtil.add(this, scrollTabbedContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
			UiUtil.add(scrollTabbedContainer.getContainer(INDEX_FECHAMENTO), sContainer, 0, 0, FILL, FILL);
			if (LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
				UiUtil.add(scrollTabbedContainer.getContainer(INDEX_CLIENTE), gridCliente, WIDTH_GAP_BIG, HEIGHT_GAP_BIG, FILL - WIDTH_GAP_BIG, height / 2);
				UiUtil.add(scrollTabbedContainer.getContainer(INDEX_CLIENTE), gridTotalizadorCliente, WIDTH_GAP_BIG, AFTER, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() * 12/10);
			}
			if (LavenderePdaConfig.usaRelatorioEstoqueProduto()) {
				UiUtil.add(scrollTabbedContainer.getContainer(INDEX_PRODUTO), gridEstoqueProduto, WIDTH_GAP_BIG, HEIGHT_GAP_BIG, FILL - WIDTH_GAP_BIG, height / 2);
				UiUtil.add(scrollTabbedContainer.getContainer(INDEX_PRODUTO), gridTotalizadorProduto, WIDTH_GAP_BIG, AFTER, FILL - WIDTH_GAP_BIG, UiUtil.getControlPreferredHeight() * 12/10);
			}
		} else {
			UiUtil.add(this, sContainer, LEFT, AFTER, FILL, FILL - barBottomContainer.getHeight());
		}
		if (LavenderePdaConfig.isLiberaFechamentoDiarioComSenha()) {
			UiUtil.add(sContainer, lbMensagem, CENTER, AFTER + WIDTH_GAP_BIG);
		}
		UiUtil.add(sContainer, lbVlTotalPedidos, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalPedidos, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalReceitas, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalReceitas, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalVendas, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalVendas, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalDinheiroVendas, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalDinheiroVendas, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalChequeVendas, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalChequeVendas, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalBoletoVendas, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalBoletoVendas, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalOutrosVendas, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalOutrosVendas, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalPagamentos, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalPagamentos, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalDinheiroPagamento, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalDinheiroPagamento, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalChequePagamento, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalChequePagamento, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalBoletoPagamento, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalBoletoPagamento, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalOutrosPagamento, CENTER, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalOutrosPagamento, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);	
		UiUtil.add(sContainer, lbVlTotalBonificacoes, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalBonificacoes, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalCreditosClientes, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalCreditoClientes, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalDevolucoes, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalDevolucoes, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalDescontos, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalDescontos, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		UiUtil.add(sContainer, lbVlTotalDeposito, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(sContainer, lvVlTotalDeposito, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
		if (LavenderePdaConfig.usaTipoLancamentoDinamico()) {
			UiUtil.add(sContainer, lbVlTotalLancamentos, CENTEREDLABEL, AFTER + HEIGHT_GAP_BIG);
			UiUtil.add(sContainer, lvVlTotalLancamentos, AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			Vector tipoLancamentoList = TipoLancamentoService.getInstance().findAllByExample(TipoLancamentoService.getInstance().getFilterByCd(null));
			int size = tipoLancamentoList.size();
			for (int i = 0; i < size; i++) {
				TipoLancamento tipoLancamento = (TipoLancamento) tipoLancamentoList.items[i];
				String label = MessageUtil.getMessage(Messages.FECHAMENTO_DIARIO_LAN_TOTAL_RELATORIO, tipoLancamento.dsTipoLancamento);
				UiUtil.add(sContainer, new LabelName(label), CENTER, AFTER + HEIGHT_GAP_BIG);
				UiUtil.add(sContainer, lvVlTotalLancamentoHashMap.get(tipoLancamento.cdTipoLancamento ), AFTER + WIDTH_GAP_BIG, SAME, FILL - WIDTH_GAP);
			}
		}
		UiUtil.add(barBottomContainer, btImprimirFinanceiro, 1);
		ajustaBotoesDinamicamente();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target instanceof CalendarWmw) {
					executaAcoesAoTrocarData();
				} else if (event.target == btFinalizarDia) {
					btFinalizarDiaClick(false);
				} else if (event.target == btExibirDepositos) {
					btExibirDepositosClick();
				} else if (event.target == btReImprimirFechamento) {
					btReImprimirFechamentoResumidoClick();
				} else if (event.target == btLiberarSenha) {
					btLiberarFechamentoPorSenhaClick();
				} else if (event.target == btImprimirFinanceiro) {
					try {
						btReImprimirFinanceiroClick();
			    	} catch (Throwable e) {
			    		UiUtil.showErrorMessage(e.getMessage());
			    	}
				} else if (event.target == btVeiculoQuilometragem) {
					btVeiculoQuilometragemClick();
				} else if (event.target == btLancamentos) {
					btLancamentosClick();
				}
				break;
			}
		}
	}

	private void btLancamentosClick() throws SQLException {
		Date dt = edDataFechamento.getValue();
		if (dt == null) {
			dt = dataUltimoFechamentoDiario;
		}
		if (dt == null) {
			UiUtil.showErrorMessage(Messages.FECHAMENTO_DIARIO_LAN_VALIDATION_DTFECHAMENTODIARIO);
			return;
		}
		show(new ListFechamentoDiarioLanForm(this, dt));
	}

	private void btVeiculoQuilometragemClick() {
		CadVeiculoQuilometragemWindow cadVeiculoQuilometragemWindow = new CadVeiculoQuilometragemWindow(!btFinalizarDia.isVisible(), fechamentoDiarioVeiculo);
		fechamentoDiarioVeiculo = cadVeiculoQuilometragemWindow.fechamentoDiario;
		cadVeiculoQuilometragemWindow.popup();
	}

	private Date getDataFechamentoAnterior(FechamentoDiario fechamentoDiario) throws SQLException {
		Date dataFechamentoAnterior = fechamentoDiario.dtFechamentoDiario;
		if (fechamentoDiario.isAgrupado()) {
			dataFechamentoAnterior = FechamentoDiarioService.getInstance().getDataUltimoFechamentoDiario(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, ValueUtil.VALOR_SIM);
		}
		return ValueUtil.isNotEmpty(dataFechamentoAnterior) ? dataFechamentoAnterior : fechamentoDiario.dtFechamentoDiario;
		
	}
	
	private void btReImprimirFechamentoResumidoClick() {
		try {
			enableButtons(false);
			if (LavenderePdaConfig.isUsaImpressaoFechamentoDiarioVendas()) {
				FechamentoDiario fechamentoDiario = getFechamentoDiarioParaDiaSelecionado(edDataFechamento.getValue());
				if (fechamentoDiario == null) {
					UiUtil.showInfoMessage(Messages.FECHAMENTO_DIARIO_MSG_FECHAR);
				} else if (!fechamentoDiario.isLiberadoPorSenha()) {
					btReImprimirFechamento.setEnabled(false);
					new FechamentoDiarioVendasResumidoPrint(fechamentoDiario, getDataFechamentoAnterior(fechamentoDiario)).run();
					btReImprimirFechamento.setEnabled(true);
				}
			}
    	}catch (Throwable e) {
    		UiUtil.showErrorMessage(e.getMessage());
		} finally {
			enableButtons(true);
		}
	}

	private void btReImprimirFinanceiroClick() throws Exception {
		enableButtons(false);
		Date dtFechamentoDiario = edDataFechamento.getValue();
		FechamentoDiario fechamentoDiario = getFechamentoDiarioParaDiaSelecionado(dtFechamentoDiario);
		if (fechamentoDiario == null) {
			fechamentoDiario = new FechamentoDiario(dtFechamentoDiario);
			fechamentoDiario.dtFinalizacao = new Date();
			fechamentoDiario.hrFinalizacao = TimeUtil.getCurrentTimeHHMM();
		}
		new FechamentoDiarioVendasDetalhadoPrint(fechamentoDiario, getDataFechamentoAnterior(fechamentoDiario)).run();
		enableButtons(true);
	}
	
	private void btLiberarFechamentoPorSenhaClick() throws SQLException {
		AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow();
		admSenhaDinamicaWindow.setMensagem(Messages.FECHAMENTO_DIARIO_LIBERACAO_SENHA);
		admSenhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_FECHAMENTO_DIARIO);
		admSenhaDinamicaWindow.setData(edDataFechamento.getValue());
		if (admSenhaDinamicaWindow.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			btFinalizarDiaClick(true);
		}
	}

	private void enableButtons(boolean enable) {
		btImprimirFinanceiro.setEnabled(enable);
		btReImprimirFechamento.setEnabled(enable);
		btExibirDepositos.setEnabled(enable);
		btFinalizarDia.setEnabled(enable);
		btLiberarSenha.setEnabled(enable);
		btVeiculoQuilometragem.setEnabled(enable);
		btLancamentos.setEnabled(enable);
	}
	
	private FechamentoDiario getFechamentoDiarioParaDiaSelecionado(Date dtFechamentoDiario) throws SQLException {
		FechamentoDiarioService.getInstance().validaDataFechamentoDiario(dtFechamentoDiario);
		return FechamentoDiarioService.getInstance().findFechamentoDiarioPorData(dtFechamentoDiario);
	}

	private void btFinalizarDiaClick(boolean liberadoSenha) throws SQLException {
		enableButtons(false);
		try {
			if (LavenderePdaConfig.usaPermiteInformarVeiculoFechamento() && LavenderePdaConfig.usaObrigaInformarVeiculoFechamento()) {
				if (!FechamentoDiarioService.getInstance().validaFechamentoDiarioObrigatorioVeiculo(fechamentoDiarioVeiculo)) {
					UiUtil.showErrorMessage(Messages.FECHAMENTO_DIARIO_VEICULO_QUILOMETRAGEM_OBRIGATORIO);
					btVeiculoQuilometragemClick();
					if (!FechamentoDiarioService.getInstance().validaFechamentoDiarioObrigatorioVeiculo(fechamentoDiarioVeiculo)) {
						return;
					}
				}
			}
			if (UiUtil.showConfirmYesNoMessage(Messages.FECHAMENTO_DIARIO_MSG_CONFIRM_FECHAMENTO)) {
				FechamentoDiarioService.getInstance().insereFechamentoDiario(lvVlTotalDeposito.getDoubleValue(), dataUltimoFechamentoDiario, edDataFechamento.getValue(), liberadoSenha, fechamentoDiarioVeiculo);
				habilitaAcoesTela();
				setaVisibilidadeBotaoLiberarSenha(null);
				ajustaBotoesDinamicamente();
				btFinalizarDia.setEnabled(false);
				btReImprimirFechamentoResumidoClick();
				btFinalizarDia.setEnabled(true);
			}
		} finally {
			enableButtons(true);
		}
	}

	private void btExibirDepositosClick() throws SQLException {
		show(new ListDepositoBancarioForm(this, edDataFechamento.getValue()));
	}

	private void habilitaAcoesTela() throws SQLException {
		boolean usaCadastroFechamentoDiarioVendas = LavenderePdaConfig.usaConfigFechamentoDiarioVendas();
		edDataFechamento.setEditable(usaCadastroFechamentoDiarioVendas);
		executaAcoesAoTrocarData();
		btExibirDepositos.setVisible(usaCadastroFechamentoDiarioVendas);
	}
	
	private void executaAcoesAoTrocarData() throws SQLException {
		Date dtFechamento = edDataFechamento.getValue();
		if (ValueUtil.isNotEmpty(dtFechamento)) {
			setTextDiaSemana();
			FechamentoDiario fechamentoDiario = FechamentoDiarioService.getInstance().findFechamentoDiarioPorData(dtFechamento);
			if (LavenderePdaConfig.usaPermiteInformarVeiculoFechamento()) {
				fechamentoDiarioVeiculo = fechamentoDiario;
			}
			boolean fechamentoDiarioPorSenha = fechamentoDiario != null && fechamentoDiario.isLiberadoPorSenha();
			btReImprimirFechamento.setVisible(!fechamentoDiarioPorSenha && fechamentoDiario != null && LavenderePdaConfig.usaConfigFechamentoDiarioVendas() && LavenderePdaConfig.isUsaImpressaoFechamentoDiarioVendas());
			btImprimirFinanceiro.setVisible(!fechamentoDiarioPorSenha && LavenderePdaConfig.usaConfigFechamentoDiarioVendas() && LavenderePdaConfig.isUsaImpressaoFinanceiroFechamentoDiario());
			atualizaValores(fechamentoDiario);
			setaVisibilidadeBotaoFechar(fechamentoDiario);
			setaVisibilidadeBotaoLiberarSenha(fechamentoDiario);
			ajustaBotoesDinamicamente();
			setDataUltimoFechamentoDiario(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(FechamentoDiario.class));
			configuraMensagemUsuario(fechamentoDiario);
		}
	}
	
	
	private void configuraMensagemUsuario(FechamentoDiario fechamentoDiario) {
		lbMensagem.setVisible(true);
		if (fechamentoDiario != null && (fechamentoDiario.isAgrupado() || fechamentoDiario.isLiberadoPorSenha())) {
			lbMensagem.setText(fechamentoDiario.isAgrupado() ? Messages.FECHAMENTO_DIARIO_REALIZADO_COM_VALORES_ACUMULADOS : Messages.FECHAMENTO_DIARIO_LIBERADO_POR_SENHA);
		} else if (dataUltimoFechamentoDiario.isBefore(edDataFechamento.getValue()) && DateUtil.isBeforeOrEquals(edDataFechamento.getValue(), new Date())) {
			lbMensagem.setText(Messages.FECHAMENTO_DIARIO_VALORES_ACUMULADOS);
		} else {
			lbMensagem.setVisible(false);
		}
	}

	protected void ajustaBotoesDinamicamente() throws SQLException {
		int posicao = 5;
		remove(btFinalizarDia);
		remove(btExibirDepositos);
		remove(btReImprimirFechamento);
		remove(btLiberarSenha);
		remove(btVeiculoQuilometragem);
		remove(btLancamentos);
		if (btFinalizarDia.isVisible()) {
			UiUtil.add(barBottomContainer, btFinalizarDia, posicao--);
		} else {
			UiUtil.add(barBottomContainer, btReImprimirFechamento, posicao--);
		}
		UiUtil.add(barBottomContainer, btExibirDepositos, posicao--);
 		if (LavenderePdaConfig.usaPermiteInformarVeiculoFechamento()) {
			UiUtil.add(barBottomContainer, btVeiculoQuilometragem, posicao--);
		}
 		if (LavenderePdaConfig.usaTipoLancamentoDinamico()) {
		    UiUtil.add(barBottomContainer, btLancamentos, posicao--);
	    }
		if (isExibeBotaoLiberarSenha(null)) {
			UiUtil.add(barBottomContainer, btLiberarSenha, posicao);
		}
	}
	
	
	private boolean isExibeBotaoLiberarSenha (FechamentoDiario fechamentoDiario) throws SQLException {
		if (LavenderePdaConfig.isLiberaFechamentoDiarioComSenha()) {
			if (fechamentoDiario != null) return !fechamentoDiario.isLiberadoPorSenha();
			
			Date dataFechamento = edDataFechamento.getValue();
			if (DepositoBancarioService.getInstance().getVlTotalDeposito(dataFechamento) > 0) return false;
			
			int qtdPedidos = PedidoService.getInstance().countPedidosDiferenteAbertoECancelado(dataFechamento);
			
			if (qtdPedidos > 0 || PagamentoService.getInstance().isExistePagamento(dataFechamento)) {
				return !FechamentoDiarioService.getInstance().isHouveFechamentoDiario(dataFechamento);
			}
		}
		return false;
	}
	
	protected void atualizaValores(FechamentoDiario fechamentoDiario) throws SQLException {
		if (fechamentoDiario != null) {
			lvVlTotalVendas.setValue(fechamentoDiario.vlTotalVendas);
			lvVlTotalReceitas.setValue(fechamentoDiario.vlTotalReceitas);
			lvVlTotalPedidos.setValue(fechamentoDiario.vlTotalPedidos);
			lvVlTotalDinheiroVendas.setValue(fechamentoDiario.vlTotalVendasDinheiro);
			lvVlTotalChequeVendas.setValue(fechamentoDiario.vlTotalVendasCheque);
			lvVlTotalBoletoVendas.setValue(fechamentoDiario.vlTotalVendasBoleto);
			lvVlTotalOutrosVendas.setValue(fechamentoDiario.vlTotalVendasOutros);
			lvVlTotalPagamentos.setValue(fechamentoDiario.vlTotalPagamentos);
			lvVlTotalDinheiroPagamento.setValue(fechamentoDiario.vlTotalPagamentosDinheiro);
			lvVlTotalChequePagamento.setValue(fechamentoDiario.vlTotalPagamentosCheque);
			lvVlTotalBoletoPagamento.setValue(fechamentoDiario.vlTotalPagamentosBoleto);
			lvVlTotalOutrosPagamento.setValue(fechamentoDiario.vlTotalPagamentosOutros);
			lvVlTotalBonificacoes.setValue(fechamentoDiario.vlTotalBonificacao);
			lvVlTotalCreditoClientes.setValue(fechamentoDiario.vlTotalCreditoCliente);
			lvVlTotalDeposito.setValue(fechamentoDiario.vlTotalDepositoBancario);
			lvVlTotalDescontos.setValue(fechamentoDiario.vlTotalDesconto);
		} else {
			String cdEmpresa = SessionLavenderePda.cdEmpresa;
			String cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Pedido.class);
			setDataUltimoFechamentoDiario(cdEmpresa, cdRepresentante);
			Date dataFechamento = edDataFechamento.getValue();
			if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
				atualizaValoresBaseadoEmPedidos();
			} else {
				lvVlTotalVendas.setValue(PagamentoPedidoService.getInstance().somaTotalVendasPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
				lvVlTotalReceitas.setValue(PagamentoPedidoService.getInstance().somaTotalReceitasPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
				lvVlTotalPedidos.setValue(PagamentoPedidoService.getInstance().somaTotalPedidosPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
				lvVlTotalBonificacoes.setValue(PagamentoPedidoService.getInstance().somaTotalBonificacaoPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
				cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(PagamentoPedido.class);
				lvVlTotalDinheiroVendas.setValue(PagamentoPedidoService.getInstance().somaDinheiroPedidoPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
				lvVlTotalChequeVendas.setValue(PagamentoPedidoService.getInstance().somaChequePedidoPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
				lvVlTotalBoletoVendas.setValue(PagamentoPedidoService.getInstance().somaBoletoPedidoPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
				lvVlTotalOutrosVendas.setValue(PagamentoPedidoService.getInstance().somaOutrosPedidoPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
			}
			cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Pagamento.class);
			lvVlTotalPagamentos.setValue(PagamentoService.getInstance().somaTotalPagamentosPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
			lvVlTotalDinheiroPagamento.setValue(PagamentoService.getInstance().somaTotalDinheiroPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
			lvVlTotalChequePagamento.setValue(PagamentoService.getInstance().somaTotalChequePorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
			lvVlTotalBoletoPagamento.setValue(PagamentoService.getInstance().somaTotalBoletoPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
			lvVlTotalOutrosPagamento.setValue(PagamentoService.getInstance().somaTotalOutrosPorData(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));			
			lvVlTotalCreditoClientes.setValue(PedidoService.getInstance().somaVlTotalNotaCredito(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
			lvVlTotalDeposito.setValue(DepositoBancarioService.getInstance().getVlTotalDeposito(dataFechamento));
			lvVlTotalDescontos.setValue(PedidoService.getInstance().somaVlTotalDesconto(cdEmpresa, cdRepresentante, dataUltimoFechamentoDiario, dataFechamento));
		}
		if (LavenderePdaConfig.usaTipoLancamentoDinamico()) {
			lvVlTotalLancamentos.setValue(FechamentoDiarioLanService.getInstance().getSumVlTotalLancamentosByDate(edDataFechamento.getValue()));
			clearLvVlTotalLancamentosHashMap();
			updateLvVlTotalLancamentosHashMap();
		}
		if (LavenderePdaConfig.usaRelatorioEstoqueProduto()) {
			atualizaValoresRelatorioEstoqueProduto();
		}
		if (LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
			atualizaValoresRelatorioCliente();
		}
	}
	
	private void atualizaValoresBaseadoEmPedidos() throws SQLException {
		String cdEmpresa = SessionLavenderePda.cdEmpresa;
		String cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Pedido.class);
		setDataUltimoFechamentoDiario(cdEmpresa, cdRepresentante);
		Date dataFechamento = edDataFechamento.getValue();
		Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(dataFechamento);
		lvVlTotalPedidos.setValue(pedidoSum.vlTotalPedido + pedidoSum.vlBonificacaoPedido);
		lvVlTotalReceitas.setValue(pedidoSum.vlTotalPedido);
		lvVlTotalVendas.setValue(lvVlTotalReceitas.getValue());
		double vlDinheiro = pedidoSum.vlTotalDinheiro;
		double vlCheque = pedidoSum.vlTotalCheque;
		double vlBoleto = pedidoSum.vlTotalBoleto;
		lvVlTotalDinheiroVendas.setValue(vlDinheiro);
		lvVlTotalChequeVendas.setValue(vlCheque);
		lvVlTotalBoletoVendas.setValue(vlBoleto);
		lvVlTotalOutrosVendas.setValue(pedidoSum.vlTotalPedido - vlDinheiro - vlCheque - vlBoleto);
		lvVlTotalBonificacoes.setValue(pedidoSum.vlBonificacaoPedido);
	}

	private void updateLvVlTotalLancamentosHashMap() throws SQLException {
		Vector fechamentoDiarioLanList = FechamentoDiarioLanService.getInstance().findAllByExample(FechamentoDiarioLanService.getInstance().getFilterByDate(edDataFechamento.getValue()));
		int size = fechamentoDiarioLanList.size();
		for (int i = 0; i < size; i++) {
			FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) fechamentoDiarioLanList.items[i];
			LabelValue labelValue = lvVlTotalLancamentoHashMap.get(fechamentoDiarioLan.cdTipoLancamento);
			if (labelValue != null) {
				labelValue.setValue(fechamentoDiarioLan.vlTotalLancamento);
			}
		}
	}

	private void clearLvVlTotalLancamentosHashMap() {
		for (LabelValue labelValue : lvVlTotalLancamentoHashMap.values()) {
			labelValue.setValue(0d);
		}
	}

	private void atualizaValoresRelatorioEstoqueProduto() throws SQLException {
		Vector fechamentoDiarioEstList = FechamentoDiarioEstService.getInstance().findAllByExample(FechamentoDiarioEstService.getInstance().getFilterByDate(edDataFechamento.getValue()));
		String[][] gridItems = FechamentoDiarioEstService.getInstance().fechamentoDiarioEstListToGridList(fechamentoDiarioEstList);
		gridEstoqueProduto.setItems(Arrays.copyOfRange(gridItems,0, gridItems.length - 1));
		gridTotalizadorProduto.captions = gridItems[gridItems.length - 1];
	}
	
	private void atualizaValoresRelatorioCliente() throws SQLException {
		String [][] gridItems = FechamentoDiarioService.getInstance().fechamentoDiarioClienteToGridList(PedidoService.getInstance().findSumTotalPedidoFechamentoDiarioCliente(edDataFechamento.getValue()));
		gridCliente.setItems(Arrays.copyOfRange(gridItems, 0, gridItems.length - 1));
		gridTotalizadorCliente.captions = gridItems[gridItems.length - 1];
	}

	private void setDataUltimoFechamentoDiario(String cdEmpresa, String cdRepresentante) throws SQLException {
		dataUltimoFechamentoDiario = FechamentoDiarioService.getInstance().getDataUltimoFechamentoDiario(cdEmpresa, cdRepresentante, ValueUtil.VALOR_NAO);
		if (ValueUtil.isEmpty(dataUltimoFechamentoDiario)) {
			dataUltimoFechamentoDiario = FechamentoDiarioService.getInstance().getDataUltimoFechamentoDiario(cdEmpresa, cdRepresentante, ValueUtil.VALOR_SIM);
		}
		dataUltimoFechamentoDiario = ValueUtil.isNotEmpty(dataUltimoFechamentoDiario) ? dataUltimoFechamentoDiario : edDataFechamento.getValue();
		
	}
	
	protected void setaVisibilidadeBotaoFechar(FechamentoDiario fechamentoDiario) throws SQLException {
		btFinalizarDia.setVisible(false);
		if (LavenderePdaConfig.usaConfigFechamentoDiarioVendas()) {
			Date dateFilter = edDataFechamento.getValue();
			if (fechamentoDiario == null && (PagamentoService.getInstance().isExistePagamento(dateFilter) || PedidoService.getInstance().isExistePedidoDiferenteAbertoECancelado(dateFilter))) {
				double vlTotalPagamentoDinheiro = PagamentoService.getInstance().getSomaTotalDinheiroPorData(dataUltimoFechamentoDiario, dateFilter);
				double vlTotalPagamentosDinheiro = vlTotalPagamentoDinheiro + getVlTotalPagamentoPedidoDinheiro(dateFilter);
				double vlTotalDeposito = lvVlTotalDeposito.getDoubleValue();
				if (LavenderePdaConfig.usaTipoLancamentoDinamico()) {
					vlTotalDeposito += FechamentoDiarioLanService.getInstance().getSumVlTotalLancamentosByDate(dateFilter);
				}
				btFinalizarDia.setVisible(ValueUtil.valueEquals(ValueUtil.round(vlTotalPagamentosDinheiro), ValueUtil.round(vlTotalDeposito)));
			}
		} 
	}

	private double getVlTotalPagamentoPedidoDinheiro(Date dateFilter) throws SQLException {
		double vlTotalPagamentoPedidoDinheiro;
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			vlTotalPagamentoPedidoDinheiro = getVlDinheiroPedido(dateFilter);
		} else {
			vlTotalPagamentoPedidoDinheiro = PagamentoPedidoService.getInstance().getSomaDinheiroPedidoPorData(dataUltimoFechamentoDiario, dateFilter);
		}
		return vlTotalPagamentoPedidoDinheiro;
	}
	
	private double getVlDinheiroPedido(Date dataFechamentoDiario) throws SQLException {
		Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(dataFechamentoDiario);
		return pedidoSum.vlTotalDinheiro;
	}
	
	protected void setaVisibilidadeBotaoLiberarSenha(FechamentoDiario fechamentoDiario) throws SQLException {
		btLiberarSenha.setVisible(isExibeBotaoLiberarSenha(fechamentoDiario));
	}
	
	private void setTextDiaSemana() {
		int dia = edDataFechamento.getValue().getDayOfWeek();
		diaSemana.setText(DateUtil.getDiaSemana(dia));
	}

	private void criaContainer() {
		containerFiltros = new SessionContainer();
		containerFiltros.setBackColor(ColorUtil.componentsBackColorDark);
		sContainer = new BaseScrollContainer(false, true);
		sContainer.setBackColor(getBackColor());
		criaTabContainer();
		if (LavenderePdaConfig.usaRelatorioEstoqueProduto()) {
			GridColDefinition[] definition =  {new GridColDefinition(Messages.ITEMPEDIDO_LABEL_PRODUTO, -35, LEFT),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_REMESSA, -16, CENTER),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_RETORNO, -16, CENTER),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_VENDIDO, -16, CENTER),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_SALDO, -10, CENTER),
					new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT)};
			gridEstoqueProduto = UiUtil.createGridEdit(definition, false);
			gridTotalizadorProduto = UiUtil.createGridEdit(definition, false);
			gridTotalizadorProduto.getSbHoriz().setEnabled(false);
			gridTotalizadorProduto.getSbVert().setEnabled(false);
		}
		if (LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
			GridColDefinition[] definition =  {new GridColDefinition(Messages.FECHAMENTO_DIARIO_CLIENTE,  -35, LEFT),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_GRID_DINHEIRO, -16, CENTER),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_GRID_CHEQUE, -16, CENTER),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_GRID_BOLETO, -16, CENTER),
					new GridColDefinition(Messages.FECHAMENTO_DIARIO_SALDO, -16, CENTER),
					new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT)};
			gridCliente = UiUtil.createGridEdit(definition, false);
			gridTotalizadorCliente = UiUtil.createGridEdit(definition, false);
			gridCliente.getSbHoriz().setEnabled(false);
			gridTotalizadorCliente.getSbVert().setEnabled(false);
		}
	}
	
	private void criaTabContainer() {
		int size = 1;
		if (LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) size++;
		if (LavenderePdaConfig.usaRelatorioEstoqueProduto()) size++;
		if (size > 1) {
			int i = 0;
			String[] captions = new String[size];
			captions[i++] = Messages.FECHAMENTO_DIARIO_FECHAMENTO;
			if (LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
				captions[i++] = Messages.FECHAMENTO_DIARIO_CLIENTE;
			}
			if (LavenderePdaConfig.usaRelatorioEstoqueProduto()) {
				captions[i] = Messages.FECHAMENTO_DIARIO_PRODUTO;
			}
			scrollTabbedContainer = new ScrollTabbedContainer(captions);
		}
	}
	
	private void criaCampoData() {
		lbDataFechamento = new LabelName(Messages.DATA_LABEL_DATA);
		edDataFechamento = new EditDate();
		edDataFechamento.setValue(DateUtil.getCurrentDate());
		edDataFechamento.onlySelectByCalendar();
	}
	
	private void criaCampoDiaSemana() {
		diaSemana = new EditText("", 15);
        diaSemana.setEnabled(false);
	}

	private void criaCampoValores() throws SQLException {
		lbVlTotalVendas = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_VENDAS);
		lvVlTotalVendas = new LabelValue();
		lvVlTotalVendas.useCurrencyValue = true;
		lvVlTotalVendas.setValue(ValueUtil.round(0));
		
		lbVlTotalReceitas = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_RECEITAS);
		lvVlTotalReceitas = new LabelValue();
		lvVlTotalReceitas.useCurrencyValue = true;
		lvVlTotalReceitas.setValue(ValueUtil.round(0));
		
		lbVlTotalPedidos = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_PEDIDOS);
		lvVlTotalPedidos = new LabelValue();
		lvVlTotalPedidos.useCurrencyValue = true;
		lvVlTotalPedidos.setValue(ValueUtil.round(0));
		
		lbVlTotalDinheiroVendas = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_DINHEIRO);
		lvVlTotalDinheiroVendas = new LabelValue();
		lvVlTotalDinheiroVendas.useCurrencyValue = true;
		lvVlTotalDinheiroVendas.setValue(ValueUtil.round(0));
		
		lbVlTotalDinheiroPagamento = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_DINHEIRO);
		lvVlTotalDinheiroPagamento = new LabelValue();
		lvVlTotalDinheiroPagamento.useCurrencyValue = true;
		lvVlTotalDinheiroPagamento.setValue(ValueUtil.round(0));
		
		lbVlTotalChequeVendas = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_CHEQUES);
		lvVlTotalChequeVendas = new LabelValue();
		lvVlTotalChequeVendas.useCurrencyValue = true;
		lvVlTotalChequeVendas.setValue(ValueUtil.round(0));
		
		lbVlTotalChequePagamento = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_CHEQUES);
		lvVlTotalChequePagamento = new LabelValue();
		lvVlTotalChequePagamento.useCurrencyValue = true;
		lvVlTotalChequePagamento.setValue(ValueUtil.round(0));
		
		lbVlTotalBoletoVendas = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_BOLETOS);
		lvVlTotalBoletoVendas = new LabelValue();
		lvVlTotalBoletoVendas.useCurrencyValue = true;
		lvVlTotalBoletoVendas.setValue(ValueUtil.round(0));
		
		lbVlTotalOutrosVendas = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_OUTROS);
		lvVlTotalOutrosVendas = new LabelValue();
		lvVlTotalOutrosVendas.useCurrencyValue = true;
		lvVlTotalOutrosVendas.setValue(ValueUtil.round(0));
		
		lbVlTotalBoletoPagamento = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_BOLETOS);
		lvVlTotalBoletoPagamento = new LabelValue();
		lvVlTotalBoletoPagamento.useCurrencyValue = true;
		lvVlTotalBoletoPagamento.setValue(ValueUtil.round(0));
		
		lbVlTotalOutrosPagamento = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_OUTROS);
		lvVlTotalOutrosPagamento = new LabelValue();
		lvVlTotalOutrosPagamento.useCurrencyValue = true;
		lvVlTotalOutrosPagamento.setValue(ValueUtil.round(0));		
		
		lbVlTotalPagamentos = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_PAGAMENTOS);
		lvVlTotalPagamentos = new LabelValue();
		lvVlTotalPagamentos.useCurrencyValue = true;
		lvVlTotalPagamentos.setValue(ValueUtil.round(0));
		
		lbVlTotalBonificacoes = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_BONIFICACOES);
		lvVlTotalBonificacoes = new LabelValue();
		lvVlTotalBonificacoes.useCurrencyValue = true;
		lvVlTotalBonificacoes.setValue(ValueUtil.round(0));
		
		lbVlTotalCreditosClientes = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_CREDITO_CLIENTES);
		lvVlTotalCreditoClientes = new LabelValue();
		lvVlTotalCreditoClientes.useCurrencyValue = true;
		lvVlTotalCreditoClientes.setValue(ValueUtil.round(0));
		
		lbVlTotalDevolucoes = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_DEVOLUCOES);
		lvVlTotalDevolucoes = new LabelValue();
		lvVlTotalDevolucoes.useCurrencyValue = true;
		lvVlTotalDevolucoes.setValue(ValueUtil.round(0));
		
		lbVlTotalDeposito = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_DEPOSITO);
		lvVlTotalDeposito = new LabelValue();
		lvVlTotalDeposito.useCurrencyValue = true;
		lvVlTotalDeposito.setValue(ValueUtil.round(0));

		if (LavenderePdaConfig.usaTipoLancamentoDinamico()) {
			lbVlTotalLancamentos = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_LANCAMENTOS);
			lvVlTotalLancamentos = new LabelValue();
			lvVlTotalLancamentos.useCurrencyValue = true;
			lvVlTotalLancamentos.setValue(ValueUtil.round(0));
			lvVlTotalLancamentoHashMap = new HashMap<>();
			Vector tipoLancamentoList = TipoLancamentoService.getInstance().findAllByExample(TipoLancamentoService.getInstance().getFilterByCd(null));
			int size = tipoLancamentoList.size();
			for (int i = 0; i < size; i++) {
				LabelValue labelValue = new LabelValue();
				labelValue.useCurrencyValue = true;
				labelValue.setValue(ValueUtil.round(0));
				lvVlTotalLancamentoHashMap.put(((TipoLancamento) tipoLancamentoList.items[i]).cdTipoLancamento, labelValue);
			}

		}

		lbVlTotalDescontos = new LabelName(Messages.FECHAMENTO_DIARIO_LABEL_VL_TOTAL_DESCONTOS);
		lvVlTotalDescontos = new LabelValue();
		lvVlTotalDescontos.useCurrencyValue = true;
		lvVlTotalDescontos.setValue(ValueUtil.round(0));
	}

	private void criaLabelMensagemUsuario() {
		lbMensagem = new LabelName(Messages.FECHAMENTO_DIARIO_LIBERADO_POR_SENHA, CENTER);
		lbMensagem.setForeColor(ColorUtil.softBlue);
	}
	
	private void criaBotoes() {
		btFinalizarDia = new ButtonAction(Messages.BOTAO_FINALIZAR_DIA, "images/fecharpedido.png");
		btReImprimirFechamento = new ButtonAction(Messages.BOTAO_IMPRESSAO_REIMPRIMIR_FECHAMENTO, "images/impressao.png");
		btImprimirFinanceiro = new ButtonAction(Messages.BOTAO_IMPRESSAO_IMPRIMIR_FINANCEIRO, "images/impressao.png");
		btExibirDepositos = new ButtonAction(Messages.BOTAO_DEPOSITO, "images/verba.png");
		btLiberarSenha = new ButtonAction(Messages.BOTAO_LIBERAR_SENHA, "images/desbloquear.png");
		btVeiculoQuilometragem = new ButtonAction(Messages.BOTAO_VEICULO_FECHAMENTO_DIARIO, "images/carro.png");
		btLancamentos = new ButtonAction(Messages.BOTAO_LANCAMENTOS_FECHAMENTO_DIARIO, "images/fechamentoDiarioLan.png");
	}

	
}
