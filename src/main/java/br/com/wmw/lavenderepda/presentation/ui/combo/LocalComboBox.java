package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Local;
import br.com.wmw.lavenderepda.business.service.LocalService;

public class LocalComboBox extends BaseComboBox {

	public LocalComboBox() throws SQLException {
		super(Messages.LABEL_LOCAL);
		load();
	}

	public String getValue() {
		Local local = (Local) getSelectedItem();
		if (local != null) {
			return local.cdLocal;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public void setValue(String cdLocal) {
		Local local = new Local();
		local.cdLocal = cdLocal;
		select(local);
	}

	public void load() throws java.sql.SQLException {
		if(LavenderePdaConfig.exibeOpcaoTodosFiltroLocal) {
			defaultItemType = DefaultItemType_ALL;
		}
		Local localFilter = new Local();
		localFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		localFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Local.class);
		add(LocalService.getInstance().findAllByExample(localFilter));
		setSelectedIndex(0);
	}

}