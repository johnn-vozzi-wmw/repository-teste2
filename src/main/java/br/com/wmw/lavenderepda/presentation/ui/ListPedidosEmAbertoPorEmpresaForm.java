package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PedidosEmAbertoPorEmpresaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.EmpresaClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.EmpresaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.sys.SpecialKeys;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class ListPedidosEmAbertoPorEmpresaForm extends LavendereCrudListForm {
	
	private ButtonAction btNovoPedido;
	private EmpresaComboBox cbEmpresa;
	private LabelName lbCliente;
	private LabelName lbData;
	public EditDate edDateInitial;
	public EditDate edDateFinal;
	public boolean mudouEmpresaSemSelecionarCliente;

    public ListPedidosEmAbertoPorEmpresaForm() throws SQLException {
    	super(Messages.PEDIDOS_POR_EMPRESA);
        setBaseCrudCadForm(new CadPedidoForm());
        singleClickOn = true;
        inicializaComponentes();       
    }

    @Override
    protected CrudService getCrudService() {
        return PedidosEmAbertoPorEmpresaService.getInstance(); 
    }
    
    protected BaseDomain getDomainFilter() {
		Pedido pedido = new Pedido();
    	pedido.cdStatusPedido = LavenderePdaConfig.cdStatusPedidoAberto;
    	pedido.cdEmpresa = cbEmpresa.getValue();
    	pedido.dsClienteFilter = getDsFiltroClientePedido();
    	pedido.dtEmissaoInicialFilter = edDateInitial.getValue();
    	pedido.dtEmissaoFinalFilter = edDateFinal.getValue();
		return pedido;
	}
    
    private String getDsFiltroClientePedido() {
		String dsFiltro =  edFiltro.getValue();
    	if (LavenderePdaConfig.usaPesquisaInicioString && !ValueUtil.isEmpty(dsFiltro)) {
    		if (dsFiltro.startsWith("*")) {
    			dsFiltro = "%" + dsFiltro.substring(1);
    		} else {
    			dsFiltro = "%" + dsFiltro;
    		}
    	}
    	return dsFiltro;
	}
    
    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        Pedido pedido = (Pedido) domain;
        String[] item = {
                StringUtil.getStringValue(pedido.nuPedido),
                StringUtil.getStringValue(pedido.getEmpresa()),
                StringUtil.getStringValue(pedido.dtEmissao),
                ValueUtil.VALOR_NI,
                StringUtil.getStringValue(pedido.getCliente()),
                Messages.ITEMPEDIDO_LABEL_TOTALVENDA + StringUtil.getStringValueToInterface(pedido.vlTotalPedido)};
        return item;
    }

    @Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }
    
    private void inicializaComponentes() throws SQLException {
    	btNovoPedido = new ButtonAction(Messages.BOTAO_NOVO_PEDIDO, "images/novopedido.png");
    	
    	cbEmpresa = new EmpresaComboBox(Messages.EMPRESA_NOME_ENTIDADE, BaseComboBox.DefaultItemType_ALL);
    	cbEmpresa.loadEmpresa();
    	
    	lbCliente = new LabelName(Messages.LABEL_CLIENTE);
    	
    	lbData = new LabelName(Messages.DATA_LABEL_DATA);
    	edDateInitial = new EditDate();
    	edDateFinal = new EditDate();
    	edDateInitial.onlySelectByCalendar();
    	edDateFinal.onlySelectByCalendar();
    	
    	ScrollPosition.AUTO_HIDE = true;
    	
    	listContainer = new GridListContainer(6, 2);
    	listContainer.setColsSort(getSortColumns());
    	configListContainer("NMRAZAOSOCIAL");
    	listContainer.setColPosition(1, RIGHT);
    	listContainer.setColPosition(3, RIGHT);
    	listContainer.setColPosition(5, RIGHT);
    }

	private String[][] getSortColumns() {
		return new String[][]{
			{Messages.CLIENTE_NOME_ENTIDADE, "NMRAZAOSOCIAL"},
			{Messages.EMPRESA_NOME_ENTIDADE, "NMEMPRESA"}, 
			{Messages.PEDIDO_NOME_ENTIDADE, "NUPEDIDO"},
			{Messages.PEDIDO_LABEL_DTEMISSAO, "DTEMISSAO"}};
	}
   
    //@Override
    protected void onFormStart() throws SQLException {
    	UiUtil.add(barBottomContainer, btNovoPedido, 5);
    	UiUtil.add(this, new LabelName(Messages.EMPRESA_NOME_ENTIDADE), cbEmpresa, getLeft(), getNextY());
    	
    	UiUtil.add(this, lbData, getLeft(), getNextY(), PREFERRED, PREFERRED);
    	UiUtil.add(this, edDateInitial, getLeft(), AFTER + HEIGHT_GAP);
    	UiUtil.add(this, edDateFinal, AFTER + WIDTH_GAP_BIG , SAME);
    	
    	UiUtil.add(this, lbCliente, getLeft(), getNextY(), PREFERRED, PREFERRED);
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
    	
        UiUtil.add(this, listContainer, LEFT, AFTER+HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btNovoPedido) {
	    			btNovoPedidoClick();
				} 
	    		break;
	    	}
	    	case KeyEvent.SPECIAL_KEY_PRESS: {
				int key = ((KeyEvent) event).key;
				if (key == SpecialKeys.ENTER || key == SpecialKeys.ACTION) {
					list();
				}
				break;
	    	}
    	}
    }
    
    @Override
    protected void filtrarClick() throws SQLException {
    	list();
    }

    @Override
    public void loadDefaultFilters() throws SQLException {
    	cbEmpresa.setSelectedIndex(0);
    	edDateInitial.setValue(null);
    	edDateFinal.setValue(null);
    }

    @Override
    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
    	edFiltro.requestFocus();
    }
    
    private void btNovoPedidoClick() throws SQLException {
    	if (!showComboEscolhaEmpresaPedido()) return;
    	mudouEmpresaSemSelecionarCliente = true;
		Cliente cliente = SessionLavenderePda.getCliente();
		AbstractBaseCadItemPedidoForm.invalidateInstance();
		new ListClienteWindow(true, false, false, cliente).popup();
		mudouEmpresaSemSelecionarCliente = false;		
	}
    
	private boolean showComboEscolhaEmpresaPedido() throws SQLException {
		EmpresaClienteComboBox cbEmpresaClienteComboBox = new EmpresaClienteComboBox(Messages.ESCOLHA_EMPRESA);
		cbEmpresaClienteComboBox.loadEmpresasRepresentante(SessionLavenderePda.getRepresentante().cdRepresentante);
		if (cbEmpresaClienteComboBox.size > 1) {
			cbEmpresaClienteComboBox.popup();
			if (cbEmpresaClienteComboBox.getSelectedIndex() != -1) {
				if (!SessionLavenderePda.cdEmpresa.equals(cbEmpresaClienteComboBox.getValue())) {
					changeEmpresaSessao(cbEmpresaClienteComboBox.getValue());
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	private void changeEmpresaSessao(String cdEmpresa) throws SQLException {
		SessionLavenderePda.cdEmpresaOld = SessionLavenderePda.cdEmpresa;
		EmpresaService.getInstance().changeEmpresaSessao(cdEmpresa, true);
		AbstractBaseCadItemPedidoForm.invalidateInstance();
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		Pedido pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(getSelectedDomain().rowKey); 

		changeEmpresaSessao(pedido.cdEmpresa);	
		prevContainer  = MainLavenderePda.getInstance().recarregarMenuSessao();
		
		UiUtil.showProcessingMessage();
		CadPedidoForm cadPedidoForm = (CadPedidoForm) getBaseCrudCadForm();
		cadPedidoForm.edit(pedido);
		cadPedidoForm.reloadTabelaPrecoSessaoEmpresa();
		show(cadPedidoForm);
		UiUtil.unpopProcessingMessage();
		cadPedidoForm.btNovoItemClick(true);
	}	
	
	@Override
	public void onFormExibition() throws SQLException {
		if (prevContainer == null) btVoltar.setText(getBtVoltarTitle());
		if (mudouEmpresaSemSelecionarCliente) {
			EmpresaService.getInstance().changeEmpresaSessao(SessionLavenderePda.cdEmpresaOld);
			SessionLavenderePda.cdEmpresaOld = null;
			mudouEmpresaSemSelecionarCliente = false;
		}
		super.onFormExibition();
	}
	
	@Override
	public void onFormClose() throws SQLException {
		if (prevContainer == null) MainLavenderePda.getInstance().recarregarMenuSessao();
		super.onFormClose();
	}
	
}
