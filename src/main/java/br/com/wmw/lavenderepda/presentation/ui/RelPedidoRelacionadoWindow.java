package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.BaseListContainer;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.GridListContainer;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.OrigemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.SenhaDinamica;
import br.com.wmw.lavenderepda.business.domain.StatusPedidoPda;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.StatusPedidoPdaService;
import br.com.wmw.lavenderepda.presentation.ui.combo.RedeComboBox;
import br.com.wmw.lavenderepda.presentation.ui.ext.LavendereWmwListWindow;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.util.Date;
import totalcross.util.Vector;

public class RelPedidoRelacionadoWindow extends LavendereWmwListWindow {
	
	public BaseComboBox cbDtEmissaoEntrega;
	private LabelValue lbDescricao;
	public ButtonPopup btSalvar;
	public Pedido pedidoVendaRelacionado;
	public Vector pedidosRelacionados;
	private boolean fromBtVendaRelacionada;
	Vector pedidoBonificacaoList;
	public ButtonPopup btLimpar;
	public ButtonPopup btSenha;
	private EditFiltro edFiltro;
	protected BaseButton btFiltroAvancado;
	private Date dtInicialFilter;
	private Date dtFinalFilter;
	private String dsClienteFilter;
	private RedeComboBox cbRede;
	private String cdClienteFilter;
	
	private Pedido pedido;
	private String dsTipoDataFiltro;
	
	public RelPedidoRelacionadoWindow(Pedido pedido, Vector pedidoBonificacaoList) throws SQLException {
		super(pedidoBonificacaoList != null ? Messages.PEDIDO_RELACIONADO_BONIFICACAO_LIST : Messages.PEDIDO_RELACIONADO_TITULO);
		lbDescricao = new LabelValue(Messages.PEDIDO_RELACIONADO_SELECIONAR_PEDIDO);
		edFiltro = new EditFiltro("9999999999", 25);
		btFiltroAvancado = new BaseButton("", UiUtil.getColorfulImage("images/maisfiltros.png", UiUtil.getLabelPreferredHeight(), UiUtil.getLabelPreferredHeight()));
		this.pedidoBonificacaoList = pedidoBonificacaoList;
		this.fromBtVendaRelacionada = pedidoBonificacaoList != null ? true : false;
		this.pedido = pedido;
		makeUnmovable();
		singleClickOn = true;
		constructorListContainer();
		if (LavenderePdaConfig.isUsaVariosPedidosBonificados() || (pedido.isPedidoVendaProducao() && LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao())) {
			listContainer.setCheckable(true);
			pedidosRelacionados = new Vector();
			btSalvar = new ButtonPopup(FrameworkMessages.BOTAO_SALVAR);
		}
		btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
		if (LavenderePdaConfig.getSementeSenhaPedidoProducao() > 0) {
			btSenha = new ButtonPopup(FrameworkMessages.BOTAO_SENHA);
		}
        cbRede = new RedeComboBox();
		setDefaultRect();
	}
	
	public RelPedidoRelacionadoWindow(Pedido pedido) throws SQLException {
		this(pedido, null);
	}

	private void constructorListContainer() throws SQLException {
		configListContainer("NUPEDIDO");
		listContainer = new GridListContainer(8, 3);
		listContainer.setColPosition(5, AFTER);
		listContainer.setColPosition(2, RIGHT);
		listContainer.setColPosition(7, RIGHT);
    	listContainer.setUseSortMenu(true);
    	listContainer.setColsSort(getColsSort());
		listContainer.atributteSortSelected = sortAtributte;
		listContainer.sortAsc = sortAsc;
    }
	
	private String[][] getColsSort() throws SQLException {
		boolean tipoPedidoProducao = isTipoPedidoVendaProducao();
		int lenght = tipoPedidoProducao ? 3 : 2;
		String[][] colsSort = new String[lenght][2];
		colsSort[0] = new String[] {Messages.CODIGO, "NUPEDIDO"};
		colsSort[1] = new String[] {Messages.PEDIDO_LABEL_EMISSAO, "DTEMISSAO, HREMISSAO, HRFIMEMISSAO"};
		if (tipoPedidoProducao) {
			colsSort[2] = new String[] {Messages.CLIENTE_LABEL_NMRAZAOSOCIAL, "CLI.NMRAZAOSOCIAL"};
		}
		return colsSort;
	}
	
	
	protected Vector getDomainList(BaseDomain domain) throws java.sql.SQLException {
		if (fromBtVendaRelacionada) {
			return pedidoBonificacaoList;
		}
		Vector listIdStatusRelacionaPedidoPda = StatusPedidoPdaService.getInstance().listIdStatusRelacionaPedidoPda();
		Vector newListPedido = new Vector();
		Vector listTodosPedidos = new Vector();
		Pedido pedidoFilter = (Pedido)domain;
    	pedidoFilter.cdEmpresa = SessionLavenderePda.cdEmpresa;
       	pedidoFilter.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
       	if (LavenderePdaConfig.isUsaPercQuantidadeDosItensPedidoOriginalBonificacaoTroca() && ValueUtil.isNotEmpty(SessionLavenderePda.getCliente().cdCategoria)) {
       		listTodosPedidos = PedidoService.getInstance().findAllByExample(pedidoFilter);
       		Cliente cliente = SessionLavenderePda.getCliente();
       		pedidoFilter.minSumQtItemFisico = ValueUtil.round(cliente.getCategoria().qtMinVendidoBonifTroca);
       		pedidoFilter.filterByMinSumQtItemFisico = true;
       	}
       	if (isTipoPedidoVendaProducao()) {
       		return PedidoService.getInstance().findPedidoRelacionadoList(pedidoFilter);
       	}
   		pedidoFilter.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	Vector listPedido = PedidoService.getInstance().findAllByExample(pedidoFilter);
    	if (listTodosPedidos.isEmpty()) {
    		listTodosPedidos = listPedido;
    	}
    	Pedido pedidoRef;
    	TipoPedido tipoPedidoRef;
    	for (int i = 0; i < listPedido.size(); i++) {
			pedidoRef = (Pedido) listPedido.items[i];
			tipoPedidoRef = pedidoRef.getTipoPedido(); 
			if (tipoPedidoRef == null || ValueUtil.getBooleanValue(tipoPedidoRef.flNaoRelacionaPedNaTrocaBonif)) {
				continue;
			}
		    if ((isUsaPedidoTransmitido(pedidoRef) || pedidoRef.isPedidoFechado()) && pedidoRef.isPedidoVenda() && OrigemPedido.FLORIGEMPEDIDO_PDA.equals(pedidoRef.flOrigemPedido)) {
		    	if (this.pedido.getTipoPedido().isComplementar() && pedidoRef.isStatusPedidoComplementavel()) {
					if (ValueUtil.isNotEmpty(pedidoRef.dtEntrega) && (this.pedido.dtEmissao.subtract(pedidoRef.dtEntrega) >= LavenderePdaConfig.usaPedidoComplementar || LavenderePdaConfig.previsaoEntregaOcultaNoPedido)) {
						newListPedido.addElement(pedidoRef);
						continue;
					}
		    	}
		    	if (LavenderePdaConfig.isObrigaRelacionarPedidoBonificacao() && (listIdStatusRelacionaPedidoPda.isEmpty() || listIdStatusRelacionaPedidoPda.contains(pedidoRef.cdStatusPedido))){
		    		if (LavenderePdaConfig.usaValorMaximoBonificaoPorCreditoPedidoVenda) {
	    				if (isPedidoRelacionadosValidoPorTipoCredito(pedidoRef)) {
	    					newListPedido.addElement(pedidoRef);
	    				}
		    		} else if (PedidoService.getInstance().adicionaPedidoListaPedidosRelacionados(pedidoRef, listTodosPedidos)) {
		    			newListPedido.addElement(pedidoRef);
		    		} 
		    	}
			}
		}
    	if (this.pedido.getTipoPedido().isComplementar()) {
    		for (int i = 0; i < listPedido.size(); i++) {
    			pedidoRef = (Pedido) listPedido.items[i];
    			tipoPedidoRef = pedidoRef.getTipoPedido(); 
				if (tipoPedidoRef == null || ValueUtil.getBooleanValue(tipoPedidoRef.flNaoRelacionaPedNaTrocaBonif)) {
					continue;
				}
    			if (pedidoRef.isPedidoVenda() && OrigemPedido.FLORIGEMPEDIDO_ERP.equals(pedidoRef.flOrigemPedido) && ValueUtil.isNotEmpty(pedidoRef.dtEntrega) && (this.pedido.dtEmissao.subtract(pedidoRef.dtEntrega) >= LavenderePdaConfig.usaPedidoComplementar) && pedidoRef.isStatusPedidoComplementavel()) {
					if (LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes() && !PedidoService.getInstance().permitidoRelacionamento(pedidoRef, listTodosPedidos)) continue;
					newListPedido.addElement(pedidoRef);
    			}
    		}
		}
    	if (this.pedido.getTipoPedido().isFlRelacaoPedidoErp() && (this.pedido.getTipoPedido().isBonificacao() || this.pedido.isPedidoTroca())) {
        	StatusPedidoPda statusRelacionaPedido = new StatusPedidoPda();
    		statusRelacionaPedido.flRelacionaPedido = ValueUtil.VALOR_SIM;
    		Vector listaStatusPedido = StatusPedidoPdaService.getInstance().findAllByExample(statusRelacionaPedido);
    		for (int i = 0; i < listPedido.size(); i++) {
    			pedidoRef = (Pedido) listPedido.items[i];
    			tipoPedidoRef = pedidoRef.getTipoPedido(); 
			    if (!LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes() && tipoPedidoRef != null && ValueUtil.getBooleanValue(tipoPedidoRef.flBloqueiaMultRelacaoPedErp) && ValueUtil.isNotEmpty(pedidoRef.nuPedidoRelBonificacao)) {
			    	continue;
			    }
			    if (pedidoRef.isPedidoVenda() && OrigemPedido.FLORIGEMPEDIDO_ERP.equals(pedidoRef.flOrigemPedido)) {
    				for (int j = 0; j < listaStatusPedido.size(); j++) {
    					statusRelacionaPedido = (StatusPedidoPda) listaStatusPedido.items[j];
    					if (ValueUtil.valueEquals(pedidoRef.cdStatusPedido, statusRelacionaPedido.cdStatusPedido)) {
							if (LavenderePdaConfig.isPermiteMultiplasTrocasBonificacoes() && !PedidoService.getInstance().permitidoRelacionamento(pedidoRef, listTodosPedidos)) {
								continue;
							}
							newListPedido.addElement(pedidoRef);
    					}
    				}
    			}
    		}
    	}
    	return newListPedido;
	}
	
	private boolean isUsaPedidoTransmitido(Pedido pedidoRef) throws SQLException {
		if ((pedido.isPedidoBonificacao() || pedido.isPedidoValidaSaldoBonificacao()) && pedidoRef.isPedidoTransmitido() && LavenderePdaConfig.restringeDescontoPedidoBaseadoMediaPonderada && LavenderePdaConfig.usaMultiplasLiberacoesDescontoNoPedido()) {
			return false;
		}
		return pedidoRef.isPedidoTransmitido();
	}

    protected String[] getItem(Object domain) throws java.sql.SQLException {
        Pedido pedidoRef = (Pedido) domain;
        String tipoCredito = "";
        if (pedidoRef.getTipoPedido() != null && pedidoRef.getTipoPedido().isFlTipoCreditoCondicao()) {
        	tipoCredito = Messages.PEDIDO_RELACIONADO_LABEL_CREDITO_CONDICAO_PAGAMENTO;
        } else if (pedidoRef.getTipoPedido() != null && pedidoRef.getTipoPedido().isFlTipoCreditoFrete()) {
        	tipoCredito = Messages.PEDIDO_RELACIONADO_LABEL_CREDITO_TIPO_FRETE;
        }
        String[] item = {
        	StringUtil.getStringValue(pedidoRef.nuPedido),
            " - " + StringUtil.getStringValue(pedidoRef.dtEmissao),
            StringUtil.getStringValue(pedidoRef.statusPedidoPda.dsStatusPedido),
            StringUtil.getStringValue(pedidoRef.cdCliente),
            " " + StringUtil.getStringValue(ClienteService.getInstance().getNmRazaoSocial(pedidoRef.cdEmpresa, pedidoRef.cdRepresentante, pedidoRef.cdCliente)),
            "",
            tipoCredito,
            new StringBuffer(Messages.PEDIDO_RELACIONADO_REAIS).append(" ").append(StringUtil.getStringValueToInterface(pedidoRef.vlTotalPedido)).toString(),
        };
        return item;
    }

	protected String getSelectedRowKey() {
		BaseListContainer.Item c = (BaseListContainer.Item)listContainer.getSelectedItem();
		return c.id;
	}

	protected CrudService getCrudService() throws java.sql.SQLException {
		return PedidoService.getInstance();
	}

	protected BaseDomain getDomainFilter() {
		Pedido pedidoFilter = new Pedido();
		try {
			if (isTipoPedidoVendaProducao()) {
				pedidoFilter.cdCliente = cdClienteFilter;
				pedidoFilter.dsClienteFilter = edFiltro.getValue();
				if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
					if (ValueUtil.isNotEmpty(dsTipoDataFiltro) && dsTipoDataFiltro.equals(Messages.COMBOFILTRODATA_ENTREGA)) {
						pedidoFilter.dtEntregaInicialFilter = dtInicialFilter;
						pedidoFilter.dtEntregaFinalFilter = dtFinalFilter;
					} else {
						pedidoFilter.dtEmissaoInicialFilter = dtInicialFilter;
						pedidoFilter.dtEmissaoFinalFilter = dtFinalFilter;
					}
				} else {
					pedidoFilter.dtEmissaoInicialFilter = dtInicialFilter;
					pedidoFilter.dtEmissaoFinalFilter = dtFinalFilter;
				}
				pedidoFilter.cdRedeCliente = cbRede.getValue();
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
		return pedidoFilter;
	}

	protected void onFormStart() throws SQLException {
		lbDescricao.setText(MessageUtil.quebraLinhas(lbDescricao.getText(), width - 20));
    	UiUtil.add(this, lbDescricao, LEFT + WIDTH_GAP, getTop() + HEIGHT_GAP, FILL);
    	if (isTipoPedidoVendaProducao()) {
    		UiUtil.add(this, btFiltroAvancado, getRight(), AFTER + HEIGHT_GAP, UiUtil.getControlPreferredHeight());
    		UiUtil.add(this, edFiltro, getLeft(), SAME, getWFill() - UiUtil.getControlPreferredHeight() - WIDTH_GAP);
    	}
    	UiUtil.add(this, listContainer, LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	@Override
	public void screenResized() {
		super.screenResized();
		lbDescricao.setText(MessageUtil.quebraLinhas(lbDescricao.getText(), width - 20));
		lbDescricao.reposition();
		listContainer.setRect(LEFT, AFTER + HEIGHT_GAP, FILL, FILL);
	}

	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case EditIconEvent.PRESSED: {
				if (event.target == edFiltro) {
					list();
				}
				break;
			}
			case ControlEvent.PRESSED: {
				if (event.target == btSalvar) {
					btSalvarPedidosRelacionados();
				} else if (event.target == btLimpar) {
					btLimparClick();
				} else if (event.target == btSenha) {
					btSenhaClick();
				} else if (event.target == btFiltroAvancado) {
					btFiltroAvancadoClick();
				}
				break;
			}
		}
	}
	
	private void btFiltroAvancadoClick() throws SQLException {
		FiltroAvancadoPedidoRelacionadoWindow filtroAvancado = new FiltroAvancadoPedidoRelacionadoWindow();
		
		filtroAvancado.cdClienteFilter = cdClienteFilter;
		filtroAvancado.edDateInitial.setValue(dtInicialFilter);
		filtroAvancado.edDateFinal.setValue(dtFinalFilter);
		filtroAvancado.edDsCliente.setText(ValueUtil.isEmpty(dsClienteFilter) ? Messages.LABEL_TODOS : dsClienteFilter);
		filtroAvancado.cbRede.setSelectedIndex(cbRede.getSelectedIndex());
		if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
			filtroAvancado.cbDtEmissaoEntrega.setSelectedItem(dsTipoDataFiltro);
		}
		filtroAvancado.popup();
		
		if (filtroAvancado.filtrando) {
			dtInicialFilter = filtroAvancado.edDateInitial.getValue();
			dtFinalFilter = filtroAvancado.edDateFinal.getValue();
			dsClienteFilter = filtroAvancado.edDsCliente.getText();
			cbRede.setSelectedIndex(filtroAvancado.cbRede.getSelectedIndex());
			cdClienteFilter = filtroAvancado.cdClienteFilter;
			edFiltro.setText(ValueUtil.VALOR_NI);
			if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
				dsTipoDataFiltro = (String) filtroAvancado.cbDtEmissaoEntrega.getSelectedItem();
			}
			list();
		}
		
	}

	private void btLimparClick() throws SQLException {
		fecharWindow();
		closedByBtFechar = false;
	}
	
	private void btSalvarPedidosRelacionados() throws SQLException {
		int[] checkedItens = listContainer.getCheckedItens();
		int gridSize = checkedItens.length;
		if (gridSize == 0) throw new ValidationException(Messages.PEDIDO_RELACIONADO_NENHUM_PEDIDO_SELECIONADO);
		for (int i = 0; i < gridSize; i++) {
			Pedido pedido = (Pedido) PedidoService.getInstance().findByRowKeyDyn(listContainer.getId(checkedItens[i]));
			pedidosRelacionados.addElement(pedido);
		}
		unpop();
	}
	
	private void btSenhaClick() throws SQLException {
		AdmSenhaDinamicaWindow senha = new AdmSenhaDinamicaWindow();
		senha.setChaveSemente(SenhaDinamica.SENHA_LIBERACAO_RELACIONAR_PEDIDO_PRODUCAO);
		senha.setMensagem(Messages.PEDIDO_VENDAPRODUCAO_LIBERAR_OBRIGATORIEDADE_SENHA);
		if (senha.show() == AdmSenhaDinamicaWindow.SENHA_VALIDA) {
			SessionLavenderePda.liberadoPorSenhaRelacionarPedidoProducao = true;
			fecharWindow();
			closedByBtFechar = false;
		}
	}

	public void singleClickInList() throws SQLException {
		if (pedido.isPedidoVendaProducao()) {
			if (!LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao()) {
				super.singleClickInList();
				pedidoVendaRelacionado = (Pedido) getSelectedDomain();
				unpop();
			}
		} else if (!LavenderePdaConfig.isUsaVariosPedidosBonificados()) {
			super.singleClickInList();
			pedidoVendaRelacionado = (Pedido) getSelectedDomain();
			unpop();
		}
	}
	
	private boolean isPedidoRelacionadosValidoPorTipoCredito(Pedido pedidoRef) throws SQLException {
		this.pedido.getTipoPedido().flTipoCredito = ValueUtil.isEmpty(this.pedido.getTipoPedido().flTipoCredito) ? "" : this.pedido.getTipoPedido().flTipoCredito;
		boolean valido = true;
		Pedido pedidoRelacionadoBonificacao = new Pedido();
		pedidoRelacionadoBonificacao.cdEmpresa = SessionLavenderePda.cdEmpresa;
		pedidoRelacionadoBonificacao.cdRepresentante = SessionLavenderePda.getRepresentante().cdRepresentante;
       	pedidoRelacionadoBonificacao.cdCliente = SessionLavenderePda.getCliente().cdCliente;
		pedidoRelacionadoBonificacao.nuPedidoRelBonificacao = pedidoRef.nuPedido;
		Vector pedidoBonificacaoList = PedidoService.getInstance().findAllByExample(pedidoRelacionadoBonificacao);
		for (int i = 0; i < pedidoBonificacaoList.size(); i++) {
			Pedido pedidoBonificacando = (Pedido) pedidoBonificacaoList.items[i];
			if (ValueUtil.valueEquals(pedidoBonificacando.getTipoPedido().flTipoCredito, this.pedido.getTipoPedido().flTipoCredito)) {
				valido = false;
			}
		}
		return valido;
	}
	
	@Override
	protected void addButtons() {
    	try {
			if (LavenderePdaConfig.isUsaVariosPedidosBonificados() || (pedido.isPedidoVendaProducao() && LavenderePdaConfig.isPermiteMultiplosRelacionamentosPedidoProducao())) {
				addButtonPopup(btSalvar);
			}
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
    	addButtonPopup(btLimpar);
    	if (LavenderePdaConfig.getSementeSenhaPedidoProducao() > 0) {
    		addButtonPopup(btSenha);
    	}
		addBtFechar();
	}
	
	private boolean isTipoPedidoVendaProducao() throws SQLException {
		return LavenderePdaConfig.usaConfigPedidoProducao() && this.pedido.getTipoPedido().isVendaProducao();
	}

}
