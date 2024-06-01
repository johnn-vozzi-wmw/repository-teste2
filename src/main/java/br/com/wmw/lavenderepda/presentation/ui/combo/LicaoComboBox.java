package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.domain.Licao;
import br.com.wmw.lavenderepda.business.service.LicaoService;

public class LicaoComboBox extends BaseComboBox {
	public LicaoComboBox() {
		super("Lições");
	}
	
	public String getValue() {
		Licao licao = (Licao) getSelectedItem();
		if (licao != null) {
			return licao.cdLicao;
		}
		
		return "";
	}
	
	public void setValue(String value) {
		if(!ValueUtil.isEmpty(value)) {
			Licao licao = new Licao();
			licao.cdLicao = value;
			select(licao);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}
	
	public void load() throws SQLException {
		removeAll();
		add(LicaoService.getInstance().findAllByExample(new Licao()));
		qsort();
	}
}
