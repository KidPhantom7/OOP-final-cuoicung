package util;

import model.User;

public class SessionManager {
    private static User currentUser = null;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clearSession() {
        currentUser = null;
    }

    public static boolean isLoggedIn() {
        return currentUser != null;
    }

    public static boolean isAdmin() {
        return isLoggedIn() && "ADMIN".equalsIgnoreCase(currentUser.getRole());
    }

    public static boolean isSeller() {
        return isLoggedIn() && "SELLER".equalsIgnoreCase(currentUser.getRole());
    }

    public static boolean isCustomer() {
        return isLoggedIn() && "CUSTOMER".equalsIgnoreCase(currentUser.getRole());
    }

    public static int getCurrentUserId() {
        return isLoggedIn() ? currentUser.getId() : -1;
    }
}
