package br.com.wmw.lavenderepda.sync.async;

import java.sql.SQLException;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.sync.AtualizacaoDadosManager;
import totalcross.sys.Vm;

public class SincronizacaoWeb2AppRunnable extends RunnableImpl {
	
	private static SincronizacaoWeb2AppRunnable instance;
	private AtualizacaoDadosManager atualizacaoDadosManager;
	private boolean firstRun;
	
	private SincronizacaoWeb2AppRunnable() {
		super(LavenderePdaConfig.intervaloSyncAutomaticoWeb2App * MINUTE);
		firstRun = true;
	}
	
	public static SincronizacaoWeb2AppRunnable getInstance() {
		if (instance == null) {
			instance = new SincronizacaoWeb2AppRunnable();
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
			atualizacaoDadosManager.executeReceberDados();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private void reload() {
		removeQueue();
		millisWait = LavenderePdaConfig.intervaloSyncAutomaticoWeb2App * MINUTE;
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
