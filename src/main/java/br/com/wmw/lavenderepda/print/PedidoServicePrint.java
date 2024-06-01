package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import br.com.wmw.framework.business.domain.Personalizacao;
import br.com.wmw.framework.business.service.PersonalizacaoService;
import br.com.wmw.framework.config.FrameworkMessages;
import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.presentation.ui.BaseContainer;
import br.com.wmw.framework.presentation.ui.ext.UiUtil;
import br.com.wmw.framework.print.AbstractPrinter;
import br.com.wmw.framework.print.Dpp250Printer;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.MonoImage;
import br.com.wmw.framework.print.Printer;
import br.com.wmw.framework.print.StarPrinter;
import br.com.wmw.framework.print.StarPrinterSimulator;
import br.com.wmw.framework.print.ZebraPrinter;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.BarCodeUtil;
import br.com.wmw.framework.util.BarCodeUtil.BarcodeType;
import br.com.wmw.framework.util.BluetoothUtil;
import br.com.wmw.framework.util.DateUtil;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.SessionLavenderePda;
import br.com.wmw.lavenderepda.business.conf.LavenderePdaConfig;
import br.com.wmw.lavenderepda.business.domain.BoletoConfig;
import br.com.wmw.lavenderepda.business.domain.Cliente;
import br.com.wmw.lavenderepda.business.domain.CondicaoPagamento;
import br.com.wmw.lavenderepda.business.domain.Empresa;
import br.com.wmw.lavenderepda.business.domain.EmpresaFilialImp;
import br.com.wmw.lavenderepda.business.domain.ItemNfe;
import br.com.wmw.lavenderepda.business.domain.ItemNfeReferencia;
import br.com.wmw.lavenderepda.business.domain.ItemPedido;
import br.com.wmw.lavenderepda.business.domain.ItemPedidoGrade;
import br.com.wmw.lavenderepda.business.domain.Nfe;
import br.com.wmw.lavenderepda.business.domain.PagamentoPedido;
import br.com.wmw.lavenderepda.business.domain.ParcelaPedido;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.domain.PedidoBoleto;
import br.com.wmw.lavenderepda.business.domain.RemessaEstoque;
import br.com.wmw.lavenderepda.business.domain.Representante;
import br.com.wmw.lavenderepda.business.domain.TipoPagamento;
import br.com.wmw.lavenderepda.business.domain.Transportadora;
import br.com.wmw.lavenderepda.business.domain.VenctoPagamentoPedido;
import br.com.wmw.lavenderepda.business.service.BoletoConfigService;
import br.com.wmw.lavenderepda.business.service.ClienteService;
import br.com.wmw.lavenderepda.business.service.CondicaoPagamentoService;
import br.com.wmw.lavenderepda.business.service.EmpresaFilialImpService;
import br.com.wmw.lavenderepda.business.service.EmpresaService;
import br.com.wmw.lavenderepda.business.service.ItemNfeService;
import br.com.wmw.lavenderepda.business.service.ItemPedidoGradeService;
import br.com.wmw.lavenderepda.business.service.ParcelaPedidoService;
import br.com.wmw.lavenderepda.business.service.PedidoBoletoService;
import br.com.wmw.lavenderepda.business.service.RemessaEstoqueService;
import br.com.wmw.lavenderepda.business.service.RepresentanteService;
import br.com.wmw.lavenderepda.business.service.TipoPagamentoService;
import br.com.wmw.lavenderepda.business.service.TransportadoraService;
import br.com.wmw.lavenderepda.business.service.VenctoPagamentoPedidoService;
import br.com.wmw.lavenderepda.util.Util;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.io.device.bluetooth.RemoteDevice;
import totalcross.sys.Convert;
import totalcross.sys.InvalidNumberException;
import totalcross.sys.Settings;
import totalcross.sys.Vm;
import totalcross.ui.font.Font;
import totalcross.ui.gfx.Color;
import totalcross.ui.gfx.Graphics;
import totalcross.ui.image.Image;
import totalcross.ui.image.ImageException;
import totalcross.util.Date;
import totalcross.util.Vector;

public class PedidoServicePrint extends AbstractPedidoServicePrint {

	protected static final int MAX_LENGHT_TEXTO_DADOS_ADICIONAIS = 155;
	public static final int QTD_MAX_ITENS_POR_PAGINA = 21;
	public static final int ZEBRA_WIDTH_IMAGE = 400;
	public static final String ZEBRA_TIPO_FONTE = "TcFont";
	public static final int ZEBRA_SMALLFONTSIZE = 20;
	public static final int MPT_WIDTH_IMAGE = 576;
	public static final String MPT_TIPO_FONTE = "arialnoaa";
	public static final int MPT_SMALLFONTSIZE = 22;
	public static final int DPP250_WIDTH_IMAGE = 370;
	public static final int TAMANHO_OBRIGATORIO_CHAVE_ACESSO_NFE = 44;
	public static final String NM_LOGO_EMPRESA_IMPRESSAO = "logo_empresa_impressao.png";
	protected AbstractPrinter printer;
	protected ZebraPrinter zebraPrinter;
	protected MPTPrinter mptPrinter;
	protected Dpp250Printer dpp250Printer;
	protected ZonerichPrinter zonerichPrinter;
	private static final int QT_CARACTERES_LINHA = 51;
	private static final int QT_CARACTERES_LINHA_DADOS_ADICIONAIS = 35;
	
	private String tipoFonte;
	private int qtLinhas;
	private Vector empresaFilialImpList;
	private int nuCasasDecimaisValoresMonetarios;

	public PedidoServicePrint(Pedido pedido) throws SQLException {
		super(pedido);
	}

	/**
	 * Imprime o pedido no layout de impressão matricial ou térmica, dependendo da configuração
	 * @throws Exception
	 */
	public void imprimePedido() throws Exception {
		if (VmUtil.isSimulador()) {
			if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth > 0) {
				printer = new StarPrinterSimulator();
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
				if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 1) {
					printer = new StarPrinter(enderecoImpressora, portaImpressora);
					printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
				} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 2) {
					zebraPrinter = new ZebraPrinter(enderecoImpressora, portaImpressora);
				} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 6) {
					mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
					mptPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
				} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 4) {
					zonerichPrinter = new ZonerichPrinter(enderecoImpressora, portaImpressora);
					zonerichPrinter.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
				} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 5) {
					dpp250Printer = Dpp250Printer.getNewInstance(enderecoImpressora, portaImpressora);
					dpp250Printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
				}
			}
		}
		
		//--
		if (pedido != null) {
			Cliente cliente = (Cliente) ClienteService.getInstance().findByRowKeyDyn(pedido.getCliente().getRowKey());
			if (cliente != null) {
				Settings.screenDensity = PRINT_SCREEN_DENSITY;
				try {
					if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 1 && printer != null) {
						imprimePedidoLayoutImpressoraMatricial();
					} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 2 && (zebraPrinter != null || VmUtil.isSimulador())) {
						setTipoFonte(ZEBRA_TIPO_FONTE);
						imprimePedidoLayoutImpressoraTermica(ZEBRA_WIDTH_IMAGE, ZEBRA_TIPO_FONTE, ZEBRA_SMALLFONTSIZE);
					} else if ((LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 6) && (mptPrinter != null || VmUtil.isSimulador())) {
						String fonte = !VmUtil.isSimulador() ? MPT_TIPO_FONTE : ZEBRA_TIPO_FONTE;
						setTipoFonte(fonte);
						imprimePedidoLayoutImpressoraTermica(MPT_WIDTH_IMAGE, fonte, !VmUtil.isSimulador() ? MPT_SMALLFONTSIZE : ZEBRA_SMALLFONTSIZE);
					} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 4 && (zonerichPrinter != null || VmUtil.isSimulador())) {
						String fonte = !VmUtil.isSimulador() ? MPT_TIPO_FONTE : ZEBRA_TIPO_FONTE;
						setTipoFonte(fonte);
						imprimePedidoLayoutImpressoraTermica(MPT_WIDTH_IMAGE, fonte, !VmUtil.isSimulador() ? MPT_SMALLFONTSIZE : ZEBRA_SMALLFONTSIZE);
					} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 5 && (dpp250Printer != null || VmUtil.isSimulador())) {
						String fonte = !VmUtil.isSimulador() ? MPT_TIPO_FONTE : ZEBRA_TIPO_FONTE;
						setTipoFonte(fonte);
						imprimePedidoLayoutImpressoraTermicaDpp250(DPP250_WIDTH_IMAGE, fonte, 22);
					}
				} finally {
					Settings.screenDensity = DEVICE_SCREEN_DENSITY;
				}
			} else {
				throw new ValidationException(Messages.IMPRESSAOPEDIDO_MSG_ERRO_CLIENTE_NAO_ENCONTRADO);
			}
		}
	}

	public void imprimeNfe() throws Exception {
		if (pedido != null) {
			MonoImage imageLayoutImpressoraTermica;
			initPrintToNfe();
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			try {
				imageLayoutImpressoraTermica = geraNfeLayoutImpressoraTermica();
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			imprimeImagemNfe(imageLayoutImpressoraTermica, "/impressaoNfe_" + pedido.nuPedido + ".png");
		}
	}
	
	public void imprimeComprovanteNfe()throws Exception {
		Vm.sleep(3000);
		if (pedido != null) {
			MonoImage imageLayoutImpressoraTermica;
			initPrintToNfe();
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			try {
				imageLayoutImpressoraTermica = geraComprovanteNfeLayoutImpressoraTermica();
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			imprimeImagemNfe(imageLayoutImpressoraTermica, "/impressaoComprovanteNfe_" + pedido.nuPedido + ".png");
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
		nuCasasDecimaisValoresMonetarios = LavenderePdaConfig.nuCasasDecimaisValoresMonetarios();
	}
	
	private void imprimeImagemNfe(MonoImage img, String fileName) throws ImageException, IOException {
		if (img != null) {
			if (VmUtil.isSimulador()) {
				String nmImagePrint = Settings.appPath + fileName; 
				FileUtil.deleteFile(nmImagePrint);
				File file = FileUtil.criaFile(nmImagePrint);
				img.createPng(file);
				file.close();
				UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
			} else if (LavenderePdaConfig.isUsaLayout3ConfigImpressaoNfeViaBluetooth() || LavenderePdaConfig.isUsaLayout6ConfigImpressaoNfeViaBluetooth()) {
				mptPrinter.printImg(img);
			} else if (LavenderePdaConfig.isUsaLayout4ConfigImpressaoNfeViaBluetooth()) {
				zonerichPrinter.printImg(img);
			}
		}
	}
	
	public void imprimeBoleto() throws Exception {
		imprimeBoleto(false);
	}
	
	public void imprimeBoleto(boolean somenteComprovanteBoleto) throws Exception {
		if (VmUtil.isSimulador()) {
			if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth > 0) {
				printer = new StarPrinterSimulator();
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
				if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoBoletoViaBluetooth == 6) {
					mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
				} else if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth == 4) {
					zonerichPrinter = new ZonerichPrinter(enderecoImpressora, portaImpressora);
				}
			}
		}
		if (pedido != null && ValueUtil.isNotEmpty(pedido.getPedidoBoletoList())) {
			Vector imgList = new Vector();
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			try {
				for (int i = 0; i < pedido.getPedidoBoletoList().size(); i++) {
					PedidoBoleto pedidoBoleto = (PedidoBoleto) pedido.getPedidoBoletoList().items[i];
					if (somenteComprovanteBoleto) {		
						imgList.addElement(geraComprovanteBoletoLayoutImpressoraTermica(pedidoBoleto));
					} else {
						imgList.addElement(geraBoletoLayoutImpressoraTermica(pedidoBoleto));
					}
				}
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			imprimeImagensBoleto(imgList, somenteComprovanteBoleto);
		}
	}
	
	private void imprimeImagensBoleto(Vector imgList, boolean somenteComprovanteBoleto) throws ImageException, IOException, SQLException {
		if (ValueUtil.isNotEmpty(imgList)) {
			if (VmUtil.isSimulador()) {
				String nmImagePrint = "";
				for (int i = 0; i < imgList.size(); i++) {
					MonoImage img = (MonoImage) imgList.items[i];
					if (somenteComprovanteBoleto) {                         
						nmImagePrint = Settings.appPath + "/impressaoComprovanteBoleto_" + pedido.nuPedido + "_" + (i + 1) + ".png";
					} else {
						nmImagePrint = Settings.appPath + "/impressaoBoleto_" + pedido.nuPedido + "_" + (i + 1) + ".png";
					}
					FileUtil.deleteFile(nmImagePrint);
					File file = FileUtil.criaFile(nmImagePrint);
					img.createPng(file);
					FileUtil.closeFile(file);
					UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
				}
			} else if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoBoletoViaBluetooth == 6) {
				mptPrinter.printImg(imgList);
			} else if (LavenderePdaConfig.usaImpressaoBoletoViaBluetooth == 4) {
				zonerichPrinter.printImg(imgList);
			}
		}
	}
	
	private void imprimePedidoLayoutImpressoraTermica(int widthImage, String tipoFonte, int smallFontSize) throws ImageException, IOException, SQLException {
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 80;
		Font bigFont = Font.getFont(tipoFonte, true, 22);
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		int heigthImage = 0;
		
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

		//Cabeçalho Empresa Filial
		if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			EmpresaFilialImp empresaFilialImp = EmpresaFilialImpService.getInstance().getDefaultFilter();
			empresaFilialImpList = EmpresaFilialImpService.getInstance().findAllByExample(empresaFilialImp);
		}

		//--
		int nuLinhas = LavenderePdaConfig.tipoPedidoOcultoNoPedido ? 17 : 18;
		if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			nuLinhas -= 11;
			if (ValueUtil.isNotEmpty(empresaFilialImpList)) {
				nuLinhas += Math.ceil(empresaFilialImpList.size() / 3d) * 3;
			}
		}
		if (logoEmpresa != null) {
			heigthImage = logoEmpresa.getHeight() + ((bigFont.fm.height + spaceBetweenLines) * 3) + ((smallFont.fm.height + spaceBetweenLines) * nuLinhas) + getHeightItensPedido(widthImage, smallFont);
		} else {
			heigthImage = (bigFont.fm.height + spaceBetweenLines) * 3 + ((smallFont.fm.height + spaceBetweenLines) * nuLinhas) + getHeightItensPedido(widthImage, smallFont);
		}
		if (LavenderePdaConfig.isUsaCamposAdicionaisImpressaoPedido) {
			String[] parametros = StringUtil.split(LavenderePdaConfig.usaCamposAdicionaisImpressaoPedido, ';');
			for (int i = 0; i < parametros.length; i++) {
				switch (parametros[i]) {
				case Pedido.CAMPOS_ADICIONAIS_IMPRESSAO_DATA:
					heigthImage += 40;
					break;
				default:
					break;
				}
			}
		}
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		g.foreColor = Color.BLACK;
		g.drawRect(0, y, widthImage, heigthImage);

		//Cabeçalho
		y = montaLayoutCabecalhoImpressoraTermica(g, y, logoEmpresa, widthImage, tipoFonte, smallFontSize);
		//Itens do pedido
		y = montaLayoutItensPedidoImpressoraTermica(g, y, widthImage, tipoFonte, smallFontSize);
		//Rodapé
		y = montaLayoutRodapeImpressoraTermina(g, y, widthImage, tipoFonte, smallFontSize);

		if (VmUtil.isSimulador()) {
			String nmImagePrint = Convert.appendPath(Settings.appPath, "impressaoPedido_" + pedido.nuPedido + ".png");
			FileUtil.deleteFile(nmImagePrint);
			file = FileUtil.criaFile(nmImagePrint);
			mi.createPng(file);
			file.close();
			UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
		} else {
			if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 2) {
				for (int i = 0; i < LavenderePdaConfig.qtCopiasImpressaoPedido; i++) {
					mi.printTo(zebraPrinter);
				}
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 6) {
				mptPrinter.printImg(mi);
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 4) {
				zonerichPrinter.printImg(mi);
			}
		} 
	}

	private int montaLayoutCabecalhoImpressoraTermica(Graphics g, int posicaoY, Image logoEmpresa, int widthImage, String tipoFonte, int smallFontSize) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		int larguraImpressa = 0;
		int larguraLabel = 0;
		Font bigFont = Font.getFont(tipoFonte, true, 22);
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		Font smallFontBold = Font.getFont(tipoFonte, true, smallFontSize);
		//--
		g.setFont(bigFont);
		Empresa empresa = EmpresaService.getInstance().getEmpresa(pedido.cdEmpresa);

		//Logo da empresa
		if (logoEmpresa != null) {

			y += spaceBetweenLines;
			x = ((widthImage / 2) - (logoEmpresa.getWidth() / 2)) + 3;
			g.drawImage(logoEmpresa, x, y);
		}

		//Detalhes da empresa
		if (!LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			y = drawInfoEmpresa(g, logoEmpresa, widthImage, y, spaceBetweenLines, larguraImpressa, bigFont, empresa);
		} else {
			if (logoEmpresa != null) {
				y += logoEmpresa.getHeight();
			}
			if (ValueUtil.isNotEmpty(empresaFilialImpList)) {
				y = drawCabecalhoEmpresaFilial(g, y, spaceBetweenLines, widthImage, smallFont);
			}
		}

		Font dtEmissaoNuPedidoFont;
		if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			dtEmissaoNuPedidoFont = bigFont;
		} else {
			dtEmissaoNuPedidoFont = smallFont;
		}
		//Data emissão
		g.setFont(dtEmissaoNuPedidoFont);
		y += bigFont.fm.height + spaceBetweenLines;
		widthCelula = widthImage / 2;
		x = spaceBetweenLines;
		g.drawRect(0, y, widthCelula, dtEmissaoNuPedidoFont.fm.height + spaceBetweenLines);
		g.drawText(Messages.IMPRESSAOPEDIDO_DATA_EMISSAO + StringUtil.getStringValue(pedido.dtEmissao), x, y + 2);

		//Número pedido
		x = widthCelula;
		x += spaceBetweenLines;
		g.drawRect(x - spaceBetweenLines - 1, y, widthCelula + 2, dtEmissaoNuPedidoFont.fm.height + spaceBetweenLines);
		g.drawText(Messages.IMPRESSAOPEDIDO_NU_PEDIDO + pedido.nuPedido, x, y + 2);
		
		if (LavenderePdaConfig.usaIndicacaoQuilometragemTempoNoPedido) {
			g.setFont(smallFont);
			y += bigFont.fm.height + spaceBetweenLines;
			widthCelula = widthImage / 4;
			x = spaceBetweenLines;
			g.drawRect(0, y - 3, widthCelula + 23, smallFont.fm.height + spaceBetweenLines);
			g.drawText(Messages.IMPRESSAOPEDIDO_NUKMINICIAL + pedido.nuKmInicial, x, y + 2);
			//--
			x = widthCelula;
			x += spaceBetweenLines;
			g.drawRect(x - spaceBetweenLines + 22, y - 3, widthCelula + 1, smallFont.fm.height + spaceBetweenLines);
			g.drawText(Messages.IMPRESSAOPEDIDO_NUKMFINAL + pedido.nuKmFinal, x + 21, y + 2);
			//--
			x = widthCelula;
			x += spaceBetweenLines;
			g.drawRect(x - spaceBetweenLines + widthCelula + 22, y - 3, widthCelula + 1, smallFont.fm.height + spaceBetweenLines);
			g.drawText(Messages.IMPRESSAOPEDIDO_HRINICIALINDICADO + StringUtil.getStringValue(pedido.hrInicialIndicado), x + widthCelula + 21, y + 2);
			//--
			x = widthCelula;
			x += spaceBetweenLines;
			g.drawRect(x - spaceBetweenLines + widthCelula * 2 + 22, y - 3, widthCelula - 21, smallFont.fm.height + spaceBetweenLines);
			g.drawText(Messages.IMPRESSAOPEDIDO_HRFINALINDICADO + StringUtil.getStringValue(pedido.hrFinalIndicado), x + widthCelula * 2 + 21, y + 2);
		}

		if (!LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1() && !LavenderePdaConfig.tipoPedidoOcultoNoPedido) {
			g.setFont(smallFont);
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(Messages.IMPRESSAOPEDIDO_TIPOPEDIDO + pedido.getTipoPedido(), x, y + 2);
			g.drawLine(0, y + smallFont.fm.height + spaceBetweenLines, widthImage, y + smallFont.fm.height + spaceBetweenLines);
		}
		
		//Dados do cliente
		Cliente cliente = (Cliente)ClienteService.getInstance().findByRowKeyDyn(pedido.getCliente().getRowKey());

		if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			g.setFont(smallFont);
		}
		//Código cliente
		y += smallFont.fm.height + spaceBetweenLines * 2;
		x = spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOPEDIDO_CODIGO + StringUtil.getStringValue(pedido.cdCliente), x, y);

		//Nome do cliente
		y += smallFont.fm.height + spaceBetweenLines;
		String nome = StringUtil.getStringValue(pedido.getCliente().getNmClienteImpressao());
		larguraTexto = smallFont.fm.stringWidth(nome);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_NOME);
		if (larguraTexto + larguraLabel > widthImage) {
			nome = StringUtil.getStringAbreviada(nome, widthImage - larguraLabel, smallFont);
		}
		if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			g.drawText(Messages.IMPRESSAOPEDIDO_NOME, x, y);
			g.setFont(smallFontBold);
			g.drawText(nome, x + larguraLabel, y);
			g.setFont(smallFont);
		} else {
			g.drawText(Messages.IMPRESSAOPEDIDO_NOME + nome, x, y);
		}

		//Detalhes do Cliente
		if (!LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			y = addExtraInfoCliente(g, widthImage, y, spaceBetweenLines, smallFont, cliente);
		}

		return y;
	}

	private int drawCabecalhoEmpresaFilial(Graphics g, int y, int spaceBetweenLines, int larguraImpressa, Font smallFont) {
		int size = empresaFilialImpList.size();
		int colSize = larguraImpressa / 3;
		int col1 = spaceBetweenLines * 5;
		int col2 = col1 + colSize;
		int col3 = col2 + colSize;
		g.setFont(smallFont);
		int index = 0;
		for (int i = 0; i < size; i += 3) {
			if (index > 0) {
				y += smallFont.fm.height + spaceBetweenLines;
			}
            EmpresaFilialImp empresaFilialImp1 = (EmpresaFilialImp) empresaFilialImpList.items[i];
			EmpresaFilialImp empresaFilialImp2 = null;
			if (size > i + 1) {
				empresaFilialImp2 = (EmpresaFilialImp) empresaFilialImpList.items[i + 1];
			}
			EmpresaFilialImp empresaFilialImp3 = null;
			if (size > i + 2) {
				empresaFilialImp3 = (EmpresaFilialImp) empresaFilialImpList.items[i + 2];
			}
			drawEmpresFilialDsRazaoSocial(g, empresaFilialImp1, col1, y);
			drawEmpresFilialDsRazaoSocial(g, empresaFilialImp2, col2, y);
			drawEmpresFilialDsRazaoSocial(g, empresaFilialImp3, col3, y);
			y += smallFont.fm.height + spaceBetweenLines;
			drawEmpresFilialNuTelefone(g, empresaFilialImp1, col1, y);
			drawEmpresFilialNuTelefone(g, empresaFilialImp2, col2, y);
			drawEmpresFilialNuTelefone(g, empresaFilialImp3, col3, y);
			y += smallFont.fm.height + spaceBetweenLines;
			drawEmpresFilialNuCelular(g, empresaFilialImp1, col1, y);
			drawEmpresFilialNuCelular(g, empresaFilialImp2, col2, y);
			drawEmpresFilialNuCelular(g, empresaFilialImp3, col3, y);
			y += spaceBetweenLines;
			index++;
		}
		return y;
	}

	private void drawEmpresFilialDsRazaoSocial(Graphics g, EmpresaFilialImp empresaFilialImp, int x, int y) {
		if (empresaFilialImp != null && ValueUtil.isNotEmpty(empresaFilialImp.dsRazaoSocialFilial)) {
			g.drawText(empresaFilialImp.dsRazaoSocialFilial, x, y);
		}
	}

	private void drawEmpresFilialNuTelefone(Graphics g, EmpresaFilialImp empresaFilialImp, int x, int y) {
		if (empresaFilialImp != null && ValueUtil.isNotEmpty(empresaFilialImp.nuTelefone)) {
			g.drawText(empresaFilialImp.nuTelefone, x, y);
		}
	}

	private void drawEmpresFilialNuCelular(Graphics g, EmpresaFilialImp empresaFilialImp, int x, int y) {
		if (empresaFilialImp != null && ValueUtil.isNotEmpty(empresaFilialImp.nuCelular)) {
			g.drawText(empresaFilialImp.nuCelular, x, y);
		}
	}

	private int addExtraInfoCliente(Graphics g, int widthImage, int y, int spaceBetweenLines, Font smallFont, Cliente cliente) throws SQLException {
		int x;
		int larguraTexto;
		int larguraLabel;
		int widthCelula;

		//CNPJ do cliente
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		String cnpjCliente = StringUtil.getStringValue(pedido.getCliente().nuCnpj);
		larguraTexto = smallFont.fm.stringWidth(cnpjCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_CNPJ_CPF);
		if (larguraTexto + larguraLabel > widthImage) {
			cnpjCliente = StringUtil.getStringAbreviada(cnpjCliente, widthImage - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_CNPJ_CPF + cnpjCliente, x, y);

		//IE do cliente
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		String ieCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("NUINSCRICAOESTADUAL"));
		larguraTexto = smallFont.fm.stringWidth(ieCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_IE);
		if (larguraTexto + larguraLabel > widthCelula) {
			ieCliente = StringUtil.getStringAbreviada(ieCliente, widthCelula - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_IE + ieCliente, x, y);

		//Endereço do cliente
		y += smallFont.fm.height + spaceBetweenLines;
		String endCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSLOGRADOUROCOMERCIAL"));
		larguraTexto = smallFont.fm.stringWidth(endCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_ENDERECO);
		if (larguraTexto + larguraLabel > widthImage) {
			endCliente = StringUtil.getStringAbreviada(endCliente, widthImage - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_ENDERECO + endCliente, x, y);

		//Bairro do cliente
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		String bairroCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSBAIRROCOMERCIAL"));
		larguraTexto = smallFont.fm.stringWidth(bairroCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_BAIRRO);
		if (larguraTexto + larguraLabel > widthCelula) {
			bairroCliente = StringUtil.getStringAbreviada(bairroCliente, widthCelula - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_BAIRRO + bairroCliente, x, y);

		//CEP do cliente
		x += widthCelula + spaceBetweenLines;
		String cepCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCEPCOMERCIAL"));
		larguraTexto = smallFont.fm.stringWidth(cepCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_CEP);
		if (larguraTexto + larguraLabel > widthCelula) {
			cepCliente = StringUtil.getStringAbreviada(cepCliente, widthCelula - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_CEP + cepCliente, x, y);

		//Cidade do cliente
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		String cidadeCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCIDADECOMERCIAL"));
		larguraTexto = smallFont.fm.stringWidth(cidadeCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_CIDADE);
		if (larguraTexto + larguraLabel > widthCelula) {
			cidadeCliente = StringUtil.getStringAbreviada(cidadeCliente, widthCelula - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_CIDADE + cidadeCliente, x, y);

		//UF do cliente
		x += widthCelula + spaceBetweenLines;
		String ufCliente = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSESTADOCOMERCIAL"));
		larguraTexto = smallFont.fm.stringWidth(ufCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_UF);
		if (larguraTexto + larguraLabel > widthCelula) {
			ufCliente = StringUtil.getStringAbreviada(ufCliente, widthCelula - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_UF + ufCliente, x, y);

		//Telefone do cliente
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		String telCliente = StringUtil.getStringValue(pedido.getCliente().nuFone);
		larguraTexto = smallFont.fm.stringWidth(telCliente);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_TELEFONE);
		if (larguraTexto + larguraLabel > widthImage) {
			telCliente = StringUtil.getStringAbreviada(telCliente, widthImage - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_TELEFONE + telCliente, x, y);
		return y;
	}

	private int drawInfoEmpresa(Graphics g, Image logoEmpresa, int widthImage, int y, int spaceBetweenLines, int larguraImpressa, Font bigFont, Empresa empresa) {
		int x;
		int larguraTexto;
		//Nome empresa
		if (ValueUtil.isNotEmpty(empresa.nmEmpresa)) {
			if (logoEmpresa != null) {
				y += logoEmpresa.getHeight() + spaceBetweenLines;
			} else {
				y += spaceBetweenLines;
			}
			x = spaceBetweenLines;
			g.drawText(empresa.nmEmpresa, x, y);
			larguraImpressa = x + bigFont.fm.stringWidth(empresa.nmEmpresa);
		}
		//Endereço
		if (ValueUtil.isNotEmpty(empresa.dsLogradouro)) {
			y += bigFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(empresa.dsLogradouro, x, y);
			larguraImpressa = x + bigFont.fm.stringWidth(empresa.dsLogradouro);
		}
		//Bairro
		if (ValueUtil.isNotEmpty(empresa.dsBairro)) {
			x = larguraImpressa;
			larguraTexto = bigFont.fm.stringWidth(" - " + empresa.dsBairro);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.dsBairro, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		//Cep
		if (ValueUtil.isNotEmpty(empresa.dsCep)) {
			y += bigFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(empresa.dsCep, x, y);
			larguraImpressa = x + bigFont.fm.stringWidth(empresa.dsCep);
		}
		//Cidade
		if (ValueUtil.isNotEmpty(empresa.dsCidade)) {
			x = larguraImpressa;
			larguraTexto = bigFont.fm.stringWidth(" - " + empresa.dsCidade);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.dsCidade, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		//Estado
		if (ValueUtil.isNotEmpty(empresa.dsEstado)) {
			x = larguraImpressa;
			larguraTexto = bigFont.fm.stringWidth(" - " + empresa.dsEstado);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.dsEstado, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		//Telefone
		if (ValueUtil.isNotEmpty(empresa.nuFone)) {
			x = larguraImpressa;
			larguraTexto = bigFont.fm.stringWidth(" - " + empresa.nuFone);
			if (larguraImpressa + larguraTexto < widthImage) {
				g.drawText(" - " + empresa.nuFone, x, y);
				larguraImpressa = x + larguraTexto;
			}
		}
		return y;
	}

	private int montaLayoutItensPedidoImpressoraTermica(Graphics g, int posicaoY, int widthImage, String tipoFonte, int smallFontSize) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		Font smallFontBold = Font.getFont(tipoFonte, true, smallFontSize);

		//Itens do pedido
		y += smallFontBold.fm.height + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawRect(0, y, widthImage, (smallFontBold.fm.height + spaceBetweenLines) * 2);

		//Cabeçalho dos itens do pedido
		x = spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOPEDIDO_DESCRICAO, x, y + 2);

		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		g.drawText(Messages.IMPRESSAOPEDIDO_QTD, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_VL_UNI, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_VL_TOTAL, x, y);

		//Dados dos Itens
		g.setFont(smallFont);
		ItemPedido itemPedido;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			//Descrição do item
			String descricao;
			String[] descricaoMultiLinhas;
			if (LavenderePdaConfig.isConfigGradeProduto() && itemPedido.itemPedidoGradeList.size() > 0) {
				int qtGrades = itemPedido.itemPedidoGradeList.size();
				ItemPedidoGrade itemPedidoGrade;
				for (int j = 0; j < qtGrades; j++) {
					itemPedidoGrade = (ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[j];
					//Descrição do item
					y += smallFont.fm.height + spaceBetweenLines;
					x = spaceBetweenLines;
					descricao = StringUtil.getStringValue(itemPedido.getDsProduto());
					String descricaoGrade = StringUtil.getStringValue(ItemPedidoGradeService.getInstance().getDescricaoGradeResumida(itemPedidoGrade));
					larguraTexto = smallFont.fm.stringWidth(descricao) + smallFont.fm.stringWidth(descricaoGrade);
					if (larguraTexto > widthImage) {
						descricao = MessageUtil.quebraLinhas(descricao, smallFont, widthImage - spaceBetweenLines);
						descricaoMultiLinhas = StringUtil.split(descricao, '\n');
						for (int k = 0; k < 2; k++) {
							if (k == 1) {
								descricaoMultiLinhas[k] = StringUtil.getStringAbreviada(descricaoMultiLinhas[k], widthImage - smallFont.fm.stringWidth(descricaoGrade) - spaceBetweenLines * 2, smallFont);
								g.drawText(descricaoMultiLinhas[k] + descricaoGrade, x, y);
							} else {
								g.drawText(descricaoMultiLinhas[k], x, y);
							}
							y += smallFontBold.fm.height + spaceBetweenLines;
						}

					} else {
						g.drawText(descricao + descricaoGrade, x, y);
						y += smallFont.fm.height + spaceBetweenLines;
					}

					//Qtd do item
					x = spaceBetweenLines;
					widthCelula = widthImage / 3;
					g.drawText(StringUtil.getStringValueToInterface(itemPedidoGrade.qtItemFisico), x, y);

					//Valor do unitário
					x += widthCelula;
					g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), x, y);

					//Valor total
					x += widthCelula;
					g.drawText(StringUtil.getStringValueToInterface(ValueUtil.round(itemPedidoGrade.qtItemFisico * itemPedido.vlItemPedido)), x, y);
				}
			} else {
				//Descrição do item
				y += smallFont.fm.height + spaceBetweenLines;
				x = spaceBetweenLines;
				descricao = StringUtil.getStringValue(itemPedido.getDsProduto());
				larguraTexto = smallFont.fm.stringWidth(descricao);
				if (larguraTexto > widthImage) {
					descricao = MessageUtil.quebraLinhas(descricao, smallFont, widthImage - spaceBetweenLines * 2);
					descricaoMultiLinhas = StringUtil.split(descricao, '\n');
					for (int j = 0; j < 2; j++) {
						g.drawText(descricaoMultiLinhas[j], x, y);
						y += smallFontBold.fm.height + spaceBetweenLines;
					}
				} else {
					g.drawText(descricao, x, y);
					y += smallFontBold.fm.height + spaceBetweenLines;
				}

				//Qtd do item
				x = spaceBetweenLines;
				widthCelula = widthImage / 3;
				g.drawText(StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), x, y);

				//Valor do unitário
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), x, y);

				//Valor Total
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido), x, y);
			}
		}
		return y;
	}

	private int montaLayoutRodapeImpressoraTermina(Graphics g, int posicaoY, int widthImage, String tipoFonte, int smallFontSize) throws SQLException {
		boolean ocultaDataEntregaPedido = LavenderePdaConfig.previsaoEntregaOcultaNoPedido || LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1();
		boolean ocultaCondicaoPagamento = LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1();
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraLabel = 0;
		int larguraTexto = 0;
		Font bigFont = Font.getFont(tipoFonte, true, 22);
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		Font smallFontBold = Font.getFont(tipoFonte, true, smallFontSize);
		//--
		//Total
		y = addRodapeTotal(g, widthImage, ocultaDataEntregaPedido, ocultaCondicaoPagamento, y, spaceBetweenLines, smallFont, smallFontBold);

		//Condição de pagamento
		if (!LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			y += smallFontBold.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			String condPag = pedido.getCondicaoPagamento() != null ? StringUtil.getStringValue(pedido.getCondicaoPagamento().dsCondicaoPagamento) : " - ";
			larguraTexto = smallFont.fm.stringWidth(condPag);
			larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_COND_PAG);
			if (larguraTexto + larguraLabel > widthImage) {
				condPag = StringUtil.getStringAbreviada(condPag, widthImage - larguraLabel, smallFont);
			}
			g.drawText(Messages.IMPRESSAOPEDIDO_COND_PAG + condPag, x, y);
		}

		//Data entrega
		if (!ocultaDataEntregaPedido) {
			y += smallFontBold.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(Messages.IMPRESSAOPEDIDO_DATA_ENTREGA + StringUtil.getStringValue(pedido.dtEntrega), x, y);
		}

		//Assinatura do cliente
		if (LavenderePdaConfig.isUsaCamposAdicionaisImpressaoPedido) {
			String[] parametros = StringUtil.split(LavenderePdaConfig.usaCamposAdicionaisImpressaoPedido, ';');
			for (int i = 0; i < parametros.length; i++) {
				switch (parametros[i]) {
				case Pedido.CAMPOS_ADICIONAIS_IMPRESSAO_DATA:
					y += spaceBetweenLines + 30;
					String msgDataImpresao = MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_PEDIDO_RECEBIDO_EM, DateUtil.getCurrentDate());
					x = ((widthImage / 2) - (smallFont.fm.stringWidth(msgDataImpresao) / 2)) + 3;
					g.drawText(msgDataImpresao, x, y);
					break;
				default:
					break;
				}
			}
		}
		g.setFont(smallFont);
		int nuLinhas = LavenderePdaConfig.tipoPedidoOcultoNoPedido ? 10 : 11;
		if (ValueUtil.isNotEmpty(empresaFilialImpList)) {
			nuLinhas += Math.ceil(empresaFilialImpList.size() / 3d) * 4;
		}
		y += bigFont.fm.height + spaceBetweenLines * nuLinhas;
		widthCelula = widthImage - (widthImage / 4);
		x = (widthImage - widthCelula) / 2;
		g.drawLine(x, y, x + widthCelula, y);
		if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 4 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 6) {
			g.drawLine(x, y + 1, x + widthCelula, y + 1);
		}
		y += spaceBetweenLines;
		String assinatura = Messages.IMPRESSAOPEDIDO_ASSINATURA_CLIENTE;
		x = ((widthImage / 2) - (smallFont.fm.stringWidth(assinatura) / 2)) + 3;
		g.drawText(assinatura, x, y);
		return y;
	}

	private int addRodapeTotal(Graphics g, int widthImage, boolean ocultaDataEntregaPedido, boolean ocultaCondicaoPagamento, int y, int spaceBetweenLines, Font smallFont, Font smallFontBold) {
		Font biggerFont = Font.getFont(tipoFonte, true, 25);
		int x;
		int widthCelula;
		if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			g.setFont(biggerFont);
		} else {
			g.setFont(smallFontBold);
		}
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = (widthImage / 4) * 3;
		int heightRodapeTotal = 3;
		if (ocultaDataEntregaPedido) {
			heightRodapeTotal--;
		}
		if (ocultaCondicaoPagamento) {
			heightRodapeTotal--;
		}
		int extraHeight = 0;
		if (heightRodapeTotal == 1) {
			if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
				extraHeight = spaceBetweenLines * 2;
			} else {
				extraHeight = spaceBetweenLines;
			}
		}
		g.drawRect(0, y, widthImage, ((smallFontBold.fm.height + spaceBetweenLines) * heightRodapeTotal) + extraHeight);
		g.drawText(Messages.IMPRESSAOPEDIDO_TOTAL, x, y + spaceBetweenLines);

		x = widthCelula + spaceBetweenLines;
		widthCelula = widthImage / 4;
		x += ((widthCelula / 2) - (smallFont.fm.stringWidth(StringUtil.getStringValueToInterface(pedido.vlTotalPedido)) / 2)) + 3;
		g.drawText(StringUtil.getStringValueToInterface(pedido.vlTotalPedido), x, y + spaceBetweenLines);

		if (LavenderePdaConfig.usaLayoutImpressaoPedidoViaBluetooth1()) {
			g.setFont(smallFontBold);
		}
		return y;
	}

	private int getHeightItensPedido(int widthImage, Font font) throws SQLException {
		int height = 0;
		int larguraTexto = 0;
		int spaceBetweenLines = 5;
		String descricao;
		ItemPedido itemPedido;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			if (LavenderePdaConfig.isConfigGradeProduto() && itemPedido.itemPedidoGradeList.size() > 0) {
				int qtGrades = itemPedido.itemPedidoGradeList.size();
				ItemPedidoGrade itemPedidoGrade;
				for (int j = 0; j < qtGrades; j++) {
					itemPedidoGrade = (ItemPedidoGrade) itemPedido.itemPedidoGradeList.items[j];
					descricao = StringUtil.getStringValue(ItemPedidoGradeService.getInstance().getDescricaoProdutoGradeResumida(itemPedidoGrade));
					larguraTexto = font.fm.stringWidth(descricao);
					if (larguraTexto > widthImage) {
						height += (font.fm.height * 3) + (spaceBetweenLines * 3);
					} else {
						height += (font.fm.height * 2) + (spaceBetweenLines * 2);
					}
				}
			} else {
				descricao = StringUtil.getStringValue(itemPedido.getDsProduto());
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
	
	private int getHeightItensNfeReferencia(int widthImage, Font font) throws SQLException {
		int height = 0;
		int larguraTexto = 0;
		int spaceBetweenLines = 5;
		ItemNfeReferencia itemNfeReferencia;
		for (int i = 0; i < pedido.getInfoNfe().itemNfeList.size(); i++) {
			itemNfeReferencia = (ItemNfeReferencia) pedido.getInfoNfe().itemNfeList.items[i];
			String cst = StringUtil.getStringValue(itemNfeReferencia.cdClassificFiscal);
			if (ValueUtil.isNotEmpty(cst)) {
				cst = " - " + cst;
			}
			String cfop = StringUtil.getStringValue(itemNfeReferencia.cdCfop);
			if (ValueUtil.isNotEmpty(cfop)) {
				cfop = " - " + cfop;
			}
			larguraTexto = font.fm.stringWidth(StringUtil.getStringValue(itemNfeReferencia.dsReferencia) + cst + cfop);
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
		boolean exibeNcm = LavenderePdaConfig.isUsaCampoNcmProdutoImpressaoLayoutNfe();
		String descricao;
		String ncm = ValueUtil.VALOR_NI;
		ItemNfe itemNfe;
		for (int i = 0; i < pedido.getInfoNfe().itemNfeList.size(); i++) {
			itemNfe = (ItemNfe) pedido.getInfoNfe().itemNfeList.items[i];
			String cst = StringUtil.getStringValue(itemNfe.cdClassificFiscal);
			if (ValueUtil.isNotEmpty(cst)) {
				cst = " - " + cst;
			}
			ncm = exibeNcm ? StringUtil.getStringValue(itemNfe.dsNcmProduto) : ValueUtil.VALOR_NI;
			if (ValueUtil.isNotEmpty(ncm)) {
				ncm = " - " + ncm;
			}
			if (LavenderePdaConfig.isConfigGradeProduto() && ItemNfeService.getInstance().isItemNfeGrade(itemNfe)) {
				ItemPedidoGrade itemPedidoGrade = ItemNfeService.getInstance().getItemPedidoGradeByItemNfe(itemNfe);
				descricao = StringUtil.getStringValue(ItemPedidoGradeService.getInstance().getDescricaoProdutoGradeResumida(itemPedidoGrade));
				larguraTexto = font.fm.stringWidth(descricao) + font.fm.stringWidth(cst) + font.fm.stringWidth(ncm);
				if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() && !LavenderePdaConfig.usaNfePorReferencia) {
					if (ValueUtil.isNotEmpty(itemNfe.cdUnidade)) {
						descricao = descricao + " - " + itemNfe.cdUnidade;
					}
				}
				if (larguraTexto > widthImage) {
					height += (font.fm.height * 3) + (spaceBetweenLines * 3);
				} else {
					height += (font.fm.height * 2) + (spaceBetweenLines * 2);
				}
			} else {
				descricao = LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(itemNfe.getDsProduto()) : StringUtil.getStringValue(itemNfe.getDsProdutoWithKey());
				if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() && !LavenderePdaConfig.usaNfePorReferencia) {
					if (ValueUtil.isNotEmpty(itemNfe.cdUnidade)) {
						descricao = descricao + " - " + itemNfe.cdUnidade;
					}
				}
				larguraTexto = font.fm.stringWidth(descricao) + font.fm.stringWidth(cst) + font.fm.stringWidth(ncm);
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

	private void imprimePedidoLayoutImpressoraMatricial() throws Exception {
		//Cabeçalho do pedido
		adicionaLayoutCabecalhoPedido();
		//--TABELA DE ITENS DO PEDIDO máximo de 19 linhas por página + 3 para o Total
		adicionaLayoutItensPedido();
		//--Rodapé do pedido
		adicionaLayoutRodapePedido();
		//--Enviar para a impressora
		printer.print();
	}

	private void adicionaLayoutCabecalhoPedido() throws SQLException {
		//--Data Emissão
		printer.append("           " + StringUtil.getStringValue(pedido.dtEmissao), Printer.LEFT, 50);
		//--Pedido Nº
		printer.append("           " + StringUtil.getStringValue(pedido.nuPedido), Printer.LEFT, 50);
		//--2 linhas para cabeçalho do cliente
		printer.skipLine(2);
		//--Código cliente
		printer.append("       " + StringUtil.getStringValue(pedido.cdCliente), Printer.LEFT, 100);
		printer.skipLine();
		//--Nome cliente
		printer.append("       " + StringUtil.getStringValue(pedido.getCliente().getNmClienteImpressao()), Printer.LEFT, 100);
		printer.skipLine();
		//--Endereço cliente
		Cliente cliente = (Cliente)ClienteService.getInstance().findByRowKeyDyn(pedido.getCliente().getRowKey());
		printer.append("       " + StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSLOGRADOUROCOMERCIAL")), Printer.LEFT, 70);
		//--CEP mesma linha
		printer.append("     " + StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCEPCOMERCIAL")), Printer.LEFT, 30);
		printer.skipLine();
		//--Município
		printer.append("       " + StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCIDADECOMERCIAL")), Printer.LEFT, 80);
		//--Uf mesma linha
		printer.append("       " + StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSESTADOCOMERCIAL")), Printer.LEFT, 20);
		printer.skipLine();
		//--CNPJ ou CPF
		printer.append("       " + StringUtil.getStringValue(pedido.getCliente().nuCnpj), Printer.LEFT, 60);
		//--Inscrição Estadual mesma linha
		printer.append("      " + StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("NUINSCRICAOESTADUAL")), Printer.LEFT, 40);
		printer.skipLine();
		//--Condição pagamento
		printer.append("                " + StringUtil.getStringValue(pedido.getCondicaoPagamento().dsCondicaoPagamento), Printer.LEFT, 100);
		//--3 linhas para iniciar TABELA DE ITENS DO PEDIDO
		printer.skipLine(2);
	}

	private void adicionaLayoutItensPedido() throws SQLException {
		int qtdItensPedido = pedido.itemPedidoList.size();
		int count = 0;
		boolean proximaPagina = false;

		for (int i = 0; i < qtdItensPedido; i++) {
			if (proximaPagina) {
				adicionaLayoutRodapePedido();
				adicionaLayoutCabecalhoPedido();
				proximaPagina = false;
			}
			ItemPedido itemPedido = (ItemPedido)pedido.itemPedidoList.items[i];
			//--Cód. Item
			printer.skipLine();
			printer.append(StringUtil.getStringValue(itemPedido.cdProduto), Printer.LEFT, 8);
			//--Descrição
			printer.append(" " + StringUtil.getStringValue(itemPedido.getDsProduto()), Printer.LEFT, 43);
			//--Unidade
			printer.append(" " + StringUtil.getStringValue(itemPedido.cdUnidade), Printer.LEFT, 7);
			//--Quantidade
			printer.append(StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), Printer.CENTER, 10);
			//--Valor Tabela
			printer.append(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), Printer.RIGHT, 16);
			//--Valor Negociado
			printer.append(StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido), Printer.RIGHT, 16);

			count++;
			if (count == QTD_MAX_ITENS_POR_PAGINA && qtdItensPedido > QTD_MAX_ITENS_POR_PAGINA) {
				proximaPagina = true;
				count = 0;
			}
		}

		int qtdSkips = QTD_MAX_ITENS_POR_PAGINA - count;
		if (qtdSkips > 0) {
			printer.skipLine(qtdSkips);
		}
	}

	private void adicionaLayoutRodapePedido() {
		printer.skipLine(3);
		//--Valor total pedido
		printer.append(StringUtil.getStringValueToInterface(pedido.vlTotalPedido), Printer.BOLD, Printer.RIGHT, 100);
		//--11 linhas para finalizar a impressão da folha
		printer.skipLine(11);
	}

	private MonoImage geraComprovanteNfeLayoutImpressoraTermica() throws ImageException, IOException, SQLException {
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
		montaLayoutComprovanteNfe(g, y, widthImage, smallFont, smallFontBold);
		return mi;
	}
	
	private MonoImage geraNfeLayoutImpressoraTermica() throws ImageException, IOException, SQLException {
		int y = 0;
		int spaceBetweenLines = 5;
		int spaceFinal = 150;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		setTipoFonte(ZEBRA_TIPO_FONTE);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
			setTipoFonte(MPT_TIPO_FONTE);
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
			ExceptionUtil.handle(ex);
		} finally {
			FileUtil.closeFile(file);
		}
		int heightComprovante = 0;
		int espacoEntreComprovanteNfe = 0;
		if (LavenderePdaConfig.usaImpressaoComprovanteNfe == 1) {
			heightComprovante = (smallFontBold.fm.height + spaceBetweenLines) * 7;
			espacoEntreComprovanteNfe = spaceFinal;
		}
		
		final Transportadora transportadora = pedido.getInfoNfe().transportadora = buscaTransportadora();
		int heightItensDaNfe = LavenderePdaConfig.usaNfePorReferencia ? getHeightItensNfeReferencia(widthImage, smallFont) : getHeightItensNfe(widthImage, smallFontBold);	
		final int heightParcelasPedido = (smallFont.fm.height + spaceBetweenLines) * pedido.parcelaPedidoList.size();
		final int qtPagamentos = pedido.pagamentoPedidoList == null ? 1 : pedido.pagamentoPedidoList.size();
		final int heightPagamentoPedido = LavenderePdaConfig.apresentaMultiplosPagamentosImpressaoNfe ? (smallFont.fm.height + spaceBetweenLines) * qtPagamentos : 0;
		final int heightImpressaoCigarro = LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && pedido.getInfoNfe().getSomaQtItemFisicoCigarro() > 0 ?  ((smallFont.fm.height + spaceBetweenLines) * 2) * Messages.IMPRESSAONFE_ADICIONAL.length() / 55 + (Messages.IMPRESSAONFE_ADICIONAL.length() % 55 == 0 ? 0 : 1) : 0;
		final int heightDadosImpostos = (smallFont.fm.height + spaceBetweenLines) * 3;
		final int heightTransportadora = LavenderePdaConfig.apresentaSecaoTransportadoraImpressaoNfe && transportadora != null ? (smallFont.fm.height + spaceBetweenLines) * 3 : 0;
		final int remessasSize = RemessaEstoqueService.getInstance().getRemessaEstoqueList(pedido.getInfoNfe().itemNfeList).size();
		final int heightRemessas = LavenderePdaConfig.listaChaveAcessoNotasRemessaImpressaNfe && remessasSize > 0? (smallFontBold.fm.height + spaceBetweenLines) + (smallFont.fm.height + spaceBetweenLines) * (remessasSize * 5) : 0;
		final int heightDispositivosLegais = LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && pedido.getInfoNfe().getSomaQtItemFisicoCigarro() > 0 ?  (smallFont.fm.height + spaceBetweenLines) * Messages.IMPRESSAONFE_MSG_CIGARRO.length() / 55 + (Messages.IMPRESSAONFE_MSG_CIGARRO.length() % 55 == 0 ? 0 : 1) + ((smallFont.fm.height + spaceBetweenLines)*5)  : 0;
		final int heightDadosAdicionais = ((smallFontBold.fm.height + spaceBetweenLines) * 2) + ((smallFont.fm.height + spaceBetweenLines) * (StringUtil.getStringValue(getDescricaoComNuNotaGeral()).length() / QT_CARACTERES_LINHA + (StringUtil.getStringValue(getDescricaoComNuNotaGeral()).length() % QT_CARACTERES_LINHA_DADOS_ADICIONAIS == 0 ? 0 : 1))) + ((smallFont.fm.height + spaceBetweenLines)*2 + spaceBetweenLines);
		heightComprovante += LavenderePdaConfig.isExibeDataEmissaoNaImpressaoComprovanteNfe() ? smallFont.fm.height + spaceBetweenLines : 0;
		heightComprovante += LavenderePdaConfig.isExibeDadosClienteNaImpressaoComprovanteNfe() ? smallFont.fm.height + spaceBetweenLines : 0;
		espacoEntreComprovanteNfe += LavenderePdaConfig.isExibeDataEmissaoNaImpressaoComprovanteNfe() ? smallFont.fm.height + spaceBetweenLines : 0;
		espacoEntreComprovanteNfe += LavenderePdaConfig.isExibeDadosClienteNaImpressaoComprovanteNfe() ? smallFont.fm.height + spaceBetweenLines + smallFont.fm.height + spaceBetweenLines : 0;
		final int heightTarjaNfeCancelada = Nfe.CDMENSAGEMRETORNO_CANCELAMENTO.equals(pedido.getInfoNfe().cdMensagemRetorno) ? ((smallFontBold.fm.height + spaceBetweenLines) * 3) : 0;
		int heigthImage = (logoEmpresa != null ? logoEmpresa.getHeight() + heightComprovante + espacoEntreComprovanteNfe : heightComprovante + espacoEntreComprovanteNfe) + ((smallFont.fm.height + spaceBetweenLines) * 36) + heightParcelasPedido + heightItensDaNfe + heightPagamentoPedido + heightImpressaoCigarro + heightDadosImpostos + heightTransportadora + heightDispositivosLegais + heightRemessas + heightDadosAdicionais + heightTarjaNfeCancelada;
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);
		//Comprovante
		if (LavenderePdaConfig.usaImpressaoComprovanteNfe == 1) {
			y = montaLayoutComprovanteNfe(g, y, widthImage, smallFont, smallFontBold);
			y += espacoEntreComprovanteNfe;
		}
		//Info Nfe
		y = montaLayoutInfoNfe(g, y, widthImage, logoEmpresa, smallFont, smallFontBold);
		//Emitente
		y = montaLayoutEmitenteNfe(g, y, widthImage, smallFont, smallFontBold);
		//Destinatário
		y = montaLayoutDestinatarioNfe(g, y, widthImage, smallFont, smallFontBold);
		//Dados Financeiros
		if (pedido.parcelaPedidoList.size() > 0) {
			y = montaLayoutDadosFinanceirosNfe(g, y, widthImage, smallFont, smallFontBold);
		}
		if (LavenderePdaConfig.apresentaMultiplosPagamentosImpressaoNfe) {
			y = montaLayoutDadosFinanceiroPagamentosNfe(g, y, widthImage, smallFont, smallFontBold);
		}
		//Item Pedido
		y = montaLayoutItemPedidoNfe(g, y, widthImage, smallFont, smallFontBold);
		//Cálculo dos impostos
		y = montaLayoutCalculoImpostoNfe(g, y, widthImage, smallFont, smallFontBold);
		if (LavenderePdaConfig.apresentaSecaoTransportadoraImpressaoNfe) {
			y = montaLayoutTransportadoraNfe(g, y, widthImage, smallFont, smallFontBold);
		}
		//Dados Adicionais
		y = montaLayoutDadosAdicionaisNfe(g, y, widthImage, smallFont, smallFontBold);
		//Remessas Liberadas
		if (LavenderePdaConfig.listaChaveAcessoNotasRemessaImpressaNfe && remessasSize > 0) {
			y = montaLayoutChaveAcessoRemessaNfe(g, y, widthImage, smallFont, smallFontBold);
		}
		//Dispositivos legais
		if (LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && pedido.getInfoNfe().getSomaQtItemFisicoCigarro() > 0) {
			y = montaLayoutDispositivosLegaisNfe(g, y, widthImage, smallFont, smallFontBold);
		}
		return mi;
	}

	private int montaLayoutComprovanteNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		g.setFont(smallFont);
		x = spaceBetweenLines;
		widthCelula = widthImage / 3;
		int height = LavenderePdaConfig.isExibeValorNfeNaImpressaoComprovanteNfe() ? 7 : 6;
		height = LavenderePdaConfig.isExibeCondicaoPagamentoNaImpressaoComprovanteNfe() ? ++height : height;
		height = LavenderePdaConfig.isExibeDataEmissaoNaImpressaoComprovanteNfe() ? ++height : height; 
		height = LavenderePdaConfig.isExibeDadosClienteNaImpressaoComprovanteNfe() ? 2+height : height; 
				
		//Nome empresa
		g.drawText(Messages.IMPRESSAONFE_CABECALHO_EMPRESA, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		String nmEmpresa = StringUtil.getStringValue(pedido.getEmpresa().nmEmpresa);
		larguraTexto = smallFont.fm.stringWidth(nmEmpresa);
		if (larguraTexto > widthImage) {
			nmEmpresa = StringUtil.getStringAbreviada(nmEmpresa, widthImage, smallFont);
		}
		g.drawText(nmEmpresa, x, y);

		//NUSERIE
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.drawText(Messages.IMPRESSAONFE_CABECALHO_NUNFE, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAONFE_NFE + pedido.getInfoNfe().nuNfe + " " + Messages.IMPRESSAONFE_SERIE + pedido.getInfoNfe().dsSerieNfe, x, y);
		
		if (LavenderePdaConfig.isExibeValorNfeNaImpressaoComprovanteNfe()) {
			//VALOR DA NFE
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL_NOTA + " " + StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalNfe, nuCasasDecimaisValoresMonetarios), x, y);
		} 
		if (LavenderePdaConfig.isExibeCondicaoPagamentoNaImpressaoComprovanteNfe()) {
			//CONDICAO PAGAMENTO
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(Messages.IMPRESSAONFE_CONDICAO_PAGAMENTO + " " + StringUtil.getStringValue(pedido.getCondicaoPagamento().toString()), x, y);
		}
		if (LavenderePdaConfig.isExibeDataEmissaoNaImpressaoComprovanteNfe()) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			g.drawText(Messages.IMPRESSAONFE_DATA_EMISSAO_COMPROVANTE + " " + StringUtil.getStringValue(pedido.getInfoNfe().dtEmissao), x, y);
		}
		
		if (LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && pedido.getInfoNfe().getSomaQtItemFisicoCigarro() > 0) {
			y += smallFont.fm.height + spaceBetweenLines;
			height++;
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;

			String[] textoSplit = Messages.IMPRESSAONFE_ADICIONAL.split(" ");
			String textoImpresso = "";
			for (String palavra : textoSplit) {
				if ((textoImpresso + palavra).length() > QT_CARACTERES_LINHA) {
					g.drawText(textoImpresso, x, y);
					height++;
					textoImpresso = palavra + " ";
					y += smallFont.fm.height + spaceBetweenLines;
				} else {
					textoImpresso += palavra + " ";
				}
			}
			height++;
			g.drawText(textoImpresso, x, y);
		}

		//DATA E ASSINATURA
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
		if (LavenderePdaConfig.isExibeDadosClienteNaImpressaoComprovanteNfe()) {
			y += smallFont.fm.height + spaceBetweenLines;
			g.drawText(StringUtil.getStringValue(pedido.getCliente().getNmClienteImpressao()) +" - "+ StringUtil.getStringValue(pedido.getCliente().cdCliente), x, y);
		}
		g.drawRoundRect(0, posicaoY, widthImage, (smallFont.fm.height + spaceBetweenLines) * height, 8);
		return y;
	}
	
	private int montaLayoutInfoNfe(Graphics g, int posicaoY, int widthImage, Image logoEmpresa, Font smallFont, Font smallFontBold) throws ImageException, IOException, SQLException {
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

		g.drawRoundRect(0, y, widthImage, (smallFont.fm.height) * 2 + smallFontBold.fm.height, 8);
				
		String[] textoDanfe = StringUtil.split(getInfoDanfe(), '#', false);
		g.drawText(textoDanfe[0], x, y);
		g.setFont(smallFont);
		final int larguraAdicional = 70;
		g.drawText(StringUtil.getStringValue(pedido.getInfoNfe().cdTipoOperacaoNfe), widthCelula * 2 + larguraAdicional, y);
		y += smallFont.fm.height;
		g.drawText(textoDanfe[1], x, y);
		g.drawText(Messages.IMPRESSAONFE_NFE + StringUtil.getStringValue(pedido.getInfoNfe().nuNfe), widthCelula * 2 + larguraAdicional, y);
		y += smallFont.fm.height;
		g.drawText(textoDanfe[2], x, y);
		g.drawText(Messages.IMPRESSAONFE_SERIE + StringUtil.getStringValue(pedido.getInfoNfe().dsSerieNfe), widthCelula * 2 + larguraAdicional, y);
		y += smallFont.fm.height;
		y += spaceBetweenLines;
		
		y = addTarjaCancelamento(g, widthImage, smallFont, y, spaceBetweenLines);
		
		//CHAVE DE ACESSO
		g.setFont(smallFontBold);
		y += spaceBetweenLines;
		g.drawRoundRect(0, y, widthImage, (smallFontBold.fm.height) * 2, 8);
		g.drawText(Messages.IMPRESSAONFE_CHAVE_DE_ACESSO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String vlChaveAcesso = StringUtil.getStringValue(pedido.getInfoNfe().vlChaveAcesso);
		if (vlChaveAcesso.length() != TAMANHO_OBRIGATORIO_CHAVE_ACESSO_NFE) {
			g.drawText(Messages.IMPRESSAONFE_CHAVE_ACESSO_INVALIDA, (widthImage - smallFont.fm.stringWidth(Messages.IMPRESSAONFE_CHAVE_ACESSO_INVALIDA)) / 2, y);
		} else {
			g.drawText(vlChaveAcesso, x, y);
		}
		y += smallFont.fm.height + spaceBetweenLines;
		
		//CODIGO DE BARRAS
		y += (smallFont.fm.height + spaceBetweenLines) * 2;
		int alturaCodigoBarra = (smallFont.fm.height + spaceBetweenLines) * 3;
		double espessuraBarra = 1.9;
		if (vlChaveAcesso.length() != TAMANHO_OBRIGATORIO_CHAVE_ACESSO_NFE) {
			g.drawText(Messages.IMPRESSAONFE_CHAVE_ACESSO_INVALIDA, (widthImage - smallFont.fm.stringWidth(Messages.IMPRESSAONFE_CHAVE_ACESSO_INVALIDA)) / 2, y);
		} else {
			Image barcodeImg = BarCodeUtil.generateBarcode(vlChaveAcesso, alturaCodigoBarra, 0, espessuraBarra, BarcodeType.CODE128);
			x = (widthImage - barcodeImg.getWidth()) / 2;
			g.drawImage(barcodeImg, x, y);
			y += alturaCodigoBarra + spaceBetweenLines;
			g.drawText(vlChaveAcesso, x, y, barcodeImg.getWidth());
		}
		y += (smallFont.fm.height + spaceBetweenLines) * 3;
		
		//NATUREZA DA OPERAÇÃO
		y += spaceBetweenLines;
		x = spaceBetweenLines;
		g.drawRoundRect(0, y, widthImage, smallFontBold.fm.height * 3, 8);
		String natOperacao = StringUtil.getStringValue(pedido.getInfoNfe().dsNaturezaOperacao);
		larguraTexto = smallFont.fm.stringWidth(natOperacao);
		if (larguraTexto > widthCelula * 2) {
			natOperacao = StringUtil.getStringAbreviada(natOperacao, widthCelula * 2, smallFont);
		}
		String dataEmissao = StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedido.getInfoNfe().dtEmissao));
		String dataSaida = StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedido.getInfoNfe().dtSaida));
		String horaSaida = StringUtil.getStringValue(pedido.getInfoNfe().hrSaida);
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAONFE_NAT_OPERACAO, x, y);
		g.drawText(Messages.IMPRESSAONFE_DATA_EMISSAO + dataEmissao, widthCelula * 2, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(natOperacao, x, y);
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAONFE_DATA_SAIDA + dataSaida, widthCelula * 2, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(Messages.IMPRESSAONFE_HORA_SAIDA + horaSaida, widthCelula * 2, y);
		y +=  smallFont.fm.height;
		x = spaceBetweenLines;
		return y;
	}

	private int addTarjaCancelamento(Graphics g, int widthImage, Font smallFont, int y, int spaceBetweenLines)
			throws SQLException {
		//TARJA CANCELAMENTO
		if (Nfe.CDMENSAGEMRETORNO_CANCELAMENTO.equals(pedido.getInfoNfe().cdMensagemRetorno)) {
			y = montaTarja(g, widthImage, y, 10);
			y += spaceBetweenLines;
			Font bigFont = Font.getFont(getTipoFonte(), true, 40);
			int centerTitle = widthImage - ((widthImage / (2)) + (bigFont.fm.stringWidth(Messages.IMPRESSAONFE_INFONFE_CANCELAMENTO) / 2));
			g.setFont(bigFont);
			g.drawText(Messages.IMPRESSAONFE_INFONFE_CANCELAMENTO, centerTitle, y);
			g.setFont(smallFont);
			y += smallFont.fm.height;
			y += smallFont.fm.height;
			y = montaTarja(g, widthImage, y, 10);
			y += spaceBetweenLines;
		}
		return y;
	}

	private int montaTarja(Graphics g, int widthImage, int y, int alturaTarja) {
		for (int i = 0; i < alturaTarja; i++) {
			g.drawLine(0, y, widthImage, y);
			y++;
		}
		return y;
	}

	private String getInfoDanfe() throws SQLException {
		if (pedido.getInfoNfe().isContingencia()) {
			return Messages.IMPRESSAONFE_INFONFE_DANFE_CONTINGENCIA;
		} 
		return Messages.IMPRESSAONFE_INFONFE_DANFE;
	}
	
	private int montaLayoutEmitenteNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
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
	
	private int montaLayoutDestinatarioNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
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
		String nome = StringUtil.getStringValue(pedido.getCliente().getNmClienteImpressao());
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
	
	private int montaLayoutDadosFinanceirosNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int alturaRect = 1;
		int alturaInicial = 0;
		
		g.setFont(smallFontBold);
		y += spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		alturaInicial = y;

		g.drawText(Messages.IMPRESSAONFE_DADOS_FINANCEIROS, x, y);
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		g.drawText(Messages.IMPRESSAONFE_DADOS_FINANCEIROS_VENCIMENTO, x, y);
		g.drawText(Messages.IMPRESSAONFE_DADOS_FINANCEIROS_VALOR, widthCelula, y);
		y += smallFontBold.fm.height;
		if (!LavenderePdaConfig.isGeraParcelasEmPercentual() && pedido.parcelaPedidoList.size() > 0) {
			for (int i = 0; i < pedido.parcelaPedidoList.size(); i++) {
				ParcelaPedido parcela = (ParcelaPedido) pedido.parcelaPedidoList.items[i];
				g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(parcela.dtVencimento)), x, y);
				g.drawText(StringUtil.getStringValue(parcela.vlParcela, nuCasasDecimaisValoresMonetarios), widthCelula, y);
				y += smallFont.fm.height;
				alturaRect += 1;
			}
		}
		g.drawRoundRect(0, alturaInicial, widthImage, (smallFontBold.fm.height + spaceBetweenLines) + ((smallFont.fm.height) * alturaRect), 8);
		y += spaceBetweenLines;
		return y;
	}
	
	private int montaLayoutDadosFinanceiroPagamentosNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int alturaRect = 0;
		int widthCelula = 0;
		int alturaInicial = 0;
		qtLinhas = 0;
		
		g.setFont(smallFontBold);
		y += spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		alturaInicial = y;
	
		g.drawText(Messages.IMPRESSAONFE_DADOS_FINANCEIROS, x, y);
		g.setFont(smallFont);
		y += smallFontBold.fm.height;
		
		if (pedido.pagamentoPedidoList == null || pedido.pagamentoPedidoList.isEmpty()) {
			y = drawTextPagamentoPedidoNfe(g, smallFont, smallFontBold, y, x, widthCelula, pedido.getInfoNfe().dtEmissao, pedido.getInfoNfe().vlTotalNfe, Messages.IMPRESSAONFE_DADOS_FINANCEIROS_CONDICAO + pedido.nfe.nuNfe);
			alturaRect += 3;
		} else {
			String origemDescricaoPagamento = LavenderePdaConfig.origemDescricaoPagamento;
			for (int i = 0; i < pedido.pagamentoPedidoList.size(); i++) {
				PagamentoPedido pagamentoPedido = (PagamentoPedido) pedido.pagamentoPedidoList.items[i];
				final Date dtVencimento = pagamentoPedido.dtVencimento != null ? pagamentoPedido.dtVencimento : pedido.getInfoNfe().dtEmissao;
				if(ValueUtil.valueEquals(origemDescricaoPagamento, "1")) {
					y = drawTextPagamentoPedido(g, smallFont, smallFontBold, y, x, widthCelula, dtVencimento, pagamentoPedido, getCondicaoPagamento(pagamentoPedido).dsCondicaoPagamento);
				} else if (ValueUtil.valueEquals(origemDescricaoPagamento, "2")) {
					y = drawTextPagamentoPedido(g, smallFont, smallFontBold, y, x, widthCelula, dtVencimento, pagamentoPedido, getTipoPagamento(pagamentoPedido).dsTipoPagamento);
				} 
			}
			alturaRect += qtLinhas;
		}
		
		g.drawRoundRect(0, alturaInicial, widthImage, (smallFontBold.fm.height + spaceBetweenLines) + ((smallFont.fm.height) * alturaRect), 8);
		y += spaceBetweenLines;
		return y;
	}

	private int drawTextPagamentoPedido(Graphics g, Font smallFont, Font smallFontBold, int y, int x, int widthCelula,
			Date dtVencimento, PagamentoPedido pagamentoPedido, String dsCondicaoPagamento) throws SQLException {
		y = drawTextCabecalhoPagamento(g, smallFont, smallFontBold, y, x, widthCelula, dsCondicaoPagamento);
		
		VenctoPagamentoPedidoService.getInstance().findVctosPagamentoPedido(pagamentoPedido);
		
		if (pagamentoPedido.venctoPagamentoPedidos != null && pagamentoPedido.venctoPagamentoPedidos.size() >= 1) {
			for (VenctoPagamentoPedido vencto  : pagamentoPedido.venctoPagamentoPedidos) {
				y = drawTextDetalhePagamento(g, smallFont, y, x, widthCelula, vencto.dtVencimento, vencto.vlBoleto);
			}
		} else {
			y = drawTextDetalhePagamento(g, smallFont, y, x, widthCelula, dtVencimento, pagamentoPedido.vlPagamentoPedido);
		}
		
		return y;
	}
	
	private int drawTextPagamentoPedidoNfe(Graphics g, Font smallFont, Font smallFontBold, int y, int x, int widthCelula,
			Date dtVencimento, Double vlPagamento, String dsCondicaoPagamento) throws SQLException {
		y = drawTextCabecalhoPagamento(g, smallFont, smallFontBold, y, x, widthCelula, dsCondicaoPagamento);
		y = drawTextDetalhePagamento(g, smallFont, y, x, widthCelula, dtVencimento, vlPagamento);
		return y;
	}

	private int drawTextCabecalhoPagamento(Graphics g, Font smallFont, Font smallFontBold, int y, int x, int widthCelula,
			String dsCondicaoPagamento) {
		g.drawText(dsCondicaoPagamento, x, y);
		y += smallFontBold.fm.height;
		qtLinhas ++;
		
		g.drawText(Messages.IMPRESSAONFE_DADOS_FINANCEIROS_VENCIMENTO, x, y);
		g.drawText(Messages.IMPRESSAONFE_DADOS_FINANCEIROS_VALOR, widthCelula, y);
		y += smallFont.fm.height;
		qtLinhas ++;
		
		return y;
	}
	
	private int drawTextDetalhePagamento(Graphics g, Font smallFont, int y, int x, int widthCelula,
			Date dtVencimento, Double vlPagamento) {
		g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(dtVencimento)), x, y);
		g.drawText(StringUtil.getStringValueToInterface(vlPagamento), widthCelula, y);
		y += smallFont.fm.height;
		qtLinhas++;
		return y;
	}

	private int montaLayoutTransportadoraNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int alturaRect = 0;
		int widthCelula = 0;
		int alturaInicial = 0;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		alturaInicial = y;
		
		Transportadora transportadora = new Transportadora();
		transportadora.cdEmpresa = pedido.getInfoNfe().cdEmpresa;
		transportadora.cdRepresentante = pedido.getInfoNfe().cdRepresentante;
		transportadora.cdTransportadora = pedido.getInfoNfe().cdTransportadora;
		transportadora = (Transportadora) TransportadoraService.getInstance().findByPrimaryKey(transportadora);
		
		if (transportadora == null) {
			return posicaoY;
		}
		
		g.drawText(Messages.IMPRESSAONFE_TRANSPORTADORA, x, y);
		g.setFont(smallFont);
		y += smallFontBold.fm.height;
		
		g.drawText(StringUtil.getStringValue(transportadora.nmTransportadora), x, y);
		
		y += smallFontBold.fm.height;
		
		g.drawText(Messages.IMPRESSAONFE_TRANSPORTADORA_CNPJ + StringUtil.getStringValue(transportadora.nuCnpj), x, y);
		
		g.drawText(Messages.IMPRESSAONFE_TRANSPORTADORA_PLACA + StringUtil.getStringValue(transportadora.nuPlaca), widthCelula, y);
		
		alturaRect += 2;
		
		g.drawRoundRect(0, alturaInicial, widthImage, (smallFontBold.fm.height + spaceBetweenLines) + ((smallFont.fm.height) * alturaRect), 8);
		y += spaceBetweenLines;
		return y;
	}

	private Transportadora buscaTransportadora() throws SQLException {
		Transportadora transportadora = new Transportadora();
		transportadora.cdEmpresa = pedido.cdEmpresa;
		transportadora.cdRepresentante = pedido.cdRepresentante;
		transportadora.cdTransportadora = pedido.getInfoNfe().cdTransportadora;
		transportadora = (Transportadora) TransportadoraService.getInstance().findByPrimaryKey(transportadora);
		return transportadora;
	}

	private int montaLayoutChaveAcessoRemessaNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException, ImageException {
		int y = posicaoY;
		int spaceBetweenLines = 5;
		int x = spaceBetweenLines;
		int alturaRect = 0;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		alturaRect = smallFontBold.fm.height + spaceBetweenLines; 
		int alturaInicial = y;
		
		g.drawText(Messages.IMPRESSAONFE_REMESSAS_LIBERADAS, x, y);
		
		int alturaCodigoBarra = (smallFont.fm.height + spaceBetweenLines) * 3;
		double espessuraBarra = 1.9;
		
		List<RemessaEstoque> remessaEstoqueList = RemessaEstoqueService.getInstance().getRemessaEstoqueList(pedido.getInfoNfe().itemNfeList);
		for (RemessaEstoque remessaEstoque : remessaEstoqueList) {
			String vlChaveAcesso = StringUtil.getStringValue(remessaEstoque.vlChaveAcesso);
			y += (smallFont.fm.height + spaceBetweenLines);
			alturaRect += (smallFont.fm.height + spaceBetweenLines);
			Image barcodeImg = BarCodeUtil.generateBarcode(vlChaveAcesso, alturaCodigoBarra, 0, espessuraBarra, BarcodeType.CODE128);
			x = (widthImage - barcodeImg.getWidth()) / 2;
			g.drawImage(barcodeImg, x, y);
			y += alturaCodigoBarra;
			alturaRect += alturaCodigoBarra;
			g.drawText(vlChaveAcesso, x, y, barcodeImg.getWidth());
			y += (smallFont.fm.height + spaceBetweenLines);
			alturaRect += (smallFont.fm.height + spaceBetweenLines);
		}
		
		g.drawRoundRect(0, alturaInicial, widthImage, alturaRect, 8);
		
		y += spaceBetweenLines;
		
		return y;
	}

	private CondicaoPagamento getCondicaoPagamento(PagamentoPedido pagamentoPedido) throws SQLException {
		CondicaoPagamento condicaoPagamentoFilter = new CondicaoPagamento();
		condicaoPagamentoFilter.cdEmpresa = pagamentoPedido.cdEmpresa;
		condicaoPagamentoFilter.cdRepresentante = pagamentoPedido.cdRepresentante;
		condicaoPagamentoFilter.cdCondicaoPagamento = pagamentoPedido.cdCondicaoPagamento;
		condicaoPagamentoFilter = (CondicaoPagamento) CondicaoPagamentoService.getInstance().findByPrimaryKey(condicaoPagamentoFilter);
		return condicaoPagamentoFilter;
	}
	
	private TipoPagamento getTipoPagamento(PagamentoPedido pagamentoPedido) throws SQLException {
		TipoPagamento tipoPagamentoFilter = new TipoPagamento();
		tipoPagamentoFilter.cdEmpresa = pagamentoPedido.cdEmpresa;
		tipoPagamentoFilter.cdRepresentante = pagamentoPedido.cdRepresentante;
		tipoPagamentoFilter.cdTipoPagamento = pagamentoPedido.cdTipoPagamento;
		tipoPagamentoFilter = (TipoPagamento) TipoPagamentoService.getInstance().findByPrimaryKey(tipoPagamentoFilter);
		return tipoPagamentoFilter;
	}
	
	private int montaLayoutItemPedidoNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
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
		if (LavenderePdaConfig.usaNfePorReferencia) {
			g.drawText(" - " + Messages.IMPRESSAOPEDIDO_CST + " - " + Messages.ITEMNFEREFERENCIA_LABEL_CDCFOP, x, y + 2);
		} else {
			String cabecalho = "";
			if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe()) {
				cabecalho = " - " + Messages.IMPRESSAOPEDIDO_UN;
			}
			if (LavenderePdaConfig.isUsaCampoNcmProdutoImpressaoLayoutNfe()) {
				cabecalho +=  " - " + Messages.IMPRESSAOPEDIDO_NCM;
			}
			g.drawText(cabecalho + " - " + Messages.IMPRESSAOPEDIDO_CST, x, y + 2);
		}
		
		y += smallFontBold.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = (widthImage - spaceBetweenLines * 2) / 7;
		if (!LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() || LavenderePdaConfig.usaNfePorReferencia) {
			g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_UNIDADE, x, y);
			x += widthCelula;
		}
		
		g.drawText(Messages.IMPRESSAOPEDIDO_QTD, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_VL_UNI, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_COM_DESCONTO, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_ICMS, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_ALIQUOTA, x, y);
		
		if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() && !LavenderePdaConfig.usaNfePorReferencia) {
			x += widthCelula;
			g.drawText(Messages.IMPRESSAONFE_ITEMPEDIDO_VLTOTALSTITEM, x, y);
		}

		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL, x - spaceBetweenLines, y);
		
		//Dados dos Itens
		g.setFont(smallFont);
		y = LavenderePdaConfig.usaNfePorReferencia ? setDadosItemNFEReferencia(g, widthImage, smallFont, smallFontBold, y, spaceBetweenLines, widthCelula) :  setDadosItemNFE(g, widthImage, smallFont, smallFontBold, y, spaceBetweenLines, widthCelula);
		y += spaceBetweenLines;
		return y;
	}
	
	private int setDadosItemNFEReferencia(Graphics g, int widthImage, Font smallFont, Font smallFontBold, int y, int spaceBetweenLines, int widthCelula) throws SQLException {
		int x = 0;
		int larguraTexto = 0;
		ItemNfeReferencia itemNfeReferencia;
		int size = pedido.getInfoNfe().itemNfeList.size();
		boolean usaLinhaFimItem = LavenderePdaConfig.usaLinhaSeparadoraItensImpressao;
		for (int i = 0; i < size; i++) {
			itemNfeReferencia = (ItemNfeReferencia) pedido.getInfoNfe().itemNfeList.items[i];
			//Descrição do item
			String descricaoReferencia = StringUtil.getStringValue(itemNfeReferencia.dsReferencia);
			String[] descricaoMultiLinhas;
			//Descrição do item
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			String cst = StringUtil.getStringValue(itemNfeReferencia.cdClassificFiscal);
			if (ValueUtil.isNotEmpty(cst)) {
				cst = " - " + cst;
			}
			String cfop = StringUtil.getStringValue(itemNfeReferencia.cdCfop);
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
			g.drawText(StringUtil.getStringValue(itemNfeReferencia.cdUnidade), x, y);
			//Qtd do item
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemNfeReferencia.qtItemFisico), x, y);
			//Valor do unitário
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemNfeReferencia.vlBaseItemTabelaPreco, nuCasasDecimaisValoresMonetarios), x, y);
			//Valor com desconto
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemNfeReferencia.vlItemPedido, nuCasasDecimaisValoresMonetarios), x, y);
			//Valor ICMS
			x += widthCelula;
			String vlIcmsItem = ValueUtil.VALOR_ZERO;
			if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
				vlIcmsItem = StringUtil.getStringValueToInterface(itemNfeReferencia.vlTotalIcmsItem, nuCasasDecimaisValoresMonetarios);
			}
			g.drawText(vlIcmsItem, x, y);
			//Valor AL. ICMS
			x += widthCelula;
			String aliquotaIcms = ValueUtil.VALOR_ZERO;
			if (LavenderePdaConfig.isUsaCalculoStItemPedido() && ValueUtil.isNotEmpty(pedido.getCliente().cdTributacaoCliente)) {
				aliquotaIcms = StringUtil.getStringValueToInterface(itemNfeReferencia.vlPctIcms, nuCasasDecimaisValoresMonetarios);
			}
			g.drawText(aliquotaIcms, x, y);
			//Valor total
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemNfeReferencia.vlTotalItemPedido, nuCasasDecimaisValoresMonetarios), x - spaceBetweenLines, y);
			int lineY = y + smallFont.fm.height;
			if (usaLinhaFimItem && i < size - 1) {
				g.drawLine(spaceBetweenLines, lineY, widthImage - spaceBetweenLines, lineY);
			}
		}
		return y;
	}

	private int setDadosItemNFE(Graphics g, int widthImage, Font smallFont, Font smallFontBold, int y, int spaceBetweenLines, int widthCelula) throws SQLException {
		int x = 0;
		int larguraTexto = 0;
		ItemNfe itemNfe;
		boolean exibeDsNcm = LavenderePdaConfig.isUsaCampoNcmProdutoImpressaoLayoutNfe();
		boolean usaLinhaFinalItem = LavenderePdaConfig.usaLinhaSeparadoraItensImpressao;
		int size = pedido.getInfoNfe().itemNfeList.size();
		for (int i = 0; i < size; i++) {
			itemNfe = (ItemNfe) pedido.getInfoNfe().itemNfeList.items[i];
			//Descrição do item
			String descricaoProduto;
			String cst = StringUtil.getStringValue(itemNfe.cdClassificFiscal);
			String ncm = exibeDsNcm ? StringUtil.getStringValue(itemNfe.dsNcmProduto) : ValueUtil.VALOR_NI;
			String[] descricaoMultiLinhas;
			if (LavenderePdaConfig.isConfigGradeProduto() && ItemNfeService.getInstance().isItemNfeGrade(itemNfe)) {
				//Descrição do item
				y += smallFont.fm.height + spaceBetweenLines;
				x = spaceBetweenLines;
				descricaoProduto = LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(itemNfe.getDsProduto()) : StringUtil.getStringValue(itemNfe.getDsProdutoWithKey());
				cst = StringUtil.getStringValue(itemNfe.cdClassificFiscal);
				ItemPedidoGrade itemPedidoGrade = ItemNfeService.getInstance().getItemPedidoGradeByItemNfe(itemNfe);
				String descricaoGrade = ItemPedidoGradeService.getInstance().getDescricaoGradeResumida(itemPedidoGrade);
				if (ValueUtil.isNotEmpty(cst)) {
					cst = " - " + cst;
				}
				if (ValueUtil.isNotEmpty(ncm)) {
					ncm = " - " + ncm;
				}
				// Caso use campo adicional Total ST, deve-se concatenar cdUnidade com descrição para não haver quebra de layout em número de colunas.
				if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() && !LavenderePdaConfig.usaNfePorReferencia) {
					if (ValueUtil.isNotEmpty(itemNfe.cdUnidade)) {
						descricaoProduto = descricaoProduto + " - " + itemNfe.cdUnidade;
					}
				}
				larguraTexto = smallFont.fm.stringWidth(descricaoProduto) + smallFont.fm.stringWidth(descricaoGrade) + smallFont.fm.stringWidth(ncm) + smallFont.fm.stringWidth(cst);
				g.setFont(smallFontBold);
				if (larguraTexto > widthImage) {
					descricaoProduto = MessageUtil.quebraLinhas(descricaoProduto, smallFont, widthImage - spaceBetweenLines);
					descricaoMultiLinhas = StringUtil.split(descricaoProduto, '\n');
					for (int k = 0; k < 2; k++) {
						if (k == 1) {
							if (descricaoMultiLinhas.length > 1) {
								descricaoMultiLinhas[k] = StringUtil.getStringAbreviada(descricaoMultiLinhas[k], widthImage - smallFont.fm.stringWidth(descricaoGrade) - smallFont.fm.stringWidth(ncm) - smallFont.fm.stringWidth(cst) - spaceBetweenLines * 2, smallFont);
								g.drawText(descricaoMultiLinhas[k] + descricaoGrade + ncm + cst, x, y);
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
				widthCelula = widthImage / 7;
				if (!LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() || LavenderePdaConfig.usaNfePorReferencia) {
					//Unidade
					g.drawText(StringUtil.getStringValue(itemNfe.cdUnidade), x, y);
					x += widthCelula;
				}
				//Qtd do item
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.qtItemFisico), x, y);
				//Valor do unitário
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.vlBaseItemTabelaPreco, nuCasasDecimaisValoresMonetarios), x, y);
				//Valor com desconto
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.vlItemPedido, nuCasasDecimaisValoresMonetarios), x, y);
				//Valor ICMS
				double vlIcms = itemNfe.vlTotalIcmsItem;
				if (LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
					vlIcms = 0;
				}
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(vlIcms, nuCasasDecimaisValoresMonetarios), x, y);
				//Valor AL. ICMS
				x += widthCelula;
				String aliquotaIcms = ValueUtil.VALOR_ZERO;
				if (ValueUtil.isNotEmpty(pedido.getCliente().cdTributacaoCliente)) {
					aliquotaIcms = StringUtil.getStringValueToInterface(itemNfe.vlPctIcms, nuCasasDecimaisValoresMonetarios);
				}
				g.drawText(aliquotaIcms, x, y);
				if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() && !LavenderePdaConfig.usaNfePorReferencia) {
					//Valor VLTOTALSTITEM
					x += widthCelula;
					double st = itemNfe.vlTotalStItem;
					if (LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
						st = 0;
					}
					g.drawText(StringUtil.getStringValueToInterface(st, nuCasasDecimaisValoresMonetarios), x, y);
				}
				//Valor total
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.vlTotalItemPedido, nuCasasDecimaisValoresMonetarios), x - spaceBetweenLines, y);
			} else {
				//Descrição do item
				y += smallFont.fm.height + spaceBetweenLines;
				x = spaceBetweenLines;
				descricaoProduto = LavenderePdaConfig.ocultaColunaCdProduto ? StringUtil.getStringValue(itemNfe.getDsProduto()) : StringUtil.getStringValue(itemNfe.getDsProdutoWithKey());
				cst = StringUtil.getStringValue(itemNfe.cdClassificFiscal);
				if (ValueUtil.isNotEmpty(cst)) {
					cst = " - " + cst;
				}
				if (ValueUtil.isNotEmpty(ncm)) {
					ncm = " - " + ncm;
				}
				// Caso use campo adicional Total ST, deve-se concatenar cdUnidade com descrição para não haver quebra de layout em número de colunas.
				if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() && !LavenderePdaConfig.usaNfePorReferencia) {
					if (ValueUtil.isNotEmpty(itemNfe.cdUnidade)) {
						descricaoProduto = descricaoProduto + " - " + itemNfe.cdUnidade;
					}
				}
				larguraTexto = smallFont.fm.stringWidth(descricaoProduto) + smallFont.fm.stringWidth(ncm) + smallFont.fm.stringWidth(cst);
				g.setFont(smallFontBold);
				if (larguraTexto > widthImage) {
					descricaoProduto = MessageUtil.quebraLinhas(descricaoProduto, smallFont, widthImage - spaceBetweenLines * 2);
					descricaoMultiLinhas = StringUtil.split(descricaoProduto, '\n');
					if (descricaoMultiLinhas.length == 1) {
						descricaoProduto = descricaoMultiLinhas[0];
						descricaoMultiLinhas = new String[2];
						descricaoMultiLinhas[0] = descricaoProduto;
						descricaoMultiLinhas[1] = "";
					}
					for (int j = 0; j < 2; j++) {
						if (j == 1) {
							descricaoMultiLinhas[j] = StringUtil.getStringAbreviada(descricaoMultiLinhas[j], widthImage - smallFont.fm.stringWidth(ncm) - smallFont.fm.stringWidth(cst) - spaceBetweenLines * 2, smallFont);
							g.drawText(descricaoMultiLinhas[j] + ncm + cst, x, y);
						} else {
							g.drawText(descricaoMultiLinhas[j], x, y);
						}
						y += smallFontBold.fm.height + spaceBetweenLines;
					}
				} else {
					g.drawText(descricaoProduto + ncm + cst, x, y);
					y += smallFontBold.fm.height + spaceBetweenLines;
				}
				g.setFont(smallFont);
				if (!LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() || LavenderePdaConfig.usaNfePorReferencia) {
					//Unidade
					g.drawText(StringUtil.getStringValue(itemNfe.cdUnidade), x, y);
					x += widthCelula;
				}
				//Qtd do item
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.qtItemFisico), x, y);
				//Valor do unitário
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.vlBaseItemTabelaPreco, nuCasasDecimaisValoresMonetarios), x, y);
				//Valor com desconto
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.vlItemPedido, nuCasasDecimaisValoresMonetarios), x, y);
				//Valor ICMS
				x += widthCelula;
				double vlIcmsItem = 0;
				if (LavenderePdaConfig.isUsaCalculoStItemPedido()) {
					if (!(LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro))) {
						vlIcmsItem = itemNfe.vlTotalIcmsItem;
					}
				}
				g.drawText(StringUtil.getStringValueToInterface(vlIcmsItem, nuCasasDecimaisValoresMonetarios), x, y);
				//Valor AL. ICMS
				x += widthCelula;
				String aliquotaIcms = ValueUtil.VALOR_ZERO;
				if (LavenderePdaConfig.isUsaCalculoStItemPedido() && ValueUtil.isNotEmpty(pedido.getCliente().cdTributacaoCliente)) {
					aliquotaIcms = StringUtil.getStringValueToInterface(itemNfe.vlPctIcms, nuCasasDecimaisValoresMonetarios);
				}
				g.drawText(aliquotaIcms, x, y);
				if (LavenderePdaConfig.isUsaCampoTotalStItemImpressaoLayoutNfe() && !LavenderePdaConfig.usaNfePorReferencia) {
					//Valor VLTOTALSTITEM
					x += widthCelula;
					double st = itemNfe.vlTotalStItem;
					if (LavenderePdaConfig.usaInfoAdicionalImpressaoNfeVendaCigarros && ValueUtil.VALOR_SIM.equals(itemNfe.flCigarro)) {
						st = 0;
					}
					g.drawText(StringUtil.getStringValueToInterface(st, nuCasasDecimaisValoresMonetarios), x, y);
				}
				//Valor total
				x += widthCelula;
				g.drawText(StringUtil.getStringValueToInterface(itemNfe.vlTotalItemPedido, nuCasasDecimaisValoresMonetarios), x - spaceBetweenLines, y);
			}
			int lineY = y + smallFont.fm.height;
			if (usaLinhaFinalItem && i < size - 1) {
				g.drawLine(spaceBetweenLines, lineY, widthImage - spaceBetweenLines, lineY);
			}
		}
		return y;
	}
	
	private int montaLayoutCalculoImpostoNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
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
		g.drawRoundRect(0, y, widthImage, alturaLinha * 3 + smallFontBold.fm.height, 8);
		
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
		g.drawText(StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalIcms, nuCasasDecimaisValoresMonetarios), x, y);
		x += widthCelula;
		//Valor ICMS Substituição Tributária
		g.drawText(StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalSt, nuCasasDecimaisValoresMonetarios), x, y);
		x += widthCelula;
		//Valor IPI
		g.drawText(StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalIpi, nuCasasDecimaisValoresMonetarios), x, y);
		x += widthCelula;
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawLine(0, y, widthImage, y);
		x = spaceBetweenLines;
		g.setFont(smallFontBold);
		if (LavenderePdaConfig.isUsaCampoBaseCalculoIcmsImpressaoLayoutNfe()) {
			//Label B. Calc. ICMS
			g.drawText(Messages.IMPRESSAONFE_BASE_CALC_ICMS, x, y);
			x += widthCelula;
			g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		} else {
			//Label Frete
			g.drawText(Messages.IMPRESSAONFE_VALOR_FRETE, x, y);
			x += widthCelula;
			g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
		}
		if (LavenderePdaConfig.isUsaCampoBaseCalculoSTImpressaoLayoutNfe()) {
			// LABEL B. Calc. ST
			g.drawText(Messages.IMPRESSAONFE_BASE_CALC_ST, x, y);
		} else {
			if (LavenderePdaConfig.isUsaCampoTotalProdutosNotaImpressaoLayoutNfe()) {
				//Label Valor Total Produtos
				g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL_PRODUTOS, x, y);
			} else {
				//Label Seguro
				g.drawText(Messages.IMPRESSAONFE_VALOR_SEGURO, x, y);
			}
		}
		x += widthCelula;
		g.drawLine(widthCelula * 2, y, widthCelula * 2, y  +  alturaLinha);
		//Label Desconto
		g.drawText(Messages.IMPRESSAONFE_VALOR_DESCONTO, x, y);
		x += widthCelula;
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		g.setFont(smallFont);
		
		if (LavenderePdaConfig.isUsaCampoBaseCalculoIcmsImpressaoLayoutNfe()) {
			//Valor B. Calc. ICMS
			String vlBaseICMS = StringUtil.getStringValueToInterface(pedido.getInfoNfe().getSomaVlTotalBaseIcmsItem(), nuCasasDecimaisValoresMonetarios);
			g.drawText(vlBaseICMS, x, y);
			x += widthCelula;
		} else {
			//Valor Frete
			String vlFrete = StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalFrete, nuCasasDecimaisValoresMonetarios);
			g.drawText(vlFrete, x, y);
			x += widthCelula;
		}
		
		if (LavenderePdaConfig.isUsaCampoBaseCalculoSTImpressaoLayoutNfe()) {
			//Valor B. Calc. ST
			String vlBaseST = StringUtil.getStringValueToInterface(pedido.getInfoNfe().getSomaVlTotalBaseStItem(), nuCasasDecimaisValoresMonetarios);
			g.drawText(vlBaseST, x, y);
		} else {
			if (LavenderePdaConfig.isUsaCampoTotalProdutosNotaImpressaoLayoutNfe()) {
				//Valor Total Produtos NFE
				String vlTotalProdutosNfe = StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalProdutosNfe, nuCasasDecimaisValoresMonetarios);
				g.drawText(vlTotalProdutosNfe, x, y);
			} else {
				//Valor Seguro
				String vlTotalSeguro = StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalSeguro, nuCasasDecimaisValoresMonetarios);
				g.drawText(vlTotalSeguro, x, y);
			}
		}
		x += widthCelula;
		//Valor Desconto
		String vlDesconto = StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalDesconto, nuCasasDecimaisValoresMonetarios);
		g.drawText(vlDesconto, x, y);
		x += widthCelula;
		y += smallFont.fm.height + spaceBetweenLines;		
		g.drawLine(0, y, widthImage, y);
		x = spaceBetweenLines;
		g.setFont(smallFontBold);
		
		if (LavenderePdaConfig.isUsaCampoBaseCalculoSTImpressaoLayoutNfe()) {
			if (LavenderePdaConfig.isUsaCampoOutrasDespesasImpressaoLayoutNfe()) {
				//Label Outras Desp.
				g.drawText(Messages.IMPRESSAONFE_OUTRAS_DESPESAS, x, y);
				g.drawLine(widthCelula, y, widthCelula, y  +  alturaLinha);
				x += widthCelula;
				//Label Valor Total Produtos
				g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL_PRODUTOS, x, y);
				x += widthCelula;
			} else {
				//Label Valor Total Produtos
				g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL_PRODUTOS, x, y);
				x += widthCelula * 2;
			}
		} else {
			if (LavenderePdaConfig.isUsaCampoOutrasDespesasImpressaoLayoutNfe()) {
				//Label Outras Desp.
				g.drawText(Messages.IMPRESSAONFE_OUTRAS_DESPESAS, x, y);
			} else {
				//Label IBPT
				g.drawText(Messages.IMPRESSAONFE_VALOR_IBPT, x, y);
			}
			x += widthCelula * 2;
		}
		g.drawLine(widthCelula * 2, y, widthCelula * 2, y  + alturaLinha - 1);
		//Label Total Nota
		g.drawText(Messages.IMPRESSAONFE_VALOR_TOTAL_NOTA, x, y);
		g.setFont(smallFont);
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		if (LavenderePdaConfig.isUsaCampoBaseCalculoSTImpressaoLayoutNfe()) {
			if (LavenderePdaConfig.isUsaCampoOutrasDespesasImpressaoLayoutNfe()) {
				//Valor Outras Desp.
				String vlOutrasDesp = StringUtil.getStringValueToInterface(pedido.getInfoNfe().getSomaVlDespesaAcessoria(), nuCasasDecimaisValoresMonetarios);
				g.drawText(vlOutrasDesp, x, y);
				x += widthCelula;
				//Valor Total Produtos NFE
				String vlTotalProdutosNfe = StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalProdutosNfe, nuCasasDecimaisValoresMonetarios);
				g.drawText(vlTotalProdutosNfe, x, y);
				x += widthCelula;
			} else {
				//Valor Total Produtos NFE
				String vlTotalProdutosNfe = StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalProdutosNfe, nuCasasDecimaisValoresMonetarios);
				g.drawText(vlTotalProdutosNfe, x, y);
				x += widthCelula;
				x += widthCelula;
			}
		} else {
			if (LavenderePdaConfig.isUsaCampoOutrasDespesasImpressaoLayoutNfe()) {
				//Valor Outras Desp.
				String vlOutrasDesp = StringUtil.getStringValueToInterface(pedido.getInfoNfe().getSomaVlDespesaAcessoria(), nuCasasDecimaisValoresMonetarios);
				g.drawText(vlOutrasDesp, x, y);
				x += widthCelula;
				x += widthCelula;
			} else {
				//Valor IBPT
				String vlIbpt = pedido.getInfoNfe().vlIbpt == 0 ? ValueUtil.VALOR_ZERO : StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlIbpt, nuCasasDecimaisValoresMonetarios);
				g.drawText(vlIbpt, x, y);
				x += widthCelula;
				g.drawText(Messages.IMPRESSAONFE_FONTE_IBPT, x, y);
				x += widthCelula;
			}
		}
		//Valor Total Nota
		String vlTotalNota = pedido.getInfoNfe().vlTotalNfe == 0 ? ValueUtil.VALOR_ZERO : StringUtil.getStringValueToInterface(pedido.getInfoNfe().vlTotalNfe, nuCasasDecimaisValoresMonetarios);
		g.drawText(vlTotalNota, x, y);
		y += spaceBetweenLines;
		return y;
	}

	private int montaLayoutDadosAdicionaisNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int spaceBetweenLines = 5;
		int x = spaceBetweenLines;
		int alturaRect = 0;
		int widthCelula = widthImage / 3;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		alturaRect = smallFontBold.fm.height + spaceBetweenLines; 
		int alturaInicial = y;
		
		g.drawText(Messages.IMPRESSAONFE_DADOS_ADICIONAIS, x, y);
		x += widthCelula * 2;
		String[] reservadoFisco = Messages.IMPRESSAONFE_RESERVADO_FISCO.split(" ");
		String textoImpresso = "";
		for (String palavra : reservadoFisco) {
			if ((textoImpresso + palavra).length() > 17) {
				g.drawText(textoImpresso, x, y);
				textoImpresso = palavra + " ";
				y += smallFont.fm.height + spaceBetweenLines;
				alturaRect += smallFont.fm.height + spaceBetweenLines;
			} else {
				textoImpresso += palavra + " ";
			}
		}
		g.drawText(textoImpresso, x, y);

		x = spaceBetweenLines;
		g.setFont(smallFont);
		y += spaceBetweenLines;
		alturaRect += smallFont.fm.height + spaceBetweenLines;
		
		String descricao = StringUtil.getStringValue(getDescricaoComNuNotaGeral());
		String[] textoSplit = descricao.split(" ");
		textoImpresso = "";
		if (textoSplit != null && textoSplit.length > 0) {
			for (String palavra : textoSplit) {
				if ((textoImpresso + palavra).length() > 35) {
					g.drawText(textoImpresso, x, y);
					textoImpresso = palavra + " ";
					y += smallFont.fm.height + spaceBetweenLines;
					alturaRect += smallFont.fm.height + spaceBetweenLines;
				} else {
					textoImpresso += palavra + " ";
				}
			}
		}
		g.drawText(textoImpresso, x, y);
		
		y += (smallFont.fm.height + spaceBetweenLines) * 2;
		alturaRect += (smallFont.fm.height + spaceBetweenLines);
		
		g.drawLine(widthCelula * 2, alturaInicial, widthCelula * 2, alturaInicial  +  alturaRect);
		g.drawRoundRect(0, alturaInicial, widthImage, alturaRect, 8);
		return y;
	}

	private int montaLayoutDispositivosLegaisNfe(Graphics g, int posicaoY, int widthImage, Font smallFont, Font smallFontBold) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = widthImage / 2;;
		int alturaRect = 0;
		
		g.setFont(smallFontBold);
		y += smallFontBold.fm.height + spaceBetweenLines;
		
		int alturaInicialQuadro = y;
		x = spaceBetweenLines;
		widthCelula = widthImage / 2;
		
		g.drawText(Messages.IMPRESSAONFE_DISPOSITIVOS_LEGAIS, x, y);
		
		y += smallFontBold.fm.height + spaceBetweenLines;
		g.setFont(smallFont);
		
		String[] textoSplit = Messages.IMPRESSAONFE_MSG_CIGARRO.split(" ");
		String textoImpresso = "";
		int qtdLinhas = 1;
		for (String palavra : textoSplit) {
			if ((textoImpresso + palavra).length() > QT_CARACTERES_LINHA) {
				g.drawText(textoImpresso, x, y);
				textoImpresso = palavra + " ";
				y += smallFont.fm.height + spaceBetweenLines;
				qtdLinhas++;
			} else {
				textoImpresso += palavra + " ";
			}
		}
		g.drawText(textoImpresso, x, y);
		
		y += smallFontBold.fm.height + spaceBetweenLines + spaceBetweenLines;
		Representante representanteFilter = new Representante();
		representanteFilter.cdRepresentante = pedido.cdRepresentante;
		Representante representante = (Representante) RepresentanteService.getInstance().findByPrimaryKey(representanteFilter);
		representante = representante != null ? representante : representanteFilter;
		g.drawText(representante.getCdRepresentanteTxt() + " - " + representante.nmRepresentante, x, y);
		y += smallFontBold.fm.height + spaceBetweenLines + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAONFE_CIGARRO_TOTAL_VENDIDO +  StringUtil.getStringValueToInterface(pedido.getInfoNfe().getSomaQtItemFisicoCigarro()), x, y);
		y += smallFontBold.fm.height + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAONFE_BASE_ICMS_ST + StringUtil.getStringValueToInterface(pedido.getInfoNfe().getVlTotalBaseStItemCigarro()), x, y);
		x += widthCelula;
		g.drawText(Messages.IMPRESSAONFE_VALOR_ICMS_SUB_CIGARRO + StringUtil.getStringValueToInterface(pedido.getInfoNfe().getVlTotalStItemCigarro()), x, y);
		
		alturaRect = ((smallFont.fm.height + spaceBetweenLines) * (qtdLinhas+4)) + smallFontBold.fm.height + spaceBetweenLines;
		g.drawRoundRect(0, alturaInicialQuadro, widthImage, alturaRect, 8);
		
		return y;
	}
	
	private String getDescricaoComNuNotaGeral() throws SQLException {
		String descricaoNuNotaGeral = null;
		if(LavenderePdaConfig.isUsaAdicionaNuNotaGeralNaNotaFiscal()) {
			Representante representante = (Representante) RepresentanteService.getInstance().getRepresentanteById(pedido.getInfoNfe().cdRepresentante);
			descricaoNuNotaGeral = Messages.IMPRESSAONFE_NU_NOTA_GERAL_REPRESENTANTE + representante.nuNotaGeral + '\n';
		}
		Nfe nfe = pedido.getInfoNfe();
		String descricao = StringUtil.getStringValue(nfe.dsObservacao);
		descricao = ValueUtil.isNotEmpty(descricaoNuNotaGeral) ? descricaoNuNotaGeral + descricao : descricao;
		String dsMensagemNotaCredito = StringUtil.getStringValue(nfe.dsMensagemNotaCredito);
		return ValueUtil.isNotEmpty(dsMensagemNotaCredito) ? descricao + '\n' + dsMensagemNotaCredito : descricao;
	}
	
	private MonoImage geraComprovanteBoletoLayoutImpressoraTermica(final PedidoBoleto pedidoBoleto) throws ImageException, IOException, SQLException {
		int x = 0;
		int spaceFinal = 120;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int widthComprovante = 450;
		int heigthImage = 576;
		int widthImage = widthComprovante;
		//--
		MonoImage mi = new MonoImage(widthImage + spaceFinal, heigthImage);
		Graphics g = mi.getGraphics();
		g.setFont(smallFont);
		g.backColor = Color.WHITE;
		g.foreColor = Color.BLACK;
		g.fillRect(x, 0, widthImage + spaceFinal, heigthImage);
		montaLayoutComprovanteBoleto(g, x, widthComprovante, heigthImage, smallFont, smallFontBold, pedidoBoleto);
		//--
		if (VmUtil.isSimulador()) {
			return mi;
		} else {
			Image imgRotacionada = mi.getRotatedScaledInstance(100, -90, -1);
			return new MonoImage(imgRotacionada);
		}
	}
	
	private MonoImage geraBoletoLayoutImpressoraTermica(final PedidoBoleto pedidoBoleto) throws ImageException, IOException, SQLException {
		int x = 0;
		int spaceFinal = 120;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);
		}
		int espacoEntreComprovanteBoleto = 0;
		int widthComprovante = 0;
		int widthBoleto = 0;
		int widthReciboSacado = 0;
		if (LavenderePdaConfig.usaImpressaoComprovanteBoleto == 1) {
			widthComprovante = 450;
			espacoEntreComprovanteBoleto = 50;
		}
		if (LavenderePdaConfig.usaImpressaoReciboPagadorBoleto) {
			widthReciboSacado = 450;
			espacoEntreComprovanteBoleto = 50;
		}
		int heigthImage = 576;
		widthBoleto = (int) (heigthImage * 2.8);
		int widthImage = widthComprovante + widthBoleto + espacoEntreComprovanteBoleto + widthReciboSacado;
		if (widthReciboSacado > 0) {
			widthImage += espacoEntreComprovanteBoleto;
		}
		//--
		MonoImage mi = new MonoImage(widthImage + spaceFinal, heigthImage);
		Graphics g = mi.getGraphics();
		g.setFont(smallFont);
		g.backColor = Color.WHITE;
		g.foreColor = Color.BLACK;
		g.fillRect(x, 0, widthImage + spaceFinal, heigthImage);
		//comprovante
		if (LavenderePdaConfig.usaImpressaoReciboPagadorBoleto) {
			montaLayoutReciboSacadoBoleto(g, widthReciboSacado, heigthImage, smallFont, smallFontBold, pedidoBoleto);
			x += widthReciboSacado + espacoEntreComprovanteBoleto;
		} else if (LavenderePdaConfig.usaImpressaoComprovanteBoleto == 1) {
			montaLayoutComprovanteBoleto(g, x, widthComprovante, heigthImage, smallFont, smallFontBold, pedidoBoleto);
			x += widthComprovante + espacoEntreComprovanteBoleto;
		}
		//Dados boleto
		montaLayoutDadosBoleto(g, x, widthBoleto, heigthImage, smallFont, smallFontBold, pedidoBoleto);
		x += widthBoleto + espacoEntreComprovanteBoleto;
		if (LavenderePdaConfig.usaImpressaoComprovanteBoleto == 1 && LavenderePdaConfig.usaImpressaoReciboPagadorBoleto) {
			montaLayoutComprovanteBoleto(g, x, widthComprovante, heigthImage, smallFont, smallFontBold, pedidoBoleto);
		}
		//--
		if (VmUtil.isSimulador()) {
			return mi;
		} else {
			Image imgRotacionada = mi.getRotatedScaledInstance(100, -90, -1);
			return new MonoImage(imgRotacionada);
		}
	}
	
	private void montaLayoutComprovanteBoleto(Graphics g, int posicaoX, int widthComprovante, int heigthComprovante, Font smallFont, Font smallFontBold, PedidoBoleto pedidoBoleto) throws SQLException {
		int spaceBetweenLines = 5;
		int y = 0;
		int x = 0;
		int widthCelula = 0;
		int heightCelula = 0;
		int larguraTexto = 0;
		//pagador
		heightCelula = (smallFontBold.fm.height * 4) + (spaceBetweenLines * 2);
		g.setFont(smallFontBold);
		x = posicaoX + spaceBetweenLines;
		y = spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOBOLETO_PAGADOR, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String pagador = StringUtil.getStringValue(pedido.getCliente().getNmClienteImpressao());
		larguraTexto = smallFont.fm.stringWidth(pagador);
		if (larguraTexto > widthComprovante - spaceBetweenLines * 2) {
			pagador = StringUtil.getStringAbreviada(pagador, widthComprovante - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(pagador, x, y);
		y += smallFont.fm.height;
		String cnpjCpf = Messages.IMPRESSAOBOLETO_CNPJ_CPF + StringUtil.getStringValue(pedido.getCliente().nuCnpj);
		larguraTexto = smallFont.fm.stringWidth(cnpjCpf);
		if (larguraTexto > widthComprovante - spaceBetweenLines * 2) {
			cnpjCpf = StringUtil.getStringAbreviada(cnpjCpf, widthComprovante - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(cnpjCpf, x, y);
		y = 0;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthComprovante, heightCelula);
		//data documento
		y += heightCelula + spaceBetweenLines;
		x += spaceBetweenLines;
		g.setFont(smallFontBold);
		heightCelula = (smallFontBold.fm.height * 2) + (spaceBetweenLines * 2);
		widthCelula = widthComprovante / 2;
		g.drawText(Messages.IMPRESSAOBOLETO_DATA_DOCUMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoBoleto.dtDocumento)), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula + 1;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//numero documento
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_NUMERO_DOCUMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String numeroDocumento = StringUtil.getStringValue(pedidoBoleto.nuDocumento);
		larguraTexto = smallFont.fm.stringWidth(numeroDocumento); 
		if (larguraTexto > widthCelula - spaceBetweenLines) {
			numeroDocumento = StringUtil.getStringAbreviada(numeroDocumento, widthCelula - spaceBetweenLines, smallFont);
		}
		g.drawText(numeroDocumento, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula + 1, heightCelula);
		//data vencimento
		y += heightCelula + spaceBetweenLines;
		x = posicaoX + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VENCIMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoBoleto.dtVencimento)), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula + 1;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//valor
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VALOR_DOCUMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValueToInterface(pedidoBoleto.vlBoleto, LavenderePdaConfig.nuCasasDecimaisBoleto), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula + 1, heightCelula);
		//vendedor
		widthCelula = widthComprovante;
		y += heightCelula + spaceBetweenLines;
		x = posicaoX + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VENDEDOR, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String vendedor = StringUtil.getStringValue(SessionLavenderePda.getRepresentante().nmRepresentante);
		larguraTexto = smallFont.fm.stringWidth(vendedor); 
		if (larguraTexto > widthCelula - spaceBetweenLines) {
			vendedor = StringUtil.getStringAbreviada(vendedor, widthCelula - spaceBetweenLines, smallFont);
		}
		g.drawText(vendedor, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula + 1;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//data recebimento
		y += heightCelula + spaceBetweenLines;
		heightCelula = (smallFontBold.fm.height * 3) + (spaceBetweenLines * 2);
		x = posicaoX + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_DATA_RECEBIMENTO, x, y);
		y -= spaceBetweenLines + 1;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//assinatura
		y += heightCelula + spaceBetweenLines;
		x = posicaoX + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_ASSINATURA, x, y);
		y -= spaceBetweenLines + 1;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//agencia/cedente
		y += heightCelula + spaceBetweenLines;
		heightCelula = (smallFontBold.fm.height * 2) + (spaceBetweenLines * 2);
		x = posicaoX + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_AGENCIA_COD_CEDENTE, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(pedidoBoleto.nuAgenciaCodigoCedente), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula + 1;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//nosso numero
		y += heightCelula + spaceBetweenLines;
		x = posicaoX + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_NOSSO_NUMERO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String nossoNumero = StringUtil.getStringValue(pedidoBoleto.nuNossoNumero);
		BoletoConfig boletoConfig = BoletoConfigService.getInstance().getBoletoConfig(pedidoBoleto.cdBoletoConfig);
		if (BoletoConfig.MODO_BRADESCO.equals(boletoConfig.flModoBoleto)) {
			nossoNumero = PedidoBoletoService.getInstance().getNossoNumeroBradesco(nossoNumero, boletoConfig.nuCarteira);
		}
		larguraTexto = smallFont.fm.stringWidth(nossoNumero); 
		if (larguraTexto > widthCelula - spaceBetweenLines) {
			vendedor = StringUtil.getStringAbreviada(vendedor, widthCelula - spaceBetweenLines, smallFont);
		}
		g.drawText(nossoNumero, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula + 1;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//Pedido - Boleto
		y += heightCelula + spaceBetweenLines;
		x = posicaoX + spaceBetweenLines;
		g.setFont(smallFontBold);
		String nuPedido = Messages.IMPRESSAOBOLETO_PEDIDO + " " + StringUtil.getStringValue(pedidoBoleto.nuPedido);
		g.drawText(nuPedido, x, y);
		y += smallFont.fm.height;
		Vector pedidoBoletoList = pedido.getPedidoBoletoList();
		String nuBoleto = Messages.IMPRESSAOBOLETO_BOLETO + " " + StringUtil.getStringValue(pedidoBoleto.nuSequenciaBoletoPedido) + "/" + StringUtil.getStringValue(pedidoBoletoList != null ? pedidoBoletoList.size() : 0); 
		g.drawText(nuBoleto, x, y);
		x -= spaceBetweenLines;
		y -= smallFont.fm.height + spaceBetweenLines + 1;
		g.drawRect(x, y, widthComprovante, heigthComprovante - y);
	}
	
	private void montaLayoutReciboSacadoBoleto(Graphics g, int widthReciboSacado, int heightBoleto, Font smallFont, Font smallFontBold, PedidoBoleto pedidoBoleto) throws ImageException, SQLException {
		int spaceBetweenLines = 5;
		int x = 0;
		int y = 0;
		int heightCelula = 0;
		y += spaceBetweenLines;
		x = spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_RECIBO_PAGADOR, x, y);
		heightCelula = smallFont.fm.height + spaceBetweenLines;
		g.drawRect(0, 0, widthReciboSacado, heightCelula);
		y += spaceBetweenLines + smallFontBold.fm.height;
		heightCelula = (smallFontBold.fm.height * 3) + (spaceBetweenLines * 2);
		// Cedente
		g.drawText(Messages.IMPRESSAOBOLETO_BENEFICIARIO, x, y);
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		Empresa empresa = pedido.getEmpresa();
		g.drawText(empresa.nmEmpresa, x, y);
		y += smallFont.fm.height;
		String cpfCnpj = Messages.IMPRESSAOBOLETO_CNPJ_CPF + StringUtil.getStringValue(empresa.nuCnpj);
		g.drawText(cpfCnpj, x, y);
		y += smallFontBold.fm.height + spaceBetweenLines;
		y -= heightCelula;
		g.drawRect(0, y, widthReciboSacado, heightCelula);
		// Vencimento e valor documento
		y += heightCelula;
		heightCelula = smallFontBold.fm.height + smallFont.fm.height + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VENCIMENTO, x, y);
		g.drawLine(widthReciboSacado / 2, y, widthReciboSacado / 2, y + heightCelula);
		x = (widthReciboSacado / 2) + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOBOLETO_VALOR_DOCUMENTO, x, y);
		x = spaceBetweenLines;
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoBoleto.dtVencimento)), x, y);
		x = (widthReciboSacado / 2) + spaceBetweenLines;
		g.drawText(StringUtil.getStringValueToInterface(pedidoBoleto.vlBoleto, LavenderePdaConfig.nuCasasDecimaisBoleto), x, y);
		y += smallFontBold.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x = spaceBetweenLines;
		g.drawRect(0, y, widthReciboSacado, heightCelula);
		// Sacado
		y += heightCelula + spaceBetweenLines;
		heightCelula = smallFontBold.fm.height + smallFont.fm.height * 2 + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_PAGADOR, x, y);
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		Cliente cliente = pedido.getCliente();
		g.drawText(cliente.getNmClienteImpressao(), x, y);
		y += smallFont.fm.height;
		cpfCnpj = Messages.IMPRESSAOBOLETO_CNPJ_CPF + StringUtil.getStringValue(cliente.nuCnpj);
		g.drawText(cpfCnpj, x, y);
		y += smallFontBold.fm.height;
		y -= heightCelula;
		g.drawRect(0, y, widthReciboSacado, heightCelula);
		// Linha digitavel
		y += heightCelula + spaceBetweenLines;
		g.setFont(smallFontBold);
		heightCelula = smallFontBold.fm.height + smallFont.fm.height * 2 + spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOBOLETO_LINHA_DIGITAVEL, x, y);
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		String dsLinhaDigitavel = PedidoBoletoService.getInstance().formataDsLinhasDigitaveis(pedidoBoleto.dsLinhasDigitavel);
		g.drawText(dsLinhaDigitavel.substring(1, 39), x, y);
		y += smallFontBold.fm.height;
		g.drawText(dsLinhaDigitavel.substring(40), x, y);
		y += smallFontBold.fm.height;
		y -= heightCelula;
		g.drawRect(0, y, widthReciboSacado, heightCelula);
		// Agencia/Cod Cedente
		y += heightCelula + spaceBetweenLines;
		heightCelula = smallFontBold.fm.height + smallFont.fm.height + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_AGENCIA_COD_CEDENTE, x, y);
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		g.drawText(pedidoBoleto.nuAgenciaCodigoCedente, x, y);
		y += smallFontBold.fm.height;
		y -= heightCelula;
		g.drawRect(0, y, widthReciboSacado, heightCelula);
		// Nosso numero
		y += heightCelula + spaceBetweenLines;
		heightCelula = smallFontBold.fm.height + smallFont.fm.height + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_NOSSO_NUMERO, x, y);
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		BoletoConfig boletoConfig = BoletoConfigService.getInstance().getBoletoConfig(pedidoBoleto.cdBoletoConfig);
		String nossoNumero = StringUtil.getStringValue(pedidoBoleto.nuNossoNumero);
		if (BoletoConfig.MODO_BRADESCO.equals(boletoConfig.flModoBoleto)) {
			nossoNumero = PedidoBoletoService.getInstance().getNossoNumeroBradesco(nossoNumero, boletoConfig.nuCarteira);
		}
		g.drawText(nossoNumero, x, y);
		y += smallFontBold.fm.height;
		y -= heightCelula;
		g.drawRect(0, y, widthReciboSacado, heightCelula);
		// Nfe, serie e boleto
		y += heightCelula + spaceBetweenLines;
		heightCelula = smallFontBold.fm.height * 2 + spaceBetweenLines;
		g.setFont(smallFontBold);
		Nfe nfe = pedido.getInfoNfe();
		String nuNfe = Messages.IMPRESSAOBOLETO_NFE + StringUtil.getStringValue(nfe != null ? nfe.nuNfe : ValueUtil.VALOR_NI);
		g.drawText(nuNfe, x, y);
		x = smallFontBold.fm.stringWidth(nuNfe) + BaseContainer.WIDTH_GAP + spaceBetweenLines;
		String serie = Messages.IMPRESSAOBOLETO_SERIE + StringUtil.getStringValue(nfe != null ? nfe.dsSerieNfe : ValueUtil.VALOR_NI);
		g.drawText(serie, x, y);
		x = spaceBetweenLines;
		y += smallFontBold.fm.height;
		Vector pedidoBoletoList = pedido.getPedidoBoletoList();
		String nuBoleto = Messages.IMPRESSAOBOLETO_BOLETO + " " + StringUtil.getStringValue(pedidoBoleto.nuSequenciaBoletoPedido) + "/" + StringUtil.getStringValue(pedidoBoletoList != null ? pedidoBoletoList.size() : 0);
		g.drawText(nuBoleto, x, y);
		y += smallFontBold.fm.height;
		y -= heightCelula;
		g.drawRect(0, y, widthReciboSacado, heightCelula);
		// Local de pagamento
		y += heightCelula + spaceBetweenLines;
		heightCelula = heightBoleto - y;
		g.drawText(Messages.IMPRESSAOBOLETO_LOCAL_PAGAMENTO, x, y);
		y += smallFontBold.fm.height;
		g.setFont(smallFont);
		g.drawText(Messages.IMPRESSAOBOLETO_DOMICILIO_CREDOR, x, y);
		y += smallFont.fm.height;
		g.drawText(empresa.nmEmpresa, x, y);
		String autenticacao = Messages.IMPRESSAOBOLETO_AUTENTICACAO_MECANICA;
		y = heightBoleto - smallFont.fm.height - spaceBetweenLines;
		x = widthReciboSacado - (smallFontBold.fm.stringWidth(autenticacao) + spaceBetweenLines);
		g.setFont(smallFontBold);
		g.drawText(autenticacao, x, y);
		y -= heightCelula;
		g.drawRect(0, 0, widthReciboSacado, heightBoleto);
	}
	
	private void montaLayoutDadosBoleto(Graphics g, int posicaoX, int widthBoleto, int heighBoleto, Font smallFont, Font smallFontBold, PedidoBoleto pedidoBoleto) throws ImageException, SQLException {
		Font bigFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 30);
		if (!VmUtil.isSimulador()) {
			bigFontBold = Font.getFont(MPT_TIPO_FONTE, true, 30);
		}
		int y = 0;
		int x = posicaoX;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int heightCelula = 0;
		int larguraTexto = 0;
		g.setFont(smallFontBold);
		x += spaceBetweenLines;
		y += spaceBetweenLines;
		//Logo Banco / Nome banco
		widthCelula = widthBoleto / 2;
		heightCelula = (smallFontBold.fm.height * 2) + (spaceBetweenLines * 2);
		BoletoConfig boletoConfig = BoletoConfigService.getInstance().getBoletoConfig(pedidoBoleto.cdBoletoConfig);
		int larguraLogo = 0;
		if (boletoConfig.imBanco != null && boletoConfig.imBanco.length > 0) {
			try {
				Image imgBanco = UiUtil.getSmoothScaledImageForMaxSize(UiUtil.getImage(boletoConfig.imBanco), smallFontBold.fm.height * 100, smallFontBold.fm.height * 2);
				if (imgBanco != null) {
					g.drawImage(imgBanco, x, y);
					larguraLogo = imgBanco.getWidth();
					x += larguraLogo + spaceBetweenLines;
				}
			} catch (Throwable ex) {
				ExceptionUtil.handle(ex);
			}
		}
		y = (heightCelula - smallFontBold.fm.height) / 2;
		String nmBanco = StringUtil.getStringValue(boletoConfig.nmBanco);
		larguraTexto = smallFontBold.fm.stringWidth(nmBanco);
		if (larguraTexto + larguraLogo + spaceBetweenLines * 2 > widthCelula) {
			nmBanco = StringUtil.getStringAbreviada(nmBanco, widthCelula - larguraLogo - spaceBetweenLines, smallFontBold);
		}
		g.drawText(nmBanco, x, y);
		x += larguraTexto;
		//dsLinhaDigitavel
		g.setFont(bigFontBold);
		y = (heightCelula - bigFontBold.fm.height) / 2;
		String dsdigitavel = StringUtil.getStringValue(pedidoBoleto.dsLinhasDigitavel);
		x += spaceBetweenLines * 4;
		g.drawLine(x, 0, x, heightCelula);
		x += 4;
		String nuBanco = StringUtil.getStringValue(boletoConfig.nuBanco);
		g.drawText(nuBanco, x, y);
		x += bigFontBold.fm.stringWidth(nuBanco) + 1;
		g.drawLine(x, 0, x, heightCelula);
		x += spaceBetweenLines * 2;
		dsdigitavel = PedidoBoletoService.getInstance().formataDsLinhasDigitaveis(dsdigitavel);
		g.drawText(dsdigitavel, x, y);
		y = (smallFontBold.fm.height * 2) + (spaceBetweenLines * 2);
		g.drawRect(posicaoX, 0, widthBoleto, heightCelula + 1);
		//Local de pagamento
		widthCelula = (widthBoleto / 4) * 3;
		x = posicaoX + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_LOCAL_PAGAMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String localPagamento = StringUtil.getStringValue(pedidoBoleto.dsLocalPagamento);
		larguraTexto = smallFont.fm.stringWidth(localPagamento);
		if (larguraTexto > widthCelula) {
			localPagamento = StringUtil.getStringAbreviada(localPagamento, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(localPagamento, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//vencimento
		widthCelula = widthBoleto / 4;
		x = posicaoX + (widthBoleto - widthCelula) + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VENCIMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoBoleto.dtVencimento)), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula);
		//beneficiario
		widthCelula = (widthBoleto / 4) * 3;
		x = posicaoX + spaceBetweenLines;
		y += heightCelula + spaceBetweenLines - 1;
		heightCelula = (smallFontBold.fm.height * 3) + (spaceBetweenLines * 2);
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_BENEFICIARIO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String beneficiario = StringUtil.getStringValue(pedido.getEmpresa().nmEmpresa + " " +  Messages.IMPRESSAOBOLETO_CNPJ + " " + pedido.getEmpresa().nuCnpj);
		larguraTexto = smallFont.fm.stringWidth(beneficiario);
		if (larguraTexto > widthCelula) {
			beneficiario = StringUtil.getStringAbreviada(beneficiario, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(beneficiario, x, y);
		y += smallFont.fm.height;
		Empresa empresa = pedido.getEmpresa();
		String endBeneficiario = StringUtil.getStringValue(empresa.dsLogradouro) + " " + StringUtil.getStringValue(empresa.nuLogradouro) + " " + StringUtil.getStringValue(empresa.dsBairro) + " " + StringUtil.getStringValue(empresa.dsCidade) + " " + StringUtil.getStringValue(empresa.dsCep);
		larguraTexto = smallFont.fm.stringWidth(endBeneficiario);
		if (larguraTexto > widthCelula) {
			endBeneficiario = StringUtil.getStringAbreviada(endBeneficiario, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(endBeneficiario, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//agencia/codigo cedente
		widthCelula = widthBoleto / 4;
		x = posicaoX + (widthBoleto - widthCelula) + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_AGENCIA_COD_CEDENTE, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(pedidoBoleto.nuAgenciaCodigoCedente), x, y);
		y += smallFont.fm.height * 2 + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula);
		//data documento
		widthCelula = ((widthBoleto / 4) * 3) / 5;
		x = posicaoX + spaceBetweenLines;
		y += heightCelula + spaceBetweenLines - 1;
		heightCelula = (smallFontBold.fm.height * 2) + (spaceBetweenLines * 2);
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_DATA_DOCUMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoBoleto.dtDocumento)), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//numero documento
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_NUMERO_DOCUMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String numeroDocumento = StringUtil.getStringValue(pedidoBoleto.nuDocumento);
		larguraTexto = smallFont.fm.stringWidth(numeroDocumento);
		if (larguraTexto > widthCelula) {
			numeroDocumento = StringUtil.getStringAbreviada(numeroDocumento, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(numeroDocumento, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//especie documento
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_ESPECIE_DOCUMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String especieDocumento = StringUtil.getStringValue(pedidoBoleto.dsEspecieDocumento);
		larguraTexto = smallFont.fm.stringWidth(especieDocumento);
		if (larguraTexto > widthCelula) {
			especieDocumento = StringUtil.getStringAbreviada(especieDocumento, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(especieDocumento, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//aceite
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_ACEITE, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String aceite = "";
		aceite = ValueUtil.VALOR_SIM.equals(pedidoBoleto.flAceite) ? FrameworkMessages.VALOR_SIM : FrameworkMessages.VALOR_NAO;
		g.drawText(aceite, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//data processamento
		x += widthCelula + spaceBetweenLines;
		widthCelula += 8;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_DATA_PROCESSAMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValue(DateUtil.formatDateDDMMYYYY(pedidoBoleto.dtProcessamento)), x, y); 
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//nosso numero
		widthCelula = widthBoleto / 4;
		x = posicaoX + (widthBoleto - widthCelula) + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_NOSSO_NUMERO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String nossoNumero = "";
		nossoNumero = StringUtil.getStringValue(pedidoBoleto.nuNossoNumero);
		if (BoletoConfig.MODO_BRADESCO.equals(boletoConfig.flModoBoleto)) {
			nossoNumero = PedidoBoletoService.getInstance().getNossoNumeroBradesco(nossoNumero, boletoConfig.nuCarteira);
		}
		larguraTexto = smallFont.fm.stringWidth(nossoNumero);
		if (larguraTexto > widthCelula) {
			nossoNumero = StringUtil.getStringAbreviada(nossoNumero, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(nossoNumero, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula);
		//uso banco
		widthCelula = ((widthBoleto / 4) * 3) / 5;
		x = posicaoX + spaceBetweenLines;
		y += heightCelula + spaceBetweenLines - 1;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_USO_BANCO, x, y);
		if (BoletoConfig.MODO_BRADESCO.equals(boletoConfig.flModoBoleto)) {
			x = posicaoX + (widthCelula / 2) + spaceBetweenLines;
			g.drawText(Messages.IMPRESSAOBOLETO_CIP, x, y);
			g.setFont(smallFont);
			g.drawText(StringUtil.getStringValue(empresa.nuCip), x, y + smallFont.fm.height);
			int xLine = (widthCelula / 2) + posicaoX;
			g.drawLine(xLine, y - spaceBetweenLines, xLine, y - spaceBetweenLines + heightCelula);
		}
		x = posicaoX + spaceBetweenLines;
		y += smallFont.fm.height;
		g.setFont(smallFont);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//carteira
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_CARTEIRA, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String carteira = BoletoConfig.MODO_CEF.equals(boletoConfig.flModoBoleto) ? StringUtil.getStringValue(boletoConfig.dsCarteira) : StringUtil.getStringValue(pedidoBoleto.nuCarteira);
		larguraTexto = smallFont.fm.stringWidth(carteira);
		if (larguraTexto > widthCelula) {
			carteira = StringUtil.getStringAbreviada(carteira, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(carteira, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//especie
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_ESPECIE, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String especie = StringUtil.getStringValue(pedidoBoleto.dsEspecie);
		larguraTexto = smallFont.fm.stringWidth(especie);
		if (larguraTexto > widthCelula) {
			especie = StringUtil.getStringAbreviada(especie, widthCelula - spaceBetweenLines * 2, smallFont);
		}
		g.drawText(especie, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//quantidade
		x += widthCelula + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_QUANTIDADE, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//valor
		x += widthCelula + spaceBetweenLines;
		widthCelula += 8;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VALOR, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines + 1;
		g.drawRect(x, y, widthCelula, heightCelula);
		//valor documento
		widthCelula = widthBoleto / 4;
		x = posicaoX + (widthBoleto - widthCelula) + spaceBetweenLines;
		y += spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VALOR_DOCUMENTO, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		g.drawText(StringUtil.getStringValueToInterface(pedidoBoleto.vlBoleto, LavenderePdaConfig.nuCasasDecimaisBoleto), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula);
		//texto de responsabilidade do cedente
		y += heightCelula + spaceBetweenLines - 1;
		x = posicaoX + spaceBetweenLines;
		widthCelula = (widthBoleto / 4) * 3;
		heightCelula = (smallFont.fm.height * 5) + (spaceBetweenLines * 2);
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_TEXTO_CEDENTE, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String textoCedente = StringUtil.getStringValue(pedidoBoleto.dsObsCedente);
		List<String> listMsgCedente ;
		if (textoCedente.contains("\r")) {
			listMsgCedente = Arrays.asList(textoCedente.split("\r"));
		} else {
			listMsgCedente = Arrays.asList(textoCedente.split("\n"));
		}
		int nuLinhas = 0;
		for (String msg : listMsgCedente) {
			g.drawText(msg, x, y);
			y += smallFontBold.fm.height;
			nuLinhas++;
			if (nuLinhas >= 4) {
				y += spaceBetweenLines;
				break;
			}
		}
		if (listMsgCedente.size() < 4) {
			y += (smallFont.fm.height * (4 - listMsgCedente.size())) + spaceBetweenLines;
		}
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//desconto/abatimento
		widthCelula = widthBoleto / 4;
		heightCelula = heightCelula / 3;
		x = posicaoX + (widthBoleto - widthCelula) + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_DESCONTO_ABATIMENTO, x, y);
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula);
		//outras deduções
		x += spaceBetweenLines;
		y += heightCelula - 1;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_OUTRAS_DEDUCOES, x, y);
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula);
		//multa/mora
		x += spaceBetweenLines;
		y += heightCelula - 1;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_MULTA_MORA, x, y);
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula + 2);
		//pagador
		y += heightCelula + spaceBetweenLines + 1;
		x = posicaoX + spaceBetweenLines;
		widthCelula = (widthBoleto / 4) * 3;
		heightCelula = (smallFontBold.fm.height * 3) + (spaceBetweenLines * 2);
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_PAGADOR, x, y);
		y += smallFont.fm.height;
		g.setFont(smallFont);
		String nmRazaoSocial = StringUtil.getStringValue(pedido.getCliente().getNmClienteImpressao());
		String cnpjCpf = StringUtil.getStringValue(pedido.getCliente().nuCnpj);
		String pagadorLinha1 = StringUtil.getStringValue(nmRazaoSocial + " - " + Messages.IMPRESSAOBOLETO_CNPJ_CPF + cnpjCpf );
		larguraTexto = smallFont.fm.stringWidth(pagadorLinha1);
		int larguraNfe = 0;
		String nfe = "";
		if (pedido.getInfoNfe().nuNfe != 0) {
			nfe = Messages.IMPRESSAOBOLETO_NFE + StringUtil.getStringValue(pedido.getInfoNfe().nuNfe);
			larguraNfe = smallFont.fm.stringWidth(nfe);
			x = posicaoX + widthCelula - larguraNfe - spaceBetweenLines;
			g.drawText(nfe, x, y);
		}
		if (larguraTexto + larguraNfe > widthCelula) {
			pagadorLinha1 = StringUtil.getStringAbreviada(pagadorLinha1, widthCelula - larguraNfe - spaceBetweenLines * 2, smallFont);
		}
		x = posicaoX + spaceBetweenLines;
		g.drawText(pagadorLinha1, x, y);
		y += smallFont.fm.height;
		Cliente cliente = (Cliente)ClienteService.getInstance().findByRowKeyDyn(pedido.getCliente().getRowKey());
		String logradouro = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSLOGRADOUROCOMERCIAL"));
		String bairro = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSBAIRROCOMERCIAL"));
		String cidade = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCIDADECOMERCIAL"));
		String estado = StringUtil.getStringValue(pedido.getCliente().cdEstadoComercial);
		String cep = StringUtil.getStringValue(cliente.getHashValuesDinamicos().get("DSCEPCOMERCIAL"));
		StringBuffer pagadorLinha2 = new StringBuffer();
		pagadorLinha2.append(logradouro);
		if (ValueUtil.isNotEmpty(bairro)) {
			pagadorLinha2.append(" - ");
			pagadorLinha2.append(bairro);
		}
		if (ValueUtil.isNotEmpty(cidade)) {
			pagadorLinha2.append(" - ");
			pagadorLinha2.append(cidade);
		}
		if (ValueUtil.isNotEmpty(estado)) {
			pagadorLinha2.append(" - ");
			pagadorLinha2.append(estado);
		}
		if (ValueUtil.isNotEmpty(cep)) {
			pagadorLinha2.append(" - ");
			pagadorLinha2.append(cep);
		}
		if (ValueUtil.isNotEmpty(boletoConfig.dsMensagemSacador)) {
			pagadorLinha2.append(" - ");
			pagadorLinha2.append(boletoConfig.dsMensagemSacador);
		}
		larguraTexto = smallFont.fm.stringWidth(pagadorLinha2.toString());
		int larguraSerieNfe = 0;
		String serieNfe = "";
		if (pedido.getInfoNfe().dsSerieNfe != null) {
			serieNfe = Messages.IMPRESSAOBOLETO_SERIE + StringUtil.getStringValue(pedido.getInfoNfe().dsSerieNfe);
			larguraSerieNfe = smallFont.fm.stringWidth(serieNfe);
			x = posicaoX + widthCelula - larguraSerieNfe - spaceBetweenLines;
			g.drawText(serieNfe, x, y);
		}
		if (larguraTexto + larguraSerieNfe > widthCelula) {
			pagadorLinha2.append(StringUtil.getStringAbreviada(pagadorLinha2.toString(), widthCelula - larguraSerieNfe - spaceBetweenLines * 2, smallFont));
		}
		x = posicaoX + spaceBetweenLines;
		g.drawText(pagadorLinha2.toString(), x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		y -= heightCelula;
		x -= spaceBetweenLines;
		g.drawRect(x, y, widthCelula, heightCelula);
		//outros acrescimos
		widthCelula = widthBoleto / 4;
		heightCelula = heightCelula / 2;
		x = posicaoX + (widthBoleto - widthCelula) + spaceBetweenLines;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_OUTROS_ACRESCIMOS, x, y);
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula);
		//valor cobrado
		x += spaceBetweenLines;
		y += heightCelula - 1;
		g.setFont(smallFontBold);
		g.drawText(Messages.IMPRESSAOBOLETO_VALOR_COBRADO, x, y);
		x -= spaceBetweenLines;
		g.drawRect(x - 1, y, widthCelula + 1, heightCelula + 1);
		//cdBarras
		widthCelula = (widthBoleto / 4) * 3;
		x = posicaoX + spaceBetweenLines * 2;
		y += heightCelula + spaceBetweenLines;
		int alturaCodigoBarra = (int) (smallFont.fm.height * 3.75);
		if (ValueUtil.isNotEmpty(pedidoBoleto.cdBarras)) {
			Image barcodeImg = BarCodeUtil.generateBarcode(pedidoBoleto.cdBarras, alturaCodigoBarra, widthCelula, 0, BarcodeType.INTERLEAVED2OF5);
			g.drawImage(barcodeImg, x, y);
		}
		//Autenticacao Mecanica
		String texto = Messages.BOLETO_OFFLINE_AUTENT_MECANICA;
		larguraTexto = bigFontBold.fm.stringWidth(texto);
		g.drawText(texto, posicaoX + widthBoleto - larguraTexto - spaceBetweenLines, heighBoleto - smallFont.fm.height - 4);
		//pedido
		if (ValueUtil.isNotEmpty(pedidoBoleto.nuPedido)) {
			String nuPedido = Messages.IMPRESSAOBOLETO_PEDIDO + " " + StringUtil.getStringValue(pedidoBoleto.nuPedido);
			larguraTexto = smallFont.fm.stringWidth(nuPedido);
			g.drawText(nuPedido, posicaoX + widthBoleto - larguraTexto - spaceBetweenLines, y);
			y += smallFont.fm.height + spaceBetweenLines;
		}
		//numero boleto
		Vector pedidoBoletoList = pedido.getPedidoBoletoList();
		String nuBoleto = Messages.IMPRESSAOBOLETO_BOLETO + " " + StringUtil.getStringValue(pedidoBoleto.nuSequenciaBoletoPedido) + "/" + StringUtil.getStringValue(pedidoBoletoList != null ? pedidoBoletoList.size() : 0);
		larguraTexto = smallFont.fm.stringWidth(nuBoleto);
		g.drawText(nuBoleto, posicaoX + widthBoleto - larguraTexto - spaceBetweenLines, y);
		y -= smallFont.fm.height + spaceBetweenLines * 2;
		g.drawRect(posicaoX, y, widthBoleto, heighBoleto - y);
	}
	
	private void imprimePedidoLayoutImpressoraTermicaDpp250(int widthImage, String tipoFonte, int smallFontSize) throws ImageException, IOException, SQLException, InvalidNumberException {
		int y = 2;
		int spaceFinal = 80;
		Font bigFont = Font.getFont(tipoFonte, true, 22);
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		int heigthImage = 0;
		//--
		heigthImage = bigFont.fm.height + ((smallFont.fm.height) * (17 + getNuLinhasObservacaoPedido(widthImage, tipoFonte, smallFontSize))) + getHeightItensPedido(widthImage, smallFont);
		//--
		MonoImage mi = new MonoImage(widthImage, heigthImage + spaceFinal);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.fillRect(0, y, widthImage, heigthImage + spaceFinal);

		//Cabeçalho
		y = montaLayoutCabecalhoImpressoraTermicaDpp250(g, y, widthImage, tipoFonte, smallFontSize);
		//Itens do pedido
		y = montaLayoutItensPedidoImpressoraTermicaDpp250(g, y, widthImage, tipoFonte, smallFontSize);
		//Rodapé
		montaLayoutRodapeImpressoraTerminaDpp250(g, y, widthImage, tipoFonte, smallFontSize);
		
		if (VmUtil.isSimulador()) {
			File file = null;
			String nmImagePrint = Convert.appendPath(Settings.appPath, "impressaoPedido_" + pedido.nuPedido + ".png");
			FileUtil.deleteFile(nmImagePrint);
			file = FileUtil.criaFile(nmImagePrint);
			mi.createPng(file);
			file.close();
			UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, nmImagePrint));
		} else {
			if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 2) {
				for (int i = 0; i < LavenderePdaConfig.qtCopiasImpressaoPedido; i++) {
					mi.printTo(zebraPrinter);
				}
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 6) {
				mptPrinter.printImg(mi);
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 4) {
				zonerichPrinter.printImg(mi);
			} else if (LavenderePdaConfig.usaImpressaoPedidoViaBluetooth == 5) {
				try {
					dpp250Printer.printImg(mi);
				} catch (IOException e) {
					tentaNovamenteImpressaoDpp250(mi);
				}
			}
		} 
	}
	
	private void tentaNovamenteImpressaoDpp250(MonoImage mi) throws InvalidNumberException, ImageException {
		try {
			dpp250Printer = Dpp250Printer.getNewInstance(enderecoImpressora, portaImpressora);
			dpp250Printer.setNuCopies(LavenderePdaConfig.qtCopiasImpressaoPedido);
			dpp250Printer.printImg(mi);
		} catch (IOException e) {
			UiUtil.showErrorMessage(Messages.PRINTER_DPP250_CONEXAO_ERRO);
		}
	}
	
	private int getNuLinhasObservacaoPedido(int widthImage, String tipoFonte, int smallFontSize) {
		int spaceBetweenLines = 5;
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		String descricao = StringUtil.getStringValue(pedido.getHashValuesDinamicos().get("DSOBSERVACAO"));
		if (ValueUtil.isNotEmpty(descricao)) {
			int larguraTexto = smallFont.fm.stringWidth(descricao);
			if (larguraTexto > widthImage) {
				descricao = MessageUtil.quebraLinhas(descricao, smallFont, widthImage - spaceBetweenLines * 7);
				String[] descricaoMultiLinhas = StringUtil.split(descricao, '\n');
				return descricaoMultiLinhas.length;
			}
		}
		return 1;
	}

	private int montaLayoutCabecalhoImpressoraTermicaDpp250(Graphics g, int posicaoY, int widthImage, String tipoFonte, int smallFontSize) throws SQLException {
		int y = posicaoY;
		int x = 2;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		int larguraLabel = 0;
		Font smallFontBold = Font.getFont(tipoFonte, true, smallFontSize);
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		//--
		g.setFont(smallFontBold);
		x = spaceBetweenLines;
		g.drawLine(x, y + 2, widthImage - x, y + 2);
		
		g.setFont(smallFont);
		//Número pedido
		y += spaceBetweenLines;
		x = spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOPEDIDO_NUMERO + pedido.nuPedido, x, y + spaceBetweenLines);
		
		//Data emissão
		widthCelula = (widthImage / 2);
		x = widthCelula + spaceBetweenLines;
		widthCelula = widthImage / 2;
		x += ((widthCelula) - (smallFont.fm.stringWidth(StringUtil.getStringValue(pedido.dtEmissao)) + 8));
		g.drawText(StringUtil.getStringValue(pedido.dtEmissao), x, y + spaceBetweenLines);
		
		//Nome empresa
		Empresa empresa = EmpresaService.getInstance().getEmpresa(pedido.cdEmpresa);
		if (ValueUtil.isNotEmpty(empresa.nmEmpresa)) {
			y += smallFont.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			String nmEmpresa = empresa.nmEmpresa;
			larguraTexto = smallFont.fm.stringWidth(nmEmpresa);
			if (larguraTexto + larguraLabel > widthImage) {
				nmEmpresa = StringUtil.getStringAbreviada(nmEmpresa, widthImage - larguraLabel, smallFont);
			}
			g.drawText(nmEmpresa, x, y);
		}
		
		y += smallFont.fm.height + spaceBetweenLines;
		g.drawLine(spaceBetweenLines, y + 2, widthImage - spaceBetweenLines, y + 2);
		
		//Nome do cliente
		y += smallFont.fm.height;
		x = spaceBetweenLines;
		String nome = pedido.getCliente().getNmClienteImpressao();
		larguraTexto = smallFont.fm.stringWidth(nome);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_NOME);
		if (larguraTexto + larguraLabel > widthImage) {
			nome = StringUtil.getStringAbreviada(nome, widthImage - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_NOME + nome, x, y);
		
		//Condição de pagamento
		y += smallFont.fm.height;
		x = spaceBetweenLines;
		String condPag = pedido.getCondicaoPagamento() != null && pedido.getCondicaoPagamento().dsCondicaoPagamento != null ? pedido.getCondicaoPagamento().dsCondicaoPagamento : " - ";
		larguraTexto = smallFont.fm.stringWidth(condPag);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_COND_PAG);
		if (larguraTexto + larguraLabel > widthImage) {
			condPag = StringUtil.getStringAbreviada(condPag, widthImage - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_COND_PAG + condPag, x, y);

		//Cidade
		y += smallFont.fm.height;
		String dsCidade = pedido.getCliente() != null && pedido.getCliente().dsCidadeComercial != null ? pedido.getCliente().dsCidadeComercial : " - ";
		larguraTexto = smallFont.fm.stringWidth(dsCidade);
		larguraLabel = smallFont.fm.stringWidth(Messages.IMPRESSAOPEDIDO_CIDADE);
		if (larguraTexto + larguraLabel > widthImage) {
			dsCidade = StringUtil.getStringAbreviada(dsCidade, widthImage - larguraLabel, smallFont);
		}
		g.drawText(Messages.IMPRESSAOPEDIDO_CIDADE + dsCidade, x, y);

		//Observacao
		String descricao = StringUtil.getStringValue(pedido.getHashValuesDinamicos().get("DSOBSERVACAO"));
		y += smallFont.fm.height;
		if (ValueUtil.isNotEmpty(descricao)) {
			String[] descricaoMultiLinhas;
			larguraTexto = smallFont.fm.stringWidth(descricao);
			if (larguraTexto > widthImage) {
				descricao = MessageUtil.quebraLinhas(descricao, smallFont, widthImage - spaceBetweenLines * 7);
				descricaoMultiLinhas = StringUtil.split(descricao, '\n');
				for (int j = 0; j < descricaoMultiLinhas.length; j++) {
					if (j == 0) {
						g.drawText(Messages.IMPRESSAOPEDIDO_OBS + descricaoMultiLinhas[j], x, y);
					} else {
						g.drawText(descricaoMultiLinhas[j], x, y);
					}
					y += smallFont.fm.height;
				}
			} else {
				g.drawText(descricao, x, y);
				y += smallFont.fm.height;
			}
		}
		x = spaceBetweenLines;
		g.drawLine(x, y + spaceBetweenLines, widthImage - x, y + spaceBetweenLines);
		return y;
	}
	
	private int montaLayoutItensPedidoImpressoraTermicaDpp250(Graphics g, int posicaoY, int widthImage, String tipoFonte, int smallFontSize) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		int widthCelula = 0;
		int larguraTexto = 0;
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		Font bigFontBold = Font.getFont(MPT_TIPO_FONTE, true, 22);

		//Itens do pedido
		y += smallFont.fm.height + spaceBetweenLines;
		g.setFont(smallFont);

		//Cabeçalho dos itens do pedido
		x = spaceBetweenLines;
		g.drawText(Messages.IMPRESSAOPEDIDO_DESCRICAO, x, y);
		
		widthCelula = (widthImage / 2);
		x = widthCelula + spaceBetweenLines;
		widthCelula = widthImage / 2;
		x += ((widthCelula) - (bigFontBold.fm.stringWidth("UUU")) -9);
		g.drawText(Messages.IMPRESSAOPEDIDO_UN, x, y);
		
		y += smallFont.fm.height + spaceBetweenLines;
		x = spaceBetweenLines;
		widthCelula = widthImage / 4;
		g.drawText(Messages.IMPRESSAOPEDIDO_COD, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_QTDE, x, y);
		
		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_UNIT, x, y);

		x += widthCelula;
		g.drawText(Messages.IMPRESSAOPEDIDO_TOT, x, y);
		
		g.drawLine(spaceBetweenLines, y + spaceBetweenLines * 4, widthImage - spaceBetweenLines, y + spaceBetweenLines * 4);
		y += spaceBetweenLines;
		//Dados dos Itens
		g.setFont(bigFontBold);
		ItemPedido itemPedido;
		for (int i = 0; i < pedido.itemPedidoList.size(); i++) {
			itemPedido = (ItemPedido) pedido.itemPedidoList.items[i];
			//Descrição do item
			String descricao;
			String[] descricaoMultiLinhas;
			//Descrição do item
			y += bigFontBold.fm.height + spaceBetweenLines;
			x = spaceBetweenLines;
			descricao = StringUtil.getStringValue(itemPedido.getDsProduto());
			larguraTexto = bigFontBold.fm.stringWidth(descricao);
			if (larguraTexto > widthImage - bigFontBold.fm.stringWidth("UUUU")) {
				descricao = MessageUtil.quebraLinhas(descricao, bigFontBold, widthImage - bigFontBold.fm.stringWidth("UUUU"));
				descricaoMultiLinhas = StringUtil.split(descricao, '\n');
				for (int j = 0; j < 2; j++) {
					g.drawText(descricaoMultiLinhas[j], x, y);
					if (j == 0) {
						y += smallFont.fm.height;
					}
				}
			} else {
				g.drawText(descricao, x, y);
			}
			//Unidade
			widthCelula = (widthImage / 2);
			x = widthCelula + spaceBetweenLines;
			widthCelula = widthImage / 2;
			x += ((widthCelula) - (bigFontBold.fm.stringWidth("UUU")) -9);
			String cdUnidade = itemPedido.cdUnidade != null && itemPedido.cdUnidade.length() > 3 ? itemPedido.cdUnidade.substring(0, 3) : itemPedido.cdUnidade;
			g.drawText(cdUnidade, x, y);

			y += smallFont.fm.height;
			//codigo
			x = spaceBetweenLines;
			widthCelula = widthImage / 4;
			g.drawText(itemPedido.cdProduto, x, y);

			//Qtd do item
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemPedido.getQtItemFisico()), x, y);
			
			//Valor do unitário
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlItemPedido), x, y);

			//Valor Total
			x += widthCelula;
			g.drawText(StringUtil.getStringValueToInterface(itemPedido.vlTotalItemPedido), x, y);
		}
		y += smallFont.fm.height;
		x = spaceBetweenLines;
		g.drawLine(x, y + spaceBetweenLines, widthImage - x, y + spaceBetweenLines);
		return y;
	}
	
	
	private void montaLayoutRodapeImpressoraTerminaDpp250(Graphics g, int posicaoY, int widthImage, String tipoFonte, int smallFontSize) throws SQLException {
		int y = posicaoY;
		int x = 0;
		int spaceBetweenLines = 5;
		Font smallFont = Font.getFont(tipoFonte, false, smallFontSize);
		
		g.setFont(smallFont);
		//N Itens
		x = spaceBetweenLines;
		y += smallFont.fm.height;
		g.drawText(Messages.IMPRESSAOPEDIDO_NITENS_PEDIDO + StringUtil.getStringValueToInterface(pedido.itemPedidoList.size()), x, y);

		//Desc Pedido
		y += smallFont.fm.height;
		g.drawText(Messages.IMPRESSAOPEDIDO_DESC_PEDIDO + StringUtil.getStringValueToInterface(pedido.vlPctDesconto), x, y);
		
		//Total Pedido
		y += smallFont.fm.height;
		g.drawText(Messages.IMPRESSAOPEDIDO_TOTAL_PEDIDO + StringUtil.getStringValueToInterface(pedido.vlTotalPedido), x, y);

		//Total Frete
		y += smallFont.fm.height;
		g.drawText(Messages.IMPRESSAOPEDIDO_TOTAL_FRETE + StringUtil.getStringValueToInterface(pedido.vlFrete), x, y);

		//Transportadora
		Transportadora transportadora = TransportadoraService.getInstance().getTransportadora(pedido.cdTransportadora);
		if (transportadora != null) {
			y += smallFont.fm.height;
			g.drawText(transportadora != null ? transportadora.toString() : null, x, y);
		}
		y += smallFont.fm.height;
		g.drawLine(x, y + spaceBetweenLines, widthImage - x, y + spaceBetweenLines);
	}
	
	public void imprimeNotaPromissoria() throws Exception {
		if (VmUtil.isSimulador() && LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth > 0) {
			printer = new StarPrinterSimulator();
		} else {
			if (ValueUtil.isEmpty(enderecoImpressora)) { 
				RemoteDevice remoteDevice = BluetoothUtil.chooseRemoteDeviceBluetooth();
				if (remoteDevice != null) {
					enderecoImpressora = remoteDevice.getBluetoothAddress();
				}
			}
			if (ValueUtil.isNotEmpty(enderecoImpressora)) {
				saveEnderecoPortaIfNecessary();
				if (LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth == 6) {
					mptPrinter = MPTPrinter.getNewInstance(enderecoImpressora, portaImpressora);
				}
			}
		}
		if (pedido != null && pedido.dtEmissao != null) {
			int nuParcelas = pedido.getCondicaoPagamento().getNuParcelas();
			Date dtVencimento = DateUtil.getDateValue(pedido.dtEmissao);
			Vector imgList = new Vector(nuParcelas);
			double[] vlParcelas = ParcelaPedidoService.getInstance().getVlParcelas(pedido.vlTotalPedido, nuParcelas);
			dtVencimento.advance(pedido.getCondicaoPagamento().nuIntervaloEntrada);
			Settings.screenDensity = PRINT_SCREEN_DENSITY;
			try {
				imgList.addElement(geraNotaImpressoraLayoutImpressoraTermica(dtVencimento, 1, nuParcelas, vlParcelas[0]));
				for (int i = 2; i <= nuParcelas; i++) {
					dtVencimento.advance(pedido.getCondicaoPagamento().nuIntervaloParcelas);
					imgList.addElement(geraNotaImpressoraLayoutImpressoraTermica(dtVencimento, i, nuParcelas, vlParcelas[i-1]));
				}
			} finally {
				Settings.screenDensity = DEVICE_SCREEN_DENSITY;
			}
			imprimeImagensPromissoria(imgList);
		}
	}
	
	private void imprimeImagensPromissoria(Vector imgList) throws IOException, ImageException{
		if (ValueUtil.isNotEmpty(imgList)) {
			int size = imgList.size();
			if (VmUtil.isSimulador()) {
				for (int i = 0; i < size; i++) {
					MonoImage mi = (MonoImage) imgList.items[i];
					String stringImgPrint  = Convert.appendPath(Settings.appPath, "impressaoNotaPromissoria_" + pedido.nuPedido + "_" + i + ".png");
					FileUtil.deleteFile(stringImgPrint);
					File f = FileUtil.criaFile(stringImgPrint);
					mi.createPng(f);
					f.close();
				}
				UiUtil.showSucessMessage(MessageUtil.getMessage(Messages.IMPRESSAOPEDIDO_MSG_SUCESSO_SIMULADOR, Settings.appPath));
			} else if ((LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth == 3 || LavenderePdaConfig.usaImpressaoNotaPromissoriaViaBluetooth == 6) && mptPrinter != null) {
				mptPrinter.printImg(imgList);
			}
		}
	}
	
	private MonoImage geraNotaImpressoraLayoutImpressoraTermica(Date dtVencimento, int nuParcela, int qtParcela, double vlParcela) throws ImageException, SQLException, IOException {
		int spaceBetweenLines = 7;
		int y = spaceBetweenLines;
		Font smallFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 26);
		Font smallFontBold = Font.getFont(ZEBRA_TIPO_FONTE, true, 26);
		Font bigFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 32);
		Font smallerFont = Font.getFont(ZEBRA_TIPO_FONTE, false, 22);
		if (!VmUtil.isSimulador()) {
			smallFont = Font.getFont(MPT_TIPO_FONTE, false, 26);
			smallFontBold = Font.getFont(MPT_TIPO_FONTE, true, 26);
			bigFont = Font.getFont(MPT_TIPO_FONTE, false, 32);
			smallerFont = Font.getFont(MPT_TIPO_FONTE, false, 22);
		}
		int heightImage = 576;
		int widthImage = (heightImage * 7) / 3;
		MonoImage mi = new MonoImage((widthImage + 150), heightImage);
		Graphics g = mi.getGraphics();
		g.backColor = Color.WHITE;
		g.foreColor = 0xFF000000;
		int x = 5;
		int finalX = widthImage;
		int startingX = spaceBetweenLines * 2;
		g.fillRect(0, y, (widthImage + 150), heightImage);
		y = spaceBetweenLines;
		g.setFont(bigFont);
		String text = Messages.IMPRESSAONOTAPROMISSORIA_REPUBLICA;
		int centerTitle = finalX - ((widthImage / (2)) + (bigFont.fm.stringWidth(text) / 2));
		y += spaceBetweenLines;
		g.drawText(text, centerTitle, y);
		g.setFont(smallFont);
		y += bigFont.fm.height + spaceBetweenLines;
		int lineWidth = 0;
		g.drawText((text = Messages.IMPRESSAONOTAPROMISSORIA_NOTA_PROMISSOARIA), spaceBetweenLines * 2, y);
		x =  finalX - (smallFont.fm.stringWidth(" de ") * 5 + smallFont.fm.stringWidth((text = Messages.IMPRESSAONOTAPROMISSORIA_VENCIMENTO)) + spaceBetweenLines + smallFont.fm.stringWidth(Date.monthNames[dtVencimento.getMonth()]));
		g.drawText(text, x, y);
		x += smallFont.fm.stringWidth(text);
		text = " " + dtVencimento.getDay() + " ";
		lineWidth = smallFont.fm.stringWidth(text);
		g.drawText(text, x, y);
		g.drawLine(x, y + smallFont.fm.height + 1, x + lineWidth, y + smallFont.fm.height + 1);
		x += lineWidth;
		g.drawText(Messages.IMPRESSAONOTAPROMISSORIA_DE, x, y);
		x += smallFont.fm.stringWidth(" de");
		text = " " + Date.monthNames[dtVencimento.getMonth()] + " ";
		lineWidth = smallFont.fm.stringWidth(text);
		g.drawText(text, x, y);
		g.drawLine(x, y + smallFont.fm.height + 1, x + lineWidth, y + smallFont.fm.height + 1);
		x += lineWidth;
		g.drawText(Messages.IMPRESSAONOTAPROMISSORIA_DE, x, y);
		x += smallFont.fm.stringWidth(" de");
		text = " " + dtVencimento.getYear() + " ";
		lineWidth = smallFont.fm.stringWidth(text);
		g.drawText(text, x, y);
		g.drawLine(x, y + smallFont.fm.height + 1, x + lineWidth, y + smallFont.fm.height + 1);
		y += smallFont.fm.height + ((spaceBetweenLines + 5) * 2) ;
		x = startingX;
		g.setFont(bigFont);
		text = Messages.IMPRESSAONOTAPROMISSORIA_NRO + " " + pedido.nuPedido + " - " + Messages.IMPRESSAONOTAPROMISSORIA_PARCELA + " "  + nuParcela + "/" + qtParcela;
		g.drawText(text, x + 5, y);
		g.drawRect(x, y - 3, bigFont.fm.stringWidth(text) + 10, bigFont.fm.height + 3);
		x =  finalX - (spaceBetweenLines + bigFont.fm.stringWidth("99999999999,99"));
		g.drawText((text = Messages.IMPRESSAONOTAPROMISSORIA_RS), x, y);
		int x2 = x + bigFont.fm.stringWidth(text) + spaceBetweenLines;
		g.drawText(StringUtil.getStringValueToInterface(vlParcela), x2, y);
		g.drawRect(x - 3, y - 3, (spaceBetweenLines + bigFont.fm.stringWidth("99999999999,99")), bigFont.fm.height + 3);
		y += bigFont.fm.height + spaceBetweenLines + 3;
		text = Messages.IMPRESSAONOTAPROMISSORIA_AOS;
		g.setFont(smallFont);
		x = startingX;
		g.drawText(text, x, y);
		x += smallFont.fm.stringWidth(text);
		String dtExtenso = Util.getValorPorExtenso(dtVencimento.getDay()) + (dtVencimento.getDay() == 1 ? " dia do mês " : " dias do mês ") + Date.monthNames[dtVencimento.getMonth()] + " de " + Util.getValorPorExtenso(dtVencimento.getYear());
		g.drawText(dtExtenso, x, y);
		y += smallFont.fm.height;
		g.drawLine(x, y + 1, finalX - spaceBetweenLines, y + 1);
		y += spaceBetweenLines;
		x = startingX;
		text = Messages.IMPRESSAONOTAPROMISSORIA_PAGAREI;
		g.drawText(text, x, y);
		g.setFont(smallFontBold);
		x += smallFont.fm.stringWidth(text);
		text = Messages.IMPRESSAONOTAPROMISSORIA_NOTA_PROMISSOARIA;
		g.drawText(text, x, y);
		x += smallFontBold.fm.stringWidth(text);
		g.setFont(smallFont);
		text = Messages.IMPRESSAONOTAPROMISSORIA_A;
		g.drawText(text, x, y);
		x += smallFont.fm.stringWidth(text) + 6;
		g.drawText(pedido.getEmpresa().nmEmpresa, x, y);
		y += smallFont.fm.height;
		g.drawLine(x, y + 1, finalX - spaceBetweenLines, y + 1);
		y += spaceBetweenLines;
		x = startingX;
		text = Messages.IMPRESSAONOTAPROMISSORIA_OU;
		g.drawText(text, x, y);
		x += smallFont.fm.stringWidth(text);
		int vlInteiro = (int)vlParcela;
		int vlDecimal = ValueUtil.getIntegerValue(Math.abs((vlParcela - vlInteiro) * 100));
		text = Util.getValorPorExtenso(vlInteiro) + " " + Messages.IMPRESSAONOTAPROMISSORIA_REAIS;
		if (vlDecimal > 0) {
			text += " e " + Util.getValorPorExtenso(vlDecimal) + " " + Messages.IMPRESSAONOTAPROMISSORIA_CENTAVOS;
		}
		g.drawText(text, x, y);
		y += smallFont.fm.height;
		g.drawLine(x, y + 1, finalX - spaceBetweenLines, y + 1);
		y += spaceBetweenLines;
		x = startingX;
		g.drawText(Messages.IMPRESSAONOTAPROMISSORIA_MOEDA, x, y);
		y += smallFont.fm.height + spaceBetweenLines;
		text = Messages.IMPRESSAONOTAPROMISSORIA_PAGAVEL;
		g.drawText(text, x, y);
		x += smallFont.fm.stringWidth(text);
		g.drawText(pedido.getEmpresa().dsCidade, x, y);
		y += smallFont.fm.height;
		g.drawLine(x, y + 1, finalX - spaceBetweenLines, y + 1);
		y += (spaceBetweenLines * 2) + 1;
		x = startingX;
		text = Messages.IMPRESSAONOTAPROMISSORIA_EMITENTE + ":";
		g.drawText(text , x, y);
		x += smallFont.fm.stringWidth(text) + 6;
		g.drawText(pedido.getCliente().getNmClienteImpressao(), x, y);
		g.drawLine(x, y + smallFont.fm.height + 1, finalX - spaceBetweenLines, y + smallFont.fm.height + 1);
		x = startingX;
		Date dataAtual = new Date();
		g.setFont(smallerFont);
		y += smallFont.fm.height + 2;
		x = startingX;
		y += smallerFont.fm.height + spaceBetweenLines;
		g.setFont(smallFont);
		text = Messages.IMPRESSAONOTAPROMISSORIA_CPF + ":";
		g.drawText(text, x, y);
		x += smallFont.fm.stringWidth(text) + 6;
		g.drawText(pedido.getCliente().nuCnpj, x, y);
		y += smallFont.fm.height + 1;
		g.drawLine(x, y, finalX - spaceBetweenLines, y);
		y += 1;
		g.setFont(smallerFont);
		
		y += smallerFont.fm.height + spaceBetweenLines;
		x = startingX;
		Cliente c = pedido.getCliente();
		g.setFont(smallFont);
		text = Messages.IMPRESSAONOTAPROMISSORIA_ENDERECO + ":";
		g.drawText(text, x, y + 1);
		x += smallFont.fm.stringWidth(text) + 6;
		g.drawText(c.dsLogradouroComercial + ", " + c.dsBairroComercial + ", " + c.dsEstadoComercial, x, y);
		g.drawLine(x, y + smallFont.fm.height + 1, finalX - spaceBetweenLines, y + smallFont.fm.height + 1);
		g.setFont(smallerFont);
		y += smallFont.fm.height + 1;
		y += smallFont.fm.height + spaceBetweenLines;
		x = startingX;
		g.setFont(smallFont);
		text = Messages.IMPRESSAONOTAPROMISSORIA_LOCAL + ":";
		g.drawText(text, x, y);
		x += smallFont.fm.stringWidth(text) + 6;
		text = pedido.getEmpresa().dsCidade + ", " + dataAtual.getDay() + " de " + Date.monthNames[dataAtual.getMonth()] + " de " + dataAtual.getYear();
		g.drawText(text, x, y);
		g.drawLine(x, y + smallFont.fm.height + 1, (smallFont.fm.stringWidth(text)) / 3 + (finalX - (widthImage / 2)) - spaceBetweenLines / 2, y + smallFont.fm.height + 1);
		g.setFont(smallerFont);
		y += smallFont.fm.height;
		x = spaceBetweenLines * 2 + (smallFont.fm.stringWidth(text)) / 3 + (finalX - (widthImage / 2)) - spaceBetweenLines / 2;
		text = Messages.IMPRESSAONOTAPROMISSORIA_ASSINATURA;
		g.drawText(text, finalX - ( (finalX - (x + spaceBetweenLines)) / 2) - (smallerFont.fm.stringWidth(text) / 2), y + 1);
		g.drawLine(x, y + 1, finalX, y + 1);
		x = startingX;
		y += smallFont.fm.height;
		y += spaceBetweenLines;
		g.drawRect(startingX - (spaceBetweenLines), spaceBetweenLines, finalX, y);
		startingX = finalX + (spaceBetweenLines * 2) + 100;
		finalX += 100 + widthImage;
		if (!VmUtil.isSimulador()) {
			Image image = mi.getRotatedScaledInstance(100, -90, -1);
			mi = new MonoImage(image);
		}
		return mi;
	}

	private String getTipoFonte() {
		return tipoFonte;
	}

	private void setTipoFonte(String tipoFonte) {
		this.tipoFonte = tipoFonte;
	}
	
}