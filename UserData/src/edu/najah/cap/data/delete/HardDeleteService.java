package edu.najah.cap.data.delete;

import edu.najah.cap.data.handler.PostDeletionHandler;
import edu.najah.cap.data.handler.TransactionDeletionHandler;
import edu.najah.cap.data.handler.UserActivityDeletionHandler;
import edu.najah.cap.data.handler.UserItemDeletion;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.posts.IPostService;

public class HardDeleteService {
    private IUserService userService;
    private final IPostService postService;
    private final IPayment paymentService;
    private final IUserActivityService userActivityService;

    public HardDeleteService(IUserService userService, IPostService postService,
                             IPayment PaymentService, IUserActivityService userActivityService) {
        this.userService = userService;
        this.postService = postService;
        this.paymentService = PaymentService;
        this.userActivityService = userActivityService;
    }

    public void deleteAccount(String userName) throws SystemBusyException, BadRequestException, NotFoundException {

        UserProfile user = userService.getUser(userName);

        if (user == null) {
            throw new NotFoundException("User with username " + userName + " does not exist. Hard delete not performed.");
        }
        try {
            UserItemDeletion.deleteItems(userName, postService.getPosts(userName),
                    new PostDeletionHandler(postService), "posts");

            if (user.getUserType() == UserType.PREMIUM_USER) {

                UserItemDeletion.deleteItems(userName, paymentService.getTransactions(userName),
                        new TransactionDeletionHandler(paymentService), "transactions");

            }

            if (user.getUserType() != UserType.NEW_USER) {
                UserItemDeletion.deleteItems(userName, userActivityService.getUserActivity(userName),
                        new UserActivityDeletionHandler(userActivityService), "activities");

            }
            UserStateManager.markAsDeleted(userName);
            userService.deleteUser(userName);
            System.out.println("Account and associated data deleted successfully.");
        } catch (SystemBusyException | BadRequestException | NotFoundException e) {
            System.err.println("An error in deletion process try again later: " + e.getMessage());
        }
    }
}

