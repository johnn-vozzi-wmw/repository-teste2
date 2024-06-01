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
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ContaCorrenteCli;
import br.com.wmw.lavenderepda.business.service.ContaCorrenteCliService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListContaCorrenteCliForm extends BaseCrudListForm {

	public Cliente cliente;
	private EditText edCliente;
	private LabelValue lvVlTotDeb;
	private LabelValue lvVlTotCred;
	private LabelValue lvVlSaldoFinal;
	private Vector domainList;

    public ListContaCorrenteCliForm(Cliente cliente) throws SQLException {
        super(Messages.CONTACORRENTECLI_TITULO_LISTA);
        setBaseCrudCadForm(new CadContaCorrenteCliForm());
        this.cliente = cliente;
        edCliente = new EditText("@@@@@@@@@@", 75);
        if (cliente != null) {
        	edCliente.setValue(cliente.toString());
        }
        edCliente.setEditable(false);
        lvVlTotDeb = new LabelValue("999999999");
        lvVlTotCred = new LabelValue("999999999");
        lvVlSaldoFinal = new LabelValue("999999999");
        singleClickOn = true;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ContaCorrenteCliService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new ContaCorrenteCli();
    }

    //@Override
    protected Vector getDomainList() throws SQLException {
    	ContaCorrenteCli filter = new ContaCorrenteCli();
    	filter.cdEmpresa = this.cliente.cdEmpresa;
    	filter.cdRepresentante = this.cliente.cdRepresentante;
    	filter.cdCliente = this.cliente.cdCliente;
    	domainList = getCrudService().findAllByExample(filter);
    	calculaTotaisSaldo();
        return domainList;
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        ContaCorrenteCli contaCorrenteCli = (ContaCorrenteCli) domain;
        String[] item = {
                StringUtil.getStringValue(contaCorrenteCli.rowKey),
                StringUtil.getStringValue(contaCorrenteCli.nuDocumento),
                ValueUtil.isEmpty(contaCorrenteCli.dtMovimentacao) ? "-" : StringUtil.getStringValue(contaCorrenteCli.dtMovimentacao),
                StringUtil.getStringValueToInterface(contaCorrenteCli.vlCredito),
                StringUtil.getStringValueToInterface(contaCorrenteCli.vlDebito)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected BaseDomain getSelectedDomainOnGrid() {
        ContaCorrenteCli contaCorrenteCli = new ContaCorrenteCli();
        String[] item = gridEdit.getSelectedItem();
        contaCorrenteCli.rowKey = StringUtil.getStringValue(item[0]);
        contaCorrenteCli.nuDocumento = StringUtil.getStringValue(item[1]);
        contaCorrenteCli.dtMovimentacao = DateUtil.getDateValue(item[2]);
        contaCorrenteCli.vlCredito = ValueUtil.getDoubleValue(item[3]);
        contaCorrenteCli.vlDebito = ValueUtil.getDoubleValue(item[4]);
        return contaCorrenteCli;
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
        int oneChar = fm.charWidth('A');
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.CONTACORRENTECLI_LABEL_NUDOCUMENTO, oneChar * 5, LEFT),
            new GridColDefinition(Messages.CONTACORRENTECLI_LABEL_DTMOVIMENTACAO, oneChar * 10, LEFT),
            new GridColDefinition(Messages.CONTACORRENTECLI_LABEL_VLCREDITO, oneChar * 5, LEFT),
            new GridColDefinition(Messages.CONTACORRENTECLI_LABEL_VLDEBITO, oneChar * 5, LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, clienteContainer.getY2() + HEIGHT_GAP, FILL, FIT , clienteContainer);
    }

    private void calculaTotaisSaldo() {
    	double vlTotDeb = 0d;
    	double vlTotCred = 0d;
    	for (int i = 0; i < domainList.size(); i++) {
    		vlTotDeb += ((ContaCorrenteCli)domainList.items[i]).vlDebito;
    		vlTotCred += ((ContaCorrenteCli)domainList.items[i]).vlCredito;
    	}
    	lvVlTotDeb.setValue(vlTotDeb);
    	lvVlTotCred.setValue(vlTotCred);
    	lvVlSaldoFinal.setValue(vlTotCred - vlTotDeb);
    	if (lvVlSaldoFinal.getDoubleValue() == 0) {
    		lvVlSaldoFinal.setForeColor(ColorUtil.componentsForeColor);
    	} else if (lvVlSaldoFinal.getDoubleValue() < 0) {
    		lvVlSaldoFinal.setForeColor(ColorUtil.softRed);
    	} else {
    		lvVlSaldoFinal.setForeColor(ColorUtil.softBlue);
    	}
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	
    }

}
