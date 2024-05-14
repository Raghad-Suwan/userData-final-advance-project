package edu.najah.cap.data.export.PdfConvertor;

import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class PdfConverter {

    private static final Logger logger = LoggerFactory.getLogger(PdfConverter.class);

    public byte[] convertUserProfileToPDF(UserProfile userProfile) {
        logger.info("Converting UserProfile to PDF: Username = {}", userProfile.getUserName());
        return new byte[0];
    }

    public byte[] convertUserActivityToPDF(List<UserActivity> userActivities) {
        logger.info("Converting UserActivity list to PDF: Number of Activities = {}", userActivities.size());
        return new byte[0];
    }

    public byte[] convertUserTransactionToPDF(List<Transaction> userTransactions) {
        logger.info("Converting UserTransaction list to PDF: Number of Transactions = {}", userTransactions.size());
        return new byte[0];
    }

    public byte[] convertUserPostToPDF(List<Post> userPosts) {
        logger.info("Converting UserPost list to PDF: Number of Posts = {}", userPosts.size());
        return new byte[0];
    }
}





