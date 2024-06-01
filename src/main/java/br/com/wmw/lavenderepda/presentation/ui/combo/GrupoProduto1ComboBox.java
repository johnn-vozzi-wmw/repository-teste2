package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.GrupoProduto1;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import totalcross.util.Vector;

public class GrupoProduto1ComboBox extends BaseComboBox {

	public GrupoProduto1ComboBox() {
		this(FrameworkMessages.TITULO_PADRAO_COMBO_BOX);
	}
	
	public GrupoProduto1ComboBox(String title) {
		super(title);
		defaultItemType = DefaultItemType_ALL;
	}

	public GrupoProduto1 getGrupoProduto1() {
		return (GrupoProduto1) getSelectedItem();
	}

	public String getValue() {
		GrupoProduto1 grupoProduto1 = getGrupoProduto1();
		return grupoProduto1 != null ? grupoProduto1.cdGrupoproduto1 : null;
	}

	public void setValue(String value) {
		GrupoProduto1 grupoProduto1 = new GrupoProduto1();
		grupoProduto1.cdGrupoproduto1 = value;
		select(grupoProduto1);
	}
	
	public void loadGrupoProduto1(Pedido pedido) throws SQLException {
		loadGrupoProduto1(pedido, null);
	}
	
	public void loadGrupoProduto1(Pedido pedido, Fornecedor fornecedor) throws SQLException {
		removeAll();
		Vector list = GrupoProduto1Service.getInstance().loadGrupoProduto1(pedido, fornecedor);
		SortUtil.qsortString(list.items, 0, list.size()-1, true);
		add(list);
		setSelectedItemDefault(list, fornecedor);
	}
	
	protected void setSelectedItemDefault(Vector list, Fornecedor fornecedor) {
		if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
			int index = list.size() == 1 && fornecedor != null && ValueUtil.isNotEmpty(fornecedor.cdFornecedor) ? 1 : 0;
			setSelectedIndex(index);
		}
	}
}
