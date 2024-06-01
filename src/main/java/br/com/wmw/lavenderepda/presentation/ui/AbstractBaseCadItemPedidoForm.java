package br.com.wmw.lavenderepda.presentation.ui;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.domain.TemaSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.framework.config.Session;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseCrudCadForm;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.BaseScrollContainer;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.event.GridEditEvent;
import br.com.wmw.framework.presentation.ui.event.HideDiscontEvent;
import br.com.wmw.framework.presentation.ui.event.ImageSelectionChangeEvent;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.event.OpenningLastPhotoListEvent;
import br.com.wmw.framework.presentation.ui.event.ResizeListEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseGridEdit;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.BaseLayoutListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.BaseToolTip;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonGroupBoolean;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditNumberTextInteger;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.ImageCarouselSlider;
import br.com.wmw.framework.presentation.ui.ext.ImageCarrousel;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.PushButtonGroupBase;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.ValueChooser;
import br.com.wmw.framework.presentation.ui.ext.WmwInputBox;
import br.com.wmw.framework.util.ClassUtil;
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
import br.com.wmw.lavenderepda.business.builder.ProdutoGradeBuilder;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.DescQuantidadePeso;
import br.com.wmw.lavenderepda.business.domain.Estoque;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevisto;
import br.com.wmw.lavenderepda.business.domain.EstoquePrevistoGeral;
import br.com.wmw.lavenderepda.business.domain.Fornecedor;
import br.com.wmw.lavenderepda.business.domain.GiroProduto;
import br.com.wmw.lavenderepda.business.domain.GrupoProdTipoPed;
import br.com.wmw.lavenderepda.business.domain.ItemGrade;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoErpDif;
import br.com.wmw.lavenderepda.business.domain.ItemTabelaPreco;
import br.com.wmw.lavenderepda.business.domain.LavendereBaseDomain;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.ProdutoBase;
import br.com.wmw.lavenderepda.business.domain.ProdutoCreditoDesc;
import br.com.wmw.lavenderepda.business.domain.ProdutoDestaque;
import br.com.wmw.lavenderepda.business.domain.ProdutoGrade;
import br.com.wmw.lavenderepda.business.domain.ProdutoTabPreco;
import br.com.wmw.lavenderepda.business.domain.ProdutoUnidade;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.TabelaPreco;
import br.com.wmw.lavenderepda.business.domain.TipoItemPedido;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.domain.VideoProduto;
import br.com.wmw.lavenderepda.business.domain.VideoProdutoGrade;
import br.com.wmw.lavenderepda.business.enums.RecalculoRentabilidadeOptions;
import br.com.wmw.lavenderepda.business.enums.TipoSolicitacaoAutorizacaoEnum;
import br.com.wmw.lavenderepda.business.service.BonificacaoService;
import br.com.wmw.lavenderepda.business.service.CalculaEmbalagensService;
import br.com.wmw.lavenderepda.business.service.CalculaEmbalagensService.EmbalagensResultantes;
import br.com.wmw.lavenderepda.business.service.ClienteProdutoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ComiRentabilidadeService;
import br.com.wmw.lavenderepda.business.service.ComissaoPedidoRepService;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ConversaoUnidadeService;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.DescPromocionalService;
import br.com.wmw.lavenderepda.business.service.DescQuantidadeService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.EstoquePrevistoGeralService;
import br.com.wmw.lavenderepda.business.service.EstoqueService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.FotoProdutoService;
import br.com.wmw.lavenderepda.business.service.ItemGradeService;
import br.com.wmw.lavenderepda.business.service.ItemKitService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoBonifCfgService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoErpDifService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.ItemTabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.LogPdaService;
import br.com.wmw.lavenderepda.business.service.LoteProdutoService;
import br.com.wmw.lavenderepda.business.service.MargemRentabFaixaService;
import br.com.wmw.lavenderepda.business.service.MargemRentabService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaProdutoService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.business.service.PontuacaoProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoBloqueadoService;
import br.com.wmw.lavenderepda.business.service.ProdutoClienteService;
import br.com.wmw.lavenderepda.business.service.ProdutoCondPagtoService;
import br.com.wmw.lavenderepda.business.service.ProdutoCreditoDescService;
import br.com.wmw.lavenderepda.business.service.ProdutoDestaqueService;
import br.com.wmw.lavenderepda.business.service.ProdutoFaltaService;
import br.com.wmw.lavenderepda.business.service.ProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.ProdutoIndustriaService;
import br.com.wmw.lavenderepda.business.service.ProdutoRetiradaService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.ProdutoTabPrecoService;
import br.com.wmw.lavenderepda.business.service.ProdutoUnidadeService;
import br.com.wmw.lavenderepda.business.service.RedeService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.business.service.RentabilidadeFaixaService;
import br.com.wmw.lavenderepda.business.service.RestricaoService;
import br.com.wmw.lavenderepda.business.service.RestricaoVendaCliService;
import br.com.wmw.lavenderepda.business.service.RestricaoVendaUnService;
import br.com.wmw.lavenderepda.business.service.STService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.SugVendaPersonService;
import br.com.wmw.lavenderepda.business.service.TabelaPrecoService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.TipoItemGradeService;
import br.com.wmw.lavenderepda.business.service.TipoPedProdDescAcresService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.business.service.UsuarioDescService;
import br.com.wmw.lavenderepda.business.service.VerbaSaldoService;
import br.com.wmw.lavenderepda.business.service.VerbaService;
import br.com.wmw.lavenderepda.business.service.VideoProdutoGradeService;
import br.com.wmw.lavenderepda.business.service.VideoProdutoService;
import br.com.wmw.lavenderepda.business.validation.DescAcresMaximoException;
import br.com.wmw.lavenderepda.business.validation.EstoqueException;
import br.com.wmw.lavenderepda.business.validation.EstoquePrevistoException;
import br.com.wmw.lavenderepda.business.validation.FilterNotInformedException;
import br.com.wmw.lavenderepda.business.validation.ProdutoTipoRelacaoException;
import br.com.wmw.lavenderepda.business.validation.ValidateProdutoInPlataFormaVendaProdutoException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.EstoquePrevistoDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoComercialComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoDescProdComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto1ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto2ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto3ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.GrupoProduto4ComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ItemGradeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.KitComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.LocalComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.ProdutoUnidadeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TabelaPrecoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.CadItemPedidoFormWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.EstoqueLabelButton;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderProdutoWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainer;
import br.com.wmw.lavenderepda.presentation.ui.ext.ItemContainer.CAMPOS_LISTA;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.TotalPedidoLabelButton;
import br.com.wmw.lavenderepda.thread.FotoProdutoThread;
import br.com.wmw.lavenderepda.util.FotoProdutoLazyLoadUtil;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.io.device.scanner.ScanEvent;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Settings;
import totalcross.sys.SpecialKeys;
import totalcross.ui.Button;
import totalcross.ui.Container;
import totalcross.ui.Control;
import totalcross.ui.ImageControl;
import totalcross.ui.PushButtonGroup;
import totalcross.ui.Window;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.GridEvent;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.event.PenEvent;
import totalcross.ui.event.ScrollEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.BigDecimal;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.IntVector;
import totalcross.util.Vector;

public abstract class AbstractBaseCadItemPedidoForm extends BaseCrudCadForm {

	private StringBuffer strToolTip = new StringBuffer(50);
	private Vector itens = new Vector(0);

	public static boolean flFocoQtFaturamento = !LavenderePdaConfig.ocultaQtItemFaturamento;
	public static final String COL_CDPRODUTO = "CDPRODUTO";
	private static final String ID_CAMPO_DT_ESTOQUE_PREVISTO = "138";
	private static final int COL_POSITION_NUCONVERSAOUNIDADE = 2;

	public static final int NU_ACAO_VOLTARCLICK = 1;
	public static final int NU_ACAO_BTLISTAITENSCLICK = 2;
	public static final int NU_ACAO_FILTRAR_PRODUTO = 3;
	public static final int NU_ACAO_DETALHE = 4;

	public boolean flFromCadPedido;
	public boolean onFechamentoPedido;
	public boolean itemSelecionado;
	public boolean showDetalheItemJaInseridoMultInsercao;
	public boolean fromListDetalheInsecaoMult;
	public boolean editingFromList;
	public boolean fromGridClick;
	protected boolean fromItemPedidoInseridoPedido = false;
	public boolean fechouTelaDescontoQuantidade = false;
	public boolean fromRelNotificaoItemWindow = false;
	public boolean fromRelProdutosPendentes = false;
	public boolean fromRelProdutosPendentesCapaPedido = false;
	public boolean fromRelGiroProduto = false;
	protected boolean inicializaItemFromGrid;
	protected boolean habilitaDescontoKitFechado;
	public boolean fromListItemPedidoForm;
	protected boolean filtrandoAgrupadorGrade;
	public boolean fromInfoExtra;
	public boolean fromEdQtItemFisicoFocusOut;
	public boolean fromBtCalcularClick;
	public int offset;
	protected boolean dontRefreshItemsOnNextExhibition = false;

	public Pedido pedido;
	public BaseButton btGradeItemPedido;
	protected ButtonAction btLeitorCamera;
	protected ButtonAction btCalcular;
	protected ButtonAction btAtualizarEstoquePrevistoGeral;
	protected ButtonAction btListaItens;
	protected ButtonAction btSugestao;
	protected ButtonAction btInfoComplementares;
	public ButtonGroupBoolean bgItemTroca;
	public ButtonGroupBoolean bgOportunidade;
	public ButtonGroupBoolean bgGeraVerba;
	public ButtonAction btTrocarEmpresaRep;
	public ProdutoUnidadeComboBox cbUnidadeAlternativa;
	public UnidadeComboBox cbUnidade;
	public EditText edEmbalagem;
	public EstoqueLabelButton lvEstoque;
	public TotalPedidoLabelButton lvTotalPedido;
	public TotalPedidoLabelButton lvVlItemPedido;
	protected GridListContainer listContainer;
	protected SessionTotalizerContainer sessaoTotalizadores;
	protected SessionTotalizerContainer containerTotalizadoresMultIns;
	protected SessionTotalizerContainer sessaoTotalizadoresMultIns;
	protected SessionTotalizerContainer sessaoTotalizadoresFiltroMultIns;
	public CadProdutoDynForm cadProdutoForm;
	public CadPedidoForm cadPedidoForm;
	public BaseGridEdit gridUnidadeAlternativa;
	public PushButtonGroupBase numericPad;
	public BaseEdit edBaseNumpad;
	protected SessionContainer containerGrid;
	public Button btSep3;
	public ButtonOptions btOpcoes;
	public BaseButton btIconeFoto;
	public BaseButton btIconeGrupoDestaque;
	public BaseButton btIconeVerba;
	public BaseButton btIconeComissaoItemPedido;
	public BaseButton btIconeOportunidade;
	public BaseButton btIconeKit;
	public BaseButton btIconeBonificacao;
	public BaseButton btIconeRentabilidade;
	public BaseButton btIconeOcultaInfoRentabilidade;
	public BaseButton btIconeProdCreditoDesc;
	public BaseButton btIconeFracaoFornecedor;
	public BaseButton btVlPesoProdutoPontuacao;
	public BaseButton btIconeAutorizacao;
	public BaseButton btIconeSimilar;
	public BaseButton btIconeDetalheProduto;
	public BaseButton btIconeInfoEstoqueUnidades;
	public ButtonAction btPreviousItem;
	public ButtonAction btNextItem;
	public BaseButton btIconeVideos;
	public Button btAgrupadorGrade;
	public BaseButton btIconeCarrosselPopup;
	public BaseButton btOcultaMostraEditLabels;
	public ImageCarrousel imageCarrousel;
	protected static AbstractBaseCadItemPedidoForm instance;
	protected int qtItensAlteracaoBackupAutomatico;
	public boolean disabledBtNextPrev = false;
	public ImageCarouselSlider imageCarouselSlider;
	//Containers
	protected SessionContainer containerVendaUnitaria;
	protected SessionContainer containerFields2;
	protected BaseScrollContainer containerVendaUnitariaScrollable;
	protected SessionContainer containerInfosProduto;
	private BaseScrollContainer containerTecladoNumerico;
	private BaseScrollContainer containerProduto;
	protected ItemContainer selectedContainer;
	protected BaseScrollContainer containerFotosProdutos;
	protected SessionContainer containerIconsProduto;
	//Campos
	public EditFiltro edFiltro;
	public LabelName lbItemByItem;
	public LabelName lbVlPctDesconto;
	public LabelName lbVlItemPedido;
	public LabelName lbVlPctAcrescimo;
	public LabelName lbVlTotalItemPedido;
	public LabelName lbVlTtItemComDeflator;
	public LabelName lbVlPctMaxDesconto;
	public LabelName lbVlTotalPedido;
	public LabelName lbVlVerbaItem;
	public LabelName lbVlVerbaItemPositiva;
	public LabelName lbVlVerbaItemNegativa;
	public LabelName lbVlVerbaPedido;
	public LabelName lbPrecoTab;
	public LabelName lbEstoque;
	public LabelName lbVlBaseItem;
	public LabelName lbVlPctPreviaDesc;
	public LabelName lbVlItemComissao;
	public LabelName lbVlTotalComissao;
	public LabelName lbVlReducaoSimples;
	public LabelName lbVlST;
	public LabelName lbVlEmbalagemSt;
	public LabelName lbVlItemSt;
	public LabelName lbVlTotalItemSt;
	public LabelName lbVlTotalPedidoST;
	public LabelName lbVlItemStReverso;
	public LabelName lbVlDescontoStReverso;
	public LabelName lbVlTonFreteCliente;
	public LabelName lbVlFretePedido;
	public LabelName lbVlPctDescCliente;
	public LabelName lbVlPctDescontoCondicao;
	public LabelName lbVlPctDescFrete;
	public LabelName lbVlFrete;
	public LabelName lbCaixaPadrao;
	public LabelName lbPrecoMaximoCons;
	public LabelName lbPrecoEmbPrimaria;
	public LabelName lbEmbalagem;
	public LabelName lbQtEmabalagemCompra;
	public LabelName lbPrecoFabrica;
	public LabelName lbTabelaPrecoAbrev;
	public LabelName lbFornecedor;
	public LabelName lbGrupoProduto;
	public LabelName lbGrupoDescProd;
	public LabelName lbCesta;
	public LabelName lbKit;
	public LabelName lbQtItemFisico;
	public LabelName lbQtItemFaturamento;
	public LabelName lbQtEstoqueCondicaoNegociacao;
	public LabelName lbVlBaseFlex;
	public LabelName lbTicketMedioPedido;
	public LabelName lbRentabilidadeItem;
	public LabelName lbRentabilidadePedido;
	public LabelName lbVlUnidadeEmbalagem;
	public LabelName lbVlVerbaManual;
	public LabelName lbVlPctDescPromocional;
	public LabelName lbQtPtItens;
	public LabelName lbQtPtPedido;
	public LabelName lbVlMaxBonificacao;
	public LabelName lbUnidadeAlternativa;
	public LabelName lbNuMultiploIdeal;
	public LabelName lbVlPctComissao;
	public LabelName lbQtMininimaVenda;
	public LabelName lbVlIndiceRentabItem;
	public LabelName lbVlIndiceRentabPedido;
	public LabelName lbVlIndiceRentabEstimadoPedido;
	public LabelName lbVlRetornoProduto;
	public LabelName lbQtEmbalagem;
	public LabelName lbVlEmbalagem;
	public LabelName lbPctMargemAgregada;
	public LabelName lbVlAgregadoSugerido;
	public LabelName lbOportunidade;
	public LabelName lbItemTroca;
	public LabelName lbStItem;
	public LabelName lbStPedido;
	public LabelName lbSaldoCreditoConsignacaoRestanteCliente;
	public LabelName lbGeraVerba;
	public LabelName lbVlItemTabPrecoVariacaoPreco;
	public LabelName lbVlIpi;
	public LabelName lbVlUnitarioIpi;
	public LabelName lbVlItemIpi;
	public LabelName lbVlTotalItemIpi;
	public LabelName lbVlTributos;
	public LabelName lbVlUnitarioTributos;
	public LabelName lbVlItemTributos;
	public LabelName lbVlTotalItemTributos;
	public LabelName lbVlAliquotaIpi;
	public LabelName lbVlTotalPedidoIpi;
	public LabelName lbVlTotalPedidoTributos;
	public LabelName lbQtItemEstoquePositivo;
	public LabelName lbVlTotalItemPositivo;
	public LabelName lbVlTotalPedidoEstoquePositvo;
	public LabelName lbVlTotalItemFrete;
	public LabelName lbVlItemFrete;
	public LabelName lbVlTotalFreteItem;
	public LabelName lbVlTotalPedidoFrete;
	public LabelName lbVlItemPedidoComDescontoCPP;
	public LabelName lbVlPctDescontoCPPItemPedido;
	public LabelName lbVlItemPedidoBruto;
	public LabelName lbVlTotalBrutoItemPedido;
	public LabelName lbVlTotalPedidoBruto;
	public LabelName lbVlEmbalagemElementTributos;
	public LabelName lbVlDesconto;
	public LabelName lbVlPctDesconto2;
	public LabelName lbVlDesconto2;
	public LabelName lbVlPctDesconto3;
	public LabelName lbVlDesconto3;
	public LabelName lbFaixaComissaoRentabilidade;
	public LabelName lbFaixaComissaoRentabilidadeMinima;
	public LabelName lbVlPctComissaoPedido;
	public LabelName lbVlPctVerba;
	public LabelName lbQtPesoCargaPedido;
	public LabelName lbPctPesoCargaPedido;
	public LabelName lbVlPctDescCanal;
	public LabelName lbVlPctDescContratoCli;
	public LabelName lbVlVerbaNecessaria;
	public LabelName lbVlNeutroItem;
	public LabelName lbVlSeguroItemPedido;
	public LabelName lbVlEmbalagemComReducaoSimples;
	public LabelName lbVlEmbalagemPrimariaST;
	public LabelName lbVlItemPedidoFreteESeguro;
	public LabelName lbVlTotalItemPedidoFreteESeguro;
	public LabelName lbPctMaxParticipacaoItemBonificacao;
	public LabelName lbPctAtualParticipacaoItemBonificacao;
	public LabelName lbPesoAtualPedido;
	public LabelName lbPesoMinimoItem;
	public LabelName lbVolumeItem;
	public LabelName lbVolumeTotalItem;
	public LabelName lbVolumePedido;
	public LabelName lbSaldoCreditoRestanteCliente;
	public LabelValue lbDsProduto;
	public LabelName lbItemGrade1;
	public LabelName lbItemGrade2;
	public LabelName lbUnidade;
	public LabelName lbUltimoPrecoPraticado;
	public LabelName lbAliquotaSt;
	public LabelName lbDifStEIpi;
	public LabelName lbQtDesejada;
	public LabelName lbUltimoDescontoPraticado;
	public LabelName lbUltimoAcrescimoPraticado;
	public LabelName lbTxAntecipacao;
	public LabelName lbDtVencimentoPrecoProduto;
	public LabelName lbVlTotalItemTabelaPreco;
	public LabelName lbPercIndiceFinanceiroCondPagto;
	public LabelName lbPctMaxDescProdutoCliente;
	public LabelName lbPctDescPoliticaInterpol;
	public LabelName lbVlDescPoliticaInterpol;
	public LabelName lbVlTotDescPoliticaInterpol;
	public LabelName lbVlPctDescEfetivo;
	public LabelName lbVlQtEstoquePrevisto;
	public LabelName lbDtEstoquePrevisto;
	public LabelName lbVlItemFreteTributacao;
	public LabelName lbVlTotalItemFreteTributacao;
	public LabelName lbVlTotalPedidoFreteTributacao;
	public LabelName lbConsisteConversaoUnidade;
	public LabelName lbVlTotalItemPorPeso;
	public LabelName lbPctDescontoDoPedido;
	public LabelName lbVlPctTotalMargemItem;
	public LabelName lbVlPctDescCondPagto;
	public LabelName lbDtPagamento;
	public LabelName lbVlBaseAntecipacao;
	public LabelName lbVlTotalItemTribFreteSeguro;
	public LabelName lbVlTotalItemUnitarioTribFreteSeguro;
	public LabelName lbVlTotalSeguroItemPedido;
	public LabelName lbVlPesoItemUnitario;
	public LabelName lbVlPesoTotalItem;
	public LabelName lbVlPontuacaoRealizadoItem;
	public LabelName lbVlPontuacaoBaseItem;
	public LabelName lbVlPctMargemRentabItem;
	public LabelName lbVlPctMargemRentabPedido;
	public LabelName lbQtItemGondola;
	public LabelName lbVlPctDescProgressivo;
	public LabelName lbVlPctPoliticaComercial ;
	public LabelName lbVlPctFaixaDescQtdPeso;
	public LabelName lbDsMoedaProduto;
	public LabelName lbVlCotacaoMoeda;
	public LabelName lbPctDescQuantidade;
	public LabelName lbQtSugerida;
	public LabelName lbDsFichaTecnica;
	public LabelName lbQtEstoquePrevistoGeral;
	public LabelName lbDtEstoquePrevistoGeral;
	public LabelName lbQtSomaEstoquePrevistoGeral;
	public LabelName lbStatusItemPedido;
	public LabelValue lvStatusItemPedido;
	public LabelName lbDsInfosPersonalizadasItemPedido;
	public LabelName lbVlUnitGiroProduto;
	public LabelValue lvVlUnitGiroProduto;
	public LabelName lbNuOrdemCompraCliente;
	public LabelName lbNuSeqOrdemCompraCliente;
	
	public EditMemo edDsFichaTecnica;
	public EditMemo edDsInfosPersonalizadasItemPedido;
	public EditNumberFrac edVlPctTotalMargemItem;
	public EditNumberFrac edVlItemPedidoFreteESeguro;
	public EditNumberFrac edVlTotalItemPedidoFreteESeguro;
	public EditNumberFrac edVlVerbaNecessaria;
	public EditNumberFrac edVlNeutroItem;
	public EditNumberFrac edVlEmbalagemComReducaoSimples;
	public EditNumberFrac edVlEmbalagemPrimariaST;
	public EditNumberFrac edVlRetornoProduto;
	public EditNumberFrac edPctPesoCargaPedido;
	public EditNumberFrac edQtPesoCargaPedido;
	public EditNumberFrac edVlEmbalagemElementTributos;
	public EditNumberFrac edQtItemFisico;
	public EditNumberFrac edQtItemFaturamento;
	public EditNumberFrac edVlItemPedido;
	public EditNumberFrac edVlBaseItemPedido;
	public EditNumberFrac edVlPctMaxDesconto;
	public EditNumberFrac edVlTtItemComDeflator;
	public EditNumberFrac edVlPctDesconto;
	public EditNumberFrac edVlPctAcrescimo;
	public EditNumberFrac edVlVerbaItem;
	public EditNumberFrac edVlVerbaItemPositiva;
	public EditNumberFrac edVlVerbaItemNegativa;
	public EditNumberFrac edVlVerbaPedido;
	public EditNumberFrac edVlTotalPedido;
	public EditNumberFrac edVlPctPreviaDesc;
	public EditNumberFrac edVlItemComissao;
	public EditNumberFrac edVlTotalComissao;
	public EditNumberFrac edVlReducaoSimples;
	public EditNumberFrac edVlST;
	public EditNumberFrac edVlEmbalagemSt;
	public EditNumberFrac edVlItemST;
	public EditNumberFrac edVlTotalItemST;
	public EditNumberFrac edVlTotalPedidoST;
	public EditNumberFrac edVlItemStReverso;
	public EditNumberFrac edVlPctDescontoStReverso;
	public EditNumberFrac edVlTonFreteCliente;
	public EditNumberFrac edVlFretePedido;
	public EditNumberFrac edVlFrete;
	public EditNumberFrac edPrecoEmbPrimaria;
	public EditNumberFrac edCaixaPadrao;
	public EditNumberFrac edPrecoMaximoCons;
	public EditNumberFrac edPrecoFabrica;
	public EditNumberFrac edPreco;
	public EditNumberFrac edVlBaseFlex;
	public EditNumberFrac edTicketMedioPedido;
	public EditNumberFrac edRentabilidadeItem;
	public EditNumberFrac edRentabilidadePedido;
	public EditNumberFrac edVlUnidadeEmbalagem;
	public EditNumberFrac edQtEmabalagemCompra;
	public EditNumberFrac edVlVerbaManual;
	public EditNumberFrac edVlPctDescPromocional;
	public EditNumberInt edQtPtItens;
	public EditNumberInt edQtPtPedido;
	public EditNumberInt edQtEstoqueNegociacao;
	public EditNumberFrac edQtDesejada;
	public EditNumberFrac edVlMaxBonificacao;
	public EditNumberFrac edNuMultiploIdeal;
	public EditNumberFrac edVlPctComissao;
	public EditNumberFrac edQtMininimaVenda;
	public EditNumberFrac edVlIndiceRentabItem;
	public EditNumberFrac edVlIndiceRentabPedido;
	public EditNumberFrac edVlIndiceRentabEstimadoPedido;
	public EditNumberFrac edQtEmbalagem;
	public EditNumberFrac edVlEmbalagem;
	public EditNumberFrac edPctMargemAgregada;
	public EditNumberFrac edVlAgregadoSugerido;
	public EditNumberFrac edVlItemTabPrecoVariacaoPreco;
	public EditNumberFrac edVlIpi;
	public EditNumberFrac edVlUnitarioIpi;
	public EditNumberFrac edVlItemIpi;
	public EditNumberFrac edVlTotalItemIpi;
	public EditNumberFrac edVlTributos;
	public EditNumberFrac edVlUnitarioTributos;
	public EditNumberFrac edVlItemTributos;
	public EditNumberFrac edVlTotalItemTributos;
	public EditNumberFrac edVlAliquotaIpi;
	public EditNumberFrac edVlTotalPedidoIpi;
	public EditNumberFrac edVlTotalPedidoTributos;
	public EditNumberFrac edQtItemEstoquePositivo;
	public EditNumberFrac edVlTotalItemPositivo;
	public EditNumberFrac edVlTotalPedidoEstoquePositvo;
	public EditNumberFrac edVlTotalItemFrete;
	public EditNumberFrac edVlItemFrete;
	public EditNumberFrac edVlTotalPedidoFrete;
	public EditNumberFrac edVlTotalFreteItem;
	public EditNumberFrac edVlItemPedidoComDescontoCCP;
	public EditNumberFrac edVlPctDescontoCCPItemPedido;
	public EditNumberFrac edVlItemPedidoBruto;
	public EditNumberFrac edVlTotalBrutoItemPedido;
	public EditNumberFrac edVlTotalPedidoComTributosEDeducoes;
	public EditNumberFrac edVlDesconto;
	public EditNumberFrac edVlPctDesconto2;
	public EditNumberFrac edVlDesconto2;
	public EditNumberFrac edVlPctDesconto3;
	public EditNumberFrac edVlDesconto3;
	public EditNumberFrac edVlPctDescCanal;
	public EditNumberFrac edVlPctDescContratoCli;
	public EditNumberFrac edVlSeguroItemPedido;
	public EditText edFaixaComissaoRentabilidade;
	public EditText edFaixaComissaoRentabilidadeMinima;
	public EditNumberFrac edVlPctComissaoPedido;
	public EditNumberFrac edVlPctVerba;
	public EditNumberFrac edPctMaxParticipacaoItemBonificacao;
	public EditNumberFrac edPctAtualParticipacaoItemBonificacao;
	public EditNumberFrac edPesoAtualPedido;
	public EditNumberFrac edPesoMinimoItem;
	public EditNumberFrac edVolumeItem;
	public EditNumberFrac edVolumeTotalItem;
	public EditNumberFrac edVolumePedido;
	public EditNumberFrac edSaldoCreditoRestanteCliente;
	public EditNumberFrac edStItem;
	public EditNumberFrac edStPedido;
	public EditNumberFrac edVlPctDescCliente;
	public EditNumberFrac edVlPctDescontoCondicao;
	public EditNumberFrac edVlPctDescFrete;
	public EditNumberFrac edSaldoCreditoConsignacaoRestanteCliente;
	public EditNumberFrac edUltimoPrecoPraticado;
	public EditNumberFrac edAliquotaSt;
	public EditNumberFrac edDifStEIpi;
	public EditNumberFrac edUltimoDescontoPraticado;
	public EditNumberFrac edUltimoAcrescimoPraticado;
	public EditNumberFrac edTxAntecipacao;
	public EditDate edDtVencimentoPrecoProduto;
	public EditNumberFrac edVlTotalItemTabelaPreco;
	public EditNumberFrac edPercIndiceFinanceiroCondPagto;
	public EditNumberFrac edPctMaxDescProdutoCliente;
	public EditNumberFrac edPctDescPoliticaInterpol;
	public EditNumberFrac edVlDescPoliticaInterpol;
	public EditNumberFrac edVlTotDescPoliticaInterpol;
	public EditNumberFrac edVlPctDescEfetivo;
	public EditNumberFrac edQtEstoquePrevisto;
	public EditDate edDtEstoquePrevisto;
	public EditNumberFrac edVlItemFreteTributacao;
	public EditNumberFrac edVlTotalItemFreteTributacao;
	public EditNumberFrac edVlTotalPedidoFreteTributacao;
	public EditText edConsisteConversaoUnidade;
	public EditNumberFrac edVlTotalItemPorPeso;
	public EditNumberFrac edPctDescontoDoPedido;
	public EditNumberFrac edVlPctDescCondPagto;
	public EditDate edDtPagamento;
	public EditNumberFrac edVlBaseAntecipacao;
	public EditNumberFrac edVlTotalItemTribFreteSeguro;
	public EditNumberFrac edVlTotalItemUnitarioTribFreteSeguro;
	public EditNumberFrac edVlTotalSeguroItemPedido;
	public EditNumberFrac edVlPesoItemUnitario;
	public EditNumberFrac edVlPesoTotalItem;
	public EditNumberFrac edVlPontuacaoRealizadoItem;
	public EditNumberFrac edVlPontuacaoBaseItem;
	public EditNumberFrac edVlPctMargemRentabItem;
	public EditNumberFrac edVlPctMargemRentabPedido;
	public EditNumberInt edQtItemGondola;
	public EditNumberFrac edVlPctDescProgressivo;
	public EditNumberFrac edVlPctPoliticaComercial;
	public EditNumberFrac edVlPctFaixaDescQtdPeso;
	public EditText edDsMoedaProduto;
	public EditNumberFrac edVlCotacaoMoeda;
	public EditNumberFrac edVlPctDescQuantidade;
	public EditNumberFrac edQtEstoquePrevistoGeral;
	public EditDate edDtEstoquePrevistoGeral;
	public EditNumberFrac edQtSomaEstoquePrevistoGeral;
	public EditNumberFrac edQtSugerida;
	public EditText edNuOrdemCompraCliente;
	public EditNumberTextInteger edNuSeqOrdemCompraCliente;

	// --ToolTip
	public BaseToolTip tipPreviaDesc;
	public BaseToolTip tipDsProduto;
	public BaseToolTip tipEstoque;
	public BaseToolTip tipPontuacaoBase;
	// --Hash's dos campos
	public Hashtable hashEditsTemp;
	public Hashtable hashLabelsTemp;
	public Hashtable hashEdits;
	public Hashtable hashLabels;
	// --Combos
	public ItemGradeComboBox cbItemGrade1;
	public ItemGradeComboBox cbItemGrade2;
	public TabelaPrecoComboBox cbTabelaPreco;
	public CondicaoComercialComboBox cbCondicaoComercial;
	public KitComboBox cbKit;
	public GrupoDescProdComboBox cbGrupoDescProd;
	public GrupoProduto1ComboBox cbGrupoProduto1;
	public GrupoProduto2ComboBox cbGrupoProduto2;
	public GrupoProduto3ComboBox cbGrupoProduto3;
	public GrupoProduto4ComboBox cbGrupoProduto4;
	public FornecedorComboBox cbFornecedor;
	public LocalComboBox cbLocal;
	// --Checks
	public CheckBoolean ckProdutoDescPromocional;
	public CheckBoolean ckProdutoDescQtd;
	// --
	public boolean inVendaUnitariaMode;
	public boolean flListInicialized;
	public boolean listMaximized;
	public boolean produtoComGradeNivel2;
	public boolean produtoComGradeNivel3;
	public String ultimaTabelaPrecoSelecionada = "";
	public String ultimaTabelaPrecoSelecionadaListaItens = "";
	public Produto produtoSelecionado;
	public EditText edDsTabelaPreco;
	public double vlItemPedidoOld;
	public double vlEmbalagemElementarOld;
	public double vlItemIpiOld;
	public boolean houveAlteracaoQtdItem;
	public String dsFiltro;
	public ItemGrade lastItemGradeSelected;
	public ItemGrade lastItemGrade2Selected;
	public int heigthContainerIcons = (int) (UiUtil.getControlPreferredHeight() * 1.2);
	public String msgGrupoDestaque;
	public Produto produtoAnterior;
	public static boolean listInicialized = false;
	public Vector descontoQuantidadeList;
	public String ultimaCondicaoComercialSelected;
	public int imageSize;
	protected boolean isAddingFromArray, isAddingFromListDescQtd;
	private CAMPOS_LISTA[] campos;
	public boolean isEnterPressionado;
	protected Vector listSugVendaPerson;
	private boolean mostraUltimoDescontoAcrescimo = false;
	public boolean fromProdutoPendenteGiroMultInsercao = false;
	private boolean exibeCampoRentabilidadeEstimado = false;
	// --Label Totalizadores
	protected LabelTotalizador lvQtTotalItensInseridos;
	protected LabelTotalizador lvVlTotalPedido;
	protected LabelTotalizador lvQtTotalItensInseridosLista;
	protected LabelTotalizador lvVlTotalPedidoLista;
	protected LabelTotalizador lvQtProdutos;

	protected Vector fotosProdSugList;
	public boolean repaintFilter = true;
	int itemCount = 2;

	private String textoMaximoPadraoComQuebraLinha;
	private int tamanhoMaximoCaracteresLinha;
	private boolean negociacaoMultipla;
	private boolean mostraCheck;
	private boolean useLeftTopIcons;
	private Map<String, Image> mapIconsMarcadores;

	public List<ItemPedido> itemPedidoCarrouselList;
	public boolean inCarrouselMode, inCarrouselProdutoCliente;
	public int carrouselType;
	public boolean firstCarrouselValidation;
	public CadItemPedidoFormWindow carrouselWindow;
	public int selectedItemPedidoCarrousel;
	protected boolean hideAttributesOld;
	protected boolean hideAttributes;
	protected boolean hideAttributesVendaUnitaria;
	protected boolean usaLazyLoading;
	protected boolean buscaLimitadaProduto;
	protected ImageSliderProdutoWindow imageSliderProdutoList;

	protected LoadingBoxWindow popupInserindoItens = UiUtil.createProcessingMessage(Messages.AGUARDE_PROCESSANDO_ITENS);
	protected Object targetFiltroThread;
	protected int nuAcaoThreadMultInsercao;
	protected int nuSeqItemPedido;
	protected boolean isFirstHideAttributesClick = true;
	protected ItemPedido itemPedidoBaseNaoInserido;

	public boolean fromMenuCatalogForm;

	public AbstractBaseCadItemPedidoForm(String newTitle, Pedido pedido, CadPedidoForm cadPedidoForm) throws SQLException {
		super(newTitle);
		this.cadPedidoForm = cadPedidoForm;
		this.cadPedidoForm.setID("cadPedidoForm");
		this.pedido = pedido;
		filtrandoAgrupadorGrade = LavenderePdaConfig.usaGradeProduto5() && ((LavenderePdaConfig.isUsaListaProdutoAgrupadorGrade() && ConfigInternoService.getInstance().isListaModoAgrupadorGrade()) || LavenderePdaConfig.isUsaConfigMenuCatalogo());
		btSalvar = new ButtonAction(FrameworkMessages.BOTAO_OK, "images/ok.png", true);
		btSalvarNovo = new ButtonAction(FrameworkMessages.BOTAO_OK, "images/ok.png", true);
		btSalvarNovo.setID("btSalvarNovo");
		btCalcular = new ButtonAction(Messages.BOTAO_CALCULAR, "images/calcular.png");
		btAtualizarEstoquePrevistoGeral = new ButtonAction(Messages.ATUALIZAR_ESTOQUE, "images/reload.png");
		btListaItens = new ButtonAction(Messages.BOTAO_ITENS_DO_PEDIDO, "images/list.png");
		btListaItens.setID("btListaItens");
		btSugestao = new ButtonAction(Messages.MULTIPLASSUGESTOES_LABEL_BOTAO_SUGESTOES, "images/sugestao.png");
		btInfoComplementares = new ButtonAction(Messages.BOTAO_INFO_COMPLEMENTARES, "images/info_complementares.png");
		if (usaCameraParaLeituraCdBarras()) {
			btLeitorCamera = new ButtonAction(Messages.CAMERA, "images/barcode.png");
		}
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			btGradeItemPedido = new BaseButton(UiUtil.getColorfulImage("images/grade.png", (UiUtil.getControlPreferredHeight() / 3) * 2, (UiUtil.getControlPreferredHeight() / 3) * 2));
			btGradeItemPedido.useBorder = false;
			btGradeItemPedido.transparentBackground = true;
		}
		bgOportunidade = new ButtonGroupBoolean();
		bgOportunidade.setValue(ValueUtil.VALOR_NAO);
		bgItemTroca = new ButtonGroupBoolean();
		bgItemTroca.setValue(ValueUtil.VALOR_NAO);
		bgGeraVerba = new ButtonGroupBoolean();
		bgGeraVerba.setValue(ValueUtil.VALOR_SIM);
		if (LavenderePdaConfig.isMostraFotoProduto()) {
			btIconeFoto = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.isMostraConfigTelaInfoProduto()) {
			btIconeDetalheProduto = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.mostraInfoEstoqueUnidades) {
			btIconeInfoEstoqueUnidades = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			btIconeGrupoDestaque = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex
				|| LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco
				|| LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao
				|| LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto
				|| LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
			btIconeVerba = new BaseButton(getEmptyIcon());
			btIconeVerba.setID("btIconeVerba");
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem() && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			btIconeComissaoItemPedido = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			btIconeOportunidade = new BaseButton(getEmptyIcon());
			btIconeOportunidade.setImage(getIcon("oportunidade.png", true));
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			btIconeProdCreditoDesc = new BaseButton(getEmptyIcon());
			btIconeProdCreditoDesc.setImage(getIcon("descontoCredito.png", true));
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			btIconeBonificacao = new BaseButton(getEmptyIcon());
			btIconeBonificacao.setID("btIconeBonificacao");
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			btIconeKit = new BaseButton(getEmptyIcon());
		}
		if (isExibeIconeRentabilidade()) {
			btIconeRentabilidade = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.mostraSeloPontuacaoItem) {
			btVlPesoProdutoPontuacao = new BaseButton(getEmptyIcon());
			btVlPesoProdutoPontuacao.setBorder(BORDER_ROUNDED);
			btVlPesoProdutoPontuacao.shiftOnPress = false;
			btVlPesoProdutoPontuacao.setID("btVlPesoProdutoPontuacao");
		}
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			int nuFracao = (getItemPedido() != null && getItemPedido().getProduto() != null) ? getItemPedido().getProduto().nuFracaoFornecedor : 0;
			btIconeFracaoFornecedor = new BaseButton(MessageUtil.getMessage(Messages.PRODUTO_LABEL_FRACAO_FORNECEDOR, nuFracao), getEmptyIcon(), RIGHT, WIDTH_GAP);
			btIconeFracaoFornecedor.shiftOnPress = false;
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			btIconeAutorizacao = new BaseButton(getEmptyIcon());
			btIconeAutorizacao.setID("btIconeAutorizacao");
			btIconeAutorizacao.setBorder(BORDER_ROUNDED);
			btIconeAutorizacao.shiftOnPress = false;
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			btIconeSimilar = new BaseButton(getEmptyIcon());
			btIconeSimilar.shiftOnPress = false;
		}
		btIconeOcultaInfoRentabilidade = new BaseButton(getEmptyIcon());
		btPreviousItem = new ButtonAction(null, UiUtil.getIconButtonAction("images/previous.png", ColorUtil.baseAppBarForeColor, true));
		btPreviousItem.setID("btPreviousItem");
		btNextItem = new ButtonAction(null, UiUtil.getIconButtonAction("images/next.png", ColorUtil.baseAppBarForeColor, true));

		btNextItem.setID("btNextItem");
		if (LavenderePdaConfig.isUsaConfigVideosProdutos() || LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			btIconeVideos = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			btIconeCarrosselPopup = new BaseButton(getEmptyIcon());
		}
		if (LavenderePdaConfig.isPermiteOcultarCamposItemPedidoGrade()) {
			btOcultaMostraEditLabels = new BaseButton(getEmptyIcon());
		}
		//--
		lbItemByItem = new LabelName("00/00", CENTER);
		lbItemByItem.setForeColor(ColorUtil.baseAppBarForeColor);
		lbItemByItem.setID("lbItemByItem");
		cbUnidadeAlternativa = new ProdutoUnidadeComboBox();
		cbUnidadeAlternativa.setID("cbUnidadeAlternativa");

		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade = new UnidadeComboBox();
			cbUnidade.setValue(pedido.cdUnidade);
		}
		final String editNumberInicial = "9999999999";
		edEmbalagem = new EditText(editNumberInicial, 9);
		numericPad = new PushButtonGroupBase(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", StringUtil.getStringValue(Settings.decimalSeparator), "<" }, false, -1, -1, UiUtil.defaultGap * 2, 2, true, PushButtonGroup.NORMAL);
		numericPad.borderLarge = true;
		numericPad.setFocusLess(true);
		btSep3 = new Button("");
		btOpcoes = new ButtonOptions();
		btOpcoes.setID("btOpcoes");
		cbItemGrade1 = new ItemGradeComboBox();
		cbItemGrade2 = new ItemGradeComboBox(true);
		cbTabelaPreco = new TabelaPrecoComboBox();
		cbCondicaoComercial = new CondicaoComercialComboBox();
		cbKit = new KitComboBox().setID("cbKit");
		ckProdutoDescPromocional = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_DESC_PROMOCIONAL);
		ckProdutoDescPromocional.setChecked(LavenderePdaConfig.usaDescPromo);
		ckProdutoDescPromocional.setID("ckProdutoDescPromocional");
		ckProdutoDescQtd = new CheckBoolean(Messages.PRODUTO_LABEL_PRODUTO_DESC_QTD);
		ckProdutoDescQtd.setID("ckProdutoDescQtd");
		lbGrupoDescProd = new LabelName(Messages.GRUPOPRODUTO_GRUPOS_DESCONTO_PRODUTO);

		cbFornecedor = new FornecedorComboBox();
		cbGrupoDescProd = new GrupoDescProdComboBox();
		cbGrupoProduto1 = new GrupoProduto1ComboBox();
		cbGrupoProduto2 = new GrupoProduto2ComboBox();
		cbGrupoProduto3 = new GrupoProduto3ComboBox();
		cbGrupoProduto4 = new GrupoProduto4ComboBox();
		if (LavenderePdaConfig.usaDescPromo) {
			cbLocal = new LocalComboBox();
			cbLocal.setEnabled(true);
		}

		// Containers
		containerGrid = new SessionContainer();
		containerGrid.setBackColor(getBackColor());
		containerVendaUnitaria = new SessionContainer();
		containerVendaUnitaria.setBackColor(getBackColor());
		containerVendaUnitariaScrollable = new BaseScrollContainer(false, true, false);
		containerVendaUnitariaScrollable.setBackColor(getBackColor());
		containerInfosProduto = new SessionContainer();
		containerInfosProduto.setBackForeColors(getBackColor(), ColorUtil.componentsForeColor);
		containerProduto = new BaseScrollContainer(true, false);
		containerProduto.setBackForeColors(ColorUtil.sessionContainerBackColor, ColorUtil.sessionContainerForeColor);
		containerTecladoNumerico = new BaseScrollContainer(false, false);
		containerTecladoNumerico.setBackForeColors(ColorUtil.componentsBackColor, ColorUtil.componentsForeColor);
		containerFields2 = new SessionContainer();
		containerFields2.setBackColor(getBackColor());
		containerFotosProdutos = new BaseScrollContainer(true, false);
		containerFotosProdutos.setBackForeColors(getBackColor(), ColorUtil.componentsForeColor);
		containerIconsProduto = new SessionContainer();
		containerIconsProduto.setBackColor(ColorUtil.formsBackColor);
		if (isShowFotoProduto()) {
			createImageCarrousel();
		}
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			imageCarouselSlider = new ImageCarouselSlider(null);
		}
		// -- Totalizadores

		containerTotalizadoresMultIns = new SessionTotalizerContainer();

		sessaoTotalizadores = new SessionTotalizerContainer();
		sessaoTotalizadoresMultIns = new SessionTotalizerContainer();
		lvQtTotalItensInseridos = new LabelTotalizador("999999999,999");
		lvVlTotalPedido = new LabelTotalizador("999999999,999");

		sessaoTotalizadoresFiltroMultIns = new SessionTotalizerContainer();
		lvQtTotalItensInseridosLista = new LabelTotalizador("999999999,999");
		lvVlTotalPedidoLista = new LabelTotalizador("999999999,999");
		lvQtProdutos = new LabelTotalizador("0" + FrameworkMessages.REGISTRO_SINGULAR);

		// -- LABELS
		criaLabelsDinamicos();
		lbItemGrade1 = new LabelName(" ### ");
		lbItemGrade2 = new LabelName(" ### ");
		lbUnidade = new LabelName(Messages.TOOLTIP_LABEL_UNIDADE_ALTERNATIVA);
		if (LavenderePdaConfig.usaFiltroFornecedor) {
			lbFornecedor = new LabelName(Messages.FORNECEDOR_NOME_ENTIDADE);
		}
		lbCesta = new LabelName(LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos() ? Messages.CESTA_NOME_ENTIDADE : " ");
		if (LavenderePdaConfig.isUsaKitProduto()) {
			lbKit = new LabelName(Messages.KIT_LABEL_KIT);
		}
		lbTabelaPrecoAbrev = new LabelName(Messages.TABELAPRECO_NOME_ENTIDADE);
		lbDsProduto = new LabelValue(" ", LEFT);
		lbDsProduto.setID("lbDsProduto");
		lbDsProduto.setForeColor(ColorUtil.sessionContainerForeColor);
		if (LavenderePdaConfig.apresentaDsFichaTecnicaCapaItemPedido && LavenderePdaConfig.usaGradeProduto5()) {
			lbDsFichaTecnica = new LabelName(Messages.ITEMPEDIDO_LABEL_FICHA_TECNICA);
			edDsFichaTecnica = new EditMemo("", 6, 500);
			edDsFichaTecnica.setFont(UiUtil.fontVerySmall);
		}
		if (LavenderePdaConfig.isConfigApresentacaoInfosPersonalizadasCapaItemPedido()) {
			lbDsInfosPersonalizadasItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_INF_PERSOANLIZADAS);
			edDsInfosPersonalizadasItemPedido = new EditMemo("", 6, 500);
			edDsInfosPersonalizadasItemPedido.setFont(UiUtil.fontVerySmall);
			edDsInfosPersonalizadasItemPedido.setEditable(false);
		}
		// - EDITS
		lvEstoque = new EstoqueLabelButton(LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPEDIDO));
		lvEstoque.setID("lvEstoque");
		lvTotalPedido = new TotalPedidoLabelButton();
		lvTotalPedido.setID("lvTotalPedido");
		lvVlItemPedido = new TotalPedidoLabelButton();
		lvVlItemPedido.setID("lvVlItemPedido");
		edDsTabelaPreco = new EditText("@@@@@@@@@@", 100);
		edFiltro = new EditFiltro(editNumberInicial, 100);
		edFiltro.setID("edFiltro");
		if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigo()) {
			if (LavenderePdaConfig.isExibeBotaoParaFiltroCodigoTecladoNumerico()) {
				edFiltro.setEditType(BaseEdit.EDIT_TYPE_INT);
			}
		}
		edFiltro.idAgrupador = Produto.APPOBJ_CAMPOS_FILTRO_PRODUTO;
		edPctPesoCargaPedido = new EditNumberFrac(editNumberInicial, 9);
		edQtPesoCargaPedido = new EditNumberFrac(editNumberInicial, 9);
		edQtItemFisico = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		edQtItemFisico.autoSelect = true;
		edQtItemFisico.setID("edQtItemFisico");
		edQtItemFaturamento = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.isUsaQtdItemPedidoFaturamentoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		edQtItemFaturamento.autoSelect = true;
		edQtItemFaturamento.setID("edQtItemFaturamento");
		edVlVerbaNecessaria = new EditNumberFrac(editNumberInicial, 9);
		edVlNeutroItem = new EditNumberFrac(editNumberInicial, 9);
		edVlEmbalagemComReducaoSimples = new EditNumberFrac(editNumberInicial, 9);
		edVlEmbalagemPrimariaST = new EditNumberFrac(editNumberInicial, 9);
		edPctMaxParticipacaoItemBonificacao = new EditNumberFrac(editNumberInicial, 9);
		edPctAtualParticipacaoItemBonificacao = new EditNumberFrac(editNumberInicial, 9);
		edVlItemPedido = new EditNumberFrac(editNumberInicial, 9).setID("edVlItemPedido");
		edVlItemPedido.autoSelect = true;
		edVlTtItemComDeflator = new EditNumberFrac(editNumberInicial, 9);
		edVlBaseItemPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlPctMaxDesconto = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDesconto = new EditNumberFrac(editNumberInicial, 9).setID("edVlPctDesconto");
		edVlPctDesconto.autoSelect = true;
		edVlPctAcrescimo = new EditNumberFrac(editNumberInicial, 9);
		edVlPctAcrescimo.autoSelect = true;
		edVlPctAcrescimo.setID("edVlPctAcrescimo");
		edVlTotalPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedido.setID("edVlTotalPedido");
		edVlVerbaItem = new EditNumberFrac(editNumberInicial, 9);
		edVlVerbaItem.setID("edVlVerbaItem");
		edVlVerbaItemPositiva = new EditNumberFrac(editNumberInicial, 9);
		edVlVerbaItemNegativa = new EditNumberFrac(editNumberInicial, 9);
		edVlVerbaPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlVerbaPedido.setID("edVlVerbaPedido");
		edPreco = new EditNumberFrac(editNumberInicial, 9);
		edPreco.setID("edPreco");
		edVlPctPreviaDesc = new EditNumberFrac(editNumberInicial, 9);
		edVlItemComissao = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalComissao = new EditNumberFrac(editNumberInicial, 9);
		edVlReducaoSimples = new EditNumberFrac(editNumberInicial, 9);
		edVlST = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlEmbalagemSt = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlItemST = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlItemStReverso = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescontoStReverso = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescontoStReverso.autoSelect = true;
		edVlTonFreteCliente = new EditNumberFrac(editNumberInicial, 9);
		edVlFretePedido = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalItemST = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlFrete = new EditNumberFrac(editNumberInicial, 9);
		boolean naoUsaPadraoFracionado = LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDEMBALAGEM);
		edCaixaPadrao = new EditNumberFrac(editNumberInicial, 9,naoUsaPadraoFracionado ? 0 : ValueUtil.doublePrecisionInterface);
		edCaixaPadrao.setID("edCaixaPadrao");
		edPrecoEmbPrimaria = new EditNumberFrac(editNumberInicial, 9);
		edPrecoEmbPrimaria.autoSelect = true;
		edPrecoMaximoCons = new EditNumberFrac(editNumberInicial, 9);
		edPrecoFabrica = new EditNumberFrac(editNumberInicial, 9);
		edVlBaseFlex = new EditNumberFrac(editNumberInicial, 9);
		edTicketMedioPedido = new EditNumberFrac(editNumberInicial, 9, 4);
		edRentabilidadeItem = new EditNumberFrac(editNumberInicial, 9);
		edRentabilidadePedido = new EditNumberFrac(editNumberInicial, 9);
		edVlUnidadeEmbalagem = new EditNumberFrac(editNumberInicial, 9);
		edVlUnidadeEmbalagem.autoSelect = true;
		edVlRetornoProduto = new EditNumberFrac(editNumberInicial, 9);
		edVlRetornoProduto.autoSelect = true;
		edQtEmabalagemCompra = new EditNumberFrac(editNumberInicial, 9);
		edVlEmbalagemElementTributos = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedidoST = new EditNumberFrac(editNumberInicial, 9);
		edVlVerbaManual = new EditNumberFrac(editNumberInicial, 9);
		edVlVerbaManual.autoSelect = true;
		edVlPctDescPromocional = new EditNumberFrac(editNumberInicial, 9);
		edQtPtItens = new EditNumberInt(editNumberInicial, 9);
		edQtPtPedido = new EditNumberInt(editNumberInicial, 9);
		edQtEstoqueNegociacao = new EditNumberInt(editNumberInicial, 9);
		edVlMaxBonificacao = new EditNumberFrac(editNumberInicial, 9);
		edNuMultiploIdeal = new EditNumberFrac(editNumberInicial, 9);
		edQtMininimaVenda = new EditNumberFrac(editNumberInicial, 9);
		edVlPctComissao = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisComissao);
		edVlIndiceRentabItem = new EditNumberFrac(editNumberInicial, 9);
		edVlIndiceRentabPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlIndiceRentabEstimadoPedido = new EditNumberFrac(editNumberInicial, 9);
		edQtEmbalagem = new EditNumberFrac(editNumberInicial, 9);
		edVlEmbalagem = new EditNumberFrac(editNumberInicial, 9);
		edPctMargemAgregada = new EditNumberFrac(editNumberInicial, 9);
		edPctMargemAgregada.autoSelect = true;
		edVlAgregadoSugerido = new EditNumberFrac(editNumberInicial, 9);
		edVlItemTabPrecoVariacaoPreco = new EditNumberFrac(editNumberInicial, 9);
		edVlAgregadoSugerido.autoSelect = true;
		edVlIpi = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlUnitarioIpi = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimais);
		edVlItemIpi = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlTotalItemIpi = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlTributos = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlUnitarioTributos = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimais);
		edVlItemTributos = new EditNumberFrac(editNumberInicial, 9);
		edVlItemTributos.setID("edVlItemTributos");
		edVlTotalItemTributos = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalItemTributos.setID("edVlTotalItemTributos");
		edVlAliquotaIpi = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedidoIpi = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedidoTributos = new EditNumberFrac(editNumberInicial, 9);
		naoUsaPadraoFracionado = LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEVALIDAVENDIDANOPEDIDO);
		edQtItemEstoquePositivo = new EditNumberFrac(editNumberInicial, 9, naoUsaPadraoFracionado ? 0 : ValueUtil.doublePrecisionInterface);
		edVlTotalItemPositivo = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedidoEstoquePositvo = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalItemFrete = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalFreteItem = new EditNumberFrac(editNumberInicial, 9);
		edVlItemFrete = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedidoFrete = new EditNumberFrac(editNumberInicial, 9);
		edVlItemPedidoComDescontoCCP = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescontoCCPItemPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlItemPedidoBruto = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalBrutoItemPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedidoComTributosEDeducoes = new EditNumberFrac(editNumberInicial, 9);
		edVlDesconto = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDesconto2 = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDesconto2.autoSelect = true;
		edVlDesconto2 = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDesconto3 = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDesconto3.autoSelect = true;
		edVlDesconto3 = new EditNumberFrac(editNumberInicial, 9);
		edFaixaComissaoRentabilidade = new EditText("", 9);
		edFaixaComissaoRentabilidadeMinima = new EditText("", 9);
		edVlPctComissaoPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlPctVerba = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescCanal = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescContratoCli = new EditNumberFrac(editNumberInicial, 9);
		edVlSeguroItemPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlItemPedidoFreteESeguro = new EditNumberFrac(editNumberInicial, 9);
		edVlPctTotalMargemItem = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalItemPedidoFreteESeguro = new EditNumberFrac(editNumberInicial, 9);
		edPesoAtualPedido = new EditNumberFrac(editNumberInicial, 9);
		edPesoMinimoItem = new EditNumberFrac(editNumberInicial, 9);
		edVolumeItem = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlVolume);
		edVolumeTotalItem = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlVolume);
		edVolumePedido = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlVolume);
		edSaldoCreditoRestanteCliente = new EditNumberFrac(editNumberInicial, 9);
		edStItem = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edStPedido = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		edVlPctDescCliente = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescontoCondicao = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescFrete = new EditNumberFrac(editNumberInicial, 9);
		edSaldoCreditoConsignacaoRestanteCliente = new EditNumberFrac("99999", 9);
		tipDsProduto = new BaseToolTip(lbDsProduto, "");
		tipEstoque = new BaseToolTip(lvEstoque, "");
		edUltimoPrecoPraticado = new EditNumberFrac(editNumberInicial, 9);
		edAliquotaSt = new EditNumberFrac(editNumberInicial, 9);
		edDifStEIpi = new EditNumberFrac(editNumberInicial, 9);
		naoUsaPadraoFracionado = LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDDESEJADACOMPRA);
		edQtDesejada = new EditNumberFrac(editNumberInicial, 9, naoUsaPadraoFracionado ? 0 : ValueUtil.doublePrecisionInterface);
		edQtDesejada.autoSelect = true;
		edUltimoDescontoPraticado = new EditNumberFrac(editNumberInicial, 9);
		edUltimoAcrescimoPraticado = new EditNumberFrac(editNumberInicial, 9);
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			edTxAntecipacao = new EditNumberFrac(editNumberInicial, 9, ValueUtil.doublePrecision);
		} else {
			edTxAntecipacao = new EditNumberFrac(editNumberInicial, 9);
		}
		edDtVencimentoPrecoProduto = new EditDate();
		edVlTotalItemTabelaPreco = new EditNumberFrac(editNumberInicial, 9);
		edPercIndiceFinanceiroCondPagto = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescEfetivo = new EditNumberFrac(editNumberInicial, 9);
		edPctMaxDescProdutoCliente = new EditNumberFrac(editNumberInicial, 9);
		edPctDescPoliticaInterpol = new EditNumberFrac(editNumberInicial, 9);
		edVlDescPoliticaInterpol = new EditNumberFrac(editNumberInicial, 9);
		edVlTotDescPoliticaInterpol = new EditNumberFrac(editNumberInicial, 9);
		naoUsaPadraoFracionado = LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPREVISTO);
		edQtEstoquePrevisto = new EditNumberFrac(editNumberInicial, 9, naoUsaPadraoFracionado ? 0 : ValueUtil.doublePrecisionInterface);
		edDtEstoquePrevisto= new EditDate();
		edVlItemFreteTributacao = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalItemFreteTributacao = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalPedidoFreteTributacao = new EditNumberFrac(editNumberInicial, 9);
		edConsisteConversaoUnidade = new EditText(editNumberInicial, 10);
		edVlTotalItemPorPeso = new EditNumberFrac(editNumberInicial, 9);
		edPctDescontoDoPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlPctDescCondPagto = new EditNumberFrac(editNumberInicial, 9);
		edDtPagamento = new EditDate();
		edVlBaseAntecipacao = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalItemTribFreteSeguro = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalItemUnitarioTribFreteSeguro = new EditNumberFrac(editNumberInicial, 9);
		edVlTotalSeguroItemPedido = new EditNumberFrac(editNumberInicial, 9);
		edVlPesoItemUnitario = new EditNumberFrac(editNumberInicial, 9);
		edVlPesoTotalItem = new EditNumberFrac(editNumberInicial, 9);
		edVlPontuacaoRealizadoItem = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisPontuacao);
		edVlPontuacaoRealizadoItem.setID("edVlPontuacaoRealizadoItem");
		edVlPontuacaoBaseItem = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisPontuacao);
		edVlPontuacaoBaseItem.setID("edVlPontuacaoBaseItem");
		edVlPctMargemRentabItem = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisPontuacao);
		edVlPctMargemRentabPedido = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.nuCasasDecimaisPontuacao);
		edQtItemGondola = new EditNumberInt(editNumberInicial, 9);
		edQtItemGondola.setID("edQtItemGondola");
		edVlPctDescProgressivo = new EditNumberFrac(editNumberInicial, 9);
		edVlPctPoliticaComercial = new EditNumberFrac(editNumberInicial, 9, ValueUtil.doublePrecisionInterface);
		edVlPctFaixaDescQtdPeso = new EditNumberFrac(editNumberInicial, 9, ValueUtil.doublePrecisionInterface);
		edDsMoedaProduto = new EditText("", 9);
		edVlCotacaoMoeda = new EditNumberFrac(editNumberInicial, 9, ValueUtil.doublePrecisionInterface);
		edVlPctDescQuantidade = new EditNumberFrac(editNumberInicial, 9, ValueUtil.doublePrecisionInterface);
		edVlPctDescQuantidade.setID("edVlPctDescQuantidade");
		edQtEstoquePrevistoGeral = new EditNumberFrac(editNumberInicial, 9, LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		edDtEstoquePrevistoGeral = new EditDate();
		edQtSomaEstoquePrevistoGeral = new EditNumberFrac(editNumberInicial, 9,LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro() ? 0 : ValueUtil.doublePrecisionInterface);
		edQtSugerida = new EditNumberFrac(editNumberInicial, 9, 0);
		edQtSugerida.setID("edQtSugerida");
		lvStatusItemPedido = new LabelValue("");
		lvVlUnitGiroProduto = new LabelValue("");
		edNuOrdemCompraCliente = new EditText(editNumberInicial, 20);
		edNuSeqOrdemCompraCliente = new EditNumberTextInteger(9);
		populateHashsTemporarias();
		constructorListContainer();
		edBaseNumpad = new EditText("", 0);
		if (LavenderePdaConfig.isPermiteOcultarValoresItemAgrupadorGrade() || LavenderePdaConfig.isPermiteOcultarValoresItemMultiplaInsercao() || LavenderePdaConfig.isPermiteOcultarCamposItemPedidoGrade()) {
			boolean ultimaSelecaoOculta = isUltimaOpcaoSelecionadaOculta();
			hideAttributes = ultimaSelecaoOculta && (LavenderePdaConfig.isPermiteOcultarValoresItemAgrupadorGrade() || LavenderePdaConfig.isPermiteOcultarValoresItemMultiplaInsercao());
			hideAttributesOld = hideAttributes;
			hideAttributesVendaUnitaria = ultimaSelecaoOculta && LavenderePdaConfig.isPermiteOcultarCamposItemPedidoGrade(); 
		}
		criaGridUnidadeAlternativa();
		if (LavenderePdaConfig.isUsaTeclaEnterComoConfirmacaoItemPedido()) {
			removeFocusOnEnter = false;
		}
		usaLazyLoading = getMaxResult() > 0;
	}

	private boolean isUltimaOpcaoSelecionadaOculta() throws SQLException {
		String vlConfig = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.ULTIMACONFIGURACAOBOTAOOCULTAR);
		return ValueUtil.valueEquals(vlConfig, ValueUtil.VALOR_SIM);
	}

	public void loadGrupoProdutos(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaFiltroGrupoProduto > 0) {
			StringBuffer sb = new StringBuffer();
			lbGrupoProduto = new LabelName();
			if (!LavenderePdaConfig.ocultaGrupoProduto1) {
				sb.append(Messages.GRUPOPRODUTO1_CDGRUPOPRODUTO1);
				cbGrupoProduto1.popupTitle = lbGrupoProduto.getValue();
				loadGrupoProduto1(pedido);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 1) {
				sb.append(" / ").append(Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2);
				cbGrupoProduto2.popupTitle = Messages.GRUPOPRODUTO2_CDGRUPOPRODUTO2;
				loadGrupoProduto2(pedido);
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 2) {
				sb.append(" / ").append(Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3);
				cbGrupoProduto3.popupTitle = Messages.GRUPOPRODUTO3_CDGRUPOPRODUTO3;
				loadGrupoProduto3();
			}
			if (LavenderePdaConfig.usaFiltroGrupoProduto > 3) {
				sb.append(" / ").append(Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4);
				cbGrupoProduto4.popupTitle = Messages.GRUPOPRODUTO4_CDGRUPOPRODUTO4;
				loadGrupoProduto4();

			}
			String txtGruposProduto = sb.toString();
			loadLabelGruposProdutos(sb);
			if (sb.length() == 0) sb.append(txtGruposProduto);
			if (sb.indexOf(" / ") == 0) sb.delete(0, 2);
			lbGrupoProduto.setText(sb.toString());
		}
	}

	protected void loadGrupoProduto4() throws SQLException {
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), cbGrupoProduto3.getValue());
	}

	protected void loadGrupoProduto3() throws SQLException {
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), null);
	}

	protected void loadGrupoProduto2(Pedido pedido) throws SQLException {
		cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
	}

	protected void loadGrupoProduto1(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
			Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
			cbGrupoProduto1.loadGrupoProduto1(pedido, fornecedorSelecionado);
		} else {
				cbGrupoProduto1.loadGrupoProduto1(pedido);
		}
	}

	public void show() throws SQLException {
		if (!listInicialized && instance != null) {
			instance.flListInicialized = false;
		}
		show(this);
	}

	public static void invalidateInstance() {
		instance = null;
	}

	protected abstract void tabelaPrecoChange() throws SQLException;

	protected abstract void addIconsProduto() throws SQLException;

	protected abstract Vector getProdutoList() throws SQLException;

	protected abstract void addComponentsInScreen() throws SQLException;

	protected abstract void removeComponentsInScreen() throws SQLException;

	protected abstract void btListaItensClick() throws SQLException;

	protected abstract void btFiltrarClick() throws SQLException;

	protected abstract boolean filtrarClick() throws SQLException;

	protected abstract void changeItemTabelaPreco() throws SQLException;

	protected abstract void changeItemTabelaPreco(String cdTabelaPreco, boolean refreshDomainToScreen) throws SQLException;

	protected abstract void changeItemTabelaPreco(String cdTabelaPreco, boolean refreshDomainToScreen, ItemTabelaPreco itemTabelaPreco) throws SQLException;

	protected abstract void addItensOnButtonMenu() throws SQLException;

	protected abstract void atualizaItemPedidoNaLista(ItemPedido itemPedido) throws SQLException;

	protected abstract String[] getDefaultEditLabels();

	protected abstract boolean isComboTabPrecoVisible() throws SQLException;

	protected abstract void cbTabelaPrecoClick() throws SQLException;

	protected abstract boolean gridClick() throws SQLException;

	protected abstract void setProdutoSelecionado(Produto produto) throws SQLException;

	protected abstract void inicializaItemParaVenda(ItemPedido itemPedido) throws SQLException;

	protected abstract void addInfosExtras() throws SQLException;

	protected abstract void gridClickAndRepaintScreen() throws SQLException;

	protected Vector getDefaultHiddenEditLabels() {
		if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isPermiteOcultarCamposItemPedidoGrade() && isModoGrade()) {
			return new Vector(LavenderePdaConfig.getCamposTelaItemPedidoGradeOcultaveis());
		}
		return new Vector(0);
	}
	
	public void constructorListContainer() throws SQLException {
		boolean permiteInserirQtdDescMultipla = isInsereMultiplosSemNegociacao();
		boolean permiteInserirMultiplosItensPorVezNoPedido = pedido.isPermiteInserirMultiplosItensPorVezNoPedido();
		int itemCount = getItemCount(permiteInserirQtdDescMultipla, permiteInserirMultiplosItensPorVezNoPedido);
		int nuItemsPerLine = 2;
		listContainer = new GridListContainer(permiteInserirQtdDescMultipla && !LavenderePdaConfig.usaGradeProduto5() ? 2 : itemCount, nuItemsPerLine, permiteInserirMultiplosItensPorVezNoPedido && LavenderePdaConfig.naoPermiteInserirQtdDescMultipla(), LavenderePdaConfig.usaScroolLateralListasProdutos);
		listContainer.useBtShowHideDiscont = (LavenderePdaConfig.isPermiteOcultarDescontoAcrescimoMultiplaInsercao() || LavenderePdaConfig.isPermiteOcultarValoresItemAgrupadorGrade() || LavenderePdaConfig.isPermiteOcultarValoresItemMultiplaInsercao()) && !pedido.isPedidoBonificacao();
		listContainer.hideDiscontField = listContainer.useBtShowHideDiscont && isUltimaOpcaoSelecionadaOculta();
		listContainer.setTotalizerQtdeVisible(false);
		listContainer.addSingleLineBarBottomContainer(sessaoTotalizadores);
		if (!LavenderePdaConfig.usaPriorizacaoPesquisaItemPedido) {
			montaOpcoesOrdenacao();
		}
		if (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isUsaListaProdutoAgrupadorGrade()) {
			createButtonAgrupadorGrade();
		}
		setColumnPositions(permiteInserirQtdDescMultipla && !LavenderePdaConfig.usaGradeProduto5(), itemCount);
		listContainer.resizeable = true;
		setFonteQuebraDeLinha();
		if (ValueUtil.valueEquals(Messages.GIROPRODUTO_ITEM_PEDIDO_NOME_ENTIDADE, title)) {
			this.onEvent(new ResizeListEvent(listContainer.btResize));
			listContainer.btResize.setVisible(false);
		}
	}

	private void createButtonAgrupadorGrade() throws SQLException {
		btAgrupadorGrade = new Button(ValueUtil.VALOR_NI);
		btAgrupadorGrade.setBorder(BORDER_NONE);
		setAgrupadorGradeButtonState(isFiltrandoAgrupadorGrade(), false);
		listContainer.setBarTopControls(new Control[]{btAgrupadorGrade});
	}

	private Image getAgrupadorGradeButtonIcon(boolean state) {
		String iconName = state ? "lista" : "grade";
		return UiUtil.getColorfulImage("images/" + iconName + ".png", UiUtil.getControlPreferredHeight() / 2, UiUtil.getControlPreferredHeight() / 2);
	}

	private void montaOpcoesOrdenacao() throws SQLException {
		int size = 0;
		size = !LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto() ? size + 1 : size;
		size = !LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto() ? size + 1 : size;
		size = LavenderePdaConfig.isMostraDescricaoReferencia() ? size + 1 : size;
		size = LavenderePdaConfig.isHabilitaOrdenacaoPrecoPadrao() ? size + 1 : size;
		size = LavenderePdaConfig.isMostraOrdenacaoRelevanciaProduto() ? size + 1 : size;
		size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoEmpresa() ? size + 1 : size;
		size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRepresentante() ? size + 1 : size;
		size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoSupervisao() ? size + 1 : size;
		size = LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRegional() ? size + 1 : size;
		size = LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao() ? size + 1 : size;
		size = LavenderePdaConfig.isMostraOrdenacaoEstoqueProduto() ? size + 1 : size;
		int position = 0;
		String[][] matriz = new String[size][2];
		if (!LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_CODIGO;
			matriz[position++][1] = "CDPRODUTO";
		}
		if (!LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSPRODUTO;
			matriz[position++][1] = "DSPRODUTO";
		}
		if (LavenderePdaConfig.isMostraDescricaoReferencia()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_DSREFERENCIA;
			matriz[position][1] = "DSREFERENCIA";
			position++;
		}
		if (LavenderePdaConfig.isHabilitaOrdenacaoPrecoPadrao()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_PRECO_PADRAO;
			matriz[position][1] = ProdutoBase.SORT_COLUMN_VLPRECOPADRAO;
			position++;
		}
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoEmpresa()) {
			matriz[position][0] = Messages.TIPORANKING_EMPRESA;
			matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGEMP;
			position++;
		}
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRepresentante()) {
			matriz[position][0] = Messages.TIPORANKING_REPRESENTANTE;
			matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGREP;
			position++;
		}
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoSupervisao()) {
			matriz[position][0] = Messages.TIPORANKING_SUPERVISAO;
			matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGSUP;
			position++;
		}
		if (LavenderePdaConfig.isUsaOrdenacaoRankingProdutoRegional()) {
			matriz[position][0] = Messages.TIPORANKING_REGIAO;
			matriz[position][1] = ProdutoBase.NMCOLUMN_NURANKINGREG;
			position++;
		}
		if (LavenderePdaConfig.usaFiltroComissao && pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			matriz[position][0] = Messages.PRODUTO_LABEL_COMISSAO;
			matriz[position][1] = ProdutoBase.SORT_COLUMN_VLPCTCOMISSAO;
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
		}
		if (LavenderePdaConfig.isPossuiOrdenacaoInsercaoProdutoNoPedido()) {
			listContainer.setColsSort(matriz);
			setDefaultSortAttribute(matriz);
		} else {
			listContainer.atributteSortSelected = "DSPRODUTO";
			listContainer.sortAsc = ValueUtil.VALOR_SIM;
		}
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
			if (!LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos && !LavenderePdaConfig.isOcultaOrdenacaoDescricaoProduto()) {
				sortAtributteDefault = "DSPRODUTO";
			} else if (LavenderePdaConfig.usaOrdenacaoPorCodigoListaProdutos && !LavenderePdaConfig.isOcultaOrdenacaoCodigoProduto()) {
				sortAtributteDefault = "CDPRODUTO";
			} else {
				sortAtributteDefault = matriz[0][1];
			}
		}
		listContainer.atributteSortSelected = sortAtributteDefault;
		listContainer.sortAsc = sortAscDefault;
	}

	private void setColumnPositions(boolean permiteInserirQtdDescMultipla, int itemCount) {
		if (itemCount >= 4 && !permiteInserirQtdDescMultipla) {
			listContainer.setColPosition(3, RIGHT);
		}
		if (itemCount >= 6 && !permiteInserirQtdDescMultipla) {
			listContainer.setColPosition(5, RIGHT);
		}
		if (itemCount >= 8 && !permiteInserirQtdDescMultipla) {
			listContainer.setColPosition(7, RIGHT);
		}
		if (itemCount >= 10 && !permiteInserirQtdDescMultipla) {
			listContainer.setColPosition(9, RIGHT);
		}
	}

	private void setFonteQuebraDeLinha() {
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			BaseLayoutListContainer layout = (BaseLayoutListContainer) listContainer.getLayout();
			if (layout.relativeFontSizes.length > 2) {
				layout.relativeFontSizes[2] = layout.relativeFontSizes[0];
			}
			if (layout.defaultItemColors.length > 2) {
				layout.defaultItemColors[2] = layout.defaultItemColors[0];
			}
			layout.lineGap = 15;
			layout.lineBreak = true;
		}
	}

	private int getItemCount(boolean permiteInserirQtdDescMultipla, boolean permiteInserirMultiplosItensPorVezNoPedido) throws SQLException {
		itemCount = LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto() ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido() ? itemCount + 2 : itemCount;
		itemCount = LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido() ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.mostraColunaMarcaNaListaProdutoDentroPedido() ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo() && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() && !pedido.isSugestaoPedidoGiroProduto() && cadPedidoForm != null && !cadPedidoForm.solicitadoAcessoGiroProduto ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.mostraPrecoUnidadeItem && !pedido.isSugestaoPedidoGiroProduto() && cadPedidoForm != null && !cadPedidoForm.solicitadoAcessoGiroProduto ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.mostraPrevisaoDescontoGridProdutos && (LavenderePdaConfig.isAplicaDescontoFimDoPedido() || LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido()) ? itemCount + 1: itemCount;
		itemCount = permiteInserirMultiplosItensPorVezNoPedido && LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens() && !permiteInserirQtdDescMultipla ? itemCount + 2 : itemCount;
		itemCount = permiteInserirMultiplosItensPorVezNoPedido && LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens() && !permiteInserirQtdDescMultipla && LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? itemCount + 2 : itemCount;
		itemCount = LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosDentroPedido() ? itemCount + 1 : itemCount;
		itemCount = (LavenderePdaConfig.exibeHistoricoEstoqueCliente || LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) && isInsereMultiplosSemNegociacao() ? itemCount + 4: itemCount;
		itemCount = LavenderePdaConfig.apresentaEstoqueDaRemessaEmpresaNaListaProdutos ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.usaFiltroComissao ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista ? itemCount + 2 : itemCount;
		itemCount = isInsereMultiplosSemNegociacao() ? itemCount + getItemCountInformacoesComplementaresInsercao() : itemCount;
		itemCount = isInsereMultiplosSemNegociacao() ? itemCount + 1 : itemCount;
		itemCount = LavenderePdaConfig.isMostraPrecoComSTNaListaDeItensPedido() ? itemCount + 1 : itemCount;
		if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosDentroPedido() && !isInsereMultiplosSemNegociacao()) {
			while (itemCount < 9) {
				itemCount++;
			}
		}
		return itemCount;
	}

	@Override
	protected BaseDomain screenToDomain() throws SQLException {
		ItemPedido itemPedido = (ItemPedido) getDomain();
		if (LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade())) {
			return itemPedido;
		}
		return screenToDomain(itemPedido);
	}

	private BaseDomain screenToDomain(ItemPedido itemPedido) throws SQLException {
		itemPedido.setQtItemFisico(isAddingFromArray || fromRelNotificaoItemWindow ? itemPedido.getQtItemFisico() : edQtItemFisico.getValueDouble());
		itemPedido.qtItemFaturamento = edQtItemFaturamento.getValueDouble();
		itemPedido.vlItemPedido = edVlItemPedido.getValueDouble();
		if (!LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && !LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlItemPedido()) {
			itemPedido.vlPctDesconto = isAddingFromArray ? itemPedido.vlPctDesconto : edVlPctDesconto.getValueDouble();
		}
		itemPedido.vlPctAcrescimo = edVlPctAcrescimo.getValueDouble();
		if (((LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco && (!LavenderePdaConfig.usaFreteApenasTipoFob || pedido.isTipoFreteFob()))
				|| LavenderePdaConfig.isPermiteInserirFreteManualItemPedido())
				&& !LavenderePdaConfig.usaFreteManualPedido) {
			itemPedido.vlItemPedidoFrete = edVlFrete.getValueDouble();
			if(LavenderePdaConfig.getTipoCalculoFreteUnitario().equals("N")) {
				itemPedido.vlTotalItemPedidoFrete = itemPedido.vlItemPedidoFrete;
			}
			else if(LavenderePdaConfig.getTipoCalculoFreteUnitario().equals("1")) {
				itemPedido.vlTotalItemPedidoFrete = itemPedido.vlItemPedidoFrete * itemPedido.qtItemFisico;
			}
		}
		if (LavenderePdaConfig.informaVerbaManual && itemPedido.permiteAplicarDesconto()) {
			itemPedido.vlVerbaManual = edVlVerbaManual.getValueDouble();
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			itemPedido.qtPontosItem = edQtPtItens.getValueInt();
		}
		if (isPermiteAlterarVlItemNaUnidadeElementar() || itemPedido.isEditandoValorElementar()) {
			itemPedido.vlEmbalagemElementar = edPrecoEmbPrimaria.getValueDouble();
		}
		if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			itemPedido.vlItemIpi = edVlItemIpi.getValueDouble();
		}
		if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar()) {
			itemPedido.vlItemPedidoUnElementar = itemPedido.vlEmbalagemElementar;
			itemPedido.qtItemPedidoUnElementar = edCaixaPadrao.getValueDouble() * edQtItemFisico.getValueDouble();
		}
		if (LavenderePdaConfig.pctMargemAgregada > 0) {
			itemPedido.vlAgregadoSugerido = edVlAgregadoSugerido.getValueDouble();
			itemPedido.pctMargemAgregada = edPctMargemAgregada.getValueDouble();
		}
		if (isPermiteUsarOportunidade()) {
			itemPedido.flOportunidade = bgOportunidade.getValue();
		}
		if (LavenderePdaConfig.usaValorRetornoProduto) {
			itemPedido.vlRetornoProduto = edVlRetornoProduto.getValueDouble();
		}
		if (LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			itemPedido.getItemPedidoAud().flGeraVerba = bgGeraVerba.getValue();
		}
		if (LavenderePdaConfig.permiteTabPrecoItemDiferentePedido()) {
			itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
		}
		if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
			itemPedido.cdCondicaoComercial = cbCondicaoComercial.getValue();
		}
		if (pedido.isGondola()) {
			if (ValueUtil.isEmpty(edQtItemGondola.getText())) {
				itemPedido.qtItemGondola = -1;
			} else {
				itemPedido.qtItemGondola = edQtItemGondola.getValueInt();
			}
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			itemPedido.vlCotacaoMoedaProduto = itemPedido.getItemTabelaPreco().vlCotacaoMoeda;
			itemPedido.dsMoedaVendaProduto = edDsMoedaProduto.getValue();
		}
		isAddingFromListDescQtd = false;
		return itemPedido;
	}

	private Image getEmptyIcon() {
		return UiUtil.getEmptyImage(UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize());
	}

	public Image getIcon(String nmIcon, boolean enabled) {
		if (enabled) {
			return UiUtil.getIconButtonAction("images/" + nmIcon);
		} else {
			return UiUtil.getIconButtonAction("images/" + nmIcon, Color.BRIGHT);
		}
	}

	protected void carregaGridUnidadesProduto(ItemPedido itemPedido, Vector produtoUnidadeList) throws SQLException {
		int listSize = 0;
		Vector domainList = new Vector(0);
		String[][] gridItems;
		ItemPedido itemExample = new ItemPedido();
		itemExample.pedido = itemPedido.pedido;
		itemExample.cdEmpresa = itemPedido.cdEmpresa;
		itemExample.cdRepresentante = itemPedido.cdRepresentante;
		itemExample.cdProduto = itemPedido.cdProduto;
		itemExample.cdTabelaPreco = itemPedido.cdTabelaPreco;
		itemExample.flOportunidade = itemPedido.flOportunidade;
		itemExample.cdUnidade = itemPedido.cdUnidade;
		itemExample.cdUfClientePedido = itemPedido.cdUfClientePedido;
		itemExample.cdItemGrade1 = itemPedido.cdItemGrade1;
		itemExample.cdItemGrade2 = itemPedido.cdItemGrade2;
		itemExample.cdItemGrade3 = itemPedido.cdItemGrade3;
		itemExample.setQtItemFisico(itemPedido.getQtItemFisico());
		itemExample.cdPrazoPagtoPreco = itemPedido.cdPrazoPagtoPreco;
		itemExample.setItemTabelaPreco(itemPedido.getItemTabelaPreco());
		// --
		try {
			gridUnidadeAlternativa.setItems(null);
			domainList = produtoUnidadeList;
			listSize = domainList.size();
			if (listSize > 0) {
				String[] item = getItemProdutoUnidade(itemExample, (ProdutoUnidade) domainList.items[0]);
				gridItems = new String[listSize][item.length];
				gridItems[0] = item;
				for (int i = 1; i < listSize; i++) {
					gridItems[i] = getItemProdutoUnidade(itemExample, (ProdutoUnidade) domainList.items[i]);
				}
				item = null;
				// --
				gridUnidadeAlternativa.setItems(gridItems);
				if (!LavenderePdaConfig.isUsaSelecaoUnidadePorGrid()) {
					gridUnidadeAlternativa.qsort(COL_POSITION_NUCONVERSAOUNIDADE);
					ordenaCbUnidadeAlternativaConformeGrid();
				}
			}
		} finally {
			gridItems = null;
			domainList = null;
		}
	}

	protected String[] getItemProdutoUnidade(ItemPedido itemPedidoBase, ProdutoUnidade produtoUnidade) throws SQLException {
		itemPedidoBase.cdUnidade = produtoUnidade.cdUnidade;
		PedidoService.getInstance().resetDadosItemPedido(itemPedidoBase.pedido, itemPedidoBase);
		double qtdElementar = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedidoBase.getItemTabelaPreco(), produtoUnidade);
		if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid() && LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 0) {
			double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedidoBase.getItemTabelaPreco(), produtoUnidade);
			String[] item = {
					produtoUnidade.cdUnidade,
					produtoUnidade.toString(),
					StringUtil.getStringValueToInterface(qtdElementar),
					StringUtil.getStringValueToInterface(itemPedidoBase.vlEmbalagemElementar),
					StringUtil.getStringValueToInterface(getVlConversaoUnidade(produtoUnidade, itemPedidoBase.getItemTabelaPreco().vlUnitario, nuConversaoUnidade)),
					StringUtil.getStringValueToInterface(itemPedidoBase.vlItemPedido), "", };
			return item;
		} else {
			double vlEmbalagemElementar = itemPedidoBase.vlEmbalagemElementar;
			if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem && getItemPedido().pedido.getCliente().isOptanteSimples()) {
				vlEmbalagemElementar = ValueUtil.round(vlEmbalagemElementar - (itemPedidoBase.getItemTabelaPreco().vlReducaoOptanteSimples / qtdElementar));
			}
			// --
			String[] item = {
					produtoUnidade.cdUnidade,
					produtoUnidade.toString(),
					StringUtil.getStringValueToInterface(qtdElementar),
					StringUtil.getStringValueToInterface(vlEmbalagemElementar),
					StringUtil.getStringValueToInterface(itemPedidoBase.vlItemPedido),
					StringUtil.getStringValueToInterface(produtoUnidade.vlIndiceFinanceiro) };
			return item;
		}
	}

	private double getVlConversaoUnidade(ProdutoUnidade produtoUnidade, double vlUnidadePadrao, double nuConversaoUnidade) {
		if (produtoUnidade.isMultiplica()) {
			return vlUnidadePadrao * nuConversaoUnidade;
		}
		return vlUnidadePadrao / nuConversaoUnidade;
	}

	protected void selectUnidadeNaGrid(String cdUnidade) {
		if (ValueUtil.isEmpty(cdUnidade)) {
			return;
		}
		for (int i = 0; i < gridUnidadeAlternativa.size(); i++) {
			if (cdUnidade.equals(gridUnidadeAlternativa.getItem(i)[0])) {
				gridUnidadeAlternativa.setSelectedIndex(i);
				return;
			}
		}
		if (gridUnidadeAlternativa.size() > 0) {
			gridUnidadeAlternativa.setSelectedIndex(0);
		}
	}

	// @Override
	protected void domainToScreen(final BaseDomain domain) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) domain;
		// --
		if (!itemPedido.usaCestaPromo) {
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				cbUnidadeAlternativa.load(itemPedido);
				cbUnidadeAlternativa.setValue(itemPedido.cdUnidade);
				if ((LavenderePdaConfig.isUsaSelecaoUnidadePorGrid() && LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 0) || LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 2) {
					if (cbUnidadeAlternativa.size() > 0) {
						carregaGridUnidadesProduto(itemPedido, new Vector(cbUnidadeAlternativa.getItems()));
					}
					selectUnidadeNaGrid(itemPedido.cdUnidade);
				}
				// --
				ProdutoUnidade produtoUnidade = cbUnidadeAlternativa.getProdutoUnidadeSelected();
				itemPedido.nuConversaoUnidade = produtoUnidade.nuConversaoUnidade;
				if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && isEditing()) {
					getItemPedidoService().calculaValorEmbalagemElementarComDescAcrescimo(itemPedido);
					getItemPedidoService().aplicaQtdElementarItemPedido(itemPedido, produtoUnidade);
				} else {
					getItemPedidoService().aplicaQtdadeElementarValorElementarNoItemPedido(itemPedido, itemPedido.vlUnidadePadrao, produtoUnidade);
				}
				if (LavenderePdaConfig.usaRestricaoVendaProdutoPorUnidade && LavenderePdaConfig.usaUnidadeAlternativa && !pedido.isFlOrigemPedidoErp()) {
					if (RestricaoVendaUnService.getInstance().isUnidadeRestrita(itemPedido, produtoUnidade.cdUnidade, pedido.cdTipoPedido)) {
						UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_UNIDADE_BLOQUEADA, StringUtil.getStringValue(produtoUnidade.toString())));
					}
				}
			}
			if (LavenderePdaConfig.isUsaGradeProduto1A4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade())) {
				loadDadosGradeProduto(itemPedido);
			}
			// --
			carregaInfosExtraDomainToScreen(itemPedido);
			// --
			if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
				cbCondicaoComercial.carregaCondicoesComerciais(pedido, true);
				cbCondicaoComercial.setValue(itemPedido.cdCondicaoComercial);
			}
			if (LavenderePdaConfig.usaValorRetornoProduto) {
				edVlRetornoProduto.setValue(itemPedido.vlRetornoProduto);
			}
			// --
			if (inVendaUnitariaMode && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
				reloadComboTabelaPreco(pedido, ProdutoTabPrecoService.getInstance().getProdutoTabPrecoColumn(itemPedido.cdProduto, "DSTABPRECOLIST"));
			} else {
				reloadComboTabelaPreco(pedido);
			}
			if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
				edVlPctDescCliente.setValue(itemPedido.vlPctDescCliente);
				edVlPctDescontoCondicao.setValue(itemPedido.vlPctDescontoCondicao);
				edVlPctDescFrete.setValue(itemPedido.vlPctDescFrete);
			} else if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
				edVlPctDescCliente.setValue(pedido.vlPctDescCliente);
				edVlPctDescontoCondicao.setValue(pedido.vlPctDesc2);
				edVlPctDescFrete.setValue(pedido.vlPctDesc3);
			}
			// Tratamento com tabela de preco , pois quando o ERP nao mandava tab dava NULL.
			cbTabelaPreco.setValue(itemPedido.cdTabelaPreco);
			if (ValueUtil.isEmpty(cbTabelaPreco.getValue())) {
				itemPedido.cdTabelaPreco = "";
			}
			// --
			String dsProduto; 
			if (LavenderePdaConfig.usaGradeProduto5() && itemPedido.getProduto().isProdutoAgrupadorGrade() && isFiltrandoAgrupadorGrade() && !fromListItemPedidoForm) {
				dsProduto = itemPedido.getProduto().getDsAgrupadorGrade();
			} else {
				dsProduto = itemPedido.getDsProdutoWithKey(itemPedido);
			}
			lbDsProduto.setValue(dsProduto);
		}
		if (LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo()) {
			ItemPedidoService.getInstance().calculateComissaoItem(itemPedido, pedido);
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			edQtPtPedido.setValue(pedido.qtPontosPedido);
		}
		// --
		loadLbItemByItem();
		// --
		if(LavenderePdaConfig.isAplicaTaxaAntecipacaoMensalNoItem()) {
			itemPedido.dtPagamento = itemPedido.pedido.dtPagamento;
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			boolean itemPedidoNaoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoNaoAutorizado(itemPedido, null);
			if (itemPedidoNaoAutorizado) {
				itemPedido.pedido = pedido;
				itemPedido.ignoraValidacaoDesconto = false;
				itemPedido.setQtItemFisico(1);
				itemPedido.vlPctAcrescimo = 0;
				itemPedido.vlPctDesconto = 0;
				itemPedido.vlDesconto = 0;
				getItemPedidoService().desfazPrecoLiberadoSenha(itemPedido);
				PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
				getItemPedidoService().calculateSimples(itemPedido, pedido);
			}
		}
		refreshDomainToScreen(itemPedido);
		refreshProdutoToScreen(itemPedido);
		if (getItemPedido().pedido.isPedidoAberto()) {
			if (!LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli  && !LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
				verificaDescontoPedidoChange(getItemPedido());
			}
			verificaAcrescimoPedidoChange(itemPedido);
		}
	}

	protected void loadLbItemByItem() throws SQLException {
		if (inCarrouselMode) {
			lbItemByItem.setValue(selectedItemPedidoCarrousel + 1 + "/" + itemPedidoCarrouselList.size());
		} else if (getBaseCrudListForm() != null && getBaseCrudListForm().getListContainer() != null) {
			lbItemByItem.setValue(getBaseCrudListForm().getListContainer().getSelectedIndex() + 1 + "/" + getBaseCrudListForm().getListContainer().size());
		}
	}

	protected void carregaInfosExtraDomainToScreen(ItemPedido itemPedido) throws SQLException {
		if (isShowFotoProduto()) {
			getFotoByItemPedido(itemPedido);
		}
	}

	protected boolean isShowFotoProduto() {
		return LavenderePdaConfig.isExibeFotoTelaItemPedido() && LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto != 2 && !LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido;
	}

	protected void getFotoByItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto5() && isModoGrade() && !LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			imageCarrousel.setImgList(FotoProdutoService.getInstance().geraListaFotoProdutoAgrupadorGradeToCarrousel(itemPedido.getProduto(), itemPedido.cdItemGrade1));
		} else if (ValueUtil.isNotEmpty(itemPedido.getProduto().cdProduto)) {
			imageCarrousel.setImgList(FotoProdutoService.getInstance().geraListaFotoProdutoToCarroussel(itemPedido.getProduto()));
		} else {
			Vector imgList = new Vector();
			imgList.addElement(new String[]{"images/nophoto", itemPedido.cdProduto, ".jpg"});
			imageCarrousel.setImgList(imgList);
		}
	}

	public void refreshDomainToScreen(ItemPedido itemPedido) throws SQLException {
		updateQtItens(itemPedido);
		Produto produto = itemPedido.getProduto(true);
		edVlItemPedido.setValue(itemPedido.vlItemPedido);
		edVlBaseItemPedido.setValue(itemPedido.vlBaseItemPedido);
		if (fromInfoExtra) {
			lvTotalPedido.lbValue.setValue(itemPedido.vlTotalItemPedido);
		} else {
			lvTotalPedido.setValue(itemPedido.vlTotalItemPedido);
		}
		edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
		if (itemPedido.qtItemFisico == 0) {
			itemPedido.vlPctFaixaDescQtdPeso = 0;
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
			edVlDesconto.setValue(itemPedido.vlDesconto);
			edVlPctDesconto2.setValue(itemPedido.vlPctDesconto2);
			edVlDesconto2.setValue(itemPedido.vlDesconto2);
			edVlPctDesconto3.setValue(itemPedido.vlPctDesconto3);
			edVlDesconto3.setValue(itemPedido.vlDesconto3);
		}
		if (LavenderePdaConfig.usaDescontoExtra) {
			edVlPctDesconto2.setValue(itemPedido.vlPctDesconto2);
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			edVlPctDesconto3.setValue(itemPedido.vlPctDesconto3);
		}
		if (LavenderePdaConfig.isAplicaDescontosSequenciaisNoItemPedido()) {
			edVlPctDesconto2.setValue(itemPedido.vlPctDescontoPromo);
		}
		edVlPctAcrescimo.setValue(itemPedido.vlPctAcrescimo);
		double vlTotalPedido =  itemPedido.pedido.vlTotalPedido;
		edVlTotalPedido.setValue(vlTotalPedido > 0 ? vlTotalPedido : 0);
		if (LavenderePdaConfig.isAplicaDeflatorCondPagtoValorTotalPedidoAutomatico()) {
			edVlTtItemComDeflator.setValue(itemPedido.vlItemPedido - ((itemPedido.vlItemPedido * pedido.vlPctDesconto) / 100));
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			cbUnidadeAlternativa.load(itemPedido);
			cbUnidadeAlternativa.setValue(itemPedido.cdUnidade);
			ProdutoUnidade produtoUnidade = cbUnidadeAlternativa.getProdutoUnidadeSelected();
			itemPedido.nuConversaoUnidade = produtoUnidade.nuConversaoUnidade;
			if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar()) {
				if (!itemPedido.isEditandoValorElementar()) {
					getItemPedidoService().calculaValorEmbalagemElementarComDescAcrescimo(itemPedido);
				}
				getItemPedidoService().aplicaQtdElementarItemPedido(itemPedido, produtoUnidade);
			}
			edCaixaPadrao.setValue(itemPedido.qtEmbalagemElementar);
		} else if (LavenderePdaConfig.mostraQtdPorEmbalagemProduto) {
			if (LavenderePdaConfig.usaFatorCUMEspecialClienteCreditoAntecipado && pedido.getCliente().isCreditoAntecipado()) {
				edCaixaPadrao.setValue(produto.nuConversaoUMCreditoAntecipado);
			} else {
				int nuFracao = produto.nuFracao <= 0 || !LavenderePdaConfig.apresentaUnidadeFracionadaDoProduto ? 1 : produto.nuFracao;
				edCaixaPadrao.setValue(produto.nuConversaoUnidadesMedida * nuFracao);
			}
		}
		if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorProduto()) {
			edPrecoMaximoCons.setValue(produto.vlPrecoMaximoConsumidor);
		} else if (LavenderePdaConfig.mostraVlPrecoMaximoConsumidorItemTabPreco()) {
			edPrecoMaximoCons.setValue(itemPedido.getItemTabelaPreco().vlPrecoMaximoConsumidor);
		}
		if (LavenderePdaConfig.mostraVlPrecoFabrica) {
			edPrecoFabrica.setValue(produto.vlPrecoFabrica);
		}
		edEmbalagem.setValue(produto.dsEmbalagem);
		if (LavenderePdaConfig.mostraEmbalagemCompraProduto) {
			edQtEmabalagemCompra.setValue(produto.qtEmbalagemCompra);
		}
		if (LavenderePdaConfig.calculaFecopItemPedido || LavenderePdaConfig.isUsaCalculoIpiItemPedido() || LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			edVlEmbalagemElementTributos.setValue(ItemPedidoService.getInstance().getVlItemPedidoUnidadeElementarComTributos(itemPedido));
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples) {
			edVlReducaoSimples.setValue(itemPedido.vlReducaoOptanteSimples);
			if (LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) {
				edVlEmbalagemComReducaoSimples.setValue(itemPedido.vlEmbalagemElementar - (itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples / itemPedido.qtEmbalagemElementar));
			}
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa && (LavenderePdaConfig.isUsaCalculaStItemPedido() || LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado)) {
			edVlEmbalagemPrimariaST.setValue(itemPedido.getVlItemComTributos() / (itemPedido.qtEmbalagemElementar == 0 ? 1 : itemPedido.qtEmbalagemElementar));
		}
		if (LavenderePdaConfig.usaFreteNoPedidoPorItemBaseadoNoItemTabPreco
				&& (!LavenderePdaConfig.usaFreteApenasTipoFob || pedido.isTipoFreteFob())
				|| LavenderePdaConfig.usaPctFreteTipoFreteNoPedido
				|| LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso()
				|| LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			edVlFrete.setValue(itemPedido.vlItemPedidoFrete);
		}
		if (pedido.isStatusPedidoNaoOcultaValoresComissao() && (LavenderePdaConfig.usaConfigCalculoComissao() || LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.isCalculaComissaoTabPrecoEGrupo())) {
			edVlItemComissao.setValue(itemPedido.vlTotalComissao);
			edVlTotalComissao.setValue(pedido.vlComissaoPedido);
			if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItemTipo1()) {
				edVlPctComissaoPedido.setValue(pedido.vlPctComissaoPedido);
			} else if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItemTipo2()) {
				edVlPctComissaoPedido.setValue(pedido.vlPctComissao);
			} else {
				edVlPctComissaoPedido.setValue(pedido.vlPctComissaoPedido);
			}
		}
		if (LavenderePdaConfig.isValidaPesoMaximoCargaPedido()) {
			edQtPesoCargaPedido.setValue(PedidoService.getInstance().findQtPesoTotalPedidosCargaPedido(itemPedido.pedido.cdCargaPedido, itemPedido.pedido.nuPedido) + (itemPedido.pedido.qtPeso - itemPedido.getQtItemFisicoAtualizaEstoque() * itemPedido.getPesoGrade()) + itemPedido.qtPeso);
			edPctPesoCargaPedido.setValue(edQtPesoCargaPedido.getValueDouble() * 100 / LavenderePdaConfig.qtdPesoMaximoCargaPedido);
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
			edVlST.setValue(itemPedido.vlSt);
			edVlItemST.setValue(itemPedido.getVlItemComSt());
			edVlTotalItemST.setValue(itemPedido.getVlTotalComST());
			edVlTotalPedidoST.setValue(pedido.vlTtPedidoComSt);
		}
		if (LavenderePdaConfig.usaCalculoReversoNaST) {
			if (!itemPedido.isEditandoValorItemST() && !itemPedido.isEditandoDescontoST()) {
				itemPedido.vlItemPedidoStReverso = itemPedido.getVlItemComSt();
			}
			edVlItemStReverso.setValue(ValueUtil.round(itemPedido.vlItemPedidoStReverso));
			edVlPctDescontoStReverso.setValue(itemPedido.vlPctDescontoStReverso);
		}
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido && pedido.getCliente().isFlAplicaSt()) {
			edVlST.setValue(itemPedido.vlSt);
			edVlItemST.setValue(itemPedido.vlItemPedido + itemPedido.vlSt);
			edVlTotalItemST.setValue((itemPedido.vlItemPedido + itemPedido.vlSt) * itemPedido.getQtItemFisico());
			edVlTotalPedidoST.setValue(pedido.vlTtPedidoComSt);
		}
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				edStItem.setValue(itemPedido.vlTotalStItem);
			} else {
				edStItem.setValue(itemPedido.getVlTotalST());
			}
			edStPedido.setValue(STService.getInstance().getVlTotalStPedido(pedido));
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			edSaldoCreditoConsignacaoRestanteCliente.setValue(FichaFinanceiraService.getInstance().getVlSaldoConsignadoCliente(null, pedido.getCliente(), false));
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
			edVlIpi.setValue(itemPedido.vlIpiItem);
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
					edVlItemIpi.setValue(itemPedido.vlItemIpi);
					edVlTotalItemIpi.setValue(itemPedido.getVlTotalItemComIpiCalcPersonalizado());
				} else {
					edVlItemIpi.setValue(itemPedido.getVlItemComIpiCalcPersonalizado());
					edVlTotalItemIpi.setValue(itemPedido.getVlTotalItemComIpiCalcPersonalizado());
				}
			} else {
				edVlItemIpi.setValue(itemPedido.getVlItemComIpi());
				edVlTotalItemIpi.setValue(itemPedido.getVlTotalItemComIpi());
			}
			edVlAliquotaIpi.setValue(produto.vlPctIpi);
			edVlTotalPedidoIpi.setValue(pedido.vlTtPedidoComIpi);
		}
		if (LavenderePdaConfig.isUsaCalculoIpiItemPedido() || LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			edVlTributos.setValue(itemPedido.getVlTributos());
			edVlItemTributos.setValue(itemPedido.getVlItemComTributos());
			edVlTotalItemTributos.setValue(itemPedido.getVlItemComTributos() * itemPedido.getQtItemFisico());
			edVlTotalPedidoTributos.setValue(pedido.vlTtPedidoComTributos);
			// Informa??es unit?rias
			edVlUnitarioTributos.setValue(itemPedido.getVlTributos() / (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? (itemPedido.getQtItemFisico() != 0 ? itemPedido.getQtItemFisico() : 1) : 1));
			edVlEmbalagemSt.setValue((LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? ValueUtil.round(itemPedido.vlTotalStItem / (itemPedido.getQtItemFisico() != 0 ? itemPedido.getQtItemFisico() : 1)) : itemPedido.vlSt));
			edVlUnitarioIpi.setValue((LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? ValueUtil.round(itemPedido.vlTotalIpiItem / (itemPedido.getQtItemFisico() != 0 ? itemPedido.getQtItemFisico() : 1)): itemPedido.vlIpiItem));
		}
		if (LavenderePdaConfig.mostraValorTotalPedidoItensComEstoque) {
			edQtItemEstoquePositivo.setValue(itemPedido.qtItemEstoquePositivo);
			edVlTotalItemPositivo.setValue(itemPedido.getVlEfetivoTotal());
			edVlTotalPedidoEstoquePositvo.setValue(pedido.vlTotalPedidoEstoquePositivo);
		}
		if ((LavenderePdaConfig.usaPctFreteTipoFreteNoPedido || LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso()) || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			edVlTotalItemFrete.setValue(itemPedido.vlTotalItemPedido + itemPedido.vlTotalItemPedidoFrete);
			edVlItemFrete.setValue(itemPedido.vlItemPedido + itemPedido.vlItemPedidoFrete);
			edVlTotalPedidoFrete.setValue(pedido.vlTotalPedido + pedido.vlFrete);
		}
		if ((LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() && LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.isUsaTipoFretePedido()) || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido()) {
			edVlTotalFreteItem.setValue(itemPedido.vlTotalItemPedidoFrete);
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			edPesoAtualPedido.setValue(pedido.qtPeso);
		}
		if (LavenderePdaConfig.isUsaPrecoItemPorPesoMinimoPedido() && (pedido.getTabelaPreco() != null || itemPedido.getTabelaPreco() != null)) {
			edPesoMinimoItem.setValue(LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() && itemPedido.getTabelaPreco() != null ? itemPedido.getTabelaPreco().qtPesoMin : pedido.getTabelaPreco().qtPesoMin);
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			edVolumeItem.setValue(itemPedido.getVlVolumeArrendondado());
			edVolumeTotalItem.setValue(itemPedido.vlVolumeItem);
			edVolumePedido.setValue(pedido.vlVolumePedido);
		}
		if (LavenderePdaConfig.controlarLimiteCreditoCliente || LavenderePdaConfig.bloquearLimiteCreditoCliente || LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
			Rede rede = null;
			if (LavenderePdaConfig.usaConfigInfoFinanceiroDaRedeParaClientes) {
				rede = RedeService.getInstance().findRedeByCliente(SessionLavenderePda.getCliente());
			}
			double saldo = FichaFinanceiraService.getInstance().getVlSaldoCliente(SessionLavenderePda.getCliente(),rede);
			edSaldoCreditoRestanteCliente.setValue(saldo);
		}
		if (LavenderePdaConfig.aplicaDescontoCCPAposInserirItem) {
			edVlItemPedidoComDescontoCCP.setValue(itemPedido.vlItemPedido - itemPedido.vlDescontoCCP);
			edVlPctDescontoCCPItemPedido.setValue(itemPedido.vlPctDescontoCCP);
		}
		if (LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido) {
			double vlTributosComVlAdicionais = itemPedido.getVlTributos() + itemPedido.vlDespesaAcessoria;
			double vlDeducoes = getVlDeducoesItem(vlTributosComVlAdicionais);
			edVlItemPedidoBruto.setValue(ValueUtil.round(itemPedido.vlItemPedido + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : itemPedido.vlItemPedidoFrete) + vlTributosComVlAdicionais - vlDeducoes));
			edVlTotalBrutoItemPedido.setValue(ValueUtil.round(itemPedido.vlTotalItemPedido + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : (itemPedido.vlItemPedidoFrete * itemPedido.getQtItemFisico())) + (vlTributosComVlAdicionais * itemPedido.getQtItemFisico()) - (vlDeducoes * itemPedido.getQtItemFisico())));
			edVlTotalPedidoComTributosEDeducoes.setValue(pedido.vlFinalPedidoDescTribFrete);
			edVlItemPedidoFreteESeguro.setValue(itemPedido.getVlFreteESeguro());
			edVlTotalItemPedidoFreteESeguro.setValue(itemPedido.getVlTotalFreteESeguro());
		}
		if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem()) {
			edVlPctDescCliente.setValue(itemPedido.vlPctDescCliente);
			edVlPctDescontoCondicao.setValue(itemPedido.vlPctDescontoCondicao);
			edVlPctDescFrete.setValue(itemPedido.vlPctDescFrete);
		} else if (LavenderePdaConfig.isAplicaDescontoCategoria()) {
			edVlPctDescCliente.setValue(pedido.vlPctDescCliente);
			edVlPctDescontoCondicao.setValue(pedido.vlPctDesc2);
			edVlPctDescFrete.setValue(pedido.vlPctDesc3);
		}
		ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
		if (itemPedido.usaCestaPromo) {
			edPreco.setValue(itemPedido.vlBaseItemPedido);
			edVlPctMaxDesconto.setValue(itemPedido.pctMaxDescCestaPromo);
		} else {
			if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco || LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
				double vlVerbaItemTmp;
				if ((LavenderePdaConfig.isMostraFlexPositivoPedido() || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco)  && itemPedido.vlVerbaItemPositivo > 0d) {
					vlVerbaItemTmp = itemPedido.vlVerbaItemPositivo;
				} else {
					vlVerbaItemTmp = itemPedido.vlVerbaItem;
				}
				if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
					vlVerbaItemTmp = itemPedido.vlVerbaItem + itemPedido.vlVerbaGrupoItem;
				}
				edVlVerbaItem.setValue(vlVerbaItemTmp);
				edVlVerbaItemPositiva.setValue(itemPedido.vlVerbaItemPositivo);
				edVlVerbaItemNegativa.setValue(itemPedido.vlVerbaItem);
				edVlPctVerba.setValue(itemPedido.vlPctVerba);
				double vlVerbaPedidoTmp = 0;
				vlVerbaPedidoTmp = LavenderePdaConfig.isMostraFlexPositivoPedido() ? pedido.vlVerbaPedidoPositivo + pedido.vlVerbaPedido : pedido.vlVerbaPedido;
				edVlVerbaPedido.setValue(vlVerbaPedidoTmp);
			}
			if (LavenderePdaConfig.informaVerbaManual && itemPedido.permiteAplicarDesconto()) {
				edVlVerbaManual.setValue(itemPedido.getVlVerbaManual());
				edVlVerbaItem.setValue(itemPedido.vlVerbaItem * -1);
				edVlVerbaItemNegativa.setValue(itemPedido.vlVerbaItem);
				double vlVerbaPedido = pedido.vlVerbaPedido < 0 ? pedido.vlVerbaPedido * -1 : pedido.vlVerbaPedido;
				edVlVerbaPedido.setValue(vlVerbaPedido);
			}
			if (itemTabelaPreco != null) {
				if ((LavenderePdaConfig.usaPrecoEspecialParaClienteEspecial) && (pedido.getCliente().isEspecial())) {
					edPreco.setValue(itemTabelaPreco.vlUnitarioEspecial);
				} else {
					edPreco.setValue(itemPedido.vlBaseItemTabelaPreco);
				}
			}
			if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente && itemTabelaPreco != null) {
				edVlPctMaxDesconto.setValue((itemTabelaPreco.vlPctDescValorBase - pedido.getCliente().vlPctMaxDesconto) + itemTabelaPreco.getVlPctMaxDescontoItemTabelaPreco(produto));
			} else {
				if ((LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() || LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido()) && !LavenderePdaConfig.isUsaDescontoMaximoEmValor()) {
					if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
						edVlPctMaxDesconto.setValue(TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido));
					} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido) && itemPedido.pedido.getTipoPedido().vlPctMaxDesconto != 0) {
						edVlPctMaxDesconto.setValue(itemPedido.pedido.getTipoPedido().vlPctMaxDesconto);
					} else {
						if (itemTabelaPreco != null) {
							edVlPctMaxDesconto.setValue(itemTabelaPreco.getVlPctMaxDescontoItemTabelaPreco(produto));
						}
					}
				}
				if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
					if (itemPedido.usaPctMaxDescLoteProduto) {
						edVlPctMaxDesconto.setValue(itemPedido.pctMaxDescLoteProdutoSelected);
					}
				}
			}
			if (LavenderePdaConfig.isAplicaDescontoFimDoPedido() || LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido()) {
				if (pedido.isPedidoFechado() && LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
					edVlPctPreviaDesc.setValue(pedido.vlPctDescProgressivo);
				} else {
					edVlPctPreviaDesc.setValue(getItemPedidoService().getMaiorPctDescontoParaItemPedido(pedido, itemPedido));
				}
			}
		}
		if (itemTabelaPreco != null) {
			edVlPctDescPromocional.setValue(!itemPedido.possuiDescMaxProdCli() && !itemPedido.getItemTabelaPreco().isFlBloqueiaDescPromo() ? itemTabelaPreco.vlPctDescPromocional : 0d);
			edVlBaseFlex.setValue(itemTabelaPreco.getVlBaseFlex(pedido, itemPedido));
			if (LavenderePdaConfig.usaPctMaxParticipacaoItemBonificacao && pedido.isPedidoBonificacao()) {
				edPctMaxParticipacaoItemBonificacao.setValue(itemTabelaPreco.vlPctMaxParticipacao);
				edPctAtualParticipacaoItemBonificacao.setValue(itemPedido.getVlAtualParticipacao());
			}
		}
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			edPrecoEmbPrimaria.setValue(itemPedido.vlEmbalagemElementar);
		} else if (produto.nuConversaoUnidadesMedida > 0) {
			edPrecoEmbPrimaria.setValue(itemPedido.getVlEmbalagemElementar());
		} else if (itemTabelaPreco != null) {
			edPrecoEmbPrimaria.setValue(itemTabelaPreco.vlPrecoEmbalagemPrimaria);
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			edRentabilidadeItem.setValue(itemPedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
			edRentabilidadePedido.setValue(pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0 || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado || LavenderePdaConfig.usaValorRentabilidadeSemCalculo()) {
			if (itemPedido.isItemBonificacao()) {
				edVlIndiceRentabItem.setText("");
			} else {
				edVlIndiceRentabItem.setValue(itemPedido.vlRentabilidade);
			}
			rentabilidadePedido(itemPedido);
			if (LavenderePdaConfig.indiceRentabilidadePedido > 0) {
				setColorsOnIndicesRentabilidade();
			}
			if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
				setColorsOnIndicesRentabilidadeSemTributos();
			}
		}
		edQtEmbalagem.setValue(produto.nuMultiploEmbalagem);
		if (itemTabelaPreco != null) {
			edVlEmbalagem.setValue(itemTabelaPreco.vlPrecoEmbalagem);
		}
		if (LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem()) {
			edVlUnidadeEmbalagem.setValue(getItemPedidoService().calculaVlUnidadePorEmbalagem(itemPedido, pedido));
		}
		if (LavenderePdaConfig.usaValorRetornoProduto) {
			edVlRetornoProduto.setValue(itemPedido.vlRetornoProduto);
		}
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			edQtPtItens.setValue(itemPedido.qtPontosItem);
		}
		setValueEdQtMinVenda(itemPedido);
		edNuMultiploIdeal.setValue(produto.nuMultiploIdeal);
		if (LavenderePdaConfig.isPermiteBonificarProduto()) {
			double vlMaxPctBonificacao = 0;
			if (LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble() > 0) {
				vlMaxPctBonificacao = LavenderePdaConfig.getPercMaxValorPedidoBonificadoDouble();
			}
			edVlMaxBonificacao.setValue(((pedido.vlTotalPedido * vlMaxPctBonificacao) / 100) - pedido.vlBonificacaoPedido);
		}
		if (pedido.isStatusPedidoNaoOcultaValoresComissao()) {
			if (LavenderePdaConfig.usaDescontoComissaoPorGrupo || LavenderePdaConfig.usaDescontoComissaoPorProduto || LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem() || LavenderePdaConfig.usaConfigCalculoComissao()) {
				edVlPctComissao.setValue(itemPedido.vlPctComissao);
			} else if (itemTabelaPreco != null) {
				edVlPctComissao.setValue(itemTabelaPreco.vlPctComissao);
			}
		}
		if (LavenderePdaConfig.pctMargemAgregada > 0) {
			edVlAgregadoSugerido.setValue(itemPedido.vlAgregadoSugerido);
			edPctMargemAgregada.setValue(itemPedido.pctMargemAgregada);
		}
		if (isPermiteUsarOportunidade()) {
			bgOportunidade.setValue(itemPedido.flOportunidade);
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && isEditing()) {
			bgItemTroca.setValueBoolean(TipoItemPedido.TIPOITEMPEDIDO_TROCA_REC.equals(itemPedido.flTipoItemPedido));
		}
		if (LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			bgGeraVerba.setValue(itemPedido.getItemPedidoAud().flGeraVerba);
			edVlVerbaNecessaria.setValue(itemPedido.getItemPedidoAud().vlVerbaNecessaria);
			edVlNeutroItem.setValue(itemPedido.getItemPedidoAud().vlItemPedidoNeutro);
		}
		if (LavenderePdaConfig.usaVariacaoPrecoProdutoPorCategoriaERegiaoCliente) {
			edVlItemTabPrecoVariacaoPreco.setValue(itemPedido.vlItemTabPrecoVariacaoPreco);
		}
		if (LavenderePdaConfig.isUsaDescontaComissaoRentabilidadePorItem()) {
			edFaixaComissaoRentabilidade.setText(ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadeItemPedido(itemPedido));
			if (LavenderePdaConfig.usaRentabilidadeMinimaItemPedido) {
				edFaixaComissaoRentabilidadeMinima.setText(ComiRentabilidadeService.getInstance().getEscalaFaixaByPctRentabilidadeMinima(itemPedido));
			}
		}
		if (LavenderePdaConfig.usaDescItemPorCanalCliEGrupoProdEContratoCli || LavenderePdaConfig.usaDecisaoPrecoBaseadoCanalCliEGrupoProdEContratoCli) {
			edVlPctDescCanal.setValue(itemPedido.vlPctDescontoCanal);
			edVlPctDescContratoCli.setValue(pedido.getCliente().vlPctContratoCli);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			edVlSeguroItemPedido.setValue(itemPedido.vlSeguroItemPedido);
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.usaFretePedidoPorToneladaCliente) {
			edVlTonFreteCliente.setValue(pedido.getCliente().vlTonFrete);
			edVlFretePedido.setValue(pedido.vlFrete);
		}
		if (LavenderePdaConfig.isUsaValidacaoEstoqueLocalEstCondNegociacao()) {
			edQtEstoqueNegociacao.setValue(ValueUtil.getIntegerValue(EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, pedido.getCondicaoNegociacao().cdLocalEstoque)));
		}
		if (LavenderePdaConfig.apresentaDsFichaTecnicaCapaItemPedido && LavenderePdaConfig.usaGradeProduto5() && (inVendaUnitariaMode || fromMenuCatalogForm || isEditing() || LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido())) {
			edDsFichaTecnica.setValue(itemPedido.getProduto().dsFichaTecnica);
			edDsFichaTecnica.setEditable(false);
		}
		if (LavenderePdaConfig.isConfigApresentacaoInfosPersonalizadasCapaItemPedido()) {
			itemPedido.infosPersonalizadas = ItemPedidoService.getInstance().getInfosPersonalizadasItemPedido(itemPedido);
			edDsInfosPersonalizadasItemPedido.setValue(itemPedido.infosPersonalizadas);
		}
		// --
		if (LavenderePdaConfig.isMostraFotoProduto()) {
			boolean hasFoto = itemPedido.getProduto().cdProduto != null && itemPedido.hasFoto(LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade());
			btIconeFoto.setImage(getIcon("slidefotos.png", hasFoto));
		}

		if (LavenderePdaConfig.isUsaConfigVideosProdutos() || LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			boolean hasVideo = produtoPossuiVideo(itemPedido);
			btIconeVideos.setImage(getIcon("iconevideos.png", hasVideo));
		}

		if (LavenderePdaConfig.isMostraConfigTelaInfoProduto()) {
			btIconeDetalheProduto.setImage(getIcon("pesquisacliente.png", true));
		}

		if (LavenderePdaConfig.mostraInfoEstoqueUnidades) {
			btIconeInfoEstoqueUnidades.setImage(getIcon("estoque.png", true));
		}

		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			btIconeCarrosselPopup.setImage(getIcon("carrossel.png", true));
		}
		if (LavenderePdaConfig.isPermiteOcultarCamposItemPedidoGrade()) {
			String iconName = hideAttributesVendaUnitaria ? "show.png" : "hide.png";
			btOcultaMostraEditLabels.setImage(getIcon(iconName, true));
		}
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			setIconGrupoDestaque(itemPedido);
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			btIconeBonificacao.setImage(getIcon("bonificacao.png",TipoItemPedido.TIPOITEMPEDIDO_BONIFICACAO.equals(getItemPedido().flTipoItemPedido)));
		}
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex
				|| LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco
				|| LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao
				|| LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto
				|| LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()
				&& btIconeVerba != null) {
			btIconeVerba.setImage(getIcon("verba.png", isUtilizaVerba(itemPedido)));
		}
		if (LavenderePdaConfig.mostraFaixaComissaoPedidoEItem()) {
			ComissaoPedidoRepService.getInstance().applyComissaoPedidoRepInItemPedido(itemPedido);
			if (pedido.isStatusPedidoNaoOcultaValoresComissao()) {
				btIconeComissaoItemPedido.setImage(ComissaoPedidoRepService.getInstance().getIconComissao(itemPedido.comissaoPedidoRep, true));
			}
		}
		if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
			btIconeKit.setImage(getIcon("kit.png", (getItemPedido().getProduto() != null) && getItemPedido().getProduto().isKit()));
		}
		if (LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido) {
			btIconeFracaoFornecedor.setImage(getIcon("fracaoFornecedor.png", true));
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			btIconeAutorizacao.setImage(SolAutorizacaoService.getInstance().getIconSolicitacao(itemPedido));
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
			boolean enabled = itemPedido.isAgrupadorSimilaridade();
			btIconeSimilar.setImage(getIcon("similaridade.png", enabled));
			btIconeSimilar.setEnabled(enabled);
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido()) {
			if (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0) {
				btIconeRentabilidade.setImage(RentabilidadeFaixaService.getInstance().getIconRentabilidadeItem(itemPedido));
			} else if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoItemTabelaPreco) {
				btIconeRentabilidade.setImage(ItemTabelaPrecoService.getInstance().getIconRentabilidadeItem(itemPedido));
				btIconeRentabilidade.eventsEnabled = false;
				int color = ItemTabelaPrecoService.getInstance().getCorRentabilidade(itemPedido);
				if (color != Color.BRIGHT) {
					lbRentabilidadeItem.setForeColor(color);
				} else {
					lbRentabilidadeItem.setForeColor(LavendereColorUtil.labelNameForeColor);
				}
			}
		} else if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			btIconeRentabilidade.setImage(ItemPedidoService.getInstance().getIconRentabilidadeItem(itemPedido));
			lbRentabilidadeItem.setForeColor(LavendereColorUtil.labelNameForeColor);
		}

		if (LavenderePdaConfig.mostraSeloPontuacaoItem) {
			double vlPesoPontuacao = PontuacaoProdutoService.getInstance().getVlPesoPontuacaoItemPedido(pedido, itemPedido);
			btVlPesoProdutoPontuacao.setText(StringUtil.getStringValueToInterface(vlPesoPontuacao, LavenderePdaConfig.nuCasasDecimaisPontuacao));
		}
		if (LavenderePdaConfig.isOcultaInfoRentabilidadeManualmente()) {
			btIconeOcultaInfoRentabilidade.setImage(getImageIconOcultaInfoRentabilidade(SessionLavenderePda.isOcultoInfoRentabilidade));
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			if (itemPedido.possuiItemTabelaPreco()) {
				edUltimoPrecoPraticado.setText(StringUtil.getStringValueToInterface(ItemPedidoService.getInstance().getUltimoPrecoPraticadoComIpi(itemPedido)));
			}
			Tributacao tributacao = itemPedido.getTributacaoItem();
			edAliquotaSt.setText(StringUtil.getStringValueToInterface(tributacao != null ? tributacao.vlPctIcmsRetido : 0.00));
			edDifStEIpi.setText(StringUtil.getStringValueToInterface(itemPedido.getPctDifStEIpi()));
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem() && itemTabelaPreco != null) {
			edTxAntecipacao.setValue(ValueUtil.round(itemTabelaPreco.vlPctTaxa, ValueUtil.doublePrecision));
			edDtVencimentoPrecoProduto.setValue(itemTabelaPreco.dtVencimentoPreco);
		}
		carregaUltimoDescontoAcrescimoItemPedido(itemPedido);

		ProdutoUnidade produtoUnidade = itemPedido.getProdutoUnidade();
		if (itemTabelaPreco != null) {
			boolean isMultiplica = (produtoUnidade != null) ? produtoUnidade.isMultiplica() : true;
			double nuConversaoUnidade = ProdutoUnidadeService.getInstance().getNuConversaoUnidade(itemPedido.getItemTabelaPreco(), produtoUnidade);
			edVlTotalItemTabelaPreco.setValue(ItemPedidoService.getInstance().calculaVlTotalItemTabelaPrecoItemPedido(itemTabelaPreco.vlUnitario, nuConversaoUnidade, isMultiplica));
		}
		if (LavenderePdaConfig.exibeDescontoAcrescimoIndice()) {
			edPercIndiceFinanceiroCondPagto.setValue(CondicaoPagamentoService.getInstance().loadVlPctDescAcresCondPagto(pedido));
		}
		if (LavenderePdaConfig.usaDescMaxProdCli && itemPedido.getDescMaxProdCli() != null) {
			edPctMaxDescProdutoCliente.setValue(itemPedido.getDescMaxProdCli().vlPctDescMax);
		} else {
			edPctMaxDescProdutoCliente.setValue(0d);
		}
		if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto()) {
			edPctDescPoliticaInterpol.setValue(itemPedido.vlPctDescontoAuto);
			edVlDescPoliticaInterpol.setValue(itemPedido.vlDescontoAuto);
			edVlTotDescPoliticaInterpol.setValue(itemPedido.vlTotalDescontoAuto);
			edVlPctDescEfetivo.setValue(itemPedido.vlPctDescontoEfetivo);
		}
		if (LavenderePdaConfig.configFreteEmbutidoDestacadoCliente()) {
			edVlItemFreteTributacao.setValue(itemPedido.vlItemFreteTributacao);
			edVlTotalItemFreteTributacao.setValue(itemPedido.vlTotalItemFreteTributacao);
			edVlTotalPedidoFreteTributacao.setValue(pedido.vlTotalPedidoFreteTributos);
		}
		if (LavenderePdaConfig.consisteMultiploEmbalagem) {
			boolean avisar = produto != null && ValueUtil.isNotEmpty(produto.flConsisteConversaoUnidade);
			edConsisteConversaoUnidade.setText(avisar ? Produto.TIPO_VALIDACAO_MULTIPLO_EMBALAGEM_AVISAR : Produto.TIPO_VALIDACAO_MULTIPLO_EMBALAGEM_BLOQUEAR);
		}
		if (LavenderePdaConfig.isValidaTabelaPrecoFechamentoPedido()) {
			if (itemPedido.qtPeso > 0) {
				edVlTotalItemPorPeso.setValue((itemPedido.vlTotalItemPedido > 0 ? itemPedido.vlTotalItemPedido : 0) / itemPedido.qtPeso);
			} else {
				edVlTotalItemPorPeso.setText(ValueUtil.VALOR_ZERO);
			}
		}
		ordenaCbUnidadeAlternativaConformeGrid();

		if ((isAvisaVendaProdutoSemEstoqueByItemPedido(itemPedido.getProduto().isIgnoraValidacao()) || LavenderePdaConfig.isCampoVisivelTelaItemPedido(ID_CAMPO_DT_ESTOQUE_PREVISTO)) && !isModoGrade()) {
			Estoque estoque = EstoqueService.getInstance().findDadosPrevistoParaEstoqueToInterface(itemPedido);
			edQtEstoquePrevisto.setValue(estoque.qtEstoquePrevisto);
			edDtEstoquePrevisto.setValue(estoque.dtEstoquePrevisto);
		}
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) {
			edPctDescontoDoPedido.setValue(pedido.vlPctDescItem);
		}
		if (LavenderePdaConfig.usaConfigMargemContribuicaoRegra2()) {
			edVlPctTotalMargemItem.setValue(itemPedido.vlPctTotalMargemItem);
		}
		if (LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) {
			edVlPctDescCondPagto.setValue(itemPedido.vlPctDescCondPagto);
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoMensalNoItem()) {
			edDtPagamento.setValue(itemPedido.dtPagamento);
			edVlBaseAntecipacao.setValue(itemPedido.vlBaseAntecipacao);
		}
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.mostraValorBruto) {
			edVlTotalItemTribFreteSeguro.setValue(itemPedido.getVlTotalItemPedidoTributosFreteSeguro());
			edVlTotalItemUnitarioTribFreteSeguro.setValue(itemPedido.getVlItemPedidoUnitarioTributosFreteSeguro());
		}
		if (LavenderePdaConfig.isCalculaSeguroNoItemPedido()) {
			edVlTotalSeguroItemPedido.setValue(itemPedido.getVlTotalSeguro());
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedidoConsideraGramatura()) {
			edVlPesoItemUnitario.setValue(itemPedido.qtPeso);
			if (itemPedido.rowKey == null) {
				edVlPesoTotalItem.setValue(itemPedido.qtPeso + pedido.qtPeso);
			} else {
				edVlPesoTotalItem.setValue((pedido.qtPeso - itemPedido.oldQtPeso) + itemPedido.qtPeso);
			}
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			edVlPontuacaoRealizadoItem.setValue(itemPedido.vlPontuacaoRealizadoItem);
			edVlPontuacaoBaseItem.setValue(itemPedido.vlPontuacaoBaseItem);
			setPontuacaoColor(itemPedido);
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade()) {
			edVlPctMargemRentabItem.setValue(itemPedido.vlPctMargemRentab);
			edVlPctMargemRentabPedido.setValue(pedido.vlPctMargemRentab);
			setColorMargemRentab(itemPedido);
			changeVisibleValueRentabilidade();
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			edVlPctDescProgressivo.setValue(itemPedido.vlPctDescProg);
		}
		if (itemPedido.isGondola()) {
			edQtItemGondola.setValue(itemPedido.qtItemGondola);
		} else if (pedido.isGondola()) {
			edQtItemGondola.setText(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			edVlPctPoliticaComercial.setValue(itemPedido.vlPctPoliticaComercial);
		}
		if (LavenderePdaConfig.usaDescQuantidadePeso()) {
			edVlPctFaixaDescQtdPeso.setValue(itemPedido.vlPctFaixaDescQtdPeso);
		}
		if (LavenderePdaConfig.usaCotacaoMoedaProduto) {
			edDsMoedaProduto.setValue(itemPedido.getProduto().dsMoedaProduto);
			edVlCotacaoMoeda.setValue(itemPedido.getItemTabelaPreco().vlCotacaoMoeda);
		}
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			edVlPctDescQuantidade.setValue(itemPedido.vlPctFaixaDescQtd);
		}
		if (LavenderePdaConfig.usaReservaEstoqueCorrenteR3() ){
			carregaDadosEstoquePrevGeral(itemPedido.getProduto());
		}
		if (isProdutoFromSugestaoIndustria()) {
			int value = ProdutoIndustriaService.getInstance().getQuantidadeSugeridaArredondada(getProdutoSelecionadoNaLista());
			edQtSugerida.setValue(value);
		} else {
			edQtSugerida.setText(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.isConfigStatusItemPedido() && itemPedido.pedido.isFlOrigemPedidoErp() && !itemPedido.pedido.isPedidoPendente()) {
			lvStatusItemPedido.setText(itemPedido.dsStatusItemPedido);
		}
		if (LavenderePdaConfig.isUsaVlUnitGiroProduto() && itemPedido.giroProduto != null) {
			lvVlUnitGiroProduto.setValue(itemPedido.giroProduto.vlUnitario);
		}
		if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido()) {
			edNuOrdemCompraCliente.setValue(itemPedido.nuOrdemCompraCliente);
			edNuSeqOrdemCompraCliente.setValue(ValueUtil.VALOR_NI);
			if (itemPedido.nuSeqOrdemCompraCliente > 0) {
				edNuSeqOrdemCompraCliente.setValue(String.valueOf(itemPedido.nuSeqOrdemCompraCliente));
			}
		}
	}

	private void carregaDadosEstoquePrevGeral(Produto produto) throws SQLException {
		if(produto != null) {
			Vector estoquePrevistoList = new Vector();
			double qtEstoquePrevisto = 0;
			edQtEstoquePrevistoGeral.setValue(0);
			edDtEstoquePrevistoGeral.setValue(null);
			EstoquePrevistoGeral estoquePrevistoGeralFilter = new EstoquePrevistoGeral(produto.cdEmpresa, produto.cdRepresentante, produto.cdProduto, new Date(),"S");
			if(LavenderePdaConfig.usaReservaEstoqueCorrenteR3()) {
				estoquePrevistoGeralFilter.cdLocalEstoque = pedido.getCdLocalEstoque();
			}
			estoquePrevistoGeralFilter.dtEstoque = null;
			Vector estoquePrevistoListAux = new Vector();
			estoquePrevistoList = EstoquePrevistoGeralService.getInstance().findAllByExample(estoquePrevistoGeralFilter);
			if (ValueUtil.isEmpty(estoquePrevistoList) && LavenderePdaConfig.usaReservaEstoqueCorrenteR3() && SessionLavenderePda.isUsuarioSupervisor()) {
				if (pedido.getCentroCusto() != null) {
					estoquePrevistoGeralFilter.cdRepresentante = pedido.getCentroCusto().cdRepresentante;
					estoquePrevistoList = EstoquePrevistoGeralService.getInstance().findAllByExample(estoquePrevistoGeralFilter);
					estoquePrevistoGeralFilter.cdRepresentante = produto.cdRepresentante;
				}
			}
			estoquePrevistoGeralFilter.dtEstoque = new Date();
			estoquePrevistoGeralFilter.flEstoqueFisico = "N";
			estoquePrevistoListAux = EstoquePrevistoGeralService.getInstance().findAllNaoVencidosSomados(estoquePrevistoGeralFilter);
			if (ValueUtil.isEmpty(estoquePrevistoListAux) && LavenderePdaConfig.usaReservaEstoqueCorrenteR3() && SessionLavenderePda.isUsuarioSupervisor()) {
				if (pedido.getCentroCusto() != null) {
					estoquePrevistoGeralFilter.cdRepresentante = pedido.getCentroCusto().cdRepresentante;
					estoquePrevistoListAux = EstoquePrevistoGeralService.getInstance().findAllNaoVencidosSomados(estoquePrevistoGeralFilter);
				}
			}
			int size = estoquePrevistoListAux.size();
			for (int i = 0; i < size; i++) {
				EstoquePrevistoGeral estoque = (EstoquePrevistoGeral) estoquePrevistoListAux.items[i];
				estoquePrevistoList.addElement(estoque);
			}

			size = estoquePrevistoList.size();
			double qtEstoqueFisico = 0d;
			edDtEstoquePrevistoGeral.setValue(null);
			for (int i = 0; i < size; i++) {
				EstoquePrevistoGeral estoque = (EstoquePrevistoGeral) estoquePrevistoList.items[i];
				if (ValueUtil.valueEquals(estoque.flEstoqueFisico,"S")) {
					qtEstoqueFisico += estoque.qtEstoque;
				}
				if ((ValueUtil.valueEquals(estoque.flEstoqueFisico,"N") || estoque.flEstoqueFisico == null)
						&& edDtEstoquePrevistoGeral.getValue() == null) {
					edDtEstoquePrevistoGeral.setValue(estoque.dtEstoque);
				}
				qtEstoquePrevisto += estoque.qtEstoque;
			}
			edQtEstoquePrevistoGeral.setValue(qtEstoqueFisico);
			edQtSomaEstoquePrevistoGeral.setValue(qtEstoquePrevisto);
		}
	}

	private boolean produtoPossuiVideo(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaConfigVideosProdutos()) {
			VideoProduto videoProdutoFilter = new VideoProduto();
			videoProdutoFilter.cdEmpresa = itemPedido.cdEmpresa;
			videoProdutoFilter.cdProduto = itemPedido.cdProduto;
			boolean hasVideoProduto = VideoProdutoService.getInstance().countByExample(videoProdutoFilter) > 0;
			if (hasVideoProduto) {
				return true;
			}
		}
		if (LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			VideoProdutoGrade videoProdutoGradeFilter = new VideoProdutoGrade();
			videoProdutoGradeFilter.cdEmpresa = itemPedido.cdEmpresa;
			videoProdutoGradeFilter.dsAgrupadorGrade = itemPedido.getProduto().getDsAgrupadorGrade();
			return VideoProdutoGradeService.getInstance().countByExample(videoProdutoGradeFilter) > 0;
		}
		return false;
	}

	private boolean possuiPontuacaoProduto(ItemPedido itemPedido) throws SQLException {
		return getItemPedidoService().possuiPontuacaoProduto(itemPedido);
	}

	protected void changeVisibleValueRentabilidade() throws SQLException {
		if (getItemPedido() != null && LavenderePdaConfig.isOcultaValorRentabilidadeSePositivaDoItemPedido(getItemPedido().vlPctMargemRentab)) {
			edVlPctMargemRentabItem.setVisible(false);
		} else if (!edVlPctMargemRentabItem.isVisible()) {
			edVlPctMargemRentabItem.setVisible(true);
		}
		if (pedido != null && LavenderePdaConfig.isOcultaValorRentabilidadeSePositivaDoPedido(pedido)) {
			edVlPctMargemRentabPedido.setVisible(false);
		} else if (!edVlPctMargemRentabPedido.isVisible()) {
			edVlPctMargemRentabPedido.setVisible(true);
		}
	}


	protected boolean isExibeIconeRentabilidadePedido() {
		return (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && (LavenderePdaConfig.usaControleRentabilidadePorFaixa > 0 || LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoItemTabelaPreco));
	}

	protected boolean isExibeIconeRentabilidade() {
		return isExibeIconeRentabilidadePedido() || LavenderePdaConfig.usaConfigMargemRentabilidade();
	}

	private void setColorMargemRentab(ItemPedido itemPedido) throws SQLException {
		int cor = MargemRentabFaixaService.getInstance().findCorMargemRentabFaixa(itemPedido.cdEmpresa, itemPedido.cdMargemRentab, itemPedido.vlPctMargemRentab);
		edVlPctMargemRentabItem.setLabelForeColor(cor);
		edVlPctMargemRentabItem.setForeColor(cor);
		cor = MargemRentabFaixaService.getInstance().findCorMargemRentabFaixa(pedido.cdEmpresa, pedido.cdMargemRentab, pedido.vlPctMargemRentab);
		edVlPctMargemRentabPedido.setLabelForeColor(cor);
		edVlPctMargemRentabPedido.setForeColor(cor);

	}

	public void setPontuacaoColor(ItemPedido itemPedido) {
		final int pontuacaoColor = PontuacaoConfigService.getInstance().getPontuacaoColor(itemPedido.vlPontuacaoRealizadoItem, itemPedido.vlPontuacaoBaseItem, LavenderePdaConfig.mostraPontuacaoListaItemBase(), LavenderePdaConfig.mostraPontuacaoListaItemRealizado(), false);
		edVlPontuacaoRealizadoItem.setLabelForeColor(pontuacaoColor);
		edVlPontuacaoRealizadoItem.setForeColor(pontuacaoColor);
		edVlPontuacaoBaseItem.setLabelForeColor(pontuacaoColor);
		edVlPontuacaoBaseItem.setForeColor(pontuacaoColor);
	}

	private void rentabilidadePedido(ItemPedido itemPedido) throws SQLException {
		if (exibeCampoRentabilidadeEstimado) {
			Pedido pedidoCopia = PedidoService.getInstance().calculoRentabilidadeEmMemoria(pedido, itemPedido);
			edVlIndiceRentabEstimadoPedido.setValue(pedidoCopia.vlRentabilidade);
		}
		edVlIndiceRentabPedido.setValue(pedido.vlRentabilidade);
	}

	private void setValueEdQtMinVenda(ItemPedido itemPedido) throws SQLException {
		double qtMinVenda = LavenderePdaConfig.usaUnidadeAlternativa ? itemPedido.getItemTabelaPreco() != null ? itemPedido.getItemTabelaPreco().qtMinimaVenda : 0 : itemPedido.getProduto().qtMinimaVenda;
		if (LavenderePdaConfig.controlaDescontoUsandoVerbaAtravesQtMinItens) {
			qtMinVenda = ItemPedidoService.getInstance().getQtdMinVendaUnidadeAlternativa(itemPedido, qtMinVenda);
		}
		edQtMininimaVenda.setValue(qtMinVenda);
	}

	private void carregaUltimoDescontoAcrescimoItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() || LavenderePdaConfig.isPermiteAcrescimoPercentualItemPedido()) {
			if (itemPedido != null && mostraUltimoDescontoAcrescimo) {
				ItemPedido itemPedidoUltimoPedidoCliente = itemPedido.getItemPedidoUltimoPedidoCliente();
				edUltimoDescontoPraticado.setValue(0);
				edUltimoAcrescimoPraticado.setValue(0);
				if (itemPedidoUltimoPedidoCliente != null) {
					if (LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() && itemPedidoUltimoPedidoCliente.vlPctDesconto > 0) {
						edUltimoDescontoPraticado.setValue(itemPedidoUltimoPedidoCliente.vlPctDesconto);
					}
					if (LavenderePdaConfig.isPermiteAcrescimoPercentualItemPedido() && itemPedidoUltimoPedidoCliente.vlPctAcrescimo > 0) {
						edUltimoAcrescimoPraticado.setValue(itemPedidoUltimoPedidoCliente.vlPctAcrescimo);
					}
				}
			}
		}
	}

	public double getVlDeducoesItem(double vlTributosComVlAdicionais) throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && itemPedido.getQtItemFisico() > 0d) {
			vlTributosComVlAdicionais = itemPedido.vlTotalIpiItem / itemPedido.getQtItemFisico() + itemPedido.vlTotalStItem / itemPedido.getQtItemFisico() + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : itemPedido.vlSeguroItemPedido);
			if (itemPedido.getProduto().isDebitaPisCofinsZonaFranca() && itemPedido.pedido.getCliente().isDebitaPisCofinsZonaFranca()) {
				return itemPedido.vlTotalPisItem / itemPedido.getQtItemFisico() + itemPedido.vlTotalCofinsItem / itemPedido.getQtItemFisico() + itemPedido.vlTotalIcmsItem / itemPedido.getQtItemFisico();
			}
		}
		return 0;
	}

	public boolean isUtilizaVerba(ItemPedido itemPedido) throws SQLException {
		boolean utilizaVerba = false;
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			if (pedido.isPedidoFechado() && itemPedido.vlVerbaItem > 0d) {
				utilizaVerba = true;
			}
		} else if (itemPedido.getProduto().isUtilizaVerba()) {
			utilizaVerba = true;
		}
		return utilizaVerba;
	}

	private void setIconGrupoDestaque(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getProduto() != null && (ValueUtil.isNotEmpty(itemPedido.getProduto().cdGrupoDestaque) || (LavenderePdaConfig.usaGrupoDestaqueItemTabPreco))) {
			boolean setNoIconGrupoDestaque = false;
			ProdutoDestaque produtoDestaque;
			if (LavenderePdaConfig.usaGrupoDestaqueItemTabPreco) {
				ProdutoTabPreco produtoTabPreco = ProdutoTabPrecoService.getInstance().getProdutoTabPreco(itemPedido.getProduto().cdProduto);
				produtoDestaque = ProdutoDestaqueService.getInstance().findProdutoDestaqueByProdutoBaseAndTabPreco(itemPedido.getProduto(), itemPedido.cdTabelaPreco, produtoTabPreco.dsTabPrecoList, produtoTabPreco.cdGrupoDestaque);
			} else {
				produtoDestaque = ProdutoDestaqueService.getInstance().findProdutoDestaqueByProdutoBase(itemPedido.getProduto());
			}
			if (produtoDestaque != null) {
				try {
					int alturaLargura = (int) (heigthContainerIcons * 0.70);
					Image img = UiUtil.getSmoothScaledImage(UiUtil.getImage(produtoDestaque.imIcone), alturaLargura, alturaLargura);
					btIconeGrupoDestaque.setImage(img);
					msgGrupoDestaque = StringUtil.getStringValue(produtoDestaque.dsGrupoDestaque);
					setNoIconGrupoDestaque = true;
				} catch (ApplicationException ex) {
					setNoIconGrupoDestaque = false;
				}
			}
			if (!setNoIconGrupoDestaque) {
				setNoIconGrupoDestaque(produtoDestaque == null);
			}
		} else {
			setNoIconGrupoDestaque(true);
		}
	}

	private void setNoIconGrupoDestaque(boolean noGrupoDestaque) {
		msgGrupoDestaque = noGrupoDestaque ? Messages.PRODUTO_NAO_POSSUI_GRUPO_DESTAQUE : Messages.PRODUTO_NAO_POSSUI_FOTOS_GRUPO_DESTAQUE;
		btIconeGrupoDestaque.setImage(getIcon("produtodestaque.png", false));
	}

	protected void refreshProdutoToScreen(final ItemPedido itemPedido) throws SQLException {
		Produto produtoItem = itemPedido.getProduto();
		if (LavenderePdaConfig.usaConversaoUnidadesMedida && produtoItem != null) {
			if (!ValueUtil.isEmpty(produtoItem.dsUnidadeFisica)) {
				lbQtItemFisico.setText(produtoItem.dsUnidadeFisica);
			} else if (!ValueUtil.isEmpty(produtoItem.dsUnidadeFaturamento) && LavenderePdaConfig.ocultaQtItemFaturamento) {
				lbQtItemFisico.setText(produtoItem.dsUnidadeFaturamento);
			} else {
				lbQtItemFisico.setText(LavenderePdaConfig.usaConversaoUnidadesMedida ? Messages.ITEMPEDIDO_LABEL_QTITEMFISICOUN : Messages.ITEMPEDIDO_LABEL_QTITEMFISICO);
			}
			if (!ValueUtil.isEmpty(produtoItem.dsUnidadeFaturamento)) {
				lbQtItemFaturamento.setText(produtoItem.dsUnidadeFaturamento);
			} else {
				lbQtItemFaturamento.setText(Messages.ITEMPEDIDO_LABEL_QTITEMFISICO);
			}
		}
		if (isAplicaMultiploEspecialAuto(itemPedido)) {
			aplicaMultiploEspecialAuto(itemPedido);
		}
	}

	protected void list() throws SQLException {
		if (!flFromCadPedido && !fromProdutoPendenteGiroMultInsercao) {
			super.list();
		}
		if (!this.equals(instance) && instance != null) {
			instance.listInstance();
		}
		if (LavenderePdaConfig.usaOportunidadeVenda && getBaseCrudListForm() != null && getBaseCrudListForm().prevContainer instanceof ListItemPedidoForm) {
			((ListItemPedidoForm) getBaseCrudListForm().prevContainer).list();
		}
	}

	protected void listInstance() throws SQLException {
		if (!flFromCadPedido) {
			BaseCrudListForm form = getBaseCrudListForm();
			if (form != null && form instanceof ListItemPedidoForm && ((ListItemPedidoForm)form).isInstanceExibited()) {
				super.list();
			}
		}
	}

	@Override
	protected BaseDomain createDomain() throws SQLException {
		return new ItemPedido();
	}

	public ItemPedido getItemPedido() throws SQLException {
		return (ItemPedido) getDomain();
	}

	// @Override
	protected String getEntityDescription() {
		return title;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return getItemPedidoService();
	}

	public ItemPedidoService getItemPedidoService() {
		return ItemPedidoService.getInstance();
	}

	public PedidoService getPedidoService() {
		return PedidoService.getInstance();
	}

	// @Override
	protected void update(final BaseDomain domain) throws java.sql.SQLException {

	}

	protected void excluirClick() throws SQLException {
		if (delete()) {
			voltarClick();
			list();
		}
	}

	private ItemPedido getItemPedidoAndPedidoInstance() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.pedido = pedido;
		return itemPedido;
	}

	@Override
	public void add() throws SQLException {
		super.add();
		addNovoItem();
		addItensOnButtonMenu();
	}

	protected ItemPedido addNovoItem() throws SQLException {
		return addNovoItem(false);
	}

	protected ItemPedido addNovoItem(boolean fromMultInsercao) throws SQLException {
		return addNovoItem(fromMultInsercao, 0);
	}

	protected ItemPedido addNovoItem(boolean fromMultInsercao, int nuSeqItemPedido) throws SQLException {
		ItemPedido itemPedido;
		if (!fromMultInsercao) {
			setDomain(createDomain());
			itemPedido = (ItemPedido) getDomain();
		} else {
			itemPedido = (ItemPedido) createDomain();
		}
		itemPedido.pedido = pedido;
		itemPedido.cdEmpresa = pedido.cdEmpresa;
		itemPedido.cdRepresentante = pedido.cdRepresentante;
		itemPedido.flOrigemPedido = pedido.flOrigemPedido;
		itemPedido.nuPedido = pedido.nuPedido;
		itemPedido.cdUfClientePedido = pedido.getCliente().dsUfPreco;
		itemPedido.cdItemGrade1 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade2 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.cdItemGrade3 = ProdutoGrade.CD_ITEM_GRADE_PADRAO;
		itemPedido.nuSeqProduto = ItemPedido.NUSEQPRODUTO_UNICO;
		itemPedido.nuSeqItemPedido = fromMultInsercao ? nuSeqItemPedido : getItemPedidoService().getNextNuSeqItemPedido(pedido);
		if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
			itemPedido.cdPrazoPagtoPreco = pedido.getCondicaoPagamento().cdPrazoPagtoPreco;
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			itemPedido.cdUnidade = cbUnidade.getValue();
		}

		recalcularRentabilidadePedido(RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_INSERIR_ITEMPEDIDO);

		return itemPedido;
	}

	protected boolean recalcularRentabilidadePedido(RecalculoRentabilidadeOptions option) throws SQLException {
		return MargemRentabService.getInstance().recalcularRentabilidadePedidoAbertoSeNecessario(pedido, option);
	}

	protected void createImagePrevContainer() throws SQLException {
		if (!isEditing() && inVendaUnitariaMode) {
			imTelaAnteriorSlide = null;
		} else {
			super.createImagePrevContainer();
		}
	}

	public void reloadTabPrecoOnBackToList() throws SQLException {
		if (inVendaUnitariaMode && LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
			reloadComboTabelaPreco(pedido);
			cbTabelaPreco.setValue(ultimaTabelaPrecoSelecionada);
			SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
		}
		if (LavenderePdaConfig.retornaTabPrecoParaPadrao && !pedido.isPedidoBonificacao()) {
			retornaTabPrecoParaPadraoCliente();
		} else if (LavenderePdaConfig.mantemTabPrecoSelecionadaListaProduto) {
			retornaTabPrecoParaUltimaSelecionadaLista();
		}
	}

	private void retornaTabPrecoParaPadraoCliente() throws SQLException {
		Cliente cliente = pedido.getCliente();
		if ((cliente != null) && !ValueUtil.isEmpty(cliente.cdTabelaPreco)) {
			cbTabelaPreco.setValue(cliente.cdTabelaPreco);
			tabelaPrecoChange();
		}
	}

	private void retornaTabPrecoParaUltimaSelecionadaLista() throws SQLException {
		cbTabelaPreco.setValue(ultimaTabelaPrecoSelecionadaListaItens);
		tabelaPrecoChange();

	}

	public void clearProdutoVendaAtual() throws SQLException {
		getItemPedidoService().clearDadosItemPedido(getItemPedido());
		getItemPedido().setProduto(null);
		produtoSelecionado = null;
	}

	protected void addBarButtons() {
		UiUtil.add(barBottomContainer, btSalvar, 5);
		UiUtil.add(barBottomContainer, btExcluir, 1);
	}

	@Override
	protected void onFormStart() throws SQLException {
		if (!LavenderePdaConfig.usaInsercaoItensDiferentesLeituraCodigoBarras && !pedido.isSugestaoPedidoGiroProduto()) {
			if (!LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido) {
				UiUtil.add(barBottomContainer, btListaItens, 2);
			} else {
				if (!isEditing()) {
					btTrocarEmpresaRep = new ButtonAction(ValueUtil.VALOR_NI, "images/trocaEmpRep.png");
					btTrocarEmpresaRep.setText(getDsEmpresa());
					UiUtil.add(barBottomContainer, btTrocarEmpresaRep, 1);
					UiUtil.add(barBottomContainer, btListaItens, 4);
				} else {
					UiUtil.add(barBottomContainer, btListaItens, 2);
				}
			}
		}
		if (!pedido.isSugestaoPedidoGiroProduto() || LavenderePdaConfig.usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto) {
			UiUtil.add(barBottomContainer, btOpcoes, 3);
		}
		if (renderBotaoCalcular()) {
			UiUtil.add(barBottomContainer, btCalcular, 4);
		}

		adicionaBtSugestaoAoBarBottomContainer();
		if (renderBotaoInfosComplementares()) {
			 UiUtil.add(barBottomContainer, btInfoComplementares, 2);
		}
		UiUtil.add(barBottomContainer, btSalvarNovo, 5);
		if (usaCameraParaLeituraCdBarras()) {
			if (isUtilizaBtLeitorCameraPos5()) {
				UiUtil.add(barBottomContainer, btLeitorCamera, 5);
			} else {
				UiUtil.add(barBottomContainer, btLeitorCamera, 2);
			}
		}
		UiUtil.add(barTopContainer, btNextItem, RIGHT, CENTER, PREFERRED, lbItemByItem.fm.height);
		UiUtil.add(barTopContainer, lbItemByItem, BEFORE, CENTER);
		UiUtil.add(barTopContainer, btPreviousItem, BEFORE, CENTER, PREFERRED, lbItemByItem.fm.height);
		int yAtual = barTopContainer.getY2();
		if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido && LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
			addCbCondComercial(yAtual);
			yAtual = AFTER + HEIGHT_GAP;
		}
		if (isComboTabPrecoVisible() && !LavenderePdaConfig.usaSelecaoPorGrid()) {
			addCbTabPreco(yAtual);
			yAtual = AFTER + HEIGHT_GAP;
		}
		if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido && !LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial && isComboTabPrecoVisible()) {
			addCbCondComercial(yAtual);
			yAtual = AFTER + HEIGHT_GAP;
		}
		UiUtil.add(this, containerGrid, LEFT, yAtual, FILL, FILL - barBottomContainer.getHeight());
	}

	private void adicionaBtSugestaoAoBarBottomContainer() throws SQLException {
		if ((LavenderePdaConfig.usaMultiplasSugestoesProdutoInicioPedido() || LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido()) && !pedido.isPedidoBonificacao() && !pedido.isPedidoTroca()) {
			int pos = 4;
			if (renderBotaoCalcular() && !renderBotaoInfosComplementares() && LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido) {
				if (inVendaUnitariaMode) {
					pos = 2;
				} else if (!isUtilizaBtLeitorCameraPos5()) {
					pos = 5;
				} else {
					return;
				}
			} else if (renderBotaoCalcular() && renderBotaoInfosComplementares()) {
				pos = 1;
			}
			UiUtil.add(barBottomContainer, btSugestao, pos);
		}
	}

	protected boolean isUtilizaBtLeitorCameraPos5() throws SQLException {
		return usaCameraParaLeituraCdBarras() && !inVendaUnitariaMode && !LavenderePdaConfig.usaInsercaoItensDiferentesLeituraCodigoBarras && !permiteInserirMultiplosItensPorVezComInterfaceNegociacao();
	}

	protected boolean renderBotaoCalcular() {
		return Session.isModoSuporte || LavenderePdaConfig.usaTecladoFixoTelaItemPedido || !LavenderePdaConfig.usaTecladoVirtual;
	}

	protected boolean renderBotaoInfosComplementares() throws SQLException {
		return (LavenderePdaConfig.calculaPrecoPorVolumeProduto && LavenderePdaConfig.usaInfoComplementarItemPedido()
					&& getItemPedido() != null && getItemPedido().getProduto() != null
					&& getItemPedido().getProduto().isApresentaInfoComplCalculoPrecoPorVolumePorProduto())
				|| (LavenderePdaConfig.usaInfoComplementarItemPedido() && !LavenderePdaConfig.calculaPrecoPorVolumeProduto);
	}

	@Override
	public boolean isEnabled() {
		if (pedido == null || !LavenderePdaConfig.isUsaSolicitacaoAutorizacao() || !isEditing()) return super.isEnabled();
		try {
			ItemPedido itemPedido = getItemPedidoAndPedidoInstance();
			return super.isEnabled()
					&& !itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoPendente(itemPedido, null)
					&& !itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, TipoSolicitacaoAutorizacaoEnum.BONIFICACAO)
					&& !itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, TipoSolicitacaoAutorizacaoEnum.VENDA_CRITICA)
					&& !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return super.isEnabled();
		}
	}

	public boolean isEnabledSolAutorizacaoSkipValidation() {
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			return super.isEnabled();
		} else {
			return isEnabled();
		}
	}


	@Override
	protected void visibleState() throws SQLException {
		super.visibleState();
		if (usaCameraParaLeituraCdBarras()) {
			btLeitorCamera.setVisible((!inVendaUnitariaMode || LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras) && isEnabled());
		}
		boolean editing = isEditing();
		boolean permiteInserirMultiplosItensPorVezNoPedido = pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && !pedido.flSugestao;
		lbItemByItem.setVisible(editing && !disabledBtNextPrev && !fromItemPedidoInseridoPedido);
		btPreviousItem.setVisible(editing && !disabledBtNextPrev && !fromItemPedidoInseridoPedido);
		btNextItem.setVisible(editing && !disabledBtNextPrev && !fromItemPedidoInseridoPedido);
		btSalvar.setVisible(editing && isEnabled() && !inCarrouselProdutoCliente);
		btSalvarNovo.setVisible(!editing && (inVendaUnitariaMode || (permiteInserirMultiplosItensPorVezNoPedido && LavenderePdaConfig.naoPermiteInserirQtdDescMultipla())));
		btExcluir.setVisible(editing && isEnabledSolAutorizacaoSkipValidation() && !isModoGrade());
		btCalcular.setVisible(isEnabled() && (inVendaUnitariaMode || (permiteInserirMultiplosItensPorVezComInterfaceNegociacao() && !pedido.flSugestao)));
		btListaItens.setVisible(!(isEnabled() && (inVendaUnitariaMode || (permiteInserirMultiplosItensPorVezComInterfaceNegociacao() && !pedido.flSugestao))) && !isEditing());
		btAtualizarEstoquePrevistoGeral.setVisible(!inVendaUnitariaMode);
		if (LavenderePdaConfig.permiteAlternarEmpresaDuranteCadastroPedido && btTrocarEmpresaRep != null) {
			btTrocarEmpresaRep.setVisible(!btExcluir.isVisible() && !inVendaUnitariaMode);
		}
		btSugestao.setVisible(isEnabled() && !inVendaUnitariaMode);
		btInfoComplementares.setVisible(inVendaUnitariaMode);
	}

	public void addGridAndFields() throws SQLException {
		if (!inVendaUnitariaMode) {
			if (permiteInserirMultiplosItensPorVezComInterfaceNegociacao()) {
				addEditsVenda(containerGrid, BOTTOM - HEIGHT_GAP);
				UiUtil.add(containerGrid, listContainer, LEFT, BEFORE - HEIGHT_GAP, FILL, btSep3.getY() - (edFiltro.getY2() + (HEIGHT_GAP * 2)));
			} else {
				UiUtil.add(containerGrid, listContainer, LEFT, edFiltro.getY2() + (HEIGHT_GAP * 2), FILL, FILL);
			}
		}
	}

	protected boolean permiteInserirMultiplosItensPorVezComInterfaceNegociacao() throws SQLException {
		return pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && !LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens();
	}

	public void addFields() throws SQLException {
		// DECRI??O DO PRODUTO
		UiUtil.add(containerVendaUnitaria, containerProduto, LEFT, TOP, FILL, UiUtil.getControlPreferredHeight() + 4);
		UiUtil.add(containerProduto, lbDsProduto, getLeft(), CENTER - 2, PREFERRED, PREFERRED);
		// TECLADO FIXO
		if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido) {
			int heightTeclado = UiUtil.getControlPreferredHeight();
			heightTeclado = LavenderePdaConfig.usaTamanhoDinamicoTecladoFixoTelaItemPedido ? heightTeclado * 4 : heightTeclado * 2;
			UiUtil.add(containerVendaUnitaria, containerTecladoNumerico, LEFT, BOTTOM, FILL + 4, heightTeclado);
			UiUtil.add(containerTecladoNumerico, numericPad, LEFT, CENTER, FILL + 4, heightTeclado);
			UiUtil.add(containerVendaUnitaria, containerVendaUnitariaScrollable, LEFT, containerProduto.getY2(), FILL, FILL - (containerVendaUnitaria.getHeight() - containerTecladoNumerico.getY()));
		} else {
			UiUtil.add(containerVendaUnitaria, containerVendaUnitariaScrollable, LEFT, containerProduto.getY2(), FILL, FILL);
		}
		setTecladoEnableInFields(!LavenderePdaConfig.usaTecladoFixoTelaItemPedido);
		// EDITS VENDA
		calculaEspacoEAdicionaEditsVenda();
		changeLabelEditVisibility();
		// INFOS DO PRODUTOS
		if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || isEditing() || fromProdutoPendenteGiroMultInsercao || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
			int spaceDisponivel = containerFields2.getY();
			int spaceMinDispponivel = heigthContainerIcons + getHeightCbItemGrade1() + getReservedHeightCbItemGrade2() + (LavenderePdaConfig.isExibeFotoTelaItemPedido() || LavenderePdaConfig.mostraPrecosPorPrazoMedioItemPedido || LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 2 ? UiUtil.getControlPreferredHeight() * 3 : 0) + (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0 && !LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil ? UiUtil.getControlPreferredHeight() * 2 : 0)
					+ (isShowFotoProduto() && LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && pedido.isPedidoVenda() ? UiUtil.getControlPreferredHeight() * 4 : 0)
					+ getHeightLbFichaTecnicaItemPedido()
					+ getHeightLbInfosPersonalizadasItemPedido();
			if (spaceDisponivel < spaceMinDispponivel) {
				spaceDisponivel = spaceMinDispponivel;
			}
			UiUtil.add(containerVendaUnitariaScrollable, containerInfosProduto, LEFT, TOP, FILL, spaceDisponivel);
			containerFields2.setRect(LEFT, AFTER, FILL, containerFields2.getHeight());
			// --
			addIconsProduto();
			// --
			if (!LavenderePdaConfig.isOcultaComboItemGrade()) {
				if (LavenderePdaConfig.isGradeProdutoModoLista()) {
					UiUtil.add(containerInfosProduto, lbItemGrade2, cbItemGrade2, getLeft(), BOTTOM - HEIGHT_GAP - UiUtil.getControlPreferredHeight());
					UiUtil.add(containerInfosProduto, lbItemGrade1, cbItemGrade1, getLeft(), BEFORE - HEIGHT_GAP - UiUtil.getControlPreferredHeight() - lbItemGrade2.getHeight());
				} else if ((LavenderePdaConfig.usaGradeProduto1() || isModoGrade()) && cbItemGrade1.isVisible()) {
					UiUtil.add(containerInfosProduto, lbItemGrade1, cbItemGrade1, getLeft(), BOTTOM - HEIGHT_GAP - UiUtil.getControlPreferredHeight());
				}
			}
			if (LavenderePdaConfig.apresentaDsFichaTecnicaCapaItemPedido && LavenderePdaConfig.usaGradeProduto5()) {
				int y = 0;
				if (((LavenderePdaConfig.isOcultaComboItemGrade() || !cbItemGrade1.isVisible()) && isModoGrade()) || !isModoGrade()) {
					y = BOTTOM - HEIGHT_GAP - edDsFichaTecnica.getHeight();
				} else {
					y = BEFORE - edDsFichaTecnica.getHeight() - lbItemGrade1.getHeight() - HEIGHT_GAP;
				}
				UiUtil.add(containerInfosProduto, lbDsFichaTecnica, edDsFichaTecnica, getLeft(), y);
			}
		}
		if (LavenderePdaConfig.isConfigApresentacaoInfosPersonalizadasCapaItemPedido()) {
			addInfosPersonalizadasCapaItemPedido();
		}
		if (this instanceof CadItemPedidoForm && LavenderePdaConfig.isExibeTotalizadoresMultiplaInsercao() && !inVendaUnitariaMode && !isEditing()) {
			UiUtil.add(containerGrid, containerTotalizadoresMultIns, LEFT, BOTTOM, FILL, UiUtil.getTotalizerHeight() / 2 * 3);
			UiUtil.add(containerTotalizadoresMultIns, sessaoTotalizadoresFiltroMultIns, LEFT, TOP, FILL, UiUtil.getTotalizerHeight() / 4 * 3);
			UiUtil.add(containerTotalizadoresMultIns, sessaoTotalizadoresMultIns, LEFT, AFTER, FILL, UiUtil.getTotalizerHeight() / 4 * 3);
			UiUtil.add(sessaoTotalizadoresMultIns, lvQtTotalItensInseridos, LEFT + UiUtil.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
			UiUtil.add(sessaoTotalizadoresFiltroMultIns, lvQtTotalItensInseridosLista, LEFT + UiUtil.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
			UiUtil.add(sessaoTotalizadoresMultIns, lvVlTotalPedido, RIGHT - UiUtil.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
			UiUtil.add(sessaoTotalizadoresFiltroMultIns, lvVlTotalPedidoLista, RIGHT - UiUtil.getTotalizerGap(), TOP, PREFERRED, PREFERRED);
		}
	}

	protected void calculaEspacoEAdicionaEditsVenda() {
		int nuEspacosOcupados = 0;
		for (int i = 1; i <= hashEdits.size(); i++) {
			Control ctr = (Control) hashEdits.get(i);
			nuEspacosOcupados += ValueUtil.getIntegerValue((String) ctr.appObj);
		}
		int qtLinhasFields = ValueUtil.getIntegerValueTruncated((nuEspacosOcupados / 3) + (nuEspacosOcupados % 3 == 0 ? 0 : 1));
		int tamContainerEdits = (UiUtil.getControlPreferredHeight() * qtLinhasFields)+ ((HEIGHT_GAP * qtLinhasFields + (HEIGHT_GAP * 2)));
		UiUtil.add(containerVendaUnitariaScrollable, containerFields2, LEFT, tamContainerEdits > containerVendaUnitariaScrollable.getHeight() ? TOP : BOTTOM, FILL, tamContainerEdits);
		addEditsVenda(containerFields2, BOTTOM - HEIGHT_GAP);
	}

	private void addCbTabPreco(int yAtual) {
		UiUtil.add(this, lbTabelaPrecoAbrev, cbTabelaPreco, getLeft(), yAtual + HEIGHT_GAP);
		UiUtil.add(this, edDsTabelaPreco, SAME, SAME);
	}

	private void addCbCondComercial(int yAtual) {
		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CONDICAOCOMERCIAL), cbCondicaoComercial, getLeft(), yAtual + HEIGHT_GAP);
	}

	protected int getHeightCbItemGrade1() {
		int spaceReserved = 0;
		if ((LavenderePdaConfig.usaGradeProduto1() || LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && isModoGrade())) && !LavenderePdaConfig.isOcultaComboItemGrade() && cbItemGrade1.size() > 0) {
			spaceReserved = (cbItemGrade1.getHeight() == 0 ? UiUtil.getControlPreferredHeight() : cbItemGrade1.getHeight()) + HEIGHT_GAP + UiUtil.getControlPreferredHeight();
		}
		return spaceReserved;
	}

	protected int getHeightLbFichaTecnicaItemPedido() {
		int spaceReserved = 0;
		if (LavenderePdaConfig.apresentaDsFichaTecnicaCapaItemPedido && LavenderePdaConfig.usaGradeProduto5()) {
			spaceReserved = (edDsFichaTecnica.getHeight() == 0 ? UiUtil.getControlPreferredHeight() : edDsFichaTecnica.getHeight()) + HEIGHT_GAP;
			spaceReserved += (LavenderePdaConfig.isOcultaComboItemGrade() || !cbItemGrade1.isVisible()) ? UiUtil.getControlPreferredHeight() : 0;
		}
		return spaceReserved;
	}

	protected int getHeightLbInfosPersonalizadasItemPedido() {
		int spaceReserved = 0;
		if (LavenderePdaConfig.isConfigApresentacaoInfosPersonalizadasCapaItemPedido()) {
			spaceReserved = (edDsInfosPersonalizadasItemPedido.getHeight() == 0 ? UiUtil.getControlPreferredHeight() : edDsInfosPersonalizadasItemPedido.getHeight()) + HEIGHT_GAP;
			spaceReserved += (LavenderePdaConfig.isOcultaComboItemGrade() || !cbItemGrade1.isVisible()) ? UiUtil.getControlPreferredHeight() : 0;
		}
		return spaceReserved;
	}

	protected boolean isItemPedidoGrade() {
		try {
			final ItemPedido itemPedido = getItemPedido();
			if (itemPedido != null) {
				final Produto produto = itemPedido.getProduto();

				if (produto != null) {
					return produto.isProdutoAgrupadorGrade();
				}
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return false;
		}
		return false;
	}

	protected boolean isModoGrade() {
		return isModoGrade(isEditing() || isAdding());
	}

	protected boolean isModoGrade(boolean isEditing) {
		if (!isEditing) {
			return LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade();
		} else {
			return LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && isItemPedidoGrade();
		}
	}

	protected boolean isFiltrandoAgrupadorGrade() {
		return filtrandoAgrupadorGrade && !fromListItemPedidoForm && !inCarrouselMode && !fromRelProdutosPendentes && !fromRelGiroProduto;
	}

	protected boolean openGridEditModeIfNecessary(boolean fromWindowProdutoSimilar) {
		if (LavenderePdaConfig.usaAcessoDiretoGradeProdutosItemPedido && btGradeItemPedido.isEnabled() && !fromWindowProdutoSimilar) {
			ControlEvent event = new ControlEvent(ControlEvent.PRESSED, btGradeItemPedido);
			postEvent(event);
			return true;
		}
		return false;
	}

	protected int getReservedHeightCbItemGrade2() {
		int spaceReserved = 0;
		if (LavenderePdaConfig.isGradeProdutoModoLista() && !LavenderePdaConfig.isOcultaComboItemGrade() && cbItemGrade2.size() > 1) {
			spaceReserved = cbItemGrade1.getHeight() + HEIGHT_GAP + UiUtil.getControlPreferredHeight() + cbItemGrade2.getHeight() + HEIGHT_GAP + UiUtil.getControlPreferredHeight();
		}
		return spaceReserved;
	}

	protected void changeLbDsProdutoValue(String value) {
		try {
			lbDsProduto.setValue(value);
			tipDsProduto.setText(MessageUtil.quebraLinhas(value));
			lbDsProduto.setRect(lbDsProduto.getX(), lbDsProduto.getY(), lbDsProduto.getPreferredWidth(), lbDsProduto.getHeight());
			containerProduto.resize();
			containerProduto.scrollToPage(0);
		} catch (Throwable ex) {
			// N?o faz nada. Met?do refreshDomainToScreen foi chamado para tela de InfoExtraItemPedidoWindow
		}
	}

	@Override
	public void reposition() {
		try {
			containerVendaUnitariaScrollable.resetPreviousControl();
			super.reposition();
			repaintFilter = false;
			addComponentsInScreen();
			repaintFilter = true;
			// --
			containerGrid.rectOrg = containerGrid.getRect();
			listContainer.rectOrg = listContainer.getRect();
			if (listMaximized) {
				resizeListToFullScreen();
			} else if (listContainer.usaScroolHorizontal) {
				listContainer.setRect(listContainer.rectOrg);
				listContainer.initUI();
				listContainer.repaintContainers();
			}
			// --
			if (inVendaUnitariaMode) {
				containerVendaUnitariaScrollable.repaintNow();
				containerVendaUnitariaScrollable.scrollToPage(1);
				containerVendaUnitariaScrollable.scrollToPage(1000);
			}
			removeAndRepositeImageCarouselIfNecessary();
		} catch (Throwable ee) {
			ExceptionUtil.handle(ee);
		}
	}

	protected void removeAndRepositeImageCarouselIfNecessary() throws SQLException {
		if (imageCarrousel == null || !isModoPaisagem()) return;

		int heightImageFotoProduto = imageCarrousel.getHeight();
		if (imageCarouselSlider != null && isShowFotoProdutoSlider()) {
			containerInfosProduto.remove(imageCarrousel);
			imageCarouselSlider.setRect(KEEP, containerIconsProduto.getY2(), FILL, heightImageFotoProduto + imageCarouselSlider.getHeight());
			imageCarouselSlider.initUI();
		}
	}

	public void addEditsVenda(Container container, int yBottom) {
		addEditsVenda(container, yBottom, Settings.screenWidth);
	}

	public void addEditsVenda(Container container, int yBottom, int screenWidth) {
		int widthCol = (screenWidth / 3) - 1;
		int widthLabelInCol = widthCol / 3;
		int xEdits = LEFT;
		int controlPos = 1;
		for (int i = 1; i <= hashEdits.size(); i++) {
			if ((controlPos == 1) || (((controlPos - 1) % 3) == 0)) {
				UiUtil.add(container, (Control) hashLabels.get(i), xEdits, yBottom, widthLabelInCol, UiUtil.getControlPreferredHeight());
				if (((controlPos - 1) % 3) == 0) {
					yBottom = BEFORE - HEIGHT_GAP;
				}
			} else {
				UiUtil.add(container, (Control) hashLabels.get(i), AFTER, SAME, widthLabelInCol, UiUtil.getControlPreferredHeight());
			}
			Control ctr = (Control) hashEdits.get(i);
			int nuColunas = ValueUtil.getIntegerValue((String) ctr.appObj);
			int widthEdit = widthCol - widthLabelInCol - WIDTH_GAP;
			if (((nuColunas == 3) && ((controlPos == 1) || (((controlPos - 1) % 3) == 0))) || ((nuColunas == 2) && ((controlPos % 3) != 0))) {
				widthEdit = ((widthCol * nuColunas) - widthLabelInCol - (WIDTH_GAP * nuColunas)) + WIDTH_GAP;
				controlPos += nuColunas;
				if (nuColunas == 2) {
					widthEdit -= WIDTH_GAP / 2;
				} else {
					widthEdit += WIDTH_GAP / 2;
				}
			} else {
				controlPos += 1;
			}
			addComponenteVenda(container, ctr, widthEdit);
		}
		UiUtil.addSeparator(container, btSep3, BEFORE - HEIGHT_GAP);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btListaItens) {
					btListaItensClick();
				} else if (event.target == btCalcular) {
					fromBtCalcularClick = true;
					calcularClick();
					fromBtCalcularClick = false;
				} else if (event.target == btPreviousItem) {
					btPreviousItemClick();
				} else if (event.target == btNextItem) {
					btNextItemClick();
				} else if (event.target == btGradeItemPedido) {
					btGradeItemPedidoClick();
				} else if (event.target == btTrocarEmpresaRep) {
					btTrocarEmpresaClick();
				} else if (event.target == cbTabelaPreco) {
					cbTabelaPrecoClick();
				} else if (event.target == cbUnidadeAlternativa) {
					try {
						if (validaTrocarCbUnidade(getItemPedido(), cbUnidadeAlternativa.getValue())) {
							changeUnidadeAlternativa(cbUnidadeAlternativa.getValue());
							setFocusInQtde();
						} else {
							cbUnidadeAlternativa.setValue(getItemPedido().cdUnidade);
						}
					} catch (Throwable e) {
						cbUnidadeAlternativa.setValue(getItemPedido().cdUnidade);
						throw e;
					}
				} else if (event.target == cbUnidade) {
					pedido.cdUnidade = getItemPedido().cdUnidade = cbUnidade.getValue();
					btFiltrarClick();
				} else if (event.target == btLeitorCamera) {
					realizaLeituraCamera();
				} else if (event.target == cbItemGrade1) {
					cbItemGrade1Click();
				} else if (event.target == cbItemGrade2) {
					cbItemGrade2Click();
				} else if (event.target == lvEstoque.btAcao) {
					atualizadoInfoEstoque();
				} else if (event.target == lvEstoque.btGradeEstoque) {
					btEstoqueGradeClick();
				} else if (event.target == lvTotalPedido.btGradeTotaisPorItemPedido) {
					btTotaisPorItemPedidoClick();
				} else if (event.target == lvVlItemPedido.btGradeTotaisPorItemPedido) {
					btPrecosGradeClick();
				} else if (event.target == cbCondicaoComercial) {
					cbCondicaoComercialChange();
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
						if (edBaseNumpad == edQtItemFisico || edBaseNumpad == edQtItemFaturamento) {
							edQtItemFisicoFaturamentoKeyPress();
						}
					}
					numericPad.setSelectedIndex(-1);
				} else if (event.target == btIconeFoto) {
					showFotosProdutoWindow();
				} else if (event.target == btInfoComplementares) {
					btInfoComplementaresClick();
				} else if (event.target == btIconeDetalheProduto) {
					detalhesClick();
				} else if (event.target == btAgrupadorGrade) {
					btAgrupadorGradeClick();
				} else if (event.target == btIconeVideos) {
					btIconeVideosClick();
				} else if (event.target == btIconeCarrosselPopup) {
					btIconePopupCarrouselClick();
				} else if (event.target == btOcultaMostraEditLabels) {
					btOcultaMostraEditLabelsClick();
				}
				break;
			}
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					btFiltrarClick();
				}
				break;
			}
			case ResizeListEvent.RESIZE_PRESS: {
				if ((listContainer != null) && (event.target == listContainer.btResize)) {
					int index = listContainer.getSelectedIndex();
					if (listMaximized) {
						containerGrid.setRect(containerGrid.rectOrg);
						if (containerTotalizadoresMultIns.isDisplayed()) {
							containerTotalizadoresMultIns.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadoresFiltroMultIns.getHeight() + sessaoTotalizadoresMultIns.getHeight());
						}
						containerGrid.initUI();
						listContainer.setRect(listContainer.rectOrg);
						listContainer.initUI();
						listContainer.repaintContainers();
						if (LavenderePdaConfig.usaCategoriaInsercaoItem()) {
							adjustContainerHeightForMenuCategoria();
						}
					} else {
						resizeListToFullScreen();
					}
					listContainer.setSelectedIndex(index);
					listMaximized = !listMaximized;
				}
				break;
			}
			case ControlEvent.WINDOW_CLOSED: {
				if ((listContainer != null) && (event.target == listContainer.popupMenuOrdenacao)) {
					if (listContainer.popupMenuOrdenacao.getSelectedIndex() != -1) {
						listContainer.reloadSortSettings();
						saveListConfig();
						carregaGridProdutos();
					}
				}
				break;
			}
			case ControlEvent.FOCUS_IN: {
				if (event.target instanceof BaseEdit && ((BaseEdit) event.target).isEditable()) {
					edBaseNumpad = (BaseEdit) event.target;
				} else {
					edBaseNumpad = new EditText("", 0);
				}
				break;
			}
			case ControlEvent.FOCUS_OUT: {
				if (!LavenderePdaConfig.usaTecladoVirtual || LavenderePdaConfig.usaTecladoFixoTelaItemPedido) {
					edBaseNumpad = new EditText("", 0);
				}
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if ((event.target == edVlPctDesconto) && edVlPctDesconto.isEditable()) {
					if (isPossuiDescExtraAplicVerba()) {
						if (UiUtil.showWarnConfirmYesNoMessage(Messages.MSG_AVISO_ALTERA_DESC_COM_DESC_EXTRA)) {
							getItemPedido().vlPctDesconto2 = 0d;
							edVlPctDesconto2.setValue(0d);
							aplicaDesconto();
						} else {
							edVlPctDesconto.setValue(getItemPedido().vlPctDesconto);
						}
					} else {
						aplicaDesconto();
					}
				} else if (event.target == edVlPctDesconto2 && edVlPctDesconto2.isEditable()) {
					if (validaAlterarValorItem(getItemPedido(), edVlPctDesconto2, getItemPedido().vlPctDesconto2)) {
						edVlPctDescontoValueChange(getItemPedido());
						calcularClick();
					}
				} else if (event.target == edVlPctDesconto3 && edVlPctDesconto3.isEditable()) {
					if (validaAlterarValorItem(getItemPedido(), edVlPctDesconto3, getItemPedido().vlPctDesconto3)) {
						edVlPctDescontoValueChange(getItemPedido());
						calcularClick();
					}
				} else if ((event.target == edVlPctAcrescimo) && edVlPctAcrescimo.isEditable()) {
					aplicaAcrescimo();
				} else if ((event.target == edVlItemPedido) && edVlItemPedido.isEditable()) {
					if (validaAlterarValorItem(getItemPedido(), edVlItemPedido, getItemPedido().vlItemPedido)) {
						edVlItemPedidoValueChange();
						calcularClick();
					}
				} else if ((event.target == edVlItemStReverso) && edVlItemStReverso.isEditable()) {
					edVlItemSTReversoValueChange();
					calcularClick();
				} else if ((event.target == edVlPctDescontoStReverso) && edVlPctDescontoStReverso.isEditable()) {
					edVlPctDescontoStReversoValueChange();
					calcularClick();
				} else if (event.target == edQtDesejada && edQtDesejada.isEditable()) {
					edQtDesejadoValueChange();
					calcularClick();
				} else if (event.target == edQtItemFisico && edQtItemFisico.isEditable()) {
					double qtItemFisico = edQtItemFisico.getValueDouble();
					try {
						fromEdQtItemFisicoFocusOut = true;
						if (validaAlterarQtdItem(getItemPedido())) {
							if (getItemPedido().itemChanged) refreshDomainToScreen(getItemPedido());
							edQtItemFisico.setValue(qtItemFisico);
							edQtItemFisicoValueChange(getItemPedido());
							calcularClick();
						} else {
							edQtItemFisico.setValue(getItemPedido().getQtItemFisico());
						}
						fromEdQtItemFisicoFocusOut = false;
					} catch (Throwable e) {
						edQtItemFisico.setValue(getItemPedido().getQtItemFisico());
						throw e;
					}
				} else if (event.target == edVlFrete && edVlFrete.isEditable()) {
					calcularClick();
				} else if ((event.target == edQtItemFaturamento) && edQtItemFaturamento.isEditable()) {
					edQtItemFaturamentoValueChange();
					calcularClick();
				} else if ((event.target == edPrecoEmbPrimaria) && edPrecoEmbPrimaria.isEditable()) {
					edPrecoEmbPrimariaValueChange();
					calcularClick();
				} else if ((event.target == edVlItemIpi) && edVlItemIpi.isEditable()) {
					edVlItemIpiValueChange();
				} else if ((event.target == edVlUnidadeEmbalagem) && edVlUnidadeEmbalagem.isEditable()) {
					edVlUnidadeEmbalagemValueChange();
				} else if ((event.target == edVlAgregadoSugerido) && edVlAgregadoSugerido.isEditable()) {
					edVlAgregadoSugeridoValueChange();
				} else if ((event.target == edPctMargemAgregada) && edPctMargemAgregada.isEditable()) {
					edPctMargemAgregadoValueChange();
				} else if ((event.target == edVlPctDescCondPagto) && edVlPctDescCondPagto.isEditable()) {
					edVlPctDescCondPagtoValueChange();
				} else if (event.target == edNuOrdemCompraCliente) {
					edNuOrdemCompraValueChange();
				} else if (event.target == edNuSeqOrdemCompraCliente) {
					edNuSeqOrdemCompraValueChange();
				}
				if (event.target instanceof Control && inVendaUnitariaMode) {
					((Control) event.target).getParent().requestFocus();
				}
				break;
			}
			case PenEvent.PEN_UP: {
				if ((event.target instanceof BaseListContainer.Item) && (listContainer.isEventoClickUnicoDisparado())) {
					if (!(pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens()) || LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao()) {
						listContainerProdutoClick();
						if (!fechouTelaDescontoQuantidade) {
							controlEstoque(getItemPedido());
						}
					}
				} else if (event.target instanceof ItemContainer && LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() && listContainer.isEventoClickUnicoDisparado()) {
					if (isInserindoItems()) {
						UiUtil.showInfoMessage(Messages.AGUARDE_PROCESSANDO_ITENS);
					} else {
						itemContainerProdutoClick((ItemContainer) event.target);
						listContainerProdutoClick();
						controlEstoque(getItemPedido());
					}
				} else if (event.target == edVlItemPedido) {
					vlItemPedidoOld = edVlItemPedido.getValueDouble();
				} else if (event.target == edVlItemIpi) {
					vlItemIpiOld = edVlItemIpi.getValueDouble();
				} else if (event.target == edPrecoEmbPrimaria) {
					vlEmbalagemElementarOld = edPrecoEmbPrimaria.getValueDouble();
				}
				break;
			}
			// TecladoF?sico
			case KeyEvent.KEY_PRESS: {
				if (event.target == edQtItemFisico || event.target == edQtItemFaturamento) {
					edQtItemFisicoFaturamentoKeyPress();
				}
				break;
			}
			// TecladoVirtual
			case KeyboardEvent.KEYBOARD_PRESS: {
				if (event.target == edFiltro) {
					filtrarClick();
				} else if (event.target == edQtItemFisico || event.target == edQtItemFaturamento) {
					setColorsEditsByConversaoUnidade(getItemPedido());
					if (LavenderePdaConfig.usaControlePontuacao) {
						setPontuacaoColor(getItemPedido());
					}
				} else if (isInsereMultiplosSemNegociacao() && event.target != null) {
					Control parent = ((Control) event.target).getParent();
					if ((parent instanceof ValueChooser || parent instanceof ItemContainer) && event.target instanceof EditNumberFrac) {
						parent.requestFocus();
					}
				}
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				executeEventSpecialKey(event);
				if (inVendaUnitariaMode) break;
				Control controlTarget = (Control)event.target;
				KeyEvent keyTarget = (KeyEvent)event;
				int indexListSelected = listContainer.getSelectedIndex();
				if (keyTarget.key == SpecialKeys.MENU && (controlTarget.getParent() instanceof ValueChooser || controlTarget.getParent() instanceof ItemContainer)) {
					edFiltro.requestFocus();
				} else if ((keyTarget.isDownKey() || keyTarget.isUpKey()) && indexListSelected != -1) {
					BaseListContainer.Item currentItem = (BaseListContainer.Item) listContainer.getContainer(indexListSelected);
					if (currentItem != null && currentItem.rightControl instanceof ItemContainer) {
						ItemContainer currentItemContainer = (ItemContainer)currentItem.rightControl;
						Control controlWithFocus = Window.getTopMost().getFocus();
						if (controlWithFocus instanceof EditNumberFrac) {
							currentItemContainer.requestFocusByAppId(controlWithFocus.appId);
						}
					}
				} else if (keyTarget.key == SpecialKeys.TAB) {
					if (edFiltro != null && listContainer != null && listContainer.size() > 0 && event.target == edFiltro && (LavenderePdaConfig.isInsereSomenteQtdMultipla() || LavenderePdaConfig.isInsereQtdDescMultipla())) {
						listContainer.setScrollPos(-listContainer.getScrollPos());
						listContainer.setSelectedIndex(0);
					}
				}
				break;
			}
			case GridEvent.SELECTED_EVENT: {
				if ((event.target == gridUnidadeAlternativa) && (gridUnidadeAlternativa.getSelectedIndex() != -1) && gridUnidadeAlternativa.getSelectedIndex() != cbUnidadeAlternativa.getSelectedIndex()) {
					gridUnidadeAlternativaChange();
				}
				break;
			}
			case GridEditEvent.COLUMN_PRESSED: {
				ordenaCbUnidadeAlternativaConformeGrid();
				break;
			}
			case HideDiscontEvent.HIDE_DISCONT_CLICK_EVENT: {
				btHideAttributesClick();
				break;
			}
			case ImageSelectionChangeEvent.IMAGE_SELECTION_EVENT: {
				imageCarouselProdutoClick();
				break;
			}
			case ScrollEvent.SCROLL_END: {
				if (usaLazyLoading && flListInicialized && listContainer.isEndScroll()) {
					listScroll();
				}
				break;
			}
			case OpenningLastPhotoListEvent.OPENNING_LAST_PHOTO_LIST_EVENT: {
	    		if (usaLazyLoading && flListInicialized && !listContainer.scrollFinished) {
					listScroll();
				}
				break;
	    	}
		}
	}

	protected void showFotosProdutoWindow() throws SQLException {
		boolean hasFoto = getItemPedido().getProduto().cdProduto != null && getItemPedido().hasFoto(LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade());
		if (hasFoto) {
			btIconeFotoClick();
		} else if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade() && getItemPedido().getProduto().isProdutoAgrupadorGrade()) {
			UiUtil.showInfoMessage(Messages.AGRUPADORGRADE_NAO_POSSUI_FOTOS);
		} else {
			UiUtil.showInfoMessage(Messages.PRODUTO_NAO_POSSUI_FOTOS);
		}
	}

	private void btIconeVideosClick() throws SQLException {
		Vector videoProdutoList = new Vector();
		Vector videoProdutoGradeList = new Vector();
		if (LavenderePdaConfig.isUsaConfigVideosProdutos()) {
			VideoProduto videoProdutoFilter = new VideoProduto();
			videoProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			if (isEditing()) {
				videoProdutoFilter.cdProduto = getItemPedido().cdProduto;
			} else {
				videoProdutoFilter.cdProduto = getProdutoSessaoVenda().cdProduto;
			}
			videoProdutoList = VideoProdutoService.getInstance().findAllByExample(videoProdutoFilter);
		}
		if (LavenderePdaConfig.isUsaConfigVideosProdutoAgrupadorGrade()) {
			VideoProdutoGrade videoProdutoGradeFilter = new VideoProdutoGrade();
			videoProdutoGradeFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			if (isEditing()) {
				videoProdutoGradeFilter.dsAgrupadorGrade = getItemPedido().getProduto().getDsAgrupadorGrade();
			} else {
				videoProdutoGradeFilter.dsAgrupadorGrade = getProdutoSessaoVenda().getDsAgrupadorGrade();
			}
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
		UiUtil.videoViewer(path);
	}

	private void imageCarouselProdutoClick() throws SQLException {
		if (imageCarouselSlider == null || ValueUtil.valueEquals(getItemPedido().getProduto().getRowKey(), imageCarouselSlider.getSelectedItemId())) {
			return;
		}
		ItemPedido itemPedido = getItemPedido();
		ItemPedido itemPedidoClone = (ItemPedido) itemPedido.clone();
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		try {
			addItemPedidoToListItemGrade(itemPedidoPorGradePrecoList, itemPedidoClone);
		} catch (DescAcresMaximoException e) {
			UiUtil.showErrorMessage(e);
			int index = itemPedidoPorGradePrecoList.indexOf(itemPedidoClone);
			if (index >= 0) {
				itemPedido.itemPedidoPorGradePrecoList.items[index] = getItemPedidoService().findByRowKey(itemPedidoClone.getRowKey());
			}
		}
		setProdutoInItemPedidoSessaoByRowKey(imageCarouselSlider.getSelectedItemId());
		loadDadosGradeProduto(getItemPedido());
		setDomainAfterItemPedidoPorPrecoGradeChange(getItemPedido(), itemPedido.itemPedidoPorGradePrecoList);
		calcularClick();
		getFotoByItemPedido(getItemPedido());
		changeLbDsProdutoValue(getItemPedido().getDsProdutoWithKey(getItemPedido()));
		setColorsEditsByConversaoUnidade(itemPedido);
		if (LavenderePdaConfig.usaControlePontuacao) {
			setPontuacaoColor(itemPedido);
		}
		refreshDomainToScreen(getItemPedido());
	}

	private void createItemPedidoNaoInseridoByItemPedidoReference(ItemPedido itemPedidoReference) throws SQLException {
		itemPedidoBaseNaoInserido = getItemPedidoService().createAndCalculateNewItemPedidoBase(itemPedidoReference, itemPedidoReference.getProduto(), itemPedidoReference.cdItemGrade1);
		itemPedidoBaseNaoInserido.cdItemGrade1 = itemPedidoReference.cdItemGrade1;
		itemPedidoBaseNaoInserido.nuSeqItemPedido = itemPedidoReference.nuSeqItemPedido;
		itemPedidoBaseNaoInserido.dsAgrupadorGradeFilter = itemPedidoReference.getProduto().getDsAgrupadorGrade();
		itemPedidoBaseNaoInserido.pedido = itemPedidoReference.pedido;
		itemPedidoBaseNaoInserido.itemPedidoPorGradePrecoList = itemPedidoReference.itemPedidoPorGradePrecoList;
	}

	private void setDomainAfterItemPedidoPorPrecoGradeChange(ItemPedido itemPedido, Vector itemPedidoPorGradePrecoList) throws SQLException {
		int index = getIndexItemGradeInItemPedidoList(itemPedido);
		if (index < 0) {
			createItemPedidoNaoInseridoByItemPedidoReference(itemPedido);
			setItemPedido(itemPedidoBaseNaoInserido);
		} else {
			Vector itemPedidoGradeList = itemPedido.itemPedidoPorGradePrecoList;
			ItemPedido itemPedidoFromList = (ItemPedido)itemPedidoPorGradePrecoList.items[index];
			itemPedidoFromList.dsAgrupadorGradeFilter = itemPedido.getProduto().getDsAgrupadorGrade();
			itemPedidoFromList.itemPedidoPorGradePrecoList = itemPedidoGradeList;
			itemPedidoFromList.pedido = itemPedido.pedido;
			setItemPedido(itemPedidoFromList);
		}
	}

	private int getIndexItemGradeInItemPedidoList(ItemPedido itemPedido) {
		int size = itemPedido.itemPedidoPorGradePrecoList.size();
		ProdutoGrade produtoGrade = new ProdutoGradeBuilder(itemPedido).build();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoFromList = (ItemPedido) itemPedido.itemPedidoPorGradePrecoList.items[i];
			if (ValueUtil.valueEquals(produtoGrade, new ProdutoGradeBuilder(itemPedidoFromList).build())) {
				return i;
			}
		}
		return -1;
	}

	protected void setProdutoInItemPedidoSessao(Produto produto) throws SQLException {
		setProdutoSelecionado(produto);
		ItemPedido itemPedido = getItemPedido();
		itemPedido.setProduto(produto);
		itemPedido.reloadItemTabelaPreco();
		ItemPedidoService.getInstance().setVlBaseItemPedidoFromItemTabelaPreco(itemPedido);
	}

	protected void setProdutoInItemPedidoSessaoByRowKey(String rowKey) throws SQLException {
		Produto produto = (Produto) ProdutoService.getInstance().findByRowKey(rowKey);
		setProdutoInItemPedidoSessao(produto);
	}

	private void btGradeItemPedidoClick() throws SQLException {
		UiUtil.showProcessingMessage();
		validaProdutoGrade1();
		Vector itemPedidoGradeList = new Vector(getItemPedido().itemPedidoGradeList.size());
		itemPedidoGradeList.addElementsNotNull(getItemPedido().itemPedidoGradeList.items);
		ItemPedido itemBackup = (ItemPedido) getItemPedido().clone();
		itemBackup.itemPedidoGradeList = itemPedidoGradeList;
		if (cbItemGrade2.getValue() != null) {
			getItemPedido().cdItemGrade2 = cbItemGrade2.getValue();
			getItemPedido().itemGrade1 = (ItemGrade) cbItemGrade1.getSelectedItem();
			getItemPedido().itemGrade2 = (ItemGrade) cbItemGrade2.getSelectedItem();
		}
		CadItemPedidoGradeWindow cadItemPedidoGradeWindow = new CadItemPedidoGradeWindow(getItemPedido(), isEnabled(), this);
		UiUtil.unpopProcessingMessage();
		cadItemPedidoGradeWindow.popup();
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			setDomainAfterItemPedidoPorPrecoGradeChange(getItemPedido(), getItemPedido().itemPedidoPorGradePrecoList);
			calcularClick();
		}
		if (cadItemPedidoGradeWindow.windowClosed) {
			itemBackup.descPromocionalGradeList = getItemPedido().descPromocionalGradeList;
			setDomain(itemBackup);
		} else if (LavenderePdaConfig.isGradeProdutoModoLista()) {
			edQtItemFisico.setEditable(ValueUtil.isEmpty(getItemPedido().itemPedidoGradeList));
			edQtItemFaturamento.setEditable(ValueUtil.isEmpty(getItemPedido().itemPedidoGradeList));
		}
		if (getItemPedido().pedido.isPedidoAberto()) {
			updateQtItens(getItemPedido());
			getItemPedido().flEditandoQtItemFaturamento = false;
			if (!cadItemPedidoGradeWindow.windowClosed) {
				if (LavenderePdaConfig.usaGradeProduto4()) {
					calcularClick(getItemPedido().itemPedidoPorGradePrecoList);
				} else {
					refreshDomainToScreen(getItemPedido());
				}
			}
		}
	}

	private void btHideAttributesClick() throws SQLException {
		persisteTrocaVisibilidadeDosCampos();
		mudaVisibilidadeAttrMultInserOuAgrupadorGrade();
		isFirstHideAttributesClick = false;
	}

	protected void mudaVisibilidadeAttrMultInserOuAgrupadorGrade() throws SQLException {
		boolean changedVisibility = mudaVisibilidadeAtributosContainer();
		if (!changedVisibility && LavenderePdaConfig.isPermiteOcultarValoresItemAgrupadorGrade() || isFirstHideAttributesClick && !hideAttributes) {
			listarMantendoScroll(listContainer);
		}
		hideAttributesOld = hideAttributes;
	}

	protected void persisteTrocaVisibilidadeDosCampos() throws SQLException {
		hideAttributes = !hideAttributes;
		hideAttributesVendaUnitaria = !hideAttributesVendaUnitaria;
		ConfigInternoService.getInstance().addValue(ConfigInterno.ULTIMACONFIGURACAOBOTAOOCULTAR, StringUtil.getStringValue(hideAttributes));
		String iconName = hideAttributesVendaUnitaria ? "show.png" : "hide.png";
		if (btOcultaMostraEditLabels != null) {
			btOcultaMostraEditLabels.setImage(getIcon(iconName, true));
		}
	}

	
	private void btOcultaMostraEditLabelsClick() throws SQLException {
		persisteTrocaVisibilidadeDosCampos();
		changeLabelEditVisibility();
	}
	
	private boolean mudaVisibilidadeAtributosContainer() {
		int size = listContainer.size();
		boolean changedVisibility = false;
		for (int i = 0; i < size; i++) {
			BaseListContainer.Item item = (BaseListContainer.Item) listContainer.getContainer(i);
			if (item != null && item.rightControl instanceof ItemContainer) {
				ItemContainer itemContainer = (ItemContainer)item.rightControl;
				setItemContainerAttrVisible(itemContainer);
				changedVisibility = true;
			}
		}
		return changedVisibility;
	}

	private void setItemContainerAttrVisible(ItemContainer itemContainer) {
		if (LavenderePdaConfig.isPermiteOcultarDescontoAcrescimoMultiplaInsercao()) {
			itemContainer.setVisibleDescAcrescChooser(!hideAttributes);
		}
		if (LavenderePdaConfig.isPermiteOcultarValoresEstoqueItemMultiplaInsercao()) {
			itemContainer.setVisibleLbEstoque(!hideAttributes);
		}
		if (LavenderePdaConfig.isPermiteOcultarValoresTotaisItemMultiplaInsercao()) {
			itemContainer.setVisibleLbsVlItemPedido(!hideAttributes);
		}
	}

	protected abstract void adjustContainerHeightForMenuCategoria();

	private void btInfoComplementaresClick() throws SQLException {
		(new CadInfoComplementarItemPedidoWindow(getItemPedido(), pedido.isPedidoAberto())).popup();
		if (LavenderePdaConfig.isCalculaPrecoPorMetroQuadradoUnidadeProduto() || LavenderePdaConfig.calculaPrecoPorVolumeProduto) {
			calcularItemEspecifico(getItemPedido(), getItemPedido());
			refreshDomainToScreen(getItemPedido());
		}
	}

	private void edVlPctDescCondPagtoValueChange() throws SQLException {
		CondicaoPagamento condPag = pedido.getCondicaoPagamento();
		if (!LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento || condPag == null || (condPag != null && condPag.vlPctMaxDesconto == 0)) return;
		double descontoInformado = edVlPctDescCondPagto.getValueDouble();
		validaDescontoMaximoCondPagUI(condPag.vlPctMaxDesconto, descontoInformado, getItemPedido().vlPctDescCondPagto);
		getItemPedido().vlPctDescCondPagto = descontoInformado;
		getItemPedido().flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
		calcularClick();
	}

	private void validaDescontoMaximoCondPagUI(double vlPctMaxDesconto, double value, double oldValue) throws SQLException {
		if (value <= vlPctMaxDesconto) return;
		edVlPctDescCondPagto.setValue(oldValue);
		throw new ValidationException(MessageUtil.getMessage(Messages.ERRO_DESCONTO_MAXIMO_COND_PAGTO_ULTRAPASSADO, new String[] {StringUtil.getStringValue(value), StringUtil.getStringValue(vlPctMaxDesconto)}));
	}

	private boolean isPossuiDescExtraAplicVerba() throws SQLException {
		return LavenderePdaConfig.usaDescontoExtra && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && LavenderePdaConfig.usaVlBaseVerbaEDescMaximoPorRedutorCliente && getItemPedido().vlPctDesconto2 > 0;
	}

	private void ordenaCbUnidadeAlternativaConformeGrid() throws SQLException {
		//Fun??o para manter a combo ordenada corretamente conforme a grid de unidade alternativa.
		//Evitando divergencias de valores nas mesmas;
		if (LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto != 2 || gridUnidadeAlternativa == null ) return;
		Object[] cbItens = (Object[]) cbUnidadeAlternativa.getItems();
		Vector gridItens = gridUnidadeAlternativa.getItemsVector();
		Object[] ordenada = new Object[cbItens.length];
		for (int i = 0; i < gridItens.size(); i++) {
			Object[] unGrid = (Object[]) gridItens.items[i];
			for (Object item : cbItens) {
				ProdutoUnidade prod = (ProdutoUnidade)item;
				if (!(prod.cdUnidade.equals(unGrid[0]) && prod.unidade.dsUnidade.equals(unGrid[1]))) continue;
				ordenada[i] = item;
				break;
			}
		}
		cbUnidadeAlternativa.removeAll();
		cbUnidadeAlternativa.add(ordenada);
		selectUnidadeNaGrid(getItemPedido().cdUnidade);
		cbUnidadeAlternativa.setSelectedIndex(gridUnidadeAlternativa.getSelectedIndex());
	}

	private void gridUnidadeAlternativaChange() throws SQLException {
		cbUnidadeAlternativa.setValue(getSelectedUnidadeGrid());
		try {
			if (validaTrocarCbUnidade(getItemPedido(), cbUnidadeAlternativa.getValue())) {
				changeUnidadeAlternativa(getSelectedUnidadeGrid());
				if (LavenderePdaConfig.usaRegistroProdutoFaltante && LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
					controlEstoque(getItemPedido());
					double estoqueByUn =  EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(getItemPedido().getItemTabelaPreco(), getItemPedido().getProdutoUnidade(), getItemPedido().estoque.qtEstoque);
					if (estoqueByUn == 0 && (ValueUtil.valueEqualsIfNotNull(getItemPedido().getProduto().flPermiteEstoqueNegativo, ValueUtil.VALOR_NAO) || ValueUtil.isEmpty(getItemPedido().getProduto().flPermiteEstoqueNegativo))) {
						CadProdutoFaltaWindow cadProdutoFaltaWindow = new CadProdutoFaltaWindow(getItemPedido(), null, false, isEditing());
						cadProdutoFaltaWindow.popup();
						if (cadProdutoFaltaWindow.closedByBtFechar) {
							voltarClick();
							getItemPedido().fromProdutoFaltaWindow = false;
							return;
						}
					}
				}
				setFocusInQtde();
			} else {
				cbUnidadeAlternativa.setValue(getItemPedido().cdUnidade);
				gridUnidadeAlternativa.setSelectedIndex(cbUnidadeAlternativa.getSelectedIndex());
			}
		} catch (ValidationException e) {
			cbUnidadeAlternativa.setValue(getItemPedido().cdUnidade);
			gridUnidadeAlternativa.setSelectedIndex(cbUnidadeAlternativa.getSelectedIndex());
			throw e;
		}
	}

	protected void validaProdutoGrade1() throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto4() && !isEditing()) {
			double qtd = ItemPedidoService.getInstance().countItensPedido(getItemPedido());
			if (qtd > 0) {
				throw new ValidationException(Messages.PRODUTO_GRADE1_JA_INSERIDO);
			}
		}
	}

	private void btTotaisPorItemPedidoClick() throws SQLException {
		ListItemPedidoForm listItemPedidoForm = (ListItemPedidoForm) getBaseCrudListForm();
		ListTotalItemPedidoWindow listTotalItemPedidoWindow = new ListTotalItemPedidoWindow(listItemPedidoForm, getItemPedido().itemPedidoPorGradePrecoList);
		listTotalItemPedidoWindow.popup();
	}
	private void btPrecosGradeClick() throws SQLException {
		UiUtil.showProcessingMessage();
		CadItemPedidoGradeWindow cadItemPedidoGradeWindow = new CadItemPedidoGradeWindow(getItemPedido(), false, this);
		cadItemPedidoGradeWindow.btPrecosClick(false, true);
		if (cadItemPedidoGradeWindow.windowClosed) {
			ItemPedidoService.getInstance().carregaItensGradePreco(getItemPedido());
		}
		if (LavenderePdaConfig.usaGradeProduto5()) calcularClick();
	}

	private void aplicaDesconto() throws SQLException {
		if (isAddingFromArray) return;
		ItemPedido itemPedido = getItemPedido();
		if (LavenderePdaConfig.usaGradeProduto4() && ValueUtil.isNotEmpty(itemPedido.itemPedidoPorGradePrecoList)) {
			aplicaDescontoPorGradePreco(itemPedido);
			return;
		}
		if (aplicaDesconto(itemPedido)) refreshDomainToScreen(itemPedido);
		validaExibicaoVlItemPedido();
	}

	protected void validaExibicaoVlItemPedido() throws SQLException {
		if (!LavenderePdaConfig.usaGradeProduto4() || !LavenderePdaConfig.usaGradeProduto5()) return;
		if (lvVlItemPedido.btGradeTotaisPorItemPedido.isEnabled()) {
			lvVlItemPedido.setText("");
		} else {
			lvVlItemPedido.setValue(getItemPedido().vlItemPedido);
		}
	}

	private void aplicaAcrescimo() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (LavenderePdaConfig.usaGradeProduto4() && ValueUtil.isNotEmpty(itemPedido.itemPedidoPorGradePrecoList)) {
			aplicaAcrescimoPorGradePreco(itemPedido);
			return;
		}
		if (aplicaAcrescimo(itemPedido)) refreshDomainToScreen(itemPedido);
		validaExibicaoVlItemPedido();
	}

	private void aplicaDescontoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		int size = itemPedido.itemPedidoPorGradePrecoList.size();
		itemPedido.vlPctDesconto = edVlPctDesconto.getValueDouble();
		itemPedido.vlPctAcrescimo = 0;
		itemPedido.vlTotalItemPedido = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
			aplicaDesconto(itemPedidoPorGradePreco);
			itemPedido.vlTotalItemPedido += itemPedidoPorGradePreco.vlTotalItemPedido;
		}
			refreshDomainToScreen(itemPedido);
		}

	private void aplicaAcrescimoPorGradePreco(ItemPedido itemPedido) throws SQLException {
		Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
		int size = itemPedido.itemPedidoPorGradePrecoList.size();
		itemPedido.vlPctAcrescimo = edVlPctAcrescimo.getValueDouble();
		itemPedido.vlPctDesconto = 0;
		itemPedido.vlTotalItemPedido = 0;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedidoPorGradePreco = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
			aplicaAcrescimo(itemPedidoPorGradePreco);
			itemPedido.vlTotalItemPedido += itemPedidoPorGradePreco.vlTotalItemPedido;
		}
			refreshDomainToScreen(itemPedido);
		}

	private boolean aplicaDesconto(ItemPedido itemPedido) throws SQLException {
		if (validaAlterarValorItem(itemPedido, edVlPctDesconto, itemPedido.vlPctDesconto)) {
			edVlPctDescontoValueChange(itemPedido);
			return calcularClick(false, itemPedido);
		}
		return false;
	}

	private boolean aplicaAcrescimo(ItemPedido itemPedido) throws SQLException {
		if (validaAlterarValorItem(itemPedido, edVlPctAcrescimo, itemPedido.vlPctAcrescimo)) {
			edVlPctAcrescimoValueChange(itemPedido);
		}
		return calcularClick(false, itemPedido);
	}

	protected int getNextItemIndex(int currentIndex) {
		return currentIndex + 1;
	}

	protected int getPreviousItemIndex(int currentIndex) {
		return currentIndex - 1;
	}

	protected void btPreviousItemClick() throws SQLException {
		if (inCarrouselMode) {
			int index = getPreviousItemIndex(selectedItemPedidoCarrousel);
			if (index >= 0) {
				itemPedidoCarrouselList.get(selectedItemPedidoCarrousel).inCarrousel = false;
				selectedItemPedidoCarrousel--;
				itemPedidoCarrouselList.get(selectedItemPedidoCarrousel).inCarrousel = true;
				edit((ItemPedido)itemPedidoCarrouselList.get(index).clone());
			}
			controlaVisibilidadeBotoesPreviousNext();
		} else if (getBaseCrudListForm() != null && getBaseCrudListForm().getListContainer() != null) {
			GridListContainer gridListContainer = getBaseCrudListForm().getListContainer();
			int index = getPreviousItemIndex(gridListContainer.getSelectedIndex());
			if (index >= 0 && index < gridListContainer.size()) {
				gridListContainer.setSelectedIndex(index);
				edit(getBaseCrudListForm().getSelectedDomain());
			}
			controlaVisibilidadeBotoesPreviousNext(gridListContainer);
		}
	}

	protected void btNextItemClick() throws SQLException {
		if (inCarrouselMode) {
			int index = getNextItemIndex(selectedItemPedidoCarrousel);
			if (index < itemPedidoCarrouselList.size()) {
				itemPedidoCarrouselList.get(selectedItemPedidoCarrousel).inCarrousel = false;
				selectedItemPedidoCarrousel++;
				itemPedidoCarrouselList.get(selectedItemPedidoCarrousel).inCarrousel = true;
				edit((ItemPedido)itemPedidoCarrouselList.get(index).clone());
			}
			controlaVisibilidadeBotoesPreviousNext();
		} else if (getBaseCrudListForm() != null && getBaseCrudListForm().getListContainer() != null) {
			GridListContainer gridListContainer = getBaseCrudListForm().getListContainer();
			int index = getNextItemIndex(gridListContainer.getSelectedIndex());
			if (index >= 0 && index < gridListContainer.size()) {
				gridListContainer.setSelectedIndex(index);
				edit(getBaseCrudListForm().getSelectedDomain());
			}
			controlaVisibilidadeBotoesPreviousNext(gridListContainer);
		}
	}

	private void btTrocarEmpresaClick() throws SQLException {
		show(new ListPedidosEmAbertoPorEmpresaForm());
	}

	public void executeEventSpecialKey(Event event) throws SQLException {
		KeyEvent ke = (KeyEvent) event;
		int key = ke.key;
		try {
			if (ke.isActionKey() && !inVendaUnitariaMode && event.target == edFiltro) {
				btFiltrarClick();
				if (!itemSelecionado) {
					edFiltro.requestFocus();
				} else {
					itemSelecionado = !itemSelecionado;
				}
			} else if (key == SpecialKeys.BACKSPACE && (ke.target == edQtItemFaturamento || ke.target == edQtItemFisico)) {
				edQtItemFisicoFaturamentoKeyPress();
			} else if (inVendaUnitariaMode && ke.isActionKey() && LavenderePdaConfig.isUsaTeclaEnterComoConfirmacaoItemPedido()) {
				if (isEditing()) {
					salvarClick();
				} else {
					salvarNovoClick();
				}
			}
		} finally {
			isEnterPressionado = true;
		}
	}

	private void edQtDesejadoValueChange() throws SQLException {
		if (edQtDesejada.isEditable() && edQtDesejada.isVisible()) {
			edQtItemRequestFocus(getItemPedido(),false);
			ItemPedido itemPedido = getItemPedido();
			double qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido,itemPedido.getCdLocalEstoque());
			qtEstoque = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), cbUnidadeAlternativa.getProdutoUnidade(), qtEstoque);
			double saldoEstoque = ValueUtil.round(qtEstoque) + itemPedido.oldQtEstoqueConsumido;
			getItemPedido().setQtItemFisico(saldoEstoque < edQtDesejada.getValueDouble() ? saldoEstoque: edQtDesejada.getValueDouble());
			edQtItemFisico.setValue(getItemPedido().getQtItemFisico());
			getItemPedido().qtItemDesejado = edQtDesejada.getValueDouble();
			changeEdQtDesejadaForeColor();
		}
	}

	private void changeEdQtDesejadaForeColor() throws SQLException {
		if (getItemPedido().getQtItemFisico() < getItemPedido().qtItemDesejado) {
			edQtDesejada.setForeColor(Color.RED);
		} else {
			edQtDesejada.setForeColor(Color.BLACK);
		}
	}

	protected void controlaVisibilidadeBotoesPreviousNext(GridListContainer gridListContainer) throws SQLException {
		if (gridListContainer != null) {
			btPreviousItem.setEnabled(gridListContainer.getSelectedIndex() != 0);
			btNextItem.setEnabled(!((gridListContainer.getSelectedIndex() + 1) == gridListContainer.size()));
		}
	}

	private void controlaVisibilidadeBotoesPreviousNext() {
		btPreviousItem.setEnabled(selectedItemPedidoCarrousel != 0);
		btNextItem.setEnabled(!((selectedItemPedidoCarrousel + 1) == itemPedidoCarrouselList.size()));
	}

	protected boolean validaTrocarCbUnidade(ItemPedido itemPedido, String cdUnidade) throws SQLException {
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			if (ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
				if (!ProdutoCreditoDescService.getInstance().validaTrocaUnidadeAlternativaItemComDesconto(itemPedido, cdUnidade)) {
					if (UiUtil.showConfirmYesNoMessage(Messages.PRODUTOCREDITODESCONTO_ERRO_ALTERAR_QTD_ITEM)) {
						ItemPedido itemPedidoSemDesconto = (ItemPedido) itemPedido.clone();
						validaComoFicaItemSemDescontoAplicado(itemPedidoSemDesconto);
						retiraCreditoDescAplicado(itemPedido, itemPedidoSemDesconto);
						return true;
					} else {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean validaAlterarQtdItem(ItemPedido itemPedido) throws SQLException {
		if (isItemPedidoComCreditoDesconto(itemPedido)) {
			if (ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
				if (UiUtil.showConfirmYesNoMessage(Messages.PRODUTOCREDITODESCONTO_ERRO_ALTERAR_QTD_ITEM)) {
					ItemPedido itemPedidoSemDesconto = (ItemPedido) itemPedido.clone();
					validaComoFicaItemSemDescontoAplicado(itemPedidoSemDesconto);
					retiraCreditoDescAplicado(itemPedido, itemPedidoSemDesconto);
					return true;
				} else {
					return false;
				}
			}
		}
		return true;
	}

	protected boolean validaAlterarValorItem(ItemPedido itemPedido, EditNumberFrac edDesconto, double oldValue) throws SQLException {
		if (oldValue == edDesconto.getValueDouble()) return true;
		try {
			boolean valido = validaAlterarValorItem(itemPedido);
			if (valido && edDesconto == edVlPctDesconto2 && LavenderePdaConfig.usaDescontoExtra) {
				boolean isMaxDescExtraValido = edDesconto.getValueDouble() <= itemPedido.getProduto().vlPctMaxDescExtra;
				valido &= isMaxDescExtraValido && getItemPedidoService().validaDescExtraFlex(itemPedido, oldValue);
				if (!valido) {
					UiUtil.showErrorMessage(getMessagemErroDescExtra(isMaxDescExtraValido, itemPedido.getProduto().vlPctMaxDescExtra));
				}
			}
			if (!valido) edDesconto.setValue(oldValue);
			return valido;
		} catch (Throwable e) {
			edDesconto.setValue(oldValue);
			throw e;
		}
	}

	private String getMessagemErroDescExtra(boolean isMaxDescExtraValido, double vlPctMaxDescExtra) {
		if (!isMaxDescExtraValido) {
			return MessageUtil.getMessage(Messages.MSG_ERRO_DESC_EXTRA_MAIOR_PERMITIDO, StringUtil.getStringValueToInterface(vlPctMaxDescExtra));
		}
		return Messages.MSG_ERRO_DESC_EXTRA_COM_FLEX;
	}

	protected boolean validaAlterarValorItem(ItemPedido itemPedido) throws SQLException {
		if (isItemPedidoComCreditoDesconto(itemPedido)) {
			if (ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO.equals(itemPedido.flTipoCadastroItem)) {
				if (UiUtil.showConfirmYesNoMessage(Messages.PRODUTOCREDITODESCONTO_MSG_ALTERACAO_VALOR_ITEM_DESCONTO)) {
					ItemPedido itemPedidoSemDesconto = (ItemPedido) itemPedido.clone();
					validaComoFicaItemSemDescontoAplicado(itemPedidoSemDesconto);
					retiraCreditoDescAplicado(itemPedido, itemPedidoSemDesconto);
					return true;
				} else {
					return false;
				}
			} else {
				if (itemPedido.vlPctDesconto != edVlPctDesconto.getValueDouble()) {
					if (itemPedido.qtdCreditoDesc > pedido.qtdCreditoDescontoGerado - pedido.qtdCreditoDescontoConsumido && pedido.qtdCreditoDescontoConsumido > 0) {
						UiUtil.showErrorMessage(Messages.PRODUTOCREDITODESCONTO_ERRO_DESCONTO_ITEM_QTD);
						return false;
					} else if (!UiUtil.showConfirmYesNoMessage(Messages.PRODUTOCREDITODESCONTO_MSG_ALTERACAO_VALOR_ITEM_QTD)) {
						return false;
					}
				} else {
					return false;
				}
			}
		}
		return true;
	}

	private void validaComoFicaItemSemDescontoAplicado(ItemPedido itemPedidoSemDesconto) throws SQLException {
		itemPedidoSemDesconto.qtdCreditoDesc = 0;
		itemPedidoSemDesconto.flTipoCadastroItem = null;
		itemPedidoSemDesconto.cdProdutoCreditoDesc = null;
		PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedidoSemDesconto);
		getPedidoService().calculateItemPedido(pedido, itemPedidoSemDesconto, true);
		validateItemPedidoUI(itemPedidoSemDesconto, pedido, true);
	}

	private void retiraCreditoDescAplicado(ItemPedido itemPedido, ItemPedido itemPedidoSemDesconto) throws SQLException {
		pedido.qtdCreditoDescontoConsumido -= itemPedido.qtdCreditoDesc;
		itemPedido.qtdCreditoDesc = 0;
		itemPedido.flTipoCadastroItem = null;
		itemPedido.cdProdutoCreditoDesc = null;
		PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
		getPedidoService().calculateItemPedido(pedido, itemPedido, true);
		ItemPedidoService.getInstance().updateItemSimples(itemPedido);
		pedido.itemPedidoList.removeElement(itemPedido);
		itemPedido.itemChanged = true;
		itemPedidoSemDesconto.itemChanged = true;
		pedido.atualizaLista = true;
		pedido.itemPedidoList.addElement(itemPedidoSemDesconto);
		PedidoService.getInstance().updatePedidoAfterCrudItemPedido(pedido);
		if (prevContainer != null && prevContainer instanceof ListItemPedidoForm) {
			((ListItemPedidoForm)prevContainer).updateCurrentRecord(itemPedidoSemDesconto);
		}
	}

	private String getSelectedUnidadeGrid() {
		if (gridUnidadeAlternativa.getSelectedIndex() == -1) {
			throw new ValidationException(Messages.UNIDADE_MSG_NENHUMA_SELECIONADA);
		}
		return gridUnidadeAlternativa.getSelectedItem()[0];
	}

	protected void listContainerProdutoClick() throws SQLException {
		produtoSelecionado = null;
	}

	private void edQtItemFisicoFaturamentoKeyPress() throws SQLException {
		if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.consisteMultiploEmbalagem || LavenderePdaConfig.avisaConversaoUnidades || LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
			setColorsEditsByConversaoUnidade(getItemPedido());
		}
		if (LavenderePdaConfig.isGradeProdutoModoLista()) {
			btGradeItemPedido.setEnabled(edQtItemFisico.getValueDouble() == 0 && isBtGradeItemPedidoEnabled());
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			setPontuacaoColor(getItemPedido());
		}
	}

	protected void edQtItemFaturamentoValueChange() throws SQLException {
		if (edQtItemFaturamento.isEditable() && edQtItemFaturamento.isVisible()) {
			edQtItemRequestFocus(getItemPedido(),true);
			if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
				houveAlteracaoQtdItem = ValueUtil.round(getItemPedido().qtItemFaturamento) != ValueUtil.round(edQtItemFaturamento.getValueDouble());
			}
			getItemPedido().qtItemFaturamento = edQtItemFaturamento.getValueDouble();
			if (getItemPedido().getProduto() != null) {
				ItemPedidoService.getInstance().aplicarConversaoUnidadeMedida(getItemPedido(), pedido);
				updateQtItens(getItemPedido(), true, false);
			}
		}
		setColorsEditsByConversaoUnidade(getItemPedido());
		if (LavenderePdaConfig.usaControlePontuacao) {
			setPontuacaoColor(getItemPedido());
		}
	}

	protected void edQtItemFisicoValueChange(ItemPedido itemPedido) throws SQLException {
		final boolean hasQtItemAlterado = ValueUtil.round(itemPedido.getQtItemFisico()) != ValueUtil.round(edQtItemFisico.getValueDouble());
		if (itemPedido != null && ItemPedidoService.getInstance().isItemBonificadoNoPedido(itemPedido) && itemPedido.isItemBonificacao()) {
			if (!validaQtItemBonificado(itemPedido)) {
				return;
			}
		}
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoProduto() && itemPedido.isItemBonificacao() && !itemPedido.pedido.isPedidoBonificacao() && !itemPedido.pedido.getTipoPedido().isIgnoraPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().validaEdicaoQtItemFisicoItemBonificadoAutomaticamente(itemPedido, edQtItemFisico.getValueDouble(), null);
		}
		if (edQtItemFisico.isEditable() && edQtItemFisico.isVisible()) {
			edQtItemRequestFocus(itemPedido, false);
			if (LavenderePdaConfig.isPermiteArrendondarCasoItemNaoAlcanceConversao()) {
				confirmaArredondaConversaoUnidade();
			}
			if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo()) {
				houveAlteracaoQtdItem = hasQtItemAlterado;
			}
			itemPedido.setQtItemFisico(edQtItemFisico.getValueDouble());
			if ((itemPedido.getProduto() != null) && LavenderePdaConfig.usaConversaoUnidadesMedida) {
				ItemPedidoService.getInstance().aplicarConversaoUnidadeMedida(itemPedido, pedido);
				updateQtItens(itemPedido, false, true);
			}
		}
		if (LavenderePdaConfig.liberaPedidoPendenteComSenhaPorDescontoMaximo) {
			if (itemPedido.getItemTabelaPreco().qtItem > 0) {
				edVlPctDesconto.setEditable(false);
			} else {
				edVlPctDesconto.setEditable(isEdVlPctDescontoEditable());
			}
		}
		setColorsEditsByConversaoUnidade(itemPedido);

		if (hasQtItemAlterado && LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto()) {
			itemPedido.loadDescPromocional(true);
			if (itemPedido.recalculaDescPromo) {
				itemPedido.recalculaDescPromo = false;
				edVlPctDescontoValueChange(itemPedido);
				getPedidoService().loadValorBaseItemPedido(pedido, itemPedido);
				calcularClick();
				edVlPctDescontoValueChange(itemPedido);
			}
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			setPontuacaoColor(itemPedido);
		}
		if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlItemPedido() && itemPedido.vlPctFaixaDescQtdPeso > 0) {
			itemPedido.vlPctDesconto = 0;
			PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
			edVlItemPedido.setValue(itemPedido.vlItemPedido);
		}
	}

	protected boolean isEdVlPctDescontoEditable() throws SQLException {
		return isEnabled()
				&& !LavenderePdaConfig.usaCalculoReversoNaST
				&& !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata
				&& LavenderePdaConfig.isPermiteDescontoPercentualItemPedido()
				&& getItemPedido() != null && getItemPedido().getItemTabelaPreco() != null
				&& !LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip
				&& (!LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() || !getItemPedido().getItemTabelaPreco().isFlBloqueiaDescPadrao())
				&& !LavenderePdaConfig.usaDescQuantidadePeso()
				&& !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(), Cliente.DESCONTO_BLOQUEADO_MANUAL)
				|| habilitaDescontoKitFechado;

	}

	protected void updateQtItens(final ItemPedido itemPedido) {
		updateQtItens(itemPedido, true, true);
	}

	protected void updateQtItens(final ItemPedido itemPedido, boolean updateQtItemFisico, boolean updateQtItemFaturamento) {
		if (updateQtItemFisico) {
			if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
				if (LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDDESEJADACOMPRA)) {
					edQtDesejada.setText((itemPedido.qtItemDesejado > 0) ? StringUtil.getStringValue(itemPedido.qtItemDesejado, 0) : ValueUtil.VALOR_NI);
				} else {
					edQtDesejada.setValue(itemPedido.qtItemDesejado);
				}
			}
			if (itemPedido.getQtItemFisico() == 0) {
				if (edQtItemFisico.isEditable()) {
					edQtItemFisico.setText("");
				} else {
					edQtItemFisico.setValue(itemPedido.getQtItemFisicoOrg());
				}
			} else {
				if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
					edQtItemFisico.setText(StringUtil.getStringValue(itemPedido.getQtItemFisicoOrg(), 0));
				} else {
					edQtItemFisico.setValue(itemPedido.getQtItemFisicoOrg());
				}
			}
		}
		if (updateQtItemFaturamento) {
			if (itemPedido.qtItemFaturamento == 0) {
				edQtItemFaturamento.setText("");
			} else {
				edQtItemFaturamento.setValue(itemPedido.qtItemFaturamento);
				if ((itemPedido.qtItemFaturamento % 1) == 0) {
					edQtItemFaturamento.setText(StringUtil.getStringValue(itemPedido.qtItemFaturamento, 0));
				}
			}
		}
	}

	protected void edVlPctDescontoValueChange(ItemPedido itemPedido) throws SQLException {
		edVlPctDescontoValueChange(itemPedido, false);
	}

	protected void edVlPctDescontoValueChange(ItemPedido itemPedido, boolean forceRecalculoDesconto) throws SQLException {
		if (edVlPctDesconto.isEditable() || edVlPctDesconto2.isEditable() || edVlPctDesconto3.isEditable() || forceRecalculoDesconto) {
			edVlPctAcrescimo.setValue(0);
			itemPedido.vlPctAcrescimo = 0;
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() && !itemPedido.getItemTabelaPreco().isFlBloqueiaDesc3()) {
				controlaVisibilidadeCampoDesconto3();
			}
			calculoRapidoDescontos(itemPedido);
			itemPedido.zerouDescAcresByPoliticaComercial = false;
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			setPontuacaoColor(getItemPedido());
		}
	}

	private void controlaVisibilidadeCampoDesconto3() throws SQLException {
		edVlPctDesconto3.setEditable(isHabilitaEdicaoCampoPctDesconto3());
		if (!edVlPctDesconto3.isEditable()) {
			edVlPctDesconto3.setValue(0);
		}
	}

	protected void edVlPctAcrescimoValueChange(ItemPedido itemPedido) throws SQLException {
		if (edVlPctAcrescimo.isEditable()) {
			edVlPctDesconto.setValue(0);
			itemPedido.vlPctDesconto = 0;
			itemPedido.descQuantidade = null;
			itemPedido.vlPctFaixaDescQtd = 0;
			if (LavenderePdaConfig.usaCalculoReversoNaST) {
				itemPedido.vlPctDescontoStReverso = 0;
			}
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
				edVlDesconto.setValue(0);
				edVlPctDesconto2.setValue(0);
				edVlDesconto2.setValue(0);
				edVlPctDesconto3.setValue(0);
				edVlDesconto3.setValue(0);
				itemPedido.vlDesconto = 0;
				itemPedido.vlPctDesconto2 = 0;
				itemPedido.vlDesconto2 = 0;
				itemPedido.vlPctDesconto3 = 0;
				itemPedido.vlDesconto3 = 0;
			}
			itemPedido.flTipoEdicao = LavenderePdaConfig.permiteAlterarValorItemComIPI ? ItemPedido.ITEMPEDIDO_EDITANDO_VLITEMIPI : ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
			calculoRapidoAcrescimo(itemPedido);
			itemPedido.zerouDescAcresByPoliticaComercial = false;
		}
		if (LavenderePdaConfig.usaControlePontuacao) {
			setPontuacaoColor(getItemPedido());
		}
	}

	protected void edPrecoEmbPrimariaValueChange() throws SQLException {
		if (LavenderePdaConfig.isPermiteApenasAcrescimoItemPedido() && !pedido.isFlPrecoLiberadoSenha()) {
			// N?o pode ter desconto nenhum
			if (edPrecoEmbPrimaria.getValueDouble() < getItemPedido().vlBaseEmbalagemElementar) {
				edPrecoEmbPrimaria.setValue(vlEmbalagemElementarOld);
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_ALTERAR_VALOR_ABAIXO_NAO_PERMITIDO);
			}
		}
		if (LavenderePdaConfig.isPermiteApenasDescontoItemPedido() && !pedido.isFlPrecoLiberadoSenha()) {
			// N?o pode ter acr?scimo nenhum
			if (edPrecoEmbPrimaria.getValueDouble() > getItemPedido().vlBaseEmbalagemElementar) {
				edPrecoEmbPrimaria.setValue(vlEmbalagemElementarOld);
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_ALTERAR_VALOR_ACIMA_NAO_PERMITIDO);
			}
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) {
			edVlEmbalagemComReducaoSimples.setValue(edPrecoEmbPrimaria.getValueDouble() - (getItemPedido().getItemTabelaPreco().vlReducaoOptanteSimples / getItemPedido().qtEmbalagemElementar));
		}
		getItemPedido().flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLELEMENTAR;
		calculoRapidoValorItem();
	}

	protected void edVlItemIpiValueChange() throws SQLException {
		if (LavenderePdaConfig.isPermiteApenasAcrescimoItemPedido() && !pedido.isFlPrecoLiberadoSenha()) {
			// N?o pode ter desconto nenhum
			if (edVlItemIpi.getValueDouble() < getItemPedido().vlBaseItemIpi) {
				edVlItemIpi.setValue(vlItemIpiOld);
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_ALTERAR_VALOR_ABAIXO_NAO_PERMITIDO);
			}
		}
		if (LavenderePdaConfig.isPermiteApenasDescontoItemPedido() && !pedido.isFlPrecoLiberadoSenha()) {
			// N?o pode ter acr?scimo nenhum
			if (edVlItemIpi.getValueDouble() > getItemPedido().vlBaseItemIpi) {
				edVlItemIpi.setValue(vlItemIpiOld);
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_ALTERAR_VALOR_ACIMA_NAO_PERMITIDO);
			}
		}
		getItemPedido().flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEMIPI;
		if (getItemPedido().getProduto() != null) {
			ItemPedido itemPedido = getItemPedido();
			itemPedido.vlItemIpi = edVlItemIpi.getValueDouble();
			Tributacao tributacao = itemPedido.getTributacaoItem(itemPedido.pedido.getCliente());
			double vlPctIpi = tributacao != null && tributacao.vlPctIpi != 0 ? tributacao.vlPctIpi : itemPedido.getProduto().vlPctIpi;
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemIpi / (1 + vlPctIpi / 100));
			itemPedido.vlTotalIpiItem = ValueUtil.round(itemPedido.vlItemIpi - itemPedido.vlItemPedido, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
			double vlPctDiferenca = ValueUtil.round(((itemPedido.vlItemIpi / itemPedido.vlBaseItemIpi) - 1) * 100);
			if (itemPedido.vlItemIpi < itemPedido.vlBaseItemIpi) {
				itemPedido.vlPctDesconto = vlPctDiferenca * -1;
				itemPedido.vlPctAcrescimo = 0;
			} else {
				itemPedido.vlPctAcrescimo = vlPctDiferenca;
				itemPedido.vlPctDesconto = 0;
			}
			edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
			edVlPctAcrescimo.setValue(itemPedido.vlPctAcrescimo);
			refreshInterfaceAfterDescAcresc(getItemPedido());
			calcularClick();
		}
	}

	protected void edVlUnidadeEmbalagemValueChange() throws SQLException {
		if (edVlUnidadeEmbalagem.isEditable() && edVlUnidadeEmbalagem.isVisible()) {
			double vlUnidadeEmbalagem = edVlUnidadeEmbalagem.getValueDouble();
			if (getItemPedido().getProduto() != null) {
				double nuConversaoUnidadeMedida = getItemPedido().getProduto().nuConversaoUnidadesMedida == 0d ? 1d: getItemPedido().getProduto().nuConversaoUnidadesMedida;
				// Retira o valor referente a ST
				if (LavenderePdaConfig.calculaStSimplificadaItemPedido && pedido.getCliente().isFlAplicaSt()) {
					vlUnidadeEmbalagem -= getItemPedido().getProduto().vlSt > 0? getItemPedido().getProduto().vlSt / nuConversaoUnidadeMedida : 0;
				} else if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
					ItemPedidoService.getInstance().calculaStItemPedido(getItemPedido(), pedido.getCliente());
					vlUnidadeEmbalagem -= getItemPedido().vlSt > 0 ? getItemPedido().vlSt / nuConversaoUnidadeMedida : 0;
				}
				// --
				if (LavenderePdaConfig.ignoraAcrescimo && getItemPedido().isRecebeuDescontoPorQuantidade()) {
					if (ValueUtil.round(vlUnidadeEmbalagem * nuConversaoUnidadeMedida) > getItemPedido().vlBaseItemPedido) {
						edVlUnidadeEmbalagem.setValue(getItemPedidoService().calculaVlUnidadePorEmbalagem(getItemPedido(), pedido));
						throw new ValidationException(Messages.ITEMPEDIDO_MSG_ALTERAR_VALOR_ACIMA_NAO_PERMITIDO);
					}
				}
				getItemPedido().vlItemPedido = ValueUtil.round(vlUnidadeEmbalagem * nuConversaoUnidadeMedida);
			}
			repaintEdVlItemPedido();
			edVlItemPedido.setValue(getItemPedido().vlItemPedido);
			calculoRapidoValorItem();
			calcularClick();
		}
	}

	protected void calculoRapidoValorItem() throws SQLException {
		calculoRapidoValorItem(getItemPedido(), false);
	}

	protected void calculoRapidoValorItem(ItemPedido itemPedido, boolean fromMultInsercao) throws SQLException {
		if (itemPedido.getProduto() != null) {
			if (itemPedido.vlBaseItemPedido > 0d) {
				if (isPermiteAlterarVlItemNaUnidadeElementar()) {
					if (!fromMultInsercao) {
						itemPedido.vlEmbalagemElementar = edPrecoEmbPrimaria.getValueDouble();
					}
					ItemPedidoService.getInstance().aplicaValorElementarNoVlItemPedido(itemPedido);
					itemPedido.vlPctDesconto = ItemPedidoService.getInstance().calculaVlPctDesconto(itemPedido.vlBaseEmbalagemElementar, itemPedido.vlEmbalagemElementar);
				} else {
					if (!fromMultInsercao) {
						itemPedido.vlItemPedido = edVlItemPedido.getValueDouble();
					}
					itemPedido.vlPctDescPedido = LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem() ? pedido.vlPctDescItem : 0d;
					itemPedido.vlPctDesconto = ItemPedidoService.getInstance().getVlPctDescontoSemDescQtdFromItemPedido(itemPedido);
				}
				if (LavenderePdaConfig.usaConversaoUnidadesMedida && (itemPedido.descQuantidade != null)) {
					double vlItemPedidoComDescQtde = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (itemPedido.descQuantidade.vlPctDesconto / 100)));
					if (vlItemPedidoComDescQtde == ValueUtil.round(itemPedido.vlItemPedido)) {
						itemPedido.vlPctDesconto = itemPedido.descQuantidade.vlPctDesconto;
					}
				}
				if (itemPedido.vlPctDesconto < 0) {
					itemPedido.vlPctDesconto = 0;
				}
				// % de acr?scimo
				itemPedido.vlPctAcrescimo = 0;
				if (itemPedido.vlBaseItemPedido != 0) {
					itemPedido.vlPctAcrescimo = ItemPedidoService.getInstance().calculaVlPctAcrescimo(itemPedido.vlBaseItemPedido, itemPedido.vlItemPedido);
					if (itemPedido.vlPctAcrescimo < 0) {
						itemPedido.vlPctAcrescimo = 0;
					}
				}
				if (!fromMultInsercao) {
					edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
					edVlPctAcrescimo.setValue(itemPedido.vlPctAcrescimo);
				}
				getItemPedidoService().applyRedutorOptanteSimplesAposCalculoValorItem(itemPedido, pedido.getCliente());
				refreshInterfaceAfterDescAcresc(itemPedido, fromMultInsercao);
			}
		}
	}

	protected void edVlItemPedidoValueChange() throws SQLException {
		edVlItemPedidoValueChange(getItemPedido(), false);
	}

	protected void edVlItemPedidoValueChange(ItemPedido itemPedido, boolean fromMultInsercao) throws SQLException {
		double vlItemPedido = itemPedido.vlBaseItemPedido;
		double valorRealItemPedido = !fromMultInsercao ? edVlItemPedido.getValueDouble() : itemPedido.vlItemPedido;
		if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedido()) {
			if (!fromMultInsercao && ValueUtil.round(itemPedido.vlItemPedido) != ValueUtil.round(valorRealItemPedido)) {
				edVlItemPedido.setForeColor(ColorUtil.componentsForeColor);
			}
			if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedidoValidandoVlMinimo()) {
				double newVlItemPedido = getItemPedidoService().getVlItemPedidoUltimoPedidoCliente(itemPedido);
				if (newVlItemPedido != 0.0) {
					vlItemPedido = newVlItemPedido;
				}
			}
		}
		if (fromMultInsercao) {
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
			if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
				itemPedido.vlPctDesconto2 = 0;
				itemPedido.vlPctDesconto3 = 0;
			}
		} else if (edVlItemPedido.isEditable()) {
			if (edVlPctDesconto.isEditable()) {
				edVlPctDesconto.setValue(0);
				if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
					edVlDesconto.setValue(0);
					edVlPctDesconto2.setValue(0);
					edVlDesconto2.setValue(0);
					itemPedido.vlPctDesconto2 = 0;
					edVlPctDesconto3.setValue(0);
					itemPedido.vlPctDesconto3 = 0;
					edVlDesconto3.setValue(0);
				}
			}
			if (edVlPctAcrescimo.isEditable()) {
				edVlPctAcrescimo.setValue(0);
			}
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
		}
		if (LavenderePdaConfig.isPermiteApenasAcrescimoItemPedido() && !pedido.isFlPrecoLiberadoSenha()) {
			// N?o pode ter desconto nenhum
			if (valorRealItemPedido < vlItemPedido) {
				if (fromMultInsercao) {
					// setar valor antigo
				} else {
					edVlItemPedido.setValue(vlItemPedidoOld);
				}
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_ALTERAR_VALOR_ABAIXO_NAO_PERMITIDO);
			}
		}
		if ((LavenderePdaConfig.isPermiteApenasDescontoItemPedido() || (LavenderePdaConfig.ignoraAcrescimo && itemPedido.isRecebeuDescontoPorQuantidade())) && !pedido.isFlPrecoLiberadoSenha()) {
			// N?o pode ter acr?scimo nenhum
			if (valorRealItemPedido > itemPedido.vlBaseItemPedido) {
				if (fromMultInsercao) {
					// setar valor antigo
				} else {
					edVlItemPedido.setValue(vlItemPedidoOld);
				}
				throw new ValidationException(Messages.ITEMPEDIDO_MSG_ALTERAR_VALOR_ACIMA_NAO_PERMITIDO);
			}
		}
		calculoRapidoValorItem(itemPedido, fromMultInsercao);
	}

	protected void edVlItemSTReversoValueChange() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		double vlItemPedidoStReversoOld = itemPedido.vlItemPedidoStReverso;
		itemPedido.vlItemPedidoStReverso = itemPedido.vlItemPedido = edVlItemStReverso.getValueDouble();
		itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEMST;
		double vlNegociadoComST = ValueUtil.round(itemPedido.vlItemPedido, LavenderePdaConfig.nuCasasDecimais);
		// % de desconto
		itemPedido.vlPctDescontoStReverso = ItemPedidoService.getInstance().getVlPctDescontoSemDescQtdFromItemPedido(itemPedido);
		if (itemPedido.vlPctDescontoStReverso < 0) {
			itemPedido.vlPctDescontoStReverso = 0;
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && (LavenderePdaConfig.isUsaDescontoMaximoPorTipoPedido() || LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto())) {
			validadeVlFinalItemUsaDescontoMax(itemPedido, vlItemPedidoStReversoOld);
		}
		// % de acr?scimo
		itemPedido.vlPctAcrescimo = 0;
		itemPedido.vlPctAcrescimo = ValueUtil.round(((vlNegociadoComST / itemPedido.vlBaseItemPedido) - 1) * 100, LavenderePdaConfig.nuCasasDecimais);
		if (itemPedido.vlPctAcrescimo < 0) {
			itemPedido.vlPctAcrescimo = 0;
		}
		edVlPctDescontoStReverso.setValue(itemPedido.vlPctDescontoStReverso);
		edVlPctDesconto.setValue(itemPedido.vlPctDescontoStReverso);
		edVlPctAcrescimo.setValue(itemPedido.vlPctAcrescimo);
		refreshInterfaceAfterDescAcresc(itemPedido);
	}

	public void validadeVlFinalItemUsaDescontoMax(ItemPedido itemPedido, double vlItemPedidoStReversoOld) {
		if (ValueUtil.isNotEmpty(edVlPctMaxDesconto.getValue()) && itemPedido.vlPctDescontoStReverso > edVlPctMaxDesconto.getValueDouble() && !LavenderePdaConfig.isNaoConsisteDescontoMaximo()) {
			edVlItemStReverso.setValue(vlItemPedidoStReversoOld);
			itemPedido.vlItemPedidoStReverso = vlItemPedidoStReversoOld;
			throw new ValidationException(Messages.ITEMPEDIDO_MSG_VL_ULTRAPASSOU_MAX_DESCONTO);
		}
	}

	private void edVlPctDescontoStReversoValueChange() throws SQLException {
		if (edVlPctDescontoStReverso.isEditable() && (getItemPedido().vlPctDescontoStReverso != 0d || edVlPctDescontoStReverso.getValueDouble() > 0d)) {
			edVlPctAcrescimo.setValue(0);
			ItemPedido itemPedido = getItemPedido();
			itemPedido.vlPctAcrescimo = 0;
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLDESCONTOST;
			itemPedido.vlPctDescontoStReverso = edVlPctDescontoStReverso.getValueDouble();
			double vlPctDescontoTotal = itemPedido.vlPctDescontoStReverso;
			if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
				vlPctDescontoTotal += itemPedido.vlPctFaixaDescQtd;
			}
			itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (vlPctDescontoTotal / 100)));
			itemPedido.vlItemPedidoStReverso = itemPedido.vlItemPedido;
			edVlPctDesconto.setValue(itemPedido.vlPctDescontoStReverso);
			refreshInterfaceAfterDescAcresc(itemPedido);
		}

	}

	protected void controlFocusCampos(final ItemPedido itemPedido) throws SQLException {
		if (! inVendaUnitariaMode && isInsereMultiplosSemNegociacao()) {
			return;
		}
		if (itemPedido.isEditandoAcrescimoPct()) {
			edVlPctAcrescimo.requestFocus();
		} else if (itemPedido.isEditandoDescontoPct()) {
			edVlPctDesconto.requestFocus();
		} else if (itemPedido.isEditandoValorItem() || itemPedido.isEditandoValorElementar()) {
			if (isPermiteAlterarVlItemNaUnidadeElementar()) {
				edPrecoEmbPrimaria.requestFocus();
			} else {
				edVlItemPedido.requestFocus();
			}
			if (edVlPctDesconto.isEditable()) {
				edVlPctDesconto.setValue(getItemPedido().vlPctDesconto);
			}
			if (edVlPctAcrescimo.isEditable()) {
				edVlPctAcrescimo.setValue(getItemPedido().vlPctAcrescimo);
			}
		} else if (itemPedido.isEditandoDescontoST()) {
			edVlPctDescontoStReverso.requestFocus();
		} else if (itemPedido.isEditandoValorItemST()) {
			edVlItemStReverso.requestFocus();
		}
	}

	private void edVlAgregadoSugeridoValueChange() throws SQLException {
		edPctMargemAgregada.setValue(getItemPedidoService().calculaPctMargemAgregada(getItemPedido().vlItemPedido, edVlAgregadoSugerido.getValueDouble()));
	}

	public boolean isItemPedidoComCreditoDesconto(ItemPedido itemPedido) throws SQLException {
		return LavenderePdaConfig.usaGerenciaDeCreditoDesconto && itemPedido != null && itemPedido.qtdCreditoDesc > 0;
	}

	protected void calculoRapidoDescontos(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getProduto() == null) return;
		boolean isEditandoMultiplaInsercao = PedidoService.getInstance().isInsereMultiplosSemNegociacao(itemPedido.pedido) && !isEditing();
		double vlPctDesconto = isEditandoMultiplaInsercao && !inVendaUnitariaMode ? itemPedido.vlPctDesconto : edVlPctDesconto.getValueDouble();
		if (LavenderePdaConfig.usaInterpolacaoPrecoProduto) {
			try {
				// Truncamento desconto
				vlPctDesconto = BigDecimal.valueOf(vlPctDesconto + 0.0001).setScale(LavenderePdaConfig.nuTruncamentoRegraDescontoVerba, BigDecimal.ROUND_FLOOR).doubleValue();
			} catch (ArithmeticException | IllegalArgumentException | InvalidNumberException e) {
				vlPctDesconto = ValueUtil.getDoubleValueTruncated(vlPctDesconto, LavenderePdaConfig.nuTruncamentoRegraDescontoVerba);
			}
			if (!isEditandoMultiplaInsercao) {
				edVlPctDesconto.setValue(vlPctDesconto);
			}
		}
		itemPedido.vlPctDesconto = vlPctDesconto;
		if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
			itemPedido.vlPctDescPedido = pedido.vlPctDescItem;
		} else {
			itemPedido.vlPctDescPedido = 0;
		}
		if (isPermiteAlterarVlItemNaUnidadeElementar()) {
			itemPedido.vlEmbalagemElementar = ValueUtil.round(itemPedido.vlBaseEmbalagemElementar * (1 - (itemPedido.vlPctDesconto / 100)));
			getItemPedidoService().aplicaValorElementarNoVlItemPedido(itemPedido);
		} else if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
			calculoRapidoDescAcrescVlItemIpi(true);
		} else {
			if (LavenderePdaConfig.aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa && !itemPedido.isCdUnidadeIgualCdUnidadeProduto()) {
				double vlItemPedido = ValueUtil.round(itemPedido.vlUnidadePadrao * (1 - (itemPedido.vlPctDesconto / 100)));
				ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
				if (produtoUnidade != null) {
					itemPedido.vlItemPedido = ItemPedidoService.getInstance().aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, vlItemPedido);
				}
			} else {
				double vlPctDescontoCalculo = itemPedido.vlPctDesconto + itemPedido.vlPctDescPedido;
				if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido && itemPedido.permiteAplicarDesconto() && itemPedido.descQuantidade != null) {
					double oldVlPctDesconto = itemPedido.vlPctDesconto;
					itemPedido.vlPctDesconto = vlPctDescontoCalculo;
					itemPedido.atualizandoDesc = itemPedido.pedido.getTabelaPreco().isAplicaDescQtdeAuto()
							|| !itemPedido.pedido.getTabelaPreco().isAplicaDescQtdeAuto() && itemPedido.isEditandoDescontoPct();
					getItemPedidoService().calculaDescQtdItemPedido(pedido, itemPedido, false);
					itemPedido.atualizandoDesc = false;
					itemPedido.vlPctDesconto = oldVlPctDesconto;
				} else {
					itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 - (vlPctDescontoCalculo / 100)));
				}
			}
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1() || LavenderePdaConfig.usaDescontoExtra) {
			calculoRapidoDesconto2(itemPedido);
			calculoRapidoDesconto3(itemPedido);
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			itemPedido.vlPctDesconto3 = edVlPctDesconto3.getValueDouble();
		}
		getItemPedidoService().applyRedutorOptanteSimplesAposCalculoValorItem(itemPedido, pedido.getCliente());
		getItemPedidoService().calculaPontuacaoItemPedido(itemPedido, pedido);
		refreshInterfaceAfterDescAcresc(itemPedido);
	}

	protected void calculoRapidoDesconto2(ItemPedido itemPedido) {
		itemPedido.vlPctDesconto2 = edVlPctDesconto2.getValueDouble();
		itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDesconto2 / 100)));
	}

	protected void calculoRapidoDesconto3(ItemPedido itemPedido) {
		itemPedido.vlPctDesconto3 = edVlPctDesconto3.getValueDouble();
		itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlItemPedido * (1 - (itemPedido.vlPctDesconto3 / 100)));
	}

	protected void calculoRapidoAcrescimo(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.getProduto() != null) {
			boolean isEditandoMultiplaInsercao = PedidoService.getInstance().isInsereMultiplosSemNegociacao(itemPedido.pedido) && !isEditing();
			itemPedido.vlPctAcrescimo = isEditandoMultiplaInsercao ? itemPedido.vlPctAcrescimo : edVlPctAcrescimo.getValueDouble();
			if (LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem() && LavenderePdaConfig.isAcumulaComDescDoItem()) {
				itemPedido.vlPctDescPedido = pedido.vlPctDescItem;
			} else {
				itemPedido.vlPctDescPedido = 0;
			}
			// --
			if (isPermiteAlterarVlItemNaUnidadeElementar()) {
				itemPedido.vlEmbalagemElementar = ValueUtil.round(itemPedido.vlBaseEmbalagemElementar * (1 + (itemPedido.vlPctAcrescimo / 100)));
				getItemPedidoService().aplicaValorElementarNoVlItemPedido(itemPedido);
			} else if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
				calculoRapidoDescAcrescVlItemIpi(false);
			} else {
				if (LavenderePdaConfig.aplicaDescAcrescNaUnidadePadraoParaUnidadeAlternativa && !itemPedido.isCdUnidadeIgualCdUnidadeProduto()) {
					double vlItemPedido = ValueUtil.round(itemPedido.vlUnidadePadrao * (1 + (itemPedido.vlPctAcrescimo / 100)));
					ProdutoUnidade produtoUnidade = ProdutoUnidadeService.getInstance().getUnidadeAlternativaByItemPedido(itemPedido);
					if (produtoUnidade != null) {
						itemPedido.vlItemPedido = ItemPedidoService.getInstance().aplicaMultiplicacaoDivisao(itemPedido, produtoUnidade, vlItemPedido);
					}
				} else {
					itemPedido.vlItemPedido = ValueUtil.round(itemPedido.vlBaseItemPedido * (1 + ((itemPedido.vlPctAcrescimo - itemPedido.vlPctDescPedido) / 100)));
				}
			}
			if (itemPedido.vlPctAcrescimo == 0d && LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
				itemPedido.atualizandoDesc = itemPedido.pedido.getTabelaPreco().isAplicaDescQtdeAuto();
				itemPedido.flTipoEdicao = 0;
			}
			getItemPedidoService().applyRedutorOptanteSimplesAposCalculoValorItem(itemPedido, pedido.getCliente());
			refreshInterfaceAfterDescAcresc(itemPedido);
		}
	}

	private void calculoRapidoDescAcrescVlItemIpi(boolean isDesconto) throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		Tributacao tributacao = itemPedido.getTributacaoItem(itemPedido.pedido.getCliente());
		double vlPctIpi = tributacao != null && tributacao.vlPctIpi != 0 ? tributacao.vlPctIpi: itemPedido.getProduto().vlPctIpi;
		if (isDesconto) {
			itemPedido.vlItemIpi = ValueUtil.round(itemPedido.vlBaseItemIpi * (1 - (itemPedido.vlPctDesconto / 100)),LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		} else {
			itemPedido.vlItemIpi = ValueUtil.round(itemPedido.vlBaseItemIpi * (1 + (itemPedido.vlPctAcrescimo / 100)),LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
		}
		itemPedido.vlItemPedido = itemPedido.vlItemIpi / (1 + vlPctIpi / 100);
		itemPedido.vlTotalIpiItem = ValueUtil.round(itemPedido.vlItemIpi - itemPedido.vlItemPedido,LavenderePdaConfig.nuCasasDecimaisVlStVlIpi);
	}

	protected void refreshInterfaceAfterDescAcresc(ItemPedido itemPedido) throws SQLException {
		refreshInterfaceAfterDescAcresc(itemPedido, false);
	}

	protected void refreshInterfaceAfterDescAcresc(ItemPedido itemPedido, boolean fromMultInsercao) throws SQLException {
		double qtItemTrue = itemPedido.getQtItemFisico();
		itemPedido.qtItemFisico = qtItemTrue != 0 ? qtItemTrue : 1;
		if (!LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido && !itemPedido.hasDescProgressivo() && !itemPedido.isCombo()) {
			VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
		}
		if (LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto && !itemPedido.hasDescProgressivo() && !itemPedido.isCombo()) {
			VerbaService.getInstance().aplicaVerbaPorGrupoProdComTolerancia(pedido, itemPedido);
		}
		itemPedido.setQtItemFisico(qtItemTrue);
		if (!fromMultInsercao) {
			repaintEdVlItemPedido();
			edVlItemPedido.setValue(itemPedido.vlItemPedido);
			if (LavenderePdaConfig.permiteAlterarValorItemComIPI) {
				edVlItemIpi.setValue(itemPedido.vlItemIpi);
			}
			double vlVerbaItemTmp;
			if (LavenderePdaConfig.isMostraFlexPositivoPedido() && itemPedido.vlVerbaItemPositivo != 0) {
				vlVerbaItemTmp = itemPedido.vlVerbaItemPositivo;
			} else {
				vlVerbaItemTmp = itemPedido.vlVerbaItem;
				if (LavenderePdaConfig.informaVerbaManual && (itemPedido.vlVerbaItem < 0)) {
					vlVerbaItemTmp = itemPedido.vlVerbaItem * -1;
				}
			}
			if (LavenderePdaConfig.usaVerbaGrupoSaldoPersonalizada()) {
				vlVerbaItemTmp = itemPedido.vlVerbaItem + itemPedido.vlVerbaGrupoItem;
			}
			edVlVerbaItem.setValue(vlVerbaItemTmp);
			edVlVerbaItemPositiva.setValue(itemPedido.vlVerbaItemPositivo);
			edVlVerbaItemNegativa.setValue(itemPedido.vlVerbaItem);
			// --
			if (LavenderePdaConfig.isMostraValorDaUnidadePrecoPorEmbalagem()) {
				edVlUnidadeEmbalagem.setValue(getItemPedidoService().calculaVlUnidadePorEmbalagem(itemPedido, pedido));
			}
			if (itemPedido.getProduto().nuConversaoUnidadesMedida > 0 && !LavenderePdaConfig.usaUnidadeAlternativa) {
				edPrecoEmbPrimaria.setValue(itemPedido.vlItemPedido / itemPedido.getProduto().nuConversaoUnidadesMedida);
			} else if (!itemPedido.isEditandoValorElementar()) {
				edPrecoEmbPrimaria.setValue(isPermiteAlterarVlItemNaUnidadeElementar() ? itemPedido.vlEmbalagemElementar : getItemPedidoService().calculaVlUnidadePorEmbalagem(itemPedido, pedido));
			}
			if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) {
				edVlEmbalagemComReducaoSimples.setValue(edPrecoEmbPrimaria.getValueDouble() - (itemPedido.getItemTabelaPreco().vlReducaoOptanteSimples / itemPedido.qtEmbalagemElementar));
			}
			if (LavenderePdaConfig.pctMargemAgregada > 0) {
				edPctMargemAgregadoValueChange();
			}
			if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
				edVlPctDescQuantidade.setValue(itemPedido.vlPctFaixaDescQtd);
			}
		}
	}

	private void edPctMargemAgregadoValueChange() throws SQLException {
		edVlAgregadoSugerido.setValue(getItemPedidoService().calculaVlAgregadoSugerido(getItemPedido().vlItemPedido, edPctMargemAgregada.getValueDouble()));
	}

	private void edNuOrdemCompraValueChange() throws SQLException {
		getItemPedido().nuOrdemCompraCliente = edNuOrdemCompraCliente.getValue();
	}

	private void edNuSeqOrdemCompraValueChange() throws SQLException {
		if (ValueUtil.isNotEmpty(edNuSeqOrdemCompraCliente.getValue())) {
			int nuSeqOrdemCompraCliente = ValueUtil.getIntegerValue(edNuSeqOrdemCompraCliente.getValue());
			if (nuSeqOrdemCompraCliente <= 0) {
				edNuSeqOrdemCompraCliente.setValue(ValueUtil.VALOR_NI);
				throw new ValidationException(Messages.ITEMPEDIDO_ERRO_NUSEQ_INVALIDO);
			}
			getItemPedido().nuSeqOrdemCompraCliente = nuSeqOrdemCompraCliente;
		} else {
			edNuSeqOrdemCompraCliente.setValue(ValueUtil.VALOR_NI);
			getItemPedido().nuSeqOrdemCompraCliente = 0;
		}
	}

	protected void repaintEdVlItemPedido() throws SQLException {
		if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedido()) {
			if (ValueUtil.round(getItemPedido().vlItemPedido) != ValueUtil.round(edVlItemPedido.getValueDouble())) {
				edVlItemPedido.setForeColor(ColorUtil.componentsForeColor);
			}
		}
	}

	protected boolean isPermiteAlterarVlItemNaUnidadeElementar() {
		return LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && (edPrecoEmbPrimaria.isEditable() || !LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido()) && !edVlItemPedido.isEditable();
	}

	public void resizeListToFullScreen() {
		containerGrid.setRect(0, getTop(), FILL, barBottomContainer.isVisible() ? FILL - barBottomContainer.getHeight() : FILL);
		if (containerTotalizadoresMultIns.isDisplayed()) {
			containerTotalizadoresMultIns.setRect(LEFT, BOTTOM, FILL, sessaoTotalizadoresFiltroMultIns.getHeight() + sessaoTotalizadoresMultIns.getHeight());
		}
		containerGrid.initUI();
		listContainer.setRect(0, LavenderePdaConfig.exibeGrupoProdutoListaMaximizada && cbGrupoProduto1.isDisplayed() ? cbGrupoProduto1.getY2() + HEIGHT_GAP : TOP, FILL, containerTotalizadoresMultIns.isVisible() ? FILL - containerTotalizadoresMultIns.getHeight() : FILL);
		listContainer.initUI();
		listContainer.repaintContainers();
	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		inVendaUnitariaMode = !isEditing();
		if (getBaseCrudListForm() != null) {
			controlaVisibilidadeBotoesPreviousNext(getBaseCrudListForm().getListContainer());
		}
		if (!inVendaUnitariaMode && LavenderePdaConfig.usaAcessoDiretoGradeProdutosItemPedido && btGradeItemPedido.isEnabled()) {
			ControlEvent event = new ControlEvent(ControlEvent.PRESSED, btGradeItemPedido);
			postEvent(event);
		}
	}

	protected void repaintScreen(boolean recarregaInterface) throws SQLException {
		inVendaUnitariaMode = !inVendaUnitariaMode;
		if (inVendaUnitariaMode && LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && usaCameraParaLeituraCdBarras()) {
			UiUtil.add(barBottomContainer, btLeitorCamera, 2);
		}
	}

	// @Override
	protected void clearScreen() throws java.sql.SQLException {
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.informaVerbaManual || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaPositivaPorGrupoProdutoTabelaPreco) {
			edVlVerbaItem.setText("");
			edVlVerbaItemPositiva.setText("");
			edVlVerbaItemNegativa.setText("");
			// --
			double vlVerbaPedidoTmp = 0;
			vlVerbaPedidoTmp = LavenderePdaConfig.isMostraFlexPositivoPedido() ? pedido.vlVerbaPedidoPositivo + pedido.vlVerbaPedido : pedido.vlVerbaPedido;
			edVlVerbaPedido.setValue(vlVerbaPedidoTmp);
		}
		edQtItemFisico.setText("");
		edQtItemFaturamento.setText("");
		edVlItemPedido.setText("");
		edPreco.setText("");
		edVlBaseItemPedido.setText("");
		edVlTtItemComDeflator.setText("");
		edVlPctDesconto.setText("");
		edVlPctAcrescimo.setText("");
		edVlDesconto.setText("");
		edVlPctDesconto2.setText("");
		edVlDesconto2.setText("");
		edVlPctDesconto3.setText("");
		edVlDesconto3.setText("");
		lvEstoque.setText("");
		lvTotalPedido.setText("");
		edVlTotalPedido.setValue(pedido.vlTotalPedido);
		if (LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() || LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido()) {
			edVlPctMaxDesconto.setText("");
		}
		edVlPctPreviaDesc.setText("");
		lbDsProduto.setValue("");
		edVlItemComissao.setText("");
		edPctPesoCargaPedido.setText("");
		edQtPesoCargaPedido.setText("");
		if (pedido.isStatusPedidoNaoOcultaValoresComissao()) {
		edVlTotalComissao.setValue(pedido.vlComissaoPedido);
		}
		edVlST.setText("");
		edVlEmbalagemSt.setText("");
		edVlItemST.setText("");
		edVlItemStReverso.setText("");
		edVlTotalItemST.setText("");
		edVlPctDescontoStReverso.setText("");
		edVlTotalPedidoST.setValue(pedido.vlTtPedidoComSt);
		if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			edStItem.setText("");
			edStPedido.setValue(STService.getInstance().getVlTotalStPedido(pedido));
		}
		if (LavenderePdaConfig.isUsaPedidoEmConsignacao()) {
			edSaldoCreditoConsignacaoRestanteCliente.setValue(FichaFinanceiraService.getInstance().getVlSaldoConsignadoCliente(null, pedido.getCliente(), false));
		}
		edCaixaPadrao.setText("");
		edPrecoMaximoCons.setText("");
		edPrecoEmbPrimaria.setText("");
		edPrecoFabrica.setText("");
		edEmbalagem.setText("");
		edQtEmabalagemCompra.setText("");
		edVlEmbalagemElementTributos.setText("");
		edVlReducaoSimples.setText("");
		edVlFrete.setText("");
		edRentabilidadeItem.setText("");
		edRentabilidadePedido.setValue(pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false));
		edVlIndiceRentabItem.setText("");
		edVlIndiceRentabPedido.setValue(pedido.vlRentabilidade);
		edVlIndiceRentabEstimadoPedido.setText("");
		edVlUnidadeEmbalagem.setText("");
		edVlRetornoProduto.setText("");
		edQtEmbalagem.setText("");
		edVlEmbalagem.setText("");
		edVlVerbaManual.setText("");
		edNuMultiploIdeal.setText("");
		edQtMininimaVenda.setText("");
		edVlPctComissao.setText("");
		edVlPctDescPromocional.setText("");
		edPctMargemAgregada.setText("");
		edVlAgregadoSugerido.setText("");
		if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
			edQtPtItens.setText("");
			edQtPtPedido.setValue(pedido.qtPontosPedido);
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto()) {
			edVlMaxBonificacao.setText("");
		}
		edQtDesejada.setText("");
		if (LavenderePdaConfig.isApresentaQtdPreCarregadaDeVendaNoItemDoPedido() && !pedido.isGondola()) {
			edQtItemFisico.setValue(LavenderePdaConfig.apresentaQtdPreCarregadaDeVendaNoItemDoPedido);
			edQtDesejada.setValue(LavenderePdaConfig.apresentaQtdPreCarregadaDeVendaNoItemDoPedido);
		}
		if (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco) {
			edVlBaseFlex.setText("");
		}
		if (LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() && LavenderePdaConfig.isConfigCalculoPesoPedido() && LavenderePdaConfig.isUsaTipoFretePedido()) {
			edVlTotalFreteItem.setText("");
		}
		if (LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			edVlVerbaNecessaria.setText("");
			edVlNeutroItem.setText("");
		}
		if (LavenderePdaConfig.aplicaReducaoPrecoItemClienteOptanteSimples && LavenderePdaConfig.aplicaReducaoSimplesAposCalculoValorItem) {
			edVlEmbalagemComReducaoSimples.setText("");
		}
		edVlEmbalagemPrimariaST.setText("");
		edVlIpi.setText("");
		edVlUnitarioIpi.setText("");
		edVlItemIpi.setText("");
		edVlTotalItemIpi.setText("");
		edVlTributos.setText("");
		edVlUnitarioTributos.setText("");
		edVlItemTributos.setText("");
		edVlTotalItemTributos.setText("");
		edVlAliquotaIpi.setText("");
		edVlTotalPedidoIpi.setValue(pedido.vlTtPedidoComIpi);
		edVlTotalPedidoTributos.setValue(pedido.vlTtPedidoComTributos);
		edQtItemEstoquePositivo.setText("");
		edVlTotalItemPositivo.setText("");
		edVlTotalPedidoEstoquePositvo.setValue(pedido.vlTotalPedidoEstoquePositivo);
		edVlTotalItemFrete.setText("");
		edVlItemFrete.setText("");
		edVlTotalPedidoFrete.setValue(pedido.vlTotalPedido + pedido.vlFrete);
		edVlItemPedidoComDescontoCCP.setText("");
		edVlPctDescontoCCPItemPedido.setText("");
		edVlItemPedidoBruto.setText("");
		edVlTotalBrutoItemPedido.setText("");
		edVlTotalPedidoComTributosEDeducoes.setValue(pedido.vlFinalPedidoDescTribFrete);
		edFaixaComissaoRentabilidade.setText("");
		edFaixaComissaoRentabilidadeMinima.setText("");
		edVlPctComissaoPedido.setText("");
		edVlPctVerba.setText("");
		edVlPctDescCanal.setText("");
		edVlPctDescContratoCli.setText("");
		edVlSeguroItemPedido.setText("");
		edVlItemPedidoFreteESeguro.setText("");
		edVlTotalItemPedidoFreteESeguro.setText("");
		edPctMaxParticipacaoItemBonificacao.setText("");
		edPctAtualParticipacaoItemBonificacao.setText("");
		edPesoAtualPedido.setText("");
		edPesoMinimoItem.setText("");
		edVolumeItem.setText("");
		edVolumeTotalItem.setText("");
		edVolumePedido.setText("");
		edSaldoCreditoRestanteCliente.setText("");
		edVlTonFreteCliente.setText("");
		edVlFretePedido.setText("");
		edQtEstoqueNegociacao.setText("");
		edUltimoPrecoPraticado.setText("");
		edAliquotaSt.setText("");
		edDifStEIpi.setText("");
		edUltimoDescontoPraticado.setText("");
		edUltimoAcrescimoPraticado.setText("");
		if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			edVlPctDescCliente.setText("");
			edVlPctDescontoCondicao.setText("");
			edVlPctDescFrete.setText("");
		}
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoNoItem()) {
			edTxAntecipacao.setText(ValueUtil.VALOR_NI);
			edDtVencimentoPrecoProduto.setText(ValueUtil.VALOR_NI);
		}
		edVlTotalItemTabelaPreco.setText(ValueUtil.VALOR_NI);
		edPercIndiceFinanceiroCondPagto.setText(ValueUtil.VALOR_NI);
		edPctMaxDescProdutoCliente.setText(ValueUtil.VALOR_NI);
		edPctDescPoliticaInterpol.setText(ValueUtil.VALOR_NI);
		edVlDescPoliticaInterpol.setText(ValueUtil.VALOR_NI);
		edVlTotDescPoliticaInterpol.setText(ValueUtil.VALOR_NI);
		edVlPctDescEfetivo.setText(ValueUtil.VALOR_NI);
		edQtEstoquePrevisto.setText(ValueUtil.VALOR_NI);
		edDtEstoquePrevisto.setText(ValueUtil.VALOR_NI);
		edVlItemFreteTributacao.setText(ValueUtil.VALOR_NI);
		edVlTotalItemFreteTributacao.setText(ValueUtil.VALOR_NI);
		edVlTotalPedidoFreteTributacao.setText(ValueUtil.VALOR_NI);
		edConsisteConversaoUnidade.setText(Produto.TIPO_VALIDACAO_MULTIPLO_EMBALAGEM_BLOQUEAR);
		edVlTotalItemPorPeso.setText(ValueUtil.VALOR_ZERO);
		edVlPctDescCondPagto.setText(ValueUtil.VALOR_ZERO);
		edVlPctTotalMargemItem.setText(ValueUtil.VALOR_NI);
		edPctDescontoDoPedido.setText("");
		if (LavenderePdaConfig.isAplicaTaxaAntecipacaoMensalNoItem()) {
			edDtPagamento.setText(ValueUtil.VALOR_NI);
			edVlBaseAntecipacao.setText(ValueUtil.VALOR_ZERO);
		}
		edVlTotalItemTribFreteSeguro.setText(ValueUtil.VALOR_ZERO);
		edVlTotalItemUnitarioTribFreteSeguro.setText(ValueUtil.VALOR_ZERO);
		edVlTotalSeguroItemPedido.setText(ValueUtil.VALOR_ZERO);
		edVlPesoItemUnitario.setText(ValueUtil.VALOR_ZERO);
		edVlPesoTotalItem.setText(ValueUtil.VALOR_ZERO);
		edVlPontuacaoRealizadoItem.setText(ValueUtil.VALOR_ZERO);
		edVlPontuacaoBaseItem.setText(ValueUtil.VALOR_ZERO);
		edVlPctMargemRentabItem.setText(ValueUtil.VALOR_ZERO);
		edVlPctMargemRentabPedido.setText(ValueUtil.VALOR_ZERO);
		edQtItemGondola.setText(ValueUtil.VALOR_NI);
		edVlPctDescProgressivo.setText(ValueUtil.VALOR_ZERO);
		edQtSugerida.setText(ValueUtil.VALOR_NI);
	}

	public void criaLabelsDinamicos() {
		/* 1 */
		lbQtItemFisico = new LabelName(LavenderePdaConfig.usaConversaoUnidadesMedida ? Messages.ITEMPEDIDO_LABEL_QTITEMFISICOUN : Messages.ITEMPEDIDO_LABEL_QTITEMFISICO, RIGHT);
		new BaseToolTip(lbQtItemFisico, Messages.TOOLTIP_LABEL_QUANTIDADE_UNI);
		/* 2 */
		lbQtItemFaturamento = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFISICOFATURAMENTO, RIGHT);
		new BaseToolTip(lbQtItemFaturamento, Messages.TOOLTIP_LABEL_QUANTIDADE_EMB);
		/* 3 */
		lbVlPctDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTDESCONTO, RIGHT);
		new BaseToolTip(lbVlPctDesconto, Messages.TOOLTIP_LABEL_PERCDESCONTO);
		/* 4 */
		lbVlItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VLITEMPEDIDO, RIGHT);
		new BaseToolTip(lbVlItemPedido, Messages.TOOLTIP_LABEL_VLITEM);
		/* 5 */
		lbVlPctAcrescimo = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTACRESCIMO, RIGHT);
		new BaseToolTip(lbVlPctAcrescimo, Messages.TOOLTIP_LABEL_PERCACRESCIMO);
		/* 6 */
		lbVlTotalItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTALITEMPEDIDO, RIGHT);
		new BaseToolTip(lbVlTotalItemPedido, Messages.TOOLTIP_LABEL_VLTOTALITEM);
		/* 7 */
		lbVlTotalPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTALPEDIDO, RIGHT);
		new BaseToolTip(lbVlTotalPedido, Messages.TOOLTIP_LABEL_VLTOTALPEDIDO);
		/* 8 */
		lbVlPctVerba = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTVERBA, RIGHT);
		new BaseToolTip(lbVlPctVerba, Messages.TOOLTIP_LABEL_PERCVERBA);
		/* 9 */
		lbVlVerbaItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VERBA_ITEM, RIGHT);
		new BaseToolTip(lbVlVerbaItem, Messages.TOOLTIP_LABEL_VLVERBAITEM);
		/* 10 */
		lbVlVerbaPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VERBA_PEDIDO, RIGHT);
		new BaseToolTip(lbVlVerbaPedido, Messages.TOOLTIP_LABEL_VLTOTALVERBAPEDIDO);
		/* 11 */
		lbPrecoTab = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO, RIGHT);
		new BaseToolTip(lbPrecoTab, Messages.TOOLTIP_LABEL_VLITEMTABPRECO);
		/* 12 */
		lbVlBaseItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VLBASEITEMPEDIDO, RIGHT);
		new BaseToolTip(lbVlBaseItem, Messages.TOOLTIP_LABEL_VLBASEITEM);
		/* 13 */
		lbEstoque = new LabelName(Messages.ITEMPEDIDO_LABEL_ESTOQUE, RIGHT);
		new BaseToolTip(lbEstoque, Messages.ESTOQUE_NOME_ENTIDADE);
		/* 14 */
		lbVlPctMaxDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPERCMAXDESCONTO, RIGHT);
		new BaseToolTip(lbVlPctMaxDesconto, Messages.TOOLTIP_LABEL_PERCMAXDESC);
		/* 15 */
		lbVlPctPreviaDesc = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTPREVIADESC, RIGHT);
		tipPreviaDesc = new BaseToolTip(lbVlPctPreviaDesc, Messages.TOOLTIP_LABEL_PREVIADESCONTO);
		/* 16 */
		lbVlItemComissao = new LabelName(Messages.ITEMPEDIDO_LABEL_VLCOMISSAOITEM, RIGHT);
		new BaseToolTip(lbVlItemComissao, Messages.TOOLTIP_LABEL_VLCOMISSAOITEM);
		/* 17 */
		lbVlTotalComissao = new LabelName(Messages.ITEMPEDIDO_LABEL_VLCOMISSAOPEDIDO, RIGHT);
		new BaseToolTip(lbVlTotalComissao, Messages.TOOLTIP_LABEL_VLCOMISSAOPEDIDO);
		/* 18 */
		lbVlST = new LabelName(Messages.ITEMPEDIDO_LABEL_VLST, RIGHT);
		new BaseToolTip(lbVlST, Messages.TOOLTIP_LABEL_VLST);
		/* 19 */
		lbVlItemSt = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEM_COM_ST, RIGHT);
		new BaseToolTip(lbVlItemSt, Messages.TOOLTIP_LABEL_VL_ITEM_COM_ST);
		/* 20 */
		lbVlTotalItemSt = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_COM_ST, RIGHT);
		new BaseToolTip(lbVlTotalItemSt, Messages.TOOLTIP_LABEL_VL_TOTAL_ITEM_COM_ST);
		/* 21 */
		lbCaixaPadrao = new LabelName(Messages.ITEMPEDIDO_LABEL_CAIXAPADRAO, RIGHT);
		new BaseToolTip(lbCaixaPadrao, Messages.TOOLTIP_LABEL_CAIXAPADRAO);
		/* 22 */
		lbPrecoFabrica = new LabelName(Messages.ITEMPEDIDO_LABEL_PRECOFABRICA, RIGHT);
		new BaseToolTip(lbPrecoFabrica, Messages.TOOLTIP_LABEL_PRECOFABRICA);
		/* 23 */
		lbPrecoMaximoCons = new LabelName(Messages.ITEMPEDIDO_LABEL_PRECOMAXIMOCONSUMIDOR, RIGHT);
		new BaseToolTip(lbPrecoMaximoCons, Messages.TOOLTIP_LABEL_PRECOMAXIMOCONSUMIDOR);
		/* 24 */
		lbEmbalagem = new LabelName(Messages.ITEMPEDIDO_LABEL_QTITEMFATURAMENTO, RIGHT);
		new BaseToolTip(lbEmbalagem, Messages.TOOLTIP_LABEL_EMBALAGEM);
		/* 25 */
		lbVlBaseFlex = new LabelName(Messages.ITEMTABELAPRECO_LABEL_VLBASEFLEX, RIGHT);
		new BaseToolTip(lbVlBaseFlex, Messages.TOOLTIP_LABEL_VLBASEFLEX);
		/* 26 */
		lbTicketMedioPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_TICKET_MEDIO_PEDIDO, RIGHT);
		new BaseToolTip(lbTicketMedioPedido, Messages.TOOLTIP_LABEL_TICKET_MEDIO_PEDIDO);
		/* 27 */
		lbVlFrete = new LabelName(Messages.ITEMPEDIDO_LABEL_VLFRETE, RIGHT);
		new BaseToolTip(lbVlFrete, Messages.TOOLTIP_LABEL_VLFRETE);
		/* 28 */
		lbVlTtItemComDeflator = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTTITEMCOMDEFLATOR, RIGHT);
		new BaseToolTip(lbVlTtItemComDeflator, Messages.TOOLTIP_LABEL_VLTOTALITEMDESCONTO);
		/* 29 */
		lbPrecoEmbPrimaria = new LabelName(Messages.ITEMPEDIDO_LABEL_PRECOEMBPRIMARIA, RIGHT);
		new BaseToolTip(lbPrecoEmbPrimaria, Messages.TOOLTIP_LABEL_PRECOEMBPRIMARIA);
		/* 30 */
		lbRentabilidadeItem = new LabelName(Messages.ITEMPEDIDO_LABEL_RENTABILIDADE_ITEMPEDIDO, RIGHT);
		new BaseToolTip(lbRentabilidadeItem, Messages.TOOLTIP_LABEL_RENTABILIDADE_ITEM);
		/* 31 */
		lbRentabilidadePedido = new LabelName(Messages.ITEMPEDIDO_LABEL_RENTABILIDADE_PEDIDO, RIGHT);
		new BaseToolTip(lbRentabilidadePedido, Messages.TOOLTIP_LABEL_RENTABILIDADE_PEDIDO);
		/* 32 */
		lbVlUnidadeEmbalagem = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_UNIDADE_RENTABILIDADE, RIGHT);
		new BaseToolTip(lbVlUnidadeEmbalagem, Messages.TOOLTIP_LABEL_VL_UNIDADE_EMBALAGEM);
		/* 33 */
		lbVlReducaoSimples = new LabelName(Messages.ITEMPEDIDO_LABEL_VLREDUCAOSIMPLES, RIGHT);
		new BaseToolTip(lbVlReducaoSimples, Messages.TOOLTIP_LABEL_VL_REDUCAO_SIMPLES);
		/* 34 */
		lbVlTotalPedidoST = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_PEDIDO_COM_ST, RIGHT);
		new BaseToolTip(lbVlTotalPedidoST, Messages.TOOLTIP_LABEL_VL_TOTAL_PEDIDO_COM_ST);
		/* 35 */
		lbQtEmabalagemCompra = new LabelName(Messages.ITEMPEDIDO_LABEL_QTEMBCOMPRA, RIGHT);
		new BaseToolTip(lbQtEmabalagemCompra, Messages.TOOLTIP_LABEL_QT_EMB_COMPRA);
		/* 36 */
		lbVlVerbaManual = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_VERBA_MANUAL, RIGHT);
		new BaseToolTip(lbVlVerbaManual, Messages.TOOLTIP_LABEL_VL_VERBA_MANUAL);
		/* 37 */
		lbVlPctDescPromocional = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PCT_DESC_PROMOCIONAL, RIGHT);
		new BaseToolTip(lbVlPctDescPromocional, Messages.TOOLTIP_LABEL_VL_PCT_DESC_PROMOCIONAL);
		/* 38 */
		lbQtPtItens = new LabelName(Messages.ITEMPEDIDO_LABEL_QT_PONTOS_ITENS, RIGHT);
		new BaseToolTip(lbQtPtItens, Messages.TOOLTIP_LABEL_QT_PONTOS_ITENS);
		/* 39 */
		lbQtPtPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_QT_PONTOS_PEDIDO, RIGHT);
		new BaseToolTip(lbQtPtPedido, Messages.TOOLTIP_LABEL_QT_PONTOS_PEDIDO);
		/* 40 */
		lbVlMaxBonificacao = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_MAX_BONIFICACAO, RIGHT);
		new BaseToolTip(lbVlMaxBonificacao, Messages.TOOLTIP_LABEL_VL_MAX_BONIFICACAO);
		/* 41 */
		lbUnidadeAlternativa = new LabelName(Messages.ITEMPEDIDO_LABEL_UNIDADE_ALTERNATIVA, RIGHT);
		new BaseToolTip(lbUnidadeAlternativa, Messages.TOOLTIP_LABEL_UNIDADE_ALTERNATIVA);
		/* 42 */
		lbQtMininimaVenda = new LabelName(Messages.ITEMPEDIDO_LABEL_QT_MINIMA_VENDA, RIGHT);
		new BaseToolTip(lbQtMininimaVenda, Messages.TOOLTIP_LABEL_QT_MINIMA_VENDA);
		/* 43 */
		lbNuMultiploIdeal = new LabelName(Messages.ITEMPEDIDO_LABEL_MULTIPLO_IDEAL, RIGHT);
		new BaseToolTip(lbNuMultiploIdeal, Messages.TOOLTIP_LABEL_MULTIPLO_IDEAL);
		/* 44 */
		lbVlPctComissao = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_COMISSAO, RIGHT);
		new BaseToolTip(lbVlPctComissao, Messages.TOOLTIP_LABEL_PCT_COMISSAO);
		/* 45 */
		lbVlIndiceRentabItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_RENTAB_ITEM, RIGHT);
		new BaseToolTip(lbVlIndiceRentabItem, Messages.TOOLTIP_LABEL_VL_RENTAB_ITEM);
		/* 46 */
		lbVlIndiceRentabPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_RENTAB_PEDIDO, RIGHT);
		new BaseToolTip(lbVlIndiceRentabPedido, Messages.TOOLTIP_LABEL_VL_RENTAB_PEDIDO);
		/* 47 */
		lbQtEmbalagem = new LabelName(Messages.ITEMPEDIDO_LABEL_QT_EMBALAGEM, RIGHT);
		new BaseToolTip(lbQtEmbalagem, Messages.TOOLTIP_LABEL_QT_EMBALAGEM);
		/* 48 */
		lbVlEmbalagem = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_EMBALAGEM, RIGHT);
		new BaseToolTip(lbVlEmbalagem, Messages.TOOLTIP_LABEL_VL_EMBALAGEM);
		/* 49 */
		lbPctMargemAgregada = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_MARGEM_AGREGADA, RIGHT);
		new BaseToolTip(lbPctMargemAgregada, Messages.TOOLTIP_LABEL_PCT_MARGEM_AGREGADA);
		/* 50 */
		lbVlAgregadoSugerido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_AGREGADO_SUGERIDO, RIGHT);
		new BaseToolTip(lbVlAgregadoSugerido, Messages.TOOLTIP_LABEL_VL_AGREGADO_SUGERIDO);
		/* 51 */
		lbOportunidade = new LabelName(Messages.OPORTUNIDADE_ABREVIADA, RIGHT);
		new BaseToolTip(lbOportunidade, Messages.OPORTUNIDADE_LABEL);
		/* 52 */
		lbVlItemTabPrecoVariacaoPreco = new LabelName(Messages.LABEL_ITEM_VARIACAO_PRECO, RIGHT);
		new BaseToolTip(lbVlItemTabPrecoVariacaoPreco, Messages.TOOLTIP_ITEM_VARIACAO_PRECO);
		/* 53 */
		lbVlIpi = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_IPI, RIGHT);
		new BaseToolTip(lbVlIpi, Messages.TOOLTIP_LABEL_VL_IPI);
		/* 54 */
		lbVlItemIpi = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEM_IPI, RIGHT);
		new BaseToolTip(lbVlItemIpi, Messages.TOOLTIP_LABEL_VL_ITEM_IPI);
		/* 55 */
		lbVlTotalItemIpi = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_IPI, RIGHT);
		new BaseToolTip(lbVlTotalItemIpi, Messages.TOOLTIP_LABEL_VL_TOTAL_ITEM_IPI);
		/* 56 */
		lbVlTributos = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TRIBUTOS, RIGHT);
		new BaseToolTip(lbVlTributos, Messages.TOOLTIP_LABEL_VL_TRIBUTOS);
		/* 57 */
		lbVlItemTributos = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEM_TRIBUTOS, RIGHT);
		new BaseToolTip(lbVlItemTributos, Messages.TOOLTIP_LABEL_VL_ITEM_TRIBUTOS);
		/* 58 */
		lbVlTotalItemTributos = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_TRIBUTOS, RIGHT);
		new BaseToolTip(lbVlTotalItemTributos, Messages.TOOLTIP_LABEL_VL_TOTAL_ITEM_TRIBUTOS);
		/* 59 */
		lbVlAliquotaIpi = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_ALIQUOTA_IPI, RIGHT);
		new BaseToolTip(lbVlAliquotaIpi, Messages.TOOLTIP_LABEL_PCT_ALIQUOTA_IPI);
		/* 60 */
		lbVlTotalPedidoIpi = new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_PEDIDO_IPI, RIGHT);
		new BaseToolTip(lbVlTotalPedidoIpi, Messages.TOOLTIP_LABEL_VL_TOTAL_PEDIDO_IPI);
		/* 61 */
		lbVlTotalPedidoTributos = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_PEDIDO_TRIBUTOS, RIGHT);
		new BaseToolTip(lbVlTotalPedidoTributos, Messages.TOOLTIP_LABEL_VL_TOTAL_PEDIDO_TRIBUTOS);
		/* 62 */
		lbQtItemEstoquePositivo = new LabelName(Messages.PEDIDO_LABEL_QTD_COM_ESTOQUE_PEDIDO, RIGHT);
		new BaseToolTip(lbQtItemEstoquePositivo, Messages.TOOLTIP_LABEL_QTD_COM_ESTOQUE_PEDIDO);
		/* 63 */
		lbVlTotalItemPositivo = new LabelName(Messages.PEDIDO_LABEL_VL_ITEM_COM_ESTOQUE_PEDIDO, RIGHT);
		new BaseToolTip(lbVlTotalItemPositivo, Messages.TOOLTIP_LABEL_VL_ITEM_COM_ESTOQUE_PEDIDO);
		/* 64 */
		lbVlTotalPedidoEstoquePositvo = new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_QTD_COM_ESTOQUE_PEDIDO, RIGHT);
		new BaseToolTip(lbVlTotalPedidoEstoquePositvo, Messages.TOOLTIP_LABEL_VL_TOTAL_QTD_COM_ESTOQUE_PEDIDO);
		/* 65 */
		lbVlTotalItemFrete = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTAL_ITEM_COM_FRETE, RIGHT);
		new BaseToolTip(lbVlTotalItemFrete, Messages.TOOLTIP_LABEL_VL_TOTAL_ITEM_PEDIDO_COM_FRETE);
		/* 66 */
		lbVlItemFrete = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEM_COM_FRETE, RIGHT);
		new BaseToolTip(lbVlItemFrete, Messages.TOOLTIP_LABEL_VL_ITEM_PEDIDO_COM_FRETE);
		/* 67 */
		lbVlTotalPedidoFrete = new LabelName(Messages.PEDIDO_LABEL_VL_TOTAL_ITEM_COM_FRETE, RIGHT);
		new BaseToolTip(lbVlTotalPedidoFrete, Messages.TOOLTIP_LABEL_VL_TOTAL_PEDIDO_COM_FRETE);
		/* 68 */
		lbVlItemPedidoComDescontoCPP = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEM_PEDIDO_COM_DESCONTO_CPP, RIGHT);
		new BaseToolTip(lbVlItemPedidoComDescontoCPP, Messages.TOOLTIP_LABEL_VL_ITEM_PEDIDO_COM_DESCONTO_CPP);
		/* 69 */
		lbVlPctDescontoCPPItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_CPP, RIGHT);
		new BaseToolTip(lbVlPctDescontoCPPItemPedido, Messages.TOOLTIP_LABEL_PCT_CPP);
		/* 70 */
		lbVlItemPedidoBruto = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEMPEDIDO_BRUTO, RIGHT);
		new BaseToolTip(lbVlItemPedidoBruto, Messages.TOOLTIP_LABEL_VL_ITEMPEDIDO_BRUTO);
		/* 71 */
		lbVlTotalBrutoItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTALBRUTO_ITEMPEDIDO, RIGHT);
		new BaseToolTip(lbVlTotalBrutoItemPedido, Messages.TOOLTIP_LABEL_VL_TOTALBRUTO_ITEMPEDIDO);
		/* 72 */
		lbVlTotalPedidoBruto = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TOTALPEDIDOBRUTO, RIGHT);
		new BaseToolTip(lbVlTotalPedidoBruto, Messages.TOOLTIP_LABEL_VL_TOTALPEDIDOBRUTO);
		/* 73 */
		lbVlTotalFreteItem = new LabelName(Messages.LABEL_FRETE_ABREV, RIGHT);
		new BaseToolTip(lbVlTotalFreteItem, Messages.LABEL_FRETE);
		/* 74 */
		lbVlEmbalagemElementTributos = new LabelName(Messages.VALOR_EMBALAGEM_IMPOSTOS_ABREV, RIGHT);
		new BaseToolTip(lbVlEmbalagemElementTributos, Messages.VALOR_EMBALAGEM_IMPOSTOS);
		/* 75 */
		lbVlDesconto = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_DESCONTO, RIGHT);
		new BaseToolTip(lbVlDesconto, Messages.TOOLTIP_LABEL_VL_DESCONTO);
		/* 76 */
		lbVlPctDesconto2 = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_DESCONTO2, RIGHT);
		new BaseToolTip(lbVlPctDesconto2, Messages.TOOLTIP_LABEL_PCT_DESCONTO2);
		/* 77 */
		lbVlDesconto2 = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_DESCONTO2, RIGHT);
		new BaseToolTip(lbVlDesconto2, Messages.TOOLTIP_LABEL_VL_DESCONTO2);
		/* 78 */
		lbVlPctDesconto3 = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_DESCONTO3, RIGHT);
		new BaseToolTip(lbVlPctDesconto3, Messages.TOOLTIP_LABEL_PCT_DESCONTO3);
		/* 79 */
		lbVlDesconto3 = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_DESCONTO3, RIGHT);
		new BaseToolTip(lbVlDesconto3, Messages.TOOLTIP_LABEL_VL_DESCONTO3);
		/* 80 */
		lbFaixaComissaoRentabilidade = new LabelName(Messages.ITEMPEDIDO_LABEL_FAIXA_COMISSAO_RENTABILIDADE, RIGHT);
		new BaseToolTip(lbFaixaComissaoRentabilidade, Messages.TOOLTIP_LABEL_FAIXA_RENTABILIDADE);
		/* 81 */
		lbFaixaComissaoRentabilidadeMinima = new LabelName(Messages.ITEMPEDIDO_LABEL_FAIXA_COMISSAO_RENTABILIDADE_MINIMA, RIGHT);
		new BaseToolTip(lbFaixaComissaoRentabilidadeMinima,MessageUtil.quebraLinhas(Messages.TOOLTIP_LABEL_FAIXA_RENTABILIDADE_MINIMA));
		/* 82 */
		lbVlPctComissaoPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PCT_COMISSAO_PEDIDO, RIGHT);
		new BaseToolTip(lbVlPctComissaoPedido, Messages.TOOLTIP_LABEL_VL_PCT_COMISSAO_PEDIDO);
		/* 83 */
		lbQtPesoCargaPedido = new LabelName(Messages.CARGAPEDIDO_LABEL_QTPESO, RIGHT);
		new BaseToolTip(lbQtPesoCargaPedido, Messages.CARGAPEDIDO_TOOLTIP_QTPESO);
		/* 84 */
		lbPctPesoCargaPedido = new LabelName(Messages.CARGAPEDIDO_LABEL_PCTPESO, RIGHT);
		new BaseToolTip(lbPctPesoCargaPedido, MessageUtil.quebraLinhas(Messages.CARGAPEDIDO_TOOLTIP_PCTPESO));
		/* 85 */
		lbVlVerbaItemPositiva = new LabelName(Messages.ITEMPEDIDO_LABEL_VERBA_ITEM_POSITIVA, RIGHT);
		new BaseToolTip(lbVlVerbaItemPositiva, Messages.TOOLTIP_LABEL_VLVERBAITEMPOSITIVA);
		/* 86 */
		lbVlVerbaItemNegativa = new LabelName(Messages.ITEMPEDIDO_LABEL_VERBA_ITEM_NEGATIVA, RIGHT);
		new BaseToolTip(lbVlVerbaItemNegativa, Messages.TOOLTIP_LABEL_VLVERBAITEMNEGATIVA);
		/* 87 */
		lbVlPctDescCanal = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PCT_DESC_CANAL, RIGHT);
		new BaseToolTip(lbVlPctDescCanal, Messages.TOOLTIP_LABEL_VL_PCT_DESC_CANAL);
		/* 88 */
		lbVlPctDescContratoCli = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PCT_DESC_CONTRATO, RIGHT);
		new BaseToolTip(lbVlPctDescContratoCli, Messages.TOOLTIP_LABEL_VL_PCT_DESC_CONTRATO);
		/* 89 */
		lbGeraVerba = new LabelName(Messages.GERAVERBA_ABREVIADA, RIGHT);
		new BaseToolTip(lbGeraVerba, Messages.GERAVERBA_LABEL);
		/* 90 */
		lbVlVerbaNecessaria = new LabelName(Messages.VERBA_NECESSARIA_ABREVIADA, RIGHT);
		new BaseToolTip(lbVlVerbaNecessaria, Messages.VERBA_NECESSARIA_LABEL);
		/* 91 */
		lbVlNeutroItem = new LabelName(Messages.VALOR_NEUTRO_ABREVIADA, RIGHT);
		new BaseToolTip(lbVlNeutroItem, Messages.VALOR_NEUTRO_LABEL);
		/* 92 */
		lbVlSeguroItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_SEGURO, RIGHT);
		new BaseToolTip(lbVlSeguroItemPedido, Messages.TOOLTIP_LABEL_VL_SEGURO);
		/* 93 */
		lbVlEmbalagemComReducaoSimples = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_EMBALAGEM_COM_REDUCAO_SIMPLES,RIGHT);
		new BaseToolTip(lbVlEmbalagemComReducaoSimples, Messages.TOOLTIP_LABEL_VL_EMBALAGEM_COM_REDUCAO_SIMPLES);
		/* 94 */
		lbVlEmbalagemPrimariaST = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_EMBALAGEM_PRIMARIA_ST, RIGHT);
		new BaseToolTip(lbVlEmbalagemPrimariaST, Messages.TOOLTIP_LABEL_VL_EMBALAGEM_PRIMARIA_ST);
		/* 95 */
		lbVlItemPedidoFreteESeguro = new LabelName(Messages.LABEL_VLITEM_FRETE_SEGURO, RIGHT);
		new BaseToolTip(lbVlItemPedidoFreteESeguro, Messages.TOOLTIP_VLITEM_FRETE_SEGURO);
		/* 96 */
		lbVlTotalItemPedidoFreteESeguro = new LabelName(Messages.LABEL_VLTOTALITEM_FRETE_SEGURO, RIGHT);
		new BaseToolTip(lbVlTotalItemPedidoFreteESeguro, Messages.TOOLTIP_VLTOTALITEM_FRETE_SEGURO);
		/* 97 */
		lbPctMaxParticipacaoItemBonificacao = new LabelName(Messages.ITEMPEDIDO_LABEL_PERCENTUAL_MAXIMO_PARTICIPACAO,RIGHT);
		new BaseToolTip(lbPctMaxParticipacaoItemBonificacao, Messages.TOOLTIP_LABEL_PCT_MAXIMO_PARTICIPACAO);
		/* 98 */
		lbPctAtualParticipacaoItemBonificacao = new LabelName(Messages.ITEMPEDIDO_LABEL_PERCENTUAL_ATUAL_PARTICIPACAO,RIGHT);
		new BaseToolTip(lbPctAtualParticipacaoItemBonificacao, Messages.TOOLTIP_LABEL_PCT_ATUAL_PARTICIPACAO);
		/* 99 */
		lbPesoAtualPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_PESO_ATUAL_PEDIDO, RIGHT);
		new BaseToolTip(lbPesoAtualPedido, Messages.TOOLTIP_LABEL_PESO_ATUAL_PEDIDO);
		/* 100 */
		lbPesoMinimoItem = new LabelName(Messages.ITEMPEDIDO_LABEL_PESO_MINIMO_ITEM, RIGHT);
		new BaseToolTip(lbPesoMinimoItem, Messages.TOOLTIP_LABEL_PESO_MINIMO_ITEM);
		/* 101 */
		lbVolumeItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VOLUME_ITEM, RIGHT);
		new BaseToolTip(lbVolumeItem, Messages.TOOLTIP_LABEL_VOLUME_ITEM);
		/* 102 */
		lbVolumeTotalItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VOLUME_TOTAL_ITEM, RIGHT);
		new BaseToolTip(lbVolumeTotalItem, Messages.TOOLTIP_LABEL_VOLUME_TOTAL_ITEM);
		/* 103 */
		lbVolumePedido = new LabelName(Messages.PEDIDO_LABEL_VOLUME_PEDIDO, RIGHT);
		new BaseToolTip(lbVolumePedido, Messages.TOOLTIP_LABEL_VOLUME_PEDIDO);
		/* 104 */
		lbSaldoCreditoRestanteCliente = new LabelName(Messages.PEDIDO_LABEL_SALDO_CREDITO_RESTANTE_CLIENTE, RIGHT);
		new BaseToolTip(lbSaldoCreditoRestanteCliente, Messages.TOOLTIP_LABEL_SALDO_CREDITO_RESTANTE_CLIENTE);
		/* 105 */
		lbItemTroca = new LabelName(Messages.LABEL_ITEM_TROCA, RIGHT);
		new BaseToolTip(lbItemTroca, Messages.TOOLTIP_LABEL_ITEM_TROCA);
		/* 106 */
		lbStItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ST_ITEM, RIGHT);
		new BaseToolTip(lbStItem, Messages.TOOLTIP_LABEL_VL_ST_ITEM);
		/* 107 */
		lbStPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ST_PEDIDO, RIGHT);
		new BaseToolTip(lbStPedido, Messages.TOOLTIP_LABEL_VL_ST_PEDIDO);
		/* 108 */
		lbVlUnitarioTributos = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_TRIBUTOS, RIGHT);
		new BaseToolTip(lbVlUnitarioTributos, Messages.TOOLTIP_LABEL_VL_UNITARIO_TRIBUTOS);
		/* 109 */
		lbVlEmbalagemSt = new LabelName(Messages.ITEMPEDIDO_LABEL_VLST, RIGHT);
		new BaseToolTip(lbVlEmbalagemSt, Messages.TOOLTIP_LABEL_UNITARIO_VLST);
		/* 110 */
		lbVlUnitarioIpi = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_IPI, RIGHT);
		new BaseToolTip(lbVlUnitarioIpi, Messages.TOOLTIP_LABEL_VL_UNITARIO_IPI);
		/* 111 */
		lbSaldoCreditoConsignacaoRestanteCliente = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_SALDO_CREDITO_CONSIGNACAO_RESTANTE, RIGHT);
		new BaseToolTip(lbSaldoCreditoConsignacaoRestanteCliente, Messages.TOOLTIP_LABEL_VL_SALDO_CREDITO_CONSIGNACAO_RESTANTE);
		/* 112 */
		lbVlItemStReverso = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_ITEM_COM_ST, RIGHT);
		new BaseToolTip(lbVlItemStReverso, Messages.TOOLTIP_LABEL_VL_FINAL_ITEM_ST);
		/* 113 */
		lbVlDescontoStReverso = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_DESC_ST, RIGHT);
		new BaseToolTip(lbVlDescontoStReverso, Messages.TOOLTIP_LABEL_VL_DESC_ST);
		/* 114 */
		lbVlTonFreteCliente = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_FRETE_TON_CLIENTE, RIGHT);
		new BaseToolTip(lbVlTonFreteCliente, Messages.TOOLTIP_LABEL_VL_FRETE_TON_CLIENTE);
		/* 115 */
		lbVlFretePedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_FRETE_PEDIDO, RIGHT);
		new BaseToolTip(lbVlFretePedido, Messages.TOOLTIP_LABEL_VL_FRETE_PEDIDO);
		/* 119 */
		lbQtEstoqueCondicaoNegociacao = new LabelName(Messages.ITEMPEDIDO_LABEL_QT_ESTOQUE_CONDICAO_NEGOCIACAO, RIGHT);
		new BaseToolTip(lbQtEstoqueCondicaoNegociacao, Messages.TOOLTIP_LABEL_QT_ESTOQUE_CONDICAO_NEGOCIACAO);
		/* 116 */
		lbVlPctDescCliente = new LabelName(Messages.ITEMPEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_CLIENTE, RIGHT);
		new BaseToolTip(lbVlPctDescCliente, Messages.TOOLTIP_LABEL_DESCONTO_CASCATA_MANUAL_CLIENTE);
		/* 117 */
		lbVlPctDescontoCondicao = new LabelName(Messages.ITEMPEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_2, RIGHT);
		new BaseToolTip(lbVlPctDescontoCondicao, Messages.TOOLTIP_LABEL_DESCONTO_CASCATA_MANUAL_2);
		/* 118 */
		lbVlPctDescFrete = new LabelName(Messages.ITEMPEDIDO_LABEL_DESCONTO_CASCATA_MANUAL_3, RIGHT);
		new BaseToolTip(lbVlPctDescFrete, Messages.TOOLTIP_LABEL_DESCONTO_CASCATA_MANUAL_3);
		/* 120 */
		lbUltimoPrecoPraticado = new LabelName(Messages.ITEMPEDIDO_LABEL_ULTIMO_PRECO, RIGHT);
		new BaseToolTip(lbUltimoPrecoPraticado, Messages.TOOLTIP_LABEL_ULTIMO_PRECO);
		/* 121 */
		lbAliquotaSt = new LabelName(Messages.ITEMPEDIDO_LABEL_ALIQUOTA_ST, RIGHT);
		new BaseToolTip(lbAliquotaSt, Messages.TOOLTIP_LABEL_PERCENTUAL_ST);
		/* 122 */
		lbDifStEIpi = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_DIFERENCA_ST_IPI, RIGHT);
		new BaseToolTip(lbDifStEIpi, Messages.TOOLTIP_LABEL_DIFERENCA_ST_IPI);
		/* 123 */
		lbQtDesejada = new LabelName(Messages.ITEMPEDIDO_LABEL_QTD_DESEJADA, RIGHT);
		new BaseToolTip(lbQtDesejada, Messages.TOOLTIP_LABEL_QTD_DESEJADA);
		/* 124 */
		lbUltimoDescontoPraticado = new LabelName(Messages.ITEMPEDIDO_LABEL_ULTIMO_DESCONTO_PRATICADO, RIGHT);
		new BaseToolTip(lbUltimoDescontoPraticado, Messages.TOOLTIP_LABEL_ULTIMO_DESCONTO_PRATICADO);
		/* 125 */
		lbUltimoAcrescimoPraticado = new LabelName(Messages.ITEMPEDIDO_LABEL_ULTIMO_ACRESCIMO_PRATICADO, RIGHT);
		new BaseToolTip(lbUltimoAcrescimoPraticado, Messages.TOOLTIP_LABEL_ULTIMO_ACRESCIMO_PRATICADO);
		/* 126 */
		lbTxAntecipacao = new LabelName(Messages.ITEMPEDIDO_LABEL_TAXA_ANTECIPACAO, RIGHT);
		new BaseToolTip(lbTxAntecipacao, Messages.TOOLTIP_LABEL_TAXA_ANTECIPACAO);
		/* 127 */
		lbDtVencimentoPrecoProduto = new LabelName(Messages.ITEMPEDIDO_LABEL_DTVENCIMENTO_PRECO_PRODUTO, RIGHT);
		new BaseToolTip(lbDtVencimentoPrecoProduto, Messages.TOOLTIP_LABEL_DTVENCIMENTO_PRECO_PRODUTO);
		/* 128 */
		lbVlTotalItemTabelaPreco = new LabelName(Messages.ITEMPEDIDO_LABEL_VLUNITARIOITEMTABELAPRECO, RIGHT);
		new BaseToolTip(lbVlTotalItemTabelaPreco, Messages.TOOLTIP_LABEL_VLTOTALITEMTABELAPRECO);
		/* 129 */
		lbPercIndiceFinanceiroCondPagto = new LabelName(Messages.ITEMPEDIDO_LABEL_PERCT_INDICE_FINANCEIRO_COND_PAGTO,RIGHT);
		new BaseToolTip(lbPercIndiceFinanceiroCondPagto, Messages.TOOLTIP_LABEL_PERCT_INDICE_FINANCEIRO_COND_PAGTO);
		/* 130 */
		lbPctMaxDescProdutoCliente = new LabelName(Messages.ITEMPEDIDO_LABEL_PERCT_DESCONTO_MAXIMO, RIGHT);
		new BaseToolTip(lbPctMaxDescProdutoCliente, Messages.TOOLTIP_LABEL_PERCT_DESCONTO_MAXIMO);
		/* 131 */
		lbVlIndiceRentabEstimadoPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_RENTAB_ESTIMADO_PEDIDO, RIGHT);
		new BaseToolTip(lbVlIndiceRentabEstimadoPedido, Messages.TOOLTIP_LABEL_VL_RENTAB_ESTIMADO_PEDIDO);
		/* 132 */
		lbVlRetornoProduto = new LabelName(Messages.ITEMPEDIDO_VL_RETORNO_PRODUTO, RIGHT);
		new BaseToolTip(lbVlRetornoProduto, Messages.TOOLTIP_LABEL_VL_RETORNO_PRODUTO);
		/* 133 */
		lbPctDescPoliticaInterpol = new LabelName(Messages.ITEMPEDIDO_LABEL_PERCENT_DESC_POLITICA_INTERPOLACAO, RIGHT);
		new BaseToolTip(lbPctDescPoliticaInterpol, Messages.TOOLTIP_LABEL_PERCENT_DESC_POLITICA_INTERPOLACAO);
		/* 134 */
		lbVlDescPoliticaInterpol = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_DESC_POLITICA_INTERPOLACAO, RIGHT);
		new BaseToolTip(lbVlDescPoliticaInterpol, Messages.TOOLTIP_LABEL_VALOR_DESC_POLITICA_INTERPOLACAO);
		/* 135 */
		lbVlTotDescPoliticaInterpol = new LabelName(Messages.ITEMPEDIDO_LABEL_VALORTOTAL_DESC_POLITICA_INTERPOLACAO, RIGHT);
		new BaseToolTip(lbVlTotDescPoliticaInterpol, Messages.TOOLTIP_LABEL_VALORTOTAL_DESC_POLITICA_INTERPOLACAO);
		/* 136 */
		lbVlPctDescEfetivo = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_PERCENT_DESC_EFETIVO, RIGHT);
		new BaseToolTip(lbVlPctDescEfetivo, Messages.TOOLTIP_LABEL_VALOR_PERCENT_DESC_EFETIVO);
		/* 137 */
		lbVlQtEstoquePrevisto = new LabelName(Messages.ITEMPEDIDO_LABEL_QTDE_ESTOQUE_PREVISTO, RIGHT);
		new BaseToolTip(lbVlQtEstoquePrevisto, Messages.TOOLTIP_LABEL_QTDE_ESTOQUE_PREVISTO);
		/* 138 */
		lbDtEstoquePrevisto = new LabelName(Messages.ITEMPEDIDO_LABEL_DATA_ESTOQUE_PREVISTO, RIGHT);
		new BaseToolTip(lbDtEstoquePrevisto, Messages.TOOLTIP_LABEL_DATA_ESTOQUE_PREVISTO);
		/* 139 */
		lbConsisteConversaoUnidade = new LabelName(Messages.ITEMPEDIDO_LABEL_VALIDACAOMULTIPLOEMBALAGEM, RIGHT);
		new BaseToolTip(lbConsisteConversaoUnidade, Messages.TOOLTIP_LABEL_VALIDACAOMULTIPLOEMBALAGEM);
		/* 140 */
		lbVlPctTotalMargemItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTTOTALMARGEMITEM, RIGHT);
		new BaseToolTip(lbVlPctTotalMargemItem, Messages.ITEMPEDIDO_LABEL_VLPCTTOTALMARGEMITEM_TOOLTIP);
		/* 141 */
		lbVlItemFreteTributacao = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_FRETE_TRIB, RIGHT);
		new BaseToolTip(lbVlItemFreteTributacao, Messages.TOOLTIP_LABEL_VALOR_FRETE_TRIB);
		/* 142 */
		lbVlTotalItemFreteTributacao = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_TOTAL_FRETE_TRIB, RIGHT);
		new BaseToolTip(lbVlTotalItemFreteTributacao, Messages.TOOLTIP_LABEL_VALOR_TOTAL_FRETE_TRIB);
		/* 143 */
		lbVlTotalPedidoFreteTributacao = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_PEDIDO_TOTAL_FRETE_TRIB, RIGHT);
		new BaseToolTip(lbVlTotalPedidoFreteTributacao, Messages.TOOLTIP_LABEL_VALOR_PEDIDO_TOTAL_FRETE_TRIB);
		/* 144 */
		lbVlTotalItemPorPeso = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PESO, RIGHT);
		new BaseToolTip(lbVlTotalItemPorPeso, Messages.TOOLTIP_LABEL_VL_PESO);
		/* 145 */
		lbVlPctDescCondPagto = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_DESCONTO_COND_PAG, RIGHT);
		new BaseToolTip(lbVlPctDescCondPagto, Messages.TOOLTIP_LABEL_PCT_DESCONTO_COND_PAG);
		/* 146 */
		lbPctDescontoDoPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_DESCONTO_DO_PEDIDO, RIGHT);
		new BaseToolTip(lbPctDescontoDoPedido, Messages.TOOLTIP_LABEL_PCT_DESCONTO_DO_PEDIDO);
		new BaseToolTip(lbVlPctDescCondPagto, Messages.TOOLTIP_LABEL_PCT_DESCONTO_COND_PAG);
		/* 147 */
		lbVlTotalItemTribFreteSeguro = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTALITEM_TRIB_FRETE_SEGURO, RIGHT);
		new BaseToolTip(lbVlTotalItemTribFreteSeguro, Messages.TOOLTIP_LABEL_VLTOTALITEM_TRIB_FRETE_SEGURO);
		/* 148 */
		lbVlTotalItemUnitarioTribFreteSeguro = new LabelName(Messages.ITEMPEDIDO_LABEL_VLTOTALITEM_UNITARIO_TRIB_FRETE_SEGURO, RIGHT);
		new BaseToolTip(lbVlTotalItemUnitarioTribFreteSeguro, Messages.TOOLTIP_LABEL_VLTOTALITEM_UNITARIO_TRIB_FRETE_SEGURO);
		/* 149 */
		lbVlTotalSeguroItemPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_TOTAL_SEGURO, RIGHT);
		new BaseToolTip(lbVlTotalSeguroItemPedido, Messages.TOOLTIP_ITEMPEDIDO_LABEL_VALOR_TOTAL_SEGURO);
		/* 150 */
		lbDtPagamento = new LabelName(Messages.ITEMPEDIDO_LABEL_DATA_PAGAMENTO, RIGHT);
		new BaseToolTip(lbDtPagamento, Messages.TOOLTIP_LABEL_DATA_PAGAMENTO);
		/* 151 */
		lbVlBaseAntecipacao = new LabelName(Messages.ITEMPEDIDO_LABEL_VLR_BASE_TAXA_ANTECIPACAO, RIGHT);
		new BaseToolTip(lbVlBaseAntecipacao, Messages.TOOLTIP_LABEL_VLR_BASE_TAXA_ANTECIPACAO);
		/* 152 */
		lbVlPesoItemUnitario = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_PESO_ITEM, RIGHT);
		new BaseToolTip(lbVlPesoItemUnitario, Messages.TOOLTIP_ITEMPEDIDO_LABEL_VALOR_PESO_ITEM);
		/* 153 */
		lbVlPesoTotalItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VALOR_PESO_TOTAL_ITEM, RIGHT);
		new BaseToolTip(lbVlPesoTotalItem, Messages.TOOLTIP_ITEMPEDIDO_LABEL_VALOR_PESO_TOTAL_ITEM);
		/* 154 */
		lbVlPontuacaoRealizadoItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PONTUACAO_REALIZADO, RIGHT);
		new BaseToolTip(lbVlPontuacaoRealizadoItem, Messages.TOOLTIP_LABEL_ITEMPEDIDO_LABEL_VL_PONTUACAO_REALIZADO);
		/* 155 */
		lbVlPontuacaoBaseItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PONTUACAO_BASE, RIGHT);
		new BaseToolTip(lbVlPontuacaoBaseItem, Messages.TOOLTIP_LABEL_ITEMPEDIDO_LABEL_VL_PONTUACAO_BASE);
		/* 156 */
		lbQtItemGondola = new LabelName(Messages.ITEMPEDIDO_LABEL_QT_ITEM_GONDOLA, RIGHT);
		new BaseToolTip(lbQtItemGondola, Messages.TOOLTIP_LABEL_QT_ITEM_GONDOLA);
		/* 157 */
		lbVlPctMargemRentabItem = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTMARGEMRENTAB_ITEM, RIGHT);
		new BaseToolTip(lbVlPctMargemRentabItem, Messages.TOOLTIP_LABEL_ITEMPEDIDO_LABEL_VLPCTMARGEMRENTAB_ITEM);
		/* 158 */
		lbVlPctMargemRentabPedido = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTMARGEMRENTAB_PEDIDO, RIGHT);
		new BaseToolTip(lbVlPctMargemRentabPedido, Messages.TOOLTIP_LABEL_ITEMPEDIDO_LABEL_VLPCTMARGEMRENTAB_PEDIDO);
		/* 159 */
		lbVlPctDescProgressivo = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_PCT_DESC_PROGRESSIVO, RIGHT);
		new BaseToolTip(lbVlPctDescProgressivo, Messages.TOOLTIP_ITEMPEDIDO_LABEL_VL_PCT_DESC_PROGRESSIVO);
		/*160*/
		lbVlPctPoliticaComercial  = new LabelName(Messages.ITEMPEDIDO_LABEL_PCT_POLITICA_COMERCIAL, RIGHT);
		new BaseToolTip(lbVlPctPoliticaComercial, Messages.TOOLTIP_LABEL_POLITICA_COMERCIAL);
		/*161*/
		lbVlPctFaixaDescQtdPeso = new LabelName(Messages.ITEMPEDIDO_LABEL_VLPCTFAIXADESCQTDPESO, RIGHT);
		new BaseToolTip(lbVlPctFaixaDescQtdPeso, Messages.ITEMPEDIDO_TOOLTIP_VLPCTFAIXADESCQTDPESO);
		/*166*/
		lbDsMoedaProduto = new LabelName(Messages.PRODUTO_LABEL_DSMOEDAPRODUTO, RIGHT);
		new BaseToolTip(lbDsMoedaProduto, Messages.PRODUTO_TOOLTIP_DSMOEDAPRODUTO);
		/*167*/
		lbVlCotacaoMoeda = new LabelName(Messages.ITEMPEDIDO_LABEL_VLCOTACAOMOEDA, RIGHT);
		new BaseToolTip(lbVlCotacaoMoeda, Messages.ITEMPEDIDO_TOOLTIP_VLCOTACAOMOEDA);
		/*168*/
		lbPctDescQuantidade = new LabelName(Messages.LABEL_PERCENTUAL_DESC_QUANTIDADE, RIGHT);
		new BaseToolTip(lbPctDescQuantidade, Messages.TOOLTIP_PERCENTUAL_DESC_QUANTIDADE);
		/*169*/
		lbQtEstoquePrevistoGeral = new LabelName(Messages.ITEMPEDIDO_LIST_LABEL_ESTOQUE_PREVISTO_GERAL, RIGHT);
		new BaseToolTip(lbQtEstoquePrevistoGeral, Messages.TOOLTIP_LABEL_ESTOQUE_PREVISTO_GERAL);
		/*170*/
		lbDtEstoquePrevistoGeral = new LabelName(Messages.ITEMPEDIDO_LIST_LABEL_DT_ESTOQUE_PREVISTO_GERAL, RIGHT);
		new BaseToolTip(lbDtEstoquePrevistoGeral, Messages.TOOLTIP_LABEL_DATA_ESTOQUE_PREVISTO);
		/*171*/
		lbQtSomaEstoquePrevistoGeral = new LabelName(Messages.ITEMPEDIDO_LIST_LABEL_SOMA_ESTOQUE_PREVISTO_GERAL, RIGHT);
		new BaseToolTip(lbQtSomaEstoquePrevistoGeral, Messages.TOOLTIP_LABEL_SOMA_ESTOQUE_PREVISTO_GERAL);
		/*172*/
		lbQtSugerida = new LabelName(Messages.ITEMPEDIDO_LABEL_QTD_SUGERIDA, RIGHT);
		new BaseToolTip(lbQtSugerida, Messages.TOOLTIP_LABEL_QT_SUGERIDA);
		/*173*/
		lbStatusItemPedido = new LabelName(Messages.ITEMPEDIDO_CDSTATUSITEMPEDIDO_ABR, RIGHT);
		new BaseToolTip(lbStatusItemPedido, Messages.TOOLTIP_CDSTATUSITEMPEDIDO);
		/*174*/
		lbVlUnitGiroProduto = new LabelName(Messages.ITEMPEDIDO_LABEL_VL_UNIT_GIRO_PRODUTO, RIGHT);
		new BaseToolTip(lbVlUnitGiroProduto, Messages.ITEMPEDIDO_TOOLTIP_VL_UNIT_GIRO_PRODUTO);
		/*175*/
		lbNuOrdemCompraCliente = new LabelName(Messages.ITEMPEDIDO_LABEL_NU_ORDEM_COMPRA, RIGHT);
		new BaseToolTip(lbNuOrdemCompraCliente, Messages.ITEMPEDIDO_TOOLTIP_NU_ORDEM_COMPRA);
		/*176*/
		lbNuSeqOrdemCompraCliente = new LabelName(Messages.ITEMPEDIDO_LABEL_NU_SEQ_ORDEM_COMPRA, RIGHT);
		new BaseToolTip(lbNuSeqOrdemCompraCliente, Messages.ITEMPEDIDO_TOOLTIP_NU_SEQ_ORDEM_COMPRA);
	}

	// ******************************** M?todos de interface ****************************************
	// **********************************************************************************************
	// **********************************************************************************************

	public void populateHashEditsAndLabels(String ordemCampos) throws SQLException {
		hashEdits = new Hashtable(82);
		hashLabels = new Hashtable(82);
		String[] params = getDefaultEditLabels();
		boolean isAgrupadorGrade = getItemPedido() != null && getItemPedido().getProduto() != null && getItemPedido().getProduto().isProdutoAgrupadorGrade() && isFiltrandoAgrupadorGrade();
		isAgrupadorGrade &= !fromListItemPedidoForm && !fromInfoExtra;
		if (LavenderePdaConfig.isUsaOrdemCamposTelaItemPedido && !isAgrupadorGrade) {
			if (pedido.isConsignacaoObrigatoria()) {
				ordemCampos = ordemCampos.replaceAll("105", "");
			}
			if (LavenderePdaConfig.isUsaFlIgnoraValidaco() && getItemPedido() != null && getItemPedido().getProduto() != null && ValueUtil.getBooleanValue(getItemPedido().getProduto().flIgnoraValidacao)) {
				ordemCampos = ordemCampos.replaceAll("13", "");
			}
			if (LavenderePdaConfig.isUsaVlUnitGiroProduto() && getItemPedido().giroProduto == null) {
				ordemCampos = ordemCampos.replaceAll("174", "");
			}
			params = StringUtil.split(ordemCampos, ';');
		}
		if (!populateEditsLabels(params)) {
			params = getDefaultEditLabels();
			populateEditsLabels(params);
		}
	}

	private boolean populateEditsLabels(String[] params) throws SQLException {
		int cont = 1;
		IntVector itensIncluidos = new IntVector();
		boolean inseridoUmOuMaisItem = false;
		for (int i = 0; i < params.length; i++) {
			int param;
			int nuColunasAlocadas = 1;
			if (params[i].indexOf(":") != -1) {
				int[] paramColunaAlocada = StringUtil.splitToInt(params[i], ':');
				param = paramColunaAlocada[0];
				nuColunasAlocadas = paramColunaAlocada[1];
			} else {
				param = ValueUtil.getIntegerValue(params[i]);
			}
			Control ctrEd = (Control) hashEditsTemp.get(param);
			if (ctrEd == null) {
				continue;
			}
			ctrEd.appObj = StringUtil.getStringValue(nuColunasAlocadas);
			if (itensIncluidos.indexOf(param) == -1) {
				if (validadeVisibleParams(ctrEd, true)) {
					Control ctrLb = (Control) hashLabelsTemp.get(param);
					hashEdits.put(cont, ctrEd);
					hashLabels.put(cont, ctrLb);
					itensIncluidos.addElement(param);
					cont++;
					if (param == 124 || param == 125) {
						mostraUltimoDescontoAcrescimo = true;
					}
					if (param == 131) {
						exibeCampoRentabilidadeEstimado = true;
					}
					ctrLb.setVisible(true);
					ctrEd.setVisible(true);
					inseridoUmOuMaisItem = true;
				} else {
					if (hashLabelsTemp.get(param) != null) {
						Control ctrLb = (Control) hashLabelsTemp.get(param);
						ctrLb.setVisible(false);
						ctrEd.setVisible(false);
					}
				}
			}
		}
		return inseridoUmOuMaisItem;
	}

	private boolean validadeVisibleParams(Control ctr, boolean onPopulate) throws SQLException {
		switch (ctr.appId) {
		case 2:
			if (!(LavenderePdaConfig.usaConversaoUnidadesMedida || !LavenderePdaConfig.usaConversaoUnidadesMedida)) {
				return false;
			}
			break;
		case 8: // edVlPctVerba
		case 9: // edVlVerbaItem 
		case 10: // edVlVerbaPedido
		case 25: // edVlBaseFlex
		case 36: // edVlVerbaManual
		case 85: // edVlVerbaItemPositiva
		case 86: // edVlVerbaItemNegativa
			if (pedido.isIgnoraControleVerba()) {
				return false;
			}
			break;
		case 18: // edVlST
		case 19: // edVlItemST
		case 20: // edVlTotalItemST
		case 34: // edVlTotalPedidoST
			if (pedido.getCliente().isOptanteSimples()) {
				return false;
			}
			break;
		case 27: // edVlFrete
			if (!(pedido.isTipoFreteFob() || LavenderePdaConfig.isPermiteInserirFreteManualItemPedido())) {
				return false;
			}
			break;
		case 33: // edVlReducaoSimples
		case 93: // edVlEmbalagemComReducaoSimples
			if (!pedido.getCliente().isOptanteSimples()) {
				return false;
			}
			break;
		case 51: // bgOportunidade
			if (pedido.isOportunidade()) {
				return false;
			}
			break;
		case 154: // edVlPontuacaoRealizadoItem
		case 155: // edVlPontuacaoBaseItem
			if (!possuiPontuacaoProduto(getItemPedido())) {
				return false;
			}
			break;
		case 156: // edQtItemGondola
			if (!(pedido.isGondola() && !pedido.isPedidoCriticoOuConversaoFob())) {
				return false;
			}
			break;
		case 157: // edVlPctMargemRentabItem
			if (LavenderePdaConfig.isOcultaRentabilidadeItemPedido()) {
				return false;
			}
			break;
		case 158: // edVlPctMargemRentabPedido
			if (LavenderePdaConfig.isOcultaRentabilidadePedido()) {
				return false;
			}
			break;
		case 172: //edQtSugerida
			if (!isProdutoFromSugestaoIndustria()) {
				return false;
			}
			break;
		default: break;
		}
		return onPopulate || isLabelEditVisible(ctr.appId);
	}
	
	protected boolean isLabelEditVisible(int controlId) {
		if (this.hideAttributesVendaUnitaria && getDefaultHiddenEditLabels().contains(StringUtil.getStringValue(controlId))) {
			return false;
		}
		return true;
	}
	
	protected void changeLabelEditVisibility() throws SQLException {
		String[] params = getDefaultEditLabels();
		for (int i = 0; i < params.length; i++) {
			int param;
			if (params[i].indexOf(":") != -1) {
				param = StringUtil.splitToInt(params[i], ':')[0];
			} else {
				param = ValueUtil.getIntegerValue(params[i]);
			}
			Control ctrEd = (Control) hashEditsTemp.get(param);
			Control ctrLb = (Control) hashLabelsTemp.get(param);
			if (ctrEd == null || ctrLb == null) {
				continue;
			}
			boolean visible = validadeVisibleParams(ctrEd, false);
			ctrLb.setVisible(visible);
			ctrEd.setVisible(visible);
			changeVisibilityEditSameId(ctrEd, visible);
		}
	}

	private void changeVisibilityEditSameId(Control ctrEd, boolean visible) {
		if (ctrEd.appId == 4) {
			if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
				edVlItemPedido.setVisible(visible);
			}
		}
	}

	private Control getGradeEdit() {
		return LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5() ? lvVlItemPedido : edVlItemPedido;
	}

	public void populateHashsTemporarias() {
		hashEditsTemp = new Hashtable(161);
		hashLabelsTemp = new Hashtable(161);
		putInHashTemp(1, edQtItemFisico, lbQtItemFisico);
		putInHashTemp(2, edQtItemFaturamento, lbQtItemFaturamento);
		putInHashTemp(3, edVlPctDesconto, lbVlPctDesconto);
		putInHashTemp(4, getGradeEdit(), lbVlItemPedido);
		putInHashTemp(5, edVlPctAcrescimo, lbVlPctAcrescimo);
		putInHashTemp(6, lvTotalPedido, lbVlTotalItemPedido);
		putInHashTemp(7, edVlTotalPedido, lbVlTotalPedido);
		putInHashTemp(8, edVlPctVerba, lbVlPctVerba);
		putInHashTemp(9, edVlVerbaItem, lbVlVerbaItem);
		putInHashTemp(10, edVlVerbaPedido, lbVlVerbaPedido);
		putInHashTemp(11, edPreco, lbPrecoTab);
		putInHashTemp(12, edVlBaseItemPedido, lbVlBaseItem);
		putInHashTemp(13, lvEstoque, lbEstoque);
		putInHashTemp(14, edVlPctMaxDesconto, lbVlPctMaxDesconto);
		putInHashTemp(15, edVlPctPreviaDesc, lbVlPctPreviaDesc);
		putInHashTemp(16, edVlItemComissao, lbVlItemComissao);
		putInHashTemp(17, edVlTotalComissao, lbVlTotalComissao);
		putInHashTemp(18, edVlST, lbVlST);
		putInHashTemp(19, edVlItemST, lbVlItemSt);
		putInHashTemp(20, edVlTotalItemST, lbVlTotalItemSt);
		putInHashTemp(21, edCaixaPadrao, lbCaixaPadrao);
		putInHashTemp(22, edPrecoFabrica, lbPrecoFabrica);
		putInHashTemp(23, edPrecoMaximoCons, lbPrecoMaximoCons);
		putInHashTemp(24, edEmbalagem, lbEmbalagem);
		putInHashTemp(25, edVlBaseFlex, lbVlBaseFlex);
		putInHashTemp(26, edTicketMedioPedido, lbTicketMedioPedido);
		putInHashTemp(27, edVlFrete, lbVlFrete);
		putInHashTemp(28, edVlTtItemComDeflator, lbVlTtItemComDeflator);
		putInHashTemp(29, edPrecoEmbPrimaria, lbPrecoEmbPrimaria);
		putInHashTemp(30, edRentabilidadeItem, lbRentabilidadeItem);
		putInHashTemp(31, edRentabilidadePedido, lbRentabilidadePedido);
		putInHashTemp(32, edVlUnidadeEmbalagem, lbVlUnidadeEmbalagem);
		putInHashTemp(33, edVlReducaoSimples, lbVlReducaoSimples);
		putInHashTemp(34, edVlTotalPedidoST, lbVlTotalPedidoST);
		putInHashTemp(35, edQtEmabalagemCompra, lbQtEmabalagemCompra);
		putInHashTemp(36, edVlVerbaManual, lbVlVerbaManual);
		putInHashTemp(37, edVlPctDescPromocional, lbVlPctDescPromocional);
		putInHashTemp(38, edQtPtItens, lbQtPtItens);
		putInHashTemp(39, edQtPtPedido, lbQtPtPedido);
		putInHashTemp(40, edVlMaxBonificacao, lbVlMaxBonificacao);
		putInHashTemp(41, cbUnidadeAlternativa, lbUnidadeAlternativa);
		putInHashTemp(42, edQtMininimaVenda, lbQtMininimaVenda);
		putInHashTemp(43, edNuMultiploIdeal, lbNuMultiploIdeal);
		putInHashTemp(44, edVlPctComissao, lbVlPctComissao);
		putInHashTemp(45, edVlIndiceRentabItem, lbVlIndiceRentabItem);
		putInHashTemp(46, edVlIndiceRentabPedido, lbVlIndiceRentabPedido);
		putInHashTemp(47, edQtEmbalagem, lbQtEmbalagem);
		putInHashTemp(48, edVlEmbalagem, lbVlEmbalagem);
		putInHashTemp(49, edPctMargemAgregada, lbPctMargemAgregada);
		putInHashTemp(50, edVlAgregadoSugerido, lbVlAgregadoSugerido);
		putInHashTemp(51, bgOportunidade, lbOportunidade);
		putInHashTemp(52, edVlItemTabPrecoVariacaoPreco, lbVlItemTabPrecoVariacaoPreco);
		putInHashTemp(53, edVlIpi, lbVlIpi);
		putInHashTemp(54, edVlItemIpi, lbVlItemIpi);
		putInHashTemp(55, edVlTotalItemIpi, lbVlTotalItemIpi);
		putInHashTemp(56, edVlTributos, lbVlTributos);
		putInHashTemp(57, edVlItemTributos, lbVlItemTributos);
		putInHashTemp(58, edVlTotalItemTributos, lbVlTotalItemTributos);
		putInHashTemp(59, edVlAliquotaIpi, lbVlAliquotaIpi);
		putInHashTemp(60, edVlTotalPedidoIpi, lbVlTotalPedidoIpi);
		putInHashTemp(61, edVlTotalPedidoTributos, lbVlTotalPedidoTributos);
		putInHashTemp(62, edQtItemEstoquePositivo, lbQtItemEstoquePositivo);
		putInHashTemp(63, edVlTotalItemPositivo, lbVlTotalItemPositivo);
		putInHashTemp(64, edVlTotalPedidoEstoquePositvo, lbVlTotalPedidoEstoquePositvo);
		putInHashTemp(65, edVlTotalItemFrete, lbVlTotalItemFrete);
		putInHashTemp(66, edVlItemFrete, lbVlItemFrete);
		putInHashTemp(67, edVlTotalPedidoFrete, lbVlTotalPedidoFrete);
		putInHashTemp(68, edVlItemPedidoComDescontoCCP, lbVlItemPedidoComDescontoCPP);
		putInHashTemp(69, edVlPctDescontoCCPItemPedido, lbVlPctDescontoCPPItemPedido);
		putInHashTemp(70, edVlItemPedidoBruto, lbVlItemPedidoBruto);
		putInHashTemp(71, edVlTotalBrutoItemPedido, lbVlTotalBrutoItemPedido);
		putInHashTemp(72, edVlTotalPedidoComTributosEDeducoes, lbVlTotalPedidoBruto);
		putInHashTemp(73, edVlTotalFreteItem, lbVlTotalFreteItem);
		putInHashTemp(74, edVlEmbalagemElementTributos, lbVlEmbalagemElementTributos);
		putInHashTemp(75, edVlDesconto, lbVlDesconto);
		putInHashTemp(76, edVlPctDesconto2, lbVlPctDesconto2);
		putInHashTemp(77, edVlDesconto2, lbVlDesconto2);
		putInHashTemp(78, edVlPctDesconto3, lbVlPctDesconto3);
		putInHashTemp(79, edVlDesconto3, lbVlDesconto3);
		putInHashTemp(80, edFaixaComissaoRentabilidade, lbFaixaComissaoRentabilidade);
		putInHashTemp(81, edFaixaComissaoRentabilidadeMinima, lbFaixaComissaoRentabilidadeMinima);
		putInHashTemp(82, edVlPctComissaoPedido, lbVlPctComissaoPedido);
		putInHashTemp(83, edQtPesoCargaPedido, lbQtPesoCargaPedido);
		putInHashTemp(84, edPctPesoCargaPedido, lbPctPesoCargaPedido);
		putInHashTemp(85, edVlVerbaItemPositiva, lbVlVerbaItemPositiva);
		putInHashTemp(86, edVlVerbaItemNegativa, lbVlVerbaItemNegativa);
		putInHashTemp(87, edVlPctDescCanal, lbVlPctDescCanal);
		putInHashTemp(88, edVlPctDescContratoCli, lbVlPctDescContratoCli);
		putInHashTemp(89, bgGeraVerba, lbGeraVerba);
		putInHashTemp(90, edVlVerbaNecessaria, lbVlVerbaNecessaria);
		putInHashTemp(91, edVlNeutroItem, lbVlNeutroItem);
		putInHashTemp(92, edVlSeguroItemPedido, lbVlSeguroItemPedido);
		putInHashTemp(93, edVlEmbalagemComReducaoSimples, lbVlEmbalagemComReducaoSimples);
		putInHashTemp(94, edVlEmbalagemPrimariaST, lbVlEmbalagemPrimariaST);
		putInHashTemp(95, edVlItemPedidoFreteESeguro, lbVlItemPedidoFreteESeguro);
		putInHashTemp(96, edVlTotalItemPedidoFreteESeguro, lbVlTotalItemPedidoFreteESeguro);
		putInHashTemp(97, edPctMaxParticipacaoItemBonificacao, lbPctMaxParticipacaoItemBonificacao);
		putInHashTemp(98, edPctAtualParticipacaoItemBonificacao, lbPctAtualParticipacaoItemBonificacao);
		putInHashTemp(99, edPesoAtualPedido, lbPesoAtualPedido);
		putInHashTemp(100, edPesoMinimoItem, lbPesoMinimoItem);
		putInHashTemp(101, edVolumeItem, lbVolumeItem);
		putInHashTemp(102, edVolumeTotalItem, lbVolumeTotalItem);
		putInHashTemp(103, edVolumePedido, lbVolumePedido);
		putInHashTemp(104, edSaldoCreditoRestanteCliente, lbSaldoCreditoRestanteCliente);
		putInHashTemp(105, bgItemTroca, lbItemTroca);
		putInHashTemp(106, edStItem, lbStItem);
		putInHashTemp(107, edStPedido, lbStPedido);
		putInHashTemp(108, edVlUnitarioTributos, lbVlUnitarioTributos);
		putInHashTemp(109, edVlEmbalagemSt, lbVlEmbalagemSt);
		putInHashTemp(110, edVlUnitarioIpi, lbVlUnitarioIpi);
		putInHashTemp(111, edSaldoCreditoConsignacaoRestanteCliente, lbSaldoCreditoConsignacaoRestanteCliente);
		putInHashTemp(112, edVlItemStReverso, lbVlItemStReverso);
		putInHashTemp(113, edVlPctDescontoStReverso, lbVlDescontoStReverso);
		putInHashTemp(114, edVlTonFreteCliente, lbVlTonFreteCliente);
		putInHashTemp(115, edVlFretePedido, lbVlFretePedido);
		putInHashTemp(116, edVlPctDescCliente, lbVlPctDescCliente);
		putInHashTemp(117, edVlPctDescontoCondicao, lbVlPctDescontoCondicao);
		putInHashTemp(118, edVlPctDescFrete, lbVlPctDescFrete);
		putInHashTemp(119, edQtEstoqueNegociacao, lbQtEstoqueCondicaoNegociacao);
		putInHashTemp(120, edUltimoPrecoPraticado, lbUltimoPrecoPraticado);
		putInHashTemp(121, edAliquotaSt, lbAliquotaSt);
		putInHashTemp(122, edDifStEIpi, lbDifStEIpi);
		putInHashTemp(123, edQtDesejada, lbQtDesejada);
		putInHashTemp(124, edUltimoDescontoPraticado, lbUltimoDescontoPraticado);
		putInHashTemp(125, edUltimoAcrescimoPraticado, lbUltimoAcrescimoPraticado);
		putInHashTemp(126, edTxAntecipacao, lbTxAntecipacao);
		putInHashTemp(127, edDtVencimentoPrecoProduto, lbDtVencimentoPrecoProduto);
		putInHashTemp(128, edVlTotalItemTabelaPreco, lbVlTotalItemTabelaPreco);
		putInHashTemp(129, edPercIndiceFinanceiroCondPagto, lbPercIndiceFinanceiroCondPagto);
		putInHashTemp(130, edPctMaxDescProdutoCliente, lbPctMaxDescProdutoCliente);
		putInHashTemp(131, edVlIndiceRentabEstimadoPedido, lbVlIndiceRentabEstimadoPedido);
		putInHashTemp(132, edVlRetornoProduto, lbVlRetornoProduto);
		putInHashTemp(133, edPctDescPoliticaInterpol, lbPctDescPoliticaInterpol);
		putInHashTemp(134, edVlDescPoliticaInterpol, lbVlDescPoliticaInterpol);
		putInHashTemp(135, edVlTotDescPoliticaInterpol, lbVlTotDescPoliticaInterpol);
		putInHashTemp(136, edVlPctDescEfetivo, lbVlPctDescEfetivo);
		putInHashTemp(137, edQtEstoquePrevisto, lbVlQtEstoquePrevisto);
		putInHashTemp(138, edDtEstoquePrevisto, lbDtEstoquePrevisto);
		putInHashTemp(139, edConsisteConversaoUnidade, lbConsisteConversaoUnidade);
		putInHashTemp(140, edVlPctTotalMargemItem, lbVlPctTotalMargemItem);
		putInHashTemp(141, edVlItemFreteTributacao, lbVlItemFreteTributacao);
		putInHashTemp(142, edVlTotalItemFreteTributacao, lbVlTotalItemFreteTributacao);
		putInHashTemp(143, edVlTotalPedidoFreteTributacao, lbVlTotalPedidoFreteTributacao);
		putInHashTemp(144, edVlTotalItemPorPeso, lbVlTotalItemPorPeso);
		putInHashTemp(145, edVlPctDescCondPagto, lbVlPctDescCondPagto);
		putInHashTemp(146, edPctDescontoDoPedido, lbPctDescontoDoPedido);
		putInHashTemp(147, edVlTotalItemTribFreteSeguro, lbVlTotalItemTribFreteSeguro);
		putInHashTemp(148, edVlTotalItemUnitarioTribFreteSeguro, lbVlTotalItemUnitarioTribFreteSeguro);
		putInHashTemp(149, edVlTotalSeguroItemPedido, lbVlTotalSeguroItemPedido);
		putInHashTemp(150, edDtPagamento, lbDtPagamento);
		putInHashTemp(151, edVlBaseAntecipacao, lbVlBaseAntecipacao);
		putInHashTemp(152, edVlPesoItemUnitario, lbVlPesoItemUnitario);
		putInHashTemp(153, edVlPesoTotalItem, lbVlPesoTotalItem);
		putInHashTemp(154, edVlPontuacaoRealizadoItem, lbVlPontuacaoRealizadoItem);
		putInHashTemp(155, edVlPontuacaoBaseItem, lbVlPontuacaoBaseItem);
		putInHashTemp(156, edQtItemGondola, lbQtItemGondola);
		putInHashTemp(157, edVlPctMargemRentabItem, lbVlPctMargemRentabItem);
		putInHashTemp(158, edVlPctMargemRentabPedido, lbVlPctMargemRentabPedido);
		putInHashTemp(159, edVlPctDescProgressivo, lbVlPctDescProgressivo);
		putInHashTemp(160, edVlPctPoliticaComercial, lbVlPctPoliticaComercial);
		putInHashTemp(165, edVlPctFaixaDescQtdPeso, lbVlPctFaixaDescQtdPeso);
		putInHashTemp(166, edDsMoedaProduto, lbDsMoedaProduto);
		putInHashTemp(167, edVlCotacaoMoeda, lbVlCotacaoMoeda);
		putInHashTemp(168, edVlPctDescQuantidade, lbPctDescQuantidade);
		putInHashTemp(169, edQtEstoquePrevistoGeral, lbQtEstoquePrevistoGeral);
		putInHashTemp(170, edDtEstoquePrevistoGeral, lbDtEstoquePrevistoGeral);
		putInHashTemp(171, edQtSomaEstoquePrevistoGeral, lbQtSomaEstoquePrevistoGeral);
		putInHashTemp(172, edQtSugerida, lbQtSugerida);
		putInHashTemp(173, lvStatusItemPedido, lbStatusItemPedido);
		putInHashTemp(174, lvVlUnitGiroProduto, lbVlUnitGiroProduto);
		putInHashTemp(175, edNuOrdemCompraCliente, lbNuOrdemCompraCliente);
		putInHashTemp(176, edNuSeqOrdemCompraCliente, lbNuSeqOrdemCompraCliente);
	}

	private void putInHashTemp(int id, Control edit, Control label) {
		hashEditsTemp.put(id, edit);
		hashLabelsTemp.put(id, label);
		edit.appId = id;
		label.appId = id;
	}

	protected void refreshComponents() throws SQLException {
		edVlTtItemComDeflator.setEditable(false);
		edDsTabelaPreco.setEditable(false);
		edVlBaseItemPedido.setEditable(false);
		edVlTotalPedido.setEditable(false);
		edVlVerbaItem.setEditable(false);
		edVlVerbaItemPositiva.setEditable(false);
		edVlVerbaItemNegativa.setEditable(false);
		edVlVerbaPedido.setEditable(false);
		edVlPctMaxDesconto.setEditable(false);
		edVlPctPreviaDesc.setEditable(false);
		edVlItemComissao.setEditable(false);
		edPctPesoCargaPedido.setEditable(false);
		edQtPesoCargaPedido.setEditable(false);
		edVlTotalComissao.setEditable(false);
		edVlST.setEditable(false);
		edVlEmbalagemSt.setEditable(false);
		edVlItemST.setEditable(false);
		edVlItemStReverso.setEditable(false);
		edVlPctDescontoStReverso.setEditable(false);
		edVlTotalItemST.setEditable(false);
		edVlTotalPedidoST.setEditable(false);
		edStItem.setEditable(false);
		edStPedido.setEditable(false);
		edSaldoCreditoConsignacaoRestanteCliente.setEditable(false);
		edCaixaPadrao.setEditable(false);
		edPrecoFabrica.setEditable(false);
		edPrecoMaximoCons.setEditable(false);
		edEmbalagem.setEditable(false);
		edQtEmabalagemCompra.setEditable(false);
		edVlEmbalagemElementTributos.setEditable(false);
		edVlReducaoSimples.setEditable(false);
		edPreco.setEditable(false);
		edVlBaseFlex.setEditable(false);
		edTicketMedioPedido.setEditable(false);
		edRentabilidadeItem.setEditable(false);
		edRentabilidadePedido.setEditable(false);
		edVlIndiceRentabItem.setEditable(false);
		edVlIndiceRentabPedido.setEditable(false);
		edVlIndiceRentabEstimadoPedido.setEditable(false);
		edQtEmbalagem.setEditable(false);
		edVlEmbalagem.setEditable(false);
		edVlPctDescPromocional.setEditable(false);
		edQtPtItens.setEditable(false);
		edQtPtPedido.setEditable(false);
		edVlMaxBonificacao.setEditable(false);
		edNuMultiploIdeal.setEditable(false);
		edQtMininimaVenda.setEditable(false);
		edVlPctComissao.setEditable(false);
		edVlItemTabPrecoVariacaoPreco.setEditable(false);
		edVlEmbalagemComReducaoSimples.setEditable(false);
		edVlEmbalagemPrimariaST.setEditable(false);
		edPctMaxParticipacaoItemBonificacao.setEditable(false);
		edPctAtualParticipacaoItemBonificacao.setEditable(false);
		edPesoAtualPedido.setEditable(false);
		edPesoMinimoItem.setEditable(false);
		edVolumeItem.setEditable(false);
		edVolumeTotalItem.setEditable(false);
		edVolumePedido.setEditable(false);
		edSaldoCreditoRestanteCliente.setEditable(false);
		edVlFrete.setEditable((!LavenderePdaConfig.bloqueiaAlterarFreteDoItemPedido && !LavenderePdaConfig.usaPctFreteTipoFreteNoPedido && !LavenderePdaConfig.isUsaPctFretePorTipoPedidoTabPrecoEPeso() )|| LavenderePdaConfig.isPermiteInserirFreteManualItemPedido());
		if (LavenderePdaConfig.isUsaDescontosAutoEmCascataNaCapaPedidoPorItem() || LavenderePdaConfig.isUsaDescontosEmCascataManuaisNaCapaPedidoPorItem() || LavenderePdaConfig.isAplicaDescontoCategoria()) {
			edVlPctDescCliente.setEditable(false);
			edVlPctDescontoCondicao.setEditable(false);
			edVlPctDescFrete.setEditable(false);
		}
		if (LavenderePdaConfig.usaValorRetornoProduto) {
			edVlRetornoProduto.setEditable(isEnabled());
		}
		setEditableEditsValor();
		edVlVerbaManual.setEditable(isEnabled() && isAlteracaoPrecoNaoControladoPelaOportunidade());
		cbUnidadeAlternativa.setEditable(isEnabled() && !LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido());
		if (isPermiteUsarOportunidade()) {
			bgOportunidade.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido()) {
			bgItemTroca.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.usaCalculoVerbaComImpostoERentabilidade) {
			bgGeraVerba.setEnabled(isEnabled());
			edVlVerbaNecessaria.setEditable(false);
			edVlNeutroItem.setEditable(false);
		}
		if (gridUnidadeAlternativa != null) {
			gridUnidadeAlternativa.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.usaTecladoFixoTelaItemPedido) {
			numericPad.setEnabled(isEnabled());
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
			cbUnidade.setEnabled(isEnabled() && pedido.itemPedidoList.size() == 0);
		}
		cbTabelaPreco.setEnabled((LavenderePdaConfig.permiteTabPrecoItemDiferentePedido() || LavenderePdaConfig.filtraTabelaPrecoPelaListaDoCliente) && !LavenderePdaConfig.bloqueiaTabPrecoPadraoClienteNoPedido && isEnabled());
		cbCondicaoComercial.setEnabled(LavenderePdaConfig.permiteCondComercialItemDiferentePedido && isEnabled() && pedido.getCondicaoComercial().isFlAcessaOutrasCond());

		boolean edQtItemFisicoEditable = isEnabled() && !LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido;
		boolean edQtItemFaturamentoEditable = isEnabled();
		ItemPedido itemPedido = getItemPedido();
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.liberaComSenhaPrecoProduto && ValueUtil.isNotEmpty(itemPedido.cdEmpresa)) {
			boolean isItemPedidoAutorizado = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizado(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
			edQtItemFisicoEditable &= !isItemPedidoAutorizado;
			edQtItemFaturamentoEditable &= !isItemPedidoAutorizado;
		}
		edQtItemFisico.setEditable(edQtItemFisicoEditable);
		edQtItemFaturamento.setEditable(edQtItemFaturamentoEditable);

		lvEstoque.setEnabled(isEnabled() || LavenderePdaConfig.isConfigGradeProduto());
		lvTotalPedido.setEnabled(isEnabled() || LavenderePdaConfig.usaGradeProduto4());
        lvVlItemPedido.setEnabled(lvTotalPedido.isEnabled());

		setEnableComponentsPossuemVariacao();

		if (LavenderePdaConfig.usaSinalizadorRentabilidadeBaseadoIndiceCalculado) {
			setColorsOnIndicesRentabilidadeSemTributos();
		}
		if (LavenderePdaConfig.indiceRentabilidadePedido > 0) {
			setColorsOnIndicesRentabilidade();
		}
		edVlIpi.setEditable(false);
		edVlUnitarioIpi.setEditable(false);
		edVlTotalItemIpi.setEditable(false);
		edVlTributos.setEditable(false);
		edVlUnitarioTributos.setEditable(false);
		edVlItemTributos.setEditable(false);
		edVlTotalItemTributos.setEditable(false);
		edVlAliquotaIpi.setEditable(false);
		edVlTotalPedidoIpi.setEditable(false);
		edVlTotalPedidoTributos.setEditable(false);
		edQtItemEstoquePositivo.setEditable(false);
		edVlTotalItemPositivo.setEditable(false);
		edVlTotalPedidoEstoquePositvo.setEditable(false);
		edVlTotalItemFrete.setEditable(false);
		edVlTotalFreteItem.setEditable(false);
		edVlItemFrete.setEditable(false);
		edVlTotalPedidoFrete.setEditable(false);
		edVlItemPedidoComDescontoCCP.setEditable(false);
		edVlPctDescontoCCPItemPedido.setEditable(false);
		edVlItemPedidoBruto.setEditable(false);
		edVlTotalBrutoItemPedido.setEditable(false);
		edVlTotalPedidoComTributosEDeducoes.setEditable(false);
		edVlDesconto.setEditable(false);
		edVlDesconto2.setEditable(false);
		edVlDesconto3.setEditable(false);
		edVlPctDesconto3.setEditable(isHabilitaEdicaoCampoPctDesconto3());
		edFaixaComissaoRentabilidade.setEditable(false);
		edFaixaComissaoRentabilidadeMinima.setEditable(false);
		edVlPctComissaoPedido.setEditable(false);
		edVlPctVerba.setEditable(false);
		edVlPctDescCanal.setEditable(false);
		edVlPctDescContratoCli.setEditable(false);
		edVlSeguroItemPedido.setEditable(false);
		edVlItemPedidoFreteESeguro.setEditable(false);
		edVlTotalItemPedidoFreteESeguro.setEditable(false);
		edVlTonFreteCliente.setEditable(false);
		edVlFretePedido.setEditable(false);
		edQtEstoqueNegociacao.setEditable(false);
		edUltimoPrecoPraticado.setEditable(false);
		edAliquotaSt.setEditable(false);
		edDifStEIpi.setEditable(false);
		edQtDesejada.setEditable(isEnabled() && LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido);
		setColorsEditsByConversaoUnidade(getItemPedido());
		if (LavenderePdaConfig.usaEstoqueOnline && LavenderePdaConfig.isConfigGradeProduto()) {
			lvEstoque.setBtGradeVisibleOnly(ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(itemPedido.cdItemGrade1));
			lvEstoque.setRectComponents();
		}
		edUltimoDescontoPraticado.setEditable(false);
		edUltimoAcrescimoPraticado.setEditable(false);
		edTxAntecipacao.setEditable(false);
		edDtVencimentoPrecoProduto.setEditable(false);
		edVlTotalItemTabelaPreco.setEditable(false);
		edPercIndiceFinanceiroCondPagto.setEditable(false);
		edPctMaxDescProdutoCliente.setEditable(false);
		edVlPctDesconto2.setEditable(isEnabled() && isDesc2Editable() && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL));
		edPctDescPoliticaInterpol.setEditable(false);
		edVlDescPoliticaInterpol.setEditable(false);
		edVlTotDescPoliticaInterpol.setEditable(false);
		edVlPctDescEfetivo.setEditable(false);
		edQtEstoquePrevisto.setEditable(false);
		edDtEstoquePrevisto.setEditable(false);
		edConsisteConversaoUnidade.setEditable(false);
		edVlItemFreteTributacao.setEditable(false);
		edVlTotalItemFreteTributacao.setEditable(false);
		edVlTotalPedidoFreteTributacao.setEditable(false);
		edVlTotalItemPorPeso.setEditable(false);
		edPctDescontoDoPedido.setEditable(false);
		edVlPctTotalMargemItem.setEditable(false);
		edVlPctDescCondPagto.setEditable(isEnabled() && enableVlPctDescCondPagto());
		edDtPagamento.setEditable(false);
		edVlBaseAntecipacao.setEditable(false);
		edVlTotalItemTribFreteSeguro.setEditable(false);
		edVlTotalItemUnitarioTribFreteSeguro.setEditable(false);
		edVlTotalSeguroItemPedido.setEditable(false);
		edVlPesoTotalItem.setEditable(false);
		edVlPesoItemUnitario.setEditable(false);
		edVlPontuacaoRealizadoItem.setEditable(false);
		edVlPontuacaoBaseItem.setEditable(false);
		edVlPctMargemRentabItem.setEditable(false);
		edVlPctMargemRentabPedido.setEditable(false);
		changeVisibleValueRentabilidade();
		edVlPctDescProgressivo.setEditable(false);
		edVlPctPoliticaComercial.setEditable(false);
		edVlPctFaixaDescQtdPeso.setEditable(false);
		edDsMoedaProduto.setEditable(false);
		edVlCotacaoMoeda.setEditable(false);
		edVlPctDescQuantidade.setEditable(false);
		edQtItemGondola.setEditable(isEnabled());
		edQtEstoquePrevistoGeral.setEditable(false);
		edDtEstoquePrevistoGeral.setEditable(false);
		edQtSomaEstoquePrevistoGeral.setEditable(false);
		if (!isEnabled() && itemPedido != null && !itemPedido.isGondola()) {
			edQtItemGondola.setText(ValueUtil.VALOR_NI);
		}
		if (LavenderePdaConfig.usaReservaEstoqueCorrenteR3()){
			carregaDadosEstoquePrevGeral(itemPedido.getProduto());
		}
		lvStatusItemPedido.setEnabled(false);
		changeEdQtDesejadaForeColor();
		edQtSugerida.setEditable(false);
	}

	private boolean enableVlPctDescCondPagto() throws SQLException {
		if (!LavenderePdaConfig.usaDescontoMaximoPorCondicaoPagamento) return false;
		return pedido.getCondicaoPagamento() != null && pedido.getCondicaoPagamento().vlPctMaxDesconto > 0;
	}

	private boolean isDesc2Editable() throws SQLException {
		return LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1() || (LavenderePdaConfig.usaDescontoExtra && getItemPedido().getProduto() != null && getItemPedido().getProduto().vlPctMaxDescExtra > 0);
	}

	public void setColorsEditsByConversaoUnidade(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.consisteMultiploEmbalagem || LavenderePdaConfig.avisaConversaoUnidades || LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
			Produto produtoItem = itemPedido.getProduto();
			if (produtoItem != null && (!(ValueUtil.VALOR_NAO.equals(produtoItem.flConsisteConversaoUnidade)))) {
				if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.avisaConversaoUnidades || LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial()) {
					double nuConversaoUnidade = itemPedido.getNuConversaoUnidade();
					if (nuConversaoUnidade > 0d) {
						if (ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuConversaoUnidade, edQtItemFisico.getValueDouble())) {
							edQtItemFisico.setForeColor(Color.darker(ColorUtil.softGreen));
						} else {
							edQtItemFisico.setForeColor(Color.darker(ColorUtil.softRed));
						}
					}
				}
				if (LavenderePdaConfig.consisteMultiploEmbalagem) {
					double vlMultiploEmbalagem = produtoItem.nuMultiploEmbalagem * 1000;
					if (vlMultiploEmbalagem > 0) {
						if (ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(vlMultiploEmbalagem, edQtItemFaturamento.getValueDouble())) {
							edQtItemFaturamento.setForeColor(Color.darker(ColorUtil.softGreen));
						} else {
							edQtItemFaturamento.setForeColor(Color.darker(ColorUtil.softRed));
						}
					}
				}
			} else {
				edQtItemFisico.setForeColor(ColorUtil.componentsForeColor);
				edQtItemFaturamento.setForeColor(ColorUtil.componentsForeColor);
			}
		}
	}

	public void confirmaArredondaConversaoUnidade() throws SQLException {
		Produto produtoItem = getItemPedido().getProduto();
		if (produtoItem != null && (!(ValueUtil.VALOR_NAO.equals(produtoItem.flConsisteConversaoUnidade)))) {
			double nuConversaoUnidade = produtoItem.nuConversaoUnidadesMedida;
			if (nuConversaoUnidade != 0) {
				if (!ConversaoUnidadeService.getInstance().isConversaoUnidadeCompleta(nuConversaoUnidade, edQtItemFisico.getValueDouble())) {
					int value = ValueUtil.doublePrecisionInterface;
					if (LavenderePdaConfig.isUsaQtdItemPedidoFisicoInteiro()) {
						value = 0;
					}
					String param1 = StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(nuConversaoUnidade)), value);
					String param2 = edQtItemFisico.getValueDouble() < nuConversaoUnidade ? StringUtil.getStringValueToInterface(nuConversaoUnidade, value) : StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(edQtItemFisico.getValueDouble() / nuConversaoUnidade, 0)) * nuConversaoUnidade, value);
					String param3 = edQtItemFisico.getValueDouble() < nuConversaoUnidade ? StringUtil.getStringValueToInterface(nuConversaoUnidade * 2, value) : StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(StringUtil.getStringValue(edQtItemFisico.getValueDouble() / nuConversaoUnidade, 0)) * nuConversaoUnidade + nuConversaoUnidade, value);
					if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ITEMFISICO_ARREDONDADO, new Object[] { param1, param2, param3}))) {
						edQtItemFisico.setValue(ConversaoUnidadeService.getInstance().arredondaObedecendoConversaoUnidade(nuConversaoUnidade, edQtItemFisico.getValueDouble()));
						if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido()) {
							BaseListContainer.Item item = (BaseListContainer.Item) listContainer.getSelectedItem();
							ItemContainer itemContainer = (ItemContainer) item.rightControl;
							itemContainer.setChooserQtdValue(edQtItemFisico.getValueDouble());
						}
					}
				}
			}
		}
	}

	public void setColorsOnIndicesRentabilidadeSemTributos() {
		if (edVlIndiceRentabItem.getValueDouble() == 0) {
			edVlIndiceRentabItem.setForeColor(ColorUtil.componentsForeColor);
		} else if (edVlIndiceRentabItem.getValueDouble() < LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
			edVlIndiceRentabItem.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_BAIXA_RENTABILIDADE_BACK));
		} else if (edVlIndiceRentabItem.getValueDouble() <= LavenderePdaConfig.indiceMinimoRentabilidadePedido && edVlIndiceRentabItem.getValueDouble() >= LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
			edVlIndiceRentabItem.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_RENTABILIDADE_DENTRO_TOLERANCIA));
		} else {
			edVlIndiceRentabItem.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_ALTA_RENTABILIDADE_BACK));
		}
		// --
		corRentabilidadePedido();
	}

	private void corRentabilidadePedido() {
		if (edVlIndiceRentabPedido.getValueDouble() == 0) {
			edVlIndiceRentabPedido.setForeColor(ColorUtil.componentsForeColor);
		} else if (edVlIndiceRentabPedido.getValueDouble() < LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
			edVlIndiceRentabPedido.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_BAIXA_RENTABILIDADE_BACK));
		} else if (edVlIndiceRentabPedido.getValueDouble() <= LavenderePdaConfig.indiceMinimoRentabilidadePedido && edVlIndiceRentabPedido.getValueDouble() >= LavenderePdaConfig.indiceMinimoRentabilidadePedido - LavenderePdaConfig.vlToleranciaIndiceMinimoRentabilidadePedido) {
			edVlIndiceRentabPedido.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_RENTABILIDADE_DENTRO_TOLERANCIA));
		} else {
			edVlIndiceRentabPedido.setForeColor(Color.darker(LavendereColorUtil.COR_ITEMPEDIDO_ALTA_RENTABILIDADE_BACK));
		}
	}

	public void setColorsOnIndicesRentabilidade() {
		if (edVlIndiceRentabItem.getValueDouble() == 0) {
			edVlIndiceRentabItem.setForeColor(ColorUtil.componentsForeColor);
		} else if (edVlIndiceRentabItem.getValueDouble() < LavenderePdaConfig.indiceMinimoRentabilidadePedido) {
			edVlIndiceRentabItem.setForeColor(Color.darker(Color.RED));
		} else {
			edVlIndiceRentabItem.setForeColor(Color.darker(Color.GREEN));
		}
		if (edVlIndiceRentabPedido.getValueDouble() == 0) {
			edVlIndiceRentabPedido.setForeColor(ColorUtil.componentsForeColor);
		} else if (edVlIndiceRentabPedido.getValueDouble() < LavenderePdaConfig.indiceMinimoRentabilidadePedido) {
			edVlIndiceRentabPedido.setForeColor(Color.darker(Color.RED));
		} else {
			edVlIndiceRentabPedido.setForeColor(Color.darker(Color.GREEN));
		}
	}

	public boolean isPermiteUsarOportunidade() throws SQLException {
		return LavenderePdaConfig.usaOportunidadeVenda && !pedido.isOportunidade() && !pedido.isPedidoBonificacao();
	}

	public void refreshPedidoToScreen() throws SQLException {
		if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido) {
			cbCondicaoComercial.carregaCondicoesComerciais(pedido, true);
			ultimaCondicaoComercialSelected = pedido.cdCondicaoComercial;
			cbCondicaoComercial.setValue(ultimaCondicaoComercialSelected);
			getItemPedido().cdCondicaoComercial = ultimaCondicaoComercialSelected;
		}
		reloadComboTabelaPreco(pedido);
		if (LavenderePdaConfig.permiteCondComercialItemDiferentePedido && LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
			cbTabelaPreco.setSelectedIndex(0);
			ultimaTabelaPrecoSelecionada = ValueUtil.isNotEmpty(cbTabelaPreco.getValue()) ? cbTabelaPreco.getValue(): ultimaTabelaPrecoSelecionada;
		} else if (!LavenderePdaConfig.isUsaTabelaPrecoPedido() && !LavenderePdaConfig.usaTabelaPrecoPorSegmento) {
			if (ValueUtil.isEmpty(pedido.getCliente().cdTabelaPreco)) {
				cbTabelaPreco.setSelectedIndex(0);
				cbTabelaPreco.setListTabelaPrecoListaColuna(cbTabelaPreco.getValue());
			} else {
				cbTabelaPreco.setValue(pedido.getCliente().cdTabelaPreco);
				if (ValueUtil.isEmpty(cbTabelaPreco.getValue())) {
					cbTabelaPreco.setSelectedIndex(0);
					cbTabelaPreco.setListTabelaPrecoListaColuna(cbTabelaPreco.getValue());
				}
			}
			SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
		} else {
			if (ValueUtil.isEmpty(SessionLavenderePda.cdTabelaPreco)) {
				if (ValueUtil.isEmpty(pedido.cdTabelaPreco)) {
					cbTabelaPreco.setSelectedIndex(0);
				} else {
					cbTabelaPreco.setValue(pedido.cdTabelaPreco);
				}
			} else {
				cbTabelaPreco.setValue(SessionLavenderePda.cdTabelaPreco);
			}
		}
		showMessageTrocaTabPreco(false);
		ultimaTabelaPrecoSelecionada = cbTabelaPreco.getValue();
		ultimaTabelaPrecoSelecionadaListaItens = cbTabelaPreco.getValue();
		// --
		if (LavenderePdaConfig.isUsaKitProduto()) {
			cbKit.loadKit1e2(pedido);
			cbKit.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroGrupoProduto == 1 && !LavenderePdaConfig.ocultaGrupoProduto1) {
			if (LavenderePdaConfig.filtraGrupoProdutoPorFornecedor) {
				Fornecedor fornecedorSelecionado = (Fornecedor) cbFornecedor.getSelectedItem();
				cbGrupoProduto1.loadGrupoProduto1(pedido, fornecedorSelecionado);
			} else {
				cbGrupoProduto1.loadGrupoProduto1(pedido);
			}
			cbGrupoProduto1.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaGrupoDescPromocionalNoDescQtdPorGrupo()) {
			cbGrupoDescProd.setSelectedIndex(0);
		}
	}

	protected void resetCombosGrupoProduto() throws SQLException {
		cbGrupoProduto1.setSelectedIndex(0);
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.setSelectedIndex(0);
	}

	public void showMessageTrocaTabPreco(boolean confirmMessage) {
		TabelaPreco tabPreco = cbTabelaPreco.getTabelaSelecionada();
		if (ValueUtil.isNotEmpty(tabPreco.dsMsgAlerta) && !tabPreco.dsMsgAlerta.equals("0")) {
			if (confirmMessage && UiUtil.showConfirmYesCancelMessage(tabPreco.dsMsgAlerta + Messages.TABELAPRECO_MSG_DESEJA_ALTERAR) != 1) {
				cbTabelaPreco.setValue(ultimaTabelaPrecoSelecionada);
			} else if (!ValueUtil.valueEquals(cbTabelaPreco.getValue(), ultimaTabelaPrecoSelecionada) && !ValueUtil.valueEquals(cbTabelaPreco.getValue(), pedido.cdTabelaPreco)) {
				UiUtil.showWarnMessage(tabPreco.dsMsgAlerta);
			}
		}
	}
	
	public void carregaGridProdutos() throws SQLException {
		if (flListInicialized) {
			int lastIndex = listContainer.getSelectedIndex();
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			try {
				msg.popupNonBlocking();
				if (LavenderePdaConfig.apresentaMarcadoresProduto() && (mapIconsMarcadores == null || mapIconsMarcadores.size() == 0)) {
		    		useLeftTopIcons = true;
		    		mapIconsMarcadores = new HashMap<String, Image>();
		    		final int iconSize = UiUtil.getDefaultIconSize();
		    		ProdutoService.getInstance().loadImagesMarcadores(mapIconsMarcadores, iconSize);
		    	}
				FotoProdutoThread.setToStop();
				FotoProdutoLazyLoadUtil.stopAllThreadsFotoProdutoLazyLoad();
				listContainer.removeAllContainers();
				VmUtil.executeGarbageCollector();
				offset = 0;
				listContainer.scrollFinished = false;
				carregaMaisRegistrosGridProdutos();
			} finally {
				msg.unpop();
				aposCarregarGridProdutos(lastIndex);
			}
		}
	}

	public void aposCarregarGridProdutos(int lastIndex) {
		atualizaTotalizadorTotalRegistrosAposFiltrar();
		if (lastIndex > 0) {
			Container previousContainer = listContainer.getContainer(lastIndex - 1);
			if (previousContainer == null && lastIndex > 1) {
				previousContainer = listContainer.getContainer(lastIndex - 2);
			}
			if (previousContainer != null) {
				listContainer.scrollToControl(previousContainer);
			}
		}
		if (isApresentaMensagemLimiteNuRegistrosBuscaSistema()) {
			showMessageFiltroQuantidadeResultados();
		}
	}

	protected void carregaMaisRegistrosGridProdutos() throws SQLException {
		Vector domainList = null;
		try {
			domainList = getProdutoList();
			int listSize = domainList.size();
			Container[] all = new Container[listSize];
			// --
			if (listSize > 0) {
				boolean permiteInserirMultiplosItensPorVezNoPedido = pedido.isPermiteInserirMultiplosItensPorVezNoPedido();
				boolean insereQtdComDescOuAcrescMultiplos = permiteInserirMultiplosItensPorVezNoPedido && LavenderePdaConfig.isInsereQtdDescMultipla() && !isFiltrandoAgrupadorGrade();
				boolean insereQtdSemPermitirDescOuAcrescMult = permiteInserirMultiplosItensPorVezNoPedido && LavenderePdaConfig.isInsereSomenteQtdMultipla() && !isFiltrandoAgrupadorGrade();
				boolean isLoadImages = LavenderePdaConfig.isLoadImagesOnProdutoList();
				boolean aplicaDescPedido = LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem();
				boolean permiteAlterarVlBruto = LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() && (insereQtdComDescOuAcrescMultiplos || insereQtdSemPermitirDescOuAcrescMult);
				boolean permiteAlterarVlItem = LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao() && (insereQtdComDescOuAcrescMultiplos || insereQtdSemPermitirDescOuAcrescMult);
				boolean permiteAlteracaoUnidadeAlternativa = permiteInserirMultiplosItensPorVezNoPedido && LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa();
				double maxDesc = -1;
				if (LavenderePdaConfig.isUsaDescontoMaximoItemPorCliente()) {
					maxDesc = pedido.getCliente().vlPctMaxDesconto;
				}
				loadImageSize(isLoadImages);
				ProdutoBase domain;
				BaseListContainer.Item c;
				ItemContainer itemCont = null;
				String toolTip = null;
				for (int i = 0; i < listSize; i++) {
					if (i % 250 == 0) {
						VmUtil.executeGarbageCollector();
					}
					int indexContainer = i + offset;
					domain = (ProdutoBase) domainList.items[i];
					Vector produtoUnidadeList = domain.produtoUnidadeList;
					preparaComboAoCarregaListaComCdUnidadePreferencial(permiteAlteracaoUnidadeAlternativa, domain, produtoUnidadeList);
					String[] items = getItemProdutoToGrid(domain);
					toolTip = getToolTip(domain);
					all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
					Image[] iconsLegend;
					iconsLegend = ProdutoService.getInstance().getIconsLegend(domain, mapIconsMarcadores, pedido.cdCliente, listContainer.getLayout().relativeFontSizes[0] + listContainer.getFont().size, filtrandoAgrupadorGrade);
					if (insereQtdSemPermitirDescOuAcrescMult) {
						itemCont = instanciaItemContainer(isLoadImages, false, maxDesc, domain, c, toolTip, indexContainer, produtoUnidadeList, items, iconsLegend);
					} else if (insereQtdComDescOuAcrescMultiplos) {
						itemCont = instanciaItemContainer(isLoadImages, true, maxDesc, domain, c, toolTip, indexContainer, produtoUnidadeList, items, iconsLegend);
						if (aplicaDescPedido) {
							if (!itemCont.chooserDescAcresc.isEnabled()) {
								itemCont.chooserDescAcresc.disableBtMais();
							}
							itemCont.setlbVl(calculateItemForGrid(itemCont.chooserDescAcresc, domain));
						}
					} else {
						c.setIconsLegend(iconsLegend, true, useLeftTopIcons);
					}
					desabilitaChooserQtd(domain, itemCont);
					if (permiteAlterarVlBruto) {
						itemCont.setVlBrutoItem(domain.vlBrutoItem);
						itemCont.vlBrutoItemOriginal = domain.vlBrutoItem;
					} else if (permiteAlterarVlItem) {
						ItemPedido itemPedido = getItemPedido();
						double vlItemPedido = itemPedido == null || (itemPedido != null && itemPedido.vlItemPedido == 0 && domain.vlProduto != 0) ? domain.vlProduto : itemPedido.vlItemPedido;
						itemPedido.vlItemPedidoOriginal = vlItemPedido;
						itemCont.vlItemOriginal = vlItemPedido;
						itemCont.setEdVlItemPedido(vlItemPedido);
					}
					c.id = domain.getRowKey();
					c.setID(c.id);
					c.domain = domain;
					c.setToolTip(toolTip);
					if (insereQtdComDescOuAcrescMultiplos || insereQtdSemPermitirDescOuAcrescMult) {
						preparaComboMultInsercaoAoCarregaListaComCdUnidadePreferencial(permiteAlteracaoUnidadeAlternativa, domain, itemCont);
						if (LavenderePdaConfig.usaGradeProduto5()) {
							c.setItens(items);
						} else {
							if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
								c.setItens(new String[] {
									items[0],
									items[1],
									items.length > 2 ? items[2] : ValueUtil.VALOR_NI,
									items.length > 3 ? items[3] : ValueUtil.VALOR_NI
								});
							} else {
								c.setItens(new String[] {items[0], items[1]});
							}
						}
						setItensValuesOnList(itemCont, domain);
						setItemContainerAttrVisible(itemCont);
					} else {
						if (isLoadImages) {
							c.setImage(Util.getDefaultLoadingImage(imageSize));
						}
						c.setItens(items);
					}

					setPropertiesInRowList(c, domain);
				}
				listContainer.addContainers(all);
				setNextTabControlEdFiltro(listContainer);
			} else {
				listContainer.scrollFinished = true;
				if (edFiltro != null) {
					edFiltro.nextTabControl = null;
				}
			}
		} finally {
			aposCarregarMaisProdutos(domainList);
		}
	}

	private void preparaComboAoCarregaListaComCdUnidadePreferencial(boolean permiteAlteracaoUnidadeAlternativa, ProdutoBase domain, Vector produtoUnidadeList) throws SQLException {
		if (permiteAlteracaoUnidadeAlternativa && ValueUtil.isNotEmpty(domain.cdUnidadePreferencial) && ValueUtil.isNotEmpty(produtoUnidadeList)) {
			cbUnidadeAlternativa.add(produtoUnidadeList);
			cbUnidadeAlternativa.setValue(domain.cdUnidadePreferencial, domain.cdProduto, domain.cdItemGrade1);
		}
	}

	private void preparaComboMultInsercaoAoCarregaListaComCdUnidadePreferencial(boolean permiteAlteracaoUnidadeAlternativa, ProdutoBase domain, ItemContainer itemCont) throws SQLException {
		if (itemCont.cbUnidadeAlternativa != null && permiteAlteracaoUnidadeAlternativa && ValueUtil.isNotEmpty(domain.cdUnidadePreferencial)) {
			itemCont.cbUnidadeAlternativa.setValue(domain.cdUnidadePreferencial, domain.cdProduto, domain.cdItemGrade1);
			cbUnidadeAlternativa.removeAll();
			cbUnidadeAlternativa.clear();
		}
	}

	private void setNextTabControlEdFiltro(GridListContainer listContainer) {
		BaseListContainer.Item item = (BaseListContainer.Item) listContainer.getContainer(0);
		if (item != null && item.rightControl instanceof ItemContainer) {
			ItemContainer itemContainer = (ItemContainer)item.rightControl;
			if (itemContainer.chooserQtd != null && edFiltro != null) {
				edFiltro.nextTabControl = itemContainer.chooserQtd.edF;
			}
		}
	}

	private ItemContainer instanciaItemContainer(boolean isLoadImages, boolean usaDesc, double maxDesc, ProdutoBase domain, BaseListContainer.Item c, String toolTip, int i, Vector produtoUnidadeList, String[] items, Image[] iconsLegend) {
		ItemContainer itemCont = new ItemContainer(items, usaDesc, hideAttributes, isLoadImages ? Util.getDefaultLoadingImage(imageSize) : null, campos, maxDesc, (ProdutoBase) domain, listContainer, produtoUnidadeList, i, true, toolTip);
		itemCont.setIconsLegend(iconsLegend);
		c.rightControl = itemCont;
		c.setupRightContainer();
		return itemCont;
	}

	protected void configureConstantesQuebraLinha() throws SQLException {
		negociacaoMultipla = LavenderePdaConfig.isInsereQtdDescMultipla() || LavenderePdaConfig.isInsereSomenteQtdMultipla();
		mostraCheck = pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && !negociacaoMultipla;
		double proporcaoImagemECheck = 0;
		if (!negociacaoMultipla && LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosDentroPedido()) {
			proporcaoImagemECheck = 15 + 2.0 * HEIGHT_GAP;
		}
		proporcaoImagemECheck += (mostraCheck ? + 10 : 0);
		proporcaoImagemECheck = (width * (proporcaoImagemECheck / 100));
		textoMaximoPadraoComQuebraLinha = Convert.insertLineBreak((int) (width - proporcaoImagemECheck), fm,Messages.MAXIMO_CARACTERES_PADRAO_TELA);
		tamanhoMaximoCaracteresLinha = Convert.tokenizeString(textoMaximoPadraoComQuebraLinha, '\n')[0].length();
	}

	private void loadImageSize(boolean isLoadImages) throws SQLException {
		if (isLoadImages) {
			int nuItensPerLine = 2;
			imageSize = ValueUtil.getIntegerValue(((9 / nuItensPerLine) * UiUtil.getLabelPreferredHeight())) - WIDTH_GAP;
		}
		Util.prepareDefaultImage(imageSize);
	}

	private void desabilitaChooserQtd(BaseDomain domain, ItemContainer itemCont) throws SQLException {
		if (LavenderePdaConfig.usaControleEstoquePorRemessa && itemCont != null) {
			ProdutoBase produto = (ProdutoBase) domain;
			boolean semEstoque = ProdutoService.getInstance().produtoSemEstoque(produto, null);
			if (semEstoque) {
				itemCont.chooserQtd.disableAll();
			}
		}
	}

	protected void aposCarregarMaisProdutos(Vector produtoList) {
		strToolTip.setLength(0);
		itens.removeAllElements();
		itens.setSize(0);
		VmUtil.executeGarbageCollector();
		loadLazyImages(produtoList);
		if (listContainer.size() == getQtTotalRegistros()) {
    		listContainer.scrollFinished = true;
    	}
    	if (imageSliderProdutoList != null) {
    		imageSliderProdutoList.carregaMaisProdutosNoImageCarrousel(getDomainListByListContainer(offset));
    	}
	}

	protected void loadLazyImages(Vector produtoList) {
		if (usaThreadFotoProduto()) {
			if (usaLazyLoading) {
				FotoProdutoLazyLoadUtil.startThreadFotoProduto(this, produtoList, imageSize, offset);
			} else {
				FotoProdutoThread.start(this, produtoList, imageSize);
			}
			UiUtil.unpopProcessingMessage();
		}
	}

	protected boolean usaThreadFotoProduto() {
		return LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosDentroPedido() || LavenderePdaConfig.isExibeFotoInsercaoMultiplos();
	}

	protected String[] getItemProdutoToGrid(final Object domain) throws SQLException {
		return produtoToGridRow(domain, cbTabelaPreco.getValue());
	}

	protected void setPropertiesInRowList(Container c, BaseDomain domain) throws SQLException {
		if (c != null) {
			ProdutoBase produto = (ProdutoBase) domain;
			int color = getGridRowColor(produto);
			if (color != -1) {
				listContainer.setContainerBackColor((Item)c, color);
			} else {
				setDefaultColorRowList(c);
			}
		}
	}

	private String[] getCdsTabelaPrecoVisibleList() {
		String[] cdsTabPrecoVisibleList = new String[] { StringUtil.getStringValue(cbTabelaPreco.getValue()) };
		if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido()
				|| LavenderePdaConfig.isDestaqueNaListaDeProdutosDaTelaDeAdicionarItemPedidoETelaEstiloDesktop()
				|| LavenderePdaConfig.usaOportunidadeVenda) {
			cdsTabPrecoVisibleList = cbTabelaPreco.getCdsTabelaPreco();
		}
		return cdsTabPrecoVisibleList;
	}

	private void setDefaultColorRowList(Container c) {
		if (c.getBackColor() != ColorUtil.baseBackColorSystem) {
			listContainer.setContainerBackColor((Item) c, ColorUtil.baseBackColorSystem);
		}
	}

	protected void updatePropertiesInRowItem(BaseListContainer.Item item, BaseDomain domain) {
		if ((LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosDentroPedido() || (LavenderePdaConfig.isExibeFotoInsercaoMultiplos() && LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens())) && domain != null) {
			ProdutoBase produtoBase = (ProdutoBase) domain;
			ImageControl leftControl = (ImageControl) item.leftControl;
			if (produtoBase.imageProduto != null) {
				if (leftControl != null) {
					leftControl.centerImage = true;
					leftControl.setImage(produtoBase.imageProduto);
					item.repaintNow();
				} else {
					ItemContainer c = (ItemContainer) item.getFirstChild();
					c.img.centerImage = true;
					c.img.setImage(produtoBase.imageProduto);
					c.repaintNow();
				}
			}
		}
	}

	protected int getGridRowColor(ProdutoBase produto) throws SQLException {
		// PRODUTOS BLOQUEADOS
		if (LavenderePdaConfig.bloqueiaItemTabelaPrecoParaVenda && LavenderePdaConfig.isUsaDestacaProdutoBloqueado() && ProdutoBloqueadoService.getInstance().isProdutoBloqueado(produto, cbTabelaPreco.getValue())) {
			return LavendereColorUtil.COR_FUNDO_GRID_PRODUTO_BLOQUEADO;
		}
		// PRODUTOS VENDIDOS NO MES CORRENTE
		if (LavenderePdaConfig.usaDestaqueItensVendidosMesCorrente && produto.isVendido() && (pedido != null && pedido.isPedidoNoMesCorrente)) {
			return LavendereColorUtil.COR_PRODUTO_VENDIDO_MES_CORRENTE;
		}
		// PRODUTOS EM OPORTUNIDADE
		if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() && LavenderePdaConfig.usaOportunidadeVenda && produto instanceof ProdutoTabPreco) {
			String tabPrecoOportunidadeList = ((ProdutoTabPreco) produto).dsTabPrecoOportunidadeList;
			if (!ValueUtil.isEmpty(ItemPedidoService.getInstance().getCdTabelaProdComOportunidadeVenda(getCdsTabelaPrecoVisibleList(), tabPrecoOportunidadeList))) {
				return LavendereColorUtil.COR_PRODUTO_EM_OPORTUNIDADE;
			}
		}
		// PRODUTOS PERTENCENTES A UM KIT EM VERDE
		if (LavenderePdaConfig.destacaProdutoDeKitNaGrid && LavenderePdaConfig.isUsaKitProduto()) {
			Vector cdsKit = cbKit.getCdsKit();
			boolean todosProdutosSaoDoKit = cbKit.getValue() != null;
			if (todosProdutosSaoDoKit) {
				return LavendereColorUtil.COR_PRODUTO_COMKIT_BACK;
			} else if (ValueUtil.isNotEmpty(cdsKit)) {
				int cdsKitSize = cdsKit.size();
				for (int i = 0; i < cdsKitSize; i++) {
					if (ItemKitService.getInstance().isProdutoPossuiKit((String) cdsKit.items[i], produto.cdProduto, produto.flBonificacao)) {
						return LavendereColorUtil.COR_PRODUTO_COMKIT_BACK;
					}
				}
			}
		}

		// PRODUTOS INSERIDOS NO PEDIDO
		if (LavenderePdaConfig.destacaProdutosJaIncluidosAoPedido && isProdutoPresenteItemPedido(produto)) {
			return LavendereColorUtil.COR_PRODUTO_INSERIDO_PEDIDO_BACK;
		}
		// PRODUTOS BONIFICAVEIS EM VERDE
		if (LavenderePdaConfig.isPermiteBonificarProduto() && !LavenderePdaConfig.isPermiteBonificarQualquerProduto()) {
			if (ValueUtil.getBooleanValue(produto.flBonificacao)) {
				return LavendereColorUtil.COR_PRODUTO_BONIFICACAO_BACK;
			}
		}
		// PRODUTOS SEM ESTOQUE EM VERMELHO
		if (isGrifaProdutoSemEstoqueNaLista(produto.isIgnoraValidacao()) || LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista) {
			if (LavenderePdaConfig.isConfigGradeProduto()) {
				if (isGrifaProdutoSemEstoqueNaLista(produto.isIgnoraValidacao()) && produto.sumEstoqueGrades != null && 0 == produto.sumEstoqueGrades) {
					return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK;
				} else if (LavenderePdaConfig.grifaProdutoSemEstoqueEmUmaGradeNaLista && produto.isAlgumaGradeSemEstoque) {
					return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_GRADE_BACK;
				}
			} else if (LavenderePdaConfig.usaControleEstoquePorRemessa && ProdutoService.getInstance().isGrifaProdutoSemEstoqueNaRemessa(produto, pedido)) {
				return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK;
			} else if (!LavenderePdaConfig.usaControleEstoquePorRemessa && ProdutoService.getInstance().isGrifaProdutoSemEstoqueNaLista(produto, pedido.getCdLocalEstoque())) {
				return LavendereColorUtil.COR_PRODUTO_SEM_ESTOQUE_BACK;
			}
		}
		// PRODUTOS COM PRE-ALTA DE CUSTO EM VERMELHO
		if (LavenderePdaConfig.usaAvisoPreAlta) {
			if (ValueUtil.getBooleanValue(produto.flPreAltaCusto)) {
				return LavendereColorUtil.COR_PRODUTO_COM_AVISO_PRE_ALTA;
			}
		}
		// PRODUTOS COM ESTOQUE ABAIXO DO MINIMO
		if (LavenderePdaConfig.destacaProdutoQtMinEstoqueLista && produto.estoque != null) {
			if (produto.estoque.qtEstoque <= produto.estoque.qtEstoqueMin) {
				return LavendereColorUtil.COR_PRODUTO_ESTOQUE_MINIMO;
			}
		}
		// PRODUTOS COM DESCONTO PROMOCIONAL ESPECIFICO (PRM 730 + 908)
		if ((LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido() || LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedidoTipo3())
				&& LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
			if (ckProdutoDescPromocional.isChecked() || (!LavenderePdaConfig.usaDescPromo && DescPromocionalService.getInstance().isProdutoPossuiValorNoGrupoDescPromo(pedido, produto, cbTabelaPreco.getValue()))) {
				return LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK;
			}
		}
		// PRODUTOS COM RESTRICAO DE VENDA EM AMARELO
		if (LavenderePdaConfig.destacaProdutoQuantidadeMaximaVenda && produto.itemTabelaPreco != null && produto.itemTabelaPreco.qtMaxVenda > 0) {
				return LavendereColorUtil.COR_ITEMPEDIDO_COM_QTMAXVENDA_BACK;
			}
		// PRODUTOS PROMOCIONAIS (PRM 498)
		if (LavenderePdaConfig.isDestaqueNaListaDeProdutosDaTelaDeAdicionarItemPedidoETelaEstiloDesktop()) {
			if (produto instanceof ProdutoTabPreco) {
				String tabPrecoPromoList = ((ProdutoTabPreco) produto).dsTabPrecoPromoList;
				if (!LavenderePdaConfig.naoConsideraProdutoDescPromocionalComoPromocional && (ProdutoService.getInstance().isProdutoHaveDescPromocional(produto, pedido)
						|| ValueUtil.isNotEmpty(ItemPedidoService.getInstance().getCdTabelaProdPromocional(getCdsTabelaPrecoVisibleList(), tabPrecoPromoList)))) {
					return LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK;
				}
			} else if (LavenderePdaConfig.isUsaFiltraPorProdutosPromocionalListaProdutoEItemPedido() && produto.isProdutoPromocao()) {
				return LavendereColorUtil.COR_PRODUTO_PROMOCIONAL_BACK;
			}
		}
		// PRODUTOS COM DESCONTO PROMOCIONAL (tipo 1 e 2)
		if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedido() && !LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional() && produto instanceof ProdutoTabPreco) {
			String tabPrecoPromoList = ((ProdutoTabPreco) produto).dsTabPrecoDescPromocionalList;
			if (!ValueUtil.isEmpty(ItemPedidoService.getInstance().getCdTabelaProdComDescPromocional(getCdsTabelaPrecoVisibleList(), tabPrecoPromoList))) {
				return LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK;
			}
		}
		// PRODUTOS COM DESCONTO PROMOCIONAL (tipo 3)
		if (LavenderePdaConfig.isMostraProdutoDescPromocionalDestacadoTelaItemPedidoTipo3() && !LavenderePdaConfig.aplicaDescontoNoProdutoPorGrupoDescPromocional()) {
			if (produto.itemTabelaPreco != null && produto.itemTabelaPreco.vlPctDescPromocional > 0d) {
				return LavendereColorUtil.COR_PRODUTO_DESC_PROMOCIONAL_BACK;
			}
		}
		// GRUPO DE DESTAQUE
		if (LavenderePdaConfig.isUsaGrupoDestaqueProduto()) {
			if (produto.produtoDestaque != null && produto.produtoDestaque.corSistema != null && produto.produtoDestaque.corSistema.cdEsquemaCor > 0) {
				CorSistema corSistema = produto.produtoDestaque.corSistema;
				return Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB);
			}
		}

		// ITENS COM COM PRECO EM QUEDA
		if (LavenderePdaConfig.destacaProdutoComPrecoEmQueda && ItemTabelaPrecoService.getInstance().isItemComPrecoEmQueda(cbTabelaPreco.getValue(), produto.cdProduto, pedido.getCliente().dsUfPreco)) {
			return LavendereColorUtil.COR_PRODUTO_COM_PRECO_EM_QUEDA;
		}

		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito() && produto.isProdutoRestrito()) {
			return LavendereColorUtil.COR_PRODUTO_RESTRITO;
		}
		// PRODUTO COM LOTE EM VIDA UTIL CRITICA
		if (LavenderePdaConfig.grifaProdutoComVidaUtilLoteCriticoNaLista && ValueUtil.VALOR_SIM.equals(produto.flLoteProdutoCritico)) {
			return LavendereColorUtil.COR_FONTE_PRODUTO_LOTE_VIDA_UTIL_CRITICA;
		}
		// PRODUTO COM SUGESTAO PERSONALIZADA
		if (LavenderePdaConfig.isUsaSugestaoVendaPersonalizavelInicioPedido() && isProdutoJaInseridoFromSugPerson(produto.cdProduto)) {
			return LavendereColorUtil.COR_FUNDO_PRODUTO_INSERIDO_SUGVENDA;
		}
		// --
		return -1;
	}

	private String getEstoqueProduto(final ProdutoBase produto, boolean isProdutoAgrupadorGrade) throws SQLException {
		if (LavenderePdaConfig.isUsaFlIgnoraValidaco()) {
			if (produto.flIgnoraValidacao == null && produtoSelecionado != null) {
				produto.flIgnoraValidacao = produtoSelecionado.flIgnoraValidacao;
			}
			if (ValueUtil.getBooleanValue(produto.flIgnoraValidacao)) {
				return ValueUtil.VALOR_EMBRANCO;
			}
		}
		double qtEstoque;
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
			qtEstoque = LoteProdutoService.getInstance().getQtEstoqueLoteDisponivel(produto.cdProduto);
		} else {
			if (produto.estoque == null) {
				qtEstoque = EstoqueService.getInstance().getQtEstoque(produto.cdProduto, pedido.getCdLocalEstoque());
				if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
					qtEstoque = EstoqueService.getInstance().getSumEstoqueEstoquePrevistoGradeProduto(produto.cdProduto, null, pedido.getCdLocalEstoque(), null);
				}
			} else {
				if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
					EstoqueService.getInstance().setDadosParcialPrevisto(produto.estoque);
				}
				if (isProdutoAgrupadorGrade) {
					qtEstoque = EstoqueService.getInstance().getSumEstoqueAgrupadorGrade(produto.getDsAgrupadorGrade(), null, pedido.getCdLocalEstoque(), false);
				} else {
					qtEstoque = produto.estoque.qtEstoque;
				}

			}
			if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido()) {
				qtEstoque = getEstoqueToGridUnidadeAlternativa(produto, qtEstoque);
			}
		}
		return EstoqueService.getInstance().getEstoqueToString(qtEstoque) + Messages.PRODUTO_LABEL_EM_ESTOQUE;
	}

	protected String[] produtoToGridRow(final Object domain, String cdTabelaPreco) throws SQLException {
		ProdutoBase produto = (ProdutoBase) domain;
		Estoque estoqueEmp = new Estoque();
		ArrayList<CAMPOS_LISTA> campos = null;
		boolean insereMultSemNegociacao = isInsereMultiplosSemNegociacao();
		boolean addVlUnFim = false;
		int posEst = -1;
		if (insereMultSemNegociacao) {
			campos = new ArrayList<>(26);
			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			}
		}
		// --
		itens.removeAllElements();
		itens.setSize(0);
		boolean isProdutoAgrupadorGrade = LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && produto.isProdutoAgrupadorGrade();
		if (isProdutoAgrupadorGrade) {
			addDsAgrupadorGrade(produto);
		} else {
			addDescricaoProduto(produto);
			if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
        		itens.addElements(new String[]{"",""});
        	}
		}
		// --
		if (LavenderePdaConfig.isUsaFiltroPrincipioAtivoNoProduto()) {
			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
				campos.add(CAMPOS_LISTA.CAMPO_VAZIO);
			}
			double multiplier = LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosDentroPedido() ? 0.7 * 0.6 : 0.6;
			if (isProdutoAgrupadorGrade) {
				itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(produto.dsAgrupadorGradeComp), (int)(width * multiplier), listContainer.getFontSubItens()));
			} else {
				itens.addElement(StringUtil.getStringAbreviada(StringUtil.getStringValue(produto.dsPrincipioAtivo), (int)(width * multiplier), listContainer.getFontSubItens()));
			}
			itens.addElement("    ");
		}
		// --
		if (LavenderePdaConfig.mostraColunaEstoqueNaListaProdutoDentroPedido()) {
			itens.addElement(getVisibleStringEstoque(getEstoqueProduto(produto, isProdutoAgrupadorGrade)));
			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_ESTOQUE);
			}
			posEst = itens.size();
		}
		if ((LavenderePdaConfig.exibeHistoricoEstoqueCliente || LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) && insereMultSemNegociacao) {

			if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
				itens.addElement(Messages.ITEMPEDIDO_LABEL_ESTOQUE_HIST_2 + " " + StringUtil.getStringValueToInterface(produto.qtEstoqueClienteHistorico2 != null ? produto.qtEstoqueClienteHistorico2 : 0.0d));
			} else {
				itens.addElement("    ");
			}

			if (LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) {
				itens.addElement(Messages.ITEMPEDIDO_LABEL_QTVENDAS_HIST_2 + " " + StringUtil.getStringValueToInterface(produto.qtItemFisicoHistorico2 != null ? produto.qtItemFisicoHistorico2 : 0.0d));
			} else {
				itens.addElement("    ");
			}

			if (LavenderePdaConfig.exibeHistoricoEstoqueCliente) {
				itens.addElement(Messages.ITEMPEDIDO_LABEL_ESTOQUE_HIST_1 + " " + StringUtil.getStringValueToInterface(produto.qtEstoqueClienteHistorico1 != null ? produto.qtEstoqueClienteHistorico1 : 0.0d));
			} else {
				itens.addElement("    ");
			}

			if (LavenderePdaConfig.exibeHistoricoQtdeVendaDoItem) {
				itens.addElement(Messages.ITEMPEDIDO_LABEL_QTVENDAS_HIST_1 + " " + StringUtil.getStringValueToInterface(produto.qtItemFisicoHistorico1 != null ? produto.qtItemFisicoHistorico1 : 0.0d));
			} else {
				itens.addElement("    ");
			}

			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			}
		}
		// --
		if (LavenderePdaConfig.isMostraQtVendasProdutoNoPeriodo()) {
			boolean houveVendasPeriodo = produto.qtVendasPeriodo > 0;
			if (houveVendasPeriodo) {
				itens.addElement((produto).qtVendasPeriodo + " vend.");
			} else {
				itens.addElement("    ");
			}
			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			}
		}
		// --
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() && !pedido.isSugestaoPedidoGiroProduto() && !cadPedidoForm.solicitadoAcessoGiroProduto) {
			if (!LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() || !insereMultSemNegociacao) {
				double precoProduto = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.permiteAlterarValorItemComIPI ? produto.vlProdutoIpi : produto.vlProduto;
				if (LavenderePdaConfig.mostraPrecoUnidadeItem) {
					itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_RS, StringUtil.getStringValueToInterface(getVlProdutoComIndiceFinanceiro(produto))));
					double nuConversaoUnidadesMedida = produto.nuConversaoUnidadesMedida;
					if (nuConversaoUnidadesMedida == 0) {
						nuConversaoUnidadesMedida = ProdutoService.getInstance().getProduto(produto.cdProduto).nuConversaoUnidadesMedida;
						if (nuConversaoUnidadesMedida == 0) {
							nuConversaoUnidadesMedida = 1;
						}
					}
					itens.addElement(getVisibleStringVlItem(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_UNID_RS, StringUtil.getStringValueToInterface(precoProduto / nuConversaoUnidadesMedida))));
				} else {
					itens.addElement(getVisibleStringVlItem(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(precoProduto)));
				}
				if (insereMultSemNegociacao) {
					campos.add(CAMPOS_LISTA.CAMPO_VLUN);
					if (LavenderePdaConfig.mostraPrecoUnidadeItem) {
						campos.add(CAMPOS_LISTA.CAMPO_FIXO);
					}
				}
			} else {
				addVlUnFim = true;
			}
		} else if (pedido.isSugestaoPedidoGiroProduto() || (cadPedidoForm != null && cadPedidoForm.solicitadoAcessoGiroProduto)) {
			itens.addElement(" ");
			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			}
		}
		if (LavenderePdaConfig.isMostraPrecoComSTNaListaDeItensPedido()) {
			itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_PRECO_COMST_RS, StringUtil.getStringValueToInterface(getVlProdutoComST(produto, pedido.getCliente()))));
		}
		// --
		if (LavenderePdaConfig.mostraPrevisaoDescontoGridProdutos && (LavenderePdaConfig.isAplicaDescontoFimDoPedido() || LavenderePdaConfig.isAplicaDescontoProgressivoPorItemFinalPedido())) {
			ItemPedido item = new ItemPedido();
			item.cdProduto = produto.cdProduto;
			item.cdTabelaPreco = cdTabelaPreco;
			item.cdUfClientePedido = pedido.getCliente().dsUfPreco;
			double prevDesconto = ItemPedidoService.getInstance().getMaiorPctDescontoParaItemPedido(pedido, item);
			itens.addElement(StringUtil.getStringValueToInterface(prevDesconto) + "%");
			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			}
		}
		if (LavenderePdaConfig.isMostraPrecoItemStNaListaProdutoDoPedido()) {
			double valorUnitarioUnidadeST;
        	if (produto.nuConversaoUnidadesMedida == 0.0) {
        		valorUnitarioUnidadeST = produto.itemTabelaPreco.vlEmbalagemSt;
        	} else {
        		valorUnitarioUnidadeST = produto.itemTabelaPreco.vlEmbalagemSt / produto.nuConversaoUnidadesMedida;
        	}
			itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_VALOR_ST, StringUtil.getStringValueToInterface(produto.itemTabelaPreco.vlEmbalagemSt)));
			itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_VALOR_UNIDADE_ST, StringUtil.getStringValueToInterface(valorUnitarioUnidadeST)));
		}
		if (LavenderePdaConfig.isMostraEstoquePrevistoNaListaProdutosDentroPedido()) {
		    if (produto.estoque == null) {
		    	produto.estoque = EstoqueService.getInstance().getEstoque(produto.cdProduto, Estoque.FLORIGEMESTOQUE_ERP);
		    	if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
		    		EstoqueService.getInstance().setDadosEstoqueParcialPrevisto(null, produto.estoque);
		    	}
		    }
		    if (LavenderePdaConfig.isSemQuantidadeFracionada(LavenderePdaConfig.QTDESTOQUEPREVISTO)) {
				itens.addElement(EstoqueService.getInstance().getEstoquePrevistoToString(produto.estoque.qtEstoquePrevisto) + Messages.PRODUTO_LABEL_ESTOQUE_PREVISTO);
			} else {
				itens.addElement(StringUtil.getStringValueToInterface(produto.estoque.qtEstoquePrevisto) + Messages.PRODUTO_LABEL_ESTOQUE_PREVISTO);
			}
			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_ESTPREVISTO);
			}
		}
		if (LavenderePdaConfig.mostraColunaMarcaNaListaProdutoDentroPedido()) {
			itens.addElement(produto.dsMarca == null ? "    " : produto.dsMarca);
			if (insereMultSemNegociacao) {
				campos.add(CAMPOS_LISTA.CAMPO_FIXO);
			}
		}
		if (LavenderePdaConfig.apresentaEstoqueDaRemessaEmpresaNaListaProdutos) {
			double qtEstoque = estoqueEmp.qtEstoque;
			if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
				qtEstoque = getEstoqueToGridUnidadeAlternativa(produto, qtEstoque);
			}
			if (posEst == -1) {
				itens.addElement(StringUtil.getStringValueToInterface(qtEstoque) + " " + Messages.PRODUTO_LABEL_EM_ESTOQUE_EMPRESA);
				if (insereMultSemNegociacao) {
					campos.add(CAMPOS_LISTA.CAMPO_FIXO);
				}
			} else {
				itens.insertElementAt(StringUtil.getStringValueToInterface(qtEstoque) + " " + Messages.PRODUTO_LABEL_EM_ESTOQUE_EMPRESA, posEst + 1);
				if (insereMultSemNegociacao) {
					campos.add(posEst + 1, CAMPOS_LISTA.CAMPO_FIXO);
				}
			}
		}
        if (LavenderePdaConfig.usaFiltroComissao) {
			if (pedido.isStatusPedidoNaoOcultaValoresComissao()) {
				itens.addElement(Messages.PRODUTO_LABEL_COMISSAO + " " + StringUtil.getStringValueToInterface(produto.vlPctComissao) + " %");
			} else {
				itens.addElement("    ");
	        }
        }

        if (LavenderePdaConfig.mostraBonusListaProduto) {
        	double valorBonusProduto = ProdutoService.getInstance().getValorBonusProdutoNaLista(produto.itemTabelaPreco);
        	itens.addElement(MessageUtil.getMessage(Messages.PRODUTO_LABEL_BONUS, Messages.MOEDA + " " + StringUtil.getStringValueToInterface(valorBonusProduto)));
	        if (insereMultSemNegociacao) {
		        campos.add(CAMPOS_LISTA.CAMPO_FIXO);
	        }
        }

        if (LavenderePdaConfig.isMostraFotoProdutoNaListaProdutosDentroPedido() && !insereMultSemNegociacao) {
        	while (itens.size() < 9) {
        		itens.addElement("");
        		if (insereMultSemNegociacao) {
        			campos.add(CAMPOS_LISTA.CAMPO_VAZIO);
        		}
        	}
        }
        if (insereMultSemNegociacao && LavenderePdaConfig.showInformacoesComplementaresInsercMultipla()) {
        	if (isFiltrandoAgrupadorGrade()) {
        	    int[] dominios = LavenderePdaConfig.getDominiosInformacoesComplementaresInsercMultipla();
        	    for (int i = 0; i < dominios.length; i++) {
        	       itens.addElement("");
        	       campos.add(CAMPOS_LISTA.CAMPO_VAZIO);
        	    }
        	} else {
        	    ItemPedido itemPedido = new ItemPedido();
        	    itemPedido.setPedido(pedido);
        	    itemPedido.setProduto((Produto) produto);
        	    ItemPedidoService.getInstance().aplicaQtdElementarItemPedido(itemPedido, cbUnidadeAlternativa.getProdutoUnidadeSelected());
        	    addInformacoesComplementaresInsercao(produto, campos, cdTabelaPreco, itemPedido);
        	}
        }
        if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && LavenderePdaConfig.isOcultaInterfaceNegociacaoMultiplosItens() && LavenderePdaConfig.naoPermiteInserirQtdDescMultipla()) {
        	calculateItemPedidoQtUnitario(produto);
        	if (!LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
        		itens.addElement(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(getItemPedido().getVlFreteESeguro()));
        		itens.addElement(getVisibleStringVlItem(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + StringUtil.getStringValueToInterface(getItemPedido().vlTotalItemPedido)));
        		if (insereMultSemNegociacao) {
        			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
        			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
        		}
        	} else {
        		itens.addElement(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(getItemPedido().getVlFreteESeguro()));
        		double vlStItem = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? getItemPedido().vlTotalStItem : getItemPedido().vlSt;
        		double vlIpiItem = LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado ? getItemPedido().vlTotalIpiItem : getItemPedido().vlIpiItem;
        		double vlTributosComVlAdicionais = getItemPedido().getVlTributos() + getItemPedido().vlDespesaAcessoria;
        		double vlDeducoes = getVlDeducoesItem(vlTributosComVlAdicionais);
        		double vlTotalComTributos = ValueUtil.round(getItemPedido().vlTotalItemPedido + (LavenderePdaConfig.usaPrecoItemComValoresAdicionaisEmbutidos ? 0 : (getItemPedido().vlItemPedidoFrete * getItemPedido().getQtItemFisico())) + (vlTributosComVlAdicionais * getItemPedido().getQtItemFisico()) - (vlDeducoes * getItemPedido().getQtItemFisico()));
        		itens.addElement(Messages.ITEMPEDIDO_LABEL_VL_TOTALBRUTO_ITEMPEDIDO + " " + StringUtil.getStringValueToInterface(vlTotalComTributos));
        		itens.addElement(Messages.ITEMPEDIDO_LABEL_VL_IPI + " " + StringUtil.getStringValueToInterface(ValueUtil.round(vlIpiItem, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi)));
        		itens.addElement(Messages.ITEMPEDIDO_LABEL_VLST + " " + StringUtil.getStringValueToInterface(ValueUtil.round(vlStItem, LavenderePdaConfig.nuCasasDecimaisVlStVlIpi)));
        		if (insereMultSemNegociacao) {
        			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
        			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
        			campos.add(CAMPOS_LISTA.CAMPO_FIXO);
        		}
        	}
        	setProdutoSelecionado(null);
		} else if (insereMultSemNegociacao) {
			if (LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao()) {
				if (addVlUnFim) {
					if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado && LavenderePdaConfig.permiteAlterarValorItemComIPI) {
						itens.addElement(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(produto.vlProdutoIpi));
					} else {
						itens.addElement(Messages.MOEDA + " " + StringUtil.getStringValueToInterface(produto.vlProduto));
					}
					campos.add(CAMPOS_LISTA.CAMPO_VLUN);
				}
			}
			addCampoVlT(produto, campos, insereMultSemNegociacao);
		}
		if (insereMultSemNegociacao) {
			this.campos = campos.toArray(new CAMPOS_LISTA[campos.size()]);
		}
		return (String[]) itens.toObjectArray();
	}

	private void addDsAgrupadorGrade(ProdutoBase produto) {
		itens.addElement(produto.getDsAgrupadorGrade());
		itens.addElement("");
		if (LavenderePdaConfig.usaQuebraLinhaDescricaoProdutoNaLista) {
			itens.addElement("");
			itens.addElement("");
		}
	}

	private void addDescricaoProduto(ProdutoBase produto) {
		if (!LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
			itens.addElement(produto.cdProduto + " - ");
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement("[" + StringUtil.getStringValue(produto.dsReferencia) + "] " + " - " + StringUtil.getStringValue(produto.dsProduto));
			} else {
				itens.addElement(StringUtil.getStringValue(produto.dsProduto) + " - " + " [" + StringUtil.getStringValue(produto.dsReferencia) + "]");
			}
		} else if (!LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
			itens.addElement(produto.cdProduto + " - ");
			itens.addElement(StringUtil.getStringValue(produto.dsProduto));
		} else if (LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
			if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
				itens.addElement("[" + StringUtil.getStringValue(produto.dsReferencia) + "] ");
				itens.addElement(StringUtil.getStringValue(produto.dsProduto));
			} else {
				itens.addElement(StringUtil.getStringValue(produto.dsProduto));
				itens.addElement(" [" + StringUtil.getStringValue(produto.dsReferencia) + "]");
			}
		} else if (LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
			itens.addElement(produto.dsProduto);
			itens.addElement("");
		}
	}

	private void addCampoVlT(ProdutoBase produto, ArrayList<CAMPOS_LISTA> campos, boolean insereMultSemNegociacao) throws SQLException {
		if (isFiltrandoAgrupadorGrade() && produto.isProdutoAgrupadorGrade() && !LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			addCampoVlTAgrupador(produto);
		} else {
			addCampoVlTNormal(produto);
		}
		if (insereMultSemNegociacao) {
			campos.add(CAMPOS_LISTA.CAMPO_VLT);
		}
	}

	private void addCampoVlTNormal(ProdutoBase produto) {
		int size = pedido.itemPedidoList.size();
		boolean found = false;
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (itemPedido.cdProduto.equals(produto.cdProduto)) {
				itens.addElement(getVisibleStringVlItem(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido)));
				found = true;
				break;
			}
		}
		if (!found) {
			itens.addElement(getVisibleStringVlItem(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(0d)));
		}
		}

	private void addCampoVlTAgrupador(ProdutoBase produto) throws SQLException {
		double vlItemPedidoAgrupador = ItemPedidoService.getInstance().sumVlTotalItemAgrupadorGrade(pedido, produto.getDsAgrupadorGrade());
		itens.addElement(Messages.ITEMPEDIDO_LABEL_VLTABELAITEMPEDIDO + " " + StringUtil.getStringValueToInterface(vlItemPedidoAgrupador));
	}

	private void addInformacoesComplementaresInsercao(ProdutoBase produto, ArrayList<CAMPOS_LISTA> campos, String cdTabelaPreco, ItemPedido itemPedidoEmb) throws SQLException {
		int[] dominios = LavenderePdaConfig.getDominiosInformacoesComplementaresInsercMultipla();
		for (int opcao : dominios) {
			switch (opcao) {
				case 1:
					itens.addElement(Messages.ITEMPEDIDO_LABEL_PRECOEMBPRIMARIA + " " + StringUtil.getStringValueToInterface(produto.vlEmbalagemElementar));
					campos.add(CAMPOS_LISTA.CAMPO_VEP);
					break;
				case 2:
					itens.addElement(Messages.ITEMPEDIDO_LABEL_CAIXAPADRAO + " " + itemPedidoEmb.getQtEmbalagemElementarToInterface());
					campos.add(CAMPOS_LISTA.CAMPO_QE);
					break;
				case 3:
					itens.addElement(Messages.MULTIPLASSUGESTOES_LABEL_VLHIST + " " + StringUtil.getStringValueToInterface(produto.vlMedioHistorico));
					campos.add(CAMPOS_LISTA.CAMPO_VLHIST);
					break;
				case 4:
					itens.addElement(Messages.MULTIPLASSUGESTOES_LABEL_QTHIST + " " + StringUtil.getStringValueToInterface(produto.qtdMediaHistorico));
					campos.add(CAMPOS_LISTA.CAMPO_QTHIS);
					break;
				case 5:
					itens.addElement(Messages.MULTIPLASSUGESTOES_LABEL_V_UE + " " + StringUtil.getStringValueToInterface(getItemPedidoService().calculaVlUnidadePorEmbalagemWhenProdutoBase(getItemPedido(),pedido, produto, cdTabelaPreco)));// veio da pesquisa
					campos.add(CAMPOS_LISTA.CAMPO_VUE);
					break;
				case 6:
					itens.addElement(Messages.ITEMTABELAPRECO_LABEL_VLBASEFLEX + " " + StringUtil.getStringValueToInterface(produto.vlBaseFlex));
					campos.add(CAMPOS_LISTA.CAMPO_FIXO);
				}
		}
	}

	private int getItemCountInformacoesComplementaresInsercao() throws SQLException {
		int[] dominios = LavenderePdaConfig.getDominiosInformacoesComplementaresInsercMultipla();
		int itemCount = 0;
		for (int opcao : dominios) {
			switch (opcao) {
				case 1:
					itemCount++;
					break;
				case 2:
					itemCount++;
					break;
				case 3:
					itemCount++;
					break;
				case 4:
					itemCount++;
					break;
				case 5:
					itemCount++;
					break;
				case 6:
					itemCount++;
				}
		}
		return itemCount;
	}

	private double getEstoqueToGridUnidadeAlternativa(ProdutoBase produtoBase, double qtEstoque) throws SQLException {
		ItemPedido itemPedidoClone = (ItemPedido) getItemPedido().clone();
		Produto produto = new Produto();
		itemPedidoClone.setProduto(produto);
		produto.cdEmpresa = produtoBase.cdEmpresa;
		produto.cdRepresentante = produtoBase.cdRepresentante;
		produto.cdProduto = produtoBase.cdProduto;
		produto.cdUnidade = produtoBase.cdUnidade;
		Vector produtoUnidadeList = ((ProdutoBase) produtoBase).produtoUnidadeList;
		return EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedidoClone.getItemTabelaPreco(), produtoUnidadeList != null && produtoUnidadeList.size() > 1 ? cbUnidadeAlternativa.getProdutoUnidade() : itemPedidoClone.getProdutoUnidade(), qtEstoque);
	}

	protected void calculateItemPedidoQtUnitario(ProdutoBase produto) throws SQLException {
		setProdutoSelecionado(ProdutoService.getInstance().getProduto(produto.cdProduto));
		try {
			inicializaItemParaVenda(getItemPedido());
			getItemPedido().tributacaoConfig = produto.tributacaoConfig;
			changeItemTabelaPreco(null, false, produto.itemTabelaPreco);
			getItemPedido().cdTabelaPreco = pedido.cdTabelaPreco;
			getItemPedido().setQtItemFisico(1);
			PedidoService.getInstance().calculateItemPedido(pedido, getItemPedido(), false);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	protected void loadVlProdutoToList(Vector produtoList) throws SQLException {
		if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido() || LavenderePdaConfig.isPermiteEditarValorBaseMultiplaInsercaoSemNegociacao() || LavenderePdaConfig.isPermiteEditarValorUnitarioMultiplaInsercao()) {
			ItemPedido itemPedidoAux = new ItemPedido();
			if (LavenderePdaConfig.permiteAlterarValorItemComIPI && LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
				itemPedidoAux.setQtItemFisico(1);
				itemPedidoAux.setPedido(pedido);
				itemPedidoAux.cdEmpresa = pedido.cdEmpresa;
			}
			int size = produtoList.size();
			for (int i = 0; i < size; i++) {
				ProdutoBase produtoBase = (ProdutoBase) produtoList.items[i];
				if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
					produtoBase.produtoUnidadeList = ProdutoUnidadeService.getInstance().getProdutoUnidadeListSorted(produtoBase, pedido);
				}
				produtoBase.vlProduto = ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produtoBase, cbTabelaPreco.getValue(), LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false);
				if (LavenderePdaConfig.permiteAlterarValorItemComIPI && LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
					loadVlProdutoIpi(produtoBase, itemPedidoAux);
				}
			}
		}
	}

	protected void loadVlPctComissaoList(Vector produtoList) throws SQLException {
		if (!LavenderePdaConfig.usaFiltroComissao) return;
		int size = produtoList.size();
		for (int i = 0; i < size; i++) {
			ProdutoBase produtoBase = (ProdutoBase) produtoList.items[i];
			loadPctComissaoProduto(produtoBase);
		}
	}

	private void loadPctComissaoProduto(ProdutoBase produtoBase) throws SQLException {
			produtoBase.vlPctComissao = ItemTabelaPrecoService.getInstance().getVlPctComissaoToListaAdicionarItens(pedido, produtoBase, cbTabelaPreco.getValue());
			produtoBase.dsPctComissao = ValueUtil.addLeadingZeros(produtoBase.vlPctComissao, 7) + produtoBase.dsProduto;
		}

	private void loadVlProdutoIpi(ProdutoBase produtoBase, ItemPedido itemPedidoAux) throws SQLException {
		if (produtoBase != null && itemPedidoAux != null) {
			Produto produto = new Produto();
			produto.cdEmpresa = produtoBase.cdEmpresa;
			produto.cdRepresentante = produtoBase.cdRepresentante;
			produto.cdProduto = produtoBase.cdProduto;
			produto.cdTributacaoProduto = produtoBase.cdTributacaoProduto;
			itemPedidoAux.vlItemIpi = 0;
			itemPedidoAux.vlBaseItemIpi = 0;
			itemPedidoAux.tributacaoConfig = produtoBase.tributacaoConfig;
			itemPedidoAux.cdProduto = produtoBase.cdProduto;
			itemPedidoAux.setProduto(produto);
			itemPedidoAux.vlItemPedido = produtoBase.vlProduto;
			itemPedidoAux.vlTotalItemPedido = produtoBase.vlProduto;
			ItemPedidoService.getInstance().calculaIpiItemPedido(itemPedidoAux);
			produtoBase.vlProdutoIpi = itemPedidoAux.vlItemIpi;
		}
	}

	protected String getToolTip(Object domain) {
		ProdutoBase produto = (ProdutoBase) domain;
		strToolTip.setLength(0);
    	String dsReferencia = StringUtil.getStringValue(produto.dsReferencia);
    	final String dsProduto = StringUtil.getStringValue(produto.dsProduto);
		boolean isProdutoAgrupadorGrade = LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && produto.isProdutoAgrupadorGrade();
    	if (isProdutoAgrupadorGrade) {
    		strToolTip.append(dsProduto).append("");
    	} else {
    	if (!LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
    		strToolTip.append(produto.cdProduto).append(" - ");
         	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
         		strToolTip.append("[").append(dsReferencia).append("] ").append(dsProduto).toString();
        	} else {
        		strToolTip.append(dsProduto).append(" [").append(dsReferencia).append("]");
			}
    	} else if (!LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
    		strToolTip.append(produto.cdProduto).append(" - ").append(dsProduto);
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	if (LavenderePdaConfig.isMostraDescricaoReferenciaAntesDsProduto()) {
        		strToolTip.append("[").append(dsReferencia).append("] ").append(dsProduto);
        	} else {
        		strToolTip.append(dsProduto).append(" [").append(dsReferencia).append("]");
        	}
        } else if (LavenderePdaConfig.ocultaColunaCdProduto && !LavenderePdaConfig.isMostraDescricaoReferencia()) {
        	strToolTip.append(produto.toString()).append("");
        }
    	}
		return strToolTip.toString();
	}

	// **************** M?todos de controle das preferencias de ordena??o da grid *******************
	// **********************************************************************************************
	// **********************************************************************************************

	protected CrudService getConfigService() {
		return ConfigInternoService.getInstance();
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
			ListContainerConfig.listasConfig.put(((ConfigInterno) domainConfig).vlChave, StringUtil.split(((ConfigInterno) domainConfig).vlConfigInterno, ConfigInterno.defaultSeparatorInfoValue));
			if (getConfigService().findByRowKey(domainConfig.getRowKey()) == null) {
				getConfigService().insert(domainConfig);
			} else {
				getConfigService().update(domainConfig);
			}
		} catch (Throwable e) {
			// N?o faz nada
		}
	}

	protected void previsaoDescontoClick() {
		if (LavenderePdaConfig.aplicaDescProgressivoPorItemFinalPedidoConsumindoFlex) {
			pedido.fecharPedidoComVerbaNeg = false;
			RelPreviaDescontosComVerbaWindow relPreviaDescontosComVerbaForm = new RelPreviaDescontosComVerbaWindow(pedido, true);
			relPreviaDescontosComVerbaForm.popup();
		}
	}

	private void setTecladoEnableInFields(final boolean enabled) {
		edVlPctDesconto.enableTeclado = enabled;
		edVlPctAcrescimo.enableTeclado = enabled;
		edVlVerbaManual.enableTeclado = enabled;
		edQtItemFisico.enableTeclado = enabled;
		edQtDesejada.enableTeclado = enabled;
		edQtItemFaturamento.enableTeclado = enabled;
		edVlItemPedido.enableTeclado = enabled;
		edPrecoEmbPrimaria.enableTeclado = enabled;
		edVlItemIpi.enableTeclado = enabled;
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra1()) {
			edVlPctDesconto2.enableTeclado = enabled;
			edVlPctDesconto3.enableTeclado = enabled;
		}
	}

	private void addComponenteVenda(Container container, Control ctr, int widthComponent) {
		// QTITEMFISICO
		if (ctr.appId == 1 && (LavenderePdaConfig.isUsaGradeProduto1A4() || isModoGrade())) {
			UiUtil.add(container, ctr, AFTER + WIDTH_GAP, SAME, widthComponent - UiUtil.getControlPreferredHeight(), UiUtil.getControlPreferredHeight());

			if (isModoGrade() || LavenderePdaConfig.isUsaGradeProduto1A4()) {
				UiUtil.add(container, btGradeItemPedido, AFTER, SAME, UiUtil.getControlPreferredHeight(),UiUtil.getControlPreferredHeight());
			}
		} else if (ctr.appId == 4 && LavenderePdaConfig.usaGradeProduto5() && (!isModoGrade() || isModoGrade() && LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade())) {
			UiUtil.add(container, edVlItemPedido, AFTER + WIDTH_GAP, SAME, widthComponent, UiUtil.getControlPreferredHeight());
		} else {
			UiUtil.add(container, ctr, AFTER + WIDTH_GAP, SAME, widthComponent, UiUtil.getControlPreferredHeight());
		}
	}

	public Produto getSelectedProduto() throws SQLException {
		return (Produto) ProdutoService.getInstance().findByRowKey(listContainer.getSelectedId());
	}

	private boolean isProdutoFromSugestaoIndustria() {
		if (LavenderePdaConfig.isInformaQtdSugeridaProdutoIndustria()) {
			final ProdutoBase produtoSelecionado = getProdutoSelecionadoNaLista();
			return produtoSelecionado != null && produtoSelecionado.produtoIndustriaSugestao != null;
		}
		return false;
	}

	private ProdutoBase getProdutoSelecionadoNaLista() {
		final Container selectedItem = listContainer.getSelectedItem();
		if (selectedItem != null) {
			return (ProdutoBase)((Item)selectedItem).domain;
		}
		return null;
	}

	public void setFocusInQtde() throws SQLException {
		if (!listMaximized) {
			if (LavenderePdaConfig.usaConversaoUnidadesMedida && flFocoQtFaturamento && lbQtItemFaturamento.isDisplayed()) {
				edQtItemFaturamento.requestFocus();
				edQtItemFaturamento.setCursorPos(edQtItemFaturamento.getLength(), edQtItemFaturamento.getLength());
			} else {
				if (LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido) {
					edQtDesejada.requestFocus();
					edQtDesejada.setCursorPos(edQtDesejada.getLength(), edQtDesejada.getLength());
				} else {
					edQtItemFisico.requestFocus();
					edQtItemFisico.setCursorPos(edQtItemFisico.getLength(), edQtItemFisico.getLength());
				}
			}
	}
	}

	public void setFocusInFiltro() {
		if (!listMaximized && !inVendaUnitariaMode) {
			edFiltro.requestFocus();
		}
	}

	public void edQtItemRequestFocus(ItemPedido itemPedido, boolean inQtItemFaturamento) throws SQLException {
		itemPedido.flEditandoQtItemFaturamento = inQtItemFaturamento;
		itemPedido.flTipoEdicao = inQtItemFaturamento ? ItemPedido.ITEMPEDIDO_EDITANDO_QTD_FATURAMENTO: ItemPedido.ITEMPEDIDO_EDITANDO_QTD;
		flFocoQtFaturamento = inQtItemFaturamento;
	}

	public void setEditableEditsValor() throws SQLException {
		boolean isEnabled = isEnabled();
		ItemPedido itemPedido = getItemPedido();
		edVlUnidadeEmbalagem.setEditable(isEnabled && LavenderePdaConfig.permiteAlterarValorDaUnidadePrecoPorEmbalagem && !itemPedido.isCombo() && !itemPedido.isKitTipo3());
		edVlItemPedido.setEditable(isEnabled && ((LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() || (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha()))
				&& !LavenderePdaConfig.usaCalculoReversoNaST && !LavenderePdaConfig.usaPercDescGrupoProdutoOuClienteVip && ValueUtil.isEmpty(pedido.getCliente().dsDescontoBloqueadoList) || (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade())));
		edVlPctDesconto.setEditable(isEdVlPctDescontoEditable());
		edVlPctAcrescimo.setEditable(isEnabled && LavenderePdaConfig.isPermiteAcrescimoPercentualItemPedido() && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL));
		edPctMargemAgregada.setEditable(isEnabled);
		edVlAgregadoSugerido.setEditable(isEnabled);
		controlaVisibilidadeVlEmbalagemElementar();
		if (LavenderePdaConfig.usaBloqueioAlteracaoPrecoPedidoPorCliente) {
			bloqueiaAlteracaoPreco();
		}
		if ((LavenderePdaConfig.isExibeComboMenuInferior() && itemPedido.isCombo()) || itemPedido.isKitTipo3()) {
			bloqueiaEdicaoValoresItem();
		}
		if (LavenderePdaConfig.ignoraAcrescimo && itemPedido.isRecebeuDescontoPorQuantidade()) {
			edVlPctAcrescimo.setEditable(false);
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
			boolean isItemPedidoAutorizadoOuPendente = itemPedido.solAutorizacaoItemPedidoCache.getIsItemPedidoAutorizadoOuPendente(itemPedido, TipoSolicitacaoAutorizacaoEnum.NEGOCIACAO_PRECO);
			if (isItemPedidoAutorizadoOuPendente) {
				bloqueiaEdicaoValoresItem();
			}
		}
		edVlItemStReverso.setEditable(isEnabled && LavenderePdaConfig.usaCalculoReversoNaST);
		edVlPctDescontoStReverso.setEditable(isEnabled && LavenderePdaConfig.usaCalculoReversoNaST);
		controlEditabilidadeVlItemIpi();
		if (LavenderePdaConfig.usaNuOrdemCompraENuSeqClienteItemPedido()) {
			edNuOrdemCompraCliente.setEditable(isEnabled);
			edNuSeqOrdemCompraCliente.setEditable(isEnabled);
		}
	}

	private void bloqueiaEdicaoValoresItem() {
		edVlPctAcrescimo.setEditable(false);
		edVlPctDesconto.setEditable(false);
		edVlItemPedido.setEditable(false);
		edQtItemFisico.setEditable(false);
	}

	private void controlEditabilidadeVlItemIpi() throws SQLException {
		if (LavenderePdaConfig.permiteAlterarValorItemComIPI && isAlteracaoPrecoNaoControladoPelaOportunidade()) {
			edVlItemIpi.setEditable(isEnabled() && (LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() || (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha())));
			edVlItemPedido.setEditable(isEnabled() && (LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() || (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha())) && !(edVlItemIpi.isDisplayed() && LavenderePdaConfig.permiteAlterarValorItemComIPI) && ValueUtil.isEmpty(pedido.getCliente().dsDescontoBloqueadoList));
		} else {
			edVlItemIpi.setEditable(false);
		}
	}

	public void controlaVisibilidadeVlEmbalagemElementar() throws SQLException {
		if (LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && isAlteracaoPrecoNaoControladoPelaOportunidade()) {
			boolean editable = isEnabled() && LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && (LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() || (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha()));
			edPrecoEmbPrimaria.setEditable(editable);
			// --
			boolean permiteAlterarVlItemNaUnidadeElementar = LavenderePdaConfig.isPermiteAlterarVlItemNaUnidadeElementar() && edPrecoEmbPrimaria.isDisplayed();
			edVlItemPedido.setEditable(isEnabled() && (LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() || (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha())) && !permiteAlterarVlItemNaUnidadeElementar && ValueUtil.isEmpty(pedido.getCliente().dsDescontoBloqueadoList));
		} else {
			edPrecoEmbPrimaria.setEditable(false);
		}
	}

	public boolean isAlteracaoPrecoNaoControladoPelaOportunidade() throws SQLException {
		if (LavenderePdaConfig.usaOportunidadeVenda && (getItemPedido().isOportunidade() || (getItemPedido().pedido != null && getItemPedido().pedido.isOportunidade()))) {
			return false;
		}
		return true;
	}

	public void bloqueiaAlteracaoPreco() throws SQLException {
		if (LavenderePdaConfig.usaBloqueioAlteracaoPrecoPedidoPorCliente && isAlteracaoPrecoNaoControladoPelaOportunidade()) {
			boolean permiteAlteracaoPreco = true;
			if (getItemPedido().getProduto() != null && SessionLavenderePda.getCliente() != null) {
				permiteAlteracaoPreco = RestricaoVendaCliService.getInstance().isPermiteAlteracaoPreco(getItemPedido().getProduto(), SessionLavenderePda.getCliente());
				if (!permiteAlteracaoPreco) {
					edVlPctAcrescimo.setEditable(permiteAlteracaoPreco && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL));
					edVlPctDesconto.setEditable(permiteAlteracaoPreco && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL));
					edVlItemPedido.setEditable(permiteAlteracaoPreco && ValueUtil.isEmpty(pedido.getCliente().dsDescontoBloqueadoList));
				} else {
					edVlPctDesconto.setEditable(isEnabled() && LavenderePdaConfig.isPermiteDescontoPercentualItemPedido() && !LavenderePdaConfig.usaAplicacaoMaiorDescontoEmCascata && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL));
					edVlPctAcrescimo.setEditable(isEnabled() && LavenderePdaConfig.isPermiteAcrescimoPercentualItemPedido() && !ClienteService.getInstance().isAcrescimoBloqueado(pedido.getCliente(),Cliente.ACRESCIMO_BLOQUEADO_MANUAL));
					edVlItemPedido.setEditable(isEnabled() && (LavenderePdaConfig.isPermiteAlterarVlUnitarioItemPedido() || (LavenderePdaConfig.liberaComSenhaPrecoDeVenda && pedido.isFlPrecoLiberadoSenha())) && ValueUtil.isEmpty(pedido.getCliente().dsDescontoBloqueadoList));
				}
			}
		}
	}

	public void setEnableComponentsPossuemVariacao() throws SQLException {
		setEditableEditsValor();
		if (LavenderePdaConfig.usaOportunidadeVenda) {
			edVlVerbaItem.setVisible(!getItemPedido().isOportunidade());
			edVlVerbaItemPositiva.setVisible(!getItemPedido().isOportunidade());
			edVlVerbaItemNegativa.setVisible(!getItemPedido().isOportunidade());
			lbVlVerbaItem.setVisible(!getItemPedido().isOportunidade());
			lbVlVerbaItemPositiva.setVisible(!getItemPedido().isOportunidade());
			lbVlVerbaItemNegativa.setVisible(!getItemPedido().isOportunidade());
			edVlVerbaPedido.setVisible(!getItemPedido().isOportunidade());
			lbVlVerbaPedido.setVisible(!getItemPedido().isOportunidade());
			edVlBaseFlex.setVisible(!getItemPedido().isOportunidade());
			lbVlBaseFlex.setVisible(!getItemPedido().isOportunidade());
			edVlVerbaManual.setVisible(!getItemPedido().isOportunidade());
			lbVlVerbaManual.setVisible(!getItemPedido().isOportunidade());
			if (!isAlteracaoPrecoNaoControladoPelaOportunidade()) {
				edVlUnidadeEmbalagem.setEditable(false);
				edVlItemPedido.setEditable(false);
				edVlPctDesconto.setEditable(false);
				edVlPctAcrescimo.setEditable(false);
				edPctMargemAgregada.setEditable(false);
				edVlAgregadoSugerido.setEditable(false);
				edPrecoEmbPrimaria.setEditable(false);
				edVlVerbaManual.setEditable(false);
				edVlItemIpi.setEditable(false);
			}
		}
		if (LavenderePdaConfig.isConfigGradeProduto()) {
			if (LavenderePdaConfig.usaGradeProduto2()) {
				btGradeItemPedido.setEnabled(cbItemGrade1.size() > 0);
				lvEstoque.setEnabled(cbItemGrade1.size() > 0);
				edQtItemFisico.setEditable(isEnabled() && (cbItemGrade1.size() == 0));
				edQtItemFaturamento.setEditable(isEnabled() && (cbItemGrade1.size() == 0));
			} else {
				boolean enabled = cbItemGrade1.size() > 0 && produtoComGradeNivel2;
				lvEstoque.setEnabled(enabled);
				lvEstoque.setGradeEstoqueButtonVisibility(isModoGrade());

				lvTotalPedido.setEnabled(enabled);
				lvTotalPedido.setGradeEstoqueButtonVisibility(!LavenderePdaConfig.usaGradeProduto5());

				lvVlItemPedido.setEnabled(enabled);
				lvVlItemPedido.setGradeEstoqueButtonVisibility(isModoGrade() || !LavenderePdaConfig.usaGradeProduto5());

				edQtItemFisico.setEditable(isEdQtItemEditable());
				edQtItemFaturamento.setEditable(isEdQtItemEditable());

				btGradeItemPedido.setVisible(isBtGradeItemPedidoEnabled());
				btGradeItemPedido.setEnabled(isBtGradeItemPedidoEnabled());
			}
			cbItemGrade1.setEditable(isEnabled() && (!isEditing() || isModoGrade()) && (cbItemGrade1.size() > 0));
			cbItemGrade2.setEditable(isEnabled() && !isEditing() && (cbItemGrade2.size() > 0));
		}
		if (LavenderePdaConfig.usaControleEspecialEdicaoUnidades && getItemPedido().getProduto() != null) {
			boolean editaCampoQuantidade = getItemPedido().getProduto().nuMultiploEspecial > 1;
			lbQtItemFaturamento.setVisible(!editaCampoQuantidade);
			edQtItemFaturamento.setVisible(!editaCampoQuantidade);
			edQtItemFisico.setEditable(isEnabled() && editaCampoQuantidade && !LavenderePdaConfig.usaInsercaoQuantidadeDesejadaPedido);
			flFocoQtFaturamento = isEnabled() && !editaCampoQuantidade;
		}
		if (LavenderePdaConfig.usaOportunidadeVenda && inVendaUnitariaMode) {
			boolean isItemOportunidadeInPedidoNormal = getItemPedido() != null && (getItemPedido().pedido != null && !getItemPedido().pedido.isOportunidade()) && getItemPedido().isItemPermiteOportunidade();
			bgOportunidade.setEnabled(isItemOportunidadeInPedidoNormal);
			btIconeOportunidade.setVisible(isItemOportunidadeInPedidoNormal);
		}
		if (LavenderePdaConfig.usaGerenciaDeCreditoDesconto) {
			btIconeProdCreditoDesc.setVisible(isBtIconeProdCredDescVisible());
		}
		if (LavenderePdaConfig.isUsaTrocaRecolherComoDescontoPagamentoPedido() && inVendaUnitariaMode) {
			boolean isEnabled = !pedido.isConsignacaoObrigatoria();
			lbItemTroca.setVisible(isEnabled);
			bgItemTroca.setVisible(isEnabled);
			bgItemTroca.setEnabled(isEnabled && pedido.isPedidoVenda() && !isEditing());
		}
		if (btIconeVerba != null) {
			btIconeVerba.setVisible(!pedido.isIgnoraControleVerba() && pedido.isPedidoAberto());
		}
		if (pedido.isStatusPedidoNaoOcultaValoresComissao() && btIconeComissaoItemPedido != null) {
			btIconeComissaoItemPedido.setVisible(true);
		}
		if (btIconeFracaoFornecedor != null) {
			btIconeFracaoFornecedor.setVisible(LavenderePdaConfig.exibeFracaoEmbalagemFornecedorItemPedido && getItemPedido() != null && getItemPedido().getProduto() != null && getItemPedido().getProduto().nuFracaoFornecedor > 0);
		}
		if (LavenderePdaConfig.isPermiteBonificarProduto() || LavenderePdaConfig.usaBonificacaoPorGrupoBonificacao) {
			btIconeBonificacao.setVisible(isEditing());
		}
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() && isVerificaItemPedidoItemTabelaPreco() && !getItemPedido().getItemTabelaPreco().isFlBloqueiaDesc3() ||
		   (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2() && isVerificaItemPedidoItemTabelaPreco() && !getItemPedido().getItemTabelaPreco().isFlBloqueiaDesc3() && getItemPedido().getItemTabelaPreco().isFlBloqueiaDescPadrao() )) {
			edVlPctDesconto3.setEditable(isHabilitaEdicaoCampoPctDesconto3());
		}
		if (!pedido.isPermiteInserirMultiplosItensPorVezNoPedido() || fromProdutoPendenteGiroMultInsercao) {
			addIconsProduto();
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao()) {
			ItemPedido itemPedido = getItemPedido();
			itemPedido.pedido = pedido;
			boolean hasSolAutorizacaoItemPedido = itemPedido.solAutorizacaoItemPedidoCache.getHasSolAutorizacaoItemPedido(itemPedido, null);
			btIconeAutorizacao.setVisible(hasSolAutorizacaoItemPedido);
			if (LavenderePdaConfig.usaAgrupadorSimilaridadeProduto) {
				btIconeSimilar.setVisible(btIconeAutorizacao.isVisible());
			}
		}
		lbVlUnitGiroProduto.setVisible(LavenderePdaConfig.isUsaVlUnitGiroProduto() && getItemPedido().giroProduto != null);
		lvVlUnitGiroProduto.setVisible(LavenderePdaConfig.isUsaVlUnitGiroProduto() && getItemPedido().giroProduto != null);
	}

	private boolean isHabilitaEdicaoCampoPctDesconto3() throws SQLException {
		if (LavenderePdaConfig.isAplicaDescontosEmCascataNoItemPedidoRegra2()) {
			return pedido.isPedidoAberto() && edVlPctDesconto.getValueDouble() > LavenderePdaConfig.getVlPctHabilitaDesconto3() && isVerificaItemPedidoItemTabelaPreco() && !getItemPedido().getItemTabelaPreco().isFlBloqueiaDesc3() ||
				(isVerificaItemPedidoItemTabelaPreco() && !getItemPedido().getItemTabelaPreco().isFlBloqueiaDesc3() && getItemPedido().getItemTabelaPreco().isFlBloqueiaDescPadrao()) && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL);
		}
		return getItemPedido().getItemTabelaPreco() != null && ValueUtil.VALOR_SIM.equals(getItemPedido().getItemTabelaPreco().flUsaDesconto3) && !ClienteService.getInstance().isDescontoBloqueado(pedido.getCliente(),Cliente.DESCONTO_BLOQUEADO_MANUAL);
	}

	private boolean isVerificaItemPedidoItemTabelaPreco() throws SQLException {
		return getItemPedido() != null && getItemPedido().getItemTabelaPreco() != null;
	}

	private boolean isEdQtItemEditable() throws SQLException {
		return isEnabled() && isEdQtItemEditableOnModoGrade();
	}

	private boolean isEdQtItemEditableOnModoGrade() throws SQLException {
		if (!isModoGrade()) return true;

		boolean editable = cbItemGrade1.size() == 0;
		editable |= !produtoComGradeNivel2;
		editable |= LavenderePdaConfig.isGradeProdutoModoLista() && (!isEditing() || !ItemPedidoGradeService.getInstance().hasItemPedidoGrade(getItemPedido().cdProduto, getItemPedido().nuPedido, getItemPedido().flOrigemPedido, getItemPedido().flTipoItemPedido));
		editable |= (LavenderePdaConfig.usaGradeProduto5() && !ConfigInternoService.getInstance().isListaModoAgrupadorGrade()) || (LavenderePdaConfig.usaGradeProduto5() && fromListItemPedidoForm);
		editable |= LavenderePdaConfig.usaGradeProduto5() && getItemPedido().getProduto() != null && !getItemPedido().getProduto().isProdutoAgrupadorGrade() || LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade();
		return isEnabled() && editable;
	}

	private boolean isBtGradeItemPedidoEnabled() throws SQLException {
		if (cbItemGrade1.size() > 0 && produtoComGradeNivel2 && LavenderePdaConfig.getConfigGradeProduto() != 3 && ((isModoGrade() && isFiltrandoAgrupadorGrade()) || !LavenderePdaConfig.usaGradeProduto5())) {
			return true;
		} else if (LavenderePdaConfig.isGradeProdutoModoLista() && cbItemGrade2.getValue() != null && produtoComGradeNivel3) {
			if (edQtItemFisico.getValueDouble() == 0 || ValueUtil.isNotEmpty(getItemPedido().itemPedidoGradeList) || ItemPedidoGradeService.getInstance().hasItemPedidoGradeNivel3(getItemPedido().cdProduto, getItemPedido().nuPedido, getItemPedido().flOrigemPedido, getItemPedido().flTipoItemPedido)) {
				return true;
			}
		}
		return false;
	}

	private boolean isBtIconeProdCredDescVisible() throws SQLException {
		return getItemPedido() != null && pedido.isPedidoAberto() && isEditing() && getItemPedido().isItemVendaNormal() && !DescPromocionalService.getInstance().isItemPedidoPossuiDescPromocional(getItemPedido()) && ProdutoCreditoDescService.getInstance().getProdutoCreditoDescontoParaAplicacao(getItemPedido().cdEmpresa, getItemPedido().cdProduto,ProdutoCreditoDesc.FLTIPOCADASTRO_DESCONTO) != null;
	}

	public void detalhesClick() throws SQLException {
		if (pedido.isPermiteInserirMultiplosItensPorVezNoPedido() && !inVendaUnitariaMode) {
			getItemPedido().setProduto((Produto) ProdutoService.getInstance().findByRowKey(listContainer.getSelectedId()));
		}

		if (getItemPedido().getProduto() != null) {
			if (cadProdutoForm == null) {
				cadProdutoForm = new CadProdutoDynForm();
				cadProdutoForm.setEnabled(false);
			}
			cadProdutoForm.pedido = pedido;
			cadProdutoForm.itemTabelaPreco = getItemPedido().getItemTabelaPreco();
			if (cadProdutoForm.itemTabelaPreco == null) {
				cadProdutoForm.itemTabelaPreco = new ItemTabelaPreco();
			}
			Produto produto = ProdutoService.getInstance().getProdutoDyn(getItemPedido().cdEmpresa,SessionLavenderePda.usuarioPdaRep.cdRepresentante, getItemPedido().getProduto().cdProduto);
			if (!ProdutoService.getInstance().isExistsProduto(getItemPedido())) {
				UiUtil.showErrorMessage(Messages.PRODUTO_ERRO_NAO_DISPONIVEL);
				return;
			}
			if (LavenderePdaConfig.isConfigGradeProduto()) {
				ItemGrade itemGrade = (ItemGrade) cbItemGrade1.getSelectedItem();
				if (itemGrade != null) {
					produto.cdTipoItemGrade1Temp = itemGrade.cdTipoItemGrade;
					produto.cdItemGrade1Temp = itemGrade.cdItemGrade;
				}
			}
			cadProdutoForm.edit(produto);
			show(cadProdutoForm);
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	protected void loadDadosGradeProduto(ItemPedido itemPedido) throws SQLException {
		lbItemGrade1.setValue("");
		cbItemGrade1.removeAll();
		cbItemGrade2.removeAll();
		lbItemGrade2.setValue("");
		produtoComGradeNivel2 = false;
		produtoComGradeNivel3 = false;
		String cdTabelaPreco = ValueUtil.VALOR_ZERO;
		if (itemPedido.getTabelaPreco() != null) {
			cdTabelaPreco = itemPedido.getCdTabelaPreco();
		}
		Vector produtoGradeList = null;
		if (LavenderePdaConfig.usaGradeProduto5() && itemPedido.getProduto().isProdutoAgrupadorGrade()) {
			produtoGradeList = ProdutoGradeService.getInstance().findAllProdutoGradeAgrupadorGrade(LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade() ? itemPedido.cdProduto : null, cdTabelaPreco, itemPedido.getProduto().getDsAgrupadorGrade(), pedido.itemPedidoList.contains(itemPedido));
		} else {
			produtoGradeList = ProdutoGradeService.getInstance().findProdutoGradeList(itemPedido.cdProduto,cdTabelaPreco);
		}
		// --
		filtraPorItemTabPrecoEEstoque(itemPedido, produtoGradeList);
		String cdTipoItemGrade = "";
		int size = produtoGradeList.size();
		if (size > 0) {
			ProdutoGrade produtoGrade = (ProdutoGrade) produtoGradeList.items[0];
			cdTipoItemGrade = produtoGrade.cdTipoItemGrade1;
			lbItemGrade1.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(produtoGrade.cdTipoItemGrade1));
			cbItemGrade1.popupTitle = lbItemGrade1.getValue();
			Vector itensGrade1List = new Vector();
			for (int i = 0; i < size; i++) {
				ProdutoGrade produtGrade = (ProdutoGrade) produtoGradeList.items[i];
				ItemGrade itemGrade1 = ItemGradeService.getInstance().getItemGrade(produtGrade.cdTipoItemGrade1,produtGrade.cdItemGrade1);
				if (itemGrade1 != null) {
					if (itensGrade1List.indexOf(itemGrade1) == -1) {
						itensGrade1List.addElement(itemGrade1);
					}
				}
				if (ValueUtil.isNotEmpty(produtGrade.cdItemGrade2) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtGrade.cdItemGrade2)) {
					produtoComGradeNivel2 = true;
				}
				if (ValueUtil.isNotEmpty(produtGrade.cdItemGrade3) && !ProdutoGrade.CD_ITEM_GRADE_PADRAO.equals(produtGrade.cdItemGrade3)) {
					produtoComGradeNivel3 = true;
				}
			}
			if (LavenderePdaConfig.usaOrdenacaoNuSequenciaGradeProduto) {
				SortUtil.qsortInt(itensGrade1List.items, 0, itensGrade1List.size() - 1, true);
			} else {
				itensGrade1List.qsort();
			}
			cbItemGrade1.add(itensGrade1List);
			// --
			cbItemGrade1.setValue(cdTipoItemGrade, itemPedido.cdItemGrade1);
			if (ValueUtil.isEmpty(cbItemGrade1.getValue())) {
				cbItemGrade1.setSelectedIndex(0);
			}
			if (cbItemGrade1.getValue() != null) {
				itemPedido.cdItemGrade1 = cbItemGrade1.getValue();
				if (LavenderePdaConfig.isGradeProdutoModoLista()) {
					loadComboItemGrade2(itemPedido.cdProduto, produtoGrade.cdTipoItemGrade2, itemPedido.cdItemGrade2);
				}
			}
			lastItemGradeSelected = (ItemGrade) cbItemGrade1.getSelectedItem();
		}
	}

	private void loadComboItemGrade2(String cdProduto, String cdTipoItemGrade2, String cdItemGrade2) throws SQLException {
		if (cdTipoItemGrade2 != null) {
			lbItemGrade2.setValue(TipoItemGradeService.getInstance().getDsTipoItemGrade(cdTipoItemGrade2));
			cbItemGrade2.popupTitle = lbItemGrade2.getValue();
		}
		Vector itemGradeList = ItemGradeService.getInstance().findAllItemGradeNivel2(cbItemGrade1.getValue(), cdProduto);
		itemGradeList.qsort();
		cbItemGrade2.add(itemGradeList);
		cbItemGrade2.setValue(cdTipoItemGrade2, cdItemGrade2);
		if (ValueUtil.isEmpty(cbItemGrade2.getValue()) && !isEditing()) {
			cbItemGrade2.setSelectedIndex(0);
		}
		lastItemGrade2Selected = (ItemGrade) cbItemGrade2.getSelectedItem();
	}

	private void filtraPorItemTabPrecoEEstoque(ItemPedido itemPedido, Vector produtoGradeList) throws SQLException {
		ItemPedido itemPedidoClone = (ItemPedido) itemPedido.clone();
		if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisParaVenda() && ValueUtil.isNotEmpty(produtoGradeList)) {
			boolean setouNovoItemGrade1 = false;
			for (Object obj : produtoGradeList.toObjectArray()) {
				ProdutoGrade produtoGrade = (ProdutoGrade) obj;
				itemPedidoClone.cdItemGrade1 = produtoGrade.cdItemGrade1;
				if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisPorItemTabPreco() && itemPedidoClone.getItemTabelaPreco().cdTabelaPreco == null) {
					produtoGradeList.removeElement(obj);
				} else if (LavenderePdaConfig.isUsaLimpezaGradesNaoDisponiveisPorEstoque() && EstoqueService.getInstance().getQtEstoqueErp(itemPedidoClone.cdProduto,itemPedidoClone.cdItemGrade1, ProdutoGrade.CD_ITEM_GRADE_PADRAO,ProdutoGrade.CD_ITEM_GRADE_PADRAO, TabelaPrecoService.getInstance().getTabelaPreco(itemPedidoClone.cdTabelaPreco).cdLocalEstoque) <= 0) {
					produtoGradeList.removeElement(obj);
				} else if (!setouNovoItemGrade1 && !isEditing()) {
					itemPedido.cdItemGrade1 = produtoGrade.cdItemGrade1;
					setouNovoItemGrade1 = true;
				}
			}
		}
	}

	private void cbItemGrade1Click() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (LavenderePdaConfig.usaGradeProduto5() && isModoGrade()) {
			itemPedido.cdItemGrade1 = cbItemGrade1.getValue();
			changeItemTabelaPreco();
			lastItemGradeSelected = (ItemGrade) cbItemGrade1.getSelectedItem();
			if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
				setDomainAfterItemPedidoPorPrecoGradeChange(getItemPedido(), getItemPedido().itemPedidoPorGradePrecoList);
				calcularClick();
			}
			if (isShowFotoProduto()) {
				getFotoByItemPedido(itemPedido);
			}
			return;
		}
		if (itemPedido.itemPedidoGradeList.size() > 0 || itemPedido.itemPedidoPorGradePrecoList.size() > 0) {
			if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ALTERACAO_ITEM_GRADE1,new String[] { lbItemGrade1.getValue() })) == 0) {
				cbItemGrade1.setSelectedItem(lastItemGradeSelected);
				return;
			}
		}
		if (itemPedido.getProduto() != null) {
			getItemPedidoService().clearDadosItemPedido(itemPedido);
			itemPedido.cdItemGrade1 = cbItemGrade1.getValue();
			changeItemTabelaPreco();
			controlEstoque(getItemPedido());
			lastItemGradeSelected = (ItemGrade) cbItemGrade1.getSelectedItem();
		}
		if (LavenderePdaConfig.isGradeProdutoModoLista()) {
			btGradeItemPedido.setEnabled(false);
			cbItemGrade2.removeAll();
			loadComboItemGrade2(itemPedido.cdProduto, null, itemPedido.cdItemGrade2);
		}
		if (LavenderePdaConfig.usaGradeProduto4()) {
			itemPedido.itemPedidoPorGradePrecoList.removeAllElements();
		}
	}

	private void cbItemGrade2Click() throws SQLException {
		if (getItemPedido().itemPedidoGradeList.size() > 0) {
			if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ALTERACAO_ITEM_GRADE1,new String[] { lbItemGrade2.getValue() })) == 0) {
				cbItemGrade2.setSelectedItem(lastItemGrade2Selected);
				return;
			}
		}
		if (getItemPedido().getProduto() != null) {
			getItemPedidoService().clearDadosItemPedido(getItemPedido());
			getItemPedido().cdItemGrade1 = cbItemGrade1.getValue();
			getItemPedido().cdItemGrade2 = cbItemGrade2.getValue();
			changeItemTabelaPreco();
			controlEstoque(getItemPedido());
			lastItemGrade2Selected = (ItemGrade) cbItemGrade2.getSelectedItem();
		}
		btGradeItemPedido.setEnabled(cbItemGrade2.getValue() != null && produtoComGradeNivel3);
		edQtItemFisico.setEditable(true);
		edQtItemFaturamento.setEditable(true);
	}

	public void controlEstoque(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaControleEstoquePorLoteProduto()) {
			lvEstoque.setForeColor(ColorUtil.componentsForeColor);
			tipEstoque.setText("");
			lvEstoque.setValue(LoteProdutoService.getInstance().getQtEstoquePorLoteDisponivel(itemPedido.cdEmpresa,itemPedido.cdProduto, itemPedido.cdLoteProduto));
			if (lvEstoque.getDoubleValue() <= 0) {
				lvEstoque.setForeColor(ColorUtil.softRed);
				tipEstoque.setText(LavenderePdaConfig.bloquearVendaProdutoSemEstoque ? Messages.ESTOQUE_MSG_BLOQUEADO : Messages.ESTOQUE_MSG_SEMESTOQUE);
			}
			return;
		}
		if (LavenderePdaConfig.isUsaGradeProduto1A4()) {
			itemPedido.estoque = new Estoque();
			if (LavenderePdaConfig.usaGradeProduto2()) {
				itemPedido.estoque.qtEstoque = EstoqueService.getInstance().getSumEstoqueGradeProduto(itemPedido.cdProduto, itemPedido.getCdLocalEstoque());
			} else {
				itemPedido.estoque.qtEstoque = EstoqueService.getInstance().getSumEstoqueGradeProduto(itemPedido.cdProduto, itemPedido.cdItemGrade1, itemPedido.getCdLocalEstoque(),itemPedido.cdItemGrade2);
			}
			lvEstoque.setValue(itemPedido.estoque.qtEstoque);
		} else if (itemPedido.cdProduto != null) {
			itemPedido.estoque = EstoqueService.getInstance().getEstoque(itemPedido.cdProduto,itemPedido.getCdLocalEstoque(), Estoque.FLORIGEMESTOQUE_ERP, itemPedido.pedido.flModoEstoque);
			if (LavenderePdaConfig.usaControleEstoquePrevistoParcial()) {
				EstoqueService.getInstance().setEstoqueItemComParcialPrevisto(itemPedido.estoque);
			}
			if (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && itemPedido.getProduto().isProdutoAgrupadorGrade() && !fromListItemPedidoForm && !LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
				itemPedido.estoque = EstoqueService.getInstance().getEstoqueAgrupadorGrade(itemPedido.getCdLocalEstoque(), Estoque.FLORIGEMESTOQUE_ERP, itemPedido.getProduto().getDsAgrupadorGrade(), itemPedido);
			}
		}
		double qtEstoque = itemPedido.estoque != null ? itemPedido.estoque.qtEstoque : 0;
		if (itemPedido.estoque != null && itemPedido.estoque.flErroEstoqueOnline) {
			lvEstoque.setText("");
		} else {
			if (LavenderePdaConfig.usaControleEstoquePorRemessa) {
				qtEstoque = RemessaEstoqueService.getInstance().getEstoqueDisponivelProduto(itemPedido);
			} else {
				qtEstoque = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(),cbUnidadeAlternativa.getProdutoUnidade(), qtEstoque);
			}
			lvEstoque.setValue(qtEstoque);
		}
		lvEstoque.setForeColor(ColorUtil.componentsForeColor);
		tipEstoque.setText("");
		if (LavenderePdaConfig.mostraEstoquePrevisto && (itemPedido.estoque != null && itemPedido.estoque.qtEstoquePrevisto > 0)) {
			defineCorEstoquePrevistoNoLvEstoque();
			tipEstoque.setText(Messages.ESTOQUE_MSG_ESTOQUEPREVISAO);
		} else if (itemPedido.getProduto() != null && (LavenderePdaConfig.bloquearVendaProdutoSemEstoque || isAvisaVendaProdutoSemEstoqueByItemPedido(itemPedido.getProduto().isIgnoraValidacao()))) {
			if (qtEstoque <= 0) {
				lvEstoque.setForeColor(ColorUtil.softRed);
				tipEstoque.setText(LavenderePdaConfig.bloquearVendaProdutoSemEstoque ? Messages.ESTOQUE_MSG_BLOQUEADO : Messages.ESTOQUE_MSG_SEMESTOQUE);
			}
		}
	}

	private void loadLabelGruposProdutos(StringBuffer sb) {
		if (!ValueUtil.isEmpty(LavenderePdaConfig.labelGruposProduto) && !ValueUtil.VALOR_NAO.equals(LavenderePdaConfig.labelGruposProduto)) {
			String[] temp = StringUtil.split(LavenderePdaConfig.labelGruposProduto, ';');
			sb.setLength(0);
			if (temp.length > 0 && lbGrupoProduto != null) {
				if (!LavenderePdaConfig.ocultaGrupoProduto1) {
					sb.append(temp[0]);
					cbGrupoProduto1.popupTitle = temp[0];
				}
				if (temp.length > 1 && LavenderePdaConfig.usaFiltroGrupoProduto >= 2) {
					sb.append(" / ").append(temp[1]);
					cbGrupoProduto2.popupTitle = temp[1];
					if (temp.length > 2 && LavenderePdaConfig.usaFiltroGrupoProduto >= 3) {
						sb.append(" / ").append(temp[2]);
						cbGrupoProduto3.popupTitle = temp[2];
						if (temp.length > 3 && LavenderePdaConfig.usaFiltroGrupoProduto >= 4) {
							sb.append(" / ").append(temp[3]);
							cbGrupoProduto4.popupTitle = temp[3];
						}
					}
				}
			}
		}
	}

	protected void processaLeitura(ScanEvent event) throws SQLException {
		dsFiltro = event.data;
		String errorCode = "NR";
		if (!errorCode.equals(dsFiltro)) {
			super.processaLeitura(event);
			if (!inVendaUnitariaMode) {
				realizaFiltroLeituraCdBarras();
			} else {
				findProdutoByLeituraCdBarras();
			}
			if (LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras) {
				calcularClick();
			}
		}
	}

	protected boolean realizaFiltroLeituraCdBarras() throws SQLException {
		ProdutoUnidade produtoUnidade = null;
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			produtoUnidade = ProdutoUnidadeService.getInstance().getProdutoUnidadeByCdBarras(getItemPedido(), dsFiltro);
			if (produtoUnidade != null) {
				dsFiltro = produtoUnidade.cdProduto;
			}
		}
		edFiltro.setText(dsFiltro);
		if (filtrarClick()) {
			if (pedido.isGondola()) {
				edQtItemGondola.requestFocus();
				edQtItemFisico.setValue(1);
				getItemPedido().setQtItemFisico(1);
				calcularClick();
				refreshDomainToScreen(getItemPedido());
			} else if (LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras) {
				edQtItemFisico.setValue(1);
				getItemPedido().setQtItemFisico(1);
			}
			if (LavenderePdaConfig.usaUnidadeAlternativa && produtoUnidade != null) {
				cbUnidadeAlternativa.setValue(produtoUnidade.cdUnidade);
				getItemPedido().cdUnidade = produtoUnidade.cdUnidade;
				changeUnidadeAlternativa(produtoUnidade.cdUnidade);
				selectUnidadeNaGrid(produtoUnidade.cdUnidade);
			}
			edFiltro.setText(""); // Pra n?o ficar com o valor do codigo de barras no edit
			if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
				EstoqueService.getInstance().validateEstoque(pedido, getItemPedido(), true);
			}
			return LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras;
		}
		edFiltro.setText(""); // Pra n?o ficar com o valor do codigo de barras no edit
		return false;
	}

	protected void realizaLeituraCamera() throws SQLException {
		StringBuffer dsInfo = new StringBuffer("");
		if (LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras && inVendaUnitariaMode) {
			dsInfo.append(lbQtItemFisico.getText());
			dsInfo.append(" ");
			dsInfo.append(edQtItemFisico.getValueDouble());
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				dsInfo.append(" - ");
				dsInfo.append(lbUnidadeAlternativa.getText());
				dsInfo.append(" ");
				dsInfo.append(cbUnidadeAlternativa.getProdutoUnidadeSelected());
			}
		}
		dsFiltro = ScannerCameraUtil.realizaLeitura(ScannerCameraUtil.MODO_SOMENTE_CODIGO_BARRAS, dsInfo.toString());
		if (ValueUtil.isNotEmpty(dsFiltro)) {
			if (!inVendaUnitariaMode) {
				if (realizaFiltroLeituraCdBarras()) {
					realizaLeituraCamera();
				}
			} else {
				if (findProdutoByLeituraCdBarras()) {
					realizaLeituraCamera();
					return;
				}
			}
		}
		if (ValueUtil.isNotEmpty(getItemPedido().cdProduto)) {
			calcularClick();
		}
		if (pedido.isGondola()) {
			edQtItemFisico.setText(ValueUtil.VALOR_NI);
		}
	}

	public void atualizaProdutoNaGrid(ProdutoBase produto, boolean seleciona) throws SQLException {
		atualizaProdutoNaGrid(produto, null, seleciona, false);
	}

	public void atualizaProdutoNaGrid(ProdutoBase produto, ItemPedido itemPedido, boolean seleciona, boolean atualizaListContainerItem) throws SQLException {
		GridListContainer listContainerToUpdate;
		if (fromListItemPedidoForm) {
			listContainerToUpdate = CadItemPedidoForm.getInstance(cadPedidoForm, pedido).listContainer;
		} else {
			listContainerToUpdate = listContainer;
		}
		int size = listContainerToUpdate.size();
		if (produto == null) {
			return;
		}
		for (int i = 0; i < size; i++) {
			if (isProdutoSelecionado(produto, i, listContainerToUpdate)) {
				if (LavenderePdaConfig.isMostraColunaPrecoNaListaDeProdutosDentroPedido()) {
					produto.vlProduto = ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, cbTabelaPreco.getValue(), LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false);
				}
				if (LavenderePdaConfig.usaFiltroComissao) {
					loadPctComissaoProduto(produto);
				}
				if (LavenderePdaConfig.destacaProdutosJaIncluidosAoPedido) {
					if (!isAddingFromArray || fromGridClick) {
						setPropertiesInRowList(listContainerToUpdate.getContainer(i), produto);
					} else if (itemPedido != null) {
						itemPedido.itemChanged = true;
					}
				}
				if ((LavenderePdaConfig.isPermiteAcessoTelaPadraoMultiplaInsercao() || fromProdutoPendenteGiroMultInsercao) && itemPedido != null) {
					Item item = (Item) listContainerToUpdate.getContainer(i);
					ItemContainer itemCont = (ItemContainer) item.rightControl;
					if (itemCont != null) {
						itemCont.setValuesOnList(itemPedido);
						if (itemPedido.removeFromGrid) {
							itemCont.itemPedido = null;
						} else {
							itemCont.itemPedido = (ItemPedido) itemPedido.clone();
						}
						if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
							itemCont.cbUnidadeAlternativa.setValue(itemPedido.cdUnidade, itemPedido.cdProduto,itemPedido.cdItemGrade1);
						}
					}
					fromListDetalheInsecaoMult = false;
				}
				if (LavenderePdaConfig.atualizarEstoqueInterno && !isInsereMultiplosSemNegociacao() && !atualizaListContainerItem) {
					produto.estoque = null;
					listContainerToUpdate.setSelectedIndex(i);
					updateCurrentRecord(produto);
					listContainerToUpdate.setSelectedIndex(-1);
				}
				if (atualizaListContainerItem) {
					BaseListContainer.Item item = (BaseListContainer.Item) listContainerToUpdate.getContainer(i);
					if (item != null) {
						ProdutoBase produtoBase = (ProdutoBase) item.domain;
						produtoBase.estoque = produto.estoque;
						item.setItens(getItemProdutoToGrid(produto));
					}
				}
				if (seleciona && !isInsereMultiplosSemNegociacao()) {
					listContainerToUpdate.setSelectedIndex(i);
				}
				break;
			}
		}
	}

	private boolean isProdutoSelecionado(ProdutoBase produto, int i, GridListContainer listContainerToUpdate) {
		if (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && produto.isProdutoAgrupadorGrade()) {
			Item item = (Item) listContainerToUpdate.getContainer(i);
			return ValueUtil.valueEquals(produto.getDsAgrupadorGrade(), ((ProdutoBase)item.domain).getDsAgrupadorGrade());
		}
		return produto.getRowKey().equals(listContainerToUpdate.getId(i));
	}

	protected void updateCurrentRecordInList(BaseDomain domain) throws SQLException {
		if (LavenderePdaConfig.usaOportunidadeVenda || LavenderePdaConfig.usaGradeProduto4()) {
			list();
			return;
		}
		super.updateCurrentRecordInList(domain);
		if (instance != null && !this.equals(instance)) {
			if (instance.getBaseCrudListForm() == null) return;
			if (((ListItemPedidoForm) instance.getBaseCrudListForm()).isInstanceExibited()) {
				instance.listInstance();
			}
		}
	}

	public void updateItem(BaseDomain newDomain, int index) {
		if (listContainer != null) {
			BaseListContainer.Item item = (Item) listContainer.getContainer(index);
			if (item != null) {
				updatePropertiesInRowItem(item, newDomain);
			}
		}
	}

	public void updateCurrentRecord(BaseDomain domain) throws SQLException {
		BaseListContainer.Item item = (BaseListContainer.Item) CadItemPedidoForm.getInstance(cadPedidoForm, pedido).listContainer.getSelectedItem();
		if (item != null) {
			item.setItens(getItemProdutoToGrid(domain));
			setPropertiesInRowList(item, domain);
		}
		listContainer.updateTotalizerRight();
	}

	public static void validateItemPedidoAplicaCreditoDescUI(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		ItemPedidoService.getInstance().validaQtMaxVendaProduto(itemPedido);
		PedidoService.getInstance().setPedidoComPagamentoAVista(pedido);
		validateEstoqueUI(itemPedido, pedido, editing);
		validateLimiteCreditoUI(itemPedido, pedido);
		validateConversaoUnidadeUI(itemPedido, pedido);
		validateQtMinimaItensRestricaoUnidadeUI(itemPedido, pedido);
		validateQtdMinimaProdutosPromocionaisERestritosUI(itemPedido, pedido, editing);
		validateMesmoProdutoNoPedidoUI(itemPedido, pedido, editing);
		validateMesmoProdutoOutroLoteNoPedidoUI(itemPedido, pedido, editing);
		validateMesmoProdutoOutraUnidadeNoPedidoUI(itemPedido, pedido, editing);
		ItemPedidoService.getInstance().validaItensPorLimiteCredito(pedido, itemPedido, false);
	}

	public static void validateItemPedidoUI(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		validatePedidoCritico(itemPedido, pedido);
		ItemPedidoService.getInstance().validaQtMaxVendaProduto(itemPedido);
		PedidoService.getInstance().setPedidoComPagamentoAVista(pedido);
		if (!pedido.isReplicandoPedido) {
			double vlSaldo = validateVerbaUI(itemPedido);
			validateVlMinimoVerbaUI(itemPedido, pedido, vlSaldo);
		}
		validateEstoqueUI(itemPedido, pedido, editing);
		validateLimiteCreditoUI(itemPedido, pedido);
		validateQtdItemOriginalNaBonificacaoUI(itemPedido);
		validateConversaoUnidadeUI(itemPedido, pedido);
		validateDescontoAcrescimoUI(itemPedido);
		validatePrecoBaseItemBonificadoUI(itemPedido, pedido);
		validatePrecoMinimoTabelaPrecoUI(itemPedido);
		validateQtMinimaItensRestricaoUnidadeUI(itemPedido, pedido);
		validateQtdMinimaProdutosPromocionaisERestritosUI(itemPedido, pedido, editing);
		validateMesmoProdutoNoPedidoUI(itemPedido, pedido, editing);
		validateMesmoProdutoOutroLoteNoPedidoUI(itemPedido, pedido, editing);
		validateMesmoProdutoOutraUnidadeNoPedidoUI(itemPedido, pedido, editing);
		validateProdutoInPlataFormaVendaProduto(itemPedido, pedido);
		ItemPedidoService.getInstance().validateNuOrdemCompraAndNuSequencialOrdemCompra(itemPedido, pedido, editing);
		ItemPedidoService.getInstance().validaItensPorLimiteCredito(pedido, itemPedido, false);
	}

	public static void validateProdutoInPlataFormaVendaProduto(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && PlataformaVendaProdutoService.getInstance().isNotExistsProdutoInPlataformaVendaProduto(pedido, itemPedido)) {
			throw new ValidateProdutoInPlataFormaVendaProdutoException(Messages.PRODUTO_SEM_PLATAFORMA_ERRO);
		}
	}

	private static void validatePedidoCritico(ItemPedido itemPedido, Pedido pedido) {
		if (!LavenderePdaConfig.usaPedidoProdutoCritico) return;
		if (LavenderePdaConfig.permiteApenasUmItemPedidoProdutoCritico && pedido.isPedidoCritico() && pedido.itemPedidoList.size() >= 1) {
			ItemPedido itemLista = (ItemPedido) pedido.itemPedidoList.items[0];
			if (ValueUtil.valueEquals(itemPedido.rowKey, itemLista.rowKey)) return;
			UiUtil.showErrorMessage(Messages.PEDIDOPRODUTOCRITICO_ERRO_QT_ITENS);
			throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
		}
	}

	private static double validateVerbaUI(ItemPedido itemPedido) throws SQLException {
		double vlSaldo = ValueUtil.round(VerbaService.getInstance().getVlSaldo(itemPedido));
		double vlTolerancia = VerbaService.getInstance().getVlTolerancia(itemPedido.pedido);
		if (vlSaldo < 0 && vlSaldo * -1 < vlTolerancia && vlTolerancia > 0) {
			if (!UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_NEGATIVA_ABAIXO_TOLERANCIA_CONFIRMA, vlSaldo))) {
				throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
		}
		return vlSaldo;
	}

	private static void validateEstoqueUI(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		try {
			EstoqueService.getInstance().validateEstoque(pedido, itemPedido, editing);
		} catch (ValidationException ve) {
			if (ProdutoService.getInstance().getProduto(itemPedido.cdProduto).isPermiteEstoqueNegativo()) {
				if (itemPedido.itemPedidoGradeList.size() > 0) {
					if (!itemPedido.isIgnoraMensagemEstoqueNegativo && !itemPedido.ignoraSegundaValidacaoGradeEstoqueNegativo) {
						if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_GRADE_LIBERADO)) {
							throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
						}
					}
				} else {
					if (!itemPedido.isIgnoraMensagemEstoqueNegativo && !UiUtil.showWarnConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_LIBERADO)) {
						throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
					}
				}
			} else if (ve instanceof EstoqueException) {
				if (!liberaComSenhaVendaProdutoSemEstoque(pedido, itemPedido, ValueUtil.getDoubleValue(StringUtil.getStringValue(((EstoqueException) ve).params[0])))) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			} else if (LavenderePdaConfig.usaRegistroProdutoFaltante || LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto()) {
				String[] values = ve.getMessage().split(" ");
				String qtEstoqueExtrapolado = LavenderePdaConfig.bloqueiaFechamentoPedidoProdutoSemEstoque ?  values[2] :  values[7];
				double qtEstoque = itemPedido.getQtItemFisico() - ValueUtil.getDoubleValueSeparador(qtEstoqueExtrapolado);
				double estoqueErp = EstoqueService.getInstance().getQtEstoqueErp(itemPedido.cdProduto);
				if (qtEstoque == 0d) {
					ve = null;
				}

				if (itemPedido.fromProdutoFaltaWindow || !itemPedido.fromProdutoFaltaMsg && !LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto() && estoqueErp > itemPedido.nuConversaoUnidade) {
					itemPedido.fromProdutoFaltaWindow = false;
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}

				if (!LavenderePdaConfig.bloquearVendaProdutoSemEstoque && !itemPedido.fromProdutoFaltaMsg && !itemPedido.fromProdutoFaltaWindow && (estoqueErp <= 0 || estoqueErp < itemPedido.nuConversaoUnidade)) {
					if (!UiUtil.showWarnConfirmYesNoMessage(Messages.ESTOQUE_MSG_SEMESTOQUE_LIBERADO)) {
						throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
					}
				}

			} else if (LavenderePdaConfig.usaControleEstoqueProdutoRateioProducao || LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
				throw ve;

			} else if (LavenderePdaConfig.isAvisaVendaProdutoSemEstoque() && !itemPedido.getProduto().isIgnoraValidacao()) {
				if (!pedido.inserindoFromSugestaoPedido && !itemPedido.isItemPedidoSugestaoGiro && TipoPedidoService.getInstance().validaQuantidadeItem(pedido.getTipoPedido())) {
					if (UiUtil.showConfirmYesCancelMessage(itemPedido.getDsProduto() + Messages.NOME_PRODUTODESEJADO_ERRO_ESTOQUE + " Deseja continuar?") == 0) {
						ItemPedidoService.getInstance().clearDadosItemPedido(itemPedido);
						throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
					}
				}
			}

			if (LavenderePdaConfig.isAvisaVendaProdutoSemEstoqueComDetalhes() && !itemPedido.getProduto().isIgnoraValidacao()) {
				double qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
				EstoqueService.getInstance().apresentaEstoquePrevisto(itemPedido, qtEstoque, pedido.onReplicacao, pedido.isPedidoMultiplaInsercao, pedido.itemPedidoInseridosAdvertenciaList);
			}
		}
	}

	private static void validateLimiteCreditoUI(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		try {
			FichaFinanceiraService.getInstance().validateLimCred(pedido, itemPedido);
		} catch (ValidationException ve) {
			if (LavenderePdaConfig.bloquearLimiteCreditoCliente) {
				StringBuffer strBuffer = new StringBuffer();
				strBuffer = strBuffer.append(Messages.CLIENTE_FINANCEIRO_BLOQUEADO).append(ve.getMessage());
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
					strBuffer.append(" " + Messages.CLIENTE_FINANCEIRO_BLOQUEADO_PEDIDO_A_VISTA);
				}
				throw new ValidationException(strBuffer.toString());
			} else if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
				if (!pedido.isFlCreditoClienteLiberadoSenha() || PedidoService.getInstance().isSolicitavaNovamenteSenhaUsuarioAlcada(pedido.cdUsuarioLiberacaoLimCred)) {
					StringBuffer strBuffer = new StringBuffer();
					strBuffer = strBuffer.append(ve.getMessage().toString());
					if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()) {
						strBuffer.append(" " + Messages.CLIENTE_FINANCEIRO_BLOQUEADO_PEDIDO_A_VISTA);
						UiUtil.showErrorMessage(strBuffer.toString());
					}
					AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow(SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP);
					senhaForm.setMensagem(strBuffer.toString());
					senhaForm.setCdCliente(pedido.cdCliente);
					if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
						repaint();
						throw new ValidationException(strBuffer.toString());
					}
					repaint();
					pedido.flCreditoClienteLiberadoSenha = ValueUtil.VALOR_SIM;
					pedido.cdUsuarioLiberacaoLimCred = senhaForm.cdUsuarioLiberado;
				}
			} else if (LavenderePdaConfig.controlarLimiteCreditoCliente) {
				if (pedido.showMessageLimiteCredito) {
					StringBuffer strBuffer = new StringBuffer();
					if (!UiUtil.showConfirmYesNoMessage(strBuffer.append(ve.getMessage()).append(Messages.CLIENTE_LIMITE_CONTINUAR_ALERTA).toString())) {
						pedido.showMessageLimiteCredito = false;
					}
				}
			}
		}
	}

	private static void validateQtdItemOriginalNaBonificacaoUI(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaPercentualItemDeTrocaOuBonificacaoDoPedidoOriginal() && (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() || LavenderePdaConfig.isObrigaRelacionarPedidoTroca()) && LavenderePdaConfig.usaApenasItemPedidoOriginalNaBonificacaoTroca && (itemPedido.pedido.isPedidoBonificacao() || itemPedido.pedido.isPedidoTroca())) {
			try {
				ItemPedidoService.getInstance().validaQtItemFisicoMaxDoItemPedidoBonificacaoTroca(itemPedido.pedido, itemPedido);
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
	}

	private static void validateConversaoUnidadeUI(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		try {
			if (!itemPedido.isKitTipo3()) {
				ConversaoUnidadeService.getInstance().validadeConversaoUnidade(pedido, itemPedido);
			}
		} catch (ValidationException ve) {
			if (LavenderePdaConfig.isConsisteConversaoUnidades() || LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecial() ||
					(LavenderePdaConfig.consisteMultiploEmbalagem && ValueUtil.isEmpty(itemPedido.getProduto().flConsisteConversaoUnidade))) {
				throw ve;
			} else if (LavenderePdaConfig.avisaConversaoUnidades || (LavenderePdaConfig.consisteMultiploEmbalagem && !ValueUtil.VALOR_NAO.equals(itemPedido.getProduto().flConsisteConversaoUnidade))) {
				if (UiUtil.showConfirmYesCancelMessage(ve.getMessage() + " Deseja continuar?") == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
		}
	}

	private static void validateDescontoAcrescimoUI(ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.apenasAvisaDescontoAcrescimoMaximo || itemPedido.pedido.isPedidoCritico() || itemPedido.hasDescProgressivo() || itemPedido.auxiliarVariaveis.isItemComDescAcresMaxExtrapolado) return;
		try {
			ItemPedidoService.getInstance().validaDescontoMaximoPermitido(itemPedido);
			ItemPedidoService.getInstance().validaAcrescimoMaximoPermitido(itemPedido);
		} catch (ValidationException ve) {
			if (itemPedido.pedido.onReplicacao) {
				if (itemPedido.pedido.itemPedidoAConfirmarInsercaoList == null) {
					itemPedido.pedido.itemPedidoAConfirmarInsercaoList = new Vector(0);
				}
				itemPedido.pedido.itemPedidoAConfirmarInsercaoList.addElement(itemPedido);
				itemPedido.auxiliarVariaveis.isItemComDescAcresMaxExtrapolado = true;
				itemPedido.dsMotivoAdvertencia = ve.getMessage();
				throw ve;
			} else if (UiUtil.showConfirmYesCancelMessage(ve.getMessage() + " " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR) == 0) {
				throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
		}
	}

	private static void validatePrecoBaseItemBonificadoUI(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaPrecoBaseItemBonificado() && itemPedido.isItemBonificacao() && !pedido.isPedidoBonificacao()) {
			double vlBase = 0;
			if (LavenderePdaConfig.isUsaPrecoBaseItemPedidoPrecoBonificado()) {
				vlBase = itemPedido.vlBaseItemPedido;
			} else if (LavenderePdaConfig.isUsaPrecoBasePorRedutorCliente()) {
				vlBase = itemPedido.getItemTabelaPreco().getVlBaseFlex(pedido, itemPedido);
			} else {
				vlBase = itemPedido.getItemTabelaPreco().vlBase;
			}
			if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_BONIFICACAO_PRECO, StringUtil.getStringValueToInterface(vlBase))) == 0) {
				itemPedido.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
				throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
		}
	}

	private static void validatePrecoMinimoTabelaPrecoUI(ItemPedido itemPedido) {
		if (LavenderePdaConfig.usaListaColunaPorTabelaPreco && (LavenderePdaConfig.ignoraPrecoMinimoListaColunaPorTabelaPreco == 2)) {
			if (itemPedido.precoMinimoListaColunaExtrapolado) {
				if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_AVISA_VLMIN_LISTACOLUNA, itemPedido.vlItemPedido)) == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
		}
	}

	private static void validateQtMinimaItensRestricaoUnidadeUI(ItemPedido itemPedido, Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade > 0 && ((itemPedido.getItemTabelaPreco().qtMaxVenda > 0) && (!ValueUtil.VALOR_SIM.equals(pedido.flMaxVendaLiberadoSenha)))) {
			if (!(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedido, pedido))) {
				int qtMinItens = itemPedido.getItemTabelaPreco().qtMinItensNormais > 0 ? itemPedido.getItemTabelaPreco().qtMinItensNormais : LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade;
				int nuItensNormaisDoPedido = 0;
				int size = pedido.itemPedidoList.size();
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedidoAux = (ItemPedido) pedido.itemPedidoList.items[i];
					if (!(LavenderePdaConfig.isUsaKitProduto()
							&& ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedidoAux, pedido))
							&& (itemPedidoAux.getItemTabelaPreco().qtMaxVenda == 0)
							&& !itemPedidoAux.getItemTabelaPreco().isFlPromocao()) {
						nuItensNormaisDoPedido++;
					} else if (itemPedidoAux.getItemTabelaPreco().qtMaxVenda > 0d) {
						qtMinItens = qtMinItens + (itemPedidoAux.getItemTabelaPreco().qtMinItensNormais > 0 ? itemPedidoAux.getItemTabelaPreco().qtMinItensNormais : LavenderePdaConfig.qtMinimaItensParaPermitirItemRestricaoQuantidade);
					}
				}
				String[] lista = { StringUtil.getStringValueToInterface(qtMinItens), StringUtil.getStringValueToInterface(nuItensNormaisDoPedido) };
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_INFO_ADD_ITEMRESTRICAO, lista));
			}
		}
	}

	private static void validateQtdMinimaProdutosPromocionaisERestritosUI(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		if ((LavenderePdaConfig.getQtdMinimaProdutosPromocionais() > 0 || LavenderePdaConfig.getQtdMinimaProdutosRestritos() > 0) && !ValueUtil.VALOR_SIM.equals(pedido.flMaxVendaLiberadoSenha)) {
			ItemTabelaPreco itemTabelaPreco = itemPedido.getItemTabelaPreco();
			if (itemTabelaPreco.qtMaxVenda > 0 || ValueUtil.VALOR_SIM.equals(itemTabelaPreco.flPromocao)) {
				int size = pedido.itemPedidoList.size();
				int nuItensNormaisPedido = 0;
				boolean possuiRestritos = false;
				boolean possuiPromocionais = false;
				for (int i = 0; i < size; i++) {
					ItemPedido itemPedidoAux = (ItemPedido) pedido.itemPedidoList.items[i];
					if (!(LavenderePdaConfig.isUsaKitProduto() && ItemKitService.getInstance().isItemPedidoPertenceKit(itemPedidoAux, pedido))) {
						nuItensNormaisPedido++;
						if (itemPedidoAux.getItemTabelaPreco().qtMaxVenda > 0) {
							possuiRestritos = true;
						} else if (ValueUtil.VALOR_SIM.equals(itemPedidoAux.getItemTabelaPreco().flPromocao)) {
							possuiPromocionais = true;
						}
					}
				}
				if (!editing) {
					nuItensNormaisPedido++;
					if (itemTabelaPreco.qtMaxVenda > 0) {
						possuiRestritos = true;
					} else if (ValueUtil.VALOR_SIM.equals(itemTabelaPreco.flPromocao)) {
						possuiPromocionais = true;
					}
				}
				if (possuiRestritos && possuiPromocionais && LavenderePdaConfig.isValidaRegraProdutoRestritoEPromocionalSeparadamente()) {
					String[] lista = {
							StringUtil.getStringValueToInterface(LavenderePdaConfig.getQtdMinimaProdutosRestritos() + LavenderePdaConfig.getQtdMinimaProdutosPromocionais()),
							StringUtil.getStringValueToInterface(nuItensNormaisPedido) };
					UiUtil.showInfoMessage( MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_INFO_ADD_ITEMPROMOCAORESTRICAO, lista));
				} else {
					if (itemTabelaPreco.qtMaxVenda > 0) {
						int qtMinItens = itemTabelaPreco.qtMinItensNormais != 0 ? itemTabelaPreco.qtMinItensNormais : LavenderePdaConfig.getQtdMinimaProdutosRestritos();
						String[] lista = { StringUtil.getStringValueToInterface(qtMinItens), StringUtil.getStringValueToInterface(nuItensNormaisPedido) };
						UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_INFO_ADD_ITEMRESTRICAO, lista));
					}
					if (ValueUtil.VALOR_SIM.equals(itemTabelaPreco.flPromocao) && itemTabelaPreco.qtMaxVenda == 0d) {
						String[] lista = {StringUtil.getStringValueToInterface(LavenderePdaConfig.getQtdMinimaProdutosPromocionais()),StringUtil.getStringValueToInterface(nuItensNormaisPedido) };
						UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_INFO_ADD_ITEMPROMOCAO, lista));
					}
				}
			}
		}
	}

	private static void validateMesmoProdutoNoPedidoUI(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		if (LavenderePdaConfig.permiteIncluirMesmoProdutoNoPedido && !LavenderePdaConfig.usaGradeProduto4()) {
			int qtUnidadesJaInseridos = ItemPedidoService.getInstance().getQtProdutoDoItemJaInseridoPedido(pedido,itemPedido);
			if (!editing && (qtUnidadesJaInseridos > 0)) {
				if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ITEM_DUPLICADO_CONFIRMACAO, qtUnidadesJaInseridos)) == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			} else if (!editing && pedido.isGondola() && ItemPedidoService.getInstance().getQtProdutoDoItemJaInseridoPedido(pedido, itemPedido, true) > 0) {
				if (UiUtil.showConfirmYesCancelMessage(Messages.ITEMPEDIDO_MSG_ITEM_DUPLICADO_CONFIRMACAO_SEM_QT) == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
			}
			if (LavenderePdaConfig.isUsaKitBaseadoNoProduto()) {
				if (itemPedido.getProduto().isKit()) {
					ItemPedido itemPedidoJaInserido = ItemPedidoService.getInstance().getProdutoDoKitJaInseridoNoPedido(itemPedido);
					if (itemPedidoJaInserido != null) {
						if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_ITEM_KIT_JA_INCLUIDO_PEDIDO_CONFIRMACAO,itemPedidoJaInserido.getDsProduto())) == 0) {
							throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
						}
					}
				} else {
					ItemPedido itemKitPedido = ItemPedidoService.getInstance().isProdutoJaInseridoNoPedidoEmUmKit(itemPedido);
					if (itemKitPedido != null) {
						if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_PRODUTO_JA_INSERIDO_KIT_CONFIRMACAO, new Object[] { itemPedido.getDsProduto(), itemKitPedido.getDsProduto() })) == 0) {
							throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
						}
					}
				}
			}
		}
	}

	private static void validateMesmoProdutoOutroLoteNoPedidoUI(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && LavenderePdaConfig.permiteIncluirMesmoProdutoLoteDiferenteNoPedido) {
			if (!editing && ItemPedidoService.getInstance().isItemJaInseridoNoPedidoComOutroLote(pedido, itemPedido)) {
				if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ITEM_LOTE_DUPLICADO_CONFIRMACAO,itemPedido.getProduto().toString())) == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
		}
	}

	private static void validateMesmoProdutoOutraUnidadeNoPedidoUI(ItemPedido itemPedido, Pedido pedido, boolean editing) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) {
			if (!editing && ItemPedidoService.getInstance().isItemJaInseridoNoPedidoComOutraUnidade(pedido, itemPedido)) {
				if (UiUtil.showConfirmYesCancelMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_ITEM_UNIDADE_DUPLICADO_CONFIRMACAO, itemPedido.getProduto().toString())) == 0) {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
		}
	}

	public static void validaProdutoRestrito(Produto produto) {
		if (isBloqueiaInsercaoDoProdutoRestrito(produto)) {
			throw new ValidationException(Messages.PRODUTO_RESTRITO_PARA_VENDA);
		}
	}

	public static boolean isBloqueiaInsercaoDoProdutoRestrito(Produto produto) {
		if (LavenderePdaConfig.isApresentaAvisoAoUsuarioNaInsercaoDoProdutoRestrito() && produto.isProdutoRestrito()) {
			return !UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.MSG_CONFIRMACAO_INSERCAO_PRODUTO_RESTRITO, produto.toString()));
		}
		return false;
	}

	private static void validateVlMinimoVerbaUI(ItemPedido itemPedido, Pedido pedido, double vlSaldo) throws SQLException {
		if (LavenderePdaConfig.usaLiberacaoSenhaValorAbaixoMinimoVerba && LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco && !ValueUtil.VALOR_SIM.equals(pedido.flMinVerbaLiberado)) {
			double vlMinVerba = VerbaSaldoService.getInstance().getVlMinVerba(pedido, pedido.cdEmpresa, pedido.cdRepresentante);
			if (vlMinVerba == 0) {
				return;
			}
			double vlSaldoAtual = vlSaldo + itemPedido.vlVerbaItem - itemPedido.vlVerbaItemOld;
			if (LavenderePdaConfig.usaDescQuantidadePesoAplicaDescNoVlBaseFlex()) {
				vlSaldoAtual += pedido.vlVerbaPedido;
			}
			if (vlSaldoAtual < vlMinVerba) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(Messages.ITEMPEDIDO_LIMITE_VERBA_MINIMO_ULTRAPASSADO);
				senhaForm.setCdCliente(pedido.cdCliente);
				senhaForm.setVlTotalPedido(pedido.vlTotalPedido + itemPedido.vlTotalItemPedido - itemPedido.vlTotalItemPedidoOld);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_TOLERANCIA_VERBA_POSITIVA);
				boolean senhaValida = senhaForm.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA;
				if (senhaValida) {
					pedido.flMinVerbaLiberado = ValueUtil.VALOR_SIM;
				} else {
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
			}
		}
	}

	private boolean registraProdutoFaltaEAjustaEstoqueDisponivel(ItemPedido itemPedido) throws SQLException {
		boolean adicionadoQtItem = false;
		double qtEstoque = EstoqueService.getInstance().getQtEstoqueErpPda(itemPedido, itemPedido.getCdLocalEstoque());
		qtEstoque = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(itemPedido.getItemTabelaPreco(), itemPedido.getProdutoUnidade(), qtEstoque);
		double estoqueErp = EstoqueService.getInstance().getQtEstoqueErp(itemPedido.cdProduto);
		if (qtEstoque > 0 || qtEstoque < itemPedido.nuConversaoUnidade) {
			double qtEstoqueFalta = ValueUtil.round(qtEstoque) - itemPedido.getQtItemFisico();
			if (LavenderePdaConfig.atualizarEstoqueInterno) {
				qtEstoqueFalta += itemPedido.getOldQtItemFisico();
				qtEstoque += itemPedido.getOldQtItemFisico();
			}
			boolean isUsaProdutoFaltanteMsg = LavenderePdaConfig.usaRegistroProdutoFaltante && !LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto();
			if (isUsaProdutoFaltanteMsg && (qtEstoqueFalta < 0 && itemPedido.getQtItemFisico() > 0 && qtEstoque > 0 || estoqueErp > itemPedido.nuConversaoUnidade)) {
				String mensagem;
				if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
					mensagem = MessageUtil.getMessage(Messages.MSG_ESTOQUE_CONFIRMAVENDAESTOQUEFALTANTE, new Object[]{StringUtil.getStringValueToInterface(qtEstoqueFalta * (-1.d), 0), itemPedido.cdUnidade,  StringUtil.getStringValueToInterface(qtEstoque, 0), itemPedido.cdUnidade});
				} else {
					mensagem = MessageUtil.getMessage(Messages.MSG_ESTOQUE_CONFIRMAVENDAESTOQUEFALTANTE_SEMESTOQUE, new Object[]{StringUtil.getStringValueToInterface(qtEstoqueFalta * (-1.d), 0), itemPedido.cdUnidade});

				}
				if (UiUtil.showConfirmYesNoMessage(mensagem)) {
					if (ValueUtil.isNotEmpty(qtEstoque)) {
						edQtItemFisico.setValue(LavenderePdaConfig.bloquearVendaProdutoSemEstoque ? qtEstoque : itemPedido.getQtItemFisico());
					}
					itemPedido.setQtItemFisico(edQtItemFisico.getValueDouble());
					ProdutoFaltaService.getInstance().registraFaltaProduto(itemPedido, (int) Math.abs(qtEstoqueFalta), qtEstoque);
					if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque && !LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) {
						adicionadoQtItem = true;
						
					}
					itemPedido.fromProdutoFaltaMsg =  true;
				}
				setFocusInQtde();
			}
		}
		return adicionadoQtItem;
	}

	private void registraProdutoFaltaEstoquePrevisto(ItemPedido itemPedido, double qtItemFisico) throws SQLException {
		if (!isAddingFromArray && LavenderePdaConfig.usaRegistroProdutoFaltante && ProdutoFaltaService.getInstance().verificaProdutoFaltaEstoquePrevistoNaoExiste(itemPedido)) {
			EstoquePrevisto estoquePrevistoFilter = new EstoquePrevisto(itemPedido.cdEmpresa, itemPedido.cdRepresentante, itemPedido.cdProduto, itemPedido.pedido.dtEntrega);
			Double qtdEstoquePrevisto = EstoquePrevistoDao.getInstance().getQtdEstoquePrevisto(estoquePrevistoFilter);
			if (qtdEstoquePrevisto == null) return;
			if (qtdEstoquePrevisto > 0) {
				double qtEstoqueFalta = ValueUtil.round(qtdEstoquePrevisto) - itemPedido.getQtItemFisico();
				if (LavenderePdaConfig.atualizarEstoqueInterno) {
					qtEstoqueFalta += itemPedido.getOldQtItemFisico();
					qtdEstoquePrevisto += itemPedido.getOldQtItemFisico();
				}
				if (qtEstoqueFalta < 0 && itemPedido.getQtItemFisico() > 0) {
					ProdutoFaltaService.getInstance().registraFaltaProdutoEstoquePrevisto(itemPedido, (int) qtItemFisico, qtdEstoquePrevisto, estoquePrevistoFilter.dtEstoque);
				}
			}
		}
	}

	private static boolean liberaComSenhaVendaProdutoSemEstoque(Pedido pedido, ItemPedido itemPedido, double qtEstoque) throws SQLException {
		if (ValueUtil.VALOR_SIM.equals(itemPedido.flEstoqueLiberado)&& itemPedido.getQtItemFisico() <= itemPedido.getQtItemFisicoAtualizaEstoque()) {
			return true;
		}
		AdmSenhaDinamicaWindow admSenhaDinamicaWindow = new AdmSenhaDinamicaWindow();
		admSenhaDinamicaWindow.setMensagem(MessageUtil.getMessage(Messages.MSG_PRODUTO_SEM_ESTOQUE, new String[] { itemPedido.getProduto().toString(), StringUtil.getStringValueToInterface(qtEstoque) }));
		admSenhaDinamicaWindow.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_VENDA_PRODUTO_SEM_ESTOQUE);
		admSenhaDinamicaWindow.setCdProduto(itemPedido.getProduto().cdProduto);
		admSenhaDinamicaWindow.setCdCliente(pedido.cdCliente);
		admSenhaDinamicaWindow.setQtdeProduto(itemPedido.getQtItemFisico());
		admSenhaDinamicaWindow.edQtdProduto.setEditable(false);
		if (admSenhaDinamicaWindow.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			itemPedido.flEstoqueLiberado = ValueUtil.VALOR_SIM;
			return true;
		}
		return false;
	}

	public static void validateBonificacaoPedidoUI(final ItemPedido itemPedido, final Pedido pedido) throws SQLException {
		try {
			if (LavenderePdaConfig.isPermiteBonificarProduto() && !pedido.bonificacaoLiberada) {
				BonificacaoService.getInstance().validateBonificacaoItem(pedido, itemPedido, true,false);
			}
		} catch (ValidationException ve) {
			if (UiUtil.showConfirmYesCancelMessage(ve.getMessage() + " " + Messages.PEDIDO_MSG_BONIFICACAO_INSERIR_SENHA_LIMITE_BONIFICACAO) == 1) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ve.getMessage());
				senhaForm.setCdCliente(pedido.cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_BONIFICACAO_PEDIDO);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					repaint();
					throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
				}
				repaint();
				pedido.bonificacaoLiberada = true;
			} else {
				throw new ValidationException(ValidationException.EXCEPTION_ABORT_PROCESS);
			}
		}
	}

	@Override
	protected void afterEdit() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		String dsProduto = LavenderePdaConfig.usaGradeProduto5() && itemPedido.getProduto().isProdutoAgrupadorGrade() && isFiltrandoAgrupadorGrade() && !fromListItemPedidoForm ? itemPedido.getProduto().getDsAgrupadorGrade() : itemPedido.getDsProdutoWithKey(itemPedido);
		changeLbDsProdutoValue(dsProduto);
		super.afterEdit();
	}

	@Override
	protected void beforeSave() throws SQLException {
		beforeSave(getItemPedido());
	}

	protected void beforeSave(ItemPedido itemPedido) throws SQLException {
		super.beforeSave();
		if (LavenderePdaConfig.usaGradeProduto4() || (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && itemPedido.getProduto().isProdutoAgrupadorGrade())) {
			Vector itemPedidoPorGradePrecoList = itemPedido.itemPedidoPorGradePrecoList;
			int size = itemPedidoPorGradePrecoList.size();
			for (int i = 0; i < size; i++) {
				ItemPedido itemPedidoPorGrade = (ItemPedido) itemPedidoPorGradePrecoList.items[i];
				processSave(itemPedidoPorGrade);
			}
		} else {
			processSave(itemPedido);
		}
	}

	private void processSave(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isAvisaUsuarioSobreConsumoVerba() && (itemPedido.vlVerbaItem < 0)
				&& (LavenderePdaConfig.usaVerbaItemPedidoPorItemTabPreco || LavenderePdaConfig.usaVerbaPorFaixaRentabilidadeComissao || LavenderePdaConfig.usaVerbaGrupoProdComToleranciaNoDesconto)
				&& !LavenderePdaConfig.aplicaDescQtdPorGrupoProdFecharPedido && itemPedido.getQtItemFisico() > 0d) {
			if (!pedido.isSimulaControleVerba() && !pedido.isIgnoraControleVerba() && !isInsereMultiplosSemNegociacao()) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_VERBA_CONSUMIDA, itemPedido.vlVerbaItem));
			}
		}
		validateItemPedidoUI(itemPedido, pedido, isEditing() || editingFromList);
	}


	private void calcularClick(Vector itemPedidoPrecoPorGradeList) throws SQLException {
		int size = itemPedidoPrecoPorGradeList.size();
		if (size == 0) {
			refreshDomainToScreen(getItemPedido());
			return;
		}

		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoPrecoPorGradeList.items[i];
			calcularClick(false, itemPedido);
		}
		refreshDomainToScreen(getItemPedido());
	}

	public void calcularClick() throws SQLException {
		if (LavenderePdaConfig.usaGradeProduto4() || LavenderePdaConfig.usaGradeProduto5()) {
			Vector itemPedidoPorGradePrecoList = getItemPedido().itemPedidoPorGradePrecoList;
			int size = itemPedidoPorGradePrecoList.size();
			if (size == 0 || LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
				if (calcularClick(false, null)) refreshDomainToScreen(getItemPedido());
				return;
			}
			for (int i = 0; i < size; i++) {
				calcularClick(false, (ItemPedido)itemPedidoPorGradePrecoList.items[i]);
			}
			refreshDomainToScreen(getItemPedido());
		} else {
			if (calcularClick(false, null)) refreshDomainToScreen(getItemPedido());
		}
	}

	public boolean calcularClick(boolean inserindoItem, ItemPedido itemPedido) throws SQLException {
		itemPedido = itemPedido != null ? itemPedido : getItemPedido();
		double qtItemFisico = isAddingFromArray ? itemPedido.getQtItemFisico() : edQtItemFisico.getValueDouble();
		if (LavenderePdaConfig.isUsaPoliticaBonificacaoProduto() && itemPedido.isItemBonificacao() && !itemPedido.pedido.isPedidoBonificacao() && !itemPedido.pedido.getTipoPedido().isIgnoraPoliticaBonificacao()) {
			ItemPedidoBonifCfgService.getInstance().validaEdicaoQtItemFisicoItemBonificadoAutomaticamente(itemPedido, qtItemFisico, null);
		}
		try {
			if ("2".equals(LavenderePdaConfig.usaPrecoPorUnidadeQuantidadePrazo)) {
				itemPedido.setItemTabelaPrecoOld(itemPedido.getItemTabelaPrecoAtual());
			}
			EstoqueService.getInstance().validateEstoque(pedido, itemPedido, true);
		} catch (EstoquePrevistoException epe) {
			resetDescontoQuantidadeOldEstoque(itemPedido);
			registraProdutoFaltaEstoquePrevisto(itemPedido, qtItemFisico);
			throw epe;
		} catch (Throwable ex) {
			if (!pedido.isIgnoraControleEstoque() && LavenderePdaConfig.usaRegistroProdutoFaltante && !fromEdQtItemFisicoFocusOut && !fromBtCalcularClick) {
				if (LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto() && !isPermitidoRegistrarFaltaEstoqueProduto(itemPedido) && LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido && LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
					showProdutoFaltaWindow(itemPedido);
					return false;
				} else if (LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto() && isPermitidoRegistrarFaltaEstoqueProduto(itemPedido)) {
					CadProdutoFaltaWindow cadProdutoFaltaWindow = new CadProdutoFaltaWindow(itemPedido, ex.getMessage(), false, isEditing());
					cadProdutoFaltaWindow.popup();
					qtItemFisico = ValueUtil.round(cadProdutoFaltaWindow.qtItemFisico);
					itemPedido.qtItemFisico = qtItemFisico;
					edQtItemFisico.setValue(qtItemFisico);
					refreshProdutoToScreen(itemPedido);
					setFocusInQtde();
				} else if (!registraProdutoFaltaEAjustaEstoqueDisponivel(itemPedido) && LavenderePdaConfig.permiteIncluirMesmoProdutoUnidadeDiferenteNoPedido) {
					resetDescontoQuantidadeOldEstoque(itemPedido);
					if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
						throw ex;
					}
					}
				}
			}
		
		if (LavenderePdaConfig.getConfigGradeProduto() != 4 && !(LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade())) {
			itemPedido = isAddingFromArray ? itemPedido : (ItemPedido) screenToDomain();
		}
		ItemPedido itemPedidoTela = isAddingFromArray ? itemPedido : (ItemPedido) screenToDomain();
		if (LavenderePdaConfig.isPermiteDecidirModoRegistroFaltaEstoqueProduto()) {
			itemPedido.qtItemFisico = qtItemFisico;
			itemPedidoTela.qtItemFisico = qtItemFisico;
		}
		boolean refreshScreen = calcularItemEspecifico(itemPedido, itemPedidoTela);
		if (LavenderePdaConfig.usaControlePontuacao) {
			setPontuacaoColor(itemPedido);
		}
		return refreshScreen;
		
	}

	private void showProdutoFaltaWindow(ItemPedido itemPedido) throws SQLException {
		if (itemPedido.estoque.qtEstoque <= 0.0 || itemPedido.estoque.qtEstoque < itemPedido.nuConversaoUnidade) {
			CadProdutoFaltaWindow cadProdutoFaltaWindow = new CadProdutoFaltaWindow(getItemPedido(), null, false, isEditing());
			cadProdutoFaltaWindow.popup();
			itemPedido.isRegistrouProdutoFalta = true;
			voltarClick();
		}
	}
		

	private boolean isPermitidoRegistrarFaltaEstoqueProduto(ItemPedido itemPedido) {
	   return itemPedido.estoque.qtEstoque > 0 && itemPedido.nuConversaoUnidade <= itemPedido.estoque.qtEstoque;
	}

	private void resetDescontoQuantidadeOldEstoque(final ItemPedido itemPedido) {
		itemPedido.oldQtItemFisicoDescQtd = itemPedido.getQtItemFisico();
	}

	public boolean calcularItemEspecifico(final ItemPedido itemPedido, final ItemPedido itemPedidoTela) throws SQLException {
		boolean refreshScreen = false;
		try {
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
				itemPedido.auxiliarVariaveis.hadDescontoProgressivoPersonalizado = itemPedido.hasDescProgressivo();
			}
			if ((itemPedido.getProduto() != null && itemPedido.getQtItemFisico() != 0) || (LavenderePdaConfig.permiteItemPedidoComQuantidadeZero && itemPedido.getProduto() != null)) {
				if (inVendaUnitariaMode) {
					validaCalcularClick(itemPedido);
					LoteProdutoService.getInstance().validateEstoque(itemPedido);
				}

				CalculaEmbalagensService.getInstance().calculaDescQtdEmbalagemCompleta(itemPedido, false);
				if (LavenderePdaConfig.isUsaDescontoMaximoBaseadoNoVlBaseFlex() && itemPedido.permiteUtilizarVlBaseFlexComoVlBaseCalculo() && itemPedido.isEditandoQtd()) {
					itemPedido.vlBaseFlex = itemPedido.getItemTabelaPreco().getVlBaseFlex(pedido, itemPedido);
				}
				if (LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo() && houveAlteracaoQtdItem) {
					PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
					refreshDomainToScreen(itemPedido);
					if (itemPedido.isEditandoValorItem()) {
						itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
					}
					houveAlteracaoQtdItem = false;
				}
				try {
					if (LavenderePdaConfig.usaFaixaPesoPorTabelaPreco()) {
						if (pedido.itemPedidoList != null && pedido.itemPedidoList.contains(itemPedido)) {
							if (pedido.descQuantidadePeso != null) {
								if (pedido.descQuantidadePeso.vlPeso <= getItemPedidoService().getAtualPesoPedidoTabelaPreco(pedido, itemPedido)) {
									for (int i = 0; i < itemPedido.pedido.descQuantidadePesoList.size(); i++) {
										DescQuantidadePeso descQtdPeso = (DescQuantidadePeso) itemPedido.pedido.descQuantidadePesoList.items[i];
										if (ValueUtil.valueEquals(descQtdPeso.cdTabelaPreco, itemPedido.cdTabelaPreco)) {
											if (ItemPedidoService.getInstance().getAtualPesoPedidoTabelaPreco(pedido, itemPedido) >= descQtdPeso.vlPeso
													&& itemPedido.vlPctFaixaDescQtdPeso < descQtdPeso.vlPctDesconto) {
												itemPedido.vlPctFaixaDescQtdPeso = descQtdPeso.vlPctDesconto;
											}
										}
									}
								}
							} else {
								itemPedido.vlPctFaixaDescQtdPeso = 0;
							}
						}
					}
					if (LavenderePdaConfig.usaDescPromocionalRegraInterpolacaoPoliticaDesconto()) {
						getPedidoService().loadValorBaseItemPedido(pedido, itemPedido);
					}
					getItemPedidoService().loadPoliticaComercial(itemPedido, pedido);
					getItemPedidoService().calculaDescQtdBeforeLoadPoliticaComercialFaixa(itemPedido, pedido);
					getItemPedidoService().loadPoliticaComercialFaixa(itemPedido);
					getPedidoService().calculateItemPedido(pedido, itemPedido, true);
					// -
					escolheVlAproximadoListaColunaTabPreco(itemPedido);
				} finally {
					if ((pedido.isPedidoBonificacao() && LavenderePdaConfig.isPermiteBonificarQualquerProduto() && LavenderePdaConfig.isUsaMotivosPendenciaPedidosBonificacao())) {
						VerbaService.getInstance().aplicaVerbaNoItemPedido(itemPedido, pedido);
						VerbaService.getInstance().aplicaVerbaPorGrupoProdComTolerancia(pedido, itemPedido);
					}
					refreshScreen = true;
//					refreshDomainToScreen(itemPedidoTela);
				}
				if (LavenderePdaConfig.usaControleNoDescontoPromocional && itemPedido.flAbaixoPtReferenciaDesc && itemPedido.permiteAplicarDesconto()) {
					UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.ITEMPEDIDO_MSG_VL_MINIMO_PCT_DESCPROMOCIONAL, new Object[] { StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), StringUtil.getStringValueToInterface(itemPedido.vlPtReferencialDesc) }));
				}
				if (LavenderePdaConfig.calculaPontuacaoDaRentabilidadeNoPedido) {
					if (itemPedido.pontosMinimoItemNaoAlcancado) {
						UiUtil.showWarnMessage(Messages.MSG_NAO_ALCANCOU_VL_MINS_PONTOS);
					} else if (itemPedido.isVlPrecoCustoNull) {
						UiUtil.showWarnMessage(Messages.MSG_PRODUTO_NAO_POSSUI_VL_CUSTO);
					}
				}
			} else if (itemPedido.getQtItemFisico() == 0) {
				getPedidoService().calculateItemPedido(pedido, itemPedido, false);
				refreshScreen = true;
//				refreshDomainToScreen(itemPedidoTela);
			}
		} finally {
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado && itemPedido.auxiliarVariaveis.hadDescontoProgressivoPersonalizado != itemPedido.hasDescProgressivo()) {
				visibleState();
				addItensOnButtonMenu();
			}
			controlFocusCampos(itemPedidoTela);
		}
		return refreshScreen;
	}

	protected void btCalculaDescQtdEmbalagemCompletaClick(ItemPedido itemPedido) throws SQLException {
		if (itemPedido == null || ValueUtil.isEmpty(itemPedido.cdProduto)) {
			UiUtil.showErrorMessage(Messages.APLICA_DESCONTOQTD_EMB_COMPLETA_CALCULO_ERRO);
			return;
		}
		if (itemPedido.getItemTabelaPreco() == null || ValueUtil.isEmpty(itemPedido.getItemTabelaPreco().descontoQuantidadeList)) {
			UiUtil.showErrorMessage(Messages.APLICA_DESCONTOQTD_EMB_COMPLETA_CALCULO_ERRO_DESCQTD);
			return;
		}
		if (itemPedido.getQtItemFisico() == 0) {
			UiUtil.showErrorMessage(Messages.APLICA_DESCONTOQTD_EMB_COMPLETA_CALCULO_ERRO_QTITEM);
			return;
		}
		EmbalagensResultantes embalagensResultantes = null;
		try {
			embalagensResultantes = CalculaEmbalagensService.getInstance().calculaDescQtdEmbalagemCompleta(itemPedido, true);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
		if (itemPedido.gerouEmbalagemCompleta) {
			UiUtil.showInfoMessage(Messages.APLICA_DESCONTOQTD_EMB_COMPLETA_CALCULO_DESNECESSARIO);
		} else {
			if (embalagensResultantes != null) {
				UiUtil.showInfoMessage(getResultadoInText(embalagensResultantes));
			} else {
				UiUtil.showErrorMessage(Messages.APLICA_DESCONTOQTD_EMB_COMPLETA_CALCULO_ERRO);
			}
		}

	}

	private String getResultadoInText(EmbalagensResultantes embalagensResultantes) {
		double qtdEmbalagemMenor = embalagensResultantes.getResultadoEmbalagemCompletaMenor();
		double qtdEmbalagemMaior = embalagensResultantes.getResultadoEmbalagemCompletaMaior();
		if (qtdEmbalagemMenor == 0) {
			return MessageUtil.getMessage(Messages.APLICA_DESCONTOQTD_EMBALAGEM_COMPLETA_MAX, StringUtil.getStringValueToInterface(qtdEmbalagemMaior));
		} else {
			return MessageUtil.getMessage(Messages.APLICA_DESCONTOQTD_EMBALAGEM_COMPLETA_MIN_MAX, new String[] { StringUtil.getStringValueToInterface(qtdEmbalagemMenor), StringUtil.getStringValueToInterface(qtdEmbalagemMaior) });
		}
	}

	protected void validaCalcularClick(ItemPedido itemPedido) throws SQLException {
	}

	public void escolheVlAproximadoListaColunaTabPreco(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usaListaColunaPorTabelaPreco && !LavenderePdaConfig.isDesconsideraAcrescimoDescontoTrocaTabPreco()) {
			Vector listTabPreco = cbTabelaPreco.listTabelaPrecoListaColuna;
			String cdTabelaPreco = ItemPedidoService.getInstance().consisteVlAproximadoTabelaPreco(listTabPreco,itemPedido);
			if (ValueUtil.isNotEmpty(cdTabelaPreco) && (!cdTabelaPreco.equals(cbTabelaPreco.getValue()))) {
				double vlunitario = edVlItemPedido.getValueDouble();
				double qtUnidade = edQtItemFisico.getValueDouble();
				double qtUnidadeFat = edQtItemFaturamento.getValueDouble();
				cbTabelaPreco.setValue(cdTabelaPreco);
				SessionLavenderePda.cdTabelaPreco = cbTabelaPreco.getValue();
				if (itemPedido.getProduto() != null) {
					getItemPedidoService().clearDadosItemPedido(itemPedido);
					changeItemTabelaPreco();
				}
				edQtItemFisico.setValue(qtUnidade);
				edQtItemFaturamento.setValue(qtUnidadeFat);
				edVlItemPedido.setValue(vlunitario);
				itemPedido = (ItemPedido) screenToDomain();
				getPedidoService().calculateItemPedido(pedido, itemPedido, true);
			}
		}
	}

	public boolean gridClickUnidadeAlternativa(ItemPedido itemPedido, boolean gridClickItemInseridoMultInsercao) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa) {
			itemPedido.cdTabelaPreco = cbTabelaPreco.getValue();
			String cdUnidadeSelecionadaLista = getCdUnidadeSelecionadaListaMultInsercao();
			cbUnidadeAlternativa.load(itemPedido);
			if (LavenderePdaConfig.isUsaSelecaoUnidadeAlternativaCapaPedido()) {
				cbUnidadeAlternativa.setValue(cbUnidade.getValue());
			} else if (mostraSelecaoUnidadeAlternativaAoClicarNaGrid()) {
				if (LavenderePdaConfig.mostraPrecoProdutoTelaAutomaticaSelecaoUnidade) {
					PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
					ListUnidadeAlternativaWindow listUnidadeAlternativaWindow = new ListUnidadeAlternativaWindow(pedido, itemPedido);
					listUnidadeAlternativaWindow.popup();
					cbUnidadeAlternativa.setValue(listUnidadeAlternativaWindow.cdUnidadeAlternativaSelecionada);
					changeUnidadeAlternativa(cbUnidadeAlternativa.getValue());
				} else {
					cbUnidadeAlternativa.setSelectedIndex(0);
					cbUnidadeAlternativa.popup();
				}
				if (cbUnidadeAlternativa.getSelectedIndex() == BaseComboBox.DefaultItemNull) {
					return false;
				}
			} else {
				String cdProdutoUnidadeInicial = null;
				if (gridClickItemInseridoMultInsercao) {
					cdProdutoUnidadeInicial = itemPedido.cdUnidade;
				} else if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa() && ValueUtil.isNotEmpty(cdUnidadeSelecionadaLista)) {
					cdProdutoUnidadeInicial = cdUnidadeSelecionadaLista;
				} else if (ValueUtil.isNotEmpty(itemPedido.getProduto().cdUnidadePreferencial)) {
					cdProdutoUnidadeInicial = itemPedido.getProduto().cdUnidadePreferencial;
				} else {
					ProdutoUnidadeService.getInstance().validaUnidadePadraoProduto(itemPedido.getProduto().cdUnidade, true);
					cdProdutoUnidadeInicial = itemPedido.getProduto().cdUnidade;
				}
				if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid() || LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 2) {
					carregaGridUnidadesProduto(getItemPedido(), cbUnidadeAlternativa.size() > 0 ? new Vector(cbUnidadeAlternativa.getItems()) : new Vector());
					selectUnidadeNaGrid(cdProdutoUnidadeInicial);
					if (gridUnidadeAlternativa.getSelectedIndex() != -1) {
						cdProdutoUnidadeInicial = gridUnidadeAlternativa.getSelectedItem()[0];
					}
				}
				cbUnidadeAlternativa.setValue(cdProdutoUnidadeInicial);
				if (cbUnidadeAlternativa.getSelectedIndex() == -1) {
					cbUnidadeAlternativa.setSelectedIndex(0);
				}
				itemPedido.cdUnidade = cbUnidadeAlternativa.getValue();
				changeUnidadeAlternativa(cbUnidadeAlternativa.getValue());
			}
		}
		return true;
	}

	private String getCdUnidadeSelecionadaListaMultInsercao() {
		if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa() && !ValueUtil.valueEquals(ProdutoUnidade.CDUNIDADE_PADRAO, cbUnidadeAlternativa.getValue())) {
			return cbUnidadeAlternativa.getValue();
		}
		return ValueUtil.VALOR_NI;
	}

	private boolean mostraSelecaoUnidadeAlternativaAoClicarNaGrid() {
		return LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 1 && cbUnidadeAlternativa.count() > 1 && !LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa();
	}

	public void changeUnidadeAlternativa(String cdUnidadeNew) throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		changeUnidadeAlternativa(cdUnidadeNew, itemPedido);
		refreshDomainToScreen(itemPedido);
		setColorsEditsByConversaoUnidade(getItemPedido());
		if (LavenderePdaConfig.limpaQtItemFisicoMudancaUnidade && edQtItemFisico.isEditable()) {
			edQtItemFisico.setText(ValueUtil.VALOR_NI);
		}
	}

	private void changeUnidadeAlternativa(String cdUnidadeNew, ItemPedido itemPedido) throws SQLException {
		itemPedido.cdUnidade = cdUnidadeNew;
		itemPedido.vlBaseCalculoDescPromocional = 0;
		itemPedido.oldQtItemFisicoDescPromocionalQtd = 0;
		itemPedido.clearProdutoUnidadeInfo();
		PedidoService.getInstance().resetDadosItemPedido(pedido, itemPedido);
		if (itemPedido.qtdCreditoDesc > 0 && itemPedido.isFlTipoCadastroDesconto()) {
			ItemPedidoService.getInstance().aplicaDescontoPorCredito(pedido, itemPedido);
		}
		if (LavenderePdaConfig.isCarregaUltimoPrecoItemPedido()) {
			itemPedido.flTipoEdicao = itemPedido.getFltipoEdicao();
		}
		try {
			if (itemPedido.getQtItemFisico() != 0 || itemPedido.qtItemFaturamento != 0) {
				ItemPedidoService.getInstance().controlaDescontosMudancaUnidade(itemPedido);
				if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
					descontoQuantidadeList = null;
				}
				itemPedido.unidadeAlternativaChanged = true;
				PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
				itemPedido.unidadeAlternativaChanged = false;
			}
		} catch (Throwable ex) {
			ExceptionUtil.handle(ex);
		}
	}

	public Vector getDescontoQuantidadeList(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.usarDescontoPorQuantidadeItemPedido) {
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				descontoQuantidadeList = DescQuantidadeService.getInstance().calcDescQuantidadeUnidadeAlternativa(DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto), itemPedido);
			} else {
				descontoQuantidadeList = DescQuantidadeService.getInstance().getDescontoQuantidadeList(itemPedido.cdTabelaPreco, itemPedido.cdProduto);
			}
		}
		return descontoQuantidadeList;
	}

	private void atualizadoInfoEstoque() {
		try {
			if (ValueUtil.isEmpty(getItemPedido().cdProduto)) {
				throw new ValidationException(Messages.PRODUTO_NENHUM_SELECIONADO);
			}
			double qtEstoqueWeb = PedidoUiUtil.getEstoqueProdutoServidor(getItemPedido().cdProduto, getItemPedido().cdLocalEstoque);
			qtEstoqueWeb = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(getItemPedido().getItemTabelaPreco(), cbUnidadeAlternativa.getProdutoUnidade(), qtEstoqueWeb);
			double qtEstoquePda = EstoqueService.getInstance().getEstoque(getItemPedido().cdProduto, Estoque.FLORIGEMESTOQUE_PDA).qtEstoque;
			qtEstoquePda = EstoqueService.getInstance().calculaEstoqueByProdutoUnidade(getItemPedido().getItemTabelaPreco(), cbUnidadeAlternativa.getProdutoUnidade(), qtEstoquePda);
			double qtSaldoEstoque = qtEstoqueWeb - qtEstoquePda;
			lvEstoque.setValue(qtSaldoEstoque);
			if (LavenderePdaConfig.mostraEstoquePrevisto && qtSaldoEstoque > 0) {
				defineCorEstoquePrevistoNoLvEstoque();
				tipEstoque.setText(Messages.ESTOQUE_MSG_ESTOQUEPREVISAO);
			} else if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque || isAvisaVendaProdutoSemEstoqueByItemPedido(getItemPedido().getProduto().isIgnoraValidacao())) {
				if (qtSaldoEstoque <= 0) {
					lvEstoque.setForeColor(ColorUtil.softRed);
					tipEstoque.setText(LavenderePdaConfig.bloquearVendaProdutoSemEstoque ? Messages.ESTOQUE_MSG_BLOQUEADO : Messages.ESTOQUE_MSG_SEMESTOQUE);
				} else {
					lvEstoque.setForeColor(ColorUtil.componentsForeColor);
					tipEstoque.setText(ValueUtil.VALOR_NI);
				}
			}
		} catch (ValidationException e) {
			throw new ValidationException(e.getMessage());
		} catch (Throwable e) {
			LogPdaService.getInstance().logSyncError("Erro ao atualizar o estoque. " + e.getMessage());
			throw new ValidationException("Erro ao atualizar o estoque. " + e.getMessage());
		}
	}


	private void defineCorEstoquePrevistoNoLvEstoque() throws SQLException {
		try {
			TemaSistema tema = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual();
			CorSistema corSistema = CorSistemaLavendereService.getInstance().getCorSistema(tema.cdEsquemaCor, 194);
			lvEstoque.setForeColor(Color.getRGB(corSistema.vlR, corSistema.vlG, corSistema.vlB));
		} catch (Throwable e) {
			lvEstoque.setForeColor(ColorUtil.softGreen);
		}
	}

	private void btEstoqueGradeClick() throws SQLException {
		UiUtil.showProcessingMessage();
		ListEstoqueGradeWindow listProdutoSimilarWindow = new ListEstoqueGradeWindow(getItemPedido() , getItemPedido().itemPedidoPorGradePrecoList, false, pedido.getCdLocalEstoque());
		UiUtil.unpopProcessingMessage();
		listProdutoSimilarWindow.popup();
	}

	public void cbCondicaoComercialChange() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		itemPedido.cdCondicaoComercial = cbCondicaoComercial.getValue();
		ultimaCondicaoComercialSelected = itemPedido.cdCondicaoComercial;
		// --
		if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial) {
			reloadComboTabelaPreco(itemPedido.pedido);
			cbTabelaPreco.setSelectedIndex(0);
		}
		cbTabelaPrecoClick();
	}

	public void reloadComboTabelaPreco(Pedido pedidoReferencia) throws SQLException {
		reloadComboTabelaPreco(pedidoReferencia, "");
	}

	public void reloadComboTabelaPreco(Pedido pedidoReferencia, String tabsPrecoValidaProduto) throws SQLException {
		Pedido pedidoClone = (Pedido) pedidoReferencia.clone();
		if (ValueUtil.isNotEmpty(ultimaCondicaoComercialSelected)) {
			pedidoClone.cdCondicaoComercial = ultimaCondicaoComercialSelected;
		}
		if (LavenderePdaConfig.restringeTabelaPrecoPorCondicaoComercial && LavenderePdaConfig.permiteCondComercialItemDiferentePedido && ValueUtil.isNotEmpty(getItemPedido().cdCondicaoComercial)) {
			pedidoClone.cdCondicaoComercial = getItemPedido().cdCondicaoComercial;
		}
		if (LavenderePdaConfig.selecionaTabelaPromocionalAoClicarNoProduto) {
			cbTabelaPreco.loadTabelasPrecos(pedidoClone);
			cbTabelaPreco.setValue(pedidoClone.cdTabelaPreco);
		} else {
			cbTabelaPreco.loadTabelasPrecos(pedidoClone, true, tabsPrecoValidaProduto);
		}
	}

	protected boolean findProdutoByLeituraCdBarras() throws SQLException {
		if (LavenderePdaConfig.usaCameraParaLeituraCdBarras() && LavenderePdaConfig.usaIncrementoQuantidadePorLeituraCodigoBarras) {
			ProdutoUnidade produtoUnidade = null;
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				produtoUnidade = ProdutoUnidadeService.getInstance().getProdutoUnidadeByCdBarras(getItemPedido(), dsFiltro);
				if (produtoUnidade != null) {
					dsFiltro = produtoUnidade.cdProduto;
				}
			}
			Vector produtoList = getProdutoList();
			if (ValueUtil.isNotEmpty(produtoList)) {
				ProdutoBase produto;
				if (LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco()) {
					produto = (ProdutoTabPreco) produtoList.items[0];
				} else {
					produto = (Produto) produtoList.items[0];
				}
				if (produtoList.size() > 1) {
					UiUtil.showErrorMessage(Messages.LEITURA_CDBARRAS_VARIOS_PRODUTOS);
					return false;
				}
				if (produto.equals(getItemPedido().getProduto())) {
					if (LavenderePdaConfig.usaUnidadeAlternativa) {
						if ((produtoUnidade != null && !ValueUtil.valueEquals(produtoUnidade.cdUnidade, cbUnidadeAlternativa.getValue())) || (produtoUnidade == null && !ValueUtil.valueEquals(produto.cdUnidade,cbUnidadeAlternativa.getValue()))) {
							UiUtil.showErrorMessage(Messages.LEITURA_CDBARRAS_PRODUTO_NAO_CORRESPONDENTE_UNIDADE);
							return false;
						}
					}
					if (pedido.isGondola()) {
						edQtItemFisico.setText(ValueUtil.VALOR_NI);
					} else {
						edQtItemFisico.setValue(edQtItemFisico.getValueDouble() + 1);
					}
					getItemPedido().setQtItemFisico(edQtItemFisico.getValueDouble());
					if (LavenderePdaConfig.bloquearVendaProdutoSemEstoque) {
						EstoqueService.getInstance().validateEstoque(pedido, getItemPedido(), true);
					}
					return true;
				} else {
					UiUtil.showErrorMessage(Messages.LEITURA_CDBARRAS_PRODUTO_NAO_CORRESPONDENTE);
				}
			} else {
				UiUtil.showErrorMessage(Messages.LEITURA_CDBARRAS_PRODUTO_NAO_CORRESPONDENTE);
			}
		}
		return false;
	}

	protected Image getImageIconOcultaInfoRentabilidade(boolean isOculto) {
		return UiUtil.getIconButtonAction("images/ocultarentabilidade.png", isOculto ? LavendereColorUtil.baseForeColorSystem : Color.BRIGHT);
	}

	protected void btItensKitClick() throws SQLException {
		if (getItemPedido().getProduto() != null) {
			if (getItemPedido().getProduto().isKit()) {
				ListProdutoKitWindow listProdutoKitForm = new ListProdutoKitWindow(getItemPedido(),!pedido.isPedidoAberto());
				listProdutoKitForm.popup();
			} else {
				UiUtil.showWarnMessage(Messages.PRODUTOKIT_MSG_PRODUTO_NAO_KIT);
			}
		} else {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
		}
	}

	protected void showAvisoProdutoPendenteRetirada() throws SQLException {
		if (LavenderePdaConfig.isUsaAvisoVendaProdutosPendentesRetiradaSelecionarProduto()) {
			Date dtMaxRetirada = ProdutoRetiradaService.getInstance().getDtMaxRetiradaProduto(pedido.getCliente(),getItemPedido().cdProduto);
			if (dtMaxRetirada != null) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PRODUTORETIRADA_LABEL_MSG_AVISO_PRODUTO, dtMaxRetirada));
			}
		}
	}

	protected String calculateItemForGrid(ValueChooser chooserDesc, BaseDomain domain) throws SQLException {
		ProdutoBase produto;
		if (domain != null) {
			produto = (ProdutoBase) domain;
		} else {
			produto = (ProdutoBase) ProdutoService.getInstance().findByRowKey(listContainer.getSelectedId());
		}
		setProdutoSelecionado(ProdutoService.getInstance().getProduto(produto.cdProduto));
		ItemPedido itemPedido = getItemPedido();
		try {
			inicializaItemFromGrid = true;
			inicializaItemParaVenda(itemPedido);
			changeItemTabelaPreco(null, false);
			itemPedido.cdTabelaPreco = pedido.cdTabelaPreco;
			itemPedido.setQtItemFisico(1);
			if (itemPedido.vlPctDesconto > 0 && ValueUtil.isEmpty(pedido.getCliente().dsDescontoBloqueadoList)) {
				ItemPedidoService.getInstance().aplicaDescontoItemPedido(itemPedido);
			} else if (itemPedido.vlPctAcrescimo > 0 && ValueUtil.isEmpty(pedido.getCliente().dsAcrescimoBloqueadoList)) {
				ItemPedidoService.getInstance().aplicaAcrescimoItemPedido(itemPedido);
			}
			chooserDesc.setValue(itemPedido.getVlPctDescontoAcrescimo());
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
			return Messages.MOEDA + " " + StringUtil.getStringValueToInterface(itemPedido.vlItemPedido);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			itemPedido.setProduto(null);
			setProdutoSelecionado(null);
			inicializaItemFromGrid = false;
		}
		return Messages.MOEDA + " " + StringUtil.getStringValueToInterface(ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, cbTabelaPreco.getValue(), LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), false));
	}

	protected ItemPedido calculateItemForGrid(ItemPedido itemPedido) throws SQLException {
		try {
			PedidoService.getInstance().calculateItemPedido(pedido, itemPedido, true);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			setProdutoSelecionado(null);
		}
		return itemPedido;
	}

	protected boolean isInsereMultiplosSemNegociacao() throws SQLException {
		if (pedido.flSugestao) return true;
		return PedidoService.getInstance().isInsereMultiplosSemNegociacao(pedido);
	}

	protected void setItensValuesOnList(ItemContainer itemCont, BaseDomain domain) throws SQLException {
		ProdutoBase produto = (ProdutoBase) domain;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (produto.cdProduto.equals(itemPedido.cdProduto)) {
				if (produto instanceof Produto) {
					itemPedido.setProduto((Produto) produto);
				}
				itemPedido.setProdutoBase(produto);
				if (LavenderePdaConfig.usaConfigBonificacaoItemPedido() && itemPedido.isItemBonificacao()) {
					ItemPedido itemPedidoNormal = (ItemPedido)itemPedido.clone();
					itemPedidoNormal.flTipoItemPedido = TipoItemPedido.TIPOITEMPEDIDO_NORMAL;
					if (pedido.itemPedidoList.contains(itemPedidoNormal)) continue;
				}
				if (LavenderePdaConfig.showInformacoesComplementaresInsercMultipla()) {
					ItemPedidoService.getInstance().aplicaQtdadeElementarValorElementarNoItemPedido(itemPedido, itemPedido.vlUnidadePadrao, itemPedido.getProdutoUnidade());
				}
				itemCont.setValuesOnList(itemPedido);
				if (itemPedido.isKitTipo3()) {
					itemCont.disableAllControls();
				}
				break;
			}
		}
	}

	protected void btProdutosRetiradaClick() {
		ListProdutoRetiradaWindow listProdutoRetiradaWindow = new ListProdutoRetiradaWindow(SessionLavenderePda.getCliente());
		listProdutoRetiradaWindow.popup();
	}

	protected void cbGrupoProduto1Change() throws SQLException {
		cbGrupoProduto2.loadGrupoProduto2(cbGrupoProduto1.getValue(), pedido);
		cbGrupoProduto2.setSelectedIndex(0);
		cbGrupoProduto3.loadGrupoProduto3(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(), pedido);
		cbGrupoProduto3.setSelectedIndex(0);
		cbGrupoProduto4.loadGrupoProduto4(cbGrupoProduto1.getValue(), cbGrupoProduto2.getValue(),cbGrupoProduto3.getValue(), pedido);
		cbGrupoProduto4.setSelectedIndex(0);
	}

	protected boolean isProdutoSugPerson(String cdProduto) throws SQLException {
		if (ValueUtil.isEmpty(listSugVendaPerson)) {
			listSugVendaPerson = SugVendaPersonService.getInstance().findProdutosSugVendaPerson(pedido,listSugVendaPerson);
		}
		int size = listSugVendaPerson.size();
		for (int i = 0; i < size; i++) {
			if (cdProduto.equals(((ItemPedido) listSugVendaPerson.items[i]).cdProduto)) {
				return true;
			}
		}
		return false;
	}

	protected boolean isProdutoPresenteItemPedido(ProdutoBase produto) {
		if (LavenderePdaConfig.usaGradeProduto5() && isFiltrandoAgrupadorGrade() && produto != null && produto.isProdutoAgrupadorGrade()) {
			return PedidoService.getInstance().isProdutoAgrupadorGradePresentePedido(produto, pedido.itemPedidoList);
		}
		return PedidoService.getInstance().isProdutoPresenteItemPedido(produto, pedido);
	}

	private boolean isProdutoJaInseridoFromSugPerson(String cdProduto) throws SQLException {
		if (ValueUtil.isEmpty(pedido.itemPedidoList)) return false;
		int size = pedido.itemPedidoList.size();
		for (int i = 0; i < size; i++) {
			if (cdProduto.equals(((ItemPedido) pedido.itemPedidoList.items[i]).cdProduto)) {
				return true;
			}
		}
		return false;
	}

	public void addFotoProduto() throws SQLException {
		int spaceReserved = getHeightCbItemGrade1() + getReservedHeightCbItemGrade2();
		if (LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil && pedido.isPedidoVenda()) {
			spaceReserved += isModoPaisagem() ? UiUtil.getControlPreferredHeight() * 3 : UiUtil.getControlPreferredHeight() * 4;
		}
		if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid()) {
			spaceReserved += (LavenderePdaConfig.getUsaSelecaoUnidadePorGrid() + 1) * UiUtil.getControlPreferredHeight();
		}
		if (LavenderePdaConfig.usaSugestaoVendaProdutosPorFoto > 0 && !LavenderePdaConfig.usaLoteProdutoComPossibilidadeDescontoPorPctVidaUtil) {
			int heightSugestaoFotos = containerInfosProduto.getHeight() / 3;
			if (heightSugestaoFotos > UiUtil.getLabelPreferredHeight() * 2 && !isEditing()) {
				UiUtil.add(containerInfosProduto, containerFotosProdutos, LEFT, BOTTOM - spaceReserved, FILL, heightSugestaoFotos);
				int gapContainer = containerFotosProdutos.getHeight() / 13;
				int widthContainer = containerInfosProduto.getWidth() / 3 - (gapContainer * 2 / 3);
				if (fotosProdSugList.size() < 3) {
					widthContainer = containerInfosProduto.getWidth() / fotosProdSugList.size() - (gapContainer * 2 / 3);
				}
				for (int i = 0; i < fotosProdSugList.size(); i++) {
					UiUtil.add(containerFotosProdutos, (ImageCarrousel) fotosProdSugList.items[i], i == 0 ? LEFT : AFTER + gapContainer, CENTER, widthContainer, containerFotosProdutos.getHeight() - (gapContainer * 2));
				}
				spaceReserved += containerFotosProdutos.getHeight();
			} else {
				containerInfosProduto.remove(containerFotosProdutos);
			}
		}
		if (isShowFotoProdutoSlider()) {
			spaceReserved += getHeightFotoProdutoSlider();
		}
		if (imageCarrousel == null) {
			createImageCarrousel();
		}
		try {
			addImageCarrouselContainers(spaceReserved);
		} catch (Throwable ex) {
			containerInfosProduto.remove(imageCarrousel);
		}
	}

	protected void addFotoProdutoSlider() throws SQLException {
		imageCarouselSlider.setVisible(true);
		UiUtil.add(containerInfosProduto, imageCarouselSlider, LEFT, AFTER, FILL, getHeightFotoProdutoSlider());
		imageCarouselSlider.initUI();
	}

	protected void addImageCarrouselContainers(int spaceReserved) throws SQLException {
		if (containerIconsProduto.getY2() >= 0) {
			UiUtil.add(containerInfosProduto, imageCarrousel, LEFT, containerIconsProduto.getY2(), FILL, FILL - spaceReserved);
			imageCarrousel.resetSetPositions();
			if (imageCarrousel.getHeight() < (UiUtil.getControlPreferredHeight() + (HEIGHT_GAP * 2))) {
				containerInfosProduto.remove(imageCarrousel);
			}
			if (isShowFotoProdutoSlider()) {
				addFotoProdutoSlider();
			} else if (imageCarouselSlider != null) {
				imageCarouselSlider.setVisible(false);
			}
		}
	}

	public void btIconeFotoClick() throws SQLException {
		ImageSliderProdutoWindow imageSliderProdutoWindow;
		if (isModoGrade() || (LavenderePdaConfig.usaGradeProduto5() && LavenderePdaConfig.isUsaFotoProdutoGrade() && isItemPedidoGrade())) {
			imageSliderProdutoWindow = new ImageSliderProdutoWindow(getItemPedido().getProduto(), getItemPedido().cdItemGrade1, LavenderePdaConfig.isUsaFotoProdutoGrade());
		} else {
			imageSliderProdutoWindow = new ImageSliderProdutoWindow(getItemPedido().getProduto(), false);
		}
		imageSliderProdutoWindow.popup();
	}
	
	protected Vector getDomainListByListContainer(int startIndex) {
		int size = listContainer.size();
		Vector produtoList = new Vector(size - startIndex);
		for (int i = startIndex; i < size; i++) {
			produtoList.addElement(getProdutoFromListContainerByIndex(i));
		}
		return produtoList;
	}

	public void btSlideFotosClick() throws SQLException {
		if (listContainer.size() > 0) {
			UiUtil.showProcessingMessage();
			Vector produtoList = getDomainListByListContainer(0);
			// --
			this.imageSliderProdutoList = new ImageSliderProdutoWindow(produtoList, listContainer.getSelectedIndex(), getQtTotalRegistros());
			UiUtil.unpopProcessingMessage();
			this.imageSliderProdutoList.popup();
			if (this.imageSliderProdutoList.imageSelected != null) {
				String[] imageSelected = this.imageSliderProdutoList.imageSelected;
				for (int i = 0; i < listContainer.size(); i++) {
					BaseListContainer.Item item = (Item) listContainer.getContainer(i);
					if (imageSelected[1].equals(ProdutoService.getInstance().getDsProdutoSlide((ProdutoBase) item.domain))) {
						listContainer.setSelectedIndex(i);
						listContainer.scrollToControl(listContainer.getContainer(i));
						setProdutoSelecionado(null);
						if (inVendaUnitariaMode) {
							gridClick();
						} else {
							gridClickAndRepaintScreen();
						}
						break;
					}
				}
			}
			this.imageSliderProdutoList = null;
		} else {
			UiUtil.showWarnMessage(Messages.SLIDE_SEM_FOTO);
		}
	}

	protected Produto getProdutoSessaoVenda() throws SQLException {
		if ((produtoSelecionado == null) || permiteInserirMultiplosItensPorVezComInterfaceNegociacao()) {
			produtoSelecionado = getSelectedProduto();
		}
		return produtoSelecionado;
	}

	public void itemContainerProdutoClick(ItemContainer itemCont) throws SQLException {
		if (isInserindoItems()) {
			nuAcaoThreadMultInsercao = NU_ACAO_DETALHE;
			UiUtil.showInfoMessage(Messages.AGUARDE_PROCESSANDO_ITENS);
			return;
		}
		if (itemCont.chooserQtd.getValue() <= 0) {
			setUnidadeSelecionadaLista(itemCont);
			return;
		}
		setDomain(itemCont.itemPedido);
		showDetalheItemJaInseridoMultInsercao = true;
		fromListDetalheInsecaoMult = true;
	}

	private void setUnidadeSelecionadaLista(ItemContainer itemCont) throws SQLException {
		if (LavenderePdaConfig.usaUnidadeAlternativa && LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
			ItemPedido itemPedido = getItemPedido();
			itemPedido.cdProduto = itemCont.produto.cdProduto;
			cbUnidadeAlternativa.load(itemPedido);
			cbUnidadeAlternativa.setValue(itemCont.cbUnidadeAlternativa.getValue(), itemCont.produto.cdProduto, itemCont.produto.cdItemGrade1);
		}
	}

	protected ProdutoBase getProdutoBaseFilter() {
		ProdutoBase produto = LavenderePdaConfig.isUsaFiltroProdutoPorTabelaPreco() ? new ProdutoTabPreco() : new Produto();
		if (usaLazyLoading) {
			produto.limit = getLimitDomainFilter();
			produto.offset = offset;
			produto.contaProdutosListados = true;
		} else if (!isFiltrandoAgrupadorGrade()){
			produto.limit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
		}
		produto.cdEmpresa = SessionLavenderePda.cdEmpresa;
		produto.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		produto.filtrandoAgrupadorGrade = isFiltrandoAgrupadorGrade();
		return produto;
	}

	private String getDsEmpresa() {
		try {
			String nmEmpresa = EmpresaService.getInstance().getEmpresaName(SessionLavenderePda.cdEmpresa);
			return StringUtil.getStringValue(nmEmpresa);
		} catch (Throwable e) { }
		return ValueUtil.VALOR_NI;
	}

	public int getLarguraColuna(int defaultLenght, String desc) {
		int lenght = fm.stringWidth(desc);
		int width = Settings.screenWidth;
		width = width > Settings.screenHeight ? Settings.screenHeight : width;
		return ValueUtil.getIntegerValueRoundUp(((double)(lenght > defaultLenght ? lenght : defaultLenght) / width) * 100);
	}

	private void criaGridUnidadeAlternativa() {
		if (LavenderePdaConfig.mostraUnidadesAlternativasAoSelecionarProduto == 2) {
			GridColDefinition[] gridColDefiniton = {new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
				new GridColDefinition(Messages.METAS_UNIDADE, -30, LEFT),
				new GridColDefinition(Messages.UNIDADE_LABEL_QT_ELEMENTAR, -20, RIGHT),
				new GridColDefinition(Messages.UNIDADE_LABEL_VL_ELEMENTAR, -20, RIGHT),
				new GridColDefinition(Messages.PRODUTO_LABEL_PRECO, -20, RIGHT),
				new GridColDefinition(Messages.UNIDADE_LABEL_FATOR, -20, RIGHT) };
			gridUnidadeAlternativa = UiUtil.createGridEdit(gridColDefiniton, false);
		} else if (LavenderePdaConfig.isUsaSelecaoUnidadePorGrid()) {
			int valuesLenght = fm.stringWidth(StringUtil.getStringValueToInterface(99999.99));
			int lbDeLenght = getLarguraColuna(valuesLenght ,Messages.UA_DE);
			int lbQtElementarLength = getLarguraColuna(valuesLenght ,Messages.UNIDADE_LABEL_QT_ELEMENTAR);
			int lbVlUnidadeLenght = getLarguraColuna(valuesLenght, Messages.VLUNIDADE);
			int lbPorLenght = getLarguraColuna(valuesLenght, Messages.UA_POR);
			int width = Settings.screenWidth;
			width = width > Settings.screenHeight ? Settings.screenHeight : width;
			int checkLenght = ValueUtil.getIntegerValue(((fmH*3d/2)/width)*100);
			int lbUnidadeLenght = 100 - lbDeLenght - lbQtElementarLength - lbVlUnidadeLenght - lbPorLenght - checkLenght;
			GridColDefinition[] gridColDefiniton = {new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
					new GridColDefinition(Messages.LABEL_UNIDADES_ALTERNATIVAS_ABREV, -lbUnidadeLenght, LEFT),
					new GridColDefinition(Messages.UNIDADE_LABEL_QT_ELEMENTAR, -lbQtElementarLength, RIGHT),
					new GridColDefinition(Messages.VLUNIDADE, -lbVlUnidadeLenght, LEFT),
					new GridColDefinition(Messages.UA_DE, -lbDeLenght, LEFT),
					new GridColDefinition(Messages.UA_POR, -lbPorLenght, LEFT),
					new GridColDefinition("  ", -checkLenght, LEFT)
					};
			gridUnidadeAlternativa = UiUtil.createGridEdit(gridColDefiniton, false, false);
			gridUnidadeAlternativa.setGridControllable();
			gridUnidadeAlternativa.gridController.setColForeColor(Color.BRIGHT, 4);
			gridUnidadeAlternativa.gridController.setColScratchedList(4);
			gridUnidadeAlternativa.gridController.setPaintCheckColOnSelectedLine(true);
		}
		if (gridUnidadeAlternativa != null) {
			gridUnidadeAlternativa.setID("gridUnidadeAlternativa");
		}
	}

	protected void btObservacaoClick() throws SQLException {
		ItemPedido itemPedido = getItemPedido();
		if (itemPedido.getProduto() == null) {
			UiUtil.showGridEmptySelectionMessage(Messages.PRODUTO_NOME_ENTIDADE);
			return;
		}
		WmwInputBox wInputBox = new WmwInputBox(Messages.ITEMPEDIDO_LABEL_DSOBSERVACAO, Messages.ITEMPEDIDO_MSG_OBSERVACAO, itemPedido.dsObservacao, LavenderePdaConfig.qtCaracteresObservacoesPedidoItemPedido);
		wInputBox.useInputMultiLine = true;
		wInputBox.setOnlyShowMessage(!pedido.isPedidoAberto() || cadPedidoForm.inOnlyConsultaItens);
		wInputBox.popup();
		if (wInputBox.getPressedButtonIndex() == 1) {
			itemPedido.dsObservacao = wInputBox.getValue();
		}
	}

	private void verificaAcrescimoPedidoChange(final ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaAcrescimoNoPedidoAplicadoPorItem() || itemPedido.pedido.vlPctAcrescimoItem == 0) return;

		if (ValueUtil.valueEquals(itemPedido.vlPctAcrescimo, pedido.vlPctAcrescimoItem) || !aplicaAcrescimoDiferenteDoPedido()) return;

		itemPedido.vlPctAcrescimo = pedido.vlPctAcrescimoItem;
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, itemPedido.pedido);
		itemPedido.vlPctAcrescimo = ItemPedidoService.getInstance().getVlPctAcrescimoPoliticaComercial(pedido.vlPctAcrescimoItem, itemPedido);
		}
		itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_ACRESCIMOPCT;
		edVlPctAcrescimo.setValue(itemPedido.vlPctAcrescimo);
		edVlPctAcrescimoValueChange(itemPedido);
		calcularClick();
	}

	private boolean aplicaDescontoDiferenteDoPedido() {
		if (LavenderePdaConfig.ocultaConfirmacaoDescAcresItens()) {
			if (LavenderePdaConfig.replicaDescontoAcrescimoDaCapaNosItens()) return true;
			if (LavenderePdaConfig.nuncaReplicaDescontoAcrescimoDaCapaNosItens()) return false;
		}
		return UiUtil.showConfirmYesNoMessage(Messages.ITEMPEDIDO_MSG_DESCONTO_DIFERENTE_DO_PEDIDO);
	}

	private boolean aplicaAcrescimoDiferenteDoPedido() {
		if (LavenderePdaConfig.ocultaConfirmacaoDescAcresItens()) {
			if (LavenderePdaConfig.replicaDescontoAcrescimoDaCapaNosItens()) return true;
			if (LavenderePdaConfig.nuncaReplicaDescontoAcrescimoDaCapaNosItens()) return false;
		}
		return UiUtil.showConfirmYesNoMessage(Messages.ITEMPEDIDO_MSG_ACRESCIMO_DIFERENTE_DO_PEDIDO);
	}

	private void verificaDescontoPedidoChange(final ItemPedido itemPedido) throws SQLException {
		if (!LavenderePdaConfig.isUsaDescontoNoPedidoAplicadoPorItem()) return;

		if (LavenderePdaConfig.isAcumulaComDescDoItem() || (LavenderePdaConfig.aplicaSomenteItemSemDesconto() && itemPedido.vlPctDesconto > 0 )) return;
		if (ValueUtil.valueEquals(itemPedido.vlPctDesconto, pedido.vlPctDescItem) || !aplicaDescontoDiferenteDoPedido()) return;

		double vlPctMaxDesconto = itemPedido.getItemTabelaPreco().getVlPctMaxDescontoItemTabelaPreco(itemPedido.getProduto());
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido && LavenderePdaConfig.isUsaDescontoPorTipoPedidoEProduto()) {
			vlPctMaxDesconto = TipoPedProdDescAcresService.getInstance().getPctDescontoPorTipoPedidoEProduto(itemPedido);
		} else if (TipoPedidoService.getInstance().isUsaDescontoMaximoPorTipoPedido(itemPedido.pedido)) {
			vlPctMaxDesconto =  itemPedido.pedido.getTipoPedido().vlPctMaxDesconto;
		}
		if (LavenderePdaConfig.usaPoliticaComercial()) {
			ItemPedidoService.getInstance().loadPoliticaComercial(itemPedido, itemPedido.pedido);
		vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescontoPoliticaComercial(vlPctMaxDesconto, itemPedido);
		}

		double pctMaxDesconto = LavenderePdaConfig.isUsaDescontoPedidoPorClienteMaximo() ? pedido.getCliente().vlPctMaxDesconto : vlPctMaxDesconto;
		pctMaxDesconto = LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacaoMaxDescUsuarioWorkflow() ? UsuarioDescService.getInstance().getVlPctMaxDescontoUsuario(true, true) : pctMaxDesconto;
		itemPedido.vlPctDescPedido = 0;
		if (pedido.vlPctDescItem < pctMaxDesconto) {
			itemPedido.vlPctDesconto = pedido.vlPctDescItem;
			itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_DESCONTOPCT;
		} else {
			itemPedido.vlPctDesconto = vlPctMaxDesconto;
			UiUtil.showConfirmMessage(MessageUtil.getMessage(Messages.DESCONTO_PEDIDO_NAO_APLICADO_ITEM_PEDIDO, pctMaxDesconto));
		}
		edVlPctDesconto.setValue(itemPedido.vlPctDesconto);
		edVlPctDescontoValueChange(itemPedido, true);
		calcularClick();
	}

	public void btDiferencaClick() throws SQLException {
		ItemPedidoErpDif itemPedidoErpDif = new ItemPedidoErpDif(getItemPedido());
		itemPedidoErpDif = (ItemPedidoErpDif) ItemPedidoErpDifService.getInstance().findByRowKey(itemPedidoErpDif.getRowKey());
		if (itemPedidoErpDif == null) {
			UiUtil.showInfoMessage(Messages.REL_DIF_ITEMPEDIDO_MSG_SEM_DIFERENCAS	);
			return;
		}
		new RelDiferencasItemPedidoWindow(itemPedidoErpDif).popup();
	}

	public boolean usaCameraParaLeituraCdBarras() {
		return LavenderePdaConfig.usaCameraParaLeituraCdBarras();
	}

	public void configureCarrouselItemPedido(List<ItemPedido> itemPedidoList, CadItemPedidoFormWindow window, int type) {
		itemPedidoCarrouselList = itemPedidoList;
		itemPedidoCarrouselList.get(0).inCarrousel = true;
		inCarrouselMode = true;
		carrouselType = type;
		this.inCarrouselProdutoCliente = CadItemPedidoFormWindow.CARROUSEL_TYPE_PRODUTOCLIENTE == type;
		carrouselWindow = window;
		selectedItemPedidoCarrousel = 0;
		title = Messages.ITEMPEDIDO_TITLE_ITENS_DIVERGENTES_CARROUSEL;
		lbItemByItem.setValue("1/" + itemPedidoCarrouselList.size());
		controlaVisibilidadeBotoesPreviousNext();
	}

	public boolean ajustouTodosItensSemQuantidade() {
		for (ItemPedido itemPedido : itemPedidoCarrouselList) {
			if (itemPedido.getQtItemFisico() == 0) {
				return false;
			}
		}
		return pedido.itemPedidoList.size() > 0;
	}

	@Override
	protected void voltarClick() throws SQLException {
		if (inCarrouselMode && carrouselWindow != null) {
			if (carrouselWindow.isDisplayed()) {
				carrouselWindow.unpop();
			}
		}
		super.voltarClick();
	}

	public boolean ajustouTodosItensBloqueados() throws SQLException {
		for (ItemPedido itemPedido : itemPedidoCarrouselList) {
			if (ProdutoBloqueadoService.getInstance().isBloqueadoAllTabelaPreco(itemPedido) || ProdutoBloqueadoService.getInstance().isBloqueadoForTabelaPreco(itemPedido)) {
				return false;
			}
		}
		return pedido.itemPedidoList.size() > 0;
	}

	public boolean ajustouTodosItensProdutoRestrito() throws SQLException {
		for (ItemPedido itemPedido : itemPedidoCarrouselList) {
			if (RestricaoService.getInstance().isProdutoRestrito(itemPedido.cdProduto, pedido.cdCliente, null, itemPedido.getQtItemFisico()) != null) return false;
		}
		return pedido.itemPedidoList.size() > 0;
	}

	public boolean ajustouTodosItensProdutoClienteRestrito() throws SQLException {
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante || LavenderePdaConfig.filtraProdutoClienteRepresentante || LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
			for (ItemPedido itemPedido : itemPedidoCarrouselList) {
				try {
					ProdutoService.getInstance().validateProdutoRelacaoDisponivel(pedido, itemPedido);
				} catch (ProdutoTipoRelacaoException e) {
					return false;
				}
			}
		}
		return pedido.itemPedidoList.size() > 0;
	}

	public boolean isAvisaVendaProdutoSemEstoqueByItemPedido(boolean flIgnoraValidacao) {
		return LavenderePdaConfig.isAvisaVendaProdutoSemEstoque() && !flIgnoraValidacao;
	}

	public boolean isGrifaProdutoSemEstoqueNaLista(boolean flIgnoraValidacao) {
		return LavenderePdaConfig.usaGrifaProdutoSemEstoqueNaListaProdutosDentroForaPedido() && !flIgnoraValidacao;
	}

	private boolean validaQtItemBonificado(ItemPedido itemPedido) throws SQLException {
		double saveQtItemFisico = itemPedido.getQtItemFisico();
		itemPedido.setQtItemFisico(edQtItemFisico.getValueDouble());
		if (!ItemPedidoService.getInstance().validaQtItemBonificado(itemPedido)) {
			edQtItemFisico.setValue(saveQtItemFisico);
			return false;
		}
		return true;
	}

	protected boolean isInserindoItems() {
		return false;
	}

	protected void btAgrupadorGradeClick() throws SQLException {
		if (hideAttributes && isFiltrandoAgrupadorGrade()) {
			btHideAttributesClick();
			listContainer.hideDiscontField = false;
		}
		filtrandoAgrupadorGrade = !isFiltrandoAgrupadorGrade();
		setAgrupadorGradeButtonState(isFiltrandoAgrupadorGrade(), true);
		carregaGridProdutos();
		if (!filtrandoAgrupadorGrade) {
			defineNuSeqItemPedidoAoIniciar(pedido);
		}
	}

	private void setAgrupadorGradeButtonState(boolean state, boolean isUserAction) throws SQLException {
		boolean permiteOcultar = isAtributoOcultavel(LavenderePdaConfig.isPermiteOcultarValoresItemAgrupadorGrade(), LavenderePdaConfig.isPermiteOcultarValoresItemMultiplaInsercao());
		btAgrupadorGrade.setImage(getAgrupadorGradeButtonIcon(state));
		listContainer.setShowHideDiscontButtonVisibility(permiteOcultar);
		if (isUserAction) {
			ConfigInternoService.getInstance().addValue(ConfigInterno.ULTIMACONFIGURACAOBOTAOAGRUPADORGRADE, StringUtil.getStringValue(isFiltrandoAgrupadorGrade()));
			listContainer.reloadTopBarControls();
		}
		addItensOnButtonMenu();
	}

	protected void atualizaListaItensPedidoAlteracaoExclusao() throws SQLException {
 		int sizeListCon = instance != null && instance.flListInicialized ? instance.listContainer.size() : listContainer.size();
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		try {
			msg.popupNonBlocking();
			for (int i = 0; i < sizeListCon; i++) {
				BaseListContainer.Item c = instance != null && instance.flListInicialized ? (BaseListContainer.Item) instance.listContainer.getContainer(i) : (BaseListContainer.Item) listContainer.getContainer(i);
				if (c == null) {
					continue;
				}
				String[] newItemString = produtoToGridRow(c.domain, pedido.cdTabelaPreco);
				c.setItens(newItemString);
				ItemPedido itemPedido = getItemPedidoFromProdutoFromVector(pedido.itemPedidoList, c.id);
				try {
					if (!PedidoService.getInstance().isProdutoPresenteItemPedido((ProdutoBase) c.domain, pedido)) {
						listContainer.setContainerBackColor(c, ColorUtil.baseBackColorSystem);
					}
					if (itemPedido == null) {
						setPropertiesInRowList(c, c.domain);
					}
				} catch (Throwable e) {
					ExceptionUtil.handle(e);
				}
				if (itemPedido != null) {
					setPropertiesInRowList(c, itemPedido.getProduto());
					if (itemPedido.itemChanged && c.rightControl instanceof ItemContainer) {
						itemPedido.itemChanged = false;
						ItemContainer item = (ItemContainer) c.rightControl;
						if (item != null) {
							item.setValuesOnList(itemPedido);
							if (LavenderePdaConfig.isPermiteSelecaoUnidadeAlternativa()) {
								item.cbUnidadeAlternativa.setValue(itemPedido.cdUnidade, itemPedido.cdProduto, itemPedido.cdItemGrade1);
							}
						}
					}
				} else if (c.rightControl instanceof ItemContainer) {
					ItemContainer item = (ItemContainer)c.rightControl;
					if (item != null && item.itemPedido != null && ValueUtil.isNotEmpty(item.itemPedido.cdProduto)) {
						if (item.itemPedido.getQtItemFisico() > 0) {
							double vlPctMaxDesconto = ItemPedidoService.getInstance().getVlPctMaxDescontoItemPedido(item.itemPedido);
							if (item.itemPedido.pedido.vlPctDescItem > vlPctMaxDesconto) {
								item.itemPedido.vlPctDesconto = vlPctMaxDesconto;
							} else if (item.itemPedido.pedido.vlPctDescItem > 0) {
								item.itemPedido.vlPctDesconto = item.itemPedido.pedido.vlPctDescItem;
								item.itemPedido.vlPctAcrescimo = 0;
							} else {
								item.itemPedido.vlPctAcrescimo = item.itemPedido.pedido.vlPctAcrescimoItem;
								item.itemPedido.vlPctDesconto = 0;
							}
							item.itemPedido.setQtItemFisico(0);
							if (item.vlItemOriginal > 0) {
								item.itemPedido.vlItemPedido = item.vlItemOriginal;
							}
							item.clear(calculateItemForGrid(item.itemPedido));
							item.setValuesOnList(item.itemPedido);
							item.itemPedido = null;
						}
					} else if (item != null) {
						item.setChooserQtdValue(0);
						item.setColorsEditsByConversaoUnidade();
					}
				}
			}
		} finally {
			msg.unpop();
		}
		pedido.atualizaLista = false;
	}

	public ItemPedido getItemPedidoFromProdutoFromVector(Vector listItem, String rowKey) throws SQLException {
		if (ValueUtil.isEmpty(listItem) || ValueUtil.isEmpty(rowKey)) return null;
		int size = listItem.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) listItem.items[i];
			if (rowKey.equals(itemPedido.getProduto().getRowKey())) return itemPedido;
		}
		return null;
	}

	protected void defineNuSeqItemPedidoAoIniciar(Pedido pedido) {
		nuSeqItemPedido = ItemPedidoService.getInstance().getNextNuSeqItemPedido(pedido);
		nuSeqItemPedido = nuSeqItemPedido == 0 ? 1 : nuSeqItemPedido;
	}

	public double getVlProdutoComST(ProdutoBase produto, Cliente cliente) throws SQLException {
		ItemPedido itemPedido = createItemPedidoComVlVendaProdutoToLista(produto, getItemPedido());
		ItemPedidoService.getInstance().applyIndiceFinanceiroCondPagto(itemPedido, pedido);
		ItemPedidoService.getInstance().calculaStItemPedido(itemPedido, cliente);
		if (LavenderePdaConfig.calculaStSimplificadaItemPedido && pedido.getCliente().isFlAplicaSt()) {
			itemPedido.vlSt = itemPedido.getProduto().vlSt;
			if (LavenderePdaConfig.usaUnidadeAlternativa) {
				ItemPedidoService.getInstance().getVlStItemPedidoComConversaoUnidade(itemPedido);
			}
		}
		return itemPedido.vlSt + itemPedido.vlItemPedido;
	}

	public double getVlProdutoComIndiceFinanceiro(ProdutoBase produto) throws SQLException {
		ItemPedido itemPedido = createItemPedidoComVlVendaProdutoToLista(produto, getItemPedido());
		ItemPedidoService.getInstance().applyIndiceFinanceiroCondPagto(itemPedido, pedido);
		return itemPedido.vlItemPedido;
	}

	private ItemPedido createItemPedidoComVlVendaProdutoToLista(ProdutoBase produto, ItemPedido itemPedidoReferencia) throws SQLException {
		ItemPedido itemPedido = (ItemPedido) itemPedidoReferencia.clone();
		itemPedido.cdProduto = produto.cdProduto;
		itemPedido.vlItemPedido = ItemTabelaPrecoService.getInstance().getVlVendaProdutoToListaAdicionarItens(pedido, produto, cbTabelaPreco.getValue(), LavenderePdaConfig.isUsaPrecoPorUnidadeQuantidadePrazo(), true);
		itemPedido.vlBaseItemPedido = itemPedido.vlItemPedido;
		return itemPedido;
	}

	protected void createImageCarrousel() {
		imageCarrousel = new ImageCarrousel(Produto.getPathImg(), false);
		imageCarrousel.showArrowButtons = true;
		imageCarrousel.hasBotaoRodape = false;
	}

	protected boolean isShowFotoProdutoSlider() throws SQLException {
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade() && isFiltrandoAgrupadorGrade()) {
			Produto produtoSessaoVenda = getProdutoSessaoVenda();
			return produtoSessaoVenda != null && produtoSessaoVenda.isProdutoAgrupadorGrade();
		}
		return false;
	}

	protected LinkedHashMap<String, Image> getImageListCarousel(ItemPedido itemPedido) throws SQLException {
		return ProdutoGradeService.getInstance().getImagesAgrupadorGradeGroupByProduto(itemPedido);
	}

	protected void listarMantendoScroll(GridListContainer listContainerSelected) throws SQLException {
		if (listContainerSelected != null) {
			int lastIndex = listContainerSelected.getSelectedIndex();
			int lastScrollPos = listContainerSelected.getScrollPos();
			carregaGridProdutos();
			if (lastIndex > 0) {
				Container previousContainer = listContainerSelected.getContainer(lastIndex - 1);
				if (previousContainer == null && lastIndex > 1) {
					previousContainer = listContainerSelected.getContainer(lastIndex - 2);
				}
				if (previousContainer != null) {
					if (lastIndex >= listContainerSelected.size()) {
						lastIndex = listContainerSelected.size() - 1;
					}
					listContainerSelected.scrollToControl(previousContainer);
					listContainerSelected.setSelectedIndex(lastIndex);
					listContainerSelected.setScrollPos(lastScrollPos);
				}
			}
		}
	}

	protected String getVisibleStringEstoque(String text) {
		if (hideAttributes && isAtributoOcultavel(LavenderePdaConfig.isPermiteOcultarValoresEstoqueItemAgrupadorGrade(), LavenderePdaConfig.isPermiteOcultarValoresEstoqueItemMultiplaInsercao())) {
			return ValueUtil.VALOR_NI;
		}
		return text;
	}

	protected String getVisibleStringVlItem(String text) {
		if (hideAttributes && isAtributoOcultavel(LavenderePdaConfig.isPermiteOcultarValoresTotaisItemAgrupadorGrade(), LavenderePdaConfig.isPermiteOcultarValoresTotaisItemMultiplaInsercao())) {
			return ValueUtil.VALOR_NI;
		}
		return text;
	}

	private boolean isAtributoOcultavel(boolean permiteOcultarAgrupadorGrade, boolean permiteOcultarMultiplaInsercao) {
		boolean ocultavel = isFiltrandoAgrupadorGrade() && permiteOcultarAgrupadorGrade;
		ocultavel |= !isFiltrandoAgrupadorGrade() && permiteOcultarMultiplaInsercao;
		return ocultavel;
	}

	protected void setImagesCarouselSliderByItemPedido(ItemPedido itemPedido) throws SQLException {
		imageCarouselSlider.addImages(getImageListCarousel(itemPedido));
	}

	protected void addItemPedidoToListItemGrade(Vector itemPedidoPorPrecoGradeList, ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade() && isModoGrade(true)) {
			validaDescontoAcrescimoItemPedidoGrade(itemPedido);
			itemPedido.naoComparaSeqItem = true;
			int index = itemPedidoPorPrecoGradeList.indexOf(itemPedido);
			itemPedido.naoComparaSeqItem = false;
			if (index < 0 && itemPedido.getQtItemFisico() > 0) {
				itemPedido.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_INSERINDO;
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
				itemPedidoPorPrecoGradeList.addElement(itemPedido);
			} else if (index >= 0) {
				if (itemPedido.getQtItemFisico() == 0) {
					itemPedido.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_EXCLUINDO;
				} else {
					boolean itemExisteNoPedido = itemPedido.pedido.itemPedidoList.contains(itemPedido);
					if (itemExisteNoPedido && houveAlteracaoItemPedido(itemPedido)) {
						itemPedido.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_ATUALIZANDO;
					} else if (!itemExisteNoPedido) {
						itemPedido.flEdicaoItemPedidoGrade = ItemPedido.FLEDICAOITEMGRADE_INSERINDO;
					}
				}
				itemPedido.flTipoEdicao = ItemPedido.ITEMPEDIDO_EDITANDO_VLITEM;
				itemPedidoPorPrecoGradeList.items[index] = itemPedido;
			}
		}
	}

	private void validaDescontoAcrescimoItemPedidoGrade(ItemPedido itemPedido) throws SQLException {
		getItemPedidoService().calculaItemPedidoGradeDescontoAcrescimo(itemPedido);
		getItemPedidoService().validateDescAcresMax(itemPedido);
		getItemPedidoService().garantirDescontoAcrescimentoNaoNegativo(itemPedido);
	}

	private boolean houveAlteracaoItemPedido(ItemPedido itemPedido) {
		return itemPedido.getQtItemFisico() != itemPedido.getOldQtItemFisico() || itemPedido.vlTotalItemPedidoOld != itemPedido.vlTotalItemPedido;
	}

	protected void prepareAndLoadImageCarouselSliderByItemPedido(ItemPedido itemPedido) throws SQLException {
		if (LavenderePdaConfig.isUsaCarrosselImagemProdutosComAgrupadorGrade()) {
			loadImageSize(true);
			setImagesCarouselSliderByItemPedido(itemPedido);
			Vector itemPedidoGradeList = getItemPedido().itemPedidoPorGradePrecoList;
			int indexProduto = findIndexProdutoInItemPedidoList(imageCarouselSlider.getSelectedItemId(), itemPedidoGradeList);
			if (indexProduto >= 0) {
				setItemPedido((ItemPedido)getItemPedido().itemPedidoPorGradePrecoList.items[indexProduto]);
				getItemPedido().itemPedidoPorGradePrecoList = itemPedidoGradeList;
				getItemPedido().pedido = pedido;
			} else {
				setProdutoInItemPedidoSessaoByRowKey(imageCarouselSlider.getSelectedItemId());
				changeItemTabelaPreco();
				createItemPedidoNaoInseridoByItemPedidoReference(getItemPedido());
				setItemPedido(itemPedidoBaseNaoInserido);
			}
		}
	}

	private int findIndexProdutoInItemPedidoList(String rowkeyProduto, Vector itemPedidoList) throws SQLException {
		for (int i = 0; i < itemPedidoList.size(); i++) {
			Produto produto = ((ItemPedido)itemPedidoList.items[i]).getProduto();
			if (ValueUtil.valueEquals(rowkeyProduto, produto.getRowKey())) {
				return i;
			}
		}
		return -1;
	}

	public void setItemPedido(ItemPedido itemPedido) {
		setDomain(itemPedido);
	}

	protected boolean isModoPaisagem() {
		return width > height;
	}

	protected int getHeightFotoProdutoSlider() {
		if (LavenderePdaConfig.apresentaDsFichaTecnicaCapaItemPedido && LavenderePdaConfig.usaGradeProduto5() && isModoGrade()) {
			return containerInfosProduto.getHeight() / 6;
		} else {
			return containerInfosProduto.getHeight() / 4;
		}
	}

	private boolean isAplicaMultiploEspecialAuto(final ItemPedido itemPedido) {
		boolean descPromoDesabilitado = LavenderePdaConfig.isCadastroDescontoPromocionalDesabilitado() || itemPedido.descPromocional == null || itemPedido.descPromocional.qtItem == 0;
		return LavenderePdaConfig.aplicaMultiploEspecialAutoItemPedido && !isEditing() && !isModoGrade() && descPromoDesabilitado;
	}

	private void aplicaMultiploEspecialAuto(ItemPedido itemPedido) throws SQLException {
		ProdutoUnidade prodUni = itemPedido.getProdutoUnidade();
		double nuMultiEspecial = itemPedido.getProduto().nuMultiploEspecial;
		nuMultiEspecial = (LavenderePdaConfig.consisteConversaoUnidadesMultiploEspecialUnidadeAlternativa() && prodUni != null && prodUni.nuMultiploEspecial != 0) ? prodUni.nuMultiploEspecial : nuMultiEspecial;
		if (nuMultiEspecial != 0d) {
			itemPedido.setQtItemFisico(nuMultiEspecial);
			edQtItemFisico.setValue(nuMultiEspecial);
			flFocoQtFaturamento = false;
		}
	}

	protected void addInfosPersonalizadasCapaItemPedido() {
		int y = 0;
		if (((LavenderePdaConfig.isOcultaComboItemGrade() || !cbItemGrade1.isVisible()) && isModoGrade()) || !isModoGrade()) {
			y = BOTTOM - HEIGHT_GAP - edDsInfosPersonalizadasItemPedido.getHeight();
		} else {
			y = BEFORE - edDsInfosPersonalizadasItemPedido.getHeight() - lbItemGrade1.getHeight() - HEIGHT_GAP_BIG;
		}
		UiUtil.add(containerInfosProduto, lbDsInfosPersonalizadasItemPedido, edDsInfosPersonalizadasItemPedido, getLeft(), y);
	}
	
	protected void prepareGrupoProdTipoPedFilter(ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.filtraGrupoProdutoPorTipoPedido) {
			if (pedido.getTipoPedido() == null) {
				throw new FilterNotInformedException();
			}
			produtoFilter.grupoProdTipoPedFilter = new GrupoProdTipoPed();
			produtoFilter.grupoProdTipoPedFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produtoFilter.grupoProdTipoPedFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GrupoProdTipoPed.class);
			produtoFilter.grupoProdTipoPedFilter.cdTipoPedido = pedido.cdTipoPedido;
			produtoFilter.grupoProdTipoPedFilter.cdGrupoProduto1 = cbGrupoProduto1.getValue();
			produtoFilter.grupoProdTipoPedFilter.cdGrupoProduto2 = cbGrupoProduto2.getValue();
			produtoFilter.grupoProdTipoPedFilter.cdGrupoProduto3 = cbGrupoProduto3.getValue();
			produtoFilter.grupoProdTipoPedFilter.excecaoGrupoProdutoFilter = pedido.getTipoPedido().isFlExcecaoGrupoProduto();
		}
	}
	protected void prepareProdutoClienteFilter(ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.filtraProdutoClienteRepresentante) {
			produtoFilter.produtoClienteFilter = ProdutoClienteService.getInstance().getProdutoClienteFilter(pedido.cdCliente);
		}
	}
	protected void prepareClienteProdutoFilter(ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.filtraClientePorProdutoRepresentante) {
			produtoFilter.clienteProdutoFilter = ClienteProdutoService.getInstance().getClienteProdutoFilter(pedido.cdCliente);
		}
	}
	
	protected void prepareProdutoCondPagtoFilter(ProdutoBase produtoFilter) throws SQLException {
		if (LavenderePdaConfig.usaFiltroProdutoCondicaoPagamentoRepresentante) {
			produtoFilter.produtoCondPagtoFilter = ProdutoCondPagtoService.getInstance().getProdutoCondPagtoFilter(pedido.cdCondicaoPagamento);
		}
	}

	private void btIconePopupCarrouselClick() throws SQLException {
		Vector produtoList = ProdutoService.getInstance().findProdutoGradeComDsProdutoByItemPedido(getItemPedido());
		int indexImageOld = imageCarouselSlider.getSelectedIndex();
		ImageSliderProdutoWindow imageSliderProdutoWindow = new ImageSliderProdutoWindow(produtoList, imageCarrousel.getSelectedImage());
		imageSliderProdutoWindow.popup();
		setSelectedCarouselSliderItemByImage(imageSliderProdutoWindow.imageSelected, produtoList, indexImageOld);
		imageCarouselProdutoClick();
	}

	private void setSelectedCarouselSliderItemByImage(String[] imageSelected, Vector produtoList, int indexImageOld) {
		if (imageSelected != null) {
			int size = produtoList.size();
			for (int i = 0; i < size; i++) {
				ProdutoBase produtoBase = (ProdutoBase) produtoList.items[i];
				if (imageSelected[1].equals(ProdutoService.getInstance().getDsProdutoSlide(produtoBase))) {
					imageCarouselSlider.setSelectedItem(produtoBase.getRowKey());
					scrollToSelectedCarouselItem(i, indexImageOld);
					break;
				}
			}
		}
	}

	private void scrollToSelectedCarouselItem(int indexImageAtual, int indexImageOld) {
		imageCarouselSlider.doScrollHorizontally(indexImageAtual - indexImageOld);
	}
	
	
	protected void prepareGiroProdutoFilter(ProdutoBase produtoFilter) {
		if (pedido.isSugestaoPedidoGiroProduto() || cadPedidoForm.solicitadoAcessoGiroProduto) {
			produtoFilter.giroProdutoFilter = new GiroProduto();
			produtoFilter.giroProdutoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
			produtoFilter.giroProdutoFilter.cdRepresentante = SessionLavenderePda.getCdRepresentanteFiltroDados(GiroProduto.class);
			produtoFilter.giroProdutoFilter.cdCliente = pedido.cdCliente;
		}
	}
	
	protected void listScroll() throws SQLException {
		offset += getMaxResult();
		carregaMaisRegistrosGridProdutos();
	}
	
	protected void setValueQtProdutosTotalizador(int qtTotalProdutos) {
		lvQtProdutos.setText(LavenderePdaConfig.getMessageCustomLeftTotalizer(qtTotalProdutos));
		lvQtProdutos.reposition();
	}
	
	protected void clearListContainerAndTotalizers() {
		listContainer.removeAllContainers();
		setValueQtProdutosTotalizador(0);
	}
	
	protected ProdutoBase getProdutoFromListContainerByIndex(int index) {
		return (ProdutoBase) ((BaseListContainer.Item) listContainer.getContainer(index)).domain;
	}
	
	protected int getMaxResult() {
		return LavenderePdaConfig.getNuRegistrosAddItemPedidoMostradosPorVezNaLista();
	}
	
	protected void atualizaTotalizadorTotalRegistrosAposFiltrar() {
		setValueQtProdutosTotalizador(getQtTotalRegistros());
	}
	
	public int getQtTotalRegistros() {
		int qtTotalRegistros = listContainer.size();
		buscaLimitadaProduto = false;
		if (usaLazyLoading && qtTotalRegistros > 0) {
			qtTotalRegistros = getProdutoFromListContainerByIndex(0).qtProdutosListados; 
		}
		if (LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0 && LavenderePdaConfig.nuLinhasRetornoBuscaSistema <= qtTotalRegistros) {
			qtTotalRegistros = LavenderePdaConfig.nuLinhasRetornoBuscaSistema;
			buscaLimitadaProduto = true;
		}
		return qtTotalRegistros;
	}
	
	@Override
	public void onFormClose() throws SQLException {
		super.onFormClose();
		FotoProdutoLazyLoadUtil.stopAllThreadsFotoProdutoLazyLoad();
	}
	
	protected boolean isApresentaMensagemLimiteNuRegistrosBuscaSistema() {
		return LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0 && buscaLimitadaProduto && LavenderePdaConfig.apresentaMensagemLimiteNuLinhasRetornoBuscaSistema;
	}
	
	protected int getLimitDomainFilter() {
		int qtLimit = getMaxResult();
		if (LavenderePdaConfig.nuLinhasRetornoBuscaSistema > 0 && offset + qtLimit > LavenderePdaConfig.nuLinhasRetornoBuscaSistema) {
			qtLimit = LavenderePdaConfig.nuLinhasRetornoBuscaSistema - offset;
		}
		return qtLimit;
	}
	
	private void showMessageFiltroQuantidadeResultados() {
		if (isApresentaMensagemLimiteNuRegistrosBuscaSistema()) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_QUANTIDADE_RESULTADOS, LavenderePdaConfig.nuLinhasRetornoBuscaSistema));
		}
	}
}
