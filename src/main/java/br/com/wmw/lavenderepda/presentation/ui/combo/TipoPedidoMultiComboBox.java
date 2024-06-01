package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseMultiComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import totalcross.util.Vector;

public class TipoPedidoMultiComboBox extends BaseMultiComboBox {

	private static String SEPARADOR = ",";

	public TipoPedidoMultiComboBox() {
		super(Messages.TIPOPEDIDO_LABEL_TIPOPEDIDO);
	}

	public void load() throws SQLException {
		removeAll();
		Vector list = TipoPedidoService.getInstance().findAllDistinctTipoPedido(SessionLavenderePda.cdEmpresa,
				SessionLavenderePda.isUsuarioSupervisor() ? null : SessionLavenderePda.getRepresentante().cdRepresentante);
		list.qsort();
		add(list);
	}	

	public String getSelected() {
		StringBuilder cdTipoPedido = new StringBuilder();
		Object[] checkedItems = getSelectedItems();
		for (Object checkedItem : checkedItems) {
			cdTipoPedido.append(((TipoPedido) checkedItem).cdTipoPedido);
			cdTipoPedido.append(SEPARADOR);
		}
		return cdTipoPedido.toString();
	}

	public void setSelectedItems(String cdTipoPedidoList) {
		unselectAll();
		String[] cds = cdTipoPedidoList.split(SEPARADOR);
		for (String cdTipoPedido : cds) {
			TipoPedido tipoPedido = getItemByCdTipoPedido(cdTipoPedido);
			if (tipoPedido != null) select(tipoPedido);
		}
	}
	
	public TipoPedido getItemByCdTipoPedido(String cdTipoPedido) {
		if (cdTipoPedido != null) {
			for (Object item : getItems()) {
				TipoPedido tipoPedido = (TipoPedido)item;
				if (ValueUtil.valueEquals(tipoPedido.cdTipoPedido, cdTipoPedido)) {
					return tipoPedido;
				}
			}
		}
		return null;
	}
}