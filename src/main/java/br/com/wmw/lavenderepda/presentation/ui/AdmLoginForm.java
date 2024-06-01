package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncExecution;
import br.com.wmw.framework.async.AsyncWindow;
import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationConstants;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.presentation.ui.BaseMainWindow;
import br.com.wmw.framework.presentation.ui.BaseWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GradientContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.EquipamentoUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigAcessoSistema;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import br.com.wmw.lavenderepda.business.domain.FotoProduto;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoEmp;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.FuncaoConfig;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.domain.dto.RetValidaSenhaDTO;
import br.com.wmw.lavenderepda.business.service.ConfigAcessoSistemaService;
import br.com.wmw.lavenderepda.business.service.ConfigIntegWebTcService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.FeriadoService;
import br.com.wmw.lavenderepda.business.service.FuncaoConfigService;
import br.com.wmw.lavenderepda.business.service.LogLoginService;
import br.com.wmw.lavenderepda.business.service.ManutencaoService;
import br.com.wmw.lavenderepda.business.service.UsuarioLavendereService;
import br.com.wmw.lavenderepda.business.validation.LoginFailureControlException;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.presentation.ui.combo.EmpresaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteComboBox;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.thread.BloqueioAcessoUsuarioSistemaRunnable;
import br.com.wmw.lavenderepda.util.AppConfFilesUtil;
import totalcross.sys.Settings;
import totalcross.sys.SpecialKeys;
import totalcross.ui.Edit;
import totalcross.ui.ImageControl;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class AdmLoginForm extends BaseWindow {

	public static final int LOGIN_RESULT_OK = 1;
	public static final int LOGIN_RESULT_CANCEL = 2;

	public int result;
	public int titleSpace;
	public int titleArea;
	private Image imgLogoSistema;
	private Image imgLogoEmpresa;
	private Image imgLogoPoweredBy;
	private ImageControl icLogoSistema;
	private ImageControl icLogoEmpresa;
	private ImageControl icLogoPoweredBy;
	private GradientContainer containerFundo;
	private SessionContainer containerEmpresaImg;
	private LabelName lbEmp;
	private LabelName lbUsuario;
	private LabelName lbUrlServidor;
	private LabelName lbRep;
	private LabelName lbSenha;
	private EmpresaComboBox cbEmpresa;
	private RepresentanteComboBox cbRep;
	private EditText lvRep;
	private EditText edUsuario;
	private EditText edUrlServidor;
	private EditText edSenha;
	private BaseButton btOK;
	private LabelName lbVersao;
	public UsuarioPdaRep usuarioPdaRep;
	private MenuSuperior menuSuperior;
	private boolean canShowMenu = true;
	boolean cargaVazia;
	private int yPenEventStart;
	private boolean warnOpen;
	private boolean recebendoDados;
	private int widthImgPerc = 5;
	
	private boolean cargaDefaultSemUsuario = true;

	public AdmLoginForm() throws Exception {
		super(null, NO_BORDER);
		setBackForeColors(ColorUtil.formsBackColor, ColorUtil.sessionContainerForeColor);
		loadImages();
		icLogoSistema = new ImageControl();
		icLogoSistema.transparentBackground = true;
		icLogoEmpresa = new ImageControl();
		icLogoEmpresa.transparentBackground = true;
		icLogoPoweredBy = new ImageControl();
		icLogoPoweredBy.transparentBackground = true;
		containerFundo = new GradientContainer();
		containerEmpresaImg = new SessionContainer();
		containerEmpresaImg.setBackColor(ColorUtil.baseForeColorSystem);
		containerEmpresaImg.setBackColor(Color.BLUE);
		containerEmpresaImg.transparentBackground = true;
		
    	lbEmp = new LabelName(Messages.EMPRESA_NOME_ENTIDADE);
    	lbEmp.setForeColor(ColorUtil.baseBackColorSystem);
    	cbEmpresa = new EmpresaComboBox(lbEmp.getValue());
    	cbEmpresa.setLoginEditProperties();
 	    lbSenha = new LabelName(FrameworkMessages.LOGIN_LABEL_SENHA);
 	    lbSenha.setForeColor(ColorUtil.baseBackColorSystem);
 	    lbUrlServidor = new LabelName(Messages.CONEXAOPDA_LABEL_DSURLWEBSERVICE);
 	    edUrlServidor = new EditText("@@@@@@@@@@@", 100);
 	    edUrlServidor.setValue("http://");
 	    edUrlServidor.setLoginEditProperties();
 	    edUsuario = new EditText("@@@@@@@@@@@", 8);
 	    edUsuario.setLoginEditProperties();
		edSenha = new EditText("@@@@@@@@@@@@@@@@@@@@@@@@", 25);
		edSenha.setLoginEditProperties();
		edSenha.setMode(Edit.PASSWORD_ALL);
		edSenha.setID("edSenha");
		lbUsuario = new LabelName(Messages.USUARIO_NOME_ENTIDADE);
		lbUsuario.setForeColor(ColorUtil.baseBackColorSystem);
		lbRep = new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE);
		lbRep.setForeColor(ColorUtil.baseBackColorSystem);
		cbRep = new RepresentanteComboBox();
		cbRep.setLoginEditProperties();
		lvRep = new EditText("ss", 100);

		lvRep.setEnabled(false);
		lvRep.drawBackgroundWhenDisabled = false;
		lvRep.setLoginEditProperties();
		btOK = new BaseButton(FrameworkMessages.BOTAO_ENTRAR, UiUtil.getColorfulImage("images/login.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight(), getBtOkForeColor()), RIGHT, WIDTH_GAP);
		btOK.setBackForeColors(getBtOkBackColor(), getBtOkForeColor());
		btOK.setBorder(BaseButton.BORDER_ROUND);
		btOK.useBorder = false;
		btOK.setID("btOK");
		btOK.roundBorderFactor = UiUtil.BASE_BUTTON_ROUND_FACTOR;
		lbVersao = new LabelName(" ");
		lbVersao.setForeColor(ColorUtil.sessionContainerForeColor);
		lbVersao.setFont(UiUtil.fontVerySmall);
		
		loadUsuario();
		loadParametrosSistema();
		MainLavenderePda.getInstance().carregaUsuarioAtivo();
		MainLavenderePda.getInstance().controlaDataEquipamento();
		cargaVazia = cbRep.size() == 0 && cbEmpresa.size() == 0;
		cargaDefaultSemUsuario = UsuarioLavendereService.getInstance().count() == 0;
		if (cargaDefaultSemUsuario) {
			loadEdUsuarioCargaGenerica();
		}
		makeUnmovable();
		setRect(LEFT, TOP, FILL, FILL);
	}

	private int getBtOkBackColor() {
		boolean gradientBackground = ColorUtil.secondarySessionColor != ColorUtil.secondaryColorSystem;
		int step = gradientBackground ? 16 : 36;
		if (ColorUtil.isLightTheme()) {
			return Color.brighter(ColorUtil.secondarySessionColor, step);
		}
		return gradientBackground ? ColorUtil.secondarySessionColor : Color.darker(ColorUtil.secondarySessionColor, step);
	}
	
	private int getBtOkForeColor() {
		if (ColorUtil.isLightTheme()) {
			return ColorUtil.baseBackColorSystem;
		}
		return ColorUtil.componentsForeColor;
	}

	private void loadImages() throws SQLException {
		try {
			imgLogoEmpresa = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DA_EMPRESA);
			if (imgLogoEmpresa == null) {
				imgLogoEmpresa = UiUtil.getImageDefault(FileUtil.PATH_LOGO_EMPRESA);
				widthImgPerc = 3;
			}
			imgLogoSistema = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DO_SISTEMA);
			if (imgLogoSistema == null) {
				imgLogoSistema = UiUtil.getImageDefault(FileUtil.PATH_LOGO_SISTEMA);
			}
			imgLogoPoweredBy = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.POWERED_BY_WMW);
			if (imgLogoPoweredBy == null) {
				imgLogoPoweredBy = UiUtil.getImageDefault(FileUtil.PATH_LOGO_POWEREDBY);
			}
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private void loadParametrosSistema() {
		try {
			LavenderePdaConfig.habilitaEquipamentoParaUsoPorPadrao = ValueUtil.getBooleanValue(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.HABILITAEQUIPAMENTOPARAUSOPORPADRAO));
			LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades = ValueUtil.getBooleanValue(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USADESCRICAOCODIGONAVISUALIZACAOENTIDADES));
			LavenderePdaConfig.utilizaApenasControleDeDataAcessoSistema = ValueUtil.getBooleanValue(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.UTILIZAAPENASCONTROLEDEDATAACESSOSISTEMA));
			LavenderePdaConfig.usaSistemaModoOffLine = ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USASISTEMAMODOOFFLINE));
			LavenderePdaConfig.usaSenhaAdministracaoTabelas = ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USASENHAADMINISTRACAOTABELAS));
			LavenderePdaConfig.sementeSenhaAdministracaoTabelas = ValueUtil.getIntegerValue(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.SEMENTESENHAADMINISTRACAOTABELAS));
			LavenderePdaConfig.geraCargaQuandoAtualizacaoMuitoGrande = ValueUtil.getIntegerValue(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.GERACARGAQUANDOATUALIZACAOMUITOGRANDE));
			LavenderePdaConfig.usaSenhaDoUsuarioPdaSensivelAoCase = LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USASENHADOUSUARIOPDASENSIVELAOCASE);
			LavenderePdaConfig.carregaParametrosTecladoAntesDeLogar();
			LavenderePdaConfig.loadSyncWebAppEntidadeRepZero(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.SYNCWEBAPPENTIDADEREPZERO));
			LavenderePdaConfig.replicaDadosItemTabelaPreco0PorTabelaPreco = ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.REPLICADADOSITEMTABELAPRECO0PORTABELAPRECO));
			LavenderePdaConfig.usaControleOnlineUsuariosInativos = ValueUtil.valueEquals(ValueUtil.VALOR_SIM, LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USACONTROLEONLINEUSUARIOSINATIVOS));
			LavenderePdaConfig.bloqueiaSistemaEquipamentoInativo = ValueUtil.getBooleanValue(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.BLOQUEIASISTEMAEQUIPAMENTOINATIVO));
			LavenderePdaConfig.loadControleLoginUsuario();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	public void initUI() {
		super.initUI();
		try {
			int heigthContainerImages = Settings.screenWidth > Settings.screenHeight ? Settings.screenHeight / 3 : Settings.screenHeight / 2;
			UiUtil.add(this, containerFundo, LEFT, TOP, FILL, FILL);
			UiUtil.add(this, containerEmpresaImg, LEFT, SAME, FILL, heigthContainerImages);
			if ((imgLogoEmpresa != null && imgLogoEmpresa.getWidth() > 0) && (imgLogoSistema != null && imgLogoSistema.getWidth() > 0)) {
				int wh = Math.min(Settings.screenWidth, Settings.screenHeight);
				icLogoEmpresa.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoEmpresa, wh * widthImgPerc / 10, wh * widthImgPerc / 10));
				UiUtil.add(containerEmpresaImg, icLogoEmpresa, CENTER, CENTER - HEIGHT_GAP_BIG * 4);
				icLogoSistema.setImage(UiUtil.getSmoothScaledImageForMaxSize(imgLogoSistema, wh * 3 / 8, wh * 3 / 8));
				UiUtil.add(containerEmpresaImg, icLogoSistema, CENTER, AFTER + HEIGHT_GAP_BIG * 2);
			}
			//--
			int gap = LEFT + WIDTH_GAP_BIG * 8;
			int fill = FILL - WIDTH_GAP_BIG * 8;
			int y = heigthContainerImages;
			if (cbEmpresa.size() > 1) {
				UiUtil.add(containerFundo, lbEmp, cbEmpresa, gap, y, fill);
				y = AFTER + HEIGHT_GAP;
			}
			if (cargaDefaultSemUsuario) {
				UiUtil.add(containerFundo, lbUsuario, edUsuario, gap, y, fill);
				UiUtil.add(containerFundo, lbSenha, edSenha, gap, AFTER + HEIGHT_GAP_BIG, fill);
			} else if (cargaVazia) {
				UiUtil.add(containerFundo, lbUsuario, edUsuario, gap, y, fill);
				UiUtil.add(containerFundo, lbUrlServidor, edUrlServidor, gap, AFTER + HEIGHT_GAP_BIG, fill);
			} else {
				UiUtil.add(containerFundo, lbRep, cbRep, gap, y, fill);
				UiUtil.add(containerFundo, lvRep, SAME, SAME, SAME, SAME);
				UiUtil.add(containerFundo, lbSenha, edSenha, gap, AFTER + HEIGHT_GAP_BIG, fill);
			}
			UiUtil.add(containerFundo, btOK, CENTER, AFTER + HEIGHT_GAP_BIG * 4, cargaVazia ? edUsuario.getWidth() : edSenha.getWidth());
			visibleState();
			menuSuperior = new MenuSuperior();
			menuSuperior.setVisible(false);
			titleSpace = Settings.screenWidth;
			titleArea = BaseMainWindow.getBaseMainWindowInstance().getHeigthTitleArea();
			if (cbRep.size() > 0) {
				edSenha.requestFocus();
			} else {
				edUsuario.requestFocus();
			}
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}
	
	@Override
	protected void postPopup() {
		super.postPopup();
		if (cargaDefaultSemUsuario && ValueUtil.isEmpty(edUsuario.getText())) {
			identificaUsuarioPeloEquipamento();
		}
	}

	private void identificaUsuarioPeloEquipamento() {
		new AsyncUIControl() {
			String lastUsuario;
			
			@Override
			public void execute() {
				try {
					LogSync.info(Messages.USUARIO_MSG_VERIFICANDO_ULTIMO_USUARIO_SERVIDOR);
					String retorno = SyncManager.buscaUsuarioEquipamento(EquipamentoUtil.getCdEquipamento());
					if (ValueUtil.isNotEmpty(retorno)) {
						String[] retornoArray = StringUtil.getStringValue(retorno).split(";");
						if (retornoArray.length == 2 && ValueUtil.VALOR_SIM.equals(retornoArray[0]) && ValueUtil.isNotEmpty(retornoArray[1])) {
							lastUsuario = retornoArray[1];
							LogSync.sucess(Messages.USUARIO_MSG_SUCESSO_RECUPERAR_ULTIMO_USUARIO_SERVIDOR);
							return;
						}
					}
					LogSync.warn(Messages.USUARIO_MSG_FALHA_RECUPERAR_ULTIMO_USUARIO_SERVIDOR);
				} catch (Exception e) {
					LogSync.error(e.getMessage());
					LogSync.warn(Messages.USUARIO_MSG_FALHA_RECUPERAR_ULTIMO_USUARIO_SERVIDOR);
					ExceptionUtil.handle(e);
				}
			}
			
			@Override
			public void after() {
				super.after();
				if (ValueUtil.isNotEmpty(lastUsuario)) {
					edUsuario.setValue(lastUsuario);
					edSenha.requestFocus();
				}
			}
		}.open();
	}

	private void loadEdUsuarioCargaGenerica() {
		try {
			String lastUsuario = AppConfFilesUtil.loadCdUsuarioConf();
			if (ValueUtil.isNotEmpty(lastUsuario)) {
				edUsuario.setValue(lastUsuario);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	public void setMenuVisibility(final boolean visible) {
		canShowMenu = visible;
	}

	public int getRepSize() {
		return cbRep.size();
	}

	public int getEmpSize() {
		return cbEmpresa.size();
	}

	public String getSelectedCdRep() {
		return cbRep.getValue();
	}

	@Override
	public void screenResized() {
		super.screenResized();
		if (!recebendoDados) {
			initUI();
		}
		if (Settings.isLandscape() && recebendoDados && !warnOpen) {
			warnOpen = true;
			UiUtil.showWarnMessage("Sistema está recebendo dados. Por favor, retorne a tela para o modo retrato.");
			warnOpen = false;
		}
	}

	private boolean validateLogin() throws SQLException {
		if (getEmpSize() == 0) {
			UiUtil.showErrorMessage(Messages.USUARIO_MSG_LOGIN_REPRESENTANTEEMP_INEXISTENTE);
			LogLoginService.getInstance().insertLogErroLogin(usuarioPdaRep.cdUsuario, Messages.USUARIO_MSG_LOGIN_REPRESENTANTEEMP_INEXISTENTE);
			return false;
		}
		try {
			if (validateLogin(edSenha.getText(), cbRep.getValue())) {
				if (validaAcessoUsuario()) {
					LogLoginService.getInstance().insertLogSucessoLogin(usuarioPdaRep.cdUsuario);
					return true;
				}
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(FrameworkMessages.LOGIN_MSG_LOG_CREDENCIAIS_INCORRETAS, new String[]{""}));
				edSenha.requestFocus();
			}
		} catch (LoginFailureControlException e) {
			LogLoginService.getInstance().insertLogErroLogin(usuarioPdaRep.cdUsuario, e.getMessage());
			UiUtil.showErrorMessage(e.getMessage());
			edSenha.requestFocus();
		}
		return false;
	}
	
	private boolean validaAcessoUsuario() throws SQLException {
		FuncaoConfig funcaoConfig = new FuncaoConfig();
		funcaoConfig.cdFuncao = usuarioPdaRep.usuario.cdFuncao;
		if (! Session.isModoSuporte && LavenderePdaConfig.usaConfigAcessoSistema && isFuncaoValidaAcesso(funcaoConfig)) {
			try {
				UiUtil.updateProcessingMessage(Messages.MSG_VALIDANDO_ACESSO);			
				String flBloqSistemaFeriado = FuncaoConfigService.getInstance().findColumnByRowKey(funcaoConfig.getRowKey(), "flBloqSistemaFeriado");
				boolean feriado = ValueUtil.VALOR_SIM.equals(flBloqSistemaFeriado) && FeriadoService.getInstance().isDtFeriado(new Date()); 
				ConfigAcessoSistema configAcessoSistema = null;
				if (! feriado) {
					configAcessoSistema = ConfigAcessoSistemaService.getInstance().getConfigAcessoSistema(funcaoConfig.cdFuncao, usuarioPdaRep.cdUsuario);
				}
				int seconds = getTempoUsoSistema(configAcessoSistema);
				if (seconds > 0) {
					BloqueioAcessoUsuarioSistemaRunnable.addQueue(seconds, funcaoConfig.cdFuncao, feriado);
				} else {
					int resultado = UiUtil.showMessage(Messages.MSG_BLOQUEIO_SISTEMA, TYPE_MESSAGE.WARN, new String[] {Messages.MSG_BOTAO_SAIR, Messages.MSG_BOTAO_SENHA});
					if (resultado == 1) {
						String tempoLiberado = showPopupSenha();
						if (ValueUtil.isEmpty(tempoLiberado)) {
							LogLoginService.getInstance().insertLogErroLogin(usuarioPdaRep.cdUsuario, Messages.MSG_BLOQUEIO_SISTEMA_LOG);
							return false;
						}
						seconds = TimeUtil.getSecondsBetween(tempoLiberado + ":00", "00:00:00");
						if (! feriado) {
							seconds = ConfigAcessoSistemaService.getInstance().verificaNovoTempoIncideEmHorarioLiberado(funcaoConfig.cdFuncao, seconds);
						}
						BloqueioAcessoUsuarioSistemaRunnable.addQueue(seconds, funcaoConfig.cdFuncao, feriado);
						return true;
					}
					LogLoginService.getInstance().insertLogErroLogin(usuarioPdaRep.cdUsuario, Messages.MSG_BLOQUEIO_SISTEMA_LOG);
					return false;
				}	
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		}
		return true;
	}


	private int getTempoUsoSistema(ConfigAcessoSistema configAcessoSistema) {
		if (configAcessoSistema == null) {
			return 0;
		}
		String hrAtual = TimeUtil.getCurrentTimeHHMMSS();
		return TimeUtil.getSecondsBetween(configAcessoSistema.hrFim + ":00", hrAtual);
	}

	private boolean isFuncaoValidaAcesso(FuncaoConfig funcaoConfig) throws SQLException {
		String flUsaConfigAcessoSistema = FuncaoConfigService.getInstance().findColumnByRowKey(funcaoConfig.getRowKey(), "flUsaConfigAcessoSistema");
		return ValueUtil.VALOR_SIM.equals(flUsaConfigAcessoSistema);
	}

	private String showPopupSenha() throws SQLException {
		AdmSenhaDinamicaWindow senhaWindow = new AdmSenhaDinamicaWindow();
		senhaWindow.setMensagem(Messages.MSG_SENHA_DESBLOQUEAR);
		senhaWindow.setChaveSemente(SenhaDinamica.SENHA_SISTEMA_BLOQUEADO);
		senhaWindow.setCdUsuario(usuarioPdaRep.usuario.cdUsuario);
		if (senhaWindow.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			return senhaWindow.edTempoLiberacao.getValueWithMask();
		}
		return null;
	}


	public void onLoginSucess() throws SQLException {
		result = LOGIN_RESULT_OK;
		SessionLavenderePda.cdEmpresa = cbEmpresa.getValue();
		ConfigInternoService.getInstance().salvaEmpRepSelecionado(cbEmpresa.getValue(), cbRep.getValue());
	}

	protected boolean validateLogin(String dsSenha, String cdRep) throws SQLException {
		usuarioPdaRep = UsuarioLavendereService.getInstance().validateLogin(dsSenha, cdRep);
		return usuarioPdaRep != null;
	}
	
	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		try {
			switch (event.type) {
				case ControlEvent.PRESSED: {
					if (event.target == btOK) {
						btEntrarClick();
					} else if (event.target == cbEmpresa) {
						cbRep.carregaRepresentantes(cbEmpresa.getValue());
						visibleState();
					}
					break;
				}
				case KeyEvent.SPECIAL_KEY_PRESS: {
					if (((KeyEvent)event).key == SpecialKeys.ENTER || ((KeyEvent)event).key == SpecialKeys.ACTION) {
						this.requestFocus();
						btEntrarClick();
					} else if (((KeyEvent)event).key == SpecialKeys.ESCAPE) {
						result = LOGIN_RESULT_CANCEL;
						unpop();
					}
					break;
				}
				case PenEvent.PEN_DOWN: {
					yPenEventStart = ((PenEvent)event).absoluteY;
					if (canShowMenu && !menuSuperior.isVisibledFull()) {
						if (((PenEvent)event).absoluteY < titleArea && ((PenEvent)event).absoluteX > Settings.screenWidth / 2 - titleSpace / 2 && ((PenEvent)event).absoluteX < Settings.screenWidth / 2 + titleSpace / 2) {
							UiUtil.add(this, menuSuperior, 0, 0 - height + (UiUtil.getLabelPreferredHeight() + UiUtil.getLabelPreferredHeight() / 3), width, height);
							menuSuperior.setVisible(true);
						}
					}
					break;
				}
				case PenEvent.PEN_UP: {
					if ((menuSuperior != null) && menuSuperior.isVisible() && !menuSuperior.isVisibledFull()) {
						menuSuperior.setVisibledFull(false);
						UiUtil.doEfectPopupUpToDownWithWindow(this, menuSuperior, UiUtil.getLabelPreferredHeight() - 2, true);
						menuSuperior.setVisible(false);
						this.remove(menuSuperior);
						yPenEventStart = -1;
					}
					break;
				}
				case PenEvent.PEN_DRAG: {
					if (canShowMenu && (yPenEventStart >= 0) && (menuSuperior != null)) {
						if ((yPenEventStart < titleArea) && !menuSuperior.isVisibledFull()) {
							if (!menuSuperior.isVisible()) {
								UiUtil.add(this, menuSuperior, 0, 0 - height, width, height);
								menuSuperior.setVisible(true);
							} else {
								menuSuperior.setRect(0, (((PenEvent)event).absoluteY - height) <= 0 ? ((PenEvent)event).absoluteY - height : 0, width, height);
							}
						} else if ((yPenEventStart > (height - titleArea)) && menuSuperior.isVisibledFull()) {
							menuSuperior.setRect(0, (((PenEvent)event).absoluteY - height) <= 0 ? ((PenEvent)event).absoluteY - height : 0, width, height);
						}
					}
					break;
				}
				case PenEvent.PEN_DRAG_END: {
					if ((menuSuperior != null) && menuSuperior.isVisible()) {
						int yPenEventEnd = ((PenEvent)event).absoluteY;
						if ((yPenEventStart < titleArea) && !menuSuperior.isVisibledFull()) {
							int minDragToShowWindow = (height / 9) * 4;
							menuSuperior.setVisibledFull(true);
							if ((yPenEventEnd - yPenEventStart) > minDragToShowWindow) {
								UiUtil.doEfectPopupUpToDownWithWindow(this, menuSuperior, yPenEventEnd, false);
								yPenEventStart = -1;
							} else {
								UiUtil.doEfectPopupUpToDownWithWindow(this, menuSuperior, yPenEventEnd, true);
								menuSuperior.setVisible(false);
								menuSuperior.setVisibledFull(false);
								this.remove(menuSuperior);
								yPenEventStart = -1;
							}
						} else if (yPenEventStart >= (height - titleArea)) {
							int minDragToShowWindow = height / 8;
							menuSuperior.setVisibledFull(false);
							if ((yPenEventStart - yPenEventEnd) > minDragToShowWindow) {
								UiUtil.doEfectPopupUpToDownWithWindow(this, menuSuperior, yPenEventEnd, true);
								menuSuperior.setVisible(false);
								this.remove(menuSuperior);
								yPenEventStart = -1;
							} else {
								UiUtil.doEfectPopupUpToDownWithWindow(this, menuSuperior, yPenEventEnd, false);
								menuSuperior.setVisibledFull(true);
								yPenEventStart = -1;
							}
						}
					}
					break;
				}
			}
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
	}
	
	private void btEntrarClick() throws Exception {
		if (cargaVazia || cargaDefaultSemUsuario) {
			recebeDadosSistema();
		} else {
			if (validateLogin()) {
				onLoginSucess();
				unpop();
			}
		}
	}
	
	private void loginAposReceberDados() throws SQLException {
		if (cargaDefaultSemUsuario) {
			loadUsuario();
			if (validateLogin()) {
				removeConfigInternoDadosPendentes();
				onLoginSucess();
				unpop();
			}
			return;
		}
		LogSync.sucess(Messages.ADMINISTRACAO_TODAS_TABLES_RECEBIDAS_REINICIOSISTEMA);
		MainLavenderePda.getInstance().simpleExit();
	}
	
	private void recebeDadosInicial() {
		try {
			if (cargaDefaultSemUsuario) {
				if (ValueUtil.isEmpty(edUsuario.getValue())) {
					throw new ValidationException(Messages.USUARIO_MSG_LOGIN_CREDENCIAIS_INVALIDAS);
				}
				RetValidaSenhaDTO retorno = validaAcessoCargaSemUsuario(edUsuario.getValue(), edSenha.getText());
				if (retorno == null || !retorno.isLoginValido()) {
					Session.setCdUsuario(null);
					throw new ValidationException(Messages.USUARIO_MSG_LOGIN_CREDENCIAIS_INVALIDAS);
				}
				if (ValueUtil.isNotEmpty(retorno.getNmUsuario()) && !confirmaReceberDadosUsuario(retorno.getNmUsuario())) {
					Session.setCdUsuario(null);
					return;
				}
			}
			ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
			ps.readTimeout = 300_000;
			Vector web2SyncList = cargaDefaultSemUsuario ? getTableNamesFromConfigIntegWebTc() : getTableNamesFromDaosConfig();
			LavendereWeb2Tc web2Tc = new LavendereWeb2Tc(ps);
			if (cargaDefaultSemUsuario) {
				web2Tc.syncCargaInicialAppSemUsuario = true;
				saveConfigInternoDadosPendentes();
			}
			web2Tc.recebeDadosDisponiveisServidor(SyncManager.getInfoAtualizacaoByWeb2SyncList(web2SyncList));
			LogSync.sucess(FrameworkMessages.MSG_SYNC_INFO_RECEBIMENTO_CONCLUIDO);
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.LOGIN_DADOS_SUCESS) {
				@Override
				public void process() throws Exception {
					UiUtil.showSucessMessage(FrameworkMessages.MSG_SYNC_INFO_RECEBIMENTO_CONCLUIDO);
				}
			});
		} catch (Throwable e) {
			String msg = e.getMessage();
			LogSync.error(msg);
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.LOGIN_DADOS_ERROR, msg) {
				@Override
				public void process() throws Exception {
					UiUtil.showErrorMessage(message);
				}
			});
		} finally {
			recebendoDados = false;
		}
	}

	private boolean confirmaReceberDadosUsuario(String nmUsuario) {
		return UiUtil.showWarnConfirmYesNoMessage(MessageUtil.getMessage(Messages.USUARIO_MSG_LOGIN_CONFIRMACAO_RECEBER_DADOS, new String[] {edUsuario.getValue(), nmUsuario}));
	}
	
	private void recebeDadosSistema() throws SQLException, Exception {
		new AsyncWindow(new AsyncExecution() {
			@Override
			public void executeAsync() {
				recebeDadosInicial();
			}

			@Override
			public boolean beforeExecuteAsync() {
				return true;
			}

			@Override
			public void afterExecuteASync() {
				postReceberDados();
			}

		}, true).popup();
	}
	
	private void postReceberDados() {
		try {
			Notification n = null;
			while ((n = NotificationManager.getNextNotification()) != null) {
				n.process();
				if (n.type == LavendereNotificationConstants.LOGIN_DADOS_SUCESS) {
					loginAposReceberDados();
					return;
				}
				if (n.type == NotificationConstants.USUARIO_INATIVO) {
					MainLavenderePda.getInstance().exit();
				}
				}
			
		} catch (Exception e) {
			UiUtil.showErrorMessage(e);
		}
	}

	private void saveConfigInternoDadosPendentes() throws SQLException {
		ConfigInternoService.getInstance().addValueGeral(ConfigInterno.CARGADADOSAPPSEMUSUARIOPENDENTE, ValueUtil.VALOR_SIM);
	}

	private void removeConfigInternoDadosPendentes() {
		ConfigInternoService.getInstance().removeConfigInternoGeral(ConfigInterno.CARGADADOSAPPSEMUSUARIOPENDENTE, ConfigInterno.VLCHAVEDEFAULT);
	}

	private Vector getTableNamesFromDaosConfig() throws SQLException {
		ConfigIntegWebTc configIntegWebTcFilter = new ConfigIntegWebTc();
		Vector web2SyncList = new Vector();
		Vector tablesNames = ManutencaoService.getInstance().getTableNamesFromDaoNamesConfig();
		int size = tablesNames.size();
		for (int i = 0; i < size; i++) {
			configIntegWebTcFilter.dsTabela = (String) tablesNames.items[i];
			ConfigIntegWebTc webTcProducaoProd = (ConfigIntegWebTc) ConfigIntegWebTcService.getInstance().findByRowKey(configIntegWebTcFilter.getRowKey());
			if (webTcProducaoProd != null) {
				web2SyncList.addElement(webTcProducaoProd);
			} else {
				VmUtil.debug("Tabela não encontrada na leitura do DaosConfig " + configIntegWebTcFilter.dsTabela);
		}
		}
		return web2SyncList;
	}
	
	private Vector getTableNamesFromConfigIntegWebTc() {
		Vector configIntegWebTcList = new Vector();
		try {
			configIntegWebTcList = ConfigIntegWebTcService.getInstance().findAllRemessa();
		} catch (SQLException e) {
			throw new ValidationException(Messages.SINCRONIZACAO_ERRO_DEFINICAO_TABELAS_REMESSA);
		}
		int size = configIntegWebTcList.size();
		Vector web2SyncList = new Vector(size);
		for (int i = 0; i < size; i++) {
			ConfigIntegWebTc configIntegWebTc = (ConfigIntegWebTc) configIntegWebTcList.items[i];
			web2SyncList.addElement(configIntegWebTc);
		}
		removeTabelasRecebidasCargaInicialFotos(web2SyncList);
		return web2SyncList;
	}

	private void removeTabelasRecebidasCargaInicialFotos(Vector web2SyncList) {
		ConfigIntegWebTc configIntegWebTc = new ConfigIntegWebTc();
		configIntegWebTc.dsTabela = FotoProduto.TABLE_NAME;
		web2SyncList.removeElement(configIntegWebTc);
		configIntegWebTc.dsTabela = FotoProdutoEmp.TABLE_NAME;
		web2SyncList.removeElement(configIntegWebTc);
		configIntegWebTc.dsTabela = FotoProdutoGrade.TABLE_NAME;
		web2SyncList.removeElement(configIntegWebTc);
		configIntegWebTc.dsTabela = FotoClienteErp.TABLE_NAME;
		web2SyncList.removeElement(configIntegWebTc);
		configIntegWebTc.dsTabela = DivulgaInfo.TABLE_NAME;
		web2SyncList.removeElement(configIntegWebTc);
	}

	private RetValidaSenhaDTO validaAcessoCargaSemUsuario(String cdUsuario, String senha) {
		Session.setCdUsuario(cdUsuario);
		try {
			return SyncManager.validaLoginOnline(Session.getCdUsuario(), senha);
		} catch (Throwable e) {
			Session.setCdUsuario(null);
			throw new ValidationException(MessageUtil.getMessage(Messages.USUARIO_ERRO_LOGIN_VALIDAR_ACESSO, e.getMessage()));
		}
	}

	@Override
	protected void paintTitle(String title, Graphics gg) {
		super.paintTitle(title, gg);
		rTitle = null;
	}

	public void loadUsuario() throws SQLException {
		String ultimoCdRep = ConfigInternoService.getInstance().getUltimoCdRepLogado();
		Empresa empresa = EmpresaService.getInstance().getEmpresaDefaultRepresentante(ultimoCdRep);
		if (empresa != null) {
			cbEmpresa.loadEmpresa(empresa.cdEmpresa);
		}
		cbRep.carregaRepresentantes(cbEmpresa.getValue());
	}

	private void visibleState() {
		if (cbRep.size() > 1) {
			lvRep.setVisible(false);
			cbRep.setVisible(true);
		} else if (cbRep.size() == 1) {
			cbRep.setSelectedIndex(0);
			cbRep.setVisible(false);
			lvRep.setVisible(true);
			lvRep.setValue(((Representante)cbRep.getSelectedItem()).toString());
		}
	}

	@Override
	protected void fecharWindow() throws SQLException {
		//Login não pode ser fechada
	}

	@Override
	protected void onUnpop() {
		super.onUnpop();
		imgLogoSistema = null;
		icLogoSistema = null;
		imgLogoEmpresa = null;
		icLogoEmpresa = null;
	}
	
}
