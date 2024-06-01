package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AtributoProd;
import br.com.wmw.lavenderepda.business.service.AtributoProdService;

public class AtributoProdComboBox  extends BaseComboBox {

	public AtributoProdComboBox(String title) {
		super(title);
		defaultItemType = DefaultItemType_ALL;
	}

	public String getValue() {
		AtributoProd atributoProd = (AtributoProd)getSelectedItem();
		if (atributoProd != null) {
			return atributoProd.cdAtributoProduto;
		} else {
			return null;
		}
	}

	public void setValue(String value) {
		if (!ValueUtil.isEmpty(value)) {
			AtributoProd atributoProd = new AtributoProd();
			atributoProd.cdEmpresa = SessionLavenderePda.cdEmpresa;
			atributoProd.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			atributoProd.cdAtributoProduto = value;
			select(atributoProd);
		} else {
			setSelectedIndex(DefaultItemNull);
		}
	}

	public void load() throws SQLException {
		if (LavenderePdaConfig.usaFiltroAtributoProduto) {
			removeAll();
			add(AtributoProdService.getInstance().findAllByEmpresaAndRep());
		}
	}

}
