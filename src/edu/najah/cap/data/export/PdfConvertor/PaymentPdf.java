package edu.najah.cap.data.export.PdfConvertor;

import edu.najah.cap.payment.Transaction;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.IOException;

public class PaymentPdf extends PdfBase<Transaction> implements Converter<Transaction> {
    @Override
    protected void drawContent(PDPageContentStream contentStream, Transaction item) throws IOException {
        contentStream.showText("Transaction ID: " + item.getId());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Transaction Amount: " + item.getAmount());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Transaction Description: " + item.getDescription());
        contentStream.newLineAtOffset(0, -20);    }
}