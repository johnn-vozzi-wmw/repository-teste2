package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;

public class ValorizacaoProdStatusComboBox extends BaseComboBox {
	
	private static final String TIPO_ENVIADO = "E";
	private static final String TIPO_NAO_ENVIADO = "N";
	
	public ValorizacaoProdStatusComboBox() {
		super(Messages.VALORIZACAOPROD_LABEL_VLUNITARIO);
        load();
	}
	
	public String getValue() {
		String select = (String)getSelectedItem();
		if (Messages.VAL_PROD_OPCAO_ENVIADOS.equals(select)) {
			return TIPO_ENVIADO;
		} else if (Messages.VAL_PROD_OPCAO_NAO_ENVIADOS.equals(select)) {
			return TIPO_NAO_ENVIADO;
		}
		return null;
	}
	
	private void load() {
		removeAll();
		add(Messages.VAL_PROD_OPCAO_TODOS);
		add(Messages.VAL_PROD_OPCAO_NAO_ENVIADOS);
		add(Messages.VAL_PROD_OPCAO_ENVIADOS);
	}
	
}