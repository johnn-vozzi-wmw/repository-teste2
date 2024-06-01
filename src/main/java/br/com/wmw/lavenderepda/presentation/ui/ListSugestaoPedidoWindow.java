package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.integration.dao.pdbx.PedidoPdbxDao;
import totalcross.sys.Time;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListSugestaoPedidoWindow extends WmwWindow {

	private int aux = -1;
	private Time time;
	public BaseGridEdit grid;
	public String rowKeySelected;
	private String cdCliente;
	private ButtonPopup btSelecionar;

	public ListSugestaoPedidoWindow(String cdCliente) {
		super(Messages.PEDIDO_SUGESTAO_HISTORICO);
		this.cdCliente = cdCliente;
		btSelecionar = new ButtonPopup(Messages.BOTAO_SELECIONAR);
		setDefaultRect();
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		GridColDefinition[] gridColDefiniton = {new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
												new GridColDefinition("Número", LavenderePdaConfig.isAdicionaCodigoUsuarioAoNumeroPedido() ? -55 : -35, LEFT),
												new GridColDefinition("Emissão", -35, LEFT),
												new GridColDefinition("Valor", -30, LEFT)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL - WIDTH_GAP, FILL);
		addButtonPopup(btSelecionar);
		addButtonPopup(btFechar);
		carregaGrid();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void carregaGrid() throws SQLException {
		grid.clear();
		Pedido pedidoFilter = new Pedido();
		pedidoFilter.cdCliente = cdCliente;
		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		pedidoFilter.flFiltraPedidosDifCancelados = LavenderePdaConfig.isIgnoraPedidoCanceladoSugestaoNovoPedido();
		if (!LavenderePdaConfig.permiteCopiaDePedidoAberto) {
			pedidoFilter.cdStatusPedidoDif = LavenderePdaConfig.cdStatusPedidoAberto;
		}
		Vector pedidoList = PedidoPdbxDao.getInstance().findAllPedidosSugestao(pedidoFilter);
		//--
		Pedido.sortAttr = "DTEMISSAO";
		SortUtil.qsortInt(pedidoList.items, 0, pedidoList.size() - 1, true);
		if (pedidoList != null) {
			Pedido pedido;
			int size = LavenderePdaConfig.qtdPedidoSugestaoParaNovoPedido;
			if (pedidoList.size() < size) {
				size = pedidoList.size();
			}

			for (int i = pedidoList.size() - 1; i >= pedidoList.size() - size; i--) {
				pedido = (Pedido)pedidoList.items[i];
				if (pedido != null) {
					String[] item = { StringUtil.getStringValue(pedido.rowKey),
							StringUtil.getStringValue(pedido.nuPedido),
							StringUtil.getStringValue(pedido.dtEmissao),
							StringUtil.getStringValueToInterface(pedido.vlTotalPedido)
					};
					grid.add(item);
				}
			}
		}
		pedidoList = null;
	}

	public void popup() {
		if (grid.size() == 0) {
			return;
		} else {
			super.popup();
		}
	}

	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case GridEvent.SELECTED_EVENT: {
				if ((grid.getSelectedIndex() != -1) && (aux == grid.getSelectedIndex())) {
					Time time2 = new Time();
					if (TimeUtil.getMillisBetween(time, time2) < 1000) {
						rowKeySelected = grid.getSelectedItem()[0];
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

			case ControlEvent.PRESSED: {
				if (event.target == btSelecionar) {
					if (grid.getSelectedIndex() != -1) {
						rowKeySelected = grid.getSelectedItem()[0];
						unpop();
					} else {
						UiUtil.showInfoMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO);
					}
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
