package br.com.wmw.lavenderepda.report.pdf;

import totalcross.xml.XmlReader;

public class LayoutReader extends XmlReader {
	
	
	private PdfReportMapper contentHandler;
	
	@Override
	protected int getTagCode(byte[] b, int offset, int count) {
		int key = super.getTagCode(b, offset, count);
		if (contentHandler == null) {
			 contentHandler = (PdfReportMapper) getContentHandler();
		}
		contentHandler.tags.put(key, new String(b, offset, count));
		return key;
	}

}
