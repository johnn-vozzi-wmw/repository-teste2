package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Familia;
import br.com.wmw.lavenderepda.business.service.FamiliaService;
import totalcross.util.Vector;

public class FamiliaComboBox  extends BaseComboBox {

	public FamiliaComboBox(String title) {
		super(title);
		defaultItemType = DefaultItemType_ALL;
	}

	public String getValue() {
		Familia familia = (Familia)getSelectedItem();
		if (familia != null) {
			return familia.cdFamilia;
		} else {
			return null;
		}
	}

	public String getValueAt(int index) {
		Familia familia = (Familia)getItemAt(index);
		if (familia != null) {
			return familia.cdFamilia;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			Familia familia = new Familia();
			familia.cdEmpresa = SessionLavenderePda.cdEmpresa;
			familia.cdFamilia = value;
			select(familia);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load(String cdMetaVenda) throws java.sql.SQLException {
		removeAll();
		Vector list = FamiliaService.getInstance().findAllFamiliasByMetaVenda(cdMetaVenda);
		list.qsort();
		add(list);
	}

}
