package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.Menu;
import br.com.wmw.framework.business.domain.Tela;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadMenuForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.MenuCarrousel;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.LavendereBaseDomain;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.SugestaoVenda;
import br.com.wmw.lavenderepda.business.domain.VideoProduto;
import br.com.wmw.lavenderepda.business.domain.VideoProdutoGrade;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.LoteProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.business.service.TelaLavendereService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.VideoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.VideoProdutoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.EstoqueLabelButton;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderProdutoWindow;
import br.com.wmw.lavenderepda.sync.LavendereWeb2Tc;
import totalcross.io.File;
import totalcross.sys.Convert;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.util.Vector;

	
public class CadProdutoMenuForm extends BaseCrudCadMenuForm {
	private static final int CDMENU_PRODUTO = 3;

    private LabelValue lbDsProduto;
    private EstoqueLabelButton lvEstoque;
	//--

    public BaseToolTip tipDsProduto;
    //--
	private BaseButton btDadosProduto;
	private BaseButton btInfoPreco;
	private BaseButton btSlideFoto;
	private BaseButton btPrecoByCondicao;
	private BaseButton btRelVendasProdCli;
	private BaseButton btProdutoLote;
	private BaseButton btDesontoPorTabela;
	private BaseButton btItensKit;
	private BaseButton btProdutosSimilares;
	private BaseButton btProdutoUnidade;
	private BaseButton btRecalculaEstoque;
	private BaseButton btEstoqueGrade;
	private BaseButton btCondComercial;
	private BaseButton btInfoEstoque;
	public boolean relNovidadeProduto;
	private BaseButton btSugestaoMultProdutos;
	private ListMultiplasSugestoesProdutosWindow listMultiplasSugestoesProdutosWindow;
	private BaseButton btVideos;
	//--
	public String cdTabPreco;
	public boolean fromSugestaoMultProdutos;
	public String cdLocalEstoque;
	public String dsLocalEstoque;

    public CadProdutoMenuForm() {
        super(Messages.PRODUTO_NOME_ENTIDADE);
        setBaseCrudCadForm(new CadProdutoDynForm());
        lbDsProduto = new LabelValue("@@@@@@@@@@");
        lbDsProduto.setForeColor(ColorUtil.sessionContainerForeColor);
        tipDsProduto = new BaseToolTip(lbDsProduto, "");
        lvEstoque = new EstoqueLabelButton(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO));
        lvEstoque.setForeColor(Color.darker(ColorUtil.sessionContainerForeColor, 20));
        lvEstoque.setFont(UiUtil.defaultFontSmall);
        lvEstoque.usaSufixoEmEstoque = true;
        //--
        menuFuncionalidades = new MenuCarrousel();
		menuFuncionalidades.percEspacamento = 20;
		menuFuncionalidades.drawnBackground = true;
		menuFuncionalidades.useTitle = false;
		menuFuncionalidades.heightButtons = (UiUtil.getScreenHeightOrigin() / 100) * 10;
		menuFuncionalidades.generateButtonsAsScMenuEvent = false;
    }

    //@Override
    public String getEntityDescription() {
    	return title;
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return ProdutoService.getInstance();
    }

    protected void visibleState() throws java.sql.SQLException {
    	barBottomContainer.setVisible(false);
    	if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
    		btItensKit.setVisible(true);
    	}
    	if (LavenderePdaConfig.getConfigGradeProduto() == 0) {
    		lvEstoque.setEnabled(true);
    	}
	    if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) {
		    lvEstoque.setVisible(!ValueUtil.getBooleanValue(((Produto) getDomain()).flIgnoraValidacao));
	    }
    }

    
    protected void domainToScreen(BaseDomain domain) throws SQLException { 
		Produto produto = (Produto) getDomain();
		lbDsProduto.setText(ProdutoService.getInstance().getDescricaoProdutoReferenciaConsideraCodigo(produto));
		tipDsProduto.setText(MessageUtil.quebraLinhas(lbDsProduto.getValue()));
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
			lvEstoque.setValue(LoteProdutoService.getInstance().getQtEstoqueLoteDisponivel(produto.cdProduto));
			return;
		}
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			ItemPedido item = new ItemPedido();
			item.cdProduto = produto.cdProduto;
			lvEstoque.setValue(RemessaEstoqueService.getInstance().getEstoqueDisponivelProduto(item));
			return;
		}
		Estoque estoque = new Estoque();
		if (LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto()) {
			produto.cdLocalEstoqueFilter = cdLocalEstoque;
			estoque = EstoqueService.getInstance().getEstoqueCentroCusto(produto);
			lvEstoque.setValue(estoque.qtEstoque, dsLocalEstoque);
		} else {
			estoque = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
			lvEstoque.setValue(estoque.qtEstoque);
		}
		if (LavenderePdaConfig.mostraEstoquePrevisto && estoque.qtEstoquePrevisto > 0) {
			try {
				TemaSistema tema = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
				CorSistema corSistema = CorSistemaLavendereService.getInstance().getCorSistema(tema.cdEsquemaCor, 194);
				lvEstoque.setForeColor(Color.brighter(Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB), 80));
			} catch (Throwable e) {
				lvEstoque.setForeColor(Color.brighter(ColorUtil.softGreen, 80));
			}
		}
    }

    protected void onFormStart() throws SQLException {
    	SessionContainer containerDadosProduto = new SessionContainer();
    	UiUtil.add(this, containerDadosProduto, LEFT, getTop(), FILL, UiUtil.getLabelPreferredHeight());
    	UiUtil.add(containerDadosProduto, lbDsProduto, getLeft(), AFTER + HEIGHT_GAP, getWFill(), PREFERRED);
		if (!LavenderePdaConfig.isOcultaEstoque() && !LavenderePdaConfig.usaLocalEstoque() && LavenderePdaConfig.getConfigGradeProduto() == 0 && !LavenderePdaConfig.usaModoControleEstoquePorTipoPedido) {
    		UiUtil.add(containerDadosProduto, lvEstoque, getLeft(), AFTER, getWFill(), PREFERRED);
    	}
    	containerDadosProduto.resizeHeight();
    	containerDadosProduto.resetSetPositions();
    	containerDadosProduto.setRect(KEEP, KEEP, FILL, containerDadosProduto.getHeight() + HEIGHT_GAP);
    	//--
    	montaMenuFuncionalidades();
    	UiUtil.add(this, menuFuncionalidades, getLeft(), AFTER, getWFill(), FILL);
    }

    private void montaMenuFuncionalidades() throws SQLException {
    	menuFuncionalidades.removeAllOptions();
    	//--
    	btDadosProduto = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_TITULO_CADASTRO);
    	if (!LavenderePdaConfig.isOcultaInfoPreco()) {
    		btInfoPreco = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_INFO_PRECO);
		}
        if (LavenderePdaConfig.isMostraPrecosPorCondicaoPagamento()) {
        	btPrecoByCondicao = menuFuncionalidades.addOptionMenu(Messages.ITEM_PRECOS_POR_CONDICAO);
        }
        if (LavenderePdaConfig.usaCondicaoComercialPedido && !LavenderePdaConfig.isOcultaPrecoCondComercial())  {
        	btCondComercial = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_LABEL_PRECO_CONDCOMERCIAL);
        }
        if (LavenderePdaConfig.relVendasProdutoPorCliente) {
			btRelVendasProdCli = menuFuncionalidades.addOptionMenu(Messages.ITEMPEDIDO_LABEL_RELVENPRODPORCLI);
		}
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil || LavenderePdaConfig.pctVidaUtilLoteProdutoCritico > 0) {
			btProdutoLote = menuFuncionalidades.addOptionMenu(Messages.ITEM_PRODUTO_LOTE);
		}
        if (LavenderePdaConfig.mostraDescontosPorTabelaPreco && !LavenderePdaConfig.isOcultaDescTabela()) {
        	btDesontoPorTabela = menuFuncionalidades.addOptionMenu(Messages.ITEM_DESCONTO_POR_TABELA);
        }
        if (LavenderePdaConfig.usaSimilarVendaProduto) {
        	btProdutosSimilares = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_LABEL_PRODUTOS_SIMILARES);
        }
        if (LavenderePdaConfig.usaUnidadeAlternativa && !LavenderePdaConfig.usaNuConversaoUnidadePorItemTabelaPreco) {
        	btProdutoUnidade = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_LABEL_PRODUTOUNIDADE);
        }
		if (!LavenderePdaConfig.isOcultaEstoque() && LavenderePdaConfig.isUsaGradeProduto1A4()) {
        	btEstoqueGrade = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_LABEL_ESTOQUE);
        }
        if (LavenderePdaConfig.isMostraFotoProduto()) {
        	btSlideFoto = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_BOTAO_FOTOS_PRODUTOS);
        }
        if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
        	btItensKit = menuFuncionalidades.addOptionMenu(Messages.PRODUTOKIT_NOME_ENTIDADE);
        }
        if (LavenderePdaConfig.atualizarEstoqueInterno && !LavenderePdaConfig.ocultaRecalculaEstoque) {
        	btRecalculaEstoque = menuFuncionalidades.addOptionMenu(Messages.PRODUTO_LABEL_RECALCULA_ESTOQUE);
        }
        if (LavenderePdaConfig.usaModoControleEstoquePorTipoPedido) {
			btInfoEstoque = menuFuncionalidades.addOptionMenu(Messages.ESTOQUE_INFO_LOCAL_ESTOQUE);
		}
        if (LavenderePdaConfig.apresentaMultiplasSugestoesLocaisAdicionais(SugestaoVenda.LOCAL_MENU_PRODUTOS) && LavenderePdaConfig.usaMultiplasSugestoesProdutoFechamentoPedidoProdComplemento()) {
        	btSugestaoMultProdutos = menuFuncionalidades.addOptionMenu(Messages.PEDIDO_TITULO_SUGESTAO_MULTIPLOS_PRODUTOS);
        }
		if (LavenderePdaConfig.isUsaConfigVideosProdutos() || LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			btVideos = menuFuncionalidades.addOptionMenu(Messages.MENU_FUNCIONALIDADES_PRODUTO_VIDEOS);
		}
        adicionaMenusDinamicos(CDMENU_PRODUTO);
    }
    
    @Override
    protected Tela findTelaDinamica(Menu menu) throws SQLException {
    	Tela tela = TelaLavendereService.getInstance().getTela(menu.cdSistema, menu.cdTela);
    	Produto produto = (Produto) getDomain();
    	tela.valoresFiltroTelaPaiHash = new HashMap<String, String>();
    	tela.valoresFiltroTelaPaiHash.put(LavendereBaseDomain.NMCOLUNA_CDEMPRESA.toLowerCase(), produto.cdEmpresa);
    	tela.valoresFiltroTelaPaiHash.put(LavendereBaseDomain.NMCOLUNA_CDREPRESENTANTE.toLowerCase(), produto.cdRepresentante);
    	tela.valoresFiltroTelaPaiHash.put(Produto.SORT_COLUMN_CDPRODUTO.toLowerCase(), produto.cdProduto);
    	tela.transfereValoresFiltroParaCamposTelaFilho();
    	return tela;
    }
    
    @Override
    protected void showRelDinamica(Tela tela) throws SQLException {
    	show(new ListRelDinamicoLavendereForm(tela));
    }

	@Override
    protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btDadosProduto) {
	    			btInfoProdutosClick();
	    		} else if (event.target == btInfoPreco) {
	    			btInfoPrecoClick();
	    		} else if (event.target == btSlideFoto) {
	    			btSlideFotosClick();
	    		} else if (event.target == btEstoqueGrade) {
	    			btEstoqueGradeClick();
	    		} else if (event.target == btPrecoByCondicao) {
	    			btPrecoByCondicaoClick(false);
	    		} else if (event.target == btRelVendasProdCli) {
	    			btRelVendasProdCliClick(true);
	    		} else if (event.target == btProdutoLote) {
	    			btProdutoLoteClick();
	    		} else if (event.target == btDesontoPorTabela) {
	    			showDescontoPorTabela();
	    		} else if (event.target == btItensKit) {
	    			showItensKit();
	    		} else if (event.target == lvEstoque.btAcao) {
	    			atualizadoInfoEstoque();
	    		} else if (event.target == btProdutoUnidade) {
	    			btProdutoUnidadeClick();
				} else if (event.target == btProdutosSimilares) {
					btProdutoSimilarClick();
				} else if (event.target == btRecalculaEstoque) {
					btRecalculaEstoqueClick();
				} else if (event.target == btCondComercial) {
					btCondComercialClick();
				} else if (event.target == btInfoEstoque) {
					btInfoEstoqueClick();
				} else if (event.target == btSugestaoMultProdutos) {
					btSugestaoMultProdutosClick();
				} else if (event.target == btVideos) {
					btVideosClick();
				}
	    		break;
	    	}
    	}
    }

	// *******************************************************************************************
    // Ações ------------------------------------------------------------------------------------
    // *******************************************************************************************


	private void btProdutoUnidadeClick() throws SQLException {
		InfoPrecoProdutoUnidadeForm infoPrecoProdutoUnidadeForm = new InfoPrecoProdutoUnidadeForm((Produto) getDomain());
		show(infoPrecoProdutoUnidadeForm);
	}

	private void btInfoProdutosClick() throws SQLException {
		Produto produto = (Produto) getDomain();
		BaseDomain domain = getCrudService().findByRowKeyDyn(produto.getRowKey());
		btInfoProdutosClick(domain);
	}

	protected void btInfoProdutosClick(BaseDomain produto) throws SQLException {
		if (getBaseCrudCadForm() != null) {
			getBaseCrudCadForm().edit(produto);
			show(getBaseCrudCadForm());
		}
	}

	private void btInfoEstoqueClick() throws SQLException {
		InfoEstoqueProdutoForm infoEstoqueProdutoForm = new InfoEstoqueProdutoForm((Produto) getDomain());
		show(infoEstoqueProdutoForm);
	}

	private void btInfoPrecoClick() throws SQLException {
		InfoPrecoProdutoForm infoPrecoProdutoForm = new InfoPrecoProdutoForm((Produto) getDomain());
		show(infoPrecoProdutoForm);
	}
	
	private void btSlideFotosClick() throws SQLException {
		ImageSliderProdutoWindow imageSliderProdutoWindow = new ImageSliderProdutoWindow((Produto) getDomain());
		imageSliderProdutoWindow.popup();
	}

	private void btPrecoByCondicaoClick(boolean relDifPedido) throws SQLException {
		Produto produto = (Produto) getDomain();
    	RelPrecosPorCondicaoWindow relCondicaoForm = new RelPrecosPorCondicaoWindow(null, new Cliente(), produto, true);
    	relCondicaoForm.popup();
	}

	private void btRelVendasProdCliClick(boolean relDifPedido) throws SQLException {
		Produto produto = (Produto) getDomain();
		RelVendasProdutoPorClienteWindow rel = new RelVendasProdutoPorClienteWindow(produto.cdProduto);
		rel.popup();
	}

	public void showDescontoPorTabela() throws SQLException {
		RelDescontoPorTabelaPrecoWindow relDescontoTabelaForm = new RelDescontoPorTabelaPrecoWindow((Produto) getDomain());
		relDescontoTabelaForm.popup();
	}

	public void showItensKit() throws SQLException {
		Produto produto = (Produto) getDomain();
		if (produto.isKit()) {
			ListProdutoKitForm relDescontoTabelaForm = new ListProdutoKitForm(produto);
			show(relDescontoTabelaForm);
		} else {
			UiUtil.showInfoMessage(Messages.PRODUTOKIT_MSG_PRODUTO_NAO_KIT, UiUtil.DEFAULT_MESSAGETIME_SHORT);
		}
	}

	private void btProdutoLoteClick() throws SQLException {
		Produto produto = (Produto) getDomain();
		RelProdutoLotesWindow relCondicaoWindow = new RelProdutoLotesWindow(produto, null, true);
		relCondicaoWindow.popup();
	}

	protected void btProdutoSimilarClick() throws SQLException {
		Produto produto = (Produto) getDomain();
		ListProdutoSimilarWindow listProdutoSimilarWindow = new ListProdutoSimilarWindow(produto, true, null, null);
		if (listProdutoSimilarWindow.possuiProdutoSimilares() && (!LavenderePdaConfig.usaAgrupadorProdutoSimilar || ValueUtil.isNotEmpty(produto.cdAgrupProdSimilar))) {
			listProdutoSimilarWindow.popup();
		} else {
			UiUtil.showInfoMessage(Messages.PRODUTO_MSG_NAO_POSSUI_PRODUTOS_SIMILARES);
		}
	}
	
	protected void btRecalculaEstoqueClick() throws SQLException {
		Produto produto = (Produto) getDomain();
		EstoqueService.getInstance().recalculaEstoqueConsumido(produto.cdProduto);
		double qtEstoque;
		if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
			ItemPedido item = new ItemPedido();
			item.cdProduto = produto.cdProduto;
			qtEstoque = RemessaEstoqueService.getInstance().getEstoqueDisponivelProduto(item);
			lvEstoque.setValue(qtEstoque);
		} else {
			qtEstoque = EstoqueService.getInstance().getQtEstoque(produto.cdProduto, Estoque.CD_LOCAL_ESTOQUE_PADRAO);
			lvEstoque.setValue(qtEstoque);
		}
		lvEstoque.setValue(qtEstoque);
		if (produto.estoque == null) {
			produto.estoque = new Estoque();
			produto.estoque.qtEstoque = qtEstoque;
		}
		if (!relNovidadeProduto) {
			getBaseCrudListForm().updateCurrentRecord(getDomain());
		}
		UiUtil.showInfoMessage(Messages.ITEMPEDIDO_MSG_ESTOQUE_RECALCULADO);
	}

	protected void btEstoqueGradeClick() throws SQLException {
		ListEstoqueGradeWindow listEstoqueGradeWindow = new ListEstoqueGradeWindow((Produto) getDomain(), null, null, 0, new Vector(), false, null);
		listEstoqueGradeWindow.popup();
	}
	
	protected void btCondComercialClick() throws SQLException {
		Produto produto = (Produto) getDomain();
    	RelPrecosPorCondicaoComercialWindow relCondicaoForm = new RelPrecosPorCondicaoComercialWindow(produto);
    	relCondicaoForm.popup();
	}

	private void atualizadoInfoEstoque() {
		try {
			ParamsSync ps = HttpConnectionManager.getDefaultParamsSync();
			ps.nuMaxTentativas = 1;
			LavendereWeb2Tc lwWeb2Tc = new LavendereWeb2Tc(ps);
			Produto produto = (Produto) getDomain();
			String newEstoque = lwWeb2Tc.getEstoqueServidorAndUpdatePda(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, produto.cdProduto, null);
			Estoque estoque = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_PDA);
			lvEstoque.setValue(ValueUtil.getDoubleValue(newEstoque) - estoque.qtEstoque);
			Estoque estoqueErp = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
			estoqueErp.qtEstoque = ValueUtil.getDoubleValue(newEstoque);
			produto.estoque = estoqueErp;
			if(getBaseCrudListForm() != null) {
				getBaseCrudListForm().updateCurrentRecord(produto);
			}
		} catch (ValidationException e) {
			throw new ValidationException(e.getMessage());
		} catch (Throwable e) {
			LogPdaService.getInstance().logSyncError("Erro ao atualizar o estoque. " + e.getMessage());
			throw new ValidationException(Messages.ESTOQUE_MSG_SYNC_FAIL);
		}
	}

	private void btSugestaoMultProdutosClick() throws SQLException {
		Produto produto = (Produto) getDomain();
		if (listMultiplasSugestoesProdutosWindow == null) {
			listMultiplasSugestoesProdutosWindow = new ListMultiplasSugestoesProdutosWindow(produto, this.cdTabPreco);
			listMultiplasSugestoesProdutosWindow.setCadProdutoMenuForm(this);
		} else {
			listMultiplasSugestoesProdutosWindow.list();
		}
		if (listMultiplasSugestoesProdutosWindow.hasSugestoes()) {
			listMultiplasSugestoesProdutosWindow.popup();
		} else {
			UiUtil.showInfoMessage(Messages.PRODUTO_SEM_SUGESTAO_MULTIPLOS);
			invalidateListMultProdutos();
		}
	}

	private void btVideosClick() throws SQLException {
		Vector videoProdutoList = new Vector();
		Vector videoProdutoGradeList = new Vector();
		if (LavenderePdaConfig.isUsaConfigVideosProdutos()) {
			VideoProduto videoProdutoFilter = new VideoProduto();
			videoProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			videoProdutoFilter.cdProduto = ((Produto)getDomain()).cdProduto;
			videoProdutoList = VideoProdutoService.getInstance().findAllByExample(videoProdutoFilter);
		}
		if (LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			VideoProdutoGrade videoProdutoGradeFilter = new VideoProdutoGrade();
			videoProdutoGradeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			videoProdutoGradeFilter.dsAgrupadorGrade = ((Produto)getDomain()).getDsAgrupadorGrade();
			if (videoProdutoGradeFilter.dsAgrupadorGrade != null) {
				videoProdutoGradeList = VideoProdutoGradeService.getInstance().findAllByExample(videoProdutoGradeFilter);
			}
		}
		if (videoProdutoList.size() + videoProdutoGradeList.size() == 1) {
			abreVideoDisponivel(videoProdutoList, videoProdutoGradeList);
		} else if (videoProdutoList.size() + videoProdutoGradeList.size() > 0) {
			abreListaVideosDisponiveis(videoProdutoList, videoProdutoGradeList);
		} else {
			UiUtil.showWarnMessage(Messages.VIDEOS_NENHUM_VIDEO_DISPONIVEL);
		}
	}

	private void abreListaVideosDisponiveis(Vector videoProdutoList, Vector videoProdutoGradeList) {
		ListVideosWindow listVideosWindow = new ListVideosWindow(videoProdutoList, videoProdutoGradeList);
		listVideosWindow.popup();
	}

	private void abreVideoDisponivel(Vector videoProdutoList, Vector videoProdutoGradeList) {
		String path;
		String pathVideo;
		LavendereBaseDomain domain;
		if (videoProdutoList.size() == 1) {
			domain = (LavendereBaseDomain) videoProdutoList.elementAt(0);
			pathVideo = VideoProduto.getPathVideo();
		} else {
			domain = (LavendereBaseDomain) videoProdutoGradeList.elementAt(0);
			pathVideo = VideoProdutoGrade.getPathVideo();
		}
		pathVideo = Convert.appendPath(Convert.appendPath(pathVideo, SessionLavenderePda.cdEmpresa), domain.getCdDomain());
		path = VmUtil.isSimulador() ? "file:///" + pathVideo : pathVideo;
		try (File videoFile = new File(pathVideo)) {
			if (videoFile.exists()) {
				UiUtil.videoViewer(path);
			} else {
				UiUtil.showWarnMessage(Messages.VIDEO_NAO_ENCONTRADO);
			}
		} catch (Throwable e) {
			UiUtil.showWarnMessage(Messages.VIDEO_NAO_ENCONTRADO);
		}
	}

	public void invalidateListMultProdutos() {
		listMultiplasSugestoesProdutosWindow = null;
	}

	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		if (fromSugestaoMultProdutos) {
			fromSugestaoMultProdutos = false;
			btSugestaoMultProdutosClick();
		}
	}
}
