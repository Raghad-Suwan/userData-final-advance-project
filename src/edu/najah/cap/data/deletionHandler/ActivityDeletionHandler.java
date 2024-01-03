package edu.najah.cap.data.deletionHandler;



import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.data.proxy.ActivityProxy;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

public class ActivityDeletionHandler implements ItemDeletionHandler<UserActivity> {
    private final IUserActivityService activityProxy;

    public ActivityDeletionHandler(IUserActivityService userActivityService) {
        this.activityProxy = new ActivityProxy(userActivityService);

    }
    @Override
    public void handleDeletion(UserActivity userActivity) throws SystemBusyException, BadRequestException, NotFoundException {
        activityProxy.removeUserActivity(userActivity.getUserId(), userActivity.getId());
    }
}