package br.com.wmw.lavenderepda.sync.async;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.config.AppConfig;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ConnectionException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.integration.dao.SQLiteDriver;
import br.com.wmw.framework.sync.LogSync;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.async.AsyncUIControl;
import br.com.wmw.framework.timer.LogSyncTimer;
import br.com.wmw.framework.util.DatabaseUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.io.File;
import totalcross.sys.Convert;

public class AsyncManutencaoDatabase extends AsyncUIControl {

	private TypeAsyncManutencao typeAsyncManutencao;
	private ParamsSync ps;
	private boolean exit;

	private AsyncManutencaoDatabase() {
	}

	public AsyncManutencaoDatabase(TypeAsyncManutencao typeAsyncManutencao) {
		this(typeAsyncManutencao, null);
	}

	public AsyncManutencaoDatabase(TypeAsyncManutencao typeAsyncManutencao, ParamsSync ps) {
		super(false);
		this.ps = ps;
		this.typeAsyncManutencao = typeAsyncManutencao;
	}

	@Override
	public boolean beforeAsync() {
		MainLavenderePda.getInstance().stopAllService();
		AsyncPool.getInstance().restart();
		return super.beforeAsync();
	}

	@Override
	public void afterExecuteASync() {
		if (exit) {
			close();
		} else {
			AsyncPool.getInstance().restart();
			SessionLavenderePda.startBackgroundServices();
		}
	}

	@Override
	public void execute() {
		switch (typeAsyncManutencao) {
			case RESET:
				executeReset();
				break;
			case RECOVER:
				executeRecover();
				break;
		}
	}

	private void executeRecover() {
		final String cdUsuarioSession = Session.getCdUsuario();
		LogSyncTimer timer = null;
		try {
			prepareSystemRecovery();
			LogSync.info(Messages.ADMINISTRACAO_USUARIO_SESSAO + ": " + Session.getCdUsuario());
			timer = new LogSyncTimer(Messages.ADMINISTRACAO_MSG_RECUPERANDO_BANCO).newLogOnFinish();
			LavendereWeb2Tc pdaToErp = getLavendereWeb2Tc();
			pdaToErp.downloadRecoverAppDatabase();
			existRecoveredDB();
			LogSync.sucess(Messages.ADMINISTRACAO_MSG_RECOVER_SUCESSO);
			exit = true;
		} catch (ConnectionException ex) {
			exit = false;
			LogSync.error(ex.getMessage());
		} catch (Exception ex) {
			exit = true;
			LogSync.error(Messages.ADMINISTRACAO_MSG_RECOVER_ERRO);
			LogSync.error(ex.getMessage());
		} finally {
			String name = Convert.appendPath(DatabaseUtil.getDriverPath(), AppConfig.DATABASE_NAME_RECOVER);
			FileUtil.deleteFile(name);
			Session.setCdUsuario(cdUsuarioSession);
			if (timer != null) {
				timer.finish();
			}
		}
	}

	private void executeReset() {
		try {
			LogSync.info(Messages.ADMINISTRACAO_MSG_SERVICOS_PARADOS);
			LavendereWeb2Tc pdaToErp = getLavendereWeb2Tc();
			pdaToErp.downloadResetAppDatabase();
			existRecoveredDB();
			String msgSucesso = Messages.ADMINISTRACAO_MSG_RESET_SUCESSO + " " + Messages.ADMINISTRACAO_MSG_CLOSE;
			LogSync.sucess(msgSucesso);
			exit = true;
		} catch (ConnectionException ex) {
			exit = false;
			LogSync.error(ex.getMessage());
		} catch (Exception ex) {
			LogSync.error(ex.getMessage());
			LogSync.error(Messages.ADMINISTRACAO_MSG_CLOSE);
			exit = true;
		}
	}

	private LavendereWeb2Tc getLavendereWeb2Tc() throws Exception {
		LavendereWeb2Tc pdaToErp;
		if (ps == null) {
			pdaToErp = new LavendereWeb2Tc();
		} else {
			pdaToErp = new LavendereWeb2Tc(ps);
		}
		return pdaToErp;
	}

	private void prepareSystemRecovery() throws Exception {
		LogSync.info(Messages.ADMINISTRACAO_MSG_SERVICOS_PARADOS);
		String dbOriginal = Convert.appendPath(DatabaseUtil.getDriverPath(), AppConfig.DATABASE_NAME);
		String dbRecover = Convert.appendPath(DatabaseUtil.getDriverPath(), AppConfig.DATABASE_NAME_RECOVER);
		if (FileUtil.exists(dbRecover)) {
			FileUtil.deleteFile(dbRecover);
		}
		FileUtil.copyFile(dbOriginal, dbRecover);
		if (!FileUtil.exists(dbRecover)) {
			throw new Exception(Messages.ADMINISTRACAO_ERRO_COPIAR_BANCO);
		}
		LogSync.info(Messages.ADMINISTRACAO_MSG_BANCO_COPIADO);
		SQLiteDriver recoverDriver = new SQLiteDriver(dbRecover);
		boolean isDescrypted = recoverDriver.decryptDB();
		if (!isDescrypted) {
			throw new Exception(Messages.ADMINISTRACAO_ERRO_PREPARAR_BANCO);
		}
		LogSync.info(Messages.ADMINISTRACAO_MSG_BANCO_PREPARADO);
	}

	private static void close() {
		MainLavenderePda.getInstance().simpleExit();
	}

	private void existRecoveredDB() {
		String pathDbRecuperado = Convert.appendPath(DatabaseUtil.getDriverPath(), DatabaseUtil.DIR_BACKUP_DATABASE_RECUPERADO);
		String pathFileDbRecuperado = Convert.appendPath(pathDbRecuperado, AppConfig.DATABASE_NAME);
		if (FileUtil.exists(pathFileDbRecuperado)) {
			LogSync.warn(Messages.ADMINISTRACAO_ERRO_SUBSTITUIR_DB);
		}
	}

	public static void replaceByRecoveredDb() {
		CrudDbxDao.closeDriver();
		String pathDbRecuperado = Convert.appendPath(DatabaseUtil.getDriverPath(), DatabaseUtil.DIR_BACKUP_DATABASE_RECUPERADO);
		String pathFileDbRecuperado = Convert.appendPath(pathDbRecuperado, AppConfig.DATABASE_NAME);
		String pathMainDb = Convert.appendPath(DatabaseUtil.getDriverPath(), AppConfig.DATABASE_NAME);
		try (File fileBkpDbProblema = new File(pathFileDbRecuperado);
			 File fileDB = new File(pathMainDb)
		) {
			if (fileBkpDbProblema.exists()) {
				if (fileDB.exists()) {
					fileDB.delete();
				}
				FileUtil.moveFile(fileBkpDbProblema.getPath(), fileDB.getPath());
			}
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}
}
