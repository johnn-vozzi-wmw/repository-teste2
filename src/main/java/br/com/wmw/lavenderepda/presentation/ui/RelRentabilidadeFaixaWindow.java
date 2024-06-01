package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.RentabilidadeFaixa;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class RelRentabilidadeFaixaWindow extends WmwWindow {
	
	private BaseGridEdit grid;
	private LabelContainer produtoContainer;
	private ItemPedido itemPedido;
	private Pedido pedido;

	public RelRentabilidadeFaixaWindow(ItemPedido itemPedido, Pedido pedido) {
		super(Messages.RENTABILIDADEFAIXA_NOME_ENTIDADE);
		this.itemPedido = itemPedido;
		this.pedido = pedido;
		produtoContainer = new LabelContainer("");
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.RENTABILIDADEFAIXA_DS_FAIXA, -75, LEFT),
			new GridColDefinition(Messages.RENTABILIDADEFAIXA_COR, -25, CENTER)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		grid.setGridControllable();
		setDefaultRect();
	}
	
	@Override
	public void initUI() {
		try {
			super.initUI();
			if (itemPedido != null) {
				produtoContainer.setDescricao(itemPedido.getProduto().toString());
				UiUtil.add(scBase, produtoContainer, LEFT, TOP, FILL, LabelContainer.getStaticHeight());
			}
			UiUtil.add(scBase, grid, LEFT, itemPedido != null ? AFTER : TOP, FILL, FILL);
			loadGrid();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}
	
	private void loadGrid() throws SQLException {
		Vector rentabilidadeFaixaList = getRentabilidadeFaixaList();
		if (ValueUtil.isNotEmpty(rentabilidadeFaixaList)) {
			RentabilidadeFaixa rentabilidadeFaixaAtingida = getRentabilidadeFaixaAtingida();
			for (int i = 0; i < rentabilidadeFaixaList.size(); i++) {
				RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) rentabilidadeFaixaList.items[i];
				grid.add(getItem(rentabilidadeFaixa));
				if (rentabilidadeFaixaAtingida != null) {
					if (rentabilidadeFaixaAtingida.vlPctRentabilidade == rentabilidadeFaixa.vlPctRentabilidade) {
						grid.gridController.setRowForeColor(ColorUtil.baseForeColorSystem, i);
					}
				}
			}
		}
	}
	
	private Vector getRentabilidadeFaixaList() throws SQLException {
		Vector rentabilidadeFaixaList = new Vector();
		RentabilidadeFaixa rentabilidadeFaixaFilter = new RentabilidadeFaixa();
		rentabilidadeFaixaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		rentabilidadeFaixaList = RentabilidadeFaixaService.getInstance().findAllByExample(rentabilidadeFaixaFilter, RentabilidadeFaixa.NOMECOLUNA_VLPCTRENTABILIDADE);
		return rentabilidadeFaixaList;
	}
	
	private RentabilidadeFaixa getRentabilidadeFaixaAtingida() throws SQLException {
		if (pedido != null) {
			return RentabilidadeFaixaService.getInstance().getRentabilidadeFaixaPedido(pedido);
		} else {
			return RentabilidadeFaixaService.getInstance().getRentabilidadeFaixaItemPedido(itemPedido);
		}
	}
	
	private String[] getItem(Object domain) {
		try {
			RentabilidadeFaixa rentabilidadeFaixa = (RentabilidadeFaixa) domain;
	        String[] item = {
	                StringUtil.getStringValue(rentabilidadeFaixa.dsFaixa),
	                StringUtil.getStringValue(Messages.RENTABILIDADE_NOME_ENTIDADE + " " + StringUtil.getStringValueToInterface(rentabilidadeFaixa.vlPctRentabilidade))};
	        int corFaixa = RentabilidadeFaixaService.getInstance().getCorRentabilidadeFaixa(rentabilidadeFaixa);
	        Image iconeFaixa = UiUtil.getIconButtonAction("images/rentabilidade.png", corFaixa, true);
	        grid.setImage(Messages.RENTABILIDADE_NOME_ENTIDADE + " " + StringUtil.getStringValueToInterface(rentabilidadeFaixa.vlPctRentabilidade), iconeFaixa, false);
	        return item;
		} catch (Throwable ex) {
			UiUtil.showErrorMessage(ex);
		}
		return null;
    }

}
