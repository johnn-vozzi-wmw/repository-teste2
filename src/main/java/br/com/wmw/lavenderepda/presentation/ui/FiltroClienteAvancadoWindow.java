package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwWindow;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditNumberMask;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Rede;
import br.com.wmw.lavenderepda.presentation.ui.combo.BairroClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CategoriaComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.CidadeClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.DescProgressivoConfigComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.FornecedorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.RepresentanteSupervComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.StatusClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoCadastroClienteComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoClienteRedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.UnidadeFederalComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;

public class FiltroClienteAvancadoWindow extends WmwWindow {
	
	private UnidadeFederalComboBox cbUf;
	private CidadeClienteComboBox cbCidadeClienteComboBox;
	private BairroClienteComboBox cbBairroClienteComboBox;
	private EditNumberMask edCepFilterInicial;
	private EditNumberMask edCepFilterFinal;
	private Cliente clienteFilter;
	public ButtonPopup btFiltrar;
	public ButtonPopup btLimpar;
	public boolean filtroAplicado = false;
	
	private RepresentanteSupervComboBox cbRepresentante;
	private StatusClienteComboBox cbStatusCliente;
	private TipoCadastroClienteComboBox cbTipoCadastroClienteComboBox;
	private TipoClienteRedeComboBox cbTipoClienteRedeComboBox;
	private RedeComboBox cbRede;
	private CategoriaComboBox cbCategoria;
	private MarcadorComboBox cbMarcador;
	private DescProgressivoConfigComboBox cbDescProgressivoConfig;
	private FornecedorComboBox cbFornecedor;
	public CheckBoolean ckClientesAtrasados;
	public CheckBoolean ckDestacaClienteNaoAtendidoMes;
	public boolean onSelectClienteNovaAgendaVisita;
	public boolean onSelectClienteNovoPedido;
	

	public FiltroClienteAvancadoWindow(Cliente clienteFilter, final boolean onSelectClienteNovaAgendaVisita, final boolean onSelectClienteNovoPedido) throws SQLException {
		super(Messages.PRODUTO_FILTRO_AVANCADO);
		this.clienteFilter = clienteFilter;
		this.onSelectClienteNovaAgendaVisita = onSelectClienteNovaAgendaVisita;
		this.onSelectClienteNovoPedido = onSelectClienteNovoPedido;
		
		if (onSelectClienteNovaAgendaVisita) {
			cbUf = new UnidadeFederalComboBox();
			cbUf.defaultItemType = BaseComboBox.DefaultItemType_ALL;
			cbUf.popupTitle = Messages.CLIENTE_LABEL_DSESTADOCOMERCIAL;
			cbUf.load();
			cbUf.setSelectedIndex(0);
			cbCidadeClienteComboBox = new CidadeClienteComboBox();
			cbBairroClienteComboBox = new BairroClienteComboBox();
			edCepFilterInicial = new EditNumberMask("#####-###");
			edCepFilterFinal = new EditNumberMask("#####-###");
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
				cbRede = new RedeComboBox();
				cbRede.setSelectedIndex(0);
				Rede rede = clienteFilter.getRede();
				if (rede != null) {
					cbRede.setSelectedItem(rede);
				}
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isExibeFiltroAgendaVisitaNaTelaPrincipal(ListAgendaVisitaForm.FILTRO_CARACTERISTICA_CLIENTE)) {
				cbCategoria = new CategoriaComboBox();
				cbCategoria.setSelectedIndex(0);
			}
		} else {
			if (!LavenderePdaConfig.isStatusClienteFixoTelaListaCliente()) {
				cbStatusCliente = new StatusClienteComboBox();
			}
			if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isRepresentanteFixoTelaListaCliente()) {
				cbRepresentante = new RepresentanteSupervComboBox(BaseComboBox.DefaultItemType_NONE);
			}
			if (LavenderePdaConfig.usaClienteEmModoProspeccao && !onSelectClienteNovoPedido && !LavenderePdaConfig.isTipoCadastroFixoTelaListaCliente()) {
				cbTipoCadastroClienteComboBox = new TipoCadastroClienteComboBox();
			}
			if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isTipoClienteFixoTelaListaCliente()) {
				cbTipoClienteRedeComboBox = new TipoClienteRedeComboBox();
			}
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				cbRede = new RedeComboBox();
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				cbCategoria = new CategoriaComboBox();
			}
			if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && !LavenderePdaConfig.isMarcadoresFixoTelaListaCliente()) {
				cbMarcador = new MarcadorComboBox(Marcador.ENTIDADE_MARCADOR_CLIENTE);
			}
			if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isFornecedorFixoTelaListaCliente()) {
				cbFornecedor = new FornecedorComboBox();
			}
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado && !LavenderePdaConfig.isDescProgressivoPersonalizadoFixoTelaListaCliente()) {
				cbDescProgressivoConfig = new DescProgressivoConfigComboBox(BaseComboBox.DefaultItemType_ALL);
				cbDescProgressivoConfig.loadAll();
			}
			if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente()) {
				ckClientesAtrasados = new CheckBoolean(MessageUtil.getMessage(Messages.MSG_CLIENTE_SEM_PEDIDOS, LavenderePdaConfig.nuDiasFiltroClienteSemPedido));
			}
			if (LavenderePdaConfig.destacaClienteAtendidoMes && !LavenderePdaConfig.isClienteAtendidoTelaListaCliente()) {
				ckDestacaClienteNaoAtendidoMes = new CheckBoolean(Messages.MSG_CLIENTE_NAO_ATENDIDO_MES);
			}
		}
		
		btFiltrar = new ButtonPopup(Messages.BOTAO_FILTRAR);
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		
		boolean verifyCbRede = ValueUtil.isNotEmpty(clienteFilter.cdRede);
		loadDefaultFilters(verifyCbRede);
		carregaFiltrosAplicados();
		setDefaultRect();
	}
	
	private void loadDefaultFilters(final boolean clearFields) throws SQLException {
		if (onSelectClienteNovaAgendaVisita) return;
		if (!LavenderePdaConfig.isStatusClienteFixoTelaListaCliente()) {
			cbStatusCliente.setSelectedIndex(0);
		}
		if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isRepresentanteFixoTelaListaCliente()) {
			cbRepresentante.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaClienteEmModoProspeccao && !onSelectClienteNovoPedido && !LavenderePdaConfig.isTipoCadastroFixoTelaListaCliente()) {
			cbTipoCadastroClienteComboBox.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede && !onSelectClienteNovoPedido && !LavenderePdaConfig.isTipoClienteFixoTelaListaCliente()) {
			cbTipoClienteRedeComboBox.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isTipoClienteFixoTelaListaCliente()) {
			cbTipoClienteRedeComboBox.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
			cbRede.setSelectedIndex(0);
			if (clienteFilter != null && !clearFields) {
				Rede rede = clienteFilter.getRede();
				if (rede != null) {
					cbRede.setSelectedItem(rede);
				}
			}
		}
		if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
			cbCategoria.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && !LavenderePdaConfig.isMarcadoresFixoTelaListaCliente()) {
			cbMarcador.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isFornecedorFixoTelaListaCliente()) {
			cbFornecedor.setSelectedIndex(0);
		}
		if (ValueUtil.isNotEmpty(SessionLavenderePda.getRepresentante().cdRepresentante) && SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isRepresentanteFixoTelaListaCliente()) {
			cbRepresentante.setSelectedIndex(0);
		}	
		if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente()) {
			ckClientesAtrasados.setChecked(false);
		}
		if (LavenderePdaConfig.destacaClienteAtendidoMes && !LavenderePdaConfig.isClienteAtendidoTelaListaCliente()) {
			ckDestacaClienteNaoAtendidoMes.setChecked(false);
		}
		if (LavenderePdaConfig.usaDescProgressivoPersonalizado && !LavenderePdaConfig.isDescProgressivoPersonalizadoFixoTelaListaCliente()) {
			cbDescProgressivoConfig.setSelectedIndex(0);
		}
	}

	//@Override
	public void initUI() {
		super.initUI();
		if (onSelectClienteNovaAgendaVisita) {
			UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_DSESTADOCOMERCIAL), cbUf, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_DSCIDADECOMERCIAL), cbCidadeClienteComboBox, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_DSBAIRROCOMERCIAL), cbBairroClienteComboBox, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.CLIENTE_FAIXA_CEP_INICIO), edCepFilterInicial, getLeft(), getNextY());
			UiUtil.add(this, new LabelName(Messages.CLIENTE_FAIXA_CEP_FIM), edCepFilterFinal, getLeft(), getNextY());
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRede, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.CATEGORIA_NOME_ENTIDADE), cbCategoria, getLeft(), getNextY());
			}
		} else {
			if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isRepresentanteFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.REPRESENTANTE_NOME_ENTIDADE), cbRepresentante, getLeft(), getNextY());
			}
			if (!LavenderePdaConfig.isStatusClienteFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_STATUS), cbStatusCliente, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.usaClienteEmModoProspeccao && !onSelectClienteNovoPedido && !LavenderePdaConfig.isTipoCadastroFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.CLIENTE_LABEL_TIPOCADASTRO), cbTipoCadastroClienteComboBox, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isTipoClienteFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.TIPO_CLIENTE_REDE_TITULO), cbTipoClienteRedeComboBox, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.REDE_NOME_ENTIDADE), cbRede, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.CATEGORIA_NOME_ENTIDADE), cbCategoria, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && !LavenderePdaConfig.isMarcadoresFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.MARCADOR_NOME_ENTIDADE), cbMarcador, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isFornecedorFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(MessageUtil.getMessage(Messages.CLIENTE_NUDIAS_FORNECEDOR_SEM_PEDIDO, LavenderePdaConfig.nuDiasClienteSemPedidoFornecedor)), cbFornecedor, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado && !LavenderePdaConfig.isDescProgressivoPersonalizadoFixoTelaListaCliente()) {
				UiUtil.add(this, new LabelName(Messages.DESC_PROG_CONFIG_NM_ENTIDADE), cbDescProgressivoConfig, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente()) {
				UiUtil.add(this, ckClientesAtrasados, getLeft(), getNextY());
			}
			if (LavenderePdaConfig.destacaClienteAtendidoMes && !LavenderePdaConfig.isClienteAtendidoTelaListaCliente()) {
				UiUtil.add(this, ckDestacaClienteNaoAtendidoMes, getLeft(), getNextY());
			}
		}
		addButtonPopup(btFiltrar);
		addButtonPopup(btLimpar);
		addButtonPopup(btFechar);
	}
	
	
	//@Override
	public void onWindowEvent(Event event) throws java.sql.SQLException {
		super.onWindowEvent(event);
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbUf) {
					loadCidadeCliente();
				} else if (event.target == cbCidadeClienteComboBox) {
					loadBairroCliente();
				} else if (event.target == btFiltrar) {
					aplicaFiltrosNoDomainFilter();
					unpop();
				} else if (event.target == btLimpar) {
					limpaFiltrosClick();
					aplicaFiltrosNoDomainFilter();
				} else if (event.target == cbRepresentante) {
					if (LavenderePdaConfig.usaStatusClienteVinculado) {
						cbStatusCliente.reloadByRepresentanteChange(cbRepresentante.getValue());
						cbStatusCliente.setSelectedIndex(0);
					}
				}
				break;
			}
		}
	}
	
	private void carregaFiltrosAplicados() throws SQLException {
		if (onSelectClienteNovaAgendaVisita) {
			cbUf.setValue(clienteFilter.cdEstadoComercial);
			loadCidadeCliente();
			cbCidadeClienteComboBox.setValue(clienteFilter.dsCidadeComercialFilter);
			loadBairroCliente();
			cbBairroClienteComboBox.setValue(clienteFilter.dsBairroComercialFilter);
			if (ValueUtil.isNotEmpty(clienteFilter.cepInicialFilter)) {
				edCepFilterInicial.setValue(clienteFilter.cepInicialFilter);
			}
			if (ValueUtil.isNotEmpty(clienteFilter.cepFinalFilter)) {
				edCepFilterFinal.setValue(clienteFilter.cepFinalFilter);
			}
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.cdRede)) {
				cbRede.setValue(clienteFilter.cdRede);
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.cdCategoria)) {
				cbCategoria.setValue(clienteFilter.cdCategoria);
			}
		} else {
			if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isRepresentanteFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.cdRepresentante)) {
				cbRepresentante.setValue(clienteFilter.cdRepresentante);
			}
			if (!LavenderePdaConfig.isStatusClienteFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.flStatusClienteFilter)) {
				cbStatusCliente.setValue(clienteFilter.flStatusClienteFilter);
			}
			if (LavenderePdaConfig.usaClienteEmModoProspeccao && !onSelectClienteNovoPedido &&!LavenderePdaConfig.isTipoCadastroFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.flTipoCadastro)) {
				cbTipoCadastroClienteComboBox.setValue(clienteFilter.flTipoCadastro);
			}
			if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isTipoClienteFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.flTipoClienteRede)) {
				cbTipoClienteRedeComboBox.setValue(clienteFilter.flTipoClienteRede);
			}
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.cdRede)) {
				cbRede.setValue(clienteFilter.cdRede);
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.cdCategoria)) {
				cbCategoria.setValue(clienteFilter.cdCategoria);
			}
			if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && !LavenderePdaConfig.isMarcadoresFixoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.cdMarcador)) {
				cbMarcador.setValue(clienteFilter.cdMarcador);
			}
			if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isFornecedorFixoTelaListaCliente()) {
				cbFornecedor.setValue(clienteFilter.cdFornecedor);
			}
			if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente() && clienteFilter.nuDiasSemPedido > 0) {
				ckClientesAtrasados.setChecked(true);
			}
			if (LavenderePdaConfig.destacaClienteAtendidoMes && !LavenderePdaConfig.isClienteAtendidoTelaListaCliente() && ValueUtil.isNotEmpty(clienteFilter.flAtendido)) {
				ckDestacaClienteNaoAtendidoMes.setChecked(true);
			}
		}
	}

	private void loadCidadeCliente() throws SQLException {
		cbCidadeClienteComboBox.load(cbUf.getValue());
		cbCidadeClienteComboBox.setSelectedIndex(0);
	}
	
	private void loadBairroCliente() throws SQLException {
		cbBairroClienteComboBox.load(cbCidadeClienteComboBox.getValue(), cbUf.getValue());
		cbBairroClienteComboBox.setSelectedIndex(0);
	}
	
	private void aplicaFiltrosNoDomainFilter() throws SQLException {
		if (onSelectClienteNovaAgendaVisita) {
			clienteFilter.cdEstadoComercialFilter = cbUf.getValue();
			clienteFilter.dsCidadeComercialFilter = cbCidadeClienteComboBox.getValue();
			clienteFilter.dsBairroComercialFilter = cbBairroClienteComboBox.getValue();
			clienteFilter.cepInicialFilter = edCepFilterInicial.getValue();
			clienteFilter.cepFinalFilter = edCepFilterFinal.getValue();
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				clienteFilter.cdRede = cbRede.getValue();
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				clienteFilter.cdCategoria = cbCategoria.getValue();
			}
		} else {
			if (SessionLavenderePda.isUsuarioSupervisor() && !LavenderePdaConfig.isRepresentanteFixoTelaListaCliente()) {
				clienteFilter.cdRepresentante = (ValueUtil.isNotEmpty(cbRepresentante.getValue())) ? cbRepresentante.getValue() : ValueUtil.VALOR_NI;
				cbRepresentante.configureSession();
			}
			if (!LavenderePdaConfig.isStatusClienteFixoTelaListaCliente()) {
				clienteFilter.flStatusClienteFilter = cbStatusCliente.getValue();
			}
			if (LavenderePdaConfig.usaClienteEmModoProspeccao && !LavenderePdaConfig.isTipoCadastroFixoTelaListaCliente()) {
				if (onSelectClienteNovoPedido) {
					clienteFilter.flTipoCadastro = Cliente.TIPO_CLIENTE;
				} else if (ValueUtil.isEmpty(cbTipoCadastroClienteComboBox.getValue())) {
					clienteFilter.flTipoCadastro = "";
				} else {
					clienteFilter.flTipoCadastro = cbTipoCadastroClienteComboBox.getValue();
				}
			}
			if (LavenderePdaConfig.usaFiltroTipoClienteRede && !LavenderePdaConfig.isTipoClienteFixoTelaListaCliente()) {
				if (!cbTipoClienteRedeComboBox.isOpcaoTodosSelecionado()) {
					clienteFilter.flTipoClienteRede = cbTipoClienteRedeComboBox.isOpcaoRedeSelecionado() ? Cliente.TIPO_REDE : Cliente.TIPO_INDIVIDUAL;
				} else {
					clienteFilter.flTipoClienteRede = "";
				}
			}
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				clienteFilter.cdRede = cbRede.getValue();
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				clienteFilter.cdCategoria = cbCategoria.getValue();
			}
			if (LavenderePdaConfig.apresentaIndicadoresCompraHistoricoClienteListaClientes && !LavenderePdaConfig.isMarcadoresFixoTelaListaCliente()) {
				clienteFilter.cdMarcador = cbMarcador.getValue();
			}
			if (LavenderePdaConfig.usaClienteSemPedidoFornecedor && !LavenderePdaConfig.isFornecedorFixoTelaListaCliente()) {
				clienteFilter.cdFornecedor = cbFornecedor.getValue();
			}
			if (LavenderePdaConfig.nuDiasFiltroClienteSemPedido > 0 && !LavenderePdaConfig.isClienteSemPedidoFixoTelaListaCliente()) {
				clienteFilter.nuDiasSemPedido = (ckClientesAtrasados.isChecked()) ? LavenderePdaConfig.nuDiasFiltroClienteSemPedido : 0;
			}
			if (LavenderePdaConfig.destacaClienteAtendidoMes && !LavenderePdaConfig.isClienteAtendidoTelaListaCliente()) {
				clienteFilter.flAtendido = (ckDestacaClienteNaoAtendidoMes.isChecked()) ? ValueUtil.VALOR_NAO : null;
			}
			if(LavenderePdaConfig.usaDescProgressivoPersonalizado && !LavenderePdaConfig.isDescProgressivoPersonalizadoFixoTelaListaCliente()) {
				clienteFilter.descProgressivoConfigFilter = cbDescProgressivoConfig.getValue();
			}
		}
		filtroAplicado = true;
	}
	
	private void limpaFiltrosClick() throws SQLException {
		if (onSelectClienteNovaAgendaVisita) {
			cbUf.setSelectedIndex(0);
			loadCidadeCliente();
			loadBairroCliente();
			edCepFilterInicial.setValue("");
			edCepFilterFinal.setValue("");
			if (LavenderePdaConfig.isUsaRedeCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				cbRede.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.isUsaCategoriaCaracteristicasClienteNoSistema() && !LavenderePdaConfig.isCaracteristicaClienteFixoTelaListaCliente()) {
				cbCategoria.setSelectedIndex(0);
			}
			if (LavenderePdaConfig.usaDescProgressivoPersonalizado && !LavenderePdaConfig.isDescProgressivoPersonalizadoFixoTelaListaCliente()) {
				cbDescProgressivoConfig.setSelectedIndex(0);
			}
			clienteFilter.cdEstadoComercial = null;
			clienteFilter.dsCidadeComercialFilter = null;
			clienteFilter.dsBairroComercialFilter = null;
			clienteFilter.cepInicialFilter = null;
			clienteFilter.cepFinalFilter = null;
		} else {
			loadDefaultFilters(true);
		}
	}
	
	
}
