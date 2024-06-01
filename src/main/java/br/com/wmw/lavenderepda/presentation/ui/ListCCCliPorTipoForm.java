package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.CCCliPorTipo;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.service.CCCliPorTipoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListCCCliPorTipoForm extends BaseCrudListForm {

	private EditText edCliente;
	private LabelValue lvVlTotDeb;
	private LabelValue lvVlTotCred;
	private LabelValue lvVlSaldoFinal;
	private Vector domainList;
	//--
	public Cliente cliente;

    public ListCCCliPorTipoForm(Cliente cliente) {
        super(Messages.CONTACORRENTECLI_TITULO_LISTA);
        //--
        this.cliente = cliente;
        edCliente = new EditText("@@@@@@@@@@", 75);
        if (cliente != null) {
        	edCliente.setValue(cliente.toString());
        }
        edCliente.setEditable(false);
        lvVlTotDeb = new LabelValue("999999999");
        lvVlTotCred = new LabelValue("999999999");
        lvVlSaldoFinal = new LabelValue("999999999");
        //--
        singleClickOn = false;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return CCCliPorTipoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new CCCliPorTipo();
    }

    //@Override
    protected Vector getDomainList() throws SQLException {
		CCCliPorTipo cCCliPorTipo = new CCCliPorTipo();
		cCCliPorTipo.cdEmpresa = this.cliente.cdEmpresa;
		cCCliPorTipo.cdRepresentante = this.cliente.cdRepresentante;
		cCCliPorTipo.cdCliente = this.cliente.cdCliente;

		domainList = getCrudService().findAllByExample(cCCliPorTipo);
		calculaTotaisSaldo();
		return domainList;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        CCCliPorTipo cCCliPorTipo = (CCCliPorTipo) domain;
        String[] item = {
        		StringUtil.getStringValue(cCCliPorTipo.rowKey),
        		StringUtil.getStringValue(cCCliPorTipo.nuDocumento),
        		StringUtil.getStringValueToInterface(cCCliPorTipo.vlQuebra),
        		StringUtil.getStringValueToInterface(cCCliPorTipo.vlMortalidade),
        		StringUtil.getStringValueToInterface(cCCliPorTipo.vlPeso),
        		StringUtil.getStringValue(cCCliPorTipo.dtDocumento)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	//--
    	UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_VLSALDOFINAL), BaseUIForm.CENTEREDLABEL, BOTTOM - (barBottomContainer.getHeight() + HEIGHT_GAP));
    	UiUtil.add(this, lvVlSaldoFinal, AFTER + WIDTH_GAP, SAME);
    	UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_VLTOTDEBITO), BaseUIForm.CENTEREDLABEL, BEFORE - HEIGHT_GAP);
    	UiUtil.add(this, lvVlTotDeb, AFTER + WIDTH_GAP, SAME);
    	UiUtil.add(this, new LabelName(Messages.CONTACORRENTECLI_LABEL_VLTOTCREDITO), BaseUIForm.CENTEREDLABEL, BEFORE - HEIGHT_GAP);
    	UiUtil.add(this, lvVlTotCred, AFTER + WIDTH_GAP, SAME);
    	//--
        int ww = fm.stringWidth("www");
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
            new GridColDefinition(Messages.CCCLIPORTIPO_LABEL_NUDOCUMENTO, ww * 2, LEFT),
            new GridColDefinition(Messages.CCCLIPORTIPO_LABEL_VLQUEBRA, ww * 2, LEFT),
            new GridColDefinition(Messages.CCCLIPORTIPO_LABEL_VLMORTALIDADE, ww * 2, LEFT),
            new GridColDefinition(Messages.CCCLIPORTIPO_LABEL_VLPESO, ww * 2, LEFT),
            new GridColDefinition(Messages.CCCLIPORTIPO_LABEL_DTDOCUMENTO, ww * 3, LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, clienteContainer.getY2(), FILL, FIT , clienteContainer);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    }

    private void calculaTotaisSaldo() {
    	double vlTotDeb = 0d;
    	double vlTotCred = 0d;
    	for (int i = 0; i < domainList.size(); i++) {
    		if (((CCCliPorTipo)domainList.items[i]).vlMortalidade < 0) {
    			vlTotDeb += ((CCCliPorTipo)domainList.items[i]).vlMortalidade;
    		} else {
    			vlTotCred += ((CCCliPorTipo)domainList.items[i]).vlMortalidade;
    		}
    		if (((CCCliPorTipo)domainList.items[i]).vlPeso < 0) {
    			vlTotDeb += ((CCCliPorTipo)domainList.items[i]).vlPeso;
    		} else {
    			vlTotCred += ((CCCliPorTipo)domainList.items[i]).vlPeso;
    		}
    		if (((CCCliPorTipo)domainList.items[i]).vlQuebra < 0) {
    			vlTotDeb += ((CCCliPorTipo)domainList.items[i]).vlQuebra;
    		} else {
    			vlTotCred += ((CCCliPorTipo)domainList.items[i]).vlQuebra;
    		}
    	}
    	vlTotCred = vlTotCred < 0 ? vlTotCred * -1 : vlTotCred;
    	vlTotDeb = vlTotDeb < 0 ? vlTotDeb * -1 : vlTotDeb;
    	lvVlTotDeb.setValue(vlTotDeb);
    	lvVlTotCred.setValue(vlTotCred);
    	lvVlSaldoFinal.setValue(vlTotCred - vlTotDeb);
    	if (lvVlSaldoFinal.getDoubleValue() == 0) {
    		lvVlSaldoFinal.setForeColor(ColorUtil.componentsForeColor);
    	} else if (lvVlSaldoFinal.getDoubleValue() < 0) {
    		lvVlSaldoFinal.setForeColor(ColorUtil.softRed);
    	} else {
    		lvVlSaldoFinal.setForeColor(ColorUtil.softGreen);
    	}
    }

}
