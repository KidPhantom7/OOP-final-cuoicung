package service;

import dao.UserDAO;
import model.User;
import util.SessionManager;

public class AuthService {
    private final UserDAO userDAO = new UserDAO();

    // Dang ky mac dinh -> CUSTOMER
    public boolean register(String username, String password, String email, String fullName) {
        return register(username, password, email, fullName, "CUSTOMER");
    }

    // Dang ky voi role cu the (chi chap nhan SELLER hoac CUSTOMER)
    public boolean register(String username, String password, String email, String fullName, String role) {
        if (!"SELLER".equalsIgnoreCase(role) && !"CUSTOMER".equalsIgnoreCase(role)) {
            System.out.println("Role khong hop le! Chi duoc dang ky SELLER hoac CUSTOMER.");
            return false;
        }
        if (userDAO.isUsernameExists(username)) {
            System.out.println("Ten tai khoan da ton tai!");
            return false;
        }
        if (userDAO.isEmailExists(email)) {
            System.out.println("Email da ton tai!");
            return false;
        }
        // Luu mat khau nguyen ban (khong ma hoa)
        User user = new User(username, password, email, fullName, role.toUpperCase());
        return userDAO.registerUser(user);
    }

    public boolean login(String username, String password) {
        User user = userDAO.getUserByUsername(username);
        if (user == null) {
            System.out.println("Tai khoan khong ton tai!");
            return false;
        }
        // So sanh mat khau nguyen ban
        if (user.getPassword().equals(password)) {
            SessionManager.setCurrentUser(user);
            return true;
        } else {
            System.out.println("Mat khau khong chinh xac!");
            return false;
        }
    }

    public void logout() {
        SessionManager.clearSession();
        System.out.println("Da dang xuat thanh cong!");
    }
}
