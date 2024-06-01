package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.GpsException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.integration.dao.CrudDao;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.SyncComponent;
import br.com.wmw.framework.sync.data.tc2web.BaseSyncTc2Web;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.WtoolsUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.ParametrosWGPS;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PontoGpsDbxDao;
import br.com.wmw.lavenderepda.sync.async.EnviaPontoGpsRunnable;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.device.gps.GPS;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.sys.Time;
import totalcross.sys.Vm;
import totalcross.util.Vector;

public class PontoGpsService extends CrudService {

	public static final String NOME_PACKAGE_WGPS = "br.com.wmw.wgps";
	public static final String NOME_APLICATION_WGPS = "MainWGps";
	
	
	public static final String PONTO_GPS_DIR = "pontoGps";
	
	private static PontoGpsService instance;
	private String dirFiles = Convert.appendPath(Settings.dataPath, PONTO_GPS_DIR);


	private PontoGpsService() {
		//--
	}

	public static PontoGpsService getInstance() {
		if (instance == null) {
			instance = new PontoGpsService();
		}
		return instance;
	}

	public void validate(BaseDomain domain) throws java.sql.SQLException {
	}

	//@Override
	protected CrudDao getCrudDao() {
		return PontoGpsDbxDao.getInstance();
	}


	public boolean startColetaGpsSistema(boolean coletaManual) {
		boolean isGpsLigado = false;
		if (LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
			isGpsLigado = StatusGpsService.getInstance().isGpsOn();
			if (!coletaManual || isGpsLigado) {
				if(!acionaAppExternoParaColetaGps(true)) {
					return false;
				}
				VerificaStatusGpsTimerService.getInstance().start();
				return true;
			} else if(!acionaAppExternoParaColetaGps(false)) {
				return false;
			}
		} else if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
			isGpsLigado = verificaGpsLigado();
			if (!coletaManual || isGpsLigado) {
				ColetaGpsTimerService.getInstance().start();
				return true;
			}
		}
		validaGpsColetaManual(coletaManual, isGpsLigado);
		return false;
	}

	private void validaGpsColetaManual(boolean coletaManual, boolean isGpsLigado) {
		if (!coletaManual) return;
		if (!isGpsLigado) {
			GpsService.getInstance().stop();
			UiUtil.showWarnMessage(Messages.INICIAR_COLETA_GPS_MANUAL_ERRO);
		} else {
			UiUtil.showWarnMessage(Messages.INICIAR_COLETA_GPS_MANUAL_ERRO_GERAL);
		}
	}

	public boolean verificaGpsLigado() {
		try {
			GpsData gpsData;
			GpsService.getInstance().start();
			Vm.sleep(3000);
			gpsData = GpsService.getInstance().readData();
			return !gpsData.isGpsOff();
		} catch (GpsException e) {
			ExceptionUtil.handle(e);
		}
		return false;
	}

	public void startColetaGpsPontoEspecificoSistema() {
		if (LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema()) {
			ColetaGpsPontosEspecificosService.getInstance().start();
		}
	}
	
	
	public void stopColetaGpsSistema() {
		if (LavenderePdaConfig.isUsaColetaGpsAppExterno()) {
			acionaAppExternoParaColetaGps(false);
			VerificaStatusGpsTimerService.getInstance().stop();
			VerificaStatusGpsTimerService.invalidateInstance();
		} else if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
			ColetaGpsTimerService.getInstance().gravaEmTxt();
			ColetaGpsTimerService.getInstance().stop();
			ColetaGpsTimerService.invalidateInstance();
		}
		EnviaPontoGpsRunnable.removeQueue();
	}
	
	public void stopColetaGpsPontoEspecificoSistema() {
		if (LavenderePdaConfig.isUsaColetaGpsPontosEspecificosSistema()) {
			ColetaGpsPontosEspecificosService.getInstance().gravaEmTxt();
			ColetaGpsPontosEspecificosService.getInstance().stop();
			ColetaGpsPontosEspecificosService.invalidateInstance();
		}
	}

	public String[] getFiles() {
		File directory = null;
		try {
			directory = new File(dirFiles);
			if (directory.exists() && directory.isDir()) {
				return directory.listFiles();
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		} finally {
			if (directory  != null) {
				FileUtil.closeFile(directory);
			}
		}
		return null;
	}

	public Vector getPontoGpsListFromFile(String file) throws IOException {
		Vector pontoGpsList = new Vector();
		if (ValueUtil.isNotEmpty(file)) {
			String pathFile = Convert.appendPath(dirFiles, file);
			readFile(pathFile, pontoGpsList);
		}
		return pontoGpsList;
	}

	private void readFile(String pathFile, Vector pontoGpsList) throws IOException {
		int size;
		File file = null;
		try {
			file = FileUtil.openFile(pathFile, File.READ_ONLY);
			size = file.getSize();
			byte[] buffer = new byte[size];
			file.readBytes(buffer, 0, size);
			//--
			String dados = new String(buffer);
			String[] lines = StringUtil.split(dados, '|');
			for (int i = 0; i < lines.length; i++) {
				String[] line = StringUtil.split(lines[i], ';');
				int qtValues = 12;
				if (line.length == qtValues) {
					pontoGpsList.addElement(line);
				}
			}
		} finally {
			FileUtil.closeFile(file);
		}
	}

	public void deletePontoGpsFile(String file) {
		FileUtil.deleteFile(Convert.appendPath(dirFiles, file));
	}

	public String addSelectColumns() {
		StringBuffer insertValues = new StringBuffer();
		insertValues.append("cdEmpresa");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" cdRepresentante");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" dtColeta");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" hrColeta");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" vlLatitude");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" vlLongitude");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" vlVelocidade");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" vlPrecisao");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" flStatus");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" flTipoAlteracao");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" cdUsuario");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" nuCarimbo");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" dtAlteracao");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" hrAlteracao");
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		return insertValues.toString();
	}


	public String addInsertValues(String[] rastreabilidade) throws IOException {
		StringBuffer insertValues = new StringBuffer();
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[0]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[1]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[2]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[3]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[4]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[5]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[6]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[7]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[8]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[9]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(rastreabilidade[10]));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(StringUtil.getStringValue(0)));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(DateUtil.getCurrentDate()));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		insertValues.append(BaseSyncTc2Web.getValueFormattedToSync(TimeUtil.getCurrentTimeHHMMSS()));
		insertValues.append(SyncComponent.SEPARADOR_ATUALIZACOES);
		insertValues.append(" ");
		return insertValues.toString();
	}

	public PontoGps getPontoGpsByGps(final GpsData gpsData) {
		PontoGps pontoGps = new PontoGps();
		pontoGps.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pontoGps.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		pontoGps.dtColeta = gpsData.data;
		pontoGps.hrColeta = gpsData.hora;
		pontoGps.vlLatitude = !gpsData.isInvalid() ? gpsData.latitude : -1;
		pontoGps.vlLongitude = !gpsData.isInvalid() ? gpsData.longitude : -1;
		pontoGps.vlVelocidade = !gpsData.isInvalid() && isValidVelocityValue(gpsData.velocidade) ? gpsData.velocidade : -1;
		pontoGps.vlPrecisao = gpsData.pdop;
		pontoGps.cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		pontoGps.flStatus = gpsData.status;
		pontoGps.flTipoAlteracao = PontoGps.FLTIPOALTERACAO_INSERIDO;
		return pontoGps;
	}

	private boolean isValidVelocityValue(double value) {
		return GPS.INVALID != value;
	}
	

	public void saveTxtAndSend2Web(Vector pontoGpsList) throws SQLException {
		synchronized (EnviaPontoGpsRunnable.getInstance().lock) {
			gravaEmTxt(pontoGpsList);
		}
	}

	public void gravaEmTxt(Vector pontoGpsList) {
		File fileDest = null;
		try {
			if (ValueUtil.isNotEmpty(pontoGpsList)) {
				fileDest = FileUtil.criaFile(getFileName());
				for (int i = 0; i < pontoGpsList.size(); i++) {
					fileDest.writeBytes(((PontoGps)pontoGpsList.items[i]).toString() + ";\r\n|");
				}
			}
			pontoGpsList.removeAllElements();
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		} finally {
			if (fileDest != null) {
				FileUtil.closeFile(fileDest);
			}
		}
	}

	private String getFileName() {
		return Convert.appendPath(dirFiles, "ponto" + TimeUtil.getCurrentDateTimeYYYYMMDDHHMMSS() + ".txt");
	}
	
	public boolean isNecessarioInserirGpsdata(GpsData gpsData) {
		return gpsData.isValid() || LavenderePdaConfig.usaAuditoriaColetaGps;
	}
	
	public boolean acionaAppExternoParaColetaGps(boolean isStart) {
		createDirFilesPontoGps(); 
		try {
			String jsonParams = gpsParamsToJson(isStart);
			SessionLavenderePda.isWGPSRunning = isStart;
			if (isStart) {
				WtoolsUtil.startWgps(jsonParams);
			} else {
				WtoolsUtil.stopWgps(jsonParams);
			}
			Vm.sleep(3000);
			return StatusGpsService.getInstance().isWGPSInstalledAndRunning();
		} catch (Throwable ex) {
			LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao acionar app externo: " + ex.getMessage());
			ExceptionUtil.handle(ex);
			return false;
		} 
	}

	private String gpsParamsToJson(boolean isStart) {
		String cdEmpresa = SessionLavenderePda.cdEmpresa;
		String cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		int nuIntervaloColetaGpsRepresentante = LavenderePdaConfig.nuIntervaloColetaGpsRepresentante;
		int nuIntervaloEnvioPontoGpsBackground = LavenderePdaConfig.nuIntervaloEnvioPontoGpsBackground;
		String cdUsuario = SessionLavenderePda.usuarioPdaRep.cdUsuario;
		String dir = Convert.appendPath(Settings.dataPath, PONTO_GPS_DIR);
		String horarioLimiteColetaGpsManual = LavenderePdaConfig.isUsaHorarioLimiteColetaGpsManual() ? LavenderePdaConfig.getHoraLimiteColetaGpsManual() : "N";
		return generateJsonWgpsParams(isStart, cdEmpresa, cdRepresentante, nuIntervaloColetaGpsRepresentante, nuIntervaloEnvioPontoGpsBackground, cdUsuario, dir, horarioLimiteColetaGpsManual);
	}

	private void createDirFilesPontoGps() {
		try {
			FileUtil.createDirIfNecessary(dirFiles);
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}
	
	private String generateJsonWgpsParams(boolean isStart, String cdEmpresa, String cdRepresentante, int nuIntervaloColetaGpsRepresentante, int nuIntervaloEnvioPontoGpsBackground, String cdUsuario, String dir, String horarioLimiteColetaGpsManual) {
		return new ParametrosWGPS(isStart, cdEmpresa, cdRepresentante, nuIntervaloColetaGpsRepresentante, nuIntervaloEnvioPontoGpsBackground, cdUsuario, dir, horarioLimiteColetaGpsManual).getAsJson().toString();
	}

	public void initializeWgps() {
		WtoolsUtil.startWgps(gpsParamsToJson(true));
	}

	public void keepWGPSRunning() throws Exception {
		if (SessionLavenderePda.isWGPSRunning) {
			try {
				Time timeWgps = StatusGpsService.getInstance().getLastTimeStatusWGps();
				int intervalo = TimeUtil.getSecondsRealBetween(new Time(), timeWgps);
				if (intervalo > 45) {
					LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Wgps ficou inativo por " + intervalo + " segundos. Reinicializando.");
					initializeWgps();
				}
			} catch (Throwable e) {
				LogPdaService.getInstance().error(LogPda.LOG_CATEGORIA_ERRO_COMUNICACAO_WGPS, "Erro ao verificar arquivo de status do Wgps. Reinicializando. Detalhes: " + e.getMessage());
				initializeWgps();
			}
		}
	}

	public Vector getLogFiles(File file, boolean currentDay) throws IOException {
		Vector logList = new Vector();
		String[] files = file.listFiles();
		String fileNameFilter = "_" + DateUtil.getCurrentDateYYYYMMDD();
		for (String fileName : files) {
			if (fileName.endsWith(ReorganizarDadosService.DS_EXTENSAO_LOG) && (!currentDay || fileName.contains(fileNameFilter))) {
				logList.addElement(fileName);
			}
		}
		return logList;
	}
	
	public double calculaDistancia(final double latitudeInicial, final double longitudeInicial, final double latitudeFinal, final double longitudeFinal) {
		double cdLatitudeInicial = degToRad(latitudeInicial);
		double cdLongitudeInicial = degToRad(longitudeInicial);
		double cdLatitudeFinal = degToRad(latitudeFinal);
		double cdLongitudeFinal = degToRad(longitudeFinal);
		double earthRadius = 6371;
		double deltaLat = cdLatitudeFinal - cdLatitudeInicial;
		double deltaLong = cdLongitudeFinal - cdLongitudeInicial;
		double a = (Math.sin(deltaLat / 2.0) * Math.sin(deltaLat / 2.0)) + (Math.cos(cdLatitudeInicial) * Math.cos(cdLatitudeFinal) * Math.sin(deltaLong / 2.0) * Math.sin(deltaLong / 2.0));
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = earthRadius * c;

		return distance;
	}

	private double degToRad(final double degrees) {
		if (degrees != 0) {
			return degrees * (Math.PI / 180);
		} else {
			return 0d;
		}
	}

}