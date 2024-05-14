package edu.najah.cap.data.export.PdfConvertor;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;



public abstract class PdfBase<T> implements Converter<T> {

    protected final int LINE_HEIGHT = 20;
    protected abstract void drawContent(PDPageContentStream contentStream, T item) throws IOException;

    public byte[] convert(List<T> items) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            int currentHeight = 700;
            final int lineHeight = 20;

            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, currentHeight);

            for (T item : items) {
                if (currentHeight <= lineHeight) {
                    contentStream.endText();
                    contentStream.close();

                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    contentStream.setFont(PDType1Font.HELVETICA, 12);
                    contentStream.beginText();
                    contentStream.newLineAtOffset(50, 700);
                    currentHeight = 700;
                }

                drawContent(contentStream, item);
                currentHeight -= lineHeight;
                contentStream.newLineAtOffset(0, -lineHeight);
            }

            contentStream.endText();
            contentStream.close();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}