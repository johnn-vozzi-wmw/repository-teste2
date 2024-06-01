
package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.List;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.domain.Usuario;
import br.com.wmw.framework.business.service.MensagemExcecService;
import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.config.CommandLineConfig;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.FrameworkMessages_en_US;
import br.com.wmw.framework.config.FrameworkMessages_es_ES;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.UsuarioBloqueadoException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseMainWindow;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.HoraSelect;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.presentation.ui.ext.tmp.PopupMenuTc;
import br.com.wmw.framework.presentation.ui.keyboard.BaseKeyboard;
import br.com.wmw.framework.sync.HttpConnection;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.WtoolsUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.Messages_en_US;
import br.com.wmw.lavenderepda.Messages_es_ES;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.ClienteRedeLiberadoComSenhaConfig;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.AcessoContato;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ColetaGps;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.ConfigNotificacao;
import br.com.wmw.lavenderepda.business.domain.CorSistemaLavendere;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.Notificacao;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.UsuarioPdaRep;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.AcessoContatoService;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaCliHistService;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.AtividadePedidoService;
import br.com.wmw.lavenderepda.business.service.CampoLavendereService;
import br.com.wmw.lavenderepda.business.service.CancelamentoPedidoAutomaticoService;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsPontosEspecificosService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsTimerService;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ConfigNotificacaoService;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.DadosTc2WebService;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.business.service.DocumentoAnexoService;
import br.com.wmw.lavenderepda.business.service.EquipamentoUsuarioLavendereService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FotoClienteErpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoEmpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import br.com.wmw.lavenderepda.business.service.LavendereBackupService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.ManutencaoService;
import br.com.wmw.lavenderepda.business.service.MensagemExcecLavendereService;
import br.com.wmw.lavenderepda.business.service.NotificacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PersonalizacaoLavendereService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.business.service.ProducaoProdService;
import br.com.wmw.lavenderepda.business.service.ProdutoDesejadoService;
import br.com.wmw.lavenderepda.business.service.RecadoService;
import br.com.wmw.lavenderepda.business.service.RegistroLoginLavendereService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.SacService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.UsuarioLavendereService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.business.service.VisitaPedidoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.business.validation.CancelamentoPedidoAutoException;
import br.com.wmw.lavenderepda.business.validation.DataHoraServidorException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.ConexaoPdaPdbxDao;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.sync.async.AsyncManutencaoDatabase;
import br.com.wmw.lavenderepda.thread.ServicoNotificacaoThread;
import br.com.wmw.lavenderepda.util.AppConfFilesUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.UiMessagesUtil;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.ui.Insets;
import totalcross.ui.Label;
import totalcross.ui.MainWindow;
import totalcross.ui.PopupMenu;
import totalcross.ui.Window;
import totalcross.ui.dialog.CalendarBox;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class MainLavenderePda extends BaseMainWindow {

	private int yPenEventStart;
	private boolean inSyncForm;
	private boolean canShowMenu = true;
	public MenuSuperior menuSuperior;
	public boolean necessarioAcoesAfterInitSistema;
	
	static {
		Settings.applicationId = "WLVP";
		Settings.companyInfo = "WMW Systems";
		Settings.appDescription = "LavenderePDA TC";
		Settings.useNewFont = true;
		Settings.appVersion = "9.000";
	}
	
	public MainLavenderePda() {
		this("", Window.NO_BORDER);
	}

	public MainLavenderePda(String nmSistema, final byte style) {
		super(nmSistema, style);
		setTitle(getNmSistema());
		VmUtil.executeGarbageCollector();
	}
	
	public static MainLavenderePda getInstance() {
		return (MainLavenderePda) MainWindow.getMainWindow();
	}

	public String getTablesId() {
		return getNmSistema();
	}

	public void setMenuVisibility(boolean visible) {
		canShowMenu = visible;
	}
	
	public void inicializaSistema() throws SQLException {
		//-- Temporariamente movido do construtor para cá, devido a bug no totalcross 1.67 de sempre ativar a VM. A partir da VM 1.68 pode devolver codigo ao construtor
		UiUtil.showStartScreen();
		try {
			try {
				String nomeSistemaPersonalizado = getNmSistema();
				if (ValueUtil.isNotEmpty(nomeSistemaPersonalizado)) {
					setDeviceTitle(nomeSistemaPersonalizado);
				}
				removeFakeGpsFile();
				AsyncManutencaoDatabase.replaceByRecoveredDb();
				moveTodasTabelasMemoriaToCartaoSeNecessario();
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				UiUtil.showErrorMessage(e);
			}
			if (!inicializaTabelasSistema()) {
				simpleExit();
				return;
			}
			validateDatabase();
			loadMensagemExcecSistema();
		} finally {
			Vm.tweak(Vm.TWEAK_DUMP_MEM_STATS, true);
		}
	}
	
	private void validateDatabase() {
		try {
			CrudDbxDao.getCurrentDriver().con();
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			showDbMaintenenceWindow(e.getMessage());
		}
	}

	private void showDbMaintenenceWindow(final String msg) {
		try {
			new AdmFatalWindow(msg).popup();
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	protected void loadModoManutencao() {
		try {
			String dsConexao = "Padrão Manutenção WMW";
			StringBuilder strUpdate = new StringBuilder();
			strUpdate.append("UPDATE ").append(ConexaoPda.TABLE_NAME).append(" SET DSURLWEBSERVICE = '");
			strUpdate.append(Session.dsConexaoManutencaoPadrao);
			strUpdate.append("', DSCONEXAO='").append(dsConexao).append("';");
			ConexaoPdaPdbxDao.getInstance().updateAllConections(strUpdate.toString());
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	private void removeFakeGpsFile() {
		if (VmUtil.isAndroid()) {
			try {
				FileUtil.deleteFile(WtoolsUtil.getPathMockGps());
			} catch (Throwable ex) {
				ExceptionUtil.handle(ex);
			}
		}
	}

	private void populateMessagesEConfiguraIdioma() throws SQLException {
		try {
			if (LavenderePdaConfig.isSistemaIdiomaIngles()) {
				Settings.dateFormat = Settings.DATE_MDY;
				Messages_en_US.setMessages();
				FrameworkMessages_en_US.setMessages();
				MensagemExcecService.getInstance().loadMensagemExcecSistema();
				PopupMenu.cancelString = FrameworkMessages.BOTAO_FECHAR;
				PopupMenuTc.cancelString = FrameworkMessages.BOTAO_FECHAR;
				CalendarBox.weekNames = FrameworkMessages.DIAS_SEMANA;
				CalendarBox.yearMonth = FrameworkMessages.ANO_MES;
				Date.monthNames = FrameworkMessages.MESES_ANO;
				Settings.decimalSeparator = FrameworkMessages.SEPARADOR_DECIMAL;
				Settings.thousandsSeparator = ',';
			} else if (LavenderePdaConfig.isSistemaIdiomaEspanhol()) {
				Messages_es_ES.setMessages();
				FrameworkMessages_es_ES.setMessages();
				MensagemExcecService.getInstance().loadMensagemExcecSistema();
				PopupMenu.cancelString = FrameworkMessages.BOTAO_FECHAR;
				PopupMenuTc.cancelString = FrameworkMessages.BOTAO_FECHAR;
				CalendarBox.weekNames = FrameworkMessages.DIAS_SEMANA;
				CalendarBox.yearMonth = FrameworkMessages.ANO_MES;
				Date.monthNames = FrameworkMessages.MESES_ANO;
				Settings.decimalSeparator = FrameworkMessages.SEPARADOR_DECIMAL;
			}
		} catch (Throwable ex) {
			UiUtil.showWarnMessage(Messages.MSG_IDIOMA_NAO_CARREGADO + ex.getMessage());
		}
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			populateMessagesEConfiguraIdioma();
			AdmLoginForm loginForm = new AdmLoginForm();
			if (ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USALOGINAUTOMATICOSISTEMA)) && ((loginForm.getEmpSize() == 1)) && (loginForm.getRepSize() == 1)) {
				loginForm.usuarioPdaRep = UsuarioLavendereService.getInstance().getUsuarioPdaRepByCdRep(loginForm.getSelectedCdRep());
				loginForm.onLoginSucess();
			} else {
				UiUtil.closeStartScreen();
				loginForm.popup();
			}
			if (loginForm.result == AdmLoginForm.LOGIN_RESULT_OK) {
				UiUtil.showStartScreen();
				try {
					loadSessao(loginForm.usuarioPdaRep);
					AppConfFilesUtil.manageUserConfFile();
					validaDataHoraServidorEliberaSenha();
					controlaNoMediaFile();
					LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_SESSAO, MessageUtil.getMessage(Messages.LOGPDA_MSG_ACESSO_SISTEMA, loginForm.usuarioPdaRep.cdUsuario));
					LogPdaService.getInstance().logMemoria(Messages.LOGPDA_MSG_MEMORIA_ACESSO_SISTEMA);
					if (LavenderePdaConfig.usaRegistroLogin) {
						RegistroLoginLavendereService.getInstance().geraRegistroLogin();
					}
					execAdmDeleteDadosAntigos();
					gerenciaQtdExecucoes();
					AdmControlUpdateIosUiUtil.verificaExpiracaoCertificadoIos();
					if (!showCadAcesso()) {
						simpleExit();
					}
					showMenu();
					if (permiteAcessoNormalSistema()) {
						afterShowMenu();
					}
					ServicoNotificacaoThread.asyncPoolExecute();
				} catch (Throwable e) {
					UiUtil.showErrorMessage(e);
					simpleExit();
					return;
				} finally {
					try {
						if (getMainForm() != null) {
							getMainForm().onFormExibition();
						}
					} catch (Throwable e) {
						ExceptionUtil.handle(e);
					}
					UiUtil.closeStartScreen();
				}
			} else if (loginForm.result == AdmLoginForm.LOGIN_RESULT_CANCEL) {
				simpleExit();
				return;
			}
			menuSuperior = new MenuSuperior();
			menuSuperior.setVisible(false);
		} catch (Throwable ee) {
			UiUtil.showErrorMessage(ee);
			MainWindow.exit(0);
		}
	}

	protected void afterShowMenu() throws SQLException {
		loadkeyboardType();
		validaDataExpiracaoSenha();
		validaDiasRestantesParaExpiracaoSenha();
		verificaCargaCompletaZerada();
		salvaDataPrimeiroAcesso();
		loadAcoesPrimeiroAcessoAoSistemaNoDia();
		validaPrimeiroAcessoClienteSemMotivoChurn();
		showNotificacaoIfHasNotoficacaoNaoLidos();
		showListRecadosIfHasRecadosNaoLidos();
		showListAniversariantes();
		carregaRepUltimaSessao();
		if (verificaPedidosSemNota()) {
			PedidoService.getInstance().reprocessaPedidoFechadoSemNfe();
		}
		this.necessarioAcoesAfterInitSistema = false;
	}

	private void loadkeyboardType() throws SQLException {
		boolean naoExibeMensagemTecladoNativo = ConfigInternoService.getInstance().isNaoMostraMensagemTecladoNativo();
		if (LavenderePdaConfig.isTecladoWMW() || naoExibeMensagemTecladoNativo) {
			return;
		}
		AdmTecladoWindow admTecladoWindow = new AdmTecladoWindow();
		admTecladoWindow.telaInicial = true;
		admTecladoWindow.popup();
	}

	private boolean verificaPedidosSemNota() {
		return (LavenderePdaConfig.isUsaGeracaoNotaNfeContingenciaPedidoSemConexao()
				|| LavenderePdaConfig.isUsaSomenteGeracaoNotaNfeContingenciaPedido()
				|| LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento())
				&& !LavenderePdaConfig.usaNfePorReferencia && !LavenderePdaConfig.isConfigGradeProduto();
	}

	private void deletaVisitaSemPedidoECliente() throws SQLException {
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() && LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			Visita visitaFilter = new Visita();
			visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			visitaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			visitaFilter.cdCliente = Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO;
			Vector visitaList = VisitaService.getInstance().findAllByExample(visitaFilter);
			if (!visitaList.isEmpty()) {
				for (int i = 0; i < visitaList.size(); i++) {
					Visita visita = (Visita) visitaList.items[i];
					if (!VisitaPedidoService.getInstance().isVisitaPossuiPedido(visita)) {
						VisitaService.getInstance().delete(visita);
					}
				}
			}
		}
	}

	private void salvaDataPrimeiroAcesso() throws SQLException {
		String configInterno = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataPrimeiroAcessoAoSistema);
		if (configInterno == null) {
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataPrimeiroAcessoAoSistema, DateUtil.getCurrentDateYYYYMMDD());
		}
	}

	@Override
	public MensagemExcecService getMensagemExcecService() {
		return MensagemExcecLavendereService.getInstance();
	}

	public void configuraCoresSistema() throws SQLException {
		setPersonColors();
		setBackForeColors(LavendereColorUtil.formsBackColor, LavendereColorUtil.componentsForeColor);
	}
	
	@Override
	protected TemaSistema getTemaDefault() throws SQLException {
		return TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
	}
	
	@Override
	public void configuraImagemPersonalizacaoSistema() {
		try {
			loadPersonalizacao();
		} catch (Exception e) {
			UiUtil.showErrorMessage(e);
		}
	}

	private void setPersonColors() throws SQLException {
		try {
		int cdEsquemaCor = TemaSistema.CD_ESQUEMA_COR_PADRAO;
		TemaSistema tema = getTemaDefault();
		if (tema != null) {
			cdEsquemaCor = tema.cdEsquemaCor;
		}
		Vector cores = CorSistemaLavendereService.getInstance().getCoresEsquema(cdEsquemaCor);
		int size = cores.size();
		CorSistema corSistema;
		ColorUtil.restartDefaultColors();
		for (int i = 0; i < size; i++) {
			corSistema = (CorSistema) cores.items[i];
			switch (corSistema.cdCor) {
			case CorSistema.BASEFORECOLORSYSTEM: 
				LavendereColorUtil.baseForeColorSystem = corSistema.getCor(); break;
			case CorSistema.BASEBACKCOLORSYSTEM: 
				LavendereColorUtil.baseBackColorSystem = corSistema.getCor(); break;
			case CorSistema.BARSBACKCOLOR: 
				LavendereColorUtil.secondaryColorSystem = corSistema.getCor(); break;
			case CorSistema.POPUPHEADERCOLOR: 
				LavendereColorUtil.popupHeaderColor = corSistema.getCor(); break;
			case CorSistema.MESSAGEBOXHEADERCOLOR: 
				LavendereColorUtil.secondarySessionColor = corSistema.getCor(); break;
			case CorSistema.COMPONENTSBACKCOLOR: 
				LavendereColorUtil.componentsBackColor = corSistema.getCor(); break;
			case CorSistema.COMPONENTSFORECOLOR: 
				LavendereColorUtil.componentsForeColor = corSistema.getCor(); break;
			case CorSistema.BUTTONEXCLUIRFORECOLOR: 
				LavendereColorUtil.buttonExcluirForeColor = corSistema.getCor(); break;
			case CorSistema.SESSIONCONTAINERBACKCOLOR: 
				LavendereColorUtil.sessionContainerBackColor = corSistema.getCor(); break;
			case CorSistema.SESSIONCONTAINERFORECOLOR: 
				LavendereColorUtil.sessionContainerForeColor = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_NA_PROMOCAO_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PRODUTO_DESC_PROMOCIONAL_BACK: 
				LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PRODUTO_EM_OPORTUNIDADE: 
				LavendereColorUtil.COR_PRODUTO_EM_OPORTUNIDADE = corSistema.getCor(); break;
			case CorSistemaLavendere.CLIENTE_ATRASADO_OU_BLOQUEADO_FUNDO: 
				LavendereColorUtil.COR_CLIENTE_ATRASADO_BLOQUEADO_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.CLIENTE_BLOQUEADO_FUNDO: 
				LavendereColorUtil.COR_CLIENTE_BLOQUEADO_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_SEM_ESTOQUE_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PRODUTO_PENDENTE_RETIRADA:
				LavendereColorUtil.COR_PRODUTO_PENDENTE_RETIRADA = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_EXTRAPOLADO: 
				LavendereColorUtil.COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_EXTRAPOLADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_NAO_EXTRAPOLADO: 
				LavendereColorUtil.COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_NAO_EXTRAPOLADO = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_SEM_ESTOQUE_GRADE_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_GRADE_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_INSERIDO_PEDIDO_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_INSERIDO_PEDIDO_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_DE_KIT_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_COMKIT_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_BONIFICACAO_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_BONIFICACAO_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_AVISO_PRE_ALTA_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_COM_AVISO_PRE_ALTA = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_VERBA_APOS_APLICAR_DESC_PROG_FUNDO: 
				LavendereColorUtil.COR_PRODUTO_COM_VERBA_APOS_APLICAR_DESC_PROG = corSistema.getCor(); break;
			case CorSistemaLavendere.CLIENTE_NU_DIAS_SEM_PEDIDO_EXTRAPOLADO_FUNDO: 
				LavendereColorUtil.COR_CLIENTE_NU_DIAS_SEM_PEDIDO_EXTRAPOLADO_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.PRODUTO_ESTOQUE_MINIMO: 
				LavendereColorUtil.COR_PRODUTO_ESTOQUE_MINIMO = corSistema.getCor(); break;
			case CorSistemaLavendere.ITEMPEDIDO_VERBA_MANUAL: 
				LavendereColorUtil.COR_ITEMPEDIDO_VERBA_MANUAL_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.ITEMPEDIDO_RENTABILIDADE_BAIXA:
				LavendereColorUtil.COR_ITEMPEDIDO_BAIXA_RENTABILIDADE_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.ITEMPEDIDO_RENTABILIDADE_ALTA: 
				LavendereColorUtil.COR_ITEMPEDIDO_ALTA_RENTABILIDADE_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_ITEMPEDIDO_COM_QTMAXVENDA_BACK: 
				LavendereColorUtil.COR_ITEMPEDIDO_COM_QTMAXVENDA_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PEDIDOS_RECENTES_ABERTOSFECHADOS: 
				LavendereColorUtil.COR_PEDIDOS_RECENTES_ABERTOSFECHADOS = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PEDIDOS_RECENTES_NAOABERTOSEFECHADOS: 
				LavendereColorUtil.COR_PEDIDOS_RECENTES_NAOABERTOSEFECHADOS = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CLIENTE_JA_ATENDIDO: 
				LavendereColorUtil.COR_CLIENTE_ATENDIDO_MES = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PRODUTO_COM_PRECO_EM_QUEDA:
				LavendereColorUtil.COR_PRODUTO_COM_PRECO_EM_QUEDA = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CARGA_PEDIDO_ABAIXO_PESO_MIN:
				LavendereColorUtil.COR_CARGA_PEDIDO_ABAIXO_PESO_MIN = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CARGA_PEDIDO_ACIMA_PESO_MIN:
				LavendereColorUtil.COR_CARGA_PEDIDO_ACIMA_PESO_MIN = corSistema.getCor(); break;
			case CorSistemaLavendere.ITEMPEDIDO_RENTABILIDADE_DENTRO_TOLERANCIA:
				LavendereColorUtil.COR_ITEMPEDIDO_RENTABILIDADE_DENTRO_TOLERANCIA = corSistema.getCor(); break;
			case CorSistemaLavendere.CLIENTE_VISITA_ANDAMENTO:
				LavendereColorUtil.COR_CLIENTE_VISITA_ANDAMENTO = corSistema.getCor(); break;
			case CorSistemaLavendere.CLIENTE_PROSPECT: 
				LavendereColorUtil.COR_CLIENTE_PROSPECTS = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FONTE_QUE_CONTEM_LOTE_PERCENTUAL_DE_VIDA_CRITICO: 
				LavendereColorUtil.COR_FONTE_QUE_CONTEM_LOTE_PERCENTUAL_DE_VIDA_CRITICO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_ITEMPEDIDO_PROBLEMA_RESERVA_ESTOQUE:
				LavendereColorUtil.COR_ITEMPEDIDO_PROBLEMA_RESERVA_ESTOQUE = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CLIENTE_STATUS_BLOQUEIO_POR_ATRASO_FUNDO: 
				LavendereColorUtil.COR_CLIENTE_STATUS_BLOQUEIO_POR_ATRASO_FUNDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CLIENTE_ATRASADO_GRID_FUNDO:
				LavendereColorUtil.COR_CLIENTE_ATRASADO_GRID_FUNDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PEDIDOS_DISP_LIBERACAO_GRID_FUNDO:
				LavendereColorUtil.COR_PEDIDOS_DISP_LIBERACAO_GRID_FUNDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PRODUTO_VENDIDO_MES_CORRENTE:
				LavendereColorUtil.COR_PRODUTO_VENDIDO_MES_CORRENTE = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_ITEMPEDIDO_PENDENTE_GRID_FUNDO: 
				LavendereColorUtil.COR_ITEMPEDIDO_PENDENTE_GRID_FUNDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PEDIDO_PENDENTE_ITENS_PENDENTES:
				LavendereColorUtil.COR_PEDIDO_PENDENTE_ITENS_PENDENTES = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PRODUTO_RESTRITO: 
				LavendereColorUtil.COR_PRODUTO_RESTRITO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PEDIDO_CONSIGNACAO_DEVOLVIDO:
				LavendereColorUtil.COR_PEDIDO_CONSIGNACAO_VENCIDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_TRANSPORTADORA_TIPOFRETE_CIF: 
				LavendereColorUtil.COR_TRANSPORTADORA_TIPOFRETE_CIF = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_TRANSPORTADORA_TIPOFRETE_CIF_CONTRATO: 
				LavendereColorUtil.COR_TRANSPORTADORA_TIPOFRETE_CIF_CONTRATO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FAIXA_DESCONTO_VOLUME_VENDA_MENSAL:
				LavendereColorUtil.COR_FAIXA_DESCONTO_VOLUME_VENDA_MENSAL = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FONTE_PRODUTO_LOTE_VIDA_UTIL_CRITICA:
				LavendereColorUtil.COR_FONTE_PRODUTO_LOTE_VIDA_UTIL_CRITICA = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_CNPJ_DUPLICADO:
				LavendereColorUtil.COR_FUNDO_CNPJ_DUPLICADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FONTE_PRODUTO_JA_RATEADO:
				LavendereColorUtil.COR_FONTE_PRODUTO_JA_RATEADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_LABEL_CNPJ_DUPLICADO:
				LavendereColorUtil.COR_LABEL_CNPJ_DUPLICADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ERRO_CAD_CIDADEUF:
				LavendereColorUtil.COR_FUNDO_ERRO_CAD_CIDADEUF = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_LABEL_ERRO_CAD_CIDADEUF:
				LavendereColorUtil.COR_LABEL_ERRO_CAD_CIDADEUF = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_PRODUTO_INSERIDO_SUGVENDA:
				LavendereColorUtil.COR_FUNDO_PRODUTO_INSERIDO_SUGVENDA = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_LINHA_LISTA_PEDIDOS_DIFERENCA:
				LavendereColorUtil.COR_FUNDO_LINHA_LISTA_PEDIDOS_DIFERENCA = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_AGENDA_POSITIVADO:
				LavendereColorUtil.COR_FUNDO_AGENDA_POSITIVADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_AGENDA_NAO_POSITIVADO:
				LavendereColorUtil.COR_FUNDO_AGENDA_NAO_POSITIVADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_DESC_MAX_ULTRAPASSADO:
				LavendereColorUtil.COR_FUNDO_ITEM_DESC_MAX_ULTRAPASSADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_LISTA_FAIXA_DESC_QUANTIDADE_ATINGIDO:
				LavendereColorUtil.COR_FUNDO_LISTA_FAIXA_DESC_QUANTIDADE_ATINGIDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_ERRO_RECALCULO_VALORES:
				LavendereColorUtil.COR_FUNDO_ITEM_ERRO_RECALCULO_VALORES = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_LISTA_CLIENTE_ATRASO:
				LavendereColorUtil.COR_FUNDO_LISTA_CLIENTE_ATRASO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_LISTA_CLIENTE_SEM_LIMITE_CREDITO:
				LavendereColorUtil.COR_FUNDO_LISTA_CLIENTE_SEM_LIMITE_CREDITO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_CELULA_GRADE_SEM_PRECO:
				LavendereColorUtil.COR_FUNDO_CELULA_GRADE_SEM_PRECO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_LISTA_PRODUTOS_FACEAMENTO:
				LavendereColorUtil.COR_FUNDO_LISTA_PRODUTOS_FACEAMENTO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_GRID_LOTE_PRODUTO_VINCULADO_TABELA_PRECO:
				LavendereColorUtil.COR_FUNDO_GRID_LOTE_PRODUTO_VINCULADO_TABELA_PRECO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_GRID_PRODUTO_SUGESTAO_VENDA:
				LavendereColorUtil.COR_GRID_PRODUTO_SEM_PRECO_SUGESTAO_VENDA = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CAPA_VALOR_PONTUACAO_POSITIVO:
				LavendereColorUtil.COR_CAPA_VALOR_PONTUACAO_POSITIVO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_CAPA_VALOR_PONTUACAO_NEGATIVO:
				LavendereColorUtil.COR_CAPA_VALOR_PONTUACAO_NEGATIVO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_EXTRATO_VALOR_PONTUACAO_POSITIVO:
				LavendereColorUtil.COR_EXTRATO_VALOR_PONTUACAO_POSITIVO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_EXTRATO_VALOR_PONTUACAO_NEGATIVO:
				LavendereColorUtil.COR_EXTRATO_VALOR_PONTUACAO_NEGATIVO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_LISTA_ITEM_PEDIDO_PROMOCIONAL:
				LavendereColorUtil.COR_FUNDO_LISTA_ITEM_PEDIDO_PROMOCIONAL = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_SEM_QT_ITEM_FISICO_GONDOLA:
				LavendereColorUtil.COR_FUNDO_ITEM_SEM_QT_ITEM_FISICO_GONDOLA = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PESQUISA_MERCADO_PRODUTO_VALOR_PREENCHIDO:
				LavendereColorUtil.COR_PESQUISA_MERCADO_PRODUTO_VALOR_PREENCHIDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_PRODUTO_RESTRITO:
				LavendereColorUtil.COR_FUNDO_ITEM_PRODUTO_RESTRITO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_PEDIDO_COMBO:
				LavendereColorUtil.COR_FUNDO_ITEM_PEDIDO_COMBO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_COMBO_ESTOQUE:
				LavendereColorUtil.COR_FUNDO_ITEM_COMBO_ESTOQUE = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_PEDIDO_NAO_AUTORIZADO:
				LavendereColorUtil.COR_FUNDO_PEDIDO_NAO_AUTORIZADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_PEDIDO_NAO_AUTORIZADO:
				LavendereColorUtil.COR_FUNDO_ITEM_PEDIDO_NAO_AUTORIZADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_KIT_SEM_ESTOQUE:
				LavendereColorUtil.COR_FUNDO_KIT_SEM_ESTOQUE = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_PRODUTO_INSERIDO_DESC_PROGRESSIVO_BACK:
				LavendereColorUtil.COR_PRODUTO_INSERIDO_DESC_PROGRESSIVO_BACK = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_PRODUTO_AUTORIZADO_OU_DISTRIBUIDO:
				LavendereColorUtil.COR_FUNDO_ITEM_PEDIDO_AUTORIZADO_OU_DISTRIBUIDO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_GRID_BONIFCFG_OBRIGATORIA_SALDO_PENDENTE: 
				LavendereColorUtil.COR_FUNDO_GRID_BONIFCFG_OBRIGATORIA_SALDO_PENDENTE = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEM_SEM_MULTIPLO:
				LavendereColorUtil.COR_FUNDO_ITEM_SEM_MULTIPLO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE: 
				LavendereColorUtil.COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE_OBRIGATORIO: 
				LavendereColorUtil.COR_FUNDO_GRID_ITEMBONIFCFG_BRINDE_OBRIGATORIO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_GRID_PRODUTO_BLOQUEADO: 
				LavendereColorUtil.COR_FUNDO_GRID_PRODUTO_BLOQUEADO = corSistema.getCor(); break;
			case CorSistemaLavendere.COR_FUNDO_ITEMKIT_BONIFICADO:
				LavendereColorUtil.COR_FUNDO_ITEMKIT_BONIFICADO = corSistema.getCor(); break;
			}
		}
		} catch (Exception e) {
			VmUtil.debug("Erro ao carregar o tema do banco de dados.");
		}
		ColorUtil.reloadColors();
		LoadingBoxWindow.clearLoadImage();
	}


	protected boolean inicializaTabelasSistema() throws SQLException {
		atualizaEstruturaTabelas();
		return true;
	}

	private void atualizaEstruturaTabelas() throws SQLException {
		if (CommandLineConfig.updateTables(appArgs)) {
			CrudDbxDao.getCurrentDriver();
			ManutencaoService.getInstance().atualizaEstruturaTabelas();
		}
	}
	
	public void setInSyncForm(boolean in) {
		inSyncForm = in;
	}

	public boolean isInSyncForm() {
		return inSyncForm;
	}
	

	private void moveTodasTabelasMemoriaToCartaoSeNecessario() throws IOException {
		FileUtil.createDirIfNecessaryQuietly(Convert.appendPath(VmUtil.isSimulador() ? Settings.appPath : Settings.dataPath, DatabaseUtil.PATH_APPCONFG));
		if (VmUtil.isAndroid() || VmUtil.isIOS()) {
				FileUtil.createDirIfNecessary(Produto.getPathImg());
				FileUtil.createDirIfNecessary(Cliente.getPathImg());
				FileUtil.createDirIfNecessary(DivulgaInfo.getPathImg());
		}
		if (VmUtil.isAndroid()) {
			boolean reset = isCargaNova();
			if (!reset) {
				VmUtil.debug("Data do build não é mais novo que o atual. As tabelas não serão atualizadas.");
				return;
			}
			copiaDataBuildCarga();
			boolean existNewDb = existsDatabaseInAppPath();
			if (existNewDb) {
						VmUtil.debug("======== fazendo nova carga ========");
			} else {
				VmUtil.debug("Não há banco de dados no caminho interno do aplicativo. As tabelas não serão atualizadas.");
			}
			try (File f = FileUtil.openFile(Settings.dataPath, File.DONT_OPEN)) {
						String[] filesToDelete = f.listFiles();
						if (filesToDelete != null) {
							for (int i = 0; i < filesToDelete.length; i++) {
						String fileName = filesToDelete[i];
						if (!fileName.endsWith(".conf") && (existNewDb || !fileName.endsWith(AppConfig.DATABASE_NAME))) {
									FileUtil.deleteFile(Settings.dataPath + fileName);
								}
							}
						}
								}
			if (existNewDb) {
				try (File db = new File(Convert.appendPath(Settings.appPath, AppConfig.DATABASE_NAME), File.DONT_OPEN)) {
					FileUtil.renameFile(db.getPath(), Convert.appendPath(Settings.dataPath, AppConfig.DATABASE_NAME));
							}
						}
					}
				}

	private void showListRecadosIfHasRecadosNaoLidos() throws SQLException {
		if (LavenderePdaConfig.usaMuralDeRecados) {
			if (!SessionLavenderePda.isUsuarioSupervisor()) {
				if (RecadoService.getInstance().existeRecadosNaoLidos()) {
					show(new ListRecadoForm());
				}
			}
		}
	}

	private void showNotificacaoIfHasNotoficacaoNaoLidos() {
		ListNotificacaoForm.showNotificacaoIfHasNotificacaoNaoLidos();
	}

	private void showListAniversariantes() throws SQLException {
		if (LavenderePdaConfig.mostraRelAniversariantesAposLogin) {
			if (!SessionLavenderePda.isUsuarioSupervisor()) {
				ListAniversariantesForm listAniversariantesForm = new ListAniversariantesForm();
				show(listAniversariantesForm);
				if (listAniversariantesForm.size() == 0) {
					listAniversariantesForm.close();
				}
			}
		}
	}
	
	private void deleteLiberacoesSenhaAntigas() throws SQLException {
		if (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica) {
			ConfigInternoService.getInstance().deleteLiberacoesSenhaAntigas();
		}
	}
	
	private void verificaDataHoraAgendaVisita() throws SQLException {
		if (LavenderePdaConfig.permiteReagendamentoAgendaParaDataIgualOriginal) {
			ConfigInterno configInternoFilter = new ConfigInterno();
			configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			configInternoFilter.cdConfigInterno = ConfigInterno.salvaDataHoraOriginalAgendaVisita;
			Vector configInternoList =  ConfigInternoService.getInstance().findAllByExample(configInternoFilter);
			for (int i = 0; i < configInternoList.size(); i++) {
				ConfigInterno configInterno = (ConfigInterno) configInternoList.items[i];
				String vlConfigInterno = StringUtil.getStringValue(configInterno.vlConfigInterno);
				String dtAgenda = vlConfigInterno.substring(0, 10);
				if (DateUtil.getDateValue(dtAgenda).isBefore(DateUtil.getCurrentDate())) {
					AgendaVisita agendaVisita = (AgendaVisita) AgendaVisitaService.getInstance().findByRowKey(configInterno.vlChave);
					if (agendaVisita != null) {
						String horaAgenda = vlConfigInterno.replaceAll(dtAgenda, "");
						agendaVisita.hrAgenda = horaAgenda;
						AgendaVisitaService.getInstance().update(agendaVisita);
						ConfigInternoService.getInstance().delete(configInterno);
					}
				}
			}
		}
	}

	private void showPainelGerenciamentoVendas() throws SQLException {
		if (LavenderePdaConfig.usaPainelGerenciamentoVendas) {
			show(new ListMetaAcompanhamentoForm());
		}
	}
	
	private void showListaVerbaGrupoSaldoAvisoVigencia() throws SQLException {
		if (LavenderePdaConfig.nuDiasRestantesAvisoSaldoVerbaGrupo > 0 && LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			RelVerbaGrupoSaldoAvisoVigenciaWindow relVerbaGrupoSaldoAvisoVigenciaWindow = new RelVerbaGrupoSaldoAvisoVigenciaWindow();
			if (relVerbaGrupoSaldoAvisoVigenciaWindow.hasInfoToShow()) {
				relVerbaGrupoSaldoAvisoVigenciaWindow.popup();
			}
		}
	}
	
	private void limpaVerbaSaldoForaVigencia() throws SQLException {
		if (LavenderePdaConfig.usaProvisionamentoConsumoVerbaSaldo()) {
			VerbaSaldoService.getInstance().limpaVerbaSaldoPdaForaVigencia();
			VerbaSaldoService.getInstance().recalculateAndUpdateVerbaSaldoPda();
		}
	}
	
	private void showListSac() throws SQLException {
		if (LavenderePdaConfig.isExibeRelatorioNovosSacsLogin() && SacService.getInstance().hasSacExibirRelatorio()) {
			ListNovoSacWindow listNovoSacWindow = new ListNovoSacWindow();
			listNovoSacWindow.popup();
		}
	}
	
	private void showListVencimentoPedidoConsignacao() {
		try {
			if (LavenderePdaConfig.apresentaListaConsignacoesPrimeiroLogin) {
				show(new ListVencimentoPedidoConsignacaoForm());
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}
	
	public void loadAcoesPrimeiroAcessoAoSistemaNoDia() throws SQLException {
		String vlConfigInterno = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.dataUltimoAcessoSistema);
		if (vlConfigInterno == null) {
			EstoqueService.getInstance().atualizaEstoquePedido();
		}
		if (vlConfigInterno == null || !vlConfigInterno.equals(DateUtil.getCurrentDateYYYYMMDD())) {
			deleteLiberacoesSenhaAntigas();
			verificaDataHoraAgendaVisita();
			limpaVerbaSaldoForaVigencia();
			ProdutoDesejadoService.getInstance().executaLimpezaProdutoDesejado();
			AtividadePedidoService.getInstance().deletaAtividadePedidoAntigos();
			DocumentoAnexoService.getInstance().deleteDocsAntigos();
			showPainelGerenciamentoVendas();
			showListaVerbaGrupoSaldoAvisoVigencia();
			showListSac();
			showListVencimentoPedidoConsignacao();
			processaCancelamentosAutomaticoPedidos();
			try {
				ProducaoProdService.getInstance().executaLimpezaEstoqueProducaoProd();
			} catch (Throwable e) {
				UiUtil.showErrorMessage(e);
			}
			deleteArquivoCriptografiaBd();
			executaLimpezaPedidoConsignacao();
			executaLimpezaPedidoNaoEnviadoErp();
			loadAgendaHistCli();
			executaLimpezaNotificacao();
			AgendaVisitaService.getInstance().processaCalculoRotaPrimeiroAcessoDia();
			ConfigInternoService.getInstance().addValue(ConfigInterno.dataUltimoAcessoSistema, DateUtil.getCurrentDateYYYYMMDD());
		}
	}

	private void deleteArquivoCriptografiaBd() throws SQLException {
		try {
			if ((VmUtil.isAndroid() || VmUtil.isIOS()) && !LavenderePdaConfig.isParametroLigadoEmQualquerEntidade(ValorParametro.USABANCOSEMCRIPTOGRAFIA)) {
				FileUtil.deleteFile(Settings.appPath + "/" + DatabaseUtil.NCRIPTOBD);
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void processaCancelamentosAutomaticoPedidos() throws SQLException {
		try {
			PedidoService.getInstance().executaCancelamentoPedidosAbertos(LavenderePdaConfig.getDataBaseCancelamentoAutoPedidoCliente(), ValueUtil.VALOR_NAO);
			PedidoService.getInstance().executaCancelamentoPedidosAbertos(LavenderePdaConfig.getDataBaseCancelamentoAutoPedidoClienteKeyAccount(), ValueUtil.VALOR_SIM);
			CancelamentoPedidoAutomaticoService.getInstance().cancelaPedidosAutomaticamente();
		} catch (CancelamentoPedidoAutoException e) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.CANCELAMENTO_PEDIDO_ERROR_DEFAULT, e.getMessage()));
		}
	}

	private void validaPrimeiroAcessoClienteSemMotivoChurn() throws SQLException {
		if (!LavenderePdaConfig.usaConfigModuloRiscoChurn()) return;

		String vlConfigInterno = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.DATAPRIMEIROACESSOREPRESENTANTE, SessionLavenderePda.getRepresentante().cdRepresentante);
		if (vlConfigInterno != null && vlConfigInterno.equals(DateUtil.getCurrentDateYYYYMMDD())) return;

		if (ClienteChurnService.getInstance().getTotalClienteChurnComMotivoInformado() == 0) return;

		UiUtil.showWarnMessage(Messages.CLIENTECHURN_CLIENTES_SEM_MOTIVO_CHURN);
		ConfigInternoService.getInstance().addValue(ConfigInterno.DATAPRIMEIROACESSOREPRESENTANTE, SessionLavenderePda.getRepresentante().cdRepresentante, DateUtil.getCurrentDateYYYYMMDD() );
	}

	private void executaLimpezaPedidoConsignacao() throws SQLException {
		if (LavenderePdaConfig.isLigadoNuDiasPermanenciaPedidoConsignacao()) {
			PedidoConsignacaoService.getInstance().executaLimpezaPedidoConsignacao();
		}
	}
	
	private boolean showCadAcesso() throws SQLException {
		if (ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.USALOGINAUTOMATICOSISTEMA)) && LavenderePdaConfig.usaSolicitacaoDadosPessoaisAcessoSistema) {
			if (AcessoContatoService.getInstance().isNovoAcesso()) {
				new CadAcessoContatoWindow().popup();
			} else if (!AcessoContatoService.getInstance().isEnviado()) {
				try {
					AcessoContatoService.getInstance().sendDadosServidor();
					if (!AcessoContatoService.getInstance().isEnviado()) {
						AcessoContato acessoContato = (AcessoContato) AcessoContatoService.getInstance().findAll().items[0];
						UiUtil.showErrorMessage(Messages.ERRO_ENVIO_DADOS_SERVIDOR);
						new CadAcessoContatoWindow().acesso(acessoContato).popup();
					}
				} catch (Throwable e) {
					UiUtil.showErrorMessage(e);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		controlaEventoCadItemPedidoForm(event);
		switch (event.type) {
		case PenEvent.PEN_DOWN:
			yPenEventStart = ((PenEvent)event).absoluteY;
			if (canShowMenu && menuSuperior != null && !menuSuperior.isVisibledFull()) {
				int titleSpace = Settings.screenWidth / 3;
				if (getActualForm() instanceof BaseUIForm) {
					Label labelTitle = ((BaseUIForm)getActualForm()).lbTitle;
					titleSpace = labelTitle != null ? labelTitle.getWidth() : 0;
				}
				if (((PenEvent)event).absoluteY < getHeigthTitleArea() && ((PenEvent)event).absoluteX > Settings.screenWidth / 2 - titleSpace / 2 && ((PenEvent)event).absoluteX < Settings.screenWidth / 2 + titleSpace / 2) {
					Insets safeAreaInsets = UiUtil.isIosInsets() ? getSafeAreaInsets() : new Insets();
					int insetsV = safeAreaInsets.top + safeAreaInsets.bottom;
					int insetsH = safeAreaInsets.left + safeAreaInsets.right;
					UiUtil.add(getActualForm(), menuSuperior, 0, 0 - height + (UiUtil.getLabelPreferredHeight() + UiUtil.getLabelPreferredHeight() / 3), width - insetsH, height - insetsV);
					menuSuperior.setVisible(true);
				}
			}
			break;
		case PenEvent.PEN_UP:
			if (canShowMenu && (menuSuperior != null) && menuSuperior.isVisible() && !menuSuperior.isVisibledFull()) {
				menuSuperior.setVisibledFull(false);
				UiUtil.doEfectPopupUpToDown(getActualForm(), menuSuperior, UiUtil.getLabelPreferredHeight() - 2, true);
				menuSuperior.setVisible(false);
				getActualForm().remove(menuSuperior);
				yPenEventStart = -1;
			}
			break;
		case PenEvent.PEN_DRAG:
			if (canShowMenu && yPenEventStart >= 0 && menuSuperior != null) {
				Insets safeAreaInsets = UiUtil.isIosInsets() ? getSafeAreaInsets() : new Insets();
				int insetsV = safeAreaInsets.top + safeAreaInsets.bottom;
				int insetsH = safeAreaInsets.left + safeAreaInsets.right;
				if (yPenEventStart < getHeigthTitleArea() && !menuSuperior.isVisibledFull()) {
					if (!menuSuperior.isVisible()) {
						UiUtil.add(getActualForm(), menuSuperior, 0, 0 - height,  width - insetsH, height - insetsV);
						menuSuperior.setVisible(true);
					} else {
						menuSuperior.setRect(0, (((PenEvent) event).absoluteY - height) <= 0 ? ((PenEvent) event).absoluteY - height: 0, width - insetsH, height - insetsV);
					}
				} else if (yPenEventStart > height - getHeigthTitleArea() && menuSuperior.isVisibledFull()) {
					menuSuperior.setRect(0, (((PenEvent) event).absoluteY - height) <= 0 ? ((PenEvent) event).absoluteY - height : 0, width - insetsH, height - insetsV);
					
				}
			}
			break;
		case PenEvent.PEN_DRAG_END:
			if (canShowMenu && menuSuperior != null && menuSuperior.isVisible()) {
				int yPenEventEnd = ((PenEvent)event).absoluteY;
				if (yPenEventStart < getHeigthTitleArea() && !menuSuperior.isVisibledFull()) {
					int minDragToShowWindow = (height / 9) * 4;
					menuSuperior.setVisibledFull(true);
					if ((yPenEventEnd - yPenEventStart) > minDragToShowWindow) {
						UiUtil.doEfectPopupUpToDown(getActualForm(), menuSuperior, yPenEventEnd, false);
						yPenEventStart = -1;
					} else {
						UiUtil.doEfectPopupUpToDown(getActualForm(), menuSuperior, yPenEventEnd, true);
						menuSuperior.setVisible(false);
						menuSuperior.setVisibledFull(false);
						getActualForm().remove(menuSuperior);
						yPenEventStart = -1;
					}
				} else if (yPenEventStart >= (height - getHeigthTitleArea())) {
					int minDragToShowWindow = height / 8;
					menuSuperior.setVisibledFull(false);
					if ((yPenEventStart - yPenEventEnd) > minDragToShowWindow) {
						UiUtil.doEfectPopupUpToDown(getActualForm(), menuSuperior, yPenEventEnd, true);
						menuSuperior.setVisible(false);
						getActualForm().remove(menuSuperior);
						yPenEventStart = -1;
					} else {
						UiUtil.doEfectPopupUpToDown(getActualForm(), menuSuperior, yPenEventEnd, false);
						menuSuperior.setVisibledFull(true);
						yPenEventStart = -1;
					}
				}
			}
			break;
		}
	}

	private void controlaEventoCadItemPedidoForm(Event event) {
		if (event instanceof KeyEvent && getActualForm() != null && getActualForm() instanceof AbstractBaseCadItemPedidoForm) {
			if (((KeyEvent) event).isActionKey()) {
				try {
					if (!((AbstractBaseCadItemPedidoForm) getActualForm()).isEnterPressionado) {
						((AbstractBaseCadItemPedidoForm) getActualForm()).onFormEvent(event);
					} else {
						((AbstractBaseCadItemPedidoForm) getActualForm()).isEnterPressionado = false;
					}
				} catch (Throwable e) {
					UiUtil.showErrorMessage(e);
				}
			}
		}
	}

	@Override
	public void reposition() {
		super.reposition();
		if (menuSuperior != null) {
			menuSuperior.reposition();
		}
	}

	@Override
	public void close(BaseContainer form) throws SQLException {
    	super.close(form);
    	if ((menuSuperior != null) && menuSuperior.isVisible()) {
			menuSuperior.setVisible(false);
			menuSuperior.setVisibledFull(false);
    	}
    }

	public void loadSessao(UsuarioPdaRep usuarioPda) throws SQLException {
		CommandLineConfig.clearDebugFiles(false);
		SessionLavenderePda.usuarioPdaRep = usuarioPda;
		Session.loadSesssion(SessionLavenderePda.usuarioPdaRep.usuario);
		loadParametros();
		loadHttpConnection();
		loadConfigListOrdering();
		loadConfigTeclado();
		loadClienteRedeConfigList();
		CampoLavendereService.getCampoLavendereInstance().loadConfigPersonCadList();
		loadMsgAlert();
		loadRecebeDadosBackgroundButton();
		deletaVisitaSemPedidoECliente();
		resetFlagProcessandoNfeTxt();
		loadOcultaInformacoesRentabilidadeManualmente();
		loadColetaGps();
		loadPreferenciaFuncaoMap();
		loadParametrosSessao();
		if (!ValueUtil.VALOR_NAO.equalsIgnoreCase(LavenderePdaConfig.cdStatusPedidoPendenteAprovacao)) {
			SessionLavenderePda.loadNuOrdemLiberacaoUsuario();
		}
	}

	private void loadHttpConnection() throws SQLException {
		if (Session.isModoSuporte) {
			return;
		}
		if (LavenderePdaConfig.usaControleOnlineUsuariosInativos || LavenderePdaConfig.bloqueiaSistemaEquipamentoInativo) {
			new AsyncUIControl(true) {
				@Override
				public void execute() {
					try {
						LogSync.logSection(FrameworkMessages.SYNC_INICIO_VERIFICA_EQUIPAMENTO);
						HttpConnectionManager.getDefaultParamsSync();
						LogSync.sucess(FrameworkMessages.SYNC_EQUIPAMENTO_LIBERADO);
					} catch (Throwable e) {
						LogSync.error(e.getMessage());
					} finally {
						LogSync.logSection(FrameworkMessages.SYNC_FIM_VERIFICA_EQUIPAMENTO);
					}
				}
			}.open();
		}
		
	}

	private void resetFlagProcessandoNfeTxt() {
		if (LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) {
			PedidoService.getInstance().resetFlProcessandoNfeTxt();
		}
	}
	
	public void carregaUsuarioAtivo() throws SQLException {
		if (LavenderePdaConfig.usaControleOnlineUsuariosInativos) {
			String configUsuarioAtivo = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.CONFIG_USUARIO_ATIVO);
			SessionLavenderePda.isUsuarioAtivo = ValueUtil.isEmpty(configUsuarioAtivo) || ValueUtil.getBooleanValue(configUsuarioAtivo);
		}
	}

	private void loadColetaGps() throws SQLException {
		if (LavenderePdaConfig.isUsaColetaGpsManual()) {
			ColetaGps coletaGpsEmAndamento = ColetaGpsService.getInstance().getLastColetaGpsEmAndamento();
			if (coletaGpsEmAndamento != null) {
				if (LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual() && ColetaGpsService.getInstance().isDeveEncerrarAutomaticamente(coletaGpsEmAndamento, TimeUtil.getCurrentTimeHHMMSS())) {
					ColetaGpsService.getInstance().finalizaColetaGpsNoHorarioLimite(coletaGpsEmAndamento);
				} else {
					boolean iniciouColeta = PontoGpsService.getInstance().startColetaGpsSistema(true);
					if (DateUtil.getCurrentDate().isAfter(coletaGpsEmAndamento.dtColetaGps)) {
						ColetaGpsService.getInstance().finalizaColetaGpsFimDia(coletaGpsEmAndamento);
						if (iniciouColeta) {
							ColetaGpsService.getInstance().iniciaColetaGps();
						}
					}
					if (iniciouColeta) {
						SessionLavenderePda.setColetaGpsEmAndamento(true);
					}
				}
			}
		} else {
			PontoGpsService.getInstance().startColetaGpsSistema(false);
		}
	}
	
	private static void loadOcultaInformacoesRentabilidadeManualmente() {
		SessionLavenderePda.isOcultoInfoRentabilidade = LavenderePdaConfig.isOcultaInfoRentabilidadeManualmenteDefaultOn();
	}

	private void loadMsgAlert() throws SQLException {
		if (LavenderePdaConfig.usaEnvioPedidoBackground) {
			String[] msgs = StringUtil.split(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.msgAlertaSistema), '#');
			for (int i = 0; i < msgs.length; i++) {
				BaseUIForm.addMsgAlerta(msgs[i]);
			}
		}
	}
	
	private void loadRecebeDadosBackgroundButton() {
		if (LavenderePdaConfig.isUsaRetornoAutomaticoDadosRelativosPedidoBackground()) {
			BaseUIForm.usaBtRecebeDadosBackground = true;
		}
	}

	private void loadParametros() throws SQLException {
		try {
			LavenderePdaConfig.loadParametros();
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e);
			simpleExit();
		}
	}
	
	private void loadConfigListOrdering() throws SQLException {
		try {
	    	ConfigInterno configInternoFilter = new ConfigInterno();
	    	configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
	    	configInternoFilter.cdConfigInterno = ConfigInterno.configSortAndOrderColumn;
			Vector listConfigSortAndOrder = ConfigInternoService.getInstance().findAllByExample(configInternoFilter);
			Hashtable hashConfigSortAndOrder = new Hashtable(listConfigSortAndOrder.size());
			for (int i = 0; i < listConfigSortAndOrder.size(); i++) {
				ConfigInterno configInterno = (ConfigInterno)listConfigSortAndOrder.items[i];
				hashConfigSortAndOrder.put(configInterno.vlChave, StringUtil.split(configInterno.vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			}
			ListContainerConfig.listasConfig = hashConfigSortAndOrder;
		} catch (ValidationException e) {
			//Faz nada
		}
	}

	private void loadConfigTeclado() throws SQLException {
		try {
			ConfigInterno configInternoFilter = new ConfigInterno();
			configInternoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			configInternoFilter.cdConfigInterno = ConfigInterno.tecladoConfiguracoes;
			Vector listTecladoConfiguracoes = ConfigInternoService.getInstance().findAllByExample(configInternoFilter);
			Hashtable hashConfigSortAndOrder = new Hashtable(listTecladoConfiguracoes.size());
			for (int i = 0; i < listTecladoConfiguracoes.size(); i++) {
				ConfigInterno configInterno = (ConfigInterno)listTecladoConfiguracoes.items[i];
				hashConfigSortAndOrder.put(configInterno.vlChave, StringUtil.split(configInterno.vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			}
			BaseKeyboard.hashConfigsDefault = hashConfigSortAndOrder;
		} catch (ValidationException e) {
			//Faz nada
		}
	}

	private void loadClienteRedeConfigList() throws SQLException {
		try {
			String value = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.clienteRedeConfig);
			if (ValueUtil.isNotEmpty(value)) {
				ClienteRedeLiberadoComSenhaConfig.splitPairValueAndSetAtributes(value);
			}
		} catch (ValidationException e) {
			ExceptionUtil.handle(e);
		}
	}

	public void execAdmDeleteDadosAntigos() {
		AdmDeleteDadosAntigosWindow reorganizarDadosForm = new AdmDeleteDadosAntigosWindow();
		reorganizarDadosForm.show(false);
	}
	
	private void gerenciaQtdExecucoes() throws SQLException {
		String v = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.QUANTIDADEEXECUCOES);
		if (ValueUtil.isEmpty(v)) {
			verificaRestauraBackupVersaoPrimeiroAcessoSistema();
			setRecebeFotoCargaInicial();
			atualizaNaoReceberFotosBaixadasNovamente();
			AppConfFilesUtil.manageConnectionConfFile();
		}
		int nr = v == null ? 0 : Convert.toInt(v, 0) + 1;
		ConfigInternoService.getInstance().addValueGeral(ConfigInterno.QUANTIDADEEXECUCOES, String.valueOf(nr));
		if (nr > 0 && (nr % 20) == 0) {
			shrinkDb();
		}
	}

	private void shrinkDb() {
		new AsyncUIControl(true) {
			@Override
			public void execute() {
				LogSync.info(Messages.ADM_MSG_INICIO_SHRINK_DB);
				LogSync.warn(Messages.ADM_MSG_ALERTA_SHRINK_DB);
				LogSync.warn(Messages.ADM_MSG_ALERTA_PERDA_DADOS_SHRINK_DB);
				LogSyncTimer timer = new LogSyncTimer(Messages.ADM_MSG_EXECUTANDO_SHRINK_DB, Messages.ADM_MSG_FIM_SHRINK_DB).newLogOnFinish();
				try {
					CrudDbxDao.getCurrentDriver().vacuum();
				} catch (Exception e) {
					ExceptionUtil.handle(e);
					LogSync.error(e.getMessage());
				} finally {
					timer.finish();
				}
			}
		}.open();
	}

	private void atualizaNaoReceberFotosBaixadasNovamente() throws SQLException {
		if (LavenderePdaConfig.mostraFotoProduto) {
			FotoProdutoService.getInstance().updateNaoReceberFotosBaixadasNovamente();
		}
		if (LavenderePdaConfig.usaFotoProdutoPorEmpresa) {
			FotoProdutoEmpService.getInstance().updateNaoReceberFotosBaixadasNovamente();
		}
		if (LavenderePdaConfig.isUsaFotoProdutoGrade()) {
			FotoProdutoGradeService.getInstance().atualizaNaoReceberFotosBaixadasNovamente();
		}
	}

	private void setRecebeFotoCargaInicial() throws SQLException {
		boolean recebeCargaInicialFotos = false;
		if (LavenderePdaConfig.mostraFotoProduto && FotoProdutoService.getInstance().countFotoCargaInicial() > 0) {
			recebeCargaInicialFotos = true;
			FotoProdutoService.getInstance().updateAllFlTipoAlteracaoInserido();
		} else if (LavenderePdaConfig.usaFotoProdutoPorEmpresa && FotoProdutoEmpService.getInstance().countFotoCargaInicial() > 0) {
			recebeCargaInicialFotos = true;
			FotoProdutoEmpService.getInstance().updateAllFlTipoAlteracaoInserido();
		}
		if (LavenderePdaConfig.usaFotoCliente() && FotoClienteErpService.getInstance().countFotoCargaInicial() > 0) {
			recebeCargaInicialFotos = true;
			FotoClienteErpService.getInstance().updateAllFlTipoAlteracaoInserido();
		}
		if (LavenderePdaConfig.usaDivulgaInformacao && DivulgaInfoService.getInstance().count() > 0) {
			recebeCargaInicialFotos = true;
			DivulgaInfoService.getInstance().updateAllFlTipoAlteracaoInserido();
		}
		if (LavenderePdaConfig.isUsaFotoProdutoGrade() && FotoProdutoGradeService.getInstance().countFotoCargaInicial() > 0) {
			recebeCargaInicialFotos = true;
			FotoProdutoGradeService.getInstance().updateAllFlTipoAlteracaoInserido();
		}
		if (recebeCargaInicialFotos) {
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.CARGAINICIALFOTOSPENDENTE, ValueUtil.VALOR_SIM);
		}
	}

	private void verificaRestauraBackupVersaoPrimeiroAcessoSistema() throws SQLException {
		if (AppConfig.MOD_TEST) {
			return;
		}
		new AsyncUIControl() {
			
			boolean houveErro;
		boolean restauraBackup = LavenderePdaConfig.isRestauraBackupVersaoSemQuestionarUsuario();
			
			@Override
			public void execute() {
				try {
					String fileName = null;
					LogSync.info(Messages.BACKUP_VERSAO_INICIO_VERIFICACAO);
					fileName = LavendereBackupService.getInstance().getLastBackupFileName();
					if (ValueUtil.isEmpty(fileName)) {
						LogSync.warn(Messages.BACKUP_VERSAO_ERRO_VERIFICACAO);
			return;
		}
					if (!fileName.endsWith(".bkp")) {
						LogSync.warn(Messages.BACKUP_VERSAO_NAO_ENCONTRADO);
						return;
					}
		if (LavenderePdaConfig.isRestauraBackupVersaoQuestionandoUsuario() && !restauraBackup) {
			restauraBackup = showRestoreBackupMessage();
		}
		if (restauraBackup) {
			try {
							LogSync.info(Messages.BACKUP_VERSAO_INICIO);
							LavendereBackupService.getInstance().restauraBackupVersaoPrimeiroAcessoSistema(fileName);
						} catch (Exception e) {
							houveErro = true;
							LogSync.error(Messages.BACKUP_VERSAO_ERRO);
							ExceptionUtil.handle(e);
						}
					}
				} catch (Exception e) {
					houveErro = true;
					LogSync.error(e.getMessage());
			} finally {
					LogSync.info(Messages.BACKUP_VERSAO_FIM_PROCESSO);
			}
		}
			
			@Override
			public void after() {
				if (houveErro) {
					MainLavenderePda.getInstance().simpleExit();
	}
			}

		}.open();
	}
	
	public boolean showRestoreBackupMessage() {
		boolean restauraBackup = false;
		WmwInputBox inputBox = UiMessagesUtil.getInputBoxBackup(Messages.BACKUP_PEDIDOS_MSG_RESTAURAR_CONFIRM_VERSAO);
		inputBox.popup();
		if (inputBox.getPressedButtonIndex() == 1) {
			restauraBackup = Messages.BACKUP_CONFERENCIA_CONFIRMO.equals(inputBox.getValue());
			if (!restauraBackup && UiUtil.showConfirmYesNoMessage(Messages.BACKUP_PEDIDOS_MSG_RESTAURAR_TENTAR_NOVAMENTE)) {
				return showRestoreBackupMessage();
			}
		}
		return restauraBackup;
	}

	public void showMenu() throws SQLException {
		SessionLavenderePda.setRepresentanteByUsuarioPdaRep();
		showMainMenu();
	}

	boolean permiteAcessoNormalSistema() throws SQLException {
		return verificaCargaInicialDadosPendente() && verificaEnvioRecebimentoObrigatorio();
	}

	private boolean verificaCargaInicialDadosPendente() throws SQLException {
		boolean cargaVazia = ValueUtil.isNotEmpty(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.CARGADADOSAPPSEMUSUARIOPENDENTE));
		if (cargaVazia) {
			ConfigInternoService.getInstance().removeConfigInternoGeral(ConfigInterno.CARGADADOSAPPSEMUSUARIOPENDENTE, ConfigInterno.VLCHAVEDEFAULT);
			UiUtil.showWarnMessage(Messages.USUARIO_MSG_ALERTA_RECEBER_DADOS);
			showSincronizacaoForm();
			necessarioAcoesAfterInitSistema = true;
			return false;
		}
		return true;
	}

	private boolean verificaEnvioRecebimentoObrigatorio() throws SQLException {
		if (!SessionLavenderePda.isUsuarioSupervisor()) {
			//Seta a data do último recebimento de dados como sendo a data do primeiro acesso ao sistema caso não exista ainda recebimento de dados
			if (LavenderePdaConfig.isObrigaReceberDadosBloqueiaNovoPedido() || LavenderePdaConfig.isObrigaReceberDadosBloqueiaUsoSistema()) {
    			String dataHoraUltRecebimento = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraUtimoRecebimentoDados);
    			if (dataHoraUltRecebimento == null) {
    				ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataHoraUtimoRecebimentoDados, StringUtil.getStringValue(TimeUtil.getTimeAsLong()));
    			}
			}
			if (!(getActualForm() instanceof AdmSincronizacaoForm)) {
				int respEnvio = ConexaoPdaService.getInstance().isEnvioPedidosNecessario();
				if (respEnvio != 0) {
					if (respEnvio != 2 || ConexaoPdaService.getInstance().isNecessarioSolicitarEnviosPedidos()) {
						MainLavenderePda.getInstance().showEnvioDadosObrigatorio(respEnvio);
					}
				}
				int respReceb = ConexaoPdaService.getInstance().isRecebimentoDadosNecessario(true);
				if (respReceb != 0) {
					boolean acessoNormal = showReceberDadosObrigatorio(respReceb, true);
					if (!acessoNormal) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void showMainMenu() throws SQLException {
		MainMenu menuP = instanceMenuPrincipal();
		showMainForm(menuP);
	}

	public MainMenu instanceMenuPrincipal() throws SQLException {
		MainMenu menuP = new MainMenu(Messages.MENU_PRINCIPAL);
		return menuP;
	}
	
	private void validaDataExpiracaoSenha() throws SQLException {
		if (!LavenderePdaConfig.usaLoginAutomaticoSistema && LavenderePdaConfig.isQtdDiasExpiracaoSenhaLigado()) {
			Usuario usuario = SessionLavenderePda.usuarioPdaRep.usuario;
			Date dataExpiracaoSenha = getDataExpiracaoSenha(usuario.dtUltimaAlteracaoSenha);
			if (ValueUtil.isEmpty(dataExpiracaoSenha) || DateUtil.isBeforeOrEquals(dataExpiracaoSenha, DateUtil.getCurrentDate())) {
				CadAlterarSenhaLavendereWindow cadAlterarSenhaLavendereWindow = new CadAlterarSenhaLavendereWindow(usuario);
				UiUtil.showWarnMessage(Messages.USUARIO_MSG_SENHA_EXPIRADA);
				cadAlterarSenhaLavendereWindow.popup();
				if (Session.isModoSuporte) {
					return;
				}
				if (!cadAlterarSenhaLavendereWindow.senhaAlteradaComSucesso) {
					simpleExit();
				}
			}
		}
	}
	
	private Date getDataExpiracaoSenha(Date dtUltimaAlteracaoSenha) {
		Date dataExpiracaoSenha = DateUtil.getDateValue(dtUltimaAlteracaoSenha);
		if (ValueUtil.isNotEmpty(dataExpiracaoSenha)) {
			DateUtil.addDay(dataExpiracaoSenha, LavenderePdaConfig.getQtdDiasExpiracaoSenha());
		}
		return dataExpiracaoSenha;
		
	}
	
	private void validaDiasRestantesParaExpiracaoSenha() throws SQLException {
		if (!LavenderePdaConfig.usaLoginAutomaticoSistema && LavenderePdaConfig.isQtdDiasRestantesParaExpiracaoSenhaLigado()) {
			Usuario usuario = SessionLavenderePda.usuarioPdaRep.usuario;
			Date dataExpiracaoSenha = getDataExpiracaoSenha(usuario.dtUltimaAlteracaoSenha);
			int qtdDiasRestantesParaExpiracaoSenha = DateUtil.getDaysBetween(dataExpiracaoSenha, DateUtil.getCurrentDate());
			if (qtdDiasRestantesParaExpiracaoSenha > 0 && qtdDiasRestantesParaExpiracaoSenha <= LavenderePdaConfig.getQtdDiasRestantesParaExpiracaoSenha()) {
				if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.USUARIO_MSG_SENHA_DESEJA_ALTERAR, qtdDiasRestantesParaExpiracaoSenha))) {
					CadAlterarSenhaLavendereWindow cadAlterarSenhaLavendereWindow = new CadAlterarSenhaLavendereWindow(usuario);
					cadAlterarSenhaLavendereWindow.popup();
				}
			}
		}
		
	}

	private void verificaCargaCompletaZerada() throws SQLException {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.novaCargaCompleta;
		configInterno = (ConfigInterno)ConfigInternoService.getInstance().findByRowKey(configInterno.getRowKey());
		if (configInterno == null) {
			int countClientes = CrudDbxDao.getCurrentDriver().getRowCount(Cliente.TABLE_NAME);
			int countTabelaPreco = CrudDbxDao.getCurrentDriver().getRowCount(TabelaPreco.TABLE_NAME);
			if ((countClientes == 0) && (countTabelaPreco == 0)) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.SISTEMA_MSG_CARGA_INCOMPLETA, StringUtil.getStringValue(SessionLavenderePda.getRepresentante().nmRepresentante)));
				showSincronizacaoForm();
			} else {
				ConfigInternoService.getInstance().addValue(ConfigInterno.novaCargaCompleta, ValueUtil.VALOR_SIM);
			}
		}
	}
	
	public void logout() throws SQLException {
		exit(true);
	}

	public void exit() throws SQLException {
		exit(false);
	}
	
	public void exit(boolean logout) throws SQLException {
		stopRunningSync();
		if (SessionLavenderePda.isRunningSync()) {
			try {
				UiUtil.showProcessingMessage(Messages.SISTEMA_MSG_SAIR_AGUARDANDO_PROCESSOS);
				while (SessionLavenderePda.isRunningSync()) {
					Vm.safeSleep(50);
				}
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		}
		UiUtil.showStartScreen();
		try {
			ConfigInternoService.getInstance().addValueGeral(ConfigInterno.msgAlertaSistema, StringUtil.concatStrings((String[])BaseUIForm.getMsgAlerta().toObjectArray(), '#'));
			if (!LavenderePdaConfig.registraFusoHorarioVisita) {
				ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataHoraPda, TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS());
			}
			ConfigInternoService.getInstance().saveInfosTeclado();
			LogPdaService.getInstance().logMemoria(Messages.LOGPDA_MSG_MEMORIA_SAIDA_SISTEMA);
			LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_SESSAO, Messages.LOGPDA_MSG_SAIDA_SISTEMA);
			if (LavenderePdaConfig.usaRegistroLogin) {
				RegistroLoginLavendereService.getInstance().geraRegistroLogout();
			}
			simpleExit(logout);
		} finally {
			UiUtil.closeStartScreen();
		}
	}

	private void stopRunningSync() {
		if (SessionLavenderePda.isRunningSync()) {
			try {
				UiUtil.showProcessingMessage(Messages.SISTEMA_MSG_SAIR_AGUARDANDO_PROCESSOS);
				while (SessionLavenderePda.isRunningSync()) {
					Vm.safeSleep(50);
				}
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		}
	}

	private void stopBackgroundServices() {
		if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
			ColetaGpsTimerService.getInstance().gravaEmTxt();
			ColetaGpsTimerService.getInstance().pause();
			GpsService.getInstance().stop();
		}
		if (LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema()) {
			ColetaGpsPontosEspecificosService.getInstance().gravaEmTxt();
			ColetaGpsPontosEspecificosService.getInstance().pause();
			GpsService.getInstance().stop();
		}
		SessionLavenderePda.stopBackgroundServices();
	}

	public void stopAllService() {
		UiUtil.showProcessingMessage(Messages.SISTEMA_MSG_SAIR_AGUARDANDO_PROCESSOS);
		try {
			stopRunningSync();
			stopBackgroundServices();
		} finally {
			CrudDbxDao.closeDriver();
			UiUtil.unpopProcessingMessage();
		}
	}

	public void simpleExit() {
		simpleExit(false);
	}
	
	public void simpleExit(boolean logout) {
		try {
			stopBackgroundServices();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			if (! logout) {
				AsyncPool.getInstance().shutdown();
				CrudDbxDao.closeDriver();
				MainWindow.exit(0);
			}
		}
	}

	public void controlaDataEquipamento() throws SQLException {
		if (SessionLavenderePda.ignoraControleDataHora || Session.isModoSuporte) {
			return;
		}
		if (ValueUtil.VALOR_SIM.equals(LavenderePdaConfig.getValorParametroPorSistema(ValorParametro.ATUALIZADATAHORASERVIDORNOACESSOAOSISTEMA))) {
			new AsyncUIControl(true) {
				@Override
				public void execute() {
					try {
						long time = new LavendereTc2Web().obterDataHoraServidorAtualizada();
						if (time > 0) {
							ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataHoraPda, TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS());
						}
					} catch (Exception e) {
						LogSync.error(e.getMessage());
					}
				}
			}.open();
		}
	}
	
	public void liberaDataHoraServidorPorSenha(String message) throws SQLException {
		boolean liberaSenha = UiUtil.showErrorConfirmClosePasswordMessage(message);
		if (liberaSenha) {
			AdmSenhaDinamicaWindow senhaWindow = new AdmSenhaDinamicaWindow();
			senhaWindow.setMensagem(Messages.INFORME_SENHA_CONTROLE_DATA_HORA);
			senhaWindow.setCdUsuario(Session.getCdUsuario());
			senhaWindow.setChaveSemente(SenhaDinamica.SENHA_CONTROLE_DATAHORA_ACESSO_SISTEMA);
			if (senhaWindow.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				SessionLavenderePda.ignoraControleDataHora = true;
				ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataHoraPda, TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS());
				return;
			}
		}
		simpleExit();
	}
	
	private void validaDataHoraServidorEliberaSenha() {
		try {
			validaDataHoraServidor();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		} catch (DataHoraServidorException e) {
			try {
				liberaDataHoraServidorPorSenha(e.getMessage());
			} catch (SQLException e1) {
				ExceptionUtil.handle(e);
			}
		}
	}

	public void validaDataHoraServidor() throws SQLException, DataHoraServidorException {
		if (LavenderePdaConfig.utilizaApenasControleDeDataAcessoSistema) {
			validaDataServidor();
		} else {
			verificaDataHoraServidor();
		}
	}
	
	private void  validaDataServidor() throws SQLException, DataHoraServidorException {
		String vlConfigInterno = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor);
		if (ValueUtil.getLongValue(vlConfigInterno) == 0) {
			return;
		}
		boolean isDataValida = true;
		Date vlConfigDate = null;
		try {
			vlConfigDate = new Date(ValueUtil.getIntegerValue(vlConfigInterno.substring(0, 8)));
			if (DateUtil.isAfterOrEquals(new Date(), vlConfigDate)) {
				return;
			}
		} catch (InvalidDateException e) {
			ExceptionUtil.handle(e);
			isDataValida = false;
		}
		String msg = isDataValida ? Messages.CONFIGINTERNO_MSG_DATASERV_ERRO : Messages.CONFIGINTERNO_MSG_HORA_SERVIDOR_INVALIDA_COM_SENHA;
		throw new DataHoraServidorException(MessageUtil.getMessage(msg, new String[]{StringUtil.getStringValue(vlConfigDate), DateUtil.getCurrentDate().toString()}));
	}
	
	private void verificaDataHoraServidor() throws SQLException, DataHoraServidorException {
		String usaControleTimeZone = LavenderePdaConfig.getPrimeiroValorValidoDoParametro(ValorParametro.USACONTROLETIMEZONE);
		int horasFusoEHorarioDeVerao = ConfigInternoService.getInstance().getHorasFusoEHorarioDeVerao(usaControleTimeZone);
		Time timeServidor = null;
		String mensagemErro = Messages.CONFIGINTERNO_MSG_CONTROLEDATASERV_ERRO;
		if ("2".equals(usaControleTimeZone)) {
			timeServidor = new Time(ValueUtil.getLongValue(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraServidor)));
		}
		if (timeServidor == null) {
			timeServidor = ConfigInternoService.getInstance().getTimeServidor(DateUtil.isDayLight(DateUtil.getCurrentDate()));
		} 
		if (timeServidor != null) {
			Time timePda = new Time();
			long millisBetween = TimeUtil.getMillisRealBetween(timePda, timeServidor);
			boolean acessoInvalido = millisBetween < -900000;
			if ("2".equals(usaControleTimeZone)) {
				boolean ultrapassouLimitePermitido = millisBetween < 0 ? millisBetween * -1 > 900000 : millisBetween > 900000;
				if (ultrapassouLimitePermitido) {
					mensagemErro = Messages.CONFIGINTERNO_MSG_CONTROLETIMEZONEDATASERV_ERRO;
					millisBetween -= TimeUtil.secondsToMilliseconds(horasFusoEHorarioDeVerao * 3600);
					if (horasFusoEHorarioDeVerao < 0) {
						acessoInvalido = timePda.getTimeLong() <  timeServidor.getTimeLong() ? millisBetween < -900000 :  millisBetween > 900000;
					} else if (horasFusoEHorarioDeVerao > 0) {
						acessoInvalido = timePda.getTimeLong() >  timeServidor.getTimeLong() ? millisBetween > 900000 : millisBetween < -900000;
					} 
				}
			}
			if (acessoInvalido) {
				Date dtServidor = DateUtil.getDateValue(timeServidor);
				throw new DataHoraServidorException(MessageUtil.getMessage(mensagemErro, new String[]{dtServidor.toString(), TimeUtil.getTimeHHMM(timeServidor), DateUtil.getCurrentDate().toString(), TimeUtil.getCurrentTimeHHMM()}));
			}
		}
		String dataHoraPdaLong = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.dataHoraPda);
		if (!ValueUtil.isEmpty(dataHoraPdaLong)) {
			Time timePda;
			try {
				timePda = new Time(ValueUtil.getLongValue(dataHoraPdaLong));
			} catch (Throwable e) {
				timePda = new Time();
			}
			long millisBetween = TimeUtil.getMillisRealBetween(new Time(), timePda);
			horasFusoEHorarioDeVerao = horasFusoEHorarioDeVerao > 0 ? horasFusoEHorarioDeVerao * -1 : horasFusoEHorarioDeVerao;
			if ("2".equals(usaControleTimeZone)) {
				millisBetween -= TimeUtil.secondsToMilliseconds(horasFusoEHorarioDeVerao * 3600); 
			}
			if (millisBetween < 0) {
				Date dtPda = DateUtil.getDateValue(timePda);
				throw new DataHoraServidorException(MessageUtil.getMessage(Messages.CONFIGINTERNO_MSG_CONTROLEDATAPDA_ERRO, new String[]{dtPda.toString(), TimeUtil.getTimeHHMM(timePda), DateUtil.getCurrentDate().toString(), TimeUtil.getCurrentTimeHHMM()}));
			}
		}
	}

	/* Metodo não usado na aplicação, anotado com deprecated devido ao vm.settime só funcionar no windows */
	@SuppressWarnings("unused")
	@Deprecated
	private boolean alterDataHoraEquip(Time timeServidor) {
		CalendarWmw calendar = new CalendarWmw();
		calendar.popup();
		Time newTime = new Time(timeServidor.getTimeLong());
		HoraSelect horaSelect = new HoraSelect(newTime);
		horaSelect.popup();
		Time time = newTime;
		time.day = calendar.getSelectedDate().getDay();
		time.month = calendar.getSelectedDate().getMonth();
		time.year = calendar.getSelectedDate().getYear();
		Vm.setTime(time); // so funciona no Windows CE
		UiUtil.showSucessMessage(Messages.DATA_ALTERADA_PARA + DateUtil.getDateValue(new Time()) + " - " + TimeUtil.getTimeHHMM(new Time()));
		Date date = DateUtil.getDateValue(timeServidor);
		if (date.isAfter(DateUtil.getDateValue(new Time())) || (TimeUtil.getMillisRealBetween(new Time(), timeServidor) < 0)) {
			return false;
		}
		return true;
	}
	
	public boolean isSistemaBloqueadoPendenteAtualizacao() throws SQLException {
		if (LavendereConfig.getInstance().isVersaoSistemaDesatualizada()) {
			UiUtil.showWarnMessage(Messages.SISTEMA_MSG_NOVA_VERSAO_FUNCIONALIDADE_BLOQUEADA);
			return true;
		}
		return false;
	}

	public boolean showEnvioDadosObrigatorio(int tipoValidacao) throws SQLException {
		String msgValidacao = Messages.SINCRONIZACAO_MSG_NECESSARIO_ENVIAR_DADOS_TEMPO;
		if (tipoValidacao == 1) {
			msgValidacao = Messages.SINCRONIZACAO_MSG_NECESSARIO_ENVIAR_DADOS_PEDIDOS;
		}
		ConfigInternoService.getInstance().addValue(ConfigInterno.tempoUltimaSugestaoEnvioPedidos, StringUtil.getStringValue(TimeUtil.getTimeAsLong()));
		int respostaWindow = UiUtil.showMessage(msgValidacao, new String[] {FrameworkMessages.BOTAO_CANCELAR, Messages.SENHADINAMICA_TITULO, FrameworkMessages.BOTAO_SIM});
		if (respostaWindow == 2) {
			LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.SINCRONIZACAO_MSG_ENVIANDO_DADOS);
			mb.popupNonBlocking();
			try {
				SyncManager.envieDados(HttpConnectionManager.getDefaultParamsSync());
				UiUtil.showSucessMessage(FrameworkMessages.MSG_SYNC_INFO_ENVIO_CONCLUIDO);
				if (ConexaoPdaService.getInstance().isEnvioPedidosNecessario() == 1) {
					UiUtil.showErrorMessage(Messages.SINCRONIZACAO_MSG_NECESSARIO_ENVIAR_DADOS_PEDIDOS_NOVAMENTE);
					return false;
				}
				ConfigInternoService.getInstance().addValue(ConfigInterno.tempoUtimoEnvioPedidos, StringUtil.getStringValue(TimeUtil.getTimeAsLong()));
			} catch (Throwable e) {
				UiUtil.showErrorMessage(Messages.SINCRONIZACAO_MSG_ENVIO_INCOMPLETO);
				return false;
			} finally {
				mb.unpop();
			}
		} else if (respostaWindow == 1) {
			AdmSenhaDinamicaWindow senhaDinamicaForm = new AdmSenhaDinamicaWindow();
			senhaDinamicaForm.setMensagem(Messages.PEDIDO_A_ENVIAR_NECESSARIO);
			senhaDinamicaForm.setChaveSemente(SenhaDinamica.SENHA_NOVO_PEDIDO_SEMENVIAR_DADOS);
			if (senhaDinamicaForm.show()) {
				UiUtil.showConfirmMessage(Messages.SENHADINAMICA_SUCESSO);
				repaintNow();
				SessionLavenderePda.autorizadoPorSenhaNovoPedidoSemEnvioDados = true;
			} else {
				repaintNow();
				return false;
			}
		} else {
			return false;
		}
		return true;
	}

	public boolean showReceberDadosObrigatorio(int tipoValidacao) throws SQLException {
		return showReceberDadosObrigatorio(tipoValidacao, false);
	}
	
	public boolean showReceberDadosObrigatorio(int tipoValidacao, boolean fromInitSistema) throws SQLException {
		String msgValidacao = Messages.SINCRONIZACAO_MSG_NECESSARIO_RECEBER_DADOS_PEDIDO;
		if (tipoValidacao == 2) {
			msgValidacao = Messages.SINCRONIZACAO_MSG_NECESSARIO_RECEBER_DADOS_SISTEMA;
		}
		int respostaWindow = UiUtil.showMessage(msgValidacao, new String[] {FrameworkMessages.BOTAO_CANCELAR, Messages.SENHADINAMICA_TITULO, FrameworkMessages.BOTAO_SIM});
		if (respostaWindow == 2) {
			showSincronizacaoForm();
			necessarioAcoesAfterInitSistema |= fromInitSistema;
			return false;
		} else if (respostaWindow == 1) {
			String msg = Messages.SINCRONIZACAO_MSG_NECESSARIO_RECEBER_DADOS_SISTEMA_SENHA;
			int chaveSemente = SenhaDinamica.SENHA_SISTEMA_SEMRECEBER_DADOS;
			if (tipoValidacao == 1) {
				msg = Messages.SINCRONIZACAO_MSG_NECESSARIO_RECEBER_DADOS_PEDIDO_SENHA;
				chaveSemente = SenhaDinamica.SENHA_NOVO_PEDIDO_SEMRECEBER_DADOS;
			}
			AdmSenhaDinamicaWindow senhaDinamicaForm = new AdmSenhaDinamicaWindow();
			senhaDinamicaForm.setMensagem(msg);
			senhaDinamicaForm.setChaveSemente(chaveSemente);
			if (senhaDinamicaForm.show()) {
				UiUtil.showConfirmMessage(Messages.SENHADINAMICA_SUCESSO);
				repaintNow();
				if (tipoValidacao == 1) {
					SessionLavenderePda.autorizadoPorSenhaNovoPedidoSemRecebimentoDados = true;
				}
				if (tipoValidacao == 2) {
					SessionLavenderePda.autorizadoPorSenhaSistemaSemRecebimentoDados = true;
				}
			} else {
				necessarioAcoesAfterInitSistema |= fromInitSistema;
				repaintNow();
				return showReceberDadosObrigatorio(tipoValidacao, fromInitSistema);
			}
		} else {
			necessarioAcoesAfterInitSistema |= fromInitSistema;
			if (tipoValidacao == 2) {
				exit();
			}
		}
		return true;
	}

	public boolean showSincronizacaoForm() throws SQLException {
		PedidoService.getInstance().validateBateria();
		if (LavenderePdaConfig.usaEstoqueOnline || LavenderePdaConfig.enviaInformacoesVisitaOnline || LavenderePdaConfig.usaEnvioPedidoBackground) {
			LoadingBoxWindow pb = UiUtil.createProcessingMessage();
			pb.popupNonBlocking();
			try {
				if (LavenderePdaConfig.usaControleOnlineUsuariosInativos) {
					ConfigInternoService.getInstance().addValueGeral(ConfigInterno.CONFIG_USUARIO_ATIVO, StringUtil.getStringValue(SessionLavenderePda.isUsuarioAtivo));
				}
				//--
				DadosTc2WebService.getInstance().updateDadosTc2WebEnviadosServidor();
			} finally {
				pb.unpop();
			}
		}
		if (LavenderePdaConfig.usaBackupAutomatico > 0) {
			try {
				LavendereBackupService.getInstance().realizaBackupPeriodico();
			} catch (Throwable e) {
				UiUtil.showErrorMessage(e);
			}
		}
		AdmSincronizacaoForm sincronizacaoForm = new AdmSincronizacaoForm();
		show(sincronizacaoForm);
		return true;
	}

	public String getMessagesClassName() {
		return Messages.class.getName();
	}
	
	private void carregaRepUltimaSessao() {
		try {
			ConfigInterno configInterno = new ConfigInterno();
			configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
			configInterno.cdConfigInterno = ConfigInterno.ultimoRepEmpSelecionado;
			configInterno.vlChave = "0";
			configInterno.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
			configInterno = (ConfigInterno) ConfigInternoService.getInstance().findByRowKey(configInterno.getRowKey());
			if (configInterno != null) {
				String [] vlConfigInternoSplit = StringUtil.getStringValue(configInterno.vlConfigInterno).split(";");
				Representante rep = (Representante) RepresentanteService.getInstance().findByRowKey(vlConfigInternoSplit[1].split(":")[1]+";");
				if (rep != null) {
					SessionLavenderePda.setRepresentante(rep.cdRepresentante, rep.nmRepresentante);
			}
			}
		} catch (Throwable t) {
			ExceptionUtil.handle(t);
		}
	}
	
	private void loadPersonalizacao() throws SQLException {
		PersonalizacaoLavendereService.getInstance().loadImagemAberturaPersonalizada();
	}
	
	public void loadParametrosSessao() throws SQLException {
		SessionLavenderePda.loadModoFeira();
	}
	
	private void loadPreferenciaFuncaoMap() throws SQLException {
		SessionLavenderePda.loadPreferenciaFuncaoMap();
	}

	@Override
	protected AppConfig getAppConfig() {
		return new LavendereConfig();
	}
	
	public BaseContainer recarregarMenuSessao() throws SQLException {
		MainMenu menu = instanceMenuPrincipal();
		setNewMainForm(menu);
		return menu;
	}

	private void controlaNoMediaFile() {
		if (!VmUtil.isAndroid()) {
			return;
		}
		try {
			String pathProduto = Convert.appendPath(Produto.getPathImg(), FotoUtil.NO_MEDIA_FILE);
			String pathCliente = Convert.appendPath(Cliente.getPathImg(), FotoUtil.NO_MEDIA_FILE);
			String pathDivulgaInfo = Convert.appendPath(DivulgaInfo.getPathImg(), FotoUtil.NO_MEDIA_FILE);
			if (!LavenderePdaConfig.gravaFotosGaleriaEquipamento) {
				createNoMediaFile(pathProduto);
				createNoMediaFile(pathCliente);
				createNoMediaFile(pathDivulgaInfo);
			} else {
				removeNoMediaFile(pathProduto);
				removeNoMediaFile(pathCliente);
				removeNoMediaFile(pathDivulgaInfo);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private void removeNoMediaFile(String path) {
		FileUtil.deleteFile(path);
	}

	private void createNoMediaFile(String path) {
		if (FileUtil.exists(path)) {
			return;
		}
		File f = FileUtil.criaFile(path);
		FileUtil.closeFile(f);
	}

	private void executaLimpezaPedidoNaoEnviadoErp() throws SQLException {
		if (!LavenderePdaConfig.usaConversaoTipoPedido()) {
			return;
		}
		PedidoService.getInstance().executaLimpezaPedidosNaoEnviadosErp();
	}

	private void executaLimpezaNotificacao() {
		try {
			if (ConfigNotificacaoService.getInstance().count() > 0) {
				Vector lstConfigNotificacao = ConfigNotificacaoService.getInstance().findAllByExample(new  ConfigNotificacao());
				Notificacao filter = new Notificacao();
				int size = lstConfigNotificacao.size();
				for (int i = 0; i < size; i++) {
					ConfigNotificacao configNotificacao = (ConfigNotificacao) lstConfigNotificacao.elementAt(i);
					Date dtLimite = DateUtil.getCurrentDate();
					DateUtil.addDay(dtLimite, -configNotificacao.nuDiasLimpeza);
					filter.dtLimpeza = dtLimite;
					filter.dsTipoNotificacao = configNotificacao.tipoNotificacao.dsTipoNotificacao;
					NotificacaoService.getInstance().deleteAllByExample(filter);
				}
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
	public void verificaWgpsInstalled() {
		if (WtoolsUtil.isWtoolsVersionOutdated()) {
			if (LavendereConfig.getInstance().nuVersionWtoolsInstalled == null) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.SISTEMA_MSG_WTOOLS_NAO_INSTALADO, new String[] {LavendereConfig.getInstance().nuVersionWtools}));
			} else {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.SISTEMA_MSG_NUVERSAO_WTOOLS, new String[] {LavendereConfig.getInstance().nuVersionWtoolsInstalled, LavendereConfig.getInstance().nuVersionWtools}));
			}
			SyncManager.downloadInstallWtools();
		}
	}

	@Override
	public List<HttpConnection> getHttpConnectionList() throws SQLException {
		return ConexaoPdaService.getInstance().findConexaoPdaSortedAsList();
	}

	@Override
	public void httpConnectionValidate(ParamsSync sync) throws SQLException {
		UsuarioLavendereService.getInstance().validaUsuarioAtivo(sync);
		SyncManager.validaEquipamentoBloqueado(sync);
	}
	
	@Override
	public void controleEquipamento(ParamsSync sync) throws SQLException {
		EquipamentoUsuarioLavendereService.getInstance().controlaEquipamento(sync);
	}

	@Override
	public void noConnectionValidate() {
		
		if (LavenderePdaConfig.bloqueiaSistemaEquipamentoInativo) {
			try {
				if (!EquipamentoUsuarioLavendereService.getInstance().validaEquipamentoLiberadoBanco()) {
					throw new UsuarioBloqueadoException(Messages.ERRO_EQUIPAMENTO_NAO_LIBERADO);
				}
			} catch (SQLException e) {
				ExceptionUtil.handle(e);
			}
		}
		if (LavenderePdaConfig.usaControleOnlineUsuariosInativos && !SessionLavenderePda.isUsuarioAtivo) {
			throw new UsuarioBloqueadoException(Messages.SISTEMA_MSG_USUARIO_INATIVO);
		}
	}
	
	private void loadAgendaHistCli() throws SQLException {
		if (LavenderePdaConfig.isRegistraAgendaCliHist()) {
			AgendaVisitaCliHistService.getInstance().geraAgendaHistCli();
		}
	}
	
}
