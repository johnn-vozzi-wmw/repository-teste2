package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.RelInadimpCli;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.RelInadimpCliService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListInadimplenciaClienteForm extends LavendereCrudListForm {

	private RepresentanteSupervComboBox cbRepresentante;
	private SessionContainer sessionContainer;
	
    public ListInadimplenciaClienteForm() throws SQLException {
        super(Messages.RELINADIMPCLI_TITULO_CADASTRO);
        singleClickOn = true;
        sessionContainer = new SessionContainer();
        listContainer = new GridListContainer(4, 2, false, LavenderePdaConfig.usaScroolLateralListasProdutos);
        listContainer.setColPosition(3, RIGHT);
		cbRepresentante = new RepresentanteSupervComboBox();
        cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
        if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
        	cbRepresentante.setSelectedIndex(0);
        }
        listContainer.setColsSort(new String[][]{
        	{Messages.PRODUTO_LABEL_CODIGO, "CDCLIENTE"},
			{Messages.CLIENTE_LABEL_NMRAZAOSOCIAL, "NMRAZAOSOCIAL"},
			{Messages.RELINADIMPREP_LABEL_VLTITULOS, "VLTITULOS"},
			{Messages.RELINADIMPCLI_LABEL_QTDIASMAIOR, "QTDIASMAIOR"}});
        listContainer.setColTotalizerRight(2, Messages.RELINADIMPREP_LABEL_TOTALINADIMP);
        barBottomContainer.setVisible(false);
        configListContainer("CDCLIENTE");
    }
    

    @Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, sessionContainer, LEFT, getTop() + HEIGHT_GAP, FILL, UiUtil.getControlPreferredHeight() * 5 / 2);
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(sessionContainer, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), getLeft(), HEIGHT_GAP);
    		UiUtil.add(sessionContainer, cbRepresentante, getLeft(), AFTER + HEIGHT_GAP);
    	} else {
    		UiUtil.add(sessionContainer, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), HEIGHT_GAP);
    	}
        UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return RelInadimpCliService.getInstance();
    }

	protected BaseDomain getDomainFilter() throws SQLException {
		RelInadimpCli relInadimpCli = new RelInadimpCli();
		relInadimpCli.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			relInadimpCli.cdRepresentante = cbRepresentante.getValue();
		} else {
			relInadimpCli.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		}
		return relInadimpCli;
	}
	
	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getCrudService().findAllByExampleSummary(domain);
	}

	private StringBuilder itens = new StringBuilder();
	
    @Override
    protected String[] getItem(Object domain) throws SQLException {
        RelInadimpCli relInadimpCli = (RelInadimpCli) domain;
        String[] s = new String[4];
        itens.setLength(0);
        itens.append(StringUtil.getStringValue(relInadimpCli.cdCliente)).append(" - ").append(StringUtil.getStringValue(relInadimpCli.nmRazaoSocial));
        s[0] = itens.toString();
        itens.setLength(0);
        
        itens.append(ValueUtil.VALOR_NI);
        s[1] = itens.toString();
        itens.setLength(0);
        
        itens.append(Messages.MOEDA + " ").append(StringUtil.getStringValueToInterface(relInadimpCli.vlTitulos));
        s[2] = itens.toString();
        itens.setLength(0);
        
        itens.append(StringUtil.getStringValueToInterface(relInadimpCli.qtDiasMaior)).append(" ").append(Messages.RELINADIMPCLI_LABEL_QTDIASMAIOR);
        s[3] = itens.toString();
        itens.setLength(0);
        
        return s;
    }

	public void singleClickInList() throws SQLException {
		financeiroClick();
	}

    private void financeiroClick() throws SQLException {
    	if (listContainer.getSelectedIndex() != -1) {
    		RelInadimpCli relInadimpCli = (RelInadimpCli)getSelectedDomain();
        	SessionLavenderePda.setCliente(ClienteService.getInstance().getCliente(relInadimpCli.cdEmpresa, relInadimpCli.cdRepresentante, relInadimpCli.cdCliente));
        	FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceiraDyn(SessionLavenderePda.getCliente());
        	if (fichaFinanceira != null) {
        		CadFichaFinanceiraDynForm cadFichaFinanceiraForm = new CadFichaFinanceiraDynForm(true);
        		cadFichaFinanceiraForm.edit(fichaFinanceira);
        		show(cadFichaFinanceiraForm);
        		cadFichaFinanceiraForm.setActiveTabTitulosItem();
        	} else {
        		UiUtil.showWarnMessage(Messages.MSG_CLIENTE_SEM_FICHAFINANCEIRA);
        	}
    	}
    }

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED :
			if (event.target == cbRepresentante) {
				if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
					SessionLavenderePda.setRepresentante(((SupervisorRep) cbRepresentante.getSelectedItem()).representante);
					list();
				}
			}
		}
	}

}
