package edu.najah.cap.data.export.PdfConvertor;

import edu.najah.cap.activity.UserActivity;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import java.io.IOException;


public class ActivityPdf extends PdfBase<UserActivity> implements Converter<UserActivity> {

    @Override
    protected void drawContent(PDPageContentStream contentStream, UserActivity item) throws IOException {
        contentStream.showText("User Activities");
        contentStream.showText("Activity ID: " + item.getId());
        contentStream.newLineAtOffset(0, -LINE_HEIGHT);
        contentStream.showText("Activity Type: " + item.getActivityType());
        contentStream.newLineAtOffset(0, -LINE_HEIGHT);
        contentStream.showText("Activity Date: " + item.getActivityDate());
        contentStream.newLineAtOffset(0, -LINE_HEIGHT);


    }
}