package edu.najah.cap.data.delete;

import java.util.HashMap;
import java.util.Map;

public class UserStateManager {
    private static Map<String, Boolean> userStates = new HashMap<>();

    public static void markAsDeleted(String userName) {
        userStates.put(userName, true);
        System.out.println("{ The name marked as deleted } ");
    }
    public boolean isDeleted(String userName) {
        return userStates.getOrDefault(userName, false);
    }
}
