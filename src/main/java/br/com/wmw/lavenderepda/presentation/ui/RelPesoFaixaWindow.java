package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ComissaoPedidoRep;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PesoFaixa;
import br.com.wmw.lavenderepda.business.service.ComissaoPedidoRepService;
import br.com.wmw.lavenderepda.business.service.PesoFaixaService;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelPesoFaixaWindow extends WmwListWindow {

	private final Pedido pedido;
	private Vector items;

	@Override protected String getSelectedRowKey() throws SQLException { return null; }
	@Override protected void onFormEvent(Event event) throws SQLException {}
	@Override protected CrudService getCrudService() throws SQLException { return ComissaoPedidoRepService.getInstance(); }
	@Override protected BaseDomain getDomainFilter() { return new ComissaoPedidoRep(); }

	public RelPesoFaixaWindow(Pedido pedido) throws ValidationException {
		super(Messages.PESO_FAIXA_WINDOW);
		this.pedido = pedido;
		setDefaultRect();
	}

	private PesoFaixa createComissaoPedidoRepFilter() {
		PesoFaixa pesoFaixaFilter = new PesoFaixa();
		pesoFaixaFilter.cdEmpresa = pedido.cdEmpresa;
		return pesoFaixaFilter;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return items = PesoFaixaService.getInstance().findAllByExample(createComissaoPedidoRepFilter());
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		String[] item = new String[3];
		try {
			PesoFaixa pesoFaixa = (PesoFaixa) domain;
			item = new String[] {
				StringUtil.getStringValue(pesoFaixa.dsFaixa),
				StringUtil.getStringValueToInterface(pesoFaixa.qtPeso),
				pesoFaixa.toString()
			};
	        int corFaixa = PesoFaixaService.getInstance().getCorIconePesoFaixa(pesoFaixa);
			grid.setImage(pesoFaixa.toString(), UiUtil.getIconButtonAction(PesoFaixaService.IMAGES_ICONE_PESOFAIXA_PNG, corFaixa, true), false);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		return item;
	}

	@Override
	public void list() throws SQLException {
		super.list();
		afterList();
	}

	private void afterList() {
		int listSize = items.size();
		int selectedRow = 0;
		for (int i = 0; i < listSize; i++) {
			PesoFaixa pesoFaixa = (PesoFaixa) items.items[i];
			if (pesoFaixa.isFaixaIdeal()) {
				selectedRow = i;
				break;
			}
		}
		grid.setSelectedIndex(selectedRow);
	}

	@Override
	protected void onFormStart() throws SQLException {
		int nextY = AFTER;
    	GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.PESO_FAIXA_DESCRICAO_WINDOW, -45, LEFT),
		    new GridColDefinition(Messages.PESO_FAIXA_VALOR_WINDOW, -25, CENTER),
			new GridColDefinition(Messages.PESO_FAIXA_COR_WINDOW, -25, CENTER)
    	};
    	grid = UiUtil.createGridEdit(gridColDefiniton);
    	grid.setGridControllable();
        UiUtil.add(this, grid);
        grid.setRect(LEFT, nextY, FILL, FILL);
	}

}