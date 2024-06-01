package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigAcessoSistema;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.FuncaoConfig;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import br.com.wmw.lavenderepda.business.service.ConfigAcessoSistemaService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.FeriadoService;
import br.com.wmw.lavenderepda.business.service.FuncaoConfigService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.ConexaoPdaComboBox;
import br.com.wmw.lavenderepda.sync.SyncActions;
import br.com.wmw.lavenderepda.sync.SyncActions.SYNC_OPTION;
import br.com.wmw.lavenderepda.thread.BloqueioAcessoUsuarioSistemaRunnable;
import br.com.wmw.lavenderepda.util.UiMessagesUtil;
import totalcross.sys.Vm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;

public class AdmSincronizacaoForm extends BaseUIForm {

	private BaseButton btRecebeDados;
	private BaseButton btEnviaDados;
	private final BaseButton btVerificarConexao;
	private CheckBoolean ckDevolverEstoque;
	private BaseButton btEnviarReceberDados;
	private final ConexaoPdaComboBox cbConexoes;
	private ButtonAction btReceberDadosBackground;

	public AdmSincronizacaoForm() throws SQLException {
		super(Messages.SINCRONIZACAO_DADOS);
		cbConexoes = new ConexaoPdaComboBox(Messages.SINCRONIZACAO_LABEL_CONEXAO);
		ckDevolverEstoque = new CheckBoolean(Messages.LABEL_DEVOLVER_ESTOQUE_ATUAL);
		ckDevolverEstoque.setEditable(!hasConfigInternoDevolverEstoque());
		ckDevolverEstoque.textColor = ColorUtil.baseForeColorSystem;
		btVerificarConexao = new BaseButton(UiUtil.getColorfulImage("images/connection.png", UiUtil.getControlPreferredHeight() / 7 * 5, UiUtil.getControlPreferredHeight() / 7 * 5));
		btEnviaDados = new BaseButton(Messages.SINCRONIZACAO_LABEL_ENVIAR_DADOS);
		btRecebeDados = new BaseButton(Messages.SINCRONIZACAO_LABEL_RECEBER_DADOS);
		btEnviarReceberDados = new BaseButton(Messages.SINCRONIZACAO_LABEL_ENVIA_RECEBE_DADOS);
		btReceberDadosBackground = new ButtonAction(Messages.SINCRONIZACAO_LABEL_HIST_RECEBIMENTO, "images/receiving.png");
	}

	private boolean hasConfigInternoDevolverEstoque() {
		try {
			return ValueUtil.isNotEmpty(ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.devolverEstoqueAtual, SessionLavenderePda.cdEmpresa + SessionLavenderePda.getRepresentante().cdRepresentante));
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}

	protected void onFormStart() throws SQLException {
		UiUtil.add(this, cbConexoes, getLeft(), getNextY(), FILL - UiUtil.getControlPreferredHeight() - WIDTH_GAP - WIDTH_GAP_BIG);
		UiUtil.add(this, btVerificarConexao, AFTER + WIDTH_GAP, SAME, cbConexoes.getHeight(), cbConexoes.getHeight());
		int positionXBtEnviar = isShowBtEnviar() && isShowBtReceber() ? CENTER - btEnviaDados.getPreferredWidth() / 2 - WIDTH_GAP : CENTER;
		UiUtil.add(this, btEnviaDados, positionXBtEnviar, !isShowBtReceber() ? getNextY() : AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, btRecebeDados, CENTER + btRecebeDados.getPreferredWidth() / 2 + WIDTH_GAP, SAME);
		UiUtil.add(this, btEnviarReceberDados, CENTER, !isShowBtEnviar() && !isShowBtReceber() ? SAME : AFTER + HEIGHT_GAP_BIG);
		setVisible();
		if (LavenderePdaConfig.isEnviaEstoqueRepParaServidor()) {
			UiUtil.add(barBottomContainer, ckDevolverEstoque, getLeft(), CENTER, PREFERRED - HEIGHT_GAP, PREFERRED - HEIGHT_GAP);
		}
		if (SessionLavenderePda.isLigadoSincronizacaoBackground()) {
			UiUtil.add(barBottomContainer, btReceberDadosBackground, 5);
		}
		MainLavenderePda.getInstance().setInSyncForm(true);
		Vm.setAutoOff(false);
	}
	
	private boolean isShowBtEnviar() {
		return Session.isModoSuporte || !LavenderePdaConfig.isUnificaBotoesEnviarReceberDados();
	}

	private boolean isShowBtReceber() {
		return Session.isModoSuporte || !LavenderePdaConfig.isUnificaBotoesEnviarReceberDados();
	}

	private boolean isShowBtEnviarReceber() {
		return Session.isModoSuporte || LavenderePdaConfig.isUnificaBotoesEnviarReceberDados();
	}

	private void setVisible() {
		btEnviaDados.setVisible(isShowBtEnviar());
		btRecebeDados.setVisible(isShowBtReceber());
		btEnviarReceberDados.setVisible(isShowBtEnviarReceber());
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btRecebeDados) {
					SyncActions.executeConnection(SYNC_OPTION.RECEBER_DADOS, cbConexoes.getValue());
					updateAdmSyncInterface();
					validaAcessoUsuario();
				} else if (event.target == btEnviaDados) {
					SyncActions.executeConnection(SYNC_OPTION.ENVIAR_DADOS, cbConexoes.getValue());
					updateAdmSyncInterface();
				} else if (event.target == btVerificarConexao) {
					SyncActions.executeConnection(SYNC_OPTION.TESTE_CONEXAO, cbConexoes.getValue());
					updateAdmSyncInterface();
				} else if (event.target == btEnviarReceberDados) {
					SyncActions.executeConnection(SYNC_OPTION.ENVIAR_DADOS, cbConexoes.getValue(), true, getNuIntervaloEnviarReceber());
					SyncActions.executeConnection(SYNC_OPTION.RECEBER_DADOS, cbConexoes.getValue());
					updateAdmSyncInterface();
				} else if (event.target == btReceberDadosBackground) {
					AdmSincronizacaoAutomaticoForm admSincronizacaoAutomaticoForm = new AdmSincronizacaoAutomaticoForm();
					show(admSincronizacaoAutomaticoForm);
				} else if (event.target == cbConexoes) {
					HttpConnectionManager.setHttpConnection(cbConexoes.getValue());
				} else if (event.target == ckDevolverEstoque) {
					confirmaDevolucaoEstoqueAtual();
				}
				break;
			}
		}
	}
	
	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		cbConexoes.carregueConexoes();
		if (HttpConnectionManager.getLastUsedHttpConnection() != null) {
			cbConexoes.setValue(HttpConnectionManager.getLastUsedHttpConnection());
		}
		if (LavenderePdaConfig.isAvisaPedidoAbertoFechadoEntrarSairSync()) {
			UiMessagesUtil.mostraMensagemPedidosAbertos();
		}
		if (LavenderePdaConfig.isAvisaPedidoAbertoAoEntrarNoSync()) {
			validaPedidosEmAberto();
		}
	}

	private void controlaConexaoObrigatoria() throws SQLException {
		if (!SessionLavenderePda.autorizadoPorSenhaNovoPedidoSemEnvioDados) {
			int resp = ConexaoPdaService.getInstance().isEnvioPedidosNecessario();
			if (resp != 0) {
				if (resp != 2 || ConexaoPdaService.getInstance().isNecessarioSolicitarEnviosPedidos()) {
					MainLavenderePda.getInstance().showEnvioDadosObrigatorio(resp);
				}
			}
		}
		//-- Verifica se recebimento de dados é necessário
		int resp = ConexaoPdaService.getInstance().isRecebimentoDadosNecessario();
		boolean acesso = false;
		if (resp != 0) {
			acesso = MainLavenderePda.getInstance().showReceberDadosObrigatorio(resp);
		}
		if (MainLavenderePda.getInstance().necessarioAcoesAfterInitSistema) {
			verificaAcoesAposLiberacaoAcesso(acesso);
		}
	}

	private void verificaAcoesAposLiberacaoAcesso(boolean acesso) throws SQLException {
		int reCheck = ConexaoPdaService.getInstance().isRecebimentoDadosNecessario();
		if ((reCheck == 0 || (acesso && reCheck == 1))) {
			MainLavenderePda.getInstance().afterShowMenu();
		}
	}

	@Override
	public void onFormClose() throws SQLException {
		if (LavenderePdaConfig.isAvisaPedidoAbertoFechadoEntrarSairSync()) {
			UiMessagesUtil.mostraMensagemPedidosAbertos();
		}
		MainLavenderePda.getInstance().setInSyncForm(false);
		Vm.setAutoOff(true);
		super.onFormClose();
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		super.voltarClick();
		controlaConexaoObrigatoria();
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return ConexaoPdaService.getInstance();
	}
	
	private int getNuIntervaloEnviarReceber() {
		int nuInvervaloEspera = LavenderePdaConfig.getIntervaloEnviarReceberDados();
		if (nuInvervaloEspera > 0) {
			return nuInvervaloEspera;
		}
		return 1;
	}

	private void validaPedidosEmAberto() throws SQLException {
		if (LavenderePdaConfig.isAvisaPedidoAbertoAoEntrarNoSync()) {
			int qtPedidosEmAberto = PedidoService.getInstance().countPedidosEmAberto();
			if (qtPedidosEmAberto > 0) {
				String param1 = StringUtil.getStringValueToInterface(qtPedidosEmAberto);
				String param2 = qtPedidosEmAberto == 1 ? "pedido" : "pedidos";
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_VALIDACAO_PEDIDO_ABERTO_ENVIO_DADOS, new Object[]{param1, param2}));
			}
		}
	}

	private void confirmaDevolucaoEstoqueAtual() throws SQLException {
		if (LavenderePdaConfig.isEnviaEstoqueRepParaServidor()) {
			SessionLavenderePda.isDevolverEstoqueAtual = ValueUtil.getBooleanValue(ckDevolverEstoque.getValue());
			if (SessionLavenderePda.isDevolverEstoqueAtual) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.CONFIRMACAO_DEVOLVER_ESTOQUE_ATUAL)) {
					throw new ApplicationException(Messages.DEVOLVER_ESTOQUE_ATUAL_SYNC_CANCELADO); 
				}
				PedidoService.getInstance().validaPedidoAbertoEnvioDados();
			}
		}
	}
	
	private void updateAdmSyncInterface() {
		if (HttpConnectionManager.getLastUsedHttpConnection() != null) {
			cbConexoes.setValue(HttpConnectionManager.getLastUsedHttpConnection());
		}
		ckDevolverEstoque.setEditable(!hasConfigInternoDevolverEstoque());
		if (SessionLavenderePda.isDevolverEstoqueAtual) {
			ckDevolverEstoque.setEditable(true);
			SessionLavenderePda.isDevolverEstoqueAtual = false;
		}
		ckDevolverEstoque.setValue(ValueUtil.VALOR_NAO);
		
	}
	
	private void validaAcessoUsuario() throws SQLException {
		if (! SessionLavenderePda.validaAcessoSistema) {
			return;
		}
		SessionLavenderePda.validaAcessoSistema = false;
		FuncaoConfig funcaoConfig = new FuncaoConfig();
		funcaoConfig.cdFuncao = SessionLavenderePda.usuarioPdaRep.usuario.cdFuncao;
		if (! Session.isModoSuporte && LavenderePdaConfig.usaConfigAcessoSistema && isFuncaoValidaAcesso(funcaoConfig)) {
			try {
				UiUtil.updateProcessingMessage(Messages.MSG_VALIDANDO_ACESSO);			
				String flBloqSistemaFeriado = FuncaoConfigService.getInstance().findColumnByRowKey(funcaoConfig.getRowKey(), "flBloqSistemaFeriado");
				boolean feriado = ValueUtil.VALOR_SIM.equals(flBloqSistemaFeriado) && FeriadoService.getInstance().isDtFeriado(new Date()); 
				ConfigAcessoSistema configAcessoSistema = null;
				if (! feriado) {
					configAcessoSistema = ConfigAcessoSistemaService.getInstance().findConfigAcessoSistema(funcaoConfig.cdFuncao);
				}
				int seconds = getTempoUsoSistema(configAcessoSistema);
				if (seconds > 0) {
					BloqueioAcessoUsuarioSistemaRunnable.removeQueue();
					BloqueioAcessoUsuarioSistemaRunnable.addQueue(seconds, funcaoConfig.cdFuncao, feriado);
				} else {
					int resultado = UiUtil.showMessage(Messages.MSG_BLOQUEIO_SISTEMA, TYPE_MESSAGE.WARN, new String[] {Messages.MSG_BOTAO_SAIR, Messages.MSG_BOTAO_SENHA});
					if (resultado == 1) {
						String tempoLiberado = showPopupSenha();
						if (ValueUtil.isEmpty(tempoLiberado)) {
							logoutConfigAcessoSistema();
						}
						seconds = TimeUtil.getSecondsBetween(tempoLiberado + ":00", "00:00:00");
						if (! feriado) {
							seconds = ConfigAcessoSistemaService.getInstance().verificaNovoTempoIncideEmHorarioLiberado(funcaoConfig.cdFuncao, seconds);
						}
						BloqueioAcessoUsuarioSistemaRunnable.addQueue(seconds, funcaoConfig.cdFuncao, feriado);
						return;
					}
					logoutConfigAcessoSistema();
				}	
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		}
	}
	
	private int getTempoUsoSistema(ConfigAcessoSistema configAcessoSistema) {
		if (configAcessoSistema == null) {
			return 0;
		}
		String hrAtual = TimeUtil.getCurrentTimeHHMMSS();
		return TimeUtil.getSecondsBetween(configAcessoSistema.hrFim + ":00", hrAtual);
	}

	private void logoutConfigAcessoSistema() throws SQLException {
		UiUtil.unpopProcessingMessage();
		voltarClick();
		MainLavenderePda.getInstance().logout();
		Session.setCdUsuario(null);
		MainLavenderePda.getInstance().initUI();
	}


	private boolean isFuncaoValidaAcesso(FuncaoConfig funcaoConfig) throws SQLException {
		String flUsaConfigAcessoSistema = FuncaoConfigService.getInstance().findColumnByRowKey(funcaoConfig.getRowKey(), "flUsaConfigAcessoSistema");
		return ValueUtil.VALOR_SIM.equals(flUsaConfigAcessoSistema);
	}

	private String showPopupSenha() throws SQLException {
		AdmSenhaDinamicaWindow senhaWindow = new AdmSenhaDinamicaWindow();
		senhaWindow.setMensagem(Messages.MSG_SENHA_DESBLOQUEAR);
		senhaWindow.setChaveSemente(SenhaDinamica.SENHA_SISTEMA_BLOQUEADO);
		senhaWindow.setCdUsuario(Session.getCdUsuario());
		if (senhaWindow.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			return senhaWindow.edTempoLiberacao.getValueWithMask();
		}
		return null;
	}
	
}
