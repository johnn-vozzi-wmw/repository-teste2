package br.com.wmw.lavenderepda.presentation.ui;

import java.sql.SQLException;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseUIForm;
import br.com.wmw.framework.presentation.ui.ext.BaseButton;
import br.com.wmw.framework.presentation.ui.ext.ButtonAction;
import br.com.wmw.framework.presentation.ui.ext.EditNumberInt;
import br.com.wmw.framework.presentation.ui.ext.EditText;
import br.com.wmw.framework.presentation.ui.ext.LabelName;
import br.com.wmw.framework.presentation.ui.ext.LoadingBoxWindow;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.AbstractPrinter;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.Printer;
import br.com.wmw.framework.print.StarPrinter;
import br.com.wmw.framework.print.StarPrinterSimulator;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.ColorUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.io.File;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.ui.event.ControlEvent;
import totalcross.ui.event.Event;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;

public class ImpressaoTesteForm extends BaseUIForm {

	private LabelName lbEnderecoImpressora, lbPortaImpressora;
	private EditText edNomeImpressora;
	private EditNumberInt edPortaImpressora;

	private BaseButton btChangeEnderecoImpressao;
	private BaseButton btImprimirTest;
	protected ButtonAction btSalvar;
	RemoteDevice remoteDeviceSelected;
	private String dsEnderecoImpressora;

	public ImpressaoTesteForm() {
		super(Messages.IMPRESSAO_CONFIG_TITULO);
		btImprimirTest = new BaseButton(Messages.IMPRESSAO_CONFIG_IMPRIMIR_TESTE);
		btChangeEnderecoImpressao = new BaseButton(" ... ");
        btSalvar = new ButtonAction(null, UiUtil.getColorfulImage("images/ok.png", (int)(UiUtil.getControlPreferredHeight() * 0.9), (int)(UiUtil.getControlPreferredHeight() * 0.9), ColorUtil.baseAppBarForeColor));
		edNomeImpressora = new EditText("@@@@@@@@@", 100);
		edNomeImpressora.setEnabled(false);
		edPortaImpressora = new EditNumberInt("@@@@", 4);
		//--
		lbEnderecoImpressora = new LabelName(Messages.IMPRESSAO_CONFIG_ENDERECO);
		lbPortaImpressora = new LabelName(Messages.IMPRESSAO_CONFIG_PORTA);
	}

	//@Override
	protected void onFormStart() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(false);
		UiUtil.add(barTopContainer, btSalvar, RIGHT - (WIDTH_GAP * 2), CENTER, PREFERRED);
		//--
		UiUtil.add(this, lbEnderecoImpressora, getLeft(), getTop() + HEIGHT_GAP_BIG, FILL, PREFERRED);
		if (VmUtil.isSimulador()) {
			UiUtil.add(this, edNomeImpressora, getLeft(), AFTER);
		} else {
			UiUtil.add(this, edNomeImpressora, getLeft(), AFTER, FILL - btChangeEnderecoImpressao.getPreferredWidth() - WIDTH_GAP_BIG - WIDTH_GAP);
			UiUtil.add(this, btChangeEnderecoImpressao, AFTER + WIDTH_GAP, SAME, PREFERRED, SAME);
		}
		UiUtil.add(this, lbPortaImpressora, edPortaImpressora, getLeft(), AFTER + HEIGHT_GAP);
		UiUtil.add(this, btImprimirTest, getLeft() + WIDTH_GAP_BIG, AFTER + HEIGHT_GAP_BIG, getWFill() - WIDTH_GAP_BIG);
	}

	public void onFormShow() throws SQLException {
		super.onFormShow();
		String enderecoImpressora = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configNomeImpressora);
		edNomeImpressora.setText(enderecoImpressora);
		String portaImpressora = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configPortaImpressora);
		edPortaImpressora.setText(portaImpressora);
		dsEnderecoImpressora = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configEnderecoImpressora);

	}

	//@Override
	protected void onFormEvent(Event event) throws SQLException {
		switch (event.type) {
			case ControlEvent.PRESSED: {
				if (event.target == btChangeEnderecoImpressao) {
					try {
						remoteDeviceSelected = BluetoothUtil.chooseRemoteDeviceBluetooth();
						if (remoteDeviceSelected != null) {
							edNomeImpressora.setText(remoteDeviceSelected.getFriendlyName());
							dsEnderecoImpressora = remoteDeviceSelected.getBluetoothAddress();
						}
					} catch (Throwable e) {
			    		UiUtil.showErrorMessage(e);
			    	}
				} else if (event.target == btImprimirTest) {
					btPrintClick();
				} else if (event.target == btSalvar) {
					btSalvarClick();
				}
				break;
			}
		}
	}

	private void btSalvarClick() throws SQLException {
		if (remoteDeviceSelected != null) {
			ConfigInternoService.getInstance().addValue(ConfigInterno.configEnderecoImpressora, remoteDeviceSelected.getBluetoothAddress());
			ConfigInternoService.getInstance().addValue(ConfigInterno.configPortaImpressora, edPortaImpressora.getText());
			ConfigInternoService.getInstance().addValue(ConfigInterno.configNomeImpressora, edNomeImpressora.getText());
		}
		close();
	}

	private void btPrintClick() {
		LoadingBoxWindow pb = UiUtil.createProcessingMessage();
		pb.popupNonBlocking();
    	try {
    		AbstractPrinter printer = null;
    		if (VmUtil.isSimulador()) {
				printer = new StarPrinterSimulator();
    		} else {
    			if (ValueUtil.isEmpty(dsEnderecoImpressora)) {
    				throw new ValidationException(Messages.IMPRESSAO_CONFIG_ENDERECO_ERROR);
    			}
    			if (LavenderePdaConfig.isUsaLayout3ConfigImpressaoNfeViaBluetooth() || LavenderePdaConfig.isUsaLayout6ConfigImpressaoNfeViaBluetooth()) {
    				printer = MPTPrinter.getNewInstance(dsEnderecoImpressora, 0);
				} else if (LavenderePdaConfig.isUsaLayout4ConfigImpressaoNfeViaBluetooth()) {
					printer = new ZonerichPrinter(dsEnderecoImpressora, 0);
				} else {
					printer = new StarPrinter(dsEnderecoImpressora, 0);
				}
    		}
    		//--
    		
    		if (LavenderePdaConfig.isUsaLayout4ConfigImpressaoNfeViaBluetooth() || LavenderePdaConfig.isUsaLayout6ConfigImpressaoNfeViaBluetooth()) {
    			MonoImage im = new MonoImage(576, 576);
    			Font fontBold = Font.getFont("arialnoaa", true, 20);
    			Font font20 = Font.getFont("arialnoaa", false, 20);
    			Font font25 = Font.getFont("arialnoaa", false, 25);
    			Font font30 = Font.getFont("arialnoaa", false, 30);
    			Font font35 = Font.getFont("arialnoaa", false, 35);
    			Graphics g = im.getGraphics();
	    		g.setFont(font20);
				g.backColor = Color.WHITE;
				g.fillRect(0, 0, im.getWidth(), im.getHeight());
				g.foreColor = 0xFF000000;
				g.drawRect(0, 30, im.getWidth(), im.getHeight() - 120);
				g.drawRect(1, 31, im.getWidth() - 2, im.getHeight() - 121);
				g.setFont(fontBold);
				g.drawText("Teste impressao em negrito", 5, 35);
				g.setFont(font20);
				g.drawText("Teste de impressão tamanho 20", 5, 50);
				g.setFont(font25);
				g.drawText("Teste de impressão tamanho 25", 5, 65);
				g.setFont(font30);
				g.drawText("Teste de impressão tamanho 30", 5, 85);
				g.setFont(font35);
				g.drawText("Teste de impressão tamanho 35", 5, 105);
				
				g.drawRect(50, 170, 250, 250);
				g.drawLine(50, 170, 300, 420);
				g.drawLine(50, 420, 300, 170);
				g.setFont(font20);
				g.drawText("12345678901112131415161718192021222324252627282930", 8, 430);
				if (VmUtil.isSimulador()) {
					String nmImagePrint = "C:/testeImpressao.png";
					File file = null;
					FileUtil.deleteFile(nmImagePrint);
					file = FileUtil.criaFile(nmImagePrint);
					im.createPng(file);
					file.close();
					UiUtil.showSucessMessage("Imagem da impressão gerada: " + nmImagePrint);
				} else if (LavenderePdaConfig.isUsaLayout3ConfigImpressaoNfeViaBluetooth() || LavenderePdaConfig.isUsaLayout6ConfigImpressaoNfeViaBluetooth()) {
					MPTPrinter mptPrinter = (MPTPrinter) printer;
					mptPrinter.printImg(im);
					mptPrinter.close();
				} else if (LavenderePdaConfig.isUsaLayout4ConfigImpressaoNfeViaBluetooth()) {
					ZonerichPrinter zonerichPrinter = (ZonerichPrinter) printer;
					zonerichPrinter.printImg(im);
					zonerichPrinter.close();
				}
    		} else {
	    		printer.append("TITULO DA PARADA", Printer.BOLD, Printer.CENTER, 100);
	    		printer.skipLine();
	    		printer.append("subtitulo da parada", Printer.CENTER, 100);
	    		printer.skipLine();
	    		printer.append("-----------------------------------------------------------------", Printer.BOLD, Printer.LEFT, 100);
	    		printer.skipLine();
	    		printer.append("Esquerda", Printer.LEFT, 50);
	    		printer.append("Direita", Printer.RIGHT, 50);
	    		printer.skipLine();
	    		printer.append("012345678910111213141516171819202122232425262728293031323334353637383940", Printer.CENTER, 100);
	    		printer.skipLine();
	    		printer.append("Centro Vermelho", Printer.RED, Printer.CENTER, 100);
	    		printer.skipLine();
	    		printer.append("____________________________________________________________", Printer.BOLD, Printer.LEFT, 100);
	    		printer.skipLine();
	    		printer.append("|", Printer.LEFT, 50);
	    		printer.append("|", Printer.RIGHT, 50);
	    		printer.skipLine();
	    		printer.append("____________________________________________________________", Printer.LEFT, 100);
	    		printer.skipLine();
	    		printer.append("Centro Vermelho Negrito", new char[]{Printer.RED, Printer.BOLD}, Printer.CENTER, 100);
	    		printer.skipLine();
	    		printer.print();
    		}
    	} catch (Throwable e) {
    		UiUtil.showErrorMessage(e);
    	} finally {
    		pb.unpop();
    	}
    }

	//@Override
	public void onFormClose() throws SQLException {
		MainLavenderePda.getInstance().setMenuVisibility(true);
	}

}
