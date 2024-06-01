package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class StatusRemessaComboBox extends BaseComboBox {

	private final String FL_LIBERADA = "S";
	private final String FL_LIBERADA_DESC = "Liberada";
	private final String FL_BLOQUEADA = "N";
	private final String FL_BLOQUEDA_DESC = "Bloqueada";
	private final String FL_FINALIZADA = "F";
	private final String FL_FINALIZADA_DESC = "Finalizada";

	public StatusRemessaComboBox() {
		super(Messages.REMESSAESTOQUE_LABEL_STATUS);
		defaultItemType = DefaultItemType_ALL;
		load();
	}

	public void setValue(String opcao) {
		switch (opcao) {
			case FL_LIBERADA: {
				select(FL_LIBERADA_DESC);
				break;
			}
			case FL_BLOQUEADA: {
				select(FL_BLOQUEDA_DESC);
				break;
			}
			case FL_FINALIZADA: {
				select(FL_FINALIZADA_DESC);
				break;
			}
			default: {
				select(null);
			}
		
		}
		
		
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (select != null) {
			if (select.equals(FL_LIBERADA_DESC)) {
				return FL_LIBERADA;
			} else if (select.equals(FL_BLOQUEDA_DESC)) {
				return FL_BLOQUEADA;
			} else if (select.equals(FL_FINALIZADA_DESC)) {
				return FL_FINALIZADA;
			} else {
				return "";
			}
		} else {
			return "";
		}
	}

	private void load() {
		addDefaultItens();
		add(FL_LIBERADA_DESC);
		add(FL_BLOQUEDA_DESC);
		add(FL_FINALIZADA_DESC);
		setSelectedIndex(0);
	}
	
	public boolean isLiberada() {
		return ValueUtil.valueEquals(FL_LIBERADA, getValue());
	}

}
