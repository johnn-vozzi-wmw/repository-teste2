package br.com.wmw.lavenderepda.sync.async;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.SyncUtil;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import br.com.wmw.lavenderepda.sync.LavendereTc2Web;
import totalcross.io.ByteArrayStream;
import totalcross.io.LineReader;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;

public class EnviaPontoGpsRunnable extends RunnableImpl {

	private static EnviaPontoGpsRunnable instance;
	public Lock lock = new Lock();
	
	public EnviaPontoGpsRunnable() {
		super(LavenderePdaConfig.nuIntervaloEnvioPontoGpsBackground * 1000);
	}

	public static EnviaPontoGpsRunnable getInstance() {
		if (instance == null) {
			instance = new EnviaPontoGpsRunnable();
		}
		return instance;
	}

	private void sendPontoGpsFromTxt2Web() {
		synchronized (lock) {
			try {
				String[] files = PontoGpsService.getInstance().getFiles();
				if (ValueUtil.isEmpty(files)) {
					return;
				}
				ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
				ps.nuMaxTentativas = 3;
				LavendereTc2Web lavendereTc2Web = new LavendereTc2Web(ps, false);
				String cdSessao = PontoGpsService.getInstance().generateIdGlobal();
				int sizeFiles = files.length;
				boolean houveEnvio = false;
				for (int i = 0; i < sizeFiles; i++) {
					String file = files[i];
					Vector pontoGpsList;
					try {
						pontoGpsList = PontoGpsService.getInstance().getPontoGpsListFromFile(file);
					} catch (Throwable e) {
						LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao ler txt \"" + file + "\" (" + TimeUtil.getCurrentTimeHHMMSS() + "): " + e.getMessage());
						continue;
					}
					int size = pontoGpsList.size();
					if (size > 0) {
						ByteArrayStream cbasEnvio = new ByteArrayStream(4096);
						try {
							SyncUtil.writeLine(cbasEnvio, PontoGpsService.getInstance().addSelectColumns());
							for (int x = 0; x < size; x++) {
								SyncUtil.writeLine(cbasEnvio, PontoGpsService.getInstance().addInsertValues((String[]) pontoGpsList.items[x]));
							}
						} catch (Throwable e) {
							LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao criar registros de ponto gps. \"" + file + "\" (" + TimeUtil.getCurrentTimeHHMMSS() + "): " + e.getMessage());
							continue;
						}
						try {
							ByteArrayStream retornoServidor = lavendereTc2Web.enviaDadosServidorBackground(cdSessao, PontoGps.TABLE_NAME, size, cbasEnvio);
							LineReader lr = new LineReader(retornoServidor);
							String retorno = lr.readLine();
							if (ValueUtil.isNotEmpty(retorno) && retorno.startsWith("OK")) {
								houveEnvio = true;
							} else {
								throw new ValidationException(retorno);
							}
						} catch (Throwable e) {
							LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao enviar os pontos do txt \"" + file + "\" para web (" + TimeUtil.getCurrentTimeHHMMSS() + "): " + e.getMessage());
							continue;
						}
					}
					PontoGpsService.getInstance().deletePontoGpsFile(file);
				}
				if (houveEnvio) {
					try {
						lavendereTc2Web.executeFimEnvioDados(cdSessao);
					} catch (Throwable e) {
						LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao chamar fimEnvioDados apos envio de pontoGPS: " + e.getMessage());
					}
				}
			} catch (Throwable e) {
				LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao ler do txt e enviar para web: " + e.getMessage());
			}
		}
	}


	@Override
	public void exec() {
		sendPontoGpsFromTxt2Web();
	}
	
	public static void addQueue() {
		if (LavenderePdaConfig.isUsaEnvioPontoGpsBackground()) {
			AsyncPool.getInstance().execute(getInstance());
		}
	}

	public static void removeQueue() {
		if (instance != null) {
			instance.stopRunning();
		}
	}
}