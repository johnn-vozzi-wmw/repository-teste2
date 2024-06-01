package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.util.Vector;

public class ProdutoComboBox extends BaseComboBox {

	public ProdutoComboBox() {
		super(Messages.MENU_OPCAO_PRODUTO);
	}

	public Produto getProduto() {
		Produto produto = (Produto) getSelectedItem();
		if (produto != null) {
			return produto;
		} else {
			return null;
		}
	}

	public String getValue() {
		Produto produto = (Produto)getSelectedItem();
		if (produto != null) {
			return produto.cdProduto;
		} else {
			return null;
		}
	}


	public void setValue(String value) {
		if (value != null) {
			Produto produto = new Produto();
			produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produto.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			produto.cdProduto = value;
			select(produto);
		} else {
			setSelectedIndex(-1);
		}
	}

	public void loadProdutos(Vector cdProdutoList) throws SQLException {
		removeAll();
		int size = cdProdutoList.size();
		for (int i = 0; i < size; i++) {
			Produto produto = ProdutoService.getInstance().getProduto((String) cdProdutoList.items[i]);
			if (produto != null) {
				add(produto);
			}
		}
		setSelectedIndex(0);
	}

}
