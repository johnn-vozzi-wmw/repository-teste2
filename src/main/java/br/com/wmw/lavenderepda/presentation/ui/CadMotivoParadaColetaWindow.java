package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ColetaGps;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MotivoColetaComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadMotivoParadaColetaWindow extends WmwWindow {

	private MotivoColetaComboBox cbMotivoColeta;
	private LabelName lbMotivoColeta;
	private LabelValue lbDescricao;
	public boolean cadastrouMotivo;
	private ButtonPopup btConfirma;
	
	public CadMotivoParadaColetaWindow() throws SQLException {
		super(Messages.LABEL_MOTIVOCOLETA);
		lbMotivoColeta = new LabelName(Messages.LABEL_MOTIVOCOLETA);
		cbMotivoColeta = new MotivoColetaComboBox();
		lbDescricao = new LabelValue("@@@");
		btConfirma = new ButtonPopup(Messages.MOTIVOCOLETA_CONFIRMA);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, lbDescricao, getLeft(), getNextY());
		lbDescricao.setMultipleLinesText(Messages.MOTIVOCOLETA_DESCRICAO);
		UiUtil.add(this, lbMotivoColeta, cbMotivoColeta, getLeft(), getNextY());
		addButtonPopup(btConfirma);
		addButtonPopup(btFechar);
	}
	
	@Override
	protected void addBtFechar() {
	}
	
	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
	   try {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btConfirma) {
					if (ValueUtil.isEmpty(cbMotivoColeta.getValue())) {
						UiUtil.showErrorMessage(Messages.MOTIVOCOLETA_SELECIONE_OPCAO);
						return;
					}
					ColetaGps coletaGpsEmAndamento = ColetaGpsService.getInstance().getLastColetaGpsEmAndamento();
					if (coletaGpsEmAndamento != null) {
						ColetaGpsService.getInstance().updateColumn(coletaGpsEmAndamento.getRowKey(), "cdMotivoColeta", cbMotivoColeta.getValue(), totalcross.sql.Types.VARCHAR);
						cadastrouMotivo = true;
						fecharWindow();
					}
				}
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}
}
