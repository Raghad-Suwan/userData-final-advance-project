package edu.najah.cap.data.handler;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

public class UserActivityDeletionHandler implements ItemDeletionHandler<UserActivity> {
    private final IUserActivityService userActivityService;
    public UserActivityDeletionHandler(IUserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }
    @Override
    public void handleDeletion(UserActivity userActivity) throws SystemBusyException, BadRequestException, NotFoundException {
        userActivityService.removeUserActivity(userActivity.getUserId(), userActivity.getId());
    }
}
