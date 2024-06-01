package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import totalcross.util.Vector;

public class CidadeClienteComboBox extends BaseComboBox {
	
	public CidadeClienteComboBox() {
		super(Messages.CLIENTE_LABEL_DSCIDADECOMERCIAL);
		this.defaultItemType = DefaultItemType_ALL;
	}
	
	public String getValue() {
		return (String) getSelectedItem();
	}
	
    public void setValue(String dsCidadeComercial) {
    	if (ValueUtil.isNotEmpty(dsCidadeComercial)) {
    		select((String)dsCidadeComercial);
    	} else {
    		setSelectedIndex(0);
    	}
    }
	
    public void load() throws SQLException {
    	load(null);
    }
    
    public void load(String cdEstadoComercial) throws SQLException {
		removeAll();
		Vector cidadeClienteList = ClienteService.getInstance().findAllCidadeByExample(cdEstadoComercial);
		removeCidadeDuplicada(cidadeClienteList);
		add(cidadeClienteList);
	}

	private void removeCidadeDuplicada(Vector cidadeClienteList) {
		String dsCidadeComercialAtual = "";
		for (int i = 0; i < cidadeClienteList.size(); i++) {
			String dsCidadeComercial = (String) cidadeClienteList.items[i];
			if (i > 0 && ValueUtil.valueEquals(dsCidadeComercial, dsCidadeComercialAtual)) {
				cidadeClienteList.removeElementAt(i);
				i--;
			} else {
				dsCidadeComercialAtual = dsCidadeComercial;
			}
		}
	}

}
