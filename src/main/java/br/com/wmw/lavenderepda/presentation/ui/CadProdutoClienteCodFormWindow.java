package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.lavenderepda.business.domain.ProdutoClienteCod;
import totalcross.ui.Window;

public class CadProdutoClienteCodFormWindow extends Window  {

	private CadProdutoClienteCodForm cadProdutoClienteCodForm;

	public CadProdutoClienteCodFormWindow(CadProdutoClienteCodForm cadProdutoClienteCodForm) {
		this.cadProdutoClienteCodForm = cadProdutoClienteCodForm;
	}
	
	public void showWindow() throws SQLException {
		showWindow(null);
	}

	public void showWindow(ProdutoClienteCod produtoClienteCod) throws SQLException {
		setRect(0, 0, FILL, FILL);
		if (produtoClienteCod != null) {
			cadProdutoClienteCodForm.edit(produtoClienteCod);
		}
		swap(cadProdutoClienteCodForm);
		cadProdutoClienteCodForm.onFormShow();
		popup();
	}


}
