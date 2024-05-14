package edu.najah.cap.data.proxy;

import edu.najah.cap.data.export.ValidateUser;
import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.activity.UserActivityService;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ActivityProxy implements IUserActivityService {

    private IUserActivityService userActivityService;

    private static final Map<String, List<UserActivity>> userActivityMap = UserActivityService.getUserActivityMap();

    public ActivityProxy(IUserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }


    @Override
    public void addUserActivity(UserActivity userActivity) {
        userActivityService.addUserActivity(userActivity);

    }

    @Override
    public List<UserActivity> getUserActivity(String userId) throws  BadRequestException, NotFoundException {
        ValidateUser.validateUser(userId);
        if (!userActivityMap.containsKey(userId)) {
            throw new NotFoundException("User does not exist");
        }
        return userActivityMap.get(userId);

    }

    @Override
    public void removeUserActivity(String userId, String id) throws SystemBusyException, BadRequestException, NotFoundException {
        ValidateUser.validateUser(userId);
        if (!userActivityMap.containsKey(userId)) {
            throw new NotFoundException("User does not exist");
        }
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        Iterator<UserActivity> iterator = userActivityMap.get(userId).iterator();
        while (iterator.hasNext()) {
            UserActivity activity = iterator.next();
            if (activity.getId().equals(id)) {
                iterator.remove();
            }
        }
        userActivityMap.put(userId, userActivityMap.get(userId));
    }
}
