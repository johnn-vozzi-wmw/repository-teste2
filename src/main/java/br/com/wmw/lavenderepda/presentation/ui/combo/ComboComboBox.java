package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.ItemCombo;
import br.com.wmw.lavenderepda.business.service.ComboService;
import totalcross.util.Vector;

public class ComboComboBox extends BaseComboBox {
	
	public ComboComboBox() {
		super(Messages.COMBO_NOME_ENTIDADE);
		if (!LavenderePdaConfig.isExibeComboMenuInferior()) {
			defaultItemType = DefaultItemType_ALL;
		}
	}
	
	public String getValue() {
		Combo combo = (Combo) getSelectedItem();
		if (combo != null) {
			return combo.cdCombo;
		}
		return ValueUtil.VALOR_NI;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			Combo combo = new Combo();
			combo.cdEmpresa = SessionLavenderePda.cdEmpresa;
			combo.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			combo.cdCombo = value;
			select(combo);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	public void load(ItemCombo itemCombo, String nuPedido, String flOrigemPedido) throws SQLException {
		Vector comboList = ComboService.getInstance().findListComboByItemCombo(itemCombo, nuPedido, flOrigemPedido);
		add(comboList);
		setSelectedIndex(0);
	}
	
	public void load(String cdEmpresa, String cdRepresentante, String cdCliente, String nuPedido, String cdTabelaPreco) throws SQLException {
		add(ComboService.getInstance().findListComboVigente(cdEmpresa, cdRepresentante, cdCliente, nuPedido, cdTabelaPreco));
		setSelectedIndex(0);
	}

}
