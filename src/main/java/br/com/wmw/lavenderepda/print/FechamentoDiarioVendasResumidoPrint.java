package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.DepositoBancario;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiario;
import br.com.wmw.lavenderepda.business.domain.FechamentoDiarioLan;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.TipoLancamento;
import br.com.wmw.lavenderepda.business.service.DepositoBancarioService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioEstService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioLanService;
import br.com.wmw.lavenderepda.business.service.FechamentoDiarioService;
import br.com.wmw.lavenderepda.business.service.PagamentoPedidoService;
import br.com.wmw.lavenderepda.business.service.PagamentoService;
import br.com.wmw.lavenderepda.business.service.PedidoService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.sys.Settings;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.ImageException;
import totalcross.util.Date;
import totalcross.util.Vector;

import static br.com.wmw.lavenderepda.print.AbstractPedidoServicePrint.DEVICE_SCREEN_DENSITY;
import static br.com.wmw.lavenderepda.print.AbstractPedidoServicePrint.PRINT_SCREEN_DENSITY;

public class FechamentoDiarioVendasResumidoPrint extends AbstractPrint {

	private FechamentoDiario fechamentoDiario; 
	private Vector depositosBancarios;
	private Vector fechamentoDiarioLanList;
	private String[][] produtoEstoqueItems;
	private String[][] clienteItems;
	private Date dataUltimoFechamentoDiario;
			
	public FechamentoDiarioVendasResumidoPrint(FechamentoDiario fechamentoDiario, Date dataUltimoFechamentoDiario) throws Exception {
		this.fechamentoDiario = fechamentoDiario;
		this.dataUltimoFechamentoDiario = dataUltimoFechamentoDiario;
		valueParametroImpressao = LavenderePdaConfig.usaImpressaoFechamentoDiarioVendas;
		findDepositos();
		findLancamentos();
		findEstoqueProduto();
		findClientes();
		setPrinterParameters();
	}

	public void run() {
		try {
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			setFonts();
			FechamentoDiarioService.getInstance().atualizaNuImpressao(fechamentoDiario);
			createFile();
			print();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			Settings.screenDensity = DEVICE_SCREEN_DENSITY;
		}
	}

	private void createFile() throws ImageException, SQLException {
		int imageHeigth = addLineSize() * (29 + qtdItensDeposito() + qtdItensLancamento() + qtdItensEstoqueProduto() + qtdItensVeiculo() + qtdItemsCliente());
		Settings.screenDensity = PRINT_SCREEN_DENSITY;
		try {
			Graphics g = getGrafics(imageHeigth);
			fileLayout(g);
		} finally {
			Settings.screenDensity = DEVICE_SCREEN_DENSITY;
		}
	}

	private void fileLayout(Graphics g) throws SQLException {
		g.setFont(fontBold);
		
		addTitle(g);
		addEmitente(g);
		addDescricao(g);
		addDepositos(g);
		if (LavenderePdaConfig.usaTipoLancamentoDinamico()) {
			addLancamentos(g);
		}
		if (LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
			addDadosCliente(g);
		}
		if (LavenderePdaConfig.usaRelatorioEstoqueProduto()) {
			addDadosProdutoEstoque(g);
		}
		if (LavenderePdaConfig.usaPermiteInformarVeiculoFechamento()) {
			addDadosVeiculo(g);
		}
		addDadosAdicionais(g);
		addNuImpressao(g);
	}

	private void addTitle(Graphics g) {
		String titulo = Messages.FECHAMENTO_DIARIO + " - " + fechamentoDiario.dtFechamentoDiario;
		titulo = fechamentoDiario.isAgrupado() ? titulo + Messages.ACUMULADO : titulo;
		int alignCenter = getAlignCenter(titulo, IMAGEM_WIDTH);
		lineBreak();
		g.drawText(titulo, alignCenter, currentHeight);
		lineBreak();
	}
	
	private void addEmitente(Graphics g) throws SQLException {
		int x = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		x += cellWidth * 2;
		g.drawText(Messages.FECHAMENTO_DIARIO_EMITENTE, SPACE_INIT_LINE, currentHeight);
		g.setFont(font);
		
		lineBreak();
		Representante representante = getRepresentante();
		g.drawText(representante.getCdRepresentanteTxt() + " - " + representante.nmRepresentante, SPACE_INIT_LINE, currentHeight);
		String placa = TransportadoraService.getInstance().findPlacaPor(representante.cdRepresentante);
		g.drawText(Messages.FECHAMENTO_DIARIO_PLACA + StringUtil.getStringValue(placa), x, currentHeight);
		
		lineBreak();
		g.drawText(Messages.FECHAMENTO_DIARIO_DATA + fechamentoDiario.dtFinalizacao, SPACE_INIT_LINE, currentHeight);
		g.drawText(Messages.FECHAMENTO_DIARIO_HORA + fechamentoDiario.hrFinalizacao, x, currentHeight);
		g.drawRoundRect(0, addLineSize(), IMAGEM_WIDTH, addLineSize() * 3, 8);

		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
	}

	private void addDescricao(Graphics g) throws SQLException {
		addTextBold(g, Messages.FECHAMENTO_DIARIO_DESCRICAO, SPACE_INIT_LINE);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_VALOR_TOTAL, getAlignRight(Messages.FECHAMENTO_DIARIO_VALOR_TOTAL, IMAGEM_WIDTH) - SPACE_INIT_LINE);

		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 12, 8);
		
		lineBreak();
		addDrawLine(g);

		addTextBold(g, Messages.FECHAMENTO_DIARIO_TOTAL_NOTAS, SPACE_INIT_LINE);
		String totalPedidos = getTotalPedidos();
		addDrawText(g, totalPedidos, getAlignRight(totalPedidos, IMAGEM_WIDTH));
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_TOTAL_RECEITAS, SPACE_INIT_LINE);
		String totalReceitas = getTotalReceitas();
		addDrawText(g, totalReceitas, getAlignRight(totalReceitas, IMAGEM_WIDTH));

		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_DEVOLUCOES, SPACE_INIT_LINE);
		addDrawText(g, Messages.FECHAMENTO_DIARIO_FINANCEIRO_ZERO, getAlignRight(Messages.FECHAMENTO_DIARIO_FINANCEIRO_ZERO, IMAGEM_WIDTH));
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_BONIFICACAO, SPACE_INIT_LINE);
		String totalBonificacao = getTotalBonificacao();
		addDrawText(g, totalBonificacao, getAlignRight(totalBonificacao, IMAGEM_WIDTH));
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_DINHEIRO, SPACE_INIT_LINE);
		String totalDinheiro = getTotalDinheiro();
		addDrawText(g, totalDinheiro, getAlignRight(totalDinheiro, IMAGEM_WIDTH));
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_CHEQUES, SPACE_INIT_LINE);
		String totalCheque = getTotalCheque();
		addDrawText(g, getTotalCheque(), getAlignRight(totalCheque, IMAGEM_WIDTH));
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_BOLETOS, SPACE_INIT_LINE);
		String totalBoleto = getTotalBoleto();
		addDrawText(g, totalBoleto, getAlignRight(totalBoleto, IMAGEM_WIDTH));
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_CREDITO, SPACE_INIT_LINE);
		String totalCreditoCliente  = getTotalCreditoCliente();
		addDrawText(g, totalCreditoCliente, getAlignRight(totalCreditoCliente, IMAGEM_WIDTH));
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_FINANCEIRO_DESCONTOS, SPACE_INIT_LINE);
		final String totalDesconto = getTotalDescontos();
		addDrawText(g, totalDesconto, getAlignRight(totalDesconto, IMAGEM_WIDTH));	
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_OUTROS, SPACE_INIT_LINE);
		String totalOutros = getTotalOutros();
		addDrawText(g, totalOutros, getAlignRight(totalOutros, IMAGEM_WIDTH));		
		
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_COMPROVANTES, SPACE_INIT_LINE);
		String totalBoletoPago = getTotalBoletoPago();
		addDrawText(g, totalBoletoPago, getAlignRight(totalBoletoPago, IMAGEM_WIDTH));

		currentHeight += addLineSize() + SPACE_INIT_LINE + SPACE_INIT_LINE;
	}

	private void addDepositos(Graphics g) throws SQLException {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		int col2 = col1 + cellWidth;
		int col3 = IMAGEM_WIDTH;
		
		addTextBold(g, Messages.FECHAMENTO_DIARIO_DEPOSITOS, SPACE_INIT_LINE);
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * (depositosBancarios.size() + 3), 8);
		
		lineBreak();
		
		addDrawText(g, Messages.FECHAMENTO_DIARIO_DEPOSITOS_DATA, col1);
		addDrawText(g, Messages.FECHAMENTO_DIARIO_DEPOSITOS_NUMERO, col2);
		addDrawText(g, Messages.FECHAMENTO_DIARIO_DEPOSITOS_VALOR, getAlignRight(Messages.FECHAMENTO_DIARIO_DEPOSITOS_VALOR, col3));
		
		lineBreak();
		Double total = 0D;
		for (int i = 0; i < depositosBancarios.size(); i++) {
			DepositoBancario depositoBancario = (DepositoBancario) depositosBancarios.items[i];
			double valorDeposito = depositoBancario.vlTotalDepositoBancario;
			addDrawText(g, StringUtil.getStringValue(depositoBancario.dtDepositoBancario), col1);
			addDrawText(g, depositoBancario.nuDepositoBancario, col2);
			addDrawText(g, StringUtil.getStringValueToInterface(valorDeposito, 2), getAlignRight(StringUtil.getStringValueToInterface(valorDeposito, 2), col3));
			total += depositoBancario.vlTotalDepositoBancario;
			lineBreak();
		}
		addTextBold(g, Messages.FECHAMENTO_DIARIO_FINANCEIRO_TOTAL, col1);
		addTextBold(g, StringUtil.getStringValueToInterface(total, 2), getAlignRight(StringUtil.getStringValueToInterface(total, 2), col3) - SPACE_INIT_LINE);
		
		lineBreak();
		currentHeight += SPACE_INIT_LINE + SPACE_INIT_LINE;
	}

	private void addLancamentos(Graphics g) {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		int col2 = col1 + cellWidth;
		int col3 = IMAGEM_WIDTH;

		addTextBold(g, Messages.FECHAMENTO_DIARIO_LAN_PRINT_TITLE, SPACE_INIT_LINE);

		int size = fechamentoDiarioLanList.size();
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * (size + 3), 8);

		lineBreak();

		addDrawText(g, Messages.FECHAMENTO_DIARIO_LAN_PRINT_DATA, col1);
		addDrawText(g, Messages.FECHAMENTO_DIARIO_LAN_PRINT_TIPO, col2);
		addDrawText(g, Messages.FECHAMENTO_DIARIO_LAN_PRINT_VALOR, getAlignRight(Messages.FECHAMENTO_DIARIO_LAN_PRINT_VALOR, col3));

		lineBreak();
		double total = 0;
		for (int i = 0; i < size; i++) {
			FechamentoDiarioLan fechamentoDiarioLan = (FechamentoDiarioLan) fechamentoDiarioLanList.items[i];
			double valorDeposito = fechamentoDiarioLan.vlTotalLancamento;
			addDrawText(g, StringUtil.getStringValue(fechamentoDiarioLan.dtFechamentoDiario), col1);
			addDrawText(g, new TipoLancamento(fechamentoDiarioLan.cdTipoLancamento, fechamentoDiarioLan.dsTipoLancamento).toString(), col2);
			addDrawText(g, StringUtil.getStringValueToInterface(valorDeposito, 2), getAlignRight(StringUtil.getStringValueToInterface(valorDeposito, 2), col3));
			total += valorDeposito;
			lineBreak();
		}
		addTextBold(g, Messages.FECHAMENTO_DIARIO_LAN_PRINT_TOTAL, col1);
		addTextBold(g, StringUtil.getStringValueToInterface(total, 2), getAlignRight(StringUtil.getStringValueToInterface(total, 2), col3) - SPACE_INIT_LINE);

		lineBreak();
		currentHeight += SPACE_INIT_LINE + SPACE_INIT_LINE;
	}


	private void addDadosProdutoEstoque(Graphics g) {
		int cellWidth = IMAGEM_WIDTH / 5;
		int col1 = SPACE_INIT_LINE;
		int col2 = col1 + cellWidth + 50;
		int col3 = col2 + cellWidth;
		int col4 = col3 + cellWidth;
		int col5 = col4 + cellWidth;
		addTextBold(g, Messages.FECHAMENTO_DIARIO_REL_ESTOQUE_PRINT, SPACE_INIT_LINE);
		int size = produtoEstoqueItems.length;
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * (size + 2), 8);
		lineBreak();
		addTextBold(g, Messages.ITEMPEDIDO_LABEL_PRODUTO, col1);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_REMESSA, col2);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_RETORNO, col3);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_VENDIDO, col4);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_SALDO, col5);
		int spacer = 10;
		col2 += spacer;
		col3 += spacer;
		col4 += spacer;
		col5 += spacer;
		lineBreak();
		for (int i = 0; i < size - 1; i++) {
			addDrawText(g, StringUtil.getStringAbreviada(produtoEstoqueItems[i][0], col2 - spacer, font), col1);
			addDrawText(g, produtoEstoqueItems[i][1], col2);
			addDrawText(g, produtoEstoqueItems[i][2], col3);
			addDrawText(g, produtoEstoqueItems[i][3], col4);
			addDrawText(g, produtoEstoqueItems[i][4], col5);
			lineBreak();
		}
		addDrawLine(g);
		addTextBold(g, produtoEstoqueItems[size - 1][0], col1);
		addTextBold(g, produtoEstoqueItems[size - 1][1], col2);
		addTextBold(g, produtoEstoqueItems[size - 1][2], col3);
		addTextBold(g, produtoEstoqueItems[size - 1][3], col4);
		addTextBold(g, produtoEstoqueItems[size - 1][4], col5);
		lineBreak();
		currentHeight += SPACE_INIT_LINE + SPACE_INIT_LINE;
	}
	
	private void addDadosCliente(Graphics g) {
		int cellWidth = IMAGEM_WIDTH / 5;
		int col1 = SPACE_INIT_LINE;
		int col2 = col1 + cellWidth + 50;
		int col3 = col2 + cellWidth;
		int col4 = col3 + cellWidth;
		int col5 = IMAGEM_WIDTH;
		addTextBold(g, Messages.FECHAMENTO_DIARIO_REL_CLIENTE_PRINT, SPACE_INIT_LINE);
		int size = clienteItems.length;
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * (size + 2), 8);
		lineBreak();
		addTextBold(g, Messages.FECHAMENTO_DIARIO_CLIENTE, col1);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_GRID_DINHEIRO, col2);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_GRID_CHEQUE, col3);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_GRID_BOLETO, col4);
		addTextBold(g, Messages.FECHAMENTO_DIARIO_SALDO, getAlignRight(Messages.FECHAMENTO_DIARIO_SALDO, col5, true));
		int spacer = 10;
		col2 += spacer;
		col3 += spacer;
		col4 += spacer;
		lineBreak();
		for (int i = 0; i < size - 1; i++) {
			addDrawText(g, StringUtil.getStringAbreviada(clienteItems[i][0], col2 - spacer, font), col1);
			addDrawText(g, clienteItems[i][1], col2);
			addDrawText(g, clienteItems[i][2], col3);
			addDrawText(g, clienteItems[i][3], col4);
			addDrawText(g, clienteItems[i][4], getAlignRight(clienteItems[i][4], col5));
			lineBreak();
		}
		addDrawLine(g);
		addTextBold(g, clienteItems[size - 1][0], col1);
		addTextBold(g, clienteItems[size - 1][1], col2);
		addTextBold(g, clienteItems[size - 1][2], col3);
		addTextBold(g, clienteItems[size - 1][3], col4);
		addTextBold(g, clienteItems[size - 1][4], getAlignRight(clienteItems[size - 1][4], col5, true));
		lineBreak();
		currentHeight += SPACE_INIT_LINE + SPACE_INIT_LINE;
	}

	private void addDadosVeiculo(Graphics g) {
		int col1 = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 2;
		int col2 = col1 + cellWidth;
		addTextBold(g, Messages.FECHAMENTO_DIARIO_VEICULO_PRINT, SPACE_INIT_LINE);
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 3, 8);
		lineBreak();
		addDrawText(g, MessageUtil.getMessage(Messages.FECHAMENTO_DIARIO_DSPLACAVEICULO_PRINT, StringUtil.getStringValue(fechamentoDiario.dsPlacaVeiculo)), col1);
		lineBreak();
		addDrawText(g, MessageUtil.getMessage(Messages.FECHAMENTO_DIARIO_KMINICIALVEICULO_PRINT, fechamentoDiario.kmInicialVeiculo), col1);
		addDrawText(g, MessageUtil.getMessage(Messages.FECHAMENTO_DIARIO_KMFINALVEICULO_PRINT, fechamentoDiario.kmFinalVeiculo), col2);
		lineBreak();
		currentHeight += SPACE_INIT_LINE + SPACE_INIT_LINE;
	}
	
	private void addDadosAdicionais(Graphics g) {
		int x = SPACE_INIT_LINE;
		int cellWidth = IMAGEM_WIDTH / 3;
		x += cellWidth * 2;
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 12, 8);
		
		addTextBold(g, Messages.FECHAMENTO_DIARIO_DADOS_ADICIONAIS, SPACE_INIT_LINE);
		g.setFont(font);

		lineBreak();
		g.drawText(Messages.FECHAMENTO_DIARIO_DATA_DE_ENTREGA, SPACE_INIT_LINE, currentHeight);
		lineBreak();
		g.drawLine(cellWidth, currentHeight, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight);
		g.drawLine(cellWidth, currentHeight + 1, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight + 1);
		g.drawText(Messages.FECHAMENTO_DIARIO_MALOTE, SPACE_INIT_LINE, currentHeight);
		g.drawText(Messages.FECHAMENTO_DIARIO_DADOS_DATA, getAlignCenter(Messages.FECHAMENTO_DIARIO_DADOS_DATA, x), currentHeight);

		lineBreak();
		lineBreak();
		g.drawText(Messages.FECHAMENTO_DIARIO_VISTO_DO, SPACE_INIT_LINE, currentHeight);
		lineBreak();
		g.drawLine(cellWidth, currentHeight, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight);
		g.drawLine(cellWidth, currentHeight + 1, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight + 1);
		g.drawText(Messages.FECHAMENTO_DIARIO_MOTORISTA, SPACE_INIT_LINE, currentHeight);
		g.drawText(Messages.FECHAMENTO_DIARIO_ASSINATURA, getAlignCenter(Messages.FECHAMENTO_DIARIO_ASSINATURA, x), currentHeight);

		lineBreak();
		lineBreak();
		g.drawText(Messages.FECHAMENTO_DIARIO_DATA_ABERTURA, SPACE_INIT_LINE, currentHeight);
		lineBreak();
		g.drawLine(cellWidth, currentHeight, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight);
		g.drawLine(cellWidth, currentHeight + 1, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight + 1);
		g.drawText(Messages.FECHAMENTO_DIARIO_MALOTE, SPACE_INIT_LINE, currentHeight);
		g.drawText(Messages.FECHAMENTO_DIARIO_DADOS_DATA, getAlignCenter(Messages.FECHAMENTO_DIARIO_DADOS_DATA, x), currentHeight);		
		
		lineBreak();
		lineBreak();
		g.drawText(Messages.FECHAMENTO_DIARIO_VISTO_SETOR, SPACE_INIT_LINE, currentHeight);
		lineBreak();
		g.drawLine(cellWidth, currentHeight, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight);
		g.drawLine(cellWidth, currentHeight + 1, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight + 1);
		g.drawText(Messages.FECHAMENTO_DIARIO_COBRANCA, SPACE_INIT_LINE, currentHeight);
		g.drawText(Messages.FECHAMENTO_DIARIO_ASSINATURA, getAlignCenter(Messages.FECHAMENTO_DIARIO_ASSINATURA, x), currentHeight);
		
		lineBreak();
		currentHeight += SPACE_INIT_LINE + SPACE_INIT_LINE;
	}
	
	private void addNuImpressao(Graphics g) throws SQLException {
		g.drawText(Messages.FECHAMENTO_DIARIO_FINANCEIRO_VIAS + fechamentoDiario.nuImpressao, SPACE_INIT_LINE, currentHeight);
	}

	private Representante getRepresentante() throws SQLException {
		Representante representante = new Representante();
		representante.cdRepresentante = fechamentoDiario.cdRepresentante;
		representante = (Representante) RepresentanteService.getInstance().findByPrimaryKey(representante);
		return representante;
	}

	private void findEstoqueProduto() throws SQLException {
		if (LavenderePdaConfig.usaRelatorioEstoqueProduto()) {
			Vector fechamentoDiarioEstList = FechamentoDiarioEstService.getInstance().findAllByExample(FechamentoDiarioEstService.getInstance().getFilterByDate(fechamentoDiario.dtFechamentoDiario));
			produtoEstoqueItems = FechamentoDiarioEstService.getInstance().fechamentoDiarioEstListToGridList(fechamentoDiarioEstList);
		} else {
			produtoEstoqueItems = new String[0][0];
		}
	}
	
	private void findClientes() throws SQLException {
		if (LavenderePdaConfig.usaRelatorioClienteTipoPagamento()) {
			clienteItems = FechamentoDiarioService.getInstance().fechamentoDiarioClienteToGridList(PedidoService.getInstance().findSumTotalPedidoFechamentoDiarioCliente(fechamentoDiario.dtFechamentoDiario));
		} else {
			clienteItems = new String[0][0];
		}
	}

	private void findDepositos() throws SQLException {
		DepositoBancario depositoBancario = new DepositoBancario();
		depositoBancario.cdEmpresa = fechamentoDiario.cdEmpresa;
		depositoBancario.cdRepresentante = fechamentoDiario.cdRepresentante;
		depositoBancario.dtFechamentoDiario = fechamentoDiario.dtFechamentoDiario;
		depositosBancarios = DepositoBancarioService.getInstance().findAllByExample(depositoBancario);
	}

	private void findLancamentos() throws SQLException {
		if (LavenderePdaConfig.usaTipoLancamentoDinamico()) {
			fechamentoDiarioLanList = FechamentoDiarioLanService.getInstance().findAllByExample(FechamentoDiarioLanService.getInstance().getFilterByDate(fechamentoDiario.dtFechamentoDiario));
		}
	}

	private int qtdItensDeposito() throws SQLException {
		DepositoBancario depositoBancario = new DepositoBancario();
		depositoBancario.cdEmpresa = fechamentoDiario.cdEmpresa;
		depositoBancario.cdRepresentante = fechamentoDiario.cdRepresentante;
		depositoBancario.dtFechamentoDiario = fechamentoDiario.dtFechamentoDiario;
		return DepositoBancarioService.getInstance().countByExample(depositoBancario);
	}

	private int qtdItensLancamento() {
		return LavenderePdaConfig.usaTipoLancamentoDinamico() ? fechamentoDiarioLanList.size() + 3 : 0;
	}

	private int qtdItensEstoqueProduto() {
		return produtoEstoqueItems.length + 2;
	}

	private int qtdItensVeiculo() {
		return LavenderePdaConfig.usaPermiteInformarVeiculoFechamento() ? 3 : 0;
	}
	
	private int qtdItemsCliente() {
		return clienteItems.length > 0 ? clienteItems.length + 3 : 0;
	}
	
	private String getTotalBoleto() throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(pedidoSum.vlTotalBoleto, 2);
		} else {
			double total = PagamentoPedidoService.getInstance().somaBoletoPedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(total, 2);
		}
	}
	
	private String getTotalCreditoCliente() throws SQLException {
		double total = PedidoService.getInstance().somaVlTotalNotaCredito(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
		return StringUtil.getStringValueToInterface(total, 2);
	}
	
	private String getTotalDescontos() throws SQLException {
		double total = PedidoService.getInstance().somaVlTotalDesconto(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
		return StringUtil.getStringValueToInterface(total, 2);
	}

	private String getTotalBoletoPago() throws SQLException {
		double total = PagamentoService.getInstance().somaTotalBoletoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
		return StringUtil.getStringValueToInterface(total, 2);
	}
	
	private String getTotalCheque() throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(pedidoSum.vlTotalCheque, 2);
		} else {
			double total = PagamentoPedidoService.getInstance().somaChequePedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			total += PagamentoService.getInstance().somaTotalChequePorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(total, 2);
		}
	}

	private String getTotalDinheiro() throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(pedidoSum.vlTotalDinheiro, 2);
		} else {
			double total = PagamentoPedidoService.getInstance().somaDinheiroPedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			total += PagamentoService.getInstance().somaTotalDinheiroPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(total, 2);
		}
	}

	private String getTotalBonificacao() throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(pedidoSum.vlBonificacaoPedido, 2);
		} else {
			double total = PagamentoPedidoService.getInstance().somaTotalBonificacaoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(total, 2);
		}
	}

	private String getTotalReceitas() throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(pedidoSum.vlTotalPedido, 2);
		} else {
			double total = PagamentoPedidoService.getInstance().somaTotalReceitasPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(total, 2);
		}
	}
	
	private String getTotalPedidos() throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(pedidoSum.vlTotalPedido + pedidoSum.vlBonificacaoPedido, 2);
		} else {
			double total = PagamentoPedidoService.getInstance().somaTotalPedidosPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(total, 2);
		}
	}

	protected void printToFile() throws ImageException, IOException {
		String nmImagePrint = Settings.appPath + "/fechamentoDiario_" + DateUtil.formatDateYYYYMMDD(fechamentoDiario.dtFechamentoDiario) + ".png"; 
		FileUtil.deleteFile(nmImagePrint);
		File file = FileUtil.criaFile(nmImagePrint);
		img.createPng(file);
		file.close();
		UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
	}
	
	private int getAlignRight(String text, int width) {
		return (width - font.fm.stringWidth(text)) - SPACE_INIT_LINE;
	}
	
	private String getTotalOutros() throws SQLException {
		if (LavenderePdaConfig.consideraValorPedidoAtualRetornado()) {
			Pedido pedidoSum = PedidoService.getInstance().getPedidoSumFechamentoDiario(fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(pedidoSum.vlTotalPedido - pedidoSum.vlTotalDinheiro - pedidoSum.vlTotalCheque - pedidoSum.vlTotalBoleto, 2);
		} else {
			double total = PagamentoPedidoService.getInstance().somaOutrosPedidoPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			total += PagamentoService.getInstance().somaTotalOutrosPorData(fechamentoDiario.cdEmpresa, fechamentoDiario.cdRepresentante, dataUltimoFechamentoDiario, fechamentoDiario.dtFechamentoDiario);
			return StringUtil.getStringValueToInterface(total, 2);
		}
	}
}
