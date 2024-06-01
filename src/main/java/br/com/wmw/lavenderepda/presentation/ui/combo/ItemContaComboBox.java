package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemConta;
import br.com.wmw.lavenderepda.business.service.ItemContaService;
import totalcross.util.Vector;

public class ItemContaComboBox extends BaseComboBox {

	public ItemContaComboBox() {
		super();
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
	}

	public String getValue() {
		ItemConta itemConta = (ItemConta) getSelectedItem();
		return itemConta != null ? itemConta.cdItemConta : null;
	}

	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			ItemConta itemConta = new ItemConta();
			itemConta.cdEmpresa = SessionLavenderePda.cdEmpresa;
			itemConta.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			itemConta.cdItemConta = value;
			//--
			select(itemConta);
		} else {
			setSelectedIndex(0);
		}
	}

	public void carregaItemConta() throws SQLException {
		removeAll();
		ItemConta itemContaFilter = new ItemConta();
		itemContaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemContaFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector itemContaList = ItemContaService.getInstance().findAllByExample(itemContaFilter);
		if (ValueUtil.isNotEmpty(itemContaList)) {
			itemContaList.qsort();
			add(itemContaList);
		}
	}

}
