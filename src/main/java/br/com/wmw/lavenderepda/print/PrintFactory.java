package br.com.wmw.lavenderepda.print;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.PrinterUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.sys.Settings;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.ImageException;

import java.sql.SQLException;

public class PrintFactory {

	protected static final String FONT_THERMAL = "arialnoaa";
	protected static final int IMAGEM_WIDTH = 576;
	protected static final int SPACE_INIT_LINE = 5;
	protected static final int SPACE_FINAL = 150;

	public static final double DEVICE_SCREEN_DENSITY = Settings.screenDensity;
	public static final double PRINT_SCREEN_DENSITY = VmUtil.isSimulador() ? 0.7 : 0.8;

	protected int currentHeight = 0;
	protected Font font;
	protected Font fontBold;
	protected MonoImage img;
	protected MPTPrinter mptPrinter;
	protected ZonerichPrinter zonerichPrinter;
	protected String enderecoImpressora;
	protected int portaImpressora;
	
	//LavenderePdaConfig.usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante
	protected void print(String tipoImpressao, String nomeArquivo) throws IOException, ImageException {
		if (img == null) return;
		
		if (VmUtil.isSimulador()) {
			printToFile(nomeArquivo);
			return;
		} 
		
		if ("3".equals(tipoImpressao) || "6".equals(tipoImpressao)) {
			mptPrinter.printImg(img);
			return;
		} 
		
		if ("4".equals(tipoImpressao)) {
			zonerichPrinter.printImg(img);
			return;
		}
	}
	
	//fechamentoEstoque.png
	private void printToFile(String nomeArquivo) throws ImageException, IOException {
		String nmImagePrint = Settings.appPath + "/" + nomeArquivo; 
		FileUtil.deleteFile(nmImagePrint);
		File file = FileUtil.criaFile(nmImagePrint);
		img.createPng(file);
		file.close();
		UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
	}
	
	
	protected void setFonts() {
		font = Font.getFont(false, 22);
		fontBold = Font.getFont(true, 22);
		if (!VmUtil.isSimulador()) {
			font = Font.getFont(FONT_THERMAL, false, 22);
			fontBold = Font.getFont(FONT_THERMAL, true, 22);
		}
	}
	
	protected Graphics getGrafics(int imageHeigth) throws ImageException {
		img = new MonoImage(IMAGEM_WIDTH, imageHeigth + SPACE_FINAL);
		Graphics g = img.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, currentHeight, IMAGEM_WIDTH, imageHeigth + SPACE_FINAL);
		return g;
	}
	
	protected void setPrinterParameters() throws Exception {
		if (VmUtil.isSimulador()) return;
		
		enderecoImpressora = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configEnderecoImpressora);
		portaImpressora = ValueUtil.getIntegerValue(ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configPortaImpressora));
		if (ValueUtil.isEmpty(enderecoImpressora)) {
			RemoteDevice remoteDevice = BluetoothUtil.chooseRemoteDeviceBluetooth();
			if (remoteDevice != null) {
				enderecoImpressora = remoteDevice.getBluetoothAddress();
				saveEnderecoPortaIfNecessary();
			} else {
				throw new ValidationException(Messages.PRINTER_SELECIONE_DISPOSITIVO_VALIDO);
			}
		}
		if (ValueUtil.isNotEmpty(enderecoImpressora)) {
			if ("3".equals(LavenderePdaConfig.usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante) || "6".equals(LavenderePdaConfig.usaImpressaoDevolucaoTotalEstoqueRemessaRepresentante)) {
				mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
				mptPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
			}
		}
	}
	
	protected int getAlignCenter(String text, int width) {
		return IMAGEM_WIDTH - ((width / 2) + (font.fm.stringWidth(text) / 2));
	}

	protected int getAlignRight(String text, int width, boolean isBold) {
		if (isBold) {
			return (width - fontBold.fm.stringWidth(text)) - SPACE_INIT_LINE;
		}
		return (width - font.fm.stringWidth(text)) - SPACE_INIT_LINE;
	}

	protected void addTextBold(Graphics g, String text, int positionColumn) {
		setFontBold(g);
		g.drawText(text, positionColumn, currentHeight);
	}

	protected void setFontBold(Graphics g) {
		g.setFont(fontBold);
	}

	protected void addDrawText(Graphics g, String text, int positionColumn) {
		setFontPattern(g);
		g.drawText(text, positionColumn, currentHeight);
	}

	protected void setFontPattern(Graphics g) {
		g.setFont(font);
	}
	
	protected int lineBreak() {
		return currentHeight += addLineSize();
	}
	
	protected int addLineSize() {
		return font.fm.height;
	}
	
	protected void addDrawLine(Graphics g) {
		g.drawLine(SPACE_INIT_LINE, currentHeight, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight);
	}
	
	protected int getAlignRight(String text, int width) {
		return (width - font.fm.stringWidth(text)) - SPACE_INIT_LINE;
	}

	protected void saveEnderecoPortaIfNecessary() throws SQLException {
		if (!ValueUtil.valueEquals(enderecoImpressora, ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configEnderecoImpressora))) {
			ConfigInternoService.getInstance().addValue(ConfigInterno.configEnderecoImpressora, enderecoImpressora);
		}
		if (VmUtil.isWinCEPocketPc() && portaImpressora == 0) {
			portaImpressora = PrinterUtil.getDefaultPort();
			ConfigInternoService.getInstance().addValue(ConfigInterno.configPortaImpressora, StringUtil.getStringValue(portaImpressora));
		}
	}
	
}
