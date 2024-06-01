package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;

public class NaoPositivadoComboBox extends BaseComboBox{

	public NaoPositivadoComboBox(){
        if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
        	load();
        }
	}

	public void setValue(String opcao) {
		if (opcao.equals(Messages.OPCAO_TODOS)) {
			select(Messages.OPCAO_TODOS);
		} else if (opcao.equals(Messages.NAO_POSITIVADOS)) {
			select(Messages.NAO_POSITIVADOS);
		} else {
			select(null);
		}
	}

	public String getValue() {
		String select = (String)getSelectedItem();
		if (select != null) {
			return select;
		} else {
			return null;
		}
	}

	private void load(){
		removeAll();
		add(Messages.OPCAO_TODOS);
		add(Messages.NAO_POSITIVADOS);
	}
}
