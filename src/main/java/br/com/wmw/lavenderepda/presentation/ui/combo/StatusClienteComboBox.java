package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.StatusCliente;
import br.com.wmw.lavenderepda.business.service.StatusClienteService;

public class StatusClienteComboBox extends BaseComboBox {

	public StatusClienteComboBox() throws SQLException {
		super(Messages.CLIENTE_LABEL_STATUS);
		this.defaultItemType = DefaultItemType_ALL;
		carregaStatus();
	}
	
	public StatusClienteComboBox(String flFiltroStatusCliente, String dsStatusClienteList) throws SQLException {
		super(Messages.CLIENTE_LABEL_STATUS);
		this.defaultItemType = DefaultItemType_ALL;
		carregaStatusPorFiltro(flFiltroStatusCliente, dsStatusClienteList);
	}

	public StatusCliente getStatusCliente() {
		return (StatusCliente)getSelectedItem();
	}

	public String getValue() {
		StatusCliente statusCliente = (StatusCliente)getSelectedItem();
		if (statusCliente != null) {
			return statusCliente.cdStatusCliente;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public void setValue(String value) {
		StatusCliente statusCliente = new StatusCliente();
		statusCliente.cdStatusCliente = value;
		select(statusCliente);
	}

	private void carregaStatus() throws SQLException {
		add(StatusClienteService.getInstance().findAll());
	}
	
	private void carregaStatusPorFiltro(String flFiltroStatusCliente, String dsStatusClienteList) throws SQLException {
		add(StatusClienteService.getInstance().findAllByFlFiltroStatusCliente(flFiltroStatusCliente, dsStatusClienteList));
		if (pop.lb.size() == 2) remove(0);
	}

	public void reloadByRepresentanteChange(String cdRepresentante) throws SQLException {
		removeAll();
		add(StatusClienteService.getInstance().findAllByCdRepresentante(cdRepresentante));
	}
}
