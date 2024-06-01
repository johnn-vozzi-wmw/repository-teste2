package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import totalcross.util.Vector;

public class MarcadorComboBox extends BaseComboBox {
	
	public MarcadorComboBox(String entidadeMarcador) throws SQLException {
		this(entidadeMarcador, null);
	}
	
	public MarcadorComboBox(String entidadeMarcador, String cdRepresentante) throws SQLException {
		super(Messages.MARCADOR_NOME_ENTIDADE);
		this.defaultItemType = DefaultItemType_ALL;
		carregaMarcadores(entidadeMarcador, cdRepresentante);
	}
	
	public void setValue(String value) throws SQLException {
		if (value != null) {
			Marcador marcador = new Marcador();
			marcador.cdMarcador = value;
			select(marcador);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public String getValue() {
		Marcador marcador = (Marcador) getSelectedItem();
		if (marcador != null) {
			return marcador.cdMarcador;
		}
		return ValueUtil.VALOR_NI;
	}
	
	private void carregaMarcadores(String entidadeMarcador, String cdRepresentante) throws SQLException {
		Vector list = null;
		switch (entidadeMarcador) {
		case Marcador.ENTIDADE_MARCADOR_PEDIDO:
			list = MarcadorService.getInstance().buscaMarcadoresVigentesDePedidos(cdRepresentante);
			break;
		default:
			list = MarcadorService.getInstance().buscaMarcadoresVigentes(entidadeMarcador);
			break;
		}
		add(list);
	}

}