package br.com.wmw.lavenderepda.report.pdf;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

import br.com.wmw.framework.exception.ValidationException;
import br.com.wmw.framework.util.ValueUtil;
import br.com.wmw.lavenderepda.Messages;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Elementos;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Espaco;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Layout;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Linha;
import br.com.wmw.lavenderepda.report.pdf.tagmap.NovaPagina;
import br.com.wmw.lavenderepda.report.pdf.tagmap.Texto;
import br.com.wmw.lavenderepda.report.pdf.tagmap.TextoLinha;
import totalcross.xml.AttributeList;
import totalcross.xml.ContentHandler;

public class PdfReportMapper extends ContentHandler {
	
	private static final String TAG_RELATORIOPDF = "relatoriopdf";
	private static final String ATTR_RELATORIOPDF_ENTIDADE = "entidade";
	private static final String TAG_LAYOUT = "layout";
	private static final String TAG_ELEMENTOS = "elementos";
	private static final String TAG_NOVAPAGINA = "novapagina";
	private static final String TAG_ESPACO = "espaco";
	private static final String TAG_LINHA = "linha";
	private static final String TAG_TEXTO = "texto";
	private static final String TAG_TEXTOLINHA = "textolinha";
	
	protected HashMap<Integer, String> tags = new HashMap<Integer, String>();
	private Stack<Layout> layoutsPrincipais = new Stack<Layout>();
	private Stack<Layout> controleLayout = new Stack<Layout>();
	private Stack<Boolean> onElementStack = new Stack<Boolean>();
	private String entidadeRelatorio;
	private boolean relatorioPdfTag;
	private boolean modoTextoLinha;
	private TextoLinha textoLinha;
	
	protected List<Layout> getLayoutList() {
		return layoutsPrincipais;
	}
	
	protected void setEntidadeRelacionada(String entidade) {
		entidadeRelatorio = entidade;
	}

	@Override
	public void startElement(int tag, AttributeList atts) {
		final String nmTag = tags.get(tag).toLowerCase();
		if (!relatorioPdfTag && !isModeloXmlRelacionadaEntidade(atts, nmTag)) {
			return;
		}
		relatorioPdfTag = true;
		mapear(nmTag, atts);
	}

	private boolean isModeloXmlRelacionadaEntidade(AttributeList atts, final String nmTag) {
		return TAG_RELATORIOPDF.equalsIgnoreCase(nmTag) && ValueUtil.isNotEmpty(entidadeRelatorio) && entidadeRelatorio.equalsIgnoreCase(atts.getAttributeValue(ATTR_RELATORIOPDF_ENTIDADE));
	}

	@Override
	public void endElement(int tag) {
		final String tagAtual = tags.get(tag).toLowerCase();
		if (!relatorioPdfTag) {
			return;
		}
		switch (tagAtual) {
		case TAG_LAYOUT:
			controleLayout.pop();
			onElementStack.pop();
			break;
		case TAG_ELEMENTOS:
			onElementStack.pop();
			break;
		case TAG_RELATORIOPDF:
			relatorioPdfTag = false;
			break;
		case TAG_TEXTOLINHA:
			finalTextoLinha();
			break;
		}
	}

	@Override
	public void characters(String s) {

	}
	
	private void mapear(final String tagAtual, AttributeList atts) {
		switch (tagAtual) {
		case TAG_RELATORIOPDF:
			relatorioPdfTag = true;
			break;
		case TAG_NOVAPAGINA:
			mapeiaPagina(atts);
			break;
		case TAG_LAYOUT:
			mapeiaLayout(atts);
			break;
		case TAG_LINHA:
			mapeiaLinha(atts);
			break;
		case TAG_ELEMENTOS:
			mapeiaElemento(atts);
			break;
		case TAG_ESPACO:
			mapeiaEspaco(atts);
			break;
		case TAG_TEXTO:
			mapeiaTexto(atts);
			break;
		case TAG_TEXTOLINHA:
			mapeiaTextoLinha(atts);
			break;
		}
	}

	private void mapeiaPagina(AttributeList atts) {
		validaContemLayoutPrincipal();
		if (isOnElement()) {
			controleLayout.peek().getElementos().genericTagList.add(new NovaPagina());
		} else {
			controleLayout.peek().genericTagList.add(new NovaPagina());
		}
	}
	
	private void mapeiaLayout(AttributeList atts) {
		Layout layout = new Layout();
		layout.id = atts.getAttributeValue(Layout.ATTR_ID);
		layout.largura = ValueUtil.getIntegerValue(atts.getAttributeValue(Layout.ATTR_LARGURA));
		layout.altura = ValueUtil.getIntegerValue(atts.getAttributeValue(Layout.ATTR_ALTURA));
		layout.margemX = ValueUtil.getIntegerValue(atts.getAttributeValue(Layout.ATTR_MARGEMX));
		layout.margemY = ValueUtil.getIntegerValue(atts.getAttributeValue(Layout.ATTR_MARGEMY));
		if (controleLayout.size() == 0) {
			layoutsPrincipais.add(layout);
		} else if (isOnElement()) {
			controleLayout.peek().getElementos().genericTagList.add(layout);
		} else {
			controleLayout.peek().genericTagList.add(layout);
		}
		onElementStack.push(false);
		controleLayout.push(layout);
	}

	private void mapeiaLinha(AttributeList atts) {
		validaContemLayoutPrincipal();
		if (isOnElement()) {
			controleLayout.peek().getElementos().genericTagList.add(new Linha());
		} else {
			controleLayout.peek().genericTagList.add(new Linha());
		}
	}

	private void mapeiaElemento(AttributeList atts) {
		validaContemLayoutPrincipal();
		if (isOnElement()) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_SUBELEMENTO);
		}
		Layout layout = controleLayout.peek();
		if (existeTagElementosInserido(layout)) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_LAYOUT_INVALIDO_MULTIPLOS_ELEMENTO);
		}
		Elementos elementos = new Elementos();
		elementos.preparaSql(atts.getAttributeValue(Elementos.ATTR_SQL));
		layout.genericTagList.add(elementos);
		onElementStack.push(true);
	}
	
	private boolean existeTagElementosInserido(Layout layout){
		return layout.findElementos() != null;
	}

	private void mapeiaEspaco(AttributeList atts) {
		validaContemLayoutPrincipal();
		Espaco espaco = new Espaco();
		espaco.tamanho = ValueUtil.getIntegerValue(atts.getAttributeValue(Espaco.ATTR_TAMANHO));
		if (isOnElement()) {
			controleLayout.peek().getElementos().genericTagList.add(espaco);
		} else {
			controleLayout.peek().genericTagList.add(espaco);
		}
	}

	private void mapeiaTextoLinha(AttributeList atts) {
		validaContemLayoutPrincipal();
		textoLinha = new TextoLinha(ValueUtil.getIntegerValue(atts.getAttributeValue(Texto.ATTR_RECUO)));
		modoTextoLinha = true;
	}

	private void finalTextoLinha() {
		modoTextoLinha = false;
		mapeiaTexto(textoLinha.getRecuo(), textoLinha.getTexto(), textoLinha.getCelulaDim(), textoLinha.getAlinhamento(), textoLinha.getFormato(), textoLinha.getNegrito(), textoLinha.getItalico());
		textoLinha = null;
	}

	private void mapeiaTexto(AttributeList atts) {
		if (modoTextoLinha) {
			mapeiaTextoPorTextoLinha(atts);
			return;
		}
		validaContemLayoutPrincipal();
		mapeiaTexto(ValueUtil.getIntegerValue(atts.getAttributeValue(Texto.ATTR_RECUO))
				, atts.getAttributeValue(TAG_TEXTO)
				, atts.getAttributeValue(Texto.ATTR_DIMENSAO_CELULA)
				, atts.getAttributeValue(Texto.ATTR_ALINHAMENTO)
				, atts.getAttributeValue(Texto.ATTR_FORMATO)
				, atts.getAttributeValue(Texto.ATTR_NEGRITO)
				, atts.getAttributeValue(Texto.ATTR_ITALICO));
		
	}

	private void mapeiaTexto(int recuo, String text, String celulaDim, String alinhamento, String formato, String negrito, String italico) {
		Layout layout = controleLayout.peek();
		Texto texto = new Texto();
		texto.setRecuo(recuo);
		texto.preparaTextosEProporcoes(text, celulaDim);
		texto.preparaAlinhamento(alinhamento);
		texto.preparaFormato(formato);
		texto.preparaEstilo(negrito, italico);
		if (isOnElement()) {
			layout.getElementos().genericTagList.add(texto);
		} else {
			layout.genericTagList.add(texto);
		}
	}

	private void mapeiaTextoPorTextoLinha(AttributeList atts) {
		textoLinha.setTexto(atts.getAttributeValue(TAG_TEXTO));
		textoLinha.setCelulaDim(atts.getAttributeValue(Texto.ATTR_DIMENSAO_CELULA));
		textoLinha.setAlinhamento(atts.getAttributeValue(Texto.ATTR_ALINHAMENTO));
		textoLinha.setFormato(atts.getAttributeValue(Texto.ATTR_FORMATO));
		textoLinha.setNegrito(atts.getAttributeValue(Texto.ATTR_NEGRITO));
		textoLinha.setItalico(atts.getAttributeValue(Texto.ATTR_ITALICO));
	}

	private boolean isOnElement() {
		return onElementStack.size() > 0 && onElementStack.peek();
	}
	
	private void validaContemLayoutPrincipal() {
		if (layoutsPrincipais.size() == 0) {
			throw new ValidationException(Messages.RELATORIO_PDF_OFFLINE_ERRO_SEM_LAYOUT_PRINCIPAL);
		}
	}

}
