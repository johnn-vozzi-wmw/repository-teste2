package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ClassUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.FotoItemTroca;
import br.com.wmw.lavenderepda.business.domain.GrupoProdTipoPed;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.AreaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.FotoItemTrocaService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import totalcross.sys.Settings;
import totalcross.sys.SpecialKeys;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class CadItemTrocaForm extends BaseCrudCadForm {

	public boolean flFromListTroca;
	public static boolean flFocoQtFaturamento = LavenderePdaConfig.ocultaQtItemFaturamento ? false : true;

	private Pedido pedido;
	private CadPedidoForm cadPedidoForm;
	private boolean flListInicialized;
	private String dsFiltro;
	public boolean useDataSource;

    private PushButtonGroupBase numericPad;
    private BaseEdit edBaseNumpad;
	//--
	private ButtonOptions btMenu;
	private EditFiltro edFiltro;
	private ButtonAction btCalcular;
	private LabelName lbQtItemFisico;
	private LabelName lbQtItemFaturamento;
	private EditText edDsCliente;
    private EditMemo edDsProduto;
    private BaseToolTip ttDsProduto;
    private TabelaPrecoComboBox cbTabelaPreco;
    private EditNumberFrac edQtItemFisico;
    private EditNumberFrac edQtItemFaturamento;
    private EditNumberFrac edVlItemPedido;
    private EditNumberFrac edVlTotalItemPedido;
    private EditNumberFrac edVlTotalPedido;
	private LabelName lbVlItemPedido;
	private LabelName lbVlTotalItemPedido;
	private LabelName lbVlTotalPedido;
	private LabelName lbTabelaPrecoAbrev;

	private StringBuffer strBuffer;

	private String menuMotivoTroca = "";

	private boolean inVendaUnitariaMode;
	private String tipoItemList;
	public Hashtable hashEditsTemp;
	public Hashtable hashLabelsTemp;
	public Hashtable hashEdits;
	public Hashtable hashLabels;
	
	private String sortAtributte;
	private String sortAsc;
	protected GridListContainer listContainer;
	private ButtonAction btListaItens;
	private boolean inRelatorioMode;
	private boolean inOnlyConsultaItens;
	private boolean houveAlteracaoCampos;

    public CadItemTrocaForm(CadPedidoForm cadPedidoForm, Pedido pedido, String tipoItemList) throws SQLException {
        super("");
        title = (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(tipoItemList)) ? Messages.TROCA_ENTREGAR_NOME_ENTIDADE : Messages.TROCA_RECOLHER_NOME_ENTIDADE;
        this.cadPedidoForm = cadPedidoForm;
        this.pedido = pedido;
        this.tipoItemList = tipoItemList;
        dsFiltro = "";
        //-
        btMenu = new ButtonOptions();
        //--
		btSalvar = new ButtonAction(FrameworkMessages.BOTAO_OK, "images/ok.png");
		btSalvar.setID("btSalvar");
		btSalvarNovo = new ButtonAction(FrameworkMessages.BOTAO_OK, "images/ok.png");
		btSalvarNovo.setID("btSalvarNovo");
		btCalcular = new ButtonAction(Messages.BOTAO_CALCULAR, "images/calcular.png");
        edFiltro = new EditFiltro("9999999999", 100);
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
        edFiltro.autoSelect = true;
        edFiltro.setID("edFiltro");
        //--
        edDsCliente = new EditText("@@@@@@@@@@", 100);
        edQtItemFisico = new EditNumberFrac("9999999999", 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        edQtItemFisico.autoSelect = true;
        edQtItemFisico.setID("edQtItemFisico");
        edQtItemFaturamento = new EditNumberFrac("9999999999", 9, LavenderePdaConfig.isUsaQtdItemPedidoFaturamentoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
        edQtItemFaturamento.autoSelect = true;
        edVlItemPedido = new EditNumberFrac("9999999999", 9);
        edVlItemPedido.autoSelect = true;
        edVlItemPedido.setID("edVlItemPedido");
        edVlTotalItemPedido = new EditNumberFrac("9999999999", 9);
        edVlTotalItemPedido.setID("edVlTotalItemPedido");
        edDsProduto = new EditMemo("@@@@@@@@@@", 1, 100);
    	edDsProduto.justify = true;
    	edDsProduto.drawDots = false;
    	edDsProduto.setID("edDsProduto");
    	//--
    	strBuffer  = new StringBuffer();
    	ttDsProduto = new BaseToolTip(edDsProduto, MessageUtil.quebraLinhas(edDsProduto.getValue()).toString());
        edVlTotalPedido = new EditNumberFrac("9999999999", 9);
        edVlTotalPedido.setID("edVlTotalPedido");
        cbTabelaPreco = new TabelaPrecoComboBox();
        cbTabelaPreco.carregaTabelaPrecoTroca(pedido.getCliente());
        cbTabelaPreco.setID("cbTabelaPreco");
    	lbVlItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, RIGHT);
    	new BaseToolTip(lbVlItemPedido, Messages.TOOLTIP_LABEL_VLTOTALITEM);
    	lbVlTotalItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTALITEMPEDIDO, RIGHT);
    	new BaseToolTip(lbVlTotalItemPedido, Messages.TOOLTIP_LABEL_VLTOTALITEM);
    	lbVlTotalPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTALPEDIDO, RIGHT);
    	new BaseToolTip(lbVlTotalPedido, Messages.TOOLTIP_LABEL_VLTOTALPEDIDO);
    	lbTabelaPrecoAbrev = new LabelName(Messages.TABELAPRECO_NOME_ENTIDADE);
    	if (LavenderePdaConfig.usaConversaoUnidadesMedida) {
            lbQtItemFisico = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICOUN, RIGHT);
        } else {
            lbQtItemFisico = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, RIGHT);
        }
    	new BaseToolTip(lbQtItemFisico, Messages.TOOLTIP_LABEL_QUANTIDADE_UNI);
    	lbQtItemFaturamento = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, RIGHT);
    	new BaseToolTip(lbQtItemFaturamento, Messages.TOOLTIP_LABEL_QUANTIDADE_EMB);
    	//--
		numericPad = new PushButtonGroupBase(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", StringUtil.getStringValue(Settings.decimalSeparator), "<" }, false, -1, -1, UiUtil.defaultGap * 2, 2, true, PushButtonGroup.NORMAL);
		numericPad.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.componentsForeColor);
		numericPad.borderLarge = true;
		numericPad.borderColor = ColorUtil.componentsBorderColor;
		numericPad.setFocusLess(true);
    	//--
    	populateHashsTemporarias();
    	hashEdits = new Hashtable(30);
    	hashLabels = new Hashtable(30);
    	
    	getListConfig();
    	
    	constructorListContainer();
		btListaItens = new ButtonAction(Messages.BOTAO_ITENS_DO_PEDIDO, "images/list.png");
	    btListaItens.setID("btListaItens");
    }

    private ItemPedido getItemPedido() throws SQLException {
    	return (ItemPedido)getDomain();
    }

    //-----------------------------------------------

    //@Override
    protected String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return getItemPedidoService();
    }

    private ItemPedidoService getItemPedidoService() {
    	return ItemPedidoService.getInstance();
    }

    private PedidoService getPedidoService() {
    	return PedidoService.getInstance();
    }

    //@Override
    protected BaseDomain createDomain() throws SQLException {
        return new ItemPedido();
    }

    public void add() throws SQLException {
    	super.add();
    	//--
    	ItemPedido itemPedido = (ItemPedido) getDomain();
    	itemPedido.pedido = pedido;
    	itemPedido.cdEmpresa = pedido.cdEmpresa;
    	itemPedido.cdRepresentante = pedido.cdRepresentante;
    	itemPedido.flOrigemPedido = pedido.flOrigemPedido;
    	itemPedido.nuPedido = pedido.nuPedido;
    	itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
    	itemPedido.flTipoItemPedido = tipoItemList;
    	itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
    	itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
    	itemPedido.nuSeqItemPedido = getItemPedidoService().getNextNuSeqItemPedido(pedido);
    	addItensOnButtonMenu();
    }

    public void edit(BaseDomain domain) throws SQLException {
    	ItemPedido itemPedido = (ItemPedido) domain;
    	if (itemPedido.pedido == null) {
			itemPedido.pedido = pedido;
		}
    	itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
    	itemPedido.getProduto();
    	super.edit(itemPedido);
    	itemPedido.getItemTabelaPreco();
    	addItensOnButtonMenu();
    }

    @Override
    protected void insert(BaseDomain domain) throws java.sql.SQLException {
    	getPedidoService().insertItemPedidoTroca(pedido, (ItemPedido)domain);
    }

    @Override
    protected void update(BaseDomain domain) throws SQLException {
    	getPedidoService().updateItemTrocaPedido(pedido, (ItemPedido)domain);
    }

    @Override
    protected void delete(BaseDomain domain) throws java.sql.SQLException {
    	getPedidoService().deleteItemTrocaPedido(pedido, (ItemPedido)domain, true);
    }

    @Override
    protected void salvarClick() throws SQLException {
    	calcularClick();
    	LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		try {
	    	super.salvarClick();
		} catch (ValidationException e) {
			if ((e.getMessage() != null) && e.getMessage().equals(ValidationException.EXCEPTION_ABORT_PROCESS)) {
				return;
			}
			throw e;
		} finally {
			msg.unpop();
		}
    }

    protected void salvarNovoClick() throws SQLException {
    	calcularClick();
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
		repaint();
    }

    protected void beforeSave() throws SQLException {
		if (LavenderePdaConfig.usaMotivoTrocaPorItemPedido() && (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(tipoItemList))) {
    		CadItemPedidoMotivoTrocaForm cadMotivoForm = new CadItemPedidoMotivoTrocaForm(getItemPedido());
    		cadMotivoForm.popup();
    		if (!cadMotivoForm.result) {
    			throw new ValidationException(Messages.ITEMPEDIDO_MSG_MOTIVOTROCA_OBRIGATORIO);
    		}
    	}
    	//--
		//Solicita confiormação para tentativa de inserção de mesmo produto no pedido
		if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
			int qtUnidadesJaInseridos = ItemPedidoService.getInstance().getQtProdutoDoItemJaInseridoPedido(pedido, getItemPedido());
			if (!isEditing() && (qtUnidadesJaInseridos > 0)) {
				if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ITEM_DUPLICADO_CONFIRMACAO, qtUnidadesJaInseridos)) == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
				if (getItemPedido().getProduto().isKit()) {
					ItemPedido itemPedidoJaInserido = ItemPedidoService.getInstance().getProdutoDoKitJaInseridoNoPedido(getItemPedido());
					if (itemPedidoJaInserido != null) {
						if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_ITEM_KIT_JA_INCLUIDO_PEDIDO_CONFIRMACAO, itemPedidoJaInserido.getDsProduto())) == 0) {
							throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
						}
					}
				} else {
					ItemPedido itemPedidoKitJaInserido = ItemPedidoService.getInstance().isProdutoJaInseridoNoPedidoEmUmKit(getItemPedido());
					if (itemPedidoKitJaInserido != null) {
						if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_JA_INSERIDO_KIT_CONFIRMACAO, new Object[] {getItemPedido().getDsProduto(), itemPedidoKitJaInserido.getDsProduto()})) == 0) {
							throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
						}	
					}
				}
			}
		}
		if (LavenderePdaConfig.isUsaPercentualItemDeTrocaOuBonificacaoDoPedidoOriginal() && LavenderePdaConfig.isObrigaRelacionarPedidoTroca()
				&& LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && pedido.isPedidoTroca()) {
			validateQtdItemOriginalNaBonificacaoUI(getItemPedido());
		}
		if (LavenderePdaConfig.isObrigaFotoItemTroca()) {
			FotoItemTroca fotoItemTroca = FotoItemTrocaService.getInstance().getFotoItemTrocaInstance(getItemPedido());
			int count = FotoItemTrocaService.getInstance().countByExample(fotoItemTroca);
			if (count == 0) {
				throw new ValidationException(MessageUtil.getMessage(Messages.ERROR_PRODUTO_SEM_FOTO_TROCA, getItemPedido().getProduto().toString()));
			}
		}
	}

	private void validatePctQtdItemOriginalNaBonificacaoTroca(Pedido pedido, ItemPedido itemPedido) throws SQLException {
		ItemPedidoService.getInstance().validaQtItemFisicoMaxDoItemPedidoBonificacaoTroca(pedido, itemPedido);
	}

	@Override
	protected void afterSave() throws SQLException {
		super.afterSave();
		ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(getItemPedido());
	}

	private void detalhesClick(boolean fromList) throws SQLException {
    	if (fromList) {
    		clearScreen();
    		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
    		msg.popupNonBlocking();
    		try {
    			ItemPedido itemPedido = getItemPedido();
    			getItemPedidoService().clearDadosItemPedido(pedido, itemPedido);
    			Produto p = (Produto) getSelectedDomain();
    			itemPedido.setProduto(p);
    			itemPedido.cdProduto = p.cdProduto;
    			itemPedido.dsProduto = p.dsProduto;
    			itemPedido.cdUnidade = p.cdUnidade;
    			itemPedido.flOrigemEscolhaItemPedido = ItemPedido.FLORIGEMESCOLHA_PADRAO;
    			itemPedido.flTipoEdicao = 0;
				if (LavenderePdaConfig.isUsaBloqueiaProdutoBloqueadoNoPedido()) {
					ProdutoBloqueadoService.getInstance().validateProdutoBloqueado(itemPedido, cbTabelaPreco);
				}
    			if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido) {
    				if (LavenderePdaConfig.usaSequenciaInsercaoNuSeqProduto) {
    					itemPedido.nuSeqProduto = itemPedido.nuSeqItemPedido;
    				} else {
    					itemPedido.nuSeqProduto = ItemPedidoService.getInstance().findMaxKey(itemPedido, "NUSEQPRODUTO") + 1;
    				}
    			}
    			//--
    			if (LavenderePdaConfig.usaAreaVendas) {
    				AreaVendaProdutoService.getInstance().validadeProdutoAreaVenda(pedido.cdAreaVenda, itemPedido.cdProduto, false);
    			}
    			//bloqueiaProdutoControladoClienteSemAlvaraOuLicenca
    			if (LavenderePdaConfig.isBloqueiaClienteSemAlvaraProdutoControlado() || LavenderePdaConfig.isBloqueiaClienteSemLicencaProdutoControlado()) {
    				ClienteService.getInstance().validateProdutoControladoClienteComAlvaraOuLicenca(itemPedido, pedido.getCliente());
    			}
    			changeItemTabelaPreco();
    			refreshProdutoToScreen(itemPedido);
    		} finally {
    			msg.unpop();
    			setFocusInQtde();
    		}
    	} else {
    		if (getItemPedido() != null && ValueUtil.isNotEmpty(getItemPedido().cdProduto)) {
    			CadProdutoDynForm cadProdutoForm = null;
			    cadProdutoForm = new CadProdutoDynForm();
			    cadProdutoForm.setEnabled(false);
			    cadProdutoForm.edit(ProdutoService.getInstance().getProdutoDyn(getItemPedido().cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, getItemPedido().cdProduto));
    			show(cadProdutoForm);
    		} else {
    			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
    		}
    	}
    }

    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        ItemPedido itemPedido = (ItemPedido) getDomain();
        itemPedido.setQtItemFisico(edQtItemFisico.getValueDouble());
        itemPedido.qtItemFaturamento = edQtItemFaturamento.getValueDouble();
        itemPedido.vlItemPedido = edVlItemPedido.getValueDouble();
        //--
        return itemPedido;
    }

    //@Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        ItemPedido itemPedido = (ItemPedido) domain;
        //--
    	cbTabelaPreco.carregaTabelaPrecoTroca(pedido.getCliente());
		cbTabelaPreco.setValue(itemPedido.cdTabelaPreco);
        //--
    	edDsCliente.setValue(pedido.getCliente().toString());
        edDsProduto.setValue(itemPedido.getDsProdutoWithKey());
        //--
        refreshDomainToScreen(itemPedido);
        refreshProdutoToScreen(itemPedido);
        //--
    	if (LavenderePdaConfig.ocultaQtItemFisico && LavenderePdaConfig.usaConversaoUnidadesMedida) {
			edQtItemFaturamentoFocusIn();
 		} else {
 			edQtItemFisicoFocusIn();
		}
    }

    private void refreshDomainToScreen(ItemPedido itemPedido) throws SQLException {
    	edDsProduto.setValue(itemPedido.getDsProdutoWithKey());
    	strBuffer.setLength(0);
    	ttDsProduto.setText(MessageUtil.quebraLinhas(edDsProduto.getValue()).toString());
    	updateQtItens(itemPedido);
        edVlItemPedido.setValue(itemPedido.vlItemPedido);
        edVlTotalItemPedido.setValue(itemPedido.vlTotalItemPedido);
        edVlTotalPedido.setValue(pedido.vlTotalPedido);
        if (pedido.isPedidoTroca()) {
        	edVlTotalPedido.setValue(pedido.vlTrocaRecolher);
        }
    }

    private void updateQtItens(ItemPedido itemPedido) {
        if (itemPedido.getQtItemFisico() == 0) {
            edQtItemFisico.setText("");
        } else {
        	if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
        		edQtItemFisico.setText(StringUtil.getStringValue(itemPedido.getQtItemFisico(), 0));
        	} else {
            	edQtItemFisico.setValue(itemPedido.getQtItemFisico());
        	}
        }
        if (itemPedido.qtItemFaturamento == 0) {
            edQtItemFaturamento.setText("");
        } else {
            edQtItemFaturamento.setValue(itemPedido.qtItemFaturamento);
            if ((itemPedido.qtItemFaturamento % 1) == 0) {
            	edQtItemFaturamento.setText(StringUtil.getStringValue(itemPedido.qtItemFaturamento, 0));
        	}
        }
    }

    private void refreshProdutoToScreen(ItemPedido itemPedido) throws SQLException {
    	if (LavenderePdaConfig.usaConversaoUnidadesMedida) {
    		Produto produtoItem = itemPedido.getProduto();
	    	if (!ValueUtil.isEmpty(produtoItem.dsUnidadeFisica)) {
		    	lbQtItemFisico.setText(produtoItem.dsUnidadeFisica);
	    	} else if (!ValueUtil.isEmpty(produtoItem.dsUnidadeFaturamento) && LavenderePdaConfig.ocultaQtItemFaturamento) {
		    	lbQtItemFisico.setText(produtoItem.dsUnidadeFaturamento);
	    	}
			if (!ValueUtil.isEmpty(produtoItem.dsUnidadeFaturamento)) {
				lbQtItemFaturamento.setText(produtoItem.dsUnidadeFaturamento);
			} else {
				lbQtItemFaturamento.setText(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO);
			}
   		}
    }

    private void clearDomainAndScreen() throws SQLException {
    	clearScreen();
    	ItemPedido itemPedido = getItemPedido();
    	getItemPedidoService().clearDadosItemPedido(itemPedido);
    	itemPedido.setProduto(null);
    }

    //@Override
    protected void clearScreen() throws java.sql.SQLException {
    	edQtItemFisico.setText("");
        edQtItemFaturamento.setText("");
        edVlItemPedido.setText("");
        edVlTotalItemPedido.setText("");
        edVlTotalPedido.setValue(pedido.vlTotalPedido);
        if (pedido.isPedidoTroca()) {
        	edVlTotalPedido.setValue(pedido.vlTrocaRecolher);
        }
    }

    public void setEnabled(boolean enabled) {
        edVlTotalItemPedido.setEditable(false);
        edVlTotalPedido.setEditable(false);
        edQtItemFisico.setEditable(enabled);
        edVlItemPedido.setEditable(false);
        edDsProduto.setEditable(false);
        //--
        edQtItemFaturamento.setEditable(enabled);
        if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido) {
        	numericPad.setEnabled(enabled);
        }
        cbTabelaPreco.setEnabled(false);
    }

    private void setVisible() {
        inVendaUnitariaMode = !isEditing();
        inVendaUnitariaMode = !inVendaUnitariaMode;
    	removeComponentsInScreen();
		addComponentsInScreen();
        //--
    	boolean editing = isEditing();
    	btSalvar.setVisible(isEnabled() && editing);
    	btSalvarNovo.setVisible(!editing);
   		btExcluir.setVisible(editing && isEnabled());
   		btCalcular.setVisible(isEnabled());
   		//--
   		edFiltro.setVisible(!editing);
   		listContainer.setVisible(!editing);
    }

    public void onFormClose() throws SQLException {
    	super.onFormClose();
    	if (pedido.isPedidoAberto() || (pedido.isPedidoFechado() && !pedido.isPedidoIniciadoProcessoEnvio())) {
    		cadPedidoForm.afterCrudItemPedido();
    	}
		listContainer.removeAllContainers();
		listContainer.uncheckAll();
    }

    //@Override
    public void close() throws SQLException {
    	super.close();
    	if (!flFromListTroca && !pedido.isPedidoTroca()) {
    		MainLavenderePda.getInstance().repaintNow();
    		cadPedidoForm.btTrocaClick();
		}
    }

    //@Override
    public void onFormShow() throws SQLException {
    	if (isEditing()) {
    		internalSetEnabled(pedido.isPedidoAberto() && !cadPedidoForm.inOnlyConsultaItens, false);
        	
    	}
    	//--
        setEnabled(isEnabled());
        setVisible();
        //--
    	super.onFormShow();
    	//--
    	if (!isEditing() && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto == 0) && !flListInicialized && (LavenderePdaConfig.qtMinimaCaracteresFiltroProduto != ValorParametro.PARAM_INT_VALOR_ZERO)) {
	    	flListInicialized = true;
	    	carregaGridProdutos();
    	}
    	//--
    	if (isEditing()) {
    		setFocusInQtde();
    	} else {
        	edFiltro.requestFocus();
    	}
    }
    
    @Override
    protected Control getComponentToFocus() {
    	return edFiltro;
    }

    private void setFocusInQtde() throws SQLException {
		if (LavenderePdaConfig.usaConversaoUnidadesMedida  && flFocoQtFaturamento) {
			edQtItemFaturamento.requestFocus();
			edQtItemFaturamentoFocusIn();
			edQtItemFaturamento.setCursorPos(edQtItemFaturamento.getText().length(), edQtItemFaturamento.getText().length());
		} else {
			edQtItemFisico.requestFocus();
			edQtItemFisicoFocusIn();
			edQtItemFisico.setCursorPos(edQtItemFisico.getLength(), edQtItemFisico.getLength());
		}
    }

    //@Override
    protected void onFormStart() throws SQLException {
   		UiUtil.add(barBottomContainer, btListaItens, 2);
    	UiUtil.add(this, lbTabelaPrecoAbrev, getLeft(),  getTop() + HEIGHT_GAP, PREFERRED, PREFERRED);
    	UiUtil.add(this, cbTabelaPreco, getLeft(), AFTER, getWFill());
        cbTabelaPreco.setSelectedIndex(0);
        UiUtil.add(this, edFiltro, getLeft(), AFTER + HEIGHT_GAP);
        UiUtil.add(this, listContainer, LEFT, edFiltro.getY2() + (HEIGHT_GAP * 2), FILL, FILL - (UiUtil.getControlPreferredHeight() * 2) - barBottomContainer.getHeight() - (HEIGHT_GAP * 18));
        UiUtil.add(barBottomContainer, btMenu, 3);
        //--
		UiUtil.add(barBottomContainer, btExcluir, 1);
		UiUtil.add(barBottomContainer, (isEnabled() && isEditing()) ? btSalvar : btSalvarNovo, 5);
		UiUtil.add(barBottomContainer, btCalcular, 4);
        //--
        populateHashEditsAndLabels();
        inVendaUnitariaMode = isEditing();
        addComponentsInScreen();
    }
    
    public void validateTabelaTroca() {
    	if (cbTabelaPreco.size() > 1) {
    		throw new ValidationException(Messages.TROCA_TABELAPRECO_DUPLICADA);
 		}
    	if (cbTabelaPreco.size() == 0) {
    		throw new ValidationException(Messages.TROCA_TABELAPRECO_VAZIO);
    	}
    }
    
	private void btUltimosPedidosClick() throws SQLException {
		ListPedidoForm listPedidoForm = new ListPedidoForm(pedido);
		listPedidoForm.inConsultaUltimosPedidos = true;
		show(listPedidoForm);
	}
	
    
    @Override
    public void onEvent(Event event) {
		try {
			switch (event.type) {
				case ControlEvent.WINDOW_CLOSED: {
					if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
						if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
							listContainer.reloadSortSettings();
							beforeOrder();
							carregaGridProdutos();
						}
					}
					break;
				}
				case PenEvent.PEN_UP: {
					if (event.target == listContainer.getSelectedItem()) {
						detalhesClick(true);
					}
					break;
				}
			}
		} catch (Throwable ex) {
			showException(ex);
			afterExceptionThrowedOnEvent(ex);
		}
    	super.onEvent(event);
    }
    
    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    		//Botões, ComboBox
    		case ControlEvent.PRESSED: {
    			if (event.target == btCalcular) {
    				calcularClick();
				} else if (event.target == btListaItens) {
					btListaItensClick();
    			} else if (event.target == numericPad) {
				    KeyEvent ke = new KeyEvent();
				    String s = numericPad.getSelectedItem();
				    if (s != null) {
					    if (s.equals("<")) {
						    ke.key = SpecialKeys.BACKSPACE;
					    } else {
						    ke.key = s.charAt(0);
					    }
					    ke.target = edBaseNumpad;
					    edBaseNumpad.onEvent(ke);
				    }
				    numericPad.setSelectedIndex(-1);
			    }
    			break;
    		}
    		case EditIconEvent.PRESSED : {
    			if (event.target == edFiltro) {
    		    	btFiltrarClick();
    			}
    			break;
    		}
    		case ButtonOptionsEvent.OPTION_PRESS : {
    			if (event.target == btMenu) {
    				if (btMenu.selectedItem.equals(Messages.BOTAO_INFO_PRODUTO_TROCA)) {
    					detalhesClick(false);
    				} else if (btMenu.selectedItem.equals(menuMotivoTroca)) {
    					CadItemPedidoMotivoTrocaForm cadItemPedidoMotivoTrocaForm = new CadItemPedidoMotivoTrocaForm(getItemPedido());
    					cadItemPedidoMotivoTrocaForm.popup();
    				//Detalhes de Cálculos
    				} else if (btMenu.selectedItem.equals(Messages.MENU_OPCAO_DETALHES_CALCULOS)) {
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
						//Calculadora
					}  else if (btMenu.selectedItem.equals(Messages.MENU_UTILITARIO_CALCULADORA)) {
						(new Calculator()).popup();
					} else if (btMenu.selectedItem.equals(Messages.CLIENTE_ULTIMOS_PEDIDOS)) {
						btUltimosPedidosClick();
					} else if (btMenu.selectedItem.equals(Messages.TITLE_FOTOS_ITEM_TROCA)) {
						if (ValueUtil.isNotEmpty(getItemPedido().cdProduto)) {
							btFotoItemTrocaClick();
						}  else {
							UiUtil.showWarnMessage(Messages.MESSAGE_SELECIONAR_PRODUTO_FOTO_ITEM_TROCA);
						}
				    }
    			}
    			break;
    		}
    		//Foco
    		case ControlEvent.FOCUS_IN: {
    			if ((event.target == edQtItemFisico) && edQtItemFisico.isEditable()) {
    				edQtItemFisicoFocusIn();
    			} else if ((event.target == edQtItemFaturamento)  && edQtItemFaturamento.isEditable()) {
    				edQtItemFaturamentoFocusIn();
    			} else {
    				edBaseNumpad = new EditText("", 0);
    			}
    			break;
    		}
    		//Teclado
    		case KeyboardEvent.KEYBOARD_PRESS: {
    			if (event.target == edFiltro) {
    				btFiltrarClick();
    			}
    			break;
    		}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				KeyEvent ke = (KeyEvent) event;
				if (ke.isActionKey() && event.target == edFiltro) {
					btFiltrarClick();
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (event.target == edQtItemFisico || event.target == edQtItemFaturamento || event.target == edVlItemPedido) {
    				calcularClick();
				}
			}
    	}
    }

    private void btFiltrarClick() throws SQLException {
    	dsFiltro = edFiltro.getText();
    	filtrarProduto();
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

    private void changeItemTabelaPreco() throws SQLException {
    	LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    	mb.popupNonBlocking();
    	ItemPedido itemPedido = getItemPedido();
		itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
		if (itemPedido.getProduto() != null) {
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
	    	if (itemTabelaPreco != null) {
	    		getItemPedidoService().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
	    		itemPedido.vlBaseItemTabelaPreco = itemPedido.vlBaseItemTabelaPreco * (1 - (itemPedido.getProduto().vlPctDepreciacaoTroca / 100));
	        	itemPedido.vlBaseItemPedido = itemPedido.vlBaseItemTabelaPreco * (1 - (itemPedido.getProduto().vlPctDepreciacaoTroca / 100));
	       		itemPedido.vlItemPedido = itemPedido.vlBaseItemTabelaPreco * (1 - (itemPedido.getProduto().vlPctDepreciacaoTroca / 100));
	        	refreshDomainToScreen(itemPedido);
	    	}
		}
    	//--
    	mb.unpop();
    }

    private void calcularClick() throws SQLException {
    	validateTabelaTroca();
    	ItemPedido itemPedido = (ItemPedido)screenToDomain();
    	try {
	    	getItemPedidoService().calculateSimples(itemPedido, pedido);
	    	//--
	    	refreshDomainToScreen(itemPedido);
    	} finally {
    		setFocusInQtde();
    	}
    }

    // ************************* Métodos de controle de Foco nos campos *****************************
    // ********************* Para calcular corretamente o item no calcularClick()********************
    // **********************************************************************************************

    private void edQtItemFisicoFocusIn() throws SQLException {
		edBaseNumpad = edQtItemFisico;
		if (edQtItemFisico.isEditable() && edQtItemFisico.isVisible()) {
        	getItemPedido().flEditandoQtItemFaturamento = false;
        	getItemPedido().flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_QTD;
			flFocoQtFaturamento = false;
    	}
    }

    private void edQtItemFaturamentoFocusIn() throws SQLException {
    	edBaseNumpad = edQtItemFaturamento;
    	getItemPedido().flEditandoQtItemFaturamento = true;
    	if (edQtItemFaturamento.isEditable() && edQtItemFaturamento.isVisible()) {
    		getItemPedido().qtItemFaturamento = edQtItemFaturamento.getValueDouble();
    		if (getItemPedido().getProduto() != null) {
    			ItemPedidoService.getInstance().aplicarConversaoUnidadeMedida(getItemPedido(), pedido);
        		updateQtItens(getItemPedido());
    		}
          	getItemPedido().flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_QTD_FATURAMENTO;
        	flFocoQtFaturamento = true;
    	}
    }

	// ********************* Métodos de controle da grid de produtos ********************************
    // **********************************************************************************************
    // **********************************************************************************************

//	protected ResultSet getDataSource() throws SQLException {
//		return ProdutoService.getInstance().findProdutosToGrid(dsFiltro, cbTabelaPreco.getValue(), null, false, true, false);
//	}

    private void carregaGridProdutos() throws SQLException {
    	if (flListInicialized) {
    		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			try {
				listContainer.removeAllContainers();
				listContainer.uncheckAll();
				
	    		int listSize = 0;
    			Vector produtoList =  getProdutoList();
    			listSize = produtoList.size();
				Container[] all = new Container[listSize];
				
				if (listSize > 0) {
					BaseListContainer.Item c;
					BaseDomain domain;
					String tooltip;
					for (int i = 0; i < listSize; i++) {
						all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
						domain = (BaseDomain) produtoList.items[i];
						ProdutoBase produto = (ProdutoBase) domain;
						tooltip = produto.cdProduto + " - " + produto.dsProduto;
						c.setToolTip(tooltip);
						c.id = domain.getRowKey();
						c.setID(c.id);
						c.domain = domain;
						c.setItens(getItem(domain));
					}
					listContainer.addContainers(all);
				}
			} finally {
				msg.unpop();
			}
    	}
    }
    
    private Vector getProdutoList() throws SQLException {
		ProdutoBase produto;
		try {
			produto = getProdutoFilter();
		} catch (FilterNotInformedException e) {
			return new Vector(0);
		}
   		return ProdutoService.getInstance().findProdutos(dsFiltro, cbTabelaPreco.getValue(), null, false, produto, false, false);
    }

    protected String[] getItemProdutoToGrid(Object domain) {
        ProdutoBase produto = (ProdutoBase) domain;
        //--
        String[] item = {
        	StringUtil.getStringValue(produto.getRowKey()),
        	StringUtil.getStringValue(!LavenderePdaConfig.ocultaColunaCdProduto ? produto.cdProduto : ""),
        	StringUtil.getStringValue(produto.dsProduto),
        	StringUtil.getStringValue(produto.dsPrincipioAtivo)
        };
	    //--
	    return item;
    }

    public String[][] getItems(String[][] items) {
    	return items;
    }

    public void afterScroll() {
    }

    private void populateHashsTemporarias() {
    	hashEditsTemp = new Hashtable(30);
    	hashLabelsTemp = new Hashtable(30);
    	hashEditsTemp.put(1, edQtItemFisico);
    	hashLabelsTemp.put(1, lbQtItemFisico);
    	hashEditsTemp.put(2, edQtItemFaturamento);
    	hashLabelsTemp.put(2, lbQtItemFaturamento);
    	hashEditsTemp.put(3, edVlItemPedido);
    	hashLabelsTemp.put(3, lbVlItemPedido);
    	hashEditsTemp.put(4, edVlTotalItemPedido);
    	hashLabelsTemp.put(4, lbVlTotalItemPedido);
    	hashEditsTemp.put(5, edVlTotalPedido);
    	hashLabelsTemp.put(5, lbVlTotalPedido);
    	for (int i = 1; i <= hashEditsTemp.size(); i++) {
    		((Control)hashEditsTemp.get(i)).appId = i;
    		((Control)hashLabelsTemp.get(i)).appId = i;
    	}
    }

    private void populateHashEditsAndLabels() {
    	if (hashEdits.size() == 0) {
    		int[] paramsTemp = {1, 2, 3, 4, 5};
    		int contTemp = 1;
        	for (int i = 0; i < paramsTemp.length; i++) {
        		Control ctrEd = (Control)hashEditsTemp.get(paramsTemp[i]);
        		if (validadeVisibleParams(ctrEd)) {
        			Control ctrLb = (Control)hashLabelsTemp.get(paramsTemp[i]);
        			hashEdits.put(contTemp, ctrEd);
        			hashLabels.put(contTemp, ctrLb);
        			contTemp++;
        		}
        	}
    	}
    }

    private boolean validadeVisibleParams(Control ctr) {
    	if (ctr == null) {
    		return false;
    	}
    	//-- edQtItemFisico
    	if (ctr.appId == 1) {
    		if (LavenderePdaConfig.ocultaQtItemFisico && LavenderePdaConfig.usaConversaoUnidadesMedida) {
    			return false;
    		}
    	}
    	//-- edQtItemFaturamento
    	if (ctr.appId == 2) {
			if (!LavenderePdaConfig.usaConversaoUnidadesMedida || (LavenderePdaConfig.ocultaQtItemFaturamento && LavenderePdaConfig.usaConversaoUnidadesMedida)) {
				return false;
			}
    	}
    	return true;
    }

    private void removeComponentsInScreen() {
		listContainer.removeAllContainers();
		listContainer.uncheckAll();
		for (int i = 1; i <= hashEdits.size(); i++) {
            remove((Control)hashEdits.get(i));
            remove((Control)hashLabels.get(i));
        }
		remove(edDsProduto);
		remove(numericPad);
		remove(btMenu);
		UiUtil.add(barBottomContainer, btListaItens, 2);
    }

    private void addComponentsInScreen() {
        if (inVendaUnitariaMode) {
        	UiUtil.add(this, edDsProduto, LEFT + WIDTH_GAP, cbTabelaPreco.getY2() + (HEIGHT_GAP * 2), FILL - WIDTH_GAP, cbTabelaPreco.getPreferredHeight() * 3);
        }
        addFields();
    }

    private void addFields() {
    	int widthCol = (Settings.screenWidth / 3) - 1;
        int widthLabelInCol = widthCol / 3;
        int yEdits = BOTTOM - barBottomContainer.getHeight() - HEIGHT_GAP;
        int xEdits = LEFT;
        //--
    	if (!isAdding()) {
    		barBottomContainer.remove(btListaItens);
    	}
    	if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido) {
			UiUtil.add(this, numericPad, LEFT + WIDTH_GAP, BOTTOM - barBottomContainer.getHeight(), FILL, UiUtil.getControlPreferredHeight() * 2);
            yEdits = BEFORE - HEIGHT_GAP;
    	}
        for (int i = 1; i <= hashEdits.size(); i++) {
        	if ((i == 1) || (((i - 1) % 3) == 0)) {
        		UiUtil.add(this, (Control)hashLabels.get(i), xEdits, yEdits, widthLabelInCol);
        		if (((i - 1) % 3) == 0) {
        			yEdits = BEFORE - HEIGHT_GAP;
        		}
        	} else {
        		UiUtil.add(this, (Control)hashLabels.get(i), AFTER, SAME, widthLabelInCol);
        	}
            UiUtil.add(this, (Control)hashEdits.get(i), AFTER + WIDTH_GAP, SAME, widthCol - widthLabelInCol - 2, UiUtil.getControlPreferredHeight());
        }
        setTecladoEnableInFields((inVendaUnitariaMode && !LavenderePdaConfig.usaTecladoFixoTelaItemPedido) || !inVendaUnitariaMode);
    }

    private void setTecladoEnableInFields(boolean enabled) {
    	edQtItemFisico.enableTeclado = enabled;
    	edQtItemFaturamento.enableTeclado = enabled;
    	edVlItemPedido.enableTeclado = enabled;
    }

    private void addItensOnButtonMenu() {
    	btMenu.removeAll();
    	btMenu.addItem(Messages.BOTAO_INFO_PRODUTO_TROCA);
    	// Detalhes de Cálculos
		if (Session.isModoSuporte || VmUtil.isJava()) {
			btMenu.addItem(Messages.MENU_OPCAO_DETALHES_CALCULOS);
		}
    	btMenu.addItem(Messages.MENU_UTILITARIO_CALCULADORA);    
		//Ultimos Pedidos
		if (isEnabled() && !LavenderePdaConfig.isOcultaAcessosRelUltimosPedidosMenuInferior()) {
			btMenu.addItem(Messages.CLIENTE_ULTIMOS_PEDIDOS);
		}
		if (isEditing() && LavenderePdaConfig.usaMotivoTrocaPorItemPedido() && pedido.isPedidoAberto()) {
        	menuMotivoTroca = Messages.ITEMPEDIDO_LABEL_MOTIVO_TROCA;
        	btMenu.addItem(menuMotivoTroca);
        }
        btMenu.addItem(Messages.TITLE_FOTOS_ITEM_TROCA);
    }
    
    private BaseDomain getSelectedDomain() throws SQLException {
		return ProdutoService.getInstance().findByRowKey(getSelectedRowKey());
	}

	private String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

    private void constructorListContainer() {
    	configListContainer(LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? "CDPRODUTO" : "DSPRODUTO", ValueUtil.VALOR_SIM);
		listContainer = new GridListContainer(2, 2, true);
		listContainer.setColsSort(new String[][]{{Messages.PRODUTO_LABEL_CODIGO, "CDPRODUTO"}, {Messages.PRODUTO_LABEL_DSPRODUTO, "DSPRODUTO"}});
		listContainer.btResize.setVisible(false);
		listContainer.sortAsc = sortAsc;
		listContainer.atributteSortSelected = sortAtributte;
    }
	
    private void configListContainer(String defaultSortAtributte, String deafultSortAscend) {
		sortAtributte = ListContainerConfig.getDefautSortColumn(getConfigClassName());
		if (ValueUtil.isEmpty(sortAtributte)) {
			sortAtributte = defaultSortAtributte;
		}
		sortAsc = ListContainerConfig.getDefautOrder(getConfigClassName());
		if (ValueUtil.isEmpty(sortAsc)) {
			sortAsc = deafultSortAscend;
		}
	}
	
	private String getConfigClassName() {
		return ClassUtil.getSimpleName(this.getClass());
	}
	
	private String[] getItem(Object domain) throws SQLException {
    	ProdutoBase produto = (ProdutoBase) domain;
    	String descricao = LavenderePdaConfig.ocultaColunaCdProduto ? produto.dsProduto : produto.cdProduto + " - " + produto.dsProduto;
		return new String[] { descricao, "" };
	}
	
	private void beforeOrder() {
		sortAtributte = listContainer.atributteSortSelected;
		sortAsc = listContainer.sortAsc;
		saveListConfig();
	}
	
	private void saveListConfig() {
		try {
			BaseDomain domainConfig = getDomainConfig();
			ListContainerConfig.listasConfig.put(((ConfigInterno)domainConfig).vlChave, StringUtil.split(((ConfigInterno)domainConfig).vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			if (getConfigService().findByRowKey(domainConfig.getRowKey()) == null) {
				getConfigService().insert(domainConfig);
			} else {
				getConfigService().update(domainConfig);
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}
	
	private void getListConfig() {
		try {
			BaseDomain domainConfig = getDomainConfig();
			ConfigInterno configInterno = (ConfigInterno) getConfigService().findByRowKey(domainConfig.getRowKey());
			if (configInterno != null) {
				String[] parametros = configInterno.vlConfigInterno.split(":");
				sortAtributte = parametros[0];
				sortAsc = parametros[1];
			} else {
				sortAtributte = LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? "CDPRODUTO" : "DSPRODUTO";
				sortAsc = ValueUtil.VALOR_SIM;
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	private BaseDomain getDomainConfig() {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.configSortAndOrderColumn;
		configInterno.vlChave = getConfigClassName();
		configInterno.vlConfigInterno = sortAtributte + ConfigInterno.defaultSeparatorInfoValue + sortAsc;
		return configInterno;
	}

	private CrudService getConfigService() {
		return ConfigInternoService.getInstance();
	}

	private void btListaItensClick() throws SQLException {
		inRelatorioMode = true;
		if (pedido.isPedidoAberto() && !inOnlyConsultaItens && houveAlteracaoCampos) {
			pedido.updateByClickNovoItemInPedido = true;
			pedido.validaDataEntrega = false;
			save();
			pedido.validaDataEntrega = true;
			pedido.updateByClickNovoItemInPedido = false;
		}
		ListItemPedidoForm listItemPedidoForm = getItemPedidoListForm();
		listItemPedidoForm.show();
		houveAlteracaoCampos = false;
	}

	private ListItemPedidoForm getItemPedidoListForm() throws SQLException {
		ListItemPedidoForm listItemPedidoForm;
		cadPedidoForm.inRelatorioMode = true;
		if (pedido.isPedidoTroca()) {
			listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
		} else if (pedido.isPedidoBonificacao()) {
			listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
		} else {
			if (inOnlyConsultaItens || inRelatorioMode) {
				listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			} else {
				listItemPedidoForm = ListItemPedidoForm.getInstance(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
			}
		}
		return listItemPedidoForm;
	}

	private void btFotoItemTrocaClick() throws SQLException {
		ItemPedido itemTroca = getItemPedido();
		ImageSliderItemTrocaWindow imageSliderItemTrocaWindow = new ImageSliderItemTrocaWindow(itemTroca,
				!itemTroca.pedido.isPedidoAberto() || isEditing());
		imageSliderItemTrocaWindow.popup();
	}

	private void validateQtdItemOriginalNaBonificacaoUI(ItemPedido itemPedido) throws SQLException {
		try {
			if (LavenderePdaConfig.isUsaPercQuantidadeDoItemOriginalBonificacaoTroca()) {
				validatePctQtdItemOriginalNaBonificacaoTroca(pedido, itemPedido);
			} else {
				ItemPedidoService.getInstance().validaQtItemFisicoMaxBonificacaoTroca(pedido, itemPedido);
			}
		} catch (ValidationException ex) {
			if (LavenderePdaConfig.liberaSenhaQtdItemMaiorPedidoOriginal) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ex.getMessage());
				senhaForm.setCdProdutoApenas(itemPedido.getProduto().cdProduto);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_QTD_ITEM_BONIFICACAO);
				if (senhaForm.show()) {
					itemPedido.flQuantidadeLiberada = ValueUtil.VALOR_SIM;
				} else {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			} else {
				throw ex;
			}
		}
	}

	public ProdutoBase getProdutoFilter() throws SQLException {
		ProdutoBase produto;
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
    		produto = new ProdutoTabPreco();
    	} else {
    		produto = new Produto();
    	}
		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produto.sortAtributte = sortAtributte;
		produto.sortAsc = sortAsc;
		//--
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido) {
			produto.flExcecaoProduto = ValueUtil.isNotEmpty(pedido.getTipoPedido().flExcecaoProduto) ? pedido.getTipoPedido().flExcecaoProduto : ValueUtil.VALOR_NAO;
			produto.cdTipoPedidoFilter = pedido.cdTipoPedido;
		}
		prepareGrupoProdTipoPedFilter(produto);
		prepareItemPedidoFilter(produto);
		return produto;
	}
	
	private void prepareGrupoProdTipoPedFilter(ProdutoBase produto) throws SQLException {
		if (LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
			if (pedido.getTipoPedido() == null) {
				throw new FilterNotInformedException();
			}
			produto.grupoProdTipoPedFilter = new GrupoProdTipoPed();
			produto.grupoProdTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produto.grupoProdTipoPedFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProdTipoPed.class);
			produto.grupoProdTipoPedFilter.cdTipoPedido = pedido.cdTipoPedido;
			produto.grupoProdTipoPedFilter.excecaoGrupoProdutoFilter = pedido.getTipoPedido().isFlExcecaoGrupoProduto();
		}
	}
	
	public void prepareItemPedidoFilter(ProdutoBase produto) throws SQLException {
		if (LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && ValueUtil.isNotEmpty(getItemPedido().pedido.nuPedidoRelTroca)) {
			produto.itemPedidoFilter = new ItemPedido();
			produto.itemPedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produto.itemPedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
			produto.itemPedidoFilter.nuPedidoListFilter = getItemPedido().pedido.nuPedidoRelTroca.split(";");
		}
	}
	
}
