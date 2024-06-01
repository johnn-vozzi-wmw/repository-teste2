package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaGrupo;
import br.com.wmw.lavenderepda.business.domain.SugestaoVendaProd;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.GrupoCliPermProdService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaGrupoService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaProdService;
import br.com.wmw.lavenderepda.business.service.SugestaoVendaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.SugestaoVendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sys.Convert;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.ui.event.TimerEvent;
import totalcross.util.Vector;

public class ListProdutoSugestaoComQtdeWindow extends WmwListWindow {

	private LabelName lbDescricao;
	private LabelValue lbMsgFechamento;
	public Pedido pedido;
	private CadItemPedidoForm cadItemPedidoForm;
	public boolean onFechamentoPedido;
	public boolean onAcessoManual;
	public boolean onChoosingProdutoGrupo;
	public SugestaoVendaGrupo lastSugestaoVendaGrupo;
	private ButtonPopup btCancelar;
	private ButtonPopup btFecharPedido;
	protected GridListContainer listContainerGrupo;
	private SugestaoVendaComboBox cbSugestaoVenda;
	private Vector estoqueCache;
	private SessionContainer scItensInserids;
	private LabelTotalizador lbItensInseridos;
	private LabelTotalizador lbPctItensInseridos;
	private boolean permiteFecharPedido = true;

	public ListProdutoSugestaoComQtdeWindow(Pedido pedido, boolean onFechamentoPedido) throws SQLException {
		super(Messages.PEDIDO_LABEL_SUGESTAO_VENDAS_COM_QTDE);
		this.pedido = pedido;
		this.onFechamentoPedido = onFechamentoPedido;
		singleClickOn = true;
		lbDescricao = new LabelName(Messages.SUGESTAO_VENDAS_DESCRICAO);
		btCancelar = new ButtonPopup(FrameworkMessages.BOTAO_CANCELAR);
		btFecharPedido = new ButtonPopup(Messages.BOTAO_FECHAR_PEDIDO);
		lbMsgFechamento = new LabelValue("", CENTER);
		cbSugestaoVenda = new SugestaoVendaComboBox(this.pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, 0, onFechamentoPedido);
		cbSugestaoVenda.setSelectedIndex(0);
		scItensInserids = new SessionContainer();
		scItensInserids.setBackColor(ColorUtil.componentsBackColor);
		lbItensInseridos = new LabelTotalizador(Messages.SUGESTAO_ITENS_INSERIDOS);
		lbPctItensInseridos = new LabelTotalizador("");
		estoqueCache = new Vector();
		//--
        constructorListContainer();
		setDefaultRect();
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return SugestaoVendaProdService.getInstance();
	}

    private void constructorListContainer() {
    	int itemCount = 3;
    	itemCount += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
        itemCount += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
    	itemCount += LavenderePdaConfig.mostraColunaMarcaNaSugestaoDeProdutos() ? 1 : 0;
    	itemCount += LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 1 : 0;
		listContainer = new GridListContainer(itemCount, 2);
    	listContainer.setUseSortMenu(false);
    	listContainer.setTotalizerQtdeVisible(false);
    	setPositionsListContainer(itemCount);
    	listContainer.setTotalizerQtdeVisible(false);
    	listContainer.setTitle(Messages.MENU_OPCAO_PRODUTO);
    	listContainerGrupo = new GridListContainer(4, 2);
    	listContainerGrupo.setUseSortMenu(false);
    	listContainerGrupo.setColPosition(3, RIGHT);
    	listContainerGrupo.setTotalizerQtdeVisible(false);
    	listContainerGrupo.setTitle(Messages.GRUPOPRODUTO_GRUPOS_PRODUTOS);
    }

	private void setPositionsListContainer(int itemCount) {
		for (int i = itemCount-1; i <= itemCount; i--) {
    		if (i % 2 > 0) {
    			listContainer.setColPosition(i, RIGHT);
    		} else if (i == 0) {
    			break;
    		}
    	}
	}

    public void loadComboSugestaoVendasAndSelectFirst() throws SQLException {
    	cbSugestaoVenda.load(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, onFechamentoPedido);
    	cbSugestaoVenda.setSelectedIndex(0);
		onChoosingProdutoGrupo = false;
    }

    protected BaseDomain getDomainFilter() {
    	return new SugestaoVendaGrupo();
    }

	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		SugestaoVenda sugestaoVenda = (SugestaoVenda)cbSugestaoVenda.getSelectedItem();
		if (sugestaoVenda != null) {
			Vector sugestaoVendaProdList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdComQtdPendenteNoPedido(sugestaoVenda, pedido);
			if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd)) {
				sugestaoVendaProdList = GrupoCliPermProdService.getInstance().restringeSugestaoVendaProdByGrupoCliPermProd(sugestaoVendaProdList);
			}
			if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
				sugestaoVendaProdList = ProdutoService.getInstance().filtraSugestaoVendaPorTipoPedido(pedido, sugestaoVendaProdList);
			}
			if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
				ProdutoService.getInstance().filtraSugestaoVendaPorProdutoClienteExclusivo(pedido, sugestaoVendaProdList);
			}
			if (LavenderePdaConfig.filtraClientePorProdutoRepresentante) {
				ProdutoService.getInstance().filtraSugestaoVendaPorClienteProdutoExclusivo(pedido, sugestaoVendaProdList);
			}
			if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
				ProdutoService.getInstance().filtraSugestaoVendaPorProdutoCondPagtoExclusivo(pedido, sugestaoVendaProdList);
			}
			SugestaoVendaProdService.getInstance().adicionaPrecoNaSugestaoVendaProduto(sugestaoVendaProdList, pedido);
			SortUtil.qsortInt(sugestaoVendaProdList.items, 0, sugestaoVendaProdList.size()-1, false);
			return sugestaoVendaProdList;
		}
		return new Vector(0);
	}

	protected Vector getDomainListGrupo() throws SQLException {
		SugestaoVenda sugestaoVenda = (SugestaoVenda)cbSugestaoVenda.getSelectedItem();
		if (sugestaoVenda != null) {
			return SugestaoVendaGrupoService.getInstance().findAllSugestaoVendaGrupoComQtdPendenteNoPedido(sugestaoVenda, pedido);
		}
		return new Vector(0);
	}

	protected String[] getItem(Object domain) throws java.sql.SQLException {
		SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd)domain;
		int sizeVector = 3;
		sizeVector += LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() ? 1 : 0;
		sizeVector += LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? 1 : 0;
		sizeVector += LavenderePdaConfig.mostraColunaMarcaNaSugestaoDeProdutos() ? 1 : 0;
		sizeVector += LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? 1 : 0;
		Vector itens = new Vector(sizeVector);
		String dsReferencia = LavenderePdaConfig.isMostraDescricaoReferencia() ? "[" + ProdutoService.getInstance().getDsReferencia(sugestaoVendaProd.cdProduto) + "]" : "";
		dsReferencia = ValueUtil.isNotEmpty(dsReferencia) && LavenderePdaConfig.isMostraDescricaoReferenciaAposDsProduto() ? " - " + dsReferencia : dsReferencia;
		if (LavenderePdaConfig.usaDescricaoCodigoNaVisualizacaoEntidades) {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement(dsReferencia + " - " + ProdutoService.getInstance().getProduto(sugestaoVendaProd.cdProduto).toString());
			} else {
				itens.addElement(ProdutoService.getInstance().getProduto(sugestaoVendaProd.cdProduto).toString());
			}
		} else {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement(dsReferencia + " - " + sugestaoVendaProd.cdProduto + " - ");
			} else {
				itens.addElement(sugestaoVendaProd.cdProduto + " - " + ProdutoService.getInstance().getDsProduto(sugestaoVendaProd.cdProduto) + dsReferencia);
			}
		}
		itens.addElement("");
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(ProdutoService.getInstance().getDsPrincipioAtivo(sugestaoVendaProd.cdProduto)), (int)(width * 0.6), listContainer.getFontSubItens()));
		}
		itens.addElement("Unidades " + StringUtil.getStringValueToInterface(sugestaoVendaProd.qtVendida) + "/" + StringUtil.getStringValueToInterface(sugestaoVendaProd.qtUnidadesVenda));
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() || LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			if (sugestaoVendaProd.produto != null) {
				if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
					itens.addElement(Messages.MOEDA + " " + StringUtil.getStringValue(sugestaoVendaProd.vlPrecoProduto));
				}
				if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
					itens.addElement(EstoqueService.getInstance().getEstoqueToString(getEstoqueProduto(sugestaoVendaProd.produto)));
				}
			}
		}
		if (LavenderePdaConfig.mostraColunaMarcaNaSugestaoDeProdutos()) {
			itens.addElement((ValueUtil.isNotEmpty(sugestaoVendaProd.produto.dsMarca)) ? sugestaoVendaProd.produto.dsMarca : "");
		}
		sugestaoVendaProd.dsTooltip = itens.elementAt(0).toString();
		return (String[]) itens.toObjectArray();
	}
	
	//@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) domain;
		Produto produto = new Produto();
		produto.cdProduto = sugestaoVendaProd.cdProduto;
		if (sugestaoVendaProd.vlPrecoProduto == 0) {
			c.setBackColor(LavendereColorUtil.COR_GRID_PRODUTO_SEM_PRECO_SUGESTAO_VENDA);
		}
		// PRODUTOS SEM ESTOQUE EM VERMELHO
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido()) {
			if (getEstoqueProduto(produto) <= 0) {
				c.setBackColor(LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK);
			}
		}
	}

	protected String[] getItemGrupo(Object domain) throws SQLException {
		SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo)domain;
		Vector itens = new Vector(0);
		itens.addElement("");
		itens.addElement(sugestaoVendaGrupo.getDsSugestaoVendaGrupo());
		itens.addElement("Mix " + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtMixVendida) + "/" + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtMixProdutosVenda));
		itens.addElement("Unidades " + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtVendida) + "/" + StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtUnidadesVenda));
		itens.addElement(StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtMixVendida));
		itens.addElement(StringUtil.getStringValueToInterface(sugestaoVendaGrupo.qtVendida));
		return (String[]) itens.toObjectArray();
	}

	private String getToolTipGrupo(Object domain) throws SQLException {
		SugestaoVendaGrupo sugestaoVendaProd = (SugestaoVendaGrupo)domain;
		return sugestaoVendaProd.getDsSugestaoVendaGrupo();
	}

    protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
    }

    private Produto getSelectedProduto() throws SQLException {
    	SugestaoVendaProd sugestaoVendaProd = (SugestaoVendaProd) SugestaoVendaProdService.getInstance().findByRowKey(listContainer.getSelectedId());
    	sugestaoVendaProd.produto = ProdutoService.getInstance().getProduto(sugestaoVendaProd.cdProduto);
    	sugestaoVendaProd.vlPrecoProduto = getPrecoProduto(sugestaoVendaProd.produto);
    	if (sugestaoVendaProd.vlPrecoProduto== 0) {
			throw new ValidationException(Messages.PRODUTO_SEM_PRECO);
		}
    	return sugestaoVendaProd.produto;
    }
    
    private double getEstoqueProduto(Produto produto) throws SQLException {
		for (int i = 0; i < estoqueCache.size(); i++) {
			Estoque est = (Estoque) estoqueCache.items[i];
			if (ValueUtil.isNotEmpty(produto.cdProduto) && produto.cdProduto.equals(est.cdProduto)) {
				return est.qtEstoque;
			}
		}
		Estoque estoque = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
		if (ValueUtil.isEmpty(estoque.cdProduto)) {
			Estoque estoqueVazio = new Estoque();
			estoqueVazio.cdProduto = produto.cdProduto;
			estoqueVazio.qtEstoque = 0;
			estoqueCache.addElement(estoqueVazio);
		} else {
			estoqueCache.addElement(estoque);
		}
		return estoque.qtEstoque;
	}
    
    private double getPrecoProduto(Produto produto) throws SQLException {
		return ItemTabelaPrecoService.getInstance().getPrecoProduto(produto, pedido);
	}

    private SugestaoVendaGrupo getSelectedSugestaoVendaGrupo() throws SQLException {
    	SugestaoVendaGrupo sugestaoVendaGrupo = (SugestaoVendaGrupo) SugestaoVendaGrupoService.getInstance().findByRowKey(listContainerGrupo.getSelectedId());
    	if (sugestaoVendaGrupo != null) {
    		sugestaoVendaGrupo.qtMixVendida = ValueUtil.getIntegerValue(ValueUtil.removeThousandSeparator(listContainerGrupo.getValueFromContainer(listContainerGrupo.getSelectedIndex(), 4)));
    		sugestaoVendaGrupo.qtVendida = ValueUtil.getIntegerValue(ValueUtil.removeThousandSeparator(listContainerGrupo.getValueFromContainer(listContainerGrupo.getSelectedIndex(), 5)));
    	}
    	return sugestaoVendaGrupo;
    }

	protected void onFormStart() {
		if (onFechamentoPedido) {
			UiUtil.add(this, lbMsgFechamento, LEFT, getNextY());
		}
		UiUtil.add(this, lbDescricao, cbSugestaoVenda, getLeft(), getNextY());
		UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, height / 3 - HEIGHT_GAP);
		UiUtil.add(this, listContainerGrupo, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, height / 3 - HEIGHT_GAP);
		UiUtil.add(this, scItensInserids, LEFT, BOTTOM + 1, FILL, PREFERRED);
		UiUtil.add(scItensInserids, lbItensInseridos, LEFT + UiUtil.getTotalizerGap(),  CENTER, PREFERRED, PREFERRED);
		UiUtil.add(scItensInserids, lbPctItensInseridos, AFTER + HEIGHT_GAP, SAME, FILL, FILL);
		if (onFechamentoPedido) {
			addButtonPopup(btFecharPedido);
			addButtonPopup(btCancelar);
		}
		ajustaComponents();
	}

	public void list(Pedido pedidoRef) throws SQLException {
		this.pedido = pedidoRef;
		list();
	}

    public void list() throws java.sql.SQLException {
    	super.list();
    	if (listContainerGrupo != null) {
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			int listSize = 0;
			Vector domainList = null;
			try {
				listContainerGrupo.removeAllContainers();
				//--
				domainList = getDomainListGrupo();
				listSize = domainList.size();
				Container[] all = new Container[listSize];
				//--
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					for (int i = 0; i < listSize; i++) {
				        all[i] = c = new BaseListContainer.Item(listContainerGrupo.getLayout());
				        domain = (BaseDomain)domainList.items[i];
				        c.id = domain.getRowKey();
				        c.setItens(getItemGrupo(domain));
				        c.setToolTip(getToolTipGrupo(domain));
				        domain = null;
					}
					listContainerGrupo.addContainers(all);
				}
			} finally {
				domainList = null;
				msg.unpop();
			}
		}
		lbPctItensInseridos.setValue(calculaPctItensInseridos());
    }
    
	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
    	if (domain.getClass() == SugestaoVendaProd.class) {
    		SugestaoVendaProd prod = (SugestaoVendaProd) domain;
        	return  prod.dsTooltip;
    	}else {
    		return super.getToolTip(domain);
    	}
    }
	
	private String calculaPctItensInseridos() throws SQLException {
		double pctInseridos = 0;
		Vector sugestaoProdutosList = new Vector();
		if (isComboAllSelected()) {
			sugestaoProdutosList = SugestaoVendaProdService.getInstance().findAllSugestaoVendaProdBySugestaoVendaList(cbSugestaoVenda.getSugestoesVendaComQtde());
		}
		if (LavenderePdaConfig.filtraProdutoPorGrupoCliente && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdGrupoPermProd)) {
			sugestaoProdutosList = GrupoCliPermProdService.getInstance().restringeSugestaoVendaProdByGrupoCliPermProd(sugestaoProdutosList);
		}
		SugestaoVendaProdService.getInstance().removeProdutosSemPreco(sugestaoProdutosList, pedido);
		double totalProdutos = sugestaoProdutosList.size();
		double produtosInseridos = totalProdutos - listContainer.size();
		if (totalProdutos > 0) {
			pctInseridos = (produtosInseridos / totalProdutos) * 100;
		}
		return StringUtil.getStringValue(ValueUtil.getIntegerValue(produtosInseridos)) + "/" + ValueUtil.getIntegerValue(totalProdutos) + " (" + StringUtil.getStringValueToInterface(pctInseridos, 2) + "%)";
	}

    public boolean isComboAllSelected() {
    	return cbSugestaoVenda.getSelectedIndex() == 0;
    }

	private void ajustaComponents() {
		int yTop = getTop() + HEIGHT_GAP;
		if (onFechamentoPedido) {
			lbMsgFechamento.setText(Convert.insertLineBreak(width - 6, lbMsgFechamento.fm, Messages.SUGESTAO_MSG_FECHAMENTO_PEDIDO));
			lbMsgFechamento.setRect(CENTER, yTop, width, PREFERRED);
			yTop = AFTER + HEIGHT_GAP;
		}
		lbDescricao.setRect(LEFT + WIDTH_GAP, yTop, PREFERRED, UiUtil.getControlPreferredHeight());
		cbSugestaoVenda.setRect(AFTER + WIDTH_GAP_BIG, SAME, FILL - 2, UiUtil.getControlPreferredHeight());
		listContainer.reposition();
		listContainerGrupo.reposition();
	}
	
	public void reposition() {
		super.reposition();
		ajustaComponents();
	}

	protected void addBtFechar() {
		if (!onFechamentoPedido) {
			super.addBtFechar();
		}
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btCancelar) {
					fecharWindow();
				} else if (event.target == btFecharPedido) {
					permiteFecharPedido = true;
					if (SugestaoVendaService.getInstance().isHasSugestoesPendentesNoPedido(pedido, SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, true, true)) {
						if (LavenderePdaConfig.liberaSenhaSugestaoVendaObrigatoria) {
							permiteFecharPedido = PedidoUiUtil.showPopupLiberacaoSenhaSugestaoVendaObrigatoria(pedido, cbSugestaoVenda.getSugestoesVendaComQtde(), SugestaoVenda.FLTIPOSUGESTAOVENDA_COMQUANTIDADE, false);
						} else {
							throw new ValidationException(Messages.VALIDACAO_FECHAR_PEDIDO_SUGESTAO_VENDA_OBRIGATORIA);
						}
					}
					if (permiteFecharPedido) {
						fecharWindow();
						if (MainLavenderePda.getInstance().getActualForm() instanceof CadPedidoForm) {
							CadPedidoForm cadPedidoForm = (CadPedidoForm)MainLavenderePda.getInstance().getActualForm();
							cadPedidoForm.getPedido().ignoraValidacaoSugestaoProdutosComQtde = true;
							cadPedidoForm.showMessageConfirmClosePedido = false;
							cadPedidoForm.fecharPedido(false);
						}
					}
				} else if (event.target == cbSugestaoVenda) {
					list();
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (listContainerGrupo.isEventoClickUnicoDisparado())) {
					listContainerGrupo.getBaseListContainer().dispararAcaoClickUnico = false;
					detalhesGrupoClick();
				}
				break;
			}
			case TimerEvent.TRIGGERED: {
				if (event.target == this) {
					removeTimer((TimerEvent)event);
					if (onChoosingProdutoGrupo && lastSugestaoVendaGrupo != null) {
						int size = listContainerGrupo.size();
						for (int i = 0; i < size; i++) {
							if (lastSugestaoVendaGrupo.getRowKey().equals(listContainerGrupo.getId(i))) {
								listContainerGrupo.setSelectedIndex(i);
								break;
							}
						}
						detalhesGrupoClick();
					}
				}
				break;
			}
		}
	}

	public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}

	public void detalhesClick() throws SQLException {
		detalhesClick(getSelectedProduto());
	}

	public void detalhesClick(Produto produto) throws SQLException {
		if (ValueUtil.isEmpty(produto.dsProduto)) {
			UiUtil.showErrorMessage(Messages.SUGESTAO_MSG_PRODUTO_NAO_ENCONTRADO);
			return;
		}
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(pedido, produto)) {
			UiUtil.showWarnMessage(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
			return;
		}
		ItemPedido itemPedido = null;
		for (int j = 0; j < pedido.itemPedidoList.size(); j++) {
			if (produto.cdProduto.equals(((ItemPedido)pedido.itemPedidoList.items[j]).cdProduto)) {
				itemPedido = (ItemPedido)pedido.itemPedidoList.items[j];
			}
		}
		if (itemPedido != null) {
			cadItemPedidoForm.edit(ItemPedidoService.getInstance().findByRowKey(itemPedido.getRowKey()));
			unpop();
			cadItemPedidoForm.onFormShow();
			cadItemPedidoForm.fromWindowSugestaoVendaComQtde = true;
		} else {
			cadItemPedidoForm.produtoSelecionado = produto;
			unpop();
			cadItemPedidoForm.gridClickAndRepaintScreen();
			cadItemPedidoForm.fromWindowSugestaoVendaComQtde = true;
			cadItemPedidoForm.setFocusInQtde();
		}
		cadItemPedidoForm.getItemPedido().cdSugestaoVenda = cbSugestaoVenda.getValue();
	}

	private void detalhesGrupoClick() throws SQLException {
		SugestaoVendaGrupo sugestaoVendaGrupo = getSelectedSugestaoVendaGrupo();
		if (sugestaoVendaGrupo != null) {
			SugestaoVenda sugestaoVenda = (SugestaoVenda)cbSugestaoVenda.getSelectedItem();
			ListProdutoSugestaoGrupoComQtdeWindow listProdutoSugestaoGrupoComQtdeWindow = new ListProdutoSugestaoGrupoComQtdeWindow(pedido, sugestaoVendaGrupo, sugestaoVenda);
			listProdutoSugestaoGrupoComQtdeWindow.selectedProduto = null;
			listProdutoSugestaoGrupoComQtdeWindow.popup();
			if (listProdutoSugestaoGrupoComQtdeWindow.selectedProduto != null) {
				if (getPrecoProduto(listProdutoSugestaoGrupoComQtdeWindow.selectedProduto)== 0) {
					throw new ValidationException(Messages.PRODUTO_SEM_PRECO);
				}
				onChoosingProdutoGrupo = true;
				this.lastSugestaoVendaGrupo = sugestaoVendaGrupo;
				detalhesClick(listProdutoSugestaoGrupoComQtdeWindow.selectedProduto);
			} else {
				onChoosingProdutoGrupo = false;
				this.lastSugestaoVendaGrupo = null;
			}
		}
	}

	protected void fecharWindow() throws SQLException {
		super.fecharWindow();
		if (onFechamentoPedido) {
			cadItemPedidoForm.voltarClick();
		}
	}

	public boolean hasSugestaoVenda() {
		return listContainer.size() > 0 || listContainerGrupo.size() > 0;
	}

	protected void onUnpop() {
	}

	public void popup() {
		addTimer(100);
		super.popup();
	}

}
