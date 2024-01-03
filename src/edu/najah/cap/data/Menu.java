package edu.najah.cap.data;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.data.deletionHandler.TransactionDeletionHandler;
import edu.najah.cap.data.download.Downloader;
import edu.najah.cap.data.export.DataExporter;
import edu.najah.cap.data.hardDelete.HardDeleteService;
import edu.najah.cap.data.softDelete.SoftDeletion;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.posts.IPostService;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Menu {
    private static final Logger logger = LoggerFactory.getLogger(TransactionDeletionHandler.class);

    private final IPostService postService ;
    private final IUserService userService ;
    private final IUserActivityService userActivityService;
    private final IPayment paymentService ;

    public Menu(IPostService postService, IUserService userService, IUserActivityService activity, IPayment payment) {
        this.postService = postService;
        this.userService = userService;
        this.userActivityService = activity;
        this.paymentService = payment;
    }



    public static void runner(IPostService postService, IUserService userService, IUserActivityService activity, IPayment payment){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choose an option:");
        System.out.println("1. Express");
        System.out.println("2. Delete");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1:
                expressOperation(userService, postService, activity, payment);
                break;
            case 2:
                handleDeleteChoice(userService, postService, activity, payment);
                break;
            default:
                System.out.println("Invalid choice. Please choose 1 or 2.");
        }

        scanner.close();
    }

    private static void handleDeleteChoice(IUserService userService, IPostService postService, IUserActivityService activity, IPayment payment)
    { Scanner scanner = new Scanner(System.in);
        System.out.println("Choose delete type:");
        System.out.println("1. Hard Delete");
        System.out.println("2. Soft Delete");

        int deleteChoice = scanner.nextInt();

        switch (deleteChoice) {
            case 1:
                hardDeleteOperation( postService,userService, activity, payment);
                break;
            case 2:
                softDeleteOperation(userService,activity ,postService, payment);
                break;
            default:
                System.out.println("Invalid choice. Please choose 1 or 2.");
        }

        scanner.close();
    }

    private static void expressOperation(IUserService userService, IPostService postService, IUserActivityService activity, IPayment payment) {

        System.out.println("Performing express operation...");

        DataExporter userDataExporter = new DataExporter(userService, postService, payment, activity);
        try {
            userDataExporter.exportUserDataForLoginUser();//first try code
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<byte[]> pdfs = userDataExporter.getPdfs();
        Downloader downloader = new Downloader();
        downloader.initiateDownload(pdfs);
    }



    private static void hardDeleteOperation(IPostService postService, IUserService userService, IUserActivityService activity, IPayment payment) {
        System.out.println("Performing hard delete operation...");

        HardDeleteService hardDelete = new HardDeleteService(userService, postService, payment, activity);
        try {
            hardDelete.deleteAccount(Application.getLoginUserName());

        }catch (SystemBusyException | BadRequestException | NotFoundException e){
            System.err.println(e.getMessage());


        }

    }

    private static void softDeleteOperation(IUserService userService, IUserActivityService userActivityService, IPostService postService, IPayment paymentService) {
        System.out.println("Performing soft delete operation...");

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the username of the account to soft delete:");
        String userName = scanner.nextLine();

        try {
            SoftDeletion.softDeleteUser(userService, userActivityService, postService, paymentService, userName);
            System.out.println("Soft deletion process completed for user: " + userName);
        } catch (SystemBusyException | BadRequestException | NotFoundException e) {
            System.err.println("Error during soft deletion: " + e.getMessage());
        }
    }




}







