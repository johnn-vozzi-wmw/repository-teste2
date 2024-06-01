package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.TipoLancamento;
import br.com.wmw.lavenderepda.business.service.TipoLancamentoService;
import totalcross.util.Vector;

import java.sql.SQLException;

public class TipoLancamentoComboBox extends BaseComboBox {

	private Vector tipoLancamentoList;

	public TipoLancamentoComboBox() throws SQLException {
		super(Messages.FECHAMENTO_DIARIO_LAN_TIPO_LANCAMENTO);
		this.defaultItemType = BaseComboBox.DefaultItemType_SELECT_ONE_ITEM;
		load();
		setSelectedIndex(0);
	}

	public String getValue() {
		int index = getSelectedIndex();
		return index == 0 ? null : ((TipoLancamento) tipoLancamentoList.items[index - 1]).cdTipoLancamento;
	}

	public void load() throws SQLException {
		removeAll();
		TipoLancamento filter = TipoLancamentoService.getInstance().getFilterByCd(null);
		tipoLancamentoList = TipoLancamentoService.getInstance().findAllByExample(filter);
		add(tipoLancamentoList);
	}
}
