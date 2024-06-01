package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class CadClienteEnderecoWindow extends WmwCadWindow {

	CadClienteEnderecoForm cadClienteEnderecoForm;
	ButtonPopup btSelecionar;
	boolean novoCliente;
	
	public CadClienteEnderecoWindow(boolean novoCliente) {
		super(Messages.CLIENTEENDERECO_NOME_ENTIDADE);
		this.novoCliente = novoCliente;
		this.cadClienteEnderecoForm = new CadClienteEnderecoForm(true, true);
		cadClienteEnderecoForm.setReadOnly();
    	btSelecionar = new ButtonPopup(Messages.BOTAO_SELECIONAR);
		setReadOnly();
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		super.initUI();
		UiUtil.add(this, cadClienteEnderecoForm, LEFT, getTop(), FILL, FILL);
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {	
		return null; 
	}

	@Override
	protected void domainToScreen(BaseDomain domain) throws SQLException { }

	@Override
	protected void clearScreen() throws SQLException { }

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return null;
	}

	@Override
	protected String getEntityDescription() {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}
	
	@Override
	protected int getTop() {
		return TOP;
	}

	@Override
	public void edit(BaseDomain domain) throws SQLException {
		super.edit(domain);
		try {
			cadClienteEnderecoForm.domainToScreen(domain);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}


	@Override
	protected void addButtons() {
		super.addButtons();
		addButtonPopup(btSelecionar);
	}
	
	@Override
	public void onWindowEvent(Event event) throws SQLException {
		super.onWindowEvent(event);
		try {
			switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btSelecionar) {
					if (getBaseListWindow() != null) {
						getBaseListWindow().unpop();
					}
					fecharWindow();
				}
				break;
			}
			}
		} catch (Exception ex) {
			ExceptionUtil.handle(ex);
		}
	}

}
