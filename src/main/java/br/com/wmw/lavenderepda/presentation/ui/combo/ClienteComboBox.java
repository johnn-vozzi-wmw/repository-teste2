package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import totalcross.util.Vector;

public class ClienteComboBox extends BaseComboBox {
	
	public ClienteComboBox(String title) throws SQLException {
		super(title);
		carregaClientes();
	}
	
	public ClienteComboBox(String title, int defaultItemType) throws SQLException {
		super(title);
		this.defaultItemType = defaultItemType;
		carregaClientes();
	}

	public Cliente getCliente() {
		return (Cliente) getSelectedItem();
	}

	public String getValue() {
		Cliente cliente = (Cliente) getSelectedItem();
		return cliente != null ? cliente.cdCliente : null;
	}

	public void setValue(String value) {
		if (value == null) return;
		Cliente cliente = getClienteFilter();
		cliente.cdCliente = value;
		select(cliente);
	}
	
	private void carregaClientes() throws SQLException {
		if (SessionLavenderePda.usuarioPdaRep == null || SessionLavenderePda.getRepresentante().cdRepresentante == null) return;
		Cliente clienteFilter = getClienteFilter();
		Vector list = ClienteService.getInstance().findAllByExampleForCombo(clienteFilter);
		add(list);
	}

	private Cliente getClienteFilter() {
		Cliente clienteFilter = new Cliente();
		clienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		clienteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Cliente.class);
		return clienteFilter;
	}

	public void recarregaClientes() throws SQLException {
		removeAll();
		carregaClientes();
	}
	
}
