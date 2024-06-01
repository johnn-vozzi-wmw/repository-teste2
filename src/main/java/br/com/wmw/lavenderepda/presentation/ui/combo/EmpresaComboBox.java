package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import totalcross.util.Vector;

public class EmpresaComboBox extends BaseComboBox {

	public EmpresaComboBox(String title) {
		this(title, BaseComboBox.DefaultItemType_NONE);
	}
	public EmpresaComboBox(String title, int defaultItemType) {
		super(title);
		this.defaultItemType = defaultItemType;
	}

	public String getValue() {
		if (getSelectedItem() == null) {
			return null;
		}
		Empresa empresa = (Empresa) getSelectedItem();
		if (empresa != null) {
			return empresa.cdEmpresa;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		Empresa empresa = new Empresa();
		empresa.cdEmpresa = value;
		select(empresa);
	}

	public void loadEmpresa(String cdEmpresaDefault) throws SQLException {
		removeAll();
		Vector list = EmpresaService.getInstance().findAll();
		list.qsort();
		if (ValueUtil.isNotEmpty(list)) {
			add(list);
			setValue(cdEmpresaDefault);
			if (getValue() == null) {
				setSelectedIndex(0);
			}
		}
	}

	public void loadEmpresa() throws SQLException {
		loadEmpresa(null);
	}

}