package edu.najah.cap.data.export.PdfConvertor;

import edu.najah.cap.posts.Post;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PostPdf extends PdfBase<Post> implements Converter<Post> {
    @Override
    protected void drawContent(PDPageContentStream contentStream, Post item) throws IOException {
        contentStream.showText("Post ID: " + item.getId());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Post Title: " + item.getTitle());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Post Body: " + item.getBody());
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Post Date: " + item.getDate());

    }
}