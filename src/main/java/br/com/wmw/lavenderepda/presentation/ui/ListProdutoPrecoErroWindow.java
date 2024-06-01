package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import totalcross.sys.Convert;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class ListProdutoPrecoErroWindow extends WmwWindow {

	private final String SORT_ATRIBUTTE_DEFAULT = "CDPRODUTO";
	Vector produtoErroList;
	private CondicaoPagamento condicaoPagamento;
	private LabelName lbDescricaoErro;
	private GridListContainer listContainer;
	private String sortAsc = ValueUtil.VALOR_SIM;
	private String sortAtributte = null;

	public ListProdutoPrecoErroWindow(Vector produtoErroList, CondicaoPagamento condicaoPagamento) {
		super(Messages.LISTA_PRODUTOS);
		this.produtoErroList = produtoErroList;
		this.condicaoPagamento = condicaoPagamento;
		lbDescricaoErro = new LabelName("--", CENTER);
		lbDescricaoErro.setForeColor(Color.RED);
		listContainer = new GridListContainer(4,3);
		listContainer.setColsSort(new String[][] { { Messages.PRODUTO_LABEL_CODIGO, "CDPRODUTO" }, { Messages.PRODUTO_LABEL_DSPRODUTO, "DSPRODUTO" }});
		sortAtributte = SORT_ATRIBUTTE_DEFAULT;
		makeUnmovable();
		setDefaultRect();
	}

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return produtoErroList;
	}

	public void initUI() {
	   try {
		super.initUI();
		UiUtil.add(this, lbDescricaoErro, LEFT, TOP, FILL, PREFERRED);
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP * 2, FILL, FILL);
		loadGrid();
		ajustaComponents();
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	private void ajustaComponents() {
		int yTop = getTop() + HEIGHT_GAP;
		lbDescricaoErro.setText(Convert.insertLineBreak(width - 6, lbDescricaoErro.fm, MessageUtil.getMessage(Messages.ERRO_PRODUTO_SEM_PRECO_CONDICAO_PAGAMENTO, StringUtil.getStringValue(condicaoPagamento.dsCondicaoPagamento))));
		lbDescricaoErro.setRect(CENTER, yTop, width - WIDTH_GAP_BIG, PREFERRED);
		listContainer.reposition();
	}

	public void screenResized() {
		super.screenResized();
		ajustaComponents();
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
			Produto produto = (Produto) domain;
			String[] item = {
					StringUtil.getStringValue(""),
					StringUtil.getStringValue(produto.cdProduto) + " - ",
					StringUtil.getStringValue(produto.dsProduto),
					StringUtil.getStringValue("Qt. "+StringUtil.getStringValueToInterface(produto.qtItemPedido))
			};
		return item;
	}

	private void loadGrid() throws SQLException {
		int listSize = 0;
		listContainer.removeAllContainers();
		listSize = produtoErroList.size();
		Container all[] = new Container[listSize];
		if (listSize > 0) {
			BaseDomain domain = getDomainFilterSortable();
			ItemPedido.sortAttr = domain.sortAtributte;
			produtoErroList.qsort();
			BaseListContainer.Item c;
			if (domain.sortAsc == null || domain.sortAsc.startsWith(ValueUtil.VALOR_SIM)) {
				for (int i = 0; i < listSize; i++) {
					domain = (BaseDomain) produtoErroList.items[i];
					all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
					if ((i % 2) == 0) {
						c.setBackColor(Color.darker(c.getBackColor(), 10));
					}
					c.id = ((Produto) domain).cdProduto;
					c.setItens(getItem(domain));
					c.setToolTip(getToolTip(domain));
					domain = null;
				}
			} else {
				int index = 0;
				for (int i = listSize - 1; i >= 0; i--) {
					domain = (BaseDomain) produtoErroList.items[i];
					all[index] = c = new BaseListContainer.Item(listContainer.getLayout());
					if ((i % 2) == 0) {
						c.setBackColor(Color.darker(c.getBackColor(), 10));
					}
					c.id = ((Produto) domain).cdProduto;
					c.setItens(getItem(domain));
					c.setToolTip(getToolTip(domain));
					domain = null;
					index++;
				}
			}
			listContainer.addContainers(all);
		}
	}

	private BaseDomain getDomainFilter() {
		return new Produto();
	}

	private BaseDomain getDomainFilterSortable() {
		BaseDomain baseDomain = getDomainFilter();
		baseDomain.sortAtributte = sortAtributte;
		baseDomain.sortAsc = sortAsc;
		return baseDomain;
	}

	private String getToolTip(Object domain) {
		return StringUtil.getStringValue(((Produto) domain).dsProduto);
	}

	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.WINDOW_CLOSED: {
				if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
					if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listContainer.reloadSortSettings();
						sortAtributte = listContainer.atributteSortSelected;
						sortAsc = listContainer.sortAsc;
						loadGrid();
					}
				}
			}
		}
	}

}