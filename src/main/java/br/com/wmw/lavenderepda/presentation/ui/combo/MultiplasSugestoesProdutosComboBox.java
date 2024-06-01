package br.com.wmw.lavenderepda.presentation.ui.combo;

import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import totalcross.util.Vector;

public class MultiplasSugestoesProdutosComboBox  extends BaseComboBox {
	
	public Vector itensSugestaoVenda;

	public MultiplasSugestoesProdutosComboBox() {
		super(Messages.PEDIDO_LABEL_SUGESTAO_MULTIPLOS_PRODUTOS);
		this.defaultItemType = DefaultItemType_ALL;
		itensSugestaoVenda = new Vector();
	}

	public String getValue() {
		Object selectedItem = getSelectedItem();
		if (selectedItem instanceof SugestaoVenda) {
			return ((SugestaoVenda) selectedItem).dsSugestaoVenda;
		}
		return selectedItem != null ? selectedItem.toString() : ""; 
	}

	public void setValue(String cdSugestaoVenda) {}

	public void load(boolean desvinculadoPedido) throws java.sql.SQLException {
		removeAll();
		itensSugestaoVenda.removeAllElements();
		Vector a = new Vector();
		if (LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro() && !desvinculadoPedido) {
			a.addElement(Messages.PRODUTO_GIROPRODUTO_SUGESTAO_LABEL);
		}
		if (LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento()) {
			a.addElement(Messages.PRODUTO_PRODUTOCOMPLEMENTADO_SUGESTAO_LABEL);
		}
		if (LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda() && !desvinculadoPedido) {
			a.addElement(Messages.PRODUTO_PARAVOCE_SUGESTAO_LABEL);
		}
		itensSugestaoVenda.addElementsNotNull(a.items);
		add(a);
	}

	public void load(Vector sugestaoVendaList) {
		itensSugestaoVenda.removeAllElements();
		removeAll();
		int finalListSize = sugestaoVendaList.size();
		Vector itens = new Vector();
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
			addSugestaoProdutosParaVoce(finalListSize, itens);
			addSugestoesCadastradas(sugestaoVendaList, finalListSize, itens);
			addSugestaoGiro(itens);
		} else {
			Vector sugestoesIndustrias = getAndRemoveSugestaoIndustrias(sugestaoVendaList);
			if (ValueUtil.isNotEmpty(sugestoesIndustrias)) {
				itens.addElementsNotNull(sugestoesIndustrias.items);
			}
			addSugestaoProdutosParaVoce(finalListSize, itens);
			addSugestaoGiro(itens);
			addSugestoesCadastradas(sugestaoVendaList, finalListSize, itens);
		}
		itensSugestaoVenda.addElementsNotNull(itens.items);
		add(itens);
	}

	private Vector getAndRemoveSugestaoIndustrias(Vector sugestaoVendaList) {
		if (sugestaoVendaList.size() == 0) return new Vector();
		int size = sugestaoVendaList.size();
		Vector sugestoesIndustrias = new Vector();
		for (int i = 0; i < size; i++) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestaoVendaList.items[i];
			if (sugestaoVenda.isIndustria()) {
				sugestoesIndustrias.addElement(sugestaoVenda);
				sugestaoVendaList.removeElementAt(i);
				size--;
				i--;
			}
		}
		return sugestoesIndustrias;
	}

	private void addSugestoesCadastradas(Vector sugestaoVendaList, int finalListSize, Vector itens) {
		if (sugestaoVendaList.size() == 0) return;
		itens.addElementsNotNull(sugestaoVendaList.items);
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
			SortUtil.qsortString(itens.items, 0, itens.size() - 1, true);
		}
	}

	private void addSugestaoGiro(Vector itens) {
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro()) return;
		SugestaoVenda sugestaoVenda = new SugestaoVenda();
		sugestaoVenda.dsSugestaoVenda = sugestaoVenda.cdSugestaoVenda = Messages.PRODUTO_GIROPRODUTO_SUGESTAO_LABEL;
		sugestaoVenda.sortAtributte = SugestaoVenda.NMCOLUNA_DSSUGESTAOVENDA;
		itens.addElement(sugestaoVenda);
	}

	private void addSugestaoProdutosParaVoce(int finalListSize, Vector itens) {
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda()) return;
		SugestaoVenda sugestaoVenda = new SugestaoVenda();
		sugestaoVenda.dsSugestaoVenda = sugestaoVenda.cdSugestaoVenda = Messages.PRODUTO_PARAVOCE_SUGESTAO_LABEL;
		sugestaoVenda.sortAtributte = SugestaoVenda.NMCOLUNA_DSSUGESTAOVENDA;
		itens.addElement(sugestaoVenda);
	}

}
