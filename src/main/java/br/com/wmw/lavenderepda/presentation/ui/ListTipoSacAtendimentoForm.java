package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudPersonListForm;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.AtendimentoAtiv;
import br.com.wmw.lavenderepda.business.domain.Sac;
import br.com.wmw.lavenderepda.business.domain.TipoSacAtividade;
import br.com.wmw.lavenderepda.business.service.AtendimentoAtivService;
import br.com.wmw.lavenderepda.business.service.TipoSacAtividadeService;
import br.com.wmw.lavenderepda.business.service.UsuarioWebService;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.InvalidDateException;
import totalcross.util.Vector;

public class ListTipoSacAtendimentoForm extends BaseCrudPersonListForm {
	
	private Sac sac;
	private Vector listAtendimentoAtiv;

    public ListTipoSacAtendimentoForm(Sac sacSelecionado, Vector listAtendimentoAtiv) {
        super(Messages.SAC_ATENDIMENTO_TITULO);
        singleClickOn = false;
        listContainer = new GridListContainer(8, 2);
        listContainer.setUseSortMenu(false);
		listContainer.setBarTopSimple();
		listResizeable = false;
        this.sac = sacSelecionado;
        this.listAtendimentoAtiv = listAtendimentoAtiv;
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
    	TipoSacAtividade tipoSacAtividadeFilter = new TipoSacAtividade();
		tipoSacAtividadeFilter.cdEmpresa = sac.cdEmpresa;
		tipoSacAtividadeFilter.cdTipoSac = sac.cdTipoSac;
		Vector tipoSacAtividadeList = TipoSacAtividadeService.getInstance().findAllByExample(tipoSacAtividadeFilter);
		tipoSacFor : for (int i = 0; i < tipoSacAtividadeList.size(); i++) {
			TipoSacAtividade tipoSacAtividade = (TipoSacAtividade) tipoSacAtividadeList.items[i];
			int sizeAtend = listAtendimentoAtiv.size();
			for (int j = 0; j < sizeAtend; j++) {
				AtendimentoAtiv atendimentoAtiv = (AtendimentoAtiv) listAtendimentoAtiv.items[j];
				if (ValueUtil.valueEquals(atendimentoAtiv.cdAtividadeSac, tipoSacAtividade.cdAtividade) && ValueUtil.valueEquals(atendimentoAtiv.cdTipoSac, tipoSacAtividade.cdTipoSac)) {
					tipoSacAtividadeList.removeElement(tipoSacAtividade);
					i--;
					continue tipoSacFor;
				}
			}
		}
        return tipoSacAtividadeList;
    }
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
    	TipoSacAtividade tipoSacAtividade = (TipoSacAtividade)domain;
		Date dtBase = AtendimentoAtivService.getInstance().getDataAtendimento(sac);
		Date dtPrevisao = null;
		Date dtPrazoMax = null;
		try {
			dtPrevisao = new Date(dtBase.getDay(), dtBase.getMonth(), dtBase.getYear());
			dtPrazoMax = new Date(dtBase.getDay(), dtBase.getMonth(), dtBase.getYear());
		} catch (InvalidDateException e) {
			e.printStackTrace();
		}
		if (ValueUtil.isNotEmpty(dtPrevisao)) {
			DateUtil.addDay(dtPrevisao, tipoSacAtividade.nuDiasUteis);
		}
		if (ValueUtil.isNotEmpty(dtPrazoMax)) {
			DateUtil.addDay(dtPrazoMax, tipoSacAtividade.nuDiasPrazoMax);
		}
		
		//--
		String[] item = {
				StringUtil.getStringValue(tipoSacAtividade.cdAtividade),
				" - " + StringUtil.getStringValue(tipoSacAtividade.dsTitulo),
				StringUtil.getStringValue(UsuarioWebService.getInstance().getNmUsuario(tipoSacAtividade.cdUsuarioTipoSac)),
				StringUtil.getStringValue(""),
				StringUtil.getStringValue(Messages.SAC_LABEL_PREVISAO + " - "),
				StringUtil.getStringValue(dtPrevisao),
				StringUtil.getStringValue(Messages.SAC_LABEL_PRAZOMAX + " - "),
				StringUtil.getStringValue(dtPrazoMax),
		};
		return item;
    }
    
    @Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	TipoSacAtividade tipoSacAtividade = (TipoSacAtividade)domain;
    	return tipoSacAtividade.dsTitulo;
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, listContainer, LEFT, TOP, FILL, FILL);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }
    
}
