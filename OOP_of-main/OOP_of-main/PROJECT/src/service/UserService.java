package service;

import dao.UserDAO;
import model.User;
import java.util.List;

public class UserService {
    private final UserDAO userDAO = new UserDAO();

    public User getUserProfile(String username) {
        return userDAO.getUserByUsername(username);
    }

    public List<User> getAllSellers() {
        return userDAO.getUsersByRole("SELLER");
    }

    public List<User> getAllCustomers() {
        return userDAO.getUsersByRole("CUSTOMER");
    }

    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}
