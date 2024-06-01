package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwComboBoxListWindow;
import br.com.wmw.framework.presentation.ui.ext.ComboBoxMultiLine;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ClienteEndAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteEndereco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ClienteEndAtuaService;
import br.com.wmw.lavenderepda.business.service.ClienteEnderecoService;
import br.com.wmw.lavenderepda.presentation.ui.ListClienteEnderecoWindow;
import totalcross.util.Vector;

public class ClienteEnderecoComboBoxMultiLine extends ComboBoxMultiLine {
	
	public String cdClienteEntrega;
	public boolean indicaClienteEntrega;
	public boolean isEnderecoCobranca;
	public Pedido pedido;
	
	public ClienteEnderecoComboBoxMultiLine() {
		super();
	}
	
	public ClienteEnderecoComboBoxMultiLine(boolean indicaClienteEntrega) {
		this();
		this.indicaClienteEntrega = indicaClienteEntrega;
	}

	public ClienteEnderecoComboBoxMultiLine(boolean indicaClienteEntrega, boolean isEnderecoCobranca) {
		this();
		this.indicaClienteEntrega = indicaClienteEntrega;
		this.isEnderecoCobranca = isEnderecoCobranca;
	}
	
	public ClienteEnderecoComboBoxMultiLine(Pedido pedido) {
		this();
		this.pedido = pedido;
	}

	@Override
	protected String[] getDescricao() {
		ClienteEndereco clienteEndereco = (ClienteEndereco) getDomainSelected();
		if (clienteEndereco != null) {
			return new String[] {StringUtil.getStringValue(clienteEndereco.dsLogradouro), StringUtil.getStringValue(clienteEndereco.dsBairro), StringUtil.getStringValue(clienteEndereco.dsCidade), StringUtil.getStringValue(clienteEndereco.dsEstado)};
		}
		return null;
	}

	@Override
	protected WmwComboBoxListWindow getListWindow() {
		if (window == null) {
			if (pedido != null && pedido.isPedidoNovoCliente()) {
				window = new ListClienteEnderecoWindow(this, indicaClienteEntrega, cdClienteEntrega, pedido);
				
			} else {
				window = isEnderecoCobranca ? new ListClienteEnderecoWindow(this, isEnderecoCobranca) : new ListClienteEnderecoWindow(this, indicaClienteEntrega, cdClienteEntrega);
			}
		}
		return window;
	}
	
	public void carregaClienteEnderecoPadrao(final Pedido pedido, boolean isEnderecoCobranca) throws SQLException {
		if (pedido != null) {
			this.pedido = pedido;
			ClienteEndereco clienteEnderecoPadrao = ClienteEnderecoService.getInstance().getClienteEnderecoPadrao(pedido, isEnderecoCobranca);
			if (clienteEnderecoPadrao != null) {
				setDomainSelected(clienteEnderecoPadrao);
			} else {
				cleanDomainSelected();
			}
		}
	}
	
	public String getValue() throws SQLException  {
		try {
			ClienteEndereco clienteEndereco = (ClienteEndereco) getDomainSelected();
			if (LavenderePdaConfig.isApresentaEnderecoAtualizadoEntrega()) {
				ClienteEndAtua cliEndAtua = new ClienteEndAtua();
				cliEndAtua.cdCliente = clienteEndereco.cdCliente;
				cliEndAtua.cdEmpresa = clienteEndereco.cdEmpresa;
				cliEndAtua.cdRepresentante = clienteEndereco.cdRepresentante;
				cliEndAtua.cdEndereco = clienteEndereco.cdEndereco;
				Vector listClienteEndAtua;
					listClienteEndAtua = ClienteEndAtuaService.getInstance().findAllByExample(cliEndAtua);
				if (listClienteEndAtua.size() < 1) {
					cliEndAtua.cdEndereco = null;
					cliEndAtua.cdRegistro = clienteEndereco.cdEndereco;
					listClienteEndAtua = ClienteEndAtuaService.getInstance().findAllByExample(cliEndAtua);
				}
				clienteEndereco = ClienteEnderecoService.getInstance().getCliEnderecoAtua(clienteEndereco, listClienteEndAtua);
			}
			setDomainSelected(clienteEndereco);
			if (clienteEndereco != null) {
				return clienteEndereco.cdEndereco;
			}
		} catch (NullPointerException e) {
			throw new ValidationException(Messages.ERRO_SELECAO_ENDERECO_PEDIDO);
		}
		return null;
	}
	
	public ClienteEndereco getClienteEndereco() {
		ClienteEndereco clienteEndereco = (ClienteEndereco) getDomainSelected();
		if (clienteEndereco != null) {
			return clienteEndereco;
		}
		return null;
	}
	
	public void setValue(String cdEmpresa, String cdRepresentante, String cdCliente, String cdEnderecoCliente) throws SQLException {
		setDomainSelected(ClienteEnderecoService.getInstance().getClienteEnderecoPedido(cdEmpresa, cdRepresentante, cdCliente, cdEnderecoCliente));
	}
	
	public void setValue(String cdEmpresa, String cdRepresentante, String cdCliente, String cdEnderecoCliente, Pedido pedido) throws SQLException {
		this.pedido = pedido;
		setDomainSelected(ClienteEnderecoService.getInstance().getClienteEnderecoPedido(cdEmpresa, cdRepresentante, cdCliente, cdEnderecoCliente, pedido));
	}
	
	public void reloadListEnderecoWindow() throws SQLException {
		ListClienteEnderecoWindow listClienteEnderecoWindow = (ListClienteEnderecoWindow)getListWindow();
		listClienteEnderecoWindow.cdClienteEntrega = this.cdClienteEntrega;
		listClienteEnderecoWindow.indicaClienteEntrega = this.indicaClienteEntrega;
		listClienteEnderecoWindow.list();
	}
	
	@Override
	protected void openWindow() throws SQLException {
		if (!indicaClienteEntrega || (indicaClienteEntrega && ValueUtil.isNotEmpty(cdClienteEntrega))) {
			super.openWindow();
		} else {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_CLIENTE_ENTREGA_NAO_SELECIONADO);
		}
	}
	
	@Override
	public void cleanDomainSelected() {
		super.cleanDomainSelected();
		setDomainSelected(null);
		cdClienteEntrega = null;
	}
	
	public void populatePedido(Pedido pedido) {
		this.pedido = pedido;
	}

}
