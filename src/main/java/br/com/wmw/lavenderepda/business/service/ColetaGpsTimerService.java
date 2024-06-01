package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.timer.AbstractTimerService;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import totalcross.io.IOException;
import totalcross.sys.Settings;
import totalcross.sys.Time;
import totalcross.ui.event.TimerEvent;
import totalcross.util.Vector;

public class ColetaGpsTimerService extends AbstractTimerService {

	private Vector pontoGpsList;
	private static ColetaGpsTimerService instance;
	private long lastSending;
	private boolean wasGpsOff;
	private int nuIntervaloGravacaoAndEnvioPontoGps;

	protected ColetaGpsTimerService() {
		super(MainLavenderePda.getInstance(), LavenderePdaConfig.nuIntervaloColetaGpsRepresentante);
		pontoGpsList = new Vector();
		BaseUIForm.useAlert = true;
	}

	public static ColetaGpsTimerService getInstance() {
		if (instance == null) {
			instance = new ColetaGpsTimerService();
			try {
				FileUtil.createDirIfNecessary(VmUtil.isWin32() ? "pontoGps/" : Settings.dataPath + "/pontoGps/");
			} catch (IOException e) {
				throw new ApplicationException(MessageUtil.getMessage(Messages.GPS_ERRO_CRIACAO_DIRETORIO, e.getMessage()));
			}
		}
		return instance;
	}
	
	public static void invalidateInstance() {
		instance = null;
	}

	protected void afterStart() {
		lastSending = TimeUtil.getTimeAsLong();
		GpsService.getInstance().start();
	}

	protected void afterStop() throws SQLException {
		GpsService.getInstance().stop();
	}

	protected void onTimerTriggered(TimerEvent arg0) {
		nuIntervaloGravacaoAndEnvioPontoGps = LavenderePdaConfig.nuIntervaloEnvioPontoGpsBackground;
		nuIntervaloGravacaoAndEnvioPontoGps = nuIntervaloGravacaoAndEnvioPontoGps != 0 ? nuIntervaloGravacaoAndEnvioPontoGps : 300 + (LavenderePdaConfig.nuIntervaloColetaGpsRepresentante * 10);
		try {
			GpsData gpsData = GpsService.getInstance().readData();
			if (gpsData.isGpsOff() != SessionLavenderePda.isGpsOff) {
				LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_STATUS_GPS, gpsData.isGpsOff() ? Messages.LOG_STATUS_GPS_OFF : Messages.LOG_STATUS_GPS_ON);
				SessionLavenderePda.isGpsOff = gpsData.isGpsOff();
			}
			if (StatusGpsService.getInstance().isPlataformaSuportadaGps()) {
				if (gpsData.isGpsOff()) {
					wasGpsOff = true;
					if (!SessionLavenderePda.isSistemaLiberadoSenhaGpsOff) {
						BaseUIForm.addMsgAlerta(Messages.MSG_GPS_NAO_HABILITADO);
						BaseContainer actualForm = MainLavenderePda.getInstance().getActualForm();
						if (actualForm instanceof BaseUIForm) {
							((BaseUIForm) actualForm).updateVisibilityBtAlert();
						}
					}
				} else {
					if (wasGpsOff) {
						BaseUIForm.clearMsgAlerta();
						wasGpsOff = false;
					}
				}
			}
			if (PontoGpsService.getInstance().isNecessarioInserirGpsdata(gpsData)) {
				pontoGpsList.addElement(PontoGpsService.getInstance().getPontoGpsByGps(gpsData));
			}
			long diffIntervaloEnvio = TimeUtil.getSecondsRealBetween(TimeUtil.getCurrentTime(), new Time(lastSending));
			if (isRunning() && diffIntervaloEnvio >= nuIntervaloGravacaoAndEnvioPontoGps) {
				PontoGpsService.getInstance().saveTxtAndSend2Web(pontoGpsList);
				lastSending = TimeUtil.getTimeAsLong();
			}
			resetGps();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public void resetGps() {
			GpsService.getInstance().reset();
		}

	public void gravaEmTxt() {
		PontoGpsService.getInstance().gravaEmTxt(pontoGpsList);
	}

	public Vector getPontoGpsList() {
		return pontoGpsList;
	}
	
	public void clearCache() {
		pontoGpsList = new Vector();
	}
	
    public void putPontoGpsInCache(PontoGps pontoGps) {
		ColetaGpsTimerService.getInstance().getPontoGpsList().addElement(pontoGps);
	}
}
