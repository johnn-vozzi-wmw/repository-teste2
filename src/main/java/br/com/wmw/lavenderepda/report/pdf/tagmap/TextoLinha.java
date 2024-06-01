package br.com.wmw.lavenderepda.report.pdf.tagmap;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.StringUtil;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;

public class TextoLinha {
	
	private static final char SEPARADOR = '|';
	
	private String texto;
	private String formato;
	private String alinhamento;
	private String celulaDim;
	private String negrito;
	private String italico;
	private int recuo;
	

	public TextoLinha(int recuo) {
		if (this.recuo > 90) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_RECUO);
		}
		this.recuo = recuo;
	}

	public String getTexto() {
		return StringUtil.getStringValue(texto);
	}

	public void setTexto(String texto) {
		validaEntrada(texto);
		this.texto = this.texto == null ? StringUtil.getStringValue(texto) : this.texto + SEPARADOR + StringUtil.getStringValue(texto);
	}

	public String getFormato() {
		return StringUtil.getStringValue(formato);
	}

	public void setFormato(String formato) {
		validaEntrada(formato);
		this.formato = this.formato == null ? StringUtil.getStringValue(formato) : this.formato + SEPARADOR + StringUtil.getStringValue(formato);
	}

	public String getAlinhamento() {
		return StringUtil.getStringValue(alinhamento);
	}

	public void setAlinhamento(String alinhamento) {
		validaEntrada(alinhamento);
		this.alinhamento = this.alinhamento == null ? StringUtil.getStringValue(alinhamento) : this.alinhamento + SEPARADOR + StringUtil.getStringValue(alinhamento);
	}

	public String getCelulaDim() {
		return StringUtil.getStringValue(celulaDim);
	}

	public void setCelulaDim(String celulaDim) {
		validaEntrada(celulaDim);
		if (ValueUtil.isEmpty(celulaDim)) {
			return;
		}
		this.celulaDim = this.celulaDim == null ? StringUtil.getStringValue(celulaDim) : this.celulaDim + SEPARADOR + StringUtil.getStringValue(celulaDim);
	}

	public String getNegrito() {
		return StringUtil.getStringValue(negrito);
	}

	public void setNegrito(String negrito) {
		validaEntrada(negrito);
		this.negrito = this.negrito == null ? StringUtil.getStringValue(negrito) : this.negrito + SEPARADOR + StringUtil.getStringValue(negrito);
	}

	public String getItalico() {
		return StringUtil.getStringValue(italico);
	}

	public void setItalico(String italico) {
		validaEntrada(italico);
		this.italico = this.italico == null ? StringUtil.getStringValue(italico) : this.italico + SEPARADOR + StringUtil.getStringValue(italico);
	}

	public int getRecuo() {
		return recuo;
	}

	private void validaEntrada(String value) {
		if (ValueUtil.isNotEmpty(value) && value.contains("|")) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_SINTAXE_INVALIDA_TEXTO_LINHA);
		}
	}

}
