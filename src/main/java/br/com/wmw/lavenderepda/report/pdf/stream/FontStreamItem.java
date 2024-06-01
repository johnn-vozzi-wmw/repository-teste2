package br.com.wmw.lavenderepda.report.pdf.stream;

import totalcross.util.pdf.PDFWriter;
import totalcross.util.pdf.StandardFonts;

public class FontStreamItem extends PdfStreamItem {
	
	private String font;
	
	public FontStreamItem(String font) {
		this.font = font;
	}

	@Override
	protected void write(PDFWriter pdfWriter, int totalHeight) {
		pdfWriter.setFont(StandardFonts.SUBTYPE, font, StandardFonts.WIN_ANSI_ENCODING);
	}

}
