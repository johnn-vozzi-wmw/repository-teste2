package br.com.wmw.lavenderepda.report.pdf.tagmap;

import java.util.ArrayList;
import java.util.List;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class Texto extends GenericTag {
	
	public static final char ESQUERDA = 'e';
	public static final char DIREITA = 'd';
	public static final char CENTRO = 'c';
	private static final String ESQUERDA_STR = "esquerda";
	private static final String DIREITA_STR = "direita";
	private static final String CENTRO_STR = "centro";

	private static final char FORMATO_DECIMAL = 'd';
	private static final char FORMATO_INTEIRO = 'i';
	private static final String FORMATO_DECIMAL_STR = "decimal";
	private static final String FORMATO_INTEIRO_STR = "inteiro";
	
	public static final String ATTR_RECUO = "recuo";
	public static final String ATTR_ALINHAMENTO = "alinhamento";
	public static final String ATTR_DIMENSAO_CELULA = "celulaDim";
	public static final String ATTR_ESTILO = "estilo";
	public static final String ATTR_FORMATO = "formato";
	public static final String ATTR_NEGRITO = "negrito";
	public static final String ATTR_ITALICO = "italico";
	

	private String[] texto;
	private char[] formato;
	private boolean[] negrito;
	private boolean[] italico;
	private byte[] celulaDim;
	private char[] alinhamento;
	private int recuo;
	private boolean contemFormato;
	private boolean formatoUnico;

	public void preparaTextosEProporcoes(String texto, String celulaDim) {
		this.texto = split(texto);
		if (this.texto.length == 0) {
			this.texto = new String[] {""};
		}
		this.celulaDim = preparaCelulaDim(celulaDim);
		validateItensCelulaDimETextos();
	}
	
	public void preparaEstilo(String negrito, String italico) {
		this.negrito = splitIntoBooleanArray(negrito);
		this.italico = splitIntoBooleanArray(italico);
	}

	public void preparaAlinhamento(String attributeValue) {
		this.alinhamento = splitIntoCharArray(attributeValue);
	}
	
	public void preparaFormato(String formato) {
		this.formato = splitIntoCharArray(formato);
		int tamanhoFormato = this.formato.length;
		if (tamanhoFormato > 0) {
			contemFormato = true;
			if (tamanhoFormato > 1 && tamanhoFormato != this.texto.length) {
				throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_TEXTO_FORMATACAO);
			}
			formatoUnico = tamanhoFormato == 1;
		}
	}
	
	private byte[] preparaCelulaDim(String attribute) {
		if (ValueUtil.isEmpty(attribute)) {
			return new byte[0];
		}
		String[] attributes = split(attribute);
		int attrLength = attributes.length;
		byte[] result = new byte[attrLength];
		int value = 0;
		for (int i = 0; i < attrLength; i++) {
			String attr = attributes[i];
			validaCharCelulaDim(attr);
			value = ValueUtil.getIntegerValue(attr);
			if (value > 100 || value < 0) {
				throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_TAMANHO_CELULA);
			}
			result[i] = (byte) value;
		}
		return result;
	}
	
	private void validaCharCelulaDim(final String attr) {
		if (ValueUtil.isEmpty(attr)) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_TAMANHO_CELULA);
		}
		char[] charArray = attr.toCharArray();
		for (char c : charArray) {
			if (!ValueUtil.isValidNumberChar(c)) {
				throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_TAMANHO_CELULA);
			}
		}
	}

	private void validateItensCelulaDimETextos() {
		if (this.celulaDim.length > 0 && this.celulaDim.length != this.texto.length) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_TEXTO_CELULA);
		}
	}
	
	public boolean isFormatoDecimal(int index) {
		if (!contemFormato) {
			return false;
		}
		if (formatoUnico) {
			return FORMATO_DECIMAL == this.formato[0];
		} else {
			return FORMATO_DECIMAL == this.formato[index];
		}
	}
	
	public boolean isFormatoInteiro(int index) {
		if (!contemFormato) {
			return false;
		}
		if (formatoUnico) {
			return FORMATO_INTEIRO == this.formato[0];
		} else {
			return FORMATO_INTEIRO == this.formato[index];
		}
	}
	
	public char[] getAlinhamento() {
		return this.alinhamento;
	}
	
	public String[] getTexto() {
		return texto;
	}
	
	public byte[] getCelulaDim() {
		return celulaDim;
	}

	public void setRecuo(int recuo) {
		if (recuo > 90) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_RECUO);
		}
		this.recuo = recuo;
	}

	public int getRecuo() {
		return recuo;
	}

	public boolean[] getNegrito() {
		return negrito;
	}

	public void setNegrito(boolean[] negrito) {
		this.negrito = negrito;
	}

	public boolean[] getItalico() {
		return italico;
	}

	public void setItalico(boolean[] italico) {
		this.italico = italico;
	}

	private String[] split(String attribute) {
		if (attribute == null) {
			return new String[0];
		}
		if ("".equals(attribute)) {
			return new String[] {attribute};
		}
		List<String> splitted = new ArrayList<String>();
		StringBuilder strAtual = new StringBuilder(32);
		int length = attribute.length();
		for (int i = 0; i < length; i++) {
			if (attribute.charAt(i) == '|') {
				splitted.add(strAtual.toString());
				strAtual.setLength(0);
			} else {
				strAtual.append(attribute.charAt(i));
			}
			if (i == length - 1) {
				splitted.add(strAtual.toString());
			}
		}
		return splitted.toArray(new String[splitted.size()]);
	}
	
	private char[] splitIntoCharArray(String attribute) {
		if (ValueUtil.isEmpty(attribute)) {
			return new char[0];
		}
		String[] attrsSplitted = split(attribute);
		int length = attrsSplitted.length;
		char [] charAttr = new char[length];
		for (int i = 0; i < length; i++) {
			String attr = attrsSplitted[i].toLowerCase();
			if (ValueUtil.isNotEmpty(attr) && verificaValoresAtributosFormatoAlinhamentoLigados(attr)) {
				verificaValoresAtributosFormatoAlinhamentoLigados(attr);
				charAttr[i] = attr.charAt(0);
			} else {
				charAttr[i] =  ' ';
			}
		}
		return charAttr;
	}

	private boolean verificaValoresAtributosFormatoAlinhamentoLigados(String attrLowerCase) {
		boolean valoresValidos = false;
		if (attrLowerCase.length() == 1) {
			char attrChar = attrLowerCase.charAt(0);
			valoresValidos = DIREITA == attrChar || CENTRO == attrChar || FORMATO_DECIMAL == attrChar || FORMATO_INTEIRO == attrChar || ESQUERDA == attrChar;
		} else {
			valoresValidos = DIREITA_STR.equals(attrLowerCase) || CENTRO_STR.equals(attrLowerCase) || FORMATO_DECIMAL_STR.equals(attrLowerCase) || FORMATO_INTEIRO_STR.equals(attrLowerCase) || ESQUERDA_STR.equals(attrLowerCase);
		}
		return valoresValidos;
	}

	private boolean[] splitIntoBooleanArray(String attribute) {
		if (ValueUtil.isEmpty(attribute)) {
			return new boolean[0];
		}
		String[] attrsSplitted = split(attribute);
		int length = attrsSplitted.length;
		boolean [] booleanAttr = new boolean[length];
		for (int i = 0; i < length; i++) {
			String attr = attrsSplitted[i];
			booleanAttr[i] = ValueUtil.isNotEmpty(attr) && (attr.length() == 1 && (attr.charAt(0) == 't' || attr.charAt(0) == 'T') || "true".equalsIgnoreCase(attr));
		}
		return booleanAttr;
	}
	
}
