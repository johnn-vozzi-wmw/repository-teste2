package br.com.wmw.lavenderepda.report.pdf.stream;

import totalcross.util.pdf.PDFWriter;

public class LineStreamItem extends PdfStreamItem {
	
	private int x;
	private int y;
	private int toX;
	private int toY;
	
	public LineStreamItem(int x, int y, int toX, int toY) {
		this.x = x;
		this.y = y;
		this.toX = toX;
		this.toY = toY;
	}

	@Override
	protected void write(PDFWriter pdfWriter, int totalHeight) {
		pdfWriter.addLine(x, totalHeight - y, toX, totalHeight - toY);
	}

}
