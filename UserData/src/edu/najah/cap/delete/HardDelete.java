package edu.najah.cap.delete;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HardDelete {

    private IUserService userService;
    private IPostService postService;
    private IPayment PaymentService;
    private IUserActivityService userActivityService;

    public HardDelete(IUserService userService, IPostService postService,
                      IPayment PaymentService, IUserActivityService userActivityService) {
        this.userService = userService;
        this.postService = postService;
        this.PaymentService = PaymentService;
        this.userActivityService = userActivityService;

    }

    public void deleteAccount(String userName) throws SystemBusyException, BadRequestException, NotFoundException {
        try {
            if (userName == null) {
                throw new NotFoundException("User with username " + userName + " does not exist. Hard delete not performed.");
            }

            List<Post> userPosts = postService.getPosts(userName);
            List<Post> postsToDelete = new ArrayList<>();

            if (userPosts != null) {
                for (Post post : userPosts) {
                    postsToDelete.add(post);
                }
            } else {
                System.out.println("No posts found for the user: " + userName);
            }

            // Iterate over postsToDelete and perform deletion
            Iterator<Post> iterator = postsToDelete.iterator();
            while (iterator.hasNext()) {
                Post post = iterator.next();
                postService.deletePost(userName, post.getId());
                iterator.remove(); // Safely remove the item from the list
            }

            if (userService.getUser(userName).getUserType() == UserType.PREMIUM_USER) {
                System.out.println("3. Payment component (Payment information - only for premium users)");
                List<Transaction> transactions = PaymentService.getTransactions(userName);
                List<Transaction> transactionsToDelete = new ArrayList<>();
                if (transactions != null) {
                    for (Transaction transaction : transactions) {
                        transactionsToDelete.add(transaction);
                    }
                } else {
                    System.out.println("No transactions found for the user: " + userName);
                }
                Iterator<Transaction> iterator3 = transactionsToDelete.iterator();
                while (iterator3.hasNext()) {
                    Transaction transaction = iterator3.next();
                    PaymentService.removeTransaction(userName, transaction.getId());
                    iterator3.remove(); // Safely remove the item from the list
                }

            }

            if (userService.getUser(userName).getUserType() != UserType.NEW_USER) {
                System.out.println("4. Activity component (User activities - not applicable for new users)");
                // Delete user activities
                List<UserActivity> userActivities = userActivityService.getUserActivity(userName);
                List<UserActivity> activitiesToDelete = new ArrayList<>();
                if (userActivities != null) {
                    for (UserActivity activity : userActivities) {
                        activitiesToDelete.add(activity);
                    }
                } else {
                    System.out.println("No activities found for the user: " + userName);
                }
                Iterator<UserActivity> iterator2 = activitiesToDelete.iterator();
                while (iterator2.hasNext()) {
                    UserActivity activity = iterator2.next();
                    userActivityService.removeUserActivity(userName, activity.getId());
                    iterator2.remove(); // Safely remove the item from the list
                }

            }
            userService.deleteUser(userName);
            System.out.println("Account and associated data deleted successfully.");

        } catch (NullPointerException e) {
            e.printStackTrace(); // or log the error
            // Handle or log the NullPointerException accordingly
        }
    }
}