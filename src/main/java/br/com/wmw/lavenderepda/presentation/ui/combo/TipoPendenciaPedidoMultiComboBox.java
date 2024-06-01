package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseMultiComboBox;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.vo.TipoPendenciaPedido;
import br.com.wmw.lavenderepda.business.service.TipoPendenciaPedidoService;
import totalcross.util.Vector;

public class TipoPendenciaPedidoMultiComboBox extends BaseMultiComboBox {

	public static char SEPARADOR = ',';

	public TipoPendenciaPedidoMultiComboBox() {
		super(Messages.TIPOPENDENCIA_LABEL);
	}

	public void load() throws SQLException {
		removeAll();
		Vector list = TipoPendenciaPedidoService.getInstance().findAllAvailable();
		list.qsort();
		add(list);
	}	

	public String getSelected() {
		StringBuilder cdTipoPedido = new StringBuilder();
		Object[] checkedItems = getSelectedItems();
		for (Object checkedItem : checkedItems) {
			cdTipoPedido.append(((TipoPendenciaPedido) checkedItem).cdTipoPendenciaPedido);
			cdTipoPedido.append(SEPARADOR);
		}
		return cdTipoPedido.toString();
	}

	public void setSelectedItems(String cdTipoPendenciaList) {
		unselectAll();
		String[] tipoPendenciaArray = StringUtil.split(cdTipoPendenciaList, SEPARADOR);
		for (String cdTipoPendencia : tipoPendenciaArray) {
			TipoPendenciaPedido tipoPedido = getItemByCdTipoPedido(cdTipoPendencia);
			if (tipoPedido != null) {
				select(tipoPedido);
			}
		}
	}
	
	public TipoPendenciaPedido getItemByCdTipoPedido(String cdTipoPendenciaPedido) {
		for (Object item : getItems()) {
			TipoPendenciaPedido tipoPendenciaPedido = (TipoPendenciaPedido) item;
			if (ValueUtil.valueEquals(tipoPendenciaPedido.cdTipoPendenciaPedido, cdTipoPendenciaPedido)) {
				return tipoPendenciaPedido;
			}
		}
		return null;
	}
}