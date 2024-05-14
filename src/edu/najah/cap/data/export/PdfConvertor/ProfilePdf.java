package edu.najah.cap.data.export.PdfConvertor;

import edu.najah.cap.iam.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProfilePdf {

    private static final Logger logger = LoggerFactory.getLogger(ProfilePdf.class);

    public byte[] export(UserProfile userProfile) {
        if (userProfile == null) {
            logger.info("No user profile to export.");
            return new byte[0];
        }

        logger.info("Exporting User Profile: Name={}, Type={}, Email={}, Phone={}, UserName={}, Password={}, Role={}, Department={}, Organization={}, Country={}, Street={}, City={}",
                userProfile.getFirstName() + " " + userProfile.getLastName(), userProfile.getUserType(), userProfile.getEmail(), userProfile.getPhoneNumber(), userProfile.getUserName(), userProfile.getPassword(), userProfile.getRole(), userProfile.getDepartment(), userProfile.getOrganization(), userProfile.getCountry(), userProfile.getStreet(), userProfile.getCity());

        return new byte[0];
    }
}

