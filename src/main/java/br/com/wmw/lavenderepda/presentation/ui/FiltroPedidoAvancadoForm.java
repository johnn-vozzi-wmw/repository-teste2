package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwListWindow;
import br.com.wmw.framework.presentation.ui.event.EditIconEvent;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.BaseComboBox;
import br.com.wmw.framework.presentation.ui.ext.ButtonPopup;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditFiltro;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Marcador;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.TipoPedido;
import br.com.wmw.lavenderepda.business.service.TipoPedidoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CargaPedidoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.MarcadorComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.NivelLiberacaoPedidoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPedidoMultiComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPendenciaPedidoMultiComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Vector;

public class FiltroPedidoAvancadoForm extends WmwListWindow {

	public ButtonPopup btLimpar;
	public ButtonPopup btFiltrar;
	public boolean filtroRealizado;
	//--
	public EditDate edDateInitial;
	public EditDate edDateFinal;
	public EditText edDsCliente;
	public EditText edDsProduto;
	public String cdClienteFilter;
	public Produto produtoFilter;
	public String cdTipoPedidoFilter;
	public String cdTipoPendenciaPedidoFilter;
	public BaseComboBox cbDtEmissaoEntrega;
	public BaseButton btFiltrarCliente;
	public BaseButton btTodosClientes;
	public BaseButton btFiltrarProduto;
	public BaseButton btTodosProdutos;
	public boolean inConsultaUltimosPedidos;
	public boolean isFiltered;
	public CheckBoolean ckFiltraPedidoNfe;
	public CheckBoolean ckFiltraPedidoDispLiberacao;
	public CheckBoolean ckFiltraPedidoItensPendentes;
	public CargaPedidoComboBox cbCargaPedido;
	public MarcadorComboBox cbMarcador;
	public TipoPedidoMultiComboBox cbTipoPedido;
	public TipoPendenciaPedidoMultiComboBox cbTipoPendenciaPedido;
	public EditFiltro edFiltroOrdemCompraCliente;
	public CheckBoolean ckFiltraMeusPedidos;
	public NivelLiberacaoPedidoComboBox nivelLiberacaoPedidoComboBox;
	private LabelName lbNivelLiberacao;
	public String cdStatusPedidoFilter;
	//--
	public static final String LABEL_TODOS = Messages.LABEL_TODOS;

	public FiltroPedidoAvancadoForm(boolean inConsultaUltimosPedidos, String cdRepresentante, String cdStatusPedidoFilter) throws SQLException {
		super(Messages.PRODUTO_FILTRO_AVANCADO);
		this.inConsultaUltimosPedidos = inConsultaUltimosPedidos;
		//--
        edDsCliente = new EditText("@@@@@@@@@@", 100);
        edDsCliente.drawBackgroundWhenDisabled = true;
        edDsCliente.setEditable(false);
        edDsCliente.setText(LABEL_TODOS);
        edDsProduto = new EditText("@@@@@@@@@@", 100);
        edDsProduto.drawBackgroundWhenDisabled = true;
        edDsProduto.setEditable(false);
        edDsProduto.setText(LABEL_TODOS);
        btFiltrarCliente = new BaseButton(UiUtil.getColorfulImage("images/alterarfiltro.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
        btTodosClientes = new BaseButton(Messages.BOTAO_TODOS);
        btFiltrarProduto = new BaseButton(UiUtil.getColorfulImage("images/alterarfiltro.png", UiUtil.getButtonImageIconSize(), UiUtil.getButtonImageIconSize()));
        btTodosProdutos = new BaseButton(Messages.BOTAO_TODOS);
        edDateInitial = new EditDate();
        edDateFinal = new EditDate();
        btLimpar = new ButtonPopup(FrameworkMessages.BOTAO_LIMPAR);
        btFiltrar = new ButtonPopup(FrameworkMessages.BOTAO_FILTRAR);
        ckFiltraPedidoNfe = new CheckBoolean(Messages.NFE_LABEL);
        ckFiltraPedidoDispLiberacao = new CheckBoolean(Messages.PEDIDO_FILTRO_DISP_LIBERACAO);
        ckFiltraPedidoItensPendentes = new CheckBoolean(Messages.PEDIDO_FILTRO_PEDIDO_ITENS_PENDENTES);
        ckFiltraMeusPedidos = new CheckBoolean(Messages.PEDIDO_FILTRO_MEUS_PEDIDOS);
        this.cdStatusPedidoFilter = cdStatusPedidoFilter;
        scrollable = true;
        if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
        	cbDtEmissaoEntrega = new BaseComboBox();
        	cbDtEmissaoEntrega.add(Messages.COMBOFILTRODATA_EMISSAO);
        	cbDtEmissaoEntrega.add(Messages.COMBOFILTRODATA_ENTREGA);
        	cbDtEmissaoEntrega.setSelectedIndex(0);
        }
        if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
        	cbCargaPedido = new CargaPedidoComboBox();
        	cbCargaPedido.loadCargaPedido(null, false);
        }
        if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
        	edFiltroOrdemCompraCliente = new EditFiltro("999999999", 50);
        }
        if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
	        cbTipoPedido = new TipoPedidoMultiComboBox();
	        cbTipoPedido.load();
        }
        if (LavenderePdaConfig.isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido()) {
        	cbTipoPendenciaPedido = new TipoPendenciaPedidoMultiComboBox();
        	cbTipoPendenciaPedido.load();
        }
        if (LavenderePdaConfig.usaFiltroMarcadorListaPedidos()) {
        	cbMarcador = new MarcadorComboBox(Marcador.ENTIDADE_MARCADOR_PEDIDO, cdRepresentante);
        }
        if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && LavenderePdaConfig.isUsaMotivoPendencia()) {
	        lbNivelLiberacao = new LabelName(Messages.NIVEL_LIBERACAO_PEDIDO_TITLE);
	        nivelLiberacaoPedidoComboBox = new NivelLiberacaoPedidoComboBox();
	        nivelLiberacaoPedidoComboBox.loadNiveisLiberacao();
        }
        //--
        setDefaultRect();
	}

	private void btLimparFiltros() {
		edDateFinal.setValue(null);
		edDateInitial.setValue(null);
		edDsCliente.setText(LABEL_TODOS);
		cdClienteFilter = "";
		clearProdutoFilters();
		ckFiltraPedidoNfe.setChecked(false);
		ckFiltraPedidoDispLiberacao.setChecked(false);
		ckFiltraPedidoItensPendentes.setChecked(false);
		ckFiltraMeusPedidos.setChecked(false);
		if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
			cbDtEmissaoEntrega.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			cbCargaPedido.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroMarcadorListaPedidos()) {
			cbMarcador.setSelectedIndex(0);
		}
		if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
			edFiltroOrdemCompraCliente.setText("");
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			cdTipoPedidoFilter = ValueUtil.VALOR_NI;
			cbTipoPedido.unselectAll();
		}
		if (LavenderePdaConfig.usaMarcaPedidoPendenteAprovacaoCondPagto() || LavenderePdaConfig.usaMarcaPedidoPendenteBaseadoLimiteCredito()) {
			cdTipoPendenciaPedidoFilter = ValueUtil.VALOR_NI;
			cbTipoPendenciaPedido.unselectAll();
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && LavenderePdaConfig.isUsaMotivoPendencia() && nivelLiberacaoPedidoComboBox.isEnabled()) {
			if (cdStatusPedidoFilter == null) {
				nivelLiberacaoPedidoComboBox.setSelectedIndex(-1);
			} else {
				nivelLiberacaoPedidoComboBox.setSelectedItem(SessionLavenderePda.nuOrdemLiberacaoUsuario);
			}
		}
	}

	@Override
	protected void btFecharClick() throws SQLException {
		isFiltered = false;
		super.btFecharClick();
	}

    private void btFiltroClienteClick() throws SQLException {
		Representante representanteOld = SessionLavenderePda.getRepresentante();
    	ListClienteWindow listCliente = new ListClienteWindow();
    	listCliente.popup();
    	if (SessionLavenderePda.isUsuarioSupervisor()) {
    		if (SessionLavenderePda.getRepresentante().cdRepresentante != null && listCliente.cliente == null) {
    			SessionLavenderePda.setRepresentante(representanteOld);
    		}
    	}
    	if (listCliente.cliente != null) {
    		edDsCliente.setText(listCliente.cliente.getDescription());
    		cdClienteFilter = listCliente.cliente.cdCliente;
    	}
    }

    private void btTodosClientesClick() {
    	edDsCliente.setText(LABEL_TODOS);
    	cdClienteFilter = ValueUtil.VALOR_NI;
    }

	@Override
    protected String[] getItem(Object domain) throws java.sql.SQLException {
    	TipoPedido tipoPedido = (TipoPedido) domain;
        return new String[] {StringUtil.getStringValue(tipoPedido.toString())};
	}
	
    private String getCheckedTipoPedido() {
    	return cbTipoPedido.getSelected();
    }
    
    private String getCheckedTipoPendenciaPedido() {
    	return cbTipoPendenciaPedido.getSelected();
    }

	@Override
	protected Vector getDomainList(BaseDomain domain) throws SQLException {
		return new Vector();
	}

	@Override
	protected String getSelectedRowKey() throws SQLException {
		return null;
	}

	@Override
	protected CrudService getCrudService() throws SQLException {
		return TipoPedidoService.getInstance();
	}

	@Override
	protected BaseDomain getDomainFilter() {
		return new TipoPedido();
	}

	@Override
	protected void onFormStart() throws SQLException {
    	if (!inConsultaUltimosPedidos) {
    		UiUtil.add(scBase, new LabelName(Messages.CLIENTE_NOME_ENTIDADE), getLeft(), getNextY(), FILL - HEIGHT_GAP, UiUtil.getLabelPreferredHeight());
    		UiUtil.add(scBase, btTodosClientes, getRight(), AFTER, PREFERRED, PREFERRED);
    		UiUtil.add(scBase, btFiltrarCliente, BEFORE - WIDTH_GAP, SAME, PREFERRED, SAME);
    		UiUtil.add(scBase, edDsCliente, getLeft(), SAME, getWFill() - btTodosClientes.getWidth() - btFiltrarCliente.getWidth() - 2 * WIDTH_GAP);
    	}
    	if (LavenderePdaConfig.usaFiltroDeProdutoNaListaDePedido) {
	    	UiUtil.add(scBase, new LabelName(Messages.PRODUTO_NOME_ENTIDADE), getLeft(), getNextY(), FILL - HEIGHT_GAP, UiUtil.getLabelPreferredHeight());
	    	UiUtil.add(scBase, btTodosProdutos, getRight(), AFTER, PREFERRED, PREFERRED);
	    	UiUtil.add(scBase, btFiltrarProduto, BEFORE - WIDTH_GAP, SAME, PREFERRED, SAME);
	    	UiUtil.add(scBase, edDsProduto, getLeft(), SAME, getWFill() - btFiltrarCliente.getPreferredWidth() - btTodosProdutos.getPreferredWidth() - 2 * WIDTH_GAP);
    	}
    	UiUtil.add(scBase, new LabelName(Messages.DATA_LABEL_DATA), getLeft(), getNextY(), PREFERRED, PREFERRED);
    	if (LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos()) {
    		UiUtil.add(scBase, cbDtEmissaoEntrega, getLeft(), AFTER, getWFill());
    	}
    	UiUtil.add(scBase, edDateInitial, getLeft(), LavenderePdaConfig.usaFiltroSelecaoTipoDataListaPedidos() ? getNextY() : AFTER);
    	UiUtil.add(scBase, edDateFinal, AFTER + WIDTH_GAP_BIG, SAME);

		if (LavenderePdaConfig.usaFiltroNfeListaPedidos() && LavenderePdaConfig.mostraAbaNfeNoPedido) {
			UiUtil.add(scBase, ckFiltraPedidoNfe, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaEnvioPedidoPendenteParaAutorizacaoEquipamento && SessionLavenderePda.nuOrdemLiberacaoUsuario > 0) {
			if (LavenderePdaConfig.isUsaMotivoPendencia()) {
				if (LavenderePdaConfig.cdStatusPedidoPendenteAprovacao.equalsIgnoreCase(cdStatusPedidoFilter) || cdStatusPedidoFilter == null) {
					UiUtil.add(scBase, lbNivelLiberacao, nivelLiberacaoPedidoComboBox, getLeft(), getNextY());
				}
			} else {
				UiUtil.add(scBase, ckFiltraPedidoDispLiberacao, getLeft(), getNextY());
			}
		}
		if (LavenderePdaConfig.isMarcaItemPedidoPendenteAprovacao()) {
			UiUtil.add(scBase, ckFiltraPedidoItensPendentes, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaPedidoQualquerRepresentanteParaHistoricoCliente) {
			UiUtil.add(scBase, ckFiltraMeusPedidos, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isUsaCargaPedidoPorRotaEntregaDoCliente()) {
			UiUtil.add(scBase, new LabelName(Messages.CARGAPEDIDO_CARGA_PEDIDO), cbCargaPedido, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaFiltroMarcadorListaPedidos()) {
			UiUtil.add(scBase, new LabelName(Messages.PEDIDO_MARCADORES), cbMarcador, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaFiltroOrdemCompraClienteListaPedidos()) {
			UiUtil.add(scBase, new LabelName(Messages.NUORDEMCOMPRACLENTE_LABEL_NUORDEMCOMPRACLIENTE), edFiltroOrdemCompraCliente, getLeft(), getNextY());
		}
		if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			UiUtil.add(scBase, new LabelName(Messages.TIPOPEDIDO_LABEL_TIPOPEDIDO), cbTipoPedido, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido()) {
			UiUtil.add(scBase, new LabelName(Messages.TIPOPENDENCIA_LABEL), cbTipoPendenciaPedido, getLeft(), getNextY());
		}
		addButtonPopup(btFiltrar);
		addButtonPopup(btLimpar);
		addButtonPopup(btFechar);
	}

	@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btFiltrarCliente) {
					btFiltroClienteClick();
				} else if (event.target == btTodosClientes) {
					btTodosClientesClick();
				} else if (event.target == btFiltrarProduto) {
					btFiltroProdutoClick();
				} else if (event.target == btTodosProdutos) {
					btTodosProdutosClick();
				} else if (event.target == btFiltrar) {
					isFiltered = true;
					if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
						cdTipoPedidoFilter = getCheckedTipoPedido();
					}
					if (LavenderePdaConfig.isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido()) {
						cdTipoPendenciaPedidoFilter = getCheckedTipoPendenciaPedido();
					}
					fecharWindow();
				} else if (event.target == btLimpar) {
					btLimparFiltros();
				}
				break;
			} case EditIconEvent.PRESSED: {
				if (event.target == edFiltroOrdemCompraCliente) {
					isFiltered = true;
					fecharWindow();
				}
				break;
			} case KeyEvent.SPECIAL_KEY_PRESS: {
				if (event.target == edFiltroOrdemCompraCliente && ((KeyEvent)event).isActionKey()){
					isFiltered = true;
					if (!LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
						cdTipoPedidoFilter = getCheckedTipoPedido();
					}
					if (LavenderePdaConfig.isUsaPrioridadeLiberacaoEPossuiConfiguradoPendenciasDoPedido()) {
						cdTipoPendenciaPedidoFilter = getCheckedTipoPendenciaPedido();
					}
					fecharWindow();
				}
				break;
			}
		}
	}
	
	private void clearProdutoFilters() {
		produtoFilter = null;
		edDsProduto.setText(LABEL_TODOS);
	}

    private void btFiltroProdutoClick() throws SQLException {
    	ListProdutoWindow listProdutoWindow = new ListProdutoWindow();
    	listProdutoWindow.popup();
    	fillProdutoFilters(listProdutoWindow.produto);
    }
    
    public void fillProdutoFilters(Produto produto) {
    	if (produtoFilter != null && produto == null) {
    		return;
    	}
    	produtoFilter = produto;
    	edDsProduto.setText(produto != null ? produto.getDescription() : LABEL_TODOS);
    }

    private void btTodosProdutosClick() {
    	edDsProduto.setText(LABEL_TODOS);
    	produtoFilter = null;
    }
}
