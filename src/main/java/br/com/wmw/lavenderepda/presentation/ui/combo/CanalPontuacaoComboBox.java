package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.service.CanalPontuacaoService;
import totalcross.util.Vector;

public class CanalPontuacaoComboBox extends BaseComboBox {

	public CanalPontuacaoComboBox() throws SQLException {
		this(DefaultItemType_ALL);
	}

	public CanalPontuacaoComboBox(int defaultItemType) throws SQLException {
		super(Messages.EXTRATO_PONTUACAO_CANAL_COMBO);
		this.defaultItemType = defaultItemType;
		carregaSegmentoPontuacao();
	}

	public String getValue() {
		return (String) getSelectedItem();
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) select(value);
		else setSelectedIndex(-1);
	}

	private void carregaSegmentoPontuacao() throws SQLException {
		Vector list = CanalPontuacaoService.getInstance().findAllCanalInPontExtPed();
		if (ValueUtil.isNotEmpty(list)) add(list);
		else addDefaultItens();
	}

}
