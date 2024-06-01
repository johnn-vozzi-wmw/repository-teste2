package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.ValueChooser;
import br.com.wmw.framework.util.ClassUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.CargaProduto;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.service.CargaProdutoService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainer.CAMPOS_LISTA;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.PenEvent;
import totalcross.util.Vector;

public class CadCargaProdutoForm extends BaseCrudCadForm {


	private CAMPOS_LISTA[] campos;
	private EditFiltro edFiltro;
	private ArrayList<CargaProduto> cargaProdutoList;
	private ArrayList<Produto> produtoCargaList;
	private EditDate edDtInicial;
	private EditDate edDtFinal;
	
    private GridListContainer listContainer;
    
    public CadCargaProdutoForm() {
        super(Messages.CARGAPRODUTO_TITULO_CADASTRO);
		edFiltro = new EditFiltro("999999999", 50);
		edDtInicial = new EditDate();
        edDtFinal = new EditDate();
    	edDtInicial.onlySelectByCalendar();
    	edDtFinal.onlySelectByCalendar();
        configureListContainer();
        cargaProdutoList = new ArrayList<>();
        produtoCargaList = new ArrayList<>();
        
    }

	private void configureListContainer() {
		String sortAtributteDefault = ListContainerConfig.getDefautSortColumn(getConfigClassName());
		if (ValueUtil.isEmpty(sortAtributteDefault)) {
			sortAtributteDefault = LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos ? "CDPRODUTO" : "DSPRODUTO";
		}
		String sortAscDefault = ListContainerConfig.getDefautOrder(getConfigClassName());
		if (ValueUtil.isEmpty(sortAscDefault)) {
			sortAscDefault = ValueUtil.VALOR_SIM;
		}
		listContainer = new GridListContainer(2, 2, false, LavenderePdaConfig.usaScroolLateralListasProdutos);
		int size = 2;
		size = LavenderePdaConfig.isMostraDescricaoReferencia() ? size + 1 : size;
		int position = 2;
		String[][] matriz = new String[size][2];
		matriz[0][0] = Messages.PRODUTO_LABEL_CODIGO;
		matriz[0][1] = "CDPRODUTO";
		matriz[1][0] = Messages.PRODUTO_LABEL_DSPRODUTO;
		matriz[1][1] = "DSPRODUTO";
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSREFERENCIA;
			matriz[position][1] = "DSREFERENCIA";
			position++;
		}
		listContainer.setColsSort(matriz);
		listContainer.resizeable = true;
		listContainer.atributteSortSelected = sortAtributteDefault;
		listContainer.sortAsc = sortAscDefault;
	}

	protected String getConfigClassName() {
		return ClassUtil.getSimpleName(ListProdutoForm.class);
	}
	
	protected BaseDomain getDomainConfig() {
		ConfigInterno configInterno = new ConfigInterno();
		configInterno.cdEmpresa = SessionLavenderePda.cdEmpresa;
		configInterno.cdConfigInterno = ConfigInterno.configSortAndOrderColumn;
		configInterno.vlChave = getConfigClassName();
		configInterno.vlConfigInterno = listContainer.atributteSortSelected + ConfigInterno.defaultSeparatorInfoValue + StringUtil.getStringValue(listContainer.sortAsc);
		return configInterno;
	}

	protected void saveListConfig() {
		try {
			BaseDomain domainConfig = getDomainConfig();
			ListContainerConfig.listasConfig.put(((ConfigInterno)domainConfig).vlChave, StringUtil.split(((ConfigInterno)domainConfig).vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			if (getConfigService().findByRowKey(domainConfig.getRowKey()) == null) {
				getConfigService().insert(domainConfig);
			} else {
				getConfigService().update(domainConfig);
			}
		} catch (Throwable e) {
			//Não faz nada
		}
	}
	
	protected CrudService getConfigService() {
		return ConfigInternoService.getInstance();
	}
    //-----------------------------------------------

    //@Override
    public String getEntityDescription() {
    	return Messages.CARGAPRODUTO_NOME_ENTIDADE;
    }

    //@Override
    protected CrudService getCrudService() {
        return CargaProdutoService.getInstance();
    }
    
    //@Override
    protected BaseDomain createDomain() {
        return new CargaProduto();
    }
    
    //@Override
    protected BaseDomain screenToDomain() throws SQLException {
        return getDomain();
    }
    
    //@Override
    protected void domainToScreen(BaseDomain domain) {
    }
    
    //@Override
    protected void clearScreen() {
    	edFiltro.setText("");
    	edDtInicial.setValue(DateUtil.getCurrentDate());
    	edDtFinal.setValue(DateUtil.getCurrentDate());
    	listContainer.removeAllContainers();
    	cargaProdutoList.clear();
    	produtoCargaList.clear();
    }
    
    public void setEnabled(boolean enabled) {
    }
    
    //-----------------------------------------------
    
    //@Override
    protected void onFormStart() {
    	UiUtil.add(this, edFiltro, getLeft(), getNextY());
		UiUtil.add(this, new LabelName(Messages.CARGAPRODUTO_LABEL_PERIODO), edDtInicial, getLeft(), getNextY());
    	UiUtil.add(this, edDtFinal, AFTER + WIDTH_GAP_BIG , SAME);
    	UiUtil.add(this, listContainer, LEFT, getNextY(), FILL, FILL - barBottomContainer.getHeight());
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					if (validateFiltro()) {
						carregaGridProdutos();
					} else {
						UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, LavenderePdaConfig.qtMinimaCaracteresFiltroProduto));
					}
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (((Control)event.target).getParent() instanceof ItemContainer && event.target instanceof EditNumberFrac) {
					setSelectedItemOnTempArray();
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if (event.target != null && ((Control)event.target).getParent() instanceof ValueChooser && event.target instanceof BaseButton) {
					setSelectedItemOnTempArray();
				} 
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (((Control)event.target).getParent() instanceof ValueChooser && event.target instanceof EditNumberFrac) {
					setSelectedItemOnTempArray();
				}
				break;
			}
			case ControlEvent.WINDOW_CLOSED: {
				if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
					if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listContainer.reloadSortSettings();
						saveListConfig();
						if (listContainer.size() > 0) {
							carregaGridProdutos();
						}
					}
				}
				break;
			}
    	}
    }
    
    @Override
    protected void salvarClick() throws SQLException {
    	if (produtoCargaList == null || produtoCargaList.size() == 0) {
    		UiUtil.showErrorMessage(Messages.CARGAPRODUTO_MSG_ERRO);
    		return;
    	}
    	CargaProdutoService.getInstance().salvarCargaProduto(produtoCargaList);
    	UiUtil.showInfoMessage(Messages.CARGAPRODUTO_MSG_SUCESSO);
    	voltarClick();
    	list();
    }

	private void setSelectedItemOnTempArray() throws SQLException {
		BaseListContainer.Item item = (BaseListContainer.Item)listContainer.getSelectedItem();
		if (item != null) {
			ItemContainer itemContainer = (ItemContainer)item.rightControl;
			double qtItem = itemContainer.chooserQtd.getValue();
			Produto produto = (Produto) ProdutoService.getInstance().findByRowKey(listContainer.getSelectedId());
			int index = produtoCargaList.indexOf(produto);
			if (index != -1) {
				if (qtItem == 0) {
					produtoCargaList.remove(index);
					return;
				}
				produto = produtoCargaList.get(index);
			} else {
				produtoCargaList.add(produto);
			}
			produto.qtSolicitCargaProd = qtItem;
		}
	}

    public void carregaGridProdutos() throws SQLException {
    	CargaProdutoService.getInstance().validaPeriodo(edDtInicial.getValue(), edDtFinal.getValue());
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = null;
		try {
			listContainer.removeAllContainers();
			//--
			domainList = getProdutoList();
			listSize = domainList.size();
			Container[] all = new Container[listSize];
			//--
			if (listSize > 0) {
				BaseDomain domain;
				BaseListContainer.Item c;
				ItemContainer itemCont = null;
				for (int i = 0; i < listSize; i++) {
					domain = (BaseDomain) domainList.items[i];
					String[] items = getItemProdutoToGrid(domain);
					all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
					if (LavenderePdaConfig.isExibeFotoInsercaoMultiplos()) {
						itemCont = new ItemContainer(items, false, Util.getImageForProdutoList(domain, 0, false), campos, -1, (ProdutoBase)domain, listContainer, null, i, false);
						c.rightControl = itemCont;
						c.setupRightContainer();
					} else {
						itemCont = new ItemContainer(items, false, null, campos, -1, (ProdutoBase)domain, listContainer, null, i, false);
						c.rightControl = itemCont;
						c.setupRightContainer();
					}
					c.id = domain.getRowKey();
					c.setItens(new String[] {items[0], items[1]});
					c.setToolTip(getToolTip(domain));
					domain = null;
				}
				listContainer.addContainers(all);
			}
		} finally {
			msg.unpop();
		}
	}

	private String[] getItemProdutoToGrid(BaseDomain domain) throws SQLException {
		Produto produto = (Produto) domain;
		Vector itens = new Vector(0);
		ArrayList<CAMPOS_LISTA> campos = new ArrayList<>(16);
		campos.add(CAMPOS_LISTA.CAMPO_FIXO);
		campos.add(CAMPOS_LISTA.CAMPO_FIXO);
		//--
		ProdutoService.getInstance().addDescricaoProdutoLista(produto, itens);
		itens.addElement(Messages.CARGAPRODUTO_LABEL_QTVENDIDA + " " + StringUtil.getStringValueToInterface(produto.qtItemPedido));
		campos.add(CAMPOS_LISTA.CAMPO_FIXO); 
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoForaPedido()) {
			String dsQtEstoque = EstoqueService.getInstance().getEstoqueToString(EstoqueService.getInstance().getQtEstoqueDisponivel(produto.cdProduto))+ Messages.PRODUTO_LABEL_EM_ESTOQUE;
			itens.addElement(dsQtEstoque);
			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
		}
		//--
		this.campos = campos.toArray(new CAMPOS_LISTA[campos.size()]);
		return (String[]) itens.toObjectArray();
	}

	private Vector getProdutoList() throws SQLException {
		Produto produto = new Produto();
		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produto.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(Produto.class);
		produto.dtInicialFilter = edDtInicial.getValue();
		produto.dtFinalFilter = edDtFinal.getValue();
		produto.sortAsc = listContainer.sortAsc;
		produto.sortAtributte = listContainer.atributteSortSelected;
		return ProdutoService.getInstance().findAllProdutoAndQtdVendaPeriodo(edFiltro.getText(), produto);
	} 
    
	protected String getToolTip(Object domain) {
		StringBuffer strToolTip = new StringBuffer(50);
    	ProdutoBase produto = (ProdutoBase) domain;
    	strToolTip.setLength(0);
    	strToolTip.append(ProdutoService.getInstance().getDescricaoProdutoComReferencia(produto));
    	if (LavenderePdaConfig.ocultaColunaCdProduto) {
    		strToolTip.append(" [").append(produto.cdProduto).append("]");
    	}
    	return strToolTip.toString();
    }

	private boolean validateFiltro() {
    	String filtro = edFiltro.getText();
    	if ((filtro == null) || ((filtro.length() < LavenderePdaConfig.qtMinimaCaracteresFiltroProduto))) {
    		return false;
    	}
    	return true;
	}
}
