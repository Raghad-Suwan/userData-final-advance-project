package edu.najah.cap.data.proxy;

import edu.najah.cap.data.export.ValidateUser;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserService;


import java.util.Map;

public class UserServiceProxy implements IUserService {

    private static final Map<String, UserProfile> users = UserService.getUsers();
    private IUserService userService;

    public UserServiceProxy(IUserService userService) {
        this.userService = userService;
    }


    @Override
    public void addUser(UserProfile user) {
        userService.addUser(user);

    }

    @Override
    public void updateUser(UserProfile user) throws NotFoundException ,BadRequestException {
        ValidateUser.validateUser(user.getUserName());
        if (!users.containsKey(user.getUserName())) {
            throw new NotFoundException("User does not exist");
        }
        users.put(user.getUserName(), user);
    }

    @Override
    public void deleteUser(String userName) throws NotFoundException, BadRequestException {
        ValidateUser.validateUser(userName);
        if (!users.containsKey(userName)) {
            throw new NotFoundException("User does not exist");
        }
        users.remove(userName);

    }

    @Override
    public UserProfile getUser(String userName) throws NotFoundException,BadRequestException {
        ValidateUser.validateUser(userName);
        if (!users.containsKey(userName)) {
            throw new NotFoundException("User does not exist");
        }
        return users.get(userName);
    }
}
