package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Unidade;
import br.com.wmw.lavenderepda.business.service.UnidadeService;

public class UnidadeComboBox extends BaseComboBox {
	
	public UnidadeComboBox() throws SQLException {
		super(Messages.TOOLTIP_LABEL_UNIDADE_ALTERNATIVA);
		load();
	}
	
	public Unidade getUnidade() {
		return (Unidade)getSelectedItem();
	}
	
	public String getValue() {
		Unidade unidade = (Unidade) getSelectedItem();
		if (unidade != null) {
			return unidade.cdUnidade;
		}
		return null;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			Unidade unidade = createUnidade(value);
			select(unidade);
		} else {
			setSelectedIndex(0);
		}
	}
	
	public void load() throws SQLException {
		removeAll();
		Unidade unidade = new Unidade();
		unidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		unidade.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Unidade.class);
		add(UnidadeService.getInstance().findAllByExample(unidade));
	}
	
	public Unidade createUnidade(String cdUnidade) {
		Unidade unidade = createUnidade();
		unidade.cdUnidade = cdUnidade;
		return unidade;
	}
	
	public Unidade createUnidade() {
		Unidade unidade = new Unidade();
		unidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		unidade.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Unidade.class);
		return unidade;
	}

}
