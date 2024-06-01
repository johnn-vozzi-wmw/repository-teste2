package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.UsuarioRelRep;
import br.com.wmw.lavenderepda.business.service.UsuarioRelRepService;
import totalcross.util.Vector;

public class UsuarioRelRepComboBox extends BaseComboBox {
	
	public UsuarioRelRepComboBox(String title, boolean liberacaoPorUsuarioEAlcada) throws SQLException {
		super(title);
		load(liberacaoPorUsuarioEAlcada);
	}
	
	public String getValue() {
		UsuarioRelRep usuarioRelRep = (UsuarioRelRep)getSelectedItem();
		return  usuarioRelRep != null ? usuarioRelRep.cdUsuarioRep : null;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			UsuarioRelRep usuarioRelRep = new UsuarioRelRep();
			usuarioRelRep.cdUsuario = value;
			select(usuarioRelRep);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	private void load(boolean liberacaoPorUsuarioEAlcada) throws SQLException {
		Vector list = UsuarioRelRepService.getInstance().findUsuarioRelRep(liberacaoPorUsuarioEAlcada);
		if (ValueUtil.isNotEmpty(list)) {
			list.qsort();
			add(list);
		}
	}
}
