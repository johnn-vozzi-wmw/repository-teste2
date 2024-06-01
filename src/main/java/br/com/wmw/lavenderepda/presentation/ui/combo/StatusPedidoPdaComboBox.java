package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import totalcross.util.Vector;

public class StatusPedidoPdaComboBox extends BaseComboBox {

	public StatusPedidoPdaComboBox() throws SQLException {
		super(Messages.PEDIDO_LABEL_CDSTATUSPEDIDO);
		this.defaultItemType = BaseComboBox.DefaultItemType_ALL;
		carregaStatusPedido();
	}

	public String getValue() {
		StatusPedidoPda statusPedido = (StatusPedidoPda)getSelectedItem();
		if (statusPedido != null) {
			return statusPedido.cdStatusPedido;
		} else {
			return null;
		}
	}

	public String getValueString() {
		if (getSelectedIndex() == 0) {
			return FrameworkMessages.OPCAO_TODOS;
		}
		StatusPedidoPda statusPedido = (StatusPedidoPda)getSelectedItem();
		if (statusPedido != null) {
			return statusPedido.dsStatusPedido;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			StatusPedidoPda statusPedido = new StatusPedidoPda();
			statusPedido.cdStatusPedido = value;

			select(statusPedido);
		} else {
			setSelectedIndex(-1);
		}
	}

	private void carregaStatusPedido() throws SQLException {
		Vector list = StatusPedidoPdaService.getInstance().findAllInCache();
		if (ValueUtil.isNotEmpty(list)) {
			SortUtil.qsortString(list.items, 0, list.size() - 1, true);
			add(list);
		}
	}

//	private Vector orderList(Vector list) {
//		int size = list.size();
//        for (int i = 0; i < size; i++) {
//			StatusPedidoPda statusPedidoPda = (StatusPedidoPda)list.items[i];
//			if (statusPedidoPda.cdStatusPedido.equals(LavenderePdaConfig.cdStatusPedidoAberto)) {
//				if (!list.items[0].equals(list.items[i])) {
//					StatusPedidoPda staPdaAux = (StatusPedidoPda)list.items[0];
//					list.items[0] = list.items[i];
//					list.items[i] = staPdaAux;
//				}
//			}
//			if (statusPedidoPda.cdStatusPedido.equals(LavenderePdaConfig.cdStatusPedidoFechado)) {
//				if (!list.items[1].equals(list.items[i])) {
//					StatusPedidoPda staPdaAux = (StatusPedidoPda)list.items[1];
//					list.items[1] = list.items[i];
//					list.items[i] = staPdaAux;
//				}
//			}
//			if (statusPedidoPda.cdStatusPedido.equals(LavenderePdaConfig.cdStatusPedidoTransmitido)) {
//				if (!list.items[2].equals(list.items[i])) {
//					StatusPedidoPda staPdaAux = (StatusPedidoPda)list.items[2];
//					list.items[2] = list.items[i];
//					list.items[i] = staPdaAux;
//				}
//			}
//			if (statusPedidoPda.cdStatusPedido.equals(LavenderePdaConfig.cdStatusPedidoPendenteAprovacao)) {
//				if (!list.items[3].equals(list.items[i])) {
//					StatusPedidoPda staPdaAux = (StatusPedidoPda)list.items[3];
//					list.items[3] = list.items[i];
//					list.items[i] = staPdaAux;
//				}
//			}
//		}
//		return list;
//	}
	
}
