package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.BaseMainWindow;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MotivoPendencia;
import br.com.wmw.lavenderepda.business.domain.MotivoPendenciaJust;
import br.com.wmw.lavenderepda.business.service.MotivoPendenciaJustService;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class MotivoPendenciaJustComboBox extends BaseComboBox {
	
	private String originalText;
	
	public MotivoPendenciaJustComboBox(MotivoPendencia motivoPendencia) throws SQLException {
		super(Messages.MOTIVO_PENDENCIA_JUST_COMBO);
		originalText = "";
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		loadMotivoPendenciaJustCombo(motivoPendencia);
	}

	public String getValue() {
		MotivoPendenciaJust motivoPendenciaJust = (MotivoPendenciaJust) getSelectedItem();
		if (motivoPendenciaJust != null) {
			return motivoPendenciaJust.cdMotivoPendenciaJust;
		} else {
			return "";
		}
	}
	
	private void loadMotivoPendenciaJustCombo(MotivoPendencia motivoPendencia) throws SQLException {
		MotivoPendenciaJust motivoPendenciaJustFilter = new MotivoPendenciaJust(motivoPendencia.cdEmpresa, motivoPendencia.cdRepresentante, motivoPendencia.cdMotivoPendencia);
		Vector motivoPendenciaJustList = MotivoPendenciaJustService.getInstance().findAllByExample(motivoPendenciaJustFilter);
		add(motivoPendenciaJustList);
		if (size() > 1) {
			setSelectedIndex(0);
		}
	}
	
	@Override
	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
		case PenEvent.PEN_DOWN:
			originalText = getValue();
			break;
		case PenEvent.PEN_UP:
			if (!getValue().equals(originalText)) {
				geraEventoValueChange();
			}
			break;
		}
	}
	
	private void geraEventoValueChange() {
		BaseMainWindow.getBaseMainWindowInstance().geraEvento(new ValueChangeEvent(this));
	}

}
