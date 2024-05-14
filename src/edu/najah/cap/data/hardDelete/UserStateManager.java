package edu.najah.cap.data.hardDelete;


import java.util.HashMap;
import java.util.Map;

public class UserStateManager {
    private static Map<String, Boolean> userStates = new HashMap<>();
    public static void markAsDeleted(String userName) {
        userStates.put(userName, true);
    }
    public static boolean isDeleted(String userName) {
        return userStates.getOrDefault(userName, false);
    }
}
