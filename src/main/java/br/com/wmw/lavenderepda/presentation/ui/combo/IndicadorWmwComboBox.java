package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.IndicadorWmw;
import br.com.wmw.lavenderepda.business.service.IndicadorWmwService;

public class IndicadorWmwComboBox extends BaseComboBox {

	public IndicadorWmwComboBox(String cdTipoApuracao) throws SQLException {
		super(Messages.VALORINDICADOR_LABEL_INDICADOR);
		load(cdTipoApuracao);
	}

	public String getValue() {
		IndicadorWmw indicador = (IndicadorWmw) getSelectedItem();
		if (indicador != null) {
			return indicador.cdIndicador;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (value != null) {
			IndicadorWmw indicador = new IndicadorWmw();
			indicador.cdIndicador = value;
			select(indicador);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void load(String cdTipoApuracao) throws java.sql.SQLException {
		IndicadorWmw indicadorWmwFilter = new IndicadorWmw();
		indicadorWmwFilter.cdTipoApuracao = cdTipoApuracao; 
		add(IndicadorWmwService.getInstance().findAllByExample(indicadorWmwFilter));
		setSelectedIndex(0);
	}
}
