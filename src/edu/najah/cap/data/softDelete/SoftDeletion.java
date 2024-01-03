package edu.najah.cap.data.softDelete;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.data.proxy.ActivityProxy;
import edu.najah.cap.data.proxy.PaymentProxy;
import edu.najah.cap.data.proxy.PostServiceProxy;
import edu.najah.cap.data.proxy.UserServiceProxy;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;

import java.util.*;
import java.util.logging.Logger;

public class SoftDeletion {
    private static final Logger logger = Logger.getLogger(SoftDeletion.class.getName());
    private static final Map<String, Boolean> softDeletedUsers = new HashMap<>();
    private static final Map<String, Set<String>> softDeletedActivities = new HashMap<>();
    private static final Map<String, Set<String>> softDeletedPosts = new HashMap<>();
    private static final Map<String, List<Transaction>> softDeletedTransactions = new HashMap<>();

    public static void softDeleteUser(IUserService userService, IUserActivityService userActivityService,
                                      IPostService postService, IPayment paymentService, String userName) throws SystemBusyException, NotFoundException, BadRequestException {
        UserServiceProxy userServiceProxy = new UserServiceProxy(userService);
        ActivityProxy activityProxy = new ActivityProxy(userActivityService);
        PostServiceProxy postServiceProxy = new PostServiceProxy(postService);
        PaymentProxy paymentProxy = new PaymentProxy(paymentService);

        UserProfile user = userServiceProxy.getUser(userName);

        if (user != null) {
            softDeletedUsers.put(userName, true);

            removeUserActivity(activityProxy, userName);
            deletePost(postServiceProxy, userName);

            softDeleteTransactions(paymentProxy, userServiceProxy, userName);

            logger.info("User '" + userName + "' has been soft-deleted.");
        } else {
            logger.warning("User '" + userName + "' not found.");
        }
    }

    private static void removeUserActivity(ActivityProxy activityProxy, String userId) {
        try {
            List<UserActivity> activities = activityProxy.getUserActivity(userId);
            Set<String> deletedActivityIds = new HashSet<>();
            for (UserActivity activity : activities) {
                deletedActivityIds.add(activity.getId());
            }
            softDeletedActivities.put(userId, deletedActivityIds);
        } catch (Exception e) {
            logger.severe("Error while soft deleting activities for user: " + userId);
        }
    }

    private static void deletePost(PostServiceProxy postServiceProxy, String author) {
        try {
            List<Post> posts = postServiceProxy.getPosts(author);
            Set<String> deletedPostIds = new HashSet<>();
            for (Post post : posts) {
                deletedPostIds.add(post.getId());
            }
            softDeletedPosts.put(author, deletedPostIds);
        } catch (Exception e) {
            logger.severe("Error while soft deleting posts for user: " + author);
        }
    }

    private static void softDeleteTransactions(PaymentProxy paymentProxy, UserServiceProxy userServiceProxy, String userName)
            throws NotFoundException, BadRequestException {
        UserProfile user = userServiceProxy.getUser(userName);
        if (user == null) {
            throw new NotFoundException("User does not exist");
        }

        if (!"premium".equals(user.getUserType())) {
            logger.info("User is not a premium user, no transactions to soft delete");
            return;
        }

        List<Transaction> userTransactions = paymentProxy.getTransactions(userName);
        for (Transaction transaction : userTransactions) {
            // Soft delete logic for each transaction
            softDeletedTransactions.computeIfAbsent(userName, k -> new ArrayList<>()).add(transaction);
            paymentProxy.removeTransaction(userName, transaction.getId());
        }
    }

    public static boolean isUserSoftDeleted(String userName) {
        return softDeletedUsers.getOrDefault(userName, false);
    }

    public static boolean isActivitySoftDeleted(String userId, String activityId) {
        return softDeletedActivities.getOrDefault(userId, new HashSet<>()).contains(activityId);
    }

    public static boolean isPostSoftDeleted(String author, String postId) {
        return softDeletedPosts.getOrDefault(author, new HashSet<>()).contains(postId);
    }

    public static boolean isTransactionSoftDeleted(String userName, String transactionId) {
        List<Transaction> deletedTransactions = softDeletedTransactions.get(userName);
        if (deletedTransactions != null) {
            for (Transaction transaction : deletedTransactions) {
                if (transaction.getId().equals(transactionId)) {
                    return true;
                }
            }
        }
        return false;
    }


}



