package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.async.AsyncPool;
import br.com.wmw.framework.async.RunnableImpl;
import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer.Item;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Categoria;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.RequisicaoServTipo;
import br.com.wmw.lavenderepda.business.domain.StatusCliente;
import br.com.wmw.lavenderepda.business.domain.SupervisorRep;
import br.com.wmw.lavenderepda.business.domain.ValorParametro;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.MarcadorService;
import br.com.wmw.lavenderepda.business.service.ProdutoRetiradaService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.StatusRentCliService;
import br.com.wmw.lavenderepda.business.service.SupervisorRepService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CategoriaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescProgressivoConfigComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoCadastroClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoClienteRedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereCrudListForm;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import br.com.wmw.lavenderepda.util.LavendereColorUtil;
import totalcross.ui.Control;
import totalcross.ui.ScrollPosition;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.ScrollEvent;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Vector;

public class ListClienteForm extends LavendereCrudListForm {
	
	private static final String NU_FILTRO_STATUS = "1";
	private static final String NU_FILTRO_TIPO_CADASTRO = "2";
	private static final String NU_FILTRO_TIPO_CLIENTE_REDE = "3";
	private static final String NU_FILTRO_REDE = "4";
	private static final String NU_FILTRO_MARCADOR = "5";
	private static final String NU_FILTRO_REPRESENTANTE = "6";
	private static final String NU_FILTRO_FORNECEDOR = "9";

	private static final String NU_FILTRO_DESC_PROGRESSIVO_PERSONALIZADO = "10";
	
	public static final int GRID_POS_STATUS = 6;

	private boolean onSelectClienteFiltroWindow;

	public boolean flListInicialized;
	private String dsFiltro;
	public String dsFiltroDefault;
	public StatusClienteComboBox cbStatusCliente;
	private RepresentanteSupervComboBox cbRepresentante;
	private TipoCadastroClienteComboBox cbTipoCadastroClienteComboBox;
	private RedeComboBox cbRede;
	private CategoriaComboBox cbCategoria;
	private MarcadorComboBox cbMarcador;
	private FornecedorComboBox cbFornecedor;
	private DescProgressivoConfigComboBox cbDescProgressivoConfig;
	private int qtMinimaCaracteres = 0;
	public boolean isAllSelected;
	public CheckBoolean ckClientesAtrasados;
	public CheckBoolean ckDestacaClienteNaoAtendidoMes;
	public boolean onSelectClienteNovoPedido;
	public boolean onSelectClienteNovaAgendaVisita;
	public boolean onReplicacaoPedido;
	private SessionTotalizerContainer sessaoTotalizadores;
	private LabelTotalizador ltQtClientes;
	private LabelTotalizador ltQtClientesAtrasados;
	private LabelTotalizador ltQtClientesAtendidos;
	private BaseButton btFiltroAvancado;
	private Cliente clienteFiltroAvancado;
	private TipoClienteRedeComboBox cbTipoClienteRedeComboBox;
	private Map<String, Image> indicadoresMap;
	private int iconSize;
	private Cliente cliente;
	public boolean fromRelClientesNaoAtendidosForm;
	public String flFiltroStatusClienteTipoRequisicao;
	public String dsStatusClienteListTipoRequisicao;
	HashMap<String, Marcador> marcadoresCliHash;
	protected boolean usaScrollInfinito;

	//--
	public ListClienteWindow clienteFilterWindow;
	
	public ListClienteForm(boolean onSelectClienteFiltroWindow, Cliente cliente, RequisicaoServTipo requisicaoServTipo) throws SQLException {
		super(Messages.MENU_OPCAO_CLIENTES);
		this.onSelectClienteFiltroWindow = onSelectClienteFiltroWindow;
		setBaseCrudCadMenuForm(new CadClienteMenuForm());
		singleClickOn = true;
		if (cliente != null) {
			this.cliente = cliente;
		}
		onSelectClienteNovoPedido = false;
		onSelectClienteNovaAgendaVisita = false;
		constructorListContainer();
		sessaoTotalizadores = new SessionTotalizerContainer();
		ltQtClientes = new LabelTotalizador(" - ");
		ltQtClientesAtrasados = new LabelTotalizador(" - ");
		ltQtClientesAtendidos = new LabelTotalizador(" - ");
		// --
		edFiltro.idAgrupador = Cliente.APPOBJ_CAMPOS_FILTRO_CLIENTE;
		if (requisicaoServTipo != null) {
			this.flFiltroStatusClienteTipoRequisicao = requisicaoServTipo.flFiltroStatusCliente;
			this.dsStatusClienteListTipoRequisicao = requisicaoServTipo.dsStatusClienteList;
		}
		if (ValueUtil.isNotEmpty(flFiltroStatusClienteTipoRequisicao) && !ValueUtil.valueEquals(ValueUtil.VALOR_NAO, flFiltroStatusClienteTipoRequisicao)) {
			cbStatusCliente = new StatusClienteComboBox(flFiltroStatusClienteTipoRequisicao, dsStatusClienteListTipoRequisicao);
		} else {
			cbStatusCliente = new StatusClienteComboBox();
		}
		cbRepresentante = new RepresentanteSupervComboBox();
		cbTipoCadastroClienteComboBox = new TipoCadastroClienteComboBox();
		cbTipoClienteRedeComboBox = new TipoClienteRedeComboBox();
		cbRede = new RedeComboBox();
		cbCategoria = new CategoriaComboBox();
		cbMarcador = new MarcadorComboBox(Marcador.ENTIDADE_MARCADOR_CLIENTE);
		cbMarcador.setID("cbMarcador");
		cbFornecedor = new FornecedorComboBox();
		ckClientesAtrasados = new CheckBoolean("");
		ckDestacaClienteNaoAtendidoMes = new CheckBoolean("");
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0) {
			ckClientesAtrasados = new CheckBoolean(MessageUtil.getMessage(Messages.MSG_CLIENTE_SEM_PEDIDOS, LavenderePdaConfig.nuDiasFiltroClienteSemPedido));
		}
		if (LavenderePdaConfig.destacaClienteAtendidoMes) {
			ckDestacaClienteNaoAtendidoMes = new CheckBoolean(Messages.MSG_CLIENTE_NAO_ATENDIDO_MES);
		}
		btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
			indicadoresMap = new HashMap<>();
			resizeIconsLegend = false;
			useLeftTopIcons = true;
			iconSize = new LabelValue().getPreferredHeight();
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			cbDescProgressivoConfig = new DescProgressivoConfigComboBox(BaseComboBox.DefaultItemType_ALL);
			cbDescProgressivoConfig.loadAll();
		}
		listContainer.setTotalizerQtdeVisible(false);
		usaScrollInfinito = getMaxResult() > 0;
	}

	private void constructorListContainer() {
		ScrollPosition.AUTO_HIDE = false;
		configListContainer("CDCLIENTE");
		//--
		listContainer = new GridListContainer(getNuItensGridCliente(), 3);
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
			listContainer.setColsSort(new String[][]{{Messages.PRODUTO_LABEL_CODIGO, "CDCLIENTE"}, {Messages.PEDIDO_LABEL_NMRAZAOSOCIAL, "NMRAZAOSOCIAL"}, {Messages.CLIENTE_LABEL_NMFANTASIA_LISTA, "NMFANTASIA"}});
		} else {
			listContainer.setColsSort(new String[][]{{Messages.PRODUTO_LABEL_CODIGO, "CDCLIENTE"}, {Messages.PEDIDO_LABEL_NMRAZAOSOCIAL, "NMRAZAOSOCIAL"}});
		}
		if (LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
			int linhaLogradouro = LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes() ? 3 : 0;
			listContainer.setColPosition(LavenderePdaConfig.exibeNomeFantasiaListaClientes() ? 10 + linhaLogradouro : 7 + linhaLogradouro, RIGHT);
		}
		adicionaBotaoNovoPedidoDeslizante();
		ScrollPosition.AUTO_HIDE = true;
	}
	
	private int getNuItensGridCliente() {
		int nuItens = 6;
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
			nuItens = nuItens + 3;
		}
		if (LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
			nuItens = nuItens + 3;
		}
		if (LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes()) {
			nuItens = nuItens + 3;
		}
		return nuItens;
	}

	private void adicionaBotaoNovoPedidoDeslizante() {
		if (!LavenderePdaConfig.usaTipoPedidoComoOpcaoMenuCliente && !onSelectClienteFiltroWindow) {
			listContainer.setUseBtHidden(Messages.BOTAO_NOVO_PEDIDO);
		}
	}

	// @Override
	protected CrudService getCrudService() throws SQLException {
		return ClienteService.getInstance();
	}

	private ClienteService getClienteService() throws SQLException {
		return (ClienteService) getCrudService();
	}

	protected BaseDomain getDomainFilter() throws SQLException {
		BaseDomain cliente;
		if (onSelectClienteNovaAgendaVisita || LavenderePdaConfig.isFiltrosFixoTelaListaClienteDominioConfigurado() || LavenderePdaConfig.isFiltrosFixoTelaListaClienteLigado()) {
			cliente = getDomainFilterAvancado();
		} else {
			cliente = new Cliente();
		}
		cliente.limit = getMaxResult();
		return cliente;
	}

	private BaseDomain getDomainFilterAvancado() {
		if (clienteFiltroAvancado == null) {
			clienteFiltroAvancado = new Cliente();
		}
		clienteFiltroAvancado.cdCliente = null;
		return clienteFiltroAvancado;
	}
	
	@Override
	public void loadDefaultFilters() throws SQLException {
		super.loadDefaultFilters();
		cbStatusCliente.setSelectedIndex(0);
		cbRepresentante.setSelectedIndex(0);
		if (LavenderePdaConfig.usaClienteEmModoProspeccao) {
			cbTipoCadastroClienteComboBox.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede) {
			cbTipoClienteRedeComboBox.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
			cbRede.setSelectedIndex(0);
			if (cliente != null) {
				Rede rede = cliente.getRede();
				if (rede != null) {
					cbRede.setSelectedItem(rede);
				}
			}
		}
		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
			cbCategoria.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
			cbMarcador.setSelectedIndex(0);
		}
		if (ValueUtil.isNotEmpty(SessionLavenderePda.getRepresentante().cdRepresentante)) {
			cbRepresentante.setValue(SessionLavenderePda.getRepresentante().cdRepresentante);
			if (ValueUtil.isEmpty(cbRepresentante.getValue())) {
				cbRepresentante.setSelectedIndex(0);
			}
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor) {
			cbFornecedor.setSelectedIndex(0);
		}
		edFiltro.setText(ValueUtil.isEmpty(dsFiltroDefault) ? ValueUtil.VALOR_NI : dsFiltroDefault);
		//--
		if ((LavenderePdaConfig.getQtMinimaCaracteresListaClientes() == 0) && (LavenderePdaConfig.getQtMinimaCaracteresListaClientes()!= ValorParametro.PARAM_INT_VALOR_ZERO) && !isUsuarioSupervidorAndComboRepItemNull()) {
			flListInicialized = true;
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			cbDescProgressivoConfig.setSelectedIndex(0);
		}
	}

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		Cliente clienteFilter = (Cliente) domain;
		prepareFilter(clienteFilter);
		Vector clienteList = getClienteService().findClientes(dsFiltro, clienteFilter);
		clienteList = afterFindClientes(clienteFilter, clienteList);
		return clienteList;
	}

	public void prepareFilter(Cliente clienteFilter) {
		clienteFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			if (LavenderePdaConfig.isRepresentanteFixoTelaListaCliente()) {
				clienteFilter.cdRepresentante = cbRepresentante.getValue();
			} else {
				String cdRepresentanteFiltro = ((Cliente) getDomainFilterAvancado()).cdRepresentante;
				clienteFilter.cdRepresentante = cdRepresentanteFiltro == null ? SessionLavenderePda.usuarioPdaRep.cdRepresentante : cdRepresentanteFiltro;
			}
			if (ValueUtil.isEmpty(clienteFilter.cdRepresentante) && cbRepresentante.size() == 1) {
				clienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			}
		} else {
			clienteFilter.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		}
		if ((LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente() || fromRelClientesNaoAtendidosForm) && ckClientesAtrasados.isChecked() && LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0) {
			clienteFilter.nuDiasSemPedido = LavenderePdaConfig.nuDiasFiltroClienteSemPedido;
		} else if (LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente()) {
			clienteFilter.nuDiasSemPedido = 0;
		}
		if (LavenderePdaConfig.isClienteAtendidoTelaListaCliente() && LavenderePdaConfig.destacaClienteAtendidoMes) {
 			clienteFilter.flAtendido = StringUtil.getStringValue(!ckDestacaClienteNaoAtendidoMes.isChecked()); 
		}
		if (LavenderePdaConfig.isTipoCadastroFixoTelaListaCliente() && LavenderePdaConfig.usaClienteEmModoProspeccao) {
			if (onSelectClienteNovoPedido) {
				clienteFilter.flTipoCadastro = Cliente.TIPO_CLIENTE;
			} else if (ValueUtil.isEmpty(cbTipoCadastroClienteComboBox.getValue())) {
				clienteFilter.flTipoCadastro = "";
			} else {
				clienteFilter.flTipoCadastro = cbTipoCadastroClienteComboBox.getValue();
			}
		}
		if (LavenderePdaConfig.isTipoClienteFixoTelaListaCliente() && LavenderePdaConfig.usaFiltroTipoClienteRede) {
			if (!cbTipoClienteRedeComboBox.isOpcaoTodosSelecionado()) {
				clienteFilter.flTipoClienteRede = cbTipoClienteRedeComboBox.isOpcaoRedeSelecionado() ? Cliente.TIPO_REDE : Cliente.TIPO_INDIVIDUAL;
			} else {
				clienteFilter.flTipoClienteRede = "";
			}
		}
		if (LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente() && LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
			clienteFilter.cdRede = cbRede.getValue();
		}
		if (LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente() && LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
			clienteFilter.cdCategoria = cbCategoria.getValue();
		}
		if (LavenderePdaConfig.isMarcadoresFixoTelaListaCliente() && LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
			clienteFilter.cdMarcador = cbMarcador.getValue();
		}
		if (LavenderePdaConfig.isDescProgressivoPersonalizadoFixoTelaListaCliente() && LavenderePdaConfig.usaDescProgressivoPersonalizado) {
			clienteFilter.descProgressivoConfigFilter = cbDescProgressivoConfig.getValue();
		}
		clienteFilter.flOculto = ValueUtil.VALOR_SIM;
		if (LavenderePdaConfig.isStatusClienteFixoTelaListaCliente()) {
			clienteFilter.flStatusClienteFilter = cbStatusCliente.getValue();
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor) {
			clienteFilter.cdFornecedor = cbFornecedor.getValue();
		}
		clienteFilter.flFiltroStatusClienteTipoRequisicao = flFiltroStatusClienteTipoRequisicao;
		clienteFilter.dsStatusClienteListTipoRequisicao = dsStatusClienteListTipoRequisicao;

	}

	private Vector afterFindClientes(Cliente cliente, Vector clienteList) throws SQLException {
		clienteList = filtrarClienteSupervisor(clienteList);
		Cliente clienteFilter = (Cliente) cliente.clone();
		clienteFilter.cdCliente = Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO;
		clienteList.removeElement(clienteFilter);
		clienteFilter.cdCliente = Cliente.CD_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO;
		clienteList.removeElement(clienteFilter);
		clienteFilter.cdCliente = Cliente.CD_CLIENTE_DEFAULT_PARA_NOVA_PESQUISA_MERCADO;
		clienteList.removeElement(clienteFilter);
		clienteFilter.cdCliente = Cliente.CD_CLIENTE_DEFAULT_PARA_CADASTRO_COORDENADA;
		clienteList.removeElement(clienteFilter);
		return clienteList;
	}


	private Vector filtrarClienteSupervisor(Vector clienteList) throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor() && ValueUtil.isEmpty(cbRepresentante.getValue()) && SupervisorRepService.getInstance().isCargaMultiplosSupervisores()) {
			return getClienteListBySupervisor(clienteList);
		}
		return clienteList;
	}

	@Override
	public void list() throws SQLException {
		if (flListInicialized) {
			loadMarcadoresHash();
			super.list();
			carregaTotalizadoresClientes();
			listContainer.setFocus();
		}
	}

	private void loadMarcadoresHash() throws SQLException {
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
			marcadoresCliHash = MarcadorService.getInstance().buscaMarcadoresVigentesHash(Marcador.ENTIDADE_MARCADOR_CLIENTE);
		}
	}

	@Override
	protected void setPropertiesInRowList(Item c, BaseDomain domain) throws SQLException {
		Cliente cliente = (Cliente)domain;
		if (LavenderePdaConfig.usaDestaqueClienteSemLimiteCredito && FichaFinanceiraService.getInstance().getVlSaldoCliente(cliente, null) <= 0) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_FUNDO_LISTA_CLIENTE_SEM_LIMITE_CREDITO);
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente && ValueUtil.isNotEmpty(cliente.cdStatusRentCli)) {
			listContainer.setContainerBackColor(c, StatusRentCliService.getInstance().getStatusRentListColor(cliente.cdEmpresa, cliente.cdStatusRentCli));
		}
		if (LavenderePdaConfig.isUsaAlertaBloqueioClienteSemPedido() || LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0) {
			if (((LavenderePdaConfig.nuDiasAlertaClienteSemPedido > 0) &&
					 	 (cliente.nuDiasSemPedido >= LavenderePdaConfig.nuDiasAlertaClienteSemPedido)) ||
						 ((LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido > 0) &&
								 (cliente.nuDiasSemPedido >= LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido)) ||
						 ((LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0) &&
								 (cliente.nuDiasSemPedido >= LavenderePdaConfig.nuDiasFiltroClienteSemPedido))) {
				listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_NU_DIAS_SEM_PEDIDO_EXTRAPOLADO_BACK);
			}
		}
		if (LavenderePdaConfig.destacaClienteAtendidoMes && cliente.isAtendido()) {
			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_ATENDIDO_MES);
		}
    	if (LavenderePdaConfig.grifaClienteAtrasado && cliente.isStatusAtrasado()) {
    		listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_ATRASADO_GRID_FUNDO);
	    }
    	if (LavenderePdaConfig.grifaClienteBloqueado && cliente.isStatusBloqueado()) {
    		listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_BLOQUEADO_BACK);
    	}
    	if (LavenderePdaConfig.destacaClienteBloqueadoPorAtrasoNaLista && cliente.isStatusBloqueadoPorAtraso()) {
    		listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_STATUS_BLOQUEIO_POR_ATRASO_FUNDO);
    	}
    	if (cliente.isModoDeProspeccao()) {
    		listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_PROSPECTS);
    	}
    	if (ClienteService.getInstance().isPossuiVisitaEmAndamento(cliente.cdCliente)) {
    		listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_VISITA_ANDAMENTO);
    	}
    	if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada && cliente.flStatusRetirada != null) {
    		if (cliente.flStatusRetirada.equals("1")) {
    			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_EXTRAPOLADO);
    		} else if (cliente.flStatusRetirada.equals("2")) {
    			listContainer.setContainerBackColor(c, LavendereColorUtil.COR_CLIENTE_PRODUTO_PENDENTE_RETIRADA_NAO_EXTRAPOLADO);
    		}
    	}
    	if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
    		int color = ClienteService.getInstance().getStyleCliente(cliente, true);
    		if (color != -1) {
    			c.textColor = color;
    		}
	}
	}
	
    @Override
    protected String getToolTip(BaseDomain domain) throws SQLException {
    	Cliente cliente = (Cliente) domain;
    	return LavenderePdaConfig.exibeNomeFantasiaListaClientes() ? StringUtil.getStringValue(cliente.nmFantasia) : StringUtil.getStringValue(cliente.nmRazaoSocial);
    }

    @Override
	protected String[] getItem(Object domain) throws SQLException {
		Cliente cliente = (Cliente) domain;
		//--
		Vector item = new Vector(0);
		item.addElement(StringUtil.getStringValue(cliente.cdCliente));
		item.addElement(" - " + StringUtil.getStringValue(cliente.nmRazaoSocial));
		item.addElement(FrameworkMessages.CAMPO_VAZIO);
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
			item.addElement(StringUtil.getStringValue(cliente.nmFantasia));
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
		if (LavenderePdaConfig.isUsaFiltroLogradouroCidadeUFBairroNaListaDeClientes()) {
			item.addElement(StringUtil.getStringValue(cliente.dsLogradouroComercial));
			item.addElement(ValueUtil.isNotEmpty(cliente.nuLogradouroComercial) ? StringUtil.getStringValue(new StringBuffer(", ").append(cliente.nuLogradouroComercial)) : FrameworkMessages.CAMPO_VAZIO);
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
		item.addElement(StringUtil.getStringValue(cliente.dsCidadeComercial));
		item.addElement(StringUtil.getStringValue(ValueUtil.isNotEmpty(cliente.dsEstadoComercial) ? new StringBuffer(" - ").append(cliente.dsEstadoComercial) : FrameworkMessages.CAMPO_VAZIO));
		item.addElement(StringUtil.getStringValue(ValueUtil.isNotEmpty(cliente.dsBairroComercial) ? new StringBuffer(" - ").append(cliente.dsBairroComercial) : FrameworkMessages.CAMPO_VAZIO));
		//--
		if (LavenderePdaConfig.isUsaCaracteristicasClienteNoSistema()) {
			String dsRede = null;
			String dsCategoria = null;
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema()) {
				Rede rede = cliente.getRede();
				if (rede != null && ValueUtil.isNotEmpty(rede.cdRede)) {
					dsRede = StringUtil.getStringValue(rede.toString());
				}
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema()) {
				Categoria categoria = cliente.getCategoria();
				if (categoria != null && ValueUtil.isNotEmpty(categoria.cdCategoria)) {
					dsCategoria = StringUtil.getStringValue(categoria.toString());
				}
			}
			if (dsCategoria == null && dsRede == null) {
				item.addElement(FrameworkMessages.CAMPO_VAZIO);
			} else {
				if (dsCategoria == null) {
			item.addElement(StringUtil.getStringValue(dsRede));
				} else {
					if (dsRede == null) {
			item.addElement(StringUtil.getStringValue(dsCategoria));
					} else {
						item.addElement(StringUtil.getStringValue(dsRede + " - " + dsCategoria));
					}
				}
			}
			cliente.rede = null;
			cliente.categoria = null;
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
			item.addElement(FrameworkMessages.CAMPO_VAZIO);
		}
		
		item.addElement(StringUtil.getStringValue(cliente.isStatusBloqueado() ? StatusCliente.FLSTATUSCLIENTE_BLOQUEADO : cliente.isStatusAtrasado() ? StatusCliente.FLSTATUSCLIENTE_ATRASADO : ValueUtil.VALOR_NAO));
		//--
		return (String[]) item.toObjectArray();
	}
	
	@Override
	protected Image[] getIconsLegend(BaseDomain domain) throws SQLException {
		Image[] iconsLegend = null;
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes) {
			iconsLegend = ClienteService.getInstance().getIconsIndicadores((Cliente)domain, marcadoresCliHash, indicadoresMap, iconSize, Cliente.APRESENTA_LISTACLI);
		}
		return ValueUtil.isNotEmpty(iconsLegend) ? iconsLegend : super.getIconsLegend(domain);
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	protected boolean isClienteSelectedBloqueado() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return (StatusCliente.FLSTATUSCLIENTE_BLOQUEADO).equals(c.getItem(GRID_POS_STATUS));
	}

	@Override
	public void onFormShow() throws SQLException {
		if ((LavenderePdaConfig.getQtMinimaCaracteresListaClientes()!= 0) && isUsuarioSupervidorAndComboRepItemNull()) {
			edFiltro.requestFocus();
		} else {
			listContainer.setFocus();
		}
		super.onFormShow();
	}

	@Override
    public void onFormExibition() throws SQLException {
    	if (SessionLavenderePda.getCliente() != null) {
    		updateCurrentRecord(SessionLavenderePda.getCliente());
    	}
    	listContainer.setFocus();
    	super.onFormExibition();
    }

    @Override
    public void onFormClose() throws SQLException {
    	super.onFormClose();
    	marcadoresCliHash = null;
    }

	@Override
	protected void onFormStart() throws SQLException {
		if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.isRepresentanteFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isStatusClienteFixoTelaListaCliente()) {
		UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_STATUS), cbStatusCliente, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && !onSelectClienteNovoPedido && !onSelectClienteNovaAgendaVisita && LavenderePdaConfig.isTipoCadastroFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_TIPOCADASTRO), cbTipoCadastroClienteComboBox, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede && LavenderePdaConfig.isTipoClienteFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(Messages.TIPO_CLIENTE_REDE_TITULO), cbTipoClienteRedeComboBox, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRede, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(Messages.CATEGORIA_NOME_ENTIDADE), cbCategoria, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && LavenderePdaConfig.isMarcadoresFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(Messages.MARCADOR_NOME_ENTIDADE), cbMarcador, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && LavenderePdaConfig.isFornecedorFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(MessageUtil.getMessage(Messages.CLIENTE_NUDIAS_FORNECEDOR_SEM_PEDIDO, LavenderePdaConfig.nuDiasClienteSemPedidoFornecedor)), cbFornecedor, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && LavenderePdaConfig.isDescProgressivoPersonalizadoFixoTelaListaCliente()) {
			UiUtil.add(this, new LabelName(Messages.DESC_PROG_CONFIG_NM_ENTIDADE), cbDescProgressivoConfig, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente()) {
			UiUtil.add(this, ckClientesAtrasados, getLeft(), getNextY(), FILL);
		}
		if (LavenderePdaConfig.destacaClienteAtendidoMes && LavenderePdaConfig.isClienteAtendidoTelaListaCliente()) {
			UiUtil.add(this, ckDestacaClienteNaoAtendidoMes, getLeft(), getNextY());
		}
		if (onSelectClienteNovaAgendaVisita) {
			UiUtil.add(this, btFiltroAvancado, RIGHT - WIDTH_GAP_BIG, getNextY() + HEIGHT_GAP, UiUtil.getControlPreferredHeight());
			int largEdFiltrar = width - btFiltroAvancado.getX() - 1;
			UiUtil.add(this, edFiltro, getLeft(), SAME, FILL - largEdFiltrar, btFiltroAvancado.getHeight());
		} else {
			final boolean isApresentaBotaoMaisFiltros = isApresentaBotaoMaisFiltros();
			int widthEdFiltro = getWFill();
			if (isApresentaBotaoMaisFiltros) {
				widthEdFiltro -= UiUtil.getControlPreferredHeight();
			}
			UiUtil.add(this, edFiltro, getLeft(), getNextY(), widthEdFiltro);
			if (isApresentaBotaoMaisFiltros) {
				UiUtil.add(this, btFiltroAvancado, RIGHT - UiUtil.getFillRightSpace(), SAME, UiUtil.getControlPreferredHeight());
			}
		}
		listContainer.addSingleLineBarBottomContainer(sessaoTotalizadores);
		if (isApresentaBotaoMaisFiltros()) {
        	UiUtil.add(this, listContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, FILL);
		} else {
        	UiUtil.add(this, listContainer, LEFT, getNextY() + HEIGHT_GAP, FILL, FILL);
		}
        addTotalizadores();
	}

	private void addTotalizadores() {
		UiUtil.add(sessaoTotalizadores, ltQtClientes, LEFT + listContainer.getTotalizerGap(), CENTER, PREFERRED, PREFERRED);
        if (LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente()) {
        	boolean addedRight = false;
        	if (LavenderePdaConfig.isUsaTotalizadorClienteAtrasado()) {
        		UiUtil.add(sessaoTotalizadores, ltQtClientesAtrasados, RIGHT - listContainer.getTotalizerGap(), SAME, PREFERRED, PREFERRED);
    			addedRight = true;
        	}
        	if (LavenderePdaConfig.isUsaTotalizadorClienteAtendido()) {
        		UiUtil.add(sessaoTotalizadores, ltQtClientesAtendidos, addedRight ? CENTER : RIGHT - listContainer.getTotalizerGap(), SAME, PREFERRED, PREFERRED);
        	}
        }
	}
	
	private boolean isApresentaBotaoMaisFiltros() {
		return LavenderePdaConfig.isFiltrosFixoTelaListaClienteLigado() || LavenderePdaConfig.isFiltrosFixoTelaListaClienteDominioConfigurado();
	}

	@Override
	public Control getComponentToFocus() {
		return edFiltro;
	}

	public void carregaTotalizadoresClientes() throws SQLException {
		
		AsyncPool.getInstance().execute(new RunnableImpl() {
			
			@Override
			public void exec() {
				loadToalizadores();
				
			}
		});
	}
		
	private void loadToalizadores() {
		try {
			Vector clienteList = new Vector();
			if (flListInicialized) {
				Cliente clienteFilter = (Cliente) getDomainFilterSortable().clone();
				clienteFilter.limit = 0;
				clienteFilter.offset = 0;
				clienteFilter.isTotalizadorConsulta = true;
				prepareFilter(clienteFilter);
				clienteList = getClienteService().findClientes(dsFiltro, clienteFilter);
				clienteList = afterFindClientes(clienteFilter, clienteList);
			}
			int sizeTotal = ValueUtil.isNotEmpty(clienteList) ? clienteList.size() : 0;
			final Vector clienteListParameter = clienteList;
			MainLavenderePda.getInstance().runOnMainThread(() -> updateInterface(sizeTotal, clienteListParameter));
		} catch (Exception e) {
			ExceptionUtil.handle(e);
		}
	}

	private void updateInterface(int sizeTotal, final Vector clienteListParameter) {
		try {
			ltQtClientes.setText(StringUtil.getStringValueToInterface(sizeTotal) + " " + Messages.LIST_CLIENTE_REGISTROS);
			ltQtClientes.setID("ltQtClientes");
			ltQtClientes.reposition();
			if (LavenderePdaConfig.isUsaTotalizadoresEspecificosListaCliente()) {
				int nuAtendidos = 0;
				int nuAtrasados = 0;
				for (int i = 0; i < sizeTotal; i++) {
					Cliente cliente = (Cliente) clienteListParameter.items[i];
					if (LavenderePdaConfig.isUsaTotalizadorClienteAtendido() && cliente.isAtendido()) {
						nuAtendidos++;
					}
					if (LavenderePdaConfig.isUsaTotalizadorClienteAtrasado() && cliente.isAtrasado()) {
						nuAtrasados++;
					}
				}
				if (sizeTotal != 0) {
					if (LavenderePdaConfig.isUsaTotalizadorClienteAtendido()) {
						StringBuffer dsClienteAtendido = new StringBuffer();
						dsClienteAtendido.append(StringUtil.getStringValueToInterface(nuAtendidos));
						dsClienteAtendido.append(" ");
						dsClienteAtendido.append(Messages.LIST_CLIENTE_ATENDIDOS_ABREV);
						dsClienteAtendido.append(" = ");
						dsClienteAtendido.append((nuAtendidos * 100) / sizeTotal);
						dsClienteAtendido.append("%");
						ltQtClientesAtendidos.setText(dsClienteAtendido.toString());
						ltQtClientesAtendidos.reposition();
					}
					if (LavenderePdaConfig.isUsaTotalizadorClienteAtrasado()) {
						StringBuffer dsClienteAtrasado = new StringBuffer();
						dsClienteAtrasado.append(StringUtil.getStringValueToInterface(nuAtrasados));
						dsClienteAtrasado.append(" ");
						dsClienteAtrasado.append(Messages.LIST_CLIENTE_ATRASADOS_ABREV);
						dsClienteAtrasado.append(" = ");
						dsClienteAtrasado.append((nuAtrasados * 100) / sizeTotal);
						dsClienteAtrasado.append("%");
						ltQtClientesAtrasados.setText(dsClienteAtrasado.toString());
						ltQtClientesAtrasados.reposition();
					}
				} else {
					ltQtClientesAtendidos.setText(Messages.LIST_CLIENTE_ATENDIDOS_ABREV_DEFAULT);
					ltQtClientesAtrasados.setText(Messages.LIST_CLIENTE_ATRASADOS_ABREV_DEFAULT);
					ltQtClientesAtendidos.reposition();
					ltQtClientesAtrasados.reposition();
				}
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	@Override
    public void visibleState() {
    	super.visibleState();
    	barBottomContainer.setVisible(false);
    	barTopContainer.setVisible(!onSelectClienteFiltroWindow);
    }

	@Override
    protected int getTop() {
    	if (onSelectClienteFiltroWindow) {
    		return TOP;
    	} else {
    		return super.getTop();
    	}
    }

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (isComboClick(event)) {
					btFiltrarClick(event.target);
				} else if (event.target == btFiltroAvancado) {
					btFiltroAvancadoClick(event.target);
    			} else if (event.target == cbRepresentante) {
					cbRepresentanteChange();
                }
				break;
			}
			case ScrollEvent.SCROLL_END: {
				if (usaScrollInfinito && flListInicialized && listContainer.isEndScroll()) {
					listScroll();
				}
				break;
			}
		}
	}

	private boolean isComboClick(Event event) {
		return event.target == ckDestacaClienteNaoAtendidoMes 
				|| event.target == cbStatusCliente && realizaFiltroAutomatico(NU_FILTRO_STATUS) 
				|| event.target == ckClientesAtrasados 
				|| event.target == cbTipoCadastroClienteComboBox && realizaFiltroAutomatico(NU_FILTRO_TIPO_CADASTRO)  
				|| event.target == cbRede && realizaFiltroAutomatico(NU_FILTRO_REDE) 
				|| event.target == cbCategoria 
				|| event.target == cbTipoClienteRedeComboBox && realizaFiltroAutomatico(NU_FILTRO_TIPO_CLIENTE_REDE)  
				|| event.target == cbMarcador && realizaFiltroAutomatico(NU_FILTRO_MARCADOR)
				|| event.target == cbFornecedor && realizaFiltroAutomatico(NU_FILTRO_FORNECEDOR)
				|| event.target == cbDescProgressivoConfig && realizaFiltroAutomatico(NU_FILTRO_DESC_PROGRESSIVO_PERSONALIZADO);
	}

	private boolean realizaFiltroAutomatico(String valor) {
		return !LavenderePdaConfig.naoRealizaFiltroAutomaticoListaClientes().contains(valor);
	}

	@Override
	public void btHidedClickInList() throws SQLException {
		if (!MainLavenderePda.getInstance().isSistemaBloqueadoPendenteAtualizacao()) {
			setClienteSessao();
			((CadClienteMenuForm)getBaseCrudCadMenuForm()).setDomain(null);
			((CadClienteMenuForm)getBaseCrudCadMenuForm()).btNovoPedidoClick();
				}
			}
	
	@Override
	protected void filtrarClick() throws SQLException {
		super.filtrarClick();
		btFiltrarClick(edFiltro);
	}

	private void btFiltrarClick(Object target) throws SQLException {
		dsFiltro = edFiltro.getText();
		if (validateFiltro()) {
			filtrar();
			listContainer.setFocus();
		} else {
			if (!((target == ckClientesAtrasados) || (target == ckDestacaClienteNaoAtendidoMes))) {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.MSG_FILTRO_OBRIGATORIO_LIST_PRODUTO, qtMinimaCaracteres));
				qtMinimaCaracteres = 0;
			}
			clearGrid();
		}
		showRequestedFocus();
	}

	private void filtrar() throws SQLException {
		flListInicialized = true;
		list();
	}
	
	private void btFiltroAvancadoClick(Object target) throws SQLException {
		FiltroClienteAvancadoWindow filtroClienteAvancadoWindow = new FiltroClienteAvancadoWindow((Cliente) getDomainFilterAvancado(), onSelectClienteNovaAgendaVisita, onSelectClienteNovoPedido);
		filtroClienteAvancadoWindow.popup();
		if (filtroClienteAvancadoWindow.filtroAplicado) {
			btFiltrarClick(target);
		}
	}

	private boolean validateFiltro() {
		String filtro = dsFiltro;
		if (LavenderePdaConfig.usaPesquisaInicioString && dsFiltro.startsWith("*")) {
			filtro = dsFiltro.substring(1);
		}
		if (LavenderePdaConfig.getQtMinimaCaracteresListaClientes()> 0) {
			qtMinimaCaracteres = LavenderePdaConfig.getQtMinimaCaracteresListaClientes();
		}
		if ((filtro == null) || ((filtro.length() < qtMinimaCaracteres) && !isAlgumFiltroEspecialSelecionado())) {
			qtMinimaCaracteres = !isUsuarioSupervidorAndComboRepItemNull() ? qtMinimaCaracteres : 2;
			return false;
		} 
		return true;
	}

	private boolean isAlgumFiltroEspecialSelecionado() {
		return ckClientesAtrasados.isChecked() || ckDestacaClienteNaoAtendidoMes.isChecked();
	}

	private boolean isUsuarioSupervidorAndComboRepItemNull() {
		return SessionLavenderePda.isUsuarioSupervisor() && (ValueUtil.isEmpty(cbRepresentante.getValue()));
	}

	private void cbRepresentanteChange() throws SQLException {
		clearGrid();
		dsFiltro = edFiltro.getText();
		if (ValueUtil.isNotEmpty(cbRepresentante.getValue())) {
			SessionLavenderePda.setRepresentante(((SupervisorRep)cbRepresentante.getSelectedItem()).representante);
		} else {
			SessionLavenderePda.setRepresentante(null);
		}
		if (LavenderePdaConfig.usaStatusClienteVinculado) {
			cbStatusCliente.reloadByRepresentanteChange(SessionLavenderePda.getRepresentante().cdRepresentante);
			cbStatusCliente.setSelectedIndex(0);
		}
		if (validateFiltro() && realizaFiltroAutomatico(NU_FILTRO_REPRESENTANTE)) {
			filtrar();
		}
	}

	private void btOKClick() throws SQLException {
		Cliente clienteFilter = (Cliente) getCrudService().findByRowKey(getSelectedRowKey());
		if (clienteFilter != null) {
			clienteFilterWindow.cliente = clienteFilter;
			clienteFilterWindow.unpop();
		}
	}

    public void setClienteForm(ListClienteWindow listClienteWindow) {
    	this.clienteFilterWindow = listClienteWindow;
    }

    @Override
	public BaseDomain getSelectedDomain() throws SQLException {
		Cliente cliente = (Cliente) getCrudService().findByRowKey(getSelectedRowKey());
		cliente.loadStatusCliente();
		return cliente;
	}

    @Override
	public void singleClickInList() throws SQLException {
		setClienteSessao();
		showAvisoProdutoPendentesRetirada();
		ColetaGpsService.getInstance().encerraColetaGpsSeNecessario();
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		}
		Cliente clienteSession = SessionLavenderePda.getCliente();
		if (LavenderePdaConfig.sugereRegistroSaidaNaListaDeClientes() && SessionLavenderePda.visitaAndamento != null && SessionLavenderePda.visitaAndamento.isVisitaEmAndamento() && !ValueUtil.valueEquals(clienteSession.cdCliente, SessionLavenderePda.visitaAndamento.cdCliente)) {
			Cliente cliente = ClienteService.getInstance().getCliente(SessionLavenderePda.visitaAndamento.cdEmpresa, SessionLavenderePda.visitaAndamento.cdRepresentante, SessionLavenderePda.visitaAndamento.cdCliente);
			if (UiUtil.showConfirmYesNoMessage(Messages.TITULO_VISITA_ANDAMENTO, MessageUtil.getMessage(Messages.MSG_CLIENTE_VISITA_ANDAMENTO, cliente.toString()))) {
				CadClienteMenuForm.btRegistrarAtualizarSaidaClick();
			}
		}
		if (onSelectClienteNovoPedido) {
			if (!onReplicacaoPedido) {
				if (DivulgaInfoService.getInstance().apresentaDivulgaInfoCliente(clienteSession)) {
					new RelDivulgaInfoWindow(clienteSession, false).popup();
				}
				((CadClienteMenuForm) getBaseCrudCadMenuForm()).btNovoPedidoClick();
			}
			btOKClick();
		} else if (onSelectClienteNovaAgendaVisita) {
			CadAgendaVisitaWindow cadAgendaVisitaWindow = new CadAgendaVisitaWindow(clienteSession);
			cadAgendaVisitaWindow.popup();
			btOKClick();
		} else {
			if (onSelectClienteFiltroWindow) {
				btOKClick();
			} else {
				if (LavenderePdaConfig.sugereRegistroChegadaNoMenuCliente()) {
					sugereRegistroChegada();
				}
				super.singleClickInList();
			}
		}
	}

	private void showAvisoProdutoPendentesRetirada() throws SQLException {
		if (LavenderePdaConfig.isUsaAvisoVendaProdutosPendentesRetiradaSelecionarCliente()) {
			Date dtMaxRetirada = ProdutoRetiradaService.getInstance().getDtMaxRetiradaProduto((Cliente) getSelectedDomain(), null);
			if (dtMaxRetirada != null) {
				UiUtil.showInfoMessage(MessageUtil.getMessage(Messages.PRODUTORETIRADA_LABEL_MSG_AVISO_CLIENTE, dtMaxRetirada));
			}
		}
	}

	private void setClienteSessao() throws SQLException {
		SessionLavenderePda.setCliente((Cliente) getSelectedDomain());
		if (SessionLavenderePda.isUsuarioSupervisor()) {
			if (SessionLavenderePda.getCliente() != null) {
				isAllSelected = ValueUtil.isEmpty(cbRepresentante.getValue());
				if (isAllSelected || !cbRepresentante.getValue().equalsIgnoreCase(SessionLavenderePda.getCliente().cdRepresentante)) {
					Representante rep = RepresentanteService.getInstance().getRepresentanteById(SessionLavenderePda.getCliente().cdRepresentante);
					if (rep != null) {
						SessionLavenderePda.setRepresentante(rep);
					}
				}
			}
		}
	}
	
	private void sugereRegistroChegada() throws SQLException {
		if (SessionLavenderePda.visitaAndamento == null) {
			Cliente cliente = (Cliente) getSelectedDomain();
			if (UiUtil.showConfirmYesNoMessage(CadClienteMenuForm.getRegistrarChegadaMessage(cliente.isNovoCliente()))) {
				if (!(((CadClienteMenuForm) getBaseCrudCadMenuForm()).checkVisitOfLastValidDay())) {
					CadClienteMenuForm.btRegistrarAtualizarChegadaClick(null);
				}
			}
		}
	}

	protected Vector getClienteListBySupervisor(Vector listCliente) throws SQLException {
		Vector listSupRepresentantes = SupervisorRepService.getInstance().getRepresentantesBySupervisor(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante);
		Vector clienteListBySupervisor = new Vector();
		if (ValueUtil.isNotEmpty(listSupRepresentantes)) {
			for (int i = 0; i < listCliente.size(); i++) {
				Cliente cli = (Cliente) listCliente.items[i];
				boolean valido = false;
				for (int j = 0; j < listSupRepresentantes.size(); j++) {
					SupervisorRep sup = (SupervisorRep) listSupRepresentantes.items[j];
					if (cli.cdRepresentante.equals(sup.cdRepresentante)) {
						valido = true;
						break;
					}
				}
				if (valido) {
					clienteListBySupervisor.addElement(cli);
				}
			}
		} else {
			clienteListBySupervisor = listCliente;
		}
		return clienteListBySupervisor;
	}


	public EditFiltro getEditFiltro() {
		return  edFiltro;
	}

	@Override
	protected void resizeListToFullScreen() {
		listContainer.setRect(0, this.getTop(), FILL, FILL);
		listContainer.initUI();
		listContainer.repaintContainers();
	}
	
	@Override
	protected int getMaxResult() {
		return LavenderePdaConfig.getNuRegistrosClientesMostradosPorVezNaLista();
	}
}
