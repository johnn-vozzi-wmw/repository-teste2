package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;

import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.StarPrinterSimulator;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoConsignacao;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoConsignacaoService;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.sys.Convert;
import totalcross.sys.Settings;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;
import totalcross.util.Date;


public class PedidoConsignacaoManagerPrint extends PedidoServicePrint {
	
	private double vlTotalIcmsPedido;
	private double vlTotalStPedido;
	private double vlTotalIpiPedido;

	public PedidoConsignacaoManagerPrint(Pedido pedido) throws SQLException {
		super(pedido);
	}
	
	public void imprimePedidoConsignacao() throws Exception {
		imprimePedidoConsignacao(false);
	}
	
	public void imprimePedidoConsignacao(boolean somenteComprovanteNfe) throws Exception {
		if (VmUtil.isSimulador()) {
			if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
				printer = new StarPrinterSimulator();
				printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedidoConsignacaoDevolucao);
			}
		} else {
			if (ValueUtil.isEmpty(enderecoImpressora)) {
				RemoteDevice remoteDevice = BluetoothUtil.chooseRemoteDeviceBluetooth();
				if (remoteDevice != null) {
					enderecoImpressora = remoteDevice.getBluetoothAddress();
				}
			}
			if (ValueUtil.isNotEmpty(enderecoImpressora)) {
				saveEnderecoPortaIfNecessary();
				if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
					zonerichPrinter = new ZonerichPrinter(enderecoImpressora, portaImpressora);
					zonerichPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedidoConsignacaoDevolucao);
				}
			}
		}
		if (pedido != null && ValueUtil.isNotEmpty(pedido.getPedidoConsignacaoList())) {
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			MonoImage imgNfe = null;
			try {
				if (somenteComprovanteNfe) {
					imgNfe = geraComprovantePedidoConsignacaoLayoutImpressoraTermica();
				} else {
					imgNfe = geraPedidoConsignacaoLayoutImpressoraTermica();
				}
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			imprimeImagemPedidoConsignacao(imgNfe, somenteComprovanteNfe);
		}
	}
	
	private void imprimeImagemPedidoConsignacao(MonoImage img, boolean somenteComprovanteNfe) throws ImageException, IOException {
		if (img != null) {
			if (VmUtil.isSimulador()) {
				String nmImagePrint = Convert.appendPath(Settings.appPath, (somenteComprovanteNfe ? "impressaoComprovantePedidoConsignacao_" + pedido.nuPedido + ".png" : "impressaoPedidoConsignacao_" + pedido.nuPedido + ".png")); 
				FileUtil.deleteFile(nmImagePrint);
				File file = FileUtil.criaFile(nmImagePrint);
				img.createPng(file);
				file.close();
				UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
			} else if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
				zonerichPrinter.printImg(img);
			}
		}
	}
	
	private MonoImage geraComprovantePedidoConsignacaoLayoutImpressoraTermica() throws ImageException, IOException, SQLException {
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 150;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int heigthImage = (smallFontBold.fm.height + spaceBetweenLines) * 9;
		int widthImage = 576;
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		montaLayoutComprovantePedidoConsignacao(g, y, widthImage, smallFont, smallFontBold);
		return mi;
	}
	
	private MonoImage geraPedidoConsignacaoLayoutImpressoraTermica() throws ImageException, IOException, SQLException {
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 150;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int widthImage = 576;
		
		//Logo empresa
		File file = null;
		Image logoEmpresa = null;
		try {
			logoEmpresa = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DA_EMPRESA_PARA_IMPRESSAO);
			if (logoEmpresa == null) {
				file = FileUtil.openFile(Settings.appPath + "/" + NM_LOGO_EMPRESA_IMPRESSAO, File.READ_ONLY);
				logoEmpresa = new Image(file.readAndClose());
			}
			logoEmpresa = UiUtil.getSmoothScaledImage(logoEmpresa, widthImage - (UiUtil.getControlPreferredHeight() * 2), UiUtil.getControlPreferredHeight() * 3);
		} catch (Throwable ex) {
			//Não faz nada
		} finally {
			FileUtil.closeFile(file);
		}

		int heightComprovante = 0;
		int espacoEntreComprovante = 0;
		if (LavenderePdaConfig.usaImpressaoComprovanteConsignacaoDevolucao == 1) {
			heightComprovante = (smallFontBold.fm.height + spaceBetweenLines) * 9;
			espacoEntreComprovante = spaceFinal;
		}
		int heightItens = getHeightItens(widthImage, smallFont);	
		int heigthImage = (logoEmpresa != null ? logoEmpresa.getHeight() + heightComprovante + espacoEntreComprovante : heightComprovante + espacoEntreComprovante) + ((smallFont.fm.height + spaceBetweenLines) * 19) + heightItens;
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		
		//Comprovante
		if (LavenderePdaConfig.usaImpressaoComprovanteConsignacaoDevolucao == 1) {
			y = montaLayoutComprovantePedidoConsignacao(g, y, widthImage, smallFont, smallFontBold);
			y += espacoEntreComprovante;
		}
		//Info Consignação
		y = montaLayoutInfoConsignacao(g, y, widthImage, logoEmpresa, smallFont, smallFontBold);
		//Emitente
		y = montaLayoutInfoEmitenteConsignacao(g, y, widthImage, logoEmpresa, smallFont, smallFontBold);
		//Destinatário
		y = montaLayoutDestinatarioConsignacao(g, y, widthImage, smallFont, smallFontBold);
		//Item Pedido
		y = montaLayoutItemPedidoConsignacao(g, y, widthImage, smallFont, smallFontBold);
		//Cálculo do imposto
		y = montaLayoutCalculoImpostoConsignacao(g, y, widthImage, smallFont, smallFontBold);
		return mi;
	}
	
	private int montaLayoutComprovantePedidoConsignacao(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoList().items[0];
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		g.setFont(smallFont);
		x = spaceBetweenLines;
		g.drawRoundRect(0, y, widthImage, (smallFont.fm.height + spaceBetweenLines) * 8, 8);
		
		//Nome empresa
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_CABECALHO_EMPRESA, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		String nmEmpresa = StringUtil.getStringValue(pedido.getEmpresa().nmEmpresa);
		larguraTexto = smallFont.fm.stringWidth(nmEmpresa);
		if (larguraTexto > widthImage) {
			nmEmpresa = StringUtil.getStringAbreviada(nmEmpresa, widthImage, smallFont);
		}
		g.drawText(nmEmpresa, x, y);

		//Número do pedido
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		String nuPedido = Messages.IMPRESSAOCONSIGNACAO_CABECALHO_NUPEDIDO + pedido.nuPedido;
		larguraTexto = smallFont.fm.stringWidth(nuPedido);
		if (larguraTexto > widthImage) {
			nuPedido = StringUtil.getStringAbreviada(nuPedido, widthImage, smallFont);
		}
		g.drawText(nuPedido, x, y);
		
		//Cliente
		g.setFont(smallFontBold);
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_CABECALHO_CLIENTE, x, y);
		g.setFont(smallFont);
		y += smallFont.fm.height + spaceBetweenLines;
		String nomeCliente = StringUtil.getStringValue(pedido.getCliente().nmRazaoSocial) + " - " + Messages.IMPRESSAOCONSIGNACAO_CABECALHO_CLIENTE_CNPJ_CPF + StringUtil.getStringValue(pedido.getCliente().nuCnpj);
		larguraTexto = smallFont.fm.stringWidth(nomeCliente);
		if (larguraTexto > widthImage) {
			nomeCliente = StringUtil.getStringAbreviada(nomeCliente, widthImage, smallFont);
		}
		g.drawText(nomeCliente, x, y);
		
		//Valor Original
		g.setFont(smallFontBold);
		y += smallFont.fm.height + spaceBetweenLines;
		widthCelula = widthImage / 2;
		String vlOriginal = Messages.IMPRESSAOCONSIGNACAO_CABECALHO_VALOR_ORIGINAL + StringUtil.getStringValueToInterface(pedido.vlPedidoOriginal);
		larguraTexto = smallFont.fm.stringWidth(vlOriginal);
		g.drawText(vlOriginal, x, y);
		
		//Vencimento
		x = widthCelula;
		Date dtVencimento = PedidoConsignacaoService.getInstance().getDtVencimento(pedidoConsignacao);
		String diaVencimento = Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_VENCIMENTO + StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(dtVencimento));
		g.drawText(diaVencimento, x, y);

		//DATA E ASSINATURA
		g.setFont(smallFont);
		x = spaceBetweenLines; 
		y += (smallFontBold.fm.height + spaceBetweenLines) * 2;
		widthCelula = widthCelula / 2;
		g.setFont(smallFont);
		g.drawLine(x, y, widthCelula, y);
		g.drawLine(x, y + 1, widthCelula, y + 1);
		g.drawLine(widthCelula * 2, y, widthImage - spaceBetweenLines, y);
		g.drawLine(widthCelula * 2, y + 1, widthImage - spaceBetweenLines, y + 1);
		y += spaceBetweenLines;
		x = ((widthCelula / 2) - (smallFont.fm.stringWidth(Messages.IMPRESSAONFE_CABECALHO_DATA) / 2)) + 3;
		g.drawText(Messages.IMPRESSAONFE_CABECALHO_DATA, x, y);
		x = ((widthImage / 2) - (smallFont.fm.stringWidth(Messages.IMPRESSAONFE_CABECALHO_ASSINATURA) / 2)) + 3;
		g.drawText(Messages.IMPRESSAONFE_CABECALHO_ASSINATURA, widthCelula  + x, y);
		y +=  smallFont.fm.height;
		x = spaceBetweenLines;
		return y;
	}
	
	private int montaLayoutInfoConsignacao(Graphics g, int posicaoY, int widthImage, Image logoEmpresa, Font smallFont, Font smallFontBold) throws ImageException, IOException, SQLException {
		PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoList().items[0];
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		
		//Logo da empresa
		if (logoEmpresa != null) {
			y += spaceBetweenLines;
			x = ((widthImage / 2) - (logoEmpresa.getWidth() / 2)) + 3;
			g.drawImage(logoEmpresa, x, y);
		}

		if (logoEmpresa != null) {
			y += logoEmpresa.getHeight() + spaceBetweenLines;
		} else {
			y += spaceBetweenLines;
		}
		x = spaceBetweenLines;
		
		g.drawRoundRect(0, y, widthImage, smallFont.fm.height + smallFontBold.fm.height * 2, 8);
		//MSG Cliente
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_MSGCLIENTE, x, y);
		y += smallFont.fm.height;
		
		//Pedido
		g.setFont(smallFont);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_PEDIDO + pedido.nuPedido, x, y);
		y += smallFont.fm.height;

		//Emissão
		widthCelula = widthImage / 2;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_EMISSAO + StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoConsignacao.dtEmissao)), x, y);
		
		//Vencimento
		x = widthCelula;
		Date dtVencimento = PedidoConsignacaoService.getInstance().getDtVencimento(pedidoConsignacao);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_VENCIMENTO + StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(dtVencimento)), x, y);
		y += smallFont.fm.height;
		x = spaceBetweenLines;
		
		return y;
	}
	
	private int montaLayoutInfoEmitenteConsignacao(Graphics g, int posicaoY, int widthImage, Image logoEmpresa, Font smallFont, Font smallFontBold) throws ImageException, IOException, SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int larguraTexto = 0;
		int larguraImpressa = 0;
		int alturaRect = 0;
		
		g.setFont(smallFontBold);
		y += spaceBetweenLines;
		x = spaceBetweenLines;
		alturaRect = y;
		
		//Emitente
		g.drawText(Messages.IMPRESSAONFE_EMITENTE, x, y);
		g.setFont(smallFont);
		y += smallFontBold.fm.height + spaceBetweenLines;
		
		Empresa empresa = EmpresaService.getInstance().getEmpresa(pedido.cdEmpresa);
		
		//Nome empresa
		if (ValueUtil.isNotEmpty(empresa.nmEmpresa)) {
			x = spaceBetweenLines;
			g.drawText(empresa.nmEmpresa, x, y);
			larguraImpressa = x + smallFont.fm.stringWidth(empresa.nmEmpresa);
		}
		//Endereço
		if (ValueUtil.isNotEmpty(empresa.dsLogradouro)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(empresa.dsLogradouro, x, y);
			larguraImpressa = x + smallFont.fm.stringWidth(empresa.dsLogradouro);
		}
		//Bairro
		if (ValueUtil.isNotEmpty(empresa.dsBairro)) {
			x = larguraImpressa;
			larguraTexto = smallFont.fm.stringWidth(" - " + empresa.dsBairro);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.dsBairro, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		//Cep
		if (ValueUtil.isNotEmpty(empresa.dsCep)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(empresa.dsCep, x, y);
			larguraImpressa = x + smallFont.fm.stringWidth(empresa.dsCep);
		}
		//Cidade
		if (ValueUtil.isNotEmpty(empresa.dsCidade)) {
			x = larguraImpressa;
			larguraTexto = smallFont.fm.stringWidth(" - " + empresa.dsCidade);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.dsCidade, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		//Estado
		if (ValueUtil.isNotEmpty(empresa.dsEstado)) {
			x = larguraImpressa;
			larguraTexto = smallFont.fm.stringWidth(" - " + empresa.dsEstado);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.dsEstado, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		//Telefone
		if (ValueUtil.isNotEmpty(empresa.nuFone)) {
			x = larguraImpressa;
			larguraTexto = smallFont.fm.stringWidth(" - " + empresa.nuFone);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.nuFone, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		//CNPJ
		if (ValueUtil.isNotEmpty(empresa.nuCnpj)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(Messages.IMPRESSAOPEDIDO_CNPJ_CPF + empresa.nuCnpj, x, y);
			larguraImpressa = x + smallFont.fm.stringWidth(empresa.nuCnpj);
		}
		//IE
		if (ValueUtil.isNotEmpty(empresa.nuInscricaoEstadual)) {
			x = spaceBetweenLines;
			y += smallFont.fm.height + spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(" - " + empresa.nuInscricaoEstadual);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(Messages.IMPRESSAOPEDIDO_IE + empresa.nuInscricaoEstadual, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		g.drawRoundRect(0, alturaRect, widthImage, y - alturaRect + smallFont.fm.height + spaceBetweenLines, 8);
		y += spaceBetweenLines;
		return y;
	}
	
	private int montaLayoutDestinatarioConsignacao(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int larguraTexto = 0;
		int larguraLabel = 0;
		int alturaRect = 0;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		alturaRect = y;
		
		//DESTINATÁRIO
		g.drawText(Messages.IMPRESSAONFE_DESTINATARIO, x, y);
		g.setFont(smallFont);
		y += smallFontBold.fm.height + spaceBetweenLines;
		Cliente cliente = (Cliente)ClienteService.getInstance().findByRowKeyDyn(pedido.getCliente().getRowKey());
		
		//Nome do cliente
		String nome = StringUtil.getStringValue(pedido.getCliente().nmRazaoSocial);
		larguraTexto = smallFont.fm.stringWidth(nome);
		if (larguraTexto > widthImage) {
			nome = StringUtil.getStringAbreviada(nome, widthImage, smallFont);
		}
		g.drawText(nome, x, y);

		//Endereço do cliente
		String endCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSLOGRADOUROCOMERCIAL"));
		if (ValueUtil.isNotEmpty(endCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(endCliente);
			if (larguraTexto > widthImage) {
				endCliente = StringUtil.getStringAbreviada(endCliente, widthImage, smallFont);
			}
			g.drawText(endCliente, x, y);
		}

		//Bairro/Cidade/UF do cliente
		String cidadeCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCIDADECOMERCIAL"));
		String ufCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSESTADOCOMERCIAL"));
		String bairroCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSBAIRROCOMERCIAL"));
		if (ValueUtil.isNotEmpty(cidadeCliente) || ValueUtil.isNotEmpty(ufCliente) || ValueUtil.isNotEmpty(bairroCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(cidadeCliente) + smallFont.fm.stringWidth(ufCliente);
			if (larguraTexto > widthImage) {
				cidadeCliente = StringUtil.getStringAbreviada(cidadeCliente, widthImage / 2, smallFont);
				bairroCliente = StringUtil.getStringAbreviada(bairroCliente, widthImage / 2, smallFont);
			}
			g.drawText(bairroCliente + " - " + cidadeCliente + " - " + ufCliente, x, y);
		}
		
		//CEP/TEL do cliente
		String cepCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCEPCOMERCIAL"));
		String telCliente = StringUtil.getStringValue(pedido.getCliente().nuFone);
		if (ValueUtil.isNotEmpty(cepCliente) || ValueUtil.isNotEmpty(telCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(cepCliente) + smallFont.fm.stringWidth(telCliente);
			if (larguraTexto + larguraLabel > widthImage) {
				cepCliente = StringUtil.getStringAbreviada(cepCliente, widthImage, smallFont);
			}
			g.drawText(Messages.IMPRESSAOPEDIDO_CEP + cepCliente + " - " + Messages.IMPRESSAOPEDIDO_TELEFONE + telCliente, x, y);
		}
		
		//CNPJ do cliente
		String cnpjCliente = StringUtil.getStringValue(pedido.getCliente().nuCnpj);
		if (ValueUtil.isNotEmpty(cnpjCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(cnpjCliente);
			larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_CNPJ_CPF);
			if (larguraTexto + larguraLabel > widthImage) {
				cnpjCliente = StringUtil.getStringAbreviada(cnpjCliente, widthImage, smallFont);
			}
			g.drawText(Messages.IMPRESSAOPEDIDO_CNPJ_CPF + cnpjCliente, x, y);
		}
		
		//IE do cliente
		String ieCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("NUINSCRICAOESTADUAL"));
		if (ValueUtil.isNotEmpty(ieCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(ieCliente);
			larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_IE);
			if (larguraTexto + larguraLabel > widthImage) {
				ieCliente = StringUtil.getStringAbreviada(ieCliente, widthImage - larguraLabel, smallFont);
			}
			g.drawText(Messages.IMPRESSAOPEDIDO_IE + ieCliente, x, y);
		}
		g.drawRoundRect(0, alturaRect, widthImage, y - alturaRect + smallFont.fm.height + spaceBetweenLines, 8);
		y += smallFont.fm.height + spaceBetweenLines;
		
		return y;
	}
	
	private int montaLayoutItemPedidoConsignacao(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;

		//Itens do pedido
		y +=  spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawRoundRect(0, y, widthImage, (smallFontBold.fm.height + spaceBetweenLines) * 2, 8);

		//Cabeçalho dos itens do pedido
		x = spaceBetweenLines;
		int larguraDescricao = smallFontBold.fm.stringWidth(Messages.IMPRESSAOPEDIDO_DESCRICAO);
		g.drawText(Messages.IMPRESSAOPEDIDO_DESCRICAO, x, y + 2);
		
		x += larguraDescricao;
		g.drawText(" - " + Messages.IMPRESSAOPEDIDO_CST, x, y + 2);
		
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = (widthImage - spaceBetweenLines * 2) / 7;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_UNIDADE, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_QTD, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_VL_UNI, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_COM_DESCONTO, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_ICMS, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_ALIQUOTA, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL, x - spaceBetweenLines, y);
		
		//Dados dos Itens
		y = montaLayoutDadosItens(g, widthImage, smallFont, smallFontBold, y, spaceBetweenLines, widthCelula);  
		y += spaceBetweenLines;
		
		return y;
	}
	
	private int montaLayoutDadosItens(Graphics g, int widthImage, Font smallFont, Font smallFontBold, int y, int spaceBetweenLines, int widthCelula) throws SQLException {
		int x = 0;
		int larguraTexto = 0;
		PedidoConsignacao pedidoConsignacao;
		ItemPedido itemPedido;
		for (int i = 0; i < pedido.getPedidoConsignacaoList().size(); i++) {
			pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoList().items[i];
			itemPedido = ItemPedidoService.getInstance().createItemPedidoByPedidoConsignacao(pedidoConsignacao);
			int index = pedido.itemPedidoList.indexOf(itemPedido);
			if (index < 0) {
				continue;
			}
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[index];
			//Descrição do item
			String descricaoProduto;
			String[] descricaoMultiLinhas;

			//Descrição do item
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			descricaoProduto = LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(pedidoConsignacao.getDsProduto()) : StringUtil.getStringValue(pedidoConsignacao.getDsProdutoWithKey());
			String cst = StringUtil.getStringValue(itemPedido.getProduto().cdClassificFiscal);
			if (ValueUtil.isNotEmpty(cst)) {
				cst = " - " + cst;
			}
			larguraTexto = smallFont.fm.stringWidth(descricaoProduto) + smallFont.fm.stringWidth(cst);
			g.setFont(smallFontBold);
			if (larguraTexto > widthImage) {
				descricaoProduto = MessageUtil.quebraLinhas(descricaoProduto, smallFont, widthImage - spaceBetweenLines * 2);
				descricaoMultiLinhas = StringUtil.split(descricaoProduto, '\n');
				for (int j = 0; j < 2; j++) {
					if (j == 1) {
						descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], widthImage - smallFont.fm.stringWidth(cst) - spaceBetweenLines * 2, smallFont);
						g.drawText(descricaoMultiLinhas[j] + cst, x, y);
					} else {
						g.drawText(descricaoMultiLinhas[j], x, y);
					}
					y += smallFontBold.fm.height + spaceBetweenLines;
				}
			} else {
				g.drawText(descricaoProduto + cst, x, y);
				y += smallFontBold.fm.height + spaceBetweenLines;
			}
			g.setFont(smallFont);
			//Unidade
			g.drawText(StringUtil.getStringValue(pedidoConsignacao.cdUnidade), x, y);
			//Qtd do item
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(pedidoConsignacao.qtItemFisico), x, y);
			//Valor do unitário
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlBaseItemPedido), x, y);
			//Valor com desconto
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(pedidoConsignacao.vlItemPedido), x, y);
			//Valor ICMS
			x += widthCelula;
			String vlIcmsItem = ValueUtil.VALOR_ZERO;
			if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
				vlIcmsItem = StringUtil.getStringValueToInterface(getIcmsItem(itemPedido));
			}
			g.drawText(vlIcmsItem, x, y);
			//Valor AL. ICMS
			x += widthCelula;
			String aliquotaIcms = ValueUtil.VALOR_ZERO;
			if (LavenderePdaConfig.isUsaCalculoStItemPedido() && ValueUtil.isNotEmpty(pedido.getCliente().cdTributacaoCliente)) {
				Tributacao tributacao = itemPedido.getTributacaoItem(pedido.getCliente());
				if (tributacao != null) {
					aliquotaIcms = StringUtil.getStringValueToInterface(tributacao.vlPctIcms);
				}
			}
			g.drawText(aliquotaIcms, x, y);
			//Valor total
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(pedidoConsignacao.vlTotalItemPedido), x - spaceBetweenLines, y);
		}
		return y;
	}
	
	private int montaLayoutCalculoImpostoConsignacao(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int alturaLinha = 0;
		
		calculaTotalIcmsIpiStPedido();
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		alturaLinha =  (smallFont.fm.height + spaceBetweenLines) * 2;
		g.drawRoundRect(0, y, widthImage, alturaLinha * 2 + smallFontBold.fm.height, 8);
		
		g.drawText(Messages.IMPRESSAONFE_CALCULO_IMPOSTO, x, y);
		g.setFont(smallFont);
		y += smallFont.fm.height;
		g.drawLine(0, y, widthImage, y);
		g.setFont(smallFontBold);
		//Label ICMS
		g.drawText(Messages.IMPRESSAONFE_VALOR_ICMS, x, y);
		x += widthCelula;
		g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		//Label ICMS Substituição Tributária
		g.drawText(Messages.IMPRESSAONFE_VALOR_ICMS_SUB, x, y);
		x += widthCelula;
		g.drawLine(widthCelula * 2, y, widthCelula * 2, y  +  alturaLinha);
		//Label IPI
		g.drawText(Messages.IMPRESSAONFE_VALOR_IPI, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.setFont(smallFont);
		//Valor ICMS
		String vlTotalIcms = LavenderePdaConfig.isUsaCalculoStItemPedido() ? StringUtil.getStringValueToInterface(vlTotalIcmsPedido) : ValueUtil.VALOR_ZERO;
		g.drawText(vlTotalIcms, x, y);
		x += widthCelula;
		//Valor ICMS Substituição Tributária
		String vlTotalSt = LavenderePdaConfig.isUsaCalculoStItemPedido() ? StringUtil.getStringValueToInterface(vlTotalStPedido) : ValueUtil.VALOR_ZERO;
		g.drawText(vlTotalSt, x, y);
		x += widthCelula;
		//Valor IPI
		String vlIpi = LavenderePdaConfig.isUsaCalculoIpiItemPedido() ? StringUtil.getStringValueToInterface(vlTotalIpiPedido) : ValueUtil.VALOR_ZERO;
		g.drawText(vlIpi, x, y);
		x += widthCelula;
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawLine(0, y, widthImage, y);
		x = spaceBetweenLines;
		g.setFont(smallFontBold);
		//Label Valor Desconto
		g.drawText(Messages.IMPRESSAONFE_VALOR_DESCONTO, x, y);
		x += widthCelula;
		g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		//Label Valor total original
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_CALCULOIMPOSTO_VLTOTALORIGINAL, x, y);
		x += widthCelula;
		g.drawLine(widthCelula * 2, y, widthCelula * 2, y  +  alturaLinha);
		//Label Valor total da nota
		g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL_NOTA, x, y);
		x += widthCelula;
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.setFont(smallFont);
		//Valor desconto
		String vlDesconto = StringUtil.getStringValueToInterface(pedido.vlDesconto);
		g.drawText(vlDesconto, x, y);
		x += widthCelula;
		//Valor total da nota
		String vlPedidoOriginal = StringUtil.getStringValueToInterface(pedido.vlPedidoOriginal);
		g.drawText(vlPedidoOriginal, x, y);
		x += widthCelula;
		//Valor total original
		String vlTotalPedido = StringUtil.getStringValueToInterface(pedido.vlTotalPedido);
		g.drawText(vlTotalPedido, x, y);
		y += smallFont.fm.height + spaceBetweenLines;		
		
		y += spaceBetweenLines;
		return y;
	}
	
	public void imprimePedidoConsignacaoDevolucao() throws Exception {
		imprimePedidoConsignacaoDevolucao(false, false);
	}
	
	public void imprimePedidoConsignacaoDevolucao(boolean somenteComprovante) throws Exception {
		imprimePedidoConsignacaoDevolucao(somenteComprovante, false);
	}
	
	public void imprimePedidoConsignacaoDevolucao(boolean somenteComprovante, boolean itensAgrupados) throws Exception {
		if (VmUtil.isSimulador()) {
			if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
				printer = new StarPrinterSimulator();
				printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedidoConsignacaoDevolucao);
			}
		} else {
			if (ValueUtil.isEmpty(enderecoImpressora)) {
				RemoteDevice remoteDevice = BluetoothUtil.chooseRemoteDeviceBluetooth();
				if (remoteDevice != null) {
					enderecoImpressora = remoteDevice.getBluetoothAddress();
				}
			}
			if (ValueUtil.isNotEmpty(enderecoImpressora)) {
				saveEnderecoPortaIfNecessary();
				if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
					zonerichPrinter = new ZonerichPrinter(enderecoImpressora, portaImpressora);
					zonerichPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedidoConsignacaoDevolucao);
				}
			}
		}
		if (pedido != null && ValueUtil.isNotEmpty(pedido.getPedidoConsignacaoDevolucaoList())) {
			MonoImage imgNfe = null;
			if (somenteComprovante) {
				imgNfe = geraComprovantePedidoConsignacaoDevolucaoLayoutImpressoraTermica();
			} else {
				imgNfe = geraPedidoConsignacaoDevolucaoLayoutImpressoraTermica(itensAgrupados);
			}
			imprimeImagemPedidoConsignacaoDevolucao(imgNfe, somenteComprovante);
		}
	}
	
	private void imprimeImagemPedidoConsignacaoDevolucao(MonoImage img, boolean somenteComprovante) throws ImageException, IOException {
		if (img != null) {
			if (VmUtil.isSimulador()) {
				String nmImagePrint = somenteComprovante ? "C:/impressaoComprovantePedidoConsignacaoDevolucao_" + pedido.nuPedido + ".png" : "C:/impressaoPedidoConsignacaoDevolucao_" + pedido.nuPedido + ".png"; 
				FileUtil.deleteFile(nmImagePrint);
				File file = FileUtil.criaFile(nmImagePrint);
				img.createPng(file);
				file.close();
				UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
			} else if (LavenderePdaConfig.usaImpressaoPedidoConsignacaoDevolucao == 4) {
				zonerichPrinter.printImg(img);
			}
		}
	}
	
	private MonoImage geraComprovantePedidoConsignacaoDevolucaoLayoutImpressoraTermica() throws ImageException, IOException, SQLException {
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 150;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int heigthImage = (smallFontBold.fm.height + spaceBetweenLines) * 11;
		int widthImage = 576;
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		montaLayoutComprovantePedidoConsignacaoDevolucao(g, y, widthImage, smallFont, smallFontBold);
		return mi;
	}
	
	private int montaLayoutComprovantePedidoConsignacaoDevolucao(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoDevolucaoList().items[0];
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		g.setFont(smallFont);
		x = spaceBetweenLines;
		g.drawRoundRect(0, y, widthImage, (smallFont.fm.height + spaceBetweenLines) * 10, 8);
		
		//Nome empresa
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_CABECALHO_EMPRESA, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		String nmEmpresa = StringUtil.getStringValue(pedido.getEmpresa().nmEmpresa);
		larguraTexto = smallFont.fm.stringWidth(nmEmpresa);
		if (larguraTexto > widthImage) {
			nmEmpresa = StringUtil.getStringAbreviada(nmEmpresa, widthImage, smallFont);
		}
		g.drawText(nmEmpresa, x, y);

		//Número do pedido
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		String nuPedido = Messages.IMPRESSAOCONSIGNACAO_CABECALHO_NUPEDIDO + pedido.nuPedido;
		larguraTexto = smallFont.fm.stringWidth(nuPedido);
		if (larguraTexto > widthImage) {
			nuPedido = StringUtil.getStringAbreviada(nuPedido, widthImage, smallFont);
		}
		g.drawText(nuPedido, x, y);
		
		//Cliente
		g.setFont(smallFontBold);
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_CABECALHO_CLIENTE, x, y);
		g.setFont(smallFont);
		y += smallFont.fm.height + spaceBetweenLines;
		String nomeCliente = StringUtil.getStringValue(pedido.getCliente().nmRazaoSocial) + " - " + Messages.IMPRESSAOCONSIGNACAO_CABECALHO_CLIENTE_CNPJ_CPF + StringUtil.getStringValue(pedido.getCliente().nuCnpj);
		larguraTexto = smallFont.fm.stringWidth(nomeCliente);
		if (larguraTexto > widthImage) {
			nomeCliente = StringUtil.getStringAbreviada(nomeCliente, widthImage, smallFont);
		}
		g.drawText(nomeCliente, x, y);
		
		//Msg devolução
		g.setFont(smallFontBold);
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOCONSIGNACAODEVOLUCAO_MSGDEVOLUCAO, x, y);
		
		//Valor Original
		y += smallFont.fm.height + spaceBetweenLines;
		widthCelula = widthImage / 2;
		String vlOriginal = Messages.IMPRESSAOCONSIGNACAO_CABECALHO_VALOR_ORIGINAL + StringUtil.getStringValueToInterface(pedido.vlPedidoOriginal);
		g.drawText(vlOriginal, x, y);
		
		//Dia Vencimento
		x = widthCelula;
		Date dtVencimento = PedidoConsignacaoService.getInstance().getDtVencimento(pedidoConsignacao);
		String diaVencimento = Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_VENCIMENTO + StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(dtVencimento));
		g.drawText(diaVencimento, x, y);
		
		//Total devolvido
		x = spaceBetweenLines; 
		y += smallFontBold.fm.height + spaceBetweenLines;
		String totalDevolvido = Messages.IMPRESSAOCONSIGNACAODEVOLUCAO_TOTALDEVOLVIDO + StringUtil.getStringValueToInterface(pedido.vlTotalDevolucoes);
		g.drawText(totalDevolvido, x, y);
		
		//% devolvido
		x = widthCelula;
		String vlPercentualDevolvido = Messages.IMPRESSAOCONSIGNACAODEVOLUCAO_PERCENTUALDEVOLVIDO + StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlTotalDevolucoes * 100 / pedido.vlPedidoOriginal));
		g.drawText(vlPercentualDevolvido, x, y);

		//DATA E ASSINATURA
		g.setFont(smallFont);
		x = spaceBetweenLines; 
		y += (smallFontBold.fm.height + spaceBetweenLines) * 2;
		widthCelula = widthCelula / 2;
		g.setFont(smallFont);
		g.drawLine(x, y, widthCelula, y);
		g.drawLine(x, y + 1, widthCelula, y + 1);
		g.drawLine(widthCelula * 2, y, widthImage - spaceBetweenLines, y);
		g.drawLine(widthCelula * 2, y + 1, widthImage - spaceBetweenLines, y + 1);
		y += spaceBetweenLines;
		x = ((widthCelula / 2) - (smallFont.fm.stringWidth(Messages.IMPRESSAONFE_CABECALHO_DATA) / 2)) + 3;
		g.drawText(Messages.IMPRESSAONFE_CABECALHO_DATA, x, y);
		x = ((widthImage / 2) - (smallFont.fm.stringWidth(Messages.IMPRESSAONFE_CABECALHO_ASSINATURA) / 2)) + 3;
		g.drawText(Messages.IMPRESSAONFE_CABECALHO_ASSINATURA, widthCelula  + x, y);
		y +=  smallFont.fm.height;
		x = spaceBetweenLines;
		return y;
	}
	
	private int montaLayoutInfoConsignacaoDevolucao(Graphics g, int posicaoY, int widthImage, Image logoEmpresa, Font smallFont, Font smallFontBold, boolean itensAgrupados) throws ImageException, IOException, SQLException {
		PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoDevolucaoList().items[0];
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		
		//Logo da empresa
		if (logoEmpresa != null) {
			y += spaceBetweenLines;
			x = ((widthImage / 2) - (logoEmpresa.getWidth() / 2)) + 3;
			g.drawImage(logoEmpresa, x, y);
		}

		if (logoEmpresa != null) {
			y += logoEmpresa.getHeight() + spaceBetweenLines;
		} else {
			y += spaceBetweenLines;
		}
		x = spaceBetweenLines;
		
		g.drawRoundRect(0, y, widthImage, (smallFont.fm.height) + smallFontBold.fm.height * 2, 8);
		//MSG Cliente
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOCONSIGNACAODEVOLUCAO_INFOCONSIGNACAODEVOLUCAO_MSGCLIENTE, x, y);
		y += smallFont.fm.height;
		
		//Pedido
		g.setFont(smallFont);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_PEDIDO + pedido.nuPedido, x, y);
		y += smallFont.fm.height;
		
		//Emissão
		widthCelula = widthImage / 2;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_EMISSAO + StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoConsignacao.dtEmissao)), x, y);
		
		//Vencimento
		x = widthCelula;
		Date dtVencimento = PedidoConsignacaoService.getInstance().getDtVencimento(pedidoConsignacao);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_INFOCONSIGNACAO_VENCIMENTO + StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(dtVencimento)), x, y);
		y += smallFont.fm.height;
		x = spaceBetweenLines;
		
		
		//Msg impressão agrupada
		if (itensAgrupados) {
			y += spaceBetweenLines;
			g.setFont(smallFontBold);
			String[] descricaoMultiLinhas;
			String msgimpressaoAgrupada = Messages.IMPRESSAOCONSIGNACAODEVOLUCAO_INFOCONSIGNACAODEVOLUCAO_MSGIMPRESSAOAGRUPADA;
			larguraTexto = smallFont.fm.stringWidth(msgimpressaoAgrupada);
			if (larguraTexto > widthImage) {
				msgimpressaoAgrupada = MessageUtil.quebraLinhas(msgimpressaoAgrupada, smallFontBold, widthImage - spaceBetweenLines);
				descricaoMultiLinhas = StringUtil.split(msgimpressaoAgrupada, '\n');
				for (int k = 0; k < descricaoMultiLinhas.length; k++) {
					if (k == descricaoMultiLinhas.length - 1) {
						descricaoMultiLinhas[k] = StringUtil.getStringAbreviada(descricaoMultiLinhas[k], widthImage - spaceBetweenLines * 2, smallFontBold);
						g.drawText(descricaoMultiLinhas[k], x, y);
					} else {
						g.drawText(descricaoMultiLinhas[k], x, y);
					}
					y += smallFontBold.fm.height + spaceBetweenLines;
				}
			} else {
				g.drawText(msgimpressaoAgrupada, x, y);
				y += spaceBetweenLines;
			}
		} else {
			y += spaceBetweenLines;
		}
		
		return y;
	}
	
	private int montaLayoutDadosItensDevolucao(Graphics g, int widthImage, Font smallFont, Font smallFontBold, int y, int spaceBetweenLines, int widthCelula) throws SQLException {
		int x = 0;
		int larguraTexto = 0;
		PedidoConsignacao pedidoConsignacao;
		ItemPedido itemPedido;
		for (int i = 0; i < pedido.getPedidoConsignacaoDevolucaoList().size(); i++) {
			pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoDevolucaoList().items[i];
			itemPedido = ItemPedidoService.getInstance().createItemPedidoByPedidoConsignacao(pedidoConsignacao);
			//Descrição do item
			String descricaoProduto;
			String[] descricaoMultiLinhas;

			//Descrição do item
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			descricaoProduto = LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(pedidoConsignacao.getDsProduto()) : StringUtil.getStringValue(pedidoConsignacao.getDsProdutoWithKey());
			String cst = StringUtil.getStringValue(itemPedido.getProduto().cdClassificFiscal);
			if (ValueUtil.isNotEmpty(cst)) {
				cst = " - " + cst;
			}
			larguraTexto = smallFont.fm.stringWidth(descricaoProduto) + smallFont.fm.stringWidth(cst);
			g.setFont(smallFontBold);
			if (larguraTexto > widthImage) {
				descricaoProduto = MessageUtil.quebraLinhas(descricaoProduto, smallFont, widthImage - spaceBetweenLines * 2);
				descricaoMultiLinhas = StringUtil.split(descricaoProduto, '\n');
				for (int j = 0; j < 2; j++) {
					if (j == 1) {
						descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], widthImage - smallFont.fm.stringWidth(cst) - spaceBetweenLines * 2, smallFont);
						g.drawText(descricaoMultiLinhas[j] + cst, x, y);
					} else {
						g.drawText(descricaoMultiLinhas[j], x, y);
					}
					y += smallFontBold.fm.height + spaceBetweenLines;
				}
			} else {
				g.drawText(descricaoProduto + cst, x, y);
				y += smallFontBold.fm.height + spaceBetweenLines;
			}
			g.setFont(smallFont);
			//Unidade
			g.drawText(StringUtil.getStringValue(pedidoConsignacao.cdUnidade), x, y);
			//Qtd do item
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(pedidoConsignacao.qtItemFisico), x, y);
			//Valor do unitário
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlBaseItemPedido), x, y);
			//Valor total
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(pedidoConsignacao.vlTotalItemPedido), x - spaceBetweenLines, y);
		}
		return y;
	}
	
	private int montaLayoutItemPedidoConsignacaoDevolucao(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;

		//Itens do pedido
		y +=  spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawRoundRect(0, y, widthImage, (smallFontBold.fm.height + spaceBetweenLines) * 2, 8);

		//Cabeçalho dos itens do pedido
		x = spaceBetweenLines;
		int larguraDescricao = smallFontBold.fm.stringWidth(Messages.IMPRESSAOPEDIDO_DESCRICAO);
		g.drawText(Messages.IMPRESSAOPEDIDO_DESCRICAO, x, y + 2);
		
		x += larguraDescricao;
		g.drawText(" - " + Messages.IMPRESSAOPEDIDO_CST, x, y + 2);
		
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = (widthImage - spaceBetweenLines * 2) / 4;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_UNIDADE, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_QTD, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_VL_UNI, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL, x - spaceBetweenLines, y);
		
		//Dados dos Itens
		y = montaLayoutDadosItensDevolucao(g, widthImage, smallFont, smallFontBold, y, spaceBetweenLines, widthCelula);  
		y += spaceBetweenLines;
		
		return y;
	}
	
	private int montaLayoutCalculoImpostoConsignacaoDevolucao(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int alturaLinha = 0;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		alturaLinha =  (smallFont.fm.height + spaceBetweenLines) * 2;
		g.drawRoundRect(0, y, widthImage, alturaLinha * 2 + smallFontBold.fm.height, 8);
		
		g.drawText(Messages.IMPRESSAONFE_CALCULO_IMPOSTO, x, y);
		g.setFont(smallFont);
		y += smallFont.fm.height;
		g.drawLine(0, y, widthImage, y);
		g.setFont(smallFontBold);
		//Label Valor total
		g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL, x, y);
		x += widthCelula;
		g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		//Label Total devolvido
		g.drawText(Messages.IMPRESSAOCONSIGNACAODEVOLUCAO_TOTALDEVOLVIDO, x, y);
		//Valor total
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValueToInterface(getVlTotalDevolucaoAtual()), x, y);
		x += widthCelula;
		//Valor total devolvido
		g.drawText(StringUtil.getStringValueToInterface(pedido.vlTotalDevolucoes), x, y);
		x += widthCelula;
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawLine(0, y, widthImage, y);
		//Label Valor Original pedido
		x = spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOCONSIGNACAO_CABECALHO_VALOR_ORIGINAL, x, y);
		x += widthCelula;
		g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		//Label % devolvido
		g.drawText(Messages.IMPRESSAOCONSIGNACAODEVOLUCAO_PERCENTUALDEVOLVIDO, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		//Valor original pedido
		x = spaceBetweenLines;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValueToInterface(pedido.vlPedidoOriginal), x, y);
		x += widthCelula;
		//% devolvido
		g.drawText(StringUtil.getStringValueToInterface(ValueUtil.round(pedido.vlTotalDevolucoes * 100 / pedido.vlPedidoOriginal)), x, y);
		y += smallFont.fm.height + spaceBetweenLines;		
		
		y += spaceBetweenLines;
		return y;
	}
	
	private MonoImage geraPedidoConsignacaoDevolucaoLayoutImpressoraTermica(boolean itensAgrupados) throws ImageException, IOException, SQLException {
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 150;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int widthImage = 576;
		
		//Logo empresa
		File file = null;
		Image logoEmpresa = null;
		try {
			logoEmpresa = PersonalizacaoService.getInstance().getImagemPersonalizada(Personalizacao.LOGO_DA_EMPRESA_PARA_IMPRESSAO);
			if (logoEmpresa == null) {
				file = FileUtil.openFile(Settings.appPath + "/" + NM_LOGO_EMPRESA_IMPRESSAO, File.READ_ONLY);
				logoEmpresa = new Image(file.readAndClose());
			}
			logoEmpresa = UiUtil.getSmoothScaledImage(logoEmpresa, widthImage - (UiUtil.getControlPreferredHeight() * 2), UiUtil.getControlPreferredHeight() * 3);
		} catch (Throwable ex) {
			//Não faz nada
		} finally {
			FileUtil.closeFile(file);
		}

		int heightComprovante = 0;
		int espacoEntreComprovante = 0;
		if (LavenderePdaConfig.usaImpressaoComprovanteConsignacaoDevolucao == 1) {
			heightComprovante = (smallFontBold.fm.height + spaceBetweenLines) * 9;
			espacoEntreComprovante = spaceFinal;
		}
		int heightItens = getHeightItensDevolucao(widthImage, smallFont);	
		int heightMsgItensAgrupados = itensAgrupados ? (smallFontBold.fm.height + spaceBetweenLines) * 3 : 0;
		int heigthImage = (logoEmpresa != null ? logoEmpresa.getHeight() + heightComprovante + espacoEntreComprovante : heightComprovante + espacoEntreComprovante) + ((smallFont.fm.height + spaceBetweenLines) * 19) + heightItens + heightMsgItensAgrupados;
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		
		//Comprovante
		if (LavenderePdaConfig.usaImpressaoComprovanteConsignacaoDevolucao == 1) {
			y = montaLayoutComprovantePedidoConsignacaoDevolucao(g, y, widthImage, smallFont, smallFontBold);
			y += espacoEntreComprovante;
		}
		//Info Devolução Consignação
		y = montaLayoutInfoConsignacaoDevolucao(g, y, widthImage, logoEmpresa, smallFont, smallFontBold, itensAgrupados);
		//Emitente
		y = montaLayoutInfoEmitenteConsignacao(g, y, widthImage, logoEmpresa, smallFont, smallFontBold);
		//Destinatário
		y = montaLayoutDestinatarioConsignacao(g, y, widthImage, smallFont, smallFontBold);
		//Item Pedido
		y = montaLayoutItemPedidoConsignacaoDevolucao(g, y, widthImage, smallFont, smallFontBold);
		//Cálculo do imposto
		y = montaLayoutCalculoImpostoConsignacaoDevolucao(g, y, widthImage, smallFont, smallFontBold);
		return mi;
	}
	
	private int getHeightItens(int widthImage, Font font) throws SQLException {
		int height = 0;
		int larguraTexto = 0;
		int spaceBetweenLines = 5;
		String descricao;
		PedidoConsignacao pedidoConsignacao;
		for (int i = 0; i < pedido.getPedidoConsignacaoList().size(); i++) {
			pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoList().items[i];
			descricao = StringUtil.getStringValue(pedidoConsignacao.getDsProduto());
			larguraTexto = font.fm.stringWidth(descricao);
			if (larguraTexto > widthImage) {
				height += (font.fm.height * 3) + (spaceBetweenLines * 3);
			} else {
				height += (font.fm.height * 2) + (spaceBetweenLines * 2);
			}
		}
		//adiciona altura cabeçalho dos itens
		height += (font.fm.height * 2) + (spaceBetweenLines * 2);
		return height;
	}
	
	private int getHeightItensDevolucao(int widthImage, Font font) throws SQLException {
		int height = 0;
		int larguraTexto = 0;
		int spaceBetweenLines = 5;
		String descricao;
		PedidoConsignacao pedidoConsignacao;
		for (int i = 0; i < pedido.getPedidoConsignacaoDevolucaoList().size(); i++) {
			pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoDevolucaoList().items[i];
			descricao = StringUtil.getStringValue(pedidoConsignacao.getDsProduto());
			larguraTexto = font.fm.stringWidth(descricao);
			if (larguraTexto > widthImage) {
				height += (font.fm.height * 3) + (spaceBetweenLines * 3);
			} else {
				height += (font.fm.height * 2) + (spaceBetweenLines * 2);
			}
		}
		//adiciona altura cabeçalho dos itens
		height += (font.fm.height * 2) + (spaceBetweenLines * 2);
		return height;
	}
	
	private double getIcmsItem(ItemPedido item) { 
        if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) { 
        	return item.vlTotalIcmsItem; 
        } else if (LavenderePdaConfig.isUsaCalculaStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) { 
        	return item.getVlTotalIcms();           
        } 
        return 0d; 
	}
	
	private void calculaTotalIcmsIpiStPedido() { 
        vlTotalIcmsPedido = 0; 
        vlTotalStPedido = 0; 
        vlTotalIpiPedido = 0; 
        ItemPedido itemPedido; 
        for (int i = 0; i < pedido.itemPedidoList.size(); i++) { 
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i]; 
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {  
				vlTotalIcmsPedido += itemPedido.vlTotalIcmsItem; 
				vlTotalStPedido += itemPedido.vlTotalStItem; 
				vlTotalIpiPedido += itemPedido.vlTotalIpiItem; 
			} else { 
				vlTotalIcmsPedido += itemPedido.getVlTotalIcms(); 
				vlTotalStPedido += itemPedido.getVlTotalST(); 
				vlTotalIpiPedido += itemPedido.getVlTotalIpi(); 
			} 
        } 
	}
	
	private double getVlTotalDevolucaoAtual() throws SQLException {
		double vlTotalDevolucaoAtual = 0;
		for (int i = 0; i < pedido.getPedidoConsignacaoDevolucaoList().size(); i++) {
			PedidoConsignacao pedidoConsignacao = (PedidoConsignacao) pedido.getPedidoConsignacaoDevolucaoList().items[i];
			vlTotalDevolucaoAtual += pedidoConsignacao.vlTotalItemPedido;
		}
		return vlTotalDevolucaoAtual;
	}

}
