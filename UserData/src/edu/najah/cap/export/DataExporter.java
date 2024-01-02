package edu.najah.cap.export;

import edu.najah.cap.activity.ActivityProxy;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.export.PdfConverter.PdfConverter;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserServiceProxy;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.PaymentProxy;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.posts.PostServiceProxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import static edu.najah.cap.data.Application.getLoginUserName;

public class DataExporter {

    private final IPostService PostServiceProxy ;
    private final IUserService userServiceProxy ;

    private final IUserActivityService activityProxy;
    private final IPayment paymentProxy ;
    private final PdfConverter pdfConverter;
    private final List<byte[]> pdfs;

    public DataExporter(IUserService userService, IPostService postService, IPayment paymentService, IUserActivityService userActivityService) {
       this.userServiceProxy = new UserServiceProxy(userService);
        this.paymentProxy = new PaymentProxy(paymentService);
        this.activityProxy = new ActivityProxy(userActivityService);
        this.pdfConverter = new PdfConverter(); // Instantiate PdfExporter
        this.pdfs = new ArrayList<>();
       this.PostServiceProxy = new PostServiceProxy(postService);
    }


    public void exportUserDataForLoginUser() throws SystemBusyException, NotFoundException, BadRequestException {
        try {
            Scanner scanner = new Scanner(System.in);

            String userId = getLoginUserName();
            UserProfile userProfile = userServiceProxy.getUser(userId); //

            if (userProfile != null) {
                System.out.println("Welcome, " + userProfile.getFirstName() + " " + userProfile.getLastName() + "!");
                System.out.println("Select the services to export data from:");
                System.out.println("1. IAM service (User profile details)");
                System.out.println("2. Post service (User posts)");
                if (userProfile.getUserType() == UserType.PREMIUM_USER) {
                    System.out.println("3. Payment component (Payment information - only for premium users)");
                }

                if (userProfile.getUserType() != UserType.NEW_USER) {
                    System.out.println("4. Activity component (User activities - not applicable for new users)");
                }
                System.out.println("Enter service numbers separated by commas (e.g., 1,2,3):");

                String choicesInput = scanner.nextLine();
                String[] choicesArray = choicesInput.split(",");

                List<Integer> choices = new ArrayList<>();
                for (String choice : choicesArray)   try {
                    choices.add(Integer.parseInt(choice.trim()));
                } catch (NumberFormatException ignored) {
                    // Ignore invalid choices
                }
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String zipFileName = "UserData_" + timeStamp + ".zip";


                String userHome = System.getProperty("user.home");

                String downloadPath = userHome + "/Downloads/" + zipFileName;
                processUserChoices(userProfile, choices); // Process user choices and export data

                PdfZipper.createZipFile(pdfs, downloadPath); // Create ZIP file with the unique file name and path
                System.out.println("Zipped file saved successfully at: " + downloadPath);


            }
         }  catch (NotFoundException | SystemBusyException | BadRequestException e) {
            // Log or handle the exception appropriately, add debugging info
            e.printStackTrace(); // Replace with your logging mechanism
            // Throw a more descriptive exception or handle according to your application logic
        }
    }


    private void processUserChoices(UserProfile userProfile, List<Integer> choices) throws SystemBusyException, BadRequestException, NotFoundException {


        for (Integer choice : choices) {
            switch (choice) {
                case 1:
                    byte[] userProfilePDF = pdfConverter.convertUserProfileToPDF(userProfile);
                    pdfs.add(userProfilePDF);
                    break;
                case 2:
                    List<Post> userPosts = PostServiceProxy.getPosts(userProfile.getUserName());
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
                    throw new BadRequestException("Invalid choice: " + choice);
            }
        }
    }


}

