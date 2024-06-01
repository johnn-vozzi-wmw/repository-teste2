package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoOpcaoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.AtributoProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CestaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescProgFamiliaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescProgressivoConfigComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescPromocionalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.EstoqueDisponivelComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoDescProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto2ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto3ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto4ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.KitComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.LocalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.LocalEstoqueComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.NaoPositivadoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.PacoteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoDestaqueComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoDescProgConfigFamComboBox;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class FiltroProdutoAvancadoWindow extends WmwWindow {
	
	private static final String HASHKEY_PA = "PA";
	private static final String HASHKEY_AP = "AP";
	private static final String HASHKEY_DS = "DS";
	private static final String HASHKEY_CD = "CD";

	public AtributoProdComboBox cbAtributoProd;
	public AtributoOpcaoProdComboBox cbAtributoOpcaoProd;
	public FornecedorComboBox cbFornecedor;
	public EditFiltro edFiltro;
	public ButtonPopup btFiltrar;
	public ButtonPopup btLimpar;
	public PushButtonGroupBase btGroupTipoFiltros;
	public boolean filtroRealizado;
	public boolean filterByPrincipioAtivo;
	public boolean filterByAplicacaoProduto;
	public boolean filterByCodigoProduto;
	public GrupoProduto1ComboBox cbGrupoProduto1;
	public GrupoProduto2ComboBox cbGrupoProduto2;
	public GrupoProduto3ComboBox cbGrupoProduto3;
	public GrupoProduto4ComboBox cbGrupoProduto4;
	public DescPromocionalComboBox cbDescPromocional;
	public CheckBoolean ckPreAltaProduto;
	public CheckBoolean ckProdutoPromocional;
	public CheckBoolean ckProdutoDescPromocional;
	public LocalComboBox cbLocal;
	public ProdutoDestaqueComboBox cbProdutoGrupoDestaque;
	public PacoteComboBox cbPacote;
	public CheckBoolean ckProdutoOportunidade;
	public CheckBoolean ckProdutoKit;
	public CheckBoolean ckProdutoVendido;
	public CheckBoolean ckDescMaxProdCli;
	public KitComboBox cbKit;
	public CestaComboBox cbCesta;
	public NaoPositivadoComboBox cbNaoPositivado;
	private LabelName lbProduto;
	private LabelName lbFornecedor;
	private LabelName lbAtributoProd;
	private LabelName lbAtributoOpcaoProd;
	private LabelName lbGrupoProduto1;
	private LabelName lbGrupoProduto2;
	private LabelName lbGrupoProduto3;
	private LabelName lbGrupoProduto4;
	private LabelName lbKit;
	private LabelName lbCesta;
	private LabelName lbProdutoGrupoDestaque;
	public LabelName lbComissaoMinima;
	public LabelName lbComissaoMaxima;
	public EditNumberFrac edComissaoMinima;
	public EditNumberFrac edComissaoMaxima;
	public LocalEstoqueComboBox cbLocalEstoque;
	public GrupoDescProdComboBox cbGrupoDescProd;
	public DescProgressivoConfigComboBox cbDescProgressivoConfig;
	public TipoDescProgConfigFamComboBox cbDescProgConfigFam;
	public DescProgFamiliaComboBox cbDescProgFamilia;
	public EstoqueDisponivelComboBox cbEstoqueDisponivel;
	public CheckBoolean ckApenasItensAdicionados;
	public MarcadorMultiComboBox cbMarcador;
	public CheckBoolean ckProdutoDescQtd;

	private boolean exibeFiltroFornecedor;
	private boolean exibeFiltroGrupoProduto;
	private boolean exibeFiltroAtributoProduto;
	private boolean exibeFiltroAvisoPreAlta;
	private boolean exibeFiltroApenasKit;
	private boolean exibeFiltroApenasProdutoVendidoMesCorrente;
	private boolean exibeFiltroProdutoDescPromocional;
	private boolean exibeFiltroProdutoGrupoDestaque;
	private boolean exibeFiltroPacote;
	private boolean exibeFiltroLocalEstoque;
	private boolean showAllFiltros;
	private boolean exibeFiltroProdutoPromocional;
	private boolean exibeFiltroKitProduto;
	private boolean exibeFiltroCampanhaVendas;
	private boolean exibeFiltroProdutoOportunidade;
	private boolean exibeFiltroGrupoDescProd;
	private boolean exibeFiltroDescPromocional;
	private boolean exibeFiltroComissao;
	private boolean exibeFiltroLocal;
	private boolean exibeFiltroDescMaxProdCli;
	private boolean exibeFiltroDescProgressivoPersonalizado;
	private boolean exibeFiltroEstoqueDisponivel;
	private boolean exibeFiltroItensInseridos;
	private boolean exibeFiltroMarcador;
	private boolean exibeFiltroProdutoComDescQtd;
	
	private Pedido pedido;
	private HashMap<String, Integer> posicoesbtGroupTipoFiltros = new HashMap<String,Integer>();
	private HashMap<String, Boolean> filtrosVisiveisTelaPrincipalMap = new HashMap<String, Boolean>();

	public FiltroProdutoAvancadoWindow() throws SQLException {
		this(null, "");
	}

	public FiltroProdutoAvancadoWindow(Pedido pedido, String cdTabelaPreco) throws SQLException {
		super(Messages.PRODUTO_FILTRO_AVANCADO);
		this.pedido = pedido;
		//--
		lbAtributoProd = new LabelName(Messages.ITEMPEDIDO_LABEL_ATRIBUTO);
		lbAtributoOpcaoProd = new LabelName(Messages.ITEMPEDIDO_LABEL_ATOPCAO);
		cbAtributoProd = new AtributoProdComboBox(lbAtributoProd.getValue());
		cbAtributoOpcaoProd = new AtributoOpcaoProdComboBox(lbAtributoOpcaoProd.getValue());
		cbAtributoOpcaoProd.drawBackgroundWhenDisabled = true;
		cbAtributoProd.load();
		cbAtributoProd.setSelectedIndex(0);
		cbAtributoOpcaoProd.setSelectedIndex(0);
		//--
		lbProduto = new LabelName(Messages.PRODUTO_NOME_ENTIDADE);
		btFiltrar = new ButtonPopup(Messages.BOTAO_FILTRAR);
		edFiltro = new EditFiltro("999999999", 100);
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		//--
		inicializaBtGroupTipoFiltros();
		//--
		cbFornecedor = new FornecedorComboBox();
		cbFornecedor.setSelectedIndex(0);
		if (LavenderePdaConfig.usaFiltroFornecedor) {
			lbFornecedor = new LabelName(Messages.FORNECEDOR_NOME_ENTIDADE);
		}
		//--
		cbGrupoProduto1 = new GrupoProduto1ComboBox();
		cbGrupoProduto2 = new GrupoProduto2ComboBox();
		cbGrupoProduto3 = new GrupoProduto3ComboBox();
		cbGrupoProduto4 = new GrupoProduto4ComboBox();
		initializeParamFilters();
		if (exibeFiltroGrupoProduto && LavenderePdaConfig.usaFiltroGrupoProduto != 0) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				lbGrupoProduto1 = new LabelName(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
				cbGrupoProduto1.popupTitle = lbGrupoProduto1.getValue();
				if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor && cbFornecedor.getSelectedItem() != null) {
					Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
					cbGrupoProduto1.loadGrupoProduto1(pedido, fornecedorSelecionado);
				} else {
					cbGrupoProduto1.loadGrupoProduto1(pedido);
				}
				cbGrupoProduto1.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				lbGrupoProduto2 = new LabelName(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
				cbGrupoProduto2.popupTitle = lbGrupoProduto2.getValue();
				cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
				cbGrupoProduto2.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				lbGrupoProduto3 = new LabelName(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
	        	cbGrupoProduto3.popupTitle = lbGrupoProduto3.getValue();
				cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
				cbGrupoProduto3.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				lbGrupoProduto4 = new LabelName(Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
				cbGrupoProduto4.popupTitle = lbGrupoProduto4.getValue();
				cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
				cbGrupoProduto4.setSelectedIndex(0);
			}
			loadLabelGruposProdutos();
		}

		//--
		cbKit = new KitComboBox();
		if (LavenderePdaConfig.isUsaKitProduto()) {
			cbKit.loadKitByTabPreco(cdTabelaPreco);
			lbKit = new LabelName(Messages.KIT_LABEL_KIT);
			cbKit.setSelectedIndex(0);
		}
		//--
		lbCesta = new LabelName(" ");
		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
			lbCesta = new LabelName(Messages.CESTA_NOME_ENTIDADE);
		}
		cbCesta = new CestaComboBox(lbCesta.getValue(), SessionLavenderePda.getCdCliente(), false);
		cbNaoPositivado = new NaoPositivadoComboBox();
		cbNaoPositivado.setValue(Messages.NAO_POSITIVADOS);
		//--
		ckPreAltaProduto = new CheckBoolean(Messages.PRODUTO_LABEL_PRE_ALTA_CUSTO);
		ckProdutoKit = new CheckBoolean(Messages.PRODUTOKIT_FILTRO);
		ckProdutoVendido = new CheckBoolean(Messages.PRODUTOVENDIDO_FILTRO);
		ckProdutoPromocional = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_PROMOCIONAL);
		ckProdutoDescPromocional = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_DESC_PROMOCIONAL);
		ckDescMaxProdCli = new CheckBoolean(Messages.DESCONTO_MAX_PRODCLI);
		ckProdutoDescQtd = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_DESC_QTD);
		if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
			cbLocal = new LocalComboBox();
		}
		lbProdutoGrupoDestaque = new LabelName(Messages.PRODUTO_LABEL_PRODUTO_DESTAQUE);
		cbProdutoGrupoDestaque = new ProdutoDestaqueComboBox();
		cbPacote = new PacoteComboBox();
		cbPacote.setSelectedIndex(0);
		ckProdutoOportunidade = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_OPORTUNIDADE);
		cbDescPromocional = new DescPromocionalComboBox(pedido, cdTabelaPreco);
		cbDescPromocional.setSelectedIndex(0);
		//--
		if (LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			final String editNumberInicial = "9999999999";
			lbComissaoMinima = new LabelName(Messages.PRODUTO_LABEL_COMISSAO_MINIMA);
			lbComissaoMaxima = new LabelName(Messages.PRODUTO_LABEL_COMISSAO_MAXIMA);
			edComissaoMinima = new EditNumberFrac(editNumberInicial, 9);
			edComissaoMaxima = new EditNumberFrac(editNumberInicial, 9);
		}
		cbLocalEstoque = new LocalEstoqueComboBox();
		cbGrupoDescProd = new GrupoDescProdComboBox();
		if (pedido != null && LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			cbDescProgressivoConfig = new DescProgressivoConfigComboBox(pedido.getCliente());
			cbDescProgConfigFam = new TipoDescProgConfigFamComboBox(true);
			cbDescProgFamilia = new DescProgFamiliaComboBox();
		}
		cbEstoqueDisponivel = new EstoqueDisponivelComboBox();
		ckApenasItensAdicionados = new CheckBoolean(Messages.FILTRO_APENAS_PRODUTOS_PEDIDO);
		cbMarcador = new MarcadorMultiComboBox();
		if (LavenderePdaConfig.apresentaMarcadorProdutoLista || LavenderePdaConfig.apresentaMarcadorProdutoInsercao) {
			cbMarcador.load();
		}
		setDefaultRect();
	}

	@Override
	protected void addBtFechar() {
		// Não deve adicionar
	}

	@Override
	public void initUI() {
	   try {
			super.initUI();
			if (exibeFiltroFornecedor) {
				UiUtil.add(this, lbFornecedor, cbFornecedor, getLeft(), getNextY());
			}
			if (exibeFiltroAtributoProduto) {
				UiUtil.add(this, lbAtributoProd, cbAtributoProd, getLeft(), getNextY());
				UiUtil.add(this, lbAtributoOpcaoProd, cbAtributoOpcaoProd, getLeft(), getNextY());
			}
			if (exibeFiltroGrupoProduto) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					UiUtil.add(this, lbGrupoProduto1, cbGrupoProduto1, getLeft(), getNextY());
				}
				if (LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
					UiUtil.add(this, lbGrupoProduto2, cbGrupoProduto2, getLeft(), getNextY());
				}
				if (LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
					UiUtil.add(this, lbGrupoProduto3, cbGrupoProduto3, getLeft(), getNextY());
				}
				if (LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
					UiUtil.add(this, lbGrupoProduto4, cbGrupoProduto4, getLeft(), getNextY());
				}
			}
		   if (exibeFiltroMarcador) {
				UiUtil.add(this, new LabelName(Messages.MARCADOR_MARCADORES), cbMarcador, getLeft(), getNextY());
			}
			if (exibeFiltroKitProduto) {
				UiUtil.add(this, lbKit, cbKit, getLeft(), getNextY());
			}
			if (exibeFiltroCampanhaVendas) {
			 	UiUtil.add(this, lbCesta, getLeft(), getNextY(), PREFERRED, PREFERRED);
			 	UiUtil.add(this, cbCesta, getLeft(), AFTER, getWidth() / 2);
			 	UiUtil.add(this, cbNaoPositivado, AFTER + HEIGHT_GAP, SAME);
			}
			if (exibeFiltroProdutoGrupoDestaque) {
				UiUtil.add(this, lbProdutoGrupoDestaque, cbProdutoGrupoDestaque, getLeft(), getNextY());
			}
			if (exibeFiltroPacote) {
				UiUtil.add(this, new LabelName(Messages.DESCONTO_PACOTE_LABEL_COMBO), cbPacote, getLeft(), getNextY());
			}
			if (exibeFiltroDescPromocional) {
				UiUtil.add(this, new LabelName(Messages.PRODUTO_LABEL_DESC_PROMOCIONAL), cbDescPromocional, getLeft(), getNextY());
			}
			if (exibeFiltroAvisoPreAlta) {
				UiUtil.add(this, ckPreAltaProduto, getLeft(), getNextY() + HEIGHT_GAP);
			}
			if (exibeFiltroApenasKit) {
				UiUtil.add(this, ckProdutoKit, getLeft(), getNextY() + HEIGHT_GAP);
			}
			if (exibeFiltroProdutoDescPromocional) {
				UiUtil.add(this, ckProdutoDescPromocional, getLeft(), getNextY() + HEIGHT_GAP, getWFill());
			}
			if (exibeFiltroLocal) {
				UiUtil.add(this, new LabelName(Messages.LABEL_LOCAL), cbLocal, getLeft(), getNextY() + HEIGHT_GAP);
			}
			if (exibeFiltroProdutoOportunidade) {
				UiUtil.add(this, ckProdutoOportunidade, getLeft(), getNextY() + HEIGHT_GAP);
			}
			if (exibeFiltroApenasProdutoVendidoMesCorrente) {
				UiUtil.add(this, ckProdutoVendido, getLeft(), getNextY() + HEIGHT_GAP);
			}
			if (exibeFiltroDescMaxProdCli) {
				UiUtil.add(this, ckDescMaxProdCli, getLeft(), getNextY() + HEIGHT_GAP);
			}
			//-- Ordenação
			if (exibeFiltroProdutoPromocional) {
				UiUtil.add(this, ckProdutoPromocional, getLeft(), getNextY() + HEIGHT_GAP);
			}
			if (exibeFiltroComissao) {
				UiUtil.add(this, lbComissaoMinima, getLeft(), getNextY() - WIDTH_GAP);
				UiUtil.add(this, lbComissaoMaxima, AFTER + lbComissaoMinima.getWidth(), SAME);
				UiUtil.add(this, edComissaoMinima, getLeft(), getNextY() - WIDTH_GAP , PREFERRED + lbComissaoMinima.getWidth() - WIDTH_GAP_BIG);
				UiUtil.add(this, edComissaoMaxima, AFTER + WIDTH_GAP, SAME);
			}
			if (exibeFiltroLocalEstoque) {
				UiUtil.add(this, new LabelName(Messages.PRODUTO_LABEL_FILTRO_LOCAL_ESTOQUE), cbLocalEstoque, getLeft(), getNextY());
			}
			if (exibeFiltroGrupoDescProd) {
				UiUtil.add(this, new LabelName(Messages.GRUPOPRODUTO_GRUPOS_DESCONTO_PRODUTO), cbGrupoDescProd, getLeft(), getNextY());
			}
			if (exibeFiltroDescProgressivoPersonalizado) {
				UiUtil.add(this, new LabelName(Messages.DESC_PROG_CONFIG_NM_ENTIDADE), cbDescProgressivoConfig, getLeft(), getNextY());
				UiUtil.add(this, new LabelName(Messages.DESC_PROG_CONFIG_TIPOCONFIGFAM), cbDescProgConfigFam, getLeft(), getNextY());
				UiUtil.add(this, new LabelName(Messages.DESC_PROG_FAMILIA_PRODUTOS_TITLE), cbDescProgFamilia, getLeft(), getNextY());
			}
			if (exibeFiltroEstoqueDisponivel) {
				UiUtil.add(this, new LabelName(Messages.ESTOQUE_COMBO_TITULO), cbEstoqueDisponivel, getLeft(), getNextY());
			}
			if (exibeFiltroItensInseridos) {
				UiUtil.add(this, ckApenasItensAdicionados, getLeft(), getNextY());
			}
			//--
			// Este deve ser o último filtro da tela
			UiUtil.add(this, lbProduto, getLeft(), getNextY(), PREFERRED, PREFERRED);
			if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() || LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
				UiUtil.add(this, btGroupTipoFiltros, getLeft(), AFTER);
				UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME);
			} else if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo() && !LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ) {
				UiUtil.add(this, btGroupTipoFiltros, getLeft(), AFTER);
				UiUtil.add(this, edFiltro, AFTER + WIDTH_GAP, SAME);
			} else {
				UiUtil.add(this, edFiltro, getLeft(), AFTER);
			}
			if (exibeFiltroProdutoComDescQtd) {
				UiUtil.add(this, ckProdutoDescQtd, getLeft(), getNextY() + HEIGHT_GAP);
			}
			//--
			addButtonPopup(btFiltrar);
			addButtonPopup(btLimpar);
			addButtonPopup(btFechar);
			
		} catch (Exception ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private void initializeParamFilters() throws SQLException {
		if (pedido != null) {
			if (LavenderePdaConfig.isTodosFiltrosFixosTelaItemPedido()) return;
			showAllFiltros = LavenderePdaConfig.isTodosFiltrosNaTelaAvancadoItemPedido();
			LavenderePdaConfig.carregaMapFiltrosProdutos(filtrosVisiveisTelaPrincipalMap, null, 1);
			
			exibeFiltroFornecedor = LavenderePdaConfig.usaFiltroFornecedor && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_FORNECEDOR) == null);
			exibeFiltroGrupoProduto = LavenderePdaConfig.usaFiltroGrupoProduto != 0 && (showAllFiltros || (filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_GRUPOPRODUTO) == null));
			exibeFiltroAtributoProduto = LavenderePdaConfig.usaFiltroAtributoProduto && (showAllFiltros || (filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_ATRIBUTO_PRODUTO) == null));
			exibeFiltroAvisoPreAlta = LavenderePdaConfig.usaAvisoPreAlta && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_AVISO_PRE_ALTA) == null);
			exibeFiltroApenasKit = LavenderePdaConfig.isUsaKitBaseadoNoProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_APENAS_KIT) == null);
			exibeFiltroKitProduto = LavenderePdaConfig.isUsaKitProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_KIT) == null);
			exibeFiltroCampanhaVendas = LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_CAMPANHA_VENDAS) == null);
			exibeFiltroProdutoPromocional = LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_PRODUTO_PROMOCIONAL) == null);
			exibeFiltroProdutoPromocional &= !LavenderePdaConfig.usaDescPromocionalRegraPoliticaDesconto();
			exibeFiltroProdutoDescPromocional = LavenderePdaConfig.isUsaFiltroProdutoDescPromocional() || (!LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_PRODUTO_DESC_PROMOCIONAL) == null);
			exibeFiltroProdutoDescPromocional &= !LavenderePdaConfig.usaDescPromocionalRegraPoliticaDesconto();
			exibeFiltroProdutoGrupoDestaque = LavenderePdaConfig.isUsaGrupoDestaqueProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_PRODUTO_GRUPO_DESTAQUE) == null);
			exibeFiltroPacote = LavenderePdaConfig.usaDescQuantidadePorPacote && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_PRODUTO_CBPACOTE) == null);
			exibeFiltroProdutoOportunidade = LavenderePdaConfig.usaOportunidadeVenda && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_PRODUTO_OPORTUNIDADE) == null && !pedido.isOportunidade());
			exibeFiltroApenasProdutoVendidoMesCorrente = LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE) == null);
			exibeFiltroGrupoDescProd = LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_GRUPO_DESCONTO_PRODUTO) == null);
			exibeFiltroDescPromocional = LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_PRODUTO_DESC_PROMOCIONAL) == null); 
			exibeFiltroComissao = LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_COMISSAO) == null);
			exibeFiltroLocal = (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_LOCAL) == null);
			exibeFiltroDescMaxProdCli = LavenderePdaConfig.usaDescMaxProdCli && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_DESCMAX_PRODUTO_CLIENTE) == null);
			exibeFiltroDescProgressivoPersonalizado = LavenderePdaConfig.usaDescProgressivoPersonalizado && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_DESC_PROGRESSIVO_PERSONALIZADO) == null);
			exibeFiltroEstoqueDisponivel = LavenderePdaConfig.isUsaFiltroEstoqueDisponivelTelaItemPedido() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_ESTOQUE_DISPONIVEL) == null);
			exibeFiltroItensInseridos = showAllFiltros ||  filtrosVisiveisTelaPrincipalMap.get(CadItemPedidoForm.NU_FILTRO_ITENS_INSERIDOS) == null;
			exibeFiltroMarcador = LavenderePdaConfig.apresentaMarcadorProdutoInsercao && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_CBMARCADOR) == null);
			exibeFiltroProdutoComDescQtd = LavenderePdaConfig.apresentaFiltroDescQtd && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_COM_DESCQTD) == null);
			return;
		}
		if (LavenderePdaConfig.isTodosFiltrosFixosTelaListaProduto()) return;
		showAllFiltros = LavenderePdaConfig.isTodosFiltrosNaTelaAvancadoListaProduto();
		LavenderePdaConfig.carregaMapFiltrosProdutos(filtrosVisiveisTelaPrincipalMap, null, 0);

		exibeFiltroFornecedor = LavenderePdaConfig.usaFiltroFornecedor && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_FORNECEDOR) == null);
		exibeFiltroGrupoProduto = LavenderePdaConfig.usaFiltroGrupoProduto > 0 && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_GRUPOPRODUTO) == null);
		exibeFiltroAtributoProduto = LavenderePdaConfig.usaFiltroAtributoProduto && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_ATRIBUTO_PRODUTO) == null);
		exibeFiltroAvisoPreAlta = LavenderePdaConfig.usaAvisoPreAlta && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_AVISO_PRE_ALTA) == null);
		exibeFiltroApenasKit = LavenderePdaConfig.isUsaKitBaseadoNoProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_APENAS_KIT) == null);
		exibeFiltroApenasProdutoVendidoMesCorrente = LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_APENAS_PRODUTO_VENDIDO_MES_CORRENTE) == null);
		exibeFiltroProdutoPromocional = LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_PROMOCIONAL) == null);
		exibeFiltroProdutoDescPromocional = LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTelaListaProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_DESC_PROMOCIONAL) == null);
		exibeFiltroProdutoGrupoDestaque = LavenderePdaConfig.isUsaGrupoDestaqueProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_GRUPO_DESTAQUE) == null);
		exibeFiltroPacote = LavenderePdaConfig.usaDescQuantidadePorPacote && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_CBPACOTE) == null);
		exibeFiltroLocalEstoque = LavenderePdaConfig.usaFiltroLocalEstoqueListaProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_LOCAL_ESTOQUE) == null);
		exibeFiltroEstoqueDisponivel = LavenderePdaConfig.isUsaFiltroEstoqueDisponivelListaProduto() && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_ESTOQUE_DISPONIVEL) == null);
		exibeFiltroMarcador = LavenderePdaConfig.apresentaMarcadorProdutoLista && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_CBMARCADOR) == null);
		exibeFiltroProdutoComDescQtd = LavenderePdaConfig.apresentaFiltroDescQtd && (showAllFiltros || filtrosVisiveisTelaPrincipalMap.get(ListProdutoForm.NU_FILTRO_PRODUTO_COM_DESCQTD) == null);
	}

	private boolean isUsaFiltroPorProdutoComDescontoPromocional() {
		if (pedido == null) {
			return LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTelaListaProduto();
		}
		return LavenderePdaConfig.isUsaFiltroProdutoDescPromocional() || (!LavenderePdaConfig.isUsaFiltroProdutoDescPromocionalTipoComboBox() && LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional());
	}

	private void enable() {
		cbAtributoOpcaoProd.setEnabled(false);
		btFiltrar.setEnabled(true);
		edFiltro.setEnabled(true);
		if (ValueUtil.isNotEmpty(cbAtributoProd.getValue())) {
			cbAtributoOpcaoProd.setEnabled(true);
			if (ValueUtil.isEmpty(cbAtributoOpcaoProd.getValue())) {
				btFiltrar.setEnabled(false);
				edFiltro.setEnabled(false);
			}
		}
		if (edFiltro.isEnabled()) {
			edFiltro.requestFocus();
		}
	}

	@Override
	protected void onPopup() {
		super.onPopup();
		enable();
	}

	@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		//--
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbFornecedor) {
					if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor && !LavenderePdaConfig.ocultaGrupoProduto1) {
						Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
						cbGrupoProduto1.loadGrupoProduto1(pedido, fornecedorSelecionado);
					}
				} else if (event.target == cbAtributoProd) {
					cbAtributoOpcaoProd.load(cbAtributoProd.getValue());
					cbAtributoOpcaoProd.setSelectedIndex(0);
					enable();
				} else if (event.target == cbAtributoOpcaoProd) {
					enable();
				} else if (event.target == cbCesta) {
					setCbNaoPositivadoEnable();
				} else if (event.target == btLimpar) {
					limpaFiltrosClick();
				} else if (event.target == btFiltrar) {
					filtroRealizado = true;
					unpop();
				} else if (event.target == btGroupTipoFiltros) {
					btGroupTipoFiltrosClick(btGroupTipoFiltros.getSelectedIndex());
				} else if (event.target == cbGrupoProduto1) {
					cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
					cbGrupoProduto2.setSelectedIndex(0);
					cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
					cbGrupoProduto3.setSelectedIndex(0);
					cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
					cbGrupoProduto4.setSelectedIndex(0);
				} else if (event.target == cbGrupoProduto2) {
					cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
					cbGrupoProduto3.setSelectedIndex(0);
					cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
					cbGrupoProduto4.setSelectedIndex(0);
				} else if (event.target == cbGrupoProduto3) {
					cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), pedido);
					cbGrupoProduto4.setSelectedIndex(0);
				} else if (event.target == ckProdutoPromocional) {
					checkProdutoPromocionalANDProdutoDescPromocional(ckProdutoPromocional);
				} else if (event.target == ckProdutoDescPromocional) {
					afterCkProdDescPromocionalValueChange(ckProdutoDescPromocional.isChecked());
					checkProdutoPromocionalANDProdutoDescPromocional(ckProdutoDescPromocional);
				}  else if (event.target != null && event.target == cbDescProgressivoConfig) {
					cbDescProgressivoConfigChange();
				} else if (event.target == cbDescProgConfigFam) {
					cbDescProgConfigFamChange();
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					filtroRealizado = true;
					unpop();
				}
				break;
			}
		}
	}

	public void alterarTipoTeclado() {
		if (filterByCodigoProduto && LavenderePdaConfig.isExibeBotaoParaFiltroCodigoTecladoNumerico()) {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_INT);
		} else {
			edFiltro.setEditType(BaseEdit.EDIT_TYPE_TEXT);
		}
	}
	
	private void limpaFiltrosClick() throws SQLException {
		cbAtributoProd.setSelectedIndex(0);
		cbAtributoOpcaoProd.setSelectedIndex(0);
		if (LavenderePdaConfig.isUsaKitProduto()) {
			cbKit.setSelectedIndex(0);
		}
		cbFornecedor.setSelectedIndex(0);
		if (LavenderePdaConfig.usaFiltroGrupoProduto != 0) {
			cbGrupoProduto1.setSelectedIndex(0);
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
				cbGrupoProduto2.setSelectedIndex(0);
				if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
					cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
					cbGrupoProduto3.setSelectedIndex(0);
					if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
						cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), pedido);
						cbGrupoProduto4.setSelectedIndex(0);
					}
				}
			}
		}
		ckPreAltaProduto.setChecked(false);
		ckProdutoKit.setChecked(false);
		ckProdutoPromocional.setChecked(false);
		ckProdutoDescPromocional.setChecked(false);
		cbProdutoGrupoDestaque.unselectAll();
		cbPacote.setSelectedIndex(0);
		ckProdutoOportunidade.setChecked(false);
		ckProdutoVendido.setChecked(false);
		ckDescMaxProdCli.setChecked(false);
		ckProdutoDescQtd.setChecked(false);
		edFiltro.setValue("");
		cbDescPromocional.setSelectedIndex(0);
		if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
			cbLocal.setSelectedIndex(0);
			cbLocal.setEnabled(false);
		}
		if (pedido != null) {
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
				cbDescProgressivoConfig.setSelectedIndex(0);
				cbDescProgressivoConfigChange();
			}
			if (LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
				edComissaoMinima.setValue(ValueUtil.VALOR_NI);
				edComissaoMaxima.setValue(ValueUtil.VALOR_NI);
			}
		}
		ckApenasItensAdicionados.setChecked(false);
		cbEstoqueDisponivel.unselectAll();
		cbMarcador.unselectAll();
		enable();
	}

	private void loadLabelGruposProdutos() {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			if (temp.length > 0) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					lbGrupoProduto1.setText(temp[0]);
				}
				if ((temp.length > 1) && (LavenderePdaConfig.usaFiltroGrupoProduto > 1)) {
					lbGrupoProduto2.setText(temp[1]);
					if ((temp.length > 2) && (LavenderePdaConfig.usaFiltroGrupoProduto > 2)) {
						lbGrupoProduto3.setText(temp[2]);
						if ((temp.length > 3) && (LavenderePdaConfig.usaFiltroGrupoProduto > 3)) {
							lbGrupoProduto4.setText(temp[3]);
						}
					}
				}
			}
		}
	}

	public void setCbNaoPositivadoEnable() {
		if (ValueUtil.isEmpty(cbCesta.getValue())) {
			cbNaoPositivado.setEnabled(false);
			cbNaoPositivado.setValue(Messages.OPCAO_TODOS);
		} else {
			cbNaoPositivado.setEnabled(true);
		}
	}
	
	private void checkProdutoPromocionalANDProdutoDescPromocional(CheckBoolean check) {
		if (isUsaFiltroPorProdutoComDescontoPromocional() && LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalItemPedido()) {
			if (check == ckProdutoPromocional) {
				if (ckProdutoPromocional.isChecked()) {
					ckProdutoDescPromocional.setChecked(!ckProdutoPromocional.isChecked());
					afterCkProdDescPromocionalValueChange(false);
				}	
			} else if (check == ckProdutoDescPromocional) {
				if (ckProdutoDescPromocional.isChecked()) {
					ckProdutoPromocional.setChecked(!ckProdutoDescPromocional.isChecked());
				}
			}
		}
	}

	public void afterCkProdDescPromocionalValueChange(boolean checked) {
		if (LavenderePdaConfig.usaDescPromo || LavenderePdaConfig.restringeItemPedidoPorLocal) {
			if (checked) {
				cbLocal.setEnabled(true);
				cbLocal.setSelectedIndex(0);
			} else {
				cbLocal.setValue(null);
				cbLocal.setEnabled(false);
			}
		}
	}

	private void inicializaBtGroupTipoFiltros() {
		List<String> list = new ArrayList<String>();
				
		if(LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO);
		}
		if(LavenderePdaConfig.isUsaFiltroAplicacaoDoProdutoSeparado()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO);
		}
		if(LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			list.add(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO);
		}
		list.add(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO);
		btGroupTipoFiltros = new PushButtonGroupBase(list.toArray(new String[0]), true, 1, -1, 1, 1, true, PushButtonGroup.NORMAL);
		setaPosicoesHashMapbtGroupTipoFiltros(list);

		if (LavenderePdaConfig.usaPesquisaProdutoPersonalizada) {
			btGroupTipoFiltros = new PushButtonGroupBase(new String[] {Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_CONTEM, Messages.PRODUTO_LABEL_FILTRO_TIPO_PESQUISA_INICIA}, true, 0, -1, 1, 1, true, PushButtonGroup.NORMAL);
		}
		btGroupTipoFiltrosClick(btGroupTipoFiltros.getSelectedIndex());
	}

	private void setaPosicoesHashMapbtGroupTipoFiltros(List<String> list) {
		int size = list.size();
		for (int i = 0; i < size; i++) {
			if (list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_PRINCIPIOATIVO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_PA, i);
				continue;
			} else if (!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_PA)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_PA, -1);
			}
			if (list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_APLICACAO_PRODUTO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_AP, i);
				continue;
			} else if (!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_AP)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_AP, -1);
			}
			if (list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_CODIGO_PRODUTO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_CD, i);
				continue;
			} else if (!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_CD)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_CD, -1);
			}
			if (list.get(i).contains(Messages.PRODUTO_LABEL_FILTRO_DESCRICAO)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_DS, i);
				continue;
			} else if (!posicoesbtGroupTipoFiltros.containsKey(HASHKEY_DS)) {
				posicoesbtGroupTipoFiltros.put(HASHKEY_DS, -1);
			}
		}
		btGroupTipoFiltros.setSelectedIndex(size);
	}
	

	private void cbDescProgressivoConfigChange() throws SQLException {
		if (cbDescProgressivoConfig.getSelectedIndex() == 0) {
			cbDescProgConfigFam.removeAll();
		} else {
			cbDescProgConfigFam.load(true);
			cbDescProgConfigFam.setSelectedIndex(0);
		}
		cbDescProgConfigFamChange();
	}
	
	private void cbDescProgConfigFamChange() throws SQLException {
		if (cbDescProgressivoConfig.getSelectedIndex() == 0) {
			cbDescProgFamilia.removeAll();
		} else {
			boolean tipoConsome = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_CONSOME);
			boolean tipoProduz = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_PRODUZ);
			boolean tipoAcumula = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_ACUMULA);
			boolean tipoAcumulaMax = ValueUtil.valueEquals(cbDescProgConfigFam.getSelectedItem(), Messages.DESC_PROG_FAM_COMBO_ACUMULA_MAX);
			cbDescProgFamilia.load(cbDescProgressivoConfig.getValue(), tipoConsome, tipoProduz, tipoAcumula, tipoAcumulaMax);
			cbDescProgFamilia.setSelectedIndex(0);
		}
	}

	private void btGroupTipoFiltrosClick(int indexSelected) {
		filterByPrincipioAtivo = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_PA);
		filterByAplicacaoProduto = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_AP);
		if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			filterByCodigoProduto = indexSelected == posicoesbtGroupTipoFiltros.get(HASHKEY_CD);
		}
		alterarTipoTeclado();
	}
	
}
