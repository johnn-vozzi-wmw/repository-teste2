package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;

public class PrincipioAtivoComboBox extends BaseComboBox{

	public PrincipioAtivoComboBox(String[] items) {
		super(Messages.PRODUTO_LABEL_PRODUTO_GENERICO);
		add(items);
	}

	public String getValue() {
		return (String)getSelectedItem();
	}

}
