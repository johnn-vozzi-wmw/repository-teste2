package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.NfeEstoque;

public class TipoNfeComboBox extends BaseComboBox {
	
	public TipoNfeComboBox() {
		super(Messages.REMESSAESTOQUE_TIPO_NFE);
		defaultItemType = DefaultItemType_ALL;
		load();
	}
	
	public String getValue() {
		if (Messages.REMESSAESTOQUE_NFE_ENTRADA.equals(getSelectedItem())) {
			return NfeEstoque.TIPONFE_ENTRADA;
		} else if (Messages.REMESSAESTOQUE_NFE_SAIDA.equals(getSelectedItem())) {
			return NfeEstoque.TIPONFE_SAIDA;
		}
		return ValueUtil.VALOR_NI;
	}
	
	public void setValue(String value) {
		if (NfeEstoque.TIPONFE_ENTRADA.equals(value)) {
			select(Messages.REMESSAESTOQUE_NFE_ENTRADA);
		} else if (NfeEstoque.TIPONFE_SAIDA.equals(value)) {
			select(Messages.REMESSAESTOQUE_NFE_SAIDA);
		}
		select(null);
	}
	
	public void load() {
		addDefaultItens();
		add(new String[] {Messages.REMESSAESTOQUE_NFE_ENTRADA, Messages.REMESSAESTOQUE_NFE_SAIDA});
		setSelectedIndex(0);
	}

}
