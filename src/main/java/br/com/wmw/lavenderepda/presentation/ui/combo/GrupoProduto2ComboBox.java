package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto2;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.GrupoProduto2Service;
import totalcross.util.Vector;

public class GrupoProduto2ComboBox extends BaseComboBox {

	private String cdGrupoProduto1;

	public GrupoProduto2ComboBox() {
		this(FrameworkMessages.TITULO_PADRAO_COMBO_BOX);
	}
	
	public GrupoProduto2ComboBox(String title) {
		super(title);
		defaultItemType = DefaultItemType_ALL;
	}

	public GrupoProduto2 getGrupoProduto2() {
		return (GrupoProduto2) getSelectedItem();
	}

	public String getValue() {
		GrupoProduto2 grupoProduto2 = getGrupoProduto2();
		return grupoProduto2 != null ? grupoProduto2.cdGrupoProduto2 : null;
	}

	public void setValue(String value) {
		GrupoProduto2 grupoProduto2 = new GrupoProduto2();
		grupoProduto2.cdGrupoProduto1 = this.cdGrupoProduto1;
		grupoProduto2.cdGrupoProduto2 = value;
		select(grupoProduto2);
	}

	public void loadGrupoProduto2(String cdGrupoProduto1, Pedido pedido) throws SQLException {
		removeAll();
		if (!LavenderePdaConfig.isNivelGrupoProd2() || ValueUtil.isEmpty(cdGrupoProduto1)) {
			return;
		}
		this.cdGrupoProduto1 = cdGrupoProduto1 = LavenderePdaConfig.ocultaGrupoProduto1 ? ValueUtil.VALOR_ZERO : cdGrupoProduto1;
		Vector result = GrupoProduto2Service.getInstance().loadGrupoProduto2(pedido, cdGrupoProduto1);
		add(result);
	}
	
}
