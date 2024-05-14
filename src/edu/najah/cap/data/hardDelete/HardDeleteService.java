package edu.najah.cap.data.hardDelete;

import edu.najah.cap.data.deletionHandler.*;
import edu.najah.cap.data.proxy.ActivityProxy;
import edu.najah.cap.data.proxy.PaymentProxy;
import edu.najah.cap.data.proxy.PostServiceProxy;
import edu.najah.cap.data.proxy.UserServiceProxy;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.posts.IPostService;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HardDeleteService {
    private static final Logger logger = LoggerFactory.getLogger(HardDeleteService.class);
    private final IPostService PostServiceProxy ;
    private final IUserService userServiceProxy ;
    private final IUserActivityService activityProxy;
    private final IPayment paymentProxy ;

    public HardDeleteService(IUserService userService, IPostService postService,
                             IPayment paymentService, IUserActivityService userActivityService) {
        this.userServiceProxy = new UserServiceProxy(userService);
        this.paymentProxy = new PaymentProxy(paymentService);
        this.activityProxy = new ActivityProxy(userActivityService);
        this.PostServiceProxy = new PostServiceProxy(postService);

    }

    public void deleteAccount(String userName) throws SystemBusyException, BadRequestException, NotFoundException {

        UserProfile user = userServiceProxy.getUser(userName);

        if (user == null) {
            throw new NotFoundException("User with username " + userName + " does not exist. Hard delete not performed.");
        }

        try {
            CompletableFuture<Void> postsFuture = CompletableFuture.runAsync(() -> {
                try {
                    UserItemDeletion.deleteItems(userName, PostServiceProxy.getPosts(userName),
                            new PostDeletionHandler(PostServiceProxy), "posts");
                } catch (SystemBusyException | BadRequestException | NotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

            CompletableFuture<Void> transactionsFuture = user.getUserType() == UserType.PREMIUM_USER
                    ? CompletableFuture.runAsync(() -> {
                try {
                    UserItemDeletion.deleteItems(userName, paymentProxy.getTransactions(userName),
                            new TransactionDeletionHandler(paymentProxy), "transactions");
                } catch (SystemBusyException | BadRequestException | NotFoundException e) {
                    throw new RuntimeException(e);
                }
            })

                    : CompletableFuture.completedFuture(null);
            CompletableFuture<Void> activitiesFuture = user.getUserType() != UserType.NEW_USER
                    ? CompletableFuture.runAsync(() -> {
                try {
                    UserItemDeletion.deleteItems(userName, activityProxy.getUserActivity(userName),
                            new ActivityDeletionHandler(activityProxy), "activities");
                } catch (SystemBusyException | NotFoundException | BadRequestException e) {
                    logger.error("An error occurred in the deletion process for {}: {}", userName, e.getMessage());

                }
            })
                    : CompletableFuture.completedFuture(null);

            CompletableFuture.allOf(postsFuture, transactionsFuture, activitiesFuture).join();

            UserStateManager.markAsDeleted(userName);
            userServiceProxy.deleteUser(userName);
            logger.info("Account and associated data deleted successfully for {}", userName);
            System.out.println("Account and associated data deleted successfully.");

        } catch (SystemBusyException | BadRequestException | NotFoundException e) {

            System.err.println("An error in deletion process try again later: " + e.getMessage());
        }
    }


}

