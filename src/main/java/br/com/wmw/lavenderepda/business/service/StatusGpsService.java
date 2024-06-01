
package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.service.BaseService;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.GpsUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.WtoolsUtil;
import br.com.wmw.lavenderepda.LavendereConfig;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.device.gps.GPS;
import totalcross.io.device.gps.GPSDisabledException;
import totalcross.json.JSONObject;
import totalcross.sys.Time;
import totalcross.sys.Vm;

/**
 * 
 * Classe Responsavel pelo Status Atual do GPS na Aplicação
 * @author Raphael Santos Custódio [18/05/2018]
 * @category Status GPS
 * @see {@link GpsData}
 * @see {@link GpsService}
 * @see {@link GpsUtil}
 * @see {@link ProjectWGPS}
 */
public class StatusGpsService extends BaseService {
	
	
	public static GPS WgpsInstance = null;
	
	// Ultimo Status Obtido pelo WGPS, true = ligado, false = desligado
	public static boolean lastStateWGps;

	// Instância do service
	private static StatusGpsService instance;

	/**
	 * Construtor do Status Gps Service.
	 */
	private StatusGpsService() {
		//--
	}

	/**
	 * Method: getInstance()
	 * Deve ter apenas uma unica instância na aplicação
	 * @return Instância do StatusGpsService, Caso não exista, será instânciado a mesma.
	 */
	public static StatusGpsService getInstance() {
		if (instance == null) {
			instance = new StatusGpsService();
		}
		return instance;
	}

	/**
	 * Method: isGpsOn()
	 * @return Retorna se o GPS está ligado ou não<br>
	 * O metodo se baseia no parâmetro <b>1169-usaAplicativoExternoParaColetaGps</b> da aplicação...<br>
	 * Caso esse parâmetro esteja desligado o método irá retornar <b><code>!SessionLavenderePda.isGpsOff</code></b><br>
	 * Caso esse parametro esteja ligado o método irá se basear no <b>arquivo</b> gerado pelo <b>WGPS</b> para determinar o <b>STATUS DO GPS</b>
	 */
	public boolean isGpsOn() {
		if (!LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
			return lastStateWGps = !SessionLavenderePda.isGpsOff;
		}
		try {
			GPS gps = getGPS();
			lastStateWGps = true;
			gps.stop();
		} catch (GPSDisabledException gpsDEx) {
			lastStateWGps = false;
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
			lastStateWGps = false;
		}
		SessionLavenderePda.isGpsOff = !lastStateWGps;
		return lastStateWGps;
	}

	private GPS getGPS() throws IOException {
		stopGPSTotalCross();
		if (WgpsInstance != null) {
			GpsUtil.stopGps(WgpsInstance);
		}
		WgpsInstance = new GPS();
		return WgpsInstance;
	}

	private void stopGPSTotalCross() {
		GpsService.getInstance().stop();
	}

	/**
	 * Method: isGpsOff()
	 * @return Retorna se o GPS está desligado ou não<br>
	 * O metódo se baseia no parâmetro <b>1169-usaAplicativoExternoParaColetaGps</b> da aplicação...<br>
	 * Caso esse parâmetro esteja desligado o método irá retornar <b><code>SessionLavenderePda.isGpsOff</code></b><br>
	 * Caso esse parametro esteja ligado o método irá se basear no <b>arquivo</b> gerado pelo <b>WGPS</b> para determinar o <b>STATUS DO GPS</b>
	 */
	public boolean isGpsOff() {
		return !isGpsOn();
	}
	
	/**
	 * Method: isFakeGpsOn
	 * @return Retorna se o fake gps está ligado ou não, suporte apenas se o 1169-usaAplicativoExternoParaColetaGps estiver ligado
	 * O método se baseia no arquivo isMockGps gerado pelo WGPS quando o mesmo detecta o uso do fakeGps...
	 */
	public boolean isFakeGpsOn() {
		return (!Session.isModoSuporte && LavenderePdaConfig.isUsaColetaGpsAppExterno()) ? FileUtil.exists(WtoolsUtil.getPathMockGps()) : false;
	}

	/**
	 * Method: isWGPSActiveInDevice
	 * @return Retorna se o WGPS está ativo ou não. suporte apenas se o 1169-usaAplicativoExternoParaColetaGps estiver ligado.<br>
	 * O método se baseia no arquivo statuswgps gerado pelo WGPS onde o mesmo possui uma estrutura JSON com os seguintes elementos: <br>
	 * {"isStarted": true | false, "lastTimeStatus":"20180723142644"}
	 */
	public boolean isWGPSActiveOnDevice() {
		if (Session.isModoSuporte || !LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
			return true;
		}
		try {
			Time timeWgps = getLastTimeStatusWGps();
			Time timeActual = new Time();
			return TimeUtil.getSecondsRealBetween(timeActual, timeWgps) < 15;
		} catch (Throwable iEx) {
			ExceptionUtil.handle(iEx);
			return false;
		}
	}
	
	public Time getLastTimeStatusWGps() throws Exception {
		File file = null;
		try {
			file = new File(WtoolsUtil.getPathStatusWgps(), File.READ_ONLY);
			if (! file.exists()) {
				throw new IOException("Arquivo não existe!");
			}
			JSONObject jsonObject = new JSONObject(new String(file.read()));
			Time timeWgps = new Time(Long.parseLong(jsonObject.getString("lastTimeStatus")));
			return timeWgps;
		} finally {
			FileUtil.closeFile(file);
		}
	}

	private void geraLogWGPSInativo() {
		LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, Messages.LOG_WGPS_INATIVO_APARELHO);
	}
	
	/**
	 * Method: verificaGpsLigado(menuClick, isMenu)<br>
	 * <b>O metodo ja apresenta a popup de liberação ou bloqueio por senha.</b>
	 * @param menuClick (Verifica se veio de um menu ou não, só funciona se o isMenu for true)
	 * @param isMenu (Verifica se a ação do bloqueio do sistema veio do menu)
	 * @return Retorna true ou false se o sistema está liberado para uso com base nos parametros do gps
	 */
	public boolean verificaGpsLigado(boolean menuClick, boolean isMenu) throws SQLException {
		return isGpsFuncaoAtualInativo(menuClick, isMenu);
	}

	// Verifica se o gps está inativo com base na função recebida
	private boolean isGpsFuncaoAtualInativo(boolean menuClick, boolean isMenu) {
		return isMenu ? isGpsInativoValidacaoMenu(menuClick) : isGpsInativo();
	}

	// Verificação auxiliar do status gps para bloqueio ou liberação do sistema
	private boolean isGpsInativo() {
		return isSistemaNaoLiberadoSenhaGps() && isBloqueiaGps();
	}

	private boolean isGpsInativoValidacaoMenu(boolean menuClick) {
		return isSistemaNaoLiberadoSenhaGps() && (isBloqueiaGps() || (isColetaAndamentoOrBloqueiaColetaManual() && menuClick));
	}

	private boolean isColetaAndamentoOrBloqueiaColetaManual() {
		return !SessionLavenderePda.isColetaGpsEmAndamento() && LavenderePdaConfig.bloqueiaSistemaColetaGpsManualDesligada;
	}
	
	private boolean isBloqueiaGps() {
		if (LavenderePdaConfig.bloqueiaSistemaGpsInativo) {
			if (isGpsOff()) {
				return true;
			}
			if (!isWGPSInstalledAndRunning()) {
				geraLogWGPSInativo();
				return true;
			}
		}
		return false;
	}
	
	private boolean isSistemaNaoLiberadoSenhaGps() {
		return isPlataformaSuportadaGps() && !SessionLavenderePda.isSistemaLiberadoSenhaGpsOff;
	}
	
	/**
	 * Method: isPlataformaSuportadaGps() - <b>(Android ou IOS)</b>
	 * @return retorna se a plataforma é suportada pelo gps
	 */
	public boolean isPlataformaSuportadaGps() {
		return VmUtil.isAndroid() || VmUtil.isIOS();
	}
	
	public String getMessageBloqueioFuncaoAtual(boolean isMenu) {
		if (isMenu && isColetaAndamentoOrBloqueiaColetaManual()) {
			return Messages.SENHADINAMICA_TITULO_BLOQUEIO_GPS_MANUAL_DESLIGADO;
		}
		return LavenderePdaConfig.isUsaColetaGpsAppExterno() ? Messages.SENHADINAMICA_TITULO_BLOQUEIO_GPS_WGPS_INATIVO : Messages.SENHADINAMICA_TITULO_BLOQUEIO_GPS_INATIVO;
	}
	
	public boolean isWGPSInstalledAndRunning() {
		if (isWGPSActiveOnDevice()) {
			return true;
		}
		UiUtil.showInfoMessage(Messages.INICIAR_COLETA_GPS_MANUAL_ERRO_TENTE_NOVAMENTE);
		LavendereConfig.getInstance().forceCheckWtoolsVersion();
		PontoGpsService.getInstance().initializeWgps();
		Vm.sleep(3000);
		if (isWGPSActiveOnDevice()) {
			UiUtil.showSucessMessage(Messages.INICIAR_COLETA_GPS_TENTE_NOVAMENTE_OK);
			return true;
		}
		LogPdaService.getInstance().warn(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, Messages.INICIAR_COLETA_GPS_MANUAL_ERRO_WGPS);
		UiUtil.showWarnMessage(Messages.INICIAR_COLETA_GPS_MANUAL_ERRO_WGPS);
		logPeriodoInatividade();
		return false;
	}

	private void logPeriodoInatividade() {
		try {
			int intervalo = TimeUtil.getSecondsRealBetween(new Time(), getLastTimeStatusWGps());
			if (intervalo > 600) {
				LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Wgps esteve inativo por " + intervalo + " segundos.");
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

}