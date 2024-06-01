package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.EquipamentoUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.sync.async.AsyncManutencaoDatabase;
import br.com.wmw.lavenderepda.sync.async.TypeAsyncManutencao;
import br.com.wmw.lavenderepda.util.AppConfFilesUtil;
import totalcross.io.File;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class AdmFatalWindow extends WmwWindow {
	
	private static final String UNKNOWN_USER = "Unknown";

	private String conexao;
	
	private LabelName lbPlatform;
	private LabelName lbDeviceId;
	
	private LabelValue lvErrorCause;
	private LabelName lbDatabaseAppPath;
	private LabelValue lvDatabaseAppPath;
	private LabelName lbDatabaseDataPath;
	private LabelValue lvDatabaseDataPath;
	private LabelName lbPermissaoArmazenamento;
	private LabelValue lvPermissaoArmazenamento;
	private LabelName lbConexao;
	private BaseComboBox cbConexao;
	private LabelName lbOutraConexao;
	private EditText edConexao;
	private LabelName lbEnviarDados;
	private BaseButton btEnviarDados;
	private LabelName lbResetDados;
	private BaseButton btResetDados;
	
	private ButtonPopup btVerificar;

	public AdmFatalWindow(String msg) {
		super(Messages.ADM_FATAL_TITULO);
		scrollable = true;
		btVerificar = new ButtonPopup(Messages.ADM_FATAL_BOTAO_VERIFICAR);
		int msgWidth = Settings.screenWidth * 85 / 100;
		lvErrorCause = new LabelValue();
		lvErrorCause.setMultipleLinesText(Convert.insertLineBreak(msgWidth, fm, msg));
		lbPlatform = new LabelName(Messages.INFOPDA_PLATAFORMA + " " + Settings.platform + " - " + Settings.romVersion);
		lbDeviceId = new LabelName("Device  " + Settings.deviceId);
		lbResetDados = new LabelName(Convert.insertLineBreak(msgWidth, UiUtil.defaultFontSmall.fm, Messages.ADM_FATAL_MSG_RESET));
		btResetDados = new BaseButton(Messages.ADMINISTRACAO_RESET_DATABASE);
		lbEnviarDados = new LabelName(Convert.insertLineBreak(msgWidth, UiUtil.defaultFontSmall.fm, Messages.ADM_FATAL_MSG_ALERTA_BANCO_ENVIADO));
		btEnviarDados = new BaseButton(Messages.ADM_FATAL_BOTAO_ENVIAR_DADOS);
		lbDatabaseAppPath = new LabelName(Messages.ADM_FATAL_LABEL_BANCO_DADOS_INTERNO);
		lvDatabaseAppPath = new LabelValue();
		lbDatabaseDataPath = new LabelName(Messages.ADM_FATAL_LABEL_BANCO_DADOS_EXISTE);
		lvDatabaseDataPath = new LabelValue();
		lbPermissaoArmazenamento = new LabelName(Messages.ADM_FATAL_LABEL_PERMISSAO_ARMAZENAMENTO);
		lvPermissaoArmazenamento = new LabelValue();
		lbConexao = new LabelName(Messages.ADM_FATAL_LABEL_CONEXAO);
		lbOutraConexao = new LabelName(Messages.ADM_FATAL_LABEL_OUTRA_CONEXAO);
		edConexao = new EditText("", 200);
		cbConexao = new BaseComboBox();
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		addButtonPopup(btVerificar);
		UiUtil.add(scBase, lvErrorCause, getLeft(), getNextY());
		UiUtil.add(scBase, lbDatabaseDataPath, getLeft(), getNextY());
		UiUtil.add(scBase, lvDatabaseDataPath, AFTER, SAME);
		if (VmUtil.isAndroid() || VmUtil.isJava()) {
			UiUtil.add(scBase, lbDatabaseAppPath, getLeft(), getNextY());
			UiUtil.add(scBase, lvDatabaseAppPath, AFTER, SAME);
			UiUtil.add(scBase, lbPermissaoArmazenamento, getLeft(), getNextY());
			UiUtil.add(scBase, lvPermissaoArmazenamento, AFTER, SAME);
		}
		UiUtil.add(scBase, lbConexao, cbConexao, getLeft(), getNextY());
		UiUtil.add(scBase, lbOutraConexao, edConexao, getLeft(), getNextY());
		UiUtil.add(scBase, lbPlatform, getLeft(), getNextY());
		UiUtil.add(scBase, lbDeviceId, getLeft(), getNextY());
		UiUtil.add(scBase, lbEnviarDados, btEnviarDados, getLeft(), getNextY() + HEIGHT_GAP_BIG);
		UiUtil.add(scBase, lbResetDados, btResetDados, getLeft(), getNextY() + HEIGHT_GAP_BIG);
	}
	
	@Override
	protected void onPopup() {
		super.onPopup();
		preencheValores();
	}

	private void preencheValores() {
		if (VmUtil.isAndroid() || VmUtil.isJava()) {
			lvDatabaseAppPath.setText(getFileExistsValue(Settings.appPath));
			lvDatabaseDataPath.setText(getFileExistsValue(Settings.dataPath));
			lvPermissaoArmazenamento.setText(getFolderPermission());
		} else {
			lvDatabaseDataPath.setText(getFileExistsValue(DatabaseUtil.getDriverPath()));
		}
		conexao = AppConfFilesUtil.loadConnectionConf();
		String[] conexaoList = conexao.split(";");
		for (String conn : conexaoList) {
			cbConexao.add(conn);
		}
		cbConexao.setSelectedIndex(0);
	}
	
	private String getFileExistsValue(final String path) {
		return FileUtil.exists(getDbCompletePath(path)) ? FrameworkMessages.VALOR_SIM : FrameworkMessages.VALOR_NAO;
	}
	
	private String getFolderPermission() {
		String folderName = TimeUtil.getCurrentTimeHHMMSS();
		try (File fileDir = new File(Convert.appendPath(Settings.dataPath, folderName))) {
			fileDir.createDir();
			fileDir.delete();
			return FrameworkMessages.VALOR_SIM;
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
		return FrameworkMessages.VALOR_NAO;
	}
	
	@Override
	protected void postPopup() {
		super.postPopup();
		enviaBancoServidor();
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
		case ControlEvent.PRESSED:
			if (event.target == btResetDados) {
				btResetDadosClick();
			} else if (event.target == btEnviarDados) {
				enviaBancoServidor();
			} else if (event.target == btVerificar) {
				verificarBancoNovamente();
			}
			break;
		default:
			break;
		}
	}

	private void btResetDadosClick() {
		if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ADMINISTRACAO_MSG_CONFIRMACAO_RESET)) {
			return;
		}
		if (verificaProblemaAusenciaBancoDataPath()) {
			if (copiaBancoAppPathParaDataPath()) {
				verificarBancoNovamente();
			}
		}
		new AsyncManutencaoDatabase(TypeAsyncManutencao.RESET, getDefaultParamSync(getSelectedConection())).open();
	}

	private boolean verificaProblemaAusenciaBancoDataPath() {
		return FileUtil.exists(getDbCompletePath(Settings.appPath)) && !FileUtil.exists(getDbCompletePath(Settings.dataPath));
	}

	private String getDbCompletePath(String path) {
		return Convert.appendPath(path, AppConfig.DATABASE_NAME);
	}

	private boolean copiaBancoAppPathParaDataPath() {
		try {
			FileUtil.copyFile(getDbCompletePath(Settings.appPath), getDbCompletePath(Settings.dataPath));
		} catch (Exception e) {
			ExceptionUtil.handle(e);
			return false;
		}
		return true;
	}
	
	private void verificarBancoNovamente() {
		try {
			CrudDbxDao.getCurrentDriver().con();
			UiUtil.showInfoMessage(Messages.ADM_FATAL_MSG_CONECTADO_BANCO_SUCESSO);
			super.fecharWindow();
		} catch (Exception e) {
			UiUtil.showErrorMessage(Messages.ADM_FATAL_MSG_CONECTADO_BANCO_FALHA + e.getMessage());
			lvErrorCause.setMultipleLinesText(Convert.insertLineBreak(Settings.screenWidth * 85 / 100, fm, e.getMessage()));
		}
	}

	@Override
	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		MainLavenderePda.getInstance().simpleExit();
	}

	private void enviaBancoServidor() {
		new AsyncUIControl() {
			@Override
			public void execute() {
				final String cdUsuarioSession = Session.getCdUsuario();
				try {
					if (ValueUtil.isEmpty(Session.getCdUsuario())) {
						Session.setCdUsuario(AppConfFilesUtil.loadCdUsuarioConf());
					}
					if (ValueUtil.isEmpty(Session.getCdUsuario())) {
						Session.setCdUsuario(SyncManager.buscaUsuarioEquipamento(EquipamentoUtil.getCdEquipamento()));
					}
					if (ValueUtil.isEmpty(Session.getCdUsuario())) {
						Session.setCdUsuario(UNKNOWN_USER);
					}
			    	String selectedConection = getSelectedConection();
			    	if (ValueUtil.isNotEmpty(selectedConection)) {
			    		LavendereTc2Web pdaToErp = new LavendereTc2Web(getDefaultParamSync(selectedConection));
			    		pdaToErp.enviaBancoDadosToErp(DatabaseUtil.DIR_BACKUP_DATABASE_PROBLEMA);
			    		LogSync.sucess(Messages.ADMINISTRACAO_MSG_BANCO_ENVIADO_SUCESSO);
			    	}
				} catch (Exception ex) {
					LogSync.error(ex.getMessage());
				} finally {
					Session.setCdUsuario(cdUsuarioSession);
				}
			}
		}.open();
	}

	private ParamsSync getDefaultParamSync(String baseUrl) {
		ParamsSync ps = new ParamsSync();
		ps.baseUrl = baseUrl;
		ps.httpWeb2TcUrl = "/public/service/web2tc/";
		ps.httpTc2WebUrl = "/public/service/tc2web/";
		ps.basePublicServiceUrl = "/public/service/";
		ps.basePublicUrl = "/public/";
		ps.cdSessao = TimeUtil.getCurrentDateTimeDDDDDSSSSS();
		return ps;
	}

	private String getSelectedConection() {
		if (ValueUtil.isNotEmpty(edConexao.getText())) {
			return edConexao.getText();
		}
		if (cbConexao.getSelectedItem() != null) {
			return cbConexao.getSelectedItem().toString();
		}
		return ValueUtil.VALOR_NI;
	}
	
}
