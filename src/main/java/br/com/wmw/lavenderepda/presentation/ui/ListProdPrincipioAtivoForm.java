package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.PrincipioAtivoComboBox;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Grid;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListProdPrincipioAtivoForm extends WmwWindow {

	private static final int ESTOQUE_COLUMN_INDEX = 2;
	private BaseGridEdit grid;
	private LabelValue lbDescricao1;
	public Produto produto;
	public String cdProdutoAtual;
	public String cdTabelaPrecoAtual;
	private PrincipioAtivoComboBox cbPrincipioAtivo;

	public ListProdPrincipioAtivoForm(String[] principioList, String cdProdutoAtual, String cdTabelaPreco) {
		super(Messages.PRODUTO_LABEL_PRODUTO_GENERICO);
		cbPrincipioAtivo = new PrincipioAtivoComboBox(principioList);
		lbDescricao1 = new LabelValue("Escolha o novo produto:", CENTER);
		this.cdProdutoAtual = cdProdutoAtual;
		this.cdTabelaPrecoAtual = cdTabelaPreco;
		setDefaultRect();
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			UiUtil.add(this, lbDescricao1, CENTER + WIDTH_GAP, getTop() + HEIGHT_GAP);
			UiUtil.add(this, cbPrincipioAtivo, LEFT + WIDTH_GAP, AFTER);
			cbPrincipioAtivo.setSelectedIndex(0);
			int oneChar = fm.charWidth('A');
			StringBuffer dsProduto = new StringBuffer();
			dsProduto.append(Messages.PRODUTO_LABEL_DSPRODUTO);
			int widthDsproduto = oneChar * 20;
			while (fm.stringWidth(dsProduto.toString()) < widthDsproduto) {
				dsProduto.append("  ");
			}
			int estoqueColumnWidth = LavenderePdaConfig.isOcultaEstoque() ? 0 : oneChar * 3;
			//--
			GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
				new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, fm.stringWidth(LavenderePdaConfig.tamanhoColunaCdProduto), LEFT),
				new GridColDefinition(Messages.ESTOQUE_NOME_ENTIDADE_ABREV, estoqueColumnWidth, LEFT),
				new GridColDefinition(dsProduto.toString(), widthDsproduto, LEFT)};
			grid = UiUtil.createGridEdit(gridColDefiniton);
			grid.setGridControllable();
			grid.sortTypes = new int[] {-1, 1, 3, 1};
			UiUtil.add(this, grid, LEFT + WIDTH_GAP, AFTER + HEIGHT_GAP, FILL - WIDTH_GAP, FILL);
			carregaGridProdutos();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private void carregaGridProdutos() throws SQLException {
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			Vector produtoList =  getProdutoList();
			listSize = produtoList.size();
			grid.clear();
			grid.setSelectedIndex(-1);
			if (listSize > 0) {
				String[][] gridItems;
				String[] item = getProdutoListItemToGrid(produtoList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getProdutoListItemToGrid(produtoList.items[i]);
				}
				item = null;
				//--
				grid.setItems(gridItems);
				grid.qsort(3);
				grifaProdutoSemEstoqueLista();
				Grid.repaint();
			}
		} finally {
			msg.unpop();
		}
    }

	private void grifaProdutoSemEstoqueLista() {
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !produto.isIgnoraValidacao()) {
			for (int i = 0; i < grid.size(); i++) {
				double qtEstoque = ValueUtil.getDoubleValue(grid.getItem(i)[ESTOQUE_COLUMN_INDEX]);
				if (qtEstoque <= 0.00) {
					grid.gridController.setRowBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK, i);
				}
			}
		}
	}

    private Vector getProdutoList() throws SQLException {
    	Vector list;
    	if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
    		list = ProdutoTabPrecoService.getInstance().findProdutosByPrincipioAtivo(cbPrincipioAtivo.getValue(), cdProdutoAtual, cdTabelaPrecoAtual);
    	} else {
    		list = ProdutoService.getInstance().findProdutosByPrincipioAtivo(cbPrincipioAtivo.getValue(), cdProdutoAtual);
    	}
    	return list;
    }

	protected String[] getProdutoListItemToGrid(Object domain) throws SQLException {
	    ProdutoBase produto = (ProdutoBase) domain;
	    //--
	    if (produto.estoque == null) {
	    	produto.estoque = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
	    }
		String qtEstoqueFormatted;
		if (produto.estoque.flErroEstoqueOnline) {
			qtEstoqueFormatted = "";
		} else {
			qtEstoqueFormatted = StringUtil.getStringValueToInterface(produto.estoque.qtEstoque);
		}
		//--
	    String[] item = {
	    	StringUtil.getStringValue(produto.getRowKey()),
	    	StringUtil.getStringValue(produto.cdProduto),
	    	qtEstoqueFormatted,
	    	StringUtil.getStringValue(produto.dsProduto)};
	        //--
	    return item;
	}

    private Produto getSelectedProduto() throws SQLException {
    	return (Produto)ProdutoService.getInstance().findByRowKey(grid.getSelectedItem()[0]);
    }

    @Override
    public void onWindowEvent(Event event) throws java.sql.SQLException {
    	super.onWindowEvent(event);
    	switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbPrincipioAtivo) {
					carregaGridProdutos();
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if (event.target == grid) {
					if (grid.getSelectedIndex() != -1) {
						produto = getSelectedProduto();
						unpop();
					}
				}
				break;
			}
		}
    }

    @Override
    protected void onUnpop() {
    	super.onUnpop();
    	grid.setItems(null);
    }

}
