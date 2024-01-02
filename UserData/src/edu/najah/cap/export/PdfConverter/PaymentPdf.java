package edu.najah.cap.export.PdfConverter;

import edu.najah.cap.payment.Transaction;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PaymentPdf implements Converter<Transaction> {
    @Override
    public byte[] export(List<Transaction> userTransactions) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font font = PDType1Font.HELVETICA;
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Payment Information:");
                contentStream.newLineAtOffset(0, -20); // Move to the next line

                for (Transaction transaction : userTransactions) {
                    contentStream.showText("Transaction ID: " + transaction.getId());
                    contentStream.newLineAtOffset(0, -20); // Move to the next line
                    contentStream.showText("Amount: " + transaction.getAmount());
                    contentStream.newLineAtOffset(0, -20); // Move to the next line
                    contentStream.showText("Description: " + transaction.getDescription());
                    contentStream.newLineAtOffset(0, -20); // Move to the next line
// get the balance from the payment service by the transaction id




                    contentStream.newLine();
                }

                contentStream.endText();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
