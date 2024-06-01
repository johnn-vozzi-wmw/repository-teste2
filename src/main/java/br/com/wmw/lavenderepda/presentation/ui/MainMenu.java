package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.Menu;
import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.domain.UsuarioSistema;
import br.com.wmw.framework.business.service.AutorizacaoService;
import br.com.wmw.framework.business.service.CampoTelaService;
import br.com.wmw.framework.business.service.FuncaoPapelService;
import br.com.wmw.framework.business.service.MenuService;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.business.service.TelaService;
import br.com.wmw.framework.business.service.UsuarioSistemaService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.DynamicMenu;
import br.com.wmw.framework.presentation.ui.event.MenuEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.UserContainer;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ColetaGps;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.MenuCatalogo;
import br.com.wmw.lavenderepda.business.domain.Novidade;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.PlatVendaMeta;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.AutorizacaoLavendereService;
import br.com.wmw.lavenderepda.business.service.CampoTelaLavendereService;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.ErroEnvioService;
import br.com.wmw.lavenderepda.business.service.FuncaoPapelLavendereService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.MenuCatalogoService;
import br.com.wmw.lavenderepda.business.service.MenuLavendereService;
import br.com.wmw.lavenderepda.business.service.NovidadeService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaAtuaService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaPeridService;
import br.com.wmw.lavenderepda.business.service.PlatVendaMetaService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.TelaLavendereService;
import br.com.wmw.lavenderepda.business.service.UsuarioLavendereService;
import br.com.wmw.lavenderepda.business.service.UsuarioPdaRepService;
import br.com.wmw.lavenderepda.business.service.UsuarioSistemaLavendereService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import br.com.wmw.lavenderepda.util.UiMessagesUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Date;
import totalcross.util.Vector;

public class MainMenu extends DynamicMenu {

	public static int CDTELA_CLIENTES = 1;
	public static int CDTELA_PEDIDOS = 2;
	public static int CDTELA_PRODUTOS = 3;
	public static int CDTELA_VISITA = 5;
	public static int CDTELA_AGENDA = 6;
	public static int CDTELA_RECADO = 7;
	public static int CDTELA_SYNC = 8;
	public static int CDTELA_NOVOCLIENTE = 9;
	public static int CDTELA_NOVIDADES = 10;
	public static int CDTELA_INFOENTREGA = 11;
	public static int CDTELA_ANIVERSARIANTES = 12;
	public static int CDTELA_INADIMPREP = 13;
	public static int CDTELA_INADIMPCLI = 14;
	public static int CDTELA_CLIENTESNAOATENDIDOS = 15;
	public static int CDTELA_METASVENDA = 16;
	public static int CDTELA_METASVENDAUNIF = 17;
	public static int CDTELA_METAACOMP = 18;
	public static int CDTELA_INDICADORESPROD = 19;
	public static int CDTELA_PRODUTIVIDADE = 20;
	public static int CDTELA_METASPRODUTO = 21;
	public static int CDTELA_METASFORNECEDOR = 22;
	public static int CDTELA_METASCLIENTE = 23;
	public static int CDTELA_CESTAPOSITIV = 24;
	public static int CDTELA_ACOMPVISITAS = 25;
	public static int CDTELA_METASGRUPOPROD = 26;
	public static int CDTELA_RESUMODIA = 27;
	public static int CDTELA_NOVOPEDIDO = 28;
	public static int CDTELA_METAVENDACLIENTE = 29;
	public static int CDTELA_CONFIGPARAMETRO = 31;
	public static int CDTELA_TEMAS = 32;
	public static int CDTELA_QRCODE = 33;
	public static int CDTELA_SOBRE = 34;
	public static int CDTELA_IMPRESSAO = 35;
	public static int CDTELA_CARGAPEDIDO = 36;
	public static int CDTELA_PRODUCAOPRODREP = 37;
	public static int CDTELA_DOCNAOIMPRESSO = 38;
	public static int CDTELA_VENCIMENTO_PEDIDO_CONSIGNACAO = 39;
	public static int CDTELA_ANALISE_CLIENTE = 40;
	public static int CDTELA_REMESSA_ESTOQUE = 43;
	public static int CDTELA_VALORIZACAO_PRODUTO = 44;
	public static int CDTELA_FECHAMENTO_DIARIO = 45;
	public static int CDTELA_CARGA_PRODUTO = 46;
	public static int CDTELA_FECHAMENTO_VENDAS = 47;
	public static int CDTELA_CESTA_POSITIVACAO = 48;
	public static int CDTELA_INDICADOR_PROD_INTERNO = 49;
	public static int CDTELA_EXTRATO_PONTUACAO_REP = 50;
	public static int CDTELA_PROSPECT = 51;
	public static int CDTELA_REQUISICAO_SERV = 52;
	public static int CDTELA_PLANEJAR_METAS = 53;
	public static int CDTELA_SOLICITACAO_AUTORIZACAO = 54;

	public static final int CDMENU_PONTUACAO = 50;
	public static final int CDMENU_PONTUACAO_EXTRATO = 51;



	public static int CDTELA_CLIENTE_CHURN = 55;
	public static int CDTELA_LEADS = 56;
	public static int CDTELA_TREINAMENTO = 237;

	private ButtonAction btSair;
	private ButtonAction btTrocarEmpresaRep;
	private ButtonAction btIniciarPararColetaGps;
	private UserContainer userContainer;
	private boolean useBtTrocarEmpresaRep;

	public MainMenu(String newTitle) throws SQLException {
		super(newTitle);
		int typeMenuSaved = ValueUtil.getIntegerValue(ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.tipoMenuSistema, newTitle));
		typeMenu = typeMenuSaved == 0 ? 1 : typeMenuSaved;
		userContainer = new UserContainer();
		Vector empresaList = EmpresaService.getInstance().findAllByRepresentante(SessionLavenderePda.usuarioPdaRep.cdRepresentante);
		useBtTrocarEmpresaRep = empresaList.size() > 1 || UsuarioPdaRepService.getInstance().count() > 1 || LavenderePdaConfig.usaEscolhaModoFeira;
	}

	@Override
	protected boolean isMenuAutorizado(Vector funcaoPapelList, Menu menu) throws SQLException {
		if (!LavenderePdaConfig.mostraExtratoPontuacao && (ValueUtil.valueEquals(CDMENU_PONTUACAO, menu.cdMenu) || ValueUtil.valueEquals(CDMENU_PONTUACAO_EXTRATO, menu.cdMenu))) return false;
		return super.isMenuAutorizado(funcaoPapelList, menu);
	}
	
	@Override
	protected void createIcons() {
		super.createIcons();
		int color;
		color = getUserContainerItemsForeColor();
		btTrocarTypeMenu = new ButtonAction(UiUtil.getColorfulImage("images/menuchange.png", UiUtil.getStyledBarImageIconSize(), UiUtil.getStyledBarImageIconSize(), color));
		btSair = new ButtonAction(UiUtil.getColorfulImage("images/sair.png", UiUtil.getStyledBarImageIconSize(), UiUtil.getStyledBarImageIconSize(), color));
		btIniciarPararColetaGps = new ButtonAction(UiUtil.getColorfulImage("images/gps.png", UiUtil.getStyledBarImageIconSize(), UiUtil.getStyledBarImageIconSize(), color));
		if (btAlert != null) {
			btAlert.setImage(UiUtil.getColorfulImage("images/notificacao.png", UiUtil.getStyledBarImageIconSize(), UiUtil.getStyledBarImageIconSize(), color));
			btAlert.setForeColor(color);
		}
		if (useBtTrocarEmpresaRep) {
			btTrocarEmpresaRep = new ButtonAction(UiUtil.getColorfulImage("images/trocaEmpRep.png", UiUtil.getStyledBarImageIconSize(), UiUtil.getStyledBarImageIconSize(), color));
		}
		if (btRecebeDadosBackground != null) {
			btRecebeDadosBackground.setImage(UiUtil.getColorfulImage("images/iconerecebidoimpressao.png", UiUtil.getStyledBarImageIconSize(), UiUtil.getStyledBarImageIconSize(), color));
		}
	}

	private int getUserContainerItemsForeColor() {
		int color;
		if (isMenuPrincipal()) {
			color = ColorUtil.isVeryBrightColor(ColorUtil.secondarySessionColor) ? Color.BLACK : Color.WHITE;
		} else {
			color = ColorUtil.baseAppBarForeColor;
		}
		return color;
	}

	protected int getCdSistema() {
		return LavendereConfig.CDSISTEMALAVENDEREPDA;
	}

	protected String getCdUsuario() {
		return SessionLavenderePda.usuarioPdaRep.cdUsuario;
	}

	@Override
	protected String getCdFuncaoUsuario() throws SQLException {
		return UsuarioLavendereService.getInstance().findColumnByRowKey(SessionLavenderePda.usuarioPdaRep.usuario.getRowKey(), "cdFuncao");
	}

	@Override
	protected MenuService getMenuServiceInstance() {
		return MenuLavendereService.getInstance();
	}

	@Override
	protected AutorizacaoService getAutorizacaoServiceInstance() {
		return AutorizacaoLavendereService.getInstance();
	}

	@Override
	protected FuncaoPapelService getFuncaoPapelServiceInstance() {
		return FuncaoPapelLavendereService.getInstance();
	}

	@Override
	protected UsuarioSistemaService getUsuarioSistemaServiceInstance() {
		return UsuarioSistemaLavendereService.getInstance();
	}

	@Override
	protected TelaService getTelaServiceInstance() {
		return TelaLavendereService.getInstance();
	}

	@Override
	protected CampoTelaService getCampoTelaServiceInstance() {
		return CampoTelaLavendereService.getInstance();
	}

	@Override
	protected void onFormStart() throws SQLException {
		barBottomContainer.setVisible(false);
		addUserContainer();		
		super.onFormStart();
		if (isMenuPrincipal()) {
			refreshLbEmpresaRepresentante();
			barTopContainer.setBackColor(ColorUtil.secondarySessionColor);
			UiUtil.add(barTopContainer, btSair, RIGHT - WIDTH_GAP_BIG, CENTER, UiUtil.getStyledBarIconSize(), UiUtil.getStyledBarIconSize());
			UiUtil.add(barTopContainer, btTrocarTypeMenu, BEFORE - getStyledIconsGap(), CENTER, UiUtil.getStyledBarIconSize(), UiUtil.getStyledBarIconSize());
			//--
			if (useBtTrocarEmpresaRep) {
				UiUtil.add(barTopContainer, btTrocarEmpresaRep, BEFORE - getStyledIconsGap(), SAME, UiUtil.getStyledBarIconSize(), UiUtil.getStyledBarIconSize());
			}
			if (isUsaColetaGps()) {
				ColetaGpsService.getInstance().encerraColetaGpsSeNecessario();
				UiUtil.add(barTopContainer, btIniciarPararColetaGps, BEFORE - getStyledIconsGap(), SAME, UiUtil.getStyledBarIconSize(), UiUtil.getStyledBarIconSize());
				atualizaCorImagemBtIniciarPararColetaGps();
			}
			LiberacaoSenhaWindow.verificaGpsDesligado(false, true);
		}
	}
	
	private int getStyledIconsGap() {
		return WIDTH_GAP_BIG * 2;
	}
	
	@Override
	protected String getTitle() {
		if (Messages.MENU_PRINCIPAL.equalsIgnoreCase(title)) {
			return ValueUtil.VALOR_NI;
		}
		return super.getTitle();
	}
	
	private void addUserContainer() throws SQLException {
		if (isMenuPrincipal()) {
			userContainer.setItemsForeColor(getUserContainerItemsForeColor());
			userContainer.setImgLogoEmpresa(PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DA_EMPRESA));
			int realUserContainerHeight = UiUtil.getScreenHeightOrigin() * 1 / 5 - UiUtil.getAppBarPreferredHeight();
			if ((double) UiUtil.getScreenHeightOrigin() / UiUtil.getScreenWidthOrigin() < 1.6 && UiUtil.getScreenHeightOrigin() < 1080) {
				realUserContainerHeight += BaseContainer.HEIGHT_GAP_BIG * 3;
			}
			UiUtil.add(this, userContainer, LEFT, UiUtil.getAppBarPreferredHeight(), FILL, realUserContainerHeight);
		}
	}
	
	@Override
	protected int getPosXBtAlert() {
		if (isMenuPrincipal()) {
			int gap = getStyledIconsGap() + UiUtil.getStyledBarIconSize();
			if (isUsaColetaGps()) {
				gap += getStyledIconsGap() + UiUtil.getStyledBarIconSize();
			}
			if (useBtTrocarEmpresaRep) {
				gap += getStyledIconsGap() + UiUtil.getStyledBarIconSize();
			}
			return RIGHT - (WIDTH_GAP * 3) - UiUtil.getStyledBarIconSize() - gap;
		}
		return super.getPosXBtAlert();
	}
	
	private void verificaAvisaVencimentoCargaPedido() throws SQLException {
		if (LavenderePdaConfig.nuDiasRestantesAvisoVencimentoCarga > 0 && CargaPedidoService.getInstance().avisaValidadeCarga()) {
			String[] botoes = new String[] { FrameworkMessages.BOTAO_FECHAR, FrameworkMessages.BOTAO_VISUALIZAR };
			int acao = UiUtil.showMessage(FrameworkMessages.TITULO_MSG_ATENCAO, Messages.CARGAPEDIDO_AVISO_VENCIMENTO, TYPE_MESSAGE.WARN, botoes);
			if (acao != 0) {
				new ListCargaPedidoVencidasWindow(Messages.CARGAPEDIDO_TITULO_LISTA_PROXIMO_VENCIMENTO).popup();
			}
		}
	}

	@Override
	protected void addFundoMenu() {
		if (isMenuPrincipal()) {
			UiUtil.add(this, sessionFundoMenu, LEFT, AFTER - HEIGHT_GAP_BIG * 3, FILL, FILL);
		} else {
			super.addFundoMenu();
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
			case MenuEvent.MENU_CLICKED: {
				Menu menu = (Menu)event.target;
				if (isMenuPrincipal()) {
					SessionLavenderePda.clearSessionCliente();
					ColetaGpsService.getInstance().encerraColetaGpsSeNecessario();
					if (menu.cdMenu != CDTELA_SYNC && !LiberacaoSenhaWindow.verificaGpsDesligado(true, true)) {
						return;
					}
				}
				//--
				if (menu.cdTela == 0) {
					if (LavenderePdaConfig.apresentaPopUpErroEnvioPedidoAcessoTelaPorMenu && ErroEnvioService.getInstance().hasPedidosErroEnvioServidor()) {
						ListErroEnvioWindow listErroEnvioWindow = new ListErroEnvioWindow(this);
						listErroEnvioWindow.popup();
						if (!listErroEnvioWindow.closedByBtFechar) {
							return;
						}
					}
					MainMenu mainSubMenu = new MainMenu(menu.nmMenu);
					mainSubMenu.cdMenuPai = menu.cdMenu;
					show(mainSubMenu);
				} else {
					showTelaFixa(menu);
				}
				break;
			}
			case ControlEvent.PRESSED: {
				if (event.target == btSair) {
					if (LavenderePdaConfig.isAvisaPedidoAbertoFechadoEntrarSairSistema()) {
						UiMessagesUtil.mostraMensagemPedidosAbertos();
					}
					if (LavenderePdaConfig.apresentaPopUpErroEnvioPedidoAoSairSistema && ErroEnvioService.getInstance().hasPedidosErroEnvioServidor()) {
						ListErroEnvioWindow listErroEnvioWindow = new ListErroEnvioWindow(this);
						listErroEnvioWindow.popup();
						if (!listErroEnvioWindow.closedByBtFechar) {
							return;
						}
					}
					MainLavenderePda.getInstance().exit();
				} else if (event.target == btTrocarEmpresaRep) {
					btTrocarEmpresaClick();
				} else if (event.target == btIniciarPararColetaGps) {
					if (LavenderePdaConfig.usaMotivoParadaColetaGps && SessionLavenderePda.isColetaGpsEmAndamento()) {
						CadMotivoParadaColetaWindow cadMotivoParadaColetaWindow = new CadMotivoParadaColetaWindow();
						cadMotivoParadaColetaWindow.popup();
						if (cadMotivoParadaColetaWindow.cadastrouMotivo) {
							startStopColetaGps();
						}
					} else {
						String msg = SessionLavenderePda.isColetaGpsEmAndamento() ? Messages.COLETAGPS_MSG_PARAR_COLETA_GPS : Messages.COLETAGPS_MSG_INICIAR_COLETA_GPS;
						if (UiUtil.showConfirmYesNoMessage(Messages.COLETAGPS_LABEL_TITULO, msg)) {
							startStopColetaGps();
						}
					}
				}
				break;
			}
		}
	}

	private void startStopColetaGps() throws SQLException {
		LoadingBoxWindow msgLoading = UiUtil.createProcessingMessage();
		try {
			msgLoading.popupNonBlocking();
			iniciarPararColetaGps();
			atualizaCorImagemBtIniciarPararColetaGps();
		} finally {
			msgLoading.unpop();
		}
	}


	private void iniciarPararColetaGps() throws SQLException {
		if (SessionLavenderePda.isColetaGpsEmAndamento()) {
			PontoGpsService.getInstance().stopColetaGpsSistema();
			ColetaGps coletaGpsEmAndamento = ColetaGpsService.getInstance().getLastColetaGpsEmAndamento();
			if (DateUtil.getCurrentDate().isAfter(coletaGpsEmAndamento.dtColetaGps)) {
				ColetaGpsService.getInstance().finalizaColetaGpsFimDia(coletaGpsEmAndamento);
				ColetaGpsService.getInstance().iniciaColetaGps("00:00:00", TimeUtil.getCurrentTimeHHMMSS());
			}
			ColetaGpsService.getInstance().paraColetaGps(coletaGpsEmAndamento);
			SessionLavenderePda.setColetaGpsEmAndamento(false);
			if (LavenderePdaConfig.isUsaEnvioPontoGpsBackground()) {
				EnviaDadosThread.getInstance().envioColetaGpsBackground(coletaGpsEmAndamento);
			}
		} else {
			if (PontoGpsService.getInstance().startColetaGpsSistema(true)) {
				ColetaGpsService.getInstance().iniciaColetaGps();
				SessionLavenderePda.setColetaGpsEmAndamento(true);
			}
		}
	}
	
	private void atualizaCorImagemBtIniciarPararColetaGps() {
		if (btIniciarPararColetaGps != null) {
			String imgPath = SessionLavenderePda.isColetaGpsEmAndamento() ? "images/gps.png" : "images/gpsOff.png";
			btIniciarPararColetaGps.setImage(UiUtil.getColorfulImage(imgPath, UiUtil.getStyledBarImageIconSize(), UiUtil.getStyledBarImageIconSize(), getUserContainerItemsForeColor()));
		}
	}
	
	private boolean isUsaColetaGps() {
		return LavenderePdaConfig.isUsaColetaGpsManual();
	}
	
	@Override
	protected void afterChangeTypeMenu(int newType) {
		super.afterChangeTypeMenu(newType);
		try {
			ConfigInternoService.getInstance().addValue(ConfigInterno.tipoMenuSistema, title, StringUtil.getStringValue(newType));
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private void btTrocarEmpresaClick() throws SQLException {
		//Tem mais de uma empresa, portanto apresenta tela para selecionar a empresa desejada.
		AdmTrocaEmpRepWindow empresaForm = new AdmTrocaEmpRepWindow();
		empresaForm.popup();
		if (ValueUtil.isEmpty(empresaForm.cdEmpresa) || empresaForm.result == AdmLoginForm.LOGIN_RESULT_CANCEL) {
			repaint();
		} else if (isSessaoAtual(empresaForm.cdEmpresa, empresaForm.cdRepresentante)) {
			repaint();
			processModoFeiraOnChange(empresaForm);
		} else {
			boolean isColetaGpsEmAndamento = SessionLavenderePda.isColetaGpsEmAndamento();
			if (isColetaGpsEmAndamento) {
				startStopColetaGps();
			}
			SessionLavenderePda.cdEmpresa = empresaForm.cdEmpresa;
			SessionLavenderePda.usuarioPdaRep = UsuarioLavendereService.getInstance().getUsuarioPdaRepByCdRep(empresaForm.cdRepresentante);
			SessionLavenderePda.setRepresentante(SessionLavenderePda.usuarioPdaRep.representante.cdRepresentante, SessionLavenderePda.usuarioPdaRep.representante.nmRepresentante);
			ConfigInternoService.getInstance().salvaEmpRepSelecionado(empresaForm.cdEmpresa, empresaForm.cdRepresentante);
			MainLavenderePda.getInstance().removeBaseFormsButNotMenu();
			SyncManager.limpeCaches();
			MainLavenderePda.getInstance().loadSessao(SessionLavenderePda.usuarioPdaRep);
			processModoFeiraOnChange(empresaForm);
			if (isColetaGpsEmAndamento) {
				startStopColetaGps();
			}
			MainLavenderePda.getInstance().execAdmDeleteDadosAntigos();
			MainMenu menu = new MainMenu(title);
			MainLavenderePda.getInstance().setNewMainForm(menu);
			CadItemPedidoForm.invalidateInstance();
			ListItemPedidoForm.invalidateInstance();
			if (MainLavenderePda.getInstance().permiteAcessoNormalSistema()) {
				MainLavenderePda.getInstance().afterShowMenu();
			}
		}
	}

	private void processModoFeiraOnChange(AdmTrocaEmpRepWindow empresaForm) throws SQLException {
		if (SessionLavenderePda.isModoFeira != empresaForm.modoFeira) {
			SessionLavenderePda.isModoFeira = empresaForm.modoFeira;
			ConfigInternoService.getInstance().salvaModoFeiraSelecionado(empresaForm.cdEmpresa, empresaForm.cdRepresentante, SessionLavenderePda.isModoFeira);
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_MODOFEIRA, MessageUtil.getMessage(Messages.LOG_STATUS_MODO_FEIRA, new String[] {empresaForm.cdRepresentante, empresaForm.cdEmpresa, StringUtil.getStringValue(SessionLavenderePda.isModoFeira)}));
		}
	}

	private boolean isSessaoAtual(String cdEmpresa, String cdRepresentante) {
		String sessionCdRepresentante;
		if (SessionLavenderePda.isUsuarioSupervisor() && ValueUtil.isEmpty(SessionLavenderePda.getRepresentante().cdRepresentante) ) {
			sessionCdRepresentante = SessionLavenderePda.usuarioPdaRep.representante.cdRepresentante;
		} else {
			sessionCdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		return SessionLavenderePda.cdEmpresa.equals(cdEmpresa) && sessionCdRepresentante.equals(cdRepresentante);
	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		if (isMenuPrincipal()) {
			showNovidade();
			showTelaPadraoUsuario();
			//--
			if (PedidoService.getInstance().isCountPedidosAbertosMaiorPermitido()) {
				if (LavenderePdaConfig.usaVerbaUnificada) {
					UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_PEDIDOS_EM_ABERTO_SISTEMA_TODAS_EMPRESA, LavenderePdaConfig.getQtdPedidosPermitidosManterAbertos()));
				} else {
					UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_PEDIDOS_EM_ABERTO_SISTEMA, LavenderePdaConfig.getQtdPedidosPermitidosManterAbertos()));
				}
				ListPedidoForm listPedidoForm = new ListPedidoForm();
				listPedidoForm.inRelPedidosAbertos = true;
				show(listPedidoForm);
			}
			if ("2".equals(LavenderePdaConfig.getUsaCadastroFotoNovoCliente()) || (LavenderePdaConfig.isUsaCadastroFotoNovoCliente() && LavenderePdaConfig.qtMinimaFotosCadastroNovoCliente > 0)) {
				NovoCliente novoCliente = NovoClienteService.getInstance().getClienteSemFoto();
				if (novoCliente != null) {
					UiUtil.showWarnMessage(Messages.NOVOCLIENTE_MSG_OBRIGATORIEDADE_FOTO_LOGAR);
					CadNovoClienteForm cadNovoClienteForm = new CadNovoClienteForm();
					novoCliente = (NovoCliente) NovoClienteService.getInstance().findByRowKeyDyn(novoCliente.getRowKey());
					cadNovoClienteForm.edit(novoCliente);
					show(cadNovoClienteForm);
				}
			}
			if (LavenderePdaConfig.isAvisaPedidoAbertoFechadoEntrarSairSistema()) {
				UiMessagesUtil.mostraMensagemPedidosAbertos();
			}
			if (LavenderePdaConfig.usaConfigCadastroMetasPlataformaVenda()) {
				validaAvisaBloqueioPlanejamento();
			}
			verificaAvisaVencimentoCargaPedido();
			SessionLavenderePda.startBackgroundServices();
		}
	}

	private void showNovidade() throws SQLException {
		Novidade novidade = NovidadeService.getInstance().findNovidadePendenteLeitura(getCdSistema());
		if (ValueUtil.isNotEmpty(novidade.cdNovidade)) {
			NovidadeWindow novidadeWindow = new NovidadeWindow(novidade);
			novidadeWindow.popup();
		}
	}

	private void validaAvisaBloqueioPlanejamento() throws SQLException {
		int qtDiasAlerta = SessionLavenderePda.isUsuarioSupervisor() ? LavenderePdaConfig.getDiasAlertaBloqueioMetaSupCadastroMetasPlataformaVenda() : LavenderePdaConfig.getDiasAlertaBloqueioMetaRepCadastroMetasPlataformaVenda();
		if (qtDiasAlerta <= 0) return;

		int qtDiasBloqueio = SessionLavenderePda.isUsuarioSupervisor() ? LavenderePdaConfig.getDiasBloqueioMetaSupCadastroMetasPlataformaVenda() : LavenderePdaConfig.getDiasBloqueioMetaRepCadastroMetasPlataformaVenda();
		PlatVendaMeta platVendaMeta = PlatVendaMetaService.getInstance().getPlatVendaMeta(qtDiasAlerta, qtDiasBloqueio);
		if (platVendaMeta == null) return;

		PlatVendaMetaAtuaService.getInstance().setPropertiesByAtuaIfExists(platVendaMeta);
		if (ValueUtil.valueEquals(platVendaMeta.flPlanejado, ValueUtil.VALOR_SIM)) return;

		Date dtMeta = platVendaMeta.dtMeta;
		Date dtBloqueio = DateUtil.getDateValue(dtMeta);
		DateUtil.decDay(dtBloqueio, qtDiasBloqueio);
		Date dtAlerta = DateUtil.getDateValue(dtBloqueio);
		DateUtil.decDay(dtAlerta, qtDiasAlerta);
		if (DateUtil.isAfterOrEquals(DateUtil.getCurrentDate(), dtAlerta) && DateUtil.isBefore(DateUtil.getCurrentDate(), dtBloqueio)) {
			String dsPeriodo = PlatVendaMetaPeridService.getInstance().getDsPeriodoBtData(dtMeta, platVendaMeta.cdRepresentante);
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.PLANEJAMENTOMETAVENDA_ALERTA_BLOQUEIO, new String[] {
							Date.getMonthName(dtMeta.getMonth()),
							dsPeriodo,
							StringUtil.getStringValue(dtBloqueio)
						}));

		}
	}

	@Override
	public void onFormExibition() throws SQLException {
		if (isUsaColetaGps()) {
			atualizaCorImagemBtIniciarPararColetaGps();
		}
		super.onFormExibition();
	}

	public void showTelaPadraoUsuario() throws SQLException {
		UsuarioSistema usuarioSistema = SessionLavenderePda.usuarioPdaRep.usuario.getUsuarioSistema();
		if (usuarioSistema != null && usuarioSistema.cdTelaPadrao != 0) {
			Menu menu = new Menu();
			menu.cdTela = usuarioSistema.cdTelaPadrao;
			menu.cdSistema = usuarioSistema.cdSistema;
			Vector funcaoPapelList = getFuncaoPapelServiceInstance().getPapelByFuncao(getCdSistema(), getCdFuncaoUsuario());
			if (getAutorizacaoServiceInstance().isTelaAutorizada(funcaoPapelList, menu)) {
				openMenu(menu);
			}
		}
	}
	
	private void showTelaFixa(Menu menu) throws SQLException {
		if (!(menu.cdTela == CDTELA_SYNC) && LavenderePdaConfig.apresentaPopUpErroEnvioPedidoAcessoTelaPorMenu && ErroEnvioService.getInstance().hasPedidosErroEnvioServidor()) {
			ListErroEnvioWindow listErroEnvioWindow = new ListErroEnvioWindow(this);
			listErroEnvioWindow.popup();
			if (!listErroEnvioWindow.closedByBtFechar) {
				return;
			}
		}
		if (menu.cdTela == CDTELA_CLIENTES) {
			if (controlaConexaoObrigatoria()) {
				if (LavenderePdaConfig.isUsaColetaGpsManual() && !SessionLavenderePda.isColetaGpsEmAndamento() && (LavenderePdaConfig.avisaColetaGpsParada == 1 || LavenderePdaConfig.avisaColetaGpsParada == 3)) {
					showAvisoColetaGpsParada();
				}
				show(new ListClienteForm(false, null, null));
			}
		} else if (menu.cdTela == CDTELA_PEDIDOS) {
			if (controlaConexaoObrigatoria()) {
				show(new ListPedidoForm());
			}
		} else if (menu.cdTela == CDTELA_PRODUTOS) {
			if (controlaConexaoObrigatoria()) {
				showListProduto();
			}
		} else if (menu.cdTela == CDTELA_VISITA) {
			show(new ListVisitaSupervisorForm());
		} else if (menu.cdTela == CDTELA_AGENDA) {
			if (controlaConexaoObrigatoria()) {
				if (LavenderePdaConfig.isUsaColetaGpsManual() && !SessionLavenderePda.isColetaGpsEmAndamento() && (LavenderePdaConfig.avisaColetaGpsParada == 1 || LavenderePdaConfig.avisaColetaGpsParada == 2)) {
					showAvisoColetaGpsParada();
				}
				show(new ListAgendaVisitaForm());
			}
		} else if (menu.cdTela == CDTELA_RECADO) {
			show(new ListRecadoForm());
		} else if (menu.cdTela == CDTELA_NOVOCLIENTE) {
			show(new ListNovoClienteForm());
		} else if (menu.cdTela == CDTELA_NOVIDADES) {
			show(new ListRelNovidadeForm());
		} else if (menu.cdTela == CDTELA_ANIVERSARIANTES) {
			show(new ListAniversariantesForm());
		} else if (menu.cdTela == CDTELA_INFOENTREGA) {
			show(new RelInfoEntregaPedidoForm());
		} else if (menu.cdTela == CDTELA_SYNC) {
			btSyncClick();
		} else if (menu.cdTela == CDTELA_INADIMPREP) {
			show(new ListInadimplenciaRepForm());
		} else if (menu.cdTela == CDTELA_INADIMPCLI) {
			show(new ListInadimplenciaClienteForm());
		} else if (menu.cdTela == CDTELA_INDICADORESPROD) {
			show(new ListValorIndicadorForm(!SessionLavenderePda.isUsuarioSupervisor()));
		} else if (menu.cdTela == CDTELA_PRODUTIVIDADE) {
			show(new ListValorIndicadorForm(true));
		} else if (menu.cdTela == CDTELA_METASPRODUTO) {
			show(new ListMetasPorProdutoForm());
		} else if (menu.cdTela == CDTELA_METASFORNECEDOR) {
			show(new ListMetasPorFornecedorForm());
		} else if (menu.cdTela == CDTELA_METASCLIENTE) {
			show(new ListMetasPorClienteForm());
		} else if (menu.cdTela == CDTELA_METAACOMP) {
			show(new ListMetaAcompanhamentoForm());
		} else if (menu.cdTela == CDTELA_CESTAPOSITIV) {
			show(new ListCestaPositProdutoForm());
		} else if (menu.cdTela == CDTELA_ACOMPVISITAS) {
			show(new ListVisitaAcompForm());
		} else if (menu.cdTela == CDTELA_METASGRUPOPROD) {
			show(new ListMetasPorGrupoProduto1Form());
		} else if (menu.cdTela == CDTELA_METASVENDAUNIF) {
			show(new ListMetaVendaTipoForm());
		} else if (menu.cdTela == CDTELA_CLIENTESNAOATENDIDOS) {
			show(new RelClientesNaoAtendidosForm());
		} else if (menu.cdTela == CDTELA_METASVENDA) {
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				show(new ListMetasVendaForm());
			} else {
				show(new ListMetasVendaRepForm(SessionLavenderePda.getRepresentante().cdRepresentante));
			}
		} else if (menu.cdTela == CDTELA_RESUMODIA) {
			show(new RelResumoDiaForm());
		} else if (menu.cdTela == CDTELA_NOVOPEDIDO) {
			if (LavenderePdaConfig.permiteIniciarPedidoSemCliente) {
				iniciaPedidoSemCliente();
			} else {
				new ListClienteWindow(true, false, false).popup();
			}
		} else if (menu.cdTela == CDTELA_METAVENDACLIENTE) {
			show(new ListMetaVendaCliForm());
		} else if (menu.cdTela == CDTELA_CONFIGPARAMETRO) {
			show(new ListValorParametroForm());
		} else if (menu.cdTela == CDTELA_TEMAS) {
			show(new ListLavendereTemaSistemaForm());
		} else if (menu.cdTela == CDTELA_QRCODE) {
			if (!VmUtil.isAndroid()) {
				UiUtil.showWarnMessage(FrameworkMessages.MENU_QRCODE_APENAS_ANDROID);
			} else {
				String result = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_QRCODE, "");
				if (result != null) {
					UiUtil.showInfoMessage(Messages.MENU_QRCODE, result);
				}
			}
		} else if (menu.cdTela == CDTELA_SOBRE) {
			new AdmSobreLavendereWindow().popup();
		} else if (menu.cdTela == CDTELA_PRODUCAOPRODREP) {
			show(new ListProducaoProdRepForm(SessionLavenderePda.isUsuarioSupervisor()));
		} else if (menu.cdTela == CDTELA_CARGAPEDIDO) {
			show(new ListCargaPedidoForm());
		} else if (menu.cdTela == CDTELA_IMPRESSAO) {
			show(new ImpressaoTesteForm());
		} else if (menu.cdTela == CDTELA_DOCNAOIMPRESSO) {
			show(new ListDocNaoImpressoForm());
		} else if (menu.cdTela == CDTELA_ANALISE_CLIENTE) {
			show(new ListNovoClienteAnaForm());
		} else if (menu.cdTela == CDTELA_CARGA_PRODUTO) {
			show(new ListCargaProdutoForm());
		} else if (menu.cdTela == CDTELA_REMESSA_ESTOQUE) {
			show(new ListRemessaEstoqueForm());
		} else if (menu.cdTela == CDTELA_VENCIMENTO_PEDIDO_CONSIGNACAO) {
			show(new ListVencimentoPedidoConsignacaoForm());
		} else if (menu.cdTela == CDTELA_VALORIZACAO_PRODUTO) {
			show(new ListValorizacaoProdForm());
		} else if (menu.cdTela == CDTELA_FECHAMENTO_DIARIO) {
			show(new RelFechamentoDiarioForm());
		} else if (menu.cdTela == CDTELA_FECHAMENTO_VENDAS) {
			show(new CadFechamentoVendasForm());
		} else if (menu.cdTela == CDTELA_CESTA_POSITIVACAO) {
			show(new ListCestaPositivacaoForm());
		} else if (menu.cdTela == CDTELA_INDICADOR_PROD_INTERNO) {
			show(new ListIndicadorProdutividadeInternoForm());
		} else if (menu.cdTela == CDTELA_REQUISICAO_SERV) {
			show(new ListRequisicaoServForm());
		} else if (menu.cdTela == CDTELA_PROSPECT) {
			show(new ListProspectForm());
		} else if (menu.cdTela == CDTELA_EXTRATO_PONTUACAO_REP) {
			show(new RelPontuacaoExtratoRepresentanteForm());
		} else if (menu.cdTela == CDTELA_PLANEJAR_METAS) {
			if (LavenderePdaConfig.usaConfigCadastroMetasPlataformaVenda()) {
				show(new ListPlanejarVendaMetaForm());
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PLANEJAMENTOMETAVENDA_MSG_PARAMETRO_NAO_CONFIGURADO, new String[] {
						StringUtil.getStringValueToInterface(ValorParametro.CONFIGCADASTROMETASPLATAFORMAVENDA),
						Messages.PLANEJAMENTOMETAVENDA_DSPARAMETRO
				}));
			}
		} else if (menu.cdTela == CDTELA_CLIENTE_CHURN) {
			acessaTelaRiscoChurn();
		} else if (menu.cdTela == CDTELA_LEADS) {
			show(new ListListaLeadsForm());
		} else if (menu.cdTela == CDTELA_SOLICITACAO_AUTORIZACAO) {
			show(new ListSolAutorizacaoForm(null, null, true));
		} else if(menu.cdTela == CDTELA_TREINAMENTO) {
			show(new ListTreinamentoForm());
		}
	}

	private static void iniciaPedidoSemCliente() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			Cliente clienteDefault = ClienteService.getInstance().getClienteDefault(Cliente.CD_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO, Cliente.NM_CLIENTE_DEFAULT);
			SessionLavenderePda.setCliente(clienteDefault);
			CadClienteMenuForm cadClienteMenuForm = new CadClienteMenuForm();
			cadClienteMenuForm.btNovoPedidoClick(true);
		} finally {
			mb.unpop();
		}
	}

	private void showListProduto() throws SQLException {
		if (LavenderePdaConfig.isUsaConfigMenuCatalogo()) {
			try {
				MenuCatalogo entidadePrimeiroNivel = MenuCatalogoService.getInstance().findEntidadePrimeiroNivel(Produto.class.getSimpleName());
				if (entidadePrimeiroNivel != null) {
					MenuCatalogoForm menuCatalogoForm = new MenuCatalogoForm(entidadePrimeiroNivel, null);
					show(menuCatalogoForm);
					return;
				}
			} catch (Exception e) {
				ExceptionUtil.handle(e);
			}
		}
		show(new ListProdutoForm());
	}

	private void acessaTelaRiscoChurn() throws SQLException {
		if (LavenderePdaConfig.usaConfigModuloRiscoChurn()) {
			show(new ListClienteChurnForm());
			return;
		}
		UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.CLIENTECHURN_MSG_PARAMETRO_NAO_CONFIGURADO, new String[] {
				StringUtil.getStringValueToInterface(ValorParametro.CONFIGMODULORISCOCHURN),
				Messages.CLIENTECHURN_DSPARAMETRO
		}));
	}

	@Override
	protected void showRelDinamico(Menu menu) throws SQLException {
		show(new ListRelDinamicoLavendereForm(menu.getTela()));
	}

	private boolean controlaConexaoObrigatoria() throws SQLException {
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			if (!SessionLavenderePda.autorizadoPorSenhaNovoPedidoSemEnvioDados) {
				int resp = ConexaoPdaService.getInstance().isEnvioPedidosNecessario();
				if (resp != 0) {
					if (resp != 2 || ConexaoPdaService.getInstance().isNecessarioSolicitarEnviosPedidos()) {
						MainLavenderePda.getInstance().showEnvioDadosObrigatorio(resp);
					}
				}
			}
			int resp = ConexaoPdaService.getInstance().isRecebimentoDadosNecessario();
			if (resp != 0) {
				if (!MainLavenderePda.getInstance().showReceberDadosObrigatorio(resp)) {
					return false;
				}
			}
			return true;
		}
		return true;
	}

	public void btSyncClick() throws SQLException {
		MainLavenderePda.getInstance().showSincronizacaoForm();
	}
	
	private void showAvisoColetaGpsParada() throws SQLException {
		if (UiUtil.showConfirmYesNoMessage(Messages.COLETAGPS_MSG_AVISO_COLETA_GPS_PARADA)) {
			startStopColetaGps();
		}
	}

	public static boolean isTelaAutorizada(int cdMenu) throws SQLException {
		UsuarioSistema usuarioSistema = SessionLavenderePda.usuarioPdaRep.usuario.getUsuarioSistema();
		if (usuarioSistema != null) {
			Menu menu = new Menu();
			menu.cdTela = cdMenu;
			menu.cdSistema = usuarioSistema.cdSistema;
			String cdFuncaoUsuario = UsuarioLavendereService.getInstance().findColumnByRowKey(SessionLavenderePda.usuarioPdaRep.usuario.getRowKey(), "cdFuncao");
			Vector funcaoPapelList = FuncaoPapelLavendereService.getInstance().getPapelByFuncao(LavendereConfig.CDSISTEMALAVENDEREPDA, cdFuncaoUsuario);
			return AutorizacaoLavendereService.getInstance().isTelaAutorizada(funcaoPapelList, menu);
		}
		return false;
	}
	
	public void refreshLbEmpresaRepresentante() throws SQLException {
		userContainer.setNmEmpresa(EmpresaService.getInstance().getEmpresaName(SessionLavenderePda.cdEmpresa));
		userContainer.setNmRep(SessionLavenderePda.getRepresentante().toString());
	}

}
