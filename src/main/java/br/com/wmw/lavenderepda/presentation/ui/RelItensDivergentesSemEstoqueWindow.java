package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

public class RelItensDivergentesSemEstoqueWindow extends WmwListWindow {
	
	private GridListContainer listItemContainer;
	private ButtonPopup btLista;
	private ListItemPedidoForm listItemPedidoForm;
	private LabelValue subTitulo;
	private double estoqueFaltante;
	public boolean chamarLista;
	private Vector listItensSemEstoque;

	public RelItensDivergentesSemEstoqueWindow(String title) {
		super(title);
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return null;
	}

	public RelItensDivergentesSemEstoqueWindow(Vector list, CadPedidoForm cadPedidoForm, Pedido pedido, String title) throws SQLException {
		super(title);
		estoqueFaltante = 0;
		listItensSemEstoque = list;
		int qtLabels = LavenderePdaConfig.exibeQuantidadeEstoqueFaltanteItemNaLista ? 7 : 5;
		listItemContainer = new GridListContainer(qtLabels, 2);
		setColPositionRight(qtLabels);
		listItemContainer.setUseSortMenu(false);
		listItemContainer.setBarTopSimple();
		btLista = new ButtonPopup(Messages.REL_DIVERGENCIA_ESTOQUE_BT_LISTA);
		listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
		listItemPedidoForm.setShowOnlyItensSemEstoque(true);
		listItemPedidoForm.inWindowMode = true;
		setDefaultRect();
	}
	
	public void setColPositionRight(int qtLabels) {
		for(int i = 3; i < qtLabels; i+=2) {
			listItemContainer.setColPosition(i, RIGHT);
		}
	}
	
	@Override
	protected void onFormStart() throws SQLException {
		addSubtitulo();
		UiUtil.add(this, listItemContainer, LEFT, AFTER+HEIGHT_GAP, FILL, FILL);
		this.addButtonPopup(this.btLista);
		loadListContainer(listItensSemEstoque, listItemContainer);
	}
	
	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btLista) {
					chamarLista = true;
					unpop();
				} else if (event.target == btFechar) {
					unpop();
				}
				break;
			}
		}
	}
	
	private void loadListContainer(Vector itemPedidoList, GridListContainer listContainer) throws SQLException {
		listContainer.removeAllContainers();
		int size = itemPedidoList.size();
		if (size > 0) {
			Container[] all = new Container[size];
			BaseListContainer.Item c;
			ItemPedido itemPedido;
			for (int i = 0; i < size; i++) {
				all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
				if (i % 2 == 0) {
					c.setBackColor(Color.darker(c.getBackColor(), 10));
				}
				itemPedido = (ItemPedido) itemPedidoList.items[i];
				c.setItens(getItem(itemPedido));
				c.setToolTip(itemPedido.getDsProduto());
				setPropertiesGradeInRowList(itemPedido, c, listContainer);
			}
			itemPedido = null;
			listContainer.addContainers(all);
		}
	}

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		return listItensSemEstoque;
	}
	
	private void setPropertiesGradeInRowList(ItemPedido itemPedido, BaseListContainer.Item c, GridListContainer listContainer) throws SQLException {
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaItensAdicionados() && isEstoqueFaltante(itemPedido) && !itemPedido.isItemBonificacao()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_SEM_ESTOQUE);
		}
	}
	
	public boolean isEstoqueFaltante(ItemPedido itemPedido) throws SQLException {
		return	ProdutoService.getInstance().produtoSemEstoque(itemPedido.getProduto(), itemPedido.getCdLocalEstoque());
	}
	
	protected String[] getItem(Object domain) throws java.sql.SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
		String mensagemEstoqueFaltante = null;
		boolean exibeQuantidadeEstoqueFaltanteItemNaLista = false;
        if (LavenderePdaConfig.exibeQuantidadeEstoqueFaltanteItemNaLista && itemPedido.pedido.isFlOrigemPedidoPda()) {
			exibeQuantidadeEstoqueFaltanteItemNaLista = true;
			if (LavenderePdaConfig.isConfigGradeProduto() && ValueUtil.valueEquals(ProdutoGrade.CD_ITEM_GRADE_PADRAO, itemPedido.cdItemGrade1)) {
				Vector itemPedidoGradeList = ItemPedidoGradeService.getInstance().getItemPedidoGradeByItemPedido(itemPedido);
				int size = itemPedidoGradeList.size();
				for (int i = 0; i < size; i++) {
					ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedidoGradeList.items[i];
					Estoque estoque = EstoqueService.getInstance().getEstoque(itemPedido.cdProduto, itemPedidoGrade.cdItemGrade1, itemPedidoGrade.cdItemGrade2, itemPedidoGrade.cdItemGrade3, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_ERP, null);
					estoqueFaltante += EstoqueService.getInstance().getEstoqueFaltante(estoque, itemPedidoGrade.qtItemFisico);
				}
			} else {
				Estoque estoque = EstoqueService.getInstance().getEstoque(itemPedido.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
				estoqueFaltante = EstoqueService.getInstance().getEstoqueFaltante(estoque, itemPedido.getQtItemFisico());	
			}
			mensagemEstoqueFaltante = (Util.getQtItemPedidoFormatted(estoqueFaltante) + " " + (estoqueFaltante >= 2 ? Messages.ITEMPEDIDO_MSG_ITENS_FALTANTES : Messages.ITEMPEDIDO_MSG_ITEM_FALTANTE));
        }
		ArrayList<String> labels = new ArrayList<String>();
		labels.add(StringUtil.getStringValue(itemPedido.cdProduto));
		labels.add("-" + StringUtil.getStringValue(itemPedido.getProduto().dsProduto));
		labels.add(Util.getQtItemPedidoFormatted(itemPedido.getQtItemFisico()) + " " +  Messages.LISTA_ITEMPEDIDO_DESC_QUANTIDADE_ITEMS + " -" + " " + Messages.MOEDA + " " + StringUtil.getStringValueToInterface(itemPedido.getVlItemComSt()));
		labels.add(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido));
		if (exibeQuantidadeEstoqueFaltanteItemNaLista) {
			labels.add(Util.getQtItemPedidoFormatted((itemPedido.getQtItemFisico() == 0) ? 0 : (itemPedido.getQtItemFisico() - estoqueFaltante)) + Messages.PRODUTO_LABEL_EM_ESTOQUE);
			labels.add(mensagemEstoqueFaltante);
		}
		labels.add(ItemPedidoService.getInstance().getDsTabelaPreco(itemPedido));
        return labels.toArray(new String[0]);
    }

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return null;
	}

	private void addSubtitulo() {
		subTitulo = new LabelValue(Messages.MSG_TIPO_PEDIDO_NAO_ALTERADO_SUBTITULO_RELATORIO);
		subTitulo.setFont(Font.getFont(Font.NORMAL_SIZE));
		UiUtil.add(this, subTitulo, AFTER + WIDTH_GAP_BIG, getNextY(), FILL, PREFERRED);
		subTitulo.setMultipleLinesOriginalBreakText(Messages.MSG_TIPO_PEDIDO_NAO_ALTERADO_SUBTITULO_RELATORIO);
	}

}
