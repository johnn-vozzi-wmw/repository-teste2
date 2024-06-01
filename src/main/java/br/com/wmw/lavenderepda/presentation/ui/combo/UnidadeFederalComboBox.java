package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.UF;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.UFService;

public class UnidadeFederalComboBox extends BaseComboBox {

	public UnidadeFederalComboBox() {
		super(LavenderePdaConfig.usaPrecoPorUf ? Messages.PRODUTO_LABEL_UF_TABELA_PRECO : "");
	}

	public UF getUnidadeFederal() {
		if ((this.defaultItemType == BaseComboBox.DefaultItemType_ALL) && (getSelectedIndex() == 0)) {
			return null;
		}
		return (UF)getSelectedItem();
	}

	public String getValue() {
		UF unidadeFederal = (UF)getSelectedItem();
		if (unidadeFederal != null) {
			return unidadeFederal.cdUf;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			UF unidadeFederal = new UF();
			unidadeFederal.cdUf = value;
			select(unidadeFederal);
		} else {
			setSelectedIndex(0);
		}
	
	}

	public void carregaUf() throws SQLException {
		removeAll();
		add(ClienteService.getInstance().getItemTabelaPrecoUf());
		qsort();
	}
	
	public void load() throws java.sql.SQLException {
		removeAll();
		add(UFService.getInstance().findAll());
	}
		
}