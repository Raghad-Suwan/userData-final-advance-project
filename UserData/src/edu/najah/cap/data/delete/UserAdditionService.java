package edu.najah.cap.data.delete;

import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserService;

public class UserAdditionService {
    private UserService userService;
    private UserStateManager userStateManager;

    public UserAdditionService(UserService userService, UserStateManager userStateManager) {
        this.userService = userService;
        this.userStateManager = userStateManager;
    }

    public void addUser(UserProfile user) throws BadRequestException{
        if (userStateManager.isDeleted(user.getUserName())) {
            throw new BadRequestException("Username was previously deleted and cannot be reused");
        }
        userService.addUser(user);
    }
}
