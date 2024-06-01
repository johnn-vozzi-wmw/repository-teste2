package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.service.VerbaClienteService;

public class CdVerbaSaldoComboBox extends BaseComboBox {
	
	public CdVerbaSaldoComboBox(String cbGrupoProduto1) throws SQLException {
		super(Messages.CODIGO);
		this.defaultItemType = DefaultItemType_ALL;
		carregaCdsVerbaSaldoCliente(cbGrupoProduto1);
	}
	
	public void setValue(String cdVerbaSaldoCliente) {
		select(cdVerbaSaldoCliente);
	}
	
	public String getValue() {
		return (String)getSelectedItem();
	}
	
	private void carregaCdsVerbaSaldoCliente(final String cbGrupoProduto1) throws SQLException {
		removeAll();
		add(VerbaClienteService.getInstance().findCdVerbaSaldoCliente(cbGrupoProduto1));
	}

}
