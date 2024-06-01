package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;

import java.sql.SQLException;
import java.util.Vector;

public class StatusNotificacaoComboBox extends BaseComboBox {

	public static final String NAO_LIDO = "Não Lido";
	public static final String LIDO = "Lido";

	public StatusNotificacaoComboBox() throws SQLException {
		super(Messages.NOTIFICACAO_LABEL_STATUS);
		defaultItemType = DefaultItemType_ALL;
		load();
	}

	public String getValue() {
		String value = (String) getSelectedItem();
		return  value;
	}

	public void setValue(String value) {
		select(value);
	}

	private void load() throws SQLException {
		Vector vector  = new Vector();
		addDefaultItens();
		vector.add(LIDO);
		vector.add(NAO_LIDO);
		add(vector.toArray());
	}

	public String getFlValue() {
		String value = getValue();
		if (LIDO.equals(value)) {
			return "S";
		} else if (NAO_LIDO.equals(value)) {
			return "N";
		}
		return null;
	}
}
