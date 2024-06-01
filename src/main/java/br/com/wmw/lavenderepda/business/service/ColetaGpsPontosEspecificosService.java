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
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import totalcross.io.IOException;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.event.TimerEvent;
import totalcross.util.Vector;

public class ColetaGpsPontosEspecificosService extends AbstractTimerService {
	
	private static final int NUMAXTTIMEOUTDEFAULT = 600;

	private Vector pontoGpsList;
	private static ColetaGpsPontosEspecificosService instance;
	private boolean wasGpsOff;
	private int nuMaxTempoColeta;
	private int timeStampInicial;

	protected ColetaGpsPontosEspecificosService() {
		super(MainLavenderePda.getInstance(), LavenderePdaConfig.nuIntervaloErroColetaGpsPontosEspecificosSistema);
		pontoGpsList = new Vector();
		BaseUIForm.useAlert = true;
		nuMaxTempoColeta = getNuMaxTempoColeta(); 
	}

	private int getNuMaxTempoColeta() {
		if (!LavenderePdaConfig.usaIntervaloErroColetaGpsPontosEspecificosSistema()) {
			return 0;
		}
		if (LavenderePdaConfig.usaIntervaloErroColetaGpsPontosEspecificosSistema() && LavenderePdaConfig.usaMaxTempoErroColetaGpsPontosEspecificosSistema()) {
			return LavenderePdaConfig.nuMaxTempoErroColetaGpsPontosEspecificosSistema;
		}
		return NUMAXTTIMEOUTDEFAULT;
	}

	public static ColetaGpsPontosEspecificosService getInstance() {
		if (instance == null) {
			instance = new ColetaGpsPontosEspecificosService();
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
		timeStampInicial = Vm.getTimeStamp();
		GpsService.getInstance().start();
	}

	protected void afterStop() throws SQLException {
		if (LavenderePdaConfig.isColetaDadosGpsRepresentante()) {
			ColetaGpsTimerService.getInstance().start();
		}
	}

	protected void onTimerTriggered(TimerEvent arg0) {
		try {
			GpsData gpsData = GpsService.getInstance().readData();
			if (gpsData.isGpsOff() != SessionLavenderePda.isGpsOff) {
				LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_STATUS_GPS, gpsData.isGpsOff() ? Messages.LOG_STATUS_GPS_OFF : Messages.LOG_STATUS_GPS_ON);
				SessionLavenderePda.isGpsOff = gpsData.isGpsOff();
			}
			if ((VmUtil.isAndroid() || VmUtil.isIOS())) {
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
				putPontoGpsInCache(PontoGpsService.getInstance().getPontoGpsByGps(gpsData));
			}
			if (isRunning() && (gpsData.isValid() || isTimeOut(Vm.getTimeStamp()))) {
				PontoGpsService.getInstance().saveTxtAndSend2Web(pontoGpsList);
				stop();
			} 
			resetGps();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private void resetGps() {
		GpsService.getInstance().reset();
	}
	
	public void gravaEmTxt() {
		PontoGpsService.getInstance().gravaEmTxt(pontoGpsList);
	}

	private boolean isTimeOut(int timeStampFinal) {
		return ((timeStampFinal - timeStampInicial) >= ValueUtil.toMillisecs(ValueUtil.SECONDS, nuMaxTempoColeta)); 
	}

	private void putPontoGpsInCache(PontoGps pontoGps) {
		pontoGpsList.addElement(pontoGps);
	}

	public Vector getPontoGpsList() {
		return pontoGpsList;
	}
	
	public void clearCache() {
		pause();
		pontoGpsList = new Vector();
	}
}
