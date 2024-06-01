package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto3;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.GrupoProduto3Service;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import totalcross.util.Vector;

public class GrupoProduto3ComboBox extends BaseComboBox {

	private String cdGrupoProduto1;
	private String cdGrupoProduto2;

	public GrupoProduto3ComboBox() {
		this(FrameworkMessages.TITULO_PADRAO_COMBO_BOX);
	}
	
	public GrupoProduto3ComboBox(String title) {
		super(title);
		defaultItemType = DefaultItemType_ALL;
	}

	public GrupoProduto3 getGrupoProduto3() {
		return (GrupoProduto3) getSelectedItem();
	}

	public String getValue() {
		GrupoProduto3 grupoProduto3 = getGrupoProduto3();
		return grupoProduto3 != null ? grupoProduto3.cdGrupoProduto3 : null;
	}

	public void setValue(String value) {
		GrupoProduto3 grupoProduto3 = new GrupoProduto3();
		grupoProduto3.cdGrupoProduto1 = this.cdGrupoProduto1;
		grupoProduto3.cdGrupoProduto2 = this.cdGrupoProduto2;
		grupoProduto3.cdGrupoProduto3 = value;
		select(grupoProduto3);
	}

	public void loadGrupoProduto3(String cdGrupoProduto1, String cdGrupoProduto2, Pedido pedido) throws SQLException {
		removeAll();
		if (!LavenderePdaConfig.isNivelGrupoProd3() || ValueUtil.isEmpty(cdGrupoProduto1) || ValueUtil.isEmpty(cdGrupoProduto2)) {
			return;
		}
		this.cdGrupoProduto1 = cdGrupoProduto1 = LavenderePdaConfig.ocultaGrupoProduto1 ? ValueUtil.VALOR_ZERO : cdGrupoProduto1;
		this.cdGrupoProduto2 = cdGrupoProduto2;
		add(GrupoProduto3Service.getInstance().loadGrupoProduto3(pedido, cdGrupoProduto1, cdGrupoProduto2));
	}

}
