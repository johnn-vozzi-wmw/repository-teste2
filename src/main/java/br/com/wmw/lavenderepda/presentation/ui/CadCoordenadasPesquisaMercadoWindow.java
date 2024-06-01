package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.gps.GpsService;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelSubtitle;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;

public class CadCoordenadasPesquisaMercadoWindow extends WmwWindow {

	private LabelValue vlNmCliente;
	private LabelValue vlLatitude;
	private LabelValue vlLongitude;
	private LabelName lbLatitude;
	private LabelName lbLongitude;
	private LabelName lbCliente;
	private ButtonPopup btCadastrar;
	private ButtonPopup btSenha;
	private BaseButton btColetar;
	private LabelSubtitle lbStatusColeta;
	private ButtonPopup btMaisTarde;
	public boolean cadastrouCoordenada;
	public boolean jaCadastrouCoordenada;
	public Cliente cliente;
	private boolean editing;
	public double latitude;
	public double longitude;
	
	
	public CadCoordenadasPesquisaMercadoWindow(Cliente cliente, double[] latLon, boolean editing) {
		super(Messages.CAD_COORD_TITULO);
		this.cliente = cliente;
		this.editing = editing;
		this.jaCadastrouCoordenada = ((latLon[0] != 0 || latLon[1] != 0) || SessionLavenderePda.autorizadoPorSenhaCoordenadaPesquisaMercado);
		lbCliente = new LabelName(Messages.CAD_COORD_CLIENTE);
		vlNmCliente = new LabelValue();
		lbLatitude = new LabelName(Messages.CAD_COORD_LAT);
		lbLongitude = new LabelName(Messages.CAD_COORD_LON);
		if (jaCadastrouCoordenada) {
			vlLatitude = new LabelValue(latLon[0] + "");
			vlLongitude = new LabelValue(latLon[1] + "");
			btCadastrar = new ButtonPopup(Messages.BT_CONFIRMAR);
		} else {
			vlLatitude = new LabelValue();
			vlLongitude = new LabelValue();
			btCadastrar = new ButtonPopup(Messages.CAD_COORD_CADASTRAR);
		}
		btMaisTarde = new ButtonPopup(Messages.CAD_COORD_MAIS_TARDE);
		btSenha = new ButtonPopup(Messages.MENU_SISTEMA_GERAR_SENHA);
		btColetar = new BaseButton(Messages.CAD_COORD_COLETAR, UiUtil.getColorfulImage("images/gps.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()), RIGHT, WIDTH_GAP_BIG);
		lbStatusColeta = new LabelSubtitle();
		lbStatusColeta.setForeColor(ColorUtil.baseForeColorSystem);
		setDefaultRect();
		btFechar.setVisible(false);
		domainToScreen();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbCliente, vlNmCliente, getLeft(), getNextY());
		UiUtil.add(this, btColetar, CENTER, getNextY());
		UiUtil.add(this, lbLatitude, vlLatitude, getLeft(), getNextY());
		UiUtil.add(this, lbLongitude, vlLongitude, getLeft(), getNextY());
		SessionContainer containerStatus = new SessionContainer();
		containerStatus.setBackColor(ColorUtil.componentsBackColor);
		UiUtil.add(this, containerStatus, LEFT, AFTER + HEIGHT_GAP, FILL);
		UiUtil.add(containerStatus, lbStatusColeta, getLeft(), CENTER, FILL, PREFERRED);
		addButtonPopup(btCadastrar);
		if (LavenderePdaConfig.obrigaCadastroCoordenadaNaPesquisaDeMercado() && !SessionLavenderePda.autorizadoPorSenhaCoordenadaPesquisaMercado && !editing) {
			addButtonPopup(btSenha);
		} 
		addButtonPopup(btMaisTarde);
	}

	private void domainToScreen() {
		vlNmCliente.setValue(cliente.nmRazaoSocial);
	}
	
	@Override
	protected void addBtFechar() {
		//--
	}

	@Override
	public void onWindowEvent(final Event event) throws SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btMaisTarde) {
					if (!LavenderePdaConfig.obrigaCadastroCoordenadaNaPesquisaDeMercado() || (SessionLavenderePda.autorizadoPorSenhaCoordenadaPesquisaMercado || jaCadastrouCoordenada) || (editing || !UiUtil.showConfirmYesNoMessage(Messages.CAD_COORDENADA_PESQUISA_MERCADO_WINDOW_MAIS_TARDE))) {
						closedByBtFechar = true;
						fecharWindow();
					}
				} else if (event.target == btColetar) {
					coletarCoordenadasGps();
				} else if (event.target == btCadastrar) {
					salvarGps();
				} else if (event.target == btSenha) {
					btSenhaClick();
				}
				break;
			}
		}
	}

	private void btSenhaClick() throws SQLException {
		AdmSenhaDinamicaWindow senha = new AdmSenhaDinamicaWindow();
		senha.setMensagem(Messages.CAD_COORDENADA_PESQUISA_MERCADO_WINDOW_SENHA);
		senha.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_CAD_COORDENADAS_PESQUISA_MERCADO);
		senha.setCdCliente(cliente.cdCliente);
		if (senha.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			closedByBtFechar = false;
			cadastrouCoordenada = true;
			SessionLavenderePda.autorizadoPorSenhaCoordenadaPesquisaMercado = true;
			UiUtil.showSucessMessage(Messages.CAD_COORDENADA_PESQUISA_MERCADO_WINDOW_SUCESS);
			fecharWindow();
		}
	}

	private void coletarCoordenadasGps() {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.CAD_COORD_COLETANDO);
		msg.popupNonBlocking();
		try {
			if (VmUtil.isAndroid() || VmUtil.isIOS()) {
				GpsData gpsData = GpsService.getInstance().forceReadData(LavenderePdaConfig.getTimeOutCoordenadaPesquisaDeMercado());
				if (gpsData.isGpsOff() && !SessionLavenderePda.isSistemaLiberadoSenhaGpsOff) {
					UiUtil.showWarnMessage(Messages.CAD_COORD_GPS_DESLIGADO);
					atualizarComponentes();
					return;
				}
				if (gpsData.isSuccess()) {
					vlLatitude.setText(gpsData.latitude + "");
					vlLongitude.setText(gpsData.longitude + "");
				} else {
					UiUtil.showWarnMessage(Messages.CAD_COORD_COLETA_ERRO);
				}
			} else {
				UiUtil.showWarnMessage(Messages.CAD_COORD_PLATAFORMA_NAO_SUPORTADA);
			}
			atualizarComponentes();
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		} finally {
			msg.unpop();
		}
	}

	private void atualizarComponentes() {
		if (ValueUtil.isNotEmpty(vlLatitude.getText()) && ValueUtil.isNotEmpty(vlLongitude.getText())) {
			lbStatusColeta.setValue(Messages.CAD_COORD_SUCESSO);
			lbStatusColeta.setForeColor(Color.darker(Color.GREEN, 150));
		} else {
			lbStatusColeta.setValue(Messages.CAD_COORD_FALHA);
			lbStatusColeta.setForeColor(ColorUtil.softRed);
		}
	}

	private void salvarGps() throws SQLException {	
		if (((ValueUtil.isEmpty(vlLatitude.getText()) || ValueUtil.isEmpty(vlLongitude.getText())))
				|| (ValueUtil.getDoubleValue(vlLatitude.getValue()) == 0 && ValueUtil.getDoubleValue(vlLongitude.getValue()) == 0)) {
			UiUtil.showErrorMessage(Messages.CAD_COORDENADA_PESQUISA_MERCADO_WINDOW_ERROR);
			return;
		}
		latitude = ValueUtil.getDoubleSimpleValue(vlLatitude.getText());
		longitude = ValueUtil.getDoubleSimpleValue(vlLongitude.getText());
		cadastrouCoordenada = true;
		fecharWindow();
	}

	
}
