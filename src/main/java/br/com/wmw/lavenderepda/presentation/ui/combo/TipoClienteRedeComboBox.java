package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Rede;

public class TipoClienteRedeComboBox extends BaseComboBox {
	
	public TipoClienteRedeComboBox() {
		super(Messages.TIPO_CLIENTE_REDE_TITULO);
		load();
	}
	
	public String getValue() {
		Rede rede = (Rede) getSelectedItem();
		return rede.cdRede;
	}
	
	public void setValue(final String value) {
		if (ValueUtil.isEmpty(value)) return;
		setSelectedItem(value);
	}

	private void load() {
		removeAll();
		add(new Rede(Cliente.TIPO_TODOS, Messages.TIPO_CLIENTE_REDE_OPCAO_TODOS));
		add(new Rede(Cliente.TIPO_REDE, Messages.TIPO_CLIENTE_REDE_OPCAO_REDE));
		add(new Rede(Cliente.TIPO_INDIVIDUAL, Messages.TIPO_CLIENTE_REDE_OPCAO_INDIVIDUAL));
	}
	
	public boolean isOpcaoTodosSelecionado() {
		return ValueUtil.valueEquals(Cliente.TIPO_TODOS, getValue());
	}
	
	public boolean isOpcaoRedeSelecionado() {
		return ValueUtil.valueEquals(Cliente.TIPO_REDE, getValue());
	}
	
	public boolean isOpcaoIndividualSelecionado() {
		return ValueUtil.valueEquals(Cliente.TIPO_INDIVIDUAL, getValue());
	}
	
}
