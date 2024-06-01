package br.com.wmw.lavenderepda.report.pdf.stream;

import totalcross.util.pdf.PDFWriter;

public class TextStreamItem extends PdfStreamItem {
	
	private int x;
	private int y;
	private int nuFonte;
	private String text;
	private boolean hex;
	
	public TextStreamItem(int x, int y, int nuFonte, String text) {
		this.x = x;
		this.y = y;
		this.nuFonte = nuFonte;
		this.text = text;
	}

	public TextStreamItem(int x, int y, int nuFonte, String text, boolean hex) {
		this(x, y, nuFonte, text);
		this.hex = hex;
	}

	@Override
	protected void write(PDFWriter pdfWriter, int totalHeight) {
		if (hex) {
			pdfWriter.addTextAsHex(x, totalHeight - y, nuFonte, text);
		} else {
			pdfWriter.addText(x, totalHeight - y, nuFonte, text);
		}
	}

}
