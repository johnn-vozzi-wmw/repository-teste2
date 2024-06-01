package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelGrupoProdutoNaoInseridoPedidoWindow extends WmwWindow {

	private LabelValue lbMensagem;
	private BaseGridEdit grid;
	private boolean controle;
	private ButtonPopup btNovoItem;
	private ButtonPopup btFecharPedido;
	private Pedido pedido;
	private boolean btClicadoFecharPedido;

	public RelGrupoProdutoNaoInseridoPedidoWindow(String title, boolean controle, Pedido pedido) {
		super(Messages.PEDIDO_RELATORIO_GRUPOPRODUTO_NAO_INSERIDO);
		this.controle = controle;
		this.pedido = pedido;
		btNovoItem = new ButtonPopup(Messages.BOTAO_ADICIONAR_ITEM);
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
		lbMensagem = new LabelValue("------------");
		setDefaultRect();
	}

	public void initUI() {
	   try {
		super.initUI();
		if (controle) {
			btFechar.setVisible(false);
			btFecharPedido.setVisible(true);
			btNovoItem.setVisible(true);
		} else {
			btFechar.setVisible(true);
			btFecharPedido.setVisible(false);
			btNovoItem.setVisible(false);
		}
		UiUtil.add(this, lbMensagem, getLeft(), getNextY(), FILL, PREFERRED + 20);
		lbMensagem.setMultipleLinesText(Messages.GRUPOPRODUTO_NAO_INSERIDO_MSG);
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(Messages.LABEL_GRUPOS, -40, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		addButtonPopup(btNovoItem);
		addButtonPopup(btFecharPedido);
		carregaGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void carregaGrid() throws SQLException {
		Vector list = GrupoProduto1Service.getInstance().findGrupoProdutoPedido(pedido);
		grid.setItems(null);
		String cdGrupoProduto1;
		int size = list.size();
        for (int i = 0; i < size; i++) {
        	cdGrupoProduto1 = (String) list.items[i];
			String[] item = {GrupoProduto1Service.getInstance().getDsGrupoProduto(cdGrupoProduto1)};
			grid.add(item);
		}
	}

	public void onEvent(Event event) {
		super.onEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFecharPedido) {
					btClicadoFecharPedido = true;
					unpop();
				} else if (event.target == btNovoItem) {
					btClicadoFecharPedido = false;
					unpop();
				}
				break;
			}
    	}
	}

	public boolean getBtClicadoFecharPedido() {
		return btClicadoFecharPedido;
	}

	public void setBtClicadoFecharPedido(boolean btClicadoFecharPedido) {
		this.btClicadoFecharPedido = btClicadoFecharPedido;
	}

}
