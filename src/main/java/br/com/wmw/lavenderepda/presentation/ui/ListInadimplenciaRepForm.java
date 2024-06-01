package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.RelInadimpRep;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.service.RelInadimpRepService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListInadimplenciaRepForm extends LavendereCrudListForm {

	private Vector list;

    public ListInadimplenciaRepForm() throws SQLException {
        super(Messages.RELINADIMPREP_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadRelInadimpRepForm());
        singleClickOn = true;
        listContainer = new GridListContainer(4, 2, false, LavenderePdaConfig.usaScroolLateralListasProdutos);
        listContainer.setColPosition(3, RIGHT);
        listContainer.setColsSort(new String[][]{
        	{Messages.REPRESENTANTE_LABEL_CDREPRESENTANTE, "CDREPRESENTANTE"},
			{Messages.REPRESENTANTE_NOME_ENTIDADE, "NMREPRESENTANTE"},
			{Messages.RELINADIMPREP_LABEL_VLTITULOS, "VLTITULOS"},
			{Messages.TITULOFINANCEIRO_LABEL_QTTITULOS, "QTTITULOS"}});
        listContainer.setColTotalizerRight(2, Messages.RELINADIMPREP_LABEL_VLTITULOS);
        barBottomContainer.setVisible(false);
        listContainer.usaScroolHorizontal = LavenderePdaConfig.usaScroolLateralListasProdutos;
        configListContainer("CDREPRESENTANTE");
        
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return RelInadimpRepService.getInstance();
    }

	protected BaseDomain getDomainFilter() throws SQLException {
		RelInadimpRep relInadimpRep = new RelInadimpRep();
		relInadimpRep.cdEmpresa = SessionLavenderePda.cdEmpresa;
		return relInadimpRep;
	}

    @Override
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		list = getCrudService().findAllByExampleSummary(domain);
		return list;
	}
    
    private StringBuilder itens = new StringBuilder();

    @Override
    protected String[] getItem(Object domain) throws SQLException {
        RelInadimpRep relInadimpRep = (RelInadimpRep) domain;
        String[] s = new String[4];
        itens.setLength(0);
        itens.append(StringUtil.getStringValue(relInadimpRep.cdRepresentante)).append(" - ").append(StringUtil.getStringValue(relInadimpRep.nmRepresentante));
        s[0] = itens.toString();
        itens.setLength(0);
        
        itens.append(ValueUtil.VALOR_NI);
        s[1] = itens.toString();
        itens.setLength(0);
        
        itens.append(Messages.MOEDA + " ").append(StringUtil.getStringValueToInterface(relInadimpRep.vlTitulos));
        s[2] = itens.toString();
        itens.setLength(0);
        
        itens.append(StringUtil.getStringValueToInterface(relInadimpRep.qtTitulos)).append(" ").append(Messages.TITULOFINANCEIRO_LABEL_QTTITULOS);
        s[3] = itens.toString();
        itens.setLength(0);
        
        return s;
    }

    @Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, listContainer, LEFT, getTop(), FILL, FILL);
    }

    private void inadimpCliClick() throws SQLException {
    	Representante representanteSessao = SessionLavenderePda.getRepresentante();
		RelInadimpRep relInadimpRep = (RelInadimpRep)getSelectedDomain();
		SessionLavenderePda.setRepresentante(relInadimpRep.cdRepresentante, RepresentanteService.getInstance().getDescription(relInadimpRep.cdRepresentante));
		show(new ListInadimplenciaClienteForm());
		SessionLavenderePda.setRepresentante(representanteSessao);
    }

    @Override
    public void singleClickInList() throws SQLException {
    	inadimpCliClick();
    }

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}
	
}
