package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.TipoPessoa;
import br.com.wmw.lavenderepda.business.service.TipoPessoaService;

/**
 * ComboBox populada com registros de TipoPessoa.
 * @see BaseComboBox
 */
public class TipoPessoaComboBox extends BaseComboBox {

	public TipoPessoaComboBox() throws SQLException {
		super(Messages.NOVOCLIENTE_LABEL_TIPOPESSOA);
		load();
	}

	public String getValue() {
		TipoPessoa tipoPessoa = (TipoPessoa) getSelectedItem();
		if (tipoPessoa != null) {
			return tipoPessoa.flTipoPessoa;
		} else {
			return ValueUtil.VALOR_NI;
		}
	}

	public void setValue(String flTipoPessoa) {
		TipoPessoa tipoPessoa = new TipoPessoa();
		tipoPessoa.flTipoPessoa = flTipoPessoa;
		select(tipoPessoa);
	}

	public void load() throws java.sql.SQLException {
		add(TipoPessoaService.getInstance().findAll());
	}

}