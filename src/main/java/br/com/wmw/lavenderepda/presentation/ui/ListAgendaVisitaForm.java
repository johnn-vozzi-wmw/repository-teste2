package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ApplicationException;
import br.com.wmw.framework.exception.ConnectionException;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.gps.GpsData;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.event.SortButtonEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.sync.ParamsSync;
import br.com.wmw.framework.sync.transport.http.HttpConnectionManager;
import br.com.wmw.framework.sync.transport.http.HttpSync;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.framework.config.ListContainerConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.StatusAgenda;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.TipoAgenda;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.StatusRentCliService;
import br.com.wmw.lavenderepda.business.service.SupervisorRepService;
import br.com.wmw.lavenderepda.business.service.TipoAgendaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CategoriaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DiaAgendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusAgendaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoCadastroClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoClienteRedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.SortButtonsContainer;
import br.com.wmw.lavenderepda.sync.SyncManager;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.sql.Types;
import totalcross.sys.Vm;
import totalcross.ui.Container;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListAgendaVisitaForm extends LavendereCrudListForm {

	protected static final String FILTRO_PERIODO_DATA = "1";
	protected static final String FILTRO_STATUS_AGENDA = "2";
	protected static final String FILTRO_MARCADORES = "3";
	protected static final String FILTRO_TIPO_CLIENTE_REDE = "4";
	protected static final String FILTRO_TIPO_CADASTRO_CLIENTE = "5";
	protected static final String FILTRO_REPRESENTANTE = "6";
	protected static final String FILTRO_CLIENTE_SEM_PEDIDO = "7";
	protected static final String FILTRO_CARACTERISTICA_CLIENTE = "8";
	protected static final String FILTRO_FORNECEDOR = "9";
	private static final int VALOR_LIMITE_GOOGLE_MAPS = 10;

	protected TipoCadastroClienteComboBox cbTipoCadastroClienteComboBox;
	protected StatusAgendaComboBox cbStatusAgenda;
	protected EditText diaSemana;
	protected DiaAgendaComboBox cbDiaAgendaComboBox;
	protected EditDate edDtInicial;
	protected EditDate edDtFinal;
	private EditText diaSemanaInicial;
	private EditText diaSemanaFinal;
	private Date currentDate;
	public CheckBoolean ckClientesAtrasados;
	protected RepresentanteSupervComboBox cbRepresentante;
	protected MarcadorComboBox cbMarcador;
	protected FornecedorComboBox cbFornecedor;
	private boolean isAllSelectedRepresentante;
	protected LabelName lbRep;
	private ButtonAction btTransferirAgendas;
	private ButtonAction btNovaAgendaVisita;
	private ButtonAction btMotivoVisitaMultiplasVisitas;
	private ButtonAction btMapaTotalCross;
	private ButtonAction btMapaGoogleMaps;
	private ButtonAction btRedefinirOrdem;
	private ButtonAction btCalcularRota;
	protected TipoClienteRedeComboBox cbTipoClienteRedeComboBox;
	private BaseButton btFiltro;
	private BaseButton btFiltroAvancado;
	public String dsFiltroDefault;
	private ButtonOptions bmOpcoes;
	protected RedeComboBox cbRede;
	protected CategoriaComboBox cbCategoria;

	private Vector tipoAgendaList;
	private Vector agendaVisitaList;
	private AgendaVisita agendaSelecionada;
	private Map<String, Image> indicadoresMap;
	private int iconSize;
	private boolean clienteCoordenadaFilter;
	private boolean visibleSortButtonsInList;
	private int countReordenarClick = 0;
	private int[] countImagesItem;
	private int lastSelectedIndex;

	private HashMap<String, Marcador> marcadoresCliHash;

    public ListAgendaVisitaForm() throws SQLException {
        super(Messages.AGENDAVISITA_NOME_ENTIDADE);
        currentDate = DateUtil.getCurrentDate();
        cbDiaAgendaComboBox = new DiaAgendaComboBox(Messages.DATA_LABEL_DATA, DateUtil.getCurrentDate());
	    cbDiaAgendaComboBox.setID("cbDiaAgendaComboBox");
		cbTipoCadastroClienteComboBox = new TipoCadastroClienteComboBox();
        cbStatusAgenda = new StatusAgendaComboBox();
	    cbStatusAgenda.setID("cbStatusAgenda");
        diaSemana = new EditText("", 15);
        diaSemana.setEnabled(false);
        cbDiaAgendaComboBox.setEnabled(true);
		singleClickOn = true;
		setBaseCrudCadMenuForm(new CadClienteMenuForm());
		cbRepresentante = new RepresentanteSupervComboBox();
		cbMarcador = new MarcadorComboBox(Marcador.ENTIDADE_MARCADOR_CLIENTE);
		lbRep = new LabelName(Messages.REPRESENTANTE_LABEL_REPRESENTANTE_RESUMIDO);
		ckClientesAtrasados = new CheckBoolean(MessageUtil.getMessage(Messages.MSG_CLIENTE_SEM_PEDIDOS, LavenderePdaConfig.nuDiasFiltroClienteSemPedido));
		btTransferirAgendas = new ButtonAction(Messages.BT_TRANSFERIR_AGENDAS, "images/transferencia.png");
		btNovaAgendaVisita = new ButtonAction(Messages.NOVA_AGENDA_VISITA, "images/addagenda.png");
		btMotivoVisitaMultiplasVisitas = new ButtonAction(Messages.BOTAO_MOTIVO_VISITA_MULTIPLAS_VISITAS, "images/motVisita.png");
		btMapaTotalCross = new ButtonAction(Messages.BOTAO_MAPA_CLIENTE, "images/mapatotalcross.png");
		btMapaGoogleMaps = new ButtonAction(Messages.BOTAO_ROTA, "images/mapagoogle.png");
		btRedefinirOrdem = new ButtonAction(Messages.BOTAO_REDEFINIR_ORDEM, "images/reordenar.png");
		btCalcularRota = new ButtonAction(Messages.BOTAO_CALCULAR_ROTA, "images/rota.png");
		tipoAgendaList = new Vector();
		if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
			tipoAgendaList = TipoAgendaService.getInstance().findAll();
		}
		cbTipoClienteRedeComboBox = new TipoClienteRedeComboBox();
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			edDtInicial = new EditDate();
			edDtInicial.setValue(DateUtil.getCurrentDate());
			edDtFinal = new EditDate();
			edDtFinal.setValue(DateUtil.getCurrentDate());
			diaSemanaInicial = new EditText("", 15);
		    diaSemanaInicial.setEnabled(false);
		    diaSemanaFinal = new EditText("", 15);
		    diaSemanaFinal.setEnabled(false);
		}
		bmOpcoes = new ButtonOptions();
		btFiltro = new BaseButton("", UiUtil.getColorfulImage("images/search.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		//--
		constructorListContainer();
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) {
			indicadoresMap = new HashMap<>();
			resizeIconsLegend = false;
			useLeftTopIcons = true;
			iconSize = new LabelValue().getPreferredHeight();
		}
		cbRede = new RedeComboBox();
		cbCategoria = new CategoriaComboBox();
		cbFornecedor = new FornecedorComboBox();
		btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
    }

    private void constructorListContainer() {
    	configListContainer(LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita ? "DTAGENDA" : "CDCLIENTE");
		listContainer = new GridListContainer(getItemCount(), 2);
		int position = LavenderePdaConfig.exibeNomeFantasiaListaClientes() ? 5 : 3;
		setListColSort();
		if (LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
			listContainer.setColPosition(position, RIGHT);
			listContainer.setColPosition(position+2, RIGHT);
			if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita && SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
				listContainer.setColPosition(7, RIGHT);
			}
		} else {
			listContainer.setColPosition(position, RIGHT);
			if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita && SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
				listContainer.setColPosition(position+2, RIGHT);
			}
		}
		listContainer.setCheckable(LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento && SessionLavenderePda.isUsuarioSupervisor());
    }

	private void setListColSort() {
		List<String[]> colsSort = new ArrayList<>(5);
		colsSort.addAll(Arrays.asList(new String[][]{{Messages.PRODUTO_LABEL_CODIGO, "CDCLIENTE"}, {Messages.AGENDAVISITA_LABEL_HRAGENDA, "HRAGENDA"}}));
		if (LavenderePdaConfig.exibeSequenciaAgenda()) {
			colsSort.add(new String[]{Messages.AGENDAVISITA_LABEL_SEQUENCIA, "NUSEQUENCIA"});
		}
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			colsSort.add(new String[]{Messages.AGENDAVISITA_LABEL_DATA, "DTAGENDA"});
		}
		if (LavenderePdaConfig.usaOrdenacaoPersonalizada() || LavenderePdaConfig.usaMelhorRotaSistema) {
			colsSort.add(new String[]{Messages.AGENDAVISITA_LABEL_ORDEM_MANUAL, "NUORDEMMANUAL"});
		}
		listContainer.setColsSort(colsSort.toArray(new String[][]{}));
	}
    
    @Override
	protected void configListContainer(String defaultSortAtributte, String deafultSortAscend) {
		if (ValueUtil.isEmpty(sortAtributte) || sortAtributte.equalsIgnoreCase("DTAGENDA")) {
			sortAtributte = defaultSortAtributte;
		}
		sortAsc = ListContainerConfig.getDefautOrder(getConfigClassName());
		if (ValueUtil.isEmpty(sortAsc)) {
			sortAsc = deafultSortAscend;
		}
		if (LavenderePdaConfig.usaMelhorRotaSistema) {
			sortAtributte = "NUORDEMMANUAL";
			sortAsc = ValueUtil.VALOR_SIM;
		}
	}
	
	private int getItemCount() {
		int itemCount = 4;
		itemCount += LavenderePdaConfig.exibeNomeFantasiaListaClientes() ? 2 : 0;
		itemCount += LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema() ? 2 : 0;
		itemCount += LavenderePdaConfig.usaTipoAgendaNaAgendaVisita ? 1 : 0;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			itemCount += LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor ? 4 : 2;
		}
		itemCount += !LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes() && LavenderePdaConfig.usaApresentacaoFixaTicketMedioListaAgendaVisita ? 1 : 0;
		itemCount += LavenderePdaConfig.usaOrdenacaoPersonalizada() ? 1 : 0;
		return itemCount;
	}

   //@Override
    public void onFormExibition() throws SQLException {
    	super.onFormExibition();
    	listarMantendoScroll();
    	if (SessionLavenderePda.getAgenda() != null) {
    		updateCurrentRecord(SessionLavenderePda.getAgenda());
    	}
    	if (isAllSelectedRepresentante && SessionLavenderePda.isUsuarioSupervisor()) {
    		SessionLavenderePda.setRepresentante(new Representante());
    	}
    }

	private void listarMantendoScroll() throws SQLException {
		if (listContainer != null) {
			list();
			if (lastSelectedIndex > 0) {
				Container previousContainer = listContainer.getContainer(lastSelectedIndex - 1);
				if (previousContainer == null && lastSelectedIndex > 1) previousContainer = listContainer.getContainer(lastSelectedIndex - 2);
				if (previousContainer != null) {
					Vm.safeSleep(100);
					listContainer.setScrollPos(previousContainer.getY());
				}
			}
		}
	}

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return AgendaVisitaService.getInstance();
    }

    protected BaseDomain getDomainFilter() throws SQLException {
    	return new AgendaVisita();
    }

    //@Override
    protected Vector getDomainList(BaseDomain domainFilter) throws SQLException {
    	AgendaVisita agendaFilter = (AgendaVisita) domainFilter;
    	if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) {
    		agendaFilter.cdMarcador = cbMarcador.getValue();
		}
    	if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
    		agendaFilter.cdRede = cbRede.getValue();
    	}
    	if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
    		agendaFilter.cdCategoria = cbCategoria.getValue();
    	}
    	if (LavenderePdaConfig.usaClienteSemPedidoFornecedor) {
    		agendaFilter.cdFornecedor = cbFornecedor.getValue();
    	}
    	agendaFilter.dsFiltro = edFiltro.getValue();
    	agendaFilter.clienteCoordenadaFilter = clienteCoordenadaFilter;
    	return getAgendaVisitaList(domainFilter, agendaFilter, cbDiaAgendaComboBox.getValue());
    }

	private Vector getAgendaVisitaList(BaseDomain domainFilter, AgendaVisita agendaFilter, Date dataAgenda) throws SQLException {
		Vector agendaList = new Vector();
		agendaFilter.cdRepresentante = SessionLavenderePda.isUsuarioSupervisor() ? cbRepresentante.getValue() : SessionLavenderePda.getRepresentante().cdRepresentante;
    	String flTipoCadastroCliente = LavenderePdaConfig.usaClienteEmModoProspeccao ? cbTipoCadastroClienteComboBox.getValue() : "";
    	String flTipoClienteRede = LavenderePdaConfig.usaFiltroTipoClienteRede ? cbTipoClienteRedeComboBox.getValue() : "";
		boolean ckClientesAtrasadosChecked = LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && ckClientesAtrasados.isChecked();
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			agendaFilter.filterAVisitar = cbStatusAgenda.getValue() == StatusAgenda.STATUSAGENDA_CDAVISITAR;
			agendaList = AgendaVisitaService.getInstance().findAgendasVisitas(agendaFilter, dataAgenda, cbStatusAgenda.getValue(), ckClientesAtrasadosChecked, flTipoCadastroCliente, edDtInicial.getValue(), edDtFinal.getValue(), flTipoClienteRede);
		} else {
			agendaList = AgendaVisitaService.getInstance().findAgendasVisitas(agendaFilter, dataAgenda, cbStatusAgenda.getValue(), ckClientesAtrasadosChecked, flTipoCadastroCliente, flTipoClienteRede);
		}
		if (SessionLavenderePda.isUsuarioSupervisor() && SupervisorRepService.getInstance().isCargaMultiplosSupervisores()) {
			agendaList = getAgendaListBySupervisor(agendaList);
		}
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
			if (isOrdenaPorHoraDataClienteOuSeq(domainFilter)) {
				SortUtil.qsortString(agendaList.items, 0, agendaList.size() - 1, ValueUtil.VALOR_SIM.equals(domainFilter.sortAsc));
			}
		}
		if(LavenderePdaConfig.usaOrdenacaoPersonalizada() && agendaList.size() > 0 && ((AgendaVisita)agendaList.items[0]).nuOrdemManual != 0 && !isOrdenaPorHoraDataClienteOuSeq(domainFilter) ) {	//Se true significa que já foi ordenado manualmente em algum momento
			SortUtil.qsortInt(agendaList.items, 0, agendaList.size() - 1, true);
		}
					
    	return agendaVisitaList = agendaList;
	}

	private boolean isOrdenaPorHoraDataClienteOuSeq(BaseDomain domainFilter) {
		return "DTAGENDA".equals(domainFilter.sortAtributte) || "HRAGENDA".equals(domainFilter.sortAtributte) || "CDCLIENTE".equals(domainFilter.sortAtributte) || "NUSEQUENCIA".equals(domainFilter.sortAtributte);
	}

	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		AgendaVisita agendaVisita = (AgendaVisita)domain;
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente && ValueUtil.isNotEmpty(agendaVisita.cliente.cdStatusRentCli)) {
			c.setBackColor(StatusRentCliService.getInstance().getStatusRentListColor(agendaVisita.cliente.cdEmpresa, agendaVisita.cliente.cdStatusRentCli));
		}
		if (LavenderePdaConfig.isUsaAlertaBloqueioClienteSemPedido() || LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0) {
			int nuDiasSemPedidoCliente = ValueUtil.getIntegerValue(ClienteService.getInstance().getClienteColumn(agendaVisita.cdEmpresa, agendaVisita.cdRepresentante, agendaVisita.cdCliente, "nuDiasSemPedido"));
			if (nuDiasSemPedidoCliente > 0) {
				if (((LavenderePdaConfig.nuDiasAlertaClienteSemPedido > 0) && (nuDiasSemPedidoCliente >= LavenderePdaConfig.nuDiasAlertaClienteSemPedido)) ||
								((LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido > 0) && (nuDiasSemPedidoCliente >= LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido)) ||
								((LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0) && (nuDiasSemPedidoCliente >= LavenderePdaConfig.nuDiasFiltroClienteSemPedido))) {
					c.setBackColor(LavendereColorUtil.COR_CLIENTE_NU_DIAS_SEM_PEDIDO_EXTRAPOLADO_BACK);
				}
			}
		}
		if (LavenderePdaConfig.destacaClienteAtendidoMes && agendaVisita.cliente.isAtendido()) {
			c.setBackColor(LavendereColorUtil.COR_CLIENTE_ATENDIDO_MES);
		}
    	if (LavenderePdaConfig.grifaClienteAtrasado && StringUtil.getStringValue(Cliente.FLSTATUSCLIENTE_ATRASADO).equals(c.getItem(getPositionColumnStatusCliente()))) {
			c.setBackColor(LavendereColorUtil.COR_CLIENTE_ATRASADO_GRID_FUNDO);
	    }
    	if (LavenderePdaConfig.grifaClienteBloqueado && StringUtil.getStringValue(Cliente.FLSTATUSCLIENTE_BLOQUEADO).equals(c.getItem(getPositionColumnStatusCliente()))) {
			c.setBackColor(LavendereColorUtil.COR_CLIENTE_BLOQUEADO_BACK);
    	}
    	if (LavenderePdaConfig.destacaClienteBloqueadoPorAtrasoNaLista && StringUtil.getStringValue(Cliente.FLSTATUSCLIENTE_BLOQUEADO_POR_ATRASO).equals(c.getItem(getPositionColumnStatusCliente()))) {
    		c.setBackColor(LavendereColorUtil.COR_CLIENTE_STATUS_BLOQUEIO_POR_ATRASO_FUNDO);
    	}
    	if (agendaVisita.cliente.isModoDeProspeccao()) {
    		c.setBackColor(LavendereColorUtil.COR_CLIENTE_PROSPECTS);
    	}
    	if (agendaVisita.getVisita().isVisitaEmAndamento()) {
    		c.setBackColor(LavendereColorUtil.COR_CLIENTE_VISITA_ANDAMENTO);
    	}
    	Visita visita = agendaVisita.getVisita();
    	if (visita.isVisitaAgendada() && visita.isAgendaVisita()) {
    		if (LavenderePdaConfig.permiteMultAgendasNoDiaMesmoCliente) {
    			if (ValueUtil.valueEquals(agendaVisita.getRowKey(), visita.cdChaveAgendaOrigem)) {
        			c.setBackColor(visita.isVisitaPositivada() ? LavendereColorUtil.COR_FUNDO_AGENDA_POSITIVADO : LavendereColorUtil.COR_FUNDO_AGENDA_NAO_POSITIVADO);
    			}
    		} else {
    			c.setBackColor(visita.isVisitaPositivada() ? LavendereColorUtil.COR_FUNDO_AGENDA_POSITIVADO : LavendereColorUtil.COR_FUNDO_AGENDA_NAO_POSITIVADO);
    		}
    	}
    	if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) {
    		int color = ClienteService.getInstance().getStyleCliente(agendaVisita.cliente, true);
    		if (color != -1) c.textColor = color;
    	}
    }

	private void addSequenciaAgenda(Vector item, AgendaVisita agendaVisita) {
		if (LavenderePdaConfig.exibeSequenciaAgenda()) {
			item.addElement(MessageUtil.getMessage(Messages.AGENDAVISITA_NUSEQUENCIA, agendaVisita.nuSequencia));
		}
	}
	
    //@Override
    protected String[] getItem(Object domain, Container rigthControl, int countImages) throws SQLException {
        AgendaVisita agendaVisita = (AgendaVisita) domain;
        String nmRazaoSocial = "";
        String flStatusCLiente = ValueUtil.VALOR_NI;
        if (agendaVisita.cliente != null) {
        	nmRazaoSocial = agendaVisita.cliente.nmRazaoSocial;
        	flStatusCLiente = agendaVisita.cliente.getFlStatusCliente();
        }

        Vector item = new Vector(0);
        item.addElement(StringUtil.getStringValue(agendaVisita.cdCliente));
        item.addElement(StringUtil.getStringAbreviada(" - " + StringUtil.getStringValue(nmRazaoSocial), width - ((visibleSortButtonsInList && countReordenarClick < 2 ? rigthControl.getPreferredWidth() + fm.stringWidth(agendaVisita.cdCliente) + (iconSize+WIDTH_GAP) * countImages  + HEIGHT_GAP_BIG * 2 : fm.stringWidth(agendaVisita.cdCliente) + (iconSize+WIDTH_GAP) * countImages))));
        
        
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
			item.addElement(getStringShortenIfNecessary(StringUtil.getStringValue(agendaVisita.cliente.nmFantasia), (int)(width * 0.4), item, rigthControl));
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
        
        if (LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
        	String dsRede = "";
        	String dsCategoria = "";
        	if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
        		if (agendaVisita.cliente != null) {
        			Rede rede = agendaVisita.cliente.getRede();
            		if (rede != null && ValueUtil.isNotEmpty(rede.cdRede)) {
            			dsRede = StringUtil.getStringValue(rede);
            		}
        		}
        	}
        	if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
        		if (agendaVisita.cliente != null) {
        			Categoria categoria = agendaVisita.cliente.getCategoria();
            		if (categoria != null && ValueUtil.isNotEmpty(categoria.cdCategoria)) {
            			dsCategoria = categoria.toString();
            		}
        		}
        	} 
        	item.addElement(getStringShortenIfNecessary(dsRede, (int)(width * 0.4), item, rigthControl));
        	item.addElement(getStringShortenIfNecessary(dsCategoria, (int)(width * 0.4), item, rigthControl));
        }
        String elemento = "";
        if (!LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) {
        	elemento = (StringUtil.getStringValue(agendaVisita.getDescricaoTipoFrequencia(agendaVisita.flTipoFrequencia)));
        } else if (LavenderePdaConfig.usaApresentacaoFixaTicketMedioListaAgendaVisita) {
        	elemento = ("T.Médio: " + Messages.MOEDA + StringUtil.getStringValueToInterface(agendaVisita.cliente.vlTicketMedio));
        }
        addSequenciaAgenda(item, agendaVisita);
        item.addElement(getStringShortenIfNecessary(elemento, (int)(width * 0.4), item, rigthControl));
        item.addElement(getStringShortenIfNecessary((LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && ValueUtil.isNotEmpty(agendaVisita.dtAgenda) ? StringUtil.getStringValue(agendaVisita.dtAgenda + " - " + DateUtil.getDiaSemana(agendaVisita.dtAgenda.getDayOfWeek())) : "") + agendaVisita.getDescricaoHoraAgenda(), (int)(width * 0.4), item, rigthControl));
        if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
        	String tipoAgenda = getDsTipoAgenda(agendaVisita.cdTipoAgenda);
        	item.addElement(getStringShortenIfNecessary(StringUtil.getStringValue(tipoAgenda), (int)(width * 0.4), item, rigthControl));
        }
        if (SessionLavenderePda.isUsuarioSupervisor()) {
        	item.addElement(StringUtil.getStringValue(""));
        	item.addElement(getStringShortenIfNecessary(Messages.AGENDAVISITA_LABEL_REPRESENTANTE + RepresentanteService.getInstance().getDescriptionWithId(agendaVisita.cdRepresentante), (int)(width * 0.4), item, rigthControl));
        }
        if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
        	String dsRepresentanteOriginal = "";
        	if (ValueUtil.isNotEmpty(agendaVisita.cdRepOriginal)) {
        		dsRepresentanteOriginal = Messages.AGENDAVISITA_LABEL_REP_ORIGINAL + RepresentanteService.getInstance().getDescriptionWithId(agendaVisita.cdRepOriginal);
			}
        	item.addElement(StringUtil.getStringValue(""));
        	item.addElement(getStringShortenIfNecessary(StringUtil.getStringValue(dsRepresentanteOriginal), (int)(width * 0.4), item, rigthControl));
        }
        if ((!LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) && LavenderePdaConfig.usaApresentacaoFixaTicketMedioListaAgendaVisita) {
        	item.addElement(getStringShortenIfNecessary(("T.Médio: " + Messages.MOEDA + StringUtil.getStringValueToInterface(agendaVisita.cliente.vlTicketMedio)), (int)(width * 0.4), item, rigthControl));
        }
        item.addElement(StringUtil.getStringValue(flStatusCLiente));
    	return (String[]) item.toObjectArray();
    }
    
    private String getStringShortenIfNecessary(String str, int width, Vector item, Container rigthControl) {
    	if (ValueUtil.isEmpty(str)) return "";
    	return StringUtil.getStringAbreviada(str, width - ((visibleSortButtonsInList && countReordenarClick < 2 && listContainer.getColPosition(item.size()) == RIGHT ? rigthControl.getPreferredWidth() : 0)), listContainer.getFontSubItens());
	}

	protected Image[] getIconsLegend(BaseDomain domain, int index) throws SQLException {
    	Cliente cliente = ((AgendaVisita)domain).cliente;
    	Image[] iconsLegend = ClienteService.getInstance().getIconsIndicadores(cliente, marcadoresCliHash, indicadoresMap, iconSize, Cliente.APRESENTA_LISTAGENDA);
    	countImagesItem[index] = ValueUtil.isNotEmpty(iconsLegend) ? iconsLegend.length : 0;
    	return ValueUtil.isNotEmpty(iconsLegend) ? iconsLegend : super.getIconsLegend(domain);
    }
    
    private int getPositionColumnStatusCliente() {
    	int position = 4;
    	if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
    		position += 2;
    	}
    	if (LavenderePdaConfig.usaTipoAgendaNaAgendaVisita) {
    		position += 1;
    	}
    	if (LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
    		position += 2;
    	}
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		position += 2;
    	}
    	if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
    		position += 2;
    	}
    	if ((!LavenderePdaConfig.isUsaAgendaVisitaBaseadaNaSemanaDoMes()) && LavenderePdaConfig.usaApresentacaoFixaTicketMedioListaAgendaVisita) {
    		position++;
    	}
    	return position;
    }
    
    //@Override
    protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item) listContainer.getSelectedItem();
		return c.id;
    }

    public BaseDomain getSelectedDomain() throws SQLException {
    	if (agendaSelecionada == null) {
    		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
    			agendaSelecionada = (AgendaVisita) agendaVisitaList.items[listContainer.getSelectedIndex()];
    			agendaSelecionada.dtAgendaAtual = agendaSelecionada.dtAgenda;
    		} else {
    			agendaSelecionada = (AgendaVisita) getCrudService().findByRowKey(getSelectedRowKey());
    			if (agendaSelecionada == null) {
    				throw new ValidationException(MessageUtil.getMessage(Messages.AGENDA_VISITA_NAO_ENCONTRADA, getSelectedRowKey()));
    			}
    			agendaSelecionada.dtAgendaAtual = cbDiaAgendaComboBox.getValue();
    		}
    		agendaSelecionada.statusAgendaCdPositivado = cbStatusAgenda.getValue();
    		agendaSelecionada.firstAgenda = listContainer.getSelectedIndex() == 0;
    		agendaSelecionada.cliente = ClienteService.getInstance().getCliente(agendaSelecionada.cdEmpresa, agendaSelecionada.cdRepresentante, agendaSelecionada.cdCliente);
    		if (agendaSelecionada.cliente.cdCliente != null) {
			    agendaSelecionada.cliente.loadStatusCliente();
		    }
    	}
    	return agendaSelecionada;
    }

    public void onFormShow() throws SQLException {
    	super.onFormShow();
    	if (!LavenderePdaConfig.naoObrigaCompletarAgendaDiaAnterior) {
    		Date lastValidDate = AgendaVisitaService.getInstance().getLastValidDay(currentDate);
			if (AgendaVisitaService.getInstance().isHasVisitaPendenteOnDay(lastValidDate)) {
				String mensagem = LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita ? MessageUtil.getMessage(Messages.AGENDA_MSG_VISITAS_PENDENTES_DIA, currentDate) : Messages.AGENDA_MSG_VISITAS_PENDENTES;
				UiUtil.showWarnMessage(mensagem);
			}
    	}
    }

    @Override
    protected void onFormStart() throws SQLException {
    	if (showBarBottomContainer()) {
    		int pos = 5;
    		if (getQtdBotoesAdicionados() <= 5) {
    			if (LavenderePdaConfig.usaCadastroAgendaVisita) {
    				UiUtil.add(barBottomContainer, btNovaAgendaVisita, pos--);
    			}
    			if (LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitas()) {
    				UiUtil.add(barBottomContainer, btMotivoVisitaMultiplasVisitas, pos--);
    			}
    			if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento && SessionLavenderePda.isUsuarioSupervisor()) {
    				UiUtil.add(barBottomContainer, btTransferirAgendas, pos--);
    			}
    			if (LavenderePdaConfig.usaOrdenacaoPersonalizada()) {
    				UiUtil.add(barBottomContainer, btRedefinirOrdem, pos--);
    			}
				if (LavenderePdaConfig.exibeBotaoClientesNoMapa()) {
					UiUtil.add(barBottomContainer, btMapaTotalCross, pos--);
				}
				if (LavenderePdaConfig.exibeBotaoRotaParaClientes()) {
					UiUtil.add(barBottomContainer, btMapaGoogleMaps, pos--);
				}
				if (LavenderePdaConfig.usaMelhorRotaSistema && (VmUtil.isAndroid() || VmUtil.isIOS())) {
					UiUtil.add(barBottomContainer, btCalcularRota, pos--);
				}
    		} else {
    			if (LavenderePdaConfig.usaCadastroAgendaVisita) {
    				UiUtil.add(barBottomContainer, btNovaAgendaVisita, pos--);
    			}
    			if (LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitas()) {
    				UiUtil.add(barBottomContainer, btMotivoVisitaMultiplasVisitas, pos--);
    			}
    			if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento && SessionLavenderePda.isUsuarioSupervisor()) {
    				UiUtil.add(barBottomContainer, btTransferirAgendas, pos == 3 ? 1 : pos--);
    			}
    			if (LavenderePdaConfig.usaOrdenacaoPersonalizada()) {
    				UiUtil.add(barBottomContainer, btRedefinirOrdem, pos == 3 ? 2 : pos--);
    			}
    			addItensOnButtonMenu();
    		}
    	}
    	if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_REPRESENTANTE)) {
    		UiUtil.add(this, lbRep, cbRepresentante, getLeft(), getNextY());
    	}
        if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_PERIODO_DATA)) {
        	UiUtil.add(this, new LabelName(Messages.AGENDAVISITA_LABEL_PERIODO), getLeft(), getNextY());
        	UiUtil.add(this, edDtInicial, getLeft(), getNextY());
        	UiUtil.add(this, diaSemanaInicial, AFTER + WIDTH_GAP, SAME);
        	UiUtil.add(this, edDtFinal, getLeft(), getNextY());
        	UiUtil.add(this, diaSemanaFinal, AFTER + WIDTH_GAP, SAME, FILL - (btFiltro.getPreferredWidth() + UiUtil.BASE_MARGIN_GAP + WIDTH_GAP));
        	UiUtil.add(this, btFiltro, AFTER + WIDTH_GAP, SAME);
        } else {
	        UiUtil.add(this, new LabelName(Messages.DATA_LABEL_DATA), getLeft(), getNextY(), PREFERRED, PREFERRED);
	        UiUtil.add(this, cbDiaAgendaComboBox, getLeft(), AFTER, PREFERRED + WIDTH_GAP_BIG + UiUtil.getControlPreferredHeight());
	        UiUtil.add(this, diaSemana, AFTER + WIDTH_GAP, SAME);
        }
        if (LavenderePdaConfig.usaClienteEmModoProspeccao && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_TIPO_CADASTRO_CLIENTE)) {
			UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_TIPOCADASTRO), cbTipoCadastroClienteComboBox, getLeft(), getNextY());
		}
        if (LavenderePdaConfig.usaFiltroTipoClienteRede && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_TIPO_CLIENTE_REDE)) {
        	UiUtil.add(this, new LabelName(Messages.TIPO_CLIENTE_REDE_TITULO), cbTipoClienteRedeComboBox, getLeft(), getNextY());
        }
        if (LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_STATUS_AGENDA)) {
        	UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_STATUS), cbStatusAgenda, getLeft(), getNextY());
        }
        if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_MARCADORES)) {
			UiUtil.add(this, new LabelName(Messages.MARCADOR_NOME_ENTIDADE), cbMarcador, getLeft(), getNextY());
		}
        if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_CARACTERISTICA_CLIENTE)) {
        	UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRede, getLeft(), getNextY());
        }
        if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_CARACTERISTICA_CLIENTE)) {
        	UiUtil.add(this, new LabelName(Messages.CATEGORIA_NOME_ENTIDADE), cbCategoria, getLeft(), getNextY());
        }
        if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_FORNECEDOR)) {
        	UiUtil.add(this, new LabelName(MessageUtil.getMessage(Messages.CLIENTE_NUDIAS_FORNECEDOR_SEM_PEDIDO, LavenderePdaConfig.nuDiasClienteSemPedidoFornecedor)), cbFornecedor, getLeft(), getNextY());
        }
        if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(FILTRO_CLIENTE_SEM_PEDIDO)) {
        	UiUtil.add(this, ckClientesAtrasados, getLeft(), getNextY());
        }
        int widthEdFiltro = FILL - UiUtil.BASE_MARGIN_GAP;
        widthEdFiltro = !LavenderePdaConfig.isTodosFiltrosNaTelaPrincipalAgendaDeVisitas() ?  widthEdFiltro - (UiUtil.getControlPreferredHeight() + WIDTH_GAP) : widthEdFiltro;
        UiUtil.add(this, edFiltro, getLeft(), getNextY(), widthEdFiltro);
		if (!LavenderePdaConfig.isTodosFiltrosNaTelaPrincipalAgendaDeVisitas()) {
			UiUtil.add(this, btFiltroAvancado, getRight(), SAME, UiUtil.getControlPreferredHeight());
		}
        if (showBarBottomContainer()) {
	        UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight());
	    } else {
	        UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL);
	    }
    }
    
    private int getQtdBotoesAdicionados() {
    	int qtdBotoesAdicionados = 0;
    	if (LavenderePdaConfig.usaCadastroAgendaVisita) {
    		qtdBotoesAdicionados++;
    	}
    	if (LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitas()) {
    		qtdBotoesAdicionados++;
    	}
    	if (LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento && SessionLavenderePda.isUsuarioSupervisor()) {
    		qtdBotoesAdicionados++;
    	}
    	if (LavenderePdaConfig.usaOrdenacaoPersonalizada()) {
    		qtdBotoesAdicionados++;
    	}
    	if (LavenderePdaConfig.exibeBotaoClientesNoMapa()) {
    		qtdBotoesAdicionados++;
    	}
    	if (LavenderePdaConfig.exibeBotaoRotaParaClientes()) {
    		qtdBotoesAdicionados++;
    	}
    	if (LavenderePdaConfig.usaMelhorRotaSistema && (VmUtil.isAndroid() || VmUtil.isIOS())) {
    		qtdBotoesAdicionados++;
    	}
    	return qtdBotoesAdicionados++;
    	
    }
    

	private void addItensOnButtonMenu() throws SQLException {
    	if (LavenderePdaConfig.exibeBotaoClientesNoMapa()) { 
    		bmOpcoes.addItem(Messages.BOTAO_MAPA_CLIENTE);
    	}
    	if (LavenderePdaConfig.exibeBotaoRotaParaClientes()) {
    		bmOpcoes.addItem(Messages.BOTAO_ROTA);
    	}
    	if (LavenderePdaConfig.usaMelhorRotaSistema && (VmUtil.isAndroid() || VmUtil.isIOS())) {
    		bmOpcoes.addItem(Messages.BOTAO_CALCULAR_ROTA);
    	}
    	if (bmOpcoes.size() > 0) UiUtil.add(barBottomContainer, bmOpcoes, 3);
    }
    
    private boolean showBarBottomContainer() {
    	return LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento && SessionLavenderePda.isUsuarioSupervisor()
    		|| LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitas() || LavenderePdaConfig.usaCadastroAgendaVisita
    		|| LavenderePdaConfig.exibeBotaoClientesNoMapa() || LavenderePdaConfig.exibeBotaoRotaParaClientes() || LavenderePdaConfig.usaOrdenacaoPersonalizada() || (LavenderePdaConfig.usaMelhorRotaSistema && (VmUtil.isAndroid() || VmUtil.isIOS()));
    }
    
    private String getRowkeyDaProximaSequencia() throws SQLException {
    	Date dataFilter = DateUtil.getCurrentDate();
    	dataFilter.advance(-1);
    	Vector agendaVisitaList = getAgendaVisitaList(getDomainFilter(), new AgendaVisita(), dataFilter);
    	if (ValueUtil.isEmpty(agendaVisitaList)) {
    		agendaVisitaList = getAgendaVisitaList(getDomainFilter(), new AgendaVisita(), DateUtil.getCurrentDate()); 
    	}
    	if (ValueUtil.isNotEmpty(agendaVisitaList)) {
    		AgendaVisita.sortAttr = "NUSEQUENCIA";
        	SortUtil.qsortInt(agendaVisitaList.items, 0, agendaVisitaList.size() - 1, true);
        	AgendaVisita.sortAttr = "";
        	AgendaVisita agendaVisita = (AgendaVisita) agendaVisitaList.items[0];
        	return agendaVisita.getRowKey();
    	}
    	return null;
    }

    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(showBarBottomContainer());
    }

    public void loadDefaultFilters() throws SQLException {
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
	    	cbRepresentante.setSelectedIndex(0);
			if (ValueUtil.isNotEmpty(SessionLavenderePda.getRepresentante().cdRepresentante)) {
				cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
				if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
					cbRepresentante.setSelectedIndex(0);
				}
			}
    	}
    	if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) {
			cbMarcador.setSelectedIndex(0);
		}
		cbDiaAgendaComboBox.setSelectedIndex(1);
		setTextDiaSemana(DateUtil.getCurrentDate().getDayOfWeek());
		setTextDiaSemanaPeriodo();
		if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
			cbTipoCadastroClienteComboBox.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede) {
			cbTipoClienteRedeComboBox.setSelectedIndex(0);
		}
        cbStatusAgenda.setSelectedIndex(1);
        edFiltro.setText(ValueUtil.isEmpty(dsFiltroDefault) ? ValueUtil.VALOR_NI : dsFiltroDefault);
        if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
        	cbRede.setSelectedIndex(0);
        }
        if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
        	cbCategoria.setSelectedIndex(0);
        }
        if (LavenderePdaConfig.usaClienteSemPedidoFornecedor) {
        	cbFornecedor.setSelectedIndex(0);
        }
    }
    
    @Override
    protected void afterList(Vector domainList) throws SQLException {
    	super.afterList(domainList);
    	if (LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitas()) {
    		if (!ValueUtil.valueEquals(StatusAgenda.STATUSAGENDA_CDAVISITAR, cbStatusAgenda.getValue())) {
    			btMotivoVisitaMultiplasVisitas.setVisible(false);
    			return;
    		}
    		Date diaAgenda = cbDiaAgendaComboBox.getValue();
    		if (ValueUtil.isNotEmpty(diaAgenda)) {
    			Date diaAtual = DateUtil.getCurrentDate();
    			if (diaAgenda.equals(diaAtual)) {
    				btMotivoVisitaMultiplasVisitas.setVisible(LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitasDiaAtualDiaAnterior() || LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitasDiaAtual());
    			} else if (diaAgenda.isBefore(diaAtual)) {
    				btMotivoVisitaMultiplasVisitas.setVisible(LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultiplasVisitasDiaAtualDiaAnterior() || LavenderePdaConfig.isPermiteRegistrarMotivoVisitaMultuplasVisitasDiaAnterior());
    			} else {
    				btMotivoVisitaMultiplasVisitas.setVisible(false);
    			}
    		}
    	}
    }

    @Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
    	case ControlEvent.PRESSED: {
    		if (event.target == cbDiaAgendaComboBox) {
    			filtrarClick();
    			int dia = cbDiaAgendaComboBox.getValue().getDayOfWeek();
    			setTextDiaSemana(dia);
    		} else if (event.target == btTransferirAgendas) {
    			btTransferirAgendasClick();
    		} else if (event.target == btNovaAgendaVisita) {
    			btNovaAgendaVisitaClick();
    		} else if (!LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && isListTarget(event)) {
    			filtrarClick();
    		} else if (event.target == cbRepresentante) {
    			cbRepresentanteChange();
    		} else if (event.target == btMotivoVisitaMultiplasVisitas) {
    			btRegistrarMotivoVisitaMultiplasVisitasClick();
    		} else if (event.target == btFiltro) {
    			filtrarClick();
    		} else if (event.target == btFiltroAvancado) {
    			btFiltroAvancadoClick(event.target);
    		} else if (event.target == btMapaGoogleMaps) {
    			btMapaGoogleMapsClick();
    		} else if (event.target == btMapaTotalCross) {
    			btMapaGoogleApiClick();
    		} else if (event.target == btRedefinirOrdem) {
    			btRedefinirOrdemClick();
    		} else if (event.target == cbRede || event.target == cbCategoria) {
    			filtrarClick();
    		} else if (event.target == btCalcularRota) {
    			btCalcularRotaClick();
    		}
    		break;
    	}
    	case ValueChangeEvent.VALUE_CHANGE: {
    		if (event.target == edDtInicial || event.target == edDtFinal) {
    			if (event.target == edDtInicial) {
    				edDtInicialClick();
    			} else {
    				edDtFinalClick();
    			}
    			setTextDiaSemanaPeriodo();
    		}
    		break;
    	}
    	case SortButtonEvent.SORT_BUTTON_CLICK_EVENT: {
    		btUpOrDownClick(event.target);
    		break;
    	}
    	case ButtonOptionsEvent.OPTION_PRESS: {
    		if (bmOpcoes.selectedItem.equals(Messages.BOTAO_MAPA_CLIENTE)) {
    			btMapaGoogleApiClick();
    		} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_ROTA)) {
    			btMapaGoogleMapsClick();
    		} else if (bmOpcoes.selectedItem.equals(Messages.BOTAO_CALCULAR_ROTA)) {
    			btCalcularRotaClick();
    		}
    		break;
    	}
    	}
    }

    @Override
 	public void reposition() {
    	super.reposition();
    	try {
    		list();
		} catch (SQLException e) {
			UiUtil.showErrorMessage(e.getMessage());
		}
    }

	private boolean isListTarget(Event event) {
		return event.target == ckClientesAtrasados || 
				event.target == cbStatusAgenda || 
				event.target == cbTipoCadastroClienteComboBox || 
				event.target == cbTipoClienteRedeComboBox || 
				event.target == cbMarcador ||
				event.target == cbFornecedor;
	} 	

    private void btUpOrDownClick(Object target) throws SQLException {
    	SortButtonsContainer sortButtons = (SortButtonsContainer)target;
    	int indexSelected = sortButtons.index;
    	if ((sortButtons.direcao == SortButtonsContainer.DIRECAO_BAIXO && indexSelected < listContainer.size()-1) || (sortButtons.direcao == SortButtonsContainer.DIRECAO_CIMA && indexSelected > 0)) {
    		visibleSortButtonsInList = true;
    		sortButtonEvent(indexSelected, sortButtons.direcao);
    	} else {
    		return;
    	}
    	list();
    }

	private void sortButtonEvent(int indexSelected, int direcao) throws SQLException {
		String rowKeyItemAtual = listContainer.getId(indexSelected);
		String rowKeyProximoItem = listContainer.getId((direcao == SortButtonsContainer.DIRECAO_BAIXO ? indexSelected + 1 : indexSelected - 1));
		AgendaVisita agendaVisitaAtual = (AgendaVisita) AgendaVisitaService.getInstance().findByRowKey(rowKeyItemAtual);
		AgendaVisita agendaVisitaProximoItem = (AgendaVisita) AgendaVisitaService.getInstance().findByRowKey(rowKeyProximoItem);
		AgendaVisitaService.getInstance().updateColumn(rowKeyItemAtual, "NUORDEMMANUAL", agendaVisitaProximoItem.nuOrdemManual, Types.INTEGER);
		AgendaVisitaService.getInstance().updateColumn(rowKeyProximoItem, "NUORDEMMANUAL", agendaVisitaAtual.nuOrdemManual, Types.INTEGER);
	}

	private void btRedefinirOrdemClick() throws SQLException {
		visibleSortButtonsInList = true;
		countReordenarClick = countReordenarClick == 2 ? 1 : ++countReordenarClick;
		updateSortColumnUI();
		limpaFiltros();
		list();
		updateSortNuOrdemManual();
	}

	private void updateSortColumnUI() {
		sortAtributte = "NUORDEMMANUAL";
		sortAsc = "S";
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
		listContainer.atualizaTamanhoComponentesBarraSuperior();
	}
	
	private void updateSortNuOrdemManual() throws SQLException {
		int size = listContainer.size();
		for (int i = 0; i < size; i++) {
			String rowKey = listContainer.getId(i);
			AgendaVisitaService.getInstance().updateColumn(rowKey, "NUORDEMMANUAL", i+1, Types.INTEGER);
		}
	}

	private void btMapaGoogleMapsClick() throws SQLException {
		if (VmUtil.isAndroid() || VmUtil.isIOS()) {
			Vector clienteList = getClienteList();
			if (ValueUtil.isEmpty(clienteList)) {
				UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
				return;
			}
			if (clienteList.size() > 10) {
				mostrarPopUpClientes(clienteList);
			} else {
				mostrarMapaGoogleMaps(clienteList);
			}			
		} else {
			UiUtil.showWarnMessage(Messages.CAD_COORD_PLATAFORMA_NAO_SUPORTADA);
		}
	}

	private void mostrarPopUpClientes(Vector clienteList) {
		ListClienteAgendaMapaWindow listClienteAgendaMapa = new ListClienteAgendaMapaWindow(clienteList);
		listClienteAgendaMapa.popup();
		if (!listClienteAgendaMapa.indexSelected || listClienteAgendaMapa.closedByBtFechar) return;
		filterListByIndex(clienteList, listClienteAgendaMapa.indexList);
		mostrarMapaGoogleMaps(clienteList);
	}
	
	private void filterListByIndex(Vector clienteList, int indexList) {
		for (int i = 0; i < indexList; i++) {
			clienteList.removeElementAt(0);
		}
	}

	private Vector getClienteList() throws SQLException {
		clienteCoordenadaFilter = true;
		Vector agendaList = getDomainList(getDomainFilterSortable());
		clienteCoordenadaFilter = false;
		LinkedHashMap<String, Cliente> clientesAgendaHash = getClientesFromAgenda(agendaList);
		Vector clienteList = new Vector();
		for(Cliente cliente: clientesAgendaHash.values()) {
			clienteList.addElement(cliente);
		}
		return clienteList;
	}

	private LinkedHashMap<String, Cliente> getClientesFromAgenda(Vector agendaList) throws SQLException {
		int size = agendaList.size();
		LinkedHashMap<String, Cliente> clientesAgendaHash = new LinkedHashMap<String, Cliente>();
		for (int i = 0; i < size; i++) {
			Cliente cliente = ((AgendaVisita)agendaList.items[i]).cliente;
			if (cliente == null) continue;
			cliente = (Cliente) ClienteService.getInstance().findByPrimaryKey(cliente);
			if (clientesAgendaHash.get(cliente.rowKey) == null) {
				clientesAgendaHash.put(cliente.rowKey, cliente);
			}
		}
		return clientesAgendaHash;
	}
	
	 private void mostrarMapaGoogleMaps(Vector clienteAgendaList) {
			if (clienteAgendaList == null || clienteAgendaList.size() == 0) return;
			String coordenadaUrl = "";
			if (clienteAgendaList.size() == 1) {
				coordenadaUrl = generateUrlForOneCliente(clienteAgendaList.items[0]);
			} else {
				coordenadaUrl = getCoordenadasUrl(clienteAgendaList);
			}
			if (!ValueUtil.valueEquals(coordenadaUrl, ValueUtil.VALOR_NI)) {
				VmUtil.debug("Retorno da abertura mapa geolocalizacao:" + UiUtil.openBrowser(coordenadaUrl));
			}
		}

	private String getCoordenadasUrl(Vector clienteAgendaList) {
		StringBuffer strbfUrl = new StringBuffer();
		int size = clienteAgendaList.size();
		int index = 2;
		generateUrlGoogle(clienteAgendaList.items[0], clienteAgendaList.items[1], strbfUrl);
		while (index < VALOR_LIMITE_GOOGLE_MAPS && index < size) {
			Cliente cliente = (Cliente) clienteAgendaList.items[index++];
			strbfUrl.append("%20to:").append(cliente.cdLatitude).append(",").append(cliente.cdLongitude);
		}
		return strbfUrl.toString();
	}

	private void generateUrlGoogle(Object primeiroClienteObject, Object segundoClienteObject, StringBuffer strbfUrl) {
		Cliente primeiroCliente = (Cliente) primeiroClienteObject;
		Cliente segundoCliente = (Cliente) segundoClienteObject;
		generateDefaultUrlGoogle(primeiroCliente.cdLatitude, primeiroCliente.cdLongitude, segundoCliente.cdLatitude, segundoCliente.cdLongitude, strbfUrl);
	}

	private String generateUrlForOneCliente(Object primeiroCliente) {
		GpsData coordenadaLocalAtual = AgendaVisitaService.getInstance().getCoordenadaAtual(true);
		if (coordenadaLocalAtual == null) return "";
		Cliente cliente = (Cliente) primeiroCliente;
		StringBuffer strbfUrl = new StringBuffer();
		generateDefaultUrlGoogle(coordenadaLocalAtual.latitude, coordenadaLocalAtual.longitude, cliente.cdLatitude, cliente.cdLongitude, strbfUrl);
		return strbfUrl.toString();
	}

	private void generateDefaultUrlGoogle(double cdLatitude, double cdLongitude, double cdLatitude2, double cdLongitude2, StringBuffer strbfUrl) {
		strbfUrl.append("http://maps.google.com/maps?saddr=").append(cdLatitude).append(",").append(cdLongitude);
		strbfUrl.append("&daddr=").append(cdLatitude2).append(",").append(cdLongitude2);
	}
	
	private void btMapaGoogleApiClick() throws SQLException {
		if (VmUtil.isAndroid() || VmUtil.isIOS()) {
			Vector clienteList = getClienteList();
			if (!testeConexao()) {
				UiUtil.showWarnMessage(Messages.AGENDA_VISITA_CLIENTE_NO_MAPA_ERRO);
				return;
			}
			if (ValueUtil.isEmpty(clienteList)) {
				UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
				return;
			}
			mostrarMapaGoogleApi(clienteList);
		} else {
			UiUtil.showWarnMessage(Messages.CAD_COORD_PLATAFORMA_NAO_SUPORTADA);
		}
	}
	
	private boolean testeConexao() {
		try {
			HttpSync httpSync = new HttpSync();
			int responseCode = httpSync.testConnection("http://www.google.com");	
			return responseCode == 200;
		} catch (Throwable ex) {
			return false;
		}
	}

	private void mostrarMapaGoogleApi(Vector clienteList) {
		try {
			String coordenadasDosClientes = getCoordenadaCliente(clienteList);
			if (ValueUtil.isEmpty(coordenadasDosClientes)) {
				UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
				return;
			}
			String retorno = SyncManager.postCoodernadasClient(coordenadasDosClientes);
			if (ValueUtil.isNotEmpty(retorno) && retorno.startsWith("OK")) {
				String link =  montarLink(HttpConnectionManager.getDefaultParamsSync());
				UiUtil.openBrowser(link);
			} else {
				UiUtil.showWarnMessage(Messages.AGENDA_MAPA_CLIENTE_SEM_COORDENADA);
				return;
			}
		} catch (Exception e) {
			UiUtil.showErrorMessage(e.getMessage());
		}
	}

	private String montarLink(ParamsSync paramsSync){
		return paramsSync.baseUrl + paramsSync.basePublicUrl + "marcadormapaapp.faces?chave=" + SessionLavenderePda.usuarioPdaRep.usuario;
	}

	private String getCoordenadaCliente(Vector clientesList) {
		int size = clientesList.size();
		String coordenadaCliente = "";
		String vir = "";
		for (int i = 0; i < size; i++) {
			Cliente cliente = (Cliente) clientesList.items[i];
			if (cliente.cdLatitude != 0.0 && cliente.cdLongitude != 0.0) {
				coordenadaCliente += vir + "[{\"lat\":" + cliente.cdLatitude + ",\"lng\":" + cliente.cdLongitude + "},\"" + getNmRazaoSocial(cliente.nmRazaoSocial) + "\"]";
				vir = ",";
			}
		}
		if (!"".equals(coordenadaCliente)) {
			coordenadaCliente = "[" + coordenadaCliente + "]";
			return coordenadaCliente;
		} else {
			return "";
		}
	}

	private String getNmRazaoSocial(String nmRazaoSocial) {
		if (nmRazaoSocial.length() > 20) {
			return nmRazaoSocial.substring(0, 20);
		} else {
			return nmRazaoSocial;	
		}
	}

	@Override
    protected void filtrarClick() throws SQLException {
    	list();
    }

	private void btFiltroAvancadoClick(Object target) throws SQLException {
		FiltroAgendaVisitaAvancadoWindow filtroClienteAvancadoWindow = new FiltroAgendaVisitaAvancadoWindow(this);
		filtroClienteAvancadoWindow.popup();
		if (filtroClienteAvancadoWindow.aplicouFiltros) list();
	}

	private void btTransferirAgendasClick() throws SQLException {
		Vector agendaList = new Vector();
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		for (int i = 0; i < gridSize; i++) {
			AgendaVisita agendaVisita;
			if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
				agendaVisita = (AgendaVisita) agendaVisitaList.items[checkedItens[i]];
				agendaVisita.dtAgendaAtual = agendaVisita.dtAgenda;
			} else {
				agendaVisita = (AgendaVisita) AgendaVisitaService.getInstance().findByRowKey(listContainer.getId(checkedItens[i]));
				agendaVisita.dtAgendaAtual = cbDiaAgendaComboBox.getValue();
			}
			agendaList.addElement(agendaVisita);
		}
		if (agendaList.size() == 0) {
			UiUtil.showInfoMessage(Messages.MSG_NENHUMA_AGENDA_SELECIONADA);
			return;
		}
		CadTransferenciaAgendaWindow cadTransferenciaAgendaWindow = new CadTransferenciaAgendaWindow(agendaList, null, cbDiaAgendaComboBox.getValue());
		cadTransferenciaAgendaWindow.popup();
		if (cadTransferenciaAgendaWindow.transferiuAgenda) {
			list();
		}
	}
	
	private void btNovaAgendaVisitaClick() throws SQLException {
		new ListClienteWindow(false, false, true).popup();
		list();
	}

	private void cbRepresentanteChange() throws SQLException {
		if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
		} else {
			SessionLavenderePda.setRepresentante(null);
		}
		list();
	}

	public void singleClickInList() throws SQLException {
		agendaSelecionada = null;
		ColetaGpsService.getInstance().encerraColetaGpsSeNecessario();
		AgendaVisita agendaVisita = (AgendaVisita) getSelectedDomain();
		if (SessionLavenderePda.visitaAndamento != null && SessionLavenderePda.visitaAndamento.isVisitaEmAndamento() && !ValueUtil.valueEquals(agendaVisita.cdCliente, SessionLavenderePda.visitaAndamento.cdCliente) && LavenderePdaConfig.sugereRegistroSaidaNaListaDeClientes()) {
			Cliente cliente = ClienteService.getInstance().getCliente(SessionLavenderePda.visitaAndamento.cdEmpresa, SessionLavenderePda.visitaAndamento.cdRepresentante, SessionLavenderePda.visitaAndamento.cdCliente);
			if (UiUtil.showConfirmYesNoMessage(Messages.TITULO_VISITA_ANDAMENTO, MessageUtil.getMessage(Messages.MSG_CLIENTE_VISITA_ANDAMENTO, cliente.toString()))) {
				CadClienteMenuForm.btRegistrarAtualizarSaidaClick();
			}
		}
		SessionLavenderePda.setCliente(agendaVisita.cliente);
		setRepresentanteSession(agendaVisita.cdRepresentante);
		((CadClienteMenuForm)getBaseCrudCadMenuForm()).setAgendaVisitaListForm(this);
		SessionLavenderePda.setAgenda(agendaVisita);
		((CadClienteMenuForm)getBaseCrudCadMenuForm()).setAgendaVisita(agendaVisita);
		((CadClienteMenuForm)getBaseCrudCadMenuForm()).setRowkeyDaProximaSequencia(getRowkeyDaProximaSequencia());
    	super.singleClickInList();
    	if (SessionLavenderePda.visitaAndamento == null && LavenderePdaConfig.sugereRegistroChegadaNoMenuCliente()) {
    		if (UiUtil.showConfirmYesNoMessage(CadClienteMenuForm.getRegistrarChegadaMessage(agendaVisita.cliente.isNovoCliente()))) {
    			if (CadClienteMenuForm.btRegistrarAtualizarChegadaClick(agendaVisita, agendaVisita.dtAgendaAtual, true)) {
    				((CadClienteMenuForm) getBaseCrudCadMenuForm()).remontaMenuFuncionalidades();
				}
    		}
    	}
    	lastSelectedIndex = listContainer.getSelectedIndex();
    }
	
	private void setRepresentanteSession(String cdRepresentante) throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			isAllSelectedRepresentante = ValueUtil.isEmpty(cbRepresentante.getValue());
			if (isAllSelectedRepresentante || !cbRepresentante.getValue().equalsIgnoreCase(cdRepresentante)) {
				Representante rep = RepresentanteService.getInstance().getRepresentanteById(cdRepresentante);
				if (rep != null) {
					SessionLavenderePda.setRepresentante(rep);
				}
			}
		}
	}

    public void setTextDiaSemana(int dia) {
		diaSemana.setText(DateUtil.getDiaSemana(dia));
	}
    
    public void setTextDiaSemanaPeriodo() {
    	if (edDtInicial == null || edDtFinal == null || ValueUtil.isEmpty(edDtInicial.getValue()) || ValueUtil.isEmpty(edDtFinal.getValue())) return;
    	
    	diaSemanaInicial.setText(DateUtil.getDiaSemana(edDtInicial.getValue().getDayOfWeek()));
    	diaSemanaFinal.setText(DateUtil.getDiaSemana(edDtFinal.getValue().getDayOfWeek()));
    }

	protected Vector getAgendaListBySupervisor(Vector agendaList) throws SQLException {
		Vector listSupRepresentantes = SupervisorRepService.getInstance().getRepresentantesBySupervisor(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
		Vector agendaVisitaListBySupervisor = new Vector();
		if (ValueUtil.isNotEmpty(listSupRepresentantes)) {
			for (int i = 0; i < agendaList.size(); i++) {
				AgendaVisita agendaVst = (AgendaVisita) agendaList.items[i];
				boolean valido = false;
				for (int j = 0; j < listSupRepresentantes.size(); j++) {
					SupervisorRep sup = (SupervisorRep) listSupRepresentantes.items[j];
					if (agendaVst.cdRepresentante.equals(sup.cdRepresentante)) {
						valido = true;
						break;
					}
				}
				if (valido) {
					agendaVisitaListBySupervisor.addElement(agendaVst);
				}
			}
		} else {
			agendaVisitaListBySupervisor = agendaList;
		}
		return agendaVisitaListBySupervisor;
	}
	
	private void btRegistrarMotivoVisitaMultiplasVisitasClick() throws SQLException {
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		}
		Vector agendaVisitaList = getDomainList(getDomainFilterSortable());
		if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && !(edDtFinal.getValue().isBefore(currentDate) || edDtFinal.getValue().equals(DateUtil.getCurrentDate()))) {
			UiUtil.showErrorMessage(Messages.AGENDAVISITA_ERRO_MOTIVO_MULTIPLOS);
		} else {
		if (ValueUtil.isNotEmpty(agendaVisitaList)) {
			CadMultiplasVisitasWindow cadVisitaWindow = new CadMultiplasVisitasWindow(agendaVisitaList, cbDiaAgendaComboBox.getValue());
			cadVisitaWindow.popup();
			if (cadVisitaWindow.registroSalvo) {
				list();
			}
		} else {
			UiUtil.showInfoMessage(Messages.VISITA_MSG_NENHUMA_AGENDA_DISPONIVEL);
		}
	}
	}
	
	private String getDsTipoAgenda(String cdTipoAgenda) {
		for (int i = 0; i < tipoAgendaList.size(); i++) {
			TipoAgenda tipoAgenda = (TipoAgenda) tipoAgendaList.items[i];
			if (ValueUtil.isNotEmpty(cdTipoAgenda) && ValueUtil.valueEquals(cdTipoAgenda, tipoAgenda.cdTipoAgenda)) {
				return tipoAgenda.dsTipoAgenda;
			}
		}
		return cdTipoAgenda;
	}
	
	private void edDtInicialClick() throws SQLException {
		if (ValueUtil.isEmpty(edDtInicial.getValue()) || ValueUtil.isEmpty(edDtFinal.getValue()) ) return;
		
		if (edDtInicial.getValue().isAfter(edDtFinal.getValue())) {
			edDtFinal.setValue(edDtInicial.getValue());
			return;
		} 
		Date limitPastDate = AgendaVisitaService.getInstance().getLastValidDay(new Date());
		if (DateUtil.isBefore(edDtInicial.getValue(), limitPastDate)) {
			edDtInicial.setValue(limitPastDate);
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.AGENDAVISITA_MSG_ERRO, DateUtil.formatDateDDMMYYYY(limitPastDate)));
		}
	}
	
	private void edDtFinalClick() {
		if (ValueUtil.isEmpty(edDtFinal.getValue()) || ValueUtil.isEmpty(edDtInicial.getValue())) return;
		
		validaDtFinal();
	}

	private void validaDtFinal() {
		if (DateUtil.isBefore(edDtFinal.getValue(), edDtInicial.getValue())) {
			edDtFinal.setValue(edDtInicial.getValue());
			throw new ValidationException(Messages.AGENDAVISITA_ERRO_FINAL_ANTERIOR_INICIAL);
		}
	}
	
	public void list() throws SQLException {
		LoadingBoxWindow msg = UiUtil.createProcessingMessage();
		msg.popupNonBlocking();
		int listSize = 0;
		Vector domainList = null;
		try {
			if (LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita) {
				validaDtFinal();
			}
			listContainer.removeAllContainers();
			listContainer.uncheckAll();
			loadMarcadoresHash();
			domainList = getDomainList(getDomainFilterSortable());
			listSize = domainList.size();
			
			countImagesItem = new int[listSize];
			Container[] all = new Container[listSize];

			if (listSize > 0) {
				BaseListContainer.Item c;
				BaseDomain domain;
				for (int i = 0; i < listSize; i++) {
					if (i % 250 == 0) {
						VmUtil.executeGarbageCollector();
					}
					all[i] = c = new BaseListContainer.Item(listContainer.getLayout());
					domain = (BaseDomain) domainList.items[i];
					c.id = domain.getRowKey();
					c.setID(ADD_ID.formatID(c.id));
					c.domain = getDomain(domain);
					if (visibleSortButtonsInList && countReordenarClick < 2) {
						SortButtonsContainer sortButtons = new SortButtonsContainer(i);
						c.rightControl = sortButtons;
					}
					c.setIconsLegend(getIconsLegend(domain, i), resizeIconsLegend, useLeftTopIcons);
					c.setItens(getItem(domain, (Container)c.rightControl, countImagesItem[i]));
					c.setToolTip(getToolTip(domain));
					c.ignoreTotalizer = isIgnoreTotalizer(domain);
					setPropertiesInRowList(c, domain);
				}
				listContainer.addContainers(all);
				configureContainersBtHidden();
			}
			afterList(domainList);

		} finally {
			domainList = null;
			msg.unpop();
			if (!visibleSortButtonsInList) {
				countReordenarClick = 0;
			} else {
				visibleSortButtonsInList = false;
			}
		}
	}
	
	private void limpaFiltros() {
		edFiltro.setValue(ValueUtil.VALOR_NI);
		cbMarcador.setSelectedIndex(0);
		cbStatusAgenda.setSelectedIndex(0);
		cbTipoCadastroClienteComboBox.setSelectedIndex(0);
		cbTipoClienteRedeComboBox.setSelectedIndex(0);
		cbFornecedor.setSelectedIndex(0);
		ckClientesAtrasados.setChecked(false);
	}
	
	private void btCalcularRotaClick() {
		if (!validatePermiteCalculoAgenda()) {
			return;
		}
		LoadingBoxWindow mb = UiUtil.createProcessingMessage(Messages.MSG_CALCULANDO_ROTA);
		try {
			mb.popupNonBlocking();
			GpsData data = AgendaVisitaService.getInstance().getCoordenadaAtual(false);
			if (data != null) {
				AgendaVisitaService.getInstance().recebeAtualizacaoAgendaVisita(data.latitude, data.longitude, agendaVisitaList);
				updateSortColumnUI();
				list();
			}
			UiUtil.showSucessMessage(Messages.MSG_ROTA_CALCULADA);
		} catch (ValidationException | ConnectionException | ApplicationException e) {
			ExceptionUtil.handle(e);
			throw e;
		} catch (Throwable e) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_CALCULO_ROTA);
			ExceptionUtil.handle(e);
		} finally {
			mb.unpop();
		}
	}
	
	private boolean validatePermiteCalculoAgenda() {
		Date dtAtual = DateUtil.getCurrentDate();
		boolean isFiltroSelecionadoHoje = LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && dtAtual.equals(edDtInicial.getValue()) && dtAtual.equals(edDtFinal.getValue());
		isFiltroSelecionadoHoje |= !LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita && dtAtual.equals(cbDiaAgendaComboBox.getValue());
		if (!isFiltroSelecionadoHoje) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_DTCALCULO_ATUAL);
			return false;
		}
		if (listContainer.size() == 0) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_SEM_AGENDA);
			return false;
		}
		if (listContainer.size() > AgendaVisita.LIMITE_PONTOS_GMAPS) {
			UiUtil.showErrorMessage(Messages.MSG_ERRO_LIMITE_PONTOS);
			return false;
		}
		return true;
	}

	private void loadMarcadoresHash() throws SQLException {
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaAgendas) {
			marcadoresCliHash = MarcadorService.getInstance().buscaMarcadoresVigentesHash(Marcador.ENTIDADE_MARCADOR_CLIENTE);
		}
	}
	
}
