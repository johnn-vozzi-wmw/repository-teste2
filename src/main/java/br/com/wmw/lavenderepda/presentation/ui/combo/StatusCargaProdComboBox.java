package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;

public class StatusCargaProdComboBox extends BaseComboBox {

	public static final String FL_STATUS_ENVIADO = "E";
	public static final String FL_STATUS_NAO_ENVIADO = "N";
	

	public StatusCargaProdComboBox() {
		super(Messages.CARGAPRODUTO_STATUS);
		defaultItemType = DefaultItemType_ALL;
		load();
	}

	public void setValue(String flStatus) {
		if (FL_STATUS_ENVIADO.equals(flStatus)) {
			select(Messages.FL_ENVIADO_DESC);
		} else if (FL_STATUS_NAO_ENVIADO.equals(flStatus)) {
			select(Messages.FL_NAO_ENVIADO_DESC);
		} else {
			select(null);
		}
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (Messages.FL_ENVIADO_DESC.equals(select)) {
			return (FL_STATUS_ENVIADO);
		} else if (Messages.FL_NAO_ENVIADO_DESC.equals(select)) {
			return (FL_STATUS_NAO_ENVIADO);
		} else {
			return "";
		}
	}

	private void load() {
		addDefaultItens();
		add(Messages.FL_ENVIADO_DESC);
		add(Messages.FL_NAO_ENVIADO_DESC);
		setSelectedIndex(2);
	}

}
