package br.com.wmw.lavenderepda.report.pdf.stream;

import totalcross.util.pdf.PDFWriter;

public abstract class PdfStreamItem {
	
	protected abstract void write(PDFWriter pdfWriter, int totalHeight);
	
}
