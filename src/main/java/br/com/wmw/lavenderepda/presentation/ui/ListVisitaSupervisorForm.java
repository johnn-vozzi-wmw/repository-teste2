package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.VisitaSupervisor;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.VisitaSupervisorService;
import br.com.wmw.lavenderepda.presentation.ui.ext.FechamentoDiarioUtil;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListVisitaSupervisorForm extends BaseCrudListForm {

    public ListVisitaSupervisorForm() throws SQLException {
        super(Messages.VISITA_TITULO_CADASTRO);
        setBaseCrudCadForm(new CadVisitaSupervisorForm());
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return VisitaSupervisorService.getInstance();
    }

    protected String getDataSource() throws SQLException {
        return getCrudService().findAllByExampleToGrid(new VisitaSupervisor());
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new VisitaSupervisor();
    }

    protected Vector getDomainList() throws java.sql.SQLException {
        return getCrudService().findAll();
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        VisitaSupervisor visita = (VisitaSupervisor) domain;
        String[] item = {
                StringUtil.getStringValue(visita.rowKey),
                StringUtil.getStringValue(visita.dtVisita),
                StringUtil.getStringValue(visita.hrVisita),
                StringUtil.getStringValue(visita.cdCliente),
                StringUtil.getStringValue(visita.getNmRazaoSocial())};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(barBottomContainer, btNovo , 5);
    	//--
        int ww = fm.stringWidth("www");
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.DATA_LABEL_DATA, fm.stringWidth("99/99/99999"), LEFT),
            new GridColDefinition(Messages.DATA_LABEL_HORA, fm.stringWidth("99:999"), LEFT),
            new GridColDefinition(Messages.CLIENTE_NOME_ENTIDADE, ww * 2, LEFT),
            new GridColDefinition(Messages.CLIENTE_NOME_ENTIDADE, ww * 6, LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, getTop(), FILL, FILL - barBottomContainer.getHeight());
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	
    }
    
    @Override
    public void novoClick() throws SQLException {
    	if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario() || ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn()) return; 
    	
    	super.novoClick();
    }

}
