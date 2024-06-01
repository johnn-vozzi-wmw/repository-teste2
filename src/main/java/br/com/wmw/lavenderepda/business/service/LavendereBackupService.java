package br.com.wmw.lavenderepda.business.service;

import java.util.HashSet;
import java.util.Set;

import br.com.wmw.framework.business.service.BackupService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigIntegWebTc;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.LogApp;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import br.com.wmw.lavenderepda.sync.SyncManager;
import totalcross.util.Vector;

public class LavendereBackupService extends BackupService {
	
	private static LavendereBackupService instance;
	
	public static LavendereBackupService getInstance() {
		if (instance == null) {
			instance = new LavendereBackupService();
		}
		return instance;
	}
	
	public void realizaBackupPeriodico() throws Throwable {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			String dtHrBackup = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.dataHoraUltimoBackup);
			if ((ValueUtil.isEmpty(dtHrBackup) && LavenderePdaConfig.usaBackupAutomatico > 0) || TimeUtil.getSecondsRealBetween(TimeUtil.getCurrentTime(), TimeUtil.toTime(dtHrBackup)) > LavenderePdaConfig.usaBackupAutomatico * 60) {
				realizaBackup(BackupFile.FLTIPOBACKUP_AUTO);
			}
		} finally {
			mb.unpop();
		}
	}

	public String realizaBackup(String flTipoBackup) throws Throwable {
		String fileName = null;
		try {
			Set<String> tables = new HashSet<String>();
			Vector findAllRetorno = ConfigIntegWebTcService.getInstance().findAllRetorno();
			for (int i = 0; i < findAllRetorno.size(); i++) {
				String dsTabela = ((ConfigIntegWebTc) findAllRetorno.items[i]).dsTabela;
				if (LogApp.TABLE_NAME.equalsIgnoreCase(dsTabela)) continue;

				tables.add(dsTabela);
			}
			BackupService backup = new BackupService.Backup().backup();
			fileName = backup.getBackupFileName();
			backup.executeBackup(tables, flTipoBackup);
			LogPdaService.getInstance().log(LogPda.LOG_NIVEL_INFO, LogPda.LOG_CATEGORIA_BACKUP, MessageUtil.getMessage(Messages.BACKUP_MSG_LOGPDA, new Object[] {fileName, getDsTipoBackup(flTipoBackup)}));
			String vlConfig = DateUtil.formatDateDDMMYYYY(DateUtil.getCurrentDate()) + " " + TimeUtil.getCurrentTimeHHMMSS();
			ConfigInternoService.getInstance().addValue(ConfigInterno.dataHoraUltimoBackup, vlConfig);
			return fileName;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			CrudDbxDao.getCurrentDriver();
		}
		return fileName;
	}
	
	public String getDsTipoBackup(String flTipoBackup) {
		if (BackupFile.FLTIPOBACKUP_AUTO.equals(flTipoBackup)) {
			return Messages.BACKUP_DSTIPO_A;
		} else if (BackupFile.FLTIPOBACKUP_MANUAL.equals(flTipoBackup)) {
			return Messages.BACKUP_DSTIPO_M;
		} else if (BackupFile.FLTIPOBACKUP_VERSAO.equals(flTipoBackup)) {
			return Messages.BACKUP_DSTIPO_V;
		}
		return ValueUtil.VALOR_NI;
	}
	
	public void restauraBackupVersaoPrimeiroAcessoSistema(String fileName) {
		try {
			LavendereWeb2Tc erp2Pda = new LavendereWeb2Tc(SyncManager.getParamsSyncBackup());
			Backup backup = new BackupService.Backup();
			erp2Pda.downloadAndUnzipBackupAppFile(fileName);
			BackupService restore = backup.restore(fileName);
			restore.restoreBackup();
			LogSync.sucess(Messages.BACKUP_RESTAURAR_MSG_SUCESSO);
			if (ValueUtil.isNotEmpty(fileName)) {
				LogPdaService.getInstance().log(LogPda.LOG_NIVEL_INFO, LogPda.LOG_CATEGORIA_BACKUP, MessageUtil.getMessage(Messages.BACKUP_MSG_RESTAURACAO_LOGPDA, new Object[] {Messages.BACKUP_CONFERENCIA_CONFIRMO, fileName, LavendereBackupService.getInstance().getDsTipoBackup(BackupFile.FLTIPOBACKUP_VERSAO)}));
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			throw new ValidationException(Messages.BACKUP_VERSAO_ERRO);
		}
	}
	
	public String getLastBackupFileName() {
		try {
			LavendereWeb2Tc erp2Pda = new LavendereWeb2Tc(SyncManager.getParamsSyncBackup());
			return erp2Pda.getLastBackupFileName();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return ValueUtil.VALOR_NI;
	}
	
}
