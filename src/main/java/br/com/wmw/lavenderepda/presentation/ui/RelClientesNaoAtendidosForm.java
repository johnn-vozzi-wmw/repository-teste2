package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConexaoPda;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.SupervisorRepService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelClientesNaoAtendidosForm extends LavendereCrudListForm {

	public RelClientesNaoAtendidosForm() {
        super(Messages.CLIENTE_SEM_PEDIDO_RELATORIO);
        listContainer = new GridListContainer(2, 1);
		listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		singleClickOn = true;
    }

    protected CrudService getCrudService() throws SQLException {
        return SupervisorRepService.getInstance();
    }

    protected Vector getDomainList(BaseDomain domain) throws SQLException {
		SupervisorRep supervisorRepFilter = new SupervisorRep();
		supervisorRepFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		supervisorRepFilter.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector listSupRepresentantes = SupervisorRepService.getInstance().findAllByExample(supervisorRepFilter);
		Vector listSupRepFinal = new Vector();
		if (listSupRepresentantes != null) {
			int sizeListRepresentantes = listSupRepresentantes.size();
			SupervisorRep supervisorRep;
			for (int i = 0; i < sizeListRepresentantes; i++) {
				supervisorRep = (SupervisorRep)listSupRepresentantes.items[i];
				supervisorRep.representante = new Representante();
				supervisorRep.representante.cdRepresentante = supervisorRep.cdRepresentante;
				supervisorRep.representante.nmRepresentante = RepresentanteService.getInstance().getDescription(supervisorRep.cdRepresentante);
		    	Cliente clienteExample = new Cliente();
		    	clienteExample.cdEmpresa = supervisorRep.cdEmpresa;
		    	clienteExample.cdRepresentante = supervisorRep.cdRepresentante;
		    	clienteExample.nuDiasSemPedido = LavenderePdaConfig.getNuDiasAlertaBloqueioClienteSemPedido();
		    	supervisorRep.nuClientesSemPedidos = ClienteService.getInstance().countByExample(clienteExample);
		    	if (supervisorRep.nuClientesSemPedidos > 0) {
		    		listSupRepFinal.addElement(supervisorRep);
		    	}
			}
		}
		listSupRepFinal.qsort();
		return listSupRepFinal;
    }

    protected String[] getItem(Object domain) throws SQLException {
    	SupervisorRep supervisorRep = (SupervisorRep) domain;
		String[] item = {
				StringUtil.getStringValue(supervisorRep.representante.nmRepresentante),
				Messages.CLIENTE_SEM_PEDIDO_QTDE_DIAS_RELATORIO + " " + StringUtil.getStringValueToInterface(supervisorRep.nuClientesSemPedidos)};
		return item;
	}

    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    }

    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	protected BaseDomain getDomainFilter() throws SQLException {
		return new ConexaoPda();
	}

	protected void onFormStart() throws SQLException {
		UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL);
	}

	protected void onFormEvent(Event event) throws SQLException {
	}

	public void singleClickInList() throws SQLException {
		SupervisorRep domain = (SupervisorRep)getSelectedDomain();
		domain.representante = new Representante();
		domain.representante.cdRepresentante = domain.cdRepresentante;
		domain.representante.nmRepresentante = RepresentanteService.getInstance().getDescription(domain.cdRepresentante);
		SessionLavenderePda.setRepresentante(((SupervisorRep)domain).representante);
		ListClienteForm listClienteForm = new ListClienteForm(false, null, null);
		listClienteForm.ckClientesAtrasados.setChecked(true);
		listClienteForm.flListInicialized = true;
		listClienteForm.fromRelClientesNaoAtendidosForm = true;
		show(listClienteForm);
	}

}