package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.CestaService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelResumoPedidosWindow extends WmwListWindow {

	private Vector pedidoList;

	private EditText edDsRepresentante;
	private LabelValue lvDsStatusPedido;
	private EditText edDsOrigemPedido;
	private LabelValue lbDsCliente;
	private EditText edDateInitial;
	private EditText edDateFinal;

    public RelResumoPedidosWindow(Vector pedidoList) {
        super(Messages.PEDIDO_LABEL_RESUMOPEDIDOS);
        this.pedidoList = pedidoList;
        edDsRepresentante = new EditText("@@@@@@@@@", 100);
        lvDsStatusPedido = new LabelValue("@");
        edDsOrigemPedido = new EditText("@@@@@@@@@", 100);
        edDsOrigemPedido.setEnabled(false);
        lbDsCliente = new LabelValue("@");
        edDateInitial = new EditText("@@@@@@", 20);
        edDateFinal = new EditText("@@@@@@", 20);
        edDateInitial.setEnabled(false);
        edDateFinal.setEnabled(false);
    }

    //@Override
    protected CrudService getCrudService() throws java.sql.SQLException {
        return CestaService.getInstance();
    }

    protected BaseDomain getDomainFilter() {
    	return new Pedido();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
    	return PedidoService.getInstance().montaRelResumoPedidos(pedidoList);
    }

    //@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
    	String[] pedido = (String[]) domain;
    	String[] item = {
    			StringUtil.getStringValue(0),
    			StringUtil.getStringValue(pedido[0]),
    			StringUtil.getStringValue(pedido[1]),
    			StringUtil.getStringValue(pedido[2]),
    			StringUtil.getStringValue(pedido[3])};
    	return item;
    }

    //@Override
    protected String getSelectedRowKey() {
        String[] item = grid.getSelectedItem();
        return item[0];
    }

    public void setFilters(String status, String cliente, String origem, String dtInicial, String dtFinal) {
    	edDsRepresentante.setValue(SessionLavenderePda.getRepresentante().nmRepresentante);
        edDsOrigemPedido.setValue(OrigemPedido.getDsOrigemPedidoMessage(origem));
        lbDsCliente.setValue(cliente);
        edDateInitial.setValue(dtInicial);
        edDateFinal.setValue(dtFinal);
        lvDsStatusPedido.setValue(status);
    }

    //@Override
    protected void onFormStart() {
        UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CDSTATUSPEDIDO), lvDsStatusPedido, getLeft(), getTop() + HEIGHT_GAP);
        if (LavenderePdaConfig.usaFiltroOrigemPedidoListaPedidos()) {
        	UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_FLORIGEM), edDsOrigemPedido, getLeft(), AFTER + HEIGHT_GAP);
        }
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), edDsRepresentante, getLeft(), AFTER + HEIGHT_GAP);
        }
        if (ValueUtil.isNotEmpty(lbDsCliente.getText()) && !lbDsCliente.getText().equals(FiltroPedidoAvancadoForm.LABEL_TODOS)) {
        	UiUtil.add(this, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), lbDsCliente, getLeft(), AFTER + HEIGHT_GAP);
        }
    	//--
        if (ValueUtil.isNotEmpty(edDateInitial.getText()) || ValueUtil.isNotEmpty(edDateFinal.getText())) {
        	UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_DTEMISSAO), getLeft(), AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
        	UiUtil.add(this, edDateInitial, getLeft(), AFTER, PREFERRED - WIDTH_GAP);
        	UiUtil.add(this, edDateFinal, AFTER +  WIDTH_GAP, SAME, PREFERRED);
        }
        //--
        String colItens = ValueUtil.isEmpty(LavenderePdaConfig.mostraColunaQtdNaListaDePedidos) || ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.mostraColunaQtdNaListaDePedidos) ? Messages.ITEMPEDIDO_LABEL_QTITEMFISICO : LavenderePdaConfig.mostraColunaQtdNaListaDePedidos;
        int oneChar = fm.charWidth('A');
    	GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.PEDIDO_NOME_ENTIDADE, oneChar * 6, LEFT),
			new GridColDefinition(Messages.PEDIDO_LABEL_QTPEDIDO, oneChar * 2, RIGHT),
			new GridColDefinition(colItens, oneChar * 6, RIGHT),
			new GridColDefinition(Messages.PEDIDO_LABEL_VLTOTALPEDIDO, oneChar * 4, RIGHT)};
    	grid = UiUtil.createGridEdit(gridColDefiniton);
    	grid.setGridControllable();
        //--
        UiUtil.add(this, grid);
        grid.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
        grid.setGridControllable();
        grid.gridController.setRowForeColor(LavendereColorUtil.baseForeColorSystem, 2);
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException { }

}
