package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.AreaVenda;
import br.com.wmw.lavenderepda.business.service.AreaVendaService;

public class AreaVendaComboBox extends BaseComboBox {

	public AreaVendaComboBox(String title) {
		super(title);
	}

	public String getValue() {
		AreaVenda areaVenda = (AreaVenda)getSelectedItem();
		if (areaVenda != null) {
			return areaVenda.cdAreavenda;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			AreaVenda areaVenda = new AreaVenda();
			areaVenda.cdEmpresa = SessionLavenderePda.cdEmpresa;
			areaVenda.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			areaVenda.cdAreavenda = value;
			select(areaVenda);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load(String cdCliente) throws SQLException {
		removeAll();
		add(AreaVendaService.getInstance().findAllByCdCliente(cdCliente));
		qsort();
	}

}
