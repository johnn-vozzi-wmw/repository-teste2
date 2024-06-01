package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondComCliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoComercial;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.service.CondComClienteService;
import br.com.wmw.lavenderepda.business.service.CondicaoComercialService;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeFederalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class RelPrecosPorCondicaoComercialWindow extends WmwWindow {

	private BaseGridEdit grid;
	private ButtonPopup btCalc;
	private ItemPedido itemBase;
	private TabelaPrecoComboBox cbTabelaPreco;
	private CondicaoPagamentoComboBox cbCondPagto;
	private Cliente cliente;
	private Produto produto;
	private LabelContainer lbDsProdutoContainer;
	private ItemGradeComboBox cbItemGrade1;
	private UnidadeFederalComboBox cbUf;
	private LabelName lbItemGrade1;

	public RelPrecosPorCondicaoComercialWindow(Produto produto) throws SQLException {
		this(produto, null);
	}
	
	public RelPrecosPorCondicaoComercialWindow(Produto produto, Cliente cliente) throws SQLException {
		super(Messages.REL_PRECO_CONDCOMERCIAL_LABEL);
		this.cliente = cliente;
		cbTabelaPreco = new TabelaPrecoComboBox();
		cbCondPagto = new CondicaoPagamentoComboBox(Messages.COND_PAGTO);
		cbUf = new UnidadeFederalComboBox();
		this.produto = produto;
		cbItemGrade1 = new ItemGradeComboBox();
		cbItemGrade1.drawBackgroundWhenDisabled = true;
		lbItemGrade1 = new LabelName(" ");
		scrollable = false;
		carregaCombos();
		createItemPedidoBase();
		btCalc = new ButtonPopup(Messages.MENU_UTILITARIO_CALCULADORA);
		btCalc.setBackColor(btFechar.getBackColor());
		lbDsProdutoContainer = new LabelContainer(produto.toString());
		setDefaultRect();
	}

	private void createItemPedidoBase() {
		ItemPedido itemPedido = new ItemPedido();
		itemPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
		itemPedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
		itemPedido.setProduto(produto);
		itemPedido.cdProduto = produto.cdProduto;
		itemPedido.cdUfClientePedido = cbUf.getValue();
		itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		if ((LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4()) && cbItemGrade1.size() > 0) {
			itemPedido.cdItemGrade1 = cbItemGrade1.getValue();
		}
		itemPedido.cdUnidade = ItemTabelaPreco.CDUNIDADE_VALOR_PADRAO;
		itemPedido.cdPrazoPagtoPreco = ItemTabelaPreco.CDPRAZOPAGTOPRECO_VALOR_PADRAO;
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			if (LavenderePdaConfig.usaUnidadeAlternativa && ValueUtil.isNotEmpty(produto.cdUnidade)) {
				itemPedido.cdUnidade = produto.cdUnidade;
    		}
			CondicaoPagamento condicaoPagamento = (CondicaoPagamento) cbCondPagto.getSelectedItem();
			if (condicaoPagamento != null && condicaoPagamento.cdPrazoPagtoPreco > 0) {
				itemPedido.cdPrazoPagtoPreco = condicaoPagamento.cdPrazoPagtoPreco;
			}
		}
		this.itemBase = itemPedido;
	}

	//@Override
	public void initUI() {
	   try {
		super.initUI();
		UiUtil.add(this, lbDsProdutoContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
		if (!LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
			UiUtil.add(this, new LabelName(Messages.TABELA_PRECO), cbTabelaPreco, getLeft(), AFTER + HEIGHT_GAP);
		}
		if (LavenderePdaConfig.usaPrecoPorUf) {
			UiUtil.add(this, new LabelName(Messages.UF), cbUf, getLeft(), AFTER + HEIGHT_GAP);
		}
		   if (LavenderePdaConfig.isConfigGradeProduto() && cbItemGrade1.size() > 0) {
    		UiUtil.add(this, lbItemGrade1, cbItemGrade1, getLeft(), AFTER + HEIGHT_GAP);
    	}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() || !LavenderePdaConfig.ocultaCondPagtoPorCondComercial) {
			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO), cbCondPagto, getLeft(), AFTER + HEIGHT_GAP);
		}
		
		GridColDefinition[] gridColDefiniton = {
			new GridColDefinition(Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL, -75, LEFT),
			new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -25, CENTER),
			new GridColDefinition(LavenderePdaConfig.mostrarPercDescontoMaximo ? Messages.MAX_DESC : FrameworkMessages.CAMPO_ID, -35, CENTER),
			new GridColDefinition(LavenderePdaConfig.aplicaDescontoPromocionalItemTabPreco ? Messages.DESC_PROMO : FrameworkMessages.CAMPO_ID, -35, CENTER)};
		grid = UiUtil.createGridEdit(gridColDefiniton, false);
		UiUtil.add(this, grid, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - footerH);
		carregaGrid();
		addButtonPopup(btCalc);
		addButtonPopup(btFechar);
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private void carregaCombos() throws SQLException {
		cbTabelaPreco.loadTabelaPrecoByProduto(produto);
		cbTabelaPreco.setSelectedIndex(0);
		if (LavenderePdaConfig.usaPrecoPorUf) {
			cbUf.carregaUf();
			cbUf.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() || !LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPedido()) {
			cbCondPagto.loadCondicoesPagamentoCombo();
			cbCondPagto.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			loadComboGradeProduto();
		}
	}
	
	public void carregaGrid() throws SQLException {
		if (ValueUtil.isEmpty(cbTabelaPreco.getValue())) {
			return;
		}
		Vector condicaoComercialList = getCondicaoComercialList();
		if (!ValueUtil.isEmpty(condicaoComercialList)) {
			Pedido pedidoExample = new Pedido();
			CondicaoPagamento condicaoPagamento = CondicaoPagamentoService.getInstance().getCondicaoPagamento(cbCondPagto.getValue());
			if (condicaoPagamento != null && condicaoPagamento.cdEmpresa != null) {
				pedidoExample.setCondicaoPagamento(condicaoPagamento);
			}
			int size = condicaoComercialList.size();
			CondicaoComercial condComercial = null;
			CondComCliente condComercialCliente = null;
			ItemPedido itemPedidoExample = new ItemPedido();
			itemPedidoExample.setPedido(pedidoExample);
			for (int i = 0; i < size; i++) {
				if (LavenderePdaConfig.isApresentaPrecoCondComercialCli()) {
					condComercialCliente = (CondComCliente)condicaoComercialList.items[i];
					pedidoExample.cdCondicaoComercial = condComercialCliente.cdCondicaoComercial;
				} else {
					condComercial = (CondicaoComercial)condicaoComercialList.items[i];
					pedidoExample.cdCondicaoComercial = condComercial.cdCondicaoComercial;
				}
				if (condComercialCliente != null) {
					condComercial = getCondicaoComercial(condComercialCliente);
				}
				pedidoExample.setCondicaoComercial(condComercial);
				pedidoExample.setCliente(cliente);
				itemPedidoExample.cdEmpresa = itemBase.cdEmpresa;
				itemPedidoExample.cdRepresentante = itemBase.cdRepresentante;
				itemPedidoExample.cdProduto = itemBase.cdProduto;
				itemPedidoExample.setProduto(itemBase.getProduto());
				itemPedidoExample.cdTabelaPreco = LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial ? condComercial.cdTabelaPreco : itemBase.cdTabelaPreco;
				itemPedidoExample.cdUfClientePedido = itemBase.cdUfClientePedido;
				itemPedidoExample.cdItemGrade1 = itemBase.cdItemGrade1;
				itemPedidoExample.cdItemGrade2 = itemBase.cdItemGrade2;
				itemPedidoExample.cdItemGrade3 = itemBase.cdItemGrade3;
				itemPedidoExample.cdUnidade = itemBase.cdUnidade;
				itemPedidoExample.cdPrazoPagtoPreco = itemBase.cdPrazoPagtoPreco;
				itemPedidoExample.descPromocional = null;
				PedidoService.getInstance().loadValorBaseItemPedido(pedidoExample, itemPedidoExample);
    			String[] itemGrid = new String[4];
    			itemGrid[0] = StringUtil.getStringValue(condComercial.dsCondicaoComercial);
    			itemGrid[1] = Messages.PRODUTO_LABEL_RS + StringUtil.getStringValueToInterface(itemPedidoExample.vlItemPedido);
				itemGrid[2] = StringUtil.getStringValueToInterface(itemBase.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemBase.getProduto()));
				itemGrid[3] = StringUtil.getStringValueToInterface(itemBase.getItemTabelaPreco().vlPctDescPromocional);
    			grid.add(itemGrid);
			}
		}
	}

	public Vector getCondicaoComercialList() throws SQLException {
		if (LavenderePdaConfig.isApresentaPrecoCondComercialCli()) {
			CondComCliente condComClienteFilter = new CondComCliente();
			condComClienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condComClienteFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondComCliente.class);
			condComClienteFilter.cdCliente = cliente.cdCliente;
			return CondComClienteService.getInstance().findAllByExample(condComClienteFilter);		
		} else {
			CondicaoComercial condicaoComercialFilter = new CondicaoComercial();
			condicaoComercialFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			condicaoComercialFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(CondicaoComercial.class);
			return CondicaoComercialService.getInstance().findAllByExample(condicaoComercialFilter);
		}
	}
	
	public CondicaoComercial getCondicaoComercial(CondComCliente condComercialCliente) throws SQLException {
		return (CondicaoComercial) CondicaoComercialService.getInstance().findByRowKey(condComercialCliente.cdEmpresa+";"+condComercialCliente.cdRepresentante+";"+condComercialCliente.cdCondicaoComercial+";");
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
				} else if (event.target == cbTabelaPreco || event.target == cbItemGrade1 || event.target == cbUf || event.target == cbCondPagto) {
					createItemPedidoBase();
					grid.removeAllElements();
					carregaGrid();
				}
				break;
			}
		}
		} catch (Throwable ee) {ee.printStackTrace();}
	}
	
	private void loadComboGradeProduto() throws SQLException {
    	lbItemGrade1.setValue("-");
		cbItemGrade1.removeAll();
		Vector produtoGradeList = ProdutoGradeService.getInstance().findProdutoGradeList(produto.cdProduto, cbTabelaPreco.getValue());
		int size = produtoGradeList.size();
		if (size > 0) {
			ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[0];
			lbItemGrade1.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtGrade.cdTipoItemGrade1));
			cbItemGrade1.popupTitle = lbItemGrade1.getValue();
		}
		Vector itensGrade1List = new Vector();
		for (int i = 0; i < size; i++) {
			ProdutoGrade produtGrade = (ProdutoGrade)produtoGradeList.items[i];
			ItemGrade itemGrade1 = ItemGradeService.getInstance().getItemGrade(produtGrade.cdTipoItemGrade1, produtGrade.cdItemGrade1);
			if (itemGrade1 != null) {
				if (itensGrade1List.indexOf(itemGrade1) == -1) {
					itensGrade1List.addElement(itemGrade1);
				}
			}
		}
		itensGrade1List.qsort();
		cbItemGrade1.add(itensGrade1List);
		cbItemGrade1.setSelectedIndex(0);
	}
}
