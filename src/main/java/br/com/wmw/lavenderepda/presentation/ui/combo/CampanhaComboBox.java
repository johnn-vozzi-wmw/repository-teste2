package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Campanha;
import br.com.wmw.lavenderepda.business.service.CampanhaService;

public class CampanhaComboBox extends BaseComboBox {

	public CampanhaComboBox(String title) throws SQLException {
		super(title);
		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
			load();
		}
	}

	public String getValue() {
		Campanha campanha = (Campanha)getSelectedItem();
		if (campanha != null) {
			return campanha.cdCampanha;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		Campanha campanha = new Campanha();
		campanha.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		campanha.cdEmpresa = SessionLavenderePda.cdEmpresa;
		campanha.cdCampanha = value;
		select(campanha);
	}

	private void load() throws SQLException {
		removeAll();
		add(CampanhaService.getInstance().findAllCampanhas());
		qsort();
	}
}
