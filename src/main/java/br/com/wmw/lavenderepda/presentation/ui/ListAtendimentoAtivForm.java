package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudPersonListForm;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.service.AtendimentoAtivService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TipoSacAtividadeService;
import br.com.wmw.lavenderepda.business.service.UsuarioWebService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListAtendimentoAtivForm extends BaseCrudPersonListForm {
	
	private Sac sac;
	public Vector listAtendimentoAtiv;
	public boolean onListAtendAtivWindow;

    public ListAtendimentoAtivForm(Sac sacSelecionado, boolean onListAtendAtivWindow) throws SQLException {
        super(Messages.SAC_ATENDIMENTO_TITULO);
        setBaseCrudCadForm(new CadAtendimentoAtivForm(false));
        this.onListAtendAtivWindow = onListAtendAtivWindow;
        singleClickOn = true;
        listContainer = new GridListContainer(6, 2);
        listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		listResizeable = false;
        this.sac = sacSelecionado;
        sortAsc = "N, N";
        sortAtributte = "DTATENDIMENTO, HRATENDIMENTO";
        listAtendimentoAtiv = new Vector();
    }
    
    //@Override
    protected CrudService getCrudService() throws SQLException {
        return AtendimentoAtivService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() throws SQLException {
		AtendimentoAtiv domainFilter = new AtendimentoAtiv();
		return domainFilter;
	}
    
    //@Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	AtendimentoAtiv atendimentoAtivFilter = (AtendimentoAtiv) domain;
    	atendimentoAtivFilter.cdEmpresa = sac.cdEmpresa;
    	atendimentoAtivFilter.cdRepresentante = sac.cdRepresentante;
    	atendimentoAtivFilter.cdCliente = sac.cdCliente;
    	atendimentoAtivFilter.cdSac = sac.cdSac;
        return listAtendimentoAtiv = getCrudService().findAllByExample(atendimentoAtivFilter);
    }
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv)domain;
		String[] item = {
				StringUtil.getStringValue(atendimentoAtiv.cdAtendimentoAtividade),
				StringUtil.getStringValue(" - " + getDsAtividade(atendimentoAtiv)),
				StringUtil.getStringValue(getAtendenteAtividade(atendimentoAtiv)),
				StringUtil.getStringValue(""),
				StringUtil.getStringValue(atendimentoAtiv.dtAtendimento),
				StringUtil.getStringValue(" - " + atendimentoAtiv.getDsStatusAtendimento(atendimentoAtiv.cdStatusAtendimento)),
		};
		return item;
    }
    
    private String getAtendenteAtividade(AtendimentoAtiv atendimentoAtiv) throws SQLException {
    	String lbNmUsuarioSac = StringUtil.getStringValue(UsuarioWebService.getInstance().getNmUsuario(atendimentoAtiv.cdUsuarioAtendimento));
		if (ValueUtil.valueEquals(lbNmUsuarioSac, "")) {
			lbNmUsuarioSac = RepresentanteService.getInstance().findColumnByRowKey(atendimentoAtiv.cdUsuarioAtendimento + ";", "NMREPRESENTANTE");
		}	
		return lbNmUsuarioSac;
	}

	@Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv)domain;
    	return getDsAtividade(atendimentoAtiv);
    }
    
    private String getDsAtividade(AtendimentoAtiv atendimentoAtiv) throws SQLException {
		String dsAtividade = TipoSacAtividadeService.getInstance().getDsAtividade(atendimentoAtiv);
		if (ValueUtil.isEmpty(dsAtividade)) {
			dsAtividade = StringUtil.getStringAbreviada(atendimentoAtiv.dsObservacao, getWidth() - HEIGHT_GAP - getFont().fm.stringWidth(atendimentoAtiv.cdAtendimentoAtividade + " - "));
		}
    	return dsAtividade;
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }
    
    @Override
    public void singleClickInList() throws SQLException {
    	if (onListAtendAtivWindow) {
    		CadAtendimentoAtivWindow cadAtendimentoAtivWindow = new CadAtendimentoAtivWindow((AtendimentoAtiv) getSelectedDomain());
    		cadAtendimentoAtivWindow.popup();
    	} else {
    		super.singleClickInList();
    	}
    }
    
    @Override
    public void visibleState() {
    	super.visibleState();
    	if(onListAtendAtivWindow) {
    		barBottomContainer.setVisible(false);
    	}
    }
    
}
