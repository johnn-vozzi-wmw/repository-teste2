package br.com.wmw.lavenderepda.presentation.ui.combo;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import totalcross.util.Vector;

public class ProdutoUnidadeComboBox extends BaseComboBox {

	private ItemPedido itemPedido;

	public ProdutoUnidadeComboBox() {
		super(Messages.TOOLTIP_LABEL_UNIDADE_ALTERNATIVA);
	}

	public ProdutoUnidade getProdutoUnidade() {
		return (ProdutoUnidade)getSelectedItem();
	}

	public String getValue() {
		ProdutoUnidade produtoUnidade = getProdutoUnidade();
		if (produtoUnidade != null) {
			return produtoUnidade.cdUnidade;
		} else {
			return ProdutoUnidade.CDUNIDADE_PADRAO;
		}
	}

	public ProdutoUnidade getProdutoUnidadeSelected() {
		ProdutoUnidade produtoUnidade = getProdutoUnidade();
		if (produtoUnidade != null) {
			return produtoUnidade;
		} else {
			produtoUnidade = new ProdutoUnidade();
			produtoUnidade.nuConversaoUnidade = 1;
			return produtoUnidade;
		}
	}
	
	public void setValue(String value, String cdProduto, String cdItemGrade1) throws SQLException {
		if (value != null) {
			ProdutoUnidade produtoUnidade = createProdutoUnidade(value, cdItemGrade1, cdProduto);
			select(produtoUnidade);
			if (getSelectedIndex() == -1) {
				setValueItemGradePadrao(value, cdProduto);
			}
		} else {
			setSelectedIndex(-1);
		}
	}

	public void setValue(String value) throws SQLException {
		if (value != null) {
			ProdutoUnidade produtoUnidade = createProdutoUnidade(value, itemPedido.cdItemGrade1, itemPedido.getProduto().cdProduto);
			select(produtoUnidade);
			if (getSelectedIndex() == -1) {
				setValueItemGradePadrao(value, itemPedido.getProduto().cdProduto);
			}
		} else {
			setSelectedIndex(-1);
		}
	}
	
	private void setValueItemGradePadrao(String value, String cdProduto) throws SQLException {
		if (value != null) {
			ProdutoUnidade produtoUnidade = createProdutoUnidadeGradePadrao(value, cdProduto);
			select(produtoUnidade);
		} else {
			setSelectedIndex(-1);
		}
	}
	
	private ProdutoUnidade createProdutoUnidade(String value, String cdItemGrade1, String cdProduto) throws SQLException {
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produtoUnidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoUnidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoUnidade.cdProduto = cdProduto;
		produtoUnidade.cdItemGrade1 = cdItemGrade1;
		produtoUnidade.cdUnidade = value;
		return produtoUnidade;
	}
	
	private ProdutoUnidade createProdutoUnidadeGradePadrao(String value, String cdProduto) throws SQLException {
		ProdutoUnidade produtoUnidade = new ProdutoUnidade();
		produtoUnidade.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produtoUnidade.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produtoUnidade.cdProduto = cdProduto;
		produtoUnidade.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		produtoUnidade.cdUnidade = value;
		return produtoUnidade;
	}
	
	public void load(ItemPedido itemPedido) throws SQLException {
		this.itemPedido = itemPedido;
		removeAll();
		Vector produtoUnidadeList = ProdutoUnidadeService.getInstance().findAllByProduto(itemPedido, itemPedido.getProduto(), itemPedido.cdTabelaPreco);
		SortUtil.qsortDouble(produtoUnidadeList.items, 0, produtoUnidadeList.size() - 1, true);
		add(produtoUnidadeList);
	}
	
	public ItemPedido getItemPedido() {
		return itemPedido;
	}
	

}