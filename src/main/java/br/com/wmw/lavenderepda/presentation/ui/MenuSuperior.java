package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.MenuSuperiorBase;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.MenuCarrousel;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.ConfigNotificacaoService;
import br.com.wmw.lavenderepda.business.service.NotificacaoService;
import br.com.wmw.lavenderepda.business.service.TabelaDbService;
import br.com.wmw.lavenderepda.business.service.UsuarioPdaService;
import br.com.wmw.lavenderepda.sync.SyncExtend;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.sys.Settings;
import totalcross.ui.Insets;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class MenuSuperior extends MenuSuperiorBase {

	private LabelValue lbDown;
	private LabelValue lbUser;
	private MenuCarrousel btMenuCarrousel;
	private boolean visibledFull;
	private Vector namesMenuCarrousel;
	private SessionContainer barTop;
	private SessionContainer barBottom;
	private SessionContainer barBottom2;

	public MenuSuperior() {
		super();
		barTop = new SessionContainer();
		barBottom = new SessionContainer();
		barBottom2 = new SessionContainer();
		//--
		lbDown = new LabelValue("---");
		lbDown.setForeColor(ColorUtil.sessionContainerForeColor);
		lbUser = new LabelValue(Session.getCdUsuario());
		lbUser.setForeColor(ColorUtil.baseForeColorSystem);
		//--
		Vector icons = new Vector();
		namesMenuCarrousel = new Vector();
		boolean sessionUsuarioNotEmpty = ValueUtil.isNotEmpty(Session.getCdUsuario());
		if (sessionUsuarioNotEmpty) {
			//Alterar senha
			icons.addElement(getImage("images/alterarSenha.png"));
			namesMenuCarrousel.addElement(FrameworkMessages.MENU_ALTERAR_SENHA);
			//Atualizar App
			icons.addElement(getImage("images/download.png"));
			namesMenuCarrousel.addElement(Messages.MENU_PDA_ATUALIZARAPP);
			if (VmUtil.isAndroid()) {
				icons.addElement(getImage("images/ic_wtools.png"));
				namesMenuCarrousel.addElement(Messages.MENU_PDA_DOWNLOAD_WTOOLS);
			}
			//Backup Pedidos
			icons.addElement(getImage("images/backup.png"));
			namesMenuCarrousel.addElement(Messages.MENU_PDA_BACKUP_PEDIDOS);
			//Calculadora
			icons.addElement(getImage("images/calculator.png"));
			namesMenuCarrousel.addElement(Messages.MENU_UTILITARIO_CALC);
			//Calendário
			icons.addElement(getImage("images/calendar.png"));
			namesMenuCarrousel.addElement(Messages.MENU_UTILITARIO_CALENDARIO);
		}
		//Conexão
		icons.addElement(getImage("images/wifi.png"));
		namesMenuCarrousel.addElement(Messages.MENU_CONFIGURACOES_CONEXAO);
		//Enviar Banco
		icons.addElement(getImage("images/iconeEnvioBD.png"));
		namesMenuCarrousel.addElement(Messages.MENU_SISTEMA_ENVIAR_BANCO);
		if (sessionUsuarioNotEmpty) {
			//Enviar Log WGps
			if (LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
				icons.addElement(getImage("images/logGps.png"));
				namesMenuCarrousel.addElement(Messages.ENVIAR_LOG_GPS);
			}
			//Rel log Sync background
			//TODO REFATORAR
//			if (SincronizacaoRunnable.getInstance().isSyncAutomaticoLigado() && LavenderePdaConfig.isLogSyncBackgroundLigado()) {
//				icons.addElement(getImage("images/log.png"));
//				namesMenuCarrousel.addElement(FrameworkMessages.LOGSYNCBACKGROUND_TITULO);
//			}
		}
		//Manutenção
		icons.addElement(getImage("images/manutencao.png"));
		namesMenuCarrousel.addElement(Messages.MENU_PDA_ADMINISTRACAO);
		if (ConfigNotificacaoService.getInstance().existeConfigNotificacao() || NotificacaoService.getInstance().existeNotificacaoGerada()) {
			//NotificacaoSistema
			icons.addElement(getImage("images/notificacao.png"));
			namesMenuCarrousel.addElement(Messages.MENU_NOTIFICACAO_SISTEMA);
		}
		if (sessionUsuarioNotEmpty) {
			//Reorganizar
			icons.addElement(getImage("images/reorganizar.png"));
			namesMenuCarrousel.addElement(Messages.MENU_PDA_REORGANIZDADOS);
			//Senhas
			if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.ocultaTelaGeracaoSenhaSupervisor) {
				icons.addElement(getImage("images/seguranca.png"));
				namesMenuCarrousel.addElement(Messages.MENU_SISTEMA_GERAR_SENHA);
			}
			//Sql
			icons.addElement(getImage("images/iconeSql.png"));
			namesMenuCarrousel.addElement(Messages.MENU_SISTEMA_SQLEXEC);
			//GPSTest
			if (LavenderePdaConfig.isColetaDadosGpsRepresentante() || LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema() || LavenderePdaConfig.isUsaColetaGpsManual()) {
				icons.addElement(getImage("images/testeGps.png"));
				namesMenuCarrousel.addElement(Messages.MENU_CONFIGURACOES_GPS);
			}
		}
		if (!LavenderePdaConfig.isTecladoWMW()) {
			icons.addElement(getImage("images/teclado.png"));
			namesMenuCarrousel.addElement(Messages.MENU_SISTEMA_TECLADO);
		}
		btMenuCarrousel = new MenuCarrousel();
		btMenuCarrousel.useTitle = false;
		btMenuCarrousel.useArrow = false;
		btMenuCarrousel.useCenterTextAlignment = true;
		btMenuCarrousel.qtItensByLine = 3;
		btMenuCarrousel.percEspacamento = 10;
		btMenuCarrousel.heightButtons = UiUtil.getControlPreferredHeight() * 3;
		btMenuCarrousel.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.componentsForeColor);
		for (int i = 0; i < namesMenuCarrousel.size(); i++) {
			btMenuCarrousel.addOptionMenu((String)namesMenuCarrousel.items[i], (Image)icons.items[i], BOTTOM, BaseContainer.HEIGHT_GAP_BIG);
		}
		//--
		changeColors();
	}

	private Image getImage(String path) {
		return UiUtil.getColorfulImage(path, UiUtil.getControlPreferredHeight() + BaseContainer.HEIGHT_GAP, UiUtil.getControlPreferredHeight() + BaseContainer.HEIGHT_GAP);
	}

	public void changeColors() {
		setBackColor(Color.darker(ColorUtil.formsBackColor, 1));
		barBottom2.setBackColor(ColorUtil.baseForeColorSystem);
	}

	protected void onFormStart() throws SQLException {
		int barsHeight = UiUtil.getLabelPreferredHeight();
		UiUtil.add(this, barTop, LEFT, TOP, FILL, barsHeight);
		if (ValueUtil.isNotEmpty(Session.getCdUsuario())) {
			UiUtil.add(barTop, lbUser, CENTER, CENTER, PREFERRED, PREFERRED);
		}
		UiUtil.add(this, btMenuCarrousel, LEFT, TOP + barsHeight, FILL, FILL - barsHeight - (barsHeight / 3));
		UiUtil.add(this, barBottom, LEFT, BOTTOM, FILL, barsHeight);
		UiUtil.add(this, barBottom2, LEFT, BEFORE, FILL, barsHeight / 3);
		UiUtil.add(barBottom, lbDown, CENTER, CENTER, PREFERRED, PREFERRED);
	}

	@Override
	public void reposition() {
		try {
			super.reposition();
			Insets safeAreaInsets = UiUtil.isIosInsets() ? MainLavenderePda.getSafeAreaInsets() : new Insets();
			int insetsH = safeAreaInsets.left + safeAreaInsets.right;
			setRect(LEFT, TOP, FILL, FILL);
			if (width > Settings.screenWidth - insetsH) {
				setRect(LEFT, TOP, FILL - insetsH, FILL);
			}
			onFormStart();
			btMenuCarrousel.reposition();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btMenuCarrousel) {
					setVisible(false);
					setVisibledFull(false);
					String optionSelected = (String) namesMenuCarrousel.items[btMenuCarrousel.getSelectIndex()];
					if (Messages.MENU_CONFIGURACOES_CONEXAO.equals(optionSelected)) {
						showConexaoWindow();
					} else if (Messages.ENVIAR_LOG_GPS.equals(optionSelected)) {
						enviaLogWGpsServidor();
					} else if (Messages.MENU_UTILITARIO_CALC.equals(optionSelected)) {
						new Calculator().popup();
					} else if (Messages.MENU_UTILITARIO_CALENDARIO.equals(optionSelected)) {
						new CalendarWmw(true).popup();
					} else if (Messages.MENU_PDA_ADMINISTRACAO.equals(optionSelected)) {
						if (LavenderePdaConfig.usaSistemaModoOffLine) {
							UiUtil.showInfoMessage(Messages.MENU_SISTEMA_OFFLINE);
						} else if (LavenderePdaConfig.usaSenhaAdministracaoTabelas) {
							AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
							senhaForm.setMensagem(Messages.SENHADINAMICA_TITULO_MENU_MANUTENCAO);
							senhaForm.setChaveSemente(SenhaDinamica.SENHA_MENU_ADMINISTRACAO_TABELAS);
							String cdUsuario = SessionLavenderePda.usuarioPdaRep != null 
									? SessionLavenderePda.usuarioPdaRep.cdUsuario 
									: UsuarioPdaService.getInstance().getCdUsuarioPda();
							if (ValueUtil.isEmpty(cdUsuario)) {
								throw new ValidationException(MessageUtil.getMessage(Messages.MENU_SISTEMA_INDISPONIVEL_FORM, new String[] { Messages.MENU_PDA_ADMINISTRACAO, "Login" }));
							}
							senhaForm.setCdUsuario(cdUsuario);
							if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
								AdmTabelasWindow adminForm = new AdmTabelasWindow();
								adminForm.popup();
							}
						} else {	
							new AdmTabelasWindow().popup();
						}
					} else if (Messages.MENU_PDA_REORGANIZDADOS.equals(optionSelected)) {
						if (LavenderePdaConfig.usaSistemaModoOffLine) {
							UiUtil.showInfoMessage(Messages.MENU_SISTEMA_OFFLINE);
						} else {
							show(new AdmReorganizacaoForm());
						}
					} else if (Messages.MENU_PDA_BACKUP_PEDIDOS.equals(optionSelected)) {
						AdmBackupPedidosForm backupPedidosForm = new AdmBackupPedidosForm();
						show(backupPedidosForm);
					} else if (Messages.MENU_SISTEMA_GERAR_SENHA.equals(optionSelected)) {
						new CadGeraSenhaWindow().popup();
					} else if (Messages.MENU_CONFIGURACOES_GPS.equals(optionSelected)) {
						show(new AdmTesteGps());
					} else if (Messages.MENU_PDA_ATUALIZARAPP.equals(optionSelected)) {
						if (LavenderePdaConfig.usaSistemaModoOffLine) {
							UiUtil.showInfoMessage(Messages.MENU_SISTEMA_OFFLINE);
						} else {
							if (LavenderePdaConfig.isUsaLiberacaoComSenhaRestauracaoBackup()) {
								AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow(SenhaDinamica.SENHA_LIBERACAO_RESTAURACAO_BACKUP);
								admSenhaDinamicaWindow.setMensagem(Messages.MSG_LIBERA_COM_SENHA_RESTAURACAO_BACKUP);
								admSenhaDinamicaWindow.setCdUsuario(Session.getCdUsuario());
								if (admSenhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
									throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
								}
							}
							SyncExtend.atualizaApp();
						}
					} else if (Messages.MENU_PDA_DOWNLOAD_WTOOLS.equals(optionSelected)) {
						if (LavenderePdaConfig.usaSistemaModoOffLine) {
							UiUtil.showInfoMessage(Messages.MENU_SISTEMA_OFFLINE);
						} else {
							if (UiUtil.showConfirmYesNoMessage(Messages.MSG_TITLE_DOWNLOAD_WTOOLS, Messages.MSG_ATUALIZAR_DOWNLOAD_WTOOLS_CONFIRMACAO)) {
								SyncManager.downloadInstallWtools();
							}
						}
					} else if (Messages.MENU_SISTEMA_SQLEXEC.equals(optionSelected)) {
						if (LavenderePdaConfig.usaSenhaSqlExecutor && !VmUtil.isJava()) {
							AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
							senhaForm.setMensagem(Messages.SENHADINAMICA_TITULO_SQL_EXECUTOR);
							senhaForm.setChaveSemente(SenhaDinamica.SENHA_SQL_EXECUTOR);
							senhaForm.setCdUsuario(SessionLavenderePda.usuarioPdaRep.cdUsuario);
							if (senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
								AdmSqlExecuteForm admSqlExecuteWindow = new AdmSqlExecuteForm();
								show(admSqlExecuteWindow);
							}
						} else {
							AdmSqlExecuteForm admSqlExecuteWindow = new AdmSqlExecuteForm();
							show(admSqlExecuteWindow);
						}
					} else if (FrameworkMessages.MENU_ALTERAR_SENHA.equals(optionSelected)) {
						CadAlterarSenhaLavendereWindow cadAlterarSenhaWindow = new CadAlterarSenhaLavendereWindow(SessionLavenderePda.usuarioPdaRep.usuario);
						cadAlterarSenhaWindow.popup();
					} else if (FrameworkMessages.LOGSYNCBACKGROUND_TITULO.equals(optionSelected)) {
						RelLogSyncBackgroundLavendereForm relLogSyncBackgroundForm = new RelLogSyncBackgroundLavendereForm();
						show(relLogSyncBackgroundForm);
					} else if (Messages.MENU_SISTEMA_ENVIAR_BANCO.equals(optionSelected)) {
						btEnviarBancoDadosClick();
					} else if (Messages.MENU_SISTEMA_TECLADO.equals(optionSelected)) {
						AdmTecladoWindow admTecladoWindow = new AdmTecladoWindow();
						admTecladoWindow.popup();
					} else if (Messages.MENU_NOTIFICACAO_SISTEMA.equals(optionSelected)){
						show(new ListNotificacaoForm());
					}
				}
				break;
			}
		}
	}

	private void showConexaoWindow() {
		if (LavenderePdaConfig.usaSistemaModoOffLine) {
			UiUtil.showInfoMessage(Messages.MENU_SISTEMA_OFFLINE);
		} else {
			if (MainLavenderePda.getInstance().isInSyncForm()) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.MENU_SISTEMA_INDISPONIVEL_FORM, new String[] { Messages.MENU_CONFIGURACOES_CONEXAO, "Sincronização" }));
			} else {
				ListConexaoPdaWindow listUsuarioPdaConexaoWindow = new ListConexaoPdaWindow();
				listUsuarioPdaConexaoWindow.popup();
			}
		}
	}

	private void enviaLogWGpsServidor() {
		new AsyncUIControl() {
			@Override
			public void execute() {
				try {
					SyncManager.enviaLogsWGpsServidor(false);
					LogSync.sucess(Messages.ENVIAR_LOG_GPS_SUCESSO);
				} catch (Throwable e) {
					LogSync.error(MessageUtil.getMessage(Messages.ENVIAR_LOG_GPS_ERRO, e.getMessage()));
				}
			}
		}.open();
		
	}

	@Override
	public void onFormClose() throws SQLException {
		//Não é necessário
	}

	public boolean isVisibledFull() {
		return visibledFull;
	}

	public void setVisibledFull(boolean visibledFull) {
		this.visibledFull = visibledFull;
	}
	
	private void btEnviarBancoDadosClick() {
		new AsyncUIControl() {
			
			@Override
			public void execute() {
				try {
					TabelaDbService.getInstance().enviaBancoToServidor();
					LogSync.sucess(Messages.ADMINISTRACAO_MSG_BANCO_ENVIADO_SUCESSO);
				} catch (Throwable ex) {
					LogSync.error(ex.getMessage());
				}
			}
			@Override
			public boolean beforeAsync() {
				return UiUtil.showWarnConfirmYesNoMessage(Messages.ADMINISTRACAO_MSG_CONFIRM_ENVIAR_BANCO);
			}
			
		}.open();
	}
	
}
