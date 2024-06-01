package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;

import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.io.IOException;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Settings;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.ImageException;

public abstract class AbstractPrint {

	protected static final String FONT_THERMAL = "arialnoaa";
	protected static final int IMAGEM_WIDTH = 576;
	protected static final int SPACE_INIT_LINE = 5;
	protected static final int SPACE_FINAL = 150;

	public static final double DEVICE_SCREEN_DENSITY = Settings.screenDensity;
	public static final double PRINT_SCREEN_DENSITY = VmUtil.isSimulador() ? 0.7 : 0.8;
	
	protected Font font;
	protected Font fontBold;
	protected MonoImage img;
	protected int currentHeight = 0;
	protected MPTPrinter mptPrinter;
	protected ZonerichPrinter zonerichPrinter;
	protected MPTPrinter mtpPrinter;
	protected String enderecoImpressora;
	protected int portaImpressora;
	protected String valueParametroImpressao;
	
	protected void print() throws IOException, ImageException {
		if (img == null) return;
		
		if (VmUtil.isSimulador()) {
			printToFile();
			return;
		} 
		
		if ("3".equals(valueParametroImpressao) || "6".equals(valueParametroImpressao)) {
			mptPrinter.printImg(img);
			return;
		} 
		
		if ("4".equals(valueParametroImpressao)) {
			zonerichPrinter.printImg(img);
			return;
		}
	}
	
	protected abstract void printToFile() throws ImageException, IOException;
			
	protected void setPrinterParameters() throws SQLException, Exception, IOException, InvalidNumberException {
		if (VmUtil.isSimulador()) return;
		
		enderecoImpressora = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configEnderecoImpressora);
		portaImpressora = ValueUtil.getIntegerValue(ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configPortaImpressora));

		if (ValueUtil.isEmpty(enderecoImpressora)) {
			RemoteDevice remoteDevice = BluetoothUtil.chooseRemoteDeviceBluetooth();
			if (remoteDevice != null) {
				enderecoImpressora = remoteDevice.getBluetoothAddress();
				ConfigInternoService.getInstance().addValue(ConfigInterno.configEnderecoImpressora, enderecoImpressora);
			}
		}
		if (ValueUtil.isNotEmpty(enderecoImpressora)) {
			if ("3".equals(valueParametroImpressao) || "6".equals(valueParametroImpressao)) {
				mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
				mptPrinter.setNuCopies(1);
			} else if ("4".equals(valueParametroImpressao)) {
				zonerichPrinter = new ZonerichPrinter(enderecoImpressora, portaImpressora);
				zonerichPrinter.setNuCopies(1);
			}
		}
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
	
	protected void addDrawLine(Graphics g) {
		g.drawLine(SPACE_INIT_LINE, currentHeight, IMAGEM_WIDTH - SPACE_INIT_LINE, currentHeight);
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
		g.setFont(fontBold);
		g.drawText(text, positionColumn, currentHeight);
	}

	protected void addDrawText(Graphics g, String text, int positionColumn) {
		g.setFont(font);
		g.drawText(text, positionColumn, currentHeight);
	}
	
	protected int lineBreak() {
		return currentHeight += addLineSize();
	}
	
	protected int addLineSize() {
		return font.fm.height;
	}
}
