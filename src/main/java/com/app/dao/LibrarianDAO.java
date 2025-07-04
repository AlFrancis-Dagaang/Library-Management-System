package com.app.dao;

import com.app.model.Admin;
import com.app.model.Book;
import com.app.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibrarianDAO {
    private DBConnection db;
    private BookDAO bookDAO;
    public LibrarianDAO(DBConnection db) {
        this.bookDAO = new BookDAO(db);
        this.db = db;
    }

    public Admin getAdminByUsername(String username){
        String sql = "select * from admin where username=?";
        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                return new Admin(rs.getString("username"), rs.getString("password"));
            }
        }catch (SQLException e){
            System.out.println("getAdminByUsernameSQLException Error:" + e.getMessage());

        }
        return null;
    }

    public Book getBookById(){
        return bookDAO.getBookById(2);
    }
}
