package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.ModoFaturamento;
import br.com.wmw.lavenderepda.business.service.ModoFaturamentoService;
import totalcross.util.Vector;

public class ModoFaturamentoComboBox extends BaseComboBox {

	public ModoFaturamentoComboBox() {
		super();
		this.defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
	}
	
	public String getValue() {
		ModoFaturamento modoFaturamento = (ModoFaturamento) getSelectedItem();
		return modoFaturamento != null ? modoFaturamento.cdModoFaturamento : null;
	}
	
	public void setValue(String value) {
		if (ValueUtil.isEmpty(value)) setSelectedIndex(0);
		ModoFaturamento modoFaturamento = new ModoFaturamento(value);
		select(modoFaturamento);
	}
	
	public void carregaModoFaturamento() throws SQLException {
		removeAll();
		ModoFaturamento modoFaturamentoFilter = new ModoFaturamento();
		Vector modoFaturamentoList = ModoFaturamentoService.getInstance().findAllByExample(modoFaturamentoFilter);
		if (ValueUtil.isNotEmpty(modoFaturamentoList)) {
			modoFaturamentoList.qsort();
			add(modoFaturamentoList);
		}
	}
	
}
