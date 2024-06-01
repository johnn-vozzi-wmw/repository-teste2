package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto4;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.GrupoProduto4Service;
import totalcross.util.Vector;

public class GrupoProduto4ComboBox extends BaseComboBox {
	
	private String cdGrupoProduto1;
	private String cdGrupoProduto2;
	private String cdGrupoProduto3;
	
	public GrupoProduto4ComboBox() {
		this(FrameworkMessages.TITULO_PADRAO_COMBO_BOX);
	}
	
	public GrupoProduto4ComboBox(String title) {
		super(title);
		defaultItemType = DefaultItemType_ALL;
	}
	
	public GrupoProduto4 getGrupoProduto4() {
		return (GrupoProduto4) getSelectedItem();
	}
	
	public String getValue() {
		GrupoProduto4 grupoProduto4 = (GrupoProduto4) getSelectedItem();
		if (grupoProduto4 != null) {
			return grupoProduto4.cdGrupoProduto4;
		} else {
			return null;
		}
	}
	
	public void setValue(String value) {
		GrupoProduto4 grupoProduto4 = new GrupoProduto4();
		grupoProduto4.cdGrupoProduto1 = this.cdGrupoProduto1;
		grupoProduto4.cdGrupoProduto2 = this.cdGrupoProduto2;
		grupoProduto4.cdGrupoProduto3 = this.cdGrupoProduto3;
		grupoProduto4.cdGrupoProduto4 = value;
		select(grupoProduto4);
	}
	
	public void loadGrupoProduto4(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3) throws SQLException {
		loadGrupoProduto4(cdGrupoProduto1, cdGrupoProduto2, cdGrupoProduto3, null);
	}
	
	public void loadGrupoProduto4(String cdGrupoProduto1, String cdGrupoProduto2, String cdGrupoProduto3, Pedido pedido) throws SQLException {
		removeAll();
		if (!LavenderePdaConfig.isNivelGrupoProd4() || ValueUtil.isEmpty(cdGrupoProduto1) || ValueUtil.isEmpty(cdGrupoProduto2) || ValueUtil.isEmpty(cdGrupoProduto3)) {
			return;
		}
		this.cdGrupoProduto1 = cdGrupoProduto1 = LavenderePdaConfig.ocultaGrupoProduto1 ? ValueUtil.VALOR_ZERO : cdGrupoProduto1;
		this.cdGrupoProduto2 = cdGrupoProduto2;
		this.cdGrupoProduto3 = cdGrupoProduto3;
		Vector result = GrupoProduto4Service.getInstance().loadGrupoProduto4(pedido, cdGrupoProduto1, cdGrupoProduto2, cdGrupoProduto3);
		add(result);
	}

}
