package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.StarPrinterSimulator;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
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
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.Produto;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.Tributacao;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
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
import totalcross.util.Vector;

public final class PedidoNfeContingenciaManagerPrint extends AbstractPedidoServicePrint {
	
	private double vlIcmsPedido;
	double vlIcmsSubPedido;
	double vlTotalIpiPedido;
	private Cliente cliente;

	public PedidoNfeContingenciaManagerPrint(Pedido pedido) throws SQLException {
		super(pedido);
		calculaTotalIcmsIpiStPedido();
		this.cliente = pedido.getCliente();
	}
	
	public void imprimeNfeContingencia() throws Exception {
		if (pedido != null) {
			MonoImage imageLayoutImpressoraTermica;
			initPrintToNfe();
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			try {
				imageLayoutImpressoraTermica = geraNfeContingenciaLayoutImpressoraTermica();
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			imprimeImagemNfeContingencia(imageLayoutImpressoraTermica, Convert.appendPath(Settings.appPath, "impressaoNfeContingencia_" + pedido.nuPedido + ".png"));
		}
	}
	
	public void imprimeComprovanteNfeContingencia()throws Exception {
		if (pedido != null) {
			MonoImage imageLayoutImpressoraTermica;
			initPrintToNfe();
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			try {
				imageLayoutImpressoraTermica = geraComprovanteNfeContingenciaLayoutImpressoraTermica();
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			imprimeImagemNfeContingencia(imageLayoutImpressoraTermica, Convert.appendPath(Settings.appPath, "impressaoComprovanteNfeContingencia_" + pedido.nuPedido + ".png"));
		}
	}
	
	private void initPrintToNfe() throws Exception {
		if (VmUtil.isSimulador()) {
			if (LavenderePdaConfig.usaImpressaoNfeViaBluetoothComCamposAdicionais()) {
				printer = new StarPrinterSimulator();
				printer.setNuCopies(LavenderePdaConfig.nrCopiasConfigImpressaoNfeViaBluetooth() == 0 ? 1 : LavenderePdaConfig.nrCopiasConfigImpressaoNfeViaBluetooth());
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
				if (LavenderePdaConfig.isUsaLayout3ConfigImpressaoNfeViaBluetooth() || LavenderePdaConfig.isUsaLayout6ConfigImpressaoNfeViaBluetooth()) {
					mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
					mptPrinter.setNuCopies(LavenderePdaConfig.nrCopiasConfigImpressaoNfeViaBluetooth() == 0 ? 1 : LavenderePdaConfig.nrCopiasConfigImpressaoNfeViaBluetooth());
				} else if (LavenderePdaConfig.isUsaLayout4ConfigImpressaoNfeViaBluetooth()) {
					zonerichPrinter = new ZonerichPrinter(enderecoImpressora, portaImpressora);
					zonerichPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
				}
			}
		}
	}
	
	private int getHeightItensNfeReferencia(int widthImage, Font font) throws SQLException {
		int height = 0;
		int larguraTexto = 0;
		int spaceBetweenLines = 5;
		ItemPedido itemNfeReferencia;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			itemNfeReferencia = (ItemPedido) pedido.itemPedidoList.items[i];
			Produto produtoNfe = itemNfeReferencia.getProduto();
			produtoNfe = produtoNfe == null ? new Produto() : produtoNfe;
			larguraTexto = font.fm.stringWidth(StringUtil.getStringValue(produtoNfe.dsReferencia));
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
	
	private int getHeightItensNfe(int widthImage, Font font) throws SQLException {
		int height = 0;
		int larguraTexto = 0;
		int spaceBetweenLines = 5;
		String descricao;
		ItemPedido itemNfe;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			itemNfe = (ItemPedido) pedido.itemPedidoList.items[i];
			boolean isItemGrade = ValueUtil.isNotEmpty(itemNfe.cdItemGrade1) && !"0".equals(itemNfe.cdItemGrade1);
			if (LavenderePdaConfig.isConfigGradeProduto() && isItemGrade) {
				ItemPedidoGrade itemPedidoGrade = ItemPedidoGradeService.getInstance().montaItemPedidoGradeByItemPedido(itemNfe);
				descricao = StringUtil.getStringValue(ItemPedidoGradeService.getInstance().getDescricaoProdutoGradeResumida(itemPedidoGrade));
				larguraTexto = font.fm.stringWidth(descricao);
				if (larguraTexto > widthImage) {
					height += (font.fm.height * 3) + (spaceBetweenLines * 3);
				} else {
					height += (font.fm.height * 2) + (spaceBetweenLines * 2);
				}
			} else {
				descricao = StringUtil.getStringValue(itemNfe.getDsProduto());
				larguraTexto = font.fm.stringWidth(descricao);
				if (larguraTexto > widthImage) {
					height += (font.fm.height * 3) + (spaceBetweenLines * 3);
				} else {
					height += (font.fm.height * 2) + (spaceBetweenLines * 2);
				}
			}
		}
		//adiciona altura cabeçalho dos itens
		height += (font.fm.height * 2) + (spaceBetweenLines * 2);
		return height;
	}
	
	private void calculaTotalIcmsIpiStPedido() {
		vlIcmsPedido = 0;
		vlIcmsSubPedido = 0;
		vlTotalIpiPedido = 0;
		ItemPedido itemPedido;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) { 
				vlIcmsPedido += itemPedido.vlTotalIcmsItem;
				vlIcmsSubPedido += itemPedido.vlTotalStItem;
				vlTotalIpiPedido += itemPedido.vlTotalIpiItem;
			} else {
				vlIcmsPedido += itemPedido.getVlTotalIcms();
				vlIcmsSubPedido += itemPedido.getVlTotalST();
				vlTotalIpiPedido += itemPedido.getVlTotalIpi();
			}
			
		}
	}
	
	protected MonoImage geraNfeContingenciaLayoutImpressoraTermica() throws ImageException, IOException, SQLException {
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
		int espacoEntreComprovanteNfe = 0;
		if (LavenderePdaConfig.usaImpressaoComprovanteNfe == 1) {
			heightComprovante = (smallFontBold.fm.height + spaceBetweenLines) * 7;
			espacoEntreComprovanteNfe = spaceFinal;
		}
		int heightItensDaNfe = LavenderePdaConfig.usaNfePorReferencia ? getHeightItensNfeReferencia(widthImage, smallFont) : getHeightItensNfe(widthImage, smallFont);	
		int heigthImage = (logoEmpresa != null ? logoEmpresa.getHeight() + heightComprovante + espacoEntreComprovanteNfe : heightComprovante + espacoEntreComprovanteNfe) + ((smallFont.fm.height + spaceBetweenLines) * 25) + ((smallFont.fm.height + spaceBetweenLines) * pedido.parcelaPedidoList.size()) + heightItensDaNfe;
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		
		//Comprovante
		if (LavenderePdaConfig.usaImpressaoComprovanteNfe == 1) {
			y = montaLayoutComprovanteNfeContingencia(g, y, widthImage, smallFont, smallFontBold);
			y += espacoEntreComprovanteNfe;
		}
		//Info Nfe
		y = montaLayoutInfoNfeContingencia(g, y, widthImage, logoEmpresa, smallFont, smallFontBold);
		//Emitente
		y = montaLayoutEmitenteNfeContingencia(g, y, widthImage, smallFont, smallFontBold);
		//Destinatário
		y = montaLayoutDestinatarioNfeContingencia(g, y, widthImage, smallFont, smallFontBold);
		//Item Pedido
		y = montaLayoutItemPedidoNfeContingencia(g, y, widthImage, smallFont, smallFontBold);
		//Cálculo dos impostos
		y = montaLayoutCalculoImpostoNfeContingencia(g, y, widthImage, smallFont, smallFontBold);
		//Dados Adicionais
		y = montaLayoutDadosAdicionaisNfeContingencia(g, y, widthImage, smallFont, smallFontBold);
		
		return mi;
	}
	
	protected MonoImage geraComprovanteNfeContingenciaLayoutImpressoraTermica() throws ImageException, IOException, SQLException {
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 150;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int heigthImage = (smallFontBold.fm.height + spaceBetweenLines) * 7;
		int widthImage = 576;
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		montaLayoutComprovanteNfeContingencia(g, y, widthImage, smallFont, smallFontBold);
		return mi;
	}
	

	private int montaLayoutComprovanteNfeContingencia(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		g.setFont(smallFont);
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		g.drawRoundRect(0, y, widthImage, (smallFont.fm.height + spaceBetweenLines) * 8, 8);
		
		//Nome empresa
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CABECALHO_EMPRESA, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		String nmEmpresa = StringUtil.getStringValue(pedido.getEmpresa().nmEmpresa);
		larguraTexto = smallFont.fm.stringWidth(nmEmpresa);
		if (larguraTexto > widthImage) {
			nmEmpresa = StringUtil.getStringAbreviada(nmEmpresa, widthImage, smallFont);
		}
		g.drawText(nmEmpresa, x, y);

		//--
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CABECALHO, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawText(pedido.nuPedido, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CABECALHO_NOTA, x, y);

		//DATA E ASSINATURA
		y += (smallFontBold.fm.height + spaceBetweenLines) * 2;
		widthCelula = widthCelula / 2;
		g.setFont(smallFont);
		g.drawLine(x, y, widthCelula, y);
		g.drawLine(x, y + 1, widthCelula, y + 1);
		g.drawLine(widthCelula * 2, y, widthImage - spaceBetweenLines, y);
		g.drawLine(widthCelula * 2, y + 1, widthImage - spaceBetweenLines, y + 1);
		y += spaceBetweenLines;
		x = ((widthCelula / 2) - (smallFont.fm.stringWidth(Messages.IMPRESSAONFECONTINGENCIA_CABECALHO_DATA) / 2)) + 3;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CABECALHO_DATA, x, y);
		x = ((widthImage / 2) - (smallFont.fm.stringWidth(Messages.IMPRESSAONFECONTINGENCIA_CABECALHO_ASSINATURA) / 2)) + 3;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CABECALHO_ASSINATURA, widthCelula  + x, y);
		y +=  smallFont.fm.height;
		x = spaceBetweenLines;
		return y;
	}
	
	protected void imprimeImagemNfeContingencia(MonoImage img, String filePath) throws ImageException, IOException {
		if (img != null) {
			if (VmUtil.isSimulador()) {
				String nmImagePrint = filePath; 
				FileUtil.deleteFile(nmImagePrint);
				File file = FileUtil.criaFile(nmImagePrint);
				img.createPng(file);
				file.close();
				UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
			} else if ("3".equals(LavenderePdaConfig.usaImpressaoContingenciaNfeViaBluetooth) || "6".equals(LavenderePdaConfig.usaImpressaoContingenciaNfeViaBluetooth)) {
				mptPrinter.printImg(img);
			} else if ("4".equals(LavenderePdaConfig.usaImpressaoContingenciaNfeViaBluetooth)) {
				zonerichPrinter.printImg(img);
			}
		}
	}
	
	private int montaLayoutInfoNfeContingencia(Graphics g, int posicaoY, int widthImage, Image logoEmpresa, Font smallFont, Font smallFontBold) throws ImageException, IOException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		
		g.setFont(smallFontBold);
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

		g.drawRoundRect(0, y, widthImage, (smallFont.fm.height) + smallFontBold.fm.height, 8);
				
		//TEXTO DANFE
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_INFONFE_CONTINGENCIA, x, y);
		g.setFont(smallFont);
		
		g.drawText("", widthCelula * 2, y);
		y += smallFont.fm.height;
		g.drawText("", x, y);
		g.drawText("", widthCelula * 2, y);
		y += smallFont.fm.height;
		
		//NATUREZA DA OPERAÇÃO
		y += spaceBetweenLines;
		x = spaceBetweenLines;
		g.drawRoundRect(0, y, widthImage, smallFontBold.fm.height * 2, 8);
		String natOperacao = StringUtil.getStringValue(Messages.IMPRESSAONFECONTINGENCIA_VENDA_MERCADORIA);
		larguraTexto = smallFont.fm.stringWidth(natOperacao);
		if (larguraTexto > widthCelula * 2) {
			natOperacao = StringUtil.getStringAbreviada(natOperacao, widthCelula * 2, smallFont);
		}
		String dataEmissao = StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedido.dtEmissao));
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_NAT_OPERACAO, x, y);
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_DATA_EMISSAO + dataEmissao, widthCelula * 2, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(natOperacao, x, y);
		g.setFont(smallFontBold);
		g.drawText("", widthCelula * 2, y);
		y += smallFont.fm.height;
		x = spaceBetweenLines;
		return y;
	}
	
	
	private int montaLayoutEmitenteNfeContingencia(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
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
		
		//EMITENTE
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_EMITENTE, x, y);
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
			g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CNPJ_CPF + empresa.nuCnpj, x, y);
			larguraImpressa = x + smallFont.fm.stringWidth(empresa.nuCnpj);
		}
		//IE
		if (ValueUtil.isNotEmpty(empresa.nuInscricaoEstadual)) {
			x = spaceBetweenLines;
			y += smallFont.fm.height + spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(" - " + empresa.nuInscricaoEstadual);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(Messages.IMPRESSAONFECONTINGENCIA_IE + empresa.nuInscricaoEstadual, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		g.drawRoundRect(0, alturaRect, widthImage, y - alturaRect + smallFont.fm.height + spaceBetweenLines, 8);
		y += spaceBetweenLines;
		return y;
	}
	
	private int montaLayoutDestinatarioNfeContingencia(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
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
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_DESTINATARIO, x, y);
		g.setFont(smallFont);
		y += smallFontBold.fm.height + spaceBetweenLines;
		Cliente cliente = (Cliente) ClienteService.getInstance().findByRowKeyDyn(this.cliente.getRowKey());
		
		//Nome do cliente
		String nome = StringUtil.getStringValue(this.cliente.nmRazaoSocial);
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
		String telCliente = StringUtil.getStringValue(this.cliente.nuFone);
		if (ValueUtil.isNotEmpty(cepCliente) || ValueUtil.isNotEmpty(telCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(cepCliente) + smallFont.fm.stringWidth(telCliente);
			if (larguraTexto + larguraLabel > widthImage) {
				cepCliente = StringUtil.getStringAbreviada(cepCliente, widthImage, smallFont);
			}
			g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CEP + cepCliente + " - " + Messages.IMPRESSAONFECONTINGENCIA_TELEFONE + telCliente, x, y);
		}
		
		//CNPJ do cliente
		String cnpjCliente = StringUtil.getStringValue(this.cliente.nuCnpj);
		if (ValueUtil.isNotEmpty(cnpjCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(cnpjCliente);
			larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAONFECONTINGENCIA_CNPJ_CPF);
			if (larguraTexto + larguraLabel > widthImage) {
				cnpjCliente = StringUtil.getStringAbreviada(cnpjCliente, widthImage, smallFont);
			}
			g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CNPJ_CPF + cnpjCliente, x, y);
		}
		
		//IE do cliente
		String ieCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("NUINSCRICAOESTADUAL"));
		if (ValueUtil.isNotEmpty(ieCliente)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			larguraTexto = smallFont.fm.stringWidth(ieCliente);
			larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAONFECONTINGENCIA_IE);
			if (larguraTexto + larguraLabel > widthImage) {
				ieCliente = StringUtil.getStringAbreviada(ieCliente, widthImage - larguraLabel, smallFont);
			}
			g.drawText(Messages.IMPRESSAONFECONTINGENCIA_IE + ieCliente, x, y);
		}
		g.drawRoundRect(0, alturaRect, widthImage, y - alturaRect + smallFont.fm.height + spaceBetweenLines, 8);
		y += smallFont.fm.height + spaceBetweenLines;
		
		return y;
	}
	
	
	private int montaLayoutItemPedidoNfeContingencia(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
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
		int larguraDescricao = smallFontBold.fm.stringWidth(Messages.IMPRESSAONFECONTINGENCIA_DESCRICAO);
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_DESCRICAO, x, y + 2);
		
		x += larguraDescricao;
		if (LavenderePdaConfig.usaNfePorReferencia) {
			g.drawText(" - " + Messages.IMPRESSAONFECONTINGENCIA_CST + " - " + Messages.ITEMNFEREFERENCIA_LABEL_CDCFOP, x, y + 2);
		} else {
			g.drawText(" - " + Messages.IMPRESSAONFECONTINGENCIA_CST, x, y + 2);
		}
		
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		if (LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
			widthCelula = (widthImage - spaceBetweenLines * 2) / 6;
		} else {
			widthCelula = (widthImage - spaceBetweenLines * 2) / 7;
		}
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_ITEMPEDIDO_UNIDADE, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_QTD, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VL_UNI, x, y);
		
		if (!LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
			x += widthCelula;
			g.drawText(Messages.IMPRESSAONFECONTINGENCIA_ITEMPEDIDO_COM_DESCONTO, x, y);
		}
			
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_ITEMPEDIDO_ICMS, x, y);
		
		x += widthCelula; 
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_ITEMPEDIDO_ALIQUOTA, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VALOR_TOTAL, x - spaceBetweenLines, y);
		
		//Dados dos Itens
		g.setFont(smallFont);
		y = LavenderePdaConfig.usaNfePorReferencia ? setDadosItemNFEReferenciaContingencia(g, widthImage, smallFont, smallFontBold, y, spaceBetweenLines, widthCelula) :  setDadosItemNFEContingencia(g, widthImage, smallFont, smallFontBold, y, spaceBetweenLines, widthCelula);
		y += spaceBetweenLines;
		return y;
	}
	
	private int setDadosItemNFEReferenciaContingencia(Graphics g, int widthImage, Font smallFont, Font smallFontBold, int y, int spaceBetweenLines, int widthCelula) throws SQLException {
		int x = 0;
		int larguraTexto = 0;
		ItemPedido itemPedidoReferencia;
		Vector itemPedidoRefList = getItemPedidoRefList();
		for (int i = 0; i < itemPedidoRefList.size(); i++) {
			itemPedidoReferencia = (ItemPedido) itemPedidoRefList.items[i];
			Produto produto = itemPedidoReferencia.getProduto();
			produto = produto == null ? new Produto() : produto;
			//Descrição do item
			String descricaoReferencia = StringUtil.getStringValue(produto.dsReferenciaProduto);
			String[] descricaoMultiLinhas;
			//Descrição do item
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			String cst = StringUtil.getStringValue(produto.cdClassificFiscal);
			if (ValueUtil.isNotEmpty(cst)) {
				cst = " - " + cst;
			}
			String cfop = StringUtil.getStringValue(produto.cdUnidade);
			if (ValueUtil.isNotEmpty(cfop)) {
				cfop = " - " + cfop;
			}
			larguraTexto = smallFont.fm.stringWidth(descricaoReferencia) + smallFont.fm.stringWidth(cst) + smallFont.fm.stringWidth(cfop) + spaceBetweenLines * 2;
			g.setFont(smallFontBold);
			if (larguraTexto > widthImage) {
				descricaoReferencia = MessageUtil.quebraLinhas(descricaoReferencia, smallFont, widthImage - spaceBetweenLines * 2);
				descricaoMultiLinhas = StringUtil.split(descricaoReferencia, '\n');
				if (descricaoMultiLinhas.length == 1) {
					descricaoMultiLinhas = new String[2];
					descricaoMultiLinhas[0] = descricaoReferencia;
					descricaoMultiLinhas[1] = "";
				}
				for (int j = 0; j < 2; j++) {
					if (j == 1) {
						descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], widthImage - smallFont.fm.stringWidth(cst) - smallFont.fm.stringWidth(cfop) - spaceBetweenLines * 2, smallFont);
						g.drawText(descricaoMultiLinhas[j] + cst + cfop, x, y);
					} else {
						g.drawText(descricaoMultiLinhas[j], x, y);
					}
					y += smallFontBold.fm.height + spaceBetweenLines;
				}
			} else {
				g.drawText(descricaoReferencia + cst + cfop, x, y);
				y += smallFontBold.fm.height + spaceBetweenLines;
			}
			g.setFont(smallFont);
			//Unidade
			String cdUnidade = StringUtil.getStringValue(itemPedidoReferencia.cdUnidade);
			g.drawText(cdUnidade.length() > 3 ? cdUnidade.substring(0, 3) : cdUnidade, x, y);
			//Qtd do item
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemPedidoReferencia.getQtItemFisico()), x, y);
			//Valor do unitário
			x += widthCelula;
			if (LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
				g.drawText(StringUtil.getStringValueToInterface(itemPedidoReferencia.vlItemPedido), x, y);
			} else {
				g.drawText(StringUtil.getStringValueToInterface(itemPedidoReferencia.vlBaseItemTabelaPreco), x, y);
				//Valor com desconto
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemPedidoReferencia.vlItemPedido), x, y);
			}
			//Valor ICMS
			x += widthCelula;
			String vlIcmsItem = ValueUtil.VALOR_ZERO;
			vlIcmsItem = StringUtil.getStringValueToInterface(getIcmsItem(itemPedidoReferencia));
			g.drawText(vlIcmsItem, x, y);
			//Valor AL. ICMS
			x += widthCelula;
			String aliquotaIcms = ValueUtil.VALOR_ZERO;
			if (LavenderePdaConfig.isUsaCalculoStItemPedido() && cliente != null && ValueUtil.isNotEmpty(cliente.cdTributacaoCliente)) {
				Tributacao tributacao = itemPedidoReferencia.getTributacaoItem(cliente);
				aliquotaIcms = StringUtil.getStringValueToInterface(tributacao != null ? tributacao.vlPctIcms : 0);
			}
			g.drawText(aliquotaIcms, x, y);
			//Valor total
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemPedidoReferencia.vlTotalItemPedido), x - spaceBetweenLines, y);
		}
		
		return y;
	}
	
	private double getIcmsItem(ItemPedido item) {
		if (LavenderePdaConfig.usaCalculoDeTributacaoPersonalizado) {
			return item.vlTotalIcmsItem;
		} else if (LavenderePdaConfig.isUsaCalculaStItemPedido() || LavenderePdaConfig.calculaStSimplificadaItemPedido) {
			return item.vlIcms * item.getQtItemFisico();
		}
		return 0d;
	}

	private int setDadosItemNFEContingencia(Graphics g, int widthImage, Font smallFont, Font smallFontBold, int y, int spaceBetweenLines, int widthCelula) throws SQLException {
		int x = 0;
		int larguraTexto = 0;
		ItemPedido itemPedido;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			//Descrição do item
			String descricaoProduto;
			String cst = StringUtil.getStringValue(itemPedido.getProduto().cdClassificFiscal);
			String[] descricaoMultiLinhas;
			boolean isItemGrade = ValueUtil.isNotEmpty(itemPedido.cdItemGrade1) && !"0".equals(itemPedido.cdItemGrade1);
			if (LavenderePdaConfig.isConfigGradeProduto() && isItemGrade) {
				//Descrição do item
				y += smallFont.fm.height + spaceBetweenLines;
				x = spaceBetweenLines;
				descricaoProduto = LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(itemPedido.getDsProduto()) : StringUtil.getStringValue(itemPedido.getDsProdutoWithKey());
				cst = StringUtil.getStringValue(itemPedido.getProduto().cdClassificFiscal);
				ItemPedidoGrade itemPedidoGrade = ItemPedidoGradeService.getInstance().montaItemPedidoGradeByItemPedido(itemPedido);
				String descricaoGrade = StringUtil.getStringValue(ItemPedidoGradeService.getInstance().getDescricaoGradeResumida(itemPedidoGrade));
				if (ValueUtil.isNotEmpty(cst)) {
					cst = " - " + cst;
				}
				larguraTexto = smallFont.fm.stringWidth(descricaoProduto) + smallFont.fm.stringWidth(descricaoGrade) + smallFont.fm.stringWidth(cst);
				g.setFont(smallFontBold);
				if (larguraTexto > widthImage) {
					descricaoProduto = MessageUtil.quebraLinhas(descricaoProduto, smallFont, widthImage - spaceBetweenLines);
					descricaoMultiLinhas = StringUtil.split(descricaoProduto, '\n');
					for (int k = 0; k < 2; k++) {
						if (k == 1) {
							if (descricaoMultiLinhas.length > 1) {
								descricaoMultiLinhas[k] = StringUtil.getStringAbreviada(descricaoMultiLinhas[k], widthImage - smallFont.fm.stringWidth(descricaoGrade) - smallFont.fm.stringWidth(cst) - spaceBetweenLines * 2, smallFont);
								g.drawText(descricaoMultiLinhas[k] + descricaoGrade + cst, x, y);
							} else {
								g.drawText(descricaoGrade + cst, x, y);
							}
						} else {
							g.drawText(descricaoMultiLinhas[k], x, y);
						}
						y += smallFontBold.fm.height + spaceBetweenLines;
					}

				} else {
					g.drawText(descricaoProduto + descricaoGrade + cst, x, y);
					y += smallFont.fm.height + spaceBetweenLines;
				}
				g.setFont(smallFont);

				x = spaceBetweenLines;
				if (LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
					widthCelula = widthImage / 6;
				} else {
					widthCelula = widthImage / 7;
				}
				//Unidade
				String cdUnidade = StringUtil.getStringValue(itemPedido.cdUnidade);
				g.drawText(cdUnidade.length() > 3 ? cdUnidade.substring(0, 3) : cdUnidade, x, y);
				//Qtd do item
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), x, y);
				//Valor do unitário
				x += widthCelula;
				if (LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
					g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), x, y);
				} else {
					g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlBaseItemTabelaPreco), x, y);
					//Valor com desconto
					x += widthCelula;
					g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), x, y);
				}
				//Valor ICMS
				x += widthCelula;
				String vlIcmsItem = ValueUtil.VALOR_ZERO;
				vlIcmsItem = StringUtil.getStringValueToInterface(getIcmsItem(itemPedido));
				g.drawText(vlIcmsItem, x, y);
				//Valor AL. ICMS
				x += widthCelula;
				String aliquotaIcms = ValueUtil.VALOR_ZERO;
				if (LavenderePdaConfig.isUsaCalculoStItemPedido() && cliente != null && ValueUtil.isNotEmpty(cliente.cdTributacaoCliente)) {
					Tributacao tributacao = itemPedido.getTributacaoItem(cliente);
					aliquotaIcms = StringUtil.getStringValueToInterface(tributacao != null ? tributacao.vlPctIcms : 0);
				}
				g.drawText(aliquotaIcms, x, y);
				//Valor total
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido), x - spaceBetweenLines, y);
			} else {
				//Descrição do item
				y += smallFont.fm.height + spaceBetweenLines;
				x = spaceBetweenLines;
				descricaoProduto = LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(itemPedido.getDsProduto()) : StringUtil.getStringValue(itemPedido.getDsProdutoWithKey());
				cst = StringUtil.getStringValue(itemPedido.getProduto().cdClassificFiscal);
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
				String cdUnidade = StringUtil.getStringValue(itemPedido.cdUnidade);
				g.drawText(cdUnidade.length() > 3 ? cdUnidade.substring(0, 3) : cdUnidade, x, y);
				//Qtd do item
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), x, y);
				//Valor do unitário
				x += widthCelula;
				if (LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
					g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), x, y);
				} else {
					g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlBaseItemTabelaPreco), x, y);
					//Valor com desconto
					x += widthCelula;
					g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), x, y);
				}
				//Valor ICMS
				x += widthCelula;
				String vlIcmsItem = ValueUtil.VALOR_ZERO;
				vlIcmsItem = StringUtil.getStringValueToInterface(getIcmsItem(itemPedido));
				g.drawText(vlIcmsItem, x, y);
				//Valor AL. ICMS
				x += widthCelula;
				String aliquotaIcms = ValueUtil.VALOR_ZERO;
				if (LavenderePdaConfig.isUsaCalculoStItemPedido() && cliente != null && ValueUtil.isNotEmpty(cliente.cdTributacaoCliente)) {
					Tributacao tributacao = itemPedido.getTributacaoItem(cliente);
					aliquotaIcms = StringUtil.getStringValueToInterface(tributacao != null ? tributacao.vlPctIcms : 0);
				}
				g.drawText(aliquotaIcms, x, y);
				//Valor total
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido), x - spaceBetweenLines, y);
			}
		}
		return y;
	}

	private int montaLayoutCalculoImpostoNfeContingencia(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int alturaLinha = 0;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		alturaLinha =  (smallFont.fm.height + spaceBetweenLines) * 2;
		g.drawRoundRect(0, y, widthImage, alturaLinha * 2 + smallFontBold.fm.height, 8);
		
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_CALCULO_IMPOSTO, x, y);
		g.setFont(smallFont);
		y += smallFont.fm.height;
		g.drawLine(0, y, widthImage, y);
		g.setFont(smallFontBold);
		//Label ICMS
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VALOR_ICMS, x, y);
		x += widthCelula;
		g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		//Substituição Tributária
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VALOR_ICMS_SUB, x, y);
		x += widthCelula;
		g.drawLine(widthCelula * 2, y, widthCelula * 2, y  +  alturaLinha);
		//Label IPI
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VALOR_IPI, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.setFont(smallFont);
		//Valor ICMS
		String vlTotalIcms = LavenderePdaConfig.isUsaCalculoStItemPedido() ? StringUtil.getStringValueToInterface(vlIcmsPedido) : ValueUtil.VALOR_ZERO;
		g.drawText(vlTotalIcms, x, y);
		x += widthCelula;
		//Substituição Tributária
		String vlTotalSt = LavenderePdaConfig.isUsaCalculoStItemPedido() ? StringUtil.getStringValueToInterface(vlIcmsSubPedido) : ValueUtil.VALOR_ZERO;
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
		//Label Frete
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VALOR_FRETE, x, y);
		x += widthCelula;
		g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		if (!LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
			//Label Desconto
			g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VALOR_DESCONTO, x, y);
			x += widthCelula;
			g.drawLine(widthCelula * 2, y, widthCelula * 2, y  +  alturaLinha);
		}
		//Label Total Nota
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_VALOR_TOTAL_NOTA, x, y);
		x += widthCelula;
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.setFont(smallFont);
		//Valor Frete
		String vlFrete = StringUtil.getStringValueToInterface(pedido.vlFrete);
		g.drawText(vlFrete, x, y);
		x += widthCelula;
		if (!LavenderePdaConfig.ocultaCamposDescontosImpressaoContingenciaNfe) {
			//Valor Desconto
			String vlDesconto = StringUtil.getStringValueToInterface(pedido.vlTotalBrutoItens - pedido.vlTotalPedido);
			g.drawText(vlDesconto, x, y);
			x += widthCelula;
		}
		//Valor Total Nota
		String vlTotalNota = StringUtil.getStringValueToInterface(pedido.vlTotalPedido);
		g.drawText(vlTotalNota, x, y);
		x += widthCelula;
		y +=  spaceBetweenLines;		
		return y;
	}
	
	private int montaLayoutDadosAdicionaisNfeContingencia(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int alturaTexto = 0;
		int alturaRect = 0;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		alturaRect = ((smallFont.fm.height + spaceBetweenLines) * 3) + smallFontBold.fm.height + spaceBetweenLines * 2;
		g.drawRoundRect(0, y, widthImage, alturaRect, 8);
		alturaTexto = y;
		g.drawText(Messages.IMPRESSAONFECONTINGENCIA_DADOS_ADICIONAIS, x, y);
		//yTextoDescricao é a posição inicial do texto de descrição dos dados adicionais 
        int yTextoDescricao = y + smallFontBold.fm.height;
		x += widthCelula * 2;
		String[] labelMultiLinhas;
		String label = MessageUtil.quebraLinhas(Messages.IMPRESSAONFECONTINGENCIA_RESERVADO_FISCO, smallFont, widthCelula - spaceBetweenLines);
		labelMultiLinhas = StringUtil.split(label, '\n');
		for (int j = 0; j < labelMultiLinhas.length; j++) {
			g.drawText(labelMultiLinhas[j], x, y);
			y += smallFontBold.fm.height;
		}
		g.setFont(smallFont);
		widthCelula = widthCelula * 2;
		x = spaceBetweenLines;
		alturaRect -= spaceBetweenLines / 2;
		g.drawLine(widthCelula, alturaTexto, widthCelula, alturaTexto + alturaRect);
		String descricao = getDescricaoComNuNotaGeral();
		int larguraTexto = smallFont.fm.stringWidth(descricao);
		g.setFont(smallFont);
		int yTextoDescricaoDiferenca = 0;
		if (larguraTexto > widthCelula) {
			descricao = MessageUtil.quebraLinhas(descricao, smallFont, widthCelula - spaceBetweenLines);
			String[] descricaoMultiLinhas;
			descricaoMultiLinhas = StringUtil.split(descricao, '\n');
			for (int j = 0; j < descricaoMultiLinhas.length; j++) {
				g.drawText(descricaoMultiLinhas[j], x, yTextoDescricao);
				yTextoDescricaoDiferenca += smallFontBold.fm.height; 
                yTextoDescricao += smallFontBold.fm.height;
			}
		} else {
			g.drawText(descricao, x, yTextoDescricao);
			yTextoDescricaoDiferenca += smallFontBold.fm.height + spaceBetweenLines; 
            yTextoDescricao += smallFontBold.fm.height + spaceBetweenLines;
		}
		// Acrescenta o y que foi utilizado para a descrição dos dados adicionais 
        y = y + yTextoDescricaoDiferenca;
		return y;
	}
	
	private String getDescricaoComNuNotaGeral() throws SQLException { 
		String descricaoNuNotaGeral = null; 
		if (LavenderePdaConfig.isUsaAdicionaNuNotaGeralNaNotaFiscal()) { 
			Representante representante = (Representante) RepresentanteService.getInstance().getRepresentanteById(pedido.cdRepresentante); 
			descricaoNuNotaGeral = Messages.IMPRESSAONFECONTINGENCIA_NU_NOTA_GERAL_REPRESENTANTE + representante.nuNotaGeral + '\n'; 
		}
		String descricao = StringUtil.getStringValue(pedido.dsObservacao); 
		if (ValueUtil.isNotEmpty(descricaoNuNotaGeral)) { 
			descricao = descricaoNuNotaGeral + descricao; 
			if (descricao.length() > MAX_LENGHT_TEXTO_DADOS_ADICIONAIS) { 
				descricao = descricao.substring(0, MAX_LENGHT_TEXTO_DADOS_ADICIONAIS).concat("..."); 
			} 
		}
		return descricao; 
	}
	
	private Vector getItemPedidoRefList() {
		Vector itemPedidoRefList = new Vector();
		Map<ItemPedidoAgrupador, ItemPedido> itemPedidoMap = new HashMap<ItemPedidoAgrupador, ItemPedido>();
		Vector itemPedidoCloneList = getItemPedidoCloneList();
		int size = itemPedidoCloneList.size();
		for (int i = 0; i < size; i++) {
			ItemPedido itemPedido = (ItemPedido) itemPedidoCloneList.items[i];
			ItemPedido semelhantes = (ItemPedido) itemPedidoMap.get(new ItemPedidoAgrupador(itemPedido));
			if (semelhantes == null) {
				itemPedidoMap.put(new ItemPedidoAgrupador(itemPedido), itemPedido);
				itemPedidoRefList.addElement(itemPedido);
			} else {
				semelhantes.setQtItemFisico(semelhantes.getQtItemFisico() + itemPedido.getQtItemFisico());
				semelhantes.vlTotalItemPedido += itemPedido.vlTotalItemPedido;
				semelhantes.vlTotalIcmsItem += itemPedido.vlTotalIcmsItem;
				itemPedidoRefList.removeElement(semelhantes);
				itemPedidoRefList.addElement(semelhantes);
			}
		}
		return itemPedidoRefList;
	}
	
	private Vector getItemPedidoCloneList() {
		Vector itemPedidoCloneList = new Vector();
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			ItemPedido itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			ItemPedido newItemPedido = (ItemPedido) itemPedido.clone();
			itemPedidoCloneList.addElement(newItemPedido);
		}
		return itemPedidoCloneList;
	}
	
}

class ItemPedidoAgrupador {
	
    private ItemPedido itemPedido;
    
    ItemPedidoAgrupador(ItemPedido itemPedido) {
        this.itemPedido = itemPedido; 
    }

    public boolean equals(Object o) {
    	ItemPedido outroItemPedido = ((ItemPedidoAgrupador)o).itemPedido;
    	try {
			return ValueUtil.valueEquals(outroItemPedido.getProduto().dsReferenciaProduto, itemPedido.getProduto().dsReferenciaProduto) &&
					ValueUtil.valueEquals(outroItemPedido.vlItemPedido, itemPedido.vlItemPedido) &&
					ValueUtil.valueEquals(outroItemPedido.cdTributacaoConfig, itemPedido.cdTributacaoConfig);
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
			return false;
		}
    }

    public int hashCode() {
    	String dsReferenciaProd = "";
		try {
			dsReferenciaProd = StringUtil.getStringValue(itemPedido.getProduto() != null && ValueUtil.isNotEmpty(itemPedido.getProduto().dsReferenciaProduto) ? itemPedido.getProduto().dsReferenciaProduto : "");
		} catch (SQLException e) {
			ExceptionUtil.handle(e);
		}
    	String cdTributacaoConfig = StringUtil.getStringValue(itemPedido.cdTributacaoConfig);
    	long fator1 = 0;
    	double fator2 = itemPedido.vlItemPedido;
    	long fator3 = 1;
    
    	char [] refToChar = dsReferenciaProd.toCharArray();
    	for (long c : refToChar) {
    		fator1 += c * c; 
    	}
    	char [] tribToChar = cdTributacaoConfig.toCharArray();
    	for (long c : tribToChar) {
    		fator3 += c * c + 1; 
    	}
    	int result = (int) (fator1 * fator2 / fator3);
    	return result;
    }

}
