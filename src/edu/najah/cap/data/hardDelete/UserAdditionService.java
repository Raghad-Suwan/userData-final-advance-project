package edu.najah.cap.data.hardDelete;

import edu.najah.cap.data.proxy.UserServiceProxy;
import edu.najah.cap.exceptions.*;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserAdditionService {
    private static final Logger logger = LoggerFactory.getLogger(UserAdditionService.class);
    private final IUserService userServiceProxy;

    public UserAdditionService(IUserService userService) {
        this.userServiceProxy = new UserServiceProxy(userService);
    }

    public void addUser(UserProfile user) throws BadRequestException, SystemBusyException, NotFoundException, UserAlreadyExistsException, UserDeletedException {
        logger.info("Attempting to add new user: ", user.getUserName());

        if (UserStateManager.isDeleted(user.getUserName())) {
            throw new UserDeletedException("User has already been deleted");
        }

        if (userServiceProxy.getUser(user.getUserName()) != null) {
            throw new UserAlreadyExistsException("Username is already in use");
        }
        userServiceProxy.addUser(user);
        logger.info("User added successfully", user.getUserName());
    }
}

