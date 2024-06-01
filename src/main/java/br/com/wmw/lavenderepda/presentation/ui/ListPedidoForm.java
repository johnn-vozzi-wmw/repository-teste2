package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.CorSistema;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.Calculator;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.vo.TipoPendenciaPedido;
import br.com.wmw.lavenderepda.business.enums.RecalculoRentabilidadeOptions;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import br.com.wmw.lavenderepda.business.service.ComboService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.CorSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.ErroEnvioService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import br.com.wmw.lavenderepda.business.service.MargemRentabService;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PontuacaoConfigService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.SolAutorizacaoService;
import br.com.wmw.lavenderepda.business.service.TemaSistemaLavendereService;
import br.com.wmw.lavenderepda.business.service.UsuarioDescService;
import br.com.wmw.lavenderepda.business.service.VisitaPedidoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CategoriaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.EmpresaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.NivelLiberacaoPedidoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.OrigemPedidoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusExcecaoMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusOrcamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusPedidoPdaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoClienteRedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPendenciaPedidoMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.thread.EnviaDadosThread;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import br.com.wmw.lavenderepda.util.UiMessagesUtil;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListPedidoForm extends LavendereCrudListForm {

	public static final int PEDIDO_GRID_POS_NUPEDIDO = 1;
	public static final int PEDIDO_GRID_POS_VLTOTALPEDIDO = 5;
	public static final int PEDIDO_GRID_POS_FLPOSSUIDIFERENCA = 7;
	public static final int PEDIDO_GRID_POS_CONDICAOPAGTO = 8;
	public static final int PEDIDO_GRID_POS_FLORIGEMPEDIDO = 9;
	private static final String DTENTREGAFILTER = "Entrega";
	private static final String DTEMISSAO_DEFAULT_ORDER_COLUMNS = "DTEMISSAO, HREMISSAO, HRFIMEMISSAO";
	private static final String DTEMISSAO_DEFAULT_ORDER_ASC = "N,N,N";
	private static ListPedidoForm listPedidoForm;
	private BaseButton btFiltroAvancado;
	private ButtonAction btNovoPedido;
	private ButtonAction btFecharPedidos;
	private ButtonOptions bmOpcoes;
	private ButtonAction btAgruparPedidoConsignacao;
	protected StatusPedidoPdaComboBox cbStatusPedido;
	protected StatusExcecaoMultiComboBox cbStatusExcecaoPedido;
	private OrigemPedidoComboBox cbOrigemPedido;
	private RepresentanteSupervComboBox cbRepresentante;
	private StatusOrcamentoComboBox cbStatusOrcamento;
	private EmpresaComboBox cbEmpresa;
	private LabelTotalizador lvTtPedidoComTributos;
	private LabelTotalizador lbTotalPeso;
	private LabelTotalizador lbTotalVolume;
	private LabelTotalizador lbTtQtItemFisico;
	private LabelTotalizador lbTtQtItemFaturamento;
	private LabelTotalizador lbTtRentabilidade;
	private LabelTotalizador lbTotalTrocas;
	public CheckBoolean ckPedidoDif;
	public CheckBoolean ckPedidoPerdido;
	private CheckBoolean ckPedidoRecalculoPendente;
	private int lastRepSelected;
	private String cdClienteFilter;
	private Produto produtoFilter;
	private String cdTipoPedidoFilter;
	private String cdTipoPendenciaPedidoFilter;
	private Date[] lastDateFilter = new Date[2];
	public int listSize;
	private Vector pedidoProblemaGeracaoPdfOffline;
	//Messages
	private static String MENU_UTILITARIO_CALCULADORA = "";
	private static String MENU_RESUMO_PEDIDOS = "";
	private boolean flListInicialized;
	public boolean inConsultaUltimosPedidos;
	public boolean inPedidosDifNaoLidos;
	public boolean inRelPedidosAbertos;
	public boolean inConsultaPedidoReqServ;
	public String cdCargaPedidoFilter;
	public String cdMarcadorFilter;
	public Pedido pedidoConsultaUltimosPedidos;
	private boolean isAllSelected;
	private Date dtEmissaoLimite;
	public boolean hasPedidosRecentes;
	public CadClienteMenuForm cadClienteMenuForm;
	public Date dtInitialFilter;
	public Date dtFinalFilter;
	public String dtEmissaoEntregaTipoFilter;
	public String dsClienteFilter;
	private CheckBoolean ckFiltraPedidoNfe;
	private CheckBoolean ckFiltraPedidoDispLiberacao;
	private CheckBoolean ckFiltraPedidoItensPendentes;
	private TipoClienteRedeComboBox cbTipoClienteRedeComboBox;
	private SessionTotalizerContainer sessaoTotalizadores;
	public boolean inPedidoDiferentesAposSync;
	private CheckBoolean ckFiltraPedidoPendenteLimiteCredito;
	private EditFiltro edFiltroNuPedido;
	private String dsFiltroOrdemCompraCliente;
	private RedeComboBox cbRede;
	private CategoriaComboBox cbCategoria;
	private CheckBoolean ckFiltraMeusPedidos;
	private Map<String, Image> marcadoresMap;
	private int iconSize;
	public NivelLiberacaoPedidoComboBox nivelLiberacaoPedidoComboBox;

	private HashMap<String, Marcador> marcadoresHash;

	public static ListPedidoForm getInstance() throws SQLException {
		if (listPedidoForm == null) {
			return new ListPedidoForm();
		} else {
			return listPedidoForm;
		}
	}

	public ListPedidoForm() throws SQLException {
		this(null);
	}

    public ListPedidoForm(Pedido pedido) throws SQLException {
        super(Messages.MENU_OPCAO_PEDIDO);
        setBaseCrudCadForm(new CadPedidoForm());
		singleClickOn = true;
		constructorListContainer();
		sessaoTotalizadores = new SessionTotalizerContainer();
        pedidoConsultaUltimosPedidos = pedido;
        cbStatusPedido = new StatusPedidoPdaComboBox();
        cbStatusPedido.setID("cbStatusPedido");
		if (LavenderePdaConfig.usaFiltroStatusExcecaoListaPedidos()) {
			cbStatusExcecaoPedido = new StatusExcecaoMultiComboBox();
			cbStatusExcecaoPedido.load();
			loadCbStatusExecaoSelect();
		}
        cbOrigemPedido = new OrigemPedidoComboBox();
        if (LavenderePdaConfig.usaFiltroEmpresaListaPedidos()) {
        	cbEmpresa = new EmpresaComboBox(Messages.EMPRESA_NOME_ENTIDADE, BaseComboBox.DefaultItemType_ALL);
        	cbEmpresa.loadEmpresa();
    	}
        btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
        btNovoPedido = new ButtonAction(Messages.BOTAO_NOVO_PEDIDO, "images/novopedido.png", true);
	    btNovoPedido.setID("btNovoPedido");
        btFecharPedidos = new ButtonAction(Messages.BOTAO_FECHAR_PEDIDOS, "images/fecharpedido.png");
        btAgruparPedidoConsignacao = new ButtonAction(Messages.BOTAO_AGRUPAR_CONSIGNACAO, "images/agrupar.png");
        cbRepresentante = new RepresentanteSupervComboBox(3);
        lvTtPedidoComTributos = new LabelTotalizador("999999999,999");
        lbTotalPeso = new LabelTotalizador("99999,99");
        lbTotalVolume = new LabelTotalizador("99999,99");
        lbTtQtItemFisico = new LabelTotalizador("9999.99");
        lbTtQtItemFaturamento = new LabelTotalizador("9999.99");
        lbTtRentabilidade = new LabelTotalizador("9999.99");
        lbTotalTrocas = new LabelTotalizador("9999.99");
        ckPedidoDif = new CheckBoolean(Messages.PEDIDO_COM_DIFERENCAS);
        ckPedidoPerdido = new CheckBoolean(Messages.CHK_PEDIDO_PERDIDO);
        ckPedidoRecalculoPendente = new CheckBoolean(Messages.PEDIDO_RECALCULO_PENDENTE);
        //--
        listPedidoForm = this;
        bmOpcoes = new ButtonOptions().setID("bmOpcoes");
    	ckFiltraPedidoNfe = new CheckBoolean(Messages.NFE_LABEL);
    	ckFiltraPedidoDispLiberacao = new CheckBoolean(Messages.PEDIDO_FILTRO_DISP_LIBERACAO);
    	ckFiltraPedidoItensPendentes = new CheckBoolean(Messages.PEDIDO_FILTRO_PEDIDO_ITENS_PENDENTES);
    	ckFiltraPedidoPendenteLimiteCredito = new CheckBoolean(Messages.PEDIDO_PENDENTE_LIMITE_CREDITO);
    	cbTipoClienteRedeComboBox = new TipoClienteRedeComboBox();
    	ckFiltraMeusPedidos= new CheckBoolean(Messages.PEDIDO_FILTRO_MEUS_PEDIDOS);
    	if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
    		cbStatusOrcamento = new StatusOrcamentoComboBox(true);
    		cbStatusOrcamento.load();
    	}
    	if (LavenderePdaConfig.usaFiltroNumeroPedidoListaPedidos()) {
    		edFiltroNuPedido = new EditFiltro("999999999", 50);
    	}
    	if (LavenderePdaConfig.usaFiltroRedeClienteListaPedidos()) {
    		cbRede = new RedeComboBox();
    	}
    	if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
    		cbCategoria = new CategoriaComboBox();
    	}
    	if (LavenderePdaConfig.usaMarcadorPedido || LavenderePdaConfig.usaFiltroMarcadorListaPedidos()) {
			marcadoresMap = new HashMap<>();
			resizeIconsLegend = false;
			useLeftTopIcons = true;
			iconSize = UiUtil.getDefaultIconSize();
		}
	    if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && LavenderePdaConfig.isUsaMotivoPendencia()) {
		    nivelLiberacaoPedidoComboBox = new NivelLiberacaoPedidoComboBox();
		    nivelLiberacaoPedidoComboBox.loadNiveisLiberacao();
		    if (SessionLavenderePda.nuOrdemLiberacaoUsuario > 0)
		        nivelLiberacaoPedidoComboBox.setSelectedItem(SessionLavenderePda.nuOrdemLiberacaoUsuario);
	    }
    }

    private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
    	configListContainer(DTEMISSAO_DEFAULT_ORDER_COLUMNS, DTEMISSAO_DEFAULT_ORDER_ASC);
		flListInicialized = true;
		int itemCount = 4;
		int indexToBold = 0; 
		if (LavenderePdaConfig.isMostraColunaQtdNaListaDePedidos()) {
			itemCount++;
		}
		if (LavenderePdaConfig.isMostraColunaQtdEmbalagensNaListaDePedidos()) {
			itemCount++;
		}
	    if (isShowRepLista()) {
			itemCount+=2;
	    }
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			itemCount++;
		}
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens() && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			itemCount++;
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			itemCount++;
		}
		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
    		if (itemCount % 2 == 1) {
    			itemCount++;
    		}
    		itemCount++;
    	}
		if (LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido()) {
			indexToBold = itemCount++;
		}
		if (LavenderePdaConfig.usaConfigMargemRentabilidade() && !LavenderePdaConfig.isOcultaPercentualMargemRentabilidadeListaPedido()) {
			itemCount++;
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			if (itemCount % 2 == 0) {
				itemCount += 2;
			} else {
				itemCount += 1;
			}
		}
		if (LavenderePdaConfig.isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido()) {
			itemCount += 2;
		}
		listContainer = new GridListContainer(itemCount, 2, true, false);
		ScrollPosition.AUTO_HIDE = true;
		if (LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido()) {
			listContainer.getLayout().boldItems[indexToBold] = true;
			listContainer.getLayout().defaultItemColors[indexToBold] = ColorUtil.componentsForeColor;
			listContainer.setColsSort(getColsSort());
		}
		listContainer.setColsSort(getColsSort());
		itemCount--;
		for (int i = itemCount; i >= 0; i--) {
			if (i % 2 > 0) {
				listContainer.setColPosition(i, RIGHT);
			}
		}
		if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
			listContainer.setColTotalizerRight(itemCount, Messages.PEDIDO_LABEL_TOTALVENDAS);
		}
    }

	@Override
	protected String[] getItem(Object domain) throws SQLException {
        Pedido pedido = (Pedido) domain;
        String lbVlTotalPedidoTributos = "";
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens()) {
    		if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
    			lbVlTotalPedidoTributos = Messages.PEDIDO_LABEL_VL_TOTAL_PEDIDO_TRIBUTOS_RESUMIDO;
    		} else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
    			lbVlTotalPedidoTributos = Messages.PEDIDO_LABEL_VLTOTALPEDIDOST;
    		} else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
    			lbVlTotalPedidoTributos = Messages.PEDIDO_LABEL_VL_TOTAL_PEDIDO_IPI;
    		}
   			ItemPedidoService.getInstance().loadTributacaoItensPedido(pedido);
    	}
    	Vector item = new Vector();
    	item.addElement(pedido.nuPedido + " - " + StringUtil.getStringValue(pedido.dtEmissao)); //left 0
    	if (isFiltroStatusTodosSelecionado()) { //right 1
    		item.addElement(pedido.statusPedidoPda.dsStatusPedido);
    	} else {
    		item.addElement("");
    	}
    	if (Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(pedido.cdCliente) || Cliente.CD_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO.equals(pedido.cdCliente)) { //left 2
    		item.addElement(Messages.PEDIDO_SEM_CLIENTE);
    	} else {
    		item.addElement(StringUtil.getStringValue(pedido.cdCliente) + " - " + StringUtil.getStringValue(pedido.getCliente().nmRazaoSocial));
    	}
    	item.addElement(""); //right 3
		if (isShowRepLista()) {
			item.addElement(StringUtil.getStringValue(pedido.cdRepresentante) + " - " + StringUtil.getStringValue(pedido.nmRepresentante));
			item.addElement("");
		}
    	if (LavenderePdaConfig.isMostraColunaQtdNaListaDePedidos()) {
    		item.addElement(StringUtil.getStringValueToInterface(PedidoService.getInstance().getQtdeItensPedido(pedido)) + " " + LavenderePdaConfig.mostraColunaQtdNaListaDePedidos + ". ");
    	}
    	if (LavenderePdaConfig.isMostraColunaQtdEmbalagensNaListaDePedidos()) {
    		double qtItensFaturamentoPedido = PedidoService.getInstance().getQtItensFaturamentoPedido(pedido);
    		String qtItemFat = LavenderePdaConfig.isUsaQtdInteiro() ? StringUtil.getStringValueToInterface((int) qtItensFaturamentoPedido) : StringUtil.getStringValueToInterface(qtItensFaturamentoPedido);  
			item.addElement(qtItemFat + " " + LavenderePdaConfig.mostraColunaQtdEmbalagensNaListaDePedidos + ". ");
    	}
    	if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
		    String qtPeso = StringUtil.getStringValueToInterface(pedido.qtPeso) + " " + Messages.ITEMPEDIDO_LABEL_PESO;
		    if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio()) {
			    qtPeso = MessageUtil.getMessage(Messages.LISTA_PEDIDO_PESO_MEDIO, StringUtil.getStringValueToInterface(pedido.qtPeso));
		    }
		    item.addElement(qtPeso);
    	}
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens() && !LavenderePdaConfig.ocultaInfosValoresPedido) {
    		item.addElement(lbVlTotalPedidoTributos + " "  + StringUtil.getStringValueToInterface(pedido.vlFinalPedidoDescTribFrete));
    	}
    	if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
    		item.addElement(StringUtil.getStringValueToInterface(pedido.vlVolumePedido, LavenderePdaConfig.nuCasasDecimaisVlVolume) + " " + Messages.ITEMPEDIDO_LABEL_VOLUME);
    	}
    	if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
    		if (item.size() % 2 == 1) {
    			item.addElement("");
    		}
    		if (pedido.statusOrcamento != null) {
        		item.addElement(StringUtil.getStringValue(pedido.statusOrcamento.dsStatusOrcamento));
    		}
    	}

    	if (LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido()) {
    		item.addElement(Messages.PEDIDO_LIST_PCT_RENTABILIDADE_NO_PEDIDO + " " + StringUtil.getStringValueToInterface(pedido.getVlPctRentabilidadeByConfigRentabilidadeNoPedido(false), LavenderePdaConfig.nuCasasDecimais));
    	}

    	if (LavenderePdaConfig.usaConfigMargemRentabilidade() && !LavenderePdaConfig.isOcultaPercentualMargemRentabilidadeListaPedido()) {
    		item.addElement(Messages.PEDIDO_LIST_PCT_MARGEM_RENTABILIDADE_NO_PEDIDO + " " + StringUtil.getStringValueToInterface(pedido.vlPctMargemRentab, LavenderePdaConfig.nuCasasDecimais));
    	}

    	if (!LavenderePdaConfig.ocultaInfosValoresPedido) {
    		if (item.size() % 2 == 0) {
    			item.addElement("");
    		}
    		item.addElement(getStringForTotalPedidoContainer(pedido));
    	}
    	if (LavenderePdaConfig.isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido()) {
    		item.addElement(getTipoPendenciaPedidoMessage(pedido));
    		item.addElement(ValueUtil.VALOR_NI);
    	}
        return (String[]) item.toObjectArray();
    }
    
    private Object getStringForTotalPedidoContainer(Pedido pedido) throws SQLException {
    	if (pedido.isPedidoTroca()) {
    		return Messages.ITEMPEDIDO_LABEL_TOTALVENDA_TROCA + StringUtil.getStringValueToInterface(pedido.vlTrocaRecolher);
    	} else if (pedido.isPedidoBonificacao()) {
    		double vlTotalPedido = pedido.vlTotalPedido == 0 ? pedido.vlBonificacaoPedido : pedido.vlTotalPedido;
    		return Messages.ITEMPEDIDO_TOTAL_BONIFICACAO + " " + Messages.ITEMPEDIDO_LABEL_TOTALVENDA + StringUtil.getStringValueToInterface(vlTotalPedido);
    	} else {
    		return LavenderePdaConfig.utilizaNotasCredito() ? (Messages.ITEMPEDIDO_LABEL_TOTALVENDA + StringUtil.getStringValueToInterface(pedido.vlTotalBrutoItens > 0 ? pedido.vlTotalBrutoItens : 0 )) : (Messages.ITEMPEDIDO_LABEL_TOTALVENDA + StringUtil.getStringValueToInterface(pedido.vlTotalPedido > 0 ? pedido.vlTotalPedido : 0 )); 
    	}
    }

    @Override
    protected CrudService getCrudService() throws SQLException {
        return PedidoService.getInstance();
    }
    
    private void fillTipoPendenciaPedidoFilter(Pedido pedido) {
    	final String[] tipoPendenciaList = StringUtil.split(cdTipoPendenciaPedidoFilter, TipoPendenciaPedidoMultiComboBox.SEPARADOR);
    	for (String cdTipoPendencia : tipoPendenciaList) {
	    	switch (cdTipoPendencia) {
			case TipoPendenciaPedido.CDTIPOPENDENCIA_LIMCREDEXTRAPOLADO:
				pedido.flPendenteLimCred = ValueUtil.VALOR_SIM;
				break;
			case TipoPendenciaPedido.CDTIPOPENDENCIA_CONDPAGTO_DIFERENTEPADRAO:
			case TipoPendenciaPedido.CDTIPOPENDENCIA_CONDPAGTO_QTDIASPAGTO_EXTRAPOLADO:
				pedido.flPendenteCondPagto = ValueUtil.VALOR_SIM;
				break;
			default:
				break;
	    	}
    	}
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	Pedido pedidoFilter = new Pedido();
    	if (LavenderePdaConfig.usaFiltroEmpresaListaPedidos()) {
    		pedidoFilter.cdEmpresa = cbEmpresa.getValue();
    	} else {
    		pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	}
    	if (produtoFilter != null) {
    		ItemPedido itemPedidoFilter = new ItemPedido();
    		itemPedidoFilter.cdProduto = produtoFilter.cdProduto;
    		pedidoFilter.itemPedidoFilter = itemPedidoFilter;
    	}
       	pedidoFilter.cdCliente = cdClienteFilter;
       	pedidoFilter.cdTipoPedido = cdTipoPedidoFilter;
       	fillTipoPendenciaPedidoFilter(pedidoFilter);
       	if (!pedidoFilter.isPendenteLimCred()) {
       		pedidoFilter.flPendenteLimCred = StringUtil.getStringValue(ckFiltraPedidoPendenteLimiteCredito.isChecked());
       	}

       	if (inConsultaUltimosPedidos) {
       		pedidoFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
       	}
       	if ((!LavenderePdaConfig.relDiferencasPedido || !inPedidosDifNaoLidos) && ! inConsultaPedidoReqServ) {
       		pedidoFilter.cdStatusPedido = cbStatusPedido.getValue();
       	}
       	//--
       	if (SessionLavenderePda.isUsuarioSupervisor()) {
			if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
				pedidoFilter.supervisorRepFilter = new SupervisorRep();
				pedidoFilter.supervisorRepFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
				pedidoFilter.supervisorRepFilter.cdSupervisor = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				pedidoFilter.cdRepresentante = null;
			} else {
				pedidoFilter.cdRepresentante = cbRepresentante.getValue();
			}
       	} else {
        	pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
       	}
       	
       	if (ValueUtil.isNotEmpty(cdCargaPedidoFilter)) {
       		pedidoFilter.cdCargaPedido = cdCargaPedidoFilter;
       	}
       	if (ValueUtil.isNotEmpty(cdMarcadorFilter)) {
       		pedidoFilter.cdMarcadorFilter = cdMarcadorFilter;
       	}
       	if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
    		pedidoFilter.cdStatusOrcamento = cbStatusOrcamento.getValue();
    	}
       	if ((LavenderePdaConfig.usaPedidoPerdido && ckPedidoPerdido.isChecked())) {
    		pedidoFilter.filtraPedidoPerdido = true;
    	}
        if (LavenderePdaConfig.usaFiltroStatusExcecaoListaPedidos()) {
        	String cdStatusExcecao = cbStatusExcecaoPedido.getSelected();
        	if (ValueUtil.isNotEmpty(cdStatusExcecao)) {
        		pedidoFilter.cdStatusExcecaoList = cdStatusExcecao.split(",");
        	}
	    }
    	//--
        pedidoFilter.cliente = new Cliente();
    	if (LavenderePdaConfig.usaFiltroRedeClienteListaPedidos() && ValueUtil.isNotEmpty(cbRede.getValue())) {
    		pedidoFilter.cliente.includeExtraFilterJoin = true;
    		pedidoFilter.cliente.cdRede = cbRede.getValue();
    	}
    	if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && ValueUtil.isNotEmpty(cbCategoria.getValue())) {
    		pedidoFilter.cliente.includeExtraFilterJoin = true;
    		pedidoFilter.cliente.cdCategoria = cbCategoria.getValue();
    	}
    	if (LavenderePdaConfig.usaFiltroTipoClienteRede && !cbTipoClienteRedeComboBox.isOpcaoTodosSelecionado()) {
    		pedidoFilter.cliente.includeExtraFilterJoin = true;
    		pedidoFilter.cliente.flTipoClienteRede = cbTipoClienteRedeComboBox.isOpcaoRedeSelecionado() ? Cliente.TIPO_REDE : Cliente.TIPO_INDIVIDUAL;
    	}
    	if (dtInitialFilter != null || dtFinalFilter != null) {
    		if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos() && dtEmissaoEntregaTipoFilter.equals(DTENTREGAFILTER)) {
    			pedidoFilter.dtEntregaInicialFilter = dtInitialFilter; 
    			pedidoFilter.dtEntregaFinalFilter = dtFinalFilter; 
    		} else {
    			pedidoFilter.dtEmissaoInicialFilter = dtInitialFilter; 
    			pedidoFilter.dtEmissaoFinalFilter = dtFinalFilter; 
    		}
    	}
    	if (LavenderePdaConfig.usaFiltroOrigemPedidoListaPedidos() && ValueUtil.isNotEmpty(cbOrigemPedido.getValue())) {
    		pedidoFilter.flOrigemPedido = cbOrigemPedido.getValue();
    	}
    	if (LavenderePdaConfig.relDiferencasPedido && ckPedidoDif.isChecked() || inPedidosDifNaoLidos) {
    		pedidoFilter.flPossuiDiferenca = ValueUtil.VALOR_SIM;
    		if (inPedidosDifNaoLidos) {
    			pedidoFilter.notFlTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
    		}
    	}
    	if (LavenderePdaConfig.usaFiltroNfeListaPedidos() && ckFiltraPedidoNfe.isChecked() && LavenderePdaConfig.mostraAbaNfeNoPedido) {
    		pedidoFilter.onlyPedidoComNfe = true;
    	}
    	if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
    		pedidoFilter.nuOrdemCompraClienteLikeFilter = dsFiltroOrdemCompraCliente;
    	}
    	if (LavenderePdaConfig.usaFiltroNumeroPedidoListaPedidos() && ValueUtil.isNotEmpty(edFiltroNuPedido.getValue())) {
    		pedidoFilter.nuPedidoLikeFilter = edFiltroNuPedido.getValue();
    	}
    	if (LavenderePdaConfig.usaPedidoQualquerRepresentanteParaHistoricoCliente && ckFiltraMeusPedidos.isChecked()) {
    		pedidoFilter.filterByMeusPedidos = true;
    	}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && LavenderePdaConfig.isUsaMotivoPendencia()) {
			int nivelLiberacaoFilter = nivelLiberacaoPedidoComboBox != null && nivelLiberacaoPedidoComboBox.getSelectedItem() != null ? (int) nivelLiberacaoPedidoComboBox.getSelectedItem() : 0;
			if (isFiltrandoPedidoPendente(nivelLiberacaoFilter)) {
				pedidoFilter.nuOrdemLiberacaoFilter = nivelLiberacaoFilter;
			}
  	    }
    	return pedidoFilter;
    }

    @Override
    protected Vector getDomainList(BaseDomain domain) throws SQLException {
    	Pedido pedidoFilter = (Pedido)domain;
    	
    	Vector listTemp = getCrudService().findAllByExampleSummary(pedidoFilter);
    	//--
    	if (inConsultaUltimosPedidos && (pedidoConsultaUltimosPedidos != null)) {
    		listTemp.removeElement(pedidoConsultaUltimosPedidos);
    	}
    	//--
    	if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && ckFiltraPedidoDispLiberacao.isChecked()) {
    		listTemp = PedidoService.getInstance().getOnlyPedidoDispLiberacao(listTemp);
    	}
    	if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() && ckFiltraPedidoItensPendentes.isChecked()) {
    		listTemp = PedidoService.getInstance().getOnlyPedidoItensPendentes(listTemp);
    	}
    	if (ckPedidoRecalculoPendente.isChecked()) {
    		listTemp = PedidoService.getInstance().filtraPedidosComRecalculoPendente(listTemp);
    	}
    	if (LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido()) {
    		listTemp = PedidoService.getInstance().getListPedidoSortedByPctRent(listTemp, sortAtributte, ValueUtil.getBooleanValue(sortAsc));
    	}
    	listSize = listTemp.size();
        return listTemp;
    }

	private boolean isFiltrandoPedidoPendente(int nivelLiberacaoFilter) {
		return nivelLiberacaoFilter > 0 && (LavenderePdaConfig.cdStatusPedidoPendenteAprovacao.equalsIgnoreCase(cbStatusPedido.getValue()) || cbStatusPedido.getValue() == null);
	}

    private void updateTotalizadores(Vector list) throws SQLException {
    	double vlTotalItensPedidos = 0.d;
    	double vlTtRentabilidade = 0.d;
    	double vlTotalPeso = 0.d;
    	double qtTotalItemFisico = 0.d;
    	double qtTotalItemFaturamento = 0.d;
    	double vlPctRentabilidade = 0.d;
    	double vlTotalBaseItensPedidos = 0.d;
    	double vlTotalVolume = 0.d;
    	double vlTrocaRecolher = 0.d;
    	double vlTotalPedidoComTributosEDeducoes = 0;
        if (listSize > 0) {
        	Pedido pedido;
            for (int i = 0; i < listSize; i++) {
            	pedido = (Pedido)list.items[i];
            	vlTotalPedidoComTributosEDeducoes += pedido.vlFinalPedidoDescTribFrete;
            	vlTotalPeso += pedido.qtPeso;
            	vlTotalItensPedidos += pedido.vlTotalItens;
            	vlTtRentabilidade += pedido.vlRentabilidade;
            	if (LavenderePdaConfig.isUsaGerenciamentoRentabilidadeComBaseNaRentabilidadeDoPedido()) {
            		vlTotalBaseItensPedidos += pedido.vlTotalBaseItens;
            	}
            	if (LavenderePdaConfig.isMostraColunaQtdNaListaDePedidos()) {
            		qtTotalItemFisico += PedidoService.getInstance().getQtdeItensPedido(pedido);
            	}
            	if (LavenderePdaConfig.isMostraColunaQtdEmbalagensNaListaDePedidos()) {
            		qtTotalItemFaturamento += PedidoService.getInstance().getQtItensFaturamentoPedido(pedido);
            	}
            	if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
            		vlTotalVolume += pedido.vlVolumePedido;
            	}
            	if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
                	vlTrocaRecolher += pedido.vlTrocaRecolher;
            	}
            }
        }
        if ((LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) && LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
        	lvTtPedidoComTributos.setValue(Messages.PEDIDO_LABEL_VL_TOTAL_PEDIDO_TRIBUTOS + " " + StringUtil.getStringValueToInterface(vlTotalPedidoComTributosEDeducoes));
        } else if (LavenderePdaConfig.isUsaCalculoStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
        	lvTtPedidoComTributos.setValue(Messages.PEDIDO_LABEL_VLTOTALPEDIDOST + " " + StringUtil.getStringValueToInterface(vlTotalPedidoComTributosEDeducoes));
        } else if (LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
        	lvTtPedidoComTributos.setValue(Messages.PEDIDO_LABEL_VL_TOTAL_PEDIDO_IPI + " " + StringUtil.getStringValueToInterface(vlTotalPedidoComTributosEDeducoes));
        }
        if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
	        String peso;
	        if (LavenderePdaConfig.isCalculaPesoTotalPedidoMedio()) {
		        peso = MessageUtil.getMessage(Messages.LISTA_PEDIDO_TOTALIZADOR_PESO, StringUtil.getStringValueToInterface(vlTotalPeso / (listSize > 0 ? listSize : 1)));
	        } else {
		        peso = Messages.PEDIDO_LABEL_TOTALPESO + " " + StringUtil.getStringValueToInterface(vlTotalPeso);
	        }
	        lbTotalPeso.setValue(peso);
        }
        if (LavenderePdaConfig.isMostraColunaQtdNaListaDePedidos()) {
        	lbTtQtItemFisico.setValue(Messages.LABEL_TOTAL + " " + LavenderePdaConfig.mostraColunaQtdNaListaDePedidos + " " + StringUtil.getStringValueToInterface(qtTotalItemFisico));
        }
    	if (LavenderePdaConfig.isMostraColunaQtdEmbalagensNaListaDePedidos()) {
    		lbTtQtItemFaturamento.setValue(Messages.LABEL_TOTAL + " " + LavenderePdaConfig.mostraColunaQtdEmbalagensNaListaDePedidos + " " + StringUtil.getStringValueToInterface(qtTotalItemFaturamento));
    	}
    	if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade() || LavenderePdaConfig.isUsaGerenciamentoRentabilidadeComBaseNaRentabilidadeDoPedido()) {
    		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade() && vlTotalItensPedidos > 0) {
    			vlPctRentabilidade = (vlTtRentabilidade / vlTotalItensPedidos) * 100;
    		} else if (vlTotalBaseItensPedidos > 0) {
    			vlPctRentabilidade = (vlTtRentabilidade / vlTotalBaseItensPedidos) * 100;
    		}
    		lbTtRentabilidade.setValue(Messages.RENTABILIDADE_PERC_RESUMIDO + " " + StringUtil.getStringValueToInterface(vlPctRentabilidade));
    	}
    	if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
    		lbTotalVolume.setText(Messages.PEDIDO_LABEL_TOTAL_VOLUME + " " + StringUtil.getStringValueToInterface(vlTotalVolume, LavenderePdaConfig.nuCasasDecimaisVlVolume));
    	}
    	if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
    		lbTotalTrocas.setText(Messages.PEDIDO_LABEL_TOTAL_TROCA + " " + StringUtil.getStringValueToInterface(vlTrocaRecolher));
    	}
    	sessaoTotalizadores.reposition();
    }

	// @Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		Pedido pedido = (Pedido)domain;
		int cdCorFundoStatusPedido = pedido.statusPedidoPda.cdCorFundo;
		if (cdCorFundoStatusPedido > 0) {
			CorSistema domainFilter = new CorSistema();
			domainFilter.cdCor = cdCorFundoStatusPedido;
			domainFilter.cdEsquemaCor = TemaSistemaLavendereService.getTemaSistemaLavendereInstance().getTemaAtual().cdEsquemaCor;
			domainFilter = (CorSistema) CorSistemaLavendereService.getInstance().findByPrimaryKey(domainFilter);
			if (domainFilter != null) {
				c.setBackColor(Color.getRGB(domainFilter.vlR, domainFilter.vlG, domainFilter.vlB));
			}
		}
		if (LavenderePdaConfig.relDiferencasPedido) {
			if (!pedido.isPossuiPedidoDiferenca() && pedido.isPossuiDiferenca()) {
				c.setBackColor(LavendereColorUtil.COR_FUNDO_LINHA_LISTA_PEDIDOS_DIFERENCA);
			}
   		}
		if (inConsultaUltimosPedidos && LavenderePdaConfig.usaRelUltimosPedidosDoCliente && !(ValueUtil.VALOR_SIM.equals(pedido.flPossuiDiferenca))) {
			if (pedido.isPedidoAberto() || pedido.isPedidoFechado()) {
				hasPedidosRecentes = true;
				c.setBackColor(LavendereColorUtil.COR_PEDIDOS_RECENTES_ABERTOSFECHADOS);
			} else if ((LavenderePdaConfig.nuDiasPedidoRecenteRelUltimosPedidos > 0) && ValueUtil.isNotEmpty(pedido.dtEmissao) && pedido.dtEmissao.isAfter(dtEmissaoLimite)) {
				hasPedidosRecentes = true;
				c.setBackColor(LavendereColorUtil.COR_PEDIDOS_RECENTES_NAOABERTOSEFECHADOS);
			}
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && !pedido.isPedidoBonificacao() && pedido.isPedidoPendente()) {
			if (SessionLavenderePda.isUsuarioLiberaItemPendente() && LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao() && pedido.isPedidoItemPendente()) {
				c.setBackColor(LavendereColorUtil.COR_PEDIDO_PENDENTE_ITENS_PENDENTES);
			}
			if (UsuarioDescService.getInstance().isProximoUsuarioLiberarPedido(pedido) && pedido.isPendente()) {
				c.setBackColor(LavendereColorUtil.COR_PEDIDOS_DISP_LIBERACAO_GRID_FUNDO);
			}
		}
		if (LavenderePdaConfig.isUsaBloqueioEnvioPedidoProdutoRestrito() && pedido.isPedidoRestrito()) {
			c.setBackColor(LavendereColorUtil.COR_PRODUTO_RESTRITO);
		}
		if (pedido.isPedidoConsignado() && pedido.isPedidoConsignadoVencido()) {
			c.setBackColor(LavendereColorUtil.COR_PEDIDO_CONSIGNACAO_VENCIDO);
		}
		if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && pedido.nuSolAutorizacaoPendenteOuNaoAutorizada > 0) {
			c.setBackColor(LavendereColorUtil.COR_FUNDO_PEDIDO_NAO_AUTORIZADO);
		}

	}

    private boolean isFiltroStatusPedidoAberto() {
    	return LavenderePdaConfig.cdStatusPedidoAberto.equals(cbStatusPedido.getValue());
    }
    
    private boolean isFiltroStatusPedidoConsignado() {
    	return LavenderePdaConfig.cdStatusPedidoConsignado.equals(cbStatusPedido.getValue());
    }
    
    private boolean isFiltroStatusTodosSelecionado() {
    	return cbStatusPedido.getSelectedIndex() == 0;
    }
    
    private boolean isShowRepLista() {
    	return SessionLavenderePda.isUsuarioSupervisor();
    }

    public void loadDefaultFilters() throws SQLException {
    	if (LavenderePdaConfig.usaFiltroOrigemPedidoListaPedidos()) {
    		cbOrigemPedido.setSelectedIndex(0);
    	}
    	if (LavenderePdaConfig.usaFiltroEmpresaListaPedidos()) {
    		cbEmpresa.setSelectedIndex(0);
    	}
    	if (LavenderePdaConfig.usaFiltroTipoClienteRede) {
    		cbTipoClienteRedeComboBox.setSelectedIndex(0);
    	}
    	if (LavenderePdaConfig.usaFiltroRedeClienteListaPedidos()) {
    		cbRede.setSelectedIndex(0);
    	}
    	if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
    		cbCategoria.setSelectedIndex(0);
    	}
    	//Status Pedido
		cbStatusPedido.setValue(LavenderePdaConfig.cdStatusPedidoAberto);
		if (LavenderePdaConfig.isNaoPermiteManterPedidosAbertos() && !inRelPedidosAbertos) {
			cbStatusPedido.setValue(LavenderePdaConfig.cdStatusPedidoFechado);
		}
		if (inConsultaUltimosPedidos || inPedidosDifNaoLidos) {
			cbStatusPedido.setSelectedIndex(0);
		}
		if (ValueUtil.isNotEmpty(cdCargaPedidoFilter)) {
    		cbStatusPedido.setSelectedIndex(0);
    	}
		if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido() && LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && !inConsultaUltimosPedidos) {
			cbStatusPedido.setValue(LavenderePdaConfig.cdStatusPedidoPendenteAprovacao);
		}
		if (inPedidoDiferentesAposSync) {
			cbStatusPedido.setSelectedIndex(0);
			ckPedidoDif.setChecked(true);
		}
		setComponentsFecharPedidoVisible();
		//Status Representante
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		cbRepresentante.setSelectedIndex(0);
    		if (SessionLavenderePda.getRepresentante().cdRepresentante != null) {
    			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
    			if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
    				cbRepresentante.setSelectedIndex(0);
    			} else {
    				flListInicialized = true;
    			}
    		}
    		lastRepSelected = cbRepresentante.getSelectedIndex();
    	}
    	if (inConsultaUltimosPedidos && (LavenderePdaConfig.nuDiasPedidoRecenteRelUltimosPedidos > 0)) {
    		dtEmissaoLimite = DateUtil.getCurrentDate();
    		dtEmissaoLimite.advance(-LavenderePdaConfig.nuDiasPedidoRecenteRelUltimosPedidos);
    	}
    }
    
    @Override
    public void onFormExibition() throws SQLException {
    	super.onFormExibition();
		if (isAllSelected && SessionLavenderePda.isUsuarioSupervisor()) {
			SessionLavenderePda.setRepresentanteByUsuarioPdaRep();
		}
    }

    @Override
    protected void onFormStart() throws SQLException {
		UiUtil.add(barBottomContainer, btFecharPedidos, 1);
    	if (LavenderePdaConfig.usaBotaoNovoPedidoNaListaPedidos && (!inConsultaUltimosPedidos || (cadClienteMenuForm != null))) {
    		UiUtil.add(barBottomContainer, btNovoPedido, 5);
		}
    	if (LavenderePdaConfig.usaAgrupamentoPedidoEmConsignacao) {
    		UiUtil.add(barBottomContainer, btAgruparPedidoConsignacao, 2);
    	}
    	if (! inPedidosDifNaoLidos && ! inConsultaPedidoReqServ) {
    		//-- TOTALIZADORES
    		addTotalizadores();
        	//-- FILTROS
    		if (LavenderePdaConfig.usaFiltroEmpresaListaPedidos()) {
    			UiUtil.add(this, new LabelName(Messages.EMPRESA_NOME_ENTIDADE), cbEmpresa, getLeft(), getNextY());
    		}
    		if (SessionLavenderePda.isUsuarioSupervisor()) {
    			UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
    		}
    		if (LavenderePdaConfig.usaFiltroOrigemPedidoListaPedidos()) {
    			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_FLORIGEM), cbOrigemPedido, getLeft(), getNextY());
    		}
    		if (LavenderePdaConfig.usaFiltroTipoClienteRede && !inConsultaUltimosPedidos) {
    			UiUtil.add(this, new LabelName(Messages.TIPO_CLIENTE_REDE_TITULO), cbTipoClienteRedeComboBox, getLeft(), getNextY());
    		}
    		if (LavenderePdaConfig.usaFiltroRedeClienteListaPedidos() && !inConsultaUltimosPedidos) {
    			UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRede, getLeft(), getNextY());
    		}
    		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !inConsultaUltimosPedidos) {
    			UiUtil.add(this, new LabelName(Messages.CATEGORIA_NOME_ENTIDADE), cbCategoria, getLeft(), getNextY());
    		}
    		if (LavenderePdaConfig.usaFiltroStatusExcecaoListaPedidos()) {
    			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CDSTATUSEXCECAO), cbStatusExcecaoPedido, getLeft(), getNextY());
    		}
    		UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CDSTATUSPEDIDO), getLeft(), getNextY(), PREFERRED, PREFERRED);
    		UiUtil.add(this, btFiltroAvancado, getRight(), AFTER, UiUtil.getControlPreferredHeight());
    		UiUtil.add(this, cbStatusPedido, getLeft(), SAME, FILL - UiUtil.BASE_MARGIN_GAP - btFiltroAvancado.getWidth() - WIDTH_GAP);
    		if (LavenderePdaConfig.usaPedidoAbertoComIndicacaoOrcamento) {
    			UiUtil.add(this, new LabelName(Messages.STATUSORCAMENTO_LABEL_COMBO), cbStatusOrcamento, getLeft(), AFTER + HEIGHT_GAP);
    		}
    		if (LavenderePdaConfig.isMarcaPedidoPendenteLimiteCredito()) {
    			UiUtil.add(this, ckFiltraPedidoPendenteLimiteCredito, getLeft(), AFTER + HEIGHT_GAP);
    		}
        	//--
    		if (!inConsultaUltimosPedidos) {
        		if (LavenderePdaConfig.relDiferencasPedido) {
        			UiUtil.add(this, ckPedidoDif, getLeft(), getNextY());
        		}
        	}
    		if (LavenderePdaConfig.usaPedidoPerdido && !inConsultaUltimosPedidos) {
    			if (LavenderePdaConfig.relDiferencasPedido) {
    				UiUtil.add(this, ckPedidoPerdido, getLeft() + ckPedidoDif.getX2(), ckPedidoDif.getY());
    			} else {
    				UiUtil.add(this, ckPedidoPerdido, getLeft(), getNextY());
    			}
    		}
    		if (LavenderePdaConfig.usaRecalculoValoresDosPedidos) {
    			UiUtil.add(this, ckPedidoRecalculoPendente, getLeft(), getNextY());
    		}
    		if (LavenderePdaConfig.usaFiltroNumeroPedidoListaPedidos()) {
    			UiUtil.add(this, new LabelName(Messages.NUPEDIDO_LABEL_NUPEDIDO), edFiltroNuPedido, getLeft(), getNextY());
    		}
        	//-- LISTA DE PEDIDOS
			UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight() - sessaoTotalizadores.getHeight());
        } else {
            UiUtil.add(this, listContainer, LEFT, TOP + HEIGHT_GAP, FILL, FILL);
        }
        addItensOnButtonMenu();
    	UiUtil.add(barBottomContainer, bmOpcoes, 3);
    }

	private void addTotalizadores() {
		UiUtil.add(this, sessaoTotalizadores, LEFT, BOTTOM, FILL, UiUtil.getLabelPreferredHeight());
		boolean addRight = false;
		boolean antesTtTroca = false;
		int gap = listContainer.getTotalizerGap();
		if (ValueUtil.isNotEmpty(LavenderePdaConfig.usaPedidoExclusivoTrocaRecolher)) {
			UiUtil.add(sessaoTotalizadores, lbTotalTrocas, RIGHT - gap, SAME, PREFERRED, PREFERRED);
			antesTtTroca = true;
		}
		if (LavenderePdaConfig.isMostraVlComTributosNasListasDePedidoEItens() && !LavenderePdaConfig.ocultaInfosValoresPedido) {
			if (LavenderePdaConfig.isUsaCalculaStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido || LavenderePdaConfig.isUsaCalculoIpiItemPedido()) {
				addControlTotalizador(lvTtPedidoComTributos, addRight, antesTtTroca, gap);
				if (!antesTtTroca) {
					addRight = !addRight;
				}
				antesTtTroca = false;
			}
		}
		if (LavenderePdaConfig.isUsaGerenciamentoRentabilidade() || LavenderePdaConfig.isUsaGerenciamentoRentabilidadeComBaseNaRentabilidadeDoPedido()) {
			addControlTotalizador(lbTtRentabilidade, addRight, antesTtTroca, gap);
			if (!antesTtTroca) {
				addRight = !addRight;
			}
			antesTtTroca = false;
		}
		if (LavenderePdaConfig.isConfigCalculoPesoPedido()) {
			addControlTotalizador(lbTotalPeso, addRight, antesTtTroca, gap);
			if (!antesTtTroca) {
				addRight = !addRight;
			}
			antesTtTroca = false;
		}
		if (LavenderePdaConfig.isMostraColunaQtdNaListaDePedidos()) {
			addControlTotalizador(lbTtQtItemFisico, addRight, antesTtTroca, gap);
			if (!antesTtTroca) {
				addRight = !addRight;
			}
			antesTtTroca = false;
		}
		if (LavenderePdaConfig.isMostraColunaQtdEmbalagensNaListaDePedidos()) {
			addControlTotalizador(lbTtQtItemFaturamento, addRight, antesTtTroca, gap);
			if (!antesTtTroca) {
				addRight = !addRight;
			}
			antesTtTroca = false;
		}
		if (LavenderePdaConfig.isUsaCalculoVolumeItemPedido()) {
			addControlTotalizador(lbTotalVolume, addRight, antesTtTroca, gap);
			if (!antesTtTroca) {
				addRight = !addRight;
			}
			antesTtTroca = false;
		}
		sessaoTotalizadores.resizeHeight();
		sessaoTotalizadores.resetSetPositions();
		sessaoTotalizadores.setRect(LEFT, BOTTOM - barBottomContainer.getHeight(), FILL, sessaoTotalizadores.getHeight() + HEIGHT_GAP);
	}

	private void addControlTotalizador(LabelTotalizador label, boolean addRight, boolean antesTtTroca, int gap) {
		UiUtil.add(sessaoTotalizadores, label, addRight ? RIGHT - gap : LEFT + gap, addRight || antesTtTroca ? SAME : AFTER + HEIGHT_GAP, PREFERRED, PREFERRED);
	}

	@Override
    public void visibleState() {
    	super.visibleState();
    	setComponentsFecharPedidoVisible();
    	barBottomContainer.setVisible(! inPedidosDifNaoLidos && ! inConsultaPedidoReqServ);
    	listContainer.btResize.setVisible(!inPedidosDifNaoLidos && ! inConsultaPedidoReqServ);
    	btAgruparPedidoConsignacao.setVisible(isFiltroStatusPedidoConsignado());
    }

    public void validateFilterRepresentante() {
	    if (cbRepresentante.getSelectedIndex() == -1){
		    cbRepresentante.setSelectedIndex(lastRepSelected);
	    }
    }

    public void filter() throws SQLException {
		validateFilterRepresentante();
		if(lastRepSelected != cbRepresentante.getSelectedIndex() && SessionLavenderePda.isUsuarioSupervisor()){
			dsClienteFilter = dsClienteFilter != null ? dsClienteFilter : FiltroPedidoAvancadoForm.LABEL_TODOS;
			cdClienteFilter = StringUtil.getStringValue(cdClienteFilter);
		}
	    lastRepSelected = cbRepresentante.getSelectedIndex();
		list();
    }

    @Override
    public void list() throws SQLException {
    	if (flListInicialized) {
    		loadMarcadoresHash();
			listContainer.sbV.clear();
	   		super.list();
    	}
    }

    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
    	marcadoresHash = null;
    	if (! inPedidosDifNaoLidos && ! inConsultaPedidoReqServ) {
    		updateTotalizadores(domainList);
    	}
    }

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == cbStatusPedido) {
	    			cbStatusPedidoChange();
	    		} else if (event.target == cbOrigemPedido || event.target == cbEmpresa || event.target == cbTipoClienteRedeComboBox || event.target == ckFiltraPedidoPendenteLimiteCredito 
	    				|| event.target == cbStatusOrcamento || event.target == ckPedidoRecalculoPendente || event.target == cbRede || event.target == cbCategoria) {
	    			filter();
	    		} else if (event.target == btFiltroAvancado) {
	    			btFiltroPedidoClick();
	    		} else if (event.target == btFecharPedidos) {
    				btFecharPedidosClick();
    			} else if (event.target == ckPedidoDif) {
    				ckPedidoDifClick();
    			} else if (event.target == ckPedidoPerdido) {
    				ckPedidoPerdidoClick();
    			} else if (event.target == cbRepresentante) {
    				cbRepresentanteChange();
    			} else if (event.target == btAgruparPedidoConsignacao) {
    				btAgruparPedidosConsignadosClick();
    			} else if (event.target == btNovoPedido) {
    				btNovoPedidoClick();
	    		} else if (event.target == cbStatusExcecaoPedido) {
					cbStatusExcecaoPedidoChange();
	    		}
	    		break;
	    	}
	    	case EditIconEvent.PRESSED: {
		    	if (event.target == edFiltroNuPedido) {
		    		filter();
		    	}
		    	break;	
	    	}
	    	case ButtonOptionsEvent.OPTION_PRESS: {
	    		if (event.target == bmOpcoes) {
	    			if (bmOpcoes.selectedItem.equals(MENU_UTILITARIO_CALCULADORA)) {
	    				(new Calculator()).popup();
	    			} else if (bmOpcoes.selectedItem.equals(MENU_RESUMO_PEDIDOS)) {
	    				btResumoPedidosClick();
	    			}
	    		}
	    		break;
	    	}
	    	case KeyEvent.SPECIAL_KEY_PRESS : {
	    		if (event.target == edFiltroNuPedido && ((KeyEvent)event).isActionKey()) {
	    			filter();
	    		}
	    	}
    	}
    }
    
    private void btNovoPedidoClick() throws SQLException {
    	if (!PedidoService.getInstance().validaUsuarioEmissaoPedido()) return;
		if (LavenderePdaConfig.apresentaPopUpErroEnvioPedidoNovoPedido && ErroEnvioService.getInstance().hasPedidosErroEnvioServidor()) {
			ListErroEnvioWindow listErroEnvioWindow = new ListErroEnvioWindow(this);
			listErroEnvioWindow.popup();
			if (!listErroEnvioWindow.closedByBtFechar) {
				return;
			}
		}
    	if (cadClienteMenuForm != null) {
			close();
			cadClienteMenuForm.novoPedido();
		} else {
			new ListClienteWindow(true, false, false).popup();
		}
    }

	public void btFiltroPedidoClick() throws SQLException {
		FiltroPedidoAvancadoForm avancadoForm = new FiltroPedidoAvancadoForm(inConsultaUltimosPedidos, getRepresentanteSelecionado(), cbStatusPedido.getValue());
		avancadoForm.edDateFinal.setValue(dtFinalFilter);
		avancadoForm.edDateInitial.setValue(dtInitialFilter);
		avancadoForm.edDsCliente.setValue(ValueUtil.isEmpty(dsClienteFilter) ? FiltroPedidoAvancadoForm.LABEL_TODOS : dsClienteFilter);
		avancadoForm.cdClienteFilter = this.cdClienteFilter;
		avancadoForm.cdTipoPedidoFilter = this.cdTipoPedidoFilter;
		avancadoForm.cdTipoPendenciaPedidoFilter = this.cdTipoPendenciaPedidoFilter;
		avancadoForm.fillProdutoFilters(produtoFilter);
		if (ValueUtil.isNotEmpty(cdTipoPedidoFilter)) {
			avancadoForm.cbTipoPedido.setSelectedItems(cdTipoPedidoFilter);
		}
		if (ValueUtil.isNotEmpty(cdTipoPendenciaPedidoFilter)) {
			avancadoForm.cbTipoPendenciaPedido.setSelectedItems(cdTipoPendenciaPedidoFilter);
		}
		if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
			avancadoForm.cbDtEmissaoEntrega.setSelectedItem(dtEmissaoEntregaTipoFilter);
		}
		if (LavenderePdaConfig.usaFiltroNfeListaPedidos() && LavenderePdaConfig.mostraAbaNfeNoPedido) {
			avancadoForm.ckFiltraPedidoNfe.setChecked(ckFiltraPedidoNfe.isChecked());
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento) {
			if (LavenderePdaConfig.isUsaMotivoPendencia()) {
				if (cbStatusPedido.getValue() == null || LavenderePdaConfig.cdStatusPedidoPendenteAprovacao.equalsIgnoreCase(cbStatusPedido.getValue())) {
					avancadoForm.nivelLiberacaoPedidoComboBox.setSelectedItem(nivelLiberacaoPedidoComboBox.getSelectedItem());
				} else {
					avancadoForm.nivelLiberacaoPedidoComboBox.setSelectedIndex(-1);
				}
			} else {
				avancadoForm.ckFiltraPedidoDispLiberacao.setChecked(ckFiltraPedidoDispLiberacao.isChecked());
			}
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			avancadoForm.ckFiltraPedidoItensPendentes.setChecked(ckFiltraPedidoItensPendentes.isChecked());
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			avancadoForm.cbCargaPedido.setSelectedItem(cdCargaPedidoFilter);
		}
		if (LavenderePdaConfig.usaFiltroMarcadorListaPedidos()) {
			avancadoForm.cbMarcador.setValue(cdMarcadorFilter);
			if (ValueUtil.isNotEmpty(cdMarcadorFilter)) {
				avancadoForm.cbMarcador.setValue(cdMarcadorFilter);
			} else {
				avancadoForm.cbMarcador.setSelectedIndex(0);
			}
		}
		if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
			avancadoForm.edFiltroOrdemCompraCliente.setText(dsFiltroOrdemCompraCliente);
		}
		if (LavenderePdaConfig.usaPedidoQualquerRepresentanteParaHistoricoCliente) {
			avancadoForm.ckFiltraMeusPedidos.setChecked(ckFiltraMeusPedidos.isChecked());
		}
		avancadoForm.popup();
		if (avancadoForm.isFiltered) {
			if (SessionLavenderePda.isUsuarioSupervisor() && (SessionLavenderePda.getRepresentante().cdRepresentante != null)
					&& (cbRepresentante.getValue() != null)) {
				cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);

			}
			dsClienteFilter = avancadoForm.edDsCliente.getValue();
			cdClienteFilter = avancadoForm.cdClienteFilter;
			produtoFilter = avancadoForm.produtoFilter;
			dtInitialFilter = avancadoForm.edDateInitial.getValue();
			dtFinalFilter = avancadoForm.edDateFinal.getValue();
			if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
				cdTipoPedidoFilter = avancadoForm.cdTipoPedidoFilter;
			}
			if (LavenderePdaConfig.isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido()) {
				cdTipoPendenciaPedidoFilter = avancadoForm.cdTipoPendenciaPedidoFilter;
			}
			validateFilterData(dtInitialFilter, dtFinalFilter);
			if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
				dtEmissaoEntregaTipoFilter = (String) avancadoForm.cbDtEmissaoEntrega.getSelectedItem();
			}
			if (LavenderePdaConfig.usaFiltroNfeListaPedidos() && LavenderePdaConfig.mostraAbaNfeNoPedido) {
				ckFiltraPedidoNfe.setChecked(avancadoForm.ckFiltraPedidoNfe.isChecked());
			}
			if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento) {
				if (LavenderePdaConfig.isUsaMotivoPendencia()) {
					nivelLiberacaoPedidoComboBox.setSelectedItem(avancadoForm.nivelLiberacaoPedidoComboBox.getSelectedItem());
				} else {
					ckFiltraPedidoDispLiberacao.setChecked(avancadoForm.ckFiltraPedidoDispLiberacao.isChecked());
				}
			}
			if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
				ckFiltraPedidoItensPendentes.setChecked(avancadoForm.ckFiltraPedidoItensPendentes.isChecked());
			}
			if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
				cdCargaPedidoFilter = avancadoForm.cbCargaPedido.getValue();
			}
			if (LavenderePdaConfig.usaFiltroMarcadorListaPedidos()) {
				cdMarcadorFilter = avancadoForm.cbMarcador.getValue();
			}
			if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
				dsFiltroOrdemCompraCliente = avancadoForm.edFiltroOrdemCompraCliente.getValue();
			}
			if (LavenderePdaConfig.usaPedidoQualquerRepresentanteParaHistoricoCliente) {
				ckFiltraMeusPedidos.setChecked(avancadoForm.ckFiltraMeusPedidos.isChecked());
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && ValueUtil.isNotEmpty(cdClienteFilter)) {
				cbCategoria.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaFiltroRedeClienteListaPedidos() && ValueUtil.isNotEmpty(cdClienteFilter)) {
				cbRede.setSelectedIndex(0);
			}
			filter();
		}
	}

	private void cbRepresentanteChange() throws SQLException {
		if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
		} else {
			SessionLavenderePda.setRepresentante(null);
		}
		flListInicialized = true;
		filter();
	}

    public void validateFilterData(Date dateInitial, Date dateFinal) {
    	if (!ValueUtil.isEmpty(dateInitial) && !ValueUtil.isEmpty(dateFinal)) {
    		if (dateInitial.isAfter(dateFinal)) {
    			String[] param = {StringUtil.getStringValue(dateFinal), StringUtil.getStringValue(dateInitial)};
                throw new ValidationException(MessageUtil.getMessage(Messages.PEDIDO_MSG_DATA_INICIAL_MAIOR, param));
    		}
    	}
		lastDateFilter[0] = dateInitial;
		lastDateFilter[1] = dateFinal;
    }

    public BaseDomain getSelectedDomain() throws SQLException {
    	return (Pedido)getCrudService().findByRowKeyDyn(getSelectedRowKey());
    }

    public void detalhesClick() throws SQLException {
    	if (inPedidosDifNaoLidos) {
			btDiferencasPedidoclick();
    	} else if (inConsultaPedidoReqServ) {
    		getParentWindow().unpop();
    	} else {
    		SessionLavenderePda.cdTabelaPreco = "";
			try {
				UiUtil.showProcessingMessage();
				BaseDomain domain = getSelectedDomain();
				if (domain == null) {
					UiUtil.showErrorMessage(Messages.PEDIDO_MSG_PEDIDO_NAO_ENCONTRADO);
					return;
				}
				Pedido pedido = (Pedido)domain;
				setSelectedRepresentanteNaSessao();
				if (LavenderePdaConfig.usaFiltroEmpresaListaPedidos() && !SessionLavenderePda.cdEmpresa.equalsIgnoreCase(pedido.cdEmpresa)) {
					SessionLavenderePda.cdEmpresaOld = SessionLavenderePda.cdEmpresa;
					SessionLavenderePda.setCliente(((Pedido) domain).getCliente());
					EmpresaService.getInstance().changeEmpresaSessao(pedido.cdEmpresa);
					setBaseCrudCadForm(new CadPedidoForm());
				}
				CadPedidoForm cadPedido = (CadPedidoForm) getBaseCrudCadForm();
				cadPedido.inOnlyConsultaItens = inConsultaUltimosPedidos;
				if (LavenderePdaConfig.exibeAbaTotalizadoresPedidoCapaPedido()) {
					cadPedido.setTotalizadoresPedidoForm(new TotalizadoresPedidoForm());
					cadPedido.getTotalizadoresPedidoForm().setPedido(pedido);
				}
				if (LavenderePdaConfig.isUsaSolicitacaoAutorizacao() && SolAutorizacaoService.getInstance().hasSolAutorizacaoPendentePedido(pedido, null)) {
					SolAutorizacaoService.getInstance().recebeAtualizacao();
					pedido.solAutorizacaoPedidoCache.reloadCaches(pedido);
				}
				PedidoService.getInstance().updatePedidoUtilizaRentabilidade(pedido);
				cadPedido.edit(pedido);
				pedido.fromPedidoSemCliente = pedido.cliente.isClienteDefaultParaNovoPedido() && !pedido.cliente.isNovoCliente();
				show(cadPedido);
				recalcularRentabilidadePedido(pedido, RecalculoRentabilidadeOptions.RECALCULO_RENTABILIDADE_REACESSO_PEDIDO);
			} finally {
				UiUtil.unpopProcessingMessage();
			}
    	}
    }

    private boolean recalcularRentabilidadePedido(final Pedido pedido, final RecalculoRentabilidadeOptions option) throws SQLException {
    	return MargemRentabService.getInstance().recalcularRentabilidadePedidoAbertoSeNecessario(pedido, option);
    }

    private void setSelectedRepresentanteNaSessao() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
        	String cdRep = ((Pedido)getSelectedDomain()).cdRepresentante;
       		isAllSelected = ValueUtil.isEmpty(cbRepresentante.getValue());
        	if (isAllSelected || !cbRepresentante.getValue().equalsIgnoreCase(cdRep)) {
        		Representante rep =  RepresentanteService.getInstance().getRepresentanteById(cdRep);
        		if (rep != null) {
        			SessionLavenderePda.setRepresentante(rep);
        		}
        	}
        }
    }

    private void cbStatusPedidoChange() throws SQLException {
    	setComponentsFecharPedidoVisible();
    	setComponentsPedidoConsignadoVisible();
    	if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && LavenderePdaConfig.isUsaMotivoPendencia()) {
    		if (cbStatusPedido.getValue() == null) {
			    nivelLiberacaoPedidoComboBox.setSelectedIndex(-1);
		    } else if (SessionLavenderePda.nuOrdemLiberacaoUsuario > 0) {
			    nivelLiberacaoPedidoComboBox.setSelectedItem(SessionLavenderePda.nuOrdemLiberacaoUsuario);
		    }
	    }
    	filter();
    }

	private void cbStatusExcecaoPedidoChange() throws SQLException {
		cbStatusExcecaoPedido.saveStatusExcecaoSelecionado();
		filter();
	}

    private void setComponentsFecharPedidoVisible() {
    	boolean statusPedidoAberto = isFiltroStatusPedidoAberto();
        btFecharPedidos.setVisible(statusPedidoAberto
        		&& ! inConsultaUltimosPedidos
        		&& ! inPedidosDifNaoLidos
        		&& ! LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()
        		&& ! inConsultaPedidoReqServ);
        listContainer.setCheckable(statusPedidoAberto
        		&& !inConsultaUltimosPedidos
        		&& !LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()
        		&& ! inConsultaPedidoReqServ);
    }

    private void setComponentsPedidoConsignadoVisible() {
    	boolean statusPedidoConsignado = isFiltroStatusPedidoConsignado();
    	listContainer.setCheckable((isFiltroStatusPedidoAberto() && !LavenderePdaConfig.usaGeracaoNfePedidoAposFechamento()) || statusPedidoConsignado);
    	btAgruparPedidoConsignacao.setVisible(statusPedidoConsignado);
    }

    private void ckPedidoDifClick() throws SQLException {
    	filter();
    }

    private void ckPedidoPerdidoClick() throws SQLException {
    	filter();
    }

    private void btDiferencasPedidoclick() throws SQLException {
		Pedido pedido = (Pedido)getSelectedDomain();
		if (ValueUtil.VALOR_SIM.equals(pedido.flPossuiDiferenca)) {
			if (!LavenderePdaConfig.isShowDifPedido() && !LavenderePdaConfig.isShowDifItemPedido()) {
				UiUtil.showErrorMessage(Messages.CONFIG_PARAM_1766_ERRADA);
				return;
			}
			RelDiferencasPedidoWindow relDiferencasPedidoForm = new RelDiferencasPedidoWindow(pedido);
			relDiferencasPedidoForm.popup();
		} else {
			UiUtil.showInfoMessage(Messages.PEDIDO_MSG_NAO_POSSUI_DIFERENCAS_PEDIDO);
		}
    }

    private void btResumoPedidosClick() throws SQLException {
    	if (cbRepresentante.getSelectedIndex() == 0) {
    		UiUtil.showInfoMessage(Messages.MENUREPRESENTANTE_MSG_SELECIONEANTESACAO);
    	} else {
    		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
    		mb.popupNonBlocking();
    		RelResumoPedidosWindow relResumoWindow;
			try {
				relResumoWindow = new RelResumoPedidosWindow(getDomainList(getDomainFilter()));
				relResumoWindow.setFilters(cbStatusPedido.getValueString(), dsClienteFilter, cbOrigemPedido.getValue(), StringUtil.getStringValue(dtInitialFilter), StringUtil.getStringValue(dtFinalFilter));
				relResumoWindow.setDefaultRect();
			} finally {
				mb.unpop();
			}
			relResumoWindow.popup();
    	}
    }

    private void btFecharPedidosClick() throws SQLException {
		//Confirma o fechamento dos pedidos
		Vector pedidoList = new Vector();
		//Pega todos os pedido selecionados
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		for (int i = 0; i < gridSize; i++) {
			Pedido pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(listContainer.getId(checkedItens[i]));
			if (LavenderePdaConfig.usaValorTotalPedidoFaixaDias && pedido.necessitaRecalculoPontuacao()) {
				PontuacaoConfigService.getInstance().reloadPontuacaoPedido(pedido, null);
			}

			if (LavenderePdaConfig.usaPedidoProdutoCritico && LavenderePdaConfig.permiteApenasUmItemPedidoProdutoCritico && pedido.isPedidoCritico() && pedido.getQtItensLista() > 1) {
				UiUtil.showErrorMessage(Messages.PEDIDOPRODUTOCRITICO_ERRO_QT_ITENS_FECHAMENTO_LOTE);
				return;
			}
			if (LavenderePdaConfig.isGeraParcelasPorTipoCondPgto()) {
				PedidoService.getInstance().findParcelaPedidoList(pedido);
			}
			pedidoList.addElement(pedido);
		}
		//Verifica se há pedidos selecionados
		if (pedidoList.size() == 0) {
			UiUtil.showInfoMessage(Messages.PEDIDO_MSG_NENHUM_PEDIDO_SELECIONADO);
			return;
		}
		//Confirma o fechamento
		boolean res = UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_CONFIRM_FECHARPEDIDOS, pedidoList.size()));
		if (res) {
			if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoFechamentoPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorRegiao()) {
				throw new ValidationException(Messages.TRANSPORTADORAREG_ERRO_FECHAMENTO_EM_LOTE);
			}
			if (LavenderePdaConfig.emiteAlertaUsuarioVerificacaoCondPagto) {
				boolean resultado = UiUtil.showWarnConfirmYesNoMessage(Messages.PEDIDO_MSG_VERIFICACAO_CONDPAGTO);
				if (!resultado) {
					return;
				}
			}
			if (LavenderePdaConfig.usaFotoPedidoNoSistema && LavenderePdaConfig.usaVerificacaoFotoPedidoPresentePedido == 2) {
				if (!UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_SEM_FOTO_CONFIRM_REPRESENTANTE_LIST)) {
					return;
				}
			}
			//--
			if (LavenderePdaConfig.usaConfirmacaoEntregaPedidoDiaNaoUtil) {
				boolean dtEntregaPedidoConfirmado = false;
				for (int i = 0; i < pedidoList.size(); i++) {
					Pedido pedido = (Pedido) pedidoList.items[i];
					if (ValueUtil.isNotEmpty(pedido.dtEntrega) && !pedido.isLiberadoEntrega()) {
						if (PedidoService.getInstance().isPedidoDtEntregaFinalSemanaFeriado(pedido.dtEntrega)) {
							if (!LavenderePdaConfig.isLiberaSenhaDiaEntregaPedido()) {
								if (dtEntregaPedidoConfirmado) {
									pedido.flLiberadoEntrega = ValueUtil.VALOR_SIM;
								} else {
									if (UiUtil.showConfirmYesNoMessage(Messages.PEDIDO_MSG_CONFIRMACAO_LIBERACAO_DTENTREGA_PEDIDOS)) {
										dtEntregaPedidoConfirmado = true;
										pedido.flLiberadoEntrega = ValueUtil.VALOR_SIM;
									} else {
										return;
									}
								}
							}
						}
					}
				}
			}
			if (LavenderePdaConfig.isExibeComboMenuInferior()) {
				for (int i = 0; i < pedidoList.size(); i++) {
					Pedido pedido = (Pedido) pedidoList.items[i];
					if (LavenderePdaConfig.isExibeComboMenuInferior() && ComboService.getInstance().isPedidoComComboForaVigencia(pedido)) {
						UiUtil.showErrorMessage(Messages.MSG_COMBO_VENCIDA_LISTA_PEDIDO);
						return;
					}
				}
			}
			verificaNecessidadeGeracaoPdfOffline(pedidoList);
			//--
			boolean enviaDadosServidor = false;
			if (LavenderePdaConfig.usaEnvioPedidoServidorSemConfirmacao) {
				enviaDadosServidor = true;
			} else if (LavenderePdaConfig.sugereEnvioAutomaticoPedido) {
				if (UiUtil.showConfirmYesNoMessage(Messages.MSG_ENVIAR_PEDIDOS_SERVIDOR)) {
					enviaDadosServidor = true;
				}
			}
			SessionLavenderePda.houveErroPedidosRestrito = false;
			String pedidosNaoFechados = "";
			LoadingBoxWindow msg = UiUtil.createProcessingMessage();
			msg.popupNonBlocking();
			try {
				String cdSessao = PedidoService.getInstance().generateIdGlobal();
				//Fecha os pedido selecionados
				pedidosNaoFechados = PedidoService.getInstance().fecharPedidos(pedidoList, false, true, true, enviaDadosServidor);
				if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
					Vector cargasAnalisadasList = new Vector();
					for (int i = 0; i < pedidoList.size(); i++) {
						Pedido pedido = (Pedido) pedidoList.items[i];
						if (pedido.isPedidoFechado() && cargasAnalisadasList.indexOf(pedido.cdCargaPedido) == -1) {
							cargasAnalisadasList.addElement(pedido.cdCargaPedido);
							if (PedidoService.getInstance().findQtPesoTotalPedidosCargaPedido(pedido.cdCargaPedido, null) > LavenderePdaConfig.qtdPesoMinimoCargaPedido) {
								try {
									if (UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.PEDIDO_PESOMINIMO_CARGAPEDIDO_ATINGIDO, pedido.getCargaPedido().toString()))) {
										CargaPedidoService.getInstance().fecharCarga(pedido.getCargaPedido());
										UiUtil.showConfirmMessage(MessageUtil.getMessage(Messages.CARGAPEDIDO_MSG_CARGA_FECHADA_SUCESSO, pedido.getCargaPedido().toString()));
									}
								} catch (Throwable ex) {
									String[] params = {pedido.getCargaPedido().toString(), ex.getMessage()};
									UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.CARGAPEDIDO_MSG_CARGA_NAO_FOI_FECHADA, params));
								}
							}
						}
					}
				}
				PedidoUiUtil.mostraAvisosAposFechamentoPedidoPelaLista(pedidoList);
				verificaErroGeracaoPdfOffline(pedidoList);
				if (enviaDadosServidor) {
					if (LavenderePdaConfig.usaEnvioPedidoBackground) {
						for (int i = 0; i < PedidoService.getInstance().pedidoEnvioServidorList.size(); i++) {
							Pedido pedido = (Pedido) PedidoService.getInstance().pedidoEnvioServidorList.items[i];
							if (LavenderePdaConfig.validaSugestaoVendaMultiplasEmpresas > 0) {
								PedidoUiUtil.enviaPedidosClienteOutrasEmpresas(cdSessao, pedido);
							} else {
								EnviaDadosThread.getInstance().enviaPedido(cdSessao, pedido);
								EnviaDadosThread.getInstance().enviaVisita(cdSessao, VisitaService.getInstance().findVisitaByPedido(pedido));
							}
						}
						PedidoService.getInstance().pedidoEnvioServidorList.removeAllElements();
					}
				}
				//--
			} finally {
				msg.unpop();
				mostraProblemasGeracaoPdfOffline(pedidoList);
				if (ValueUtil.isEmpty(pedidosNaoFechados)) {
					UiUtil.showConfirmMessage(Messages.PEDIDO_MSG_FECHARPEDIDOS_SUCESSO);
				} else {
					RelFechamentoPedidosWindow relFechamentoPedidosForm = new RelFechamentoPedidosWindow(pedidosNaoFechados);
					relFechamentoPedidosForm.popup();
				}
				if (!LavenderePdaConfig.usaEnvioPedidoBackground && SessionLavenderePda.houveErroPedidosRestrito) {
					UiUtil.showWarnMessage(Messages.PRODUTO_RESTRITO_NAO_ENVIADO);
				}
				fecharVisitasPelaListaPedidos(pedidoList, pedidosNaoFechados);
				if (LavenderePdaConfig.isAvisaPedidoAbertoFechadoFecharPedido()) {
					UiMessagesUtil.mostraMensagemPedidosAbertos();
				}
				filter();
			}
		}
    }
    
    private void fecharVisitasPelaListaPedidos(Vector pedidoList, String pedidosNaoFechados) throws SQLException {
    	if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
    		int size = pedidoList.size();
    		for (int i = 0; i < size; i++) {
    			Pedido pedido = (Pedido) pedidoList.items[i];
    			if (!isPedidoComProblemas(pedido, pedidosNaoFechados)) {
    				if (SessionLavenderePda.visitaAndamento != null) {
    					if (LavenderePdaConfig.registraSaidaClienteAoFecharPedido) {
    						Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
    						if (ValueUtil.valueEquals(pedido.getCliente().cdCliente, visitaEmAndamento.cdCliente)) {
    							CadClienteMenuForm.btRegistrarSaidaClick(visitaEmAndamento, true);
    							VisitaPedidoService.getInstance().updateVisitaPedidoParaEnvio(visitaEmAndamento, pedido);
    						}
    					}
    				} else {
    					Visita visita = VisitaService.getInstance().findVisitaByPedido(pedido);
    					if (ValueUtil.isNotEmpty(visita.cdVisita)) {
    						visita.flVisitaPositivada = ValueUtil.VALOR_SIM;
    						VisitaService.getInstance().fechaVisita(visita, false);
    						VisitaPedidoService.getInstance().updateVisitaPedidoParaEnvio(visita, pedido);
    					}
    				}
    			}
    		}
    	}
    }
    
    private boolean isPedidoComProblemas(Pedido pedido, String pedidosNaoFechados) {
    	if (ValueUtil.isNotEmpty(pedidosNaoFechados)) {
    		String[] pedidosErros = StringUtil.split(pedidosNaoFechados, '#');
    		for (int i = 0; i < pedidosErros.length; i++) {
    			String[] dados = StringUtil.split(pedidosErros[i], '*');
    			String rowKey = dados[0];
    			if (pedido.getRowKey().equals(rowKey)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	MENU_UTILITARIO_CALCULADORA = Messages.MENU_UTILITARIO_CALCULADORA;
    	bmOpcoes.addItem(MENU_UTILITARIO_CALCULADORA);
    	if (LavenderePdaConfig.relResumoPedidos) {
    		MENU_RESUMO_PEDIDOS = Messages.BOTAO_RESUMO_PEDIDOS;
    		bmOpcoes.addItem(MENU_RESUMO_PEDIDOS);
    	}
    }

    protected void voltarClick() throws SQLException {
		if (PedidoService.getInstance().isCountPedidosAbertosMaiorPermitido() && !inConsultaUltimosPedidos) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_PEDIDOS_EM_ABERTO_SISTEMA, LavenderePdaConfig.getQtdPedidosPermitidosManterAbertos()));
			return;
		}
		//--
    	super.voltarClick();
    	boolean isNotFromPedido = !(prevContainer instanceof CadPedidoForm) && !(prevContainer instanceof CadItemPedidoForm);
    	if (((inConsultaUltimosPedidos && LavenderePdaConfig.usaEscolhaEmpresaPedido)) && ValueUtil.isNotEmpty(SessionLavenderePda.cdEmpresaOld) && isNotFromPedido) {
    		EmpresaService.getInstance().changeEmpresaSessao(SessionLavenderePda.cdEmpresaOld);
    		SessionLavenderePda.cdEmpresaOld = null;
    	}
    	if (prevContainer instanceof CadCargaPedidoForm) {
    		CadCargaPedidoForm cadCargaPedidoForm = (CadCargaPedidoForm) prevContainer;
    		cadCargaPedidoForm.atualizaTela();
		}
    }

    public void onFormClose() throws SQLException {
		SessionLavenderePda.clearPedidoProcessandoFechamentoList();
    	setBaseCrudCadForm(null);
    	super.onFormClose();
		if (inConsultaUltimosPedidos && SessionLavenderePda.cadItemPedidoFormConsultaUltimosPedidosInstance != null) {
			PedidoService.getInstance().findItemPedidoList(SessionLavenderePda.pedidoConsultaUltimosPedidos, true);
			SessionLavenderePda.cadItemPedidoFormConsultaUltimosPedidosInstance.pedido = SessionLavenderePda.pedidoConsultaUltimosPedidos;
			SessionLavenderePda.cadItemPedidoFormConsultaUltimosPedidosInstance = null;
			SessionLavenderePda.pedidoConsultaUltimosPedidos = null;
		}
    }

    private void btAgruparPedidosConsignadosClick() throws SQLException {
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		if (gridSize < 2) {
			UiUtil.showWarnMessage(Messages.PEDIDO_ERRO_AGRUPAR_PEDIDOS_NUMERO_INSUFICIENTE);
			return;
		}
		List<Pedido> pedidoList = new ArrayList<Pedido>();
		for (int i = 0; i < gridSize; i++) {
			Pedido pedido = (Pedido)PedidoService.getInstance().findByRowKeyDyn(listContainer.getId(checkedItens[i]));
			if (pedido != null) {
				PedidoService.getInstance().findItemPedidoList(pedido);
				if (LavenderePdaConfig.usaDevolucaoPedidosEmConsignacao) {
					pedido.pedidoConsignacaoDevolucaoList = PedidoConsignacaoService.getInstance().findPedidoConsignacaoTipoDevolucaoList(pedido);
				}
				pedidoList.add(pedido);
			}
		}
		Pedido newPedido;
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		try {
			mb.popupNonBlocking();
			newPedido = PedidoConsignacaoService.getInstance().getPedidoConsignadoAgrupado(pedidoList);
		} finally {
			mb.unpop();
		}
		if (newPedido != null) {
			CadPedidoForm cadPedido = (CadPedidoForm) getBaseCrudCadForm();
			cadPedido.edit(newPedido);
			show(cadPedido);
		}
    }
    
    @Override
    protected void resizeListToFullScreen() {
    	int fullScreenListHeight = barBottomContainer.isVisible() ? FILL - barBottomContainer.getHeight() : FILL;
    	fullScreenListHeight = sessaoTotalizadores.isVisible() ? fullScreenListHeight - sessaoTotalizadores.getHeight() : fullScreenListHeight;
		listContainer.setRect(0, getTop(), FILL, fullScreenListHeight);
		listContainer.initUI();
		listContainer.repaintContainers();    	
    }
    
    private void verificaNecessidadeGeracaoPdfOffline(Vector pedidoList) {
    	if (!LavenderePdaConfig.isSugereGeracaoPdfNoFechamento() && !LavenderePdaConfig.isGeraPdfOfflineAuto()) {
    		return;
    	}
		if (LavenderePdaConfig.isGeraPdfOfflineAuto() || UiUtil.showConfirmYesNoMessage(Messages.RELATORIO_PDF_OFFLINE_SUGERE_GERACAO_LOTE)) {
			int pedidoListSize = pedidoList.size();
			for (int i = 0; i < pedidoListSize; i++) {
				Pedido pedido = (Pedido) pedidoList.items[i];
				pedido.geraPdfOfflineFechamentoLote = true;
			}
		}
	}

    private void verificaErroGeracaoPdfOffline(Vector pedidoList) {
    	if (!LavenderePdaConfig.isSugereGeracaoPdfNoFechamento() && !LavenderePdaConfig.isGeraPdfOfflineAuto()) {
    		return;
    	}
    	int pedidoListSize = pedidoList.size();
		for (int i = 0; i < pedidoListSize; i++) {
			Pedido pedido = (Pedido) pedidoList.items[i];
			if (ValueUtil.isEmpty(pedido.msgProblemaGeracaoPdfOffline)) {
				continue;
			}
			if (pedidoProblemaGeracaoPdfOffline == null) {
				pedidoProblemaGeracaoPdfOffline = new Vector(pedidoListSize);
			}
			pedidoProblemaGeracaoPdfOffline.addElement(pedido);
    	}
		
	}

	private void mostraProblemasGeracaoPdfOffline(Vector pedidoList) {
		if (ValueUtil.isNotEmpty(pedidoProblemaGeracaoPdfOffline) && UiUtil.showWarnConfirmYesNoMessage(Messages.RELATORIO_PDF_OFFLINE_PEDIDOS_LOTE_PROBLEMAS)) {
			RelProblemaGeracaoPdf RelProblemaGeracaoPdf = new RelProblemaGeracaoPdf(Messages.RELATORIO_PDF_OFFLINE_ERRO_TITULO_REL_LOTE_PROBLEMAS, pedidoProblemaGeracaoPdfOffline);
			RelProblemaGeracaoPdf.popup();
			pedidoProblemaGeracaoPdfOffline = null;
		}
	}
	
	@Override
	public void singleClickInList() throws SQLException {
		Pedido pedido = (Pedido) getSelectedDomain();
		if (pedido.isPedidoAberto() && !PedidoService.getInstance().validaUsuarioEmissaoPedido()) {
			return;
		}
		super.singleClickInList();
	}

	private String[][] getColsSort() {
		int lenght = LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido() ? 4 : 3;
		String[][] colsSort = new String[lenght][2];
		colsSort[0] = new String[] {Messages.CODIGO, "NUPEDIDO"};
		colsSort[1] = new String[] {Messages.PEDIDO_LABEL_EMISSAO, DTEMISSAO_DEFAULT_ORDER_COLUMNS};
		colsSort[2] = new String[] {Messages.PEDIDO_LABEL_VLTOTALPEDIDO, "VLTOTALPEDIDO"};
		if (LavenderePdaConfig.isApresentaRentabilidadeNaListaPedido()) {
			colsSort[3] = new String[] {Messages.PEDIDO_LABEL_PERCENTUAL_RENTABILIDADE, "VLRENTABILIDADE"};
		}
		return colsSort;
	}

	@Override
	protected Image[] getIconsLegend(BaseDomain domain) throws SQLException {
		Image[] iconsLegend = PedidoService.getInstance().getIconsMarcadores((Pedido)domain, marcadoresHash, marcadoresMap, iconSize);
		return ValueUtil.isNotEmpty(iconsLegend) ? iconsLegend : super.getIconsLegend(domain);
	}

	public String getRepresentanteSelecionado() {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
				return "";
			} else {
				return cbRepresentante.getValue();
			}
		} else {
			return SessionLavenderePda.getRepresentante().cdRepresentante;
		}
	}

	private void loadMarcadoresHash() throws SQLException {
		if (LavenderePdaConfig.usaMarcadorPedido) {
			marcadoresHash = MarcadorService.getInstance().buscaMarcadoresVigentesHash(Marcador.ENTIDADE_MARCADOR_PEDIDO);
		}
	}

	private void loadCbStatusExecaoSelect() throws SQLException {
		String checkedItems = ConfigInternoService.getInstance().getVlConfigInternoGeral(ConfigInterno.ULTIMOSTATUSEXCECAOSELECIONADO);
		String[] values = StringUtil.split(checkedItems, ',');
		int size = values.length;
		if (size > 0) {
			StatusPedidoPda statusPedidoPda = new StatusPedidoPda();
			for (int i = 0; i < size; i++) {
				statusPedidoPda.cdStatusPedido = values[i];
				cbStatusExcecaoPedido.select(statusPedidoPda);
			}
		}
	}

	protected String getTipoPendenciaPedidoMessage(Pedido pedido) {
		String tipoPendencia = null;
		if (pedido.isPendenteCondPagto()) {
			tipoPendencia = Messages.COND_PAGTO;
		}
		if (pedido.isPendenteLimCred()) {
			tipoPendencia = (tipoPendencia == null ? ValueUtil.VALOR_NI 
					: tipoPendencia + FrameworkMessages.CONJUCAO_ADITIVA_E + ValueUtil.VALOR_EMBRANCO) + Messages.CLIENTE_LABEL_VLLIMITECREDITO;  
		}
		return tipoPendencia == null ? ValueUtil.VALOR_NI : Messages.TIPOPENDENCIAPEDIDO_PENDENTEPOR + tipoPendencia;
	}
}
