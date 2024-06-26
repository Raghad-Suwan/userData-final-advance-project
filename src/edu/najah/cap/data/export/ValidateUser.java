package edu.najah.cap.data.export;


import edu.najah.cap.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValidateUser {

    private static final Logger logger = LoggerFactory.getLogger(ValidateUser.class);

    public static void validateUser(String userName) throws BadRequestException {

        if (userName == null || userName.isEmpty()) {
            throw new BadRequestException("User name is required");
        }
        if (userName.length() < 4) {
            throw new BadRequestException("User name must be at least 4 characters");
        }
        if (!userName.startsWith("user")) {
            throw new BadRequestException("User name must start with 'user'");
        }

        logger.info("User name validated successfully");
    }
}

