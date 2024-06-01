package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.VerbaSaldo;
import br.com.wmw.lavenderepda.business.service.VerbaContaCorService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import totalcross.ui.Label;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListVerbaSaldoForm extends BaseCrudListForm {

	private Label lbTotalSaldo;
	private EditText edTotalSaldo;
	private boolean isBarTopVisible;

    public ListVerbaSaldoForm(boolean isBarTopVisible) {
        super(Messages.VERBASALDO_NOME_ENTIDADE);
        lbTotalSaldo = new LabelName(Messages.VERBASALDO_LABEL_TOTALSALDO);
        edTotalSaldo = new EditText("@@@@@@@", 20);
        this.isBarTopVisible = isBarTopVisible;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return VerbaSaldoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new VerbaSaldo();
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
        Vector list =  ((VerbaSaldoService)getCrudService()).findAllGrupoByContaC();
        updateTotalizadores(list);
        return list;
    }

    private void updateTotalizadores(Vector list) throws SQLException {
    	double vlTotalPedidos = ((VerbaSaldoService)getCrudService()).findTotalSaldoVerba(list);
        edTotalSaldo.setValue(StringUtil.getStringValueToInterface(vlTotalPedidos));
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        VerbaSaldo verbaSaldo = (VerbaSaldo) domain;
        String[] item = {
                StringUtil.getStringValue(verbaSaldo.rowKey),
                StringUtil.getStringValue(VerbaContaCorService.getInstance().getDescricaoContaCorrente(verbaSaldo.cdContaCorrente)),
                StringUtil.getStringValue(verbaSaldo.dtVigenciaInicial),
                StringUtil.getStringValue(verbaSaldo.dtVigenciaFinal),
                StringUtil.getStringValueToInterface(verbaSaldo.vlSaldoInicial),
                StringUtil.getStringValueToInterface(verbaSaldo.vlSaldo)};
        return item;
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	boolean useVigencia = LavenderePdaConfig.usaVigenciaNaVerbaPorItemTabPreco();
    	if (!useVigencia) {
    		UiUtil.add(this, lbTotalSaldo, CENTER, BOTTOM - HEIGHT_GAP);
    		UiUtil.add(this, edTotalSaldo, AFTER + HEIGHT_GAP, SAME);
    		edTotalSaldo.setText("0,00");
    		edTotalSaldo.setEditable(false);
    	}
        //--
        int ww = fm.stringWidth("www");
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, ww, LEFT),
            new GridColDefinition(Messages.VERBASALDO_LABEL_CDCONTACORRENTE, useVigencia ? 0 : ww * 2, LEFT),
            new GridColDefinition(Messages.VERBASALDO_LABEL_VIGENCIA_INICIAL, useVigencia ? ww * 3 : 0, LEFT),
            new GridColDefinition(Messages.VERBASALDO_LABEL_VIGENCIA_FINAL, useVigencia ? ww * 3 : 0, LEFT),
            new GridColDefinition(Messages.VERBASALDO_LABEL_VLSALDOINICIAL, useVigencia ? ww * 3 : 0, LEFT),
            new GridColDefinition(Messages.VERBASALDO_LABEL_VLSALDO, ww * 2, LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, getTop(), FILL, FILL - lbTotalSaldo.getPreferredHeight());
    }

    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    	barTopContainer.setVisible(isBarTopVisible);
    }

    protected int getTop() {
    	if (isBarTopVisible) {
    		return super.getTop();
    	} else {
    		return TOP + WIDTH_GAP;
    	}
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	
    }

}
