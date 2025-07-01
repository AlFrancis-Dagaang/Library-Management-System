package com.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection{
    private final String url = "jdbc:mysql://localhost:3306/librarydb";
    private final String user = "root";
    private final String password = "@Pollywag123";

    public DBConnection() {}

    public Connection getConnection() throws SQLException {
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");

            return DriverManager.getConnection(url, user, password);
        }catch(ClassNotFoundException e){
            System.out.println("error: " + e.getMessage());
            return null;
        }
    }
}
