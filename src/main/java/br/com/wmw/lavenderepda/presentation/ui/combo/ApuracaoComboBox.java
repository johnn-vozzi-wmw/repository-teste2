package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.service.ValorIndicadorWmwService;


public class ApuracaoComboBox extends BaseComboBox {


	public ApuracaoComboBox() throws SQLException {
		super(Messages.VALORINDICADOR_PERIODO);
		loadApuracao();
	}

	public String getValue() {
		String indicador = (String) getSelectedItem();
		if (indicador != null) {
			return indicador;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (value != null) {
			select(value);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public void loadApuracao()  throws SQLException {
		 loadApuracao(null);
	}

	public void loadApuracao(String cdIndicador)  throws SQLException {
		removeAll();
		add(ValorIndicadorWmwService.getInstance().findDistinctPeriodoList(cdIndicador));
		setSelectedIndex(0);
	}

}
