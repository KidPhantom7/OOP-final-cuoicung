package dao;
import java.util.*;
import model.Location;
import java.sql.*;

public class Locationdao
{
    public boolean isLocationExists(int userId, String detail)
    {
        try (Connection conn = DBconnection.getConnection())
        {
            String sql = "SELECT * FROM location WHERE userId = ? AND detail = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ps.setString(2, detail);
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
    public void AddLocate(Location locate)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="INSERT INTO location(userId,detail,phone) VALUES(?,?,?)";
            PreparedStatement ps= conn.prepareStatement(sql);
            ps.setInt(1,locate.getUserId());
            ps.setString(2, locate.getDetail());
            ps.setString(3,locate.getPhone());
            ps.executeUpdate();
            System.out.println("Da them thanh cong");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public List<Location> getAllLocate(int userId)
    {
        List<Location> list=new ArrayList<>();
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="SELECT * FROM location WHERE userId=?";
            PreparedStatement ps=conn.prepareStatement(sql);
            ps.setInt(1,userId);
            ResultSet rs=ps.executeQuery();
            while(rs.next())
            {
                Location diachi=new Location();
                diachi.setLocationId(rs.getInt("locationId"));
                diachi.setUserId(rs.getInt("userId"));
                diachi.setDetail(rs.getString("detail"));
                diachi.setPhone(rs.getString("phone"));
                list.add(diachi);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return list;
    }
    public void updateLocate(Location locate)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="UPDATE location SET detail=?, phone=? WHERE locationId=? AND userId=?";
            PreparedStatement ps= conn.prepareStatement(sql);
            ps.setString(1,locate.getDetail());
            ps.setString(2, locate.getPhone());
            ps.setInt(3,locate.getLocationId());
            ps.setInt(4,locate.getUserId());
            ps.executeUpdate();
            System.out.println("Da update");
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void deleteLocate(int id)
    {
        try(Connection conn=DBconnection.getConnection())
        {
            String sql="DELETE FROM location where locationId=?";
            PreparedStatement ps= conn.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
            System.out.println("Da xoa");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    // Kiem tra location co thuoc ve userId nay khong
    public boolean isLocationOwner(int locationId, int userId)
    {
        try (Connection conn = DBconnection.getConnection())
        {
            String sql = "SELECT locationId FROM location WHERE locationId=? AND userId=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, locationId);
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
}