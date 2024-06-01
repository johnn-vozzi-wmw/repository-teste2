package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Indicador;
import br.com.wmw.lavenderepda.business.service.IndicadorService;
import br.com.wmw.lavenderepda.business.service.IndicadorWmwService;

public class IndicadorComboBox extends BaseComboBox{

	public IndicadorComboBox() throws SQLException {
		super(Messages.VALORINDICADOR_LABEL_INDICADOR);
		load();
	}

	public String getValue() {
		Indicador indicador = (Indicador) getSelectedItem();
		if (indicador != null) {
			return indicador.cdIndicador;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (value != null) {
			Indicador indicador = new Indicador();
			indicador.cdIndicador = value;
			select(indicador);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void load() throws java.sql.SQLException {
		add(IndicadorService.getInstance().findAll());
		add(IndicadorWmwService.getInstance().findAll());
	}
}
