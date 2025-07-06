package com.app.dao;

import com.app.model.Admin;
import com.app.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthDAO {
    private DBConnection db;
    public AuthDAO() {}
    public AuthDAO(DBConnection db) {
        this.db = db;
    }

    public Admin getAdminByUsername(String username){
        String sql = "SELECT * FROM admin WHERE username= ?";
        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                String adminUsername = rs.getString("username");
                String adminPassword = rs.getString("password");
                return new Admin(adminUsername, adminPassword );
            }
        }catch (SQLException e){
            System.err.println("SQLException in getAdminByUsername: " + e.getMessage());
            throw new RuntimeException("Database error in getAdminUsername(): " + e.getMessage());
        }
        return null;
    }

}
