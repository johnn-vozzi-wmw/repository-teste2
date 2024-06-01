package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.GridEditEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ClassUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.IntVector;
import totalcross.util.Vector;

public class RelPrecosPorCondicaoWindow extends WmwWindow {

	private BaseGridEdit grid;
	private ButtonPopup btCalc;
	private ItemPedido itemBase;
	private Cliente cliente;
	private TabelaPrecoComboBox cbTabelaPreco;
	private Produto produto;
	private boolean inRelatorioMode;
	private LabelContainer lbDsProdutoContainer;

	public RelPrecosPorCondicaoWindow(ItemPedido itemPedido, Cliente cliente, Produto produto, boolean inRelatorioMode) throws SQLException {
		super(Messages.PEDIDO_PRECOCONDICAO_RELATORIO);
		cbTabelaPreco = new TabelaPrecoComboBox();
		this.inRelatorioMode = inRelatorioMode;
		if (inRelatorioMode) {
			cbTabelaPreco.loadForListProduto();
			cbTabelaPreco.setSelectedIndex(0);
			this.produto = produto;
			itemPedido = loadItemPedido();
		} else {
			this.produto = itemPedido.getProduto();
		}
  		this.itemBase = itemPedido;
		this.cliente = cliente;
		btCalc = new ButtonPopup(Messages.MENU_UTILITARIO_CALCULADORA);
		btCalc.setBackColor(btFechar.getBackColor());
		lbDsProdutoContainer = new LabelContainer(ProdutoService.getInstance().getDescricaoProdutoReferenciaConsideraCodigo(itemBase.getProduto()));
		
		setDefaultRect();
	}

	private ItemPedido loadItemPedido() throws SQLException {
		ItemPedido itemPedido;
		ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cbTabelaPreco.getValue(), produto.cdProduto, null);
		itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemPedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
		itemPedido.setProduto(produto);
		itemPedido.cdProduto = produto.cdProduto;
		if (itemTabelaPreco != null) {
			itemPedido.vlBaseItemTabelaPreco = itemTabelaPreco.vlUnitario;
			itemPedido.vlBaseItemPedido = itemTabelaPreco.vlUnitario;
		}
		return itemPedido;
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		UiUtil.add(this, lbDsProdutoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		if (inRelatorioMode) {
			UiUtil.add(this, new LabelName(Messages.TABELA_PRECO), cbTabelaPreco, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.isMostraPrecosPorCondicaoPagamentoPercentual()) { 
			GridColDefinition[] gridColDefiniton = {
				new GridColDefinition(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO, -50, LEFT),
				new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -25, CENTER),
				new GridColDefinition(Messages.PRODUTO_LABEL_VL_INDICE_FINANCEIRO, -25, CENTER)};
			grid = UiUtil.createGridEdit(gridColDefiniton, false);
		
		} else {
			GridColDefinition[] gridColDefiniton = {
					new GridColDefinition(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO, -75, LEFT),
					new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -25, CENTER)};
			grid = UiUtil.createGridEdit(gridColDefiniton, false);			
		}
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
		carregaGrid();
		addButtonPopup(btCalc);
		addButtonPopup(btFechar);
		} catch (Throwable ee) {ee.printStackTrace();}
	}

	public void carregaGrid() throws SQLException {
		CondicaoPagamento condFilter = new CondicaoPagamento();
		condFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		condFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoPagamento.class);
		Vector list = new Vector();
		if (LavenderePdaConfig.isMostraPrecosPorCondicaoPagamento()) {
			list = CondicaoPagamentoService.getInstance().findAllByExample(condFilter);
		} else if (LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido) {
			list = CondicaoPagamentoService.getInstance().findAllByExampleOrderByQtDiasMedio(condFilter);
		}
		IntVector listValidation = new IntVector();
		if (!ValueUtil.isEmpty(list)) {
			if (LavenderePdaConfig.isOrdenaCondPagtoPedidoPorDiasMedioPagtoOnRel()) {
				SortUtil.qsortInt(list.items, 0, list.size() - 1, true);
			}
			int size = list.size();
			CondicaoPagamento condPagto;
			ItemPedido itemPedidoTest = new ItemPedido();
	        for (int i = 0; i < size; i++) {
				itemPedidoTest.cdProduto = itemBase.cdProduto;
				itemPedidoTest.setProduto(itemBase.getProduto());
				itemPedidoTest.cdTabelaPreco = itemBase.cdTabelaPreco;
				itemPedidoTest.vlBaseItemTabelaPreco = itemBase.vlBaseItemTabelaPreco;
				itemPedidoTest.vlBaseItemPedido = itemBase.vlBaseItemTabelaPreco;
	        	condPagto = (CondicaoPagamento)list.items[i];
	        	if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() && inRelatorioMode) {
	        		ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPrecoPrazoPagtoPreco(cbTabelaPreco.getValue(), produto.cdProduto, null, condPagto.cdPrazoPagtoPreco);
	        		if (itemTabelaPreco != null) {
	        			itemPedidoTest.vlBaseItemTabelaPreco = itemTabelaPreco.vlUnitario;
	        			itemPedidoTest.vlBaseItemPedido = itemTabelaPreco.vlUnitario;
	        		}
	        	} else {
	        		itemPedidoTest.vlBaseItemTabelaPreco = itemBase.vlBaseItemTabelaPreco;
	        		itemPedidoTest.vlBaseItemPedido = itemBase.vlBaseItemTabelaPreco;
	        	}
	    		if (LavenderePdaConfig.isMostraPrecosPorCondicaoPagamento()) {
	    			Pedido pedidoExample = new Pedido();
	    			pedidoExample.setCliente(cliente);
	    			pedidoExample.cdCondicaoPagamento = condPagto.cdCondicaoPagamento;
	    			pedidoExample.setCondicaoPagamento(condPagto);
        			ItemPedidoService.getInstance().applyIndiceFinanceiroCondPagto(itemPedidoTest, pedidoExample);
        			if (LavenderePdaConfig.isMostraPrecosPorCondicaoPagamentoPercentual()) { 
        				String[] item = {StringUtil.getStringValue(condPagto.dsCondicaoPagamento), StringUtil.getStringValueToInterface(itemPedidoTest.vlBaseItemPedido), StringUtil.getStringValueToInterface(CondicaoPagamentoService.getInstance().loadVlPctDescAcresCondPagto(pedidoExample))};
        				grid.add(item);
        			} else {
        				if (LavenderePdaConfig.usaIndiceGrupoProdutoTabPrecoCondPagto) {
        					itemPedidoTest.pedido = pedidoExample;
        					itemPedidoTest.cdEmpresa = itemBase.cdEmpresa;
        					itemPedidoTest.cdRepresentante = itemBase.cdRepresentante;
        					itemPedidoTest.cdTabelaPreco = cbTabelaPreco.getValue() == null?itemBase.cdTabelaPreco:cbTabelaPreco.getValue();
        					ItemPedidoService.getInstance().aplicaIndiceFinanceiroGrupoProdTabPreco(itemPedidoTest);
        				}
        				String[] item = {StringUtil.getStringValue(condPagto.dsCondicaoPagamento), StringUtil.getStringValueToInterface(itemPedidoTest.vlBaseItemPedido)};
        				grid.add(item);
        			}
	    		} else if (LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido) {
	        		int qtDias = condPagto.qtDiasMediosPagamento;
	        		if (listValidation.indexOf(qtDias) == -1) {
	        			ItemPedidoService.getInstance().getAndApplyIndiceFinanceiroLinhaProdutoNoItemPedido(itemPedidoTest, ((CondicaoPagamento)list.items[i]).cdCondicaoPagamento, cliente);
	        			String[] item = {StringUtil.getStringValueToInterface(qtDias) + " dias", StringUtil.getStringValueToInterface(itemPedidoTest.vlBaseItemPedido)};
	        			grid.add(item);
	        			listValidation.addElement(qtDias);
	        		}
	        	}
			}
		}
		listValidation = null;
		list = null;
	}

	//@Override
	public void onEvent(Event event) {
	   try {
		super.onEvent(event);
		switch (event.type) {
			case  ControlEvent.PRESSED: {
				if (event.target == btCalc) {
					Calculator calculator = new Calculator();
					calculator.popup();
				} else if (event.target == cbTabelaPreco) {
					itemBase = loadItemPedido();
					grid.removeAllElements();
					carregaGrid();
				}
				break;
			}
			case  GridEditEvent.COLUMN_PRESSED: {
				saveListConfig(((GridEditEvent)event).nuColumn);
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private void saveListConfig(int nuColumn) {
		try {
			ConfigInterno configInterno = new ConfigInterno();
			configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
			configInterno.cdConfigInterno = ConfigInterno.configSortAndOrderColumn;
			configInterno.vlChave = ClassUtil.getSimpleName(this.getClass());
			configInterno.vlConfigInterno = StringUtil.getStringValue(nuColumn) + ConfigInterno.defaultSeparatorInfoValue + "S";
			//--
			BaseDomain domainConfig = configInterno;
			ListContainerConfig.listasConfig.put(((ConfigInterno)domainConfig).vlChave, StringUtil.split(((ConfigInterno)domainConfig).vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			if (ConfigInternoService.getInstance().findByRowKey(domainConfig.getRowKey()) == null) {
				ConfigInternoService.getInstance().insert(domainConfig);
			} else {
				ConfigInternoService.getInstance().update(domainConfig);
			}
		} catch (Throwable e) {
		}
	}


	//@Override
	public void popup() {
		if (itemBase.cdProduto != null) {
			super.popup();
		} else {
			UiUtil.showInfoMessage(MessageUtil.getMessage(FrameworkMessages.MSG_INFO_NENHUM_REGISTRO_SELECIONADO_GRID, Messages.PRODUTO_LABEL_CODIGO));
		}
	}

	protected void onUnpop() {
		super.onUnpop();
		grid.setItems(null);
	}
}
