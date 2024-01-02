package edu.najah.cap.export.PdfConverter;


import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.Post;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class PdfConverter {

  private Converter ActivityPdf = new ActivityPdf();
  private final Converter PaymentPdf = new PaymentPdf();
  private Converter PostPdf = new PostPdf();
  private ProfilePdf ProfilePdf = new ProfilePdf();



  public byte[] convertUserProfileToPDF (UserProfile userProfile) {
      return ProfilePdf.export(userProfile);
  }

  public byte[] convertUserActivityToPDF (List<UserActivity> userActivities) {
      return ActivityPdf.export(userActivities);
  }

  public byte[] convertUserTransactionToPDF (List<Transaction> userTransactions) {
      return PaymentPdf.export(userTransactions);
  }

  public byte[] convertUserPostToPDF (List<Post> userPosts) {
      return PostPdf.export(userPosts);
  }


}












