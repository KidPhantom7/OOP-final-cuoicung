package dao;

import model.Shop;
import java.sql.*;
import java.util.*;

public class Shopdao
{
    public boolean existsByName(String name)
    {
        try (Connection conn = DBconnection.getConnection())
        {
            String sql = "SELECT * FROM shop WHERE name = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, name);
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
    public void add(Shop s)
    {
        try(Connection conn =DBconnection.getConnection())
        {
            String sql ="INSERT INTO shop(userId,name,description,address,status) VALUES(?,?,?,?,?)";
            PreparedStatement ps= conn.prepareStatement(sql); //gui cau lenh sql
            ps.setInt(1,s.getUserId());
            ps.setString(2,s.getName());
            ps.setString(3,s.getDescription());
            ps.setString(4,s.getAddress());
            ps.setString(5,"active");
            ps.executeUpdate();
            System.out.println("Them thanh cong");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // Lay tat ca shop cua mot seller theo userId
    public List<Shop> getByUserId(int userId)
    {
        List<Shop> list = new ArrayList<>();
        try(Connection conn = DBconnection.getConnection())
        {
            String sql = "SELECT * FROM shop WHERE userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Shop s = new Shop();
                s.setShopId(rs.getInt("shopId"));
                s.setUserId(rs.getInt("userId"));
                s.setName(rs.getString("name"));
                s.setAddress(rs.getString("address"));
                s.setStatus(rs.getString("status"));
                list.add(s);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    // Kiem tra shop co thuoc ve userId nay khong
    public boolean isOwner(int shopId, int userId)
    {
        try (Connection conn = DBconnection.getConnection())
        {
            String sql = "SELECT shopId FROM shop WHERE shopId=? AND userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, shopId);
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
    public List<Shop> getAll()
    {
        List<Shop> list=new ArrayList<>();
        try(Connection conn=DBconnection.getConnection())
        {
            String sql = "SELECT * FROM shop";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next())
            {
                Shop s = new Shop();
                s.setShopId(rs.getInt("shopId"));
                s.setUserId(rs.getInt("userId"));
                s.setName(rs.getString("name"));
                s.setAddress(rs.getString("address"));
                s.setStatus(rs.getString("status"));
                list.add(s);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    public void update(Shop s)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="UPDATE shop SET description=?, address=? WHERE shopId=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setString(1,s.getDescription());
            ps.setString(2,s.getAddress());
            ps.setInt(3,s.getShopId());
            ps.executeUpdate();
            System.out.println("Da update");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void close(int id)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="UPDATE shop SET status='close' WHERE shopId=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
            System.out.println("Da cap nhat trang thai: dong");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void reopen(int id)
    {
        try(Connection conn = DBconnection.getConnection())
        {
            String sql = "UPDATE shop SET status='active' WHERE shopId=?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1, id);

            ps.executeUpdate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void removeShop(int id)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="DELETE FROM shop WHERE shopId=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
            System.out.println("Da xoa");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}