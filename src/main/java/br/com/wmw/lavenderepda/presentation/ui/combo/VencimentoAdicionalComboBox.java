package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.VencimentoAdicBoleto;
import totalcross.util.Vector;

public class VencimentoAdicionalComboBox  extends BaseComboBox {

	public VencimentoAdicionalComboBox() {
		super();
	}

	public String getValue() {
		VencimentoAdicBoleto vencimentoAdicBoleto = (VencimentoAdicBoleto)getSelectedItem();
		if (vencimentoAdicBoleto != null) {
			return vencimentoAdicBoleto.cdVencimentoAdicBoleto;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			VencimentoAdicBoleto vencimentoAdicBoleto = new VencimentoAdicBoleto();
			vencimentoAdicBoleto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			vencimentoAdicBoleto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			vencimentoAdicBoleto.cdVencimentoAdicBoleto = value;
			select(vencimentoAdicBoleto);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load(Vector list) throws SQLException {
		removeAll();
		add(list);
	}

}
