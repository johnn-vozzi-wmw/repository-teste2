package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;

public class StatusNfDevolucaoComboBox extends BaseComboBox {
	
	
	public StatusNfDevolucaoComboBox() throws SQLException {
		super(Messages.NFDEVOLUCAO_STATUS);
		load();
	}

	public String getValue() {
		String select = (String) getSelectedItem();
		if (ValueUtil.valueEquals(Messages.NFDEVOLUCAO_COMBOBOX_APROVADO, select)) {
			return ValueUtil.VALOR_SIM;
		} else if (ValueUtil.valueEquals(Messages.NFDEVOLUCAO_COMBOBOX_REPROVADO, select)) {
			return ValueUtil.VALOR_NAO;
		} else if (ValueUtil.valueEquals(Messages.NFDEVOLUCAO_COMBOBOX_PENDENTE, select)) {
			return NfDevolucao.NFDEVOLUCAO_PENDENTE;
		}
		return null;
	}

	public void setValue(String value) {
		if (value != null) {
			select(value);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void load() {
		addDefaultItens();
		add(Messages.NFDEVOLUCAO_COMBOBOX_PENDENTE);
		add(Messages.NFDEVOLUCAO_COMBOBOX_APROVADO);
		add(Messages.NFDEVOLUCAO_COMBOBOX_REPROVADO);
		setSelectedIndex(0);
	}
}
