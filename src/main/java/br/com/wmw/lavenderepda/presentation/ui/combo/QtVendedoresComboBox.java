package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;

public class QtVendedoresComboBox extends BaseComboBox {

	public enum QTD_VENDEDORES {
		QT_VENDEDORES(Messages.QUANTIDADE_VENDEDORES_LABEL, ""),
		QT_VENDEDORES_1_5(Messages.QT_VENDEDORES_1_5, "1-5"),
		QT_VENDEDORES_6_15(Messages.QT_VENDEDORES_6_15, "6-15"),
		QT_VENDEDORES_16_25(Messages.QT_VENDEDORES_16_25, "16-25"),
		QT_VENDEDORES_26_25(Messages.QT_VENDEDORES_26_25, "26-35"),
		QT_VENDEDORES_36_45(Messages.QT_VENDEDORES_36_45, "36-45"),
		QT_VENDEDORES_MAIS_46(Messages.QT_VENDEDORES_MAIS_46, "46+");
		
		private String label, valor;
		
		private QTD_VENDEDORES(String label, String valor) {
			this.label = label;
			this.valor = valor;
		}
	}

	public QtVendedoresComboBox() {
		super(Messages.TITULO_COMBO_QT_VENDEDORES_LABEL);
	}

	public void loadCombo() {
		add(new String[] { QTD_VENDEDORES.QT_VENDEDORES.label, QTD_VENDEDORES.QT_VENDEDORES_1_5.label,
				QTD_VENDEDORES.QT_VENDEDORES_6_15.label, QTD_VENDEDORES.QT_VENDEDORES_16_25.label, QTD_VENDEDORES.QT_VENDEDORES_26_25.label,
				QTD_VENDEDORES.QT_VENDEDORES_36_45.label, QTD_VENDEDORES.QT_VENDEDORES_MAIS_46.label });
	}

	public String getValue() {
		for (QTD_VENDEDORES e : QTD_VENDEDORES.values()) {
			if (e.label.equals((String) getSelectedItem())) {
				return e.valor;
			}
		}
		return null;
	}
	
	@Override
	public void setSelectedItem(Object name) {
		for (QTD_VENDEDORES e : QTD_VENDEDORES.values()) {
			if (e.valor == null) {
				break;
			} else if (e.valor.equals((String) name)) {
				super.setSelectedItem(e.label);
				break;
			}
		}
	}

}
