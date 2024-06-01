package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;

public class StatusTituloFinanceiroComboBox extends BaseComboBox {
	
	public StatusTituloFinanceiroComboBox() throws SQLException {
		super(Messages.TITULOFINANCEIRO_LABEL_STATUS_TITULO);
		defaultItemType = DefaultItemType_ALL;
		load();
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

	public void load() throws SQLException {
		addDefaultItens();
		add(Messages.COMBOBOX_TITULOFINANCEIRO_PAGO);
		add(Messages.COMBOBOX_TITULOFINANCEIRO_NAO_PAGO);
		setSelectedIndex(0);
	}
}
