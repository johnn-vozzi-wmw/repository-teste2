package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.RelInadimpRep;
import br.com.wmw.lavenderepda.business.service.RelInadimpRepService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import totalcross.ui.event.Event;

public class CadRelInadimpRepForm extends BaseCrudCadForm {

	private LabelValue lbRepresentante;
    private LabelValue lbQtTitulos;
    private LabelValue lbVlTitulos;

    public CadRelInadimpRepForm() {
        super(Messages.RELINADIMPREP_TITULO_CADASTRO);
        lbQtTitulos = new LabelValue("@@@@@@@@@@@");
        lbVlTitulos = new LabelValue("@@@@@@@@@@@");
        setReadOnly();
    }

    //-----------------------------------------------

    //@Override
    protected String getEntityDescription() {
    	return Messages.RELINADIMPREP_TITULO_CADASTRO;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return RelInadimpRepService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new RelInadimpRep();
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        RelInadimpRep relInadimpRep = (RelInadimpRep) getDomain();
        return relInadimpRep;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        RelInadimpRep relInadimpRep = (RelInadimpRep) domain;
    	lbRepresentante.setValue(RepresentanteService.getInstance().getDescriptionWithId(relInadimpRep.cdRepresentante));
        lbQtTitulos.setValue(relInadimpRep.qtTitulos);
        lbVlTitulos.setValue(relInadimpRep.vlTitulos);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        lbQtTitulos.setText("");
        lbVlTitulos.setText("");
    }

    //@Override
    protected void onFormStart() throws SQLException {
        UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), lbRepresentante = new LabelValue(), getLeft(), getTop() + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.TITULOFINANCEIRO_LABEL_QTTITULOS), lbQtTitulos, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.RELINADIMPREP_LABEL_VLTITULOS), lbVlTitulos, getLeft(), AFTER + HEIGHT_GAP);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }
}
