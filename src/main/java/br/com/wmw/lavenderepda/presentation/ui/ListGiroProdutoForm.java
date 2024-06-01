package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.service.GiroProdutoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import totalcross.sys.Convert;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class ListGiroProdutoForm extends BaseCrudListForm {

	private int posYIni;
	private CadItemPedidoForm cadItemPedidoForm;
	private LabelTotalizador lbTtQtItemFisico;
	private SessionTotalizerContainer sessaoTotalizadores;
	private Vector domainList;
	private String lastFilter;
	private boolean onPopUpGiro;
	private LabelValue lbMsgFechamento;
	private ListGiroProdutoWindow listGiroProdutoWindow;
	private CadPedidoForm cadPedidoForm;
	private Pedido pedido;
	private boolean onFechamentoPedido;
	private boolean fromMenuCatalogo;
	public boolean hasGiroProduto;

	public ListGiroProdutoForm() throws SQLException {
		this(false, null, null, null, false, false, false);
	}
	
    public ListGiroProdutoForm(boolean onPopUpGiro, ListGiroProdutoWindow listGiroProdutoWindow, CadPedidoForm cadPedidoForm, Pedido pedido, boolean fromBtNovoItemOrFechamentoPed, boolean fromMenuCatalogo, boolean onFechamentoPedido) throws SQLException {
        super(Messages.GIROPRODUTO_NOME_ENTIDADE);
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
        edFiltro.autoSelect = true;
        lbTtQtItemFisico = new LabelTotalizador("9999 registros");
		sessaoTotalizadores = new SessionTotalizerContainer();
		if (onPopUpGiro) {
			lbMsgFechamento = new LabelValue("", CENTER);
			lbMsgFechamento.autoSplit = true;
			lbMsgFechamento.setText(onFechamentoPedido ? Messages.REL_GIRO_PRODUTO_PENDENTES_MSG_NAO_INSERIDOS_PEDIDO : Messages.PEDIDO_LABEL_DESCRICAO_MULTIPLOS_PRODUTOS);
		}
		this.onPopUpGiro = onPopUpGiro;
		this.listGiroProdutoWindow = listGiroProdutoWindow;
		this.cadPedidoForm = cadPedidoForm;
		this.pedido = pedido;
		this.onFechamentoPedido = onFechamentoPedido;
		this.fromMenuCatalogo = fromMenuCatalogo;
		if (fromBtNovoItemOrFechamentoPed || fromMenuCatalogo) {
			getDomainList();
		}
    }

    protected CrudService getCrudService() throws SQLException {
        return GiroProdutoService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	String filtro = (edFiltro.getValue()).toUpperCase();
    	Cliente cliente = SessionLavenderePda.getCliente();
    	GiroProduto giroProdutoFilter = GiroProdutoService.getInstance().getGiroProdutoFilterByClienteAndPedido(cliente, pedido, false);
		giroProdutoFilter.produtoFilter.cdProduto = filtro;
		giroProdutoFilter.produtoFilter.dsProduto = filtro;
    	return giroProdutoFilter;
    }

    @Override
    public void visibleState() {
    	super.visibleState();
    	barTopContainer.setVisible(!onPopUpGiro);
    }
    
    @Override
    protected int getTop() {
		return (onPopUpGiro) ? TOP : super.getTop();
    }
    
    @Override
    protected Vector getDomainList() throws SQLException {
    	String filtro = (edFiltro.getValue()).toUpperCase();
    	if (ValueUtil.valueEquals(filtro, lastFilter)) {
    		return domainList;
    	}
    	lastFilter = filtro;
		domainList = getCrudService().findAllByExampleSummary(getDomainFilter());
		hasGiroProduto = !domainList.isEmpty(); 
        return domainList;
    }

    @Override
    protected String[] getItem(Object domain) throws SQLException {
        GiroProduto giroProduto = (GiroProduto) domain;
        loadGradeGiroProduto(giroProduto);
        loadProdutoGiro(giroProduto);
        loadUltPrecoGiroProduto(giroProduto);
        String[] item = new String[LavenderePdaConfig.ocultaColunaCdProduto ? 12 : 13];
        int i = 0;
    	item[i++] = StringUtil.getStringValue(giroProduto.rowKey);
    	if (!LavenderePdaConfig.ocultaColunaCdProduto) {
    		item[i++] = StringUtil.getStringValue(giroProduto.cdProduto);
    	}
    	item[i++] = StringUtil.getStringValue(getDsProdutoReferencia(giroProduto.produto, giroProduto.dsProduto));
    	item[i++] = StringUtil.getStringValueToInterface(giroProduto.vlUltPreco);
    	item[i++] = StringUtil.getStringValueToInterface(giroProduto.qtMediasemanal);
    	item[i++] = StringUtil.getStringValueToInterface(giroProduto.qtMaiorcompra);
    	item[i++] = StringUtil.getStringValueToInterface(giroProduto.qtCompra);
    	item[i++] = StringUtil.getStringValueToInterface(giroProduto.vlUnitario);
    	item[i++] = StringUtil.getStringValue(giroProduto.dsDia);
    	item[i++] = giroProduto.itemGrade1 != null ? giroProduto.itemGrade1.toString() : "";
    	item[i++] = giroProduto.itemGrade2 != null ? giroProduto.itemGrade2.toString() : "";
    	item[i++] = giroProduto.itemGrade3 != null ? giroProduto.itemGrade3.toString() : "";
    	item[i++] = StringUtil.getStringValue(giroProduto.dsObservacao);
        return item;
    }

	private void loadGradeGiroProduto(GiroProduto giroProduto) throws SQLException {
		GiroProdutoService.getInstance().loadGradesGiroProduto(giroProduto);
	}

	private void loadProdutoGiro(GiroProduto giroProduto) throws SQLException {
		if (!LavenderePdaConfig.isMostraDescricaoReferencia()) return;
		GiroProdutoService.getInstance().loadProdutoGiroProduto(giroProduto);
	}

	@Override
    protected String getSelectedRowKey() throws SQLException {
        return gridEdit.getSelectedItem()[0];
    }

    public void setCadItemPedidoForm(CadItemPedidoForm cadItemPedidoForm) {
		this.cadItemPedidoForm = cadItemPedidoForm;
	}
    
    @Override
    public void list() throws SQLException {
    	super.list();
    	int size = gridEdit.size();
    	StringBuffer totalizador = new StringBuffer(); 
        totalizador.append(size).append(" ").append((size > 1) ? Messages.GIROPRODUTO_TOTALIZADOR_PLURAL : Messages.GIROPRODUTO_TOTALIZADOR_SINGULAR);
    	lbTtQtItemFisico.setValue(totalizador.toString());
    }

    protected void onFormStart() throws SQLException {
    	if (onPopUpGiro) {
    		UiUtil.add(this, lbMsgFechamento, LEFT, getTop() + HEIGHT_GAP);
    	} else {
    		UiUtil.add(this, new LabelContainer(SessionLavenderePda.getCliente().toString()), LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	}
		UiUtil.add(this, edFiltro, getLeft(), AFTER + HEIGHT_GAP_BIG);
		edFiltro.setText("");
		
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM, FILL, UiUtil.getLabelPreferredHeight());
		UiUtil.add(sessaoTotalizadores, lbTtQtItemFisico, LEFT + UiUtil.getTotalizerGap(), CENTER, PREFERRED, PREFERRED);
		
    	GridColDefinition[] gridColDefiniton = getGridColDefinitionByConfigParameters();
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
    	gridEdit.sortTypes[1] = LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto ? Convert.SORT_INT : Convert.SORT_STRING;
        UiUtil.add(this, gridEdit, LEFT, edFiltro.getY2() + HEIGHT_GAP_BIG, FILL, FILL - UiUtil.getLabelPreferredHeight());
    }

	private GridColDefinition[] getGridColDefinitionByConfigParameters() throws SQLException {
        return !LavenderePdaConfig.usaScroolLateralListasProdutos ? defineGridColDefinitionByCols() : defineGridColDefinitionScroolLateral();
	}

	private GridColDefinition[] defineGridColDefinitionByCols() {
        final int maxCol = (LavenderePdaConfig.ocultaColunaCdProduto) ? 12 : 13;
		GridColDefinition[] gridColDef = new GridColDefinition[maxCol];
        int col = getDefaultGridColDefinition(gridColDef);
        
        final String firstColumnAlignLeft = GiroProdutoService.getInstance().getMessageColunaRelGiroProduto(5);
        final int nuSizeAlignLeft = fm.stringWidth("00/00/00000");
        final int nuSizeAlignRight = fm.stringWidth("000000,000");
        int align = RIGHT;
        int valorPosGiro = 0;
        for (int i = col; i < maxCol; i++) {
			String messageGrid = GiroProdutoService.getInstance().getMessageColunaRelGiroProduto(valorPosGiro);
			align = loadAlignType(align, firstColumnAlignLeft, messageGrid);
			if (i == maxCol - 1) {
				gridColDef[i] = new GridColDefinition(messageGrid, 400, align);
			} else if (align == LEFT) {
				gridColDef[i] = new GridColDefinition(messageGrid, nuSizeAlignLeft, align);
			} else {
				gridColDef[i] = new GridColDefinition(messageGrid, nuSizeAlignRight, align);
			}
			valorPosGiro++;
        }
		return gridColDef;
	}

	private GridColDefinition[] defineGridColDefinitionScroolLateral() throws SQLException {
		String[] captions = new String[10];
		for (int i = 0; i < 10; i++) {
			captions[i] = GiroProdutoService.getInstance().getMessageColunaRelGiroProduto(i);
		}
		return getSizesForGrid(captions);
	}

	private int getDefaultGridColDefinition(GridColDefinition[] gridColDef) {
		int col = 0;
		gridColDef[col++] = new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT);
        if (!LavenderePdaConfig.ocultaColunaCdProduto) {
        	gridColDef[col++] = new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, fm.stringWidth("0000000000"), LEFT);
        }
        gridColDef[col++] = new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, fm.stringWidth("0000000000000000000000000"), LEFT);
		return col;
	}

	private int loadAlignType(int align, String firstColumnAlignLeft, String messageGrid) {
		if (align == RIGHT && messageGrid.equalsIgnoreCase(firstColumnAlignLeft) ) {
			align = LEFT;
		}
		return align;
	}

    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    		case PenEvent.PEN_DOWN : {
    			if (event.target == gridEdit) {
    				posYIni = ((PenEvent) event).y;
    			}
    			break;
    		}
    		case PenEvent.PEN_UP: {
    			if (event.target == gridEdit) {
    				final int posIni = ((PenEvent) event).y - posYIni;
					if (gridEdit.getSelectedIndex() != -1 && !(posIni < -30 || posIni > 30)) {
						gridClick();
    				}
    			}
    			break;
    		}
    	}
    }
    
    @Override
    protected void filtrarClick() throws SQLException {
    	list();
		if (LavenderePdaConfig.limpaFiltroProdutoAutomaticamente) {
			edFiltro.setValue("");
		}
    	edFiltro.requestFocus();
    }

    private void gridClick() throws SQLException {
    	if (!onPopUpGiro) return;
    	GiroProduto giroProduto = (GiroProduto) getCrudService().findByRowKey(getSelectedRowKey());
		Produto produto = ProdutoService.getInstance().getProduto(giroProduto.cdProduto);
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(pedido, produto)) {
			UiUtil.showWarnMessage(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
			return;
		}
		CadItemPedidoForm cadItemPedido = cadItemPedidoForm == null ? CadItemPedidoForm.getInstance(cadPedidoForm, pedido) : cadItemPedidoForm;
		cadItemPedido.fromProdutoPendenteGiroMultInsercao = true;
		cadItemPedido.getItemPedido().giroProduto = giroProduto;
		if (produto == null) return;
		if (onFechamentoPedido) {
			cadItemPedido.add();
			cadItemPedido.flFromCadPedido = true;
			cadItemPedido.onFechamentoPedido = true;
			cadPedidoForm.inItemNegotiationGiroProdutoPendente = true;
			cadItemPedido.getItemPedido().giroProduto = giroProduto;
			MainLavenderePda.getInstance().show(cadItemPedido);
		}
		if (fromMenuCatalogo) {
			cadItemPedido.fromMenuCatalogForm = true;
			cadItemPedido.fromRelGiroProduto = true;
			listGiroProdutoWindow.close();
			cadItemPedido.setProdutoSelecionado(produto);
			cadItemPedido.add();
			cadItemPedido.getItemPedido().giroProduto = giroProduto;
			MainLavenderePda.getInstance().show(cadItemPedido);
			cadItemPedido.gridClickAndRepaintScreen();
		} else {
			cadItemPedido.fromRelGiroProduto = true;
			listGiroProdutoWindow.close();
			cadItemPedido.produtoSelecionado = produto;
			if (LavenderePdaConfig.isUsaVlUnitGiroProduto() && LavenderePdaConfig.ordemCamposTelaItemPedido.contains("174")) {
				cadItemPedido.inVendaUnitariaMode = false;
			}
			cadItemPedido.getItemPedido().giroProduto = giroProduto;
			cadItemPedido.gridClickAndRepaintScreen(!cadItemPedido.inVendaUnitariaMode);
		}
    }
    
    @Override
    public void onFormClose() throws SQLException {
    	super.onFormClose();
    	if (cadItemPedidoForm != null) {
    		cadItemPedidoForm.fromRelGiroProduto = false;
    	}
    }
    
    private GridColDefinition[] getSizesForGrid(String[] captions) throws SQLException {
    	GridColDefinition[] gridColDefinition = LavenderePdaConfig.ocultaColunaCdProduto ? new GridColDefinition[12] : new GridColDefinition[13];
    	domainList = getDomainList();
    	int size = domainList.size();
    	int l1, l2, l3, l4, l5, l6, l8, l9, cdLen = 0, vlUltPrecLen = 0, nmProdLen = 0, mediaLen = 0, maxCompraLen = 0, qtLen = 0, vlUnitLen = 0, observacaoL = 400;
    	for (int i = 0; i < size; i++) {
    		GiroProduto giroProduto = (GiroProduto) domainList.items[i];
    		loadProdutoGiro(giroProduto);
    		loadUltPrecoGiroProduto(giroProduto);
    		l1 = fm.stringWidth(StringUtil.getStringValue(giroProduto.cdProduto));
    		cdLen = l1 > cdLen ? l1 : cdLen;
    		l2 = fm.stringWidth(StringUtil.getStringValue(getDsProdutoReferencia(giroProduto.produto, giroProduto.dsProduto)));
    		nmProdLen = l2 > nmProdLen ? l2 : nmProdLen;
    		l3 = fm.stringWidth(StringUtil.getStringValueToInterface(giroProduto.qtMediasemanal));
    		mediaLen = l3 > mediaLen ? l3 : mediaLen;
    		l4 = fm.stringWidth(StringUtil.getStringValueToInterface(giroProduto.qtMaiorcompra));
    		maxCompraLen = l4 > maxCompraLen ? l4 : maxCompraLen;
    		l5 = fm.stringWidth(StringUtil.getStringValueToInterface(giroProduto.qtCompra));
    		qtLen = l5 > qtLen ? l5 : qtLen;
    		l6 = fm.stringWidth(StringUtil.getStringValueToInterface(giroProduto.vlUnitario));
    		vlUnitLen = l6 > vlUnitLen ? l6 : vlUnitLen;
    		l8 = fm.stringWidth(StringUtil.getStringValue(giroProduto.dsObservacao));
    		observacaoL = l8 > observacaoL ? l8 : observacaoL;
    		l9 = fm.stringWidth(StringUtil.getStringValue(giroProduto.vlUltPreco));
    		vlUltPrecLen = l9 > vlUltPrecLen ? l9 : vlUltPrecLen;
    	}
    	defineGridColDefinitionSizes(captions, gridColDefinition, cdLen, vlUltPrecLen, nmProdLen, mediaLen, maxCompraLen, qtLen, vlUnitLen, observacaoL);
    	return gridColDefinition;
    }

	private void defineGridColDefinitionSizes(String[] captions, GridColDefinition[] gridColDefinition, int cdLen, int vlUltPrecLen, int nmProdLen, int mediaLen, int maxCompraLen, int qtLen, int vlUnitLen, int observacaoL) {
		int i = 0;
    	gridColDefinition[i++] = new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT);
    	if (!LavenderePdaConfig.ocultaColunaCdProduto) {
    		gridColDefinition[i++] = new GridColDefinition(Messages.PRODUTO_LABEL_CODIGO, cdLen + WIDTH_GAP_BIG, LEFT);
    	}
    	gridColDefinition[i++] = new GridColDefinition(Messages.PRODUTO_NOME_ENTIDADE, nmProdLen + WIDTH_GAP_BIG, LEFT);
    	gridColDefinition[i++] = new GridColDefinition(captions[0], vlUltPrecLen + WIDTH_GAP_BIG, RIGHT);
    	gridColDefinition[i++] = new GridColDefinition(captions[1], mediaLen + WIDTH_GAP_BIG, RIGHT);
    	gridColDefinition[i++] = new GridColDefinition(captions[2], maxCompraLen + WIDTH_GAP_BIG, RIGHT);
    	gridColDefinition[i++] = new GridColDefinition(captions[3], qtLen + WIDTH_GAP_BIG, RIGHT);
    	gridColDefinition[i++] = new GridColDefinition(captions[4], vlUnitLen + WIDTH_GAP_BIG, RIGHT);
    	gridColDefinition[i++] = new GridColDefinition(captions[5], fm.stringWidth("00/00/0000") + WIDTH_GAP_BIG, LEFT);
    	gridColDefinition[i++] = new GridColDefinition(captions[6], fm.stringWidth("00/00/0000") + WIDTH_GAP_BIG, LEFT);
    	gridColDefinition[i++] = new GridColDefinition(captions[7], fm.stringWidth("00/00/0000") + WIDTH_GAP_BIG, LEFT);
    	gridColDefinition[i++] = new GridColDefinition(captions[8], fm.stringWidth("00/00/0000") + WIDTH_GAP_BIG, LEFT);
    	gridColDefinition[i] = new GridColDefinition(captions[9], observacaoL + WIDTH_GAP_BIG, LEFT);
	}
    
    private void loadUltPrecoGiroProduto(GiroProduto giroProduto) throws SQLException {
    	if (LavenderePdaConfig.naoUsaUltPrecoRelGiroProduto() || !LavenderePdaConfig.isCarregaUltimoPrecoItemPedido() || giroProduto.ultPrecoCarregado) {
    		return;
    	}
    	giroProduto.ultPrecoCarregado = true;
    	giroProduto.pedidoAtual = pedido == null ? new Pedido() : pedido;
    	giroProduto.cdCliente = pedido == null ? SessionLavenderePda.getCliente().cdCliente : pedido.cdCliente;
    	giroProduto.vlUltPreco = GiroProdutoService.getInstance().loadVlUltPrecoProdutoGiroProduto(giroProduto);
	}

	private String getDsProdutoReferencia(Produto produto, String dsProdutoGiro) {
    	if (produto == null) {
    		return StringUtil.getStringValue(dsProdutoGiro);
    	}
		if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
			return StringUtil.getStringValue("[" + produto.dsReferencia + "] " + dsProdutoGiro);
		} else if (LavenderePdaConfig.isMostraDescricaoReferenciaAposDsProduto()) {
			return StringUtil.getStringValue(dsProdutoGiro + " [" + produto.dsReferencia + "]");
		}
    	return StringUtil.getStringValue(dsProdutoGiro);
    }

}
