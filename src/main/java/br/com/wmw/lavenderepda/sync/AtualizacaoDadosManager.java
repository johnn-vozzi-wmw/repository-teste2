package br.com.wmw.lavenderepda.sync;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.notification.Notification;
import br.com.wmw.framework.notification.NotificationManager;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.CancelamentoPedidoAutomaticoService;
import br.com.wmw.lavenderepda.business.service.ClienteAtuaService;
import br.com.wmw.lavenderepda.business.service.ClienteInativacaoService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.EnderecoGpsPdaService;
import br.com.wmw.lavenderepda.business.service.PedidoLogService;
import br.com.wmw.lavenderepda.business.service.SacService;
import br.com.wmw.lavenderepda.business.validation.CancelamentoPedidoAutoException;
import br.com.wmw.lavenderepda.business.validation.DataHoraServidorException;
import br.com.wmw.lavenderepda.notification.LavendereNotificationConstants;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import totalcross.sys.Vm;
import totalcross.util.Vector;

public class AtualizacaoDadosManager {

	public ParamsSync paramsSync;
	public boolean houveErroEnvio;
	private boolean houveErroRecebimento;
	public Vector errorDetalhesSyncList;
	
	public AtualizacaoDadosManager(ParamsSync sync) {
		paramsSync = sync;
		if (paramsSync == null) {
			throw new ValidationException(FrameworkMessages.ADMSYNC_ERRO_CONEX);
		}
	}
	
	public void executeEnviarDados() {
		int count = 1;
		boolean initLock = false;
		while (!(initLock = SessionLavenderePda.initLockSync()) && count++ < 10) {
			Vm.sleep(1000);
		}
		if (initLock) {
			try {
				execucaoEnvioDados();
			} catch (Throwable e) {
				LogSync.error(e.getMessage());
			} finally {
				checkErrorLogNotification();
				SessionLavenderePda.releaseLockSync();
			}
		} else {
			LogSync.warn(Messages.SINCRONIZACAO_MSG_SYNC_BACKGROUND_EM_ANDAMENTO);
			NotificationManager.putNotification(SyncActions.getSyncAndamentoNotification());
		}
	}
	
	public void executeReceberDados() {
		int count = 1;
		boolean initLock = false;
		while (!(initLock = SessionLavenderePda.initLockSync()) && count++ < 10) {
			Vm.sleep(1000);
		}
		if (initLock) {
			try {
				execucaoRecebimentoDados();
			} catch (Throwable e) {
				LogSync.error(e.getMessage());
			} finally {
				checkErrorLogNotification();
				SessionLavenderePda.releaseLockSync();
			}
		} else {
			LogSync.warn(Messages.SINCRONIZACAO_MSG_SYNC_BACKGROUND_EM_ANDAMENTO);
			NotificationManager.putNotification(SyncActions.getSyncAndamentoNotification());
		}
			
	}
	
	private void execucaoEnvioDados() throws SQLException {
		if (!antesEnviarDados()) {
			return;
		}
		try {
			atualizaDataHoraServidor();
			houveErroEnvio = SyncManager.envieDados(paramsSync);
			SyncManager.sendBackupFiles(paramsSync);
			aposEnviarDados(paramsSync);
		} catch (Throwable ex) {
			LogSync.error(ex.getMessage());
			houveErroEnvio = true;
		} finally {
			String msg = houveErroEnvio ? FrameworkMessages.SINCRONIZACAO_MSG_ERRO_ENVIO : FrameworkMessages.MSG_SYNC_INFO_ENVIO_CONCLUIDO; 
			if (houveErroEnvio) {
				LogSync.error(msg);
			} else {
				LogSync.sucess(msg);
			}
//			WmwToast.show(msg, 1000);
			aposSincronizacao();
		}
	}
	
	public boolean antesReceberDados() throws SQLException {
		if (SyncManager.verificaAtualizacaoVersaoPda(paramsSync)) {
			return false;
		}
		SyncManager.clearInfoAtualizacao();
		return true;
	}
	
	public boolean antesEnviarDados() throws SQLException {
		try {
			CancelamentoPedidoAutomaticoService.getInstance().cancelaPedidosAutomaticamente();
		} catch (CancelamentoPedidoAutoException e) {
			LogSync.error(e.getMessage());
		}
		return true;
	}
	
	private void aposEnviarDados(ParamsSync ps) throws Exception {
		LogSyncTimer timer = new LogSyncTimer("Início ações pós envio", "Fim ações pós envio");
		try {
			atualizaUltimaHoraEnvio();
			clearDadosAposEnvio();
			SessionLavenderePda.reloadVisitaAndamento();
			LogSync.sucess(Messages.SINCRONIZACAO_MSG_FIM_ACAOES_APOS_ENVIO);
		} finally {
			timer.finish();
		}
	}
	
	private void aposSincronizacao() throws SQLException {
		atualizaDataHoraServidor();
		try {
			MainLavenderePda.getInstance().validaDataHoraServidor();
		} catch (DataHoraServidorException e) {
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.VALIDADATAHORASERVIDOR) {
				@Override
				public void process() throws Exception {
					MainLavenderePda.getInstance().liberaDataHoraServidorPorSenha(e.getMessage());
				}
			});
		}
 	}

	private void checkErrorLogNotification() {
		if (Session.saveLogErrorList && LogSync.getErrorList().size() > 0) {
			NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_DETALHES_SYNC) {
				@Override
				public void process() {
					SyncActions.showDetalhesErrosSync();
				}
			});
		}
	}
	
	
	public void execucaoRecebimentoDados() throws SQLException {
		if (!antesReceberDados()) {
			return;
		}
		LogSync.logSection(Messages.SINCRONIZACAO_MSG_INICIO_RECEBIMENTO);
		try {
			atualizaDataHoraServidor();
			houveErroRecebimento = SyncManager.recebaDados(paramsSync);
			aposReceberDados();
		} catch (Throwable ex) {
			LogSync.error(ex.getMessage());
			houveErroRecebimento = true;
		} finally {
			String msg = houveErroRecebimento ? FrameworkMessages.SINCRONIZACAO_MSG_ERRO_RECEBIMENTO : FrameworkMessages.MSG_SYNC_INFO_RECEBIMENTO_CONCLUIDO;
			if (houveErroRecebimento) {
				LogSync.error(msg);
			} else {
				LogSync.sucess(msg);
			}
//			WmwToast.show(msg, 1000);
			aposSincronizacao();
		}
	}
	
	private void atualizaDataHoraServidor() {
		try {
			LogSync.info(Messages.SINCRONIZACAO_MSG_RECEBENDO_DATA_HORA);
			String time = SyncManager.getDataHoraServidor(paramsSync);
			long longValue = ValueUtil.getLongValue(time);
			if (ValueUtil.isNotEmpty(time) && longValue > 0) {
				ConfigInternoService.getInstance().addValueGeral(ConfigInterno.dataHoraServidor, time);
				LogSync.replace(MessageUtil.getMessage(Messages.SINCRONIZACAO_MSG_DATA_HORA_ATUALIZADO, DateUtil.getDateTimeAsString(longValue)));
			}
		} catch (Throwable ex) {
			LogSync.error(Messages.SINCRONIZACAO_MSG_DATA_HORA_NAO_ATUALIZADO);
		}
	}
	
	private void clearDadosAposEnvio() throws SQLException {
		EnderecoGpsPdaService.getInstance().deleteAllEnviadosServidor();
		ClienteAtuaService.getInstance().deleteAllEnviadosServidor();
		if (LavenderePdaConfig.geraLogAcaoPedidoItemPedido) {
			PedidoLogService.getInstance().deleteAllEnviadosServidor();
		}
		if (LavenderePdaConfig.permiteInativarClienteProspeccao) {
			ClienteInativacaoService.getInstance().deleteCancelados();
		}
	}

	private void atualizaUltimaHoraEnvio() throws SQLException {
		long time = TimeUtil.getTimeAsLong();
		String horaEnvio = StringUtil.getStringValue(time);
		LogSync.info("Atualiza hora último envio " + DateUtil.getDateTimeAsString(time));
		ConfigInternoService.getInstance().addValue(ConfigInterno.tempoUtimoEnvioPedidos, horaEnvio);
	}
	
	private void aposReceberDados() throws Exception {
		LogSyncTimer timer = new LogSyncTimer(Messages.SINCRONIZACAO_MSG_INICIO_ACAOES_APOS_RECEBIMENTO, Messages.SINCRONIZACAO_MSG_FIM_ACAOES_APOS_RECEBIMENTO);
		try {
			boolean isPrimeiroSyncDia = SyncManager.isPrimeiroReceberDadosDoDia();
			if (LavenderePdaConfig.isUsaAlertaBloqueioClienteSemPedido() && SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.exibeRelClientesNaoAtendidosAposPrimeiroSync && isPrimeiroSyncDia) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_RELCLINAOATENDIDO) {
					@Override
					public void process() throws Exception {
						SyncActions.showRelClientesNaoAtendidos();
					}
				});
			}
			if (LavenderePdaConfig.relMetasDeVendaUnificado && LavenderePdaConfig.exibeRelMetasDeVendaAposPrimeiroSync && isPrimeiroSyncDia) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_RELMETAVENDAS) {
					@Override
					public void process() throws Exception {
						SyncActions.showRelMetaVendas();
					}
				});
			}
			if ((LavenderePdaConfig.isExibeRelatorioNovosSacsSync() || (isPrimeiroSyncDia && LavenderePdaConfig.isExibeRelatorioNovosSacsPrimeiroSync())) && SacService.getInstance().hasSacExibirRelatorio()) {
				NotificationManager.putNotification(new Notification(LavendereNotificationConstants.OPEN_LISTNOVOSAC) {
					@Override
					public void process() throws Exception {
						SyncActions.showListNovoSac();
					}
				});
			}
			if (LavenderePdaConfig.isEnviaEstoqueRepParaServidor()) {
				ConfigInternoService.getInstance().removeConfigInterno(ConfigInterno.devolverEstoqueAtual, SessionLavenderePda.cdEmpresa + SessionLavenderePda.getRepresentante().cdRepresentante);
			}
			LogSync.sucess(Messages.SINCRONIZACAO_MSG_FIM_ACAOES_APOS_RECEBIMENTO);
		} finally {
			timer.finish();
		}
	}

}
