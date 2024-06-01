package br.com.wmw.lavenderepda.report.pdf.stream;

import java.util.ArrayList;
import java.util.List;

import totalcross.util.pdf.PDFWriter;

public class PdfStream {
	
	private List<PdfStreamItem> stream = new ArrayList<PdfStreamItem>();
	private PDFWriter pdfWriter;
	private int pixelsPreenchidos;
	private boolean primeiraPagina = true;
	
	public void addText(int x, int y, int nuFonte, String text) {
		stream.add(new TextStreamItem(x, y, nuFonte, text));
	}

	public void addTextAsHex(int x, int y, int nuFonte, String text) {
		stream.add(new TextStreamItem(x, y, nuFonte, text, true));
	}
	
	public void incrementaPixelsPreenchidos(int pixels) {
		if (primeiraPagina) {
			pixelsPreenchidos += pixels;
		}
	}
	
	public void addLine(int x, int y, int toX, int toY) {
		stream.add(new LineStreamItem(x, y, toX, toY));
	}
	
	public void newPage() {
		stream.add(new NewPageStreamItem());
		primeiraPagina = false;
	}
	
	public void setFont(String font) {
		stream.add(new FontStreamItem(font));
	}

	public void writePdf(int totalPageWidth, int altura, int margemY) {
		int totalPageHeight = altura == 0 ? pixelsPreenchidos + (margemY * 2) : altura;
		pdfWriter = new PDFWriter(totalPageWidth, totalPageHeight);
		if (totalPageHeight == 0) {
			return;
		}
		for (PdfStreamItem item : stream) {
			item.write(pdfWriter, totalPageHeight);
		}
	}

	public String asString() {
		return pdfWriter == null ? null : pdfWriter.asString();
	}

}
