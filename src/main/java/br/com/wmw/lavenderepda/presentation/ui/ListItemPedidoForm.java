package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.BaseLayoutListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.ScrollTabbedContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwToast;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ScannerCameraUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.Colecao;
import br.com.wmw.lavenderepda.business.domain.ColecaoStatus;
import br.com.wmw.lavenderepda.business.domain.Combo;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.Kit;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pacote;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.enums.RecalculoRentabilidadeOptions;
import br.com.wmw.lavenderepda.business.service.ComboService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.DescComiFaixaService;
import br.com.wmw.lavenderepda.business.service.DescProgQtdService;
import br.com.wmw.lavenderepda.business.service.DescProgressivoConfigService;
import br.com.wmw.lavenderepda.business.service.DescontoGrupoService;
import br.com.wmw.lavenderepda.business.service.DescontoPacoteService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.GiroProdutoService;
import br.com.wmw.lavenderepda.business.service.GrupoProduto1Service;
import br.com.wmw.lavenderepda.business.service.IpiService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoAgrSimilarService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoBonifCfgService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.KitService;
import br.com.wmw.lavenderepda.business.service.MarcadorProdutoService;
import br.com.wmw.lavenderepda.business.service.MargemRentabService;
import br.com.wmw.lavenderepda.business.service.PacoteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTipoPedService;
import br.com.wmw.lavenderepda.business.service.RestricaoService;
import br.com.wmw.lavenderepda.business.service.RestricaoVendaUnService;
import br.com.wmw.lavenderepda.business.service.STService;
import br.com.wmw.lavenderepda.business.service.TermoCorrecaoService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.business.service.TributosService;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto2ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto3ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto4ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.SoundUtil;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.DragEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.MouseEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.BigDecimal;
import totalcross.util.Vector;
import totalcross.util.concurrent.Lock;
public class ListItemPedidoForm extends LavendereCrudListForm {

	private static final String COL_CDPRODUTO = "CDPRODUTO";

	private Pedido pedido;

	private LabelValue lvDsCliente;
	private LabelTotalizador lvVlTotalItensComTributos;
	private LabelTotalizador lvQtTotalItens;
	private LabelTotalizador lvQtTotalItensTroca;
	private LabelTotalizador lvVlTotalVolumeItens;
	private LabelTotalizador lvPctMedioAcrescDesc;
	private LabelTotalizador lvPctMedioComissao;
	private LabelTotalizador lvQtPesoPedido;
	private LabelTotalizador lvQtItensFaturados;
	private LabelTotalizador lvVlTotalBonificacao;
	private LabelTotalizador lvPontuacao;
	private LabelTotalizador lvRentabilidade;
	private String tipoItemList;
	private ButtonAction btAplicarDescProg;
	private ButtonAction btDescontos;
	private SessionTotalizerContainer sessaoTotalizadores;
	private SessionTotalizerContainer sessaoTotalizadoresItemsTroca;
	private boolean showOnlyItensNaoConforme;
	private CadPedidoForm cadPedidoForm;
	private ScrollTabbedContainer tabs;
	private GridListContainer listContainerItemTroca;
	protected ButtonAction btLeitorCamera;
	public ButtonOptions btOpcoes;

	private GrupoProduto1ComboBox cbGrupoProduto1;
	private GrupoProduto2ComboBox cbGrupoProduto2;
	private GrupoProduto3ComboBox cbGrupoProduto3;
	private GrupoProduto4ComboBox cbGrupoProduto4;
	private MarcadorMultiComboBox cbMarcador;
	private LabelName lbGrupoProduto1;
	private LabelName lbGrupoProduto2;
	private LabelName lbGrupoProduto3;
	private LabelName lbGrupoProduto2e3;
	private LabelName lbGrupoProduto1e2;
	private LabelName lbGrupoProduto3e4;

	private EditText edCdBarrasHidden;
	private String msgSucesso;
	private Lock insereItemByLeitorCdBarrasLock = new Lock();
	private boolean usaInsercaoItensDiferentesLeituraCodigoBarras;

	public boolean showOnlyItensNaoConformeByDescComissao;
	public boolean showOnlyItensNaoConformeByDescontoGrupo;
	public boolean showOnlyItensNaoConformeByDescontoPacote;
	public boolean showOnlyItensNaoConformeByDescProgQtd;
	public boolean showOnlyItensNaoConformeByDescontoGrupoAuto;
	public boolean showOnlyItensNaoConformeByRestricaoVendaUn;
	public boolean showOnlyItensNaoConformeByTipoPedido;
	public boolean showOnlyItensNegociacaoConsumoVerba;
	public boolean showOnlyItensNaoConformeByCalculoVinco;
	public boolean setShowOnlyItensSemEstoque;
	public TipoPedido tipoPedidoItensNaoConforme;
	public boolean inWindowMode;
	private boolean showCadItemPedido;
	private boolean leituraPelaCameraAparelho;
	private boolean showListDescontoCredito;

	private static ListItemPedidoForm instance;
	private static boolean listInicialized = false;

	private String dsFiltro = "";
	private Map<String, Image> mapIconsMarcadores;
	private Image iconSimilar;

	public static ListItemPedidoForm getInstance(CadPedidoForm cadPedidoForm, Pedido pedido, String tipoItemList) throws SQLException {
		if (instance == null) {
			instance = new ListItemPedidoForm(cadPedidoForm, pedido, tipoItemList, false);
			listInicialized = true;
		} else {
			instance.configure(cadPedidoForm, pedido, tipoItemList, false);
			instance.limpaFiltros();
		}
		return instance;
	}

	private void limpaFiltros() throws SQLException {
		if (LavenderePdaConfig.usaFiltroGrupoProdutoListaItemPedido && cadPedidoForm.inRelatorioMode) {
			resetCombosGrupoProduto();
		}
		edFiltro.setText(ValueUtil.VALOR_NI);
		edFiltroCd.setText(ValueUtil.VALOR_NI);
	}

	public static void invalidateInstance() {
		instance = null;
	}

	public static ListItemPedidoForm getNewListItemPedido(CadPedidoForm cadPedidoForm, Pedido pedido, String tipoItemList) throws SQLException {
		return new ListItemPedidoForm(cadPedidoForm, pedido, tipoItemList, true);
	}

	protected void resetCombosGrupoProduto() throws SQLException {
		cbGrupoProduto1.setSelectedIndex(0);
		cbGrupoProduto1Change();
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.setSelectedIndex(0);
	}

	public void show() throws SQLException {
		show(this);
		if (!listInicialized && this.equals(instance)) {
			instance.list();
		}
	}

	public boolean isInstanceExibited() {
		return listContainer != null && listContainer.getRect().width != 0;
	}

	private ListItemPedidoForm(CadPedidoForm cadPedidoForm, Pedido pedido, String tipoItemList, boolean useOnlyNewInstance) throws SQLException {
		super(Messages.ITEMPEDIDO_NOME_ENTIDADE);
		singleClickOn = true;
		this.pedido = pedido;
		lvDsCliente = new LabelValue("");
		lvVlTotalItensComTributos = new LabelTotalizador("999999999,999");
		lvQtTotalItens = new LabelTotalizador("999999999,999");
		lvQtTotalItens.setID("lvQtTotalItens");
		lvPontuacao = new LabelTotalizador("999999999,999");
		lvRentabilidade = new LabelTotalizador("999999999,999");
		lvQtPesoPedido = new LabelTotalizador("999999999,999");
		lvQtTotalItensTroca = new LabelTotalizador("999999999,999");
		lvQtItensFaturados = new LabelTotalizador("999999999,999");
		lvVlTotalBonificacao = new LabelTotalizador("999999999,999");
		lvVlTotalVolumeItens = new LabelTotalizador("999999999,999", RIGHT);
		if (LavenderePdaConfig.mostraDescAcrescNaSublistItemPedido()) {
			lvPctMedioAcrescDesc = new LabelTotalizador("999999999,999", RIGHT);
			lvPctMedioAcrescDesc.setText(Messages.ITEMPEDIDO_LABEL_PCT_MED_ACRESC_DESC + " " + StringUtil.getStringValueToInterface(0.0, LavenderePdaConfig.nuCasasDecimaisVlVolume));
		}
		if (LavenderePdaConfig.mostraPercComissaoNaSublistItemPedido()) {
			lvPctMedioComissao = new LabelTotalizador("999999999,999", RIGHT);
			lvPctMedioComissao.setText(Messages.ITEMPEDIDO_LABEL_PCT_MED_COMISSAO + " " + StringUtil.getStringValueToInterface(0.0, LavenderePdaConfig.nuCasasDecimaisVlVolume));
		}
		btNovo = new ButtonAction(Messages.BOTAO_ADICIONAR_ITEM, "images/add.png");
		btAplicarDescProg = new ButtonAction(Messages.DESCONTO_PROGRESSIVO_APLICAR, "images/aplicadesc.png");
		btDescontos = new ButtonAction(Messages.BT_PRODUTOCREDITODESCONTO, "images/descontoCredito.png");
		sessaoTotalizadores = new SessionTotalizerContainer();
		sessaoTotalizadoresItemsTroca = new SessionTotalizerContainer();
		tabs = new ScrollTabbedContainer(new String[]{Messages.LABEL_ITEMS_VENDA, Messages.LABEL_ITEMS_TROCA});

		listContainer = constructorListContainer(cadPedidoForm.isEnabled());
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
			listContainerItemTroca = constructorListContainer(cadPedidoForm.isEnabled());
			listContainerItemTroca.resizeable = listResizeable;
			listContainerItemTroca.atributteSortSelected = sortAtributte;
			listContainerItemTroca.sortAsc = sortAsc;
		}
		edCdBarrasHidden = new EditText("", 40);
		btLeitorCamera = new ButtonAction(Messages.CAMERA, "images/barcode.png");
		btOpcoes = new ButtonOptions();
		if (LavenderePdaConfig.permiteExclusaoNaListaDeItensDoPedido()) {
			btExcluir = new ButtonAction(FrameworkMessages.BOTAO_EXCLUIR, "images/delete.png", ColorUtil.buttonExcluirForeColor);
		}
		//--
        cbGrupoProduto1 = new GrupoProduto1ComboBox(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
        cbGrupoProduto2 = new GrupoProduto2ComboBox(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
        cbGrupoProduto3 = new GrupoProduto3ComboBox(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
        cbGrupoProduto4 = new GrupoProduto4ComboBox(Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
        cbMarcador = new MarcadorMultiComboBox();
        if (LavenderePdaConfig.apresentaMarcadorProdutoInseridos) {
    		cbMarcador.load(false);
    	}
		configure(cadPedidoForm, pedido, tipoItemList, useOnlyNewInstance);
		if (usaInsercaoItensDiferentesLeituraCodigoBarras) {
			listContainer.setBackgroundText(Messages.ITEMPEDIDO_LISTA_MSG_FUNDO);
			if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
				listContainerItemTroca.setBackgroundText(Messages.ITEMPEDIDO_LISTA_MSG_FUNDO);
			}
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			useLeftTopIcons = true;
			int size = listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size;
			iconSimilar = UiUtil.getColorfulImage("images/similaridade.png", size, size);
		}
	}

	private void loadLabelGruposProdutos() {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			if (temp.length > 0) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					lbGrupoProduto1.setText(temp[0]);
					cbGrupoProduto1.popupTitle = temp[0];
				}
				if ((temp.length > 1) && (lbGrupoProduto2 != null)) {
					lbGrupoProduto2.setText(temp[1]);
		    		cbGrupoProduto2.popupTitle = lbGrupoProduto2.getValue();
					if ((temp.length > 2) && (lbGrupoProduto2e3 != null)) {
						lbGrupoProduto2e3.setText(temp[1] + " / " + temp[2]);
						lbGrupoProduto3.setText(temp[2]);
						cbGrupoProduto3.popupTitle = temp[2];
						if (temp.length > 3) {
							if (!LavenderePdaConfig.ocultaGrupoProduto1) {
								lbGrupoProduto1e2.setText(temp[0] + " / " + temp[1]);
							}
							lbGrupoProduto3e4.setText(temp[2] + " / " + temp[3]);
							cbGrupoProduto4.popupTitle = temp[3];
						}
					}
				}
			}
		}
	}

	private void configure(CadPedidoForm pedidoForm, Pedido pedidoRef, String tipoItemPedido, boolean useOnlyNewInstance) throws SQLException {
		this.cadPedidoForm = pedidoForm;
		usaInsercaoItensDiferentesLeituraCodigoBarras = false;
		if (TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(tipoItemPedido)) {
			title = Messages.ITEMPEDIDO_NOME_ENTIDADE;
			if (pedidoForm.inOnlyConsultaItens || useOnlyNewInstance || pedidoForm.inRelatorioMode) {
				setBaseCrudCadForm(CadItemPedidoForm.getNewCadItemPedido(pedidoForm, pedidoRef));
			} else {
				setBaseCrudCadForm(CadItemPedidoForm.getInstance(pedidoForm, pedidoRef));
			}
			usaInsercaoItensDiferentesLeituraCodigoBarras = LavenderePdaConfig.usaInsercaoItensDiferentesLeituraCodigoBarras;
		} else if (TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(tipoItemPedido)) {
			if (pedidoForm.inOnlyConsultaItens || useOnlyNewInstance || pedidoForm.inRelatorioMode) {
				setBaseCrudCadForm(CadItemPedidoBonificacaoForm.getNewCadItemPedidoBonificacao(pedidoForm, pedidoRef));
			} else {
				setBaseCrudCadForm(CadItemPedidoBonificacaoForm.getInstance(pedidoForm, pedidoRef));
			}
			if (listContainer != null) {
				listContainer.setCheckable(LavenderePdaConfig.permiteExclusaoNaListaDeItensDoPedido() && pedidoForm.isEnabled());
			}
			title = Messages.ITEMPEDIDO_NOME_ENTIDADE;
		} else if (TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE.equals(tipoItemPedido)) {
			title = Messages.OPORTUNIDADE_LABEL;
			if (pedidoForm.inOnlyConsultaItens || useOnlyNewInstance || pedidoForm.inRelatorioMode) {
				setBaseCrudCadForm(CadItemPedidoForm.getNewCadItemPedido(pedidoForm, pedidoRef));
			} else {
				setBaseCrudCadForm(CadItemPedidoForm.getInstance(pedidoForm, pedidoRef));
			}
			usaInsercaoItensDiferentesLeituraCodigoBarras = LavenderePdaConfig.usaInsercaoItensDiferentesLeituraCodigoBarras;
		} else {
			if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(tipoItemPedido)) {
				title = Messages.TROCA_ENTREGAR_NOME_ENTIDADE;
			} else {
				title = Messages.TROCA_RECOLHER_NOME_ENTIDADE;
			}
			setBaseCrudCadForm(new CadItemTrocaForm(pedidoForm, pedidoRef, tipoItemPedido));
		}
		if (lbTitle != null) {
			lbTitle.setText(title);
			lbTitle.reposition();
		}

		this.tipoItemList = tipoItemPedido;
		this.pedido = pedidoRef;
		if (LavenderePdaConfig.usaFiltroGrupoProdutoListaItemPedido && cadPedidoForm.inRelatorioMode) {
			loadGrupoProdutos();
			loadDefaultFilters();
		}
		lvDsCliente.setValue(pedidoRef.getCliente().toString());
		btNovo.setVisible(pedidoRef.isPedidoAberto() && !pedidoForm.inOnlyConsultaItens && !pedidoForm.inRelatorioMode && !TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE.equals(tipoItemPedido) && !usaInsercaoItensDiferentesLeituraCodigoBarras);
		btDescontos.setVisible(LavenderePdaConfig.usaGerenciaDeCreditoDesconto && pedidoRef.isPedidoAberto());
		addItensOnButtonMenu();
		if (lvPctMedioComissao != null) {
			lvPctMedioComissao.setVisible(!pedido.isPedidoBonificacao() && pedido.isTipoPedidoPermiteComissao());
		}
	}

	@Override
	public void visibleState() {
		super.visibleState();
		barBottomContainer.setVisible(!inWindowMode);
        barTopContainer.setVisible(!inWindowMode);
		try {
			lvVlTotalBonificacao.setVisible(pedido.isPedidoBonificacao());
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
	}

	public void limpaDadosAoSairPedido() {
		if (getBaseCrudCadForm() instanceof CadItemPedidoForm) {
			((CadItemPedidoForm)getBaseCrudCadForm()).limpaDadosAoSairPedido();
		}
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return ItemPedidoService.getInstance();
	}

	public void setShowOnlyItensNaoConformeByDescComissao(boolean showOnlyItensNaoConformeByDescComissao) {
		this.showOnlyItensNaoConformeByDescComissao = showOnlyItensNaoConformeByDescComissao;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByDescComissao;
		title = Messages.DESCCOMIGRUPO_ITENS_PROBLEMATICOS;
	}

	public void setShowOnlyItensNaoConformeByDescontoGrupo(boolean showOnlyItensNaoConformeByDescontoGrupo) {
		this.showOnlyItensNaoConformeByDescontoGrupo = showOnlyItensNaoConformeByDescontoGrupo;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByDescontoGrupo;
		title = Messages.DESCCOMIGRUPO_ITENS_PROBLEMATICOS;
	}

	public void setShowOnlyItensNaoConformeByDescontoPacote(boolean showOnlyItensNaoConformeByDescontoPacote) {
		this.showOnlyItensNaoConformeByDescontoPacote = showOnlyItensNaoConformeByDescontoPacote;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByDescontoPacote;
		title = Messages.DESCCOMIGRUPO_ITENS_PROBLEMATICOS;
	}

	public void setShowOnlyItensNaoConformeByCalculoVinco(boolean showOnlyItensNaoConformeByCalculoVinco) {
		this.showOnlyItensNaoConformeByCalculoVinco = showOnlyItensNaoConformeByCalculoVinco;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByCalculoVinco;
		title = Messages.INFO_COMPLEMENTAR_VINCO_MUDANCA_NA_LARGURA;
	}

	public void setShowOnlyItensNaoConformeByDescProgQtd(boolean showOnlyItensNaoConformeByDescProgQtd) {
		this.showOnlyItensNaoConformeByDescProgQtd = showOnlyItensNaoConformeByDescProgQtd;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByDescProgQtd;
		title = Messages.DESCCOMIGRUPO_ITENS_PROBLEMATICOS;
	}

	public void setShowOnlyItensNaoConformeByDescontoGrupoAuto(boolean showOnlyItensNaoConformeByDescontoGrupoAuto) {
		this.showOnlyItensNaoConformeByDescontoGrupoAuto = showOnlyItensNaoConformeByDescontoGrupoAuto;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByDescontoGrupoAuto;
		title = Messages.DESCCOMIGRUPO_ITENS_DESC_AUTO;
		if (showOnlyItensNaoConformeByDescontoGrupoAuto) {
			listContainer.setColTotalizerRight(4, Messages.ITEMPEDIDO_LABEL_VLTOTALITENSPEDIDOS);
		}
	}

	public void setShowOnlyItensNaoConformeByRestricaoVendaUn(boolean showOnlyItensNaoConformeByRestricaoVendaUn) {
		this.showOnlyItensNaoConformeByRestricaoVendaUn = showOnlyItensNaoConformeByRestricaoVendaUn;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByRestricaoVendaUn;
		title = Messages.DESCCOMIGRUPO_ITENS_PROBLEMATICOS;
	}

	public void setShowOnlyItensNaoConformeByTipoPedido(boolean showOnlyItensNaoConformeByTipoPedido) {
		this.showOnlyItensNaoConformeByTipoPedido = showOnlyItensNaoConformeByTipoPedido;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByTipoPedido;
		title = Messages.DESCCOMIGRUPO_ITENS_PROBLEMATICOS;
	}

	public void setShowOnlyItensNegociacaoConsumoVerba(boolean showOnlyItensNegociacaoConsumoVerba) throws SQLException {
		this.showOnlyItensNegociacaoConsumoVerba = showOnlyItensNegociacaoConsumoVerba;
		sortAtributte = ItemPedido.DS_COLUNA_VERBA_ITEM.toUpperCase();
		sortAsc = StringUtil.getStringValue(ValueUtil.VALOR_SIM);
		beforeOrder();
		title = Messages.VERBASALDO_MSG_ITENS_COM_CONSUMO_VERBA;
	}

	public void setShowOnlyItensSemEstoque(boolean setShowOnlyItensSemEstoque) {
		this.setShowOnlyItensSemEstoque = setShowOnlyItensSemEstoque;
		this.showOnlyItensNaoConforme = showOnlyItensNaoConformeByTipoPedido;
		title = Messages.DESCCOMIGRUPO_ITENS_PROBLEMATICOS;
	}

	protected BaseDomain getDomainFilter() throws SQLException {
		return new ItemPedido();
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
			Vector list;
		if (LavenderePdaConfig.usaDescontoComissaoPorGrupo && showOnlyItensNaoConformeByDescComissao) {
			list = DescComiFaixaService.getInstance().verificaQtdeMinimaDosItensPedido(pedido, false);
			lvVlTotalItensComTributos.setVisible(false);
			return filterListItemPedido(list);
		}
		if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && showOnlyItensNaoConformeByDescontoGrupo) {
			list = DescontoGrupoService.getInstance().verificaPctMaxDescontoPorGrupoProduto(pedido, false);
			lvVlTotalItensComTributos.setVisible(false);
			return filterListItemPedido(list);
		}
		if (LavenderePdaConfig.usaDescQuantidadePorPacote && showOnlyItensNaoConformeByDescontoPacote) {
			list = DescontoPacoteService.getInstance().verificaPctMaxDescontoPorPacote(pedido, false);
			lvVlTotalItensComTributos.setVisible(false);
			return list;
		}
		if (LavenderePdaConfig.filtraProdutoPorTipoPedido && showOnlyItensNaoConformeByTipoPedido) {
			list = ProdutoTipoPedService.getInstance().getItensNaoConformeByTipoPedido(pedido, tipoPedidoItensNaoConforme);
			lvVlTotalItensComTributos.setVisible(false);
			return filterListItemPedido(list);
		}
		if (LavenderePdaConfig.isAplicaDescProgressivoPorQtdPorItemFinalPedidoPorNuConversaUnidadesMedida() && LavenderePdaConfig.isBloqueiaDescontoMaiorDescontoProgressivo() && showOnlyItensNaoConformeByDescProgQtd) {
			list = DescProgQtdService.getInstance().getItensDescontoMaiorDescProgressivo(pedido);
			lvVlTotalItensComTributos.setVisible(false);
			return filterListItemPedido(list);
		}
		if (LavenderePdaConfig.usaRestricaoVendaProdutoPorUnidade && LavenderePdaConfig.usaUnidadeAlternativa && showOnlyItensNaoConformeByRestricaoVendaUn) {
			list = RestricaoVendaUnService.getInstance().getItensNaoConformeByRestricaoVendaUn(pedido, tipoPedidoItensNaoConforme);
			lvVlTotalItensComTributos.setVisible(false);
			return filterListItemPedido(list);
		}
		if (LavenderePdaConfig.usaValidaPosicaoVincoLargura() && showOnlyItensNaoConformeByCalculoVinco) {
			list = PedidoService.getInstance().getListItemCalculoVinco(pedido);
			lvVlTotalItensComTributos.setVisible(false);
			return list;
		}
		//--
		if (LavenderePdaConfig.isUsaDescontoQtdPorGrupo() && showOnlyItensNaoConformeByDescontoGrupoAuto) {
			list = DescontoGrupoService.getInstance().getDescQtdGrupoAplicadoAuto(pedido);
			lvVlTotalItensComTributos.setVisible(false);
			return filterListItemPedido(list);
		}
		//--
		if (LavenderePdaConfig.naoConsomeVerbaAutomaticamenteAoFecharPedido && showOnlyItensNegociacaoConsumoVerba) {
			list = PedidoService.getInstance().getItensListApenasConsumoGeradoVerba(pedido);
			PedidoService.getInstance().realizaOrdenacaoListaItens(pedido, list, domain.sortAtributte, domain.sortAsc);
			return filterListItemPedido(list);
		}
		if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT.equals(tipoItemList)) {
			list = PedidoService.getInstance().getItensListByTipo(TipoItemPedido.TIPOITEMPEDIDO_TROCA_ENT, pedido);
		} else if (TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(tipoItemList)) {
			list = PedidoService.getInstance().getItensListByTipo(TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC, pedido);
		} else if (TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE.equals(tipoItemList)) {
			list = PedidoService.getInstance().getItensListByTipo(TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE, pedido);
		} else {
			list = PedidoService.getInstance().getItensListByTipo(TipoItemPedido.TIPOITEMPEDIDO_NORMAL, pedido);
		}

		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			list.addElementsNotNull(pedido.getItemPedidoAgrSimilares().items);
		}
		if (LavenderePdaConfig.apresentaMarcadorProdutoInseridos && ValueUtil.isNotEmpty(cbMarcador.getSelected())) {
			list = ItemPedidoService.getInstance().filtraItemPedidoListMarcador(list, cbMarcador.getSelected());
		}
		if (LavenderePdaConfig.usaFiltroGrupoProdutoListaItemPedido && LavenderePdaConfig.usaFiltroGrupoProduto > 0 && (ValueUtil.isNotEmpty(cbGrupoProduto1.getValue()) || LavenderePdaConfig.ocultaGrupoProduto1) && cadPedidoForm.inRelatorioMode) {
   	   		list = ItemPedidoService.getInstance().findItensPedidoByGrupoProduto(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), cbGrupoProduto4.getValue(), list);
   		}
		if (setShowOnlyItensSemEstoque && LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaItensAdicionados()) {
			list = PedidoService.getInstance().getItensListSemEstoqueEstoqueInsuficiente(pedido);
			PedidoService.getInstance().realizaOrdenacaoListaItens(pedido, list, domain.sortAtributte, domain.sortAsc);
			return list;
		}
		PedidoService.getInstance().realizaOrdenacaoListaItens(pedido, list, domain.sortAtributte, domain.sortAsc);
   		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
   			for (int i = 0; i < list.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) list.items[i];
				if (OrigemPedido.FLORIGEMPEDIDO_ERP.equals(itemPedido.flOrigemPedido) && itemPedido.qtItemDesejado == 0) {
					itemPedido.qtItemDesejado = itemPedido.getQtItemFisico();
				}
			}
   		}
   		if (LavenderePdaConfig.usaGradeProduto4()) {
   			list = ItemPedidoService.getInstance().agrupaItemPedido(list);
   		}
   		if (LavenderePdaConfig.usaGradeProduto5()) {
//   			list = ItemPedidoService.getInstance().agrupaItemPedidoGrade(list);
   		}
		return filterListItemPedido(list);
	}

	private Vector filterListItemPedido(Vector list) throws SQLException {
		if (!ValueUtil.isEmpty(dsFiltro)) {
			Vector itensPedidoPorBusca = ItemPedidoService.getInstance().filtraItensPedidoPorBusca(list, dsFiltro);
			updateTotalizadores(itensPedidoPorBusca);
			return itensPedidoPorBusca;
		} else {
			updateTotalizadores(list);
			return list;
		}
	}

	@Override
	public String getLabelOrdenacaoCrescente(String title) {
		if (title.equalsIgnoreCase(Messages.LISTA_ITEMPEDIDO_LABEL_GONDOLA_ORDENACAO)) {
			return Messages.LISTA_ITEMPEDIDO_LABEL_ORDENACAO_ASC;
		}
		return super.getLabelOrdenacaoCrescente(title);
	}

	@Override
	public String getLabelOrdenacaoDecrescente(String title) {
		if (title.equalsIgnoreCase(Messages.LISTA_ITEMPEDIDO_LABEL_GONDOLA_ORDENACAO)) {
			return Messages.LISTA_ITEMPEDIDO_LABEL_ORDENACAO_DESC;
		}
		return super.getLabelOrdenacaoDecrescente(title);
	}

	private boolean isOrdenaGondola() {
		return pedido.isGondola() && ItemPedido.DS_COLUNA_QTITEMGONDOLA.equalsIgnoreCase(ItemPedido.sortAttr);
	}

	private boolean isOrdenaCombo() {
		return LavenderePdaConfig.isExibeComboMenuInferior() && ItemPedido.DS_COLUNA_CDCOMBO.equalsIgnoreCase(ItemPedido.sortAttr);
	}

	private boolean isOrdenaVerba() {
		return LavenderePdaConfig.usaOrdenacaoVerbaItemPedido && (ItemPedido.DS_COLUNA_VERBA_ITEM.equalsIgnoreCase(ItemPedido.sortAttr));
	}

	private void updateTotalizadores() throws SQLException {
		updateTotalizadores(null);
	}

	private void updateTotalizadores(Vector domainList) throws SQLException {
		if (showOnlyItensNaoConformeByDescontoGrupo || showOnlyItensNaoConformeByDescontoPacote || showOnlyItensNaoConformeByDescComissao || showOnlyItensNaoConformeByDescProgQtd || showOnlyItensNaoConformeByRestricaoVendaUn || showOnlyItensNaoConformeByTipoPedido) {
			return;
		}
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens()) {
			if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				lvVlTotalItensComTributos.setValue(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_TRIBUTOS + " " + StringUtil.getStringValueToInterface(TributosService.getInstance().getVlTotalPedidoComTributos(pedido)));
			} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
				lvVlTotalItensComTributos.setValue(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_COM_ST + " " + StringUtil.getStringValueToInterface(STService.getInstance().getVlTotalItensComSt(pedido)));
			} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				lvVlTotalItensComTributos.setValue(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_IPI + " " + StringUtil.getStringValueToInterface(IpiService.getInstance().getVlTotalPedidoComIpi(pedido)));
			}
			lvVlTotalItensComTributos.reposition();
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedidoTotal() || LavenderePdaConfig.isUsaCalculoVolumeItemPedidoTotalizador()) {
			lvVlTotalVolumeItens.setText(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_VOLUME + " " + StringUtil.getStringValueToInterface(pedido.vlVolumePedido, LavenderePdaConfig.nuCasasDecimaisVlVolume));
			lvVlTotalVolumeItens.setRect(KEEP, KEEP, PREFERRED, PREFERRED);
		}
		defineTotalizadoresDeMedia();
		if (LavenderePdaConfig.isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido()) {
			if (LavenderePdaConfig.isUsaQtdInteiro()) {
				lvQtItensFaturados.setValue(Messages.ITEMPEDIDO_LABEL_QTITEMFATURAMENTO + " " + StringUtil.getStringValueToInterface((int)pedido.getQtItensFaturamento()));
			} else {
				lvQtItensFaturados.setValue(Messages.ITEMPEDIDO_LABEL_QTITEMFATURAMENTO + " " + StringUtil.getStringValueToInterface((double)pedido.getQtItensFaturamento()));
			}
			lvQtItensFaturados.reposition();
		}
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoNoTotalizador()) {
			String qtPeso = Messages.ITEMPEDIDO_LABEL_QT_PESOITENS + " " + StringUtil.getStringValueToInterface(pedido.qtPeso);
			if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio()) {
				qtPeso = MessageUtil.getMessage(Messages.LISTA_ITEM_PEDIDO_PESO_MEDIO, StringUtil.getStringValueToInterface(pedido.qtPeso));
			}
			lvQtPesoPedido.setValue(qtPeso);
			lvQtPesoPedido.reposition();
		}
		if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() || LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			if (domainList == null) {
				lvVlTotalBonificacao.setValue(Messages.ITEMPEDIDO_TOTAL_BONIFICACAO + " " + StringUtil.getStringValueToInterface(ItemPedidoService.getInstance().getVlTotalItemsBonificacao(pedido.itemPedidoList)));
			} else {
				lvVlTotalBonificacao.setValue(Messages.ITEMPEDIDO_TOTAL_BONIFICACAO + " " + StringUtil.getStringValueToInterface(ItemPedidoService.getInstance().getVlTotalItemsBonificacao(domainList)));
			}
		}
		lvQtTotalItens.setValue(Messages.ITEMPEDIDO_LABEL_QT_TOTALITENS + " " + StringUtil.getStringValueToInterface(pedido.getQtItensLista(domainList), LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface));
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
			lvQtTotalItensTroca.setValue(Messages.ITEMPEDIDO_LABEL_QT_TOTALITENS + " " + StringUtil.getStringValueToInterface(pedido.getQtItensTrocaLista()));
			lvQtTotalItensTroca.reposition();
			sessaoTotalizadoresItemsTroca.reposition();
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			final String vlPontuacao = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(pedido.vlTotalPontuacaoRealizado, pedido.vlTotalPontuacaoBase, LavenderePdaConfig.mostraPontuacaoListaItemBase(), LavenderePdaConfig.mostraPontuacaoListaItemRealizado());
			if (vlPontuacao != null) {
				lvPontuacao.setValue(Messages.PONTUACAO_ITEM_PEDIDO_TOTALIZADOR + " " + vlPontuacao);
				lvPontuacao.setForeColor(PontuacaoConfigService.getInstance().getPontuacaoColor(pedido.vlTotalPontuacaoRealizado, pedido.vlTotalPontuacaoBase, LavenderePdaConfig.mostraPontuacaoListaItemBase(), LavenderePdaConfig.mostraPontuacaoListaItemRealizado(), false));
			}
		}
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			final String vlRentabilidadeAplicada = StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlRentabTotalItens));
			final String vlRentabilidadeSugerida = StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlRentabSugItens));
			lvRentabilidade.setValue(Messages.RENTABILIDADE_SUG_APLICADA_PEDIDO_TOTALIZADOR + " " + vlRentabilidadeAplicada + " / " + vlRentabilidadeSugerida);
		}
		lvQtTotalItens.reposition();
		sessaoTotalizadores.reposition();
	}

	private void defineTotalizadoresDeMedia() throws SQLException {
		if (!LavenderePdaConfig.mostraDescAcrescNaSublistItemPedido() 
				&& !LavenderePdaConfig.mostraPercComissaoNaSublistItemPedido()) return;
		double mediaDescAcresc = 0;
		double mediaComissao = 0;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido ip = (ItemPedido) pedido.itemPedidoList.items[i];
			mediaDescAcresc += ip.getVlPctDescontoAcrescimo();
		}
		int itemPedidoListSize = pedido.itemPedidoList.size();
		mediaDescAcresc = itemPedidoListSize == 0 ? 0.0 : mediaDescAcresc / itemPedidoListSize;
		mediaComissao =  pedido.vlPctComissaoTotal;

		if (LavenderePdaConfig.mostraDescAcrescNaSublistItemPedido()) {
			lvPctMedioAcrescDesc.setText(Messages.ITEMPEDIDO_LABEL_PCT_MED_ACRESC_DESC + " " + StringUtil.getStringValueToInterface(mediaDescAcresc, LavenderePdaConfig.nuCasasDecimaisVlVolume));
			lvPctMedioAcrescDesc.setRect(KEEP, KEEP, PREFERRED, PREFERRED);
		}

		if (LavenderePdaConfig.mostraPercComissaoNaSublistItemPedido() && !pedido.isPedidoBonificacao() && pedido.isTipoPedidoPermiteComissao()) {
			lvPctMedioComissao.setText(Messages.ITEMPEDIDO_LABEL_PCT_MED_COMISSAO + " " + StringUtil.getStringValueToInterface(mediaComissao, LavenderePdaConfig.nuCasasDecimaisVlVolume));
			lvPctMedioComissao.setRect(KEEP, KEEP, PREFERRED, PREFERRED);
		}
	}

	@Override
	protected boolean isIgnoreTotalizer(Object domain) throws SQLException {
		if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() || LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			ItemPedido itemPedido = (ItemPedido) domain;
			return (pedido != null && pedido.isPedidoBonificacao()) ? false : itemPedido.isItemBonificacao();
		}
		return super.isIgnoreTotalizer(domain);
	}

	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		addSubListInfos(c, itemPedido);
		//ITENS PROMOCIONAIS
		if (LavenderePdaConfig.isConfigValorMinimoDescPromocional() && ValueUtil.getBooleanValue(itemPedido.flPromocional)) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_LISTA_ITEM_PEDIDO_PROMOCIONAL);
			return;
		}
		if (ItemPedidoService.getInstance().isPermiteBonificarItem(itemPedido)) {
			if (itemPedido.isItemBonificacao()) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PRODUTO_BONIFICACAO_BACK);
			}
		}
		if ((LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.informaVerbaManual) && !itemPedido.hasDescProgressivo() && !itemPedido.pedido.isIgnoraControleVerba()) {
			boolean possuiVerbaManual = possuiVerbaManual(itemPedido);
			if (possuiVerbaManual) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_VERBA_MANUAL_BACK);
			}
			boolean flBonificacao = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(itemPedido.flTipoItemPedido);
			if (flBonificacao) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PRODUTO_BONIFICACAO_BACK);
			}
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0 && !SessionLavenderePda.isOcultoInfoRentabilidade) {
			if (itemPedido.vlRentabilidade >= LavenderePdaConfig.indiceMinimoRentabilidadePedido) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_ALTA_RENTABILIDADE_BACK);
			} else if (itemPedido.vlRentabilidade > 0) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_BAIXA_RENTABILIDADE_BACK);
			}
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoItemTabelaPreco && !SessionLavenderePda.isOcultoInfoRentabilidade) {
			listContainer.setContainerBackColor(c, ItemTabelaPrecoService.getInstance().getCorRentabilidadeLista(itemPedido));
		}
		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			if (itemPedido.vlRentabilidade < LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_BAIXA_RENTABILIDADE_BACK);
			} else if (itemPedido.vlRentabilidade <= LavenderePdaConfig.indiceMinimoRentabilidadePedido && itemPedido.vlRentabilidade >= LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_RENTABILIDADE_DENTRO_TOLERANCIA);
			} else {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_ALTA_RENTABILIDADE_BACK);
			}
		}
		if (LavenderePdaConfig.usaSugestaoParaNovoPedidoGiroProduto && itemPedido.isItemPedidoSugestaoGiro) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PRODUTO_INSERIDO_PEDIDO_SUGESTAO_GIRO_BACK);
		}
		if (((LavenderePdaConfig.isUsaReservaEstoqueCentralizado() && !itemPedido.getProduto().isIgnoraValidacao())
				|| LavenderePdaConfig.isUsaReservaEstoqueCentralizadoAtomico()
				|| LavenderePdaConfig.isUsaReservaEstoqueCorrente()
				|| LavenderePdaConfig.usaReservaEstoqueCorrenteR3())
				&& ValueUtil.isNotEmpty(pedido.itemPedidoProblemaReservaEstoqueList)) {
			if (ItemPedidoService.getInstance().isItemPedidoProblemaReservaEstoque(pedido.itemPedidoProblemaReservaEstoqueList, itemPedido)) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_PROBLEMA_RESERVA_ESTOQUE);
			}
		}
		if ((LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() || LavenderePdaConfig.isUsaMotivoPendencia()) && itemPedido.isPendente()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_ITEMPEDIDO_PENDENTE_GRID_FUNDO);
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito() && itemPedido.isRestrito()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PRODUTO_RESTRITO);
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
			listContainer.setContainerBackColor(c, Color.GREEN);
		}
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido() && itemPedido.isSugVendaPerson()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_PRODUTO_INSERIDO_SUGVENDA);
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo && itemPedido.vlPctDesconto > itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto())) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_DESC_MAX_ULTRAPASSADO);
		}
		// PRODUTOS PERTENCENTES A UM KIT EM VERDE
		if ((LavenderePdaConfig.destacaProdutoDeKitNaGrid && LavenderePdaConfig.isUsaKitProdutoFechado() && itemPedido.isFazParteKitFechado()) || itemPedido.isKitTipo3() ) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PRODUTO_COMKIT_BACK);
		}
		if (LavenderePdaConfig.isDestaqueNaListaDeItensInseridosNoPedido() && itemPedido.isFazPartePromocao()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK);
		}
		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos && itemPedido.isErroRecalculo()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_ERRO_RECALCULO_VALORES);
		}
		// Item usando Gôndola
		if (isItemPedidoGondola(itemPedido)) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_SEM_QT_ITEM_FISICO_GONDOLA);
		}
		// Item com Produto Restrito
		if (LavenderePdaConfig.usaRestricaoVendaClienteProduto && pedido.isPedidoAberto() && RestricaoService.getInstance().isProdutoRestrito(itemPedido.cdProduto, pedido.cdCliente, null, itemPedido.getQtItemFisico()) != null) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_PRODUTO_RESTRITO);
		}
		if (LavenderePdaConfig.isExibeComboMenuInferior() && itemPedido.isCombo()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_PEDIDO_COMBO);
		}
		// Item sem estoque
		if (LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaItensAdicionados() && estoqueFaltante(itemPedido) && !itemPedido.isItemBonificacao()){
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_SEM_ESTOQUE);
		}
		// Autorizações
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			boolean hasSolAutorizacaoItemPedido = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoNaoAutorizadoOuPendente(itemPedido, null);
			if (hasSolAutorizacaoItemPedido) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_PEDIDO_NAO_AUTORIZADO);
			} else if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && ((itemPedido.isItemSimilar && ValueUtil.isNotEmpty(itemPedido.cdProdutoSimilarOrg)) || itemPedido.solAutorizacaoItemPedidoCache.getIsItemAutorizadoSimilar(itemPedido, null))) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_ITEM_PEDIDO_AUTORIZADO_OU_DISTRIBUIDO);
			}
		}
	}
	
	public boolean estoqueFaltante(ItemPedido itemPedido) throws SQLException {
		return	ProdutoService.getInstance().produtoSemEstoque(itemPedido.getProduto(), itemPedido.getCdLocalEstoque());
	}

	private boolean isItemPedidoGondola(ItemPedido itemPedido) {
		return itemPedido.getQtItemFisico() == 0 && itemPedido.isGondola();
	}

	private void addSubListInfos(Item c, ItemPedido itemPedido) throws SQLException {
		Vector subItens = new Vector();
		StringBuilder info = new StringBuilder();
		c.removeSublistItens();
		boolean mostraEmbalagemGrid = LavenderePdaConfig.usaConversaoUnidadesMedida && !LavenderePdaConfig.ocultaQtItemFisico && !LavenderePdaConfig.ocultaQtItemFaturamento;
		String qtItemPrincipal = StringUtil.getStringValueToInterface(itemPedido.getQtItemLista(), LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		if (itemPedido.isItemSimilar) {
			addSubItensSimilar(c, itemPedido, subItens, info);
			return;
		}
		if (isItemPedidoGondola(itemPedido)) {
			info.append(itemPedido.qtItemGondola);
			info.append(" ");
			if (itemPedido.qtItemGondola == 1) {
				info.append(Messages.LISTA_ITEMPEDIDO_ITENS_GONDOLA_SING);
			} else {
				info.append(Messages.LISTA_ITEMPEDIDO_ITENS_GONDOLA_PLUR);
			}
		} else {
			info.append(qtItemPrincipal);
			info.append(" ");
			info.append(ItemPedidoService.getInstance().getTipoDescQtdListaItemPedido(itemPedido));
			if (mostraEmbalagemGrid && !LavenderePdaConfig.isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido() && !itemPedido.isItemSimilar) {
				info.append(" (").append(itemPedido.getQtItemFaturamentoToInterface()).append(")");
			}
			if (!LavenderePdaConfig.ocultaInfosValoresPedido && !LavenderePdaConfig.usaGradeProduto4() && !itemPedido.isItemSimilar) {
				info.append(" - ").append(Messages.MOEDA).append("").append(getVlItemPedidoListaItensPedido(itemPedido));
			}
		}
		addSublistItem(c, info, subItens);
		// index 3
		info.setLength(0);
		if (!LavenderePdaConfig.ocultaInfosValoresPedido && !(isItemPedidoGondola(itemPedido))) {
			double rawItemValue = LavenderePdaConfig.permiteAlterarValorItemComIPI ? itemPedido.vlTotalItemPedido + itemPedido.vlTotalIpiItem : itemPedido.vlTotalItemPedido;
			String vlItem = StringUtil.getStringValueToInterface(rawItemValue);
			info.append(Messages.MOEDA).append(" ").append(vlItem);
			c.itemRightTotalizer = Messages.MOEDA + " " + rawItemValue;
			addSublistItem(c, info, subItens);
		}

		if (showOnlyItensNaoConformeByDescComissao) {
			info.append(Messages.LABEL_QTITEM_MIN_DESC_COMISSAO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.qtItemMinDescComissao));
			addSublistItem(c, info, subItens);

			info.append(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto));
			addSublistItem(c, info, subItens);

			info.append(Messages.LABEL_QTITEM_MESMO_GRUPO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.qtItemMesmoGrupo));
			addSublistItem(c, info, subItens);

			info.append(StringUtil.getStringValue(GrupoProduto1Service.getInstance().getLabelGrupoProduto1())).append(" ");
			info.append(StringUtil.getStringValue(GrupoProduto1Service.getInstance().getDsGrupoProduto(itemPedido.getProduto().cdGrupoProduto1)));
			addSublistItem(c, info, subItens);
		} else if (showOnlyItensNaoConformeByDescontoGrupo) {
			info.append(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto));
			addSublistItem(c, info, subItens);

			info.append(Messages.LABEL_PCTMAX_DESC_GRUPO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctMaxDescGrupo));
			addSublistItem(c, info, subItens);

			info.append(StringUtil.getStringValue(GrupoProduto1Service.getInstance().getLabelGrupoProduto1())).append(" ");
			info.append(StringUtil.getStringValue(GrupoProduto1Service.getInstance().getDsGrupoProduto(itemPedido.getProduto().cdGrupoProduto1)));
			addSublistItem(c, info, subItens);
		} else if (showOnlyItensNaoConformeByDescontoPacote) {
			info.append(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto));
			addSublistItem(c, info, subItens);

			info.append(Messages.LABEL_PCTMAX_DESC_GRUPO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctMaxDescPacote));
			addSublistItem(c, info, subItens);

			info.append(StringUtil.getStringValue(Messages.DESCONTO_PACOTE_LABEL_COMBO)).append(" ");
			Pacote pacote = (Pacote) PacoteService.getInstance().findByPrimaryKey(getPacoteFilter(itemPedido));
			if (pacote != null) {
				info.append(StringUtil.getStringValue(pacote.dsPacote));
			}
			addSublistItem(c, info, subItens);
		} else if (showOnlyItensNaoConformeByDescProgQtd) {
			info.append(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto));
			addSublistItem(c, info, subItens);

			info.append(Messages.LABEL_PCT_DESC_PREV).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctDescPrev));
			addSublistItem(c, info, subItens);
		} else if (showOnlyItensNaoConformeByDescontoGrupoAuto) {
			info.append(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO).append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctDesconto));
			addSublistItem(c, info, subItens);

			info.append(StringUtil.getStringValue(GrupoProduto1Service.getInstance().getLabelGrupoProduto1())).append(" ");
			info.append(StringUtil.getStringValue(GrupoProduto1Service.getInstance().getDsGrupoProduto(itemPedido.getProduto().cdGrupoProduto1)));
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens()) {
			if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				boolean customTrib = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.mostraValorBruto;
				info.append(Messages.ITEMPEDIDO_LABEL_VL_TRIBUTOS);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(itemPedido.getVlTributos()));
				addSublistItem(c, info, subItens);

				info.append(Messages.ITEMPEDIDO_LABEL_VL_ITEM_TRIBUTOS);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(customTrib ? itemPedido.getVlItemPedidoUnitarioTributosFreteSeguro() : itemPedido.getVlItemComTributos()));
				addSublistItem(c, info, subItens);

				info.append(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_TRIBUTOS);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(customTrib ? itemPedido.getVlTotalItemPedidoTributosFreteSeguro() : itemPedido.getVlTotalItemComTributos()));
				addSublistItem(c, info, subItens);

			} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
				info.append(Messages.ITEMPEDIDO_LABEL_VLST);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(itemPedido.vlSt));
				addSublistItem(c, info, subItens);

				info.append(Messages.ITEMPEDIDO_LABEL_VL_ITEM_COM_ST);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(itemPedido.getVlItemComSt()));
				addSublistItem(c, info, subItens);

				info.append(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_COM_ST);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(itemPedido.getVlTotalComST()));
				addSublistItem(c, info, subItens);

			} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				info.append(Messages.ITEMPEDIDO_LABEL_VL_IPI);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(itemPedido.vlIpiItem));
				addSublistItem(c, info, subItens);

				info.append(Messages.ITEMPEDIDO_LABEL_VL_ITEM_IPI);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(itemPedido.getVlItemComIpi()));
				addSublistItem(c, info, subItens);

				info.append(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_IPI);
				info.append(" ");
				info.append(StringUtil.getStringValueToInterface(itemPedido.getVlTotalItemComIpi()));
				addSublistItem(c, info, subItens);
			}
				
		}
		if (LavenderePdaConfig.usaGradeProduto1()) {
			if (itemPedido.itemPedidoGradeList.size() > 0) {
				ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade)itemPedido.itemPedidoGradeList.items[0];
				String cdTipoItemGrade1 = ProdutoGradeService.getInstance().getCdTipoItemGrade1ByItemPedidoGrade(itemPedidoGrade, itemPedido.getCdTabelaPreco());
				String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(cdTipoItemGrade1);
				String dsItemGrade = ItemGradeService.getInstance().getDsItemGrade(cdTipoItemGrade1, itemPedido.cdItemGrade1);
				info.append(StringUtil.getStringValue(dsTipoItemGrade)).append(" ").append(StringUtil.getStringValue(dsItemGrade));
				addSublistItem(c, info, subItens);
			} else if (ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1)) {
				Vector produtoGradeList = ProdutoGradeService.getInstance().findProdutoGradeList(itemPedido.cdProduto, itemPedido.getCdTabelaPreco());
				int size = produtoGradeList.size();
				if (size > 0) {
					ProdutoGrade produtoGrade = (ProdutoGrade)produtoGradeList.items[0];
					String dsTipoItemGrade = TipoItemGradeService.getInstance().getDsTipoItemGrade(produtoGrade.cdTipoItemGrade1);
					String dsItemGrade = ItemGradeService.getInstance().getDsItemGrade(produtoGrade.cdTipoItemGrade1, itemPedido.cdItemGrade1);
					info.append(StringUtil.getStringValue(dsTipoItemGrade)).append(" ").append(StringUtil.getStringValue(dsItemGrade));
					addSublistItem(c, info, subItens);
				}
			}
		}
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaItensInseridosNoPedido() && !ValueUtil.getBooleanValue(itemPedido.getProduto().flIgnoraValidacao)) {
			double qtEstoque;
			if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
				qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPdaComParcialPrevisto(itemPedido);
			} else {
				qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
			}
			info.append(EstoqueService.getInstance().getEstoqueToString(qtEstoque) + Messages.PRODUTO_LABEL_EM_ESTOQUE);
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.mostraColunaMarcaNaListaItensInseridosNoPedido()) {
			info.append(itemPedido.getProduto().dsMarca == null ? "" : itemPedido.getProduto().dsMarca);
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
			info.append(Messages.ITEMPEDIDO_LABEL_QTD_DESEJADA);
			info.append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.qtItemDesejado));
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido()) {
			info.append(itemPedido.getQtItemFaturamentoToInterface());
			info.append(" ");
			info.append(Messages.ITEMPEDIDO_LABEL_QTITEMFATURAMENTO);
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedidoTotal() || LavenderePdaConfig.isUsaCalculoVolumeItemPedidoLista()) {
			info.setLength(0);
			info.append(Messages.ITEMPEDIDO_LABEL_VOLUME_TOTAL_ITEM);
			info.append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlVolumeItem, LavenderePdaConfig.nuCasasDecimaisVlVolume));
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.exibeQuantidadeEstoqueFaltanteItemNaLista && itemPedido.pedido.isFlOrigemPedidoPda()) {
			double estoqueFaltante = 0;
			if (LavenderePdaConfig.isConfigGradeProduto() && ValueUtil.valueEquals(ProdutoGrade.CD_ITEM_GRADE_PADRAO, itemPedido.cdItemGrade1)) {
				Vector itemPedidoGradeList = ItemPedidoGradeService.getInstance().getItemPedidoGradeByItemPedido(itemPedido);
				for (int i = 0; i < itemPedidoGradeList.size(); i++) {
					ItemPedidoGrade itemPedidoGrade = (ItemPedidoGrade) itemPedidoGradeList.items[i];
					Estoque estoque = EstoqueService.getInstance().getEstoque(itemPedido.cdProduto, itemPedidoGrade.cdItemGrade1, itemPedidoGrade.cdItemGrade2, itemPedidoGrade.cdItemGrade3, Estoque.CD_LOCAL_ESTOQUE_PADRAO, Estoque.FLORIGEMESTOQUE_ERP, null);
					estoqueFaltante += EstoqueService.getInstance().getEstoqueFaltante(estoque, itemPedidoGrade.qtItemFisico);
				}
			} else {
				Estoque estoque = EstoqueService.getInstance().getEstoque(itemPedido.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
				estoqueFaltante = EstoqueService.getInstance().getEstoqueFaltante(estoque, itemPedido.getQtItemFisico());
			}
			if (estoqueFaltante > 0) {
				info.append(StringUtil.getStringValueToInterface(estoqueFaltante));
				info.append(" ").append(estoqueFaltante >= 2 ? Messages.ITEMPEDIDO_MSG_ITENS_FALTANTES : Messages.ITEMPEDIDO_MSG_ITEM_FALTANTE);
				addSublistItem(c, info, subItens);
			}
		}
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			info.setLength(0);
			info.append(" ");
			info.append(ItemPedidoService.getInstance().getDsTabelaPreco(itemPedido));
			addSublistItem(c, info, subItens);
		}
		if (itemPedido.possuiDiferenca) {
				info.append(Messages.ITEMPEDIDO_MSG_DIFERENTE);
				addSublistItem(c, info, subItens);
			}
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
			info.append(Messages.LOTEPRODUTO_LOTE + " " + itemPedido.cdLoteProduto);
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.usaOrdenacaoVerbaItemPedido || LavenderePdaConfig.usaConfigVerbaSaldoPorGrupoProduto()) {
			double vlVerba = getVlVerbaItemPedido(itemPedido);
			String msgVerba = vlVerba > 0 ? Messages.PEDIDO_LABEL_VLTOTVERBA_GERADA : Messages.VERBASALDO_LABEL_VERBA_CONSUMIDA;
			info.append(msgVerba + " " + Messages.VERBAUSUARIO_LABEL_RS + StringUtil.getStringValueToInterface(vlVerba));
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()) {
			info.append(Messages.ITEMPEDIDO_LABEL_VLPCTTOTALMARGEMITEM);
			info.append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctTotalMargemItem));
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.usaControlePontuacao && itemPedido.pontuacaoProduto != null) {
			final String vlPontuacao = PontuacaoConfigService.getInstance().getPontuacaoBaseRealizada(itemPedido.vlPontuacaoRealizadoItem, itemPedido.vlPontuacaoBaseItem, LavenderePdaConfig.mostraPontuacaoListaItemBase(), LavenderePdaConfig.mostraPontuacaoListaItemRealizado());
			if (vlPontuacao != null) {
				info.append(Messages.PONTUACAO_ITEM_PEDIDO_NAME);
				info.append(" ");
				info.append(vlPontuacao);
				addSublistItem(c, info, subItens);
			}
		}
		if (!ValueUtil.valueEquals(ValueUtil.VALOR_SIM, itemPedido.getProduto().flNeutro) && LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			String vlRentabAplicada = StringUtil.getStringValue(itemPedido.vlRentabilidade);
			String vlRentabSugerida = StringUtil.getStringValue(itemPedido.vlRentabilidadeSug);
			vlRentabAplicada = vlRentabAplicada !=null ? vlRentabAplicada : "-";
			vlRentabSugerida = vlRentabSugerida !=null ? vlRentabSugerida : "-";

			info.append(Messages.RENTABILIDADE_PRATICADA_SUGERIDA_ITEM_PEDIDO_NAME);
			info.append(" ");
			info.append(vlRentabAplicada);
			info.append("/");
			info.append(vlRentabSugerida);
			addSublistItem(c, info, subItens);

		}
		if (LavenderePdaConfig.isExibeComboMenuInferior() && itemPedido.isCombo()) {
			Combo combo = ComboService.getInstance().getComboByItemPedido(itemPedido);
			if (combo != null) {
				info.append(StringUtil.getStringAbreviada(combo.toString(), (int) (width / 1.5)));
				addSublistItem(c, info, subItens);
			}
		}
		if (itemPedido.isKitTipo3()) {
			Kit kit = KitService.getInstance().getKit(itemPedido.cdKit, itemPedido.pedido);
			if (kit != null) {
				info.append(StringUtil.getStringAbreviada(kit.toString(), (int) (width / 1.5)));
				addSublistItem(c, info, subItens);
			}
		}

		Produto produto = itemPedido.getProduto();
		if (LavenderePdaConfig.mostraPrincipioAtivoNaSublistItemPedido()) {
			info.append(StringUtil.getStringValue(produto.dsPrincipioAtivo));
			addSublistItem(c, info, subItens);
		}

		if (LavenderePdaConfig.mostraDescAcrescNaSublistItemPedido()) {
			info.append(Messages.ITEMPEDIDO_LABEL_DESC_AC);
			info.append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.getVlPctDescontoAcrescimo()));
			addSublistItem(c, info, subItens);
		}

		if (LavenderePdaConfig.mostraPercComissaoNaSublistItemPedido() && pedido.isTipoPedidoPermiteComissao() && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			info.append(Messages.ITEMPEDIDO_LABEL_PCT_COM);
			info.append(" ");
			info.append(StringUtil.getStringValueToInterface(itemPedido.vlPctComissao));
			addSublistItem(c, info, subItens);
		}
		
		if (LavenderePdaConfig.mostraColecaoNosDetalhes()) {
			Colecao colecao = itemPedido.getProduto().getColecao();
			String dsColecao = itemPedido.dsColecao != null ? itemPedido.dsColecao : colecao != null ? colecao.dsColecao : ValueUtil.VALOR_NI;
			info.append(StringUtil.getStringValue(dsColecao)).append(" ");
			addSublistItem(c, info, subItens);
		}
		
		if (LavenderePdaConfig.mostraStatusColecaoNosDetalhes()) {
			ColecaoStatus statusColecao = itemPedido.getProduto().getStatusColecao();
			String dsStatusColecao = itemPedido.dsStatusColecao != null ? itemPedido.dsStatusColecao : statusColecao != null ? statusColecao.dsStatusColecao : ValueUtil.VALOR_NI;
			info.append(StringUtil.getStringValue(dsStatusColecao)).append(" ");
			addSublistItem(c, info, subItens);
		}

		if (LavenderePdaConfig.mostraDimensoesNosDetalhes()) {
			String dsDimensoes = itemPedido.dsDimensoes == null ? itemPedido.getProduto().dsDimensoes : itemPedido.dsDimensoes;
			info.append(StringUtil.getStringValue(dsDimensoes));
			addSublistItem(c, info, subItens);
		}

		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && ValueUtil.isNotEmpty(itemPedido.cdDescProgressivo)) {
			DescProgressivoConfig config = DescProgressivoConfigService.getInstance().findByCdDescProgressivo(itemPedido.cdDescProgressivo);
			if (config != null) {
				info.append(config.dsDescProgressivo + " [" + config.cdDescProgressivo + "]");
				addSublistItem(c, info, subItens);
			}
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && itemPedido.isAgrupadorSimilaridade()) {
			if (subItens.size() % 2 != 0) addSublistItem(c, info, subItens);
			String qtItemDistribuida = StringUtil.getStringValueToInterface(itemPedido.getQtItemFisicoOrg(), LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
			info.append(MessageUtil.getMessage(Messages.ITEMPEDIDO_QTD_DISTRIBUIDA, qtItemDistribuida));
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem()) {
			final String preco = StringUtil.getStringValueToInterface(itemPedido.getVlEmbalagemElementar());
			info.append(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_UNID_RS, preco));
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.isConfigReservaEstoqueCorrente || LavenderePdaConfig.usaReservaEstoqueCorrenteR3() ) {
			if (ItemPedidoService.getInstance().isItemPedidoProblemaReservaEstoque(pedido.itemPedidoProblemaReservaEstoqueList, itemPedido)) {
				info.append(Messages.LABEL_ESTD).append(StringUtil.getStringValue(itemPedido.qtEstoqueDisponivel));
				addSublistItem(c, info, subItens);
			}
		}
		if (LavenderePdaConfig.isPermiteItemBonificado() && itemPedido.isKitTipo3() && itemPedido.isItemBonificacao()) {
			info.append(Messages.ITEMKIT_BONIFICADO);
			addSublistItem(c, info, subItens);
		}
		if (LavenderePdaConfig.isConfigStatusItemPedido() && itemPedido.pedido.isFlOrigemPedidoErp() && !itemPedido.pedido.isPedidoPendente()) {
			if (subItens.size() % 2 != 0) addSublistItem(c, info, subItens);
			String dsStatusItemPedido = itemPedido.dsStatusItemPedido != null ? itemPedido.dsStatusItemPedido : ValueUtil.VALOR_NI;
			info.append(MessageUtil.getMessage(Messages.ITEMPEDIDO_CDSTATUSITEMPEDIDO, dsStatusItemPedido));
			addSublistItem(c, info, subItens);
		}
		addSublistItem(c, info, subItens, subItens.size() > 0);
	}
	
	private double getVlVerbaItemPedido(ItemPedido itemPedido) {
		if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			return itemPedido.vlVerbaGrupoItem;
		} 
		return itemPedido.vlVerbaItemPositivo > 0 ? itemPedido.vlVerbaItemPositivo : itemPedido.vlVerbaItem;
	}

	private void addSubItensSimilar(Item c, ItemPedido itemPedido, Vector subItens, StringBuilder info) throws SQLException {
		String qtItemPrincipal = StringUtil.getStringValueToInterface(itemPedido.getQtItemLista(), LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		info.append(MessageUtil.getMessage(Messages.ITEMPEDIDO_PRODUTO_SIMILAR, itemPedido.cdProdutoSimilarOrg));
		addSublistItem(c, info, subItens);
		int i = subItens.size();
		if (i % 2 != 0) {
			addSublistItem(c, info, subItens);
			i++;
		}
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaItensInseridosNoPedido()) {
			info.append(EstoqueService.getInstance().getEstoqueToString(EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque()))+ Messages.PRODUTO_LABEL_EM_ESTOQUE);
			addSublistItem(c, info, subItens);
			i++;
		}
		if (i % 2 != 0) {
			addSublistItem(c, info, subItens);
			i++;
		}
		info.append(MessageUtil.getMessage(Messages.ITEMPEDIDO_QTD_DISTRIBUIDA, qtItemPrincipal));
		addSublistItem(c, info, subItens);
		addSubItens(c, subItens, info);
	}

	private void addSubItens(Item c, Vector subItens, StringBuilder info) {
		if (subItens.size() >= 1) {
			if (LavenderePdaConfig.usaQtColunasSublistItemPedidoPersonalizado()) {
				int qtEspacosEmBranco = LavenderePdaConfig.qtColunasSublistItemPedido() - subItens.size();
				if (qtEspacosEmBranco > 0) {
					for (int i = 0; i < qtEspacosEmBranco; i++) {
						addSublistItem(c, info.append(""), subItens);
					}
				}
			} else {
				c.addSublistItem((String[]) subItens.toObjectArray());
			}
		}
	}

	private BaseDomain getPacoteFilter(ItemPedido itemPedido) {
		Pacote pacoteFilter = new Pacote();
		pacoteFilter.cdEmpresa = itemPedido.cdEmpresa;
		pacoteFilter.cdRepresentante = itemPedido.cdRepresentante;
		pacoteFilter.cdPacote = itemPedido.cdPacote;
		return pacoteFilter;
	}

	private void addSublistItem(Item c, StringBuilder info, Vector subItens) {
		addSublistItem(c, info, subItens, false);
	}

	private void addSublistItem(Item c, StringBuilder info, Vector subItens, boolean forceAdd) {
		int columns = LavenderePdaConfig.qtColunasSublistItemPedido();
		subItens.addElement(info.toString());
		info.setLength(0);
		if (subItens.size() == columns || forceAdd) {
			c.addSublistItem((String[]) subItens.toObjectArray());
			subItens.removeAllElements();
		}
	}

	@Override
	protected String getToolTip(BaseDomain domain) throws SQLException {
    	ItemPedido itemPedido = (ItemPedido) domain;
    	//--
    	StringBuffer str = getDsItemPedido(itemPedido);
    	return str.toString();
    }

	private StringBuffer getDsItemPedido(ItemPedido itemPedido) throws SQLException {
		StringBuffer str = new StringBuffer();
		if (LavenderePdaConfig.mostraSequencialNaDescricaoItemPedido()) {
			str.append(StringUtil.getStringValueToInterface(itemPedido.nuSeqItemPedido)).append(" - ");
		}
		if (!LavenderePdaConfig.ocultaColunaCdProduto) {
			str.append(itemPedido.cdProduto);
		}
		str.append(getCamposComplementaresParaDescricaoItemPedido(itemPedido));
		String dsReferencia = dsReferencia(itemPedido);
		if (ValueUtil.isNotEmpty(dsReferencia)) {
			if (str.length() > 0) {
				str.append(" - ");
			}
			str.append(dsReferencia);
		}
		if ((itemPedido != null) && itemPedido.dsProduto != null && ValueUtil.isEmpty(dsReferencia) ) {
			if (str.length() > 0) {
				str.append(" - ");
			}
			str.append(itemPedido.dsProduto);
		}
		return str;
	}

	private String dsReferencia(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto4()) {
			if (ValueUtil.isNotEmpty(itemPedido.itemPedidoPorGradePrecoList)) {
				return ItemPedidoGradeService.getInstance().getDescricaoItemPedidoNivel1(itemPedido);
			} else {
				ItemPedidoGrade itemPedidoGradeFilter = ItemPedidoGradeService.getInstance().montaItemPedidoGradeByItemPedido(itemPedido);
				return ItemPedidoGradeService.getInstance().getDescricaoProdutoGradeCompleta(itemPedidoGradeFilter);
			}
		}
		return ProdutoService.getInstance().getDescricaoProdutoComReferencia(itemPedido.getProduto());
	}


	private boolean possuiVerbaManual(ItemPedido itemPedido) throws SQLException {
		double soma = ValueUtil.round(itemPedido.vlTotalItemPedido + (itemPedido.vlVerbaItem * -1));
		double vlBase = ValueUtil.round(itemPedido.getItemTabelaPreco() != null ? itemPedido.getItemTabelaPreco().vlBase : 0);
		double vlBaseTotalItemPedido = ValueUtil.round(vlBase * itemPedido.getQtItemFisico());
		if (soma < vlBaseTotalItemPedido) {
			return true;
		}
		return false;
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda() && tabs.getActiveTab() == 1) {
			return listContainerItemTroca.getSelectedId();
		}
		return listContainer.getSelectedId();
	}

	@Override
	public BaseDomain getSelectedDomain() throws SQLException {
		if (TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE.equals(tipoItemList)) {
			return getItemInOportunidadeList();
		}
		ItemPedido itemPedido = canFindItemAgrupador() ? findItemPedidoAgrupador() : (ItemPedido) super.getSelectedDomain();
		if (LavenderePdaConfig.isUsaGiroProduto() && LavenderePdaConfig.usaSugestaoParaNovoPedidoGiroProduto && itemPedido.giroProduto == null) {
			itemPedido.giroProduto = GiroProdutoService.getInstance().findGiroProdutoByProduto(itemPedido.getProduto(), pedido);
		}
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem || LavenderePdaConfig.usaGradeProduto4() /*|| LavenderePdaConfig.usaGradeProduto5(false)*/) {
			itemPedido.pedido = pedido;
		}
		ItemPedidoService.getInstance().carregaItensGradePreco(itemPedido);
		return itemPedido;
	}

	private boolean canFindItemAgrupador() {
		return (LavenderePdaConfig.usaAgrupadorProdutoSimilar || LavenderePdaConfig.usaAgrupadorSimilaridadeProduto)
				&& LavenderePdaConfig.isUsaSolicitacaoAutorizacao()
				&& isAutoDistSimilar();
	}

	private ItemPedido findItemPedidoAgrupador() {
		ItemPedido agrupador = (ItemPedido)getSelectedDomain2();
		Vector similares = pedido.itemPedidoList;
		int size = similares.size();
		for (int i = 0; i < size; i++) {
			Object item = similares.items[i];
			if (item != null) {
				ItemPedido itemPedido = (ItemPedido)item;
				if (ValueUtil.valueEquals(itemPedido.cdProduto, agrupador.cdProdutoSimilarOrg)) {
					agrupador = itemPedido;
					break;
				}
			}
		}
		return agrupador;
	}

	private BaseDomain getItemInOportunidadeList() throws SQLException {
		String selectedRowKey = getSelectedRowKey();
		int size = pedido.itemPedidoOportunidadeList.size();
		for (int i = 0; i < size; i++) {
			if (selectedRowKey.equals(((ItemPedido) pedido.itemPedidoOportunidadeList.items[i]).rowKey)) {

				ItemPedido itemPedido = (ItemPedido)((ItemPedido) pedido.itemPedidoOportunidadeList.items[i]).clone();
				itemPedido.flOportunidade = ValueUtil.VALOR_SIM;
				return itemPedido;
			}
		}
		return null;
	}

	@Override
	public void updateCurrentRecord(BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda() && itemPedido.isItemTrocaRecolher()) {
			updateCurrentRecordItemTroca(domain);
		} else if (itemPedido.isSugVendaPerson()) {
			updateCurrentRecordItemSugestao(domain);
		} else {
			super.updateCurrentRecord(domain);
		}
		updateTotalizadores();
	}


	public void updateCurrentRecordItemTroca(BaseDomain domain) throws SQLException {
		String[] newRow = getItem(domain);
		if (listContainerItemTroca != null) {
			BaseListContainer.Item item = (BaseListContainer.Item)listContainerItemTroca.getSelectedItem();
			if (item != null) {
				item.setItens(newRow);
				listContainerItemTroca.setContainerBackColor(item, ColorUtil.baseBackColorSystem);
				setPropertiesInRowList(item, domain);
			}
			listContainerItemTroca.updateTotalizerRight();
		}
	}

	public void updateCurrentRecordItemSugestao(BaseDomain domain) throws SQLException {
		String[] newRow = getItem(domain);
		if (listContainer != null) {
			BaseListContainer.Item item = (BaseListContainer.Item)listContainer.getSelectedItem();
			if (item != null) {
				item.setItens(newRow);
				setPropertiesInRowList(item, domain);
			}
			listContainer.updateTotalizerRight();
		} else {
			gridEdit.replace(newRow, gridEdit.getSelectedIndex());
		}
	}

	@Override
	protected int getTop() {
    	if (inWindowMode) {
    		return TOP + HEIGHT_GAP;
    	} else {
    		return super.getTop();
    	}
    }
	
	@Override
	protected void onFormStart() throws SQLException {
		addComponentesFiltroGrupoProduto();
		if (LavenderePdaConfig.apresentaMarcadorProdutoInseridos) {
			UiUtil.add(this, new LabelName(Messages.MARCADOR_MARCADORES), cbMarcador, getLeft(), getNextY());
		}
		UiUtil.add(this, edFiltro, getLeft(), getNextY());
		if (usaInsercaoItensDiferentesLeituraCodigoBarras) {
			UiUtil.add(this, edCdBarrasHidden, LEFT, height - 1, PREFERRED, PREFERRED);
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			UiUtil.add(barBottomContainer, btDescontos, 1);
			if (!showOnlyItensNaoConformeByRestricaoVendaUn && !showOnlyItensNaoConformeByTipoPedido) {
				UiUtil.add(barBottomContainer, btNovo, 5);
			}
		} else {
			if (showOnlyItensNaoConformeByDescProgQtd) {
				UiUtil.add(barBottomContainer, btAplicarDescProg, 1);
			} else {
				if (!showOnlyItensNaoConformeByRestricaoVendaUn && !showOnlyItensNaoConformeByTipoPedido) {
					UiUtil.add(barBottomContainer, btNovo, 5);
				}
			}
		}
		int heigthGrid = FILL - barBottomContainer.getHeight();
		if (usaInsercaoItensDiferentesLeituraCodigoBarras) {
			UiUtil.add(barBottomContainer, btOpcoes, 3);
			if (VmUtil.isAndroid()) {
				UiUtil.add(barBottomContainer, btLeitorCamera, 2);
			}
		} else if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() || LavenderePdaConfig.usaHistoricoVendasPorListaColunaTabelaPreco || (LavenderePdaConfig.isUsaGeracaoCatalogoProduto() && LavenderePdaConfig.isUsaRelCatalogoListaItemPedido())) {
			UiUtil.add(barBottomContainer, btOpcoes, 3);
		}

		if (LavenderePdaConfig.permiteExclusaoNaListaDeItensDoPedido()) {
			if (btNovo.isVisible()) {
				UiUtil.add(barBottomContainer, btExcluir, 4);
			} else {
				UiUtil.add(barBottomContainer, btExcluir, 5);
			}
		}

		if (!pedido.isConsignacaoObrigatoria() && LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
			UiUtil.add(this, tabs, LEFT, getTop() + HEIGHT_GAP, FILL, heigthGrid);
			UiUtil.add(tabs.getContainer(0), sessaoTotalizadores, LEFT, BOTTOM, FILL, UiUtil.getLabelPreferredHeight());
			UiUtil.add(tabs.getContainer(1), sessaoTotalizadoresItemsTroca, LEFT, BOTTOM, FILL, UiUtil.getLabelPreferredHeight());
			UiUtil.add(sessaoTotalizadoresItemsTroca, lvQtTotalItensTroca, LEFT + 3, TOP, PREFERRED, PREFERRED);
		} else {
			UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, UiUtil.getLabelPreferredHeight());
		}
		int gap = listContainer.getTotalizerGap();
		UiUtil.add(sessaoTotalizadores, lvQtTotalItens, LEFT + gap, TOP, PREFERRED, PREFERRED);
		if (LavenderePdaConfig.mostraPontuacaoListaItemBase() || LavenderePdaConfig.mostraPontuacaoListaItemRealizado()) {
			UiUtil.add(sessaoTotalizadores, lvPontuacao, CENTER - 5, TOP, PREFERRED, PREFERRED);
		}
		if (LavenderePdaConfig.isMostraRentabPraticadaSugerida()) {
			UiUtil.add(sessaoTotalizadores, lvRentabilidade, LEFT + gap, AFTER, PREFERRED, PREFERRED);
		}
		boolean addRight = true;
		if (TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(tipoItemList) && LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens() && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				UiUtil.add(sessaoTotalizadores, lvVlTotalItensComTributos, RIGHT - gap, SAME, PREFERRED, PREFERRED);
				addRight = !addRight;
			}
		}

		if (LavenderePdaConfig.mostraTodosPercentuaisMediosNosTotalizadoresDaListaItemPedido()) {
			//Adiciona os dois totalizadores na mesma linha para poupar espaço caso os dois estejam ativos
			if (addRight) {
				UiUtil.add(sessaoTotalizadores, lvPctMedioAcrescDesc, RIGHT - gap, SAME, PREFERRED, PREFERRED);
				UiUtil.add(sessaoTotalizadores, lvPctMedioComissao, RIGHT - gap - lvPctMedioAcrescDesc.getPreferredWidth() - lvPctMedioComissao.getPreferredWidth(), SAME, PREFERRED, PREFERRED);
			} else {
				UiUtil.add(sessaoTotalizadores, lvPctMedioAcrescDesc, LEFT + gap, AFTER, PREFERRED, PREFERRED);
				UiUtil.add(sessaoTotalizadores, lvPctMedioComissao, LEFT + gap + lvPctMedioAcrescDesc.getWidth(), SAME, PREFERRED, PREFERRED);
			}
			addRight = !addRight;
		} else {
			if (LavenderePdaConfig.mostraPercComissaoNaSublistItemPedido()) {
				if (addRight) {
					UiUtil.add(sessaoTotalizadores, lvPctMedioComissao, RIGHT - gap, SAME, PREFERRED, PREFERRED);
				} else {
					UiUtil.add(sessaoTotalizadores, lvPctMedioComissao, LEFT + gap, AFTER, PREFERRED, PREFERRED);
				}
				addRight = !addRight;
			}
			if (LavenderePdaConfig.mostraDescAcrescNaSublistItemPedido()) {
				if (addRight) {
					UiUtil.add(sessaoTotalizadores, lvPctMedioAcrescDesc, RIGHT - gap, SAME, PREFERRED, PREFERRED);
				} else {
					UiUtil.add(sessaoTotalizadores, lvPctMedioAcrescDesc, LEFT + gap, AFTER, PREFERRED, PREFERRED);
				}
				addRight = !addRight;
			}
		}

		if (LavenderePdaConfig.isApresentaTotalEmbalagemVendidaNoPedidoEListaItemPedido()) {
			if (addRight) {
				UiUtil.add(sessaoTotalizadores, lvQtItensFaturados, RIGHT - gap, SAME, PREFERRED, PREFERRED);
			} else {
				UiUtil.add(sessaoTotalizadores, lvQtItensFaturados, LEFT + gap, AFTER, PREFERRED, PREFERRED);
			}
			addRight = !addRight;
		}
		if (TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(tipoItemList) && (LavenderePdaConfig.isUsaCalculoVolumeItemPedidoTotal() || LavenderePdaConfig.isUsaCalculoVolumeItemPedidoTotalizador())) {
			if (addRight) {
				UiUtil.add(sessaoTotalizadores, lvVlTotalVolumeItens, RIGHT - gap, SAME, PREFERRED, PREFERRED);
			} else {
				UiUtil.add(sessaoTotalizadores, lvVlTotalVolumeItens, LEFT + gap, AFTER, PREFERRED, PREFERRED);
			}
			addRight = !addRight;
		}
		if (LavenderePdaConfig.isCalculaPesoTotalPedidoNoTotalizador() && LavenderePdaConfig.mostraVPesoListaItens()) {
			if (addRight) {
				UiUtil.add(sessaoTotalizadores, lvQtPesoPedido, RIGHT - gap, SAME, PREFERRED, PREFERRED);
			} else {
				UiUtil.add(sessaoTotalizadores, lvQtPesoPedido, LEFT + gap, AFTER, PREFERRED, PREFERRED);
			}
		}
		if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() || LavenderePdaConfig.isUsaPoliticaBonificacao()) {
			if (addRight) {
				UiUtil.add(sessaoTotalizadores, lvVlTotalBonificacao, RIGHT - gap, SAME, PREFERRED, PREFERRED);
			} else {
				UiUtil.add(sessaoTotalizadores, lvVlTotalBonificacao, LEFT + gap, AFTER, PREFERRED, PREFERRED);
			}
		}
		sessaoTotalizadores.resizeHeight();
		sessaoTotalizadores.resetSetPositions();
		if (!pedido.isConsignacaoObrigatoria() && LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
			sessaoTotalizadoresItemsTroca.resizeHeight();
			sessaoTotalizadoresItemsTroca.resetSetPositions();
			sessaoTotalizadoresItemsTroca.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
			sessaoTotalizadores.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
		} else {
			sessaoTotalizadores.setRect(LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
		}
		if (!showOnlyItensNaoConforme) {
			heigthGrid -= sessaoTotalizadores.getHeight();
		}
		if (inWindowMode) {
			heigthGrid = FILL;
		}
		if (!pedido.isConsignacaoObrigatoria() && LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda()) {
			UiUtil.add(tabs.getContainer(0), listContainer, LEFT, TOP, FILL, FILL - sessaoTotalizadores.getHeight());
			UiUtil.add(tabs.getContainer(1), listContainerItemTroca, LEFT, TOP, FILL, FILL - sessaoTotalizadoresItemsTroca.getHeight());
		} else {
			UiUtil.add(this, listContainer, LEFT, edFiltro.getY2() + HEIGHT_GAP_BIG, FILL, heigthGrid);
		}
	}

	private boolean usaListItemTroca() throws SQLException {
		return !pedido.isConsignacaoObrigatoria() && LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && pedido.isPedidoVenda();
	}

	@Override
	protected void filtrarClick() throws SQLException {
		String filtro = edFiltro.getText();
		dsFiltro = TermoCorrecaoService.getInstance().getDsTermoCorrigido(edFiltro.getText());
		if (filtro.startsWith("*") && !dsFiltro.startsWith("*")) {
			dsFiltro = "*" + dsFiltro;
		}
		clearGrid();
		list();
		setFocusInListContainer();
	}

	private void addComponentesFiltroGrupoProduto() {
		if (LavenderePdaConfig.usaFiltroGrupoProdutoListaItemPedido && cadPedidoForm.inRelatorioMode) {
			if (LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					UiUtil.add(this, lbGrupoProduto1e2, getLeft(), getNextY(), PREFERRED, PREFERRED);
					UiUtil.add(this, cbGrupoProduto1, getLeft(), getNextY(), FILL - (width / 2));
					UiUtil.add(this, cbGrupoProduto2, AFTER + WIDTH_GAP, SAME);
				} else {
					UiUtil.add(this, lbGrupoProduto2, cbGrupoProduto2, getLeft(), getNextY());
				}
				UiUtil.add(this, lbGrupoProduto3e4, getLeft(), getNextY(), PREFERRED, PREFERRED);
				UiUtil.add(this, cbGrupoProduto3, getLeft(), getNextY(), FILL - (width / 2));
				UiUtil.add(this, cbGrupoProduto4, AFTER + WIDTH_GAP, SAME);
			} else if (LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					UiUtil.add(this, lbGrupoProduto1, cbGrupoProduto1, getLeft(), getNextY());
					UiUtil.add(this, lbGrupoProduto2e3, getLeft(), getNextY(), PREFERRED, PREFERRED);
					UiUtil.add(this, cbGrupoProduto2, getLeft(), AFTER, FILL - (width / 2));
					UiUtil.add(this, cbGrupoProduto3, AFTER + WIDTH_GAP, SAME);
				} else {
					UiUtil.add(this, lbGrupoProduto2, cbGrupoProduto2, getLeft(), getNextY());
					UiUtil.add(this, lbGrupoProduto3, cbGrupoProduto3, getLeft(), getNextY());
				}
			} else if (LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					UiUtil.add(this, lbGrupoProduto1, cbGrupoProduto1, getLeft(), getNextY());
				}
				UiUtil.add(this, lbGrupoProduto2, cbGrupoProduto2, getLeft(), getNextY());
			} else if (LavenderePdaConfig.usaFiltroGrupoProduto == 1 && !LavenderePdaConfig.ocultaGrupoProduto1) {
				UiUtil.add(this, lbGrupoProduto1, cbGrupoProduto1, getLeft(), getNextY());
			}
		}
	}

	@Override
	public void loadDefaultFilters() throws SQLException {
		edFiltro.setText("");
		edFiltroCd.setText("");
		//GrupoProduto
		if (LavenderePdaConfig.usaFiltroGrupoProdutoListaItemPedido && cadPedidoForm.inRelatorioMode) {
	        if (LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
	        	cbGrupoProduto1.setSelectedIndex(0);
				if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
					cbGrupoProduto2.setSelectedIndex(0);
				}
				if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
					cbGrupoProduto3.setSelectedIndex(0);
				}
				if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
					cbGrupoProduto4.setSelectedIndex(0);
				}
	        }
		}
	}

	@Override
	public void list() throws SQLException {
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao && (mapIconsMarcadores == null || mapIconsMarcadores.size() == 0)) {
    		useLeftTopIcons = true;
    		mapIconsMarcadores = new HashMap<String, Image>();
    		int iconSize = listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size;
    		ProdutoService.getInstance().loadImagesMarcadores(mapIconsMarcadores, iconSize);
    	}
		super.list();
		if (usaListItemTroca()) {
			listItemTroca();
		}
	}

	@Override
	protected Image[] getIconsLegend(BaseDomain domain) throws SQLException {
		Vector images = new Vector();
		ItemPedido itemPedido = (ItemPedido) domain;
		Produto produto = itemPedido.getProduto();
		if (LavenderePdaConfig.apresentaMarcadorProdutoInsercao || LavenderePdaConfig.apresentaMarcadorProdutoInseridos) {
			if (ValueUtil.isEmpty(produto.cdMarcadores)) {
				produto.cdMarcadores = MarcadorProdutoService.getInstance().findMarcadoresByProduto(produto, pedido.cdCliente);
			}
			if (ValueUtil.isNotEmpty(produto.cdMarcadores)) {
				Image[] iconsMarcadores = ProdutoService.getInstance().getIconsMarcadores(produto, mapIconsMarcadores);
				images = iconsMarcadores != null ? new Vector(iconsMarcadores) : new Vector();
			}
		}
		if (isItemPedidoGondola(itemPedido)) {
			useLeftTopIcons = true;
			images.addElement(UiUtil.getImage("images/warning.png"));
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && ValueUtil.isNotEmpty(produto.cdAgrupadorSimilaridade) && !LavenderePdaConfig.isIgnoraAgrupadorSimilaridade() && itemPedido.solAutorizacaoItemPedidoCache.getIsItemAutorizadoSimilar(itemPedido, null)) {
			images.addElement(iconSimilar);
		}
		int size = images.size();
		if (size > 0) {
			Image[] icons = new Image[size];
			for (int i = 0; i < size; i++) {
				icons[i] = (Image) images.items[i];
			}
			return icons;
		}
		return super.getIconsLegend(domain);
	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		// Necessario para girar a tela, já que é uma intancia
		int dif = Settings.screenWidth - barBottomContainer.getWidth();
		if ((dif > 5) || (dif < -5)) {
			reposition();
		}
		boolean enable = cadPedidoForm.isEnabled();
		btOpcoes.setVisible(enable);
		if (LavenderePdaConfig.permiteExclusaoNaListaDeItensDoPedido()) {
			btExcluir.setVisible(enable);
		}
		btLeitorCamera.setVisible(enable);
		edCdBarrasHidden.setEditable(enable);
		edCdBarrasHidden.setVisible(false);
		clearEdCdBarrasHidden();
		setFocusEdCdBarrasHidden();
		recalcularRentabilidadePedidoSeNecessario(RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_ACESSO_LISTA_ITENS);
		if (LavenderePdaConfig.apresentaMarcadorProdutoInseridos) {
			cbMarcador.load(false);
		}
		if (listContainer != null) {
			listContainer.setCheckable(pedido.isPedidoAberto() && cadPedidoForm.isEnabled() && LavenderePdaConfig.permiteExclusaoNaListaDeItensDoPedido());
		}
		if (listContainerItemTroca != null) {
			listContainerItemTroca.setCheckable(pedido.isPedidoAberto() && cadPedidoForm.isEnabled() && LavenderePdaConfig.permiteExclusaoNaListaDeItensDoPedido());
		}
		reposition();
		visibleState();
	}
	
	private void recalcularRentabilidadePedidoSeNecessario(RecalculoRentabilidadeOptions option) throws SQLException {
		MargemRentabService.getInstance().recalcularRentabilidadePedidoAbertoSeNecessario(pedido, option);
	}

	@Override
	public void onFormExibition() throws SQLException {
		if (LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip && !PedidoService.getInstance().possuiCondicaoComercial(pedido) || pedido.refreshListItemPedidoFromPoliticaBonificacaoAtualizada) {
			pedido.refreshListItemPedidoFromPoliticaBonificacaoAtualizada = false;
			list();
		}
		super.onFormExibition();
		if (showListDescontoCredito) {
			showListDescontoCredito = false;
			ProdutoCreditoDesc domainFilter = new ProdutoCreditoDesc();
			domainFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			domainFilter.flTipoCadastroProduto = ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO;
			if (ValueUtil.isNotEmpty(ProdutoCreditoDescService.getInstance().findAllByItemPedidoList(pedido, domainFilter))) {
				btDescontosClick();
			}
		}
		clearEdCdBarrasHidden();
		setFocusEdCdBarrasHidden();
		repaintNow();
	}


	@Override
	public void onEvent(Event event) {
		switch (event.type) {
			case KeyEvent.KEY_PRESS: {
				setFocusEdCdBarrasHidden();
				if (ValueUtil.isEmpty(edCdBarrasHidden.getText())) {
					edCdBarrasHidden.postEvent(event);
				}
				break;
			}
		}
		super.onEvent(event);

	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btAplicarDescProg) {
					aplicarDescProgAutoClick();
				} else if (event.target == btDescontos) {
					btDescontosClick();
				} else if (event.target == btLeitorCamera) {
					realizaLeituraCamera();
				}
				if (LavenderePdaConfig.usaFiltroGrupoProdutoListaItemPedido && cadPedidoForm.inRelatorioMode) {
					if (event.target == cbGrupoProduto1) {
						cbGrupoProduto1Change();
						btFiltrarClick(event.target);
	    			} else if (event.target == cbGrupoProduto2) {
	    				cbGrupoProduto2Change();
						btFiltrarClick(event.target);
	     			} else if (event.target == cbGrupoProduto3) {
	     				cbGrupoProduto3Change();
						btFiltrarClick(event.target);
	     			} else if (event.target == cbGrupoProduto4) {
	     				btFiltrarClick(event.target);
	     			}
				}
				if (event.target == cbMarcador) {
     				btFiltrarClick(cbMarcador);
     			}
				break;
			}
	    	case KeyEvent.SPECIAL_KEY_PRESS: {
	    		KeyEvent ke = (KeyEvent) event;
				if (usaInsercaoItensDiferentesLeituraCodigoBarras && ke.isActionKey()) {
					leituraPelaCameraAparelho = false;
					insereItemPedidoByCdBarras();
					if (ValueUtil.isNotEmpty(msgSucesso)) {
						WmwToast.show(msgSucesso, 1000);
					}
				}
				break;
	    	}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && singleClickOn && (listContainerItemTroca != null && listContainerItemTroca.isEventoClickUnicoDisparado())) {
					listContainerItemTroca.setEventoClickUnicoDisparado(false);
					singleClickInList();
				}
				break;
			}
			case ButtonOptionsEvent.OPTION_PRESS: {
				if (event.target == btOpcoes) {
					if (btOpcoes.selectedItem.equals(Messages.ITEMPEDIDO_LISTA_MENU_INFERIOR_INSERCAO_MANUAL)) {
						novoClick();
					}
					if (btOpcoes.selectedItem.equals(Messages.BOTAO_DESCONTO_VENDAS_MES)) {
						if (LavenderePdaConfig.isConfigColunasDescontoVolumeVendaMensalDesligado()) {
							UiUtil.showErrorMessage(Messages.DESCONTOQUANTIDADE_VOLUME_VENDAS_CONFIG_COLUNAS_EXCEPTION);
						} else {
							new ListDescontoVendaVolumeMensal(pedido).popup();
						}
					}
					if (btOpcoes.selectedItem.equals(Messages.BOTAO_HISTLISTATABPRECO)) {
						TabelaPreco tabPreco = pedido.getTabelaPreco();
						if (tabPreco != null && ValueUtil.isNotEmpty(tabPreco.cdTabelaPreco)) {
							new RelHistListaTabPrecoWindow(pedido, tabPreco.cdListaTabelaPreco, tabPreco.cdColunaTabelaPreco, null, null).popup();
						} else {
							UiUtil.showWarnMessage(Messages.PEDIDO_HISTORICO_TABPRECO_VAZIO);
						}
					}
					if (btOpcoes.selectedItem.equals(Messages.GERAR_CATALOGO)) {
						btGerarCatalogoClick();
					}
				}
				break;
			}
		}
		if (usaInsercaoItensDiferentesLeituraCodigoBarras && isNotCommonMouseKeyboardEvents(event.type)) {
			setFocusEdCdBarrasHidden();
		}
	}

	private boolean isNotCommonMouseKeyboardEvents(int eventType) {
		return MouseEvent.MOUSE_IN != eventType
				&& MouseEvent.MOUSE_OUT != eventType
				&& MouseEvent.MOUSE_MOVE != eventType
				&& DragEvent.PEN_DRAG != eventType
				&& KeyEvent.KEY_PRESS != eventType
				&& KeyboardEvent.KEYBOARD_PRESS != eventType;
	}

	protected void cbGrupoProduto1Change() throws SQLException {
		cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), pedido);
		cbGrupoProduto4.setSelectedIndex(0);
	}

	private void cbGrupoProduto2Change() throws SQLException {
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), pedido);
		cbGrupoProduto4.setSelectedIndex(0);
	}

	private void cbGrupoProduto3Change() throws SQLException {
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue(), pedido);
		cbGrupoProduto4.setSelectedIndex(0);
	}

	private void btFiltrarClick(Object target) throws SQLException {
		try {
			list();
		} finally {
			setFocusInListContainer();
		}
	}

	public void setFocusInListContainer() {
    	listContainer.setFocus();
    	showRequestedFocus();
    }

	private void btDescontosClick() throws SQLException {
		String cdTabelaPreco = getCdTabelaPreco();
		ListProdutoCreditoDescontoWindow listProdutoCreditoDescontoWindow = new ListProdutoCreditoDescontoWindow(pedido, cdTabelaPreco);
		listProdutoCreditoDescontoWindow.popup();
		if (listProdutoCreditoDescontoWindow.cdProduto != null) {
			for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
				ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
				if (itemPedido.cdProduto.equals(listProdutoCreditoDescontoWindow.cdProduto)) {
					listContainer.setSelectedItemById(itemPedido.getRowKey());
					break;
				}
			}
			if (getBaseCrudCadForm() instanceof CadItemPedidoForm) {
				((CadItemPedidoForm) getBaseCrudCadForm()).disabledBtNextPrev = true;
				((CadItemPedidoForm) getBaseCrudCadForm()).showPopUpCredDesc = true;
			}
			showListDescontoCredito = true;
			singleClickInList();
		}
	}

	private String getCdTabelaPreco() {
		if (LavenderePdaConfig.ocultaTabelaPrecoPedido) {
			if (ValueUtil.isNotEmpty(pedido.itemPedidoList)) {
				return ((ItemPedido) pedido.itemPedidoList.items[0]).cdTabelaPreco;
			}
		}
		return pedido.cdTabelaPreco;
	}

	private void insereItemPedidoByCdBarras() {
		synchronized (insereItemByLeitorCdBarrasLock) {
			String cdBarras = edCdBarrasHidden.getValue();
			edCdBarrasHidden.setText("");
			try {
				ProdutoBase produto = getProduto(cdBarras);
				if (ValueUtil.isNotEmpty(produto.cdProduto)) {
					LoadingBoxWindow pb = UiUtil.createProcessingMessage();
					try {
						pb.popupNonBlocking();
						listContainer.setSelectedIndex(-1);
						boolean itemTrocaRec = tabs.getActiveTab() == 1;
						ItemPedido itemPedido;
						msgSucesso = "";
						CadItemPedidoForm cadItemPedidoForm = (CadItemPedidoForm) getBaseCrudCadForm();
						itemPedido = cadItemPedidoForm.salvaItemPelaListaComCdBarras(cadPedidoForm.getPedido(), produto.cdProduto, itemTrocaRec);
						montaMsgSucesso(itemPedido);
						if (itemTrocaRec) {
							listItemTroca();
						} else {
							list();
						}
					} finally {
						pb.unpop();
					}
				} else {
					throw new ValidationException(Messages.ITEMPEDIDO_LISTA_MSG_PRODUTO_NAO_ENCONTRADO);
				}
			} catch (Throwable ex) {
				if (LavenderePdaConfig.emiteBeepDeErro) {
					SoundUtil.soundError();
				}
				UiUtil.showErrorMessageVerificaEnter(MessageUtil.getMessage(Messages.MSG_TITULO_CODIGO_BARRAS, cdBarras), ex);
				msgSucesso = "";
			} finally {
				setFocusEdCdBarrasHidden();
			}
		}
	}

	private ProdutoBase getProduto(String cdBarras) throws SQLException {
		boolean usaProdutoTabPreco = LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco();
		ProdutoBase produto = usaProdutoTabPreco ? new ProdutoTabPreco() : new Produto();
		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produto.nuCodigoBarras = cdBarras;
		Vector produtoList = usaProdutoTabPreco ? ProdutoTabPrecoService.getInstance().findAllByExample(produto) : ProdutoService.getInstance().findAllByExample(produto);
		if (produtoList.size() == 0) {
			throw new ValidationException(Messages.ITEMPEDIDO_LISTA_MSG_PRODUTO_NAO_ENCONTRADO);
		} else if (produtoList.size() > 1) {
			throw new ValidationException(Messages.ITEMPEDIDO_LISTA_MSG_CODIGO_BARRAS_DUPLICADO);
		}
		produto = (ProdutoBase) produtoList.items[0];
		if (produto == null) {
			produto = usaProdutoTabPreco ? new ProdutoTabPreco() : new Produto();
		}
		return produto;
	}

	private void montaMsgSucesso(ItemPedido itemPedido) throws SQLException {
		if (itemPedido != null) {
			String dsProduto = StringUtil.getStringValue(itemPedido.getProduto() != null ? itemPedido.getProduto().dsProduto : itemPedido.cdProduto);
			if (!leituraPelaCameraAparelho) {
				dsProduto = Convert.insertLineBreak(width - HEIGHT_GAP_BIG * 2, WmwToast.getFont().fm, dsProduto);
			}
			msgSucesso = ValueUtil.isNotEmpty(dsProduto) ? dsProduto + "\nQtd. " + StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()) : "";
		} else {
			msgSucesso = "";
		}
	}

	private void setFocusEdCdBarrasHidden() {
		if (usaInsercaoItensDiferentesLeituraCodigoBarras) {
			edCdBarrasHidden.requestFocus();
			int length = edCdBarrasHidden.getText().length();
			edCdBarrasHidden.setCursorPos(length, length);
		}
	}

	private void clearEdCdBarrasHidden() {
		if (usaInsercaoItensDiferentesLeituraCodigoBarras) {
			synchronized (insereItemByLeitorCdBarrasLock) {
				edCdBarrasHidden.setText("");
			}
		}
	}

	@Override
	protected void beforeOrder() {
		super.beforeOrder();
		setFocusEdCdBarrasHidden();
	}

	protected void realizaLeituraCamera() {
		String dsInfo = "";
		leituraPelaCameraAparelho = true;
		String data;
		while ((data = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, dsInfo)) != null) {
			edCdBarrasHidden.setText(data);
			dsInfo = "";
			try {
				insereItemPedidoByCdBarras();
				dsInfo = msgSucesso;
			} catch (Throwable e) {
				UiUtil.showErrorMessage(e);
			}
		}
	}

	protected void addItensOnButtonMenu() throws SQLException {
		btOpcoes.removeAll();
		if (usaInsercaoItensDiferentesLeituraCodigoBarras) {
			btOpcoes.addItem(Messages.ITEMPEDIDO_LISTA_MENU_INFERIOR_INSERCAO_MANUAL);
		}
		if (LavenderePdaConfig.isUsaDescontoPorVolumeVendaMensal() && pedido.isPedidoAberto()) {
			btOpcoes.addItem(Messages.BOTAO_DESCONTO_VENDAS_MES);
		}
		if (LavenderePdaConfig.usaHistoricoVendasPorListaColunaTabelaPreco) {
			btOpcoes.addItem(Messages.BOTAO_HISTLISTATABPRECO);
		}
		if (LavenderePdaConfig.isUsaGeracaoCatalogoProduto() && LavenderePdaConfig.isUsaRelCatalogoListaItemPedido()) {
			btOpcoes.addItem(Messages.GERAR_CATALOGO);
		}
	}

	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		listInicialized = false;
		ItemPedido.sortAttr = "NUSEQITEMPEDIDO";
		if (usaInsercaoItensDiferentesLeituraCodigoBarras) {
			cadPedidoForm.afterCrudItemPedido();
		}
		if (LavenderePdaConfig.naoConsomeVerbaAutomaticamenteAoFecharPedido) {
			showOnlyItensNegociacaoConsumoVerba = false;
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			invalidateInstance();
		}
	}

	public void novoClickFromRelProdutosPendentes() throws SQLException {
		if (getBaseCrudCadForm() instanceof CadItemPedidoForm) {
			((CadItemPedidoForm) getBaseCrudCadForm()).flFromCadPedido = false;
		} else if (getBaseCrudCadForm() instanceof CadItemPedidoBonificacaoForm) {
			((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).flFromCadPedido = false;
		}
		showCadItemPedido = true;
		novoItemPedido(true);
	}

	@Override
	public void novoClick() throws SQLException {
		if (getBaseCrudCadForm() instanceof AbstractBaseCadItemPedidoForm) {
			AbstractBaseCadItemPedidoForm baseCadItemPedidoForm = (AbstractBaseCadItemPedidoForm) getBaseCrudCadForm();
			baseCadItemPedidoForm.flFromCadPedido = false;
			baseCadItemPedidoForm.fromListItemPedidoForm = true;
		}
		showCadItemPedido = true;
		novoItemPedido(false);
	}

	private void novoItemPedido(boolean fromRelProdutosPendentes) throws SQLException {
		PedidoService.getInstance().validateBateria();
		PedidoService.getInstance().validateObrigaRelacionarPedidoBonificaoTroca(pedido);
		if (getBaseCrudCadForm() instanceof CadItemPedidoForm) {
			if (LavenderePdaConfig.usaFaceamento()) {
				Vector itemPedidoList = PedidoService.getInstance().findItemPedidoListByFaceamentoEstoque(pedido);
				PedidoService.getInstance().insertItemPedidoByItemPedidoList(pedido, itemPedidoList, Pedido.SUGESTAO_PEDIDO_BASEADO_ULTIMO_PEDIDO);
				PedidoUiUtil.showRelInsercaoPedidoWindow(pedido);
			}
			//--
			CadItemPedidoForm cadItemPedidoForm = (CadItemPedidoForm) getBaseCrudCadForm();
			cadItemPedidoForm.add();
			if (showCadItemPedido) {
				cadItemPedidoForm.show();
				if (LavenderePdaConfig.showGiroProdutoBtNovoItemClick() && !pedido.isSugestaoPedidoGiroProduto() && !fromRelProdutosPendentes) {
					cadItemPedidoForm.btGiroProdutoClick(true);
				}
				if (! cadItemPedidoForm.fromProdutoPendenteGiroMultInsercao) {
					showPopupSugestaoVenda();
				}
			}
		} else if (getBaseCrudCadForm() instanceof CadItemPedidoBonificacaoForm) {
			((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).add();
			((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).show();
		} else {
			((CadItemTrocaForm) getBaseCrudCadForm()).flFromListTroca = true;
			super.novoClick();
		}
	}

	private void showPopupSugestaoVenda() throws SQLException {
		if (!pedido.isPedidoBonificacao()) {
			CadItemPedidoForm cadItemPedidoForm = (CadItemPedidoForm) getBaseCrudCadForm();
			if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) {
				if (ValueUtil.VALOR_SIM.equals(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.NAOHOUVEPRIMEIROACESSOMULTIPLASSUGESTOES, pedido.getRowKey()))) {
					ConfigInternoService.getInstance().removeConfigInternoGeral(ConfigInterno.NAOHOUVEPRIMEIROACESSOMULTIPLASSUGESTOES, pedido.getRowKey());
					cadItemPedidoForm.loadSugVendaPersonWindow();
				}
			} else if (LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido()) {
				if (ValueUtil.VALOR_SIM.equals(ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.NAOHOUVEPRIMEIROACESSOMULTIPLASSUGESTOES, pedido.getRowKey()))) {
					ListMultiplasSugestoesProdutosWindow listMultiplasSugestoesProdutosWindow = cadItemPedidoForm.getListMultiplasSugestoesProdutosNovoItemWindow(false);
					ConfigInternoService.getInstance().removeConfigInternoGeral(ConfigInterno.NAOHOUVEPRIMEIROACESSOMULTIPLASSUGESTOES, pedido.getRowKey());
					if (listMultiplasSugestoesProdutosWindow.hasSugestoes()) {
						listMultiplasSugestoesProdutosWindow.popup();
						cadItemPedidoForm.afterShowMultSugesProdutosWindow(listMultiplasSugestoesProdutosWindow);
					}
				}
			} else if (LavenderePdaConfig.isUsaSugestaoVendaComCadastroNovoItem() && ValueUtil.isEmpty(pedido.itemPedidoList)) {
				ListProdutoSugestaoSemQtdeWindow listProdutoSugestaoSemQtdeWindow = cadItemPedidoForm.getListProdutoSugestaoSemQtdeWindow(false, false);
				if (listProdutoSugestaoSemQtdeWindow.hasSugestaoVenda()) {
					listProdutoSugestaoSemQtdeWindow.popup();
				} else {
					ListProdutoSugestaoComQtdeWindow listProdutoSugestaoComQtdeWindow = cadItemPedidoForm.getListProdutoSugestaoComQtdeWindow(false, false);
					if (listProdutoSugestaoComQtdeWindow.hasSugestaoVenda()) {
						listProdutoSugestaoComQtdeWindow.popup();
					}
				}
			} else if (LavenderePdaConfig.usaSugestaoVendaBaseadaDifPedidos > 0 && ValueUtil.isEmpty(pedido.itemPedidoList)) {
				ListProdutoSugestaoDifPedidoWindow listProdutoSugestaoDifPedidoWindow = cadItemPedidoForm.getListProdutoSugestaoDifPedidoWindow(false, false);
				if (listProdutoSugestaoDifPedidoWindow.hasSugestaoVendaProd()) {
					listProdutoSugestaoDifPedidoWindow.popup();
				}
			}
		}
	}

	public void novoClickFromCadPedido() throws SQLException {
		if (pedido.isPedidoBonificacao()) {
			((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		} else {
			((CadItemPedidoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		}
		showCadItemPedido = !usaInsercaoItensDiferentesLeituraCodigoBarras;
		novoItemPedido(false);
	}

	public boolean showListMultiplasSugestoesProdutosFechamentoPedido() throws SQLException {
		CadItemPedidoForm cadItemPedidoForm = (CadItemPedidoForm) getBaseCrudCadForm();
		ListMultiplasSugestoesProdutosWindow listMultiplasSugestoesProdutos = cadItemPedidoForm.getListMultiplasSugestoesProdutosWindow(false);
		cadItemPedidoForm.flFromCadPedido = true;
		if (listMultiplasSugestoesProdutos.hasSugestoes()) {
			cadItemPedidoForm.add();
			cadItemPedidoForm.show();
			listMultiplasSugestoesProdutos.popup();
			cadItemPedidoForm.afterShowMultSugesProdutosWindow(listMultiplasSugestoesProdutos);
			return false;
		}
		return true;
	}

	public void showSugestaoProdutosFechamentoPedido() throws SQLException {
		ListProdutoSugestaoSemQtdeWindow listProdutoSugestaoWindow = ((CadItemPedidoForm) getBaseCrudCadForm()).getListProdutoSugestaoSemQtdeWindow(true, false);
		((CadItemPedidoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		((CadItemPedidoForm) getBaseCrudCadForm()).add();
		((CadItemPedidoForm) getBaseCrudCadForm()).show();
		listProdutoSugestaoWindow.popup();
	}

	public void showSugestaoProdutosComQtdeFechamentoPedido() throws SQLException {
		ListProdutoSugestaoComQtdeWindow listProdutoSugestaoWindow = ((CadItemPedidoForm) getBaseCrudCadForm()).getListProdutoSugestaoComQtdeWindow(true, false);
		((CadItemPedidoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		((CadItemPedidoForm) getBaseCrudCadForm()).add();
		((CadItemPedidoForm) getBaseCrudCadForm()).show();
		listProdutoSugestaoWindow.popup();
	}

	public void showSugestaoProdDifPedidoFechamentoPedido() throws SQLException {
		ListProdutoSugestaoDifPedidoWindow listProdutoSugestaoWindow = ((CadItemPedidoForm) getBaseCrudCadForm()).getListProdutoSugestaoDifPedidoWindow(true, false);
		((CadItemPedidoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		((CadItemPedidoForm) getBaseCrudCadForm()).add();
		((CadItemPedidoForm) getBaseCrudCadForm()).show();
		listProdutoSugestaoWindow.popup();
	}

	public void showItemValorExtrapoladoFechamentoPedido() throws SQLException {
		ItemParticipacaoExtrapoladoWindow itemParticipacaoExtrapoladoWindow = ((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).getListItemValorExtrapoladoWindow();
		((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).add();
		((CadItemPedidoBonificacaoForm) getBaseCrudCadForm()).show();
		itemParticipacaoExtrapoladoWindow.popup();
	}

	public void showItemPedidoAbaixoPesoMinimoFechamentoPedido() throws SQLException {
		ListItemPedidoAbaixoPesoMinimoWindow listItemPedidoAbaixoPesoMinimoWindow = ((CadItemPedidoForm) getBaseCrudCadForm()).getListItemPedidoAbaixoPesoMinimoWindow(true, false);
		((CadItemPedidoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		((CadItemPedidoForm) getBaseCrudCadForm()).add();
		((CadItemPedidoForm) getBaseCrudCadForm()).show();
		listItemPedidoAbaixoPesoMinimoWindow.popup();
	}

	public void showItemPedidoAbaixoValorMinimoFechamentoPedido() throws SQLException {
		ListItemPedidoAbaixoValorMinimoWindow listItemPedidoAbaixoValorMinimoWindow = ((CadItemPedidoForm) getBaseCrudCadForm()).getListItemPedidoAbaixoValorMinimoWindow(true, false);
		((CadItemPedidoForm) getBaseCrudCadForm()).flFromCadPedido = true;
		((CadItemPedidoForm) getBaseCrudCadForm()).add();
		((CadItemPedidoForm) getBaseCrudCadForm()).show();
		listItemPedidoAbaixoValorMinimoWindow.popup();
	}

	private boolean isAutoDistSimilar() {
		return getBaseCrudCadForm() instanceof CadItemPedidoForm
				&& ((CadItemPedidoForm)getBaseCrudCadForm()).autoOpenDistSimilar;
	}

	private void setAutoOpenDistSimilar(boolean autoOpenDistSimilar) {
		if (getBaseCrudCadForm() instanceof CadItemPedidoForm) {
			((CadItemPedidoForm) getBaseCrudCadForm()).autoOpenDistSimilar = autoOpenDistSimilar;
		}
	}
	
	@Override
	public void detalhesClick() throws SQLException {
		if (inWindowMode) {
			return;
		}
		if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			ItemPedido itemPedido = (ItemPedido) getSelectedDomain2();
			if (itemPedido.isItemSimilar) {
				setAutoOpenDistSimilar(true);
				if (canFindItemAgrupador()) {
					listContainer.setSelectedItemById(getSelectedDomain().getRowKey());
				}
			}
		}

		PedidoService.getInstance().validateBateria();
		if (getBaseCrudCadForm() instanceof CadItemTrocaForm) {
			((CadItemTrocaForm) getBaseCrudCadForm()).flFromListTroca = true;
		}
		if (pedido.isPedidoAberto() && cadPedidoForm.inRelatorioMode) {
			cadPedidoForm.inRelatorioMode = false;
		}
		if (getBaseCrudCadForm() instanceof AbstractBaseCadItemPedidoForm) {
			((AbstractBaseCadItemPedidoForm) getBaseCrudCadForm()).fromListItemPedidoForm = true;
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto && pedido.isPedidoVenda() && ProdutoCreditoDescService.getInstance().validaProdutoSemVigencia(pedido)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.PRODUTOCREDITODESCONTO_VIGENCIA_ULTRAPASSADA)) {
				pedido.qtdCreditoDescontoConsumido = 0;
				pedido.qtdCreditoDescontoGerado = 0;
				for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
					ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
					itemPedido.qtdCreditoDesc = 0;
					itemPedido.flTipoCadastroItem = null;
					itemPedido.cdProdutoCreditoDesc = null;
					PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
					ItemPedidoService.getInstance().calculate(itemPedido, pedido);
					itemPedido.oldQtItemFisicoDescQtd = 0;
					ProdutoCreditoDescService.getInstance().atualizaQtdCreditoPedido(itemPedido);
					ItemPedidoService.getInstance().updateItemSimples(itemPedido);
				}
				PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
				ProdutoCreditoDescService.getInstance().loadCreditosPedido(pedido);
				int index = listContainer.getSelectedIndex();
				list();
				listContainer.setSelectedIndex(index);
			} else {
				return;
			}
		}
		
		ItemPedido itemPedido = (ItemPedido) getSelectedDomain();
		
		if (pedido.isPedidoAberto() || pedido.isPedidoReaberto) {
			itemPedido.pedido = pedido;
			if (LavenderePdaConfig.isConfigGradeProduto()) {
				if (isProblemaEmTodasAsGrades(itemPedido)) {
					return;
				}
			}
			
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto && !itemPedido.isAgrupadorSimilaridade()) {
				if (!LavenderePdaConfig.isIgnoraAgrupadorSimilaridade() && itemPedido.solAutorizacaoItemPedidoCache.getIsItemAutorizadoSimilar(itemPedido, null)) {
					ItemPedido itemPedidoAgrupador = (ItemPedido)getSelectedDomain2();
					Produto produto = itemPedidoAgrupador.getProduto();
					if (produto.itemTabelaPreco == null) {
						produto.itemTabelaPreco = new ItemTabelaPreco();
						produto.itemTabelaPreco.cdTabelaPreco = pedido.cdTabelaPreco;
					}
					produto.qtItemPedido = itemPedidoAgrupador.getQtItemFisico();
					ListItemProdutoAgrupadorWindow window = new ListItemProdutoAgrupadorWindow(itemPedidoAgrupador);
					window.popup();
					ItemPedidoService.getInstance().updateItemAutorizadoAgrupador(itemPedidoAgrupador);
					if (window.saved) {
						ItemPedidoAgrSimilarService.getInstance().insereItensPedidoAgrSimilar(itemPedidoAgrupador);
						pedido.itemPedidoList.removeElement(itemPedido);
						pedido.itemPedidoList.addElement(itemPedidoAgrupador);
					}
				}
			}
		}
		super.detalhesClick();
	}
	
	private boolean isProblemaEmTodasAsGrades(final ItemPedido itemPedido) throws SQLException {
		int validaInconsistenciaComAGrade = ItemPedidoGradeService.getInstance().verificaInconsistenciasGrade(itemPedido);
		if (validaInconsistenciaComAGrade == ItemGrade.ITEMGRADELIST_PROBLEMA_EM_TODAS_AS_GRADES) {
			if (!isCancelaExclusao()){
				itemPedido.pedido = itemPedido.pedido != null ? itemPedido.pedido : pedido;
				CadItemPedidoForm.getInstance(cadPedidoForm, pedido).delete(itemPedido);
				if (pedido.isPedidoAberto()) {
					cadPedidoForm.afterCrudItemPedido();
				}
				list();
			}
			return true;
		} else if (validaInconsistenciaComAGrade == ItemGrade.ITEMGRADELIST_PROBLEMA_EM_ALGUMAS_GRADES) {
			UiUtil.showErrorMessage(Messages.MENSAGEM_ERRO_ALGUMAS_GRADES_NAO_ENCONTRADAS);
		}
		return false;
	}

	private boolean isCancelaExclusao() {
		return UiUtil.showConfirmYesCancelMessage(Messages.MENSAGEM_ERRO_NENHUMA_GRADE_ENCONTRADA) == 0;
	}

	@Override
	public void close() throws SQLException {
		super.close();
		if (pedido.isPedidoAberto() && cadPedidoForm.inRelatorioMode) {
			cadPedidoForm.inRelatorioMode = false;
		}
		if ((!TipoItemPedido.TIPOITEMPEDIDO_NORMAL.equals(tipoItemList)) && LavenderePdaConfig.usaModuloTrocaNoPedido && !pedido.isPedidoTroca()) {
			MainLavenderePda.getInstance().repaintNow();
			cadPedidoForm.btTrocaClick();
		}
		dsFiltro = null;
	}

	private void aplicarDescProgAutoClick() throws SQLException {
		double vlPctDescProgPrevisto = pedido.getVlPctDescProgressivo();
		int result = UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.DESCONTO_PROGRESSIVO_MSG_APLICAR_DESC_ITENS_BLOQUEADOS, vlPctDescProgPrevisto));
		if (result == 1) {
			try {
				ItemPedidoService.getInstance().aplicaDescontoProgressivoItensBloqueados(DescProgQtdService.getInstance().getItensDescontoMaiorDescProgressivo(pedido), vlPctDescProgPrevisto);
				PedidoService.getInstance().updatePedidoAndItens(pedido);
				voltarClick();
			} catch (Throwable ex) {
				UiUtil.showErrorMessage(Messages.DESCONTO_PROGRESSIVO_MSG_ERRO_APLICAR_DESC_ITENS_BLOQUEADOS);
			}
		}
	}
	
	@Override
	protected Image getBtTopRightImage() {
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			return UiUtil.getColorfulImage("images/itensOportunidade.png", UiUtil.getAppBarImageIconSize(), UiUtil.getAppBarImageIconSize());
		} else {
			return null;
		}
	}

	@Override
	protected void btTopRightClick() throws SQLException {
		super.btTopRightClick();
		ListItemPedidoForm listItemPedidoForm = getNewListItemPedido(cadPedidoForm, pedido, TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE);
		listItemPedidoForm.show();
	}

	@Override
	protected String getBtTopRightTitle() {
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			return StringUtil.getStringValue(pedido.itemPedidoOportunidadeList.size());
		} else {
			return "";
		}
	}

	@Override
	public void initCabecalhoRodape() throws SQLException {
		super.initCabecalhoRodape();
		updateVisibiltiBtTopRight();
	}

	@Override
	public void updateVisibilityBtAlert() {
		super.updateVisibilityBtAlert();
		updateVisibiltiBtTopRight();
		if (btTopRight != null && btTopRight.isVisible() && btAlert != null) {
			btAlert.setVisible(false);
		}
	}

	private void updateVisibiltiBtTopRight() {
		try {
			if (LavenderePdaConfig.usaOportunidadeVenda && pedido.getTipoPedido() != null && btTopRight != null) {
				btTopRight.setVisible(ValueUtil.isNotEmpty(pedido.itemPedidoOportunidadeList));
				repaintTitleAndButtons();
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		if (TipoItemPedido.TIPOITEMPEDIDO_OPORTUNIDADE.equals(tipoItemList) && btTopRight != null) {
			btTopRight.setVisible(false);
		}
	}

	public void listItemTroca() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = null;
		try {
			listContainerItemTroca.removeAllContainers();
			listContainerItemTroca.uncheckAll();
			//---
			domainList =  getItemTrocaDomainList();
			listSize = domainList.size();
			Container[] all = new Container[listSize];
			//--
			if (listSize > 0) {
				BaseListContainer.Item c;
				BaseDomain domain;
				for (int i = 0; i < listSize; i++) {
			        all[i] = c = new BaseListContainer.Item(listContainerItemTroca.getLayout());
			        domain = (BaseDomain)domainList.items[i];
			        c.id = domain.getRowKey();
			        c.setItens(getItem(domain));
			        c.setToolTip(getToolTip(domain));
			        c.setIconsLegend(getIconsLegend(domain), resizeIconsLegend);
			        setPropertiesInRowList(c, domain);
			        domain = null;
				}
				listContainerItemTroca.addContainers(all);
				configureContainersBtHidden();
			}
			afterList(domainList);
		} finally {
			domainList = null;
			msg.unpop();
		}
	}


	private Vector getItemTrocaDomainList() throws SQLException {
		BaseDomain baseDomain = getDomainFilter();
		baseDomain.sortAtributte = listContainerItemTroca.atributteSortSelected;
		baseDomain.sortAsc = sortAsc;
		Vector list = PedidoService.getInstance().getItensListByTipo(TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC, pedido);
		ItemPedido.sortAttr = baseDomain.sortAtributte;
   		if ((LavenderePdaConfig.usaOrdemNumericaColunaCodigoProduto && COL_CDPRODUTO.equals(baseDomain.sortAtributte)) || "NUSEQITEMPEDIDO".equals(baseDomain.sortAtributte)) {
   			SortUtil.qsortInt(list.items, 0, list.size() - 1, true);
   		} else {
   			list.qsort();
   		}
   		//Ordenação desc
   		if (baseDomain.sortAsc.startsWith(ValueUtil.VALOR_NAO)) {
   			list.reverse();
   		}
   		updateTotalizadores(list);
		return list;
	}

    protected GridListContainer constructorListContainer(boolean isCadPedidoFormEnabled) {
    	configListContainer(LavenderePdaConfig.usaOrdenacaoDescricaoListaItemPedido ? "DSPRODUTO" : "NUSEQITEMPEDIDO");
    	int itemCount = LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista ? 4 : 2;
		GridListContainer listContainer = new GridListContainer(itemCount, 2, isCadPedidoFormEnabled && LavenderePdaConfig.permiteExclusaoNaListaDeItensDoPedido(), false, true, true);
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			listContainer.setColTotalizerRight(1, Messages.ITEMPEDIDO_LABEL_VLTOTALITENSPEDIDOS);
		}
		int size = 1;
		size = LavenderePdaConfig.isMostraDescricaoReferencia() ? size + 1 : size;
		size = LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido() ? size + 1 : size;
		size = LavenderePdaConfig.usaOrdenacaoVerbaItemPedido ? size + 1 : size;
		size = LavenderePdaConfig.mostraPontuacaoListaItemBase() ? size + 1 : size;
		size = LavenderePdaConfig.mostraPontuacaoListaItemRealizado() ? size + 1 : size;
		size = LavenderePdaConfig.usaConfigMargemContribuicao() ? size + 1 : size;
	    size = pedido.isGondola() ? size + 1 : size;
	    size = LavenderePdaConfig.isExibeComboMenuInferior() ? size + 1 : size;
	    size = !LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto() ? size + 1 : size;
		size = !LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto() ? size + 1 : size;
	    size = LavenderePdaConfig.isMostraOrdenacaoRelevanciaProduto() ? size + 1 : size; 
		size = LavenderePdaConfig.isMostraOrdenacaoEstoqueProduto() ? size + 1 : size;

		String[][] matriz = new String[size][2];
		int position = 1;
		matriz[0][0] = Messages.ITEM_PEDIDO_SEQ_INSERCAO;
		matriz[0][1] = "NUSEQITEMPEDIDO";
		
		if (!LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_CODIGO;
			matriz[position][1] = "CDPRODUTO";
			position++;
		}
		if (!LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSPRODUTO;
			matriz[position][1] = "DSPRODUTO";
			position++;
		}
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSREFERENCIA;
			matriz[position][1] = "DSREFERENCIA";
			position++;
		}
		if (LavenderePdaConfig.usaOrdenacaoVerbaItemPedido) {
			matriz[position][0] = Messages.PEDIDO_LABEL_VLTOTVERBA_CONSUMIDA;
			matriz[position][1] = "VLVERBAITEM";
			position++;
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicao()) {
			matriz[position] = new String[]{Messages.ITEMPEDIDO_PERC_MARGEM, "VLPCTTOTALMARGEMITEM"};
			position++;
		}
		if (pedido.isGondola()) {
			matriz[position++] = new String[]{Messages.LISTA_ITEMPEDIDO_LABEL_GONDOLA_ORDENACAO, ItemPedido.DS_COLUNA_QTITEMGONDOLA};
		}
		if (LavenderePdaConfig.mostraPontuacaoListaItemBase()) {
			matriz[position][0] = Messages.PONTUACAO_PEDIDO_BASE;
			matriz[position][1] = "VLPONTUACAOBASEITEM";
			position++;
		}
		if (LavenderePdaConfig.mostraPontuacaoListaItemRealizado()) {
			matriz[position][0] = Messages.PONTUACAO_PEDIDO_REALIZADA;
			matriz[position][1] = "VLPONTUACAOREALIZADOITEM";
			position++;
		}
		if (LavenderePdaConfig.isExibeComboMenuInferior()) {
			matriz[position][0] = Messages.COMBO_NOME_ENTIDADE;
			matriz[position][1] = ItemPedido.DS_COLUNA_CDCOMBO;
			position++;
		}
		if (LavenderePdaConfig.isMostraOrdenacaoRelevanciaProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_NURELEVANCIA;
			matriz[position][1] = "NURELEVANCIA";
			position++;
		}
		if (LavenderePdaConfig.isMostraOrdenacaoEstoqueProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_QTESTOQUE;
			matriz[position][1] = ProdutoBase.SORT_COLUMN_QTESTOQUE;
			position++;
		}
		if (LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido()) {
			matriz[position][0] = Messages.PRODUTO_VALOR_ST;
			matriz[position][1] = "VLEMBALAGEMST";
			matriz[position][0] = Messages.PRODUTO_VALOR_UNIDADE_ST;
			matriz[position][1] = "";
		}
		listContainer.setColsSort(matriz);
		BaseLayoutListContainer layout = listContainer.getLayout();
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			if (layout.relativeFontSizes.length > 2) {
				layout.relativeFontSizes[2] = layout.relativeFontSizes[0] - 1;
			}
			if (layout.defaultItemColors.length > 2) {
				layout.defaultItemColors[2] = layout.defaultItemColors[0];
			}
			layout.controlGap = 50;
			layout.lineBreak = true;
		}
		if (pedido.isGondola()) {
			listContainer.atributteSortSelected = ItemPedido.DS_COLUNA_QTITEMGONDOLA;
			sortAtributte = ItemPedido.DS_COLUNA_QTITEMGONDOLA;
		}
		setDefaultSortAttribute(matriz);
		return listContainer;
    }
    
    private void setDefaultSortAttribute(String[][] matriz) {
		String sortAscDefault = ListContainerConfig.getDefautOrder(getConfigClassName());
		if (ValueUtil.isEmpty(sortAscDefault)) {
			sortAscDefault = ValueUtil.VALOR_SIM;
		}
		boolean validSortAttribute = false;
		String sortAtributteDefault = ListContainerConfig.getDefautSortColumn(getConfigClassName());
		if (ValueUtil.isNotEmpty(sortAtributteDefault)) {
			for (int i = 0; i < matriz.length; i++) {
				String sortColumn = matriz[i][1];
				if (sortColumn.equalsIgnoreCase(sortAtributteDefault)) {
					validSortAttribute = true;
					break;
				}
			}
		}
		if (!validSortAttribute) {
			sortAscDefault = ValueUtil.VALOR_SIM;
			sortAtributteDefault = "NUSEQITEMPEDIDO";
		}
		sortAtributte = sortAtributteDefault;
		sortAsc = sortAscDefault;
	}

    @Override
	protected String[] getItem(Object domain) throws SQLException {
		return itemPedidoToGridRow((ItemPedido) domain);
	}

	protected String[] itemPedidoToGridRow(ItemPedido itemPedido) throws SQLException {
		Vector itens = new Vector();
		// index 0
		itens.addElement(getDsItemPedido(itemPedido).toString());
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			itens.addElements(new String[]{"",""});
		}
		itens.addElement("");
		return (String[]) itens.toObjectArray();
	}

	private String getCamposComplementaresParaDescricaoItemPedido(ItemPedido itemPedido) {
		String retorno = "";
		List<String> listCamposDescricao = LavenderePdaConfig.getInfoComplementarItemPedidoListaCamposParaDescricaoItemPedido();
		if (listCamposDescricao.isEmpty() || ValueUtil.valueEquals(listCamposDescricao.get(0), ValueUtil.VALOR_NAO)) return retorno;

		retorno += " - [";
		for (int i = 0; i < listCamposDescricao.size(); i++) {
			if (i > 0) retorno += " x ";
			switch (listCamposDescricao.get(i)) {
			case "dtEntrega":
				retorno += itemPedido.dtEntrega != null ? itemPedido.dtEntrega : "";
				break;
			case "vlLargura":
				retorno += ValueUtil.getBigDecimalValue(itemPedido.vlLargura).setScale(LavenderePdaConfig.nuCasasDecimais, BigDecimal.ROUND_HALF_UP);
				break;
			case "vlComprimento":
				retorno += ValueUtil.getBigDecimalValue(itemPedido.vlComprimento).setScale(LavenderePdaConfig.nuCasasDecimais, BigDecimal.ROUND_HALF_UP);
				break;
			case "vlAltura":
				retorno += ValueUtil.getBigDecimalValue(itemPedido.vlAltura).setScale(LavenderePdaConfig.nuCasasDecimais, BigDecimal.ROUND_HALF_UP);
				break;
			case "vlPosVinco1":
				retorno += itemPedido.vlPosVinco1;
				break;
			case "vlPosVinco2":
				retorno += itemPedido.vlPosVinco2;
				break;
			case "vlPosVinco3":
				retorno += itemPedido.vlPosVinco3;
				break;
			case "vlPosVinco4":
				retorno += itemPedido.vlPosVinco4;
				break;
			case "vlPosVinco5":
				retorno += itemPedido.vlPosVinco5;
				break;
			case "vlPosVinco6":
				retorno += itemPedido.vlPosVinco6;
				break;
			case "vlPosVinco7":
				retorno += itemPedido.vlPosVinco7;
				break;
			case "vlPosVinco8":
				retorno += itemPedido.vlPosVinco8;
				break;
			case "vlPosVinco9":
				retorno += itemPedido.vlPosVinco9;
				break;
			case "vlPosVinco10":
				retorno += itemPedido.vlPosVinco10;
				break;
			}
		}
		retorno += "]";
		if (retorno.length() == 5) return "";

		return retorno;
	}

	private String getVlItemPedidoListaItensPedido(ItemPedido itemPedido) {
		if  (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				return StringUtil.getStringValueToInterface(itemPedido.getVlItemComIpiCalcPersonalizado(), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
			} else {
				return StringUtil.getStringValueToInterface(itemPedido.getVlItemComIpi(), LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
			}
		}
		return StringUtil.getStringValueToInterface(itemPedido.vlItemPedido);
	}

	public void loadGrupoProdutos() throws SQLException {
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				lbGrupoProduto1 = new LabelName(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
				cbGrupoProduto1.loadGrupoProduto1(pedido);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				lbGrupoProduto2 = new LabelName(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
				cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), null);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				lbGrupoProduto2e3 = new LabelName(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2 + " / " + Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
				lbGrupoProduto3 = new LabelName(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
				cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				lbGrupoProduto1e2 = new LabelName(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1 + " / " + Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
				lbGrupoProduto3e4 = new LabelName(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3 + " / " + Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
				cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
			}
			loadLabelGruposProdutos();
		}
	}

	@Override
	protected void voltarClick() throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto4())  {
			pedido.itemPedidoList.removeAllElements();
			PedidoService.getInstance().findItemPedidoList(pedido);
		}
		super.voltarClick();
	}

	@Override
	protected void excluirClick() throws SQLException {
		if (ValueUtil.isEmpty(listContainer.getCheckedItens())) {
			UiUtil.showWarnMessage(Messages.ITEMPEDIDO_NENHUM_ITEM_SELECIONADO);
			return;
		}
		if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ITEMPEDIDO_EXCLUIR_VARIOS)) {
			return;
		}
		LoadingBoxWindow popupExcluindoItens = UiUtil.createProcessingMessage(Messages.AGUARDE_EXCLUINDO_ITENS);
		try {
			popupExcluindoItens.popupNonBlocking();
			int [] itensExclusao;
			if (LavenderePdaConfig.isUsaPoliticaBonificacao()) {
				itensExclusao = ordenaListaItensParaExclusao();
			} else {
				itensExclusao = listContainer.getCheckedItens();
			}
			for (int i : itensExclusao) {
				ItemPedido itemPedido = (ItemPedido) ItemPedidoService.getInstance().findByRowKey(listContainer.getId(i));
				if (itemPedido != null) {
					itemPedido.setPedido(pedido);
					if (pedido.isPedidoBonificacao()) {
						PedidoService.getInstance().deleteItemPedidoBonificacao(pedido, itemPedido);
					} else if (itemPedido.isKitTipo3()) {
						if (UiUtil.showWarnConfirmYesNoMessage(MessageUtil.getMessage(Messages.KIT_TIPO_3_EXCLUSAO,
								KitService.getInstance().getDsKit(itemPedido.cdKit, pedido)))) {
							try {
								UiUtil.showProcessingMessage();
								PedidoService.getInstance().deleteItensKit(pedido, itemPedido.cdKit);

							} finally {
								UiUtil.unpopProcessingMessage();
							}
						}

					} else {
						deleteItemPadrao(itemPedido);
					}
				}
				if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
					PontuacaoConfigService.getInstance().reloadPontuacaoValorTotal(pedido, itemPedido);
				}
				if (LavenderePdaConfig.isUsaPoliticaBonificacao() && itemPedido != null && !itemPedido.pedido.getTipoPedido().isIgnoraPoliticaBonificacao()) {
					ItemPedidoBonifCfgService.getInstance().processaPoliticasBonificacaoPedido(itemPedido, false);
					ItemPedidoService.getInstance().marcaItemPedidoPorMotivoPendencia(itemPedido.pedido.itemPedidoList, true);
				}
				if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias) {
					PontuacaoConfigService.getInstance().reloadPontuacaoValorTotal(pedido, itemPedido);
				}
			}
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
				pedido.recalculoDescontoProgressivoDTO = PedidoService.getInstance().atualizaDescProgressivoPedido(pedido);
			}
		} finally {
			popupExcluindoItens.unpop();
			pedido.atualizaLista = true;
			PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido, true);
			cadPedidoForm.afterCrudItemPedido();
		}
		showPopUpRelDiferencasDescProgressivo(pedido);

		list();
		listContainer.repaintNow();
	}

	private int[] ordenaListaItensParaExclusao() {
		int decCursor = listContainer.getCheckedItens().length - 1;
		int incCursor = 0;
		int[] orderedItens = new int[listContainer.getCheckedItens().length];
		ItemPedido itemPedido;
		for (int i : listContainer.getCheckedItens()) {
			Item item = (BaseListContainer.Item) listContainer.getContainer(i);
			itemPedido = (ItemPedido) item.domain;
			if (itemPedido.isItemVendaNormal()) {
				orderedItens[incCursor] = i;
				incCursor++;
			} else {
				orderedItens[decCursor] = i;
				decCursor--;
			}
		}
		return orderedItens;
	}

	protected boolean isInsereMultiplosSemNegociacao() throws SQLException {
		if (pedido.flSugestao) return true;
		return PedidoService.getInstance().isInsereMultiplosSemNegociacao(pedido);
	}

	private void deleteItemPadrao(ItemPedido itemPedido) throws SQLException {
		itemPedido.oldQtEstoqueConsumido = ItemPedidoService.getInstance().getQtItemFisicoConversaoUnidade(itemPedido, itemPedido.getQtItemFisico(), false);
		if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() && itemPedido.isItemBonificacao()) {
			ItemPedido itemBoni = (ItemPedido) itemPedido.clone();
			itemBoni.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO;
			if (itemPedido.pedido.itemPedidoList.contains(itemBoni)) {
				PedidoService.getInstance().deleteItemPedido(pedido, itemBoni, false);
			}
		}
		PedidoService.getInstance().deleteItemPedido(pedido, itemPedido, false);
	}

	@Override
	protected BaseDomain getDomain(BaseDomain domain) {
		return domain;
	}

	public Vector getDomainListSelection() {
		int size = listContainer.size();
		Vector list = new Vector(size);
		for (int i = 0; i < size; i++) {
			Item item = (BaseListContainer.Item)listContainer.getContainer(i);
			if (item != null) {
				ItemPedido itemPedido = (ItemPedido)item.domain;
				if (!itemPedido.isItemSimilar) list.addElement(itemPedido);
			}

		}
		return list;
	}

	private void btGerarCatalogoClick() throws SQLException {
		(new CatalogoProdutoWindow()).popup();
	}
	
	private void showPopUpRelDiferencasDescProgressivo(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			if (pedido.recalculoDescontoProgressivoDTO != null && ValueUtil.isNotEmpty(pedido.recalculoDescontoProgressivoDTO.listItemDescontoDTO)) {
				new RelDiferencasDescontoProgressivoWindow(pedido.recalculoDescontoProgressivoDTO).popup();
			}
		}
	}

}
