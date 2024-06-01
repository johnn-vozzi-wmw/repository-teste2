package br.com.wmw.lavenderepda.report.pdf;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.integration.dao.CrudDbxDao;
import br.com.wmw.framework.util.ExceptionUtil;
import br.com.wmw.framework.util.FileUtil;
import br.com.wmw.framework.util.MessageUtil;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.framework.util.VmUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.report.pdf.stream.PdfStream;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Elementos;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Espaco;
import br.com.wmw.lavenderepda.report.pdf.tagmap.GenericTag;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Layout;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Linha;
import br.com.wmw.lavenderepda.report.pdf.tagmap.NovaPagina;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Texto;
import totalcross.io.File;
import totalcross.io.IOException;
import totalcross.sql.PreparedStatement;
import totalcross.sql.ResultSet;
import totalcross.sys.Convert;
import totalcross.util.pdf.PaperSize;
import totalcross.util.pdf.StandardFonts;

public class SemanticInterpreter {
	
	private static final char SPACE = ' ';
	private static final double COEFICIENTE_CENTRO_LINHA = 2.5;
	private static final String EXTENSAO_PDF = ".pdf";
	private static final int LIMITE_MINIMO_LARGURA_ALTURA_PIXELS = 50;
	private static final int LIMITE_MAXIMO_LARGURA_ALTURA_PIXELS = 14400;
	private static final int LIMITE_LINHAS_APROX = 5700;  //limite de aproximadamente 100 páginas para o pdf
	private static final double COURIER_CHAR_WIDTH = 7.2;
	private static final int COURIER_CHAR_WIDTH_ROUND_TOP = (int) COURIER_CHAR_WIDTH + 1;
	
	private PdfStream pdfStream;
	
	private String filePath;
	private boolean arquivoGerado;
	private String msgErro;
	
	private int nuFonte;
	private boolean bold;
	private boolean italico;
	
	private int largura;
	private int altura;
	private int margemX;
	private int margemY;
	private int x;
	private int y;
	
	private HashMap<String, String> internalParams = new HashMap<String, String>();
	private char alinhamentoAtual;
	
	private StringBuilder textoBuffer = new StringBuilder(128);
	private StringBuilder linhaDeTextoBuffer = new StringBuilder(64);
	private List<String> linhaTextoQuebradoList = new ArrayList<String>();
	
	private final static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	private static final String CHARS_COMUNS_UTF8_CP1252_ESC_PDF = " ,.0123456789aeosrindmutclpAEOSRINDMUTCLPvghqbfzjxkwyVGHQBFZJXKWY!\"#$%&'*+,-./:;<=>?@[]^_`{|}~";
	private final int commonCharLength = CHARS_COMUNS_UTF8_CP1252_ESC_PDF.length();
	private StringBuilder strToHexBuilder = new StringBuilder(256);
	
	private int lineCount;
	private boolean controleTamanhoPdf = !VmUtil.isSimulador();
	private boolean controlePrimeiraLinha;
	

	private void inicializaPdfWriter() {
		this.pdfStream = new PdfStream();
		this.reloadFonteAtual();
		this.y = margemY;
		this.x = margemX;
	}
	
	private void configuraDimensoesPagina(Layout layout) {
		this.largura = layout.largura;
		if (this.largura < LIMITE_MINIMO_LARGURA_ALTURA_PIXELS || this.largura > LIMITE_MAXIMO_LARGURA_ALTURA_PIXELS) {
			this.largura = PaperSize.A4_WIDTH;
		}
		this.altura = layout.altura;
		if (!isPaginaContinua() && (this.altura < LIMITE_MINIMO_LARGURA_ALTURA_PIXELS || this.altura > LIMITE_MAXIMO_LARGURA_ALTURA_PIXELS)) {
			this.altura = PaperSize.A4_HEIGHT;
		}
		this.margemX = layout.margemX;
		if (this.margemX >= largura >> 1) {
			this.margemX = 10;
		}
		this.margemY = layout.margemY;
		if (!isPaginaContinua() && this.margemY >= altura >> 1) {
			this.margemY = 10;
		}
	}

	private boolean isPaginaContinua() {
		return this.altura == 0;
	}

	protected void interpretaElementosEGeraArquivo(String pathDestino, String nmArquivo, List<Layout> layoutList) throws SQLException, IOException {
		int qtLayouts = layoutList.size();
		boolean multiplosLayoutsBase = qtLayouts > 1;
		for (int i = 0; i < qtLayouts; i++) {
			controlePrimeiraLinha = true;
			lineCount = 0;
			Layout layout = layoutList.get(i);
			configuraDimensoesPagina(layout);
			inicializaPdfWriter();
			geraConteudoAPartirDoLayout(layout, null);
			geraArquivoPdf(pathDestino, getNomeArquivo(nmArquivo, multiplosLayoutsBase, i + 1));
		}
		if (qtLayouts == 0) {
			msgErro = Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_NAO_ENCONTRADO;
		}
	}

	private String getNomeArquivo(String nmArquivo, boolean multiplesBaseLayout, int index) {
		return multiplesBaseLayout ? nmArquivo += "_" + index : nmArquivo;
	}

	private void reloadFonteAtual() {
		String fonte = StandardFonts.COURIER;
		if (bold && italico) {
			fonte = StandardFonts.COURIER_BOLDOBLIQUE;
		} else if (bold) {
			fonte = StandardFonts.COURIER_BOLD;
		} else if (italico) {
			fonte = StandardFonts.COURIER_OBLIQUE;
		}
		setFont(fonte, 12);
	}
	
	private void setFont(String nmFonte, int nuFonte) {
		this.nuFonte = nuFonte;
		this.pdfStream.setFont(nmFonte);
	}
	
	private void setFonteNormal() {
		bold = italico = false;
		setFont(StandardFonts.COURIER, this.nuFonte);
	}
	
	private void setFonteNegrito() {
		setFont(StandardFonts.COURIER_BOLD, this.nuFonte);
		bold = true;
	}
	
	private void setFonteItalico() {
		setFont(StandardFonts.COURIER_OBLIQUE, this.nuFonte);
		italico = true;
	}
	
	private void setFonteNegritoItalico() {
		setFont(StandardFonts.COURIER_BOLDOBLIQUE, this.nuFonte);
		italico = bold = true;
	}
	
	private void novaLinha() {
		if (!controlePrimeiraLinha) {
			y += posicaoNovaLinha();
			controleTamanhoMaximo();
		}
		controlePrimeiraLinha = false;
	}

	private void controleTamanhoMaximo() {
		lineCount++;
		if (controleTamanhoPdf && lineCount >= LIMITE_LINHAS_APROX) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_TAMANHO_MAXIMO);
		}
	}
	
	private int posicaoNovaLinha() {
		return nuFonte * 6 / 5;
	}
	
	private void geraConteudoAPartirDoLayout(Layout layout, ResultSet rs) throws SQLException {
		for (GenericTag tag : layout.genericTagList) {
			processaTag(tag, rs);
		}
	}

	private ResultSet montaSql(Elementos elementos, ResultSet parentRs) throws SQLException {
		if (ValueUtil.isEmpty(elementos.sqlPs)) {
			return null;
		}
		return executaConsulta(elementos, parentRs);
	}

	private ResultSet executaConsulta(Elementos elementos, ResultSet parentRs) throws SQLException {
		PreparedStatement ps = CrudDbxDao.getCurrentDriver().prepareStatement(elementos.sqlPs);		
		int size = elementos.sqlParamsNamesList.size();
		for (int i = 0; i < size; i++) {
			ps.setString(i + 1, getValorParamsInternosOuResultSet(parentRs, elementos.sqlParamsNamesList.get(i)));
		}
		ResultSet resultAtual = ps.executeQuery();
		return resultAtual;
	}

	protected void addParamsInternos(String column, String value) {
		internalParams.put(column.toUpperCase(), value);
	}

	private String getValorParamsInternosOuResultSet(ResultSet rs, String subTexto) throws SQLException {
		if (rs == null) {
			return getValorParamInterno(subTexto);
		} else {
			try {
				return rs.getString(subTexto);
			} catch (SQLException e) {
				return getValorParamInterno(subTexto);
			}
		}
	}

	private String getValorParamInterno(String subTexto) {
		String sybTextoUpperCase = subTexto.toUpperCase();
		if (!internalParams.containsKey(sybTextoUpperCase)) {
			throw new ValidationException(MessageUtil.getMessage(Messages.RELATORIO_PDF_OFFLINE_VALOR_NAO_ENCONTRADO, subTexto));
		}
		return internalParams.get(sybTextoUpperCase);
	}
	
	private void processaElementos(ResultSet parentRs, Elementos elemento) throws SQLException {
		
		try (ResultSet rs = montaSql(elemento, parentRs);) {
			if (rs == null) {
				return;
			}
			while (rs.next()) {
				for (GenericTag tag : elemento.genericTagList) {
					processaTag(tag, rs);
				}
			}
		}
	}

	private void processaTag(GenericTag tag, ResultSet rs) throws SQLException {
		if (tag instanceof Linha) {
			desenhaLinha();
		} else if (tag instanceof Texto) {
			escreveTexto((Texto) tag, rs);
		} else if (tag instanceof Espaco) {
			espaco(((Espaco) tag).tamanho);
		} else if (tag instanceof NovaPagina) {
			novaPagina();
			reloadFonteAtual();
			y = margemY;
		} else if (tag instanceof Elementos) {
			Elementos elem = (Elementos) tag;
			processaElementos(rs, elem);
		} else if (tag instanceof Layout) {
			Layout layout = (Layout) tag;
			if (layout != null && ValueUtil.isNotEmpty(layout.id)) {
				geraConteudoAPartirDoLayout(layout, rs);
			}
		}
	}

	private void escreveTexto(Texto texto, ResultSet rs) throws SQLException {
		configuraEstiloGeral(texto);
		configuraAlinhamentoGeral(texto);
		String[] textoOriginal = texto.getTexto();
		int length = textoOriginal.length;
		String[] textoFormatado = new String[length];
		StringBuilder textoBufferLocal = textoBuffer;
		textoBufferLocal.setLength(0);
		for (byte i = 0; i < length; i++) {
			preparaTextoBufferParaCelula(textoBufferLocal, rs, textoOriginal[i]);
			textoFormatado[i] = formataNumero(texto, textoBufferLocal, i);
		}
		pdfStream.incrementaPixelsPreenchidos(posicaoNovaLinha());
		if (texto.getCelulaDim().length > 0) {
			escreveComTamanhoCelulasDiferentes(textoBufferLocal, texto, textoFormatado);
		} else {
			escreveSimples(textoBufferLocal, texto, textoFormatado);
		}
		resetaEstiloGeral(texto);
	}

	private void configuraEstiloGeral(Texto texto) {
		boolean negritoLigado = texto.getNegrito().length == 1 && texto.getNegrito()[0];
		boolean italicoLigado = texto.getItalico().length == 1 && texto.getItalico()[0];
		if (negritoLigado || italicoLigado) {
			configuraEstilo(negritoLigado, italicoLigado);
		}
	}

	private void configuraEstilo(boolean isBold, boolean isItalico) {
		if (isBold && isItalico) {
			setFonteNegritoItalico();
		} else if (isBold) {
			setFonteNegrito();
		} else if (isItalico) {
			setFonteItalico();
		}
	}

	private void resetaEstiloGeral(Texto texto) {
		boolean negritoLigado = texto.getNegrito().length == 1 && texto.getNegrito()[0];
		boolean italicoLigado = texto.getItalico().length == 1 && texto.getItalico()[0];
		resetaEstilo(negritoLigado, italicoLigado);
	}

	private void resetaEstilo(boolean isBold, boolean isItalico) {
		if (isBold || isItalico) {
			setFonteNormal();
		}
	}

	private void preparaTextoBufferParaCelula(StringBuilder textBuffer, ResultSet rs, String textoSplitted) throws SQLException {
		StringBuilder textoBufferLocal = textBuffer;
		textoBufferLocal.setLength(0);
		textoBufferLocal.append(textoSplitted);
		int bufferSize = textoBufferLocal.length();
		if (!isPropriedadeDinamica(textoBufferLocal, bufferSize)) {
			return;
		}
		CharSequence cs = textoBufferLocal.toString().substring(2, bufferSize - 1);
		textoBufferLocal.setLength(0);
		textoBufferLocal.append(cs);
		if (rs == null) {
			return;
		}
		textoBufferLocal.setLength(0);
		String result = rs.getString(cs.toString());
		if (result != null) {
			textoBufferLocal.append(result);
		}
	}

	private boolean isPropriedadeDinamica(StringBuilder textoBufferLocal, int bufferSize) {
		return bufferSize > 2 && textoBufferLocal.charAt(0) == '{' && textoBufferLocal.charAt(1) == '#' && textoBufferLocal.charAt(bufferSize - 1) == '}';
	}
	
	private String formataNumero(Texto texto, StringBuilder textoBufferLocal, byte index)  {
		String textoFinalItem = textoBufferLocal.toString();
		if (texto.isFormatoDecimal(index) && isValidDecimal(textoFinalItem)) {
			return StringUtil.getStringValueToInterface(ValueUtil.getDoubleValue(textoFinalItem));
		} else if (texto.isFormatoInteiro(index) && isValidDecimal(textoFinalItem)) {
			return StringUtil.getStringValueSimple(ValueUtil.getDoubleValue(textoFinalItem), 0, true);
		}
		return textoFinalItem;
	}

	private void configuraAlinhamentoPorCelula(char[] textoAlinhamento, int i) {
		if (i + 1 > textoAlinhamento.length) {
			return;
		}
		alinhamentoAtual = Texto.ESQUERDA;
		if (textoAlinhamento.length == 0) {
			return;
		}
		setAlinhamento(textoAlinhamento[i]);
	}
	private void configuraEstiloPorCelula(boolean[] negrito, boolean[] italico, int i) {
		boolean negritoLigado = i + 1 <= negrito.length && negrito.length > 1 && negrito[i];
		boolean italicoLigado = i + 1 <= italico.length && italico.length > 1 && italico[i];
		configuraEstilo(negritoLigado, italicoLigado);
	}
	private void resetaEstiloPorCelula(boolean[] negrito, boolean[] italico, int i) {
		boolean negritoLigado = i + 1 <= negrito.length && negrito.length > 1 && negrito[i];
		boolean italicoLigado = i + 1 <= italico.length && italico.length > 1 && italico[i];
		resetaEstilo(negritoLigado, italicoLigado);
	}

	private void configuraAlinhamentoGeral(Texto texto) {
		alinhamentoAtual = Texto.ESQUERDA;
		if (texto.getAlinhamento().length == 1) {
			setAlinhamento(texto.getAlinhamento()[0]);
		}
	}
	
	private void setAlinhamento(char textoAlinhamento) {
		if (Texto.DIREITA == textoAlinhamento) {
			alinhamentoAtual = textoAlinhamento;
		} else if (Texto.CENTRO == textoAlinhamento) {
			alinhamentoAtual = textoAlinhamento;
		}
	}

	private void escreveComTamanhoCelulasDiferentes(StringBuilder textoBufferLocal, Texto textoObj, String[] textoFormatado) {
		int larguraTt = largura - (margemX << 1);
		int recuo = larguraTt * textoObj.getRecuo() / 100; 
		larguraTt -= recuo;
		int larguraRestante = larguraTt;
		byte[] tamanhoCelulas = textoObj.getCelulaDim();
		int celulasLength = tamanhoCelulas.length;
		short[] posXs = new short[celulasLength];
		short[] larguraCelulas = new short[celulasLength];
		for (int i = 0; i < celulasLength; i++) {
			larguraCelulas[i] = (short) (larguraTt * tamanhoCelulas[i] / 100);
			short larguraCelula = larguraCelulas[i];
			if (i != celulasLength - 1) {
				posXs[i + 1] = (short) (larguraTt - larguraRestante + larguraCelula + margemX + recuo);
			}
			larguraRestante -= larguraCelula;
			if (i == 0) {
				posXs[0] = (short) (0 + margemX + recuo);
			}
		}
		novaLinha();
		int baseY = this.y;
		int textoLength = textoFormatado.length;
		for (int i = 0; i < textoLength; i++) {
			textoBufferLocal.setLength(0);
			textoBufferLocal.append(textoFormatado[i]);
			int proximaLinhaCasoNecessario = baseY;
			short posY = posXs[i];
			int limite = posY + larguraCelulas[i];
			List<String> textLn = quebraTextoEmLinhas(textoBufferLocal, posY, limite);
			configuraAlinhamentoPorCelula(textoObj.getAlinhamento(), i);
			int qtLinhas = textLn.size();
			for (int j = 0; j < qtLinhas; j++) {
				textoBufferLocal.setLength(0);
				textoBufferLocal.append(textLn.get(j));
				if (verificaNecessidadeNovaPagina()) {
					baseY = proximaLinhaCasoNecessario = this.y;
				}
				configuraEstiloPorCelula(textoObj.getNegrito(), textoObj.getItalico(), i);
				addTextPdf(posY, proximaLinhaCasoNecessario, nuFonte, textoBufferLocal, limite);
				if (proximaLinhaCasoNecessario > this.y) {
					novaLinha();
					pdfStream.incrementaPixelsPreenchidos(posicaoNovaLinha());
				}
				proximaLinhaCasoNecessario += posicaoNovaLinha();
				resetaEstiloPorCelula(textoObj.getNegrito(), textoObj.getItalico(), i);
			}
		}
	}

	private void escreveSimples(StringBuilder textoBufferLocal, Texto textoObj, String[] textoFormatado) {
		int larguraTt = largura - (margemX << 1);
		int recuo = larguraTt * textoObj.getRecuo() / 100; 
		int size = textoFormatado.length;
		int celula = (larguraTt - recuo) / size;
		int posX = margemX + recuo;
		int limite = posX + celula;
		novaLinha();
		int baseY = this.y;
		for (int i = 0; i < size; i++) {
			textoBufferLocal.setLength(0);
			textoBufferLocal.append(textoFormatado[i]);
			int proximaLinhaCasoNecessario = baseY;
			List<String> textoQuebradoList = quebraTextoEmLinhas(textoBufferLocal, posX, limite);
			configuraAlinhamentoPorCelula(textoObj.getAlinhamento(), i);
			int qtLinhas = textoQuebradoList.size();
			for (int j = 0; j < qtLinhas; j++) {
				textoBufferLocal.setLength(0);
				textoBufferLocal.append(textoQuebradoList.get(j));
				if (verificaNecessidadeNovaPagina()) {
					baseY = proximaLinhaCasoNecessario = this.y;
				}
				configuraEstiloPorCelula(textoObj.getNegrito(), textoObj.getItalico(), i);
				addTextPdf(posX, proximaLinhaCasoNecessario, nuFonte, textoBufferLocal, limite);
				if (proximaLinhaCasoNecessario > this.y) {
					novaLinha();
					pdfStream.incrementaPixelsPreenchidos(posicaoNovaLinha());
				}
				proximaLinhaCasoNecessario += posicaoNovaLinha();
				resetaEstiloPorCelula(textoObj.getNegrito(), textoObj.getItalico(), i);
			}
			posX += celula;
			limite += celula;
		}
	}

	private void addTextPdf(int x, int y, int nuFonte, StringBuilder text, int limite) {
		if (Texto.DIREITA == alinhamentoAtual) {
			x = posXParaAlinhamentoDireita(x, limite, text);
		} else if (Texto.CENTRO == alinhamentoAtual) {
			x = posXParaAlinhamentoCentro(x, limite, text);
		}
		int length = text.length();
		if (contemCharAcima128AscII(text, length)) {
			for (int i = 0; i < length; i++) {
				strToHexBuilder.append(toHexadecimal(text.charAt(i)));
			}
			pdfStream.addTextAsHex(x, y, nuFonte,  strToHexBuilder.toString());
			strToHexBuilder.setLength(0);
		} else {
			pdfStream.addText(x, y, nuFonte, text.toString());
		}
	}
	
	private char[] toHexadecimal(char val) {
		final int hexadecimalShift = 4;
		int qtHexChars = 2;
		char[] buf = new char[qtHexChars];
		formatInt(val, hexadecimalShift, buf, 0, qtHexChars);
		return buf;
	}

    private void formatInt(int val, int shift, char[] buf, int offset, int len) {
	    int charPos = len;
	    int radix = 1 << shift;
	    int mask = radix - 1;
	    do {
	        buf[offset + --charPos] = hexDigits[val & mask];
	        val >>>= shift;
	    } while (val != 0 && charPos > 0);
	}

	private boolean contemCharAcima128AscII(StringBuilder texto, int length) {
		boolean charComum = false;
		for (int i = 0; i < length; i++) {
			charComum = false;
			char charAt = texto.charAt(i);
			for (int j = 0; j < commonCharLength; j++) {
				if (CHARS_COMUNS_UTF8_CP1252_ESC_PDF.charAt(j) == charAt) {
					charComum = true;
					break;
				}
			}
			if (!charComum) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isValidDecimal(String s) {
		if (ValueUtil.isEmpty(s)) {
			return false;
		}
		char[] charArray = s.toCharArray();
		int length = charArray.length;
		for (int i = 0; i < length; i++) {
			char ch = charArray[i];
			if (ValueUtil.isValidNumberChar(ch) || ((ch == '.' || ch == ',') && i != length - 1 && i != 0)) {
				continue;
			}
			return false;
		}
		return true;
	}
	
	private List<String> quebraTextoEmLinhas(final StringBuilder textoASerCortado, int posX, int limite) {
		linhaTextoQuebradoList.clear();
		if (tamanhoTextoEmPixel(textoASerCortado) < limite - COURIER_CHAR_WIDTH_ROUND_TOP - posX && naoContemCharDiferenteQuebraLinha(textoASerCortado)) {
			linhaTextoQuebradoList.add(textoASerCortado.toString());
			return linhaTextoQuebradoList;
		}
		int currentLineInitialIndex = 0;
		int size = textoASerCortado.length();
		linhaDeTextoBuffer.setLength(0);
		for (int i = 0; i < size; i++) {
			boolean isCharDiferenteQuebraLinha = isCharDiferenteQuebraLinha(textoASerCortado, i);
			if (posX + tamanhoTextoEmPixel(linhaDeTextoBuffer) < limite - COURIER_CHAR_WIDTH_ROUND_TOP && isCharDiferenteQuebraLinha) {
				linhaDeTextoBuffer.append(textoASerCortado.charAt(i));
			} else {
				if (isCharDiferenteQuebraLinha) {
					i = regredirPalavraInteira(textoASerCortado, currentLineInitialIndex, i);
				}
				currentLineInitialIndex = i + 1;
				linhaTextoQuebradoList.add(linhaDeTextoBuffer.toString());
				linhaDeTextoBuffer.setLength(0);
				linhaDeTextoBuffer.append(isCharDiferenteQuebraLinha && textoASerCortado.charAt(i) != SPACE ? textoASerCortado.charAt(i) : "");
			}
			if (i == size - 1 && isCharDiferenteQuebraLinha) {
				linhaTextoQuebradoList.add(linhaDeTextoBuffer.toString());
			}
		}
		return linhaTextoQuebradoList;
	}
	
	private int regredirPalavraInteira(final StringBuilder textoASerCortado, int currentLineInitialIndex, int mainLoopIndex) {
		if (textoASerCortado.length() >= mainLoopIndex + 1 && textoASerCortado.charAt(mainLoopIndex) == SPACE) {
			return mainLoopIndex;
		}
		int newIndex = mainLoopIndex;
		int size = linhaDeTextoBuffer.length();
		for (int i = size - 1; i >= 0; i--) {
			if (isCharPermiteQuebraTexto(linhaDeTextoBuffer, i)) {
				break;
			}
			newIndex--;
		}
		if (newIndex > currentLineInitialIndex) {
			linhaDeTextoBuffer.setLength(newIndex - currentLineInitialIndex);
			return newIndex;
		} else {
			return mainLoopIndex;
		}
	}

	private boolean naoContemCharDiferenteQuebraLinha(StringBuilder texto) {
		int length = texto.length();
		for (int i = 0; i < length; i++) {
			if (!isCharDiferenteQuebraLinha(texto, i)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean isCharPermiteQuebraTexto(StringBuilder texto, int i) {
		char charAt = texto.charAt(i);
		return charAt == SPACE || charAt == '-';
	}

	private boolean isCharDiferenteQuebraLinha(StringBuilder texto, int i) {
		return texto.charAt(i) != '\n';
	}

	private int tamanhoTextoEmPixel(StringBuilder texto) {
		return (int) (texto.length() * COURIER_CHAR_WIDTH);
	}
	
	private int posXParaAlinhamentoDireita(int posX, int limite, StringBuilder text) {
		return limite - tamanhoTextoEmPixel(text);
	}
	
	private int posXParaAlinhamentoCentro(int x, int limite, StringBuilder text) {
		return x + ((limite - x) >> 1) - (tamanhoTextoEmPixel(text) >> 1);
	}
	
	private void espaco(int tamanho) {
		tamanho = tamanho == 0 ? posicaoNovaLinha() : tamanho;
		y += tamanho;
		pdfStream.incrementaPixelsPreenchidos(tamanho);
	}

	private void desenhaLinha() {
		int yAtual = y;
		novaLinha();
		if (verificaNecessidadeNovaPagina()) {
			yAtual = this.y;
		}
		pdfStream.incrementaPixelsPreenchidos(posicaoNovaLinha());
		int posCentralLinhaAprox = (int) ((yAtual - y) / COEFICIENTE_CENTRO_LINHA);
		int posY = y + posCentralLinhaAprox;
		pdfStream.addLine(x, posY, largura - margemX, posY);
	}
	
	private boolean verificaNecessidadeNovaPagina() {
		if (isPaginaContinua()) {
			return false;
		}
		if (y >= altura - margemY) {
			novaPagina();
			reloadFonteAtual();
			y = margemY;
			VmUtil.executeGarbageCollector();
			return true;
		}
		return false;
	}

	private void novaPagina() {
		pdfStream.newPage();
	}

	private void geraArquivoPdf(String path, String nmArquivo) throws IOException {
		VmUtil.executeGarbageCollector();
		path = Convert.normalizePath(path);
		pdfStream.writePdf(largura, altura, margemY);
		String pdfAsString = pdfStream.asString();
		FileUtil.createDirIfNecessary(path);
		filePath = path + nmArquivo + EXTENSAO_PDF;
		File file;
		try {
			file = FileUtil.openFile(filePath, File.CREATE_EMPTY);
		} catch (Throwable e) {
			ExceptionUtil.handle(e);
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_ARQUIVO_ABERTO);
		}
		try {
			file.writeBytes(pdfAsString.getBytes());
			arquivoGerado = true;
		} finally {
			FileUtil.closeFile(file);
		}
	}
	
	protected boolean isArquivoGerado() {
		return arquivoGerado;
	}

	protected String getFilePath() {
		return filePath;
	}
	
	protected String getMsgErro() {
		return StringUtil.getStringValue(msgErro);
	}

}
