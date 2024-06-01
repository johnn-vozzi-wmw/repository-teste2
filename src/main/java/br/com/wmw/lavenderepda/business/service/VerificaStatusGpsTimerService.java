package br.com.wmw.lavenderepda.business.service;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.timer.AbstractTimerService;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.LogPda;
import br.com.wmw.lavenderepda.presentation.ui.MainLavenderePda;
import totalcross.ui.event.TimerEvent;

public class VerificaStatusGpsTimerService extends AbstractTimerService {

	private static VerificaStatusGpsTimerService instance;
	private boolean wasGpsOff;

	protected VerificaStatusGpsTimerService() {
		super(MainLavenderePda.getInstance(), LavenderePdaConfig.nuIntervaloColetaGpsRepresentante);
		BaseUIForm.useAlert = true;
	}

	public static VerificaStatusGpsTimerService getInstance() {
		if (instance == null) {
			instance = new VerificaStatusGpsTimerService();
		}
		return instance;
	}
	
	public static void invalidateInstance() {
		instance = null;
	}
	
	@Override
	protected void afterStop() throws SQLException {
		SessionLavenderePda.isGpsOff = false;
		SessionLavenderePda.isSistemaLiberadoSenhaGpsOff = false;
		BaseUIForm.clearMsgAlerta();
	}

	protected void onTimerTriggered(TimerEvent arg0) {
		try {
			boolean isGpsOff = StatusGpsService.getInstance().isGpsOff();
			if (isGpsOff != SessionLavenderePda.isGpsOff) {
				LogPdaService.getInstance().info(LogPda.LOG_CATEGORIA_STATUS_GPS, isGpsOff ? Messages.LOG_STATUS_GPS_OFF : Messages.LOG_STATUS_GPS_ON);
				SessionLavenderePda.isGpsOff = isGpsOff;
			}
			if (StatusGpsService.getInstance().isPlataformaSuportadaGps()) {
				if (isGpsOff) {
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
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	
}
