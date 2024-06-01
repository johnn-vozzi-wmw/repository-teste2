package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.DescProgressivoConfigService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListDescontoProgressivoForm extends LavendereCrudListForm {

	private EditDate edDtInicialFilter;
	private EditDate edDtFinalFilter;

	private boolean fromItemPedido;

	private Pedido pedido;
	private Cliente cliente;

	public Vector listDescProgressivo;
	
	private CadPedidoForm pedidoForm;

	public ListDescontoProgressivoForm(Cliente cliente) throws SQLException {
		this(cliente, null, false);
	}
	
	public ListDescontoProgressivoForm(Cliente cliente, CadPedidoForm pedidoForm, boolean fromItemPedido) throws SQLException {
		super(Messages.DESC_PROG_LIST_TITLE);
		this.pedido = pedidoForm != null ? pedidoForm.getPedido() : null;
		this.cliente = cliente;
		this.pedidoForm = pedidoForm;
		this.fromItemPedido = fromItemPedido;
		setBaseCrudCadForm(null);
		singleClickOn = true;
		constructorListContainer();
		initializeComponents();
	}

	private void initializeComponents() {
		edDtInicialFilter = new EditDate();
		edDtInicialFilter.setValue(DateUtil.getCurrentDate());
		edDtFinalFilter = new EditDate();
		edDtFinalFilter.setValue(DateUtil.getLastDayOfMonth(DateUtil.getCurrentDate()));
	}

	private void constructorListContainer() {
		configListContainer("CDDESCPROGRESSIVO");
		listContainer = new GridListContainer(4, 2);
		listContainer.setColsSort(new String[][]{
				{Messages.DESC_PROG_CONFIG_CDDESCPROGRESSIVO, "CDDESCPROGRESSIVO"}
		});
		listContainer.setColPosition(1, RIGHT);
		listContainer.setColPosition(3, RIGHT);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		DescProgressivoConfig item = (DescProgressivoConfig) domain;
		Vector itens = new Vector();
		itens.addElement(item.toString());
		itens.addElement(ValueUtil.VALOR_NI);
		itens.addElement(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_VIGENCIA_INICIO_FIM, new String[] {StringUtil.getStringValue(item.dtInicialVigencia), StringUtil.getStringValue(item.dtFimVigencia)}));
		int daysBetween = DateUtil.getDaysBetween(item.dtFimVigencia, DateUtil.getCurrentDate());
		String dias = daysBetween > 1 ? Messages.DESC_PROG_CONFIG_DIA_PLURAL : Messages.DESC_PROG_CONFIG_DIA_SINGULAR;
		itens.addElement(MessageUtil.getMessage(Messages.DESC_PROG_CONFIG_DIAS_RESTANTES, new String[] {StringUtil.getStringValue(daysBetween), dias}));
		return (String[]) itens.toObjectArray();
	}

	@Override
	protected BaseDomain getDomainFilter() throws SQLException {
		DescProgressivoConfig descProgressivoConfig = new DescProgressivoConfig();
		descProgressivoConfig.cdEmpresa = cliente.cdEmpresa;
		descProgressivoConfig.cdRepresentante = cliente.cdRepresentante;
		descProgressivoConfig.cliente = cliente;
		descProgressivoConfig.dtInicialVigencia = edDtInicialFilter.getValue();
		descProgressivoConfig.dtFimVigencia = edDtFinalFilter.getValue();
		return descProgressivoConfig;
	}

	@Override
	public void list() throws SQLException {
		super.list();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return listDescProgressivo = DescProgressivoConfigService.getInstance().findAllByExample(domain);
	}

	@Override
	public void onFormExibition() throws SQLException {
		list();
		if (listDescProgressivo.size() == 1) {
			voltarClick();
		}
	}

	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		return domain.toString();
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return DescProgressivoConfigService.getInstance();
	}

	private void firstEnterControl() {
		if (listDescProgressivo.size() == 1) {
			detailDescProgressivo((DescProgressivoConfig) listDescProgressivo.items[0], true);
		}
	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		firstEnterControl();
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.DESC_PROG_CONFIG_VIGENCIA), getLeft(), getNextY(), FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(this, edDtInicialFilter, getLeft(), AFTER, FILL);
		UiUtil.add(this, edDtFinalFilter, edDtInicialFilter.getX2() + WIDTH_GAP_BIG, SAME, FILL);
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - barBottomContainer.getHeight());
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case EditIconEvent.PRESSED:
				if (event.target == edFiltro) {
					list();
				}
			break;
		}
	}

	@Override
	public void detalhesClick() throws SQLException {
		detailDescProgressivo((DescProgressivoConfig) getSelectedDomain(), false);
	}

	private boolean usaPedido() {
		return pedido != null && ValueUtil.isEmpty(pedido.nuPedido);
	}

	private void detailDescProgressivo(DescProgressivoConfig selectedDomain, boolean skipListDescForm) {
		try {
			selectedDomain.cliente = cliente;
			show(new RelDescontoProgressivoForm(selectedDomain, skipListDescForm, usaPedido() ? null : pedido, pedidoForm, fromItemPedido));
		} catch (Throwable e) {
			UiUtil.showErrorMessage(Messages.DESC_PROG_CONFIG_DETAIL_ERROR + e.getMessage());
			ExceptionUtil.handle(e);
		}
	}

}
