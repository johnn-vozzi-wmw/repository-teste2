package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ListaLeads;

public class TipoCadastroListaLeadsBox extends BaseComboBox {

	public TipoCadastroListaLeadsBox() {
		super(Messages.CLIENTE_LABEL_TIPOCADASTRO);
		this.defaultItemType = DefaultItemType_ALL;
        load();
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (Messages.OPCAO_WEB.equals(select)) {
			return ListaLeads.LEADS_TIPO_WEB;
		} else if (Messages.OPCAO_PDA.equals(select)) {
			return ListaLeads.LEADS_TIPO_PDA;
		}
		return null;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isEmpty(value)) {
			return;
		}
		setSelectedItem(value);
 	}

	@Override
	public void setSelectedItem(Object name) {
		if (ListaLeads.LEADS_TIPO_WEB.equals(name)) {
			name = Messages.OPCAO_WEB;
		} else if (ListaLeads.LEADS_TIPO_PDA.equals(name)) {
			name = Messages.OPCAO_PDA;
		}
		super.setSelectedItem(name);
	}
	
	private void load() {
		add(FrameworkMessages.OPCAO_TODOS);
		add(Messages.OPCAO_WEB);
		add(Messages.OPCAO_PDA);
	}
}
