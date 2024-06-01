package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.TipoRecado;
import br.com.wmw.lavenderepda.business.service.TipoRecadoService;

public class TipoRecadoComboBox extends BaseComboBox {

	public TipoRecadoComboBox() throws SQLException {
		super(Messages.RECADO_LABEL_CDTIPO);
		load();
	}

	public int getValue() {
		TipoRecado tipoRecado = (TipoRecado)getSelectedItem();
		if (tipoRecado != null) {
			return tipoRecado.cdTipoRecado;
		} else {
			return DefaultItemNull;
		}
	}

	public void setValue(int value) {
		TipoRecado tipoRecado = new TipoRecado();
		tipoRecado.cdTipoRecado = value;
		select(tipoRecado);
	}

	private void load() throws SQLException {
		add(TipoRecadoService.getInstance().findAll());
	}
}