package com.dynamike.pos.util;

import ch.qos.logback.classic.Logger;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;

public class PDFUtil {

    private final static Logger LOG = (Logger) LoggerFactory.getLogger(PDFUtil.class);

    private PDFUtil(){}

    public static String getContent(Object data, String templatePath, String fileName) {
        try {
            Configuration config = new Configuration(Configuration.VERSION_2_3_25);
            config.setDefaultEncoding("UTF-8");
            config.setDirectoryForTemplateLoading(new File(templatePath));
            config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
            config.setLogTemplateExceptions(false);
            Template template = config.getTemplate(fileName);
            StringWriter writer = new StringWriter();
            template.process(data, writer);
            writer.flush();
            return writer.toString();
        } catch (Exception e) {
            LOG.error("Loading pdf template error:", e);
            return null;
        }
    }

    public static void convertToPdf(PdfWriter writer, Document document, String htmlString){
        try {
        	document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(htmlString.getBytes()), null,
                    Charset.forName("UTF-8"));
        } catch (IOException e) {
            LOG.error("Generate PDF file error", e);
        }finally {
            document.close();
        }
    }
}
