package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.RotaEntrega;
import br.com.wmw.lavenderepda.business.service.RotaEntregaService;
import totalcross.util.Vector;

public class RotaEntregaComboBox extends BaseComboBox {

	public RotaEntregaComboBox() {
		super(Messages.PEDIDO_LABEL_DSROTAENTREGA);
	}

	public String getValue() {
		RotaEntrega rotaEntrega = (RotaEntrega) getSelectedItem();
		if (rotaEntrega != null) {
			return rotaEntrega.cdRotaEntrega;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (value != null) {
			RotaEntrega rotaEntrega = new RotaEntrega();
			rotaEntrega.cdEmpresa = SessionLavenderePda.cdEmpresa;
			rotaEntrega.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(RotaEntrega.class);
			rotaEntrega.cdRotaEntrega = value;
			//--
			select(rotaEntrega);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void load(String cdCliente) throws SQLException {
		removeAll();
		Vector list = RotaEntregaService.getInstance().findAllRotasEntregaCliente2Combo(cdCliente);
		if (!ValueUtil.isEmpty(list)) {
			add(list);
			qsort();
		}
	}

	
	public void load() throws java.sql.SQLException {
		load(DefaultItemType_NONE);
	}
	
	public void load(int defaultItemTp) throws SQLException {
		defaultItemType = defaultItemTp;
		removeAll();
		Vector list = RotaEntregaService.getInstance().findAllRotaEntrega();
		if (!ValueUtil.isEmpty(list)) {
			list.qsort();
			add(list);
		}
	}
}
