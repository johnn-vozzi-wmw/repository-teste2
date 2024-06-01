package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.MargemContribFaixa;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.MargemContribFaixaService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelMargemContribFaixaWindow extends WmwListWindow {
	
	private Pedido pedido;
	
	private LabelValue lvVlTotalMargem;
	private LabelValue lvVlPctTotalMargem;

	public RelMargemContribFaixaWindow(Pedido pedido) {
		super(Messages.MARGEMCONTRIBUICAO_TITLE_WINDOW);
		this.pedido = pedido;
		constructorGrid();
		lvVlTotalMargem = new LabelValue();
		lvVlPctTotalMargem = new LabelValue();
		lvVlTotalMargem.setValue(pedido.vlTotalMargem);
		lvVlPctTotalMargem.setValue(pedido.vlPctTotalMargem);
		setDefaultRect();
	}
	
	private void constructorGrid() {
		grid = UiUtil.createGridEdit(new GridColDefinition[] {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.MARGEMCONTRIBUICAO_FAIXA, -35, LEFT),
			new GridColDefinition(Messages.MARGEMCONTRIBUICAO_INICIO_FAIXA, -15, LEFT),
			new GridColDefinition(Messages.MARGEMCONTRIBUICAO_FIM_FAIXA, -15, LEFT),
			new GridColDefinition(Messages.MARGEMCONTRIBUICAO_BLOQUEIA_PEDIDO, -15, LEFT),
			new GridColDefinition(Messages.MARGEMCONTRIBUICAO_ACAO_VERBA, -15, LEFT)
		});
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return getCrudService().findAllByExample(domain);
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		MargemContribFaixa margemContribFaixa = (MargemContribFaixa) domain;
		return new String[] {
				margemContribFaixa.getPrimaryKey(),
				margemContribFaixa.dsFaixa,
				StringUtil.getStringValueToInterface(margemContribFaixa.vlPctMargemInicio),
				StringUtil.getStringValueToInterface(margemContribFaixa.vlPctMargemFim),
				margemContribFaixa.isBloqueiaPedido() ? FrameworkMessages.VALOR_SIM : FrameworkMessages.VALOR_NAO,
				margemContribFaixa.getDsAcaoVerba()
		};
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return MargemContribFaixaService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		MargemContribFaixa filter = new MargemContribFaixa();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		return filter;
	}

	@Override
	protected void onFormStart() throws SQLException {
		UiUtil.add(this, new LabelName(Messages.MARGEMCONTRIBUICAO_VL_TOTAL_MARGEM), lvVlTotalMargem, getLeft(), getTop() + HEIGHT_GAP);
		UiUtil.add(this, new LabelName(Messages.MARGEMCONTRIBUICAO_PCT_TOTAL_MARGEM), lvVlPctTotalMargem, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
	}

}
