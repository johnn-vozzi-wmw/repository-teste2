package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VectorUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoComplemento;
import br.com.wmw.lavenderepda.business.domain.ProdutoIndustria;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaGrupo;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.GiroProdutoService;
import br.com.wmw.lavenderepda.business.service.GrupoCliPermProdService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoComplementoService;
import br.com.wmw.lavenderepda.business.service.ProdutoIndustriaService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.RestricaoService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaGrupoService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaProdService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.MultiplasSugestoesProdutosComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.SugestaoVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Convert;
import totalcross.ui.Container;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class ListMultiplasSugestoesProdutosWindow extends LavendereWmwListWindow {

	private static final String EMPTY = ValueUtil.VALOR_NI;
	private final String cdGiroProduto = "1";
	private final String cdProdutoComplemento = "2";
	private final String cdProdutoSugestaoVenda = "3";
	private final String cdProdutoColumn = "cdProduto";

	private LabelName lbTipoSugestao;
	private LabelName lbDescricao;
	private LabelValue lbMsgFechamento;
	public Pedido pedido;
	public Produto produto;
	private CadItemPedidoForm cadItemPedidoForm;
	private CadProdutoMenuForm cadProdutoMenuForm;
	public boolean onFechamentoPedido;

	private ButtonPopup btCancelar;
	private ButtonPopup btFecharPedido;
	private ButtonPopup btBuscarItem;
	private ButtonPopup btListaItem;
	private ButtonPopup btPedido;

	private MultiplasSugestoesProdutosComboBox cbSugestoesProdutos;
	private SugestaoVendaComboBox cbSugestaoVenda;
	private TabelaPrecoComboBox cbTabelaPreco;
	private Vector listGeralGiroProdutosCache;
	private Vector listEspecificaGiroProdutosCache;
	private Vector listGeralProdutosComplementadoCache;
	private Vector listEspecificaProdutosComplementadoCache;
	private Vector produtosSugestaoVendaProdCache;

	private ScrollTabbedContainer tabSugestaoCadastrada;
	private GridListContainer sugestaoGrupoListContainer;
	private Vector sugestaoVendaList;
	private int cdSugestaoVendaPos;
	private int cbSugestaoProdutoUltimoSelecionado;
	private int tabProdutoGrupoUltimoSelecionado;
	private String[] cdsSugestaoVenda;
	public boolean desvinculadoPedido;
	
	private BaseScrollContainer bs;
	public PushButtonGroupBase pbSugestoes;
	public ProdutoIndustria produtoIndustriaSelected;
	private boolean isOpenByMenuInferior;

	private boolean hasSugestoes;
	
	public ListMultiplasSugestoesProdutosWindow(Pedido pedido, boolean onFechamentoPedido) throws SQLException {
		this(pedido, onFechamentoPedido, false, null);
	}
	
	public ListMultiplasSugestoesProdutosWindow(Pedido pedido, boolean onFechamentoPedido, boolean isOpenByMenuInferior, Produto produto) throws SQLException {
		super(Messages.PEDIDO_TITULO_SUGESTAO_MULTIPLOS_PRODUTOS);
		this.pedido = pedido;
		this.desvinculadoPedido = pedido == null;
		this.onFechamentoPedido = onFechamentoPedido;
		this.isOpenByMenuInferior = isOpenByMenuInferior;
		this.produto = produto;
		singleClickOn = true;
		lbTipoSugestao = new LabelName(Messages.PEDIDO_LABEL_SUGESTAO_MULTIPLOS_PRODUTOS);
		lbDescricao = new LabelName(Messages.PEDIDO_LABEL_DESCRICAO_COMBO_MULTIPLOS_PRODUTOS);
		cbSugestoesProdutos = new MultiplasSugestoesProdutosComboBox();
		if (!desvinculadoPedido) {
			cbSugestaoVenda = new SugestaoVendaComboBox(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_SEMQUANTIDADE, true);
		}
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btBuscarItem = new ButtonPopup(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_BUSCAR_ITEM);
		btListaItem = new ButtonPopup(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_LISTA_ITEM);
		btPedido = new ButtonPopup(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_PEDIDO);
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
		lbMsgFechamento = new LabelValue(EMPTY, CENTER);
		lbMsgFechamento.setText(Convert.insertLineBreak(width - 6, lbMsgFechamento.fm, Messages.PEDIDO_LABEL_DESCRICAO_MULTIPLOS_PRODUTOS));
		clearCaches();
		sugestaoVendaList = new Vector();
		sortAtributte = onFechamentoPedido ? ProdutoBase.SORT_COLUMN_RELEVANCIA : ProdutoBase.SORT_COLUMN_DSPRODUTO;
		sortAsc = ValueUtil.VALOR_NAO;
		constructorListContainer();
		constructorSugestaoCadastradaListContainer();
		loadComboSugestaoVendasAndSelectTodos();
		constructorTabSugestoes();
		validaComboSugestoesProdutos();
		if (!desvinculadoPedido) {
			setDefaultRect();
			afterLoadScreen();
		}
		populateHasSugestoes();
	}

	private void populateHasSugestoes() throws SQLException {
		if (LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && cbSugestoesProdutos != null && ValueUtil.isNotEmpty(cbSugestoesProdutos.itensSugestaoVenda) && !onFechamentoPedido) {
			ProdutoIndustria produtoIndustriaFilter = new ProdutoIndustria(SessionLavenderePda.cdEmpresa, SessionLavenderePda.getRepresentante().cdRepresentante, null);
			produtoIndustriaFilter.cdSugestaoVendaIn = getListCdSugestaoIndustria(cbSugestoesProdutos.itensSugestaoVenda);
			if (ValueUtil.isNotEmpty(produtoIndustriaFilter.cdSugestaoVendaIn)) {
				hasSugestoes = ProdutoIndustriaService.getInstance().countByExample(produtoIndustriaFilter) > 0;
			}
		}
		hasSugestoes |= listContainer != null && listContainer.size() > 0;
		if (!hasSugestoes) {
			hasSugestoes = ValueUtil.isNotEmpty(getListSugestoes(getDomainFilter(), null));
		}
	}

	private String[] getListCdSugestaoIndustria(Vector itensSugestaoVenda) {
		int size = itensSugestaoVenda.size();
		StringBuilder cdIn = new StringBuilder();
		for (int i = 0; i < size; i++) {
            if (itensSugestaoVenda.items[i] instanceof SugestaoVenda) {
                SugestaoVenda sugestaoVenda = (SugestaoVenda) itensSugestaoVenda.items[i];
                if (!sugestaoVenda.isIndustria()) continue;
                cdIn.append(",");
                cdIn.append(sugestaoVenda.cdSugestaoVenda);
            }
		}
		return StringUtil.split(cdIn.toString(), ',', false, true);
	}

	public ListMultiplasSugestoesProdutosWindow(Produto produto, String cdTabPreco) throws SQLException {
		this(null, false, false, produto);
		this.produto = produto;
		cbTabelaPreco = new TabelaPrecoComboBox();
		cbTabelaPreco.loadTabelaPrecoByProduto(produto);
		cbTabelaPreco.setValue(cdTabPreco);
		if (cbTabelaPreco.getSelectedIndex() == -1) {
			cbTabelaPreco.setSelectedIndex(0);
		}
		sortAtributte = desvinculadoPedido ? ProdutoBase.SORT_COLUMN_RELEVANCIA : ProdutoBase.SORT_COLUMN_DSPRODUTO;
		setDefaultRect();
		afterLoadScreen();
	}
	
	public ListMultiplasSugestoesProdutosWindow(Pedido pedido, Produto produto, TabelaPreco tabelaPreco, boolean isOpenByMenuInferior) throws SQLException {
		this(pedido, false, isOpenByMenuInferior, produto);
		cbTabelaPreco = new TabelaPrecoComboBox();
		cbTabelaPreco.loadTabelaPrecoByProduto(produto);
		cbTabelaPreco.setSelectedItem(tabelaPreco);
		sortAtributte = desvinculadoPedido ? ProdutoBase.SORT_COLUMN_RELEVANCIA : ProdutoBase.SORT_COLUMN_DSPRODUTO;
		setDefaultRect();
		afterLoadScreen();
	}
	
	private void clearCaches() {
		listGeralGiroProdutosCache = new Vector();
		listGeralProdutosComplementadoCache = new Vector();
		produtosSugestaoVendaProdCache = new Vector();
	}

	private void afterLoadScreen() {
		if (!onFechamentoPedido && !desvinculadoPedido && LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas() && !LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
			tabSugestaoCadastrada.setActiveTab(listContainer.size() == 0 ? 1 : 0);
			tabSugestaoCadastrada.requestFocus();
		}
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return SugestaoVendaProdService.getInstance();
	}

	private boolean usaMultiplasSugestoesProdutoFechamentoPedidoGiro() {
		return onFechamentoPedido ? LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoGiro() : LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoGiro();
	}

	private boolean usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento() {
		return (onFechamentoPedido || desvinculadoPedido || isOpenByMenuInferior) && LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento();
	}

	private boolean usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda() {
		return onFechamentoPedido ? LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda() : LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestaoVenda();
	}

	private boolean usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas() {
		return !onFechamentoPedido && LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas() && (isComboAllSelected() || isSugestaoCadastradaSelecionada());
	}

	private void constructorListContainer() {
		int itemCount = 2;
		if (!isSugestaoIndustria()) {
			itemCount += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
			itemCount += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
			itemCount += LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 1 : 0;
			listContainer = new GridListContainer(itemCount, 2);
			if (itemCount > 2) {
				listContainer.setColPosition(2, LEFT);
			}
			if (itemCount > 3) {
				listContainer.setColPosition(3, RIGHT);
			}
			setPropertiesListContainer();
		} else {
			if (!ValueUtil.valueEquals(ProdutoBase.SORT_COLUMN_DSPRODUTO, sortAtributte) && !ValueUtil.valueEquals(cdProdutoColumn, sortAtributte)) {
				sortAtributte = ProdutoBase.SORT_COLUMN_DSPRODUTO;
				sortAsc = ValueUtil.VALOR_NAO;
			}
			if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
				itemCount++;
			}
			listContainer = new GridListContainer(itemCount, 2);
			setPropertiesListContainer();
			configListContainer(ProdutoBase.SORT_COLUMN_DSPRODUTO);
		}
	}
	
	public boolean isSugestaoIndustria() {
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) return false;
		if (cbSugestaoProdutoUltimoSelecionado != 0) {
			cbSugestoesProdutos.setSelectedIndex(cbSugestaoProdutoUltimoSelecionado);
		}
		Object selectedItem = cbSugestoesProdutos.getSelectedItem();
		if (!(selectedItem instanceof SugestaoVenda)) return false;
		SugestaoVenda sugestaoVenda = (SugestaoVenda) selectedItem;
		return sugestaoVenda.isIndustria();
	}

	private void setPropertiesListContainer() {
		listContainer.setUseSortMenu(true);
		List<String[]> itens = new ArrayList<>();
		itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_CODIGO, cdProdutoColumn});
		itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_DESCRICAO, ProdutoBase.SORT_COLUMN_DSPRODUTO});
		if (!isSugestaoIndustria()) {
			if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() && LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
				itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_PRECO, ProdutoBase.SORT_COLUMN_PRECO});
				itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_ESTOQUE, ProdutoBase.SORT_COLUMN_ESTOQUE});
			} else if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
				itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_PRECO, ProdutoBase.SORT_COLUMN_PRECO});
			} else if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
				itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_ESTOQUE, ProdutoBase.SORT_COLUMN_ESTOQUE});
			}
			if (onFechamentoPedido || desvinculadoPedido) {
				itens.add(new String[] { Messages.MULTIPLASSUGESTOES_LABEL_RELEVANCIA, ProdutoBase.SORT_COLUMN_RELEVANCIA });
			}
		}
		int size = itens.size();
		String[][] cols = new String[size][];
		for (int i = 0; i < size; i++) {
			cols[i] = itens.get(i);
		}
		listContainer.setColsSort(cols);
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
	}

	private void constructorTabSugestoes() {
		tabSugestaoCadastrada = new ScrollTabbedContainer(new String[] { Messages.MULTIPLASSUGESTOES_LABEL_PRODUTO, Messages.MULTIPLASSUGESTOES_LABEL_GRUPO });
	}

	private void constructorSugestaoCadastradaListContainer() {
		sugestaoGrupoListContainer = new GridListContainer(2, 2);
		sugestaoGrupoListContainer.setColsSort(new String[][] { { Messages.MULTIPLASSUGESTOES_LABEL_DESCRICAO, SugestaoVendaGrupo.SORT_DSGRUPO } });
		sugestaoGrupoListContainer.atributteSortSelected = SugestaoVendaGrupo.SORT_DSGRUPO;
		sugestaoGrupoListContainer.sortAsc = ValueUtil.VALOR_SIM;
	}

	public void loadComboSugestaoVendasAndSelectTodos() throws SQLException {
		if (!onFechamentoPedido) {
			if ((LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas() || LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) && !desvinculadoPedido && ValueUtil.isNotEmpty(pedido.cdEmpresa) && ValueUtil.isNotEmpty(pedido.getCliente().cdRamoAtividade)) {
				sugestaoVendaList = SugestaoVendaService.getInstance().findSugestaoVendaVigenteSemFechamento(pedido.cdEmpresa, pedido.getCliente());
			}
			cbSugestoesProdutos.load(sugestaoVendaList);
			// --
			int size = sugestaoVendaList.size();
			cdsSugestaoVenda = new String[size];
			for (int i = 0; i < size; i++) {
				SugestaoVenda sugestaoVenda = (SugestaoVenda) sugestaoVendaList.items[i];
				cdsSugestaoVenda[i] = sugestaoVenda.cdSugestaoVenda;
			}
		} else {
			cbSugestoesProdutos.load(desvinculadoPedido);
		}
		cbSugestoesProdutos.setSelectedIndex(0);
		if (!desvinculadoPedido && !isOpenByMenuInferior) {
			cbSugestaoVenda.removeAll();
			cbSugestaoVenda.add(SugestaoVendaService.getInstance().findAllSugestoesVigentesNaoObrigatoriaSemQtdFechamentoPedido(pedido.getCliente(), pedido.cdEmpresa));
			cbSugestaoVenda.setSelectedIndex(0);
		} else {
			cbSugestoesProdutos.load(desvinculadoPedido);
			cbSugestoesProdutos.setSelectedIndex(0);
		}
	}

	protected void validaComboSugestoesProdutos() throws SQLException {
		if (cbSugestoesProdutos.size() == 2) {
			cbSugestoesProdutos.setSelectedIndex(1);
			cbSugestoesProdutos.setEnabled(false);
			if (ValueUtil.valueEquals(cbSugestoesProdutos.getValue(), Messages.PRODUTO_PARAVOCE_SUGESTAO_LABEL)) {
				cbSugestoesProdutosClick();
			}
		}
	}

	protected BaseDomain getDomainFilter() {
		return new Produto();
	}

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		String cbSugestaoAtual = cbSugestoesProdutos.getValue();
		return getListSugestoes(domain, cbSugestaoAtual);
	}

	private Vector getListSugestoes(BaseDomain domain, String cbSugestaoAtual) throws SQLException {
		Vector domainList;
		Vector sugestaoGiroProduto = new Vector();
		Vector sugestaoProdutoComplementado = new Vector();
		Vector sugestaoParaVoce = new Vector();
		Vector sugestaoCadastrada = new Vector();
		Produto.sortAttr = ProdutoBase.SORT_COLUMN_RELEVANCIA;
		boolean comboAllSelected = isComboAllSelected() || cbSugestaoAtual == null;
		if (usaMultiplasSugestoesProdutoFechamentoPedidoGiro() && !desvinculadoPedido) {
			if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && comboAllSelected) {
				sugestaoGiroProduto = getListaGeralSugestoesByGiroProduto(sugestaoGiroProduto);
			}
			if (Messages.PRODUTO_GIROPRODUTO_SUGESTAO_LABEL.equals(cbSugestaoAtual)) {
				sugestaoGiroProduto = getListaEspecificaSugestoesByGiroProduto(sugestaoGiroProduto);
			}
		}
		if (usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento()) {
			if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && comboAllSelected) {
				sugestaoProdutoComplementado = getListaGeralSugestoesByProdutoComplementar(sugestaoGiroProduto, sugestaoProdutoComplementado);
			}
			if (Messages.PRODUTO_PRODUTOCOMPLEMENTADO_SUGESTAO_LABEL.equals(cbSugestaoAtual)) {
				sugestaoProdutoComplementado = getListaEspecificaSugestoesByProdutoComplementar(sugestaoGiroProduto, sugestaoProdutoComplementado);
			}
		}
		if (usaMultiplasSugestoesProdutoFechamentoPedidoSugestaoVenda() && !desvinculadoPedido && (comboAllSelected || Messages.PRODUTO_PARAVOCE_SUGESTAO_LABEL.equals(cbSugestaoAtual))) {
			sugestaoParaVoce = getSugestoesBySugestaoVenda(sugestaoGiroProduto, sugestaoProdutoComplementado, sugestaoParaVoce);
		}
		if ((LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() || usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas()) && !desvinculadoPedido && !onFechamentoPedido) {
			sugestaoCadastrada = getSugestaoCadastrada(sugestaoGiroProduto, sugestaoParaVoce);
		}
		// --
		SortUtil.qsortInt(sugestaoCadastrada.items, 0, sugestaoCadastrada.size()-1, false);
		domainList = VectorUtil.concatVectors(VectorUtil.concatVectors(sugestaoParaVoce, sugestaoCadastrada), VectorUtil.concatVectors(sugestaoGiroProduto, sugestaoProdutoComplementado));
		SortUtil.qsortInt(domainList.items, 0, domainList.size()-1, false);
		if (pedido != null) {
			if (LavenderePdaConfig.usaRestricaoVendaClienteProduto) {
				domainList = RestricaoService.getInstance().filtraProdutosPorRestricao(domainList, pedido.getCliente(), pedido.nuPedido, 1);
			}
			if (!isSugestaoIndustria()) {
				if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
					domainList = ProdutoService.getInstance().findProdutosCliente(pedido.cdCliente, domainList);
				}
				if (LavenderePdaConfig.filtraClientePorProdutoRepresentante) {
					domainList = ProdutoService.getInstance().findClienteProdutos(pedido.cdCliente, domainList);
				}
				if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
					domainList = ProdutoService.getInstance().findProdutosCondPagto(pedido.cdCondicaoPagamento, domainList);
				}
			}
		}
		removeNulos(domainList);
		if (cbSugestaoAtual != null) {
			ordenaLista(domain, domainList);
		}
		return domainList;
	}

	private void ordenaLista(BaseDomain domain, Vector list) throws SQLException {
		Produto.sortAttr = domain.sortAtributte;
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() || LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			preencheColunaEstoqueEPrecoOrdenacao(list);
		}
		if (cdProdutoColumn.equals(domain.sortAtributte) || ProdutoBase.SORT_COLUMN_RELEVANCIA.equals(domain.sortAtributte) || ProdutoBase.SORT_COLUMN_ESTOQUE.equals(domain.sortAtributte)) {
			SortUtil.qsortInt(list.items, 0, list.size() - 1, ValueUtil.VALOR_SIM.equals(domain.sortAsc));
		} else if (ProdutoBase.SORT_COLUMN_DSPRODUTO.equals(domain.sortAtributte)) {
			list.qsort();
			if (ValueUtil.VALOR_NAO.equals(domain.sortAsc)) {
				list.reverse();
			}
		} else if (ProdutoBase.SORT_COLUMN_PRECO.equals(domain.sortAtributte)) {
			SortUtil.qsortDouble(list.items, 0, list.size() - 1, ValueUtil.VALOR_SIM.equals(domain.sortAsc));
		}
		Produto.sortAttr = EMPTY;
	}

	private void removeNulos(Vector list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Object object = list.items[i];
			if (object instanceof Produto) {
				Produto produto = (Produto) object;
				if (ValueUtil.isEmpty(produto.cdProduto)) {
					list.removeElementAt(i);
					size--;
					i--;
				}
			} else if (object instanceof ProdutoIndustria) {
				ProdutoIndustria produtoIndustria = (ProdutoIndustria) object;
				if (ValueUtil.isEmpty(produtoIndustria.cdProduto)) {
					list.removeElementAt(i);
					size--;
					i--;
				}
			}
		}
	}

	private void preencheColunaEstoqueEPrecoOrdenacao(Vector list) throws SQLException {
		if (isSugestaoIndustria()) return;
		int size = list.size();
		for (int i = 0; i < size; i++) {
			Produto produto = (Produto) list.items[i];
			produto.vlPrecoSort = getPrecoProduto(produto);
			if (produto.estoque == null) {
				produto.qtEstoqueProduto = EstoqueService.getInstance().getQtEstoque(produto.cdProduto, pedido != null ? pedido.getCdLocalEstoque() : Estoque.CD_LOCAL_ESTOQUE_PADRAO);
			} else {
				produto.qtEstoqueProduto = produto.estoque.qtEstoque;
			}
		}
	}
	
	protected Vector getDomainListGrupo() throws SQLException {
		if (cbSugestoesProdutos.getSelectedItem() instanceof SugestaoVenda) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda) cbSugestoesProdutos.getSelectedItem();
			if (sugestaoVenda != null && ValueUtil.isNotEmpty(sugestaoVenda.cdSugestaoVenda)) {
				Vector sugestaoVendaGrupoList = SugestaoVendaGrupoService.getInstance().findAllSugestaoVendaGrupoComQtdPendenteNoPedido(sugestaoVenda, pedido);
				ordenaSugestaoGrupo(sugestaoVendaGrupoList);
				return sugestaoVendaGrupoList;
			}
		}
		return new Vector(0);
	}

	private void ordenaSugestaoGrupo(Vector sugestaoVendaGrupoList) {
		SugestaoVendaGrupo.sortAttr = sugestaoGrupoListContainer.atributteSortSelected;
		sugestaoVendaGrupoList.qsort();
		if (ValueUtil.VALOR_NAO.equals(sugestaoGrupoListContainer.sortAsc)) {
			sugestaoVendaGrupoList.reverse();
		}
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
		if (!onFechamentoPedido && domain instanceof SugestaoVendaGrupo) {
			return getItemGrupo(domain);
		}
		int itemCount = 2;
		Vector itens = new Vector(itemCount);
		if (domain instanceof Produto) {
			Produto produto = (Produto) domain;
			itemCount += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
			itemCount += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
			itemCount += LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 1 : 0;
			if (itemCount != 2) {
				itens = new Vector(itemCount);
			}
			if (produto != null) {
				if (!LavenderePdaConfig.ocultaColunaCdProduto) {
					itens.addElement(StringUtil.getStringValue(produto.cdProduto) + " - ");
					itens.addElement(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto));
				} else {
					itens.addElement(StringUtil.getStringValue(produto.dsProduto));
					itens.addElement(EMPTY);
				}
				cdSugestaoVendaPos = 2;
				if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
					itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(produto.dsPrincipioAtivo), (int)(width * 0.6), listContainer.getFontSubItens()));
					cdSugestaoVendaPos++;
				}
				if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
					itens.addElement(Messages.ESTOQUE_NOME_ENTIDADE + " " + EstoqueService.getInstance().getEstoqueToString(produto.qtEstoqueProduto) + " ");
					cdSugestaoVendaPos++;
				}
				if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
					itens.addElement(Messages.PRODUTO_LABEL_PRECO + "  " + Messages.MOEDA + StringUtil.getStringValueToInterface(produto.vlPrecoSort));
					cdSugestaoVendaPos++;
				}
				itens.addElement(StringUtil.getStringValue(produto.cdSugestaoVenda));
			}
		} else if (domain instanceof ProdutoIndustria) {
			ProdutoIndustria produtoIndustria = (ProdutoIndustria) domain;
			itens.addElement(StringUtil.getStringValue(produtoIndustria.cdRefProdIndustria) + " - ");
			itens.addElement(produtoIndustria.dsRefProdIndustria);
			itens.addElement(getQtdeSugeridaColumn(produtoIndustria));
		}
		return (String[]) itens.toObjectArray();
	}
	
	private String getQtdeSugeridaColumn(final ProdutoIndustria produtoIndustria) {
		if (produtoIndustria.vlLitroSugestao == 0) {
			return "";
		}
		return MessageUtil.getMessage(Messages.MULTIPLASSUGESTOES_MSG_QT_SUGERIDA, (int)produtoIndustria.vlLitroSugestao);
	}

	private String[] getItemGrupo(Object domain) throws SQLException {
		SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) domain;
		Vector itens = new Vector(0);
		itens.addElement(sugestaoVendaGrupo.getDsSugestaoVendaGrupo());
		itens.addElement(EMPTY);
		itens.addElement(sugestaoVendaGrupo.cdSugestaoVenda);
		return (String[]) itens.toObjectArray();
	}

	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
	}

	private Produto getSelectedProduto(GridListContainer gridList) throws SQLException {
		String cdSugestaoVenda = gridList.getValueFromContainer(gridList.getSelectedIndex(), cdSugestaoVendaPos);
		Produto produto = (Produto) ProdutoService.getInstance().findByRowKey(gridList.getSelectedId());
		produto = produto == null ? new Produto() : produto;
		produto.cdSugestaoVenda = cdSugestaoVenda;
		if (LavenderePdaConfig.usaColetaInfoAdicionaisEscolhaItemPedido) {
			Produto prod = (Produto)getSelectedDomain2();
			produto.complementar = prod != null ? prod.complementar : false;
		}
		return produto;
	}

	private SugestaoVendaGrupo getSelectedSugestaoVendaGrupo() throws SQLException {
		SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) SugestaoVendaGrupoService.getInstance().findByRowKey(sugestaoGrupoListContainer.getSelectedId());
		if (sugestaoVendaGrupo != null) {
			sugestaoVendaGrupo.qtMixVendida = ValueUtil.getIntegerValue(ValueUtil.removeThousandSeparator(sugestaoGrupoListContainer.getValueFromContainer(sugestaoGrupoListContainer.getSelectedIndex(), 4)));
			sugestaoVendaGrupo.qtVendida = ValueUtil.getIntegerValue(ValueUtil.removeThousandSeparator(sugestaoGrupoListContainer.getValueFromContainer(sugestaoGrupoListContainer.getSelectedIndex(), 5)));
		}
		return sugestaoVendaGrupo;
	}

	protected void onFormStart() {
		if (!desvinculadoPedido) {
			UiUtil.add(this, lbMsgFechamento, LEFT, getNextY());
		}
		
		if (LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
			if (createBtnOptionsByCombo(cbSugestoesProdutos)) {
				UiUtil.add(this, lbTipoSugestao, CENTER, getNextY(), fm.stringWidth(lbTipoSugestao.getValue()));
				pbSugestoes.setFont(UiUtil.defaultFontSmall);
				UiUtil.add(bs, pbSugestoes, LEFT, TOP);
			}
			UiUtil.add(this, bs, getLeft(), AFTER - HEIGHT_GAP, getWFill(), UiUtil.getControlPreferredHeight());
		} else {
			UiUtil.add(this, lbTipoSugestao, cbSugestoesProdutos, getLeft(), getNextY());
		}
		
		if (!desvinculadoPedido) {
			if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
				UiUtil.add(this, lbDescricao, cbSugestaoVenda, getLeft(), getNextY());
			}
		} else {
			UiUtil.add(this, new LabelName(Messages.TABELAPRECO_NOME_TABPRECO), cbTabelaPreco, getLeft(), getNextY());
		}
		
		if (!onFechamentoPedido && !desvinculadoPedido && LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas()) {
			if (LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
				UiUtil.add(this, listContainer, LEFT, bs.getY2() + HEIGHT_GAP_BIG, FILL, FILL);
			} else {
				UiUtil.add(this, tabSugestaoCadastrada, LEFT, cbSugestoesProdutos.getY2() + HEIGHT_GAP_BIG, FILL, FILL);
				UiUtil.add(tabSugestaoCadastrada.getContainer(0), listContainer, LEFT, getTop(), FILL, FILL);
				UiUtil.add(tabSugestaoCadastrada.getContainer(1), sugestaoGrupoListContainer, LEFT, getTop(), FILL, FILL);
			}
			setModoTabEnabled(true);
		} else {
			int y2 = desvinculadoPedido ? cbTabelaPreco.getY2() : LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() ? bs.getY2() : cbSugestoesProdutos.getY2();
			UiUtil.add(this, listContainer, LEFT, y2 + HEIGHT_GAP_BIG, FILL, FILL);
			setModoTabEnabled(false);
		}
		if (desvinculadoPedido || isOpenByMenuInferior) {
			addButtonPopup(btCancelar);
		} else if (onFechamentoPedido) {
			addButtonPopup(btFecharPedido);
			addButtonPopup(btCancelar);
		} else {
			addButtonPopup(btBuscarItem);
			addButtonPopup(btListaItem);
			addButtonPopup(btPedido);
		}

		if (isSugestaoIndustria()) {
			ajustaComponents();
		}

	}

	private boolean createBtnOptionsByCombo(MultiplasSugestoesProdutosComboBox cbSugestoesProdutos) {
		try {
			Vector sugestoes = cbSugestoesProdutos.itensSugestaoVenda;
			int qtBotoes = sugestoes.size();
			bs = new BaseScrollContainer(true, false);
			if (qtBotoes == 0) return false;
			String[] btnValues = new String[qtBotoes];
			for (int i = 0; i < qtBotoes; i++) {
				Object object = sugestoes.items[i];
				if (object instanceof SugestaoVenda) {
					SugestaoVenda sugestaoVenda = (SugestaoVenda) object;
					btnValues[i] = sugestaoVenda.dsSugestaoVenda;
				} else {
					btnValues[i] = (String) object;
				}
			}
			if (cbSugestoesProdutos.getSelectedIndex() == 0) {
				cbSugestoesProdutos.setSelectedIndex(1);
			}
			pbSugestoes = new PushButtonGroupBase(btnValues, true, cbSugestoesProdutos.getSelectedIndex() - 1, -1, 10, 1, false, PushButtonGroup.BUTTON);
			cbSugestaoProdutoUltimoSelecionado = cbSugestoesProdutos.getSelectedIndex();
			return true;
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			return false;
		}
	}

	private void ajustaComponents() {
		int yTop = getTop() + HEIGHT_GAP;
		lbMsgFechamento.setText(Convert.insertLineBreak(width - 6, lbMsgFechamento.fm, Messages.PEDIDO_LABEL_DESCRICAO_MULTIPLOS_PRODUTOS));
		lbMsgFechamento.setRect(CENTER, yTop, width, PREFERRED);
		remove(listContainer);
		remove(tabSugestaoCadastrada);
		constructorListContainer();
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && cbSugestoesProdutos != null && Messages.PRODUTO_PARAVOCE_SUGESTAO_LABEL.equals(cbSugestoesProdutos.getValue()) && !desvinculadoPedido) {
			UiUtil.add(this, listContainer, LEFT, cbSugestaoVenda.getY2() + HEIGHT_GAP, FILL, FILL);
			setModoTabEnabled(false);
		} else if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas()) {
			remove(sugestaoGrupoListContainer);
			constructorTabSugestoes();
			UiUtil.add(this, tabSugestaoCadastrada, LEFT, cbSugestoesProdutos.getY2() + HEIGHT_GAP_BIG, FILL, FILL);
			UiUtil.add(tabSugestaoCadastrada.getContainer(0), listContainer, LEFT, getTop(), FILL, FILL);
			UiUtil.add(tabSugestaoCadastrada.getContainer(1), sugestaoGrupoListContainer, LEFT, getTop(), FILL, FILL);
			setModoTabEnabled(true);
		} else {
			int yPos = desvinculadoPedido ? cbTabelaPreco.getY2() : LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && bs != null ? bs.getY2() : cbSugestoesProdutos.getY2();
			UiUtil.add(this, listContainer, LEFT, yPos + HEIGHT_GAP_BIG, FILL, FILL);
			setModoTabEnabled(false);
		}
	}

	private void setModoTabEnabled(boolean enabled) {
		lbDescricao.setVisible(!enabled);
		if (!desvinculadoPedido) {
			cbSugestaoVenda.setVisible(!enabled);
		}
		tabSugestaoCadastrada.setVisible(enabled);
	}

	// @Override
	protected void addBtFechar() {}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCancelar) {
					fecharWindow();
				} else if (event.target == cbSugestoesProdutos) {
					cbSugestoesProdutosClick();
				} else if (event.target == cbSugestaoVenda) {
					super.list();
				} else if (event.target == btFecharPedido) {
					fecharWindow();
					if (MainLavenderePda.getInstance().getActualForm() instanceof CadPedidoForm) {
						CadPedidoForm cadPedidoForm = (CadPedidoForm) MainLavenderePda.getInstance().getActualForm();
						cadPedidoForm.getPedido().ignoraValidacaoMultiplosSugestaoProdutos = true;
						cadPedidoForm.showMessageConfirmClosePedido = false;
						cadPedidoForm.fecharPedido(!onFechamentoPedido);
					}
				} else if (event.target == btPedido) {
					fecharWindow();
					cadItemPedidoForm.voltarClick();
				} else if (event.target == btListaItem) {
					closePopupSuper();
					cadItemPedidoForm.btListaItensClick();
				} else if (event.target == btBuscarItem) {
					closePopupSuper();
				} else if (event.target == cbTabelaPreco) {
					super.list();
				}
				if (event.target == pbSugestoes) {
					int indexSelected = pbSugestoes.getSelectedIndex();
					cbSugestaoProdutoUltimoSelecionado = indexSelected + 1;
					cbSugestoesProdutos.setSelectedIndex(cbSugestaoProdutoUltimoSelecionado);
					cbSugestoesProdutosClick();
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && singleClickOn && (sugestaoGrupoListContainer != null && sugestaoGrupoListContainer.isEventoClickUnicoDisparado())) {
					sugestaoGrupoListContainer.setEventoClickUnicoDisparado(false);
					detalhesSugestaoCadastroGrupoClick();
				}
				break;
			}
			case ControlEvent.WINDOW_CLOSED: {
				if ((sugestaoGrupoListContainer != null) && (event.target == sugestaoGrupoListContainer.popupMenuOrdenacao)) {
					if (sugestaoGrupoListContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
						sugestaoGrupoListContainer.reloadSortSettings();
						listSugestaoVendaGrupoOnly();
					}
				}
				break;
			}
		}
	}

	private void closePopupSuper() throws SQLException {
		produtoIndustriaSelected = null;
		super.fecharWindow();
	}

	private void cbSugestoesProdutosClick() throws SQLException {
		cbSugestaoProdutoUltimoSelecionado = cbSugestoesProdutos.getSelectedIndex();
		if (!desvinculadoPedido) {
			cbSugestaoVenda.setSelectedIndex(0);
		}
		ajustaComponents();
		list();
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas()) {
			tabSugestaoCadastrada.setActiveTab(listContainer.size() == 0 && sugestaoGrupoListContainer.size() > 0 ? 1 : 0);
			tabSugestaoCadastrada.requestFocus();
		}
	}

	public void acaoAntesReabrirPopupAposEdicao() throws SQLException {
		cbSugestoesProdutos.setSelectedIndex(cbSugestaoProdutoUltimoSelecionado);
		ajustaComponents();
		list();
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria() && usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas()) {
			tabSugestaoCadastrada.setActiveTab(tabProdutoGrupoUltimoSelecionado);
			tabSugestaoCadastrada.requestFocus();
		}
	}

	@Override
	public void list() throws SQLException {
		super.list();
		if (!LavenderePdaConfig.usaMultiplasSugestoesProdutoIndustria()) {
			listSugestaoVendaGrupoOnly();
		}
	}

	private void listSugestaoVendaGrupoOnly() throws SQLException {
		if (usaMultiplasSugestoesProdutoInicioPedidoSugestoesCadastradas() && !desvinculadoPedido) {
			if (isComboAllSelected()) {
				if (ValueUtil.isNotEmpty(cdsSugestaoVenda)) {
					SugestaoVendaGrupo sugestaoVendaGrupo = new SugestaoVendaGrupo();
					sugestaoVendaGrupo.cdEmpresa = pedido.cdEmpresa;
					sugestaoVendaGrupo.cdsSugestao = cdsSugestaoVenda;
					Vector sugestaoVendaGrupoList = SugestaoVendaGrupoService.getInstance().findAllByExample(sugestaoVendaGrupo);
					sugestaoVendaGrupoList = getSugestaoVendaGrupoSemDuplicacao(sugestaoVendaGrupoList);
					ordenaSugestaoGrupo(sugestaoVendaGrupoList);
					listSugestaoVendaGrupoOnly(sugestaoVendaGrupoList);
				}
			} else if (isSugestaoCadastradaSelecionada()) {
				listSugestaoVendaGrupoOnly(getDomainListGrupo());
			}
		}
	}

	private Vector getSugestaoVendaGrupoSemDuplicacao(Vector sugestaoVendaGrupoList) {
		HashMap<String, SugestaoVendaGrupo> sugestaoVendaGrupoHash = new HashMap<String, SugestaoVendaGrupo>();
		Vector result = new Vector();
		StringBuffer sb = new StringBuffer(16);
		int size = sugestaoVendaGrupoList.size();
		for (int i = 0; i < size; i++) {
			sb.setLength(0);
			SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) sugestaoVendaGrupoList.items[i];
			sugestaoVendaGrupoHash.put(sb.append(sugestaoVendaGrupo.cdGrupoProduto1).append(";").append(sugestaoVendaGrupo.cdGrupoProduto2).append(";").append(sugestaoVendaGrupo.cdGrupoProduto3).toString(), sugestaoVendaGrupo);
		}
		for (SugestaoVendaGrupo sugestaoVendaGrupo : sugestaoVendaGrupoHash.values()) {
			result.addElement(sugestaoVendaGrupo);
		}
		return result;
	}

	private boolean isSugestaoCadastradaSelecionada() {
		return !Messages.PRODUTO_GIROPRODUTO_SUGESTAO_LABEL.equals(cbSugestoesProdutos.getValue()) && !Messages.PRODUTO_PARAVOCE_SUGESTAO_LABEL.equals(cbSugestoesProdutos.getValue()) && !isComboAllSelected();
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

	private void detalhesSugestaoCadastroGrupoClick() throws SQLException {
		SugestaoVendaGrupo sugestaoVendaGrupo = getSelectedSugestaoVendaGrupo();
		if (sugestaoVendaGrupo != null) {
			SugestaoVenda sugestaoVenda = new SugestaoVenda();
			sugestaoVenda.cdEmpresa = sugestaoVendaGrupo.cdEmpresa;
			sugestaoVenda.cdSugestaoVenda = sugestaoVendaGrupo.cdSugestaoVenda;
			sugestaoVenda = (SugestaoVenda) SugestaoVendaService.getInstance().findByRowKey(sugestaoVenda.getRowKey());
			ListProdutoSugestaoGrupoComQtdeWindow listProdutoSugestaoGrupoComQtdeWindow = new ListProdutoSugestaoGrupoComQtdeWindow(pedido, sugestaoVendaGrupo, sugestaoVenda, true);
			listProdutoSugestaoGrupoComQtdeWindow.selectedProduto = null;
			listProdutoSugestaoGrupoComQtdeWindow.popup();
			if (listProdutoSugestaoGrupoComQtdeWindow.selectedProduto != null) {
				Produto produto = listProdutoSugestaoGrupoComQtdeWindow.selectedProduto;
				cbSugestaoProdutoUltimoSelecionado = cbSugestoesProdutos.getSelectedIndex();
				tabProdutoGrupoUltimoSelecionado = 1;
				editProduto(produto);
			}
		}
	}

	public void detalhesClick() throws SQLException {
		if (isSugestaoIndustria()) {
			if (desvinculadoPedido) return;
			ProdutoIndustria produtoIndustria = (ProdutoIndustria) ProdutoIndustriaService.getInstance().findByRowKey(getSelectedRowKey());
			produtoIndustriaSelected = produtoIndustria;
			unpop();
		} else {
			produtoIndustriaSelected = null;
			Produto produto = getSelectedProduto(listContainer);
			cbSugestaoProdutoUltimoSelecionado = cbSugestoesProdutos.getSelectedIndex();
			if (!desvinculadoPedido && LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(pedido, produto)) {
				UiUtil.showWarnMessage(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
				return;
			}
			tabProdutoGrupoUltimoSelecionado = 0;
			if (desvinculadoPedido || isOpenByMenuInferior) {
				btInfoProdutosClick(produto);
			} else {
				editProduto(produto);
			}
		}
	}

	private void editProduto(Produto produto) throws SQLException {
		if (ValueUtil.isEmpty(produto.dsProduto)) {
			UiUtil.showErrorMessage(Messages.SUGESTAO_MSG_PRODUTO_NAO_ENCONTRADO);
			return;
		}
		cadItemPedidoForm.produtoSelecionado = produto;
		cadItemPedidoForm.gridClickAndRepaintScreen();
		unpop();
		cadItemPedidoForm.fromWindowMultiplaSugestaoProduto = onFechamentoPedido;
		cadItemPedidoForm.fromWindowMultiplaSugestaoNovoItemProduto = !onFechamentoPedido;
		cadItemPedidoForm.setFocusInQtde();
		cadItemPedidoForm.getItemPedido().cdSugestaoVenda = produto.cdSugestaoVenda;
	}

	protected void fecharWindow() throws SQLException {
		clearCaches();
		closePopupSuper();
		if (desvinculadoPedido) {
			if (cadProdutoMenuForm != null) {
				cadProdutoMenuForm.invalidateListMultProdutos();
			} else if (cadItemPedidoForm != null) {
				cadItemPedidoForm.invalidateListMultSugestoesProd();
			}
		}
		if (onFechamentoPedido && cadItemPedidoForm != null) {
			cadItemPedidoForm.voltarClick();
		}
	}

	public boolean hasSugestoes() {
		return (hasSugestoes || (!onFechamentoPedido && sugestaoGrupoListContainer != null && sugestaoGrupoListContainer.size() > 0));
	}

	private boolean isComboAllSelected() {
		return cbSugestoesProdutos.getSelectedIndex() == 0;
	}

	@Override
	protected void onUnpop() {
		if (!desvinculadoPedido) {
			cbSugestoesProdutos.setSelectedIndex(0);
			cbSugestaoVenda.setSelectedIndex(0);
			ajustaComponents();
		}
	}

	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		if (!onFechamentoPedido && domain instanceof SugestaoVendaGrupo) return;
		if (domain instanceof ProdutoBase) {
			ProdutoBase produtoBase = (ProdutoBase) domain;
			// PRODUTOS SEM ESTOQUE EM VERMELHO
			if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !produtoBase.isIgnoraValidacao()) {
				if (produtoBase.estoque == null) {
					produtoBase.estoque = new Estoque();
					produtoBase.estoque.qtEstoque = EstoqueService.getInstance().getQtEstoque(produtoBase.cdProduto, pedido != null ? pedido.getCdLocalEstoque() : Estoque.CD_LOCAL_ESTOQUE_PADRAO);
				}
				if (produtoBase.estoque.qtEstoque <= 0) {
					c.setBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK);
				}
			}
			if (!onFechamentoPedido) {
				// MOSTRA PRODUTO PROMOCIONAL DESTACADO
				String cdTabelaPreco = desvinculadoPedido ? cbTabelaPreco.getValue() : pedido.cdTabelaPreco;
				if (LavenderePdaConfig.isMostraProdutoPromocionalDestacadoTelaItemPedido() && (!LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional || !ProdutoService.getInstance().isProdutoHaveDescPromocional(produtoBase, cdTabelaPreco))) {
					if (destacaProdutoPromocionalItemTabPreco(c, produtoBase)) {
						c.setBackColor(LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK);
					}
				}
				// MOSTRA PRODUTO COM DESCONTO PROMOCIONAL DESTACADO
				if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido()) {
					if (destacaProdutoDescPromocional(c, produtoBase)) {
						c.setBackColor(LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK);
					}
				}
			}
		}
	}

	private Vector getListaGeralSugestoesByGiroProduto(Vector sugestaoGiroProdutoList) throws SQLException {
		populateListaGeralGiroProdutoCache(sugestaoGiroProdutoList);

		int qtItensLista = LavenderePdaConfig.getQtLimiteItensSugestaoListas(isComboAllSelected(), cdGiroProduto, onFechamentoPedido);
		if (qtItensLista < sugestaoGiroProdutoList.size() && qtItensLista >= 0) {
			sugestaoGiroProdutoList.setSize(qtItensLista);
		}
		return sugestaoGiroProdutoList;
	}

	private void populateListaGeralGiroProdutoCache(Vector sugestaoGiroProdutoList) throws SQLException {
		if (listGeralGiroProdutosCache.size() == 0) {
			Vector giroProdutoList = GiroProdutoService.getInstance().findAllProdutosGiroProdutoByClienteAndPedido(pedido.getCliente(), pedido, true);
			for (int i = 0; i < giroProdutoList.size(); i++) {
				GiroProduto giroProduto = (GiroProduto) giroProdutoList.items[i];
				if (giroProduto != null) {
					Produto produto = ProdutoService.getInstance().getProduto(giroProduto.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, giroProduto.cdProduto);
					if (!ValueUtil.valueEquals(produto, new Produto())) {
						produto.nuRelevancia = giroProduto.nuRelevancia;
						produto.nuRelevanciaSort = giroProduto.nuRelevancia;
						listGeralGiroProdutosCache.addElement(produto);
					}
				}
			}
			if (listGeralGiroProdutosCache.size() > 1) {
				SortUtil.qsortInt(listGeralGiroProdutosCache.items, 0, listGeralGiroProdutosCache.size() - 1, false);
			}
			int qtItensListaGeral = LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, cdGiroProduto, onFechamentoPedido);
			int qtItensListaEspecifica = LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, cdGiroProduto, onFechamentoPedido);
			int qtAtual = qtItensListaGeral > qtItensListaEspecifica ? qtItensListaGeral : qtItensListaEspecifica;
			if (listGeralGiroProdutosCache.size() > qtAtual && qtItensListaGeral >= 0) {
				listGeralGiroProdutosCache.setSize(qtAtual);
			}
		}
		for (int i = 0; i < listGeralGiroProdutosCache.size(); i++) {
			Produto produto = (Produto) listGeralGiroProdutosCache.items[i];
			if (isProdutoValido(produto)) {
				addProdutoMaiorRelevancia(produto, sugestaoGiroProdutoList, new Vector());
			}
		}
	}

	private Vector getListaEspecificaSugestoesByGiroProduto(Vector sugestaoGiroProdutoList) throws SQLException {
		populateListaGeralGiroProdutoCache(sugestaoGiroProdutoList);

		if (listEspecificaGiroProdutosCache == null || listEspecificaGiroProdutosCache.size() == 0) {
			int qtItensListaEspecifica = LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, cdGiroProduto, onFechamentoPedido);
			int listGeralGiroProdutosSize = listGeralGiroProdutosCache.size();
			qtItensListaEspecifica = (qtItensListaEspecifica >= 0 && qtItensListaEspecifica <= listGeralGiroProdutosSize) ? qtItensListaEspecifica : listGeralGiroProdutosSize;
			listEspecificaGiroProdutosCache = new Vector();
			if (ValueUtil.isNotEmpty(listGeralGiroProdutosCache)) {
				for (int i = 0; i < qtItensListaEspecifica; i++) {
					listEspecificaGiroProdutosCache.addElement(listGeralGiroProdutosCache.items[i]);
				}
			}
		}

		sugestaoGiroProdutoList = new Vector(listEspecificaGiroProdutosCache.size());
		for (int i = 0; i < listEspecificaGiroProdutosCache.size(); i++) {
			Produto produto = (Produto) listEspecificaGiroProdutosCache.items[i];
			if (isProdutoValido(produto)) {
				sugestaoGiroProdutoList.addElement(produto);
			}
		}

		return sugestaoGiroProdutoList;
	}

	private Vector getListaGeralSugestoesByProdutoComplementar(Vector sugestaoGiroProduto, Vector sugestaoProdutoComplementado) throws SQLException {
		populateListaGeralSugestoesByProdutoComplementar(sugestaoGiroProduto, sugestaoProdutoComplementado);

		int qtItensLista = LavenderePdaConfig.getQtLimiteItensSugestaoListas(isComboAllSelected(), cdProdutoComplemento, onFechamentoPedido);
		if (qtItensLista < sugestaoProdutoComplementado.size() && qtItensLista >= 0) {
			sugestaoProdutoComplementado.setSize(qtItensLista);
		}
		return sugestaoProdutoComplementado;
	}

	private void populateListaGeralSugestoesByProdutoComplementar(Vector sugestaoGiroProduto, Vector sugestaoProdutoComplementado) throws SQLException {
		Vector produtoComplementarlist = new Vector();
		if (listGeralProdutosComplementadoCache.size() == 0) {
			if (desvinculadoPedido || isOpenByMenuInferior) {
				produtoComplementarlist = ProdutoComplementoService.getInstance().findAllProdutoComplementarByProd(produto);
			} else {
				produtoComplementarlist = ProdutoComplementoService.getInstance().findAllProdutoComplementarByProdList(pedido.itemPedidoList);
			}
			if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && !desvinculadoPedido && ValueUtil.isNotEmpty(pedido.getCliente().cdGrupoPermProd)) {
				produtoComplementarlist = GrupoCliPermProdService.getInstance().restringeProdutoComplementoByGrupoCliPermProd(produtoComplementarlist);
			}
			for (int i = 0; i < produtoComplementarlist.size(); i++) {
				ProdutoComplemento produtoComplementar = (ProdutoComplemento) produtoComplementarlist.items[i];
				if (produtoComplementar != null) {
					Produto produto = ProdutoService.getInstance().getProduto(produtoComplementar.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, produtoComplementar.cdProdutoComplemento);
					if (!ValueUtil.valueEquals(produto, new Produto())) {
						produto.nuRelevanciaSort = produto.nuRelevancia;
						listGeralProdutosComplementadoCache.addElement(produto);
					}
				}
			}
			if (listGeralProdutosComplementadoCache.size() > 1) {
				SortUtil.qsortInt(listGeralProdutosComplementadoCache.items, 0, listGeralProdutosComplementadoCache.size() - 1, false);
			}
		}
		for (int i = 0; i < listGeralProdutosComplementadoCache.size(); i++) {
			Produto produto = (Produto) listGeralProdutosComplementadoCache.items[i];
			if (isProdutoValido(produto)) {
				produto.complementar = true;
				addProdutoMaiorRelevancia(produto, sugestaoProdutoComplementado, sugestaoGiroProduto);
			}
		}
		int qtItensListaGeral = LavenderePdaConfig.getQtLimiteItensSugestaoListas(true, cdProdutoComplemento, onFechamentoPedido);
		int qtItensListaEspecifica = LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, cdProdutoComplemento, onFechamentoPedido);
		int qtAtual = qtItensListaGeral > qtItensListaEspecifica ? qtItensListaGeral : qtItensListaEspecifica;
		if (sugestaoProdutoComplementado.size() > qtAtual && qtItensListaGeral >= 0 && qtItensListaEspecifica >= 0) {
			sugestaoProdutoComplementado.setSize(qtAtual);
		}
	}

	private Vector getListaEspecificaSugestoesByProdutoComplementar(Vector sugestaoGiroProduto, Vector sugestaoProdutoComplementado) throws SQLException {
		int qtItensListaEspecifica = LavenderePdaConfig.getQtLimiteItensSugestaoListas(false, cdProdutoComplemento, onFechamentoPedido);
		populateListaGeralSugestoesByProdutoComplementar(sugestaoGiroProduto, sugestaoProdutoComplementado);

		if (listEspecificaProdutosComplementadoCache == null || listEspecificaProdutosComplementadoCache.size() == 0) {
			listEspecificaProdutosComplementadoCache = new Vector();
			for (int i = 0; i < sugestaoProdutoComplementado.size(); i++) {
				listEspecificaProdutosComplementadoCache.addElement(sugestaoProdutoComplementado.items[i]);
			}
		}

		int qtListaGeral = listEspecificaProdutosComplementadoCache.size();
		int qtTotal = qtListaGeral < qtItensListaEspecifica ? qtListaGeral : qtItensListaEspecifica;
		qtTotal = (qtTotal < 0) ? qtListaGeral : qtTotal;
		sugestaoProdutoComplementado = new Vector(qtTotal);

		for (int i = 0; i < qtTotal; i++) {
			Produto produto = (Produto) listEspecificaProdutosComplementadoCache.items[i];
			if (produto != null && isProdutoValido(produto)) {
				produto.complementar = true;
				sugestaoProdutoComplementado.addElement(produto);
			}
		}

		return sugestaoProdutoComplementado;
	}

	private Vector getSugestoesBySugestaoVenda(Vector sugestaoGiroProduto, Vector sugestaoProdutoComplementado, Vector sugestaoParaVoce) throws SQLException {
		Vector listProdutoPorSugestaoVenda = new Vector();
		Vector sugestaoVendaProdList = new Vector();
		if (!desvinculadoPedido) {
			if (produtosSugestaoVendaProdCache.size() == 0) {
				Vector sugestaoVendaList = new Vector(0);
				if (cbSugestaoVenda.getSelectedIndex() == 0) {
					sugestaoVendaList = SugestaoVendaService.getInstance().findAllSugestoesVigentesNaoObrigatoriaSemQtdFechamentoPedido(pedido.getCliente(), pedido.cdEmpresa);
					sugestaoVendaProdList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdBySugestaoVendaList(sugestaoVendaList);
				} else {
					sugestaoVendaProdList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdSemQtdPendenteNoPedido((SugestaoVenda) cbSugestaoVenda.getSelectedItem(), pedido);
				}
				if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(pedido.getCliente().cdGrupoPermProd)) {
					sugestaoVendaProdList = GrupoCliPermProdService.getInstance().restringeSugestaoVendaProdByGrupoCliPermProd(sugestaoVendaProdList);
				}
				produtosSugestaoVendaProdCache.addElements(sugestaoVendaProdList.items);
			} else {
				if (cbSugestaoVenda.getSelectedIndex() == 0) {
					sugestaoVendaProdList = produtosSugestaoVendaProdCache;
				} else {
					SugestaoVenda sugestaoVenda = (SugestaoVenda) cbSugestaoVenda.getSelectedItem();
					for (int i = 0; i < produtosSugestaoVendaProdCache.size(); i++) {
						SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) produtosSugestaoVendaProdCache.items[i];
						if (sugestaoVendaProd != null && sugestaoVenda.cdSugestaoVenda.equals(sugestaoVendaProd.cdSugestaoVenda)) {
							sugestaoVendaProdList.addElement(produtosSugestaoVendaProdCache.items[i]);
						}
					}
				}
			}
		}
		SugestaoVendaProd sugestaoVendaProd = sugestaoVendaProdList.size() > 0 ? (SugestaoVendaProd) sugestaoVendaProdList.items[0] : null;
		String cdSugestaoVenda = sugestaoVendaProd != null ? sugestaoVendaProd.cdSugestaoVenda : EMPTY;
		int sugestaoVendaProdListSize = sugestaoVendaProdList.size();
		String cdEmpresa = desvinculadoPedido ? SessionLavenderePda.cdEmpresa : pedido.cdEmpresa;
		String cdRepresentante = desvinculadoPedido ? SessionLavenderePda.getRepresentante().cdRepresentante : pedido.cdRepresentante;
		for (int i = 0; i < sugestaoVendaProdListSize; i++) {
			sugestaoVendaProd = (SugestaoVendaProd) sugestaoVendaProdList.items[i];
			if (sugestaoVendaProd != null) {
				Produto produto = ProdutoService.getInstance().getProduto(cdEmpresa, cdRepresentante, sugestaoVendaProd.cdProduto);
				if (!ValueUtil.valueEquals(produto, new Produto())) {
					produto.nuRelevanciaSort = sugestaoVendaProd.nuRelevancia;
					if (sugestaoVendaProd.cdSugestaoVenda.equals(cdSugestaoVenda)) {
						produto.cdSugestaoVenda = cdSugestaoVenda;
						listProdutoPorSugestaoVenda.addElement(produto);
					} else {
						if (listProdutoPorSugestaoVenda.size() > 1) {
							SortUtil.qsortInt(listProdutoPorSugestaoVenda.items, 0, listProdutoPorSugestaoVenda.size() - 1, false);
						}
						int qtItensLista = LavenderePdaConfig.getQtLimiteItensSugestaoListas(isComboAllSelected(), cdProdutoSugestaoVenda, onFechamentoPedido);
						if (listProdutoPorSugestaoVenda.size() > qtItensLista && qtItensLista >= 0) {
							listProdutoPorSugestaoVenda.setSize(qtItensLista);
						}
						int listProdutoPorSugestaoVendaSize = listProdutoPorSugestaoVenda.size();
						for (int j = 0; j < listProdutoPorSugestaoVendaSize; j++) {
							Produto produtoSugestaoVendaProd = (Produto) listProdutoPorSugestaoVenda.items[j];
							if (!isProdutoValido(produtoSugestaoVendaProd)) {
								listProdutoPorSugestaoVenda.removeElement(produtoSugestaoVendaProd);
								listProdutoPorSugestaoVendaSize--;
								j--;
							}
						}
						sugestaoParaVoce = VectorUtil.concatVectors(sugestaoParaVoce, listProdutoPorSugestaoVenda);
						listProdutoPorSugestaoVenda = new Vector();
						cdSugestaoVenda = sugestaoVendaProd.cdSugestaoVenda;
						i--;
					}
				}
			}
		}
		if (listProdutoPorSugestaoVenda.size() > 1) {
			SortUtil.qsortInt(listProdutoPorSugestaoVenda.items, 0, listProdutoPorSugestaoVenda.size() - 1, false);
		}
		int qtItensLista = LavenderePdaConfig.getQtLimiteItensSugestaoListas(isComboAllSelected(), cdProdutoSugestaoVenda, onFechamentoPedido);
		if (listProdutoPorSugestaoVenda.size() > qtItensLista && qtItensLista >= 0) {
			listProdutoPorSugestaoVenda.setSize(qtItensLista);
		}
		int listProdutoPorSugestaoVendaSize = listProdutoPorSugestaoVenda.size();
		for (int j = 0; j < listProdutoPorSugestaoVendaSize; j++) {
			Produto produtoSugestaoVendaProd = (Produto) listProdutoPorSugestaoVenda.items[j];
			if (!isProdutoValido(produtoSugestaoVendaProd)) {
				listProdutoPorSugestaoVenda.removeElement(produtoSugestaoVendaProd);
				listProdutoPorSugestaoVendaSize--;
				j--;
			}
		}
		removeProdutosDuplicados(sugestaoParaVoce, listProdutoPorSugestaoVenda);
		sugestaoParaVoce = VectorUtil.concatVectors(sugestaoParaVoce, listProdutoPorSugestaoVenda);
		return sugestaoParaVoce;
	}

	private void removeProdutosDuplicados(Vector sugestaoParaVoce, Vector listProdutoPorSugestaoVenda) {
		if (ValueUtil.isNotEmpty(sugestaoParaVoce) && ValueUtil.isNotEmpty(listProdutoPorSugestaoVenda)) {
			int sizeSugestoes = sugestaoParaVoce.size();
			for (int x = 0; x < sizeSugestoes; x++) {
				Produto produto = (Produto) sugestaoParaVoce.items[x];
				if (listProdutoPorSugestaoVenda.contains(produto)) {
					listProdutoPorSugestaoVenda.removeElement(produto);
				}
			}
		}
	}

	private Vector getSugestaoCadastrada(Vector sugestaoGiroProduto, Vector sugestaoParaVoce) throws SQLException {
		Vector domainList = VectorUtil.concatVectors(sugestaoGiroProduto, sugestaoParaVoce);
		Vector listProdutoPorSugestaoCadastrada = new Vector();
		Vector sugestaoVendaProdList = new Vector();
		if (isSugestaoIndustria()) {
			loadSugestaoIndustria(listProdutoPorSugestaoCadastrada);
		} else {
			if (cbSugestoesProdutos.getSelectedIndex() == 0) {
				sugestaoVendaProdList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdBySugestaoVendaList(sugestaoVendaList);
			} else {
                if (cbSugestoesProdutos.getSelectedItem() instanceof SugestaoVenda) {
				    sugestaoVendaProdList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdSemQtdPendenteNoPedido( (SugestaoVenda) cbSugestoesProdutos.getSelectedItem(), pedido);
                }
			}
			if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(pedido.getCliente().cdGrupoPermProd)) {
				sugestaoVendaProdList = GrupoCliPermProdService.getInstance().restringeSugestaoVendaProdByGrupoCliPermProd(sugestaoVendaProdList);
			}
			int sugestaoVendaProdListSize = sugestaoVendaProdList.size();
			for (int i = 0; i < sugestaoVendaProdListSize; i++) {
				SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) sugestaoVendaProdList.items[i];
				Produto produto = ProdutoService.getInstance().getProduto(pedido.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, sugestaoVendaProd.cdProduto);
				if (produto != null && ValueUtil.isNotEmpty(produto.cdProduto) && isProdutoValido(produto)) {
					produto.cdSugestaoVenda = sugestaoVendaProd.cdSugestaoVenda;
					produto.nuRelevancia = sugestaoVendaProd.nuRelevancia;
					produto.nuRelevanciaSort = sugestaoVendaProd.nuRelevancia;
					if (isComboAllSelected()) {
						produto.vlPrecoSort = getPrecoProduto(produto);
						produto.qtEstoqueProduto = EstoqueService.getInstance().getEstoqueProduto(produto);
					}
					addProdutoMaiorRelevancia(produto, listProdutoPorSugestaoCadastrada, domainList);
				}
			}
		}
		return listProdutoPorSugestaoCadastrada;
	}

	private void loadSugestaoIndustria(Vector listProdutoPorSugestaoCadastrada) throws SQLException {
		SugestaoVenda sugestaoVenda = (SugestaoVenda) cbSugestoesProdutos.getSelectedItem();
		if (sugestaoVenda == null) return;
		ProdutoIndustria produtoIndustria = new ProdutoIndustria(sugestaoVenda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, sugestaoVenda.cdSugestaoVenda);
		if (ProdutoBase.SORT_COLUMN_DSPRODUTO.equals(sortAtributte)) {
			produtoIndustria.sortAtributte = "dsRefProdIndustria";
		} else {
			produtoIndustria.sortAtributte = "cdRefProdIndustria";
		}
		produtoIndustria.sortAsc = sortAsc;
		Vector listProdutoIndustria = ProdutoIndustriaService.getInstance().findAllByExample(produtoIndustria);
		if (ValueUtil.isNotEmpty(listProdutoIndustria)) {
			listProdutoPorSugestaoCadastrada.addElementsNotNull(listProdutoIndustria.items);
		}
	}

	private boolean isProdutoValido(Produto produto) {
		if (produto != null) {
			if (desvinculadoPedido) {
				return true;
			}
			int size = pedido.itemPedidoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.cdProduto.equals(produto.cdProduto)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

	private double getPrecoProduto(Produto produto) throws SQLException {
		if (ValueUtil.isNotEmpty(produto.cdProduto)) {
			if (desvinculadoPedido) {
				ItemTabelaPreco itemTabelaPreco = ItemTabelaPrecoService.getInstance().getItemTabelaPreco(cbTabelaPreco.getValue(), produto.cdProduto, ValueUtil.VALOR_ZERO);
				produto.vlPrecoSort = itemTabelaPreco.vlUnitario;
			} else {
				String cdTabelaPreco = ValueUtil.isNotEmpty(pedido.cdTabelaPreco) ? pedido.cdTabelaPreco : pedido.getCliente().cdTabelaPreco;
				cdTabelaPreco = ValueUtil.isEmpty(cdTabelaPreco) ? TabelaPreco.CDTABELAPRECO_VALOR_ZERO : cdTabelaPreco;
				produto.vlPrecoSort = ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, cdTabelaPreco, LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false);
			}
			return produto.vlPrecoSort;
		}
		return 0;
	}

	private Vector addProdutoMaiorRelevancia(Produto produto, Vector produtoList, Vector fullList) {
		fullList = VectorUtil.concatVectors(produtoList, fullList);
		int index = fullList.indexOf(produto);
		if (index != -1) {
			Produto prod = (Produto) fullList.elementAt(index);
			if (produto.nuRelevanciaSort > prod.nuRelevanciaSort) {
				prod.nuRelevanciaSort = produto.nuRelevanciaSort;
			}
		} else {
			produtoList.addElement(produto);
		}
		return produtoList;
	}

	public void list(Pedido pedidoBase) throws SQLException {
		this.pedido = pedidoBase;
		list();
	}

	// @Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
		if (!onFechamentoPedido) return super.getToolTip(domain);
		if (domain instanceof Produto) {
			return ProdutoService.getInstance().getDescricaoProdutoComReferencia((Produto) domain);
		} else if (domain instanceof ProdutoIndustria) {
			ProdutoIndustria produtoIndustria = (ProdutoIndustria) domain;
			return produtoIndustria.toString();
		}
		return super.getToolTip(domain);
	}

	private void listSugestaoVendaGrupoOnly(Vector domainList) throws SQLException {
		if (sugestaoGrupoListContainer != null) {
			int listSize = 0;
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			try {
				sugestaoGrupoListContainer.removeAllContainers();
				// --
				listSize = domainList.size();
				Container[] all = new Container[listSize];
				// --
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					for (int i = 0; i < listSize; i++) {
						all[i] = c = new BaseListContainer.Item(sugestaoGrupoListContainer.getLayout());
						domain = (BaseDomain) domainList.items[i];
						c.id = domain.getRowKey();
						c.setItens(getItem(domain));
						c.setToolTip(getToolTip(domain));
						c.setIconsLegend(getIconsLegend(domain), resizeIconsLegend);
						setPropertiesInRowList(c, domain);
						domain = null;
					}
					sugestaoGrupoListContainer.addContainers(all);
				}
			} finally {
				domainList = null;
				msg.unpop();
			}
		} else {
			int listSize = 0;
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			String[][] gridItems;
			try {
				clearGrid();
				// --
				listSize = domainList.size();
				// --
				if (listSize > 0) {
					String[] item = getItem(domainList.items[0]);
					gridItems = new String[listSize][item.length];
					gridItems[0] = item;
					for (int i = 1; i < listSize; i++) {
						gridItems[i] = getItem(domainList.items[i]);
					}
					item = null;
					// --
					grid.setItems(gridItems);
				}
			} finally {
				gridItems = null;
				domainList = null;
				msg.unpop();
			}
		}
	}

	private boolean destacaProdutoPromocionalItemTabPreco(Item c, ProdutoBase produtoBase) throws SQLException {
		if (produtoBase instanceof ProdutoTabPreco) {
			String dsTabPrecoPromoList = ProdutoTabPrecoService.getInstance().getProdutoTabPrecoColumn(produtoBase.cdProduto, "dsTabPrecoPromoList");
			String[] tabelasPromocionais = StringUtil.split(dsTabPrecoPromoList, '|');
			Vector vector = new Vector(tabelasPromocionais);
			String cdTabelaPreco = desvinculadoPedido ? cbTabelaPreco.getValue() : pedido.cdTabelaPreco;
			int index = vector.indexOf(cdTabelaPreco);
			return (index != -1) || (ValueUtil.isEmpty(cdTabelaPreco) && (tabelasPromocionais.length > 0));
		} else {
			return produtoBase.isProdutoPromocao();
		}
	}

	private boolean destacaProdutoDescPromocional(Item c, ProdutoBase produto) throws SQLException {
		String dsTabPrecoDescPromocionalList = ProdutoTabPrecoService.getInstance().getProdutoTabPrecoColumn(produto.cdProduto, "dsTabPrecoDescPromocionalList");
		String[] tabelasPromocionais = StringUtil.split(dsTabPrecoDescPromocionalList, '|');
		Vector vector = new Vector(tabelasPromocionais);
		String cdTabelaPreco = desvinculadoPedido ? cbTabelaPreco.getValue() : pedido.cdTabelaPreco;
		int index = vector.indexOf(cdTabelaPreco);
		return (index != -1) || (ValueUtil.isEmpty(cdTabelaPreco) && (tabelasPromocionais.length > 0));
	}

	private void btInfoProdutosClick(Produto produto) throws SQLException {
		BaseDomain domain = ProdutoService.getInstance().findByRowKeyDyn(produto.getRowKey());
		closePopupSuper();
		if (cadProdutoMenuForm != null) {
			cadProdutoMenuForm.fromSugestaoMultProdutos = true;
			cadProdutoMenuForm.btInfoProdutosClick(domain);
		} else {
			cadItemPedidoForm.fromSugestaoMultProdutos = true;
			cadItemPedidoForm.btInfoProdutosClick(domain);
		}
	}

	public void setCadProdutoMenuForm(CadProdutoMenuForm cadProdutoMenuForm) {
		this.cadProdutoMenuForm = cadProdutoMenuForm;
	}

	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}
	
}
