package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.ValueChooser;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.SugVendaPerson;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainer.CAMPOS_LISTA;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainerControlState;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.sys.Vm;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Vector;

public class ListSugestaoMultProdutosWindow extends WmwListWindow {
	
	private Map<CheckBoolean, SugVendaPerson> checkSugestaoMap;
	private Map<String, SugVendaPerson> sugVendaPersonMap;
	private Vector listSugVendaPerson;
	private Pedido pedido;
	private ButtonPopup btBuscarItem;
	private ButtonPopup btListaItem;
	private ButtonPopup btPedido;
	private CadItemPedidoForm cadItemPedidoForm;
	private BaseButton btFiltroAvancado;
	private boolean listLoaded;
	private CAMPOS_LISTA[] campos;
	private boolean[] statusChecks;
	private FiltroAvancadoSugPersonWindow filtroAvancadoWindow;
	private int imageSize;
	
	private boolean isInserindoItem;
	
	public ListSugestaoMultProdutosWindow(Pedido pedido, Vector listSugVendaPerson) {
		super(Messages.PEDIDO_TITULO_SUGESTAO_VENDA_PERSON);
		this.pedido = pedido;
		btBuscarItem = new ButtonPopup(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_BUSCAR_ITEM);
		btListaItem = new ButtonPopup(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_LISTA_ITEM);
		btPedido = new ButtonPopup(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_PEDIDO);
		btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		this.listSugVendaPerson = listSugVendaPerson;
		constructorListContainer();
		setDefaultRect();
	}
	
	private void constructorListContainer() {
		listContainer = new GridListContainer(1, 1);
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return null;
	}

	private String[] getItem(Object domain, CAMPOS_LISTA[] campos) throws SQLException {
		ItemPedido itemPedido = (ItemPedido)domain;
		ProdutoBase produto = itemPedido.getProduto();
		String[] item = new String[campos.length];
		ProdutoUnidade produtoUnidade = null;
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			produtoUnidade = itemPedido.getProdutoUnidade();
		}
		itemPedido.qtEmbalagemElementar = getQtCaixaPadrao(itemPedido);
		itemPedido.vlEmbalagemElementar = itemPedido.getVlEmbalagemElementar();
		for (int i = 0; i < item.length && campos[i] != null; i++) {
			switch (campos[i]) {
				case CAMPO_FIXO:
					if (i == 0) {
						item[i] = produto.cdProduto;
					} else if (i == 1) {
						item[i] = " - " + StringUtil.getStringValue(produto.dsProduto);
					} else {
						if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
							item[i] = StringUtil.getStringAbreviada(StringUtil.getStringValue(produto.dsPrincipioAtivo), (int)(width * 0.55), listContainer.getFontSubItens());
						}
					}
					break;
				case CAMPO_ESTOQUE:
					item[i] = EstoqueService.getInstance().getEstoqueToString(produto.qtEstoqueProduto) + Messages.PRODUTO_LABEL_EM_ESTOQUE;
					break;
				case CAMPO_VLUN:
					item[i] = Messages.MOEDA + " " + StringUtil.getStringValueToInterface(itemPedido.vlItemPedido);
					break;
				case CAMPO_VLT:
					item[i] = Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(0d);
					break;
				case CAMPO_QE:
					item[i] = Messages.ITEMPEDIDO_LABEL_CAIXAPADRAO + " " + StringUtil.getStringValueToInterface(itemPedido.qtEmbalagemElementar);
					break;
				case CAMPO_VEP:
					item[i] = Messages.ITEMPEDIDO_LABEL_PRECOEMBPRIMARIA + " " + StringUtil.getStringValueToInterface(itemPedido.getVlEmbalagemElementar());
					break;
				case CAMPO_VUE:
					item[i] = getVlVue(itemPedido, produto);
					break;
				case CAMPO_QTHIS:
					double qtMediaHist = produto.qtdMediaHistorico;
					if (produtoUnidade != null) {
						qtMediaHist = produtoUnidade.isMultiplica() ? qtMediaHist * produtoUnidade.nuConversaoUnidade : qtMediaHist / produtoUnidade.nuConversaoUnidade;
					}
					item[i] = Messages.MULTIPLASSUGESTOES_LABEL_QTHIST + " " + StringUtil.getStringValueToInterface(qtMediaHist);
					break;
				case CAMPO_VLHIST:
					double vlMedioHist = produto.vlMedioHistorico;
					if (produtoUnidade != null) {
						vlMedioHist = produtoUnidade.isMultiplica() ? vlMedioHist * produtoUnidade.nuConversaoUnidade : vlMedioHist / produtoUnidade.nuConversaoUnidade;
					}
					item[i] = Messages.MULTIPLASSUGESTOES_LABEL_VLHIST + " " + StringUtil.getStringValueToInterface(vlMedioHist);
			default:
				break;
			}
		}
		return item;
	}

	private String getVlVue(ItemPedido itemPedido, ProdutoBase produto) throws SQLException {
		StringBuffer sb = new StringBuffer(Messages.ITEMPEDIDO_LABEL_VL_UNIDADE_RENTABILIDADE);
		sb.append(" ");
		sb.append(StringUtil.getStringValueToInterface(ItemPedidoService.getInstance().calculaVlUnidadePorEmbalagemWhenProdutoBase(itemPedido, itemPedido.pedido, produto, null)));
		return sb.toString();
	}

	private double getQtCaixaPadrao(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			return itemPedido.qtEmbalagemElementar;
		} else if (LavenderePdaConfig.mostraQtdPorEmbalagemProduto) {
			if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado && pedido.getCliente().isCreditoAntecipado()) {
				return itemPedido.getProduto().nuConversaoUMCreditoAntecipado;
			} else {
				int nuFracao = itemPedido.getProduto().nuFracao <= 0 || !LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto ? 1 : itemPedido.getProduto().nuFracao;
				return itemPedido.getProduto().nuConversaoUnidadesMedida * nuFracao;
			}
		}
		return 0;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return null;
	}

	@Override
	protected BaseDomain getDomainFilter() {
		ProdutoBase filter = LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? new ProdutoTabPreco() : new Produto();
		filter.cdEmpresa = pedido.cdEmpresa;
		filter.cdRepresentante = pedido.cdRepresentante;
		return filter;
	}
	
	private int listY;

	@Override
	protected void onFormStart() throws SQLException {
		addButtonPopup(btBuscarItem);
		addButtonPopup(btListaItem);
		addButtonPopup(btPedido);
		if (!listLoaded) {
			addChecksSugestaoPerson();
		}
		UiUtil.add(this, listContainer, LEFT, listY, FILL, FILL);
	}
	
	@Override
	public void initUI() {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		super.initUI();
		msg.unpop();
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target instanceof BaseButton) {
					if (event.target == btPedido) {
						fecharWindow();
						cadItemPedidoForm.voltarClick();
					} else if (event.target == btListaItem) {
						fecharWindow();
						cadItemPedidoForm.btListaItensClick();
					} else if (event.target == btBuscarItem) {
						fecharWindow();
					} else if (event.target == btFiltroAvancado) {
						btFiltroAvancadoClick();
					}
				} else if (event.target instanceof CheckBoolean) {
					CheckBoolean check = (CheckBoolean) event.target;
					statusChecks[check.appId] = check.isChecked();
					recarregaAoAlterarCheck(checkSugestaoMap);
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (!isInserindoItem && cadItemPedidoForm.isInsereMultiplosSemNegociacao() && event.target != null) {
					Control parent = ((Control) event.target).getParent();
					if (parent instanceof ValueChooser || parent instanceof ItemContainer) {
						if (event.target instanceof EditNumberFrac) {
							BaseListContainer.Item item = null;
							Container parente = ((EditNumberFrac) event.target).getParent();
							if (parente instanceof ItemContainer) {
								ItemContainer ic = (ItemContainer) ((EditNumberFrac) event.target).getParent();
								if (ic.index >= 0) {
									item = (BaseListContainer.Item) listContainer.getContainer(ic.index);
								}
							} else {
								parente = parent.getParent();
								if (parente instanceof ItemContainer) {
									ItemContainer ic = (ItemContainer) parente;
									if (ic.index >= 0) {
										item = (BaseListContainer.Item) listContainer.getContainer(ic.index);
									}
								}
							}
							if (item != null) {
								isInserindoItem = true;
								ItemContainer itemContainer = (ItemContainer) item.rightControl;
								itemContainer.changedValue = event.target == itemContainer.edVlItemPedido;
								ItemContainerControlState itemContainerState = new ItemContainerControlState(itemContainer);
								ItemPedido itemPedido = cadItemPedidoForm.iniciaItemVendaMultInsercao(itemContainerState, itemContainer, false,
										parent == itemContainer.chooserQtd, itemContainer.changedValue, parent == itemContainer.chooserDescAcresc, false);
								cadItemPedidoForm.saveItemMultInsercao(item, itemContainer, itemContainer.itemPedido, false, true, false, true);
								itemContainer.setControlsEnabledByState(itemContainerState);
								itemContainer.setColorsEditsByConversaoUnidade();
								ValueChooser chooserDesc = itemContainer.chooserDescAcresc;
								if (chooserDesc != null && chooserDesc.isValueMaxReached() && !LavenderePdaConfig.isPermiteEditarDescontoAcrescimoMultiplaInsercao()) {
									chooserDesc.disableBtMais();
								}
								isInserindoItem = false;
							}
						}
					}
				}
				break;
			}
		}
	}
	
	@Override
	protected void addBtFechar() {
	}
	
	@Override
	public void list() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		try {
			listContainer.removeAllContainers();
			//--
			listSize = listSugVendaPerson.size();
			Container[] all = new Container[listSize];
			//--
			if (listSize > 0) {
				boolean insereQtdMultOnly = true;
				boolean isLoadImages = LavenderePdaConfig.isMostraFotoProdutoListSugPerson();
				boolean reloadInfosMudancaCondComercial = pedido.cdCondicaoComercialChanged;
				if (isLoadImages) {
					double screenWidth = width;
					double screenHeight = height;
					double systemFactor = VmUtil.isAndroid() || VmUtil.isIOS() ? 1.75 : 1.3;
					if (width < height) {
						imageSize = ValueUtil.getIntegerValue(((screenWidth / screenHeight) * 100) * systemFactor);
					} else {
						imageSize = ValueUtil.getIntegerValue(((screenHeight / screenWidth) * 100) * systemFactor);
					}
					Util.prepareDefaultImage(imageSize);
				}
				ProdutoBase produto;
				ItemPedido itemPedido = null;
				Vector produtoUnidadeList = null;
				BaseListContainer.Item c;
				ItemContainer itemCont = null;
				if (!listLoaded) {
					campos = getCamposExibicaoLista();
					setProperitesOnListContainer(campos);
				}
				ordenaLista(listSugVendaPerson);
				int k = 0;
				for (int i = 0; i < listSize; i++) {
					if (i % 250 == 0) {
						VmUtil.executeGarbageCollector();
					}
					itemPedido = (ItemPedido) listSugVendaPerson.items[i];
					if (reloadInfosMudancaCondComercial) {
						PedidoService.getInstance().loadValorBaseItemPedido(pedido, itemPedido);
					}
					if (itemPedido.flInvisivelSug || isItemOnItemPedidoList(itemPedido)) {
						itemPedido.naoComparaSeqItem = false;
						continue;
					}
					produto = itemPedido.getProduto();
					produtoUnidadeList = itemPedido.unidadesListSugPerson;
					String[] items = getItem(itemPedido, campos);
					all[k] = c = new BaseListContainer.Item(listContainer.getLayout());
					if (insereQtdMultOnly) {
						if (isLoadImages) {
							itemCont = new ItemContainer(items, false, Util.getImageForProdutoList(produto, imageSize, false), campos, -1, produto, listContainer, produtoUnidadeList, itemPedido, k++);
							c.rightControl = itemCont;
							c.setupRightContainer();
						} else {
							itemCont = new ItemContainer(items, false, null, campos, -1, produto, listContainer, produtoUnidadeList, itemPedido, k++);
							c.rightControl = itemCont;
							c.setupRightContainer();
						}
					}
					c.id = produto.getRowKey();
					c.setItens(new String[]{" "});
					c.setToolTip(getToolTip(produto));
					setPropertiesInRowList(c, itemPedido);
					produto = null;
				}
				Container[] cArray = null;
				if (k < listSize) {
					cArray = new Container[k];
					Vm.arrayCopy(all, 0, cArray, 0, k);
					cArray = removeContainersSemUnidadeAlterantiva(cArray);
					listContainer.addContainers(cArray);
				} else {
					all = removeContainersSemUnidadeAlterantiva(all);
					listContainer.addContainers(all);
				}
			}
		} finally {
			pedido.cdCondicaoComercialChanged = false;
			listLoaded = true;
			msg.unpop();
		}
	}
	
	private Container[] removeContainersSemUnidadeAlterantiva(Container[] array) {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			List<Container> newArray = new ArrayList<Container>();
			for (int i = 0; i < array.length; i++) {
				ItemContainer item = (ItemContainer) ((BaseListContainer.Item) array[i]).rightControl;
				if (item.cbUnidadeAlternativa != null && !ValueUtil.isEmpty(item.cbUnidadeAlternativa.getItems())) {
					newArray.add(array[i]);
				}
			}
			return  newArray.toArray(new Container[] {});
		}
		return array;
	}

	@Override
	public void reposition() {
		super.reposition();
		ajustaTamanhoBotoes();
	}
	
	
	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}
	
	private void addChecksSugestaoPerson() throws SQLException {
		List<SugVendaPerson> sugVendaPersonList = pedido.sugVendaPersonList;
		int size = sugVendaPersonList.size();
		int nSugLinha = 1;
		int nuSugestoesTela = LavenderePdaConfig.qtLimiteSugestaoVendaPersExibidasNaTela;
		nuSugestoesTela = nuSugestoesTela > size ? size : nuSugestoesTela;
		checkSugestaoMap = new HashMap<>(size);
		sugVendaPersonMap = new HashMap<>(size);
		int y = getTop() + HEIGHT_GAP;
		int x = LEFT + WIDTH_GAP;
		CheckBoolean check = null;
		int checkWidth = (width - WIDTH_GAP_BIG * 3) / 3; 
		int i = 0;
		int imgSize = new LabelValue().getPreferredHeight();
		statusChecks = new boolean[size];
		for (; i < size; i++, nSugLinha++) {
			SugVendaPerson sugVendaPerson = sugVendaPersonList.get(i);
			check = new CheckBoolean(ValueUtil.isEmpty(sugVendaPerson.dsSugVendaPersonAbrev) ? sugVendaPerson.dsSugVendaPerson : sugVendaPerson.dsSugVendaPersonAbrev);
			check.setChecked(true);
			checkSugestaoMap.put(check, sugVendaPerson);
			check.appId = i;
			statusChecks[i] = true;
			try {
				if (sugVendaPerson.imSugVendaPerson != null) {
					sugVendaPerson.image = UiUtil.getSmoothScaledImage(UiUtil.getImage(sugVendaPerson.imSugVendaPerson), imgSize, imgSize);
				}
			} catch (Throwable e) {
				VmUtil.debug("Não foi possível carregar a imagem da sugestão "  +  sugVendaPerson != null ? sugVendaPerson.dsSugVendaPerson : "");
			}
			sugVendaPersonMap.put(sugVendaPerson.cdSugVendaPerson, sugVendaPerson);
			if (i < nuSugestoesTela) {
				UiUtil.add(this, check, x, y, checkWidth);
				x = check.getX2() + WIDTH_GAP_BIG;
				if (nSugLinha == 3) {
					if (!btFiltroAvancado.isDisplayed() && size > nuSugestoesTela) {
						UiUtil.add(this, btFiltroAvancado, RIGHT - WIDTH_GAP, getTop() + HEIGHT_GAP);
					}
					y = check.getY2() + HEIGHT_GAP;
					x = LEFT + WIDTH_GAP;
					nSugLinha = 0;
				}
				if (check != null) {
					int j = nuSugestoesTela % 3 > 0 ? nuSugestoesTela / 3 + 1 : nuSugestoesTela / 3;
					this.listY = ((check.getHeight() + HEIGHT_GAP) * j) + HEIGHT_GAP;
				}
			}
		}
		ItemContainer.setSugVendaPersonMap(sugVendaPersonMap);
	}
	
	private void btFiltroAvancadoClick() throws SQLException {
		if (filtroAvancadoWindow == null) {
			filtroAvancadoWindow = new FiltroAvancadoSugPersonWindow(pedido.sugVendaPersonList, statusChecks);
		}
		filtroAvancadoWindow.popup();
		if (filtroAvancadoWindow.filtroAplicado) {
			for (CheckBoolean check : checkSugestaoMap.keySet()) {
				check.setChecked(statusChecks[check.appId]);
			}
			filtroAvancadoWindow.filtroAplicado = false;
			recarregaAoAlterarCheck(filtroAvancadoWindow.checkSugestaoMap);
		}
	}

	@Override
	protected String[] getItem(Object domain) throws SQLException {
		return null;
	}
	
	private CAMPOS_LISTA[] getCamposExibicaoLista() {
		CAMPOS_LISTA[] campos = new CAMPOS_LISTA[10];
		// Codigo e descricao
		int i = 0;
		campos[i++] = CAMPOS_LISTA.CAMPO_FIXO;
		campos[i++] = CAMPOS_LISTA.CAMPO_FIXO;
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			campos[i++] = CAMPOS_LISTA.CAMPO_FIXO;
		}
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			campos[i++] = CAMPOS_LISTA.CAMPO_ESTOQUE;
		}
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
			campos[i++] = CAMPOS_LISTA.CAMPO_VLUN;
		}
		int[] infosExtras = LavenderePdaConfig.getDominioInformacoesSugVendaPerson();
		for (int k = 0; k < infosExtras.length && infosExtras[k] != 0; k++) {
			switch (infosExtras[k]) {
				case SugVendaPerson.CAMPO_VLT:
					campos[i++] = CAMPOS_LISTA.CAMPO_VLT;
					break;
				case SugVendaPerson.CAMPO_VEP:
					campos[i++] = CAMPOS_LISTA.CAMPO_VEP;
					break;
				case SugVendaPerson.CAMPO_QE:
					campos[i++] = CAMPOS_LISTA.CAMPO_QE;
					break;
				case SugVendaPerson.CAMPO_VLHIS:
					campos[i++] = CAMPOS_LISTA.CAMPO_VLHIST;
					break;
				case SugVendaPerson.CAMPO_QTDHIS:
					campos[i++] = CAMPOS_LISTA.CAMPO_QTHIS;
					break;
				case SugVendaPerson.CAMPO_VUE:
					campos[i++] = CAMPOS_LISTA.CAMPO_VUE;
					break;
			}
		}
		CAMPOS_LISTA[] camposRetorno = new CAMPOS_LISTA[i];
		return Vm.arrayCopy(campos, 0, camposRetorno, 0, i) ? camposRetorno : campos;
	}

	@Override
	protected void onPopup() {
		pedido.flSugestao = true;
		super.onPopup();
	}

	@Override
	protected void onUnpop() {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage(Messages.SAVE_ITEMS_FROM_ARRAY_MSG);
		try {
			msg.popupNonBlocking();
			filtroAvancadoWindow = null;
			setSortAttributes();
			for (CheckBoolean check : checkSugestaoMap.keySet()) {
				check.setChecked(statusChecks[check.appId] = true);
			}
			int[] itensInseridos = listContainer.getCheckedItens();
			int size = itensInseridos.length;
			ArrayList<ItemPedido> listItensPedido = new ArrayList<>(listContainer.size());
			listItemContainerToListItemPedido(itensInseridos, size, listItensPedido, ValueUtil.isNotEmpty(pedido.itemPedidoList));
			resetaVisibilidadeItens();
			if (ValueUtil.isNotEmpty(listItensPedido)) {
				cadItemPedidoForm.itemPedidoAgrupadorSugPerson = new ItemPedido();
				if (LavenderePdaConfig.usaDescProgressivoPersonalizado && ProdutoService.getInstance().isPossuiFamiliasDescProg(pedido, null)) {
					pedido.recalculoDescontoProgressivoDTO = PedidoService.getInstance().atualizaDescProgressivoPedido(pedido);
				}
				for (ItemPedido itemSugestao : listItensPedido) {
					cadItemPedidoForm.itemPedidoAgrupadorSugPerson.sugPersonVlInserido += itemSugestao.vlTotalItemPedido;
				}
				cadItemPedidoForm.showMessageItensInseridos(true,
						cadItemPedidoForm.itemPedidoAgrupadorSugPerson.sugPersonCreditosGerados,
						cadItemPedidoForm.itemPedidoAgrupadorSugPerson.sugPersonQtItensInseridos,
						cadItemPedidoForm.itemPedidoAgrupadorSugPerson.sugPersonVlInserido);
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(e.getMessage());
		} finally {
			Util.resetImages();
			cadItemPedidoForm.requestFocus();
			pedido.flSugestao = false;
			msg.unpop();
			showPopUpRelDiferencasDescProgressivo(pedido);
		}
	}

	private void listItemContainerToListItemPedido(int[] itensInseridos, int size, ArrayList<ItemPedido> listItensPedido, boolean hasItensInPedido) {
		ItemContainer itemCont;
		for (int i = 0; i < size; i++) {
			itemCont = (ItemContainer)listContainer.getListContainerItemControl(itensInseridos[i]);
			ItemPedido itemPedido = (ItemPedido) itemCont.itemPedido.clone();
			itemPedido.nuSeqItemPedido = !hasItensInPedido ? i + 1 : ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
			listItensPedido.add(itemPedido);
		}
	}

	private void setProperitesOnListContainer(CAMPOS_LISTA[] campos) {
		int length = campos.length;
		List<String[]> itens = new ArrayList<>(length);
		itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_CODIGO, ProdutoBase.SORT_COLUMN_CDPRODUTO});
		itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_DESCRICAO, ProdutoBase.SORT_COLUMN_DSPRODUTO});
		for (int i = 2; i < length; i++) {
			switch (campos[i]) {
			case CAMPO_ESTOQUE: {
				itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_ESTOQUE, ProdutoBase.SORT_COLUMN_ESTOQUE});
				break;
			}
			case CAMPO_VLHIST: {
				itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_VLHIST, ProdutoBase.SORT_COLUMN_VLMEDIOHIST});
				break;
			}
			case CAMPO_QTHIS: {
				itens.add(new String[] {Messages.MULTIPLASSUGESTOES_LABEL_QTHIST, ProdutoBase.SORT_COLUMN_QTDMEDHIST});
			}
			default:
				break;
			}
		}
		length = itens.size();
		String[][] cols =  new String[length][];
		for (int i = 0; i < length; i++) {
			cols[i] = itens.get(i);
		}
		listContainer.setColsSort(cols);
		setSortAttributes();
	}
	
	private void ordenaLista(Vector list) {
		ItemPedido.sortAttr = ProdutoBase.sortAttr = sortAtributte;
		boolean asc = ValueUtil.VALOR_SIM.equals(listContainer.sortAsc);
		if (ProdutoBase.SORT_COLUMN_CDPRODUTO.equals(sortAtributte) || ProdutoBase.SORT_COLUMN_ESTOQUE.equals(sortAtributte) || ProdutoBase.SORT_COLUMN_QTDMEDHIST.equals(sortAtributte)) {
			SortUtil.qsortInt(list.items, 0, list.size() - 1, asc);
		} else if (ProdutoBase.SORT_COLUMN_DSPRODUTO.equals(sortAtributte)) {
			SortUtil.qsortString(list.items, 0, list.size() - 1, asc);
		} else if (ProdutoBase.SORT_COLUMN_VLMEDIOHIST.equals(sortAtributte)) {
			SortUtil.qsortDouble(list.items, 0, list.size() - 1, asc);
		}
	}
	
	protected boolean hasSugestoes() {
		return listContainer != null && listContainer.size() > 0;
	}
	
	private ArrayList<String> getSugestoesUnchecked(Map<CheckBoolean, SugVendaPerson> checkMap) {
		ArrayList<String> checkedItens = new ArrayList<>(checkSugestaoMap.size());
		boolean isChecked = false;
		for (Entry<CheckBoolean, SugVendaPerson> entryCheck : checkMap.entrySet()) {
			isChecked = entryCheck.getKey().isChecked();
			if (!isChecked) {
				checkedItens.add(entryCheck.getValue().cdSugVendaPerson);
			}
			SugVendaPerson sugVendaPerson = null;
			if ((sugVendaPerson = sugVendaPersonMap.get(entryCheck.getValue().cdSugVendaPerson)) != null) {
				sugVendaPerson.unchecked = !isChecked;
			}
		}
		return checkedItens;
	}
	
	private boolean isItemOnItemPedidoList(ItemPedido itemPedido) {
		itemPedido.naoComparaSeqItem = true;
		return pedido.itemPedidoList.indexOf(itemPedido) > -1;
	}
	
	private void recarregaAoAlterarCheck(Map<CheckBoolean, SugVendaPerson> checkMap) throws SQLException {
		ArrayList<String> sugestoesUnchecked = getSugestoesUnchecked(checkMap);
		ItemPedido item;
		int size = listSugVendaPerson.size();
		for (int i = 0; i < size; i++) {
			item = (ItemPedido)listSugVendaPerson.items[i];
			item.flInvisivelSug = sugestoesUnchecked.containsAll(item.getProduto().cdsSugVendaPerson);
			if (item.flInvisivelSug) {
				resetaDadosItem(item);
			}
		}
		list();
	}

	private void resetaDadosItem(ItemPedido item) throws SQLException {
		ProdutoUnidade produtoUnidade = (ProdutoUnidade)item.unidadesListSugPerson.items[0];
		if (produtoUnidade != null && !ValueUtil.valueEquals(produtoUnidade.cdUnidade, item.cdUnidade)) {
			item.setQtItemFisico(0);
			ItemPedidoService.getInstance().changeUnidadeAlternativa(item, produtoUnidade.cdUnidade);
		} else if (item.getQtItemFisico() > 0) {
			item.setQtItemFisico(0);
			item.vlTotalItemPedido = 0;
		}
	}
	
	private void setSortAttributes() {
		sortAtributte = SugVendaPerson.getParamSortAttr();
		listContainer.atributteSortSelected = sortAtributte;
		sortAsc = listContainer.sortAsc = !SugVendaPerson.ORDEMDESC.equals(LavenderePdaConfig.sentidoOrdenacaoListaSugPerson) ? ValueUtil.VALOR_SIM : ValueUtil.VALOR_NAO;
		listContainer.atualizaTamanhoComponentesBarraSuperior();
	}
	
	private void resetaVisibilidadeItens() throws SQLException {
		int size = listSugVendaPerson.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido)listSugVendaPerson.items[i]; 
			itemPedido.flInvisivelSug = false;
			resetaDadosItem(itemPedido);
		}
		for (SugVendaPerson sugVendaPerson : sugVendaPersonMap.values()) {
			sugVendaPerson.unchecked = false;
		}
	}
	
	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		ItemPedido item = (ItemPedido) domain;
		// PRODUTOS SEM ESTOQUE EM VERMELHO
		if (item.getProduto().qtEstoqueProduto <= 0) {
			if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !item.getProduto().isIgnoraValidacao()) {
				c.setBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK);
			}
		}
	}
	private void showPopUpRelDiferencasDescProgressivo(Pedido pedido) {
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			if (pedido.recalculoDescontoProgressivoDTO != null && ValueUtil.isNotEmpty(pedido.recalculoDescontoProgressivoDTO.listItemDescontoDTO)) {
				new RelDiferencasDescontoProgressivoWindow(pedido.recalculoDescontoProgressivoDTO).popup();
			}
		}
	}
}
