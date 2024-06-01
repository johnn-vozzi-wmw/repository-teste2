package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.Produto;

public class ListProdutoWindow extends WmwWindow {

	public ListProdutoForm newListProdutoForm;
	public Produto produto;

	public ListProdutoWindow() throws SQLException {
		this("", false);
	}

	public ListProdutoWindow(String cdTabelaPreco, boolean isFromItemPedidoDesktop) throws SQLException {
		super(Messages.PRODUTO_NOME_ENTIDADE);
		newListProdutoForm = new ListProdutoForm(cdTabelaPreco, isFromItemPedidoDesktop);
		newListProdutoForm.inWindowSelectProduto = true;
		newListProdutoForm.setListProdutoWindow(this);
		scrollable = false;
		//--
		setDefaultRect();
	}

	public void initUI() {
		super.initUI();
		UiUtil.add(this, newListProdutoForm, LEFT , getTop(), FILL, FILL - footerH);
		newListProdutoForm.setFocusInListContainer();
	}

	public void fecharWindow() throws SQLException {
		newListProdutoForm.onFormClose();
		super.fecharWindow();
	}


}
