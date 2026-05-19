package dao;
import model.Review;
import java.util.*;
import java.sql.*;

public class Reviewdao
{
    public boolean isAlreadyReviewed(int userId, int shopId)
    {
        try (Connection conn = DBconnection.getConnection())
        {
            String sql = "SELECT * FROM review WHERE userId = ? AND shopId = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setInt(2, shopId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    // Kiem tra review co thuoc ve userId nay khong
    public boolean isReviewOwner(int reviewId, int userId)
    {
        try (Connection conn = DBconnection.getConnection())
        {
            String sql = "SELECT reviewId FROM review WHERE reviewId=? AND userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, reviewId);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }
    public void add(Review r)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="INSERT INTO review(userId,shopId,rating,comment) VALUES(?,?,?,?)";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1,r.getUserId());
            ps.setInt(2,r.getShopId());
            ps.setInt(3,r.getRating());
            ps.setString(4,r.getComment());
            ps.executeUpdate();
            System.out.println("Da them thanh cong");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public List<Review> getAll(int shopId)
    {
        List<Review> list=new ArrayList<>();
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="SELECT * FROM review WHERE shopId=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1,shopId);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                Review r=new Review();
                r.setReviewId(rs.getInt("reviewId"));
                r.setUserId(rs.getInt("userId"));
                r.setShopId(rs.getInt("shopId"));
                r.setRating(rs.getInt("rating"));
                r.setComment(rs.getString("comment"));
                list.add(r);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    public void upadte(Review r)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="UPDATE review SET rating=?, comment=? WHERE reviewId=? AND userID=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1, r.getRating());
            ps.setString(2, r.getComment());
            ps.setInt(3, r.getReviewId());
            ps.setInt(4, r.getUserId());
            ps.executeUpdate();
            System.out.println("Da update");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void delete(int id)
    {
        try (Connection conn = DBconnection.getConnection())
        {
            String sql = "DELETE FROM review WHERE reviewId=? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            System.out.println("Da xoa");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}