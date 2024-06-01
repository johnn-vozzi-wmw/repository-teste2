package br.com.wmw.lavenderepda.report.pdf.stream;

import totalcross.util.pdf.PDFWriter;

public class NewPageStreamItem extends PdfStreamItem {

	@Override
	protected void write(PDFWriter pdfWriter, int totalHeight) {
		pdfWriter.newPage();
	}

}
