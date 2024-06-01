package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import totalcross.util.Vector;

public class BairroClienteComboBox extends BaseComboBox {
	
	public BairroClienteComboBox() {
		super(Messages.CLIENTE_LABEL_DSBAIRROCOMERCIAL);
		this.defaultItemType = DefaultItemType_ALL;
	}
	
	public String getValue() {
		return (String) getSelectedItem();
	}
	
	public void setValue(String dsBairroComercial) {
		if (ValueUtil.isNotEmpty(dsBairroComercial)) {
			select((String)dsBairroComercial);
		} else {
			setSelectedIndex(0);
		}
	}
	
	public void load(String dsCidadeComercial , String cdEstadoComercial) throws SQLException {
		removeAll();
		Vector bairroClienteList = ClienteService.getInstance().findAllBairroByExample(dsCidadeComercial, cdEstadoComercial);
		removeBairroDuplicada(bairroClienteList);
		add(bairroClienteList);
	}
	
	private void removeBairroDuplicada(Vector bairroClienteList) {
		String dsBairroComercialAtual = "";
		for (int i = 0; i < bairroClienteList.size(); i++) {
			String dsBairroComercial = (String) bairroClienteList.items[i];
			if (i > 0 && ValueUtil.valueEquals(dsBairroComercial, dsBairroComercialAtual)) {
				bairroClienteList.removeElementAt(i);
				i--;
			} else {
				dsBairroComercialAtual = dsBairroComercial;
			}
		}
	}

}
