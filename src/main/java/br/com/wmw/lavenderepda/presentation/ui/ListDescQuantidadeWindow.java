package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DescQuantidade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.DescQtdeAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.DescQuantidadeService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.util.Vector;

public class ListDescQuantidadeWindow extends WmwWindow {

	private Vector descQuantidadeList;
	private Vector produtosSimilaresList;
	public DescQuantidade descontoQuantidadeSelected;
	private BaseGridEdit grid;
	private BaseGridEdit gridProdutosSimilares;
	private Produto produto;
	private LabelValue lvDsUnidade;
	private LabelValue lvDsTabelaPreco;
	private LabelValue lvDsQtdSimilar;
	private ButtonPopup btCancelar;
	private ButtonPopup btIgnorar;
	private ButtonPopup btAplicar;
	public boolean isCanceled;
	public boolean isIgnorar;
	private double vlBaseItemPedido;
	private boolean showBtSemDesconto;
	private boolean isBloqueiaAlteracaoDescQtd;
	private boolean isShowGridItensSimilares;
	private Pedido pedido;

	public ListDescQuantidadeWindow(Produto produto, String cdTabelaPreco, Vector descQuantidadeList, boolean maiorDesc, double vlBaseItemPedido, String cdUnidade, boolean showBtSemDesconto, ItemPedido itemPedido) throws SQLException {
		super(maiorDesc ? Messages.DESCONTOQUANTIDADE_MAIOR_DESCONTO_TITULO : Messages.DESCONTOQUANTIDADE_NOME_ENTIDADE);
		this.produto = produto;
		this.descQuantidadeList = descQuantidadeList;
		lvDsTabelaPreco = new LabelValue(TabelaPrecoService.getInstance().getDsTabelaPreco(cdTabelaPreco));
		lvDsTabelaPreco.setID("lvDsTabelaPreco");
		lvDsUnidade = new LabelValue(cdUnidade);
		lvDsUnidade.setID("lvDsUnidade");
		descontoQuantidadeSelected = null;
		this.vlBaseItemPedido = vlBaseItemPedido;
		this.showBtSemDesconto = showBtSemDesconto;
		isIgnorar = false;
		isBloqueiaAlteracaoDescQtd = LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && SolAutorizacaoService.getInstance().isItemPedidoAutorizadoOuPendente(itemPedido, null);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btIgnorar = new ButtonPopup(Messages.DESCONTOQUANTIDADE_BOTAO_IGNORAR);
		btAplicar = new ButtonPopup(Messages.DESCONTOQUANTIDADE_BOTAO_APLICAR);
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && DescQuantidadeService.getInstance().isProdutoPossuiSimilares(cdTabelaPreco, itemPedido.cdProduto)) {
			Vector itensSimilares = DescQtdeAgrSimilarService.getInstance().getProdutoItensPedidoSimilares(itemPedido, DescQuantidadeService.getInstance().getCdProdutoDescQtd(cdTabelaPreco, itemPedido.cdProduto, null), false);
			double qtdVendidosSimilares = getSumQtItemFisicoSimilares(itensSimilares);
			lvDsQtdSimilar = new LabelValue(MessageUtil.getMessage(Messages.DESCQTD_ITENS_SIMILARES_VENDIDOS, StringUtil.getStringValueToInterface(qtdVendidosSimilares, LavenderePdaConfig.isUsaQtdInteiro() ? 0 : LavenderePdaConfig.nuCasasDecimais)));
			lvDsQtdSimilar.setID("lvDsQtdSimilar");
			isShowGridItensSimilares = !LavenderePdaConfig.ocultaListaSimilaresDescQuantidade ? itensSimilares.size() > 0 : false;
			this.pedido = itemPedido.pedido;
			if(isShowGridItensSimilares) {
				produtosSimilaresList = itensSimilares;
			}
		}
		setDefaultRect();
	}
	
	private void loadGridProdutosSimilares() throws SQLException {
		int widths[] = getWidths();
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
			new GridColDefinition(Messages.DESCQTD_LABEL_QTESTOQUE, widths[0], CENTER),
			new GridColDefinition(Messages.DESCQTD_LABEL_QTITEMFISICO, widths[1], CENTER),
			new GridColDefinition(Messages.PRODUTO_LABEL_PRODUTOS_SIMILARES, widths[2], LEFT)};
		gridProdutosSimilares = UiUtil.createGridEdit(gridColDefiniton);
		gridProdutosSimilares.setID("gridProdutosSimilares");
		UiUtil.add(this, gridProdutosSimilares);
		gridProdutosSimilares.setVisibleLines(3);
		gridProdutosSimilares.setRect(getLeft(), getNextY(), getWFill(), gridProdutosSimilares.getPreferredHeight());
	}

	//@Override
	public void initUI() {
		try {
			super.initUI();
			if (showBtSemDesconto) {
				addButtonPopup(btAplicar);
				if (!LavenderePdaConfig.ocultaBotaoIgnorar) {
					addButtonPopup(btIgnorar);
				}
			}
			addButtonPopup(btCancelar);
			//--
			
			UiUtil.add(this, new LabelName(Messages.PRODUTO_NOME_ENTIDADE), new LabelValue(produto.toString()).setID("lvDsProduto"), getLeft(), getNextY());
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				UiUtil.add(this, new LabelName(Messages.METAS_UNIDADE), lvDsUnidade, getLeft(), getNextY());
			}
			if (!isShowGridItensSimilares) {
				UiUtil.add(this, new LabelName(Messages.TABELAPRECO_NOME_TABELA), lvDsTabelaPreco, getLeft(), getNextY());
			}
			if (lvDsQtdSimilar != null) {
				UiUtil.add(this, new LabelName(Messages.DESCQTD_SIMILARES), lvDsQtdSimilar, getLeft(), getNextY());
			}
			//--
			int oneChar = fm.charWidth('A');
			boolean colCdDesconto = LavenderePdaConfig.usaDescontoPorQuantidadeValor;
			GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_QTITEMFISICONAOABREVIADO, oneChar * 9, CENTER),
				new GridColDefinition(!colCdDesconto && !LavenderePdaConfig.ocultaPercentualDescontoListaFaixa ? Messages.DESCONTO_LABEL_VLPCTDESCONTO : FrameworkMessages.CAMPO_ID , oneChar * 12, CENTER),
				new GridColDefinition(colCdDesconto ? Messages.DESCONTOQUANTIDADE_NOME_ENTIDADE : FrameworkMessages.CAMPO_ID, oneChar * 12, CENTER),
				new GridColDefinition(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, oneChar * 8, CENTER)};
			grid = UiUtil.createGridEdit(gridColDefiniton);
			grid.setID("grid");
			UiUtil.add(this, grid);
			grid.setVisibleLines(4);
			grid.setRect(getLeft(), getNextY(), getWFill(), grid.getPreferredHeight());
			if(isShowGridItensSimilares) {
				loadGridProdutosSimilares();
			}
			//--
			list();
	   	} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
	   	}
	}
	
	@Override
	protected void addBtFechar() {
	}

	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCancelar) {
					isCanceled = true;
					unpop();
				} else if (event.target == btIgnorar) {
					isCanceled = false;
					isIgnorar = true;
					unpop();
				}
				else if (event.target == btAplicar) {
					isCanceled = false;
					unpop();
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if ((event.target == grid) && (grid.getSelectedIndex() != -1) && !isBloqueiaAlteracaoDescQtd) {
					descontoQuantidadeSelected = getSelectedDomainOnGrid();
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
			Vector domainList = this.descQuantidadeList;
			listSize = domainList.size();
			//--
			clearGrid();
			if (listSize > 0) {
				String[][] gridItems;
				String[] item = getItem(domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItem(domainList.items[i]);
				}
				item = null;
				//--
				grid.setItems(gridItems);
				domainList = null;
				loadProdutosSimilaresList();
			}
		} finally {
			msg.unpop();
		}
	}
	
	private void loadProdutosSimilaresList() throws SQLException {
		if(isShowGridItensSimilares) {
			Vector itensSimilaresList = this.produtosSimilaresList;
			int listSize = itensSimilaresList.size();
			String[][] gridItems = new String[listSize][getItemPedidoSimilar(itensSimilaresList.items[0]).length];
			for (int i = 0; i < listSize; i++) {
				gridItems[i] = getItemPedidoSimilar(itensSimilaresList.items[i]);
			}
			gridProdutosSimilares.setItems(gridItems);
		}
	}
	
	public void clearGrid() {
		grid.clear();
		grid.setSelectedIndex(-1);
		if(isShowGridItensSimilares) {
			gridProdutosSimilares.clear();
			gridProdutosSimilares.setSelectedIndex(-1);
		}
	}

    protected String[] getItem(Object domain) throws java.sql.SQLException {
        DescQuantidade descontoQuantidade = (DescQuantidade) domain;
        String[] item = {
                StringUtil.getStringValue(descontoQuantidade.rowKey),
                StringUtil.getStringValueToInterface(descontoQuantidade.qtItem, LavenderePdaConfig.isUsaQtdInteiro() ? 0 : LavenderePdaConfig.nuCasasDecimais),
                StringUtil.getStringValueToInterface(descontoQuantidade.vlPctDesconto),
                StringUtil.getStringValueToInterface(descontoQuantidade.vlDesconto),
                StringUtil.getStringValueToInterface(descontoQuantidade.getVlDescVlBaseItemPedido(vlBaseItemPedido))};
        return item;
    }
    
    protected String[] getItemPedidoSimilar(Object domain) throws SQLException {
        ItemPedido itemPedidoSimilar = (ItemPedido) domain;
        itemPedidoSimilar.pedido = this.pedido;
        String[] item = {
    		StringUtil.getStringValue(itemPedidoSimilar.cdProduto),
    		StringUtil.getStringValue(EstoqueService.getInstance().getEstoqueToString(EstoqueService.getInstance().getQtEstoqueErpPda(itemPedidoSimilar, itemPedidoSimilar.getCdLocalEstoque()))),
    		LavenderePdaConfig.isUsaQtdInteiro() ? StringUtil.getStringValueToInterface((int) itemPedidoSimilar.qtItemFisico) : StringUtil.getStringValueToInterface(itemPedidoSimilar.qtItemFisico),
            StringUtil.getStringValue(itemPedidoSimilar.getProduto())};
        return item;
    }

    protected DescQuantidade getSelectedDomainOnGrid() {
        DescQuantidade descontoQuantidade = new DescQuantidade();
        String[] item = grid.getSelectedItem();
        descontoQuantidade.rowKey = item[0];
        double qtItemSemSeparadores = ValueUtil.getDoubleValue(ValueUtil.removeThousandSeparator(item[1]));
		descontoQuantidade.qtItem = ValueUtil.getIntegerValue(qtItemSemSeparadores);
       	descontoQuantidade.vlPctDesconto = ValueUtil.getDoubleValue(item[2]);
       	descontoQuantidade.vlDesconto = ValueUtil.getDoubleValue(item[3]);
        return descontoQuantidade;
    }
    
    private double getSumQtItemFisicoSimilares(Vector itensSimilares) {
    	int size = itensSimilares.size();
    	double sumQtItemFisicoSimilares = 0;
    	for(int i = 0; i < size; i++) {
    		sumQtItemFisicoSimilares += ((ItemPedido)itensSimilares.items[i]).qtItemFisico;
    	}
    	return sumQtItemFisicoSimilares;
    }
    
    private int[] getWidths() throws SQLException {
    	int[] widths = new int[3];
    	int[] biggerWidths = new int[3];
    	for(int i = 0; i < produtosSimilaresList.size(); i++) {
    		ItemPedido itemPedido = (ItemPedido) produtosSimilaresList.items[i];
    		itemPedido.pedido = this.pedido;
    		widths[0] = fm.stringWidth(StringUtil.getStringValue(EstoqueService.getInstance().getEstoqueToString(EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque())))) + 2;
    		biggerWidths[0] = widths[0] > biggerWidths[0] ? widths[0] : biggerWidths[0];
    		widths[1] = fm.stringWidth(LavenderePdaConfig.isUsaQtdInteiro() ? StringUtil.getStringValueToInterface((int) itemPedido.qtItemFisico) : StringUtil.getStringValueToInterface(itemPedido.qtItemFisico));
    		biggerWidths[1] = widths[1] > biggerWidths[1] ? widths[1] : biggerWidths[1];
    		widths[2] = fm.stringWidth(StringUtil.getStringValue(itemPedido.getProduto()));
    		biggerWidths[2] = widths[2] > biggerWidths[2] ? widths[2] : biggerWidths[2];
    	}
    	return biggerWidths;
    }
}
