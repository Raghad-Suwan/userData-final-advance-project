package edu.najah.cap.export.PdfConverter;

import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserService;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class ProfilePdf  {

    public byte[] export(UserProfile userProfile) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font font = PDType1Font.HELVETICA_BOLD;
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750); // Starting position

                contentStream.showText("User Profile Details:");
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User Name: " + userProfile.getFirstName() + " " + userProfile.getLastName());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User Type: " + userProfile.getUserType());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User Email: " + userProfile.getEmail());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User Phone: " + userProfile.getPhoneNumber());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("UserName: " + userProfile.getUserName());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User Password: " + userProfile.getPassword());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User Role: " + userProfile.getRole());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User Department: " + userProfile.getDepartment());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User organization: " + userProfile.getOrganization());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("User country: " + userProfile.getCountry());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("user street: " + userProfile.getStreet());
                contentStream.newLineAtOffset(0, -20);
                contentStream.showText("user city: " + userProfile.getCity());
                contentStream.newLineAtOffset(0, -20);



                contentStream.endText();
            }

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(); // Create a byte array output stream
            document.save(byteArrayOutputStream);
// Save the document to a byte array
            return byteArrayOutputStream.toByteArray(); // Return the byte array for the PDF file

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;}}

