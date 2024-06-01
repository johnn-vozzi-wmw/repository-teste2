package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseMultiComboBox;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import totalcross.util.Vector;

public class StatusExcecaoMultiComboBox extends BaseMultiComboBox {
	
	private static String SEPARADOR = ",";

	public StatusExcecaoMultiComboBox() {
		super(Messages.PEDIDO_LABEL_CDSTATUSEXCECAO);
	}
	
	public void load() throws SQLException {
		removeAll();
		Vector list = StatusPedidoPdaService.getInstance().findAll();
		list.qsort();
		add(list);
	}
	
	public String getSelected() {
		StringBuilder cdStatusPedido = new StringBuilder();
		Object[] checkedItems = getSelectedItems();
		for (Object checkedItem : checkedItems) {
			cdStatusPedido.append(((StatusPedidoPda) checkedItem).cdStatusPedido);
			cdStatusPedido.append(SEPARADOR);
		}
		return cdStatusPedido.toString();
	}

	public void saveStatusExcecaoSelecionado() throws SQLException {
		ConfigInternoService.getInstance().addValueGeral(ConfigInterno.ULTIMOSTATUSEXCECAOSELECIONADO, getSelected());
	}

}
