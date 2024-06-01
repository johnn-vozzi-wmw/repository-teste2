package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ColecaoStatus;
import br.com.wmw.lavenderepda.business.service.ColecaoStatusService;
import totalcross.util.Vector;

public class ColecaoStatusComboBox extends BaseComboBox {
	
	public ColecaoStatusComboBox(String title) {
		super(title);
	}
	
	public String getValue() {
		ColecaoStatus colecaoStatus = (ColecaoStatus) getSelectedItem();
		if (colecaoStatus != null) {
			return colecaoStatus.cdStatusColecao;
		}
		return "";
	}
	
	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			ColecaoStatus colecaoStatus = new ColecaoStatus();
			colecaoStatus.cdEmpresa = SessionLavenderePda.cdEmpresa;
			colecaoStatus.cdStatusColecao = value;
			select(colecaoStatus);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}
	
	public void load() throws SQLException {
		removeAll();
		ColecaoStatus colecaoStatus = new ColecaoStatus();
		colecaoStatus.cdEmpresa = SessionLavenderePda.cdEmpresa;
		Vector list = ColecaoStatusService.getInstance().findAllColecaoStatus(colecaoStatus);
		if (list != null) {
			add(list);
			qsort();
		}
		setSelectedIndex(DefaultItemNull);
	}

}
