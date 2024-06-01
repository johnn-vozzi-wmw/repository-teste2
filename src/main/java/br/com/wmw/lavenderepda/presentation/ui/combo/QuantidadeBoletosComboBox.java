package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;

public class QuantidadeBoletosComboBox extends BaseComboBox{

	public void setValue(int nuDias) {
		select(nuDias);
	}

	public int getValue() {
		return (int) getSelectedItem();
	}

	public void load(int valueMax) {
		removeAll();
		for (int i = 1; i <= valueMax; i++) {
			add(i);
		}
		select(valueMax);
	}

}
