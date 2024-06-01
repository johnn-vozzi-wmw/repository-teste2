package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;

public class ItemGradeComboBox extends BaseComboBox {

	public ItemGradeComboBox() {
	}

	public ItemGradeComboBox(boolean defaultItemSelectOne) {
		if (defaultItemSelectOne) {
			defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		}
	}

	public String getValue() {
		ItemGrade itemGrade = (ItemGrade)getSelectedItem();
		if (itemGrade != null) {
			return itemGrade.cdItemGrade;
		} else {
			return null;
		}
	}

	public void setValue(String cdTipoItemGrade, String cdItemGrade) {
		ItemGrade itemGrade = new ItemGrade();
		itemGrade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemGrade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemGrade.cdTipoItemGrade = cdTipoItemGrade;
		itemGrade.cdItemGrade = cdItemGrade;
		select(itemGrade);
	}
	
}
