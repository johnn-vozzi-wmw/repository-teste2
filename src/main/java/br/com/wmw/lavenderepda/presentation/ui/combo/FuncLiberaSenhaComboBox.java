package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.ValorParametroService;

public class FuncLiberaSenhaComboBox extends BaseComboBox{

	public FuncLiberaSenhaComboBox() throws SQLException {
		super(Messages.SENHADINAMICA_LABEL_FUNCIONALIDADES);
		load();
	}

	public int getValue() {
		ValorParametro param = (ValorParametro)getSelectedItem();
		if (param != null) {
			return param.cdParametro;
		} else {
			return DefaultItemNull;
		}
	}

	public void setValue(int value) {
		ValorParametro param = new ValorParametro();
		param.cdParametro = value;
		param.cdSistema = ValorParametro.CDSISTEMA;
		param.nmEntidade = ValorParametro.NMENTIDADE_EMPRESA;
		param.vlChave = "[" + StringUtil.getStringValue(SessionLavenderePda.cdEmpresa) + "]";
		select(param);
	}

	public void load() throws java.sql.SQLException {
		removeAll();
		defaultItemType = DefaultItemType_SELECT_ONE_ITEM;
		add(ValorParametroService.getInstance().findAllParamLiberaComSenha());
	}

}
