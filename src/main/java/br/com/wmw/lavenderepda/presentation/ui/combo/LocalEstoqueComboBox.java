package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.LocalEstoque;
import br.com.wmw.lavenderepda.business.service.LocalEstoqueService;

public class LocalEstoqueComboBox extends BaseComboBox{

	public LocalEstoqueComboBox() throws SQLException {
		super(Messages.PRODUTO_LABEL_FILTRO_LOCAL_ESTOQUE);
		load();
	}
	
	public String getValue() {
		LocalEstoque localEstoque = (LocalEstoque) getSelectedItem();
		if (localEstoque != null) {
			return localEstoque.cdLocalEstoque;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public void setValue(String cdLocalEstoque) {
		LocalEstoque localEstoque = new LocalEstoque();
		localEstoque.cdLocalEstoque = cdLocalEstoque;
		select(localEstoque);
	}

	public void load() throws SQLException {
		LocalEstoque localEstoqueFilter = new LocalEstoque(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getCdRepresentanteFiltroDados(LocalEstoque.class), null);
		add(LocalEstoqueService.getInstance().findAllByExample(localEstoqueFilter));
		setSelectedIndex(0);
	}

}
