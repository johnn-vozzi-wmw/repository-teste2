package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.RestricaoVendaCli;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.domain.VerbaCliente;
import br.com.wmw.lavenderepda.business.service.AreaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.BonificacaoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemKitPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoKitService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.RestricaoVendaCliService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.TipoRankingService;
import br.com.wmw.lavenderepda.business.service.VerbaClienteService;
import br.com.wmw.lavenderepda.business.service.VerbaGrupoSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaService;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.WindowUtil;
import totalcross.sys.Settings;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.Grid;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class CadItemPedidoBonificacaoForm extends AbstractBaseCadItemPedidoForm {
	
	private static final String HASHKEY_PA = "PA";
	private static final String HASHKEY_AP = "AP";
	private static final String HASHKEY_DS = "DS";
	private static final String HASHKEY_CD = "CD";
	
	private ItemParticipacaoExtrapoladoWindow itemParticipacaoExtrapoladoWindow;
	protected PushButtonGroupBase btTipoPesquisaEdFiltro;
	private HashMap<String, Integer> posicoesbtTipoPesquisaEdFiltro = new HashMap<String,Integer>();
	public boolean filterByPrincipioAtivo;
	public boolean filterByAplicacaoProduto;
	public boolean filterByCodigoProduto;
	
	private MarcadorMultiComboBox cbMarcador;
	
	public CadItemPedidoBonificacaoForm(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		super(Messages.PEDIDO_LABEL_VLBONIFICACAO, pedido, cadPedidoForm);

		configure(cadPedidoForm, pedido);
		
		if (LavenderePdaConfig.permiteTodasTabelasPedidoBonificacao) {
			cbTabelaPreco.loadTabelasPrecos(pedido, true);
		} else {
			cbTabelaPreco.carregaTabelaPrecoBonificacao(pedido.getCliente());
		}
		
		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada) {
			btTipoPesquisaEdFiltro = new PushButtonGroupBase(new String[] {Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_CONTEM, Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_INICIA}, true, 0, -1, 1, 1, true, PushButtonGroup.NORMAL);
		} 
		else {
			inicializabtTipoPesquisaEdFiltro();
		}
		
		cbMarcador = new MarcadorMultiComboBox();
		
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
			cbMarcador.load();
		}
	}
	
	public static CadItemPedidoBonificacaoForm getInstance(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		if (instance == null || !(instance instanceof CadItemPedidoBonificacaoForm)) {
			instance = new CadItemPedidoBonificacaoForm(cadPedidoForm, pedido);
			listInicialized = true;
		} else {
			((CadItemPedidoBonificacaoForm) instance).configure(cadPedidoForm, pedido);
			((CadItemPedidoBonificacaoForm) instance).limpaFiltros();
		}
		return (CadItemPedidoBonificacaoForm) instance;
	}
	
	private void configure(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		this.cadPedidoForm = cadPedidoForm;
		this.pedido = pedido;
		dsFiltro = "";
	}
	
	public static CadItemPedidoBonificacaoForm getNewCadItemPedidoBonificacao(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		return new CadItemPedidoBonificacaoForm(cadPedidoForm, pedido);
	}
	
	@Override
	protected void refreshComponents() throws SQLException {
		super.refreshComponents();
		cbTabelaPreco.setEnabled(isEnabled());
	}
	
	private void limpaFiltros() throws SQLException {
		edFiltro.setValue("");
		dsFiltro = edFiltro.getText();
		resetCombosGrupoProduto();
		ckProdutoDescPromocional.setChecked(false);
		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada && btTipoPesquisaEdFiltro != null) {
			btTipoPesquisaEdFiltro.setSelectedIndex(0);
		} else if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			btTipoPesquisaEdFiltro.setSelectedIndex(0);
		}
		
	}
	
	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		//--
		if (pedido.isPedidoAberto()) {
			cadPedidoForm.afterCrudItemPedido();
		}
		if (listContainer != null) {
			clearListContainerAndTotalizers();
		}
		edFiltro.setValue("");
		dsFiltro = "";
		listInicialized = false;
		flFromCadPedido = false;
		cadPedidoForm.inRelatorioMode = prevContainer instanceof ListItemPedidoForm;
		
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) cbMarcador.unselectAll();
	}

	@Override
	protected Vector getProdutoList() throws SQLException {
		return getProdutoList(true);
	}
	
	protected Vector getProdutoList(boolean includeInterfaceFilters) throws SQLException {
		ProdutoBase produtoFilter;
		Vector listProduto;
		try {
			produtoFilter = getProdutoFilter(includeInterfaceFilters);
			listProduto = getListProduto(produtoFilter);
			afterList(listProduto, produtoFilter);
		} catch (FilterNotInformedException e) {
			listProduto = new Vector(0);
		}
		return listProduto;
	}
	
	private void getProdutoFilterInterface(ProdutoBase filter) {
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
			filter.cdMarcadores = cbMarcador.getSelected();
		}
	}
	
	private ProdutoBase getProdutoFilter(boolean includeInterfaceFilters) throws SQLException {
		ProdutoBase produtoFilter = super.getProdutoBaseFilter();
		produtoFilter.sortAsc = listContainer.sortAsc;
		
		if (includeInterfaceFilters) {
			getProdutoFilterInterface(produtoFilter);
		}
		
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProduto() && ProdutoBase.SORTCOLUMN_NURANKING.equals(listContainer.atributteSortSelected)) {
			produtoFilter.sortAtributte = TipoRankingService.getInstance().getSortColumnNuRankingProduto(0);
		} else {
			produtoFilter.sortAtributte = listContainer.atributteSortSelected;
		}
		produtoFilter.itemTabelaPreco = new ItemTabelaPreco();
		produtoFilter.itemTabelaPreco.cdTabelaPreco = pedido.cdTabelaPreco;
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			produtoFilter.itemTabelaPreco.cdPrazoPagtoPreco = pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
		}
		Estoque estoque = null;
		if (LavenderePdaConfig.isUsaLocalEstoque()) {
			estoque = new Estoque();
			estoque.cdLocalEstoque = pedido.getCdLocalEstoque();
		}
		if (LavenderePdaConfig.isUsaFiltroProdutosPorCentroCusto()  && LavenderePdaConfig.isUsaCentroCustoInformacoesAdicionais()) {
			produtoFilter.cdCentroCustoProdFilter = pedido.cdCentroCusto;
		}
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && LavenderePdaConfig.isUsaPlataformaVendaInformacoesAdicionais()) {
			produtoFilter.cdPlataformaVendaFilter = pedido.cdPlataformaVenda;
        }
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			produtoFilter.flExcecaoProduto = ValueUtil.isNotEmpty(pedido.getTipoPedido().flExcecaoProduto) ? pedido.getTipoPedido().flExcecaoProduto : ValueUtil.VALOR_NAO;
			produtoFilter.cdTipoPedidoFilter = pedido.cdTipoPedido;
		}
		produtoFilter.estoque = estoque;
		prepareGrupoProdTipoPedFilter(produtoFilter);
		prepareProdutoClienteFilter(produtoFilter);
		prepareClienteProdutoFilter(produtoFilter);
		prepareProdutoCondPagtoFilter(produtoFilter);
		prepareItemPedidoFilter(produtoFilter);
		prepareGiroProdutoFilter(produtoFilter);
		return produtoFilter;
	}

	private Vector getListProduto(ProdutoBase produto) throws SQLException {
		boolean onlyStartString = LavenderePdaConfig.usaPesquisaProdutoPersonalizada && btTipoPesquisaEdFiltro != null && btTipoPesquisaEdFiltro.getSelectedIndex() == 1;
		return ProdutoService.getInstance().findProdutos(dsFiltro, cbTabelaPreco.getValue(), null, filterByPrincipioAtivo, produto, onlyStartString, filterByCodigoProduto);
	}

	private void afterList(Vector listProduto, ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto5()) {
			SessionLavenderePda.setUltimoProdutoFilter(produtoFilter);
		}
		loadVlProdutoToList(listProduto);
	}

	protected String getSelectedRowKey() {
		return listContainer.getSelectedId();
	}

	@Override
	protected void onFormStart() throws SQLException {
		super.onFormStart();
		if (LavenderePdaConfig.permiteTodasTabelasPedidoBonificacao) {
			cbTabelaPreco.setValue(pedido.getCliente().cdTabelaPreco);
		} else {
			cbTabelaPreco.setValue(pedido.cdTabelaPreco);
		}
		
		if (!pedido.isSugestaoPedidoGiroProduto()) {
			addFiltros();
		}
		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada && btTipoPesquisaEdFiltro != null) {
			UiUtil.add(containerGrid, btTipoPesquisaEdFiltro, getLeft(), AFTER + HEIGHT_GAP, PREFERRED + 6, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerGrid, edFiltro, AFTER + WIDTH_GAP, SAME);
		} else if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			UiUtil.add(containerGrid, btTipoPesquisaEdFiltro, getLeft(), AFTER + HEIGHT_GAP, PREFERRED + 6, UiUtil.getControlPreferredHeight());
			UiUtil.add(containerGrid, edFiltro, AFTER + WIDTH_GAP, SAME);
		} else {
			UiUtil.add(containerGrid, edFiltro, getLeft(), AFTER + HEIGHT_GAP);
		}
		UiUtil.add(this, containerVendaUnitaria, SAME, SAME, FILL, FILL - barBottomContainer.getHeight() + 1);
		populateHashEditsAndLabels(LavenderePdaConfig.ordemCamposTelaItemPedidoBonificacao);
		addComponentsInScreen();
	}

	protected void addComponentsInScreen() throws SQLException {
		addFields();
		
		addGridAndFields();
		addInfosExtras();
	}
	
	protected void addFiltros() {
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
    		UiUtil.add(containerGrid, new LabelName(Messages.MARCADOR_MARCADORES), cbMarcador, getLeft(), AFTER + HEIGHT_GAP);
    	}
	}

	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		if (inVendaUnitariaMode) {
			containerGrid.setVisible(false);
			repaintNow();
			containerVendaUnitaria.setVisible(true);
			if (isShowFotoProduto()) {
				imageCarrousel.setVisible(true);
			}
		} else {
			if (isShowFotoProduto()) {
				imageCarrousel.setVisible(false);
				if (!isEditing()) {
					imageCarrousel.setImgEmpty();
				}
			}
			containerVendaUnitaria.setVisible(false);
			if (!isEditing() && pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
				containerVendaUnitaria.remove(containerInfosProduto);
			}
			containerGrid.setVisible(true);
		}
		//--
		if (inVendaUnitariaMode && !LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras) {
			stopScanner();
		} else {
			startScanner();
		}
		btSalvar.setVisible(isEditing() && isEnabled());
		btSalvarNovo.setVisible(!isEditing() && inVendaUnitariaMode);
		btExcluir.setVisible(isEditing() && isEnabledSolAutorizacaoSkipValidation() && !isModoGrade());
		btCalcular.setVisible(isEnabled() && inVendaUnitariaMode);
		btListaItens.setVisible(!(isEnabled() && inVendaUnitariaMode) && !isEditing());
		lbItemByItem.setVisible(isEditing() && !fromItemPedidoInseridoPedido);
		btPreviousItem.setVisible(isEditing() && !fromItemPedidoInseridoPedido);
		btNextItem.setVisible(isEditing() && !fromItemPedidoInseridoPedido);
		cbTabelaPreco.setVisible(true);
		edDsTabelaPreco.setVisible(false);
		if (LavenderePdaConfig.usaGradeProduto2()) {
			lbItemGrade1.setVisible(false);
			cbItemGrade1.setVisible(false);
		} else {
			cbItemGrade1.setVisible(cbItemGrade1.size() > 0);
		}
	}

	@Override
	protected void voltarClick() throws SQLException {
		if (inVendaUnitariaMode && isEditing() && !fromItemPedidoInseridoPedido) {
			super.voltarClick();
		} else if (!inVendaUnitariaMode) {
			super.voltarClick();
		} else {
			if (inVendaUnitariaMode && isEditing()) {
				add();
			}
			if (inVendaUnitariaMode && LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && LavenderePdaConfig.usaCameraParaLeituraCdBarras()) {
				UiUtil.add(barBottomContainer, btLeitorCamera, 4);
			}
			backToListAndClearDadosItemPedido();
			setFocusInFiltro();
		}
	}

	@Override
	protected void onSave() throws SQLException {
		if (!fromItemPedidoInseridoPedido) {
			super.onSave();
		} else {
			save();
		}
	}

	protected void backToListAndClearDadosItemPedido() throws SQLException {
		reloadTabPrecoOnBackToList();
		repaintScreen();
		clearProdutoVendaAtual();
		addItensOnButtonMenu();
	}

	protected ItemPedido addNovoItem() throws SQLException {
		ItemPedido itemPedido = super.addNovoItem();
		itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
		return itemPedido;
	}

	@Override
	public void edit(BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		if (itemPedido.pedido == null) {
			itemPedido.pedido = pedido;
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			ItemKitPedidoService.getInstance().findItemKitPedidoList(itemPedido);
		}
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			ItemPedidoGradeService.getInstance().findItemPedidoGradeList(itemPedido);
		}
		internalSetEnabled(pedido.isPedidoAberto() && !cadPedidoForm.inOnlyConsultaItens, false);
		if (LavenderePdaConfig.isUsaVendaRelacionada() && LavenderePdaConfig.aplicaValoresProdPrincipalProdRelacionado) {
			internalSetEnabled(false, false);
		}
		super.edit(domain);
		addItensOnButtonMenu();
	}

	@Override
	protected void btFiltrarClick() throws SQLException {
		dsFiltro = edFiltro.getText();
		filtrarClick();
		Grid.repaint();
	}

	private void filtrarProduto() throws SQLException {
		if (validateFiltro()) {
			flListInicialized = true;
			carregaGridProdutos();
			//--
			clearDomainAndScreen();
			if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
				edFiltro.setValue("");
			}
		}
	}

	private void clearDomainAndScreen() throws SQLException {
		clearScreen();
		addNovoItem();
	}

	private boolean validateFiltro() {
		dsFiltro = edFiltro.getText();
		String filtro = dsFiltro;
		if (LavenderePdaConfig.usaPesquisaInicioString && dsFiltro.startsWith("*")) {
			filtro = dsFiltro.substring(1);
		}
		if ((filtro == null) || (filtro.length() < LavenderePdaConfig.qtMinimaCaracteresFiltroProduto)) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, LavenderePdaConfig.qtMinimaCaracteresFiltroProduto));
			return false;
		}
		return true;
	}
	
	public void btHistoricoItemClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			UiUtil.showProcessingMessage();
			try {
				RelHistoricoItensPedidoWindow historicoItensPedidoForm = new RelHistoricoItensPedidoWindow((ItemPedido)screenToDomain(), pedido);
				historicoItensPedidoForm.popup();
			} catch (ValidationException e) {
				UiUtil.showInfoMessage(e.getMessage());
			} finally {
				UiUtil.unpopProcessingMessage();
			}
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}
	
	public void btSubstituicaoTributariaClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			new RelSubstituicaoTributariaWindow(pedido, getItemPedido(), isEditing()).popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}
	
	protected void btInfoExtraItemPedidoClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			InfoExtraItemPedidoWindow infoExtraItemPedidoForm = new InfoExtraItemPedidoWindow(getItemPedido(), pedido, cadPedidoForm);
			infoExtraItemPedidoForm.popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	protected void btRelVendasProdCliClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			RelVendasProdutoPorClienteWindow rel = new RelVendasProdutoPorClienteWindow(getItemPedido().cdProduto);
			rel.popup();
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btIconeVerba) {
					if (isUtilizaVerba(getItemPedido())) {
						verbaClick();
					} else {
						UiUtil.showInfoMessage(Messages.NAO_UTILIZA_VERBA);
					}
				} else if (event.target != null && event.target == btIconeAutorizacao) {
					openListAutorizacaoWindow();
				} else if (event.target == btIconeKit) {
					btIconeKitClick();
				} else if (event.target == cbMarcador) {
					filtrarClick();
				} else if (event.target == btTipoPesquisaEdFiltro) {
					btGroupTipoFiltrosClick(btTipoPesquisaEdFiltro.getSelectedIndex());
				}									
				break;
			}

			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == btOpcoes) {
					if (btOpcoes.selectedItem.equals(Messages.PRODUTO_TITULO_CADASTRO)) {
						detalhesClick();
					} else if (btOpcoes.selectedItem.equals(Messages.VERBA_NOME_ENTIDADE)) {
						verbaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_HISTORICO)) {
						btHistoricoItemClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTOKIT_TITULO_CADASTRO)) {
						btItensKitClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_SLIDEFOTOS)) {
						btSlideFotosClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_REGISTRAR_FALTA_PRODUTO)) {
						CadProdutoFaltaWindow cadProdutoFaltaWindow = new CadProdutoFaltaWindow(getItemPedido(), null, true, isEditing());
						cadProdutoFaltaWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.BOTAO_PREVISAO_DESCONTOS)) {
						previsaoDescontoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.REL_TITULO_SUBSTITUICAO_TRIBUTARIA)) {
						getPedidoService().calculateItemPedido(pedido, (ItemPedido)screenToDomain(), false);
						btSubstituicaoTributariaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_GRUPOS_PENDENTES)) {
						RelGrupoProdutoNaoInseridoPedidoWindow relGrupoProdutoNaoInseridoPedidoWindow = new RelGrupoProdutoNaoInseridoPedidoWindow(super.title, false, pedido);
						relGrupoProdutoNaoInseridoPedidoWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_PRODUTOS_PENDENTES)) {
						try {
							RelProdutosPendentesWindow relProdutosPendentesWindow = RelProdutosPendentesWindow.getNewInstance(pedido, true, false);
							relProdutosPendentesWindow.cadPedidoForm = cadPedidoForm;
							relProdutosPendentesWindow.popup();
						} catch (ValidationException ex) {
							UiUtil.showWarnMessage(ex.getMessage());
						} finally {
							RelProdutosPendentesWindow.cleanInstance();
						}
					} else if (btOpcoes.selectedItem.equals(Messages.REL_TITULO_INFO_TRIBUTARIA_DETALHADA)) {
						if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && getSelectedProduto() != null) {
							if (getSelectedProduto() != null) {
								calculateItemPedidoQtUnitario(getSelectedProduto());
							}	
						}
						getPedidoService().calculateItemPedido(pedido, getItemPedido(), false);
						RelInfoTributariaDoItemPedidoWindow relInfoTributariaDoItemPedidoWindow = new RelInfoTributariaDoItemPedidoWindow(getItemPedido());
						relInfoTributariaDoItemPedidoWindow.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_INFORMACOES_EXTRAS)) {
						btInfoExtraItemPedidoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.REL_RENTABILIDADE_COMISSAO_TITULO)) {
						RelRentabilidadeComissaoWindow relRentabilidadeComissao = new RelRentabilidadeComissaoWindow(getItemPedido());
						relRentabilidadeComissao.popup();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_DSOBSERVACAO)) {
						btObservacaoClick();
					} else if (btOpcoes.selectedItem.equals(Messages.PRODUTORETIRADA_NOME_BT)) {
						btProdutosRetiradaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.MENU_OPCAO_DETALHES_CALCULOS)) {
						boolean funcionalidadesDisponiveis = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado || LavenderePdaConfig.isUsaRentabilidadeNoPedido() || LavenderePdaConfig.isUsaCalculaStItemPedido();
						if (funcionalidadesDisponiveis) {
							if (getItemPedido().getProduto() != null) {
								RelDetalhesCalculosItemWindow relDetalhesCalculos = new RelDetalhesCalculosItemWindow(pedido, getItemPedido());
								relDetalhesCalculos.popup();
							} else {
								UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
							}
						} else {
							UiUtil.showInfoMessage(Messages.REL_DETALHES_CALCULOS_NENHUMA_FUNC_DISPONIVEL);
						}
					} else if (btOpcoes.selectedItem.equals(Messages.REL_DIF_ITEMPEDIDO_LABEL_DIFERENCAS)) {
						btDiferencaClick();
					} else if (btOpcoes.selectedItem.equals(Messages.SOL_AUTORIZACAO_ITEM_PEDIDO)) {
						openListAutorizacaoWindow();
					} else if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LABEL_RELVENPRODPORCLI)) {
						btRelVendasProdCliClick();
					} else if (btOpcoes.selectedItem.equals(Messages.ATENDIMENTOHIST_TITLE)) {
						WindowUtil.btAtendimentoHistClick(this, getItemPedido().pedido.cdEmpresa, getItemPedido().pedido.cdCliente);
					}
				}
				break;
			}
		}
	}

	private void btIconeKitClick() throws SQLException {
		if (getItemPedido().getProduto() != null && getItemPedido().getProduto().isKit()) {
			UiUtil.showInfoMessage(Messages.PRODUTOKIT_MSG_PRODUTO_KIT);
		} else {
			UiUtil.showInfoMessage(Messages.PRODUTOKIT_MSG_PRODUTO_NAO_KIT);
		}
	}

	private void alterarTipoTeclado() {
		if (filterByCodigoProduto && LavenderePdaConfig.isExibeBotaoParaFiltroCodigoTecladoNumerico()) {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_INT);
		} else {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_TEXT);
		}
	}

	private void openListAutorizacaoWindow() throws SQLException {
		new ListSolAutorizacaoWindow(null, getItemPedido()).popup();
	}

	@Override
	protected void adjustContainerHeightForMenuCategoria() {

	}

	@Override
	protected void listContainerProdutoClick() throws SQLException {
		super.listContainerProdutoClick();
		gridClickAndRepaintScreen();
		if (inVendaUnitariaMode) {
			containerVendaUnitariaScrollable.repaintNow();
			containerVendaUnitariaScrollable.scrollToPage(1);
			containerVendaUnitariaScrollable.scrollToPage(1000);
		}
	}
	
	public boolean gridClick() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			ItemPedido itemPedido = getItemPedido();
			if (!LavenderePdaConfig.usaAcessoItemInseridoModoEdicao && !LavenderePdaConfig.usaGradeProduto5() || (LavenderePdaConfig.usaAcessoItemInseridoModoEdicao && !pedido.itemPedidoList.contains(itemPedido)) || (LavenderePdaConfig.usaGradeProduto5() && !isProdutoPresenteItemPedido(itemPedido.getProduto()))) {
				inicializaItemParaVenda(itemPedido);
			}
			itemPedido.pedido = pedido;
			Produto p = getSelectedProduto();
			if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido()) {
				ProdutoBloqueadoService.getInstance().validateProdutoBloqueado(itemPedido, cbTabelaPreco);
			}
			itemPedido.setProduto(p);
			itemPedido.cdProduto = p.cdProduto;
			itemPedido.flTipoEdicao = 0;
			itemPedido.dsProduto = p.dsProduto;
			if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
				if (LavenderePdaConfig.usaSequenciaInsercaoNuSeqProduto) {
					itemPedido.nuSeqProduto = itemPedido.nuSeqItemPedido;
				} else {
					itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedido, "NUSEQPRODUTO") + 1;
				}
			} else {
				itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
			}
			showAvisoProdutoPendenteRetirada();
			if (LavenderePdaConfig.usaAreaVendas) {
				AreaVendaProdutoService.getInstance().validadeProdutoAreaVenda(pedido.cdAreaVenda, itemPedido.cdProduto, false);
			}
			if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
				ClienteService.getInstance().validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, pedido.getCliente());
			}
			if (LavenderePdaConfig.isConfigGradeProduto() && (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade())) {
				loadDadosGradeProduto(itemPedido);
			}
			if (!gridClickUnidadeAlternativa(itemPedido, false)) {
				return false;
			}
			if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
				itemPedido.cdCondicaoComercial = cbCondicaoComercial.getValue();
			}
			boolean isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem = LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem();
			if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
				if (LavenderePdaConfig.isUsaDescontoPedidoPorClienteMinimoMaximo() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
					itemPedido.vlPctDescCliente = pedido.vlPctDescCliente;
				}
				if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoManual() || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
					itemPedido.vlPctDescontoCondicao = pedido.vlPctDescontoCondicao;
				}
				if (LavenderePdaConfig.usaDescontoPedidoPorTipoFrete || isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem) {
					itemPedido.vlPctDescFrete = pedido.vlPctDescFrete;
				}
			}
			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
				if (itemPedido.getProduto().isKit()) {
					ProdutoKitService.getInstance().loadItemKitPedidoFromProdutoKit(itemPedido);
				} else {
					itemPedido.itemKitPedidoList = new Vector(0);
				}
			}
			if (LavenderePdaConfig.isExibeFotoTelaItemPedido()) {
				if (isShowFotoProduto()) {
					repaintNow();
					getFotoByItemPedido(itemPedido);
				}
			}
			changeItemTabelaPreco();
			String dsProduto = LavenderePdaConfig.usaGradeProduto5() && isModoGrade() ? itemPedido.getProduto().getDsAgrupadorGrade() : itemPedido.getDsProdutoWithKey(itemPedido);
			changeLbDsProdutoValue(dsProduto);
			refreshProdutoToScreen(itemPedido);
			calcularClick();
			refreshComponents();
			setEnableComponentsPossuemVariacao();
			addItensOnButtonMenu();
		} finally {
			msg.unpop();
			setFocusInQtde();
		}
		return true;
	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		if (!isEditing()) {
			refreshPedidoToScreen();
		}
		boolean recarregaInterface = false;
		recarregaInterface |= LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples;
		recarregaInterface |= ValueUtil.isNotEmpty(LavenderePdaConfig.ordemCamposTelaItemPedidoBonificacao) && (LavenderePdaConfig.ordemCamposTelaItemPedidoBonificacao.indexOf("/") != -1);
		recarregaInterface |= LavenderePdaConfig.usaOportunidadeVenda;
		recarregaInterface |= LavenderePdaConfig.usaIgnoraControleVerbaNoTipoPedido;
		repaintScreen(recarregaInterface);
		if (!isEditing() && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == 0) && !flListInicialized && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto != ValorParametro.PARAM_INT_VALOR_ZERO)) {
			flListInicialized = true;
			carregaGridProdutos();
		}
		// Necessario para girar a tela, já que uma intancia
		int dif = Settings.screenWidth - barBottomContainer.getWidth();
		if ((dif > 5) || (dif < -5)) {
			reposition();
		}
		//--
		if (isEditing()) {
			setFocusInQtde();
		} else {
			setFocusInFiltro();
		}
	}
	
	@Override
	protected Control getComponentToFocus() {
		return edFiltro;
	}
	
	protected void repaintScreen() throws SQLException {
		repaintScreen(false);
	}

	@Override
	protected void repaintScreen(boolean recarregaInterface) throws SQLException {
		super.repaintScreen(recarregaInterface);
		if (recarregaInterface) {
			removeComponentsInScreen();
			populateHashEditsAndLabels(LavenderePdaConfig.ordemCamposTelaItemPedidoBonificacao);
			addComponentsInScreen();
			removeAndRepositeImageCarouselIfNecessary();
		}
		visibleState();
		reposition();
	}

	protected void removeComponentsInScreen() throws SQLException {
		Container container = permiteInserirMultiplosItensPorVezComInterfaceNegociacao() && !isItemPedidoGrade() ? containerGrid : containerFields2;
		for (int i = 1; i <= hashEdits.size(); i++) {
			container.remove((Control) hashEdits.get(i));
			container.remove((Control) hashLabels.get(i));
		}
		container.remove(lbDsProduto);
		container.remove(numericPad);
		container.remove(btSep3);
		if (isShowFotoProduto()) {
			containerInfosProduto.remove(imageCarrousel);
			if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0 && !LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
				containerInfosProduto.remove(containerFotosProdutos);
			}
		}
		if (btGradeItemPedido != null) container.remove(btGradeItemPedido);
		if (LavenderePdaConfig.usaGradeProduto5()) container.remove(edVlItemPedido);
	}

	protected void btListaItensClick() throws SQLException {
		ListItemPedidoForm listItemPedidoForm = getItemPedidoListForm();
		listItemPedidoForm.show();
	}

	private ListItemPedidoForm getItemPedidoListForm() throws SQLException {
		ListItemPedidoForm listItemPedidoForm;
		cadPedidoForm.inRelatorioMode = true;
		if (flFromCadPedido) {
			listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
		} else {
			listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
		}
		return listItemPedidoForm;
	}

	public void verbaClick() throws SQLException {
		if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir && pedido.isPedidoBonificacao()) {
			String cdGrupoProduto1 = getItemPedido().getProduto() != null ? getItemPedido().getProduto().cdGrupoProduto1 : null;
			new RelVerbaClienteWindow(pedido.getCliente(), cdGrupoProduto1).popup();
		} else if (VerbaGrupoSaldoService.getInstance().isUsaVerbaSaldoPorGrupoProduto(pedido) || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) {
			RelVerbaGrupoSaldoWindow verbaGrupoSaldoWindow = new RelVerbaGrupoSaldoWindow(pedido, getItemPedido());
			verbaGrupoSaldoWindow.popup();
		} else {
			RelVerbaSaldoWindow verbaWindow = new RelVerbaSaldoWindow(pedido);
			verbaWindow.popup();
		}
	}

	protected void addItensOnButtonMenu() throws SQLException {
		btOpcoes.removeAll();
		if (getItemPedido().getProduto() != null && !isModoGrade() || pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
			btOpcoes.addItemAt(Messages.PRODUTO_TITULO_CADASTRO, 0);
			// --Histórico do item
			if (LavenderePdaConfig.usaHistoricoItemPedido) {
				btOpcoes.addItemAt(Messages.ITEMPEDIDO_LABEL_HISTORICO, 3);
			}
			// --Observaçoes
			if (LavenderePdaConfig.usaObservacaoPorItemPedido) {
				btOpcoes.addItemAt(Messages.ITEMPEDIDO_LABEL_DSOBSERVACAO, 6);
			}
		}
		if ((LavenderePdaConfig.usaPedidoBonificacao() || (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir && pedido.isPedidoBonificacao())) && !LavenderePdaConfig.naoDescontaVerbaEmPedidoBonificacao && !LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex && !pedido.isIgnoraControleVerba()) {
			btOpcoes.addItemAt(Messages.VERBA_NOME_ENTIDADE, 1);
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex && pedido.isPedidoAberto() && !pedido.isIgnoraControleVerba()) {
			btOpcoes.addItemAt(Messages.BOTAO_PREVISAO_DESCONTOS, 2);
		}
		
		// --Kit
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			btOpcoes.addItemAt(Messages.PRODUTOKIT_TITULO_CADASTRO, 4);
		}
		// --Relatorio vendas produto
		if (LavenderePdaConfig.relVendasProdutoPorCliente && !isModoGrade()) {
			btOpcoes.addItemAt(Messages.ITEMPEDIDO_LABEL_RELVENPRODPORCLI, 5);
		}
		
		// --Rel.Informações Tributárias
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido && !pedido.getCliente().isNovoCliente() && !pedido.getCliente().isClienteDefaultParaNovoPedido()) {
			btOpcoes.addItemAt(Messages.REL_TITULO_INFO_TRIBUTARIA_DETALHADA, 8);
		}
		// -- Rel. Rentabilidade x Comissão
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
			btOpcoes.addItemAt(Messages.REL_RENTABILIDADE_COMISSAO_TITULO, 9);
		}
		// --Informações Extras do Item do Pedido
		if (LavenderePdaConfig.isUsaOrdemCamposTelaInfoExtra && !pedido.isPedidoBonificacao()) {
			btOpcoes.addItemAt(Messages.ITEMPEDIDO_INFORMACOES_EXTRAS, 10);
		}
		if (Session.isModoSuporte || VmUtil.isJava()) {
			btOpcoes.addItemAt(Messages.MENU_OPCAO_DETALHES_CALCULOS, 11);
		}
		if (LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto()) {
			btOpcoes.addItemAt(Messages.MENU_REGISTRAR_FALTA_PRODUTO, 12);
		}
		if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
			btOpcoes.addItem(Messages.PRODUTORETIRADA_NOME_BT);
		}
		if (LavenderePdaConfig.isMostraFotoProduto() && !isEditing() && pedido.isPedidoAberto() && !isFiltrandoAgrupadorGrade()) {
			btOpcoes.addItem(Messages.ITEMPEDIDO_LABEL_SLIDEFOTOS);
		}
		// --Diferenças
		if (LavenderePdaConfig.usaRetornoAutomaticoDadosErpDif && pedido.isFlOrigemPedidoErp()) {
			btOpcoes.addItemAt(Messages.REL_DIF_ITEMPEDIDO_LABEL_DIFERENCAS, 24);
		}
		// --Solicitação de Autorização
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			btOpcoes.addItem(Messages.SOL_AUTORIZACAO_ITEM_PEDIDO);
		}
		if (LavenderePdaConfig.usaHistoricoAtendimentoUnificado) {
			btOpcoes.addItem(Messages.ATENDIMENTOHIST_TITLE);
		}
	}

	@Override
	protected void addIconsProduto() throws SQLException {
		int iconsGap = Settings.screenWidth < 320 ? WIDTH_GAP * 2 : WIDTH_GAP_BIG;
		int xIcons = LEFT + iconsGap;
		UiUtil.add(containerInfosProduto, containerIconsProduto, LEFT, TOP, FILL, heigthContainerIcons);
		if (LavenderePdaConfig.isMostraFotoProduto() && btIconeFoto.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeFoto, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + iconsGap;
		}
		if ((LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex || LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto) && btIconeVerba.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeVerba, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + iconsGap;
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto() && btIconeKit.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeKit, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade() && isShowFotoProdutoSlider()) {
			UiUtil.add(containerIconsProduto, btIconeCarrosselPopup, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		if (LavenderePdaConfig.isPermiteOcultarCamposItemPedidoGrade() && isModoGrade()) {
			UiUtil.add(containerIconsProduto, btOcultaMostraEditLabels, xIcons, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xIcons = AFTER + WIDTH_GAP_BIG;
		}
		//Deve ser mantido no final, pois seu posicionamento é a direita da tela, Usar xPosRightIcon para manter a ordem dos botões à direita!
		int xPosNextRightIcon = RIGHT - WIDTH_GAP_BIG;
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && btIconeAutorizacao.isVisible()) {
			UiUtil.add(containerIconsProduto, btIconeAutorizacao, xPosNextRightIcon, CENTER, PREFERRED, UiUtil.getControlPreferredHeight());
			xPosNextRightIcon = BEFORE - WIDTH_GAP_BIG;
		}
	}

	protected void tabelaPrecoChange() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido.getProduto() != null) {
			getItemPedidoService().clearDadosItemPedido(itemPedido);
			changeItemTabelaPreco();
		}
	}

	@Override
	protected void changeItemTabelaPreco() throws SQLException {
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		try {
			ItemPedido itemPedido = getItemPedido();
			itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
			if (itemPedido.getProduto() != null) {
				ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
				if (itemTabelaPreco != null) {
					if (LavenderePdaConfig.usaUnidadeAlternativa) {
						itemPedido.cdUnidade = cbUnidadeAlternativa.getValue();
					}
					getPedidoService().resetDadosItemPedido(pedido, itemPedido);
					refreshDomainToScreen(itemPedido);
				}
			}
		} finally {
			mb.unpop();
		}
	}
	
	@Override
	public void refreshDomainToScreen(ItemPedido itemPedido) throws SQLException {
		super.refreshDomainToScreen(itemPedido);
		controlEstoque(itemPedido);
	}
	
	@Override
	protected void beforeSave() throws SQLException {
		reloadCdUnidade();
		ItemPedido itemPedido = getItemPedido();
		if (isModoGrade()) {
			Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
			boolean hasItemPedidoPermiteEstNegativo = itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo || ItemGradeService.getInstance().validateEstoqueItemAgrGrade(itemPedido);
			if (hasItemPedidoPermiteEstNegativo && !itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo) {
				if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_GRADE_LIBERADO)){
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
			addItemPedidoToListItemGrade(itemPedido.itemPedidoPorGradePrecoList, itemPedido);
			int size = itemPedidoPorGradePrecoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoGrade = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
				if (hasItemPedidoPermiteEstNegativo) {
					itemPedidoGrade.isIgnoraMensagemEstoqueNegativo = true;
				}
				if (itemPedidoGrade.pedido == null) {
					itemPedidoGrade.pedido = itemPedido.pedido;
				}
				beforeSaveBonificacao(itemPedidoGrade);
			}
		} else {
			beforeSaveBonificacao(itemPedido);
		}
	}
	
	private void beforeSaveBonificacao(ItemPedido itemPedido) throws SQLException {
		if (!isModoGrade()) {
			BonificacaoService.getInstance().validaQtdeItemBonificacao(itemPedido);
		}
		if (itemPedido.vlVerbaItem < 0) {
			if (LavenderePdaConfig.permiteEscolhaSaldoVerbaAConsumir) {
				selecionaVerbaCliente();
			}
    		if (LavenderePdaConfig.usaPedidoBonificacao() && !LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
    			if (LavenderePdaConfig.usaPedidoBonificacaoUsandoVerbaCliente) {
        			VerbaClienteService.getInstance().validateSaldo(getItemPedido());
        		} else {
        			VerbaService.getInstance().validateSaldo(getItemPedido());
        		}
    		}
    		
    	}
		ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(itemPedido);
		super.beforeSave();
	}

	private void selecionaVerbaCliente() throws SQLException {
		String cdGrupoProduto1 = getItemPedido().getProduto() != null ? getItemPedido().getProduto().cdGrupoProduto1 : null;
		if (!VerbaClienteService.getInstance().isPossuiVerbaClienteGrupoProduto1(cdGrupoProduto1, pedido.cdCliente)) {
			throw new ValidationException(Messages.VERBACLIENTE_ERRO_NAO_POSSUI_VERBA);
		}
		RelVerbaClienteWindow window = new RelVerbaClienteWindow(pedido.getCliente(), cdGrupoProduto1);
		window.singleClickOn = true;
		window.popup();
		VerbaCliente verbaCliente = window.getVerbaCliente();
		if (verbaCliente != null) {
			VerbaClienteService.getInstance().changeCdVerbaSaldoCliente(getItemPedido(), verbaCliente);
		} else {
			throw new ValidationException(Messages.VERBACLIENTE_ERRO_NAO_SELECIONOU_VERBA);
		}
	}

	private void reloadCdUnidade() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (!LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() && itemPedido != null && ValueUtil.isEmpty(itemPedido.cdUnidade)) {
			Produto produto = itemPedido.getProduto();
			if (produto != null) {
				itemPedido.cdUnidade = produto.cdUnidade;
			}
		}
	}

	@Override
	protected void salvarClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		produtoAnterior = itemPedido.getProduto();
		if (LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && itemPedido.getProduto().isProdutoAgrupadorGrade())) {
			calculaItensGrade(itemPedido);
		} else {
			if (calcularClick(true, null)) {
				refreshDomainToScreen(getItemPedido());
			}
		}
		//--
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) SolAutorizacaoService.getInstance().updateFlVisualizadoByItemPedido(getItemPedido(), ValueUtil.VALOR_SIM);
		try {
			super.salvarClick();
		} catch (Throwable e) {
			if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) SolAutorizacaoService.getInstance().updateFlVisualizadoByItemPedido(getItemPedido(), ValueUtil.VALOR_NAO);
			if (e instanceof ValidationException && e.getMessage() != null && e.getMessage().equals(ValidationException.EXCEPTION_ABORT_PROCESS)) {
				return;
			}
			throw e;
		} finally {
			msg.unpop();
		}
		produtoAnterior.estoque = itemPedido.estoque;
		atualizaProdutoNaGrid(produtoAnterior, true);
		voltarClick();
	}

	protected void salvarNovoClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		produtoAnterior = itemPedido.getProduto();
		if (LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && itemPedido.getProduto().isProdutoAgrupadorGrade())) {
			calculaItensGrade(itemPedido);
		} else {
			if (calcularClick(true, null)) {
				refreshDomainToScreen(getItemPedido());
			}
		}
		//--
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
			super.salvarNovoClick();
		} catch (ValidationException e) {
			if ((e.getMessage() != null) && e.getMessage().equals(ValidationException.EXCEPTION_ABORT_PROCESS)) {
				return;
			}
			throw e;
		} finally {
			msg.unpop();
		}
		produtoAnterior.estoque = itemPedido.estoque;
		atualizaProdutoNaGrid(produtoAnterior, true);
		voltarClick();
	}
		
	private void calculaItensGrade(ItemPedido itemPedido) throws SQLException {
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		if (!btGradeItemPedido.isEnabled() && !itemPedidoPorGradePrecoList.contains(itemPedido)) {
			itemPedidoPorGradePrecoList.addElement(itemPedido);
		}
		int size = itemPedidoPorGradePrecoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
			if (itemPedidoPorGradePreco.pedido == null) {
				itemPedidoPorGradePreco.pedido = itemPedido.pedido;
			}
			calcularClick(true, itemPedidoPorGradePreco);
		}
		refreshDomainToScreen(getItemPedido());
	}
	
	//@Override
	protected void afterSave() throws SQLException {
		super.afterSave();
		if (LavenderePdaConfig.atualizarEstoqueInterno && LavenderePdaConfig.usaLocalEstoquePorTabelaPreco()) {
			EstoqueService.getInstance().recalculaEstoqueConsumido(getItemPedido().getProduto().cdProduto);
		}
	}
	
	@Override
	protected void insert(BaseDomain domain) throws java.sql.SQLException {
		ItemPedido itemPedido = (ItemPedido)domain;
		if (isModoGrade()) {
			insereItemPedidoPorGradePreco(itemPedido);
		} else {
			getPedidoService().insertItemPedidoBonificacao(pedido, itemPedido);
	}
	}
	
	private void insereItemPedidoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		int size = itemPedidoPorGradePrecoList.size();
		try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			try {
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
					if (itemPedidoPorGradePreco.qtItemFisico == 0) {
						continue;
					}
					itemPedidoPorGradePreco.itemPedidoPorGradePrecoList = new Vector(0);
					getPedidoService().atualizaNuSeqProduto(itemPedidoPorGradePreco);
					itemPedidoPorGradePreco.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
					getPedidoService().insertItemPedidoBonificacao(pedido, itemPedidoPorGradePreco);
					itemPedidoPorGradePreco.flTipoEdicao = ItemPedido.ITEMPEDIDO_SEM_EDICAO;
				}
			} catch (Throwable ex) {
				CrudDbxDao.getCurrentDriver().rollback();
				throw ex;
			}
			CrudDbxDao.getCurrentDriver().commit();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
	}
	
	 //@Override
    protected void update(BaseDomain domain) throws SQLException {
    	ItemPedido itemPedido = (ItemPedido)domain;
    	if (isModoGrade()) {
			updateItemPedidoAgrupadorGrade(itemPedido);
    	} else {
    		getPedidoService().updateItemPedidoBonificacao(pedido, itemPedido);
    }
    }

    private void updateItemPedidoAgrupadorGrade(ItemPedido itemPedido) throws SQLException {
    	try {
			CrudDbxDao.getCurrentDriver().startTransaction();
			ItemPedidoService.getInstance().emTransacao = true;
			Vector itemPedidoPorGradePrecoMemoriaList = itemPedido.itemPedidoPorGradePrecoList;
			int size = itemPedidoPorGradePrecoMemoriaList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoPorGradePrecoMemoria = (ItemPedido) itemPedidoPorGradePrecoMemoriaList.items[i];
				if (itemPedidoPorGradePrecoMemoria.nuSeqItemPedido == -1) {
					itemPedidoPorGradePrecoMemoria.nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
				}
				itemPedidoPorGradePrecoMemoria.itemPedidoPorGradePrecoList = new Vector(0);
				crudItemPedidoGrade(itemPedidoPorGradePrecoMemoria);
				itemPedidoPorGradePrecoMemoria.flTipoEdicao = itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade = ItemPedido.ITEMPEDIDO_SEM_EDICAO;
			}
			getPedidoService().updatePedidoAfterCrudItemPedido(pedido);
			CrudDbxDao.getCurrentDriver().commit();
		} catch (Throwable e) {
			CrudDbxDao.getCurrentDriver().rollback();
		} finally {
			CrudDbxDao.getCurrentDriver().finishTransaction();
			ItemPedidoService.getInstance().emTransacao = false;
		}
    	
    	
//		Vector itemPedidoPorGradePrecoBancoList = ItemPedidoService.getInstance().findItemPedidoAgrupadorGrade(itemPedido);
//		ItemPedidoService.getInstance().deletaItemPedidoPorGrade1(itemPedidoPorGradePrecoBancoList, itemPedido, pedido);
//		Vector itemPedidoPorGradePrecoMemoriaList = itemPedido.itemPedidoPorGradePrecoList;
//		int size = itemPedidoPorGradePrecoMemoriaList.size();
//		for (int i = 0; i < size; i++) {
//			ItemPedido itemPedidoPorGradePrecoMemoria = (ItemPedido) itemPedidoPorGradePrecoMemoriaList.items[i];
//			getPedidoService().atualizaNuSeqProduto(itemPedidoPorGradePrecoMemoria);
//			getPedidoService().insertItemPedidoBonificacao(pedido, itemPedidoPorGradePrecoMemoria);
//			itemPedidoPorGradePrecoMemoria.flTipoEdicao = ItemPedido.ITEMPEDIDO_SEM_EDICAO;
//		}
//		excluiItemPedidoPorGradePreco(itemPedidoPorGradePrecoMemoriaList);
//		pedido.itemPedidoList.addElementsNotNull(itemPedido.itemPedidoPorGradePrecoList.items);
	}
    
    private void crudItemPedidoGrade(ItemPedido itemPedidoPorGradePrecoMemoria) throws SQLException {
		if (itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade == ItemPedido.FLEDICAOITEMGRADE_INSERINDO) {
			getPedidoService().atualizaNuSeqProduto(itemPedidoPorGradePrecoMemoria);
			itemPedidoPorGradePrecoMemoria.nuSeqItemPedido = getItemPedidoService().getNextNuSeqItemPedidoDatabase(itemPedidoPorGradePrecoMemoria);
			getPedidoService().insertItemPedidoBonificacao(pedido, itemPedidoPorGradePrecoMemoria);
		} else if (itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade == ItemPedido.FLEDICAOITEMGRADE_ATUALIZANDO) {
			getPedidoService().updateItemPedidoBonificacao(pedido, itemPedidoPorGradePrecoMemoria);
		} else if (itemPedidoPorGradePrecoMemoria.flEdicaoItemPedidoGrade == ItemPedido.FLEDICAOITEMGRADE_EXCLUINDO) {
			ItemPedido itemPedidoAux = getItemPedidoService().getItemPedidoByCdProduto(pedido, itemPedidoPorGradePrecoMemoria.getProduto().cdProduto);
			itemPedidoPorGradePrecoMemoria.setQtItemFisico(itemPedidoAux.getQtItemFisico());
			itemPedidoPorGradePrecoMemoria.qtItemFaturamento = itemPedidoAux.qtItemFaturamento;
			getPedidoService().deleteItemPedidoBonificacao(pedido, itemPedidoPorGradePrecoMemoria);
		}
	}
    
    private void excluiItemPedidoPorGradePreco(Vector itemPedidoPorGradePrecoBancoList) {
		int size = itemPedidoPorGradePrecoBancoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoExcluir = (ItemPedido) itemPedidoPorGradePrecoBancoList.items[i];
			pedido.itemPedidoList.removeElement(itemPedidoExcluir);
		}
	}

    //@Override
    protected void delete(BaseDomain domain) throws SQLException {
    	getPedidoService().deleteItemPedidoBonificacao(pedido, (ItemPedido)domain);
		atualizaItemPedidoNaLista();
	}
	
    @Override
    protected void excluirClick() throws SQLException {
    	ItemPedido itemPedido = getItemPedido();
    	if (isModoGrade() && btGradeItemPedido.isEnabled()) {
			excluiItemPedidoPorGradePreco(itemPedido);
		} else {
			super.excluirClick();
		}
    }
    
    private boolean excluiItemPedidoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		if (UiUtil.showConfirmDeleteMessage(Messages.ITEM_GRADE_MSG_EXCLUSAO)) {
			Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
			int size = itemPedidoPorGradePrecoList.size();
			try {
				CrudDbxDao.getCurrentDriver().startTransaction();
				try {
					for (int i = 0; i < size; i++) {
						ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
						delete(itemPedidoPorGradePreco);
					}
				} catch (Throwable ex) {
					CrudDbxDao.getCurrentDriver().rollback();
					throw ex;
				}
				CrudDbxDao.getCurrentDriver().commit();
			} finally {
				CrudDbxDao.getCurrentDriver().finishTransaction();
			}
			return true;
		}
		return false;
	}
	
	private void atualizaItemPedidoNaLista() throws SQLException {
		if (LavenderePdaConfig.destacaProdutosJaIncluidosAoPedido) {
			if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
				getInstance(cadPedidoForm, pedido).atualizaProdutoNaGrid(ProdutoTabPrecoService.getInstance().getProdutoTabPreco(getItemPedido().getProduto().cdProduto), false);
			} else {
				getInstance(cadPedidoForm, pedido).atualizaProdutoNaGrid(getItemPedido().getProduto(), false);
			}
		}
	}

	@Override
	protected boolean filtrarClick() throws SQLException {
		filtrarProduto();
		if (listContainer.size() == 1) {
			listContainer.setSelectedIndex(0);
			produtoSelecionado = null;
			if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
				itemSelecionado = true;
				gridClickAndRepaintScreen();
			}
		}
		if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
			edFiltro.setValue("");
		}
		return listContainer.size() == 1;
	}

	public void validateTabelaBonificacao() {
		if (!LavenderePdaConfig.permiteTodasTabelasPedidoBonificacao) {
			if (cbTabelaPreco.size() > 1) {
				throw new ValidationException(Messages.TABELAPRECO_BONIFICACAO_DUPLICADA);
			}
			if (cbTabelaPreco.size() == 0) {
				throw new ValidationException(Messages.TABELAPRECO_BONIFICACAO_VAZIO);
			}
		}
	}
	
	public void setEditableEditsValor() throws SQLException {
		super.setEditableEditsValor();
		edVlUnidadeEmbalagem.setEditable(false);
		if (LavenderePdaConfig.permiteAlterarItemDePedidoBonificacao) {
        	edVlItemPedido.setEditable(isEnabled());
        } else {
        	edVlItemPedido.setEditable(false);
        }
        edVlPctDesconto.setEditable(isEnabled() && LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() && LavenderePdaConfig.permiteAlterarItemDePedidoBonificacao);
		edVlPctAcrescimo.setEditable(false);
		edPctMargemAgregada.setEditable(false);
		edVlAgregadoSugerido.setEditable(false);
		edPrecoEmbPrimaria.setEditable(false);
	}

	@Override
	protected String[] getDefaultEditLabels() {
		if (isModoGrade()) {
			return new String[] {"1", "4", "6", "13"};
		}
		return new String[] { "1", "4", "3", "6", "15"};
	}

	@Override
	protected boolean isComboTabPrecoVisible() throws SQLException {
		return true;
	}

	@Override
	protected void cbTabelaPrecoClick() throws SQLException {
		tabelaPrecoChange();
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
			int index = listContainer.getScrollPos();
			carregaGridProdutos();
			listContainer.setScrollPos(index);
		}
		repaint();
	}
	
	public ItemParticipacaoExtrapoladoWindow getListItemValorExtrapoladoWindow() throws SQLException {
		if (itemParticipacaoExtrapoladoWindow == null || (!pedido.equals(itemParticipacaoExtrapoladoWindow.pedido))) {
			itemParticipacaoExtrapoladoWindow = new ItemParticipacaoExtrapoladoWindow(pedido);
			itemParticipacaoExtrapoladoWindow.setCadItemPedidoBonificacaoForm(this);
		}
		if (itemParticipacaoExtrapoladoWindow.hasVlItemPedidoExtrapolado()) {
			itemParticipacaoExtrapoladoWindow.list(pedido);
		}
		return itemParticipacaoExtrapoladoWindow;
	}

	@Override
	protected void setProdutoSelecionado(Produto produto) throws SQLException {
		produtoSelecionado = produto;
	}

	@Override
	protected void inicializaItemParaVenda(ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().clearDadosItemPedido(pedido, itemPedido);
		itemPedido.pedido = pedido;
		itemPedido.flTipoEdicao = 0;
		itemPedido.setProduto(getProdutoSessaoVenda());
		validateProdutoClicado(itemPedido);
		//Tabela de preço
		if (!inVendaUnitariaMode && !isEditing()) {
			if (ValueUtil.isEmpty(itemPedido.cdTabelaPreco)) {
				itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
				if (cbTabelaPreco.getSelectedIndex() != -1) {
					itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
				}
			}
		}
		itemPedido.cdUnidade = LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido() ? cbUnidade.getValue() : itemPedido.getProduto().cdUnidade;
		if (LavenderePdaConfig.isApresentaQtdPreCarregadaDeVendaNoItemDoPedido() && !pedido.isGondola()) {
			itemPedido.setQtItemFisico(LavenderePdaConfig.apresentaQtdPreCarregadaDeVendaNoItemDoPedido);
		}
	}

	@Override
	protected void changeItemTabelaPreco(String cdTabelaPreco, boolean refreshDomainToScreen) throws SQLException {}

	@Override
	protected void atualizaItemPedidoNaLista(ItemPedido itemPedido) throws SQLException {}

	@Override
	protected void changeItemTabelaPreco(String cdTabelaPreco, boolean refreshDomainToScreen, ItemTabelaPreco itemTabelaPreco) throws SQLException {}

	@Override
	protected void addInfosExtras() throws SQLException {
		if (LavenderePdaConfig.isExibeFotoTelaItemPedido() || fromProdutoPendenteGiroMultInsercao) {
			addFotoProduto();
		}
		UiUtil.add(sessaoTotalizadores, lvQtProdutos, LEFT + listContainer.getTotalizerGap(), CENTER, PREFERRED, PREFERRED);
	}
	
	@Override
	protected boolean isItemPedidoGrade() {
		try {
			setupItemPedidoAsGrade();
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		return super.isItemPedidoGrade();
	}
	
	protected void setupItemPedidoAsGrade() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.naoComparaSeqItem = true;
		
		Produto produto = getProdutoSessaoVenda();
		if (produto != null) {
			itemPedido.cdProduto = produto.cdProduto;
		}
	}
	
	public void gridClickAndRepaintScreen(boolean repaintScreen) throws SQLException {
		if (gridClick()) {
			if (repaintScreen) {
				repaintScreen(true);
			}
			setFocusInQtde();
		} else {
			if (!inVendaUnitariaMode) {
				clearProdutoVendaAtual();
			}
		}
	}

	@Override
	protected void gridClickAndRepaintScreen() throws SQLException {
		if (isModoGrade(true)) {
			ItemPedido itemPedido = getItemPedido();
			int index = findIndexItemPedido(pedido.itemPedidoList, itemPedido);
			if (index >= 0) {
				itemPedido = (ItemPedido)pedido.itemPedidoList.items[index];
				setDomain((ItemPedido)itemPedido.clone());
				fromItemPedidoInseridoPedido = !LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade();
				getItemPedido().dsAgrupadorGradeFilter = itemPedido.getProduto().getDsAgrupadorGrade();
				ItemPedidoService.getInstance().carregaItensAgrupadorGrade(getItemPedido());
			}
			prepareAndLoadImageCarouselSliderByItemPedido(getItemPedido());
			gridClickAndRepaintScreen(true);
			if (fromItemPedidoInseridoPedido) {
				edit(getItemPedido());
			}
		} else {
			gridClickAndRepaintScreen(true);
		}
		openGridEditModeIfNecessary(false);
	}
	
	private int findIndexItemPedido(Vector itemPedidoList, ItemPedido itemPedido) throws SQLException {
		int index = pedido.itemPedidoList.indexOf(itemPedido);
		if (!LavenderePdaConfig.usaGradeProduto5() || index >= 0) return index;
		int size = itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido item = (ItemPedido) itemPedidoList.items[i];
			if (ValueUtil.valueEqualsIfNotNull(item.getProduto().getDsAgrupadorGrade(), itemPedido.getProduto().getDsAgrupadorGrade())) return i;
		}
		return -1;
	}
	
	@Override 
	public void setEnableComponentsPossuemVariacao() throws SQLException { 
		super.setEnableComponentsPossuemVariacao(); 
		edVlVerbaItemPositiva.setVisible(!pedido.isIgnoraControleVerba());
		edVlVerbaItemNegativa.setVisible(!pedido.isIgnoraControleVerba());
		edVlVerbaPedido.setVisible(!pedido.isIgnoraControleVerba());
		edVlBaseFlex.setVisible(!pedido.isIgnoraControleVerba());
		lbVlVerbaItemPositiva.setVisible(!pedido.isIgnoraControleVerba());
		lbVlVerbaItemNegativa.setVisible(!pedido.isIgnoraControleVerba());
		lbVlVerbaPedido.setVisible(!pedido.isIgnoraControleVerba());
		lbVlBaseFlex.setVisible(!pedido.isIgnoraControleVerba());
	}
	
	public void validateProdutoClicado(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorCliente(RestricaoVendaCli.RESTRICAO_PEDIDO_BONIFICACAO)) {
			RestricaoVendaCliService.getInstance().validaBloqueiaVendaProdutoSelecionado(itemPedido.getProduto(), SessionLavenderePda.getCliente());
		}
	}

	private void inicializabtTipoPesquisaEdFiltro() {
		List<String> list = new ArrayList<>();

		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO);
		}
		if (LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO);
		}
		if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO);
		}
		list.add(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO);
		btTipoPesquisaEdFiltro = new PushButtonGroupBase(list.toArray(new String[0]), true, 1, -1, 1, 1, true, PushButtonGroup.NORMAL);
		setaPosicoesHashMapbtTipoPesquisaEdFiltro(list);

		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada) {
			btTipoPesquisaEdFiltro = new PushButtonGroupBase(new String[] {Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_CONTEM, Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_INICIA}, true, 0, -1, 1, 1, true, PushButtonGroup.NORMAL);
		}
		btGroupTipoFiltrosClick(btTipoPesquisaEdFiltro.getSelectedIndex());
	}

	private void setaPosicoesHashMapbtTipoPesquisaEdFiltro (List<String> list){
		 for (int i = 0; i < list.size(); i++) {
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_PA, i);
					continue;
				}
				else if(!posicoesbtTipoPesquisaEdFiltro.containsKey(HASHKEY_PA)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_PA, -1);
				}
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_AP, i);
					continue;
				}
				else if(!posicoesbtTipoPesquisaEdFiltro.containsKey(HASHKEY_PA)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_AP, -1);
				}
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_CD, i);
					continue;
				}
				else if(!posicoesbtTipoPesquisaEdFiltro.containsKey(HASHKEY_CD)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_CD, -1);
				}
				if(list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_DS, i);
					continue;
				}
				else if(!posicoesbtTipoPesquisaEdFiltro.containsKey(HASHKEY_DS)) {
					posicoesbtTipoPesquisaEdFiltro.put(HASHKEY_DS, -1);
				}
			}	
			btTipoPesquisaEdFiltro.setSelectedIndex(list.size());
	}
	
	@Override
	public void onFormExibition() throws SQLException {
		if (!isAddingFromArray) {
			atualizaListaItensPedidoAlteracaoExclusao();
		}
		super.onFormExibition();
	}

	private void btGroupTipoFiltrosClick(int indexSelected) {
		filterByPrincipioAtivo = indexSelected == posicoesbtTipoPesquisaEdFiltro.get(HASHKEY_PA);
		filterByAplicacaoProduto = posicoesbtTipoPesquisaEdFiltro.get(HASHKEY_AP) != null && indexSelected == posicoesbtTipoPesquisaEdFiltro.get(HASHKEY_AP);
		if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			filterByCodigoProduto = indexSelected == posicoesbtTipoPesquisaEdFiltro.get(HASHKEY_CD);
		}
		alterarTipoTeclado();
	}

	@Override
	public void updateVisibilityBtAlert() {
		super.updateVisibilityBtAlert();
		if (btAlert != null) {
			btAlert.setVisible(false);
		}
	}	
	public void prepareItemPedidoFilter(ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && ValueUtil.isNotEmpty(getItemPedido().pedido.nuPedidoRelBonificacao)) {
			produtoFilter.itemPedidoFilter = new ItemPedido();
			produtoFilter.itemPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produtoFilter.itemPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			produtoFilter.itemPedidoFilter.nuPedidoListFilter = getItemPedido().pedido.nuPedidoRelBonificacao.split(";");
		}
	}
	
}
