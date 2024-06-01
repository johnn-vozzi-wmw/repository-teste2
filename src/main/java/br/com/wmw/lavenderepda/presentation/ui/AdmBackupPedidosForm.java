package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.BackupService;
import br.com.wmw.framework.business.service.BackupService.BackupFile;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.SyncInfo;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.LavendereBackupService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.io.IOException;
import totalcross.sys.Convert;
import totalcross.sys.Vm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PressListener;
import totalcross.ui.gfx.Color;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class AdmBackupPedidosForm extends BaseCrudListForm {

	private ButtonAction btBackup, btRestore, btSend;
	private int contadorDadosRestauracao = 0;

	public AdmBackupPedidosForm() {
		super(Messages.BACKUP_TITULO);
		
		btBackup = new ButtonAction(Messages.BOTAO_REALIZAR_BACKUP, "images/realizarbackup.png");
		btBackup.addPressListener(backupClick());
		
		btRestore = new ButtonAction(Messages.BOTAO_RECUPERAR_BACKUPS, "images/reload.png");
		btRestore.addPressListener(restoreClick());
		
		btSend = new ButtonAction(Messages.BOTAO_ENVIAR_BACKUP_SERVIDOR, "images/enviar.png");
		btSend.addPressListener(sendBackupClick());
		listContainer = new GridListContainer(6, 2);
		listContainer.setColPosition(3, RIGHT);
		listContainer.setColPosition(5, RIGHT);
		listContainer.setBarTopSimple();
		listContainer.resizeable = false;
		
	}

	@Override
	protected void onFormStart() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(false);
		UiUtil.add(this, listContainer, LEFT, TOP + barTopContainer.getHeight(), FILL, FILL - barBottomContainer.getHeight());
		UiUtil.add(barBottomContainer, btBackup, 1);
		UiUtil.add(barBottomContainer, btSend, 2);
		UiUtil.add(barBottomContainer, btRestore, 5);
	}
	
	@Override
	protected String[] getItem(Object domain) throws SQLException {
		BackupFile backupFile = (BackupFile) domain;
		String[] item = new String[6];
		item[0] = backupFile.name;
		item[1] = ValueUtil.VALOR_NI;
		item[2] = StringUtil.getStringValue(backupFile.dtBackup) + " - " + backupFile.hora;
		item[3] = StringUtil.getStringValue(backupFile.cdVersaoDb);
		item[4] = backupFile.restored ? Messages.BKP_PEDIDO_RESTAURADO : Messages.BKP_PEDIDO_NAO_RESTAURADO;
		item[5] = LavendereBackupService.getInstance().getDsTipoBackup(backupFile.flTipoBackup);
		return item;
	}
	
	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}
	
	@Override
	protected void setPropertiesInRowList(Item containerItem, BaseDomain domain) throws SQLException {
		if (((BackupFile)domain).restored) {
			containerItem.setBackColor(Color.darker(ColorUtil.errorColor, -120));
		} else {
			containerItem.setBackColor(Color.darker(ColorUtil.sucessColor, -120));
		}
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		try {
			Vector listBackupLocal = BackupService.listBackups();
			try {
				LavendereWeb2Tc lavendereWeb2Tc = new LavendereWeb2Tc(SyncManager.getParamsSyncBackup());
				Vector listBackupServidor = lavendereWeb2Tc.listBackupServidor();
				int size = listBackupServidor.size();
				for (int i = 0; i < size; i++) {
					BackupFile bk = (BackupFile) listBackupServidor.items[i];
					if (!FileUtil.exists(Convert.appendPath(DatabaseUtil.getDriverBackupPath(), bk.name)) && !FileUtil.exists(Convert.appendPath(DatabaseUtil.getDriverBackupEnviadoPath(), bk.name))) {
						bk.servidor = true;
						listBackupLocal.addElement(bk);
					}
				}
			} catch (Throwable e) {
				ExceptionUtil.handle(e);
				UiUtil.showErrorMessage(Messages.BACKUP_ERRO_RECUPERAR_SERVIDOR);
			}
			SortUtil.qsortString(listBackupLocal.items, 0, listBackupLocal.size() - 1, false);
			return listBackupLocal;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return new Vector();
	}

	private void realizaBackupTabelasPedido() throws Throwable {
		if (UiUtil.showConfirmYesNoMessage(Messages.BACKUP_PEDIDOS_MSG_CONFIRM)) {
			
			LoadingBoxWindow mbRealizandoBackup = UiUtil.createProcessingMessage(Messages.BACKUP_MSG_REALIZANDO_BACKUP);
			mbRealizandoBackup.popupNonBlocking();
			try {
				LavendereBackupService.getInstance().realizaBackup(BackupService.BackupFile.FLTIPOBACKUP_MANUAL);
				UiUtil.showSucessMessage(Messages.BACKUP_MSG_SUCESSO);
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(ex);
			} finally {
				mbRealizandoBackup.unpop();
				CrudDbxDao.getCurrentDriver();
				list();
			}
		}
	}

	private void recuperaBackupTabelasPedido() throws SQLException, IOException {
		BackupFile backupFile = (BackupFile) getSelectedDomain2();
		if (backupFile == null) {
			UiUtil.showErrorMessage(Messages.BACKUP_NENHUM_SELECIONADO);
			return;
		}
		InfoDadosRestoureBackup infoDadosRestoure = new InfoDadosRestoureBackup(Messages.BACKUP_RESTAURACAO, AvisaPerdaDadosRestauracao(), contadorDadosRestauracao);
		infoDadosRestoure.popup();
		if (infoDadosRestoure.closedByBtFechar == false) {
			if (Messages.BACKUP_CONFERENCIA_CONFIRMO.equals(infoDadosRestoure.edConfirmar.getText().toUpperCase())) {
				LoadingBoxWindow mbAguarde = UiUtil.createProcessingMessage();
				mbAguarde.popupNonBlocking();
				try {
					if (backupFile.servidor) {
						new LavendereWeb2Tc(SyncManager.getParamsSyncBackup()).downloadAndUnzipBackupAppFile(backupFile.name);
					}
					BackupService restore = new BackupService.Backup().restore(backupFile.name);
					restore.restoreBackup();
					UiUtil.showSucessMessage(Messages.BACKUP_RESTAURAR_MSG_SUCESSO);
				} catch (Throwable e) {
					UiUtil.showErrorMessage(e);
				} finally {
					mbAguarde.unpop();
					LogPdaService.getInstance().log(LogPda.LOG_NIVEL_WARN, LogPda.LOG_CATEGORIA_BACKUP, MessageUtil.getMessage(Messages.BACKUP_MSG_RESTAURACAO_LOGPDA, new Object[] {Messages.BACKUP_CONFERENCIA_CONFIRMO, backupFile.name, LavendereBackupService.getInstance().getDsTipoBackup(backupFile.flTipoBackup)}));
					list();
				}
			} else {
				UiUtil.showErrorMessage(Messages.BACKUP_MSG_ERRO_RESTAURACAO);
				LogPdaService.getInstance().log(LogPda.LOG_NIVEL_ERROR, LogPda.LOG_CATEGORIA_BACKUP, MessageUtil.getMessage(Messages.BACKUP_MSG_ERRO_RESTAURACAO, new Object[] {Messages.BACKUP_MSG_ERRO_RESTAURACAO, backupFile.name, LavendereBackupService.getInstance().getDsTipoBackup(backupFile.flTipoBackup)}));
			}
		}
	}
	
	public String[][] AvisaPerdaDadosRestauracao() throws IOException, SQLException {
		LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(new ParamsSync());
		Hashtable infoList = SyncManager.getInfoAtualizacao(true);
		lavendereTc2Web.removeTabelasEnviadasPorOutroServico(infoList);
		Vector keys = infoList.getKeys();
		keys.qsort();
		int infoSize = keys.size();
		String[][] tabelasDados = new String[infoSize][2];
		String tableName;
		SyncInfo syncInfo;
		contadorDadosRestauracao = 0;
		for (int iInfo = 0; iInfo < infoSize; iInfo++) {
			tableName = (String)keys.items[iInfo];
			syncInfo = (SyncInfo) infoList.get(tableName);
			if (syncInfo.flRetorno && syncInfo.flAtivo) {
				int nuLinhas = lavendereTc2Web.verificaEntidadePendenteEnvio(tableName);
				if (nuLinhas > 0) {
					tabelasDados[contadorDadosRestauracao][0] = tableName;
					tabelasDados[contadorDadosRestauracao][1] = StringUtil.getStringValue(nuLinhas);
					contadorDadosRestauracao++;					
				}
			}
		}	
		
		addInfoPerdaTabelasEspeciais(lavendereTc2Web, tabelasDados);
		
		return tabelasDados;
	}

	private void addInfoPerdaTabelasEspeciais(LavendereTc2Web lavendereTc2Web, String[][] tabelasDados)	throws SQLException {
		String tableName = "TBLVPPEDIDO";
		Vector pedidoList = lavendereTc2Web.getPedidoListParaAvisoDePerda();
		if (pedidoList.size() > 0) {
			tabelasDados[contadorDadosRestauracao][0] = tableName;
			tabelasDados[contadorDadosRestauracao][1] = StringUtil.getStringValue(pedidoList.size());
			contadorDadosRestauracao++;
		}
		
		try {
			tableName = "TBLVPNOVOCLIENTE";
			int nuLinhas = lavendereTc2Web.verificaEntidadePendenteEnvio(tableName);
			if (nuLinhas > 0) {
				tabelasDados[contadorDadosRestauracao][0] = tableName;
				tabelasDados[contadorDadosRestauracao][1] = StringUtil.getStringValue(nuLinhas);
				contadorDadosRestauracao++;	
			}
		} catch (Throwable e) {
			// Não faz nada, pode ser que o cliente não utilize a tabela NovoCliente
		}
	}

	@Override
	public void onFormClose() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(true);
	}
	
	private void enviarBackupServidor() throws Exception {
		BackupFile backupFile = (BackupFile) getSelectedDomain2();
		if (backupFile == null) {
			UiUtil.showErrorMessage(Messages.BACKUP_NENHUM_SELECIONADO);
			return;
		}
		UiUtil.showProcessingMessage();
		try {
			if (!backupFile.servidor) {
				LavendereTc2Web pdaToErp = new LavendereTc2Web(SyncManager.getParamsSyncBackup());
				pdaToErp.sendBackupApp(backupFile.name);
			}
			UiUtil.showSucessMessage(Messages.BACKUP_ENVIADO_SUCESSO);
		} finally {
			UiUtil.unpopProcessingMessage();
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		
	}

	private PressListener backupClick() {
		return new PressListener() {
			@Override
			public void controlPressed(ControlEvent e) {
				try {
					realizaBackupTabelasPedido();
				} catch (Throwable ex) {
					UiUtil.showErrorMessage(ex);
				}
			}
		};
	}
	
	private PressListener restoreClick() {
		return new PressListener() {
			@Override
			public void controlPressed(ControlEvent e) {
				try {
					if (LavenderePdaConfig.isUsaLiberacaoComSenhaRestauracaoBackup()) {
						AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow(SenhaDinamica.SENHA_LIBERACAO_RESTAURACAO_BACKUP);
						admSenhaDinamicaWindow.setMensagem(Messages.MSG_LIBERA_COM_SENHA_RESTAURACAO_BACKUP);
						admSenhaDinamicaWindow.setCdUsuario(Session.getCdUsuario());
						if (admSenhaDinamicaWindow.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
							repaint();
							throw new ValidationException(Messages.MSG_SENHA_INVALIDA);
						}
					}
					recuperaBackupTabelasPedido();
					EstoqueService.getInstance().atualizaEstoquePedido();
				} catch (ValidationException ve) {
					UiUtil.showErrorMessage(ve.getMessage());
				} catch (Throwable e1) {}
			}
		};
	}
	
	private PressListener sendBackupClick() {
		return new PressListener() {
			@Override
			public void controlPressed(ControlEvent e) {
				try {
					enviarBackupServidor();
				} catch (Throwable e1) {
					UiUtil.showErrorMessage(e1);
				}
			}
		};
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		return new BackupFile(ValueUtil.VALOR_NI, DateUtil.getCurrentDate(), ValueUtil.VALOR_NI);
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}
	
}