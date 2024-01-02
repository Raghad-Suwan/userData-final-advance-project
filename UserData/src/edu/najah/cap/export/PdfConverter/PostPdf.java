package edu.najah.cap.export.PdfConverter;

import edu.najah.cap.posts.Post;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PostPdf implements Converter<Post> {
    @Override
    public byte[] export(List<Post> userPosts) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font font = PDType1Font.HELVETICA;
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.setFont(font, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, 750); // Starting position
                contentStream.showText("User Posts:");
                contentStream.newLineAtOffset(0, -20); // Move to the next line

                for (Post post : userPosts) {
                    contentStream.showText("Post ID: " + post.getId());
                    contentStream.newLineAtOffset(0, -20); // Move to the next line
                    contentStream.showText("Title: " + post.getTitle());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Body: " + post.getBody());
                    contentStream.newLineAtOffset(0, -20);
                    contentStream.showText("Date: " + post.getDate());
                    contentStream.newLineAtOffset(0, -20);

                }

                contentStream.endText();
            }
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            document.save(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null; }}
