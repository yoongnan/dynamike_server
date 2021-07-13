package com.dynamike.pos.util;
import java.io.IOException;

import org.slf4j.LoggerFactory;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import ch.qos.logback.classic.Logger;
 
public class PDFBuilder extends PdfPageEventHelper {
 
	private final static Logger LOG = (Logger) LoggerFactory.getLogger(PDFBuilder.class);
	
	public void onEndPage(PdfWriter writer, Document document) {
 
		BaseFont bf = null;
		try {
			bf = BaseFont.createFont(
			        BaseFont.TIMES_ROMAN,
			        BaseFont.CP1252,
			        BaseFont.EMBEDDED);
		} catch (DocumentException e) {
			LOG.error("Create Base Front error:", e);
		} catch (IOException e) {
			LOG.error("Create Base Front error:", e);
		}
        Font font = new Font(bf, 8);
		
		 // 2. The front half of the written page X / co
		int pageS = writer.getPageNumber();
		String foot1 = pageS+"";
		Phrase footer = new Phrase(foot1,font);
 
		PdfContentByte cb = writer.getDirectContent();
		ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
				footer,
				(document.left() + document.right())/2 , document.bottom()-20, 0);
		
	}
}