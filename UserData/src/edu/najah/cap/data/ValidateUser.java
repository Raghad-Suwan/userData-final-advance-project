package edu.najah.cap.data;

import edu.najah.cap.exceptions.BadRequestException;


public class ValidateUser {
    public static void validateUser(String userName) throws BadRequestException { // break SRP and OCP
        if (userName == null || userName.isEmpty()) {
            throw new BadRequestException("User name is required");
        }
        if (userName.length() < 4) {
            throw new BadRequestException("User name must be at least 4 characters");
        }
        if (!userName.startsWith("user")) {
            throw new BadRequestException("User name must start with 'user'");
        }

    }

}
