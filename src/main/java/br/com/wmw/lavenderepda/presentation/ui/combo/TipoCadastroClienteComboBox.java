package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class TipoCadastroClienteComboBox extends BaseComboBox {

	private static final String OPCAO_TODOS = "--- Todos ---";
	private static final String OPCAO_CLIENTE = "Cliente";
	private static final String OPCAO_PROSPECTS = "Prospects";
	private static final String TIPO_CLIENTE = "C";
	private static final String TIPO_PROSPECTS = "P";

	public TipoCadastroClienteComboBox() {
		super(Messages.CLIENTE_LABEL_TIPOCADASTRO);
        load();
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (OPCAO_CLIENTE.equals(select)) {
			return TIPO_CLIENTE;
		} else if (OPCAO_PROSPECTS.equals(select)) {
			return TIPO_PROSPECTS;
		}
		return null;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isEmpty(value)) return;
		setSelectedItem(value);
 	}

	@Override
	public void setSelectedItem(Object name) {
		if (TIPO_CLIENTE.equals(name)) {
			name = OPCAO_CLIENTE;
		} else if (TIPO_PROSPECTS.equals(name)) {
			name = OPCAO_PROSPECTS;
		}
		super.setSelectedItem(name);
	}
	
	private void load() {
		removeAll();
		add(OPCAO_TODOS);
		add(OPCAO_CLIENTE);
		add(OPCAO_PROSPECTS);
	}
}
