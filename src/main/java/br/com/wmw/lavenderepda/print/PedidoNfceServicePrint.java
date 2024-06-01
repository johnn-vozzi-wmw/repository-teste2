package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;

import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.StarPrinterSimulator;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.ItemNfce;
import br.com.wmw.lavenderepda.business.domain.Nfce;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.NfceService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.sys.Settings;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;

public class PedidoNfceServicePrint extends AbstractPedidoServicePrint {

	private static final int SPACE_BETWEEN_LINES = 5;
	
	private boolean isViaEstabelecimento;
	private boolean possuiLogoEmpresa;
	private Nfce nfce;
	
	public PedidoNfceServicePrint(Pedido pedido) throws SQLException {
		super(pedido);
		this.nfce = pedido.nfce;
	}
	
	private void imprimeNfce(final boolean isContingencia) throws Exception {
		configurePrinter();
		if (pedido != null) {
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			MonoImage imgNfe;
			try {
				imgNfe = geraNfceLayout(isContingencia);
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			initPrintToNfce(imgNfe);
		}
	}
	
	public void imprimeNfce() throws Exception {
		imprimeNfce(false);
	}
	
	public void imprimeNfceContingencia() throws Exception {
		imprimeNfce(true);
	}

	private void configurePrinter() throws Exception {
		if (VmUtil.isSimulador()) {
			printer = new StarPrinterSimulator();
			printer.setNuCopies(1);
		} else {
			if (ValueUtil.isEmpty(enderecoImpressora)) {
				RemoteDevice remoteDevice = BluetoothUtil.chooseRemoteDeviceBluetooth();
				if (remoteDevice != null) {
					enderecoImpressora = remoteDevice.getBluetoothAddress();
				}
			}
			if (ValueUtil.isNotEmpty(enderecoImpressora)) {
				saveEnderecoPortaIfNecessary();
				if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
					mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
					mptPrinter.setNuCopies(1);
				} 
			}
		}
	}

	private MonoImage geraNfceLayout(boolean isContingencia) throws Exception {
		int y = 0;
		int spaceFinal = 150;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int widthImage = 576;
		int heightItensDaNfce = getHeightItensNfce(widthImage, smallFont);	
		int heigthImage = isContingencia ? ((smallFont.fm.height + SPACE_BETWEEN_LINES) * 38 + heightItensDaNfce) * 2 : (smallFont.fm.height + SPACE_BETWEEN_LINES) * 31 + heightItensDaNfce;

		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		if (isContingencia) {
			y = montaLayoutNfce(isContingencia, y, smallFont, smallFontBold, widthImage, g);
			isViaEstabelecimento = true;
			y = desenhaLinhaEntreViasNfce(y, smallFont, widthImage, g);
			montaLayoutNfce(isContingencia, y, smallFont, smallFontBold, widthImage, g);
		} else {
			montaLayoutNfce(isContingencia, y, smallFont, smallFontBold, widthImage, g);
		}
		
		return mi;
	}

	private int desenhaLinhaEntreViasNfce(int y, Font smallFont, int widthImage, Graphics graphics) {
		y += smallFont.fm.height + (SPACE_BETWEEN_LINES * 3);
		graphics.drawLine(0, y, widthImage, y);
		graphics.drawLine(0, y + 1, widthImage, y + 1);
		y += smallFont.fm.height + (SPACE_BETWEEN_LINES * 3);
		return y;
	}

	private int montaLayoutNfce(boolean isContingencia, int y, Font smallFont, Font smallFontBold, int widthImage,
			Graphics g) throws SQLException, Exception {
		y = montaLayoutLogoComEmitente(g, y, widthImage, smallFont, smallFontBold);
		y = montaLayoutDescricaoDocumento(g, y, widthImage, smallFont);
		if (isContingencia) {
			y = montaLayoutTituloContingencia(g, y, widthImage, smallFont, smallFontBold);
		}
		y = montaLayoutItensNfce(g, y, widthImage, smallFont, smallFontBold);
		y = montaLayoutInfoTotalizadores(g, y, widthImage, smallFont, smallFontBold);
		y = montaLayoutInfoComplementaresNFCe(g, y, widthImage, smallFont, smallFontBold, isContingencia);
		y = montaLayoutQRCodeNFCe(g, y, widthImage, smallFontBold);
		y = montaLayoutTotalizadoresTributos(g, y, widthImage, smallFont);
		return y;
	}
	
	private int montaLayoutDescricaoDocumento(Graphics g, int posicaoY, int widthImage, Font smallFont) {
		int y = posicaoY;
		int x = SPACE_BETWEEN_LINES;
		
		g.setFont(smallFont);
		String textoTitulo = Messages.IMPRESSAONFCE_DESCRICAO;
		if (possuiLogoEmpresa) {
			x = getPosicaoCentralizada(widthImage, textoTitulo, smallFont);
		}
		g.drawText(textoTitulo, x, y);
		
		return y;
	}

	private int montaLayoutTotalizadoresTributos(Graphics g, int posicaoY, int widthImage, Font smallFont) {
		int y = posicaoY;
		int x = 0;
		
		x += SPACE_BETWEEN_LINES;
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		g.setFont(smallFont);
		
		String textoTributos = MessageUtil.getMessage(Messages.IMPRESSAONFCE_TRIBUTOS_TOTAIS, new String[]{StringUtil.getStringValueToInterface(nfce.vlTotalTributos)});
		String textoTributosFederais = MessageUtil.getMessage(Messages.IMPRESSAONFCE_TRIBUTOS_FEDERAIS, new String[]{StringUtil.getStringValueToInterface(nfce.vlPctTributosFederais)});
		String textoTributosEstaduais = MessageUtil.getMessage(Messages.IMPRESSAONFCE_TRIBUTOS_ESTADUAIS, new String[]{StringUtil.getStringValueToInterface(nfce.vlPctTributosEstaduais)});
		String textoTributosMunicipais = MessageUtil.getMessage(Messages.IMPRESSAONFCE_TRIBUTOS_MUNICIPAIS, new String[]{StringUtil.getStringValueToInterface(nfce.vlPctTributosMunicipais)});
		textoTributos = textoTributos.concat(textoTributosFederais)
										.concat(textoTributosEstaduais)
										.concat(textoTributosMunicipais);

		int larguraTexto = smallFont.fm.stringWidth(textoTributos);
		String[] descricaoMultiLinhas;
		if (larguraTexto > widthImage - SPACE_BETWEEN_LINES * 2) {
			textoTributos = MessageUtil.quebraLinhas(textoTributos, smallFont, widthImage - SPACE_BETWEEN_LINES * 2);
			descricaoMultiLinhas = StringUtil.split(textoTributos, '\n');
			for (int j = 0; j < 2; j++) {
				if (j == 1) {
					descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], widthImage - SPACE_BETWEEN_LINES * 2, smallFont);
					g.drawText(descricaoMultiLinhas[j], x, y);
				} else {
					g.drawText(descricaoMultiLinhas[j], x, y);
				}
				y += smallFont.fm.height + SPACE_BETWEEN_LINES;
			}
		} else {
			g.drawText(textoTributos, x, y);
			y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		}
		return y;
	}

	private int montaLayoutQRCodeNFCe(Graphics g, int posicaoY, int widthImage, Font smallFontBold) throws SQLException, Exception {
		int y = posicaoY;
		int widthQrCodeImage = 320;
		int x = (widthImage - widthQrCodeImage) / 2;
		File file = null;
		try {
			Image imgQRCode = NfceService.getInstance().geraQrCode(nfce, pedido.getCliente().nuCnpj);
			if (imgQRCode != null) {
				g.drawImage(imgQRCode, x, y);
			}
			y += widthQrCodeImage;
		} finally {
			FileUtil.closeFile(file);
		}
		
		return y;
	}

	private int montaLayoutTituloContingencia(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) {
		int y = posicaoY;
		int x = 0;
		y += smallFontBold.fm.height + (SPACE_BETWEEN_LINES * 2);
		x = getPosicaoCentralizada(widthImage, Messages.IMPRESSAONFCE_TITULO_CONTINGENCIA, smallFontBold);
		int yImagens = y;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAONFCE_TITULO_CONTINGENCIA, x, y);
		int tamanhoTexto = smallFontBold.fm.stringWidth(Messages.IMPRESSAONFCE_TITULO_CONTINGENCIA);
		y += smallFontBold.fm.height + (SPACE_BETWEEN_LINES * 2);
		g.drawText(Messages.IMPRESSAONFCE_CONTINGENCIA_PENDENTE_AUTORIZACAO, ValueUtil.getIntegerValue(x * 1.10), y);
		y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		
		int espacoLateralTexto = (widthImage - tamanhoTexto) / 2;
		int xInicial = tamanhoTexto + SPACE_BETWEEN_LINES + espacoLateralTexto;
		int alturaLinha = 0;
		while (yImagens < y) {
			do {
				g.drawLine(0, yImagens + alturaLinha, espacoLateralTexto - SPACE_BETWEEN_LINES, yImagens + alturaLinha);
				g.drawLine(xInicial, yImagens + alturaLinha, xInicial + espacoLateralTexto, yImagens + alturaLinha);
				alturaLinha = alturaLinha + 1;
			} while (alturaLinha < 3);
			alturaLinha = 0;
			yImagens = yImagens + 7;
		}
		
		return y;
	}

	private int montaLayoutInfoComplementaresNFCe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold, boolean isContingencia) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int widthCelula = 0;
		
		widthCelula = widthImage / 9;
		widthCelula = widthCelula * 2;
		
		x = widthCelula;
		y += SPACE_BETWEEN_LINES;
		
		Empresa empresa = EmpresaService.getInstance().getEmpresa(pedido.cdEmpresa);
		g.setFont(smallFontBold);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		String msgChaveAcesso = Messages.IMPRESSAONFCE_MSG_CHAVE_ACESSO;
		g.drawText(msgChaveAcesso, getPosicaoCentralizada(widthImage, msgChaveAcesso, smallFontBold), y);
		y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		g.setFont(smallFont);
		String dsUrlConsulta = StringUtil.getStringValue(empresa.dsUrlConsulta);
		g.drawText(dsUrlConsulta, getPosicaoCentralizada(widthImage, dsUrlConsulta, smallFont), y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		String nuChaveAcesso = StringUtil.getStringValue(nfce.nuChaveAcesso);
		g.drawText(nuChaveAcesso, getPosicaoCentralizada(widthImage, nuChaveAcesso, smallFont), y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		y += SPACE_BETWEEN_LINES;

		widthCelula = widthImage / 9;
		x = widthCelula;
		int xProtocoloTeste = getPosicaoCentralizada(widthImage, Messages.IMPRESSAONFCE_MSG_CONSUMIDOR_CPFCNPJ + StringUtil.getStringValue(pedido.getCliente().nuCnpj) , smallFontBold);
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAONFCE_MSG_CONSUMIDOR_CPFCNPJ + StringUtil.getStringValue(pedido.getCliente().nuCnpj), xProtocoloTeste, y);
		y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		xProtocoloTeste = getPosicaoCentralizada(widthImage, StringUtil.getStringValue(pedido.getCliente().dsLogradouroComercial) + ", " + StringUtil.getStringValue(pedido.getCliente().nuLogradouroComercial) , smallFontBold);
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(pedido.getCliente().dsLogradouroComercial) + ", " + StringUtil.getStringValue(pedido.getCliente().nuLogradouroComercial), xProtocoloTeste, y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		xProtocoloTeste = getPosicaoCentralizada(widthImage, pedido.getCliente().dsBairroComercial + ", " + StringUtil.getStringValue(pedido.getCliente().dsCidadeComercial), smallFontBold);
		g.setFont(smallFontBold);
		g.drawText(pedido.getCliente().dsBairroComercial + ", " + StringUtil.getStringValue(pedido.getCliente().dsCidadeComercial), xProtocoloTeste, y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		g.setFont(smallFontBold);
		xProtocoloTeste = getPosicaoCentralizada(widthImage, StringUtil.getStringValue(pedido.getCliente().dsEstadoComercial) + Messages.IMPRESSAONFCE_CEP + StringUtil.getStringValue(pedido.getCliente().dsCepComercial), smallFontBold);
		g.drawText(StringUtil.getStringValue(pedido.getCliente().dsEstadoComercial) + Messages.IMPRESSAONFCE_CEP + StringUtil.getStringValue(pedido.getCliente().dsCepComercial), xProtocoloTeste, y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		g.setFont(smallFontBold);		
		String msgInfoNfce = MessageUtil.getMessage(Messages.IMPRESSAONFCE_INFO_NFCE, new String[]{StringUtil.getStringValue(nfce.nuNfce), StringUtil.getStringValue(nfce.nuSerie), StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(nfce.dtEmissao)), StringUtil.getStringValue(nfce.hrEmissao)});
		g.drawText(msgInfoNfce, getPosicaoCentralizada(widthImage, msgInfoNfce, smallFontBold), y);
		y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		if (!isContingencia) {
			int xProtocolo = getPosicaoCentralizada(widthImage, Messages.IMPRESSAONFCE_PROTOCOLO + StringUtil.getStringValue(nfce.nuProtocoloAutorizacao), smallFontBold);
			g.drawText(Messages.IMPRESSAONFCE_PROTOCOLO, xProtocolo, y);
			xProtocolo += smallFontBold.fm.stringWidth(Messages.IMPRESSAONFCE_PROTOCOLO);
			g.setFont(smallFont);
			g.drawText(StringUtil.getStringValue(nfce.nuProtocoloAutorizacao), xProtocolo, y);
			y += smallFont.fm.height + SPACE_BETWEEN_LINES;
			
			xProtocolo = getPosicaoCentralizada(widthImage, Messages.IMPRESSAONFCE_DT_AUTORIZACAO + StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(nfce.dtAutorizacao)) + " " +  StringUtil.getStringValue(nfce.hrAutorizacao), smallFontBold);
			g.setFont(smallFontBold);
			g.drawText(Messages.IMPRESSAONFCE_DT_AUTORIZACAO, xProtocolo, y);
			xProtocolo += smallFontBold.fm.stringWidth(Messages.IMPRESSAONFCE_DT_AUTORIZACAO);
			g.setFont(smallFont);
			
			g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(nfce.dtAutorizacao)) + " " +  StringUtil.getStringValue(nfce.hrAutorizacao), xProtocolo, y);
			y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		} else {
			String msgVia = isViaEstabelecimento ? Messages.IMPRESSAONFCE_VIA_ESTABELECIMENTO : Messages.IMPRESSAONFCE_VIA_CONSUMIDOR;
			x = getPosicaoCentralizada(widthImage, msgVia, smallFontBold);
			g.drawText(msgVia, x, y);
			y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
			g.drawText(Messages.IMPRESSAONFCE_TITULO_CONTINGENCIA, getPosicaoCentralizada(widthImage, Messages.IMPRESSAONFCE_TITULO_CONTINGENCIA, smallFontBold), y);
			y += smallFontBold.fm.height + (SPACE_BETWEEN_LINES * 2);
			g.drawText(Messages.IMPRESSAONFCE_CONTINGENCIA_PENDENTE_AUTORIZACAO, getPosicaoCentralizada(widthImage, Messages.IMPRESSAONFCE_CONTINGENCIA_PENDENTE_AUTORIZACAO, smallFontBold), y);
			y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		}
		
		return y;
	}
	
	private int montaLayoutInfoTotalizadores(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int widthCelula = 0;
		
		y +=  SPACE_BETWEEN_LINES;
		g.setFont(smallFont);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		x = SPACE_BETWEEN_LINES;
		widthCelula = widthImage / 9;
		
		int yInicial = y;
		g.drawText(Messages.IMPRESSAONFCE_QTD_TOTAL_ITENS, x, y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		g.drawText(Messages.IMPRESSAONFCE_VLTOTAL_ITENS, x, y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		g.drawText(Messages.IMPRESSAONFCE_DESCONTO_ITENS, x, y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAONFCE_VLPAGAR_ITENS, x, y);
		g.setFont(smallFont);
		y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		y = drawTextFormaPagamentoNfce(y, x, g, smallFont, smallFontBold);
		
		widthCelula = widthCelula * 8;
		x += widthCelula;
		String qtTotalItem = ValueUtil.getIntegerValue(nfce.qtTotalItem) + "";
		g.drawText(qtTotalItem, getPosicaoAlinhadoDireita(x, widthImage, qtTotalItem, smallFont), yInicial);
		yInicial += smallFont.fm.height + SPACE_BETWEEN_LINES;
		String vlTotalNfce = StringUtil.getStringValueToInterface(nfce.vlTotalNfce);
		g.drawText(vlTotalNfce, getPosicaoAlinhadoDireita(x, widthImage, vlTotalNfce, smallFont), yInicial);
		yInicial += smallFont.fm.height + SPACE_BETWEEN_LINES;
		String vlTotalDesconto = StringUtil.getStringValueToInterface(nfce.vlTotalDesconto);
		g.drawText(vlTotalDesconto, getPosicaoAlinhadoDireita(x, widthImage, vlTotalDesconto, smallFont), yInicial);
		yInicial += smallFont.fm.height + SPACE_BETWEEN_LINES;
		String vlTotalLiquidoNfce = StringUtil.getStringValueToInterface(nfce.vlTotalLiquidoNfce);
		g.setFont(smallFontBold);
		g.drawText(vlTotalLiquidoNfce, getPosicaoAlinhadoDireita(x, widthImage, vlTotalLiquidoNfce, smallFontBold), yInicial);
		yInicial += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		g.setFont(smallFont);
		yInicial = drawVlFormaPagamentoNfce(yInicial, x, smallFont, widthImage, g, smallFontBold);
		
		return y;
	}

	private int drawVlFormaPagamentoNfce(int yInicial, int x, Font smallFont, int widthImage, Graphics g, Font smallFontBold) {
		double vlTotalDesconto = 0;
		double vlTotalPagamento = 0;
		String textoVlPago = Messages.IMPRESSAONFCE_VLPAGO;
		g.drawText(textoVlPago, getPosicaoAlinhadoDireita(x, widthImage, textoVlPago, smallFont), yInicial);
		yInicial += smallFont.fm.height + SPACE_BETWEEN_LINES;
		
		if (!LavenderePdaConfig.usaMultiplosPagamentosParaPedido || ValueUtil.isEmpty(pedido.pagamentoPedidoList)) {
			String vlTotalPago = StringUtil.getStringValueToInterface(nfce.vlTotalPago);
			g.drawText(vlTotalPago, getPosicaoAlinhadoDireita(x, widthImage, vlTotalPago, smallFont), yInicial);
			yInicial += smallFont.fm.height + SPACE_BETWEEN_LINES;
		} else {
			int size = pedido.pagamentoPedidoList.size();
			for (int i = 0; i < size; i++) {
				PagamentoPedido pagamentoPedido = (PagamentoPedido) pedido.pagamentoPedidoList.items[i];
				if (pagamentoPedido == null) continue;
				String vlTotalPago = StringUtil.getStringValueToInterface(pagamentoPedido.vlPagamentoPedido - pagamentoPedido.vlDesconto);
				g.drawText(vlTotalPago, getPosicaoAlinhadoDireita(x, widthImage, vlTotalPago, smallFont), yInicial);
				yInicial += smallFont.fm.height + SPACE_BETWEEN_LINES;
				vlTotalDesconto += pagamentoPedido.vlDesconto;
				vlTotalPagamento += pagamentoPedido.vlPagamentoPedido;
			}
			g.setFont(smallFontBold);
			String vlTotalPagametoDescontoString = StringUtil.getStringValueToInterface(vlTotalPagamento - vlTotalDesconto);
			g.drawText(vlTotalPagametoDescontoString, getPosicaoAlinhadoDireita(x, widthImage, vlTotalPagametoDescontoString, smallFontBold), yInicial);
			yInicial += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
			String vlTotalDescontoString = StringUtil.getStringValueToInterface(vlTotalDesconto);
			g.drawText(vlTotalDescontoString, getPosicaoAlinhadoDireita(x, widthImage, vlTotalDescontoString, smallFontBold), yInicial);
			yInicial += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
			g.setFont(smallFont);
		}
		
		return yInicial;
	}

	private int drawTextFormaPagamentoNfce(int y, int x, Graphics g , Font smallFont, Font smallFontBold) throws SQLException {
		g.drawText(Messages.IMPRESSAONFCE_FORMA_PAGAMENTO, x, y);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		
		if (!LavenderePdaConfig.usaMultiplosPagamentosParaPedido || ValueUtil.isEmpty(pedido.pagamentoPedidoList)) {
			g.drawText(StringUtil.getStringValue(nfce.dsFormaPagamento), x, y);
			y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		} else {
			int size = pedido.pagamentoPedidoList.size();
			for (int i = 0; i < size; i++) {
				PagamentoPedido pagamentoPedido = (PagamentoPedido) pedido.pagamentoPedidoList.items[i];
				g.drawText(StringUtil.getStringValue(getTipoPagamento(pagamentoPedido).dsTipoPagamento), x, y);
				y += smallFont.fm.height + SPACE_BETWEEN_LINES;
			}
			g.setFont(smallFontBold);
			g.drawText(Messages.IMPRESSOANFCE_TOTAL_PAGAMENTO_DESCONTO, x, y);
			y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
			g.drawText(Messages.IMPRESSAONFCE_TOTAL_DESCONTO, x, y);
			y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
			g.setFont(smallFont);
		}
		
		return y;
	}
	
	private TipoPagamento getTipoPagamento(PagamentoPedido pagamentoPedido) throws SQLException {
		TipoPagamento tipoPagamentoFilter = new TipoPagamento();
		tipoPagamentoFilter.cdEmpresa = pagamentoPedido.cdEmpresa;
		tipoPagamentoFilter.cdRepresentante = pagamentoPedido.cdRepresentante;
		tipoPagamentoFilter.cdTipoPagamento = pagamentoPedido.cdTipoPagamento;
		tipoPagamentoFilter = (TipoPagamento) TipoPagamentoService.getInstance().findByPrimaryKey(tipoPagamentoFilter);
		return tipoPagamentoFilter;
	}
	
	private int montaLayoutItensNfce(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int widthCelula = 0;

		//Itens da NFC-e
		y +=  SPACE_BETWEEN_LINES;
		g.setFont(smallFontBold);
		
		y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
		x = SPACE_BETWEEN_LINES;
		widthCelula = widthImage / 9;
		g.drawText(Messages.IMPRESSAONFCE_CODIGO_PRODUTO, x, y);
		x += widthCelula * 1.10;
		g.drawText(Messages.IMPRESSAONFCE_DESCRICAO_PRODUTO, x, y);
		int widthDescricao = widthCelula * 4;
		x += widthCelula * 4;
		g.drawText(Messages.IMPRESSAONFCE_QTDE_PRODUTO, x, y);
		x += widthCelula * 0.75;
		g.drawText(Messages.IMPRESSAONFCE_UNIDADE_PRODUTO, x, y);
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFCE_VLUNITARIO_PRODUTO, x, y);
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFCE_VLTOTAL_PRODUTO, x, y);
		
		//Dados dos Itens
		g.setFont(smallFont);
		y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		y = setDadosItemNfce(g, widthImage, smallFont, y, widthCelula, widthDescricao);
		return y;
	}

	private int setDadosItemNfce(Graphics g, int widthImage, Font smallFont, int y, int widthCelula, int widthDescricao) throws SQLException {
		int x = 0;
		int larguraTexto = 0;
		ItemNfce itemNfce;
		String descricaoProduto;
		String[] descricaoMultiLinhas;
		
		g.setFont(smallFont);
		int sizeItensNfce = pedido.nfce.getListItensNfce().size();
		for (int i = 0; i < sizeItensNfce; i++) {
			itemNfce = (ItemNfce) pedido.nfce.getListItensNfce().items[i];
			
			x = SPACE_BETWEEN_LINES;
			//Código do produto
			g.drawText(StringUtil.getStringValue(itemNfce.cdProduto), x, y);
			
			//Descrição do produto
			x += widthCelula * 1.10;
			descricaoProduto = StringUtil.getStringValue(itemNfce.getDsProduto());
			larguraTexto = smallFont.fm.stringWidth(descricaoProduto);
			int yInicial = y;
			if (larguraTexto > widthCelula) {
				descricaoProduto = MessageUtil.quebraLinhas(descricaoProduto, smallFont, widthDescricao - SPACE_BETWEEN_LINES);
				descricaoMultiLinhas = StringUtil.split(descricaoProduto, '\n');
				for (int j = 0; j < 2; j++) {
					if (j == 1) {
						descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], widthDescricao - SPACE_BETWEEN_LINES, smallFont);
						g.drawText(descricaoMultiLinhas[j], x, y);
					} else {
						g.drawText(descricaoMultiLinhas[j], x, y);
					}
					y += smallFont.fm.height;
				}
			} else {
				g.drawText(descricaoProduto, x, y);
			}
			
			//Qtd do item
			x += widthDescricao;
			g.drawText(ValueUtil.getIntegerValue(itemNfce.qtItemFisico) + "", x, yInicial);
			//Unidade
			x += widthCelula * 0.75;
			String cdUnidade = StringUtil.getStringValue(itemNfce.cdUnidade);
			g.drawText(cdUnidade.length() > 3 ? cdUnidade.substring(0, 3) : cdUnidade, x, yInicial);
			//Valor unitário
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemNfce.vlUnitario), x, yInicial);
			//Valor total
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemNfce.vlTotalItem), x, yInicial);
		}
		
		return y;
	}

	private int montaLayoutLogoComEmitente(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int widthCelula = 0;
		int larguraTexto = 0;
		int larguraLabel = 0;
		
		g.setFont(smallFontBold);
		x = SPACE_BETWEEN_LINES;
		widthCelula = widthImage / 5;
		
		int widthImageLogo = 120;
		
		//Logo empresa
		File file = null;
		Image logoEmpresa = null;
		try {
			file = FileUtil.openFile(Settings.appPath + "/" + NM_LOGO_EMPRESA_IMPRESSAO, File.READ_ONLY);
			logoEmpresa = new Image(file.readAndClose());
			logoEmpresa = UiUtil.getSmoothScaledImage(logoEmpresa, widthImageLogo, 100);
		} catch (Throwable ex) {
			//Não faz nada
		} finally {
			FileUtil.closeFile(file);
		}
		
		//Logo da empresa
		if (logoEmpresa != null) {
			y += SPACE_BETWEEN_LINES;
			x = SPACE_BETWEEN_LINES;
			g.drawImage(logoEmpresa, x, y);
			x = widthImageLogo + SPACE_BETWEEN_LINES * 2;
			possuiLogoEmpresa = true;
		}
		
		int widthLogoEmpresa = x;
		g.setFont(smallFont);

		Empresa empresa = EmpresaService.getInstance().getEmpresa(pedido.cdEmpresa);
		//CNPJ
		String cnpjCliente = StringUtil.getStringValue(empresa.nuCnpj);
		String fullTextCNPJ = Messages.IMPRESSAONFCE_CNPJ + cnpjCliente;
		if (ValueUtil.isNotEmpty(cnpjCliente)) {
			larguraTexto = smallFont.fm.stringWidth(cnpjCliente);
			larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAONFCE_CNPJ);
			g.drawText(fullTextCNPJ, x, y);
		}
		
		//Nome da empresa
		String nmEmpresa = StringUtil.getStringValue(empresa.nmEmpresa);
		String[] descricaoMultiLinhas;
		int maxWidthQuebraLinhas = widthImage - widthLogoEmpresa;
		if (logoEmpresa == null) {
			maxWidthQuebraLinhas = widthImage - (SPACE_BETWEEN_LINES * 2);
		}
		if (ValueUtil.isNotEmpty(nmEmpresa)) {
			x += SPACE_BETWEEN_LINES * 2 + larguraLabel + larguraTexto;
			int widthTextCnpj = x;
			g.setFont(smallFontBold);
			larguraTexto += smallFontBold.fm.stringWidth(nmEmpresa);
			if (larguraTexto + larguraLabel > maxWidthQuebraLinhas) {
				String textoNomeEmpresa = null;
				textoNomeEmpresa = MessageUtil.quebraLinhas(fullTextCNPJ + "  " + nmEmpresa, smallFontBold, maxWidthQuebraLinhas);
				descricaoMultiLinhas = StringUtil.split(textoNomeEmpresa, '\n');
				for (int j = 0; j < 2; j++) {
					if (j == 1) {
						x = widthCelula * 1;
						x += SPACE_BETWEEN_LINES * 2;
						descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], maxWidthQuebraLinhas, smallFont);
						g.drawText(descricaoMultiLinhas[j], x, y);
					} else {
						g.drawText(descricaoMultiLinhas[j].substring(fullTextCNPJ.length()), widthTextCnpj, y);
					}
					y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
				}
			} else {
				g.drawText(nmEmpresa, x, y);
				y += smallFontBold.fm.height + SPACE_BETWEEN_LINES;
			}
		}
		
		if (logoEmpresa != null) {
			x = widthCelula * 1;
			x += SPACE_BETWEEN_LINES * 2;
		} else { 
			x = SPACE_BETWEEN_LINES;
		}
		
		//Logradouro
		g.setFont(smallFont);
		String dsLogradouro = StringUtil.getStringValue(empresa.dsLogradouro);
		String dsBairro = StringUtil.getStringValue(empresa.dsBairro);
		String dsCidade = StringUtil.getStringValue(empresa.dsCidade);
		String dsEstado = StringUtil.getStringValue(empresa.dsEstado);
		String fullNameEndereco = new String();
		if (ValueUtil.isNotEmpty(dsLogradouro)) {
			fullNameEndereco = fullNameEndereco.concat(dsLogradouro).concat(", ");
		} 
		if (ValueUtil.isNotEmpty(dsBairro)) {
			fullNameEndereco = fullNameEndereco.concat(dsBairro).concat(", ");
		}
		if (ValueUtil.isNotEmpty(dsCidade)) {
			fullNameEndereco = fullNameEndereco.concat(dsCidade).concat(", ");
		} 
		if (ValueUtil.isNotEmpty(dsEstado)) {
			fullNameEndereco = fullNameEndereco.concat(dsEstado);
		}
		
		if (fullNameEndereco.endsWith(", ")) {
			fullNameEndereco = fullNameEndereco.substring(0, fullNameEndereco.length() - 3);
		}
		larguraTexto = smallFont.fm.stringWidth(fullNameEndereco.toString());
		if (larguraTexto > maxWidthQuebraLinhas) {
			descricaoMultiLinhas = StringUtil.split(MessageUtil.quebraLinhas(fullNameEndereco.toString(), smallFont, maxWidthQuebraLinhas), '\n');
			for (int j = 0; j < 2; j++) {
				if (j == 1) {
					descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], maxWidthQuebraLinhas, smallFont);
					g.drawText(descricaoMultiLinhas[j], x, y);
				} else {
					g.drawText(descricaoMultiLinhas[j], x, y);
				}
				y += smallFont.fm.height + SPACE_BETWEEN_LINES;
			}
		} else {
			g.drawText(fullNameEndereco.toString(), x, y);
			y += smallFont.fm.height + SPACE_BETWEEN_LINES;
		}
		
		if (y < widthCelula && logoEmpresa != null) {
			y = widthCelula;
		} else {
			y += SPACE_BETWEEN_LINES;
		}
			
		return y;
	}

	private int getHeightItensNfce(int widthImage, Font font) throws SQLException {
		int height = 0;
		int larguraTexto = 0;
		String descricao;
		ItemNfce itemNfce;
		for (int i = 0; i < pedido.nfce.getListItensNfce().size(); i++) {
			itemNfce = (ItemNfce) pedido.nfce.getListItensNfce().items[i];
			descricao = StringUtil.getStringValue(itemNfce.getDsProduto());
			larguraTexto = font.fm.stringWidth(descricao);
			if (larguraTexto > widthImage / 9) {
				height += (font.fm.height * 3) + (SPACE_BETWEEN_LINES * 3);
			} else {
				height += (font.fm.height * 2) + (SPACE_BETWEEN_LINES * 2);
			}
		}
		//Adiciona altura cabeçalho dos itens
		height += (font.fm.height * 2) + (SPACE_BETWEEN_LINES * 2);
		return height;
	}

	private void initPrintToNfce(MonoImage imgNfce) throws ImageException, IOException {
		if (imgNfce != null) {
			if (VmUtil.isSimulador()) {
				String nmImagePrint = Settings.appPath + "/impressaoNfce_" + pedido.nuPedido + ".png";
				FileUtil.deleteFile(nmImagePrint);
				File file = FileUtil.criaFile(nmImagePrint);
				imgNfce.createPng(file);
				file.close();
				UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
			} else if (LavenderePdaConfig.isUsaImpressaoNfceViaBluetooth()) {
				mptPrinter.printImg(imgNfce);
			} 
		}
	}
}
