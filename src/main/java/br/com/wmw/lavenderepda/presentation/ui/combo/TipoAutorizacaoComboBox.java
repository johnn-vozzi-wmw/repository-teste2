package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.PermissaoSolAut;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.PermissaoSolAutService;
import totalcross.util.Vector;

public class TipoAutorizacaoComboBox extends BaseComboBox {

	public TipoAutorizacaoComboBox() throws SQLException {
		super(Messages.SOL_AUTORIZACAO_DS_TIPO_SOL_AUTORIZACAO);
		load();
	}

	public TipoSolicitacaoAutorizacaoEnum getValue() {
		int selectedIndex = getSelectedIndex();
		TipoSolicitacaoAutorizacaoEnum[] values = TipoSolicitacaoAutorizacaoEnum.values();
		return selectedIndex > 0 && selectedIndex <= values.length ? values[selectedIndex] : null;
	}

	public void setValue(String value) {
		try {
			select(TipoSolicitacaoAutorizacaoEnum.valueOf(value));
		} catch (IllegalArgumentException e) {
			setSelectedIndex(0);
			ExceptionUtil.handle(e);
		}
	}

	public void load() throws SQLException {
		removeAll();
		Vector itens = new Vector();
		TipoSolicitacaoAutorizacaoEnum[] enums = TipoSolicitacaoAutorizacaoEnum.values();
		Vector permissaoList = PermissaoSolAutService.getInstance().getBySessionUser();
		int size = permissaoList.size();
		for (int i = 0; i < size; i++) {
			itens.addElement(enums[((PermissaoSolAut) permissaoList.items[i]).tipoSolicitacaoAutorizacaoEnum.ordinal()].getTitle());
		}
		defaultItemType = itens.size() > 1 ? DefaultItemType_ALL : DefaultItemType_NONE;
		add(itens);
	}

}
