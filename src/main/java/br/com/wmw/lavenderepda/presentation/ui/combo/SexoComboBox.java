package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;

public class SexoComboBox extends BaseComboBox{

	public final String FLSEXO_MASCULINO = "M";
	public final String FLSEXO_MASCULINO_DESC = "Masculino";
	public final String FLSEXO_FEMININO = "F";
	public final String FLSEXO_FEMININO_DESC = "Feminino";

	public SexoComboBox() {
		super(Messages.CONTATO_LABEL_FLSEXO);
		load();
	}

	public void setValue(String flSexo) {
		if (flSexo.equals(FLSEXO_MASCULINO)) {
			select(FLSEXO_MASCULINO_DESC);
		} else if (flSexo.equals(FLSEXO_FEMININO)) {
			select(FLSEXO_FEMININO_DESC);
		} else {
			select(null);
		}
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (select != null) {
			if (select.equals(FLSEXO_MASCULINO_DESC)) {
				return (FLSEXO_MASCULINO);
			} else if (select.equals(FLSEXO_FEMININO_DESC)) {
				return (FLSEXO_FEMININO);
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private void load() {
		removeAll();
		add(FLSEXO_MASCULINO_DESC);
		add(FLSEXO_FEMININO_DESC);
	}

}
