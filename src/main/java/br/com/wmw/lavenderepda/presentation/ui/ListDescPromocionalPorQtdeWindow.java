package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescPromocional;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.UnidadeService;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListDescPromocionalPorQtdeWindow extends WmwWindow {
	
	private BaseGridEdit grid;
	private EditText edCdProduto;
	private EditMemo edDsProduto;
	private EditText edDsUnidade;
	private EditText edDsTabelaPreco;
	private ButtonPopup btCancelar;
	private ButtonPopup btIgnorar;
	private ButtonPopup btAplicar;
	public DescPromocional descPromocional;
	public boolean isCanceled;
	public boolean isIgnorar;
	private boolean showBtCancelApenas;
	private ItemPedido itemPedido;

	public ListDescPromocionalPorQtdeWindow(boolean showBtCancelApenas,ItemPedido itemPedido) throws SQLException {
		super(Messages.DESC_PROMOCIONAL_POR_QTDE);
		this.itemPedido = (ItemPedido)itemPedido.clone();
		descPromocional = null;
		this.showBtCancelApenas = showBtCancelApenas;
		scrollable = false;
		isIgnorar = false;
		edCdProduto = new EditText("@@@@@@@@@@@@", 100);
		edCdProduto.setEditable(false);
		edCdProduto.setText(itemPedido.getProduto().cdProduto);
		edDsProduto = new EditMemo("@@@@@@@@@@@@", 2, 100);
		edDsProduto.setEditable(false);
		edDsProduto.setText(itemPedido.getProduto().dsProduto);
		edDsUnidade = new EditText("@@@@@@@@@@@@", 100);
		edDsUnidade.setEditable(false);
		edDsUnidade.setText(getDsUnidade(itemPedido));
		edDsTabelaPreco = new EditText("@@@@@@@@@@@@", 100);
		edDsTabelaPreco.setEditable(false);
		edDsTabelaPreco.setText(TabelaPrecoService.getInstance().getDsTabelaPreco(itemPedido.cdTabelaPreco));
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btIgnorar = new ButtonPopup(Messages.DESCONTOQUANTIDADE_BOTAO_IGNORAR);
		btAplicar = new ButtonPopup(Messages.DESCONTOQUANTIDADE_BOTAO_APLICAR);
		setDefaultRect();
	}

	@Override
	public void initUI() {
		try {
			super.initUI();
			if (showBtCancelApenas) {
				addButtonPopup(btAplicar);
				addButtonPopup(btIgnorar);
			}
			addButtonPopup(btCancelar);
			UiUtil.add(this, new LabelName(Messages.PRODUTO_LABEL_CODIGO), edCdProduto, getLeft(), getTop() + HEIGHT_GAP);
			UiUtil.add(this, new LabelName(Messages.PRODUTO_NOME_ENTIDADE), edDsProduto, getLeft(), AFTER);
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				UiUtil.add(this, new LabelName(Messages.METAS_UNIDADE), edDsUnidade, getLeft(), AFTER);
			}
			UiUtil.add(this, new LabelName(Messages.TABELAPRECO_NOME_TABELA), edDsTabelaPreco, getLeft(), AFTER);
			int oneChar = fm.charWidth('A');
			GridColDefinition[] gridColDefiniton = { new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, oneChar * 8, LEFT),
				new GridColDefinition(Messages.PEDIDO_LABEL_DESCONTO_VALOR, oneChar * 6, LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, oneChar * 4, LEFT)};
			grid = UiUtil.createGridEdit(gridColDefiniton);
			if (LavenderePdaConfig.ocultaPercentualDesconto) {
				grid.setColumnWidth(2, -0);
			}
			UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP, FILL, FILL - footerH);
			list();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	@Override
	protected void addBtFechar() { /**/	}

	@Override
	public void onWindowEvent(Event event) throws SQLException {
		switch (event.type) {
		case ControlEvent.PRESSED: {
			if (event.target == btCancelar) {
				isCanceled = true;
				unpop();
			} else if (event.target == btIgnorar) {
				this.showBtCancelApenas = false;
				isCanceled = false;
				isIgnorar = true;
				unpop();
			} else if (event.target == btAplicar) {
				this.showBtCancelApenas = false;
				isCanceled = false;
				unpop();
			}
			break;
		}
		case GridEvent.SELECTED_EVENT: {
			if (event.target == grid && grid.getSelectedIndex() != -1) {
				descPromocional = getSelectedDomainOnGrid();
				unpop();
			}
			break;
		}
		}
	}

	public void list() throws java.sql.SQLException {
		int listSize = 0;
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			Vector domainList =  itemPedido.descPromocionalComQtdList;
			listSize = domainList.size();
			//--
			clearGrid();
			if (listSize > 0) {
				String[][] gridItems;
				gridItems = new String[listSize][4];
				for (int i = 0; i < listSize; i++) {
					gridItems[i] = getItem(domainList.items[i]);
				}
				//--
				grid.setItems(gridItems);
			}
		} finally {
			msg.unpop();
		}
	}

	public void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
		DescPromocional descontoPromocional = (DescPromocional) domain;
		String labelValueDesconto = "";
		if (descontoPromocional.vlPctDescontoProduto > 0) {
			labelValueDesconto = StringUtil.getStringValueToInterface(descontoPromocional.vlPctDescontoProduto) + Messages.SIMBOLO_PERCENTUAL;
		} else if (descontoPromocional.vlDescontoProduto > 0) {
			labelValueDesconto = StringUtil.getStringValueToInterface(descontoPromocional.vlDescontoProduto);
		}
		String[] item = {
				StringUtil.getStringValue(descontoPromocional.rowKey),
				StringUtil.getStringValueToInterface(descontoPromocional.qtItem),
				labelValueDesconto,
				StringUtil.getStringValueToInterface(descontoPromocional.getVlDescVlBaseItemPedido(itemPedido.vlBaseCalculoDescPromocional))};
		return item;
	}

	protected DescPromocional getSelectedDomainOnGrid() {
		DescPromocional descontoQuantidade = new DescPromocional();
		String[] item = grid.getSelectedItem();
		descontoQuantidade.rowKey = item[0];
		descontoQuantidade.populatePrimaryKeyFromRowKey();
		descontoQuantidade.qtItem = ValueUtil.getIntegerValue(ValueUtil.removeThousandSeparator(item[1]));
		descontoQuantidade.vlPctDescontoProduto = ValueUtil.getDoubleValue(item[2]);
		return descontoQuantidade;
	}
    
	private String getDsUnidade(ItemPedido itemPedido) throws SQLException {
		double nuConversaoUnidade = 1;
		ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
		if (LavenderePdaConfig.usaUnidadeAlternativa &&  produtoUnidade != null) {
			nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
		}
		return UnidadeService.getInstance().getDsUnidade(itemPedido.cdUnidade) + "/" + Util.getQtItemPedidoFormatted(nuConversaoUnidade);
	}
    
}
