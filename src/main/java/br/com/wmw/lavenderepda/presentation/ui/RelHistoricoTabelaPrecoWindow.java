package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.sys.Time;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class RelHistoricoTabelaPrecoWindow extends WmwWindow {

	private int aux = -1;
	private Time time;
	public BaseGridEdit grid;
	public String cdTabelaPrecoSelected;
	private String cdCliente;

	public RelHistoricoTabelaPrecoWindow(String cdCliente) {
		super(Messages.TABELAPRECOCLIENTE_HISTORICO);
		this.cdCliente = cdCliente;
		setDefaultRect();
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition("Data", -30, LEFT),
			new GridColDefinition("Condição", -70, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, getTop() + 1, FILL, FILL);
		carregaGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void carregaGrid() throws SQLException {
		grid.clear();
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdCliente = cdCliente;
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		Vector pedidosList = PedidoPdbxDao.getInstance().findAllByExample(pedidoFilter);
		if (pedidosList != null) {
			String dsTabPreco;
			Pedido pedido;
			int size = pedidosList.size();
			for (int x = 0; x < size; x++) {
				pedido = (Pedido)pedidosList.items[x];
				dsTabPreco = TabelaPrecoService.getInstance().getDsTabelaPreco(pedido.cdTabelaPreco);
				if (dsTabPreco != null) {
					String[] item = {"", StringUtil.getStringValue(pedido.dtEmissao), dsTabPreco, StringUtil.getStringValue(pedido.cdTabelaPreco)};
					grid.add(item);
				}
			}
		}
		pedidosList = null;
	}

	//@Override
	public void popup() {
		if (grid.size() == 0) {
			UiUtil.showInfoMessage(Messages.PEDIDO_MSG_HISTORICO_TABELA);
		} else {
			super.popup();
		}
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case GridEvent.SELECTED_EVENT: {
				if ((grid.getSelectedIndex() != -1) && (aux == grid.getSelectedIndex())) {
					Time time2 = new Time();
					if (TimeUtil.getMillisBetween(time, time2) < 1000) {
						cdTabelaPrecoSelected = grid.getSelectedItem()[3];
						unpop();
					} else {
						aux = grid.getSelectedIndex();
						time = new Time();
					}
				} else {
					aux = grid.getSelectedIndex();
					time = new Time();
				}
				break;
			}
		}
	}

	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}

}
