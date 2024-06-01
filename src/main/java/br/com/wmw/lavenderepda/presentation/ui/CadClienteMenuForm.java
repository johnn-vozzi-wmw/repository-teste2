package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.HashMap;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.domain.Menu;
import br.com.wmw.framework.business.domain.Tela;
import br.com.wmw.framework.business.domain.dto.WtoolsParametrosDTO;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.BaseCrudCadMenuForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.LabelPhone;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.MenuCarrousel;
import br.com.wmw.framework.presentation.ui.ext.SessionContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox.TYPE_MESSAGE;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.SortUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.framework.util.WtoolsUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.AgendaVisita;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.ClienteAtua;
import br.com.wmw.lavenderepda.business.domain.ClienteChurn;
import br.com.wmw.lavenderepda.business.domain.ClienteInativacao;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.ContatoErp;
import br.com.wmw.lavenderepda.business.domain.ContratoCliente;
import br.com.wmw.lavenderepda.business.domain.DapMatricula;
import br.com.wmw.lavenderepda.business.domain.DescProgressivoConfig;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.FotoCliente;
import br.com.wmw.lavenderepda.business.domain.FotoClienteErp;
import br.com.wmw.lavenderepda.business.domain.LavendereBaseDomain;
import br.com.wmw.lavenderepda.business.domain.NfDevolucao;
import br.com.wmw.lavenderepda.business.domain.NovoCliente;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SaldoVendaRep;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.StatusAgenda;
import br.com.wmw.lavenderepda.business.domain.StatusCliente;
import br.com.wmw.lavenderepda.business.domain.StatusRentCli;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.domain.Visita;
import br.com.wmw.lavenderepda.business.domain.VisitaFoto;
import br.com.wmw.lavenderepda.business.service.AcessoMaterialService;
import br.com.wmw.lavenderepda.business.service.AgendaVisitaService;
import br.com.wmw.lavenderepda.business.service.CargaPedidoService;
import br.com.wmw.lavenderepda.business.service.ClienteAtuaService;
import br.com.wmw.lavenderepda.business.service.ClienteChurnService;
import br.com.wmw.lavenderepda.business.service.ClienteEnderecoService;
import br.com.wmw.lavenderepda.business.service.ClienteInativacaoService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ColetaGpsService;
import br.com.wmw.lavenderepda.business.service.ConexaoPdaService;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import br.com.wmw.lavenderepda.business.service.ConsignacaoService;
import br.com.wmw.lavenderepda.business.service.ContatoErpService;
import br.com.wmw.lavenderepda.business.service.ContratoClienteService;
import br.com.wmw.lavenderepda.business.service.DapMatriculaService;
import br.com.wmw.lavenderepda.business.service.DescProgressivoConfigService;
import br.com.wmw.lavenderepda.business.service.DivulgaInfoService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.EnderecoGpsPdaService;
import br.com.wmw.lavenderepda.business.service.ErroEnvioService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.FotoClienteErpService;
import br.com.wmw.lavenderepda.business.service.FotoClienteService;
import br.com.wmw.lavenderepda.business.service.GiroProdutoService;
import br.com.wmw.lavenderepda.business.service.NfDevolucaoService;
import br.com.wmw.lavenderepda.business.service.NotaCreditoService;
import br.com.wmw.lavenderepda.business.service.NotificacaoPdaService;
import br.com.wmw.lavenderepda.business.service.NovoClienteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.PesquisaAppService;
import br.com.wmw.lavenderepda.business.service.PlataformaVendaClienteService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.RotaEntregaService;
import br.com.wmw.lavenderepda.business.service.SacService;
import br.com.wmw.lavenderepda.business.service.SaldoVendaRepService;
import br.com.wmw.lavenderepda.business.service.StatusOrcamentoService;
import br.com.wmw.lavenderepda.business.service.StatusRentCliService;
import br.com.wmw.lavenderepda.business.service.TelaLavendereService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.business.service.TituloFinanceiroService;
import br.com.wmw.lavenderepda.business.service.VisitaFotoService;
import br.com.wmw.lavenderepda.business.service.VisitaPedidoService;
import br.com.wmw.lavenderepda.business.service.VisitaService;
import br.com.wmw.lavenderepda.business.validation.ClienteInadimplenteException;
import br.com.wmw.lavenderepda.integration.dao.pdbx.VisitaFotoDao;
import br.com.wmw.lavenderepda.presentation.ui.combo.EmpresaClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.FechamentoDiarioUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.ImageSliderClienteWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.LiberacaoSenhaWindow;
import br.com.wmw.lavenderepda.presentation.ui.ext.PedidoUiUtil;
import br.com.wmw.lavenderepda.presentation.ui.ext.WindowUtil;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.gfx.Color;
import totalcross.ui.image.Image;
import totalcross.util.Date;
import totalcross.util.Hashtable;
import totalcross.util.Vector;

public class CadClienteMenuForm extends BaseCrudCadMenuForm {
	
	private static final int CDMENU_CLIENTE = 1;

	private LabelValue lbNmRazaoSocial;
    private LabelPhone lbNuFone;
    private LabelValue lbDsCidade;
    private LabelValue lbNmFantasia;
	private ButtonAction btNovoPedido;
	protected ButtonAction btTornarCliente;
	protected ButtonAction btInativarProspect;

	//--
	private BaseButton btDadosCliente;
	private BaseButton btFichaFinanceira;
	private BaseButton btUltimosPedidos;
	private BaseButton btRecadosCliente;
	private BaseButton btMetaCliente;
	private BaseButton btRelDifPedidos;
	private BaseButton btRelContaCorrente;
	private BaseButton btGiroProduto;
	private BaseButton btPesquisaMercado;
	private BaseButton btContato;
	private BaseButton btCestaPositivCli;
	private BaseButton btVisita;
	private BaseButton btExtratoCCCli;
	private BaseButton btFaceamento;
	private BaseButton btForecast;
	private BaseButton btGrade;
	private BaseButton btConsignacao;
	private BaseButton btPagamento;
	private BaseButton btMotivoVisita;
	private BaseButton btCadastroCoordenadas;
	private BaseButton btFotosCliente;
	private BaseButton btAtualizaDadosCliente;
	private BaseButton btNavegacaoMapa;
	private BaseButton btRegistrarAtualizarChegada;
	private BaseButton btDesfazerChegada;
	private BaseButton btRegistrarAtualizarSaida;
	private BaseButton btEnderecosCliente;
	private BaseButton btListaSacs;
	private BaseButton btReagendarAgenda;
	private BaseButton btTransferirAgenda;
	private BaseButton btNovaAgendaVisita;
	private Hashtable buttonsTipoPedido;
	private BaseButton btIconeRentabilidade;
	private BaseButton btProdutoRetirada;
	private BaseButton btPesquisaCliente;
	private BaseButton btRequisicaoServ;
	private BaseButton btPesquisaDeMercadoProdConc;
	private BaseButton btProdutoClienteCod;
	private BaseButton btMaisParceiroIconic;
	private BaseButton btDescProgressivo;
	private BaseButton btDivulgaInfo;
	private BaseButton btDap;
	private BaseButton btPoliticaBonificacao;
	private BaseButton btAtendimentoHist;

	private ListAgendaVisitaForm listAgendaVisitaForm;
	private Visita visita;
	private AgendaVisita agendaVisita;
	private static String rowKeyDaProximaSequencia;
	private static ListPesquisaClienteForm listPesquisaClienteForm;

	// Variáveis de controle
	private String cdTipoPedido;
	public String cdMotVisitaForaOrdem;
	public String obsVisitaForaOrdem;

    public CadClienteMenuForm() {
        super(Messages.CLIENTE_NOME_ENTIDADE);
        setBaseCrudCadForm(new CadClienteDynForm());
        lbNmRazaoSocial = new LabelValue("@@@@@@@@@@");
        lbNmRazaoSocial.setID("lbNmRazaoSocial");
        lbNmRazaoSocial.setForeColor(ColorUtil.sessionContainerForeColor);
        lbNuFone = new LabelPhone("@@@@@@@@@@", RIGHT);
        lbNuFone.setFont(UiUtil.defaultFontSmall);
        lbNuFone.setForeColor(ColorUtil.baseForeColorSystem);
        lbNmFantasia = new LabelValue("@@@@@@@@@@");
        lbNmFantasia.setFont(UiUtil.defaultFontSmall);
        lbNmFantasia.setForeColor(Color.darker(ColorUtil.sessionContainerForeColor, 20));
        lbDsCidade = new LabelValue("@@@@@@@@@@");
        lbDsCidade.setFont(UiUtil.defaultFontSmall);
        lbDsCidade.setForeColor(Color.darker(ColorUtil.sessionContainerForeColor, 20));
        btNovoPedido = new ButtonAction(Messages.BOTAO_NOVO_PEDIDO, "images/novopedido.png");
        btTornarCliente = new ButtonAction(Messages.BOTAO_TORNAR_CLIENTE, "images/tornarCliente.png");
		if (LavenderePdaConfig.permiteInativarClienteProspeccao) {
			btInativarProspect = new ButtonAction(Messages.BOTAO_SOLICITAR_INATIVACAO, "images/inativarCliente.png");
		}
        btDadosCliente = new BaseButton("");
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente) {
			btIconeRentabilidade = new BaseButton(Messages.RENTABILIDADEFAIXA_MSG_NENHUMA_RENT_CADASTRADA, getEmptyIcon(), LEFT, WIDTH_GAP);
			btIconeRentabilidade.useBorder = false;
			btIconeRentabilidade.setForeColor(Color.BRIGHT);
			btIconeRentabilidade.eventsEnabled = false;
		}
        //--
        menuFuncionalidades = new MenuCarrousel();
		menuFuncionalidades.percEspacamento = 20;
		menuFuncionalidades.drawnBackground = true;
		menuFuncionalidades.useTitle = false;
		menuFuncionalidades.heightButtons = (UiUtil.getScreenHeightOrigin() / 100) * 10;
		menuFuncionalidades.generateButtonsAsScMenuEvent = false;
		buttonsTipoPedido = new Hashtable(5);
		setAgendaVisita(new AgendaVisita());

    }

    public void setAgendaVisitaListForm(ListAgendaVisitaForm listAgendaVisita) {
    	listAgendaVisitaForm = listAgendaVisita;
    }

    //-----------------------------------------------

    @Override
	public String getEntityDescription() {
    	return title;
    }

    @Override
	protected CrudService getCrudService() throws SQLException {
        return ClienteService.getInstance();
    }
	
	@Override
	public void setDomain(BaseDomain baseDomain) {
		super.setDomain(baseDomain);
	}

	private Cliente getCliente() throws SQLException {
		if (getDomain() instanceof Cliente) {
			return (Cliente)getDomain();
		}
		return SessionLavenderePda.getCliente();
	}


    @Override
	protected void domainToScreen(BaseDomain domain) throws SQLException {
		Cliente cliente = null;
		if (domain instanceof Cliente) {
			cliente = (Cliente)domain;
		} else if (domain instanceof AgendaVisita) {
			AgendaVisita agenda = (AgendaVisita) getDomain();
			btDadosCliente.setVisible(!((agenda.statusAgendaCdPositivado == (StatusAgenda.STATUSAGENDA_CDPOSITIVADO)) && LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia));
			cliente = agenda.cliente;
		} else {
			return;
		}
		lbNmRazaoSocial.setText(cliente.toString());
		lbNuFone.setText(cliente.nuFone);
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
			lbNmFantasia.setText(cliente.nmFantasia);
		}
		if (LavenderePdaConfig.exibeCidadeEstadoNoMenuCliente) {
			lbDsCidade.setText(StringUtil.getStringValue(cliente.dsCidadeComercial) + StringUtil.getStringValue(ValueUtil.isNotEmpty(cliente.dsEstadoComercial) ? " - " + cliente.dsEstadoComercial : FrameworkMessages.CAMPO_VAZIO));
		}
		btNovoPedido.setVisible(!getCliente().isModoDeProspeccao());
		btTornarCliente.setVisible(getCliente().isModoDeProspeccao() && LavenderePdaConfig.isUsaCadastroNovoCliente() && !VisitaService.getInstance().isExisteVisitaPositivadaParaClienteProspect());
		if (LavenderePdaConfig.permiteInativarClienteProspeccao) {
			btInativarProspect.setVisible(getCliente().isModoDeProspeccao() && LavenderePdaConfig.isUsaCadastroNovoCliente() && !VisitaService.getInstance().isExisteVisitaPositivadaParaClienteProspect());
		}

		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente) {
			int corIcon = Color.BRIGHT;
			String dsRentabilidade = Messages.RENTABILIDADEFAIXA_MSG_NENHUMA_RENT_CADASTRADA;
			if (ValueUtil.isNotEmpty(cliente.cdStatusRentCli)) {
				StatusRentCli status = StatusRentCliService.getInstance().getStatusRentabilidade(cliente.cdEmpresa, cliente.cdStatusRentCli);
				if (status != null) {
					corIcon = Color.getRGB(status.vlRIcon, status.vlGIcon, status.vlBIcon);
					dsRentabilidade = StringUtil.getStringValue(status.toString());
				} else {
					corIcon = Color.BRIGHT;
				}
			}
			btIconeRentabilidade.setText(dsRentabilidade);
			btIconeRentabilidade.setImage(UiUtil.getColorfulImage("images/rentabilidade.png",  UiUtil.getLabelPreferredHeight() / 7 * 6, UiUtil.getLabelPreferredHeight() / 7 * 6, corIcon));
			btIconeRentabilidade.setForeColor(corIcon);
			btIconeRentabilidade.setRect(RIGHT, SAME, PREFERRED, UiUtil.getLabelPreferredHeight());
		}
    }

	protected void onFormStart() throws SQLException {
    	SessionContainer containerDadosCli = new SessionContainer();
    	UiUtil.add(this, containerDadosCli, LEFT, getTop(), FILL, UiUtil.getLabelPreferredHeight() + (HEIGHT_GAP * 2));
		UiUtil.add(containerDadosCli, lbNmRazaoSocial, getLeft(), AFTER + HEIGHT_GAP, getWFill(), PREFERRED);
		UiUtil.add(containerDadosCli, lbNuFone, getRight(), AFTER, lbNuFone.fm.stringWidth("04899995404400000000"), PREFERRED);
		if (LavenderePdaConfig.exibeNomeFantasiaListaClientes()) {
        	UiUtil.add(containerDadosCli, lbNmFantasia, getLeft(), SAME, getWFill() - lbNuFone.getWidth() - WIDTH_GAP, PREFERRED);
        	if (LavenderePdaConfig.exibeCidadeEstadoNoMenuCliente) {
        		UiUtil.add(containerDadosCli, lbDsCidade, getLeft(), AFTER, getWFill(), PREFERRED);
        	}
		} else if (LavenderePdaConfig.exibeCidadeEstadoNoMenuCliente){
    		UiUtil.add(containerDadosCli, lbDsCidade, getLeft(), SAME, getWFill() - lbNuFone.getWidth() - WIDTH_GAP, PREFERRED);
		}
		if (LavenderePdaConfig.isUsaRentabilidadeNoPedido() && LavenderePdaConfig.usaDestaqueStatusRentabilidadeCliente) {
			UiUtil.add(containerDadosCli, btIconeRentabilidade, RIGHT, AFTER, PREFERRED, UiUtil.getLabelPreferredHeight());
			btIconeRentabilidade.setBackColor(containerDadosCli.getBackColor());
		}
        containerDadosCli.resizeHeight();
        containerDadosCli.resetSetPositions();
        containerDadosCli.setRect(KEEP, KEEP, FILL, containerDadosCli.getHeight() + HEIGHT_GAP);
    	// MENU
    	montaMenuFuncionalidades();
    	UiUtil.add(this, menuFuncionalidades, getLeft(), AFTER, getWFill(), FILL - barBottomContainer.getHeight());
		//--
    	if (!LavenderePdaConfig.usaTipoPedidoComoOpcaoMenuCliente) {
    		UiUtil.add(barBottomContainer, btNovoPedido, 5);
    	}
		if (LavenderePdaConfig.isUsaCadastroNovoCliente() && LavenderePdaConfig.usaClienteEmModoProspeccao && LavenderePdaConfig.permiteInativarClienteProspeccao) {
			UiUtil.add(barBottomContainer, btInativarProspect, 4);
		}
		if (LavenderePdaConfig.isUsaCadastroNovoCliente() && LavenderePdaConfig.usaClienteEmModoProspeccao) {
			UiUtil.add(barBottomContainer, btTornarCliente, 5);
		}
    }

    public void montaMenuFuncionalidades() throws SQLException {
    	menuFuncionalidades.removeAllOptions();
    	//--
    	if (!LavenderePdaConfig.ocultaAcessoInfoCliente || listAgendaVisitaForm != null) {
    		btDadosCliente = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_TITULO_CADASTRO);
    	}
    	if (LavenderePdaConfig.usaHistoricoAtendimentoUnificado) {
    		btAtendimentoHist = menuFuncionalidades.addOptionMenu(Messages.ATENDIMENTOHIST_TITLE);
    	}
    	if (LavenderePdaConfig.usaMultiplosEnderecosCliente()) {
			btEnderecosCliente = menuFuncionalidades.addOptionMenu(Messages.CLIENTEENDERECO_LABEL_ENDERECOS);
		}
    	if (VmUtil.isAndroid() && LavenderePdaConfig.isUsaNavegacaoGpsNoMapa()) {
    		btNavegacaoMapa = menuFuncionalidades.addOptionMenu(Messages.NAVEGACAO);
    	}
    	if ((LavenderePdaConfig.isUsaModuloContatoCliente() && !getCliente().isNovoCliente() && !getCliente().isClienteDefaultParaNovoPedido())
				|| (LavenderePdaConfig.isUsaContatosNovoCliente() && getCliente().isNovoCliente())) {
    		btContato = menuFuncionalidades.addOptionMenu(Messages.CONTATO_NOME_ENTIDADE);
    	}
    	if ((LavenderePdaConfig.isUsaRegistroManualDeVisitaSemAgenda() && listAgendaVisitaForm == null) && (!(listAgendaVisitaForm == null && isClientePossuiAgendaVisitaPendente())) && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
    		btVisita = menuFuncionalidades.addOptionMenu(Messages.VISITA_NOME_ENTIDADE);
    	}
    	if (isClientePossuiAgendaVisitaPendente() && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
    		btMotivoVisita = menuFuncionalidades.addOptionMenu(Messages.VISITA_LABEL_VISITA_MOTIVO);
    	}
    	if (LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
    		Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
    		if (visitaEmAndamento != null && ValueUtil.valueEquals(visitaEmAndamento.cdCliente, getCliente().cdCliente)) {
    			btRegistrarAtualizarSaida = menuFuncionalidades.addOptionMenu(Messages.BOTAO_REGISTRAR_SAIDA);
    			if (!visitaEmAndamento.isVisitaEnviadaServidor()) {
    				btRegistrarAtualizarChegada = menuFuncionalidades.addOptionMenu(Messages.BOTAO_ATUALIZAR_CHEGADA);
    				if (!VisitaPedidoService.getInstance().isVisitaPossuiPedido(visitaEmAndamento)) {
    					btDesfazerChegada = menuFuncionalidades.addOptionMenu(Messages.BOTAO_DESFAZER_CHEGADA);
					}
    			}
    		} else if (visitaEmAndamento == null) {
    			btRegistrarAtualizarChegada = menuFuncionalidades.addOptionMenu(Messages.BOTAO_REGISTRAR_CHEGADA);
    			Visita ultimoRegistroSaidaDoCliente = VisitaService.getInstance().findLastRegistroSaidaDoCliente(getCliente().cdCliente);
    			if (ValueUtil.valueEquals(ultimoRegistroSaidaDoCliente.cdCliente, getCliente().cdCliente) && !ultimoRegistroSaidaDoCliente.isVisitaEnviadaServidor()) {
					Visita ultimoRegistroDeChegadaDoRepresentante = VisitaService.getInstance().findLastRegistroChegadaRepresentante();
					if (VisitaService.getInstance().permiteAtualizarRegistroSaida(ultimoRegistroDeChegadaDoRepresentante, ultimoRegistroSaidaDoCliente)) {
						btRegistrarAtualizarSaida = menuFuncionalidades.addOptionMenu(Messages.BOTAO_ATUALIZAR_SAIDA);
					}
				}
    		}
    	}
    	if (LavenderePdaConfig.isUsaAgendaDeVisitas() && LavenderePdaConfig.usaReagendamentoAgendaVisita && (isClientePossuiAgendaVisitaPendente() || (listAgendaVisitaForm != null && agendaVisita != null && StatusAgenda.STATUSAGENDA_CDAVISITAR == agendaVisita.statusAgendaCdPositivado))) {
			btReagendarAgenda = menuFuncionalidades.addOptionMenu(Messages.BT_REAGENDAR_AGENDA);
		}
    	if (SessionLavenderePda.isUsuarioSupervisor() && LavenderePdaConfig.isUsaAgendaDeVisitas() && LavenderePdaConfig.usaTransferenciaAgendaVisitaParaAgendaAtendimento && (isClientePossuiAgendaVisitaPendente() || (listAgendaVisitaForm != null && agendaVisita != null && StatusAgenda.STATUSAGENDA_CDAVISITAR == agendaVisita.statusAgendaCdPositivado))) {
    		btTransferirAgenda = menuFuncionalidades.addOptionMenu(Messages.BT_TRANSFERIR_AGENDA);
    	}
    	if (LavenderePdaConfig.isUsaAgendaDeVisitas() && LavenderePdaConfig.usaCadastroAgendaVisita) {
    		btNovaAgendaVisita = menuFuncionalidades.addOptionMenu(Messages.NOVA_AGENDA_VISITA);
    	}
    	if (LavenderePdaConfig.usaApresentacaoProdutosPendentesRetirada) {
    		btProdutoRetirada = menuFuncionalidades.addOptionMenu(Messages.PRODUTORETIRADA_NOME_BT);
    	}
    	if ((LavenderePdaConfig.isUsaPesquisaCliente() && !getCliente().isNovoCliente() && !getCliente().isClienteDefaultParaNovoPedido()) 
			    || (LavenderePdaConfig.isUsaPesquisaNovoCliente() && getCliente().isNovoCliente())) {
		    btPesquisaCliente = menuFuncionalidades.addOptionMenu(Messages.PESQUISAS_CLIENTE_PESQUISAS);
	    }
    	if (LavenderePdaConfig.isPermiteAplicarFiltros()) {
    		btRecadosCliente = menuFuncionalidades.addOptionMenu(Messages.RECADO_NOME_MENU);
    	}
    	if (!getCliente().isModoDeProspeccao() && !getCliente().isNovoCliente() && !getCliente().isClienteDefaultParaNovoPedido()) {
    		if (!LavenderePdaConfig.isOcultaFichaFinanceira()) {
        		btFichaFinanceira = menuFuncionalidades.addOptionMenu(Messages.BOTAO_FICHAFINANCEIRA);
        	}
        	if (!LavenderePdaConfig.isOcultaTodosAcessosRelUltimosPedidos()) {
        		btUltimosPedidos = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_ULTIMOS_PEDIDOS);
        	}
        	if (LavenderePdaConfig.usaRelatorioMetaVendaCliente) {
        		btMetaCliente = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_META_VENDA);
        	}
    		if (LavenderePdaConfig.relDiferencasPedido) {
    			btRelDifPedidos = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_PEDIDOS_DIF);
    		}
    		if (LavenderePdaConfig.mostraRelatorioContaCorrenteCliente) {
    			btRelContaCorrente = menuFuncionalidades.addOptionMenu(Messages.CONTACORRENTECLI_LABEL_EXTRATO);
    		}
    		if (LavenderePdaConfig.isUsaGiroProduto()) {
    			btGiroProduto = menuFuncionalidades.addOptionMenu(Messages.GIROPRODUTO_NOME_ENTIDADE);
    		}
    		if (LavenderePdaConfig.usaPesquisaMercado || LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
    			btPesquisaMercado = menuFuncionalidades.addOptionMenu(Messages.PESQUISAMERCADO_NOME_ENTIDADE);
    		}
    		if (LavenderePdaConfig.usaConfigPesquisaMercadoProdutoConcorrente()) {
    			btPesquisaDeMercadoProdConc = menuFuncionalidades.addOptionMenu(Messages.PESQUISA_MERCADO_PROD_CONC_NOME_ENTIDADE);
		    }
    		if (LavenderePdaConfig.usaCampanhaDeVendasPorCestaDeProdutos()) {
    			btCestaPositivCli = menuFuncionalidades.addOptionMenu(Messages.CESTAPOSITCLIENTE_NOME_ENTIDADE);
    		}
			if (LavenderePdaConfig.usaCCClientePorTipoPedido) {
				btExtratoCCCli = menuFuncionalidades.addOptionMenu(Messages.CCCLIPORTIPO_MENU_EXTRATO_CC);
			}
			if (LavenderePdaConfig.isUsaListaSacCliente()) {
    			btListaSacs = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_LISTA_SACS);
    		}
			if (LavenderePdaConfig.usaFaceamento()) {
				btFaceamento = menuFuncionalidades.addOptionMenu(Messages.FACEAMENTO_NOME_ENTIDADE);
			}
			if (LavenderePdaConfig.usaContratoForecast) {
				btForecast = menuFuncionalidades.addOptionMenu(Messages.FORECAST_NOME_ENTIDADE);
			}
			if (LavenderePdaConfig.usaControleEstoqueGondola) {
				btGrade = menuFuncionalidades.addOptionMenu(Messages.GRADE_NOME_ENTIDADE);
			}
			if (LavenderePdaConfig.usaModuloConsignacao) {
				btConsignacao = menuFuncionalidades.addOptionMenu(Messages.CONSIGNACAO_NOME_ENTIDADE);
			}
			if (LavenderePdaConfig.isUsaModuloPagamento()) {
				btPagamento = menuFuncionalidades.addOptionMenu(Messages.PAGAMENTO_NOME_ENTIDADE);
			}
			if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasCliente()) {
				btCadastroCoordenadas = menuFuncionalidades.addOptionMenu(Messages.CAD_COORD_TITULO);
			}
			if (LavenderePdaConfig.permiteFotoMenuCliente) {
				btFotosCliente = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_LABEL_SLIDEFOTOS);
			}
			if (LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisCliente()) {
				btAtualizaDadosCliente = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_LABEL_ATUALIZA_DADOS);
			}
			if (MainMenu.isTelaAutorizada(MainMenu.CDTELA_REQUISICAO_SERV)) {
				btRequisicaoServ = menuFuncionalidades.addOptionMenu(Messages.REQUISICAO_SERV_NOME_ENTIDADE);				
			}
			if (LavenderePdaConfig.usaTipoPedidoComoOpcaoMenuCliente) {
				buttonsTipoPedido = new Hashtable(5);
				//--
				TipoPedido tipoPedido = new TipoPedido();
				tipoPedido.cdEmpresa = SessionLavenderePda.cdEmpresa;
				tipoPedido.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
				Vector list = TipoPedidoService.getInstance().findAllByExample(tipoPedido);
				list.qsort();
				//--
				tipoPedido.cdTipoPedido = getCliente().cdTipoPedido;
				int indexOf = list.indexOf(tipoPedido);
				if (indexOf != -1) {
					tipoPedido = (TipoPedido)list.items[indexOf];
					buttonsTipoPedido.put(menuFuncionalidades.addOptionMenu(StringUtil.getStringValue(tipoPedido.dsTipoPedido)), tipoPedido.cdTipoPedido);
					list.removeElement(tipoPedido);
				}
				for (int i = 0; i < list.size(); i++) {
					tipoPedido = (TipoPedido)list.items[i];
					buttonsTipoPedido.put(menuFuncionalidades.addOptionMenu(StringUtil.getStringValue(tipoPedido.dsTipoPedido)), tipoPedido.cdTipoPedido);
				}
			}
	    	if (LavenderePdaConfig.usaCodigosProdutosParaClientes()) {
	    		btProdutoClienteCod = menuFuncionalidades.addOptionMenu(Messages.CAD_PROD_CLI_COD_MENU);
	    	}
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado) {
				btDescProgressivo = menuFuncionalidades.addOptionMenu(Messages.DESC_PROG_BTN_CLI_NAME);
			}
		    if (LavenderePdaConfig.usaDivulgaInformacao) {
			    btDivulgaInfo = menuFuncionalidades.addOptionMenu(Messages.CLIENTEMENU_DIVULGAINFO_OPTION);
		    }
		    if (isExibeBtDap()) {
		    	btDap = menuFuncionalidades.addOptionMenu(Messages.CLIENTEMENU_RELATORIO_DAP);
		    }
		    if (LavenderePdaConfig.isUsaPoliticaBonificacaoClientes()) {
		    	btPoliticaBonificacao = menuFuncionalidades.addOptionMenu(Messages.TITULO_POLITICAS_BONIFICACAO);
		    }
    	} else if (getCliente().isNovoCliente()) {
        	if (!LavenderePdaConfig.isOcultaTodosAcessosRelUltimosPedidos()) {
        		btUltimosPedidos = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_ULTIMOS_PEDIDOS);
        	}
    		if (LavenderePdaConfig.permitePesquisaMercadoNovoCliente) {
    			btPesquisaMercado = menuFuncionalidades.addOptionMenu(Messages.PESQUISAMERCADO_NOME_ENTIDADE);
    		}
			if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasNovoCliente()) {
				btCadastroCoordenadas = menuFuncionalidades.addOptionMenu(Messages.CAD_COORD_TITULO);
			}
			if (LavenderePdaConfig.isUsaCadastroFotoNovoCliente()) {
				btFotosCliente = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_LABEL_SLIDEFOTOS);
			}
			if (LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisCliente()) {
				btAtualizaDadosCliente = menuFuncionalidades.addOptionMenu(Messages.CLIENTE_LABEL_ATUALIZA_DADOS);
			}
    	}
	    if (LavenderePdaConfig.usaAppExternoMenuCliente) {
		    btMaisParceiroIconic = menuFuncionalidades.addOptionMenu(Messages.CAD_CLI_BT_APP_EXTERNO);
	    }
    	adicionaMenusDinamicos(CDMENU_CLIENTE);
	}

	private boolean isExibeBtDap() throws SQLException {
		if (!LavenderePdaConfig.isUsaLaudoDap()) return false;
		Cliente cliente =  getCliente();
		DapMatricula dapMatricula = new DapMatricula();
		dapMatricula.cdEmpresa = cliente.cdEmpresa;
		dapMatricula.cdCliente = cliente.cdCliente;
		return ValueUtil.isNotEmpty(DapMatriculaService.getInstance().findAllByExample(dapMatricula));
	}
    
    @Override
    protected Tela findTelaDinamica(Menu menu) throws SQLException {
    	Tela tela = TelaLavendereService.getInstance().getTela(menu.cdSistema, menu.cdTela);
    	Cliente cliente = SessionLavenderePda.getCliente();
    	tela.valoresFiltroTelaPaiHash = new HashMap<String, String>();
    	tela.valoresFiltroTelaPaiHash.put(LavendereBaseDomain.NMCOLUNA_CDEMPRESA.toLowerCase(), cliente.cdEmpresa);
    	tela.valoresFiltroTelaPaiHash.put(LavendereBaseDomain.NMCOLUNA_CDREPRESENTANTE.toLowerCase(), cliente.cdRepresentante);
    	tela.valoresFiltroTelaPaiHash.put(Cliente.NMCOLUNA_CDCLIENTE.toLowerCase(), cliente.cdCliente);
    	tela.transfereValoresFiltroParaCamposTelaFilho();
    	return tela;
    }
    
    @Override
    protected void showRelDinamica(Tela tela) throws SQLException {
    	show(new ListRelDinamicoLavendereForm(tela));
    }
    
	private boolean isClientePossuiAgendaVisitaPendente() throws SQLException {
		if (agendaVisita == null) {
			agendaVisita = new AgendaVisita();
		}
		return AgendaVisitaService.getInstance().findAgendaVisitaPendente(getCliente().cdRepresentante, getCliente().cdCliente, agendaVisita.dtAgendaAtual) != null;
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		super.onFormEvent(event);
    	switch (event.type) {
	    	case ControlEvent.PRESSED: {
	    		if (event.target == btNovoPedido) {
	    			btNovoPedidoClick();
	    		} else if (event.target == btTornarCliente) {
	    			btTornarClienteClick();
				} else if (event.target == btInativarProspect) {
					btInativarProspectClick();
				} else if (event.target == btDadosCliente) {
	    			btInfoClienteClick();
	    		} else if (event.target == btFichaFinanceira) {
	    			btFinanceiroClick();
	    		} else if (event.target == btTransferirAgenda) {
	    			btTransferirAgendaClick();
	    		} else if (event.target == btProdutoRetirada) {
	    			btProdutoRetiradaClick();
	    		} else if (event.target == btPesquisaCliente) {
	    		    btPesquisaClienteClick();
			    } else if (event.target == btNovaAgendaVisita) {
	    			btNovaAgendaVisitaClick();
	    		}  else if (event.target == btReagendarAgenda) {
	    			btReagendarAgendaClick();
	    		} else if (event.target == btUltimosPedidos) {
	    			btUltimosPedidosClick(false);
	    		} else if (event.target == btRecadosCliente) {
	    			btRecadosClienteClick();
	    		} else if (event.target == btRelDifPedidos) {
	    			btUltimosPedidosClick(true);
	    		} else if (event.target == btMetaCliente) {
	    			btMetaCliente();
	    		} else if (event.target == btGiroProduto) {
	    			btGiroProdutoClick();
	    		} else if (event.target == btPesquisaMercado) {
	    			btPesquisaMercadoClick();
	    		} else if (event.target == btPesquisaDeMercadoProdConc) {
	    			btPesquisaMercadoProdConcClick();
			    } else if (event.target == btVisita) {
	    			btVisitaManualClick();
	    		} else if (event.target == btContato) {
	    			btContatoClick();
	    		} else if (event.target == btCestaPositivCli) {
	    			btCestaPositivCliClick();
	    		} else if (event.target == btExtratoCCCli) {
	    			btExtratoCCClienteClick();
	    		} else if (event.target == btRelContaCorrente) {
	    			btCCClientePorTipo();
	    		} else if (event.target == btFaceamento) {
	    			if (LavenderePdaConfig.usaFaceamento()) {
    				btFaceamentoClick();
	    			}
	    		} else if (event.target == btForecast) {
					btForecastClick();
	    		} else if (event.target == btGrade) {
	    			btGradeClick();
	    		} else if (event.target == btConsignacao) {
	    			btConsignacaoClick();
	    		} else if (event.target == btPagamento) {
	    			btPagamentoClick();
	    		} else if (event.target == btCadastroCoordenadas) {
	    			showCadCoordenadas();
		    	} else if (event.target == btMotivoVisita) {
	    			btMotivoVisitaClick();
	    		} else if (event.target == btFotosCliente) {
	    			btFotosClienteClick();
	    		} else if (event.target == btNavegacaoMapa) {
	    			btNavegacaoMapaClick();
	    		} else if (event.target == btAtualizaDadosCliente) {
	    			btAtualizaDadosClienteClick();
				} else if (event.target == btRegistrarAtualizarChegada) {
					if (ValueUtil.valueEquals(btRegistrarAtualizarChegada.appObj, Messages.BOTAO_REGISTRAR_CHEGADA) && checkVisitOfLastValidDay()) {
						break;
					}
					Date dtAgendaAtual = agendaVisita != null ? agendaVisita.dtAgendaAtual : null;
					agendaVisita = AgendaVisitaService.getInstance().findAgendaVisitaPendente(SessionLavenderePda.getRepresentante().cdRepresentante, getCliente().cdCliente, DateUtil.getCurrentDate());
					if (agendaVisita == null) {
						if (SessionLavenderePda.getAgenda() != null) {
							agendaVisita = SessionLavenderePda.getAgenda();
						} else {
							agendaVisita = new AgendaVisita();
							agendaVisita.dtAgendaAtual = DateUtil.getCurrentDate();
						}
					}  else if (dtAgendaAtual == null) {
						dtAgendaAtual = agendaVisita.dtAgendaAtual;
					}
					if (btRegistrarAtualizarChegadaClick(agendaVisita, dtAgendaAtual, listAgendaVisitaForm != null)) {
						remontaMenuFuncionalidades();
					}
				} else if (event.target == btRegistrarAtualizarSaida) {
					if (btRegistrarAtualizarSaidaClick()) {
						this.agendaVisita = null;
						remontaMenuFuncionalidades();
					}
				} else if (event.target == btDesfazerChegada) {
					btDesfazerChegadaClick();
				} else if (event.target == btEnderecosCliente) {
					btEnderecosClienteClick();
	    		} else if (event.target == btListaSacs) {
	    			btListaSacsClick();
	    		} else if (event.target == btRequisicaoServ) {
	    			btRequisicaoServClick();
	    		} else if (event.target == btProdutoClienteCod) {
	    			btProdutoClienteCodClick();
	    		} else if (event.target == btMaisParceiroIconic) {
	    			btMaisParceiroIconicClick();
	    		} else if (event.target == btDescProgressivo) {
				    btDescProgressivoClick();
			    } else if (event.target == btDivulgaInfo) {
	    			btDivulgaInfoClick();
			    } else if (event.target == btDap) {
			    	btDapClick();
			    } else if (event.target == btPoliticaBonificacao) {
			    	btPoliticaBonificacaoClick();
	    		} else if (event.target == btAtendimentoHist) {
	    			WindowUtil.btAtendimentoHistClick(this, getCliente().cdEmpresa, getCliente().cdCliente);
	    		}
	    		
				if (LavenderePdaConfig.usaTipoPedidoComoOpcaoMenuCliente) {
					Vector btDinamicos = buttonsTipoPedido.getKeys();
					for (int i = 0; i < btDinamicos.size(); i++) {
						if (event.target == btDinamicos.items[i]) {
							if (!MainLavenderePda.getInstance().isSistemaBloqueadoPendenteAtualizacao()) {
								btNovoPedidoByItemPedido((String) buttonsTipoPedido.get(btDinamicos.items[i]));
							}
						}
					}
				}
				break;
			}
		}
	}

	private void btDapClick() throws SQLException {
		Cliente cliente = getCliente();
		if (cliente != null) {
			show(new ListDapMatriculaForm(cliente));			
		}
	}

	private void btDescProgressivoClick() throws SQLException {
		Cliente cliente = getCliente();
		if (cliente != null) {
			DescProgressivoConfig descProgressivoConfigFilter = new DescProgressivoConfig();
			descProgressivoConfigFilter.cdEmpresa = cliente.cdEmpresa;
			descProgressivoConfigFilter.cdRepresentante = cliente.cdRepresentante;
			descProgressivoConfigFilter.cliente = cliente;
			if (DescProgressivoConfigService.getInstance().countByCliente(descProgressivoConfigFilter) > 0) {
				show(new ListDescontoProgressivoForm(cliente));
			} else {
				UiUtil.showWarnMessage(Messages.DESC_PROG_CONFIG_SEM_REGISTROS);
			}
		}
	}

	private void btDivulgaInfoClick() throws SQLException {
		Cliente cliente = getCliente();
		if (DivulgaInfoService.getInstance().hasDivulgaInfoByCliente(cliente)) {
			new RelDivulgaInfoWindow(cliente, true).popup();
		} else {
			UiUtil.showWarnMessage(Messages.DIVULGA_INFO_SEM_REGISTRO);
		}
	}

	private void btMaisParceiroIconicClick() {
		
		if (VmUtil.isAndroid()) {
			String json = new WtoolsParametrosDTO().openExternalApp(LavenderePdaConfig.nmPacoteAppExternoMenuClienteAndroid);
			WtoolsUtil.openUrlWtools(json);
		} else {
			UiUtil.openBrowser(LavenderePdaConfig.urlAppExternoMenuClienteIOS);
		}
	}
	
	private void btProdutoClienteCodClick() throws SQLException {
		Cliente cliente = getCliente();
		ListProdutoClienteCodWindow listProdutoClienteCodWindow = new ListProdutoClienteCodWindow(cliente);
		listProdutoClienteCodWindow.popup();
	}
	
	private void btRequisicaoServClick() throws SQLException {
		Cliente cliente = getCliente();
		if (cliente != null) {
			CadRequisicaoServForm cadRequisicaoServForm = new CadRequisicaoServForm(cliente.nmRazaoSocial, cliente.cdCliente);
			cadRequisicaoServForm.add();
			show(cadRequisicaoServForm);			
		}
	}

	private void btMetaCliente() throws SQLException {
		if (LavenderePdaConfig.usaRelatorioMetaVendaCliente) {
			ListMetaVendaPorClienteWindow metaVendaPorClienteForm = new ListMetaVendaPorClienteWindow(getCliente());
			metaVendaPorClienteForm.popup();
		}
	}

	private void btProdutoRetiradaClick() throws SQLException {
		ListProdutoRetiradaWindow listProdutoRetiradaWindow = new ListProdutoRetiradaWindow(getCliente());
		listProdutoRetiradaWindow.popup();
	}

	private void btTransferirAgendaClick() throws SQLException {
		Date dtAgendaAtual = agendaVisita != null ? agendaVisita.dtAgendaAtual : null;
		if (listAgendaVisitaForm == null) {
			agendaVisita = AgendaVisitaService.getInstance().findAgendaVisitaPendente(getCliente().cdRepresentante, getCliente().cdCliente);
		}
		CadTransferenciaAgendaWindow cadTransferenciaAgendaWindow = new CadTransferenciaAgendaWindow(null, agendaVisita, dtAgendaAtual);
		cadTransferenciaAgendaWindow.popup();
		if (cadTransferenciaAgendaWindow.transferiuAgenda) {
			if (agendaVisita != null) {
				agendaVisita.statusAgendaCdPositivado = StatusAgenda.STATUSAGENDA_CDTRANSFERIDO; 
			}
			remontaMenuFuncionalidades();
		}
	}

	private void btReagendarAgendaClick() throws SQLException {
		Date dtAgendaAtual = agendaVisita != null ? agendaVisita.dtAgendaAtual : null;
		if (listAgendaVisitaForm == null) {
			agendaVisita = AgendaVisitaService.getInstance().findAgendaVisitaPendente(getCliente().cdRepresentante, getCliente().cdCliente);
		}
		CadReagendamentoAgendaWindow cadReagendamentoAgendaWindow = new CadReagendamentoAgendaWindow(agendaVisita, dtAgendaAtual);
		cadReagendamentoAgendaWindow.popup();
		if (cadReagendamentoAgendaWindow.reagendouAgenda) {
			if (agendaVisita != null) {
				agendaVisita.statusAgendaCdPositivado = StatusAgenda.STATUSAGENDA_CDREAGENDADO; 
			}
			remontaMenuFuncionalidades();
		}
	}
	
	private void btNovaAgendaVisitaClick() throws SQLException {
		CadAgendaVisitaWindow cadAgendaVisitaWindow = new CadAgendaVisitaWindow(getCliente());
		cadAgendaVisitaWindow.popup();
	}

	
	public void remontaMenuFuncionalidades() throws SQLException {
		try {
			montaMenuFuncionalidades();
			menuFuncionalidades.drawButtons();
		} catch (NullPointerException ex) {
			//não remonta o menu, pois esta tela não chegou a ser exibida.
			VmUtil.debug(Messages.MENUFUNCIONALIDADES_ERRO_RELOAD);
		}
	}

	private void btNavegacaoMapaClick() throws SQLException {
		Cliente cliente = getCliente();
	    try {
	    	String param = null;
	        if (cliente.cdLatitude == 0 || cliente.cdLatitude > 90 || cliente.cdLatitude < -90 ||
	            cliente.cdLongitude == 0 || cliente.cdLongitude > 180 || cliente.cdLongitude < -180) {
	        	if (UiUtil.showConfirmYesNoMessage(Messages.MSG_CLIENTE_SEM_COORDENADA)) {
	        		param = "maps";
	            } else {
	            	return;
	            }
	        }
	        if (param == null) {
		        switch (LavenderePdaConfig.usaNavegacaoGpsNoMapa) {
				case "1": {
					param = "gps";
					break;
				}
				case "2": {
					param = "waze";
					break;
				}
				default:
					param = "maps";
					break;
				}
	        }
	        String json = new WtoolsParametrosDTO().openExternalNavigation(param, cliente.cdLatitude + "", cliente.cdLongitude + "");
	        WtoolsUtil.openUrlWtools(json);
	        
	    } catch (Throwable ex) {
	    	UiUtil.showErrorMessage("Erro ao acionar aplicativo de navegação: " + ex.getMessage());
	    } 
	}

	private void showCadCoordenadas() {
		CadCoordenadasGeograficasWindow cadCoordenadasGeograficasWindow = new CadCoordenadasGeograficasWindow(SessionLavenderePda.getCliente(), true);
		cadCoordenadasGeograficasWindow.popup();
	}

	private void btNovoPedidoByItemPedido(String newCdTipoPedido) throws SQLException {
		this.cdTipoPedido = newCdTipoPedido;
		btNovoPedidoClick();
	}

	private void btPagamentoClick() throws SQLException {
		if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario() || ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn()) return;
		
		ListPagamentoForm listPagamentoForm = new ListPagamentoForm();
		show(listPagamentoForm);
	}

	private void btInfoClienteClick() throws SQLException {
		BaseDomain domain = getCrudService().findByRowKeyDyn(SessionLavenderePda.getCliente().getRowKey());
		if (getBaseCrudCadForm() != null) {
			getBaseCrudCadForm().edit(domain);
			show(getBaseCrudCadForm());
		}
	}

	private void btFinanceiroClick() throws SQLException {
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceiraDyn(SessionLavenderePda.getCliente());
		if (fichaFinanceira != null) {
			fichaFinanceira.getTitulos();
			CadFichaFinanceiraDynForm cadFichaFinanceiraForm = new CadFichaFinanceiraDynForm();
			cadFichaFinanceiraForm.edit(fichaFinanceira);
			show(cadFichaFinanceiraForm);
		} else {
			UiUtil.showWarnMessage(Messages.MSG_CLIENTE_SEM_FICHAFINANCEIRA);
		}
	}

	private void btUltimosPedidosClick(boolean relDifPedido) throws SQLException {
		ListPedidoForm listPedidoForm = new ListPedidoForm();
		listPedidoForm.inConsultaUltimosPedidos = true;
		listPedidoForm.ckPedidoDif.setChecked(relDifPedido);
		show(listPedidoForm);
	}

	private void btRecadosClienteClick() throws SQLException {
		ListRecadoForm listRecadoForm = new ListRecadoForm(getCliente());
		show(listRecadoForm);
	}

	private void btGiroProdutoClick() throws SQLException {
		ListGiroProdutoForm listGiroForm = new ListGiroProdutoForm();
		show(listGiroForm);
	}

	public void btPesquisaMercadoClick() throws SQLException {
		if (LavenderePdaConfig.usaPesquisaMercado && LavenderePdaConfig.usaAppExternoPesquisaMercado()) {
			String material;
			if (VmUtil.isAndroid()) {
				String json = new WtoolsParametrosDTO().openExternalApp(LavenderePdaConfig.pacoteAppExternoPesquisaMercado);
				WtoolsUtil.openUrlWtools(json);
				material = json;
			} else {
				UiUtil.openBrowser(LavenderePdaConfig.pacoteAppExternoPesquisaMercadoUrl);
				material = LavenderePdaConfig.pacoteAppExternoPesquisaMercadoUrl;
			}
			if (LavenderePdaConfig.contabilizaAcessoExternoPesquisaMercado) {
				AcessoMaterialService.getInstance().insereAcesso(material);
			}
		} else {
			ListPesquisaMercadoForm listPesquisaForm = new ListPesquisaMercadoForm(null);
			show(listPesquisaForm);
		}
	}
	
	public void btPesquisaMercadoProdConcClick() throws SQLException {
    	ListPesquisaMercadoConfigForm listPesquisaMercadoConfigForm = new ListPesquisaMercadoConfigForm();
    	show(listPesquisaMercadoConfigForm);
	}

	private void btContatoClick() throws SQLException {
		ListContatoForm listPesquisaForm = new ListContatoForm();
		show(listPesquisaForm);
	}

	private void btCestaPositivCliClick() throws SQLException {
		ListCestaPositClienteForm listCestaPositClienteForm = new ListCestaPositClienteForm();
		show(listCestaPositClienteForm);
	}

	private void btExtratoCCClienteClick() throws SQLException {
		ListContaCorrenteCliForm listContaCorrenteCliForm = new ListContaCorrenteCliForm(SessionLavenderePda.getCliente());
		show(listContaCorrenteCliForm);
	}

	private void btCCClientePorTipo() throws SQLException {
		ListCCCliPorTipoForm cCClienteCcCliPorTipo = new ListCCCliPorTipoForm(SessionLavenderePda.getCliente());
		show(cCClienteCcCliPorTipo);
	}

	private void btFaceamentoClick() throws SQLException {
		ListFaceamentoForm listFaceamentoForm = new ListFaceamentoForm();
		show(listFaceamentoForm);
	}

	private void btForecastClick() throws SQLException {
		ContratoCliente contratoClienteVigente = ContratoClienteService.getInstance().getContratoClienteForecastVigente();
		ListForecastForm listForecastForm = new ListForecastForm(contratoClienteVigente);
		show(listForecastForm);
	}

	private void btGradeClick() throws SQLException {
		ContratoCliente contratoClienteVigente = ContratoClienteService.getInstance().getContratoClienteGradeVigente();
		ListGradeForm listGradeForm = new ListGradeForm(contratoClienteVigente);
		show(listGradeForm);
	}
	
	private void btListaSacsClick() throws SQLException {
		ListSacForm listSacsForm = new ListSacForm(getCliente());
		show(listSacsForm);
	}

	private void btConsignacaoClick() throws SQLException {
		CadConsignacaoForm cadConsignacaoForm = new CadConsignacaoForm();
		//--
		Consignacao consignacao = ConsignacaoService.getInstance().findConsignacaoInOpenByCdCliente(SessionLavenderePda.getCliente().cdCliente);
		//--
		if (consignacao == null) {
			cadConsignacaoForm.add();
		} else {
			cadConsignacaoForm.edit(consignacao);
		}
		//--
		show(cadConsignacaoForm);
	}
	
	public void btNovoPedidoClick() throws SQLException {
		btNovoPedidoClick(false);
	}
	
	public void btNovoPedidoClick(boolean iniciaPedidoSemCliente) throws SQLException {
		if (notValidateActionsOnSelectCliente()) {
			return;
		}
		LoadingBoxWindow mb = UiUtil.createProcessingMessage();
		mb.popupNonBlocking();
		boolean voltaEmpresaAnterior = true;
		try {
			if (periodoEntradaParaPedidoNaoValido()) {
				return;
			}
			if (!beforeCreateNovoPedido(getCliente())) {
				return;
			}
			// --
			if ("2".equals(LavenderePdaConfig.usaListaSacCliente) && SacService.getInstance().haveSacs(getCliente().cdCliente)) {
				ListSacWindow listSacsWindow = new ListSacWindow(getCliente());
				listSacsWindow.popup();
			}
		    if (!verificaClienteInAgenda()) {
		    	if (!showHistoricoPedidosCliente()) {
					if (iniciaPedidoSemCliente) {
						if (novoPedidoSemCliente()) {
							voltaEmpresaAnterior = false;
						}
					} else {
						if (novoPedido()) {
							voltaEmpresaAnterior = false;
						}
					}
		    	} else {
		    		voltaEmpresaAnterior = false;
		    	}
			} else {
				voltaEmpresaAnterior = false;
			}
		} finally {
			boolean deveRetornarEmpresa = voltaEmpresaAnterior && LavenderePdaConfig.usaEscolhaEmpresaPedido;
			if (deveRetornarEmpresa && ValueUtil.isNotEmpty(SessionLavenderePda.cdEmpresaOld)) {
				EmpresaService.getInstance().changeEmpresaSessao(SessionLavenderePda.cdEmpresaOld);
				SessionLavenderePda.cdEmpresaOld = null;
			}
			mb.unpop();
		}
	}

	public boolean periodoEntradaParaPedidoNaoValido() throws SQLException {
		if (LavenderePdaConfig.validaPeriodoEntregaParaPedido && !ClienteEnderecoService.getInstance().isTodosEnderecosComPeriodoEntrega(SessionLavenderePda.getCliente())) {
			UiUtil.showErrorMessage(Messages.PEDIDO_MSG_ERRO_SEM_PERIODO_ENTREGA);
			return true;
		}
		return false;
	}

	public boolean notValidateActionsOnSelectCliente() throws SQLException {
		if (SessionLavenderePda.visitaAndamento != null && SessionLavenderePda.visitaAndamento.isVisitaEmAndamento() && ValueUtil.valueEquals(SessionLavenderePda.getCliente().cdCliente, SessionLavenderePda.visitaAndamento.cdCliente)) {
			showPesquisaPendente();
			if (SessionLavenderePda.visitaAndamento == null) {
				return true;
			}
		}
		if (LavenderePdaConfig.isExibeHistoricoCliente()) {
			ListHistoricoTrocaDevolucaoCliWindow listHistoricoTrocaDevolucaoCliWindow = new ListHistoricoTrocaDevolucaoCliWindow();
			if (ValueUtil.isNotEmpty(listHistoricoTrocaDevolucaoCliWindow.historicoTrocaDevolucaoCliList)) {
				listHistoricoTrocaDevolucaoCliWindow.popup();
			}
		}
		if (LavenderePdaConfig.apresentaHistoricoTitulosAtrasados) {
			new ListTituloFinanceiroWindow().popup();
		}
		if (LavenderePdaConfig.apresentaPopUpErroEnvioPedidoNovoPedido && ErroEnvioService.getInstance().hasPedidosErroEnvioServidor()) {
			ListErroEnvioWindow listErroEnvioWindow = new ListErroEnvioWindow(this);
			listErroEnvioWindow.popup();
			if (!listErroEnvioWindow.closedByBtFechar) {
				return true;
			}
		}
		if (getCliente().isModoDeProspeccao()) {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_CLIENTE_TIPO_PROSPECT);
			return true;
		}
		if (getCliente().isNovoCliente() && !LavenderePdaConfig.isPermitePedidoNovoCliente()) {
			UiUtil.showWarnMessage(Messages.PEDIDO_MSG_NOVO_CLIENTE);
			return true;
		}
		if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada) {
			SessionLavenderePda.getCliente().somentePedidoPreOrcamento = false;
		}
		return false;
	}

	private void btTornarClienteClick() throws SQLException {
		if (cancelaSolicitacaoInativacao()) {
			return;
		}
		if (VisitaService.getInstance().isExisteVisitaPositivadaParaClienteProspect()) {
			UiUtil.showWarnMessage(Messages.MSG_CLIENTE_PROSPECT_CADASTRO_REALIZADO);
		} else {
			getCliente().nuSequenciaVisitaCliente = agendaVisita != null ? agendaVisita.nuSequencia : 0;
			CadNovoClienteForm cadNovoClienteForm = new CadNovoClienteForm(false, null, getCliente());
			if (SessionLavenderePda.isUsuarioSupervisor()){
				if (cadNovoClienteForm.showRepresentanteCombo())
					cadNovoClienteForm.add();
					show(cadNovoClienteForm);
			} else {
				cadNovoClienteForm.add();
				show(cadNovoClienteForm);
			}
		}
	}

	private void btInativarProspectClick() throws SQLException {
		ClienteInativacao clienteInativacao = (ClienteInativacao) ClienteInativacaoService.getInstance().findByPrimaryKey(new ClienteInativacao(getCliente()));
		if (clienteInativacao != null && !ValueUtil.VALOR_SIM.equals(clienteInativacao.flCancelado)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.MSG_INATIVACAO_JA_SOLICITADA)) {
				ClienteInativacaoService.getInstance().delete(clienteInativacao);
				UiUtil.showInfoMessage(Messages.MSG_INATIVACAO_CANCELADA);
			}
		} else if (UiUtil.showConfirmYesNoMessage(Messages.MSG_INATIVACAO_CONFIRMACAO)) {
			ClienteInativacaoService.getInstance().insertOrUpdate(new ClienteInativacao(getCliente()));
			getCliente().setFlStatusCliente(StatusCliente.STATUSCLIENTE_CDPENDENTEINATIVACAO);
			ClienteService.getInstance().update(getCliente());
			UiUtil.showInfoMessage(Messages.MSG_INATIVACAO_SUCESSO);
		}
	}

	private boolean cancelaSolicitacaoInativacao() throws SQLException {
		if (!LavenderePdaConfig.permiteInativarClienteProspeccao) {
			return false;
		}
		ClienteInativacao clienteInativacao = (ClienteInativacao) ClienteInativacaoService.getInstance().findByPrimaryKey(new ClienteInativacao(getCliente()));
		if (clienteInativacao != null && !ValueUtil.VALOR_SIM.equals(clienteInativacao.flCancelado)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.MSG_INATIVACAO_JA_SOLICITADA)) {
				ClienteInativacaoService.getInstance().delete(clienteInativacao);
				UiUtil.showInfoMessage(Messages.MSG_INATIVACAO_CANCELADA);
				return false;
			} else {
				return true;
			}
		}
		return false;
	}
	
	private void btPoliticaBonificacaoClick() throws SQLException {
		new ListBonifCfgWindow(getCliente()).popup();
	}
	
	public boolean beforeCreateNovoPedido(Cliente cliente) throws SQLException {
		return beforeCreateNovoPedido(cliente, true, false);
	}

	public boolean beforeCreateNovoPedido(Cliente cliente, boolean onReplicandoPedido) throws SQLException {
		return beforeCreateNovoPedido(cliente, true, onReplicandoPedido);
	}

	private boolean beforeCreateNovoPedido(Cliente cliente, boolean onCreateNovoPedido, boolean onReplicandoPedido) throws SQLException {
		if (!validaClienteChurn(cliente)) return false;
		
		if (!PedidoService.getInstance().validaUsuarioEmissaoPedido()) return false;
		if (LavenderePdaConfig.usaEscolhaEmpresaPedido && !onReplicandoPedido && !showComboEscolhaEmpresaPedido()) {
			return false;
		}
		if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoNovoPedido()) {
			if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
				if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor()) {
					int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
					if (result < 1) {
						String msg = "";
						if (result == -1) {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()});
						} else {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
						}
						if (UiUtil.showConfirmYesNoMessage(msg)) {
							btPesquisaMercadoClick();
							return false;
						}
					}
				}
				if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
					int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISAGONDOLA);
					if (result < 1) {
						String msg = "";
						if (result == -1) {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_GONDOLA_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()});
						} else {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_GONDOLA_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
						}
						if (UiUtil.showConfirmYesNoMessage(msg)) {
							btPesquisaMercadoClick();
							return false;
						}
					}
				}
			} else {
				int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
				if (result < 1) {
					String msg = result == -1 ? MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()}) : MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
					if (UiUtil.showConfirmYesNoMessage(msg)) {
						btPesquisaMercadoClick();
						return false;
					}
				}
			}
		}
		if (validaCargaVencida()) {
			return false;
		}
		ColetaGpsService.getInstance().encerraColetaGpsSeNecessario();
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return false;
		}
		// --
		if (checkVisitOfLastValidDay()) {
			return false;
		}
		// --
		if (PedidoService.getInstance().isCountPedidosAbertosMaiorPermitido()) {
			if (LavenderePdaConfig.usaVerbaUnificada) {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_PEDIDOS_EM_ABERTO_SISTEMA_TODAS_EMPRESA, LavenderePdaConfig.getQtdPedidosPermitidosManterAbertos()));
			} else {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.PEDIDO_MSG_PEDIDOS_EM_ABERTO_SISTEMA, LavenderePdaConfig.getQtdPedidosPermitidosManterAbertos()));
			}
			return false;
		}
		// --
		if (!ConexaoPdaService.getInstance().validateEnvioRecebimentoDadosObrigatorio()) {
			return false;
		}
		if (VisitaUiUtil.avisaEBloqueiaCriarPedidoVisitaAndamento(cliente)) {
			return false;
		}
		if (representantePossuiAlgumaAgenda()) {
			AgendaVisita agendaTemp = AgendaVisitaService.getInstance().findAgendaVisitaPendente(SessionLavenderePda.getRepresentante().cdRepresentante, getCliente().cdCliente);
			if (agendaTemp != null) {
				if (DateUtil.isBeforeOrEquals(agendaTemp.dtAgendaAtual, AgendaVisitaService.getInstance().getLastValidDay(DateUtil.getCurrentDate())) || !validaVisitaForaAgenda(cliente)) {
					throw new ValidationException(MessageUtil.getMessage(Messages.AGENDA_NAO_REALIZADA_VERIFIQUE, agendaTemp.dtAgendaAtual));
				}
			} else if (!validaVisitaForaAgenda(cliente)) {
				return false;
			}
		}
		
		// --
		if (LavenderePdaConfig.isPermitePedidoNovoCliente() && !LavenderePdaConfig.isIgnoraBloqueioPedidoNovoCliente() && !LavenderePdaConfig.isPermitePedidoParaNovoClienteApenasPelaTelaDetalhes()) {
			if (ValueUtil.valueEquals(cliente.cdCliente, cliente.nuCnpj)) {
				NovoCliente novoCliente = NovoClienteService.getInstance().getNovoClienteByCliente(cliente);
				if (novoCliente != null && !novoCliente.isAlteradoPalm()) {
					UiUtil.showErrorMessage(Messages.NOVOCLIENTE_MSG_PEDIDO_BLOQUEADO);
					return false;
				}
			}
		}
		TipoPagamentoService.getInstance().validaTipoPagamentoRestrito(cliente);
		try {
			ClienteService.getInstance().validateClienteBloqueado(cliente);
		} catch (ValidationException e) {
			if (LavenderePdaConfig.bloquearNovoPedidoClienteBloqueado) {
				StringBuffer strBuffer = new StringBuffer();
				strBuffer.append(e.getMessage());
				if (ValueUtil.isNotEmpty(getCliente().dsSituacao)) {
					strBuffer.append(", ");
					strBuffer.append(getCliente().dsSituacao);
				}
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && onCreateNovoPedido) {
					if (showOpcaoPagamentoAVista(strBuffer.toString(), false) == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						return true;
					} else {
						return false;
					}
				} else {
					UiUtil.showErrorMessage(strBuffer.toString());
					return false;
				}
			} else if (LavenderePdaConfig.confirmaNovoPedidoClienteBloqueado) {
				StringBuffer strBuffer = new StringBuffer();
				if (!UiUtil.showConfirmYesNoMessage(strBuffer.append(e.getMessage()).append(" " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString())) {
					return false;
				}
			}
		}
		try {
			ClienteService.getInstance().validateClienteBloqueadoPorAtraso(cliente);
		} catch (ValidationException e) {
			if (LavenderePdaConfig.bloquearNovoPedidoClienteBloqueadoPorAtraso) {
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueadoPorAtraso() && onCreateNovoPedido) {
					if (showOpcaoPagamentoAVista(e.getMessage(), false) == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						return true;
					} else {
						return false;
					}
				} else {
					UiUtil.showErrorMessage(e.getMessage());
					return false;
				}
			} else {
				StringBuffer strBuffer = new StringBuffer();
				if (!UiUtil.showConfirmYesNoMessage(strBuffer.append(e.getMessage()).append(" " + Messages.PEDIDO_MSG_SALVAR_DESEJA_CONTINUAR).toString())) {
					return false;
				}
			}
		}
		if (!validateLimiteCredito(cliente, onCreateNovoPedido)) {
			return false;
		}

		if (VisitaUiUtil.isCadastraCoordenada() && !VisitaUiUtil.sugereNovoOrcamentoSemCheckIn(cliente)) {
			boolean impedeCriacaoPedidoCadCoord = !VisitaUiUtil.mostraMensagemDeCadastroDeCoordenadasCliente();
			if (impedeCriacaoPedidoCadCoord) {
				return false;
			}
		}
		if (!isValidateClienteInadimplente(cliente, onCreateNovoPedido)) {
			return false;
		}
		if (!isValidaClienteAtrasadoPorVlTotalTitulosAtraso(cliente)) {
			return false;
		}
		// --
		if (validateClienteSemPedido(cliente, onCreateNovoPedido)) {
			return false;
		}
		if (!atualizaCadastroCliente(cliente)) {
			return false;
		}
	    if (LavenderePdaConfig.isPermiteRegistrarNovasFotosDeCliente() && !LavenderePdaConfig.ignoraNovaFotoEmissaoPedido && isClienteNaoPossuiFotos(cliente)
			    && ValueUtil.valueNotEqualsIfNotNull(cliente.cdCliente, Cliente.CD_NOVO_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO) && ValueUtil.valueNotEqualsIfNotNull(cliente.cdCliente, Cliente.CD_CLIENTE_DEFAULT_PARA_NOVO_PEDIDO)) {
	    	if (UiUtil.showConfirmYesNoMessage(Messages.CLIENTE_MSG_CONFIRMACAO_NOVA_FOTO)) {
	    		btFotosClienteClick();
	    	}
	    }
		if (LavenderePdaConfig.sugereRegistroChegadaNoMenuPedido()) {
			if (!VisitaUiUtil.sugereNovoOrcamentoSemCheckInNaChegadaCliente() && !VisitaUiUtil.sugereRegistroChegada(getCliente(), agendaVisita)) {
				return false;
			}
			remontaMenuFuncionalidades();
		}
		if (LavenderePdaConfig.nuDiasObrigaFechamentoVendas > 0 && !validaNuDiasObrigaFechamentoVendas()) {
			return false;
		}
		String messageCliente = ClienteService.getInstance().verificaClienteAberturaOuFundacao(cliente);
		if (ValueUtil.isNotEmpty(messageCliente)) {
			UiUtil.showWarnMessage(messageCliente);
		}
				
		return true;
	}

	private boolean validaClienteChurn(Cliente cliente) throws SQLException {
    	if (!LavenderePdaConfig.usaConfigModuloRiscoChurn()) return true;
		if (ClienteChurnService.getInstance().obrigaRegistrarRiscoChurn()) return false;
		
		ClienteChurn clienteChurn = ClienteChurnService.getInstance().getClienteChurn(cliente);
		if (clienteChurn== null) return true;
		
		if (ValueUtil.isEmpty(clienteChurn.cdMotivoChurn) || clienteChurn.getMotivoChurn().isSemMotivoInformado()) {
			UiUtil.showWarnMessage(Messages.CLIENTECHURN_CAMPO_MOTIVO_OBRIGATORIO);
			CadClienteChurnWindow cadClienteChurnWindow = new CadClienteChurnWindow();
			cadClienteChurnWindow.edit(clienteChurn);
			cadClienteChurnWindow.popup();
			return !cadClienteChurnWindow.closedByBtFechar;
		}
		return true;
	}

	private void sugereRegistroSaida() throws SQLException {
		Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
		if (visitaEmAndamento != null && ValueUtil.valueEquals(getCliente().cdCliente, visitaEmAndamento.cdCliente) && !getCliente().isClienteDefaultParaNovoPedido()) {
			if (UiUtil.showConfirmYesNoMessage(getRegistrarSaidaMessage(getCliente().isNovoCliente()))) {
				btRegistrarSaidaClick(visitaEmAndamento, false);
			}
		}
	}

	private boolean isClienteNaoPossuiFotos(Cliente cliente) throws SQLException {
		int qtdFotos = 0;
		FotoClienteErp fotoClienteErp = new FotoClienteErp();
		fotoClienteErp.cdEmpresa = cliente.cdEmpresa;
		fotoClienteErp.cdRepresentante = cliente.cdRepresentante;
		fotoClienteErp.cdCliente = cliente.cdCliente;
		qtdFotos = FotoClienteErpService.getInstance().countByExample(fotoClienteErp);
		if (qtdFotos == 0) {
			FotoCliente fotoCliente = new FotoCliente();
			fotoCliente.cdEmpresa = cliente.cdEmpresa;
			fotoCliente.cdRepresentante = cliente.cdRepresentante;
		   	fotoCliente.cdCliente = cliente.cdCliente;
		   	qtdFotos = FotoClienteService.getInstance().countByExample(fotoCliente);
		}
		return qtdFotos == 0;
		
	}

	private boolean showComboEscolhaEmpresaPedido() throws SQLException {
		EmpresaClienteComboBox cbEmpresaClienteComboBox = new EmpresaClienteComboBox(Messages.ESCOLHA_EMPRESA);
		cbEmpresaClienteComboBox.loadEmpresasCliente(getCliente().cdCliente);
		if (cbEmpresaClienteComboBox.size > 1) {
			cbEmpresaClienteComboBox.popup();
			if (cbEmpresaClienteComboBox.getSelectedIndex() != -1) {
				if (!SessionLavenderePda.cdEmpresa.equals(cbEmpresaClienteComboBox.getValue())) {
					SessionLavenderePda.cdEmpresaOld = SessionLavenderePda.cdEmpresa;
					EmpresaService.getInstance().changeEmpresaSessao(cbEmpresaClienteComboBox.getValue());
					BaseContainer mainForm = MainLavenderePda.getInstance().getMainForm();
					if (mainForm instanceof MainMenu) {
						((MainMenu) mainForm).refreshLbEmpresaRepresentante();
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean validateLimiteCredito(Cliente cliente, boolean onCreateNovoPedido) throws SQLException {
		try {
			FichaFinanceiraService.getInstance().validateLimCred();
		} catch (ValidationException ve) {
			StringBuffer strBuffer = new StringBuffer();
			if (LavenderePdaConfig.bloquearLimiteCreditoCliente) {
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado()  && onCreateNovoPedido) {
					int opcao = showOpcaoPagamentoAVista(strBuffer.append(Messages.CLIENTE_FINANCEIRO_BLOQUEADO).append(ve.getMessage()).toString(), false);
					if (opcao == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						return true;
					} else if (opcao == 0) {
						return false;
					}
				}
				UiUtil.showErrorMessage(strBuffer.append(Messages.CLIENTE_FINANCEIRO_BLOQUEADO).append(ve.getMessage()).toString());
				return false;
			} else if (LavenderePdaConfig.isUsaConfigLiberacaoComSenhaLimiteCreditoCliente()) {
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && onCreateNovoPedido) {
					int opcao = showOpcaoPagamentoAVista(strBuffer.append(Messages.CLIENTE_FINANCEIRO_BLOQUEADO).append(ve.getMessage()).toString(), true);
					if (opcao == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						return true;
					} else if (opcao == 0) {
						return false;
					}
				}
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ve.getMessage());
				senhaForm.setCdCliente(cliente.cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_LIMITE_CREDITO_EXTRP);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				cliente.flCreditoClienteLiberadoSenha = ValueUtil.VALOR_SIM;
				if (SessionLavenderePda.isSessionCliente()) {
					SessionLavenderePda.getCliente().flCreditoClienteLiberadoSenha = ValueUtil.VALOR_SIM;
				}

			} else if (LavenderePdaConfig.controlarLimiteCreditoCliente) {
				UiUtil.showWarnMessage(ve.getMessage());
			}
		}
		return true;
	}

    public boolean checkVisitOfLastValidDay() throws SQLException {
    	if (!LavenderePdaConfig.naoObrigaCompletarAgendaDiaAnterior && representantePossuiAlgumaAgenda()) {
    		Date lastValidDate = AgendaVisitaService.getInstance().getLastValidDay(DateUtil.getCurrentDate());
    		if ((getDomain() instanceof AgendaVisita) && lastValidDate.equals(((AgendaVisita)getDomain()).dtAgendaAtual)) {
    			return false;
			}
    		if (AgendaVisitaService.getInstance().isHasVisitaPendenteOnDay(lastValidDate)) {
    			String mensagem = LavenderePdaConfig.usaFiltroPeriodoDataAgendaVisita ? MessageUtil.getMessage(Messages.AGENDA_MSG_VISITAS_PENDENTES_DIA, lastValidDate) : Messages.AGENDA_MSG_VISITAS_PENDENTES;
				UiUtil.showWarnMessage(mensagem);
    			return true;
    		}
		}
    	return false;
	}

	private boolean representantePossuiAlgumaAgenda() throws SQLException {
		AgendaVisita agendaVisita = new AgendaVisita();
		agendaVisita.cdEmpresa = SessionLavenderePda.cdEmpresa;
		agendaVisita.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
		return AgendaVisitaService.getInstance().countByExample(agendaVisita) > 0;
	}

	private boolean verificaClienteInAgenda() throws SQLException {
		boolean controlsequence = true;
		if (LavenderePdaConfig.isUsaAgendaDeVisitas() && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita || !(LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia || LavenderePdaConfig.naoObrigaCompletarAgendaDiaAnterior)) {
				agendaVisita = new AgendaVisita();
				Vector listAgenda = AgendaVisitaService.getInstance().findAgendasVisitas(DateUtil.getCurrentDate(), StatusAgenda.STATUSAGENDA_CDAVISITAR);
				int size = listAgenda.size();
				AgendaVisita agenda;
				Visita visitaFilter;
				for (int i = 0; i < size; i++) {
					agenda = (AgendaVisita) listAgenda.items[i];
					// --
					visitaFilter = new Visita();
					visitaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
					visitaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
					visitaFilter.cdCliente = agenda.cdCliente;
					visitaFilter.dtVisita = DateUtil.getCurrentDate();
					if (LavenderePdaConfig.usaNuSequenciaNaChaveDaAgendaVisita) {
						visitaFilter.nuSequencia = agenda.nuSequencia;
					}
					Vector listVisitasToday = VisitaService.getInstance().findAllByExample(visitaFilter);
					if (ValueUtil.isEmpty(listVisitasToday)) {
						if (agenda.cdCliente.equals(SessionLavenderePda.getCliente().cdCliente)) {
							agendaVisita = agenda;
							i = size;
						} else {
							controlsequence = false;
						}
					}
					listVisitasToday = null;
				}
				if (ValueUtil.isEmpty(agendaVisita.cdCliente)) {
					agendaVisita.cdCliente = SessionLavenderePda.getCliente().cdCliente;
					agendaVisita.cdEmpresa = SessionLavenderePda.cdEmpresa;
					agendaVisita.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
					agendaVisita.flTipoFrequencia = ValueUtil.VALOR_NI;
					agendaVisita.nuSequencia = 0;
				}
			}

			if (!(LavenderePdaConfig.naoSolicitaMotivoVisitaClienteForaSequencia || LavenderePdaConfig.naoObrigaCompletarAgendaDiaAnterior)) {
				if (!controlsequence && (!LavenderePdaConfig.usaPositivacaoVisitaPorTipoPedido || TipoPedidoService.getInstance().isTipoPedidoPermitePositivacaoVisita(cdTipoPedido))) {
					if (UiUtil.showConfirmYesCancelMessage(Messages.VISITA_POSITIVADA_PEDIDOCLIENTE_FORAORDEM) == 1) {
						CadVisitaForm cadVisitaForm = new CadVisitaForm(Visita.CD_VISITA_POSITIVADA);
						cadVisitaForm.setBaseCrudCadMenuForm(this);
						cadVisitaForm.edit(agendaVisita);
						show(cadVisitaForm);
					} else if ((LavenderePdaConfig.usaEscolhaEmpresaPedido) && ValueUtil.isNotEmpty(SessionLavenderePda.cdEmpresaOld)) {
						EmpresaService.getInstance().changeEmpresaSessao(SessionLavenderePda.cdEmpresaOld);
						SessionLavenderePda.cdEmpresaOld = null;
					}
					return true;
				}
			}
		}
		return false;
	}

	public void setVisitaPositivadaTemp(Visita visitaSelected) {
		visita = visitaSelected;
	}
	
	public boolean novoPedidoSemCliente() throws SQLException {
		boolean result = novoPedido();
		CadPedidoForm cadPedidoForm = (CadPedidoForm) MainLavenderePda.getInstance().getActualForm();
		cadPedidoForm.getPedido().fromPedidoSemCliente = true;
		if (LavenderePdaConfig.isUsaConfigMenuCatalogo()) {
			cadPedidoForm.btNovoItemPedidoMenuCatalogoClick();
		} else {
			cadPedidoForm.btNovoItemClick(true);
		}
		return result;
	}

	public boolean novoPedido() throws SQLException {
		if (FechamentoDiarioUtil.isBloqueiaPorFechamentoDiario()) {
			return false;
		}
		
		Cliente cliente = SessionLavenderePda.getCliente();
		if (!validaClienteChurn(cliente)) return false;
		
		PedidoService.getInstance().validateBateria();
		SessionLavenderePda.cdTabelaPreco = "";
		if (bloqueiaNovoPedidoClienteSemContatos()) {
			return false;
		}
		if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isOcultaTodosAcessosRelUltimosPedidos()
				&& ValueUtil.isEmpty(SessionLavenderePda.getRepresentante().cdRepresentante)) {
			SessionLavenderePda.setRepresentante(RepresentanteService.getInstance().getRepresentanteById(cliente.cdRepresentante));
		}
		
		CadPedidoForm cadPedidoForm = new CadPedidoForm();
		cadPedidoForm.cadClienteMenuForm = this;

		RotaEntregaService.getInstance().validaRotaEntregaPadraoConfigurada(cliente);
		if (LavenderePdaConfig.usaTipoPedidoComoOpcaoMenuCliente) {
			cadPedidoForm.cdTipoPedidoAuto = cdTipoPedido;
			cdTipoPedido = null;
		}
		if (representantePossuiAlgumaAgenda()) {
			AgendaVisita agendaPendente = AgendaVisitaService.getInstance().findAgendaVisitaPendente(getCliente().cdRepresentante, getCliente().cdCliente, true);
			if (agendaPendente != null) {
				agendaVisita = agendaPendente;
			}
			cadPedidoForm.nuSequenciaAgenda = agendaVisita != null ? agendaVisita.nuSequencia : 0;
		}
		cliente.vlTotalPedidosValidateLimiteCredito = -1;
		cliente.vlTotalPedidosValidateLimiteCreditoConsignado = -1;
		if (LavenderePdaConfig.isUsaSugestaoParaNovoPedido() && LavenderePdaConfig.usaSugestaoParaNovoPedidoGiroProduto && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			String msgCriacaoNovoPedido = SessionLavenderePda.getCliente().somentePedidoPreOrcamento ? Messages.PEDIDO_NOVO_PEDIDO_ORCAMENTO_ESCOLHA : Messages.PEDIDO_NOVO_PEDIDO_ESCOLHA;
			int resultMessage = UiUtil.showMessage(Messages.BOTAO_NOVO_PEDIDO, msgCriacaoNovoPedido, new String[]{Messages.BOTAO_SUGESTAO_PEDIDO, Messages.BOTAO_SUGESTAO_PEDIDOGIRO, Messages.BOTAO_PEDIDO_BRANCO});
			if (resultMessage == 0) {
				if (!createAndShowSugestaoPedidoCliente(cadPedidoForm, cliente)) {
					return true;
				}
			} else if (resultMessage == 1) {
				if (createAndShowPedidoBaseadoGiroProduto(cadPedidoForm, cliente)) {
					return true;
				}
			}
		} else if (LavenderePdaConfig.isUsaSugestaoParaNovoPedido() && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			String msgCriacaoNovoPedido = SessionLavenderePda.getCliente().somentePedidoPreOrcamento ? Messages.PEDIDO_NOVO_PEDIDO_ORCAMENTO_ESCOLHA : Messages.PEDIDO_NOVO_PEDIDO_ESCOLHA;
			int resultMessage = UiUtil.showMessage(Messages.BOTAO_NOVO_PEDIDO, msgCriacaoNovoPedido, new String[]{Messages.BOTAO_SUGESTAO_PEDIDO, Messages.BOTAO_PEDIDO_BRANCO});
			if (resultMessage == 0) {
				if (!createAndShowSugestaoPedidoCliente(cadPedidoForm, cliente)) {
					return true;
				}
			}
		} else if (LavenderePdaConfig.usaSugestaoParaNovoPedidoGiroProduto && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			String msgCriacaoNovoPedido = SessionLavenderePda.getCliente().somentePedidoPreOrcamento ? Messages.PEDIDO_NOVO_PEDIDO_ORCAMENTO_ESCOLHA : Messages.PEDIDO_NOVO_PEDIDO_ESCOLHA;
			int resultMessage = UiUtil.showMessage(Messages.BOTAO_NOVO_PEDIDO, msgCriacaoNovoPedido, new String[]{Messages.BOTAO_SUGESTAO_PEDIDOGIRO, Messages.BOTAO_PEDIDO_BRANCO});
			if (resultMessage == 0) {
				if (createAndShowPedidoBaseadoGiroProduto(cadPedidoForm, cliente)) {
					return true;
				}
			}
		}
		cadPedidoForm.add();
		if (cancelaPedidoPorNaoSelecionarNotaCredito(cadPedidoForm, cliente)) {
			return false;
		}
		if (LavenderePdaConfig.apresentaMensagemClientePossuiIndiceFinanceiroConfigurado() && cliente.vlIndiceFinanceiro > 0) {
			double vlTotalNotaCredito = cadPedidoForm.getPedido().vlTotalNotaCredito;
			String mensagem = LavenderePdaConfig.utilizaNotasCredito() && vlTotalNotaCredito > 0 ? MessageUtil.getMessage(Messages.CLIENTE_POSSUI_INDICE_FINANCEIRO_CONFIGURADO_COM_NOTA_CREDITO, PedidoService.getInstance().vlMinimoPedidoDeAcordoComNotaCredito(vlTotalNotaCredito, cliente.vlIndiceFinanceiro)) : Messages.CLIENTE_POSSUI_INDICE_FINANCEIRO_CONFIGURADO_SEM_NOTA_CREDITO; 
			UiUtil.showWarnMessage(mensagem);
		}
		
		if (LavenderePdaConfig.utilizaEscolhaTransportadoraNoInicioPedido() && LavenderePdaConfig.escolhaTransportadoraPedidoPorCep()) {
			ListTransportadoraCepWindow listTransportadoraCepWindow = new ListTransportadoraCepWindow(cadPedidoForm.getPedido());
			listTransportadoraCepWindow.popup();
			cadPedidoForm.atualizaFrete(cadPedidoForm.getPedido(), true);
		}
		if (LavenderePdaConfig.isUsaAgendaDeVisitas() && !LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			cadPedidoForm.setPropVisitaNoPedido(visita);
		}
		
		show(cadPedidoForm);	
		if (LavenderePdaConfig.acessaDiretamenteTelaAdicionarItemPedido) {
			cadPedidoForm.novoItemPedido();
		}
		showHistoricoTabPrecoCliente(cadPedidoForm);
		return true;
	}

	private boolean validaCargaVencida() throws SQLException {
		if (CargaPedidoService.getInstance().isNecessarioValidarValidadeDasCargas()) {
			String[] botoes = new String[] {FrameworkMessages.BOTAO_FECHAR, FrameworkMessages.BOTAO_VISUALIZAR};
			int acao = UiUtil.showMessage(FrameworkMessages.TITULO_MSG_ATENCAO, Messages.CARGAPEDIDO_VALIDACAO_NOVO_PEDIDO, TYPE_MESSAGE.WARN, botoes);
			if (acao != 0) {
				new ListCargaPedidoVencidasWindow(Messages.CARGAPEDIDO_TITULO_LISTA_VENCIDOS).popup();
				
			}
			return true;
		} 
		return false;
	}

	private boolean createAndShowPedidoBaseadoGiroProduto(CadPedidoForm cadPedidoForm,  Cliente cliente) {
		try {
			Vector giroProdutoList = GiroProdutoService.getInstance().findAllProdutosGiroProdutoByCliente(cliente);
			if (giroProdutoList != null && giroProdutoList.size() > 0) {
				Pedido pedido; 
				if (LavenderePdaConfig.usaListaMultiplaInsercaoItensNoPedidoPorGiroProduto && LavenderePdaConfig.isPermiteInserirMultiplosItensPorVezNoPedido()) {
					pedido = GiroProdutoService.getInstance().criaPedidoListaGiroProduto(cliente);
				} else {
					pedido = GiroProdutoService.getInstance().criaPedidoBaseadoGiroProduto(cliente, giroProdutoList, true);
				}
				if (pedido != null && ValueUtil.isNotEmpty(pedido.nuPedido)) {
					if (LavenderePdaConfig.usaFiltroProdutosPorPlataformaVenda() && !PlataformaVendaClienteService.getInstance().validatePlataformaVendaCliente(cliente, pedido)) {
						UiUtil.showErrorMessage(Messages.CLIENTE_SEM_PLATAFORMA_ERRO);
						return false;
					}
					PedidoUiUtil.showRelInsercaoPedidoWindow(pedido);
					if (pedido.isSugestaoPedidoGiroProduto()) {
						cadPedidoForm.btNovoItemClick(true);
					}
					aposCriacaoPedidoAPartirDeSugestaoOuGiroProduto(cadPedidoForm, pedido);
					return true;
				} else {
					UiUtil.showErrorMessage(Messages.TABELA_PRECO_AUSENTE);
					return false;
				}
			} else {
				UiUtil.showWarnMessage(Messages.PEDIDO_NOVO_PEDIDOGIRO_SUGESTAO_NAO_ENCONTRADA);
			}
		} catch (ValidationException ve) {
			if (ValueUtil.isNotEmpty(ve.params)) {
				UiUtil.showErrorMessage(Messages.ERRO, MessageUtil.getMessage(Messages.PEDIDO_NOVO_PEDIDOGIRO_ERRO_SEM_CAMPOS_NECESSARIOS, ve.params));
			} else {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.PEDIDO_NOVO_PEDIDOGIRO_ERRO_OUTROS, ve.getMessage()));
			}
		} catch (Throwable e) {
			UiUtil.showErrorMessage(Messages.PEDIDO_NOVO_PEDIDOGIRO_ERRO);
		}
		return false;
	}

	private boolean createAndShowSugestaoPedidoCliente(CadPedidoForm cadPedidoForm, Cliente cliente) throws SQLException {
		boolean hadProblemOcurredInCopyPedido = false;
		try {
			Pedido pedido = null;
			if (LavenderePdaConfig.qtdPedidoSugestaoParaNovoPedido <= 1) {
				// Continua da mesma forma, sem apresentar popup. Pois sempre deve trazer o último pedido de qualquer forma.
				UiUtil.showProcessingMessage();
				try {
					pedido = PedidoService.getInstance().findUltimoPedidoNaoAbertoByCliente(cliente.cdCliente);
					if (LavenderePdaConfig.usaNovoPedidoOrcamentoSemRegistroChegada && pedido.cdStatusOrcamento != null) {
						pedido.statusOrcamento = StatusOrcamentoService.getInstance().getStatusOrcamentoInicialPedidoBySugestao(pedido.cdStatusOrcamento);
						pedido.cdStatusOrcamento = pedido.statusOrcamento.cdStatusOrcamento;
					}
					ClienteService.getInstance().copyAndInsertPedidoCliente(pedido, cliente, showPopupUsaVerbaPedidoSugestao(pedido), true);
				} catch (Throwable e) {
					hadProblemOcurredInCopyPedido = true;
					pedido = null;
					UiUtil.showErrorMessage(Messages.PEDIDO_NOVO_PEDIDO_SUGESTAO_ERROR);
				}
			} else {
				// Apresenta a popup com a lista de pedidos de sugestão. Quantidade de pedidos configurada parametrizada.
				ListSugestaoPedidoWindow listSugestaoPedidoWindow = new ListSugestaoPedidoWindow(cliente.cdCliente);
				listSugestaoPedidoWindow.popup();
				if (listSugestaoPedidoWindow.closedByBtFechar) {
					return false;
				}
				//--
				if (ValueUtil.isNotEmpty(listSugestaoPedidoWindow.rowKeySelected)) {
					pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(listSugestaoPedidoWindow.rowKeySelected);
					ClienteService.getInstance().copyAndInsertPedidoCliente(pedido, cliente, showPopupUsaVerbaPedidoSugestao(pedido), true);
				}
			}
			if (pedido != null) {
				aposCriacaoPedidoAPartirDeSugestaoOuGiroProduto(cadPedidoForm, pedido);
				return false;
			} else if (!hadProblemOcurredInCopyPedido) {
				UiUtil.showWarnMessage(Messages.PEDIDO_NOVO_PEDIDO_SUGESTAO_NAO_ENCONTRADA);
			}

		} finally {
			UiUtil.unpopProcessingMessage();
		}
		return true;
	}

	private boolean showPopupUsaVerbaPedidoSugestao(Pedido pedido) throws SQLException {
		if (LavenderePdaConfig.isUsaConfirmacaoVerbaPedidoSugestao() && !pedido.isIgnoraControleVerba()) {
			double vlVerba = LavenderePdaConfig.permiteDescValorPorPedidoConsumindoVerba > 0 ? ValueUtil.round(pedido.vlVerbaPedido) +  pedido.vlDesconto * -1 : ValueUtil.round(pedido.vlVerbaPedido);
			if (vlVerba * -1 > 0) {
				return UiUtil.showConfirmYesNoMessage(MessageUtil.getMessage(Messages.VERBASALDO_MSG_CONFIRMACAO_PEDIDO_SUGESTAO, StringUtil.getStringValuePositivo(vlVerba)));
			}
		}
		return false;
	}

	private boolean atualizaCadastroCliente(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente > 0 && !cliente.isNovoClienteDefaultParaNovoPedido() && !cliente.isClienteDefaultParaNovoPedido()) {
			return atualizaCadClientePorDias(cliente);
		} else if (LavenderePdaConfig.habilitaAtualizacaoCadastroCliente && !cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido()) {
			if (ClienteService.getInstance().hasClienteUpdates()) {
				UiUtil.showInfoMessage(Messages.MSG_RECEBE_DADOS_CLIENTES_BEFORE_PEDIDO);
				ClienteService.getInstance().recebeAtualizacaoCliente();
				cliente = (Cliente)ClienteService.getInstance().findByPrimaryKey(cliente);
			}
			if (cliente.isAtualizaCadastroWmw()) {
				return showCadClienteAtuaWindow(cliente);
			} else if (LavenderePdaConfig.nuDiasAtualizaCadastroCliente > 0 && ValueUtil.isNotEmpty(cliente.dtAtualizacaoCadastroWmw)) {
				if (DateUtil.getDaysBetween(DateUtil.getCurrentDate(), cliente.dtAtualizacaoCadastroWmw) >= LavenderePdaConfig.nuDiasAtualizaCadastroCliente) {
					ClienteAtua clienteAtuaFilter = ClienteAtuaService.getInstance().getClienteAtuaFilter(cliente, LavenderePdaConfig.nuDiasAtualizaCadastroCliente);
					clienteAtuaFilter.flAtualizaCadastroWmw = ClienteAtua.FLTIPOALTERACAO_ALTERADO;
					int count = ClienteAtuaService.getInstance().countByExample(clienteAtuaFilter);
					if (count == 0) {
						return showCadClienteAtuaWindow(cliente);
					}
				}
			}
		}
		return true;
	}

	private boolean atualizaCadClientePorDias(Cliente cliente) throws SQLException {
		Date dtUltimaAtualizacaoCLiente = cliente.dtAtualizacaoCadastro;
		if (!cliente.isNovoCliente() && !cliente.isClienteDefaultParaNovoPedido() 
				&& (ValueUtil.isEmpty(dtUltimaAtualizacaoCLiente) || DateUtil.getDaysBetween(DateUtil.getCurrentDate(), dtUltimaAtualizacaoCLiente) >= LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente)) {
			ClienteAtua clienteAtuaFilter = ClienteAtuaService.getInstance().getClienteAtuaFilter(cliente, LavenderePdaConfig.nuDiasSolicitaAtualizacaoCliente);
			int count = ClienteAtuaService.getInstance().countByExample(clienteAtuaFilter);
			if (count == 0) {
				return showCadClienteAtuaWindow(cliente);
			}
		}
		return true;
	}

	private boolean showCadClienteAtuaWindow(Cliente cliente) throws SQLException {
		CadClienteAtuaDynWindow cadClienteAtuaDynForm = new CadClienteAtuaDynWindow(cliente, true);
		cadClienteAtuaDynForm.popup();
		return cadClienteAtuaDynForm.updateSucess && showPopUpAtualizaCliente();
	}

	private boolean showHistoricoPedidosCliente() throws SQLException {
		if (LavenderePdaConfig.usaRelUltimosPedidosDoCliente && !LavenderePdaConfig.isOcultaTodosAcessosRelUltimosPedidos()) {
			ListPedidoForm listPedidoForm = new ListPedidoForm();
			listPedidoForm.title = Messages.CLIENTE_ULTIMOS_PEDIDOS;
			listPedidoForm.inConsultaUltimosPedidos = true;
			listPedidoForm.cadClienteMenuForm = this;
			show(listPedidoForm);
			if (!listPedidoForm.hasPedidosRecentes) {
				listPedidoForm.close();
				return false;
			}
			return true;
		}
		return false;
	}

	private void showHistoricoTabPrecoCliente(CadPedidoForm cadPedidoForm) throws SQLException {
		if (LavenderePdaConfig.usaHistoricoTabelaPrecoUsadasPorCliente) {
			RelHistoricoTabelaPrecoWindow infoUltimasTabsUsadasForm = new RelHistoricoTabelaPrecoWindow(SessionLavenderePda.getCliente().cdCliente);
			if (infoUltimasTabsUsadasForm.grid.size() > 0) {
				infoUltimasTabsUsadasForm.popup();
				if (!ValueUtil.isEmpty(infoUltimasTabsUsadasForm.cdTabelaPrecoSelected)) {
					String cdTabPrecoAtual = cadPedidoForm.cbTabelaPreco.getValue();
					cadPedidoForm.cbTabelaPreco.setValue(infoUltimasTabsUsadasForm.cdTabelaPrecoSelected);
					if (ValueUtil.isEmpty(cadPedidoForm.cbTabelaPreco.getValue())) {
						UiUtil.showErrorMessage(Messages.TABELAPRECO_INEXISTENTE);
						cadPedidoForm.cbTabelaPreco.setValue(cdTabPrecoAtual);
					}
					cadPedidoForm.cbTabelaPrecoChange();
				}
			}
		}
	}

	public boolean isValidateClienteInadimplente(Cliente cliente, boolean onCreateNovoPedido) throws SQLException {
		try {
			ClienteService.getInstance().validateClienteInadimplente(cliente, false, false, false, false);
		} catch (ClienteInadimplenteException ex) {
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_REDE_LIBERA_SENHA) {
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() && onCreateNovoPedido) {
					int opcao = showOpcaoPagamentoAVista(ex.getMessage(), true);
					if (opcao == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						return true;
					} else if (opcao == 0) {
						return false;
					}
				}
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ex.getMessage());
				senhaForm.setCdCliente(cliente.cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_REDE_ATRASADO_NOVO_PEDIDO);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				ClienteService.getInstance().saveClienteRedeLiberadoComSenha(cliente);
				repaint();
			}
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_BLOQUEADO) {
				if (LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado() && onCreateNovoPedido) {
					if (showOpcaoPagamentoAVista(ex.getMessage(), false) == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						return true;
					} else {
						return false;
					}
				}
				throw new ValidationException(ex.getMessage());
			}	
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_LIBERA_SENHA) {
				if ((LavenderePdaConfig.isApresentaPopUPPedidoAVistaSenha() || LavenderePdaConfig.isPermitePedidoAVistaClienteAtrasado()) && onCreateNovoPedido) {
					int opcao = showOpcaoPagamentoAVista(ex.getMessage(), true);
					if (opcao == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						SessionLavenderePda.getCliente().flClienteAtrasadoLiberadoSenha = false;
						return true;
					} else if (opcao == 0) {
						return false;
					}
				}
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(ex.getMessage());
				senhaForm.setCdCliente(cliente.cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				cliente.flClienteAtrasadoLiberadoSenha = true;
				if (SessionLavenderePda.isSessionCliente()) {
					SessionLavenderePda.getCliente().flClienteAtrasadoLiberadoSenha = true;
				}
				if (ClienteService.getInstance().isClienteAtrasadoPorVlTotalTitulosAtraso(cliente)) {
					UiUtil.showInfoMessage(Messages.CLIENTE_ATRASADO_VLTOTAL_TITULOS_LIBERADO);
				}
				repaint();
			}
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_AVISA || ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_REDE) {
				if ((LavenderePdaConfig.isApresentaPopUPPedidoAVista() && cliente.isStatusBloqueadoPorAtraso()) && onCreateNovoPedido) {
					if (showOpcaoPagamentoAVista(ex.getMessage(), false) == 2) {
						SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
						return true;
					} else {
						return false;
					}
				}
				String[] params = ClienteService.getInstance().getClienteInadimplementeParams(SessionLavenderePda.getCliente(), TituloFinanceiroService.getInstance().getDiasAtrasoCliente(SessionLavenderePda.getCliente()));
				return showPopUpMotivoClienteAtrasado(params, ex);
			}
			if (ex.TIPO_CLIENTE_INADIMPLENTE == Cliente.CLIENTE_ATRASADO_LIBERADO_PAGAMENTO) {
				UiUtil.showInfoMessage(Messages.CLIENTE_ATRASADO_LIBERADO_PAGAMENTO);
			}
		}
		return true;
	}
	
	private boolean isValidaClienteAtrasadoPorVlTotalTitulosAtraso(Cliente cliente) throws SQLException {
		if (!LavenderePdaConfig.validaClienteAtrasadoPorValorTotalTitulosEmAtrasoApenasAoFecharPedido && ClienteService.getInstance().isValidaClienteAtrasadoPorValorTotalTitulosEmAtraso(null, cliente)) {
			double vlTotalTitulosAtraso = TituloFinanceiroService.getInstance().getVlTotalTitulosAtraso();
			if (vlTotalTitulosAtraso > cliente.vlMaxTitulosAtraso) {
				String msgErro = MessageUtil.getMessage(Messages.CLIENTE_ATRASADO_VLTOTAL_TITULOS, new Object[] {StringUtil.getStringValueToInterface(vlTotalTitulosAtraso), StringUtil.getStringValueToInterface(cliente.vlMaxTitulosAtraso)});
				if (LavenderePdaConfig.isBloqueiaClienteAtrasadoPorValorTotalTitulosEmAtraso() && LavenderePdaConfig.liberaComSenhaClienteAtrasadoNovoPedido()) {
					AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
					senhaForm.setMensagem(msgErro);
					senhaForm.setCdCliente(cliente.cdCliente);
					senhaForm.setChaveSemente(SenhaDinamica.SENHA_CLIENTE_ATRASADO_NOVO_PEDIDO);
					if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
						return false;
					}
					if (LavenderePdaConfig.validaClienteAtrasadoApenasAoFecharPedido) {
						int qtDiasAtrasoCliente = TituloFinanceiroService.getInstance().getDiasAtrasoCliente(SessionLavenderePda.getCliente());
						final int nuDiasAtrasoParam = LavenderePdaConfig.nuDiasLiberacaoComSenhaClienteAtrasadoNovoPedido;
						if (nuDiasAtrasoParam > 0 && nuDiasAtrasoParam <= qtDiasAtrasoCliente) {
							UiUtil.showInfoMessage(Messages.CLIENTE_ATRASADO_DIAS_TITULOS_LIBERADO);
						}
					}
					cliente.flClienteAtrasadoLiberadoSenha = true;
					if (SessionLavenderePda.isSessionCliente()) {
						SessionLavenderePda.getCliente().flClienteAtrasadoLiberadoSenha = true;
					}
				} else {
					if (LavenderePdaConfig.isBloqueiaClienteAtrasadoPorValorTotalTitulosEmAtraso()) {
						UiUtil.showErrorMessage(msgErro);
						return false;
					}
					UiUtil.showWarnMessage(msgErro);
				}
			}
		}
		return true;
	}
	
	public int showOpcaoPagamentoAVista(String mensagem, boolean showBtLiberarComSenha) {
		String msg = ValueUtil.isEmpty(mensagem) ? Messages.CLIENTE_BLOQUEADO_PEDIDO_A_VISTA : mensagem;
		OpcaoPagamentoAVistaWindow opcaoPagamentoAVistaWindow = new OpcaoPagamentoAVistaWindow(msg, showBtLiberarComSenha);
		opcaoPagamentoAVistaWindow.popup();
		return opcaoPagamentoAVistaWindow.options;
	}
	
	public boolean showPopUpMotivoClienteAtrasado(String[] params, final ClienteInadimplenteException cliEx) throws SQLException {
		String message = getMessageMotivoAtraso(params, cliEx);
		CadMotivoClienteAtrasadoWindow cadMotivoClienteAtrasadoWindow = new CadMotivoClienteAtrasadoWindow(message, cliEx.isSomenteRedeAtraso(), cliEx.qtClientesRedeAtraso);
		cadMotivoClienteAtrasadoWindow.popup();
		int opcao = cadMotivoClienteAtrasadoWindow.options;
		if (opcao == 0) {
			return true;
		} else if (opcao == 1) {
			btVerTitulosClick(cliEx);
			return false;
		} else if (opcao == 3) {
			btVerRedeClienteClick(cliEx);
			return false;
		} else {
			return false;
		}
	}
	
	public Boolean showPopUpAtualizaCliente() throws SQLException {
		if (!LavenderePdaConfig.permiteAtualizacaoContatoNaAtualizacaoDeClientePorPeriodo()) {
			return true;
		}
		
		InfoAtualizaContatoWindow infoExtraItemPedidoWindow = new InfoAtualizaContatoWindow();
		infoExtraItemPedidoWindow.popup();
		int opcao = infoExtraItemPedidoWindow.options;
		if (opcao == 0) {
			return true;
		} else if (opcao == 1) {
			btContatoClick();
			return false;
		} else {
			return false;
		}
	}

	private String getMessageMotivoAtraso(String[] params, final ClienteInadimplenteException cliEx) {
		String message = MessageUtil.getMessage(Messages.MSG_CLIENTE_ATRASADO_TITULO, params);
		if (!cliEx.isSomenteRedeAtraso() && cliEx.qtClientesRedeAtraso > 0) {
			message += MessageUtil.getMessage(Messages.MSG_REDE_CLIENTE_ATRASADO, new String[] {cliEx.redeAtraso.toString(), StringUtil.getStringValue(cliEx.qtClientesRedeAtraso)});
		}
		if (cliEx.isSomenteRedeAtraso()) {
			message = MessageUtil.getMessage(Messages.MSG_REDE_CLIENTE_ATRASADO, new String[] {cliEx.redeAtraso.toString(), StringUtil.getStringValue(cliEx.qtClientesRedeAtraso)});
		}
		return message;
	}

	private void btVerRedeClienteClick(final ClienteInadimplenteException cliEx) throws SQLException {
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceiraDyn(SessionLavenderePda.getCliente());
		CadFichaFinanceiraDynForm cadFichaFinanceiraForm = new CadFichaFinanceiraDynForm();
		fichaFinanceira = (fichaFinanceira == null) ? new FichaFinanceira() : fichaFinanceira;
		cadFichaFinanceiraForm.edit(fichaFinanceira);
		cadFichaFinanceiraForm.isAcessoPopUpMotivo = true;
		cadFichaFinanceiraForm.clienteInadiplenteException = cliEx;
		show(cadFichaFinanceiraForm);
		cadFichaFinanceiraForm.setActiveTabRede();
	}
	
	private void btVerTitulosClick(final ClienteInadimplenteException cliEx) throws SQLException {
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceiraDyn(SessionLavenderePda.getCliente());
		CadFichaFinanceiraDynForm cadFichaFinanceiraForm = new CadFichaFinanceiraDynForm(true);
		cadFichaFinanceiraForm.edit(fichaFinanceira);
		cadFichaFinanceiraForm.isAcessoPopUpMotivo = true;
		cadFichaFinanceiraForm.clienteInadiplenteException = cliEx;
		show(cadFichaFinanceiraForm);
		cadFichaFinanceiraForm.setActiveTabTitulosItem();
	}

	private boolean validateClienteSemPedido(Cliente cliente, boolean onCreateNovoPedido) throws SQLException {
		if ((LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido > 0) && (cliente.nuDiasSemPedido >= LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido) && !ValueUtil.VALOR_SIM.equals(SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista)) {
			String message = MessageUtil.getMessage(Messages.CLIENTE_SEM_PEDIDO_BLOQUEIO, new String[]{StringUtil.getStringValueToInterface(LavenderePdaConfig.nuDiasBloqueiaClienteSemPedido)});
			if (LavenderePdaConfig.isPermitePedidoAVistaClienteBloqueado() && onCreateNovoPedido) {
				int opcao = showOpcaoPagamentoAVista(message, true);
				if (opcao == 2) {
					SessionLavenderePda.getCliente().flClienteLiberadoPedidoAVista = ValueUtil.VALOR_SIM;
					return false;
				} else if (opcao == 0) {
					return true;
				}
			}
			AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
			senhaForm.setMensagem(message);
			senhaForm.setCdCliente(cliente.cdCliente);
			senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_CLIENTE_SEM_PEDIDO);
			if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
				repaint();
				return true;
			}
			repaint();
		} else if (LavenderePdaConfig.nuDiasAlertaClienteSemPedido > 0 && SessionLavenderePda.getCliente().nuDiasSemPedido >= LavenderePdaConfig.nuDiasAlertaClienteSemPedido) {
			UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.CLIENTE_SEM_PEDIDO_ALERTA, new String[]{StringUtil.getStringValueToInterface(LavenderePdaConfig.nuDiasAlertaClienteSemPedido)}));
		} else if (LavenderePdaConfig.isUsaAlertaBloqueioClienteSemPedido() && SessionLavenderePda.getCliente().nuDiasSemPedido == -1) {
			UiUtil.showWarnMessage(Messages.CLIENTE_SEM_PEDIDO_SEM_HISTORICO);
		}
		return false;
	}

	public void showTitulosFinanceiros() throws SQLException {
		CadPedidoForm cadPedidoForm = new CadPedidoForm();
		cadPedidoForm.add();
		show(cadPedidoForm);
		// --
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceiraDyn(SessionLavenderePda.getCliente());
		CadFichaFinanceiraDynForm cadFichaFinanceiraForm = new CadFichaFinanceiraDynForm();
		cadFichaFinanceiraForm.edit(fichaFinanceira);
		show(cadFichaFinanceiraForm);
		cadFichaFinanceiraForm.setActiveTabTitulosItem();
	}
	
	public void btFotosClienteClick() throws SQLException {
		ImageSliderClienteWindow imageSliderClienteWindow = new ImageSliderClienteWindow(getCliente());
		imageSliderClienteWindow.popup();
	}
	
	public void btAtualizaDadosClienteClick() throws SQLException {
		new CadClienteAtuaDynWindow(getCliente(), false).popup();
	}

	public void btMotivoVisitaClick() throws SQLException {
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		}
		if (getDomain() instanceof AgendaVisita) {
			agendaVisita = (AgendaVisita) getDomain();
		} else {
			Cliente cliente = (Cliente) getDomain();
			agendaVisita = AgendaVisitaService.getInstance().findAgendaVisitaPendente(cliente.cdRepresentante, cliente.cdCliente);
		}
		if (agendaVisita != null) {
			int cdTipoVisita = LavenderePdaConfig.isCarregaMotivoVisitaPositiva() ? Visita.CD_VISITA_TODOS : Visita.CD_VISITA_NAO_POSITIVADA; 
			CadVisitaForm cadVisitaForm;
			cadVisitaForm = new CadVisitaForm(cdTipoVisita);
			cadVisitaForm.edit(agendaVisita);
			cadVisitaForm.setBaseCrudCadMenuForm(this);
			show(cadVisitaForm);
		} else {
			btVisitaManualClick();
		}
	}
	
	private void btVisitaManualClick() throws SQLException {
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return;
		} 
		if (!LavenderePdaConfig.isNaoConsideraRegistroVisitaManualTolerancia()) {
			if (LavenderePdaConfig.isNaoConsideraApenasRegistroVisitaManualDiaAnteriorTolerancia()) {
				if (!AgendaVisitaService.getInstance().isHasAgendaVisitaNoDiaAnterior(getCliente().cdCliente)) {
					if (!validaVisitaForaAgenda(getCliente())) {
						return;
					}
				}
			} else if (AgendaVisitaService.getInstance().findAgendaVisitaPendente(SessionLavenderePda.usuarioPdaRep.cdRepresentante, getCliente().cdCliente) == null && !validaVisitaForaAgenda(getCliente())) {
				return;	
			}
		} 
		CadVisitaManualForm cadManualForm = new CadVisitaManualForm();
		Visita visitaHoje = VisitaService.getInstance().findVisitaManualForClienteToday(SessionLavenderePda.getCliente().cdCliente);
		if (visitaHoje == null) {
			visitaHoje = new Visita();
		}
		if (LavenderePdaConfig.usaCopiaAgendaRepresentanteParaSupervisor) {
			visitaHoje.cdRepOriginal = agendaVisita.cdRepOriginal;
    	}
		cadManualForm.editOrAdd(visitaHoje);
		show(cadManualForm);
	}
	
	private void btEnderecosClienteClick() throws SQLException {
		ListClienteEnderecoForm clienteEnderecoForm = new ListClienteEnderecoForm(getCliente());
		show(clienteEnderecoForm);
	}
	
	public AgendaVisita getAgendaVisita() {
		return this.agendaVisita;
	}
	
	public static boolean btRegistrarAtualizarChegadaClick(Date dtAgendaAtual) throws SQLException {
		AgendaVisita agendaVisita = AgendaVisitaService.getInstance().findAgendaVisitaPendente(SessionLavenderePda.getRepresentante().cdRepresentante, SessionLavenderePda.getCliente().cdCliente, dtAgendaAtual, true);
		return btRegistrarAtualizarChegadaClick(agendaVisita == null ? new AgendaVisita() : agendaVisita, dtAgendaAtual, false);
	}
	
	private static boolean cadastraEnderecoCoordenadaCliente() throws SQLException {
		boolean retorno = true;
		if (UiUtil.showMessage(Messages.REGISTRO_CHEGADA_CADASTRAR_COORDENADA, new String[] {Messages.CAD_COORD_TITULO_ABREVIADO, Messages.BT_CANCELAR}) == 1) return false;
		if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasSomenteMenuCliente()) {
			CadClienteMenuForm cad = new CadClienteMenuForm();
			cad.setDomain(SessionLavenderePda.getCliente());
			cad.show(cad);
			return true;
		} else {
			CadCoordenadasGeograficasWindow cadCoordenadasGeograficasWindow = new CadCoordenadasGeograficasWindow(SessionLavenderePda.getCliente(), false);
			cadCoordenadasGeograficasWindow.popup();
			retorno = cadCoordenadasGeograficasWindow.cadastrouCoordenada;
		}
		if (!retorno) {
			UiUtil.showErrorMessage(Messages.REGISTRO_CHEGADA_CLIENTE_SEM_COORD_GEOGRAFICAS);
		}
		return retorno;
	}
	
	public static boolean btRegistrarAtualizarChegadaClick(AgendaVisita agendaVisita, Date dtAgendaAtual, boolean isFlAgendada) throws SQLException {
		
		if (!validaPresencaNoLocal()) return false;
		
		rowKeyDaProximaSequencia = ValueUtil.isEmpty(rowKeyDaProximaSequencia) ? getRowkeyDaProximaSequencia() : rowKeyDaProximaSequencia;
		agendaVisita.rowkeyDaProximaSequencia = rowKeyDaProximaSequencia;
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return false;
		}
		if (agendaVisita.nuSequencia == 0 && !validaVisitaForaAgenda(SessionLavenderePda.getCliente())) {
			return false;
		}
		Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
		boolean registrouChegada = false;
		if (LavenderePdaConfig.usaVisitaFoto && visitaEmAndamento != null) {
			visitaEmAndamento.setVisitaFotoList(VisitaFotoService.getInstance().findAllVisitaFotoByVisita(visitaEmAndamento));
		}
		if (dtAgendaAtual == null) {
			dtAgendaAtual = agendaVisita.dtAgendaAtual;
		}
		CadRegistroChegadaClienteWindow cadRegistroChegada = new CadRegistroChegadaClienteWindow(SessionLavenderePda.getCliente(), agendaVisita,  visitaEmAndamento, dtAgendaAtual, isFlAgendada);
		cadRegistroChegada.popup();
		if (cadRegistroChegada.registrouChegada) {
			registrouChegada = true;
			if (LavenderePdaConfig.usaVisitaFoto) {
				VisitaFotoDao.getInstance().updateFotosVisitaParaEnvio(cadRegistroChegada.visita.cdEmpresa, cadRegistroChegada.visita.cdRepresentante, cadRegistroChegada.visita.flOrigemVisita, cadRegistroChegada.visita.cdVisita, VisitaFoto.FLTIPOALTERACAO_INSERIDO);
			}
			if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
				String cdSessao = PedidoService.getInstance().generateIdGlobal();
				NotificacaoPdaService.getInstance().createNotificacaoPdaInicioPedidoAndSend2Web(cdSessao, cadRegistroChegada.visita.nuSequencia);
			}
			if (LavenderePdaConfig.nuDiasRespPesq() > 0 || LavenderePdaConfig.isObrigaRespostaRegistroChegada()) {
				showPesquisaPendente();
				registrouChegada = SessionLavenderePda.visitaAndamento != null;
			}
		}
		return registrouChegada;
	}

	private static void showPesquisaPendente() throws SQLException {
		listPesquisaClienteForm = new ListPesquisaClienteForm(true, SessionLavenderePda.getCliente());
		boolean isObrigaRespPesquisa = LavenderePdaConfig.nuDiasRespPesq() > 0;
		boolean isObrigaRespPesquisaRegistroChegada = LavenderePdaConfig.isObrigaRespostaRegistroChegada();
		if ((LavenderePdaConfig.isUsaPesquisaCliente() && !SessionLavenderePda.getCliente().isNovoCliente() && !SessionLavenderePda.getCliente().isClienteDefaultParaNovoPedido()) || (LavenderePdaConfig.isUsaPesquisaNovoCliente() && SessionLavenderePda.getCliente().isNovoCliente())) {
			if ((isObrigaRespPesquisa || isObrigaRespPesquisaRegistroChegada) && listPesquisaClienteForm.possuiPesquisaPendente()) {
				UiUtil.showConfirmMessage(isObrigaRespPesquisaRegistroChegada ? Messages.OBRIGA_RESPONDER_PESQUISA_SEMPRE_AO_REGISTRAR_CHEGADA_CLIENTE : Messages.OBRIGA_RESPONDER_PESQUISA_REGISTRAR_CHEGADA_CLIENTE);
				ListPesquisaClienteWindow.getInstance(SessionLavenderePda.getCliente()).popup();
			}
		}
	}

	private static boolean validaPresencaNoLocal() throws SQLException {
		if (!LavenderePdaConfig.bloquearRegistroChegadaRepNaoPresente || VmUtil.isSimulador()) return true; 
		
		if (LavenderePdaConfig.isUsaCadastroCoordenadasGeograficasCliente() && EnderecoGpsPdaService.getInstance().isCadastraCoordenada(SessionLavenderePda.getCliente())) {
			if (!cadastraEnderecoCoordenadaCliente()) {
				UiUtil.showErrorMessage(Messages.REGISTRO_CHEGADA_CLIENTE_SEM_COORD_GEOGRAFICAS);
				return false;
			}
			if (EnderecoGpsPdaService.getInstance().isCadastraCoordenada(SessionLavenderePda.getCliente())) return false;
		}
		boolean presente = true;
		UiUtil.showProcessingMessage();
		try {
			presente = VisitaService.getInstance().validaPresencaClienteCoordenada(SessionLavenderePda.getCliente());
			if (!presente) {
				UiUtil.showErrorMessage(Messages.REGISTRO_CHEGADA_CLIENTE_FORA_TOLERANCIA);
				return false;
			}
		} finally {
			UiUtil.unpopProcessingMessage();
		}
		return true;
	}
	
	protected static String getRegistrarSaidaMessage(boolean novoCliente) {
		return novoCliente ? Messages.MSG_REGISTRAR_SAIDA_NOVO_CLIENTE : Messages.MSG_REGISTRAR_SAIDA;
	}
	
	protected static String getRegistrarChegadaMessage(boolean novoCliente) {
		return novoCliente ? Messages.MSG_REGISTRAR_CHEGADA_NOVO_CLIENTE : Messages.MSG_REGISTRAR_CHEGADA;
	}

	public static boolean btRegistrarAtualizarSaidaClick() throws SQLException {
		return btRegistrarSaidaClick(SessionLavenderePda.visitaAndamento, false);
	}
	
	public static boolean btRegistrarSaidaClick(Visita visitaEmAndamento, boolean registraAutomatico) throws SQLException {
		if (!LiberacaoSenhaWindow.verificaGpsDesligado(false, false)) {
			return false;
		}
		if (visitaEmAndamento == null) {
			visitaEmAndamento = VisitaService.getInstance().findLastRegistroSaidaDoCliente(SessionLavenderePda.getCliente().cdCliente);
		}
		boolean novoCliente;
		if (SessionLavenderePda.getCliente() != null) {
			if (SessionLavenderePda.getCliente().isClienteDefaultParaNovoPedido()) {
				return false;
			}
			novoCliente = SessionLavenderePda.getCliente().isNovoCliente();
		} else {
			Cliente cliente = ClienteService.getInstance().getCliente(SessionLavenderePda.visitaAndamento.cdEmpresa, SessionLavenderePda.visitaAndamento.cdRepresentante, SessionLavenderePda.visitaAndamento.cdCliente);
			novoCliente = cliente.isNovoCliente();
		}
		
		if (LavenderePdaConfig.exibirNotasDevolucaoNaFichaFinanceira && LavenderePdaConfig.obrigaAprovacaoNotasDeDevolucao() && NfDevolucaoService.getInstance().hasNotaDevolucaoPendente(NfDevolucao.getNfDevolucao())) {
			showNfDevolucaoWindow();
			if (NfDevolucaoService.getInstance().hasNotaDevolucaoPendente(NfDevolucao.getNfDevolucao())) {
				return false;
			}
		}
		
		CadRegistroSaidaClienteWindow cadRegistroSaida = new CadRegistroSaidaClienteWindow(visitaEmAndamento, registraAutomatico, novoCliente);
		cadRegistroSaida.popup();
		if (cadRegistroSaida.registrouSaida) {
			visitaEmAndamento.flEnviadoServidor = ValueUtil.VALOR_NAO;
			VisitaService.getInstance().fechaVisita(visitaEmAndamento, !registraAutomatico);
			if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
				NotificacaoPdaService.getInstance().restauraNotificacaoPdaAndSend2Web(VisitaService.getInstance().generateIdGlobal());
			}
			int sizeVisitasReplicadas = cadRegistroSaida.visitasReplicadasList.size();
			for (int i = 0; i < sizeVisitasReplicadas; i++) {
				Visita visitaSendoReplicada = (Visita) cadRegistroSaida.visitasReplicadasList.items[i];
				visitaSendoReplicada.flEnviadoServidor = ValueUtil.VALOR_NAO;
				VisitaService.getInstance().fechaVisita(visitaSendoReplicada, !registraAutomatico);
			}
			SessionLavenderePda.setAgenda(null);
		}
		return cadRegistroSaida.registrouSaida;
	}
	
	
	private static void showNfDevolucaoWindow() throws SQLException {
		UiUtil.showConfirmMessage(Messages.NFDEVOLUCAO_PENDENCIA_CHECKOUT);
		new ListNfDevolucaoWindow(NfDevolucao.getNfDevolucao()).popup();
		
	}

	private static String getRowkeyDaProximaSequencia() throws SQLException {
		Date dataFilter = DateUtil.getCurrentDate();
		dataFilter.advance(-1);
		Vector agendaVisitaList = AgendaVisitaService.getInstance().findAgendasVisitas(new AgendaVisita(), dataFilter, StatusAgenda.STATUSAGENDA_CDAVISITAR, false, null, null);
		if (ValueUtil.isEmpty(agendaVisitaList)) {
			agendaVisitaList = AgendaVisitaService.getInstance().findAgendasVisitas(new AgendaVisita(), DateUtil.getCurrentDate(), StatusAgenda.STATUSAGENDA_CDAVISITAR, false, null, null);
		}
		if (ValueUtil.isNotEmpty(agendaVisitaList)) {
			AgendaVisita.sortAttr = "NUSEQUENCIA";
		   	SortUtil.qsortInt(agendaVisitaList.items, 0, agendaVisitaList.size() - 1, true);
		   	AgendaVisita.sortAttr = "";
		   	AgendaVisita agendaVisitaCliente = (AgendaVisita) agendaVisitaList.items[0];
		   	return agendaVisitaCliente.getRowKey();
		}
		return null;
	}
	 
	
	private void btDesfazerChegadaClick() throws SQLException {
		Visita visitaEmAndamento = SessionLavenderePda.visitaAndamento;
		if (VisitaService.getInstance().isVisitaEmAndamento(visitaEmAndamento)) {
			if (UiUtil.showConfirmYesNoMessage(Messages.MSG_EXCLUIR_REGISTRO_CHEGADA)) {
				PesquisaAppService.getInstance().deletePesquisasByVisitaAndamento();
				VisitaService.getInstance().desfazVisitaEmAndamento(visitaEmAndamento);
				if (LavenderePdaConfig.enviaInformacoesVisitaOnline) {
					NotificacaoPdaService.getInstance().restauraNotificacaoPdaAndSend2Web(VisitaService.getInstance().generateIdGlobal());
				}
			}
		}
		if (LavenderePdaConfig.isToleranciaVisitasForaAgenda() && LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
			SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada = null;
		}
		remontaMenuFuncionalidades();
	}
	
	private Image getEmptyIcon() {
		int heigthContainerIcons = (int)(UiUtil.getControlPreferredHeight() * 1.2);
		int alturaLargura = (int)(heigthContainerIcons * 0.70);
		return UiUtil.getEmptyImage(alturaLargura, alturaLargura);
	
	}

	@Override
	public void onFormShow() throws SQLException {
		super.onFormShow();
		Cliente cliente = getCliente();
		if (DivulgaInfoService.getInstance().apresentaDivulgaInfoCliente(cliente)) {
			new RelDivulgaInfoWindow(cliente, false).popup();
		}
		
		if (LavenderePdaConfig.usaTipoPedidoComoOpcaoMenuCliente || LavenderePdaConfig.isUsaAgendaDeVisitas() || LavenderePdaConfig.isPermiteAtualizarManualmenteDadosCadastraisCliente() || LavenderePdaConfig.usaRegistroChegadaSaidaClienteVisita) {
			remontaMenuFuncionalidades();
		}
		if (LavenderePdaConfig.controlaVencimentoAlvaraCliente > 0 && cliente != null && ClienteService.getInstance().isAvisaVencimentoAlvara(cliente, LavenderePdaConfig.controlaVencimentoAlvaraCliente)) {
			if (cliente.isAlvaraVigente()) {
				if (DateUtil.getCurrentDate().equals(cliente.dtVencimentoAlvara)) {
					UiUtil.showWarnMessage(Messages.CLIENTE_MSG_ALVARA_VENCER_HOJE);
				} else {
					UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.CLIENTE_MSG_ALVARA_VENCER, DateUtil.getDaysBetween(cliente.dtVencimentoAlvara, DateUtil.getCurrentDate())));
				}
			} else {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.CLIENTE_MSG_ALVARA_VENCIDO, DateUtil.getDaysBetween(DateUtil.getCurrentDate(), cliente.dtVencimentoAlvara)));
			}
		}
		//--
		if (LavenderePdaConfig.controlaVencimentoAfeCliente > 0 && cliente != null && ClienteService.getInstance().isAvisaVencimentoAfe(cliente, LavenderePdaConfig.controlaVencimentoAfeCliente)) {
			if (cliente.isAfeVigente()) {
				if (DateUtil.getCurrentDate().equals(cliente.dtVencimentoAfe)) {
					UiUtil.showWarnMessage(Messages.CLIENTE_MSG_AFE_VENCER_HOJE);
				} else {
					UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.CLIENTE_MSG_AFE_VENCER, DateUtil.getDaysBetween(cliente.dtVencimentoAfe, DateUtil.getCurrentDate())));
				}
			} else {
				UiUtil.showWarnMessage(MessageUtil.getMessage(Messages.CLIENTE_MSG_AFE_VENCIDO, DateUtil.getDaysBetween(DateUtil.getCurrentDate(), cliente.dtVencimentoAfe)));
			}
		}
		if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoCliente()) {
			if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentes()) {
				if (LavenderePdaConfig.isUsaAvisoClienteSemPesquisaMercadoTipoValor()) {
					int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
					if (result < 1) {
						String msg = "";
						if (result == -1) {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()});
						} else {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_VALOR_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
						}
						if (UiUtil.showConfirmYesNoMessage(msg)) {
							btPesquisaMercadoClick();
							return;
						}
					}
				}
				if (LavenderePdaConfig.isUsaPesquisaMercadoProdutosConcorrentesTipoGondola()) {
					int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISAGONDOLA);
					if (result < 1) {
						String msg = "";
						if (result == -1) {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_GONDOLA_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()});
						} else {
							msg = MessageUtil.getMessage(Messages.PESQUISA_MERCADO_TIPO_GONDOLA_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
						}
						if (UiUtil.showConfirmYesNoMessage(msg)) {
							btPesquisaMercadoClick();
						}
					}
				}
			} else {
				int result = ClienteService.getInstance().getAvisaClienteSemPesquisaMercado(Cliente.NMCOLUNA_NUDIASSEMPESQUISA);
				if (result < 1) {
					String msg = result == -1 ? MessageUtil.getMessage(Messages.PESQUISA_MERCADO_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString()}) : MessageUtil.getMessage(Messages.PESQUISA_MERCADO_NUDIAS_SEM_PESQUISA, new Object[]{SessionLavenderePda.getCliente().toString(), StringUtil.getStringValue(LavenderePdaConfig.getNuDiasAvisoClienteSemPesquisaMercado())});
					if (UiUtil.showConfirmYesNoMessage(msg)) {
						btPesquisaMercadoClick();
					}
				}
			}
		}
    }

	@Override
	public void onFormClose() throws SQLException {
		if (LavenderePdaConfig.sugereRegistroSaidaNoMenuCliente()) {
			sugereRegistroSaida();
		}
		if (getBaseCrudListForm() instanceof ListClienteForm) {
			if (SessionLavenderePda.isUsuarioSupervisor()) {
				ListClienteForm listClienteForm = (ListClienteForm)getBaseCrudListForm();
				if (listClienteForm.isAllSelected) {
					SessionLavenderePda.setRepresentante(null);
				}
			}
		}
	}

	public void setAgendaVisita(AgendaVisita agendaVisita) {
		this.agendaVisita = agendaVisita;
	}
	
	public void setRowkeyDaProximaSequencia(String rowkey) {
		rowKeyDaProximaSequencia = rowkey;
	}
	
	private static boolean validaVisitaForaAgenda(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.usaToleranciaVisitasForaDeAgendaPorClienteVisitado) {
			if (!AgendaVisitaService.getInstance().isHasAgendaVisitaNoDia(cliente.cdCliente) && !VisitaService.getInstance().verificaToleranciaVisitaForaAgenda(cliente)) {
				if (!isLiberacaoSenhaToleranciaVisitaForaAgendaUltrapassada(cliente)) {
					return false;
				} 
			}
		} else if (LavenderePdaConfig.isToleranciaVisitasForaAgenda()) {
			if (!VisitaService.getInstance().isVisitasForaAgendaDoDiaDentroTolerancia(cliente)) {
				if (!isLiberacaoSenhaToleranciaVisitaForaAgendaUltrapassada(cliente)) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean isLiberacaoSenhaToleranciaVisitaForaAgendaUltrapassada(Cliente cliente) throws SQLException {
		if (LavenderePdaConfig.liberaSenhaVisitaClienteForaAgenda) {
			if (SessionLavenderePda.visitaAndamento != null && ValueUtil.valueEquals(SessionLavenderePda.visitaAndamento.cdCliente, cliente.cdCliente) && ValueUtil.getBooleanValue(SessionLavenderePda.visitaAndamento.flLiberadoSenha)) {
				SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada = ValueUtil.VALOR_SIM;
				return true;
			}
			if (ValueUtil.getBooleanValue(SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada) || (LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica && ConfigInternoService.getInstance().isVisitaForaAgendaLiberadoSenha(cliente.cdCliente))) {
				return true;
			}
			if (UiUtil.showMessage(MessageUtil.getMessage(Messages.VISITA_MSG_TOLERANCIA_FORA_AGENDA_ULTRAPASSADA, StringUtil.getStringValueToInterface((int) VisitaService.getInstance().getNuMaxVisitasForaAgendaPermitida(cliente))), TYPE_MESSAGE.ERROR, new String[] {FrameworkMessages.BOTAO_FECHAR, Messages.BOTAO_LIBERAR_SENHA}) == 1) {
				AdmSenhaDinamicaWindow senhaForm = new AdmSenhaDinamicaWindow();
				senhaForm.setMensagem(Messages.VISITA_MSG_SENHA_LIBERACAO);
				senhaForm.setCdCliente(cliente.cdCliente);
				senhaForm.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_VISITA_FORA_AGENDA);
				if (senhaForm.show() != AdmSenhaDinamicaWindow.SENHA_VALIDA) {
					return false;
				}
				if (!LavenderePdaConfig.usaMultiplasLiberacoesParaClienteComSenhaUnica) {
					UiUtil.showConfirmMessage(Messages.VISITA_MSG_TOLERANCIA_LIBERADA);
				}
				SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada = ValueUtil.VALOR_SIM;
				return true;
			} else {
				SessionLavenderePda.getCliente().flVisitaForaDaAgendaLiberada = null;
				return false;
			}
		} else {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.VISITA_MSG_TOLERANCIA_FORA_AGENDA_ULTRAPASSADA, StringUtil.getStringValueToInterface((int) VisitaService.getInstance().getNuMaxVisitasForaAgendaPermitida(cliente))));
			return false;
		}
	}
	
	private boolean bloqueiaNovoPedidoClienteSemContatos() throws SQLException {
		if (LavenderePdaConfig.isObrigaInformarContatoERPClienteNoPedido()) {
			ContatoErp contatoErp = new ContatoErp();
			contatoErp.cdEmpresa = SessionLavenderePda.cdEmpresa;
			contatoErp.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
			contatoErp.cdCliente = getCliente().cdCliente;
			if (ContatoErpService.getInstance().countByExample(contatoErp) <= 0) {
				UiUtil.showErrorMessage(Messages.CONTATO_MSG_CLIENTE_NAO_POSSUI_CONTATOS);
				return true;
			}
		}
		return false;
	}
	
	private boolean cancelaPedidoPorNaoSelecionarNotaCredito(CadPedidoForm cadPedidoForm, Cliente cliente) throws SQLException {
		if (NotaCreditoService.getInstance().existeNotasCreditoParaCliente(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCliente)) {
			ListNotaCreditoWindow listNotaCreditoWindow = new ListNotaCreditoWindow(cliente.cdEmpresa, cliente.cdRepresentante, cliente.cdCliente);
			listNotaCreditoWindow.popup();
			if (listNotaCreditoWindow.isCancelaPedido()) return true;
			
			cadPedidoForm.setNotaCreditoPedido(listNotaCreditoWindow.getNotaCreditoSelecionadaList());
		}
		return false;
	}
	
	@Override
	protected void voltarClick() throws SQLException {
		super.voltarClick();
		SessionLavenderePda.setAgenda(null);
	}
	
	private boolean validaNuDiasObrigaFechamentoVendas() throws SQLException {
		SaldoVendaRep saldoVendaFilter = new SaldoVendaRep();
		saldoVendaFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
		saldoVendaFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
		saldoVendaFilter.dtUltimoSaldo = DateUtil.getDateValue(DateUtil.toDate(SaldoVendaRepService.getInstance().maxByExample(saldoVendaFilter, "DTULTIMOSALDO")));
		SaldoVendaRep saldoVendaRep = (SaldoVendaRep) SaldoVendaRepService.getInstance().findByRowKey(saldoVendaFilter.getRowKey());
		if (saldoVendaRep != null) {
			if (LavenderePdaConfig.nuDiasObrigaFechamentoVendas < DateUtil.getDaysBetween(DateUtil.getCurrentDate(), saldoVendaFilter.dtUltimoSaldo)) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.FECHAMENTO_VENDAS_DIA_LIMITE_BLOQUEIO, LavenderePdaConfig.nuDiasObrigaFechamentoVendas));
				return false;
			}
		} 
		return true;
	}

	private void btPesquisaClienteClick() throws SQLException {
		show(new ListPesquisaClienteForm(false, getCliente()));
	}

	@Override
	public void onFormExibition() throws SQLException {
		super.onFormExibition();
		remontaMenuFuncionalidades();
	}
	
	private void aposCriacaoPedidoAPartirDeSugestaoOuGiroProduto(CadPedidoForm cadPedidoForm, Pedido pedido) throws SQLException {
		cadPedidoForm.edit(pedido);
		show(cadPedidoForm);
		//--
		if (pedido.hasErrosInsertSugestaoPedido()) {
			RelItemPedidoDivergenciaWindow relProdutoErroWindow = new RelItemPedidoDivergenciaWindow(Messages.PEDIDO_ITENS_NAO_INSERIDOS, pedido);
			relProdutoErroWindow.popup();
		}
		if (ValueUtil.isNotEmpty(pedido.itemPedidoEstoquePrevistoExcepList)) {
			RelItemPedidoEstoquePrevistoExcep relItemPedidoEstoquePrevistoExcep = new RelItemPedidoEstoquePrevistoExcep(pedido.itemPedidoEstoquePrevistoExcepList);
			relItemPedidoEstoquePrevistoExcep.popup();
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			Vector itemPedidoPendenteList = pedido.getOnlyItemPedidoPendenteList();
			if (ValueUtil.isNotEmpty(itemPedidoPendenteList)) {
				new RelItensPedidoPendenteWindow(itemPedidoPendenteList).popup();
			}
		}
	}
}
