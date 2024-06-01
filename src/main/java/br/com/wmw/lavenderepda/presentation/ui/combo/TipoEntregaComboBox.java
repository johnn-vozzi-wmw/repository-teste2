package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.TipoEntrega;
import br.com.wmw.lavenderepda.business.service.TipoEntregaService;

public class TipoEntregaComboBox extends BaseComboBox {

	public TipoEntregaComboBox() throws SQLException {
		super(LavenderePdaConfig.usaTipoDeEntregaNoPedido ? Messages.TIPOENTREGA_LABEL_TIPOENTREGA : "");
		if (LavenderePdaConfig.usaTipoDeEntregaNoPedido) {
			load();
		}
	}

	public String getValue() {
		TipoEntrega tipoEntrega = (TipoEntrega) getSelectedItem();
		if (tipoEntrega != null) {
			return tipoEntrega.cdTipoentrega;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (value != null) {
			TipoEntrega tipoEntrega = new TipoEntrega();
			tipoEntrega.cdTipoentrega = value;
			select(tipoEntrega);
		} else {
			setSelectedIndex(-1);
		}
	}

	private void load() throws SQLException {
		add(TipoEntregaService.getInstance().findAllTipoEntregaSemEmpresa());
	}
}