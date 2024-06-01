package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.TipoAgenda;
import br.com.wmw.lavenderepda.business.service.TipoAgendaService;
import totalcross.util.Vector;

public class TipoAgendaComboBox extends BaseComboBox {

	public TipoAgendaComboBox() throws SQLException {
		super(Messages.TIPO_AGENDA);
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		load();
	}

	public String getValue() {
		TipoAgenda tipoAgenda = (TipoAgenda)getSelectedItem();
		if (tipoAgenda != null) {
			return tipoAgenda.cdTipoAgenda;
		} else {
			return null;
		}
	}

	public String getValueAt(int index) {
		TipoAgenda tipoAgenda = (TipoAgenda)getItemAt(index);
		if (tipoAgenda != null) {
			return tipoAgenda.cdTipoAgenda;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			TipoAgenda tipoAgenda = new TipoAgenda();
			select(tipoAgenda);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load() throws java.sql.SQLException {
		removeAll();
		Vector list = TipoAgendaService.getInstance().findAll();
		list.qsort();
		add(list);
		setSelectedIndex(0);
	}
	
}
