package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.LinkedHashMap;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCatalogForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonCatalog;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.FotoUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.enums.GroupTypeFile;
import br.com.wmw.framework.util.enums.TypeFile;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.MenuCatalogo;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.dto.PedidoDTO;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.MenuCatalogoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import totalcross.io.File;
import totalcross.io.FileStates;
import totalcross.json.JSONObject;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Vector;

public class MenuCatalogoForm extends BaseCatalogForm {

    private static final TypeFile[] SUPPORTED_IMAGE_EXTENSIONS = GroupTypeFile.IMAGE_TC.getTypeFiles();
    public ButtonOptions btOpcoes;
    private final MenuCatalogo menuCatalogo;
    private final CadPedidoForm cadPedidoForm;

    private final ButtonAction btListaItens;
    private final ButtonAction btGiroProduto;
    private Vector menuCatalogoIndividualList;

    public MenuCatalogoForm(MenuCatalogo menuCatalogo, CadPedidoForm cadPedidoForm) throws SQLException {
        super(menuCatalogo.nmMenu);
        this.menuCatalogo = menuCatalogo;
        useProgressBar = true;
        itensPerLine = LavenderePdaConfig.getQuantidadeItensPorLinhaMenuCatalogo();
        if (!LavenderePdaConfig.isApresentaTextoPadraoMenuCatalogo()) {
            setContainerButtonStyle();
        }
        this.cadPedidoForm = cadPedidoForm;
        if (cadPedidoForm != null) {
            PedidoDTO pedidoDTO = new PedidoDTO(cadPedidoForm.getPedido());
            this.menuCatalogo.pedidoJsonFilters = new JSONObject(pedidoDTO);
        }
        btOpcoes = new ButtonOptions();
        btOpcoes.setID("btOpcoes");
        btListaItens = new ButtonAction(Messages.BOTAO_ITENS_DO_PEDIDO, "images/list.png");
        btListaItens.setID("btListaItens");
		btGiroProduto = new ButtonAction(Messages.GIROPRODUTO_NOME_ENTIDADE, "images/giroproduto.png");
    }

    @Override
    protected void onButtonClick(ButtonCatalog clickedButtonCatalog) {
        super.onButtonClick(clickedButtonCatalog);
        if (menuCatalogo.isIndividual()) {
            handleNivelIndividualClick(clickedButtonCatalog);
        } else {
            handleNextNivelClick(clickedButtonCatalog);
        }
    }

    private void handleNivelIndividualClick(ButtonCatalog clickedButtonCatalog) {
        if (isMenuCatalogoAtualFuncionalidadeItemPedidoEFiltroProduto()) {
            try {
                showVendaUnitaria((LinkedHashMap<String, String>) clickedButtonCatalog.appObj);
            } catch (ValidationException ve) {
                throw ve;
            } catch (Exception e) {
                ExceptionUtil.handle(e);
            }
        } else if (isMenuCatalogoAtualFuncionalidadeProdutoEFiltroProduto()) {
            try {
                showMenuProduto(clickedButtonCatalog);
            } catch (ValidationException ve) {
                throw ve;
            } catch (Exception e) {
                ExceptionUtil.handle(e);
            }
        }
    }

    private void handleNextNivelClick(ButtonCatalog clickedButtonCatalog) {
        MenuCatalogo filter = new MenuCatalogo();
        filter.cdFuncionalidade = this.menuCatalogo.cdFuncionalidade;
        filter.nuNivel = menuCatalogo.nuNivel + 1;
        filter.limit = 1;
        try {
        	MenuCatalogo nextMenuCatalogo = (MenuCatalogo) MenuCatalogoService.getInstance().findByPrimaryKey(filter);
            if (nextMenuCatalogo != null) {
                nextMenuCatalogo.pedidoJsonFilters = this.menuCatalogo.pedidoJsonFilters;
                if (isProximaFuncionalidadeItemPedidoEFiltroProduto(nextMenuCatalogo)) {
                    showNivelItemPedido(clickedButtonCatalog, nextMenuCatalogo);
                } else {
                    showNovoNivelCatalog(clickedButtonCatalog, nextMenuCatalogo);
                }
            } else if (isMenuCatalogoAtualFuncionalidadeProdutoEFiltroProduto()) {
                showMenuProduto(clickedButtonCatalog);
            } else if (isMenuCatalogoAtualFuncionalidadeItemPedidoEFiltroProduto()) {
                showVendaUnitaria((LinkedHashMap<String, String>) clickedButtonCatalog.appObj);
            }
        } catch (ValidationException ve) {
            throw ve;
        } catch (Exception e) {
            ExceptionUtil.handle(e);
        }
    }

    private boolean isProximaFuncionalidadeItemPedidoEFiltroProduto(MenuCatalogo filter) {
        return ItemPedido.SIMPLE_NAME.equalsIgnoreCase(filter.cdFuncionalidade) && Produto.SIMPLE_NAME.equalsIgnoreCase(filter.nmEntidade);
    }

    private boolean isMenuCatalogoAtualFuncionalidadeItemPedidoEFiltroProduto() {
        return ItemPedido.SIMPLE_NAME.equalsIgnoreCase(menuCatalogo.cdFuncionalidade) && Produto.SIMPLE_NAME.equalsIgnoreCase(menuCatalogo.nmEntidade);
    }

    private boolean isMenuCatalogoAtualFuncionalidadeProdutoEFiltroProduto() {
        return Produto.SIMPLE_NAME.equalsIgnoreCase(menuCatalogo.cdFuncionalidade) && Produto.SIMPLE_NAME.equalsIgnoreCase(menuCatalogo.nmEntidade);
    }

    private void showMenuProduto(ButtonCatalog target) throws SQLException {
        LinkedHashMap<String, String> telaFilter = (LinkedHashMap<String, String>) target.appObj;
        Produto produtoBanco = findProdutoBanco(telaFilter);
        CadProdutoMenuForm cadProdutoMenuForm = new CadProdutoMenuForm();
        cadProdutoMenuForm.edit(produtoBanco);
        show(cadProdutoMenuForm);
    }

    @Override
    protected void filtrarClick() throws SQLException {
        super.filtrarClick();
        menuCatalogo.dsFiltro = edFiltro.getText();
        clearItems();
        addMenuItens();
    }

    private void showNivelItemPedido(ButtonCatalog clickedButtonCatalog, MenuCatalogo nextMenuCatalogo) throws SQLException {
        LinkedHashMap<String, String> produtoAsMap;
        nextMenuCatalogo.valoresFiltroTelaPaiHash = (LinkedHashMap<String, String>) clickedButtonCatalog.appObj;
        Vector produtosFiltered = MenuCatalogoService.getInstance().findRegistrosEntidadeSql(nextMenuCatalogo);
        if (produtosFiltered.size() > 1) {
            showNovoNivelCatalog(clickedButtonCatalog, nextMenuCatalogo);
        } else if (produtosFiltered.size() == 1) {
            produtoAsMap = (LinkedHashMap<String, String>) produtosFiltered.elementAt(0);
            showVendaUnitaria(produtoAsMap);
        } else {
            throw new ValidationException(Messages.ERRO_VALIDACAO_NENHUM_PRODUTO_VALIDA_NIVEL_CATALOGO);
        }
    }

    private void showVendaUnitaria(LinkedHashMap<String, String> produtoAsMap) throws SQLException {
        Produto produtoBanco = findProdutoBanco(produtoAsMap);
        validaPrecoProdutoAntesIniciarVenda(produtoBanco);

        UiUtil.showLoadingScreen();
        CadItemPedidoForm cadItemPedidoFormInstance = CadItemPedidoForm.getNewCadItemPedido(cadPedidoForm, cadPedidoForm.getPedido());
        cadItemPedidoFormInstance.fromMenuCatalogForm = true;
        cadItemPedidoFormInstance.setProdutoSelecionado(produtoBanco);
        cadItemPedidoFormInstance.addNovoItem();
        try {
            show(cadItemPedidoFormInstance);
            cadItemPedidoFormInstance.gridClickAndRepaintScreen();
        } finally {
            UiUtil.closeLoadingScreen();
        }
    }

    private void validaPrecoProdutoAntesIniciarVenda(Produto produtoBanco) throws SQLException {
        double precoProduto = ItemTabelaPrecoService.getInstance().getPrecoProduto(produtoBanco, cadPedidoForm.getPedido());
        if (precoProduto == 0) {
            throw new ValidationException(Messages.ERRO_VALIDACAO_NENHUM_PRECO_VALIDO_PRODUTO_NIVEL_CATALOGO);
        }
    }

    private Produto findProdutoBanco(LinkedHashMap<String, String> firstProdutoFiltered) throws SQLException {
        ProdutoBase produtoBase = new ProdutoBase("");
        produtoBase.cdEmpresa = firstProdutoFiltered.get(Empresa.NMCOLUNA_CDEMPRESA);
        produtoBase.cdRepresentante = firstProdutoFiltered.get(Representante.NMCOLUNA_CDREPRESENTANTE);
        produtoBase.cdProduto = firstProdutoFiltered.get(ProdutoBase.NMCOLUNA_CDPRODUTO);
        return (Produto) ProdutoService.getInstance().findByRowKey(produtoBase.getRowKey());
    }

    private void showNovoNivelCatalog(ButtonCatalog target, MenuCatalogo filter) throws SQLException {
        filter.valoresFiltroTelaPaiHash = (LinkedHashMap<String, String>) target.appObj;
        MenuCatalogoForm nextMenuCatalogoForm = new MenuCatalogoForm(filter, cadPedidoForm);
        show(nextMenuCatalogoForm);
    }

    @Override
    protected Image getButtonImage(int buttonSize, String nmImage) {
        try {
        	String imagePath = null;
            for (TypeFile typeFile : SUPPORTED_IMAGE_EXTENSIONS) {
                try {
                    imagePath = FotoUtil.getPathImg(menuCatalogo.nmEntidade).concat(nmImage).concat(typeFile.getExtencao());
                    if (FileUtil.exists(imagePath)) {
                    	try (File f = new File(imagePath, FileStates.READ_ONLY)) {
                    		return UiUtil.getSmoothScaledImage(new Image(f), buttonSize, buttonSize);
                    	}
                    }
                } catch (Exception e) {
                    ExceptionUtil.handle(e);
				}
            }
        } catch (Exception e) {
        	ExceptionUtil.handle(e);
        }
        return getImagemSemImagem(buttonSize);
    }

    @Override
    protected int countDomainList() throws SQLException {
        return MenuCatalogoService.getInstance().countRegistrosEntidadeSql(menuCatalogo);
    }

    @Override
    protected Vector getDomainList() throws SQLException {
        return MenuCatalogoService.getInstance().findRegistrosEntidadeSql(menuCatalogo);
    }

    @Override
    protected Object getItemAppObj(Object elementAt, int i) {
        return elementAt;
    }

    @Override
    protected void onLoadButtonError(Exception e) {

    }

    @Override
    protected String getItemImage(Object elementAt) {
        LinkedHashMap<String, String> registroArray = (LinkedHashMap<String, String>) elementAt;
        if (registroArray.get(MenuCatalogo.NM_COLUNA_NMFOTO) != null) {
            return StringUtil.getStringValue(registroArray.get(MenuCatalogo.NM_COLUNA_NMFOTO));
        } else {
            return StringUtil.getStringValue(registroArray.get(menuCatalogo.nmColunaDescricao));
        }
    }

    @Override
    protected String getItemText(Object elementAt) {
        LinkedHashMap<String, String> registroArray = (LinkedHashMap<String, String>) elementAt;
        return StringUtil.getStringValue(registroArray.get(menuCatalogo.nmColunaDescricao));
    }

    @Override
    protected void addBottomBarButtons() throws SQLException {
        if (LavenderePdaConfig.isUsaGiroProduto() && cadPedidoForm != null) {
            Cliente cliente = cadPedidoForm.getPedido().cliente;
            if (!cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
                UiUtil.add(barBottomContainer, btGiroProduto, 1);
            }
        }
        if (!LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido && cadPedidoForm != null) {
            UiUtil.add(barBottomContainer, btListaItens, 2);
        }
        UiUtil.add(barBottomContainer, btOpcoes, 3);
        addItensOnButtonMenu();
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
        super.onFormEvent(event);
        switch (event.type) {
            case ControlEvent.PRESSED: {
                if (event.target == btListaItens && cadPedidoForm != null) {
                    btListaItensClick();
                } else if (event.target == btGiroProduto && cadPedidoForm != null) {
                	btGiroProdutoClick();
                }
                break;
            }
            case ButtonOptionsEvent.OPTION_PRESS: {
                if (event.target == btOpcoes) {
                    for (int i = 0; i < menuCatalogoIndividualList.size(); i++) {
                        MenuCatalogo menuCatalogoIndividual = (MenuCatalogo) menuCatalogoIndividualList.elementAt(i);
                        if (btOpcoes.selectedItem.equals(menuCatalogoIndividual.nmMenu)) {
                            MenuCatalogoForm menuCatalogoIndividualForm = new MenuCatalogoForm(menuCatalogoIndividual, cadPedidoForm);
                            show(menuCatalogoIndividualForm);
                        }
                    }
                }
            }
        }
    }

    private void btListaItensClick() throws SQLException {
        ListItemPedidoForm listItemPedidoForm;
        cadPedidoForm.inRelatorioMode = true;
        if (cadPedidoForm.getPedido().isPedidoTroca()) {
            listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC);
        } else if (cadPedidoForm.getPedido().isPedidoBonificacao()) {
            listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO);
        } else {
            listItemPedidoForm = ListItemPedidoForm.getNewListItemPedido(cadPedidoForm, cadPedidoForm.getPedido(), TipoItemPedido.TIPOITEMPEDIDO_NORMAL);
        }
        listItemPedidoForm.show();
    }
    
    private void btGiroProdutoClick() throws SQLException {
    	ListGiroProdutoWindow listGiroProdutoWindow = new ListGiroProdutoWindow(null, cadPedidoForm, cadPedidoForm.getPedido(), false, true, false);
		if (listGiroProdutoWindow.hasGiroProduto) {
			listGiroProdutoWindow.popup();
		}
    }

    @Override
    protected boolean isShowButtonText(String text, Image image) {
        return LavenderePdaConfig.isApresentaTextoPadraoMenuCatalogo() || image.getPath().contains("nophoto") || Produto.SIMPLE_NAME.equalsIgnoreCase(menuCatalogo.nmEntidade);
    }

    @Override
    public void onFormExibition() throws SQLException {
        super.onFormExibition();
        edFiltro.clear();
    }

    @Override
    protected void addItensOnButtonMenu() throws SQLException {
        super.addItensOnButtonMenu();
        menuCatalogoIndividualList = MenuCatalogoService.getInstance().findAllMenuCatalogoIndividual(menuCatalogo.cdFuncionalidade);
        for (int i = 0; i < menuCatalogoIndividualList.size(); i++) {
            MenuCatalogo menuCatalogoIndividual = (MenuCatalogo) menuCatalogoIndividualList.elementAt(i);
            btOpcoes.addItemAt(menuCatalogoIndividual.nmMenu, i);
        }
    }
}
