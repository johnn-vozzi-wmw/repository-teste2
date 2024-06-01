package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.enums.StatusSolicitacaoAutorizacaoEnum;
import totalcross.util.Vector;

public class StatusSolicitacaoAutorizacaoComboBox extends BaseComboBox {

	public StatusSolicitacaoAutorizacaoComboBox(int defaultItemType) {
		super("Status da Autorização");
		this.defaultItemType = defaultItemType;
		load();
	}

	public String getValue() {
		try {
			int selectedIndex = getSelectedIndex() - 1;
			StatusSolicitacaoAutorizacaoEnum[] values = StatusSolicitacaoAutorizacaoEnum.values();
			String name = values[selectedIndex].name();
			return ValueUtil.valueEquals("P", name) ? "" : name;
		} catch (Throwable e) {
			return null;
		}
	}

	public void setValue(String value) {
		try {
			select(StatusSolicitacaoAutorizacaoEnum.valueOf(value));
		} catch (IllegalArgumentException e) {
			setSelectedIndex(0);
			ExceptionUtil.handle(e);
		}
	}

	public void load() {
		removeAll();
		Vector itens = new Vector();
		StatusSolicitacaoAutorizacaoEnum[] enums = StatusSolicitacaoAutorizacaoEnum.values();
		for (int i = 0; i < enums.length; i++) {
			itens.addElement(enums[i].getTitle());
		}
		add(itens);
	}

}
