package br.com.wmw.lavenderepda.business.service;

import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.timer.AbstractTimerService;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import totalcross.ui.event.TimerEvent;

public class ForceColetaGpsService extends AbstractTimerService {

	private static ForceColetaGpsService instance;

	protected ForceColetaGpsService() {
		super(MainLavenderePda.getInstance(), 1);
	}

	public static ForceColetaGpsService getInstance() {
		if (instance == null) {
			instance = new ForceColetaGpsService();
		}
		return instance;
	}
	

	protected void onTimerTriggered(TimerEvent event) {
		try {
			stop();
			ColetaGpsTimerService.getInstance().putPontoGpsInCache(PontoGpsService.getInstance().getPontoGpsByGps(GpsService.getInstance().forceReadData()));
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
}
