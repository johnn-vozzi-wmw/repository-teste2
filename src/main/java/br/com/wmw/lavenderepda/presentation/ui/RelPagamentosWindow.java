package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.CalendarWmw;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.service.CestaService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import totalcross.ui.Grid;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class RelPagamentosWindow extends WmwListWindow {

	private EditDate edDateInitial;
	private EditDate edDateFinal;
	public BaseButton btFiltrar;

    public RelPagamentosWindow() {
        super(Messages.PAGAMENTO_BOTAO_RELATORIO_PAGAMENTOS);
        edDateInitial = new EditDate();
        edDateFinal = new EditDate();
        btFiltrar = new BaseButton(FrameworkMessages.BOTAO_FILTRAR);
        setDefaultRect();
    }

    //@Override
    protected CrudService getCrudService() throws java.sql.SQLException {
        return CestaService.getInstance();
    }

    protected BaseDomain getDomainFilter() {
    	return new Pagamento();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
    	Pagamento pagamentoFilter = new Pagamento();
    	pagamentoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	pagamentoFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	pagamentoFilter.dtPagamentoInicialFilter = edDateInitial.getValue();
    	pagamentoFilter.dtPagamentoFinalFilter = edDateFinal.getValue();
    	return PagamentoService.getInstance().findAllGroupByTipoPagamento(pagamentoFilter);
    }

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
    	Pagamento pagamento = (Pagamento) domain;
    	String dsTipoPagamento = TipoPagamentoService.getInstance().getDsTipoPagamento(pagamento.cdTipoPagamento);
    	String[] item = {
    			StringUtil.getStringValue(dsTipoPagamento),
    			StringUtil.getStringValueToInterface(pagamento.vlPago)};
    	return item;
    }

    //@Override
    protected String getSelectedRowKey() {
        String[] item = grid.getSelectedItem();
        return item[0];
    }

    //--------------------------------------------------------------

    //@Override
    protected void onFormStart() {
		UiUtil.add(this, new LabelName(Messages.DATA_LABEL_DT), LEFT +  WIDTH_GAP , getTop() + WIDTH_GAP);
		UiUtil.add(this, edDateInitial, AFTER + WIDTH_GAP, SAME, PREFERRED);
		UiUtil.add(this, new LabelName("-"), AFTER +  WIDTH_GAP , SAME);
        UiUtil.add(this, edDateFinal, AFTER +  WIDTH_GAP, SAME, PREFERRED);
        edDateInitial.onlySelectByCalendar();
        edDateFinal.onlySelectByCalendar();
    	GridColDefinition[] gridColDefiniton = {
                new GridColDefinition(Messages.TIPOPAGTO_LABEL_TIPOPAGTO, -70, LEFT),
                new GridColDefinition(Messages.LABEL_VALOR, -30, LEFT)};
    	grid = UiUtil.createGridEdit(gridColDefiniton);
    	grid.setGridControllable();
        UiUtil.add(this, grid);
        grid.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
        grid.setGridControllable();
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target instanceof CalendarWmw) {
				validateFiltro();
				list();
			}
			break;
		}
    	}
    }

    public void list() throws java.sql.SQLException {
    	grid.gridController.clearColors();
    	super.list();
    	grid.qsort(0);
    	if (grid.size() > 0) {
    		Vector itens = grid.getItemsVector();
    		double total = 0;
    		for (int i = 0; i < itens.size(); i++) {
    			String[] item = (String[])itens.items[i];
    			total += ValueUtil.getDoubleValue(item[1]);
    		}
    		grid.add(new String[]{"TOTAL", StringUtil.getStringValueToInterface(total)});
    		grid.gridController.setRowBackColor(Color.getRGB(178, 196, 208), grid.size()-1);
    		Grid.repaint();
    	}
    }

    private void validateFiltro() {
    	if (ValueUtil.isNotEmpty(edDateInitial.getValue()) && ValueUtil.isNotEmpty(edDateFinal.getValue())) {
    		if (edDateInitial.getValue().isAfter(edDateFinal.getValue())) {
    			edDateInitial.setValue(null);
    			UiUtil.showWarnMessage(FrameworkMessages.MSG_VALIDACAO_DATA_INICIAL_INVALIDA);
    		}
    	}
    }

}
