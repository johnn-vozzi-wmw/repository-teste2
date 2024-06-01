package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.TimeUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Pagamento;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
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

public class PagamentoPrint extends AbstractPrint{

	private static final int QT_CARACTERES_LINHA = 51;
	
	private Cliente cliente;
	private Vector pagamentosList;
	private String debitoAnterior, valorPago, vlTotalAdicional, saldo;
	private int nuViaImpressa, qtdCopias;
	
	public PagamentoPrint(Cliente cliente, Vector pagamentosList, String debitoAnterior, String valorPago, String vlTotalAdicional, String saldo) {
		try {
			this.valueParametroImpressao = LavenderePdaConfig.usaImpressaoModuloPagamento.get(0);
			this.cliente = cliente;
			this.debitoAnterior = debitoAnterior;
			this.valorPago = valorPago;
			this.vlTotalAdicional = vlTotalAdicional;
			this.saldo = saldo;
			this.pagamentosList = pagamentosList;
			setPrinterParameters();
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		}
	}

	public void run() {
		try {
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			setFonts();
			qtdCopias = LavenderePdaConfig.qtdCopiasImpressaoModuloPagamento();
			int imageHeigth = addLineSize() * (29 + qtdLinhasObs() + pagamentosList.size());
			for (int i = 0; i < qtdCopias; i++) {
				nuViaImpressa = (i+1);
				createFile(imageHeigth);
				print();
				currentHeight = 0;
			}
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
		} finally {
			Settings.screenDensity = DEVICE_SCREEN_DENSITY;
		}
	}

	private void createFile(int imageHeigth ) throws ImageException, SQLException {
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
		addCliente(g);
		addDadosFinanceiros(g);
		addPagamentos(g);
		addObservacao(g);
		addAssinatura(g);
	}

	private void addTitle(Graphics g) {
		String nuVia = qtdCopias > 1 ? "(" + nuViaImpressa + Messages.RELATORIO_PAGAMENTO_NUMERO_VIA + ")" : "";
		String titulo = Messages.RELATORIO_PAGAMENTO_TITULO + " - " + DateUtil.formatDateDDMMYYYY(new Date()) + " " + nuVia;
		int alignCenter = getAlignCenter(titulo, IMAGEM_WIDTH);
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
		Representante representante = (Representante) RepresentanteService.getInstance().findByPrimaryKey(SessionLavenderePda.getRepresentante());
		representante = representante != null ? representante : SessionLavenderePda.getRepresentante();
		g.drawText(representante.getCdRepresentanteTxt() + " - " + representante.nmRepresentante, SPACE_INIT_LINE, currentHeight);
		String placa = TransportadoraService.getInstance().findPlacaPor(representante.cdRepresentante);
		g.drawText(Messages.FECHAMENTO_DIARIO_PLACA + StringUtil.getStringValue(placa), x, currentHeight);
		
		lineBreak();
		g.drawText(Messages.FECHAMENTO_DIARIO_DATA + DateUtil.formatDateDDMMYYYY(new Date()), SPACE_INIT_LINE, currentHeight);
		g.drawText(Messages.FECHAMENTO_DIARIO_HORA + TimeUtil.getCurrentTimeHHMMSS(), x, currentHeight);
		g.drawRoundRect(0, addLineSize(), IMAGEM_WIDTH, addLineSize() * 3, 8);

		currentHeight += addLineSize() + SPACE_INIT_LINE  + SPACE_INIT_LINE;
	}
	

	private void addCliente(Graphics g) {
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * 7, 8);

		addTextBold(g, Messages.RELATORIO_PAGAMENTO_CLIENTE, SPACE_INIT_LINE);
		
		lineBreak();
		String nmCliente = cliente.getNmClienteImpressao();
		addDrawText(g, nmCliente, SPACE_INIT_LINE);
		
		lineBreak();
		String logradouro = cliente.dsLogradouroComercial;
		addDrawText(g, logradouro, SPACE_INIT_LINE);

		lineBreak();
		String endereco = cliente.dsBairroComercial + " - " + cliente.dsCidadeComercial + " - " + cliente.dsEstadoComercial; 
		addDrawText(g, endereco, SPACE_INIT_LINE);

		lineBreak();
		String cepTelefone = Messages.RELATORIO_PAGAMENTO_CEP + cliente.dsCepComercial + " - " + Messages.RELATORIO_PAGAMENTO_TEL + cliente.nuFone; 
		addDrawText(g, cepTelefone, SPACE_INIT_LINE);

		lineBreak();
		String cnpj = Messages.RELATORIO_PAGAMENTO_CNPJ + cliente.nuCnpj; 
		addDrawText(g, cnpj, SPACE_INIT_LINE);

		lineBreak();
		String inscricaoEstadual = Messages.RELATORIO_PAGAMENTO_IE + cliente.nuInscricaoEstadual; 
		addDrawText(g, inscricaoEstadual, SPACE_INIT_LINE);

		currentHeight += addLineSize() + SPACE_INIT_LINE + SPACE_INIT_LINE;
	}

	private void addDadosFinanceiros(Graphics g) {
		int multiplicador = LavenderePdaConfig.isUsaModuloPagamentoComAdicional() ? 5 : 4; 
		
		g.drawRoundRect(0, currentHeight, IMAGEM_WIDTH, addLineSize() * multiplicador, 8);

		addTextBold(g, Messages.IMPRESSAONFE_DADOS_FINANCEIROS, SPACE_INIT_LINE);
		
		lineBreak();
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_VALOR_PAGO, SPACE_INIT_LINE);
		addDrawText(g, valorPago, getAlignRight(valorPago, IMAGEM_WIDTH, false));
		
		lineBreak();
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_DEBITO_ANTERIOR, SPACE_INIT_LINE);
		addDrawText(g, debitoAnterior, getAlignRight(debitoAnterior, IMAGEM_WIDTH, false));
		
		if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
			lineBreak();
			addDrawText(g, Messages.PAGAMENTO_TOTAL_ADICIONAL_PAGO.toUpperCase(), SPACE_INIT_LINE);
			addDrawText(g, vlTotalAdicional, getAlignRight(vlTotalAdicional, IMAGEM_WIDTH, false));
		}
		
		lineBreak();
		addTextBold(g, Messages.RELATORIO_PAGAMENTO_SALDO, SPACE_INIT_LINE);
		addDrawText(g, saldo, getAlignRight(saldo, IMAGEM_WIDTH, false));
		
		currentHeight += addLineSize() + SPACE_INIT_LINE + SPACE_INIT_LINE;
	}

	private void addPagamentos(Graphics g) throws SQLException {
		addTextBold(g, Messages.RELATORIO_PAGAMENTO, SPACE_INIT_LINE);
		double totalPagamentos = 0;
		double totalSemAdicionalPago = 0;
		double totalAdicionalPago = 0;
		int posicaoInicial = currentHeight;
		lineBreak();
		
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_TIPO, SPACE_INIT_LINE);
		int cellWidth = IMAGEM_WIDTH / 4;
		desenhaCabecalho(g, cellWidth);
		for (int i = 0; i < pagamentosList.size(); i++) {
			Pagamento pagamento = (Pagamento)pagamentosList.items[i];
			double vlTotalPago = pagamento.vlPago + pagamento.vlAdicionalPago;
			lineBreak();
			addDrawText(g, getTipoPagamento(pagamento), SPACE_INIT_LINE);
			if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
				addDrawText(g, StringUtil.getStringValueToInterface(pagamento.vlPago), cellWidth * 2 + 50 - font.fm.stringWidth(StringUtil.getStringValueToInterface(pagamento.vlPago)));
				addDrawText(g, StringUtil.getStringValueToInterface(pagamento.vlAdicionalPago), (cellWidth * 3) + 30 - (font.fm.stringWidth(StringUtil.getStringValueToInterface(pagamento.vlAdicionalPago))));
			} 
			addDrawText(g, StringUtil.getStringValueToInterface(vlTotalPago), getAlignRight(StringUtil.getStringValueToInterface(vlTotalPago), IMAGEM_WIDTH, false));
			if (ValueUtil.isNotEmpty(pagamento.dsObservacao)) {
				montaObservacao(g, pagamento.dsObservacao);
			} 
			
			totalPagamentos += vlTotalPago;
			totalSemAdicionalPago += pagamento.vlPago;
			totalAdicionalPago += pagamento.vlAdicionalPago;
		}
		desenhaTotalizadores(g, totalPagamentos, totalSemAdicionalPago, totalAdicionalPago, cellWidth);
        lineBreak();
		g.drawRoundRect(0, posicaoInicial, IMAGEM_WIDTH, currentHeight - posicaoInicial , 8);
		
	}

	private void desenhaTotalizadores(Graphics g, double totalPagamentos, double totalSemAdicionalPago, double totalAdicionalPago, int cellWidth) {
		lineBreak();
		addTextBold(g, Messages.RELATORIO_PAGAMENTO_TOTAL, SPACE_INIT_LINE);
		if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
			addTextBold(g, StringUtil.getStringValueToInterface(totalSemAdicionalPago), cellWidth * 2 + 50 - font.fm.stringWidth(StringUtil.getStringValueToInterface(totalSemAdicionalPago)));
			addTextBold(g, StringUtil.getStringValueToInterface(totalAdicionalPago), (cellWidth * 3) + 30 - (font.fm.stringWidth(StringUtil.getStringValueToInterface(totalAdicionalPago))));
		}
		addTextBold(g, StringUtil.getStringValueToInterface(totalPagamentos), getAlignRight(StringUtil.getStringValueToInterface(totalPagamentos), IMAGEM_WIDTH, true));
	}

	private void desenhaCabecalho(Graphics g, int cellWidth) {
		if (LavenderePdaConfig.isUsaModuloPagamentoComAdicional()) {
			addDrawText(g,Messages.RELATORIO_PAGAMENTO_VALOR, cellWidth * 2  + 50 - font.fm.stringWidth(Messages.RELATORIO_PAGAMENTO_VALOR));
			addDrawText(g, Messages.LABEL_ADICIONAL, (cellWidth*3) + 30 - font.fm.stringWidth(Messages.LABEL_ADICIONAL));
		} 
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_TOTAL_SEM_ADICIONAL, getAlignRight(Messages.RELATORIO_PAGAMENTO_TOTAL_SEM_ADICIONAL, IMAGEM_WIDTH, false));
	}

	private void addObservacao(Graphics g) {
		lineBreak();
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_TEXTO_COMPLEMENTAR, SPACE_INIT_LINE);
		lineBreak();
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_TEXTO_COMPLEMENTAR_2, SPACE_INIT_LINE);
		lineBreak();
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_TEXTO_COMPLEMENTAR_3, SPACE_INIT_LINE);
	}

	private void addAssinatura(Graphics g) {
		lineBreak();
		lineBreak();
		lineBreak();
		lineBreak();
		addDrawLine(g);
		addDrawText(g, Messages.RELATORIO_PAGAMENTO_ASSINATURA, getAlignCenter(Messages.RELATORIO_PAGAMENTO_ASSINATURA, IMAGEM_WIDTH));
	}
	
	public void montaObservacao(Graphics g, String observacao) {
		String[] textoSplit =  (Messages.RELATORIO_PAGAMENTO_OBSERVACAO + " " + observacao).split(" ");
		String textoImpresso = "";
		for (String palavra : textoSplit) {
			if ((textoImpresso + palavra).length() > QT_CARACTERES_LINHA) {
				lineBreak();
				addDrawText(g, textoImpresso, SPACE_INIT_LINE);
				textoImpresso = palavra + " ";
			} else {
				textoImpresso += palavra + " ";
			}
		}
		lineBreak();
		addDrawText(g, textoImpresso, SPACE_INIT_LINE);
	}
	
	public int qtdLinhasObs() {
		int qtdLinhas = 0;
		for (int i = 0; i < pagamentosList.size(); i++) {
			Pagamento pagamento = (Pagamento)pagamentosList.items[i];
			qtdLinhas += pagamento.dsObservacao.length()/QT_CARACTERES_LINHA;
		}	
		return qtdLinhas;
	}
	
	private String getTipoPagamento(Pagamento pagamento) throws SQLException {
		TipoPagamento tipoPagamento = new TipoPagamento();
		tipoPagamento.cdEmpresa = pagamento.cdEmpresa;
		tipoPagamento.cdRepresentante = pagamento.cdRepresentante;
		tipoPagamento.cdTipoPagamento = pagamento.cdTipoPagamento;
		tipoPagamento = (TipoPagamento) TipoPagamentoService.getInstance().findByPrimaryKey(tipoPagamento);
		return tipoPagamento.dsTipoPagamento;
	}
	
	protected void printToFile() throws ImageException, IOException {
		String diretorio = Settings.appPath;
		String nmImagePrint = (qtdCopias > 1) ? diretorio  + "/reciboPagamento" + nuViaImpressa + ".png" : diretorio  + "/reciboPagamento.png";
		FileUtil.deleteFile(nmImagePrint);
		File f = FileUtil.criaFile(nmImagePrint);
		img.createPng(f);
		f.close();
		if (qtdCopias > 1 && nuViaImpressa == qtdCopias) {
			UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPAGAMENTOS_MSG_SUCESSO_SIMULADOR, diretorio));
		} else if (qtdCopias == 1) { 
			UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
		}
	}
	
}
