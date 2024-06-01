package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.BoletoConfig;
import br.com.wmw.lavenderepda.business.service.BoletoConfigService;

public class BoletoConfigComboBox extends BaseComboBox {
	
	public BoletoConfigComboBox(String title) throws SQLException {
		super(title);
		load();
	}
	
	public String getValue() {
		BoletoConfig boletoConfig = (BoletoConfig) getSelectedItem();
		if (boletoConfig != null) {
			return boletoConfig.nuBanco;
		}
		return "";
	}
	
	public void setValue(String value) {
		if (ValueUtil.isNotEmpty(value)) {
			BoletoConfig boletoConfig = new BoletoConfig();
			boletoConfig.nuBanco = value;
			select(boletoConfig);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}
	
	public void load() throws SQLException {
		removeAll();
		add(BoletoConfigService.getInstance().findAllDistinct());
	}
	
}
