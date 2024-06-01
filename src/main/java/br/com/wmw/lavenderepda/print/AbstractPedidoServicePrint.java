package br.com.wmw.lavenderepda.print;

import java.sql.SQLException;

import br.com.wmw.framework.print.AbstractPrinter;
import br.com.wmw.framework.print.Dpp250Printer;
import br.com.wmw.framework.print.MPTPrinter;
import br.com.wmw.framework.print.ZebraPrinter;
import br.com.wmw.framework.print.ZonerichPrinter;
import br.com.wmw.framework.util.PrinterUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.business.domain.ConfigInterno;
import br.com.wmw.lavenderepda.business.domain.Pedido;
import br.com.wmw.lavenderepda.business.service.ConfigInternoService;
import totalcross.sys.Settings;
import totalcross.ui.font.Font;

public abstract class AbstractPedidoServicePrint {
	
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
	private static final int MARGIN_TEXT = 3;
	public static final String NM_LOGO_EMPRESA_IMPRESSAO = "logo_empresa_impressao.png";
	public static final double DEVICE_SCREEN_DENSITY = Settings.screenDensity;
	public static final double PRINT_SCREEN_DENSITY = VmUtil.isSimulador() ? 0.7 : 0.8;
	protected AbstractPrinter printer;
	protected ZebraPrinter zebraPrinter;
	protected MPTPrinter mptPrinter;
	protected Dpp250Printer dpp250Printer;
	protected ZonerichPrinter zonerichPrinter;
	protected Pedido pedido;
	protected String enderecoImpressora;
	protected int portaImpressora;

	public AbstractPedidoServicePrint(Pedido pedido) throws SQLException {
		this.pedido = pedido;
		enderecoImpressora = ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configEnderecoImpressora);
		portaImpressora = ValueUtil.getIntegerValue(ConfigInternoService.getInstance().getVlConfigInterno(ConfigInterno.configPortaImpressora));
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

	protected int getPosicaoCentralizada(int widthImage, String mensagem, Font font) {
		int larguraTexto = font.fm.stringWidth(mensagem);
		int posicaoInicialMensagem = (widthImage - larguraTexto) / 2;
		return posicaoInicialMensagem;
	}
	
	protected int getPosicaoAlinhadoDireita(int x, int widthImage, String mensagem, Font font) {
		int larguraTexto = font.fm.stringWidth(mensagem);
		int posicaoInicialMensagem = (widthImage - larguraTexto) - MARGIN_TEXT;
		return posicaoInicialMensagem;
	}
	
	protected int getPosicaoAlinhadoEsquerda(int x, int widthImage) {
		int posicaoInicialMensagem = widthImage + MARGIN_TEXT;
		return posicaoInicialMensagem;
	}
	
}
