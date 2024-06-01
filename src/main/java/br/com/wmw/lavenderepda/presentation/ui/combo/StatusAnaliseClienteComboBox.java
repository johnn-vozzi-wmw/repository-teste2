package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.StatusAnaliseCliente;
import br.com.wmw.lavenderepda.business.service.StatusAnaliseClienteService;
import totalcross.util.Vector;

public class StatusAnaliseClienteComboBox extends BaseComboBox {

	public StatusAnaliseClienteComboBox() throws SQLException {
		super(Messages.NOVOCLIENTEANA_LABEL_CDSTATUSANALISE);
		defaultItemType = DefaultItemType_ALL;
		carregaTipoPedidos();
	}

	public String getValue() {
		StatusAnaliseCliente statusAnaliseCliente = (StatusAnaliseCliente) getSelectedItem();
		if (statusAnaliseCliente != null) {
			return statusAnaliseCliente.cdStatusAnalise;
		} else {
			return "";
		}
	}

	public void setValue(String value) {
		if (value != null) {
			StatusAnaliseCliente statusAnaliseCliente = new StatusAnaliseCliente();
			statusAnaliseCliente.cdStatusAnalise = value;
			//--
			select(statusAnaliseCliente);
		} else {
			setSelectedIndex(BaseComboBox.DefaultItemNull);
		}
	}

	public void carregaTipoPedidos() throws SQLException {
		removeAll();
		Vector list = StatusAnaliseClienteService.getInstance().findAll();
		if (!ValueUtil.isEmpty(list)) {
			list.qsort();
			add(list);
		}
		setSelectedIndex(0);
	}

}
