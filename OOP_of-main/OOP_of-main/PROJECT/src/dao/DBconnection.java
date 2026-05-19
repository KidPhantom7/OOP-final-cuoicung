package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DBconnection {
    public static Connection getConnection() {
        try
        {
            String url = "jdbc:mysql://localhost:3306/btl";
            String user = "root";
            String password = "12345";
            return DriverManager.getConnection(url, user, password);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
    public static void closeConnection(Connection conn, Statement st, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (st != null) st.close();
            if (conn != null) conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Connection conn = getConnection();
        if (conn != null)
        {
            System.out.println("Kết nối thành công!");
        }
    }
}