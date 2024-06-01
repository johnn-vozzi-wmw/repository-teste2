package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ValorIndicador;
import br.com.wmw.lavenderepda.business.service.IndicadorService;
import br.com.wmw.lavenderepda.business.service.ValorIndicadorService;
import totalcross.ui.event.Event;

public class CadValorIndicadorForm extends BaseCrudCadForm {

    private LabelValue lbIndicador;
    private LabelValue lbDsVlIndicador;

    public CadValorIndicadorForm() {
        super(Messages.VALORINDICADOR_TITULO_CADASTRO);
        lbIndicador = new LabelValue("@@@@@@@@@@@@@@@@@");
        lbDsVlIndicador = new LabelValue("@@@@@@@@@@@@@@@@@");
        //--
        setReadOnly();
    }

    //-----------------------------------------------

    //@Override
    protected String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ValorIndicadorService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new ValorIndicador();
    }

    protected void visibleState() throws SQLException {
    	barBottomContainer.setVisible(false);
    	btExcluir.setVisible(false);
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        ValorIndicador valorIndicador = (ValorIndicador) getDomain();
        valorIndicador.dsVlIndicador = lbDsVlIndicador.getValue();
        return valorIndicador;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        ValorIndicador valorIndicador = (ValorIndicador) domain;
        lbIndicador.setValue(IndicadorService.getInstance().getDescription(valorIndicador.cdIndicador));
        lbDsVlIndicador.setValue(valorIndicador.dsVlIndicador);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
        lbIndicador.setText("");
        lbDsVlIndicador.setText("");
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), new LabelValue(SessionLavenderePda.getRepresentante().toString()), getLeft(), getTop() + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_LABEL_INDICADOR), lbIndicador, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, new LabelName(Messages.VALORINDICADOR_LABEL_DSVLINDICADOR), lbDsVlIndicador, getLeft(), AFTER + HEIGHT_GAP);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

}
