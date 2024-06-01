package br.com.wmw.lavenderepda.presentation.ui;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipo;

import java.sql.SQLException;

public class ListClienteWindow extends WmwWindow {

	public ListClienteForm listClienteForm;
	public Cliente cliente;

	public ListClienteWindow() throws SQLException {
		this(false, false, false);
	}
	public ListClienteWindow(boolean flNovoPedido, boolean flReplicaoPedido, boolean flNovaAgendaVisita) throws SQLException {
		this(flNovoPedido, flReplicaoPedido, flNovaAgendaVisita, null);
	}
	
	public ListClienteWindow(boolean flNovoPedido, boolean flReplicaoPedido, boolean flNovaAgendaVisita, Cliente cliente) throws SQLException {
		super((flNovoPedido || flNovaAgendaVisita) ? Messages.NOVO_PEDIDO_SELECIONAR_CLIENTE : Messages.CLIENTE_NOME_ENTIDADE);
		listClienteForm = new ListClienteForm(true, cliente, null);
		listClienteForm.onSelectClienteNovoPedido = flNovoPedido;
		listClienteForm.onReplicacaoPedido = flReplicaoPedido;
		listClienteForm.onSelectClienteNovaAgendaVisita = flNovaAgendaVisita;
		scrollable = false;
		setDefaultRect();
	}
	
	public void initUI() {
		super.initUI();
		listClienteForm.setClienteForm(this);
		UiUtil.add(this, listClienteForm, LEFT, getTop(), FILL, FILL - footerH);
		listClienteForm.visibleState();
		listClienteForm.showRequestedFocus();
	}

	public ListClienteWindow(RequisicaoServTipo requisicaoServTipo) throws SQLException {
		super(Messages.CLIENTE_NOME_ENTIDADE);
		listClienteForm = new ListClienteForm(true, null, requisicaoServTipo);
		scrollable = false;
		setDefaultRect();
	}

	@Override
	protected void onUnpop() {
		super.onUnpop();
		listClienteForm.marcadoresCliHash = null;
	}

}
