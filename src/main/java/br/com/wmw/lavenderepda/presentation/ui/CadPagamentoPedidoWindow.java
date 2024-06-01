package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.WmwCadWindow;
import br.com.wmw.framework.presentation.ui.event.KeyboardEvent;
import br.com.wmw.framework.presentation.ui.event.ValueChangeEvent;
import br.com.wmw.framework.presentation.ui.ext.CheckBoolean;
import br.com.wmw.framework.presentation.ui.ext.EditDate;
import br.com.wmw.framework.presentation.ui.ext.EditMemo;
import br.com.wmw.framework.presentation.ui.ext.EditNumberFrac;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.WmwMessageBox;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.VencimentoAdicBoleto;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import br.com.wmw.lavenderepda.business.service.VencimentoAdicBoletoService;
import br.com.wmw.lavenderepda.business.service.VenctoPagamentoPedidoService;
import br.com.wmw.lavenderepda.presentation.ui.combo.CondicaoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.QuantidadeBoletosComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.TipoPagamentoComboBox;
import br.com.wmw.lavenderepda.presentation.ui.combo.VencimentoAdicionalComboBox;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.event.KeyEvent;
import totalcross.util.Date;
import totalcross.util.Vector;

public class CadPagamentoPedidoWindow extends WmwCadWindow {
	
    private LabelValue lbInfoPagamento;
    private TipoPagamentoComboBox cbTipoPagamento;
    private CondicaoPagamentoComboBox cbCondicaoPagamento;
    private VencimentoAdicionalComboBox cbVencimentoAdicional;
    private QuantidadeBoletosComboBox cbQuantidadeBoletos;
    private Pedido pedido;
    private LabelName lbCbVencimentoAdicional;
    private LabelName lbCbQuantidadeBoletos;
    private LabelName lbVlPedidoAberto;
    private LabelValue lvVlPedidoAberto;
    private LabelName lbPctDescMax;
    private LabelValue lvPctDescMax;
    private LabelName lbVlMinimo;
    private LabelValue lvVlMinimo;
    private LabelName lbVlPagamento;
    private EditNumberFrac edVlPagamento;
    private LabelName lbVlDesconto;
    private EditNumberFrac edVlDesconto;
    private LabelName lbVlTotal;
    private LabelName lbDtVencimento;
    private LabelValue lvVlTotal;
    private LabelName lbPctDesconto;
    private LabelValue lvPctDesconto;
    private LabelName lbCdBoletoConfig;
    private EditNumberInt edNuBanco;
    private LabelName lbNuComplemento;
    private EditNumberInt edNuComplemento;
    private LabelName lbNuAgencia;
    private EditText edNuAgencia;
    private LabelName lbNuConta;
    private EditText edNuConta;
    private LabelName lbNuCheque;
    private EditText edNuCheque;
    private CheckBoolean ckChequeTerceiro;
    private LabelName lbDsEmitente;
    private EditText edDsEmitente;
    private LabelName lbDsReferenteCheque;
    private EditMemo edDsReferenteCheque;
    private EditDate edDtVencimento;
    private LabelName lbDsBanco;
    private EditText edDsBanco;
    private LabelName lbDtCheque;
    private EditDate edDtCheque;
    private ListPagamentoPedidoForm listPagamentoPedidoForm;
    private LinkedHashMap<Integer, EditDate> mapDatasEntrega;
    private List<Date> datasEntrega;
    private String cdTipoPagamento;
    
    private double vlPedidoAberto;
    private LabelName lbVlPagamentoBruto;
    private EditNumberFrac edVlPagamentoBruto;
    private LabelName lbVlIndicePagamento;
    private LabelValue lvPercentualIndicePagamento;
	
	public CadPagamentoPedidoWindow(Pedido pedido, double vlPedidoAberto) throws SQLException {
        super(Messages.PAGAMENTOPEDIDO_TITULO_CADASTRO);
        mapDatasEntrega = new LinkedHashMap<>();
        this.pedido = (Pedido) pedido.clone();
        this.pedido.setCliente(SessionLavenderePda.getCliente());
        this.vlPedidoAberto = vlPedidoAberto;
        lbInfoPagamento = new LabelValue(Messages.PAGAMENTOPEDIDO_LABEL_INFO_PAGAMENTO);
        cbTipoPagamento = new TipoPagamentoComboBox();
        cbCondicaoPagamento = new CondicaoPagamentoComboBox(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO);
        cbVencimentoAdicional = new VencimentoAdicionalComboBox();
        cbQuantidadeBoletos = new QuantidadeBoletosComboBox();
        lbCbVencimentoAdicional = new LabelName(Messages.PEDIDO_LABEL_VENCIMENTO_ADICIONAL);
        lbCbQuantidadeBoletos = new LabelName(Messages.PEDIDO_LABEL_QUANTIDADE_BOLETOS);
        lbVlPedidoAberto = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_VLEMABERTO);  
        lvVlPedidoAberto = new LabelValue();
        lvVlPedidoAberto.setValue(vlPedidoAberto > 0 ? vlPedidoAberto : 0);
        lbPctDescMax = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_PCT_DESC_MAX);  
        lvPctDescMax = new LabelValue(); 
        lbVlMinimo = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_VLMIN); 
        lvVlMinimo = new LabelValue();         
        lbVlPagamento = new LabelName(LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido ? Messages.PAGAMENTOPEDIDO_LABEL_VLPAGAMENTOLIQUIDO : Messages.PAGAMENTOPEDIDO_LABEL_VLPAGAMENTOPEDIDO);
        edVlPagamento = new EditNumberFrac("9999999", 9);
        edVlPagamento.setEditable(!LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido);
        lbVlDesconto = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_VLDESCONTO);
        edVlDesconto = new EditNumberFrac("9999999", 9);
        lbVlTotal = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_VLTOTAL);    
        lvVlTotal = new LabelValue();           
        lbPctDesconto = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_PCT_DESC);
        lvPctDesconto = new LabelValue();
        lbCdBoletoConfig = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_CODIGO_BANCO);
        edNuBanco = new EditNumberInt("99999", 3);
        lbNuComplemento = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_COMPLEMENTO);
        edNuComplemento = new EditNumberInt("99999", 3);
        lbNuAgencia = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_AGENCIA);
        edNuAgencia = new EditText("@@@@@@@@@@", 6);
        lbNuConta = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_CONTA);
        edNuConta = new EditText("@@@@@@@@@@", 30);
        lbNuCheque = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_CHEQUE);
        edNuCheque = new EditText("@@@@@@@@@@", 10);
        ckChequeTerceiro = new CheckBoolean(Messages.PAGAMENTOPEDIDO_LABEL_CHEQUE_TERCEIRO);
        lbDsEmitente = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_EMITENTE);
        edDsEmitente = new EditText("@@@@@@@@@@", 100);
        lbDsReferenteCheque = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_REFERENTE_CHEQUE);
        edDsReferenteCheque = new EditMemo("@@@@@@@@@@", 6, 4000);
        edDtVencimento = new EditDate();
        lbVlPagamentoBruto = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_VLPAGAMENTOBRUTO);
        edVlPagamentoBruto = new EditNumberFrac("9999999", 9);
        lbVlIndicePagamento = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_DESCACRESCINDICEPAGAMENTO);
        lvPercentualIndicePagamento = new LabelValue();
        lbDsBanco = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_BANCO_CHEQUE);
        edDsBanco = new EditText("@@@@@@@@@@", 50);
        lbDtCheque =  new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_DATA_CHEQUE);
        edDtCheque = new EditDate();
        lbDtVencimento = new LabelName(Messages.PAGAMENTOPEDIDO_LABEL_DTVENCIMENTO);
        loadComboCondicaoPagamento();
        loadComboTipoPagamento();
		getVencimentos();
    }
	
	public CadPagamentoPedidoWindow(Pedido pedido, double vlPedidoAberto, String cdTipoPagamento) throws SQLException {
		this(pedido, vlPedidoAberto);
		this.cdTipoPagamento = cdTipoPagamento;
	}

	private void loadComboCondicaoPagamento() throws SQLException {
		cbCondicaoPagamento.loadCondicoesPagamento(pedido);
		if (cbCondicaoPagamento.size() > 0) {
        	cbCondicaoPagamento.setValue(pedido.getCliente().cdCondicaoPagamento);
        	pedido.cdCondicaoPagamento = pedido.getCliente().cdCondicaoPagamento;
        	if (ValueUtil.isEmpty(cbCondicaoPagamento.getValue())) {
        		cbCondicaoPagamento.setSelectedIndex(0);
        		pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
        	}
        	lvVlMinimo.setValue(CondicaoPagamentoService.getInstance().getQtVlMinCondicaoPagamento(pedido.cdCondicaoPagamento));
        	lvPctDescMax.setValue(CondicaoPagamentoService.getInstance().getPctDescMaxCondicaoPagamento(pedido.cdCondicaoPagamento));
        } else {
        	lvVlMinimo.setText("");
        	lvPctDescMax.setText("");
        }
	}
	
	private void loadComboCondicaoPagamento(String cdCondicaoPagamento) throws SQLException {
		cbCondicaoPagamento.loadCondicoesPagamento(pedido);
		if (cbCondicaoPagamento.size() > 0) {
			cbCondicaoPagamento.setValue(cdCondicaoPagamento);
			pedido.cdCondicaoPagamento = cdCondicaoPagamento;
			if (ValueUtil.isEmpty(cbCondicaoPagamento.getValue())) {
        		cbCondicaoPagamento.setSelectedIndex(0);
        		pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
        	}
			lvVlMinimo.setValue(CondicaoPagamentoService.getInstance().getQtVlMinCondicaoPagamento(cdCondicaoPagamento));
        	lvPctDescMax.setValue(CondicaoPagamentoService.getInstance().getPctDescMaxCondicaoPagamento(cdCondicaoPagamento));
		} else {
        	lvVlMinimo.setText("");
        	lvPctDescMax.setText("");
        }
	}

	private void loadComboTipoPagamento() throws SQLException {
        cbTipoPagamento.carregaTipoPagamentos(this.pedido, false);
        if (!isEditing()) {
        	cbTipoPagamento.setValue(this.pedido.getCliente().cdTipoPagamento);
			if (ValueUtil.isEmpty(cbTipoPagamento.getValue())) {
				cbTipoPagamento.setSelectedIndex(0);
			}
			//--
			pedido.cdTipoPagamento = cbTipoPagamento.getValue();
        }
	}

    //-----------------------------------------------

    @Override
    public String getEntityDescription() {
    	return Messages.PAGAMENTOPEDIDO_TITULO_CADASTRO;
    }

    @Override
    protected CrudService getCrudService() throws java.sql.SQLException {
        return PagamentoPedidoService.getInstance();
    }
    
    @Override
    protected BaseDomain createDomain() throws SQLException {
        return new PagamentoPedido();
    }
    
    @Override
    protected BaseDomain screenToDomain() throws SQLException {
        PagamentoPedido pagamentoPedido = (PagamentoPedido) getDomain();
        pagamentoPedido.cdEmpresa = pedido.cdEmpresa;
        pagamentoPedido.cdRepresentante = pedido.cdRepresentante;
        pagamentoPedido.nuPedido = pedido.nuPedido;
        pagamentoPedido.flOrigemPedido = pedido.flOrigemPedido;
        pagamentoPedido.cdTipoPagamento = cbTipoPagamento.getValue();
        pagamentoPedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
        pagamentoPedido.vlPagamentoPedido = edVlPagamento.getValueDouble();
        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	pagamentoPedido.vlPagamentoBruto = edVlPagamentoBruto.getValueDouble();
        	pagamentoPedido.vlIndicePagamento = cbTipoPagamento.getTipoPagamento().vlIndicePagamento;
        }
        if (LavenderePdaConfig.usaDescontoMultiplosPagamentosParaPedido) {
        	pagamentoPedido.vlDesconto = edVlDesconto.getValueDouble();
    	}
        pagamentoPedido.cdUsuario = pedido.cdUsuario;
        pagamentoPedido.vlTotalPedido = LavenderePdaConfig.detalhaInfoTributariaPedidoEItemPedido ? pedido.vlFinalPedidoDescTribFrete : pedido.vlTotalPedido;
        setValoresRelacionadosAChequeDomain(pagamentoPedido);
        pagamentoPedido.dtVencimento = edDtVencimento.getValue();
        if (cbTipoPagamento.isUsaVencimento()) {
        	pagamentoPedido.dtVencimentoBase = LavenderePdaConfig.permiteAlterarVencimentoConformeCondPagto ? getDtVencimentoConformeCondPagto() : LavenderePdaConfig.permiteAlterarVencimentoConformeCliente ? getDtVencimentoConformeCliente() : getDtVencimento();
        }
		populaVencimentos(pagamentoPedido);
        return pagamentoPedido;
    }

	private Date getDtVencimentoConformeCliente() throws SQLException {
		Date dtVencimento = DateUtil.getCurrentDate();
		int prazoCliente = retornaDiasMaximoPagamentoCliente();
		dtVencimento.advance(prazoCliente);
		return dtVencimento;
	}

	private void setValoresRelacionadosAChequeDomain(PagamentoPedido pagamentoPedido) {
		if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
        	pagamentoPedido.nuBanco = edNuBanco.getText();
        	pagamentoPedido.nuComplemento = edNuComplemento.getText();
        	pagamentoPedido.nuAgencia = edNuAgencia.getText();
        	pagamentoPedido.nuConta = edNuConta.getValue();
        	pagamentoPedido.nuCheque = edNuCheque.getValue();
        	pagamentoPedido.flChequeTerceiro = cbTipoPagamento.isCheque() ? ckChequeTerceiro.getValue() : null;
        	pagamentoPedido.dsEmitente = edDsEmitente.getValue();
        	pagamentoPedido.dsReferenteCheque = edDsReferenteCheque.getValue();
        	pagamentoPedido.dsBanco = edDsBanco.getValue();
        	pagamentoPedido.dtCheque = edDtCheque.getValue();
        }
	}
    
    @Override
    protected void domainToScreen(BaseDomain domain) throws SQLException {
        PagamentoPedido pagamentoPedido = (PagamentoPedido) domain;
        edVlPagamento.setValue(pagamentoPedido.vlPagamentoPedido);
        if (LavenderePdaConfig.usaDescontoMultiplosPagamentosParaPedido) {
        	edVlDesconto.setValue(pagamentoPedido.vlDesconto);
        }
        cbTipoPagamento.setValue(pagamentoPedido.cdTipoPagamento);
        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	calculaPercentualIndicePagamento();
        	edVlPagamentoBruto.setValue(pagamentoPedido.vlPagamentoBruto);
        }
        pedido.cdTipoPagamento = pagamentoPedido.cdTipoPagamento;
        loadComboCondicaoPagamento(pagamentoPedido.cdCondicaoPagamento);
        calculaDesconto();
        vlPedidoAberto += pagamentoPedido.vlPagamentoPedido;
        setValoresRelacionadosAChequeScreen(pagamentoPedido);
        if (cbTipoPagamento.isUsaVencimento()) {
        	edDtVencimento.setValue(pagamentoPedido.dtVencimento);
        }
        if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto && ValueUtil.isNotEmpty(pagamentoPedido.cdVencimentoAdicBoleto)) {
        	VenctoPagamentoPedidoService.getInstance().findVctosPagamentoPedido(pagamentoPedido);
        	cbVencimentoAdicional.setValue(pagamentoPedido.cdVencimentoAdicBoleto);
        	cbQuantidadeBoletos.load(getVencimentoAdicionalSelecionado().nuMaxVencimentos);
        	cbQuantidadeBoletos.setValue(VenctoPagamentoPedidoService.getInstance().countQtVctosPagamentoPedido(pagamentoPedido));
        	atualizaComponenteRelacionadosAVencimento();
        }
    }

	private void setValoresRelacionadosAChequeScreen(PagamentoPedido pagamentoPedido) {
		if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
        	edNuBanco.setText(pagamentoPedido.nuBanco);
        	edNuComplemento.setText(pagamentoPedido.nuComplemento);
        	edNuAgencia.setText(pagamentoPedido.nuAgencia);
        	edNuConta.setValue(pagamentoPedido.nuConta);
        	edNuCheque.setValue(pagamentoPedido.nuCheque);
        	ckChequeTerceiro.setValue(pagamentoPedido.flChequeTerceiro);
        	edDsEmitente.setEnabled(ckChequeTerceiro.isChecked());
        	edDsEmitente.setValue(pagamentoPedido.dsEmitente);
        	edDsReferenteCheque.setValue(pagamentoPedido.dsReferenteCheque);
        	edDsBanco.setValue(pagamentoPedido.dsBanco);
        	edDtCheque.setValue(pagamentoPedido.dtCheque);
        }
	}
    
    @Override
    protected void clearScreen() throws java.sql.SQLException {
    	//--
    }
    
    @Override
    public void setEnabled(boolean enabled) {
    	super.setEnabled(enabled);
    	cbTipoPagamento.setEditable(enabled);
        cbCondicaoPagamento.setEditable(enabled);
        if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
        	edVlPagamentoBruto.setEditable(enabled);
        	edVlPagamento.setEditable(false);
        } else {
        	edVlPagamento.setEditable(enabled);
        }
        edVlDesconto.setEditable(enabled);
        edNuBanco.setEditable(enabled);
        edNuComplemento.setEditable(enabled);
        edNuAgencia.setEditable(enabled);
        edNuConta.setEditable(enabled);
        edNuCheque.setEditable(enabled);
        ckChequeTerceiro.setEditable(enabled);
        edDsEmitente.setEditable(enabled);
        edDsReferenteCheque.setEditable(enabled);
        edDtVencimento.setEditable(enabled);
        edVlPagamentoBruto.setEditable(enabled);
        edDsBanco.setEditable(enabled);
        edDtCheque.setEditable(enabled);
        cbVencimentoAdicional.setEditable(enabled);
        cbQuantidadeBoletos.setEditable(enabled);
    }
    
    @Override
    public void add() throws java.sql.SQLException {
    	super.add();
    	loadComboTipoPagamento();
    	setVlIndicePagamento();
    	loadComboCondicaoPagamento();
        adicionaComponentesRelacionadosACheque();
        mostraComponentesRelacionadosAoVencimento();
        setDtVencimento();
    }
    
    private void setVlIndicePagamento() {
		if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido && cbTipoPagamento.getTipoPagamento() != null) {
			calculaPercentualIndicePagamento();
			if (edVlPagamentoBruto.getValueDouble() != 0) {
				applyIndicePagamento();
				calculaDesconto();
			}
		}
	}
    
    private void calculaPercentualIndicePagamento() {
    	TipoPagamento tipoPagamento = cbTipoPagamento.getTipoPagamento();
    	if (tipoPagamento != null) {
    		if (tipoPagamento.vlIndicePagamento > 0.0 && tipoPagamento.vlIndicePagamento != 1.0) {
    			double vlPercentualIndice = (tipoPagamento.vlIndicePagamento * 100) - 100;
				lvPercentualIndicePagamento.setText(StringUtil.getStringValueToInterface(vlPercentualIndice) + " %");
    		} else {
    			lvPercentualIndicePagamento.setText("--");
    		}
    	}
    }

    @Override
    public void edit(BaseDomain domain) throws java.sql.SQLException {
    	loadComboTipoPagamento();
    	super.edit(domain);
    	adicionaComponentesRelacionadosACheque();
    	mostraComponentesRelacionadosAoVencimento();
    }
    
    @Override
    protected void beforeSave() throws SQLException {
    	super.beforeSave();
    	PagamentoPedido pagamentoPedido = (PagamentoPedido) getDomain();
    	pagamentoPedido.vlPedidoAberto = vlPedidoAberto;
    	pagamentoPedido.vlMinimo = ValueUtil.round(lvVlMinimo.getDoubleValue());
    	pagamentoPedido.vlPctDescontoMaxCondicaoPagamento = ValueUtil.round(lvPctDescMax.getDoubleValue());
    	pagamentoPedido.vlPctDescontoPagamento = ValueUtil.round(lvPctDesconto.getDoubleValue());
    	pagamentoPedido.validaInformacoesRelacionadasACheque = cbTipoPagamento.isCheque();
    	pagamentoPedido.validaInformacoesRelacionadasAoVencimento = cbTipoPagamento.isUsaVencimento() && !usaVctoAdicional();
    	pagamentoPedido.dtVencimento = edDtVencimento.getValue();
    }

	private void populaVencimentos(PagamentoPedido pagamentoPedido) {
		TipoPagamento tipoPagamento = getTipoPagamento();
		VencimentoAdicBoleto vencimentoAdicionalSelecionado = getVencimentoAdicionalSelecionado();
		if (vencimentoAdicionalSelecionado != null) {
			pagamentoPedido.venctoPagamentoPedidos = new ArrayList<>();
			pagamentoPedido.cdVencimentoAdicBoleto = vencimentoAdicionalSelecionado.cdVencimentoAdicBoleto;
			if (tipoPagamento != null && ValueUtil.VALOR_SIM.equals(tipoPagamento.flBoleto) && ValueUtil.VALOR_SIM.equals(tipoPagamento.flUsaVencimento)) {
				Iterator<Entry<Integer, EditDate>> iterator = mapDatasEntrega.entrySet().iterator();
				double valorTotalBoleto = 0D;
				double vlPagamentoPedido = pagamentoPedido.vlPagamentoPedido - pagamentoPedido.vlDesconto;
				final Integer qtVencimentos = mapDatasEntrega.size();
				while (iterator.hasNext()) {
					Map.Entry<Integer, EditDate> entry = iterator.next();
					VenctoPagamentoPedido venctoPagamentoPedido = new VenctoPagamentoPedido();
					venctoPagamentoPedido.cdEmpresa = pagamentoPedido.cdEmpresa;
					venctoPagamentoPedido.cdRepresentante = pagamentoPedido.cdRepresentante;
					venctoPagamentoPedido.nuPedido = pagamentoPedido.nuPedido;
					venctoPagamentoPedido.flOrigemPedido = pagamentoPedido.flOrigemPedido;
					venctoPagamentoPedido.nuSeqVenctoPagamentoPedido = entry.getKey();
					venctoPagamentoPedido.dtVencimento = entry.getValue().getValue();
					venctoPagamentoPedido.vlBoleto = PagamentoPedidoService.getInstance().arredondaValor(vlPagamentoPedido / qtVencimentos);
					valorTotalBoleto += venctoPagamentoPedido.vlBoleto;
					if (!iterator.hasNext()) {
						venctoPagamentoPedido.vlBoleto += vlPagamentoPedido - valorTotalBoleto;
					}
					pagamentoPedido.venctoPagamentoPedidos.add(venctoPagamentoPedido);
				}
			}
		}
	}
    
    @Override
    protected void afterSave() throws java.sql.SQLException {
    	super.afterSave();
    	listPagamentoPedidoForm.list();
    }
    
    @Override
    protected void list() throws java.sql.SQLException {
    	listPagamentoPedidoForm.list();
    }
    
    @Override
    public void initUI() {
		super.initUI();
		addComponentsScreen();
		setDsReferenteCheque();
		setDtVencimento();
	}
    
    @Override
    protected void addButtons() {
    	if (edVlPagamento.isEditable() || (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido && edVlPagamentoBruto.isEditable())) {
    		super.addButtons();
    	} else {
        	btFechar.setText(FrameworkMessages.BOTAO_FECHAR);
        	addButtonPopup(btFechar);
    	}
    }

	private void addComponentsScreen() {
		UiUtil.add(this, lbInfoPagamento, getLeft(), getNextY());
		UiUtil.add(this, lbVlPedidoAberto, lvVlPedidoAberto, getLeft(), AFTER + HEIGHT_GAP_BIG);
		UiUtil.add(this, new LabelName(Messages.TIPOPAGTO_LABEL_TIPOPAGTO), cbTipoPagamento, getLeft(), getNextY());
		if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
			UiUtil.add(this, lbVlIndicePagamento, lvPercentualIndicePagamento, getLeft(), getNextY(), PREFERRED);
		}
		if (!LavenderePdaConfig.isOcultaSelecaoCondicaoPagamentoPagamentoPedido()) {
			UiUtil.add(this, new LabelName(Messages.PEDIDO_LABEL_CDCONDICAOPAGAMENTO), cbCondicaoPagamento, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaPctMaxDescontoPagamentoPorCondPagto) {
			UiUtil.add(this, lbPctDescMax, lvPctDescMax, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.isValorMinimoParaPedidoPorCondPagto() && !LavenderePdaConfig.isConfigValorMinimoUnicoParaPedido()) {
			UiUtil.add(this, lbVlMinimo, lvVlMinimo, getLeft(), getNextY());
		}
		if (LavenderePdaConfig.usaIndiceFinanceiroTipoPagamentoPagamentoPedido) {
			UiUtil.add(this, lbVlPagamentoBruto, edVlPagamentoBruto, getLeft(), getNextY(), SCREENSIZE + 35);
		}
		UiUtil.add(this, lbVlPagamento, edVlPagamento, getLeft(), getNextY(), SCREENSIZE + 35);
		if (LavenderePdaConfig.usaDescontoMultiplosPagamentosParaPedido) {
			UiUtil.add(this, lbVlDesconto, edVlDesconto, getLeft(), getNextY(), SCREENSIZE + 35);
			UiUtil.add(this, lbPctDesconto, lvPctDesconto, getLeft(), getNextY());
		}
		UiUtil.add(this, lbVlTotal, lvVlTotal, getLeft(), getNextY());
		adicionaComponentesRelacionadosACheque();
		TipoPagamento tipoPagamento = getTipoPagamento();
		if (tipoPagamento != null && ValueUtil.VALOR_SIM.equals(tipoPagamento.flBoleto) && ValueUtil.VALOR_SIM.equals(tipoPagamento.flUsaVencimento)) {
			adicionaComponentesVencimento();
		}
		mostraComponentesRelacionadosAoVencimento();
	}

	private void adicionaComponentesVencimento() {
		if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto && ValueUtil.isNotEmpty(getVencimentoAdicional()) && datasEntrega != null) {
			UiUtil.add(this, lbCbVencimentoAdicional, getLeft(), getNextY());
			UiUtil.add(this, lbCbQuantidadeBoletos, getRight(), SAME);

			UiUtil.add(this, cbVencimentoAdicional, getLeft(), getNextY(), SCREENSIZE + 35, UiUtil.getControlPreferredHeight());
			UiUtil.add(this, cbQuantidadeBoletos, getRight(), SAME, SCREENSIZE + 35, UiUtil.getControlPreferredHeight());
			carregaDatasEntrega();
			return;
		}
		UiUtil.add(this, lbDtVencimento, edDtVencimento, getLeft(), getNextY());
	}

	private TipoPagamento getTipoPagamento() {
		TipoPagamento tipoPagamento = new TipoPagamento();
		tipoPagamento.cdEmpresa = pedido.cdEmpresa;
		tipoPagamento.cdRepresentante = pedido.cdRepresentante;
		tipoPagamento.cdTipoPagamento = cdTipoPagamento == null ? pedido.cdTipoPagamento : cdTipoPagamento;
		try {
			tipoPagamento = (TipoPagamento) TipoPagamentoService.getInstance().findByPrimaryKey(tipoPagamento);
		} catch (SQLException e) {
			WmwMessageBox.showException(e, true);
		}
		return tipoPagamento;
	}

	private void carregaVencimentos(Vector vencimentos) {
		try {
			cbVencimentoAdicional.load(vencimentos);
			cbVencimentoAdicional.setSelectedIndex(0);
			final VencimentoAdicBoleto venctoAdic = (VencimentoAdicBoleto) vencimentos.items[0];
			cbQuantidadeBoletos.load(venctoAdic.nuMaxVencimentos);
			datasEntrega = new ArrayList<>(venctoAdic.nuMaxVencimentos);
		} catch (SQLException e) {
			WmwMessageBox.showException(e, true);
		}
	}

	private void getVencAdicionalPorTipoPagamentoSelecionado() {
		TipoPagamento tipoPagamento = getTipoPagamento();
		if (tipoPagamento != null && ValueUtil.VALOR_SIM.equals(tipoPagamento.flBoleto) && ValueUtil.VALOR_SIM.equals(tipoPagamento.flUsaVencimento)) {
			getVencimentos();
		}
		if (tipoPagamento != null && ValueUtil.VALOR_NAO.equals(tipoPagamento.flBoleto) && ValueUtil.VALOR_NAO.equals(tipoPagamento.flUsaVencimento)) {
			cbVencimentoAdicional.setSelectedIndex(1);
		}
	}
	
	private void carregaDatasEntrega() {
		mapDatasEntrega.clear();
		int y = getNextY();
		try {
			List<VenctoPagamentoPedido> venctoList = ((PagamentoPedido)getDomain()).venctoPagamentoPedidos;
			datasEntrega.clear();
			for (int i = 1; i <= cbQuantidadeBoletos.getValue(); i++) {
				final EditDate campoData = new EditDate();
				Date dataReferencia = null;
				int nuIntervalo = getVencimentoAdicionalSelecionado().nuIntervaloVencimentos;
				if (mapDatasEntrega.isEmpty()) {
					dataReferencia = DateUtil.getCurrentDate();
					nuIntervalo = retornaNuIntervalo();
				} else {
					dataReferencia = mapDatasEntrega.get(i - 1).getValue();
				}
				dataReferencia.advance(nuIntervalo);
				Date date = VencimentoAdicBoletoService.getInstance().getDateVctoOuReferencia(dataReferencia, venctoList, i - 1);
				campoData.setValue(date);
				campoData.setEnabled(isEnabled());
				mapDatasEntrega.put(i, campoData);
				campoData.appId = i;
				datasEntrega.add(date);
				boolean par = i % 2 == 0;
				LabelName lbl = new LabelName(Messages.PAGAMENTO_DATA_VENCIMENTO + i);
				UiUtil.add(this, lbl, par ? getRight() : getLeft(), par ? y : getNextY());
				UiUtil.add(this, campoData, par ? getRight() : getLeft(), AFTER + HEIGHT_GAP, campoData.getInternalComponentsPreferredWidth());
				if (!par) {
					y = lbl.getY();
				}
			}
		} catch (SQLException e) {
			WmwMessageBox.showException(e, true);
		}
	}

	private int retornaNuIntervalo() throws SQLException {
		if (LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
			return retornaDiasMaximoPagamentoCliente();
		} else {
			return pedido.getCondicaoPagamento().nuIntervaloEntrada == 0 ? pedido.getCondicaoPagamento().nuIntervaloParcelas : pedido.getCondicaoPagamento().nuIntervaloEntrada;
		}
	}

	private VencimentoAdicBoleto getVencimentoAdicionalSelecionado() {
		return (VencimentoAdicBoleto) cbVencimentoAdicional.getSelectedItem();
	}

	private Vector buscaVencimentoAdicBoleto() {
		try {
			return VencimentoAdicBoletoService.getInstance().findVencimentos(pedido.cdEmpresa, pedido.cdRepresentante, pedido.getCliente().cdVencimentoAdicBoleto);
		} catch (SQLException e) {
			WmwMessageBox.showException(e, true);
		}
		return new Vector(0);
	}

	private String getVencimentoAdicional() {
		try {
			return pedido.getCliente().cdVencimentoAdicBoleto;
		} catch (Exception e) {
			WmwMessageBox.showException(e, true);
		}
		return null;
	}

	private void getVencimentos() {
		if (LavenderePdaConfig.usaVencimentosAdicionaisBoleto) {
			if (ValueUtil.isNotEmpty(getVencimentoAdicional())) {
				Vector vencimentos = buscaVencimentoAdicBoleto();
				if (ValueUtil.isNotEmpty(vencimentos)) {
					carregaVencimentos(vencimentos);
				}
			}
		}
	}

	private void adicionaComponentesRelacionadosACheque() {
		if (cbTipoPagamento.isCheque() && LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
			String[] vlInformacoesAdicionaisPagamentoCheque = LavenderePdaConfig.usaInformacoesAdicionaisPagamentoCheque.split(";");
			int size = vlInformacoesAdicionaisPagamentoCheque.length;
			for (int i = 0; i < size; i++) {
				String vlDominioParametro = vlInformacoesAdicionaisPagamentoCheque[i];
				switch (vlDominioParametro) {
				case PagamentoPedido.CODIGO_BANCO: {
					UiUtil.add(this, lbCdBoletoConfig, edNuBanco, getLeft(), getNextY(), edNuBanco.getPreferredWidth());
					break;
				}
				case PagamentoPedido.COMPLEMENTO: {
					UiUtil.add(this, lbNuComplemento, edNuComplemento, getLeft(), getNextY(), edNuComplemento.getPreferredWidth());
					break;
				}
				case PagamentoPedido.AGENCIA: {
					UiUtil.add(this, lbNuAgencia, edNuAgencia, getLeft(), getNextY(), edNuAgencia.getPreferredWidth());
					break;
				}
				case PagamentoPedido.CONTA: {
					UiUtil.add(this, lbNuConta, edNuConta, getLeft(), getNextY(), edNuConta.getPreferredWidth());
					break;
				}
				case PagamentoPedido.CHEQUE: {
					UiUtil.add(this, lbNuCheque, edNuCheque, getLeft(), getNextY(), edNuCheque.getPreferredWidth());
					break;
				}
				case PagamentoPedido.CHEQUE_TERCEIRO: {
					UiUtil.add(this, ckChequeTerceiro, getLeft(), getNextY());
					break;
				}
				case PagamentoPedido.EMITENTE: {
					UiUtil.add(this, lbDsEmitente, edDsEmitente, getLeft(), getNextY());
					break;
				}
				case PagamentoPedido.REFERENTE_A: {
					UiUtil.add(this, lbDsReferenteCheque, edDsReferenteCheque, getLeft(), getNextY());
					break;
				}
				case PagamentoPedido.DESCRICAO_BANCO: {
					UiUtil.add(this, lbDsBanco, edDsBanco, getLeft(), getNextY());
					break;
				}
				case PagamentoPedido.DATA_CHEQUE: {
					UiUtil.add(this, lbDtCheque, edDtCheque, getLeft(), getNextY());
					break;
				}
				}
			}
		}
	}
	
	private void mostraComponentesRelacionadosAoVencimento() {
		boolean mostrarDtVencimento = cbTipoPagamento.isUsaVencimento(); 
		lbDtVencimento.setVisible(mostrarDtVencimento);
		edDtVencimento.setVisible(mostrarDtVencimento);
	}
	
	private void setDsReferenteCheque() {
		edDsReferenteCheque.setValue(cbTipoPagamento.isCheque() ? getDsReferenteCheque() : null);
	}

	private String getDsReferenteCheque() {
		return LavenderePdaConfig.usaTextoPadraoReferenteCheque ? MessageUtil.getMessage(Messages.PAGAMENTOPEDIDO_MSG_PARAMETRO_TEXTOPADRAOREFERENTECHEQUE, pedido.nuPedido) : null;
	}
	
	private void setDtVencimento() {
		edDtVencimento.setValue(cbTipoPagamento.isUsaVencimento() ? getDtVencimento() : null);
	}

	private Date getDtVencimento() {
		Date dtVencimento = DateUtil.getCurrentDate();
		if (LavenderePdaConfig.permiteAlterarVencimentoConformeCliente) {
			dtVencimento.advance(retornaDiasMaximoPagamentoCliente());	
		} else if (cbCondicaoPagamento.getNuIntervaloEntrada() != 0) {
			dtVencimento.advance(cbCondicaoPagamento.getNuIntervaloEntrada());
		} else {
			dtVencimento.advance(cbCondicaoPagamento.getNuIntervaloParcelas());
		}
		return dtVencimento;
	}
	
	private int retornaDiasMaximoPagamentoCliente() {
		try {
			return pedido.getCliente().qtDiasMaximoPagamento;
		} catch (Exception e) {
			return 0;
		}
	}
	
	private Date getDtVencimentoConformeCondPagto() {
		Date dtVencimento = DateUtil.getCurrentDate();
		dtVencimento.advance(cbCondicaoPagamento.getQtDiasMaximoPagamento());
		return dtVencimento;
	}
	

	@Override
	public void onEvent(Event event) {
		try {
			super.onEvent(event);
			switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == cbTipoPagamento) {
					cbTipoPagamentoChange();
				} else if (event.target == cbCondicaoPagamento) {
					cbCondicaoPagamentoChange();
				} else if (event.target == cbVencimentoAdicional) {
					cbVencimentoAdicionalChange();
				} else if (event.target == cbQuantidadeBoletos) {
					atualizaComponenteRelacionadosAVencimento();
				} else if (event.target == ckChequeTerceiro) {
					ckChequeTerceiroChange(ckChequeTerceiro.isChecked());
				}
				break;
			}
			case KeyEvent.KEY_PRESS: {
				actionComponents(event);
				break;
			}
			case KeyEvent.SPECIAL_KEY_PRESS: {
				actionComponents(event);
				break;
			}
			case KeyboardEvent.KEYBOARD_PRESS: {
				actionComponents(event);
				break;
			}
			case ValueChangeEvent.VALUE_CHANGE: {
				if (isEditDateVencAdicional(event.target)) {
					edDtVencAdicionalChange((EditDate) event.target);
					event.consumed = true;
				}
			}
			}
		} catch (Exception ee) {
			ExceptionUtil.handle(ee);
		}
	}

	private void cbVencimentoAdicionalChange() throws SQLException {
		VencimentoAdicBoleto adicBoleto = getVencimentoAdicionalSelecionado();
		cbQuantidadeBoletos.load(adicBoleto.nuMaxVencimentos);
		cbQuantidadeBoletos.setValue(adicBoleto.nuMaxVencimentos);
		PagamentoPedido pagamentoPedido = (PagamentoPedido)getDomain();
		pagamentoPedido.venctoPagamentoPedidos = new ArrayList<>();
		atualizaComponenteRelacionadosAVencimento();
	}

	private void actionComponents(Event event) {
		if (event.target == edVlDesconto || event.target == edVlPagamento) {
			calculaDesconto();
		} else if (event.target == edVlPagamentoBruto) {
			applyIndicePagamento();
			calculaDesconto();
		}
	}

	private void cbTipoPagamentoChange() throws SQLException {
		pedido.cdTipoPagamento = cbTipoPagamento.getValue();
		getVencAdicionalPorTipoPagamentoSelecionado();
		loadComboCondicaoPagamento();
		atualizaComponenteRelacionadosACheque();
		atualizaComponenteRelacionadosAVencimento();
		setDtVencimento();
		setVlIndicePagamento();
		mostraComponentesRelacionadosAoVencimento();
	}

	private void atualizaComponenteRelacionadosAVencimento() {
		scBase.removeAll();
		yControlStarted = false;
		addComponentsScreen();
		scBase.reposition();
	}

	private void atualizaComponenteRelacionadosACheque() {
		if (LavenderePdaConfig.isUsaInformacoesAdicionaisPagamentoCheque()) {
			limpaComponentesRelacionadosACheque();
			scBase.removeAll();
			addComponentsScreen();
			scBase.reposition();
			ckChequeTerceiroChange(false);
			setDsReferenteCheque();
		}
	}
	
	private void limpaComponentesRelacionadosACheque() {
		edNuBanco.clear();
		edNuComplemento.clear();
		edNuAgencia.clear();
		edNuConta.clear();
		edNuCheque.clear();
		ckChequeTerceiro.clear();
		edDsEmitente.clear();
		edDsReferenteCheque.clear();
		edDtVencimento.clear();
		edDsBanco.clear();
		edDtCheque.clear();
	}

	private void cbCondicaoPagamentoChange() throws SQLException {
		pedido.cdCondicaoPagamento = cbCondicaoPagamento.getValue();
		lvVlMinimo.setValue(CondicaoPagamentoService.getInstance().getQtVlMinCondicaoPagamento(pedido.cdCondicaoPagamento));
		lvPctDescMax.setValue(CondicaoPagamentoService.getInstance().getPctDescMaxCondicaoPagamento(pedido.cdCondicaoPagamento));
		setDtVencimento();
	}
	
	private void ckChequeTerceiroChange(boolean habilita) {
		edDsEmitente.setEnabled(habilita);
		edDsEmitente.setValue(null);
	}

	private void calculaDesconto() {
		if (edVlPagamento.getValueDouble() > 0 && edVlDesconto.getValueDouble() > 0) {
			lvPctDesconto.setValue(ValueUtil.round((edVlDesconto.getValueDouble() * 100) / edVlPagamento.getValueDouble()));
		} else {
			lvPctDesconto.setValue(ValueUtil.VALOR_NI);
		}
		if (edVlPagamento.getValueDouble() > 0) {
			lvVlTotal.setValue(edVlPagamento.getValueDouble() - edVlDesconto.getValueDouble());
		} else {
			lvVlTotal.setValue(ValueUtil.VALOR_NI);
		}
	}
	
	private void applyIndicePagamento() {
		if (cbTipoPagamento.getTipoPagamento() != null) {
			double vlIndicePagamento = 0d;
			if (cbTipoPagamento.getTipoPagamento().vlIndicePagamento > 0.0) {
				vlIndicePagamento = cbTipoPagamento.getTipoPagamento().vlIndicePagamento;
			}
			if (vlIndicePagamento == 0) {
				vlIndicePagamento = 1;
			}
			edVlPagamento.setValue(ValueUtil.round(edVlPagamentoBruto.getValueDouble() * vlIndicePagamento));
		}
	}

	public ListPagamentoPedidoForm getListPagamentoPedidoForm() {
		return listPagamentoPedidoForm;
	}

	public void setListPagamentoPedidoForm(ListPagamentoPedidoForm listPagamentoPedidoForm) {
		this.listPagamentoPedidoForm = listPagamentoPedidoForm;
	}
	
	public boolean isEditDateVencAdicional(Object target) {
		return target instanceof EditDate && target != edDtCheque && target != edDtVencimento;
	}
	
	public void edDtVencAdicionalChange(EditDate editDate) throws SQLException {
		Date date = editDate.getValue();
		Date dataInicial = DateUtil.getCurrentDate();
		boolean error = false;
		if (error = ValueUtil.isEmpty(date)) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.VENCTOADIC_DATA_VCTO_VAZIA, editDate.appId));
		} else if (error = date.isBefore(dataInicial)) {
			UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.VENCTOADIC_DATA_VCTO_ANTERIOR_MINIMA, editDate.appId));
		} else {
			dataInicial.advance(VencimentoAdicBoletoService.getInstance().getDataMaximaVencAdic(pedido.getCondicaoPagamento(), getVencimentoAdicionalSelecionado(), pedido.getCliente()));
			if (error = date.isAfter(dataInicial)) {
				UiUtil.showErrorMessage(MessageUtil.getMessage(Messages.VENCTOADIC_DATA_VCTO_POSTERIOR_MAXIMO, new String[] {String.valueOf(editDate.appId), DateUtil.formatDateDDMMYYYY(dataInicial)}));
			}
		}
		int index = editDate.appId - 1;
		if (error) {
			editDate.setValue(datasEntrega.get(index));
		} else {
			datasEntrega.set(index, date);
			PagamentoPedido pagamentoPedido = (PagamentoPedido) getDomain();
			if (pagamentoPedido.venctoPagamentoPedidos != null && pagamentoPedido.venctoPagamentoPedidos.size() > index) {
				pagamentoPedido.venctoPagamentoPedidos.get(index).dtVencimento = date;
			}
		}
	}
	
	private boolean usaVctoAdicional() {
		TipoPagamento tipoPagamento = getTipoPagamento();
		if (tipoPagamento == null) {
			return false;
		}
		return LavenderePdaConfig.usaVencimentosAdicionaisBoleto && ValueUtil.getBooleanValue(tipoPagamento.flBoleto) && ValueUtil.getBooleanValue(tipoPagamento.flUsaVencimento) && ValueUtil.isNotEmpty(getVencimentoAdicional()); 
	}
	
}
