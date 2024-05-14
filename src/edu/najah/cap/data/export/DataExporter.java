package edu.najah.cap.data.export;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.data.Application;
import edu.najah.cap.data.export.PdfConvertor.PdfConverter;
import edu.najah.cap.data.proxy.PostServiceProxy;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.exceptions.UnExpectedException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;
import java.util.logging.Level;

public class DataExporter {

    private static final Logger logger = Logger.getLogger(DataExporter.class.getName());

    private final IPostService postServiceProxy;
    private final IUserService userServiceProxy;
    private final IUserActivityService activityProxy;
    private final IPayment paymentProxy;
    private final PdfConverter pdfConverter;
    private final List<byte[]> pdfs;

    public DataExporter(IUserService userService, IPostService postService, IPayment paymentService, IUserActivityService userActivityService) {
        this.userServiceProxy = userService;
        this.postServiceProxy = postService;
        this.paymentProxy = paymentService;
        this.activityProxy = userActivityService;
        this.pdfConverter = new PdfConverter();
        this.pdfs = new ArrayList<>();
    }


    public void exportUserDataForLoginUser() throws SystemBusyException, NotFoundException, BadRequestException {
        try {
            Scanner scanner = new Scanner(System.in);

            String userId = Application.getLoginUserName();
            UserProfile userProfile = userServiceProxy.getUser(userId); //

            if (userProfile != null) {
                logger.info("Starting data export for user: " + userId);

                displayServiceOptions(userProfile);

                List<Integer> choices = getUserChoices();
                processUserChoices(userProfile, choices);
            }else {
                logger.warning("User profile not found for user: " + userId);
            }

        }  catch (NotFoundException | SystemBusyException | BadRequestException | UnExpectedException e) {
            System.out.println("Error during data export for user: " + e.getMessage());
        }
    }


    private void processUserChoices(UserProfile userProfile, List<Integer> choices) throws SystemBusyException, BadRequestException, NotFoundException, UnExpectedException {


        for (Integer choice : choices) {
            switch (choice) {
                case 1:
                    byte[] userProfilePDF = pdfConverter.convertUserProfileToPDF(userProfile);
                    pdfs.add(userProfilePDF);
                    break;
                case 2:
                    List<Post> userPosts = postServiceProxy.getPosts(userProfile.getUserName());
                    byte[] userPostsPDF = pdfConverter.convertUserPostToPDF(userPosts);
                    pdfs.add(userPostsPDF);
                    break;
                case 3:
                    if (userProfile.getUserType() == UserType.PREMIUM_USER) {
                        List<Transaction> userTransactions = paymentProxy.getTransactions(userProfile.getUserName());
                        byte[] paymentInfoPDF = pdfConverter.convertUserTransactionToPDF(userTransactions);
                        pdfs.add(paymentInfoPDF);
                    } else {
                        throw new NotFoundException("Payment information is only available for premium users.");
                    }
                    break;
                case 4:

                    if (userProfile.getUserType() != UserType.NEW_USER) {
                        List<UserActivity> userActivities = activityProxy.getUserActivity(userProfile.getUserName());
                        byte[] userActivitiesPDF = pdfConverter.convertUserActivityToPDF(userActivities);
                        pdfs.add(userActivitiesPDF);
                    } else {
                        throw new NotFoundException("User activities are not available for new users.");
                    }
                    break;
                default:
                    logger.warning("Invalid choice made: " + choice);
                    throw new UnExpectedException("Invalid choice: " + choice);
            }
        }
    }
    private void displayServiceOptions(UserProfile userProfile) {
        System.out.println("Select the services to export data from:");
        System.out.println("1. IAM service ");
        System.out.println("2. Post service ");
        if (userProfile.getUserType() == UserType.PREMIUM_USER) {
            System.out.println("3. Payment component ");
        }
        if (userProfile.getUserType() != UserType.NEW_USER) {
            System.out.println("4. Activity component");
        }
        System.out.println("Enter service numbers separated by commas (e.g., 1,2,3):");
    }

    private List<Integer> getUserChoices() {
        Scanner scanner = new Scanner(System.in);
        String choicesInput = scanner.nextLine();
        String[] choicesArray = choicesInput.split(",");
        List<Integer> choices = new ArrayList<>();
        for (String choice : choicesArray) {
            int num = Integer.parseInt(choice.trim());
            if (!choices.contains(num)) {
                choices.add(Integer.parseInt(choice.trim()));
            } else {
                logger.log(Level.SEVERE, "Error processing choice: " + choice);
                throw new NumberFormatException("Duplicate choice: " + num);
            }
        }
        return choices;
    }

    public List<byte[]> getPdfs() {
        return pdfs;
    }


}