package dao;

import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Categorydao {

    public void add(Category c) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "INSERT INTO category(name, description) VALUES(?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean findById(int categoryId) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT categoryId FROM category WHERE categoryId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // Nếu tìm thấy sẽ trả về true, ngược lại false
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public void update(Category c) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "UPDATE category SET name = ?, description = ? WHERE categoryId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, c.getName());
            ps.setString(2, c.getDescription());
            ps.setInt(3, c.getId());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int categoryId) {
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "DELETE FROM category WHERE categoryId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, categoryId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Category> getAll() {
        List<Category> list = new ArrayList<>();
        try (Connection conn = DBconnection.getConnection()) {
            String sql = "SELECT * FROM category";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Category(rs.getInt("categoryId"), rs.getString("name"), rs.getString("description")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
