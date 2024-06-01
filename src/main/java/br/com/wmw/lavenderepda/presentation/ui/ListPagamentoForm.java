package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.BaseDomain;
import br.com.wmw.framework.business.service.CrudService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.presentation.ui.BaseCrudListForm;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.event.ButtonOptionsEvent;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.ButtonOptions;
import br.com.wmw.framework.presentation.ui.ext.GridColDefinition;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LabelTotalizador;
import br.com.wmw.framework.presentation.ui.ext.LabelValue;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.SessionTotalizerContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.presentation.ui.ext.tmp.PopupMenuTc;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.ZebraPrinter;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Consignacao;
import br.com.wmw.lavenderepda.business.domain.FichaFinanceira;
import br.com.wmw.lavenderepda.business.domain.ItemConsignacao;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.ConsignacaoService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.FichaFinanceiraService;
import br.com.wmw.lavenderepda.business.service.ItemConsignacaoService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.business.service.ProdutoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import br.com.wmw.lavenderepda.presentation.ui.ext.LabelContainer;
import br.com.wmw.lavenderepda.print.PagamentoPrint;
import totalcross.io.File;
import totalcross.io.device.bluetooth.DiscoveryAgent;
import totalcross.io.device.bluetooth.LocalDevice;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.util.Vector;

public class ListPagamentoForm extends BaseCrudListForm {

	private LabelValue lbVlDebtAnterior;
	private LabelValue lbVlVenda;
	private LabelValue lbVlTotal;
	private LabelName lbVlTotalPago;
	private LabelTotalizador lvVlTotalPago;
	private LabelName lbVlSaldo;
	private LabelTotalizador lvVlSaldo;
	private LabelName lbVlTotalAmortizado;
	private LabelTotalizador lvVlTotalAmortizado;
	private ButtonAction btImprimir;
	private ButtonOptions bmOpcoes;
	private double vlTotalPago = 0;
	private double vlTotal = 0;
	private double vlTotalAdicionalPago = 0; 
	private Consignacao consignacao;
	private ZebraPrinter z;
	private SessionTotalizerContainer sessaoTotalizadores;

    public ListPagamentoForm() throws SQLException {
        super(Messages.PAGAMENTO_LISTA_PAGAMENTOS);
        setBaseCrudCadForm(new CadPagamentoForm());
        singleClickOn = true;
		sessaoTotalizadores = new SessionTotalizerContainer();
    	lbVlDebtAnterior = new LabelValue("@@@@@@");
    	lbVlVenda = new LabelValue("@@@@@@");
    	lbVlTotal = new LabelValue("@@@@@@");
    	lbVlTotalPago = new LabelName(Messages.PAGAMENTO_TOTAL_PAGO);
    	lvVlTotalPago = new LabelTotalizador("@@@@@@");
    	lbVlSaldo = new LabelName(Messages.CONTACORRENTECLI_LABEL_VLSALDOFINAL);
    	lvVlSaldo = new LabelTotalizador("@@@@@@");
    	lbVlTotalAmortizado = new LabelName(Messages.PAGAMENTO_TOTAL_ADICIONAL_PAGO);
    	lvVlTotalAmortizado =  new LabelTotalizador("@@@@@@");
		btImprimir = new ButtonAction(Messages.BOTAO_IMPRESSAO, "images/impressao.png");
		bmOpcoes = new ButtonOptions();
		consignacao = ConsignacaoService.getInstance().findConsignacaoFechadaByCdCliente(SessionLavenderePda.getCliente().cdCliente);
    }

    //@Override
    protected CrudService getCrudService() throws SQLException {
        return PagamentoService.getInstance();
    }

    //@Override
    protected Vector getDomainList() throws java.sql.SQLException {
        return PagamentoService.getInstance().findAllNotSentByExample();
    }

    //@Override
    protected String[] getItem(Object domain) throws SQLException {
        Pagamento pagamento = (Pagamento) domain;
        String dsTipoPagamento = TipoPagamentoService.getInstance().getDsTipoPagamento(pagamento.cdTipoPagamento);
        vlTotalPago += pagamento.vlPago;
        vlTotalAdicionalPago += pagamento.vlAdicionalPago;
        String[] item = {
            pagamento.rowKey,
            StringUtil.getStringValue(pagamento.dtPagamento),
            dsTipoPagamento == null ? StringUtil.getStringValue(pagamento.cdTipoPagamento) : StringUtil.getStringValue(dsTipoPagamento),
            StringUtil.getStringValueToInterface(pagamento.vlPago + pagamento.vlAdicionalPago),
            StringUtil.getStringValue(pagamento.dsObservacao)};
        return item;
    }

    private void refreshDomainToScreen() {
    	lbVlTotal.setValue(vlTotal);
    	lvVlTotalPago.setValue(vlTotalPago + vlTotalAdicionalPago);
    	lbVlTotalPago.setText(Messages.PAGAMENTO_TOTAL_PAGO + " " + lvVlTotalPago.getValue());
    	lvVlSaldo.setValue(vlTotal - vlTotalPago);
    	lbVlSaldo.setText(Messages.CONTACORRENTECLI_LABEL_VLSALDOFINAL + " " + lvVlSaldo.getValue());
    	lvVlTotalAmortizado.setValue(vlTotalPago);
    	lbVlTotalAmortizado.setText(Messages.PAGAMENTO_TOTAL_ADICIONAL_PAGO + " " + lvVlTotalAmortizado.getValue());
    	//--
    	lbVlTotalPago.reposition();
    	lvVlTotalPago.reposition();
    	lbVlTotalAmortizado.reposition();
    	lvVlTotalAmortizado.reposition();
    	lvVlSaldo.reposition();
    	lbVlSaldo.reposition();
    }

    public void list() throws SQLException {
    	limpaTotalizadores();
    	super.list();
    	refreshDomainToScreen();
    }

    private void limpaTotalizadores() {
    	vlTotalPago = 0;
    	vlTotalAdicionalPago = 0;
	}

	protected BaseDomain getDomainFilter() throws SQLException {
    	return new Pagamento();
    }

    //@Override
    protected String getSelectedRowKey() throws SQLException {
        String[] item = gridEdit.getSelectedItem();
        return item[0];
    }

    //@Override
    protected void onFormStart() throws SQLException {
    	LabelContainer clienteContainer = new LabelContainer(SessionLavenderePda.getCliente().toString());
    	UiUtil.add(this, clienteContainer, LEFT, getTop(), FILL, LabelContainer.getStaticHeight());
    	UiUtil.add(this, new LabelName(Messages.PAGAMENTO_DEBITO_ANTERIOR), BaseUIForm.CENTEREDLABEL, AFTER + WIDTH_GAP);
    	UiUtil.add(this, lbVlDebtAnterior , AFTER + WIDTH_GAP_BIG, SAME);
    	UiUtil.add(this, new LabelName(Messages.PAGAMENTO_VALOR_VENDA), BaseUIForm.CENTEREDLABEL, AFTER + HEIGHT_GAP);
    	UiUtil.add(this, lbVlVenda, AFTER + WIDTH_GAP_BIG, SAME);
    	UiUtil.add(this, new LabelName(Messages.PAGAMENTO_TOTAL_DEBTOS), BaseUIForm.CENTEREDLABEL , AFTER + HEIGHT_GAP);
    	UiUtil.add(this, lbVlTotal , AFTER + WIDTH_GAP_BIG , SAME);
    	//--
        GridColDefinition[] gridColDefiniton = {
            new GridColDefinition(FrameworkMessages.CAMPO_ID, 0, LEFT),
            new GridColDefinition(Messages.PAGAMENTO_LABEL_DTPAGAMENTO, -23, LEFT),
            new GridColDefinition(Messages.TIPOPAGTO_LABEL_TIPOPAGTO, -27, LEFT),
            new GridColDefinition(Messages.LABEL_VALOR, -20, LEFT),
            new GridColDefinition(Messages.OBSERVACAO_LABEL, -30, LEFT)};
        gridEdit = UiUtil.createGridEdit(gridColDefiniton);
        UiUtil.add(this, gridEdit);
        gridEdit.setRect(LEFT, AFTER + HEIGHT_GAP_BIG, FILL, FILL - barBottomContainer.getHeight() - UiUtil.getLabelPreferredHeight());
        //--
        UiUtil.add(this, sessaoTotalizadores, LEFT, AFTER, FILL, UiUtil.getLabelPreferredHeight());
        UiUtil.add(sessaoTotalizadores, lbVlTotalPago, LEFT, CENTER, PREFERRED);
        UiUtil.add(sessaoTotalizadores, lbVlSaldo, RIGHT, CENTER, PREFERRED);
        if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
        	UiUtil.add(sessaoTotalizadores, lbVlTotalAmortizado, CENTER , CENTER, PREFERRED);
        }
        
        UiUtil.add(barBottomContainer, btNovo, 5);
        if (!"N".equals(LavenderePdaConfig.usaImpressaoModuloPagamento.get(0))) {
        UiUtil.add(barBottomContainer, btImprimir , 1);
        }
        calcDebitos();
        addItensOnButtonMenu();
    	UiUtil.add(barBottomContainer, bmOpcoes, 3);
    }

	private void calcDebitos() throws SQLException {
		FichaFinanceira fichaFinanceira = FichaFinanceiraService.getInstance().getFichaFinanceira(SessionLavenderePda.getCliente());
        fichaFinanceira.getTitulos();
        double vlPedidoConsignacao = 0;
        if (consignacao != null) {
        	Vector itemConsignacaoList = ItemConsignacaoService.getInstance().findItemConsignacaoList(consignacao);
        	int size = itemConsignacaoList.size();
        	for (int i = 0; i < size; i++) {
        		ItemConsignacao itemConsignacao = (ItemConsignacao)itemConsignacaoList.items[i];
        		vlPedidoConsignacao += itemConsignacao.getQtItemVenda() * itemConsignacao.vlItem;
        	}
        }
        Pagamento pagamento = new Pagamento();
        pagamento.cdCliente = SessionLavenderePda.getCliente().cdCliente;
    	pagamento.cdEmpresa = SessionLavenderePda.cdEmpresa;
    	pagamento.cdRepresentante = SessionLavenderePda.usuarioPdaRep.cdRepresentante;
    	pagamento.flTipoAlteracao = BaseDomain.FLTIPOALTERACAO_ALTERADO;
    	double vlTotalPagamentos = PagamentoService.getInstance().sumByExample(pagamento, "VLPAGO");
    	double vlTotalPedidosAberto = 0;
    	if (!LavenderePdaConfig.usaMultiplosPagamentosParaPedido) {
    		vlTotalPedidosAberto = FichaFinanceiraService.getInstance().getValuesPedidos(SessionLavenderePda.getCliente());
    	}
        double vlTotalTitulosAberto = fichaFinanceira.vlTotalAberto;
        double vlTotalDebitoAnterior = (vlTotalTitulosAberto + (vlTotalPedidosAberto - vlPedidoConsignacao)) - vlTotalPagamentos;
    	lbVlDebtAnterior.setValue(vlTotalDebitoAnterior);
    	lbVlVenda.setValue(vlPedidoConsignacao);
        vlTotal = vlTotalDebitoAnterior + vlPedidoConsignacao;
	}

    //@Override
    protected void onFormEvent(Event event) throws SQLException {
    	switch (event.type) {
			case ControlEvent.PRESSED : {
				if (event.target == btImprimir) {
					btImprimirClick();
				}
				break;
			}
	    	case ButtonOptionsEvent.OPTION_PRESS: {
	    		if (event.target == bmOpcoes) {
	    			if (bmOpcoes.selectedItem.equals(Messages.PAGAMENTO_BOTAO_RELATORIO_PAGAMENTOS)) {
	    				(new RelPagamentosWindow()).popup();
	    			}
	    		}
	    		break;
	    	}
		}
    }

    private void btImprimirClick() throws SQLException {
    	btImprimir.setEnabled(false);
    	Cliente cliente = getCliente();
    	if ("1".equals(LavenderePdaConfig.usaImpressaoModuloPagamento.get(0))) {
    		layoutImpressao1();
    		return;
    	}
    	new PagamentoPrint(cliente, getDomainList(), lbVlDebtAnterior.getText(), lvVlTotalPago.getText(), lvVlTotalAmortizado.getText(), lvVlSaldo.getText()).run();
    	btImprimir.setEnabled(true);
    }

	private Cliente getCliente() throws SQLException {
		return ClienteService.getInstance().getCliente(SessionLavenderePda.cdEmpresa, SessionLavenderePda.usuarioPdaRep.cdRepresentante, SessionLavenderePda.getCliente().cdCliente);
	}

	private void layoutImpressao1() throws SQLException {
    	LoadingBoxWindow pb = UiUtil.createProcessingMessage();
    	pb.popupNonBlocking();
    	try {
	    	int size = 0;
	    	Vector itemConsignacaoList = new Vector(0);
	    	if (consignacao != null) {
	    		itemConsignacaoList = ItemConsignacaoService.getInstance().findItemConsignacaoList(consignacao);
	    	}
	    	size = itemConsignacaoList.size();
	    	Vector pagamentosList = gridEdit.getItemsVector();
	    	try {
	    		BluetoothUtil.turOnBluetooth();
				int initialX = 5;
				int x = initialX;
				int y = 0;
				int spaceBetweenLines = 5;
				int spaceFinal = 45;
				Font bigFont = Font.getFont("TcFont", true, 23);
				Font fontTable = Font.getFont("TcFont", true, 16);
				Font smallFont = Font.getFont("TcFont", true, 18);
				int heigthImage = bigFont.fm.height + (smallFont.fm.height * (17 + pagamentosList.size())) + (spaceBetweenLines * 21);
				if (LavenderePdaConfig.usaModuloConsignacao) {
					heigthImage += (fontTable.fm.height * (1 + size)) + (spaceBetweenLines * 2);
				}
				int widthImage = 400;
				MonoImage mi = new MonoImage(widthImage + initialX, heigthImage + spaceFinal);
				Graphics g = mi.getGraphics();
				g.backColor = Color.WHITE;
				g.fillRect(0, y, widthImage + initialX, heigthImage + spaceFinal);
				g.foreColor = Color.BLACK;
				g.drawRect(x, y, widthImage, heigthImage);
				//--
				String titulo = EmpresaService.getInstance().getEmpresaName(SessionLavenderePda.cdEmpresa);
				y += spaceBetweenLines;
				x = initialX + ((widthImage / 2) - (bigFont.fm.stringWidth(titulo) / 2)) + 3;
				g.setFont(bigFont);
				g.drawText(titulo, x, y);
				//--
				Cliente cliente = getCliente();
				y += bigFont.fm.height + (spaceBetweenLines * 2);
				x = initialX + spaceBetweenLines;
				g.setFont(smallFont);
				g.drawText(Messages.CLIENTE_LABEL_NMFANTASIA_LISTA + ": " + StringUtil.getStringValue(cliente.nmFantasia), x, y);
				y += smallFont.fm.height;
				g.drawText(Messages.CLIENTE_LABEL_NMRAZAOSOCIAL + ": " + StringUtil.getStringValue(cliente.nmRazaoSocial), x, y);
				y += smallFont.fm.height;
				g.drawText(Messages.CLIENTE_LABEL_NUCNPJ + ": " + StringUtil.getStringValue(cliente.nuCnpj), x, y);
				y += smallFont.fm.height;
				g.drawText(Messages.DATA_LABEL_DATA + ": " + DateUtil.getCurrentDate(), x, y);
				//--
				int widthCelula = (widthImage - (spaceBetweenLines * 2)) / 6;
				g.setFont(fontTable);
				y += fontTable.fm.height + (spaceBetweenLines * 2);
				x = initialX + spaceBetweenLines;
				if (LavenderePdaConfig.usaModuloConsignacao && (size > 0)) {
					g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
					g.drawText(Messages.CONSIGNACAO_IMPRESSAO_QUANTIDADE, x + 2, y);
					x += widthCelula;
					g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
					g.drawText(Messages.PRODUTO_NOME_ENTIDADE, x + 2, y);
					x += widthCelula;
					g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
					g.drawText(Messages.CONSIGNACAO_IMPRESSAO_TOTAL, x + 2, y);
					x += widthCelula;
					g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
					g.drawText(Messages.CONSIGNACAO_QTDADE_SOBRA, x + 2, y);
					x += widthCelula;
					g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
					g.drawText(Messages.CONSIGNACAO_QT_VENDA, x + 2, y);
					x += widthCelula;
					g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
					g.drawText(Messages.CONSIGNACAO_TOT_RECEBER, x + 2, y);
					//--
					y += fontTable.fm.height - 1;
					x = initialX + spaceBetweenLines;
					for (int i = 0; i < size; i++) {
						ItemConsignacao itemConsignacao = (ItemConsignacao)itemConsignacaoList.items[i];
						g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
						g.drawText(StringUtil.getStringValueToInterface(itemConsignacao.qtItemConsignado), x + 2, y);
						x += widthCelula;
						g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
						String dsProduto = ProdutoService.getInstance().getDsProduto(itemConsignacao.cdProduto);
						if ((fontTable.fm.stringWidth(dsProduto) > widthCelula) && (dsProduto.length() > 7)) {
							dsProduto = dsProduto.substring(0, 7) + ".";
						}
						g.drawText(dsProduto, x + 2, y);
						x += widthCelula;
						g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
						g.drawText(StringUtil.getStringValueToInterface(itemConsignacao.qtItemConsignado * itemConsignacao.vlItem), x + 2, y);
						x += widthCelula;
						g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
						g.drawText(StringUtil.getStringValueToInterface(itemConsignacao.qtItemSobra), x + 2, y);
						x += widthCelula;
						g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
						g.drawText(StringUtil.getStringValueToInterface(itemConsignacao.qtItemConsignado - itemConsignacao.qtItemSobra), x + 2, y);
						x += widthCelula;
						g.drawRect(x, y, widthCelula + 1, fontTable.fm.height);
						g.drawText(StringUtil.getStringValueToInterface((itemConsignacao.qtItemConsignado - itemConsignacao.qtItemSobra) * itemConsignacao.vlItem), x + 2, y);
						y += fontTable.fm.height - 1;
						x = initialX + spaceBetweenLines;
					}
					y += spaceBetweenLines;
				}
				//--
				x = initialX + spaceBetweenLines;
				g.setFont(smallFont);
				g.drawRect(x, y, (widthCelula * 5) + 1, smallFont.fm.height);
				g.drawText(Messages.CONSIGNACAO_IMPRESSAO_TOTAL_RECEBER, x + 2, y);
				x += widthCelula * 5;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(lbVlVenda.getText(), x + 2, y);
				y += smallFont.fm.height - 1;
				x = initialX + spaceBetweenLines;
				g.drawRect(x, y, (widthCelula * 5) + 1, smallFont.fm.height);
				g.drawText(Messages.CONSIGNACAO_IMPRESSAO_DEBITO_ANTERIOR, x + 2, y);
				x += widthCelula * 5;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(lbVlDebtAnterior.getText(), x + 2, y);
				y += smallFont.fm.height - 1;
				x = initialX + spaceBetweenLines;
				g.drawRect(x, y, (widthCelula * 5) + 1, smallFont.fm.height);
				g.drawText(Messages.CONSIGNACAO_IMPRESSAO_TOTAL_GERAL, x + 2, y);
				x += widthCelula * 5;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(lbVlTotal.getText(), x + 2, y);
				//--
				y += smallFont.fm.height + (spaceBetweenLines * 3);
				x = initialX + (spaceBetweenLines * 3);
				widthCelula = (widthImage - (spaceBetweenLines * 6)) / 2;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(Messages.TIPOPAGTO_LABEL_TIPOPAGTO, x + 2, y);
				x += widthCelula;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(Messages.LABEL_VALOR, x + 2, y);
				//--
				y += smallFont.fm.height - 1;
				x = initialX + (spaceBetweenLines * 3);
				for (int i = 0; i < pagamentosList.size(); i++) {
					String[] pagamento = (String[])pagamentosList.items[i];
					g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
					g.drawText(pagamento[2], x + 2, y);
					x += widthCelula;
					g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
					g.drawText(pagamento[3], x + 2, y);
					y += smallFont.fm.height - 1;
					x = initialX + (spaceBetweenLines * 3);
				}
				//--
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(Messages.PAGAMENTO_TOTAL_PAGO, x + 2, y);
				x += widthCelula;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(lvVlTotalPago.getText(), x + 2, y);
				//--
				widthCelula = (widthImage - (spaceBetweenLines * 2)) / 2;
				y += smallFont.fm.height + (spaceBetweenLines * 2);
				x = initialX + spaceBetweenLines;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(Messages.PAGAMENTO_DEBITO_VALOR_ABERTO, x + 2, y);
				x += widthCelula;
				g.drawRect(x, y, widthCelula + 1, smallFont.fm.height);
				g.drawText(lvVlSaldo.getText(), x + 2, y);
				//--
				y += smallFont.fm.height + (spaceBetweenLines * 2);
				x = initialX + spaceBetweenLines;
				g.drawText(Messages.DECLARO_QUE_RECEBI_VALOR + Messages.PRODUTO_LABEL_RS + lvVlTotalPago.getText(), x, y);
				y += smallFont.fm.height;
				g.drawText(Messages.REFERENTE_DEBITOS, x, y);
				y += smallFont.fm.height;
				g.drawText(Messages.EM_ABERTO + Messages.PRODUTO_LABEL_RS + lvVlSaldo.getText() + Messages.NAO_QUITADO, x, y);
				//--
				y += smallFont.fm.height + (spaceBetweenLines * 5);
				String lineAss = "__________________________";
				x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(lineAss) / 2)) + 3;
				g.drawText(lineAss, x, y);
				y += smallFont.fm.height;
				String dsRep = RepresentanteService.getInstance().getDescription(SessionLavenderePda.usuarioPdaRep.cdRepresentante);
				x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(dsRep) / 2)) + 3;
				g.drawText(dsRep, x, y);
				//--
				y += smallFont.fm.height + (spaceBetweenLines * 5);
				x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(lineAss) / 2)) + 3;
				g.drawText(lineAss, x, y);
				y += smallFont.fm.height;
				x = initialX + ((widthImage / 2) - (smallFont.fm.stringWidth(StringUtil.getStringValue(cliente.nmRazaoSocial)) / 2)) + 3;
				g.drawText(StringUtil.getStringValue(cliente.nmRazaoSocial), x, y);
				//-- Para TESTES
				if (VmUtil.isSimulador()) {
					
					
					
					String nmImagePrint = Settings.appPath + "/reciboPagamento.png"; 
					FileUtil.deleteFile(nmImagePrint);
					File f = FileUtil.criaFile(nmImagePrint);
					mi.createPng(f);
					f.close();
					UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
				} else {
					RemoteDevice[] rd = LocalDevice.getLocalDevice().getDiscoveryAgent().retrieveDevices(DiscoveryAgent.PREKNOWN);
					if (rd == null) {
						UiUtil.showErrorMessage(Messages.CONSIGNACAO_IMPRESSAO_SEM_DISPOSITIVOS);
						return;
					}
					String[] itens = new String[rd.length];
					for (int i = 0; i < itens.length; i++) {
						itens[i] = rd[i].getBluetoothAddress();
					}
					PopupMenuTc p = new PopupMenuTc(Messages.CONSIGNACAO_IMPRESSAO_DISPOSITIVOS, itens);
					p.popup();
					if (p.getSelectedIndex() != -1) {
						RemoteDevice rdEscolhido = rd[p.getSelectedIndex()];
						z = new ZebraPrinter(rdEscolhido.getBluetoothAddress());
						mi.printTo(z);
						LoadingBoxWindow progressBox = UiUtil.createProcessingMessage();
						progressBox.popupNonBlocking();
						Vm.safeSleep(6000);
						progressBox.unpop();
					}
				}
			} catch (Throwable e) {
				UiUtil.showErrorMessage(Messages.PAGAMENTO_IMPRESSORA_ERRO + e.getMessage());
			}
    	} finally {
			pb.unpop();
		}
    }

    public Consignacao getConsignacao() {
    	return consignacao;
    }

	public void onFormClose() throws SQLException {
		try {
			z.close(); 
		} catch (Throwable e) {
			//--
		}
		super.onFormClose();
	}

    private void addItensOnButtonMenu() {
    	bmOpcoes.removeAll();
    	bmOpcoes.addItem(Messages.PAGAMENTO_BOTAO_RELATORIO_PAGAMENTOS);
    }

}