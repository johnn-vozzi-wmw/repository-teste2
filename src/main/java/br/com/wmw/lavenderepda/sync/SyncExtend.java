package br.com.wmw.lavenderepda.sync;

import java.sql.SQLException;
import java.util.Map;
import java.util.Map.Entry;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.CatalogoExterno;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DivulgaInfo;
import br.com.wmw.lavenderepda.business.domain.FotoProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.CatalogoExternoService;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.business.service.FotoClienteErpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoEmpService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.VideoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.VideoProdutoService;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.presentation.ui.ListPedidoForm;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.util.MediaUtil.MEDIA_ENTIDADE;
import totalcross.sys.Vm;

public class SyncExtend {
	
	public static void receiveFileForeGroung(Map<MEDIA_ENTIDADE, Integer> entidades, String msg, ParamsSync sync) {
		if (!SessionLavenderePda.isDownloadingMedia() && UiUtil.showConfirmYesNoMessage(msg)) {
			new AsyncUIControl() {
				@Override
				public void execute() {
					try {
						receiveFile(entidades, new LavendereWeb2Tc(sync));
					} catch (Throwable e) {
						LogSync.error(e.getMessage());
					}
				}
				public void after() {
					if (SessionLavenderePda.isDownloadingMedia()) {
						Notification notification = getStopDownloadMediaNotification();
						try {
							notification.process();
						} catch (Exception e) {
							//
						}
					}
				}
			}.closeKeppRunning().open();
		}
		
	}
	
	private static void receiveFile(Map<MEDIA_ENTIDADE, Integer> entidades, LavendereWeb2Tc web2Tc) throws Exception {
		SessionLavenderePda.setDownloadingMedia(true);
		LogSyncTimer timer = new LogSyncTimer(Messages.WEB2TC_RECEBIMENTO_ARQUIVO, Messages.WEB2TC_RECEBIMENTO_ARQUIVO_FIM).newLogOnFinish();
		try {
			recebeMediaEntidade(entidades, web2Tc);
		} finally {
			SessionLavenderePda.setDownloadingMedia(false);
			timer.finish();
			LogSync.sucess(Messages.WEB2TC_RECEBIMENTO_ARQUIVO_FIM);
		}
	}
	
	private static void recebeMediaEntidade(Map<MEDIA_ENTIDADE, Integer> entidades, LavendereWeb2Tc web2Tc) throws Exception {
		for (Entry<MEDIA_ENTIDADE, Integer> entry : entidades.entrySet()) {
			if (!SessionLavenderePda.isDownloadingMedia()) {
				break;
			}
			LogSync.info(ValueUtil.VALOR_EMBRANCO);
			LogSync.logSection(entry.getKey().name() + ": " + entry.getValue() + " arquivos.");
			switch (entry.getKey()) {
			case FOTOPRODUTO:
				downloadFotoProduto(web2Tc);
				break;
			case FOTOPRODUTOEMP:
				downloadFotoProdutoEmp(web2Tc);
				break;
			case FOTOCLIENTEERP:
				downloadFotoClienteErp(web2Tc);
				break;
			case FOTOPRODUTOGRADE:
				downloadFotoProdutoGrade(web2Tc);
				break;
			case DIVULGAINFO:
				downloadFotoDivulgaInfo(web2Tc);
				break;
			case CATALOGOEXTERNO:
				downloadArquivoCatalogoExterno(web2Tc);
				break;
			case VIDEOPRODUTO:
				downloadArquivoVideoProduto(web2Tc);
				break;
			case VIDEOPRODUTOGRADE:
				downloadArquivoVideoProdutoGrade(web2Tc);
				break;
			case MENUCATALOGO:
				web2Tc.processaFotoMenuCatalogo();
				break;
			default:
				break;
			}
		}
	}
	
	private static void downloadFotoProduto(LavendereWeb2Tc web2Tc) throws Exception {
		FileUtil.createDirIfNecessaryQuietly(Produto.getPathImg());
		web2Tc.downloadFileEntidade(FotoProdutoService.getInstance());
	}
	
	private static void downloadFotoProdutoEmp(LavendereWeb2Tc web2Tc) throws Exception {
		FotoProdutoEmpService.getInstance().createDirsFotoProdutoEmp();
		web2Tc.downloadFileEntidade(FotoProdutoEmpService.getInstance());
	}
	
	private static void downloadFotoProdutoGrade(LavendereWeb2Tc web2Tc) throws Exception {
		FileUtil.createDirIfNecessaryQuietly(FotoProdutoGrade.getPathImg());
		web2Tc.downloadFileEntidade(FotoProdutoGradeService.getInstance());
	}
	
	private static void downloadFotoClienteErp(LavendereWeb2Tc web2Tc) throws Exception {
		FileUtil.createDirIfNecessaryQuietly(Cliente.getPathImg());
		web2Tc.downloadFileEntidade(FotoClienteErpService.getInstance());
	}
	
	private static void downloadFotoDivulgaInfo(LavendereWeb2Tc web2Tc) throws Exception {
		FileUtil.createDirIfNecessaryQuietly(DivulgaInfo.getPathImg());
		web2Tc.downloadFileEntidade(DivulgaInfoService.getInstance());
	}
	
	private static void downloadArquivoCatalogoExterno(LavendereWeb2Tc web2Tc) throws Exception {
		FileUtil.createDirIfNecessaryQuietly(CatalogoExterno.getPathCatalogoExterno());
		web2Tc.downloadFileEntidade(CatalogoExternoService.getInstance());
	}
	
	private static void downloadArquivoVideoProduto(LavendereWeb2Tc web2Tc) throws Exception {
		VideoProdutoService.getInstance().createDirsFotoProdutoEmp();
		web2Tc.downloadFileEntidade(VideoProdutoService.getInstance());
	}
	
	private static void downloadArquivoVideoProdutoGrade(LavendereWeb2Tc web2Tc) throws Exception {
		VideoProdutoGradeService.getInstance().createDirsFotoProdutoEmp();
		web2Tc.downloadFileEntidade(VideoProdutoGradeService.getInstance());
	}
	
	public static int getQtFotoProdutoParaDownload() throws SQLException {
		return FotoProdutoService.getInstance().countAllNaoAlterados();
	}
	public static int getQtFotoProdutoEmpParaDownload() throws SQLException {
		return FotoProdutoEmpService.getInstance().countAllNaoAlterados();
	}
	
	public static int getQtFotoClienteParaDownload() throws SQLException {
		return FotoClienteErpService.getInstance().countAllNaoAlterados();
	}
	
	public static int getQtFotoDivulgacaoInfoParaDownload() throws SQLException {
		return DivulgaInfoService.getInstance().countAllNaoAlterados();
	}

	public static int getQtFotoProdutoGradeParaDownload() throws SQLException {
		return FotoProdutoGradeService.getInstance().countAllNaoAlterados();
	}

	public static int getQtCatalogoExternoParaDownload() throws SQLException {
		return CatalogoExternoService.getInstance().countAllNaoAlterados();
	}
	
	public static int getQtVideProdutoParaDownload() throws SQLException {
		return VideoProdutoService.getInstance().countAllNaoAlterados();
	}
	
	public static int getQtVideProdutoGradeParaDownload() throws SQLException {
		return VideoProdutoGradeService.getInstance().countAllNaoAlterados();
	}
	
	public static void showMessageAtualizaApp(String msg) {
		if (msg != null) {
			if (Messages.SISTEMA_MSG_VERSAO_REINICIAR.equalsIgnoreCase(msg)) {
				if (!LavenderePdaConfig.isRestauraBackupAutomaticoPermitindoAtualizarVersaoComPedidoAberto() && PedidoService.getInstance().hasPedidosPendentes()) {
					UiUtil.showWarnMessage(Messages.SISTEMA_MSG_CONFIRM_NOVA_VERSAO_PEDIDOS_PENDENTES);
					try {
						MainLavenderePda.getInstance().show(new ListPedidoForm());
					} catch (Exception e) {
						ExceptionUtil.handle(e);
					}
					return;
				}
				UiUtil.showConfirmMessage(msg);
				try {
					SyncManager.instalaNewApp();
					return;
				} catch (Exception e) {
					UiUtil.showErrorMessage(e.getMessage());
				}
			}
			UiUtil.showErrorMessage(msg);
		}
	}
	
	public static void atualizaApp() {
		new AsyncUIControl() {
			@Override
			public void execute() {
				try {
					SyncManager.atualizaApp();
				} catch (Throwable e) {
					LogSync.error(e.getMessage());
				}
			}
			
			@Override
			public boolean beforeAsync() {
				return UiUtil.showConfirmYesNoMessage(Messages.MSG_TITLE_ATUALIZAR_APP, Messages.MSG_ATUALIZAR_APP_CONFIRMACAO);
			}
			
			@Override
			public void after() {
				Notification atualizaApp = NotificationManager.checkNotificationAndRemove(LavendereNotificationConstants.ATUALIZACAO_APP);
				try {
					if (atualizaApp != null) {
						atualizaApp.process();
					}
				} catch (Exception e) {
					UiUtil.showErrorMessage(e.getMessage());
				}
			}
		}.open();
	}
	
	public static Notification getStopDownloadMediaNotification() {
		return new Notification(LavendereNotificationConstants.STOP_SYNC_MEDIA) {
			
			public void process() throws Exception {
				if (SessionLavenderePda.isDownloadingMedia() && UiUtil.showConfirmYesNoMessage(Messages.TITLE_DOWNLOAD_MEDIA, Messages.CONFIRM_DOWNLOAD_MEDIA)) {
					AsyncPool.getInstance().execute(new RunnableImpl() {
						@Override
						public void exec() {
							Vm.sleep(10000);
							if (SessionLavenderePda.isDownloadingMedia()) {
								NotificationManager.putNotification(getStopDownloadMediaNotification());
							}
						}
					});
				} else {
					SessionLavenderePda.setDownloadingMedia(false);
				}
			}
		};
	}

}
