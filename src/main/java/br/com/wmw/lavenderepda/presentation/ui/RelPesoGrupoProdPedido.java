package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class RelPesoGrupoProdPedido extends WmwWindow {
	
	private BaseGridEdit grid;
	private Vector itemPedidoList;
	private Hashtable grupoProdutoHash;

	public RelPesoGrupoProdPedido(Vector itemPedidoList) {
		super(Messages.REL_PESO_GRUPO_PROD_PED);
		this.itemPedidoList = itemPedidoList;
		setDefaultRect();
	}

	
//	@Override
	public void initUI() {
	   try {
		super.initUI();
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(GrupoProduto1Service.getInstance().getLabelGrupoProduto1(), -70, LEFT),
			new GridColDefinition(Messages.LABEL_PESO, -30, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton);
		UiUtil.add(this, grid, LEFT, getTop() + HEIGHT_GAP, FILL, FILL);
		carregarGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}


	private void carregarGrid() throws SQLException {
		grupoProdutoHash = new Hashtable("");
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoList.items[i];
			String cdGrupoProduto1 = itemPedido.getProduto().cdGrupoProduto1;
			if (ValueUtil.isNotEmpty(cdGrupoProduto1)) {
				Object object = grupoProdutoHash.get(cdGrupoProduto1);
				double qtPesoProduto = object != null ? (double) object : 0;
				grupoProdutoHash.put(cdGrupoProduto1, qtPesoProduto + itemPedido.qtPeso);
			}
		}
		int sizeGrupos = grupoProdutoHash.getKeys().size();
		for (int i = 0; i < sizeGrupos; i++) {
			String key = (String) grupoProdutoHash.getKeys().items[i];
			double qtPeso = (double) grupoProdutoHash.get(key);
			String[] item = {GrupoProduto1Service.getInstance().getDsGrupoProduto(key), StringUtil.getStringValueToInterface(qtPeso)};
			grid.add(item);
		}
		grid.qsort(1);
	}
	
}
