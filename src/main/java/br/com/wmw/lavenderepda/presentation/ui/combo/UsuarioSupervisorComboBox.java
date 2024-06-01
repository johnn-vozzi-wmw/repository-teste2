package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.UsuarioSupervisor;
import br.com.wmw.lavenderepda.business.service.UsuarioSupervisorService;
import totalcross.util.Vector;

public class UsuarioSupervisorComboBox extends BaseComboBox {

	public UsuarioSupervisorComboBox() {
		super();
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
	}

	public UsuarioSupervisor getMotivoAgenda() {
		UsuarioSupervisor usuarioSupervisor = (UsuarioSupervisor) getSelectedItem();
		if (usuarioSupervisor != null) {
			return usuarioSupervisor;
		} else {
			return null;
		}
	}

	public String getValue() {
		UsuarioSupervisor usuarioSupervisor = (UsuarioSupervisor) getSelectedItem();
		if (usuarioSupervisor != null) {
			return usuarioSupervisor.cdUsuarioTelevendas;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		UsuarioSupervisor usuarioSupervisor = new UsuarioSupervisor();
		usuarioSupervisor.cdUsuarioTelevendas = value;
		select(usuarioSupervisor);
	}

	public void loadUsuarioSupervisor() throws SQLException {
		removeAll();
		UsuarioSupervisor usuarioSupervisor = new UsuarioSupervisor();
		usuarioSupervisor.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		Vector list = UsuarioSupervisorService.getInstance().findAllByExample(usuarioSupervisor);
		list.qsort();
		add(list);
		setSelectedIndex(0);
	}
	
}
