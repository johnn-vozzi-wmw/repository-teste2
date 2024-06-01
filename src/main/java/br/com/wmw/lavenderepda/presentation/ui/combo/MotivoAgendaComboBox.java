package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.MotivoAgenda;
import br.com.wmw.lavenderepda.business.service.MotivoAgendaService;
import totalcross.util.Vector;

public class MotivoAgendaComboBox extends BaseComboBox {

	public MotivoAgendaComboBox() {
		super();
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
	}

	public MotivoAgenda getMotivoAgenda() {
		MotivoAgenda motivoAgenda = (MotivoAgenda) getSelectedItem();
		if (motivoAgenda != null) {
			return motivoAgenda;
		} else {
			return null;
		}
	}

	public String getValue() {
		MotivoAgenda motivoAgenda = (MotivoAgenda) getSelectedItem();
		if (motivoAgenda != null) {
			return motivoAgenda.cdMotivoAgenda;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		MotivoAgenda motivoAgenda = new MotivoAgenda();
		motivoAgenda.cdMotivoAgenda = value;
		select(motivoAgenda);
	}

	public void loadMotivoTransferenciaAgendaVista() throws SQLException {
		removeAll();
		MotivoAgenda motivoAgenda = new MotivoAgenda();
		motivoAgenda.flTransferenciaAgenda = ValueUtil.VALOR_SIM;
		Vector list = MotivoAgendaService.getInstance().findAllByExample(motivoAgenda);
		list.qsort();
		add(list);
		setSelectedIndex(0);
	}
	
	public void loadMotivoReagendamentoAgendaVista() throws SQLException {
		removeAll();
		MotivoAgenda motivoAgenda = new MotivoAgenda();
		motivoAgenda.flAgendaVisita = ValueUtil.VALOR_SIM;
		Vector list = MotivoAgendaService.getInstance().findAllByExample(motivoAgenda);
		list.qsort();
		add(list);
		setSelectedIndex(0);
	}
}
