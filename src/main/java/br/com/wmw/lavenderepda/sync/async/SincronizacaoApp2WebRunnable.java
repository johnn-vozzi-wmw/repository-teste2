package br.com.wmw.lavenderepda.sync.async;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.sync.AtualizacaoDadosManager;
import totalcross.sys.Vm;

public class SincronizacaoApp2WebRunnable extends RunnableImpl {
	
	private static SincronizacaoApp2WebRunnable instance;
	private AtualizacaoDadosManager atualizacaoDadosManager;
	private boolean firstRun;
	
	private SincronizacaoApp2WebRunnable() {
		super(LavenderePdaConfig.intervaloSyncAutomaticoApp2Web * MINUTE);
		firstRun = true;
	}
	
	public static SincronizacaoApp2WebRunnable getInstance() {
		if (instance == null) {
			instance = new SincronizacaoApp2WebRunnable();
		}
		return instance;
	}
	
	@Override
	public void exec() {
		try {
			if (firstRun) {
				firstRun = false;
				Vm.sleep(10000);
			}
			atualizacaoDadosManager = new AtualizacaoDadosManager(HttpConnectionManager.getDefaultParamsSync());
			atualizacaoDadosManager.executeEnviarDados();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}
	
	
	private void reload() {
		removeQueue();
		millisWait = LavenderePdaConfig.intervaloSyncAutomaticoApp2Web * MINUTE;
	}
	
	public static void addQueue() {
		getInstance().reload();
		if (getInstance().isSyncAutomaticoLigado()) {
			AsyncPool.getInstance().execute(getInstance());
		}
	}
	
	@Override
	public boolean isKeepRunning() {
		return isSyncAutomaticoLigado();
	}
	
	public boolean isSyncAutomaticoLigado() {
		return millisWait >= MINUTE;
	}
	
	public static void removeQueue() {
		if (instance != null) {
			instance.stopRunning();
		}
	}

}
