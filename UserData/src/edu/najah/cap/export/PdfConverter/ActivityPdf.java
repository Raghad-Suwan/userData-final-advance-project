package edu.najah.cap.export.PdfConverter;

import edu.najah.cap.activity.UserActivity;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ActivityPdf implements Converter<UserActivity> {

    @Override
    public byte[] export(List<UserActivity> userActivities) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font font = PDType1Font.HELVETICA;
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("User Activities:");
                contentStream.newLineAtOffset(0, -20); // Move to the next line

                for (UserActivity activity : userActivities) {
                    contentStream.showText("Activity ID: " + activity.getId());
                    contentStream.newLineAtOffset(0, -20); // Move to the next line
                    contentStream.showText("Type: " + activity.getActivityType());
                    contentStream.newLineAtOffset(0, -20); // Move to the next line
                    contentStream.showText("Date: " + activity.getActivityDate());
                    contentStream.newLineAtOffset(0, -20); // Move to the next line

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
