package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.StatusAgenda;
import br.com.wmw.lavenderepda.business.service.StatusAgendaService;

public class StatusListaLeadsComboBox extends BaseComboBox {

	public StatusListaLeadsComboBox() throws SQLException {
		super(Messages.CLIENTE_LABEL_STATUS);
		this.defaultItemType = DefaultItemType_ALL;
		load();
	}

	public int getValue() {
		StatusAgenda statusAgenda = (StatusAgenda)getSelectedItem();
		if (statusAgenda != null) {
			return statusAgenda.cdStatusAgenda;
		} else {
			return DefaultItemNull;
		}
	}

	public void load() throws java.sql.SQLException {
		add(StatusAgendaService.getInstance().findAll());
	}

}
