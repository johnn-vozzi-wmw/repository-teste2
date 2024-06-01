package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.InfoEntregaPed;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.InfoEntregaPedService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelInfoEntregaPedidoForm extends LavendereCrudListForm {

	private EditFiltro edFiltroCli;
	private EditText edFiltroInfo;
	private BaseButton btFiltrarCliente;
	private BaseButton btTodos;
	private LabelName lbCliente;
	private String cdClienteFilter;
	private RepresentanteSupervComboBox cbRepresentante;

	public static final String LABEL_TODOS = Messages.LABEL_TODOS;

    public RelInfoEntregaPedidoForm() throws SQLException {
        super(Messages.RELINFOENTREGAPEDIDO_LABEL);
        setBaseCrudCadForm(new CadInfoEntregaPedidoForm());
        singleClickOn = true;
        constructorListContainer();
		btFiltrarCliente = new BaseButton("", UiUtil.getColorfulImage("images/alterarfiltro.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
		btTodos = new BaseButton(Messages.BOTAO_TODOS);
		edFiltroCli = new EditFiltro("999999999", 10);
		edFiltroInfo = new EditText("999999999", 100);
		lbCliente = new LabelName(Messages.CLIENTE_NOME_ENTIDADE);
		cbRepresentante = new RepresentanteSupervComboBox();
    }

    protected CrudService getCrudService() throws SQLException {
        return InfoEntregaPedService.getInstance();
    }

    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	InfoEntregaPed infoEntregaPedFilter = (InfoEntregaPed)domain;
    	infoEntregaPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	infoEntregaPedFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
				infoEntregaPedFilter.cdRepresentante = "";
			} else {
				infoEntregaPedFilter.cdRepresentante = cbRepresentante.getValue();
			}
		}
    	infoEntregaPedFilter.cdCliente = cdClienteFilter;
    	infoEntregaPedFilter.dsInfoEntrega = edFiltroInfo.getValue();
    	return InfoEntregaPedService.getInstance().findAllByExample(infoEntregaPedFilter);
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new InfoEntregaPed();
    }

    private void constructorListContainer() {
		listContainer = new GridListContainer(3, 1);
		listContainer.setColsSort(new String[][]{{Messages.NU_PEDIDO, "NUPEDIDO"}, {Messages.PEDIDO_LABEL_EMISSAO, "DTEMISSAO"}, {Messages.MENU_OPCAO_RELINFOENTREGAPED, "DSINFOENTREGA"}});
    	configListContainer("DTEMISSAO", ValueUtil.VALOR_NAO);
    }

    protected String[] getItem(Object domain) throws SQLException {
    	InfoEntregaPed infoEntregaPed = (InfoEntregaPed) domain;
    	String infoCliente = infoEntregaPed.cliente != null ? infoEntregaPed.cliente.toString() : infoEntregaPed.cdCliente;
    	String[] item = {
    			StringUtil.getStringValue(infoEntregaPed.nuPedido) + " - " +
    			StringUtil.getStringValue(infoEntregaPed.dtEmissao),
    			StringUtil.getStringValue(infoCliente),
    			StringUtil.getStringValue(infoEntregaPed.dsInfoEntrega)};
    	return item;
    }

    protected String getToolTip(BaseDomain domain) throws SQLException {
    	InfoEntregaPed infoEntregaPed = (InfoEntregaPed) domain;
    	return infoEntregaPed.dsInfoEntrega;
    }

    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

    @Override
    protected void onFormStart() throws SQLException {
        int yComponents = getTop() + HEIGHT_GAP;
        if (SessionLavenderePda.isUsuarioSupervisor()) {
    		UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO), cbRepresentante, getLeft(), yComponents);
        	yComponents = AFTER + HEIGHT_GAP;
    	}
        UiUtil.add(this, lbCliente, getLeft(), yComponents);
		UiUtil.add(this, edFiltroCli, getLeft(), AFTER, FILL - btFiltrarCliente.getPreferredWidth() - btTodos.getPreferredWidth() - WIDTH_GAP_BIG - UiUtil.BASE_MARGIN_GAP);
		edFiltroCli.setEditable(false);
		setEdFiltroCli(LABEL_TODOS);
		UiUtil.add(this, btTodos, getRight(), SAME, PREFERRED, UiUtil.getControlPreferredHeight());
		UiUtil.add(this, btFiltrarCliente, BEFORE - WIDTH_GAP, SAME, PREFERRED, UiUtil.getControlPreferredHeight());
		UiUtil.add(this, edFiltroInfo, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
    }

    @Override
    public void loadDefaultFilters() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
    			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
    		}
    		if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
    			cbRepresentante.setSelectedIndex(0);
    		}
    	}
    }

    public void showListCliente() throws SQLException {
    	ListClienteWindow listCliente = new ListClienteWindow();
    	listCliente.popup();
    	if (listCliente.cliente != null) {
    		setEdFiltroCli(listCliente.cliente.getDescription());
    		cdClienteFilter = listCliente.cliente.cdCliente;
    		list();
    	}
    }

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbRepresentante) {
    				if (event.target == cbRepresentante) {
    					SessionLavenderePda.setRepresentante(null);
    					if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
    						SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
    					}
    				}
    				list();
    			} else if (event.target == btFiltrarCliente) {
					showListCliente();
				} else if (event.target == btTodos) {
					setEdFiltroCli(LABEL_TODOS);
					cdClienteFilter = null;
			    	list();
				}
				break;
			}
		}
	}

	private void setEdFiltroCli(String dsCliente) {
		edFiltroCli.setText(dsCliente);
	}

}