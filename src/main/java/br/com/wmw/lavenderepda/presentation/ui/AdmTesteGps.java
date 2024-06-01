package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ExpandableContainer;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.GpsUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PontoGps;
import br.com.wmw.lavenderepda.business.service.ColetaGpsTimerService;
import br.com.wmw.lavenderepda.business.service.PontoGpsService;
import totalcross.io.device.gps.GPS;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class AdmTesteGps extends BaseUIForm {

	private static int TABPANEL_PONTOS_COLETADOS = 0;
	private static int TABPANEL_TESTE_GPS = 1;

	private BaseButton btStartStopGps;
	private GPS gps;
	private ScrollTabbedContainer tabs;
	protected BaseGridEdit baseGrid;
	public boolean isGpsStarted;
	private LabelName lbLat;
	private LabelName lbLong;
	private LabelName lbVel;
	private LabelValue lvLat;
	private LabelValue lvLong;
	private LabelValue lvVel;
	ExpandableContainer exp;

	public AdmTesteGps() {
		super(Messages.GPS_TITLE);
		tabs = new ScrollTabbedContainer(new String[]{Messages.GPS_PONTOS_COLETADOS, Messages.GPS_TESTAR_GPS});
		btStartStopGps = new BaseButton(Messages.GPS_BT_INICIAR);
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.GPS_DATA, -35, LEFT),
			new GridColDefinition(Messages.GPS_HORA, -20, RIGHT),
			new GridColDefinition(Messages.GPS_LATITUDE, -40, RIGHT),
			new GridColDefinition(Messages.GPS_LONGITUDE, -40, RIGHT) };
		baseGrid = UiUtil.createGridEdit(gridColDefiniton);
		baseGrid.liveScrolling = true;
		baseGrid.enableColumnResize = false;
		lbLat = new LabelName("Lat");
		lbLong = new LabelName("Long");
		lbVel = new LabelName("Vel");
		lvLat = new LabelValue();
		lvLong = new LabelValue();
		lvVel = new LabelValue();
	}

	protected void onFormStart() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(false);
		UiUtil.add(this, tabs, getTop() + HEIGHT_GAP, getBottomTab());
		UiUtil.add(getFirstContainer(), baseGrid, LEFT, TOP, FILL, FILL);
		UiUtil.add(getSecondContainer(), btStartStopGps, CENTER, AFTER + HEIGHT_GAP);
		exp = new ExpandableContainer("Gps data");
		UiUtil.add(getSecondContainer(), exp, LEFT, AFTER + HEIGHT_GAP, FILL);
		exp.addComponentInMaximizedContainer(lbLat);
		exp.addComponentInMaximizedContainer(lvLat);
		exp.addComponentInMaximizedContainer(lbLong);
		exp.addComponentInMaximizedContainer(lvLong);
		exp.addComponentInMaximizedContainer(lbVel);
		exp.addComponentInMaximizedContainer(lvVel);
//		UiUtil.add(getSecondContainer(), gpsCont = new Container(), LEFT, AFTER, FILL, UiUtil.getLabelPreferredHeight() * 6);
		carregaGridPontoGpsColetados();
	}


	private void carregaGridPontoGpsColetados() {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int size = 0;
		Vector domainList = new Vector(0);
		try {
			baseGrid.setItems(null);
			domainList = ColetaGpsTimerService.getInstance().getPontoGpsList();
			size = domainList.size();
			if (size > 0) {
				for (int i = 0; i < size; i++) {
					PontoGps pontoGps = (PontoGps) domainList.items[i];
					String[] item = {"", StringUtil.getStringValue(pontoGps.dtColeta), StringUtil.getStringValue(pontoGps.hrColeta), StringUtil.getStringValue(pontoGps.vlLatitude, 7), StringUtil.getStringValue(pontoGps.vlLongitude, 7)};
					baseGrid.add(item);
				}
			}
		} finally {
			domainList = null;
			msg.unpop();
		}
	}

	private Container getFirstContainer() {
		return tabs.getContainer(TABPANEL_PONTOS_COLETADOS);
	}

	private BaseScrollContainer getSecondContainer() {
		return (BaseScrollContainer)tabs.getContainer(TABPANEL_TESTE_GPS);
	}

	protected void onFormEvent(Event e) throws SQLException {
		switch (e.type) {
			case ControlEvent.PRESSED: {
				if (e.type == ControlEvent.PRESSED) {
					if (e.target == btStartStopGps) {
						try {
							UiUtil.showProcessingMessage((btStartStopGps.getText().equals(Messages.GPS_BT_INICIAR)) ? Messages.TEST_GPS_INICIANDO_GPS : Messages.TEST_GPS_PARANDO_GPS);
							startStopGps();
						} catch (Throwable ex) {
							ExceptionUtil.handle(ex);
						} finally {
							UiUtil.unpopProcessingMessage();
						}
					} else if (e.target == tabs && tabs.getActiveTab() == TABPANEL_PONTOS_COLETADOS) {
						carregaGridPontoGpsColetados();
					}
				}
				break;
			}
		}
	}


	private void startStopGps() {
		if (!isGpsStarted) {
			if (!LavenderePdaConfig.isUsaColetaGpsManual() || (LavenderePdaConfig.isUsaColetaGpsManual() && SessionLavenderePda.isColetaGpsEmAndamento())) {
				PontoGpsService.getInstance().stopColetaGpsSistema();
				PontoGpsService.getInstance().stopColetaGpsPontoEspecificoSistema();
			}
			try {
				btStartStopGps.setText(Messages.GPS_BT_PARAR);
				if (gps == null) {
					gps = GpsUtil.getGps();
				}
				isGpsStarted = true;
				if (gps != null) {
					GpsUtil.retriaveData(gps);
					lvLat.setText(""+Double.valueOf(gps.location[0]));
					lvLong.setText(""+Double.valueOf(gps.location[1]));
					lvVel.setText(""+Double.valueOf(gps.velocity));
				}
				if (!exp.isExpanded()) {
					postExpEvent();
				}
				reposition();
				
			} catch (Throwable e) {
				isGpsStarted = false;
				UiUtil.showErrorMessage(Messages.GPS_ERRO_COLETA);
			}
		} else {
			try {
				isGpsStarted = false;
				if (gps != null) {
					gps.stop();
				}
			} catch (Throwable e) {
				UiUtil.showErrorMessage(Messages.GPS_ERRO_PARAR_COLETA);
			}
			if (!LavenderePdaConfig.isUsaColetaGpsManual() || (LavenderePdaConfig.isUsaColetaGpsManual() && SessionLavenderePda.isColetaGpsEmAndamento())) {
				PontoGpsService.getInstance().startColetaGpsSistema(false);
			}
			btStartStopGps.setText(Messages.GPS_BT_INICIAR);
			if (exp.isExpanded()) {
				postExpEvent();
			}
			reposition();
		}
	}

	private void postExpEvent() {
		PenEvent event = new PenEvent();
		event.target = exp.ctMinimizedContainers;
		event.type = PenEvent.PEN_UP;
		exp.postEvent(event);
	}
	
	public void onFormShow() throws SQLException {
		super.onFormShow();
		tabs.setActiveTab(TABPANEL_PONTOS_COLETADOS);
		tabs.requestFocus();
	}

	public void onFormClose() throws SQLException {
		if (isGpsStarted) {
			try {
				gps.stop();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		if (!LavenderePdaConfig.isUsaColetaGpsManual() || (LavenderePdaConfig.isUsaColetaGpsManual() && SessionLavenderePda.isColetaGpsEmAndamento())) {
			PontoGpsService.getInstance().startColetaGpsSistema(false);
		}
		MainLavenderePda.getInstance().setMenuVisibility(true);
		super.onFormClose();
	}

	protected int getBottomTab() {
		return barBottomContainer.isVisible() ? barBottomContainer.getHeight() : 0;
	}

}
