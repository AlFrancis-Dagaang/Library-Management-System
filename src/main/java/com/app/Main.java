package com.app;

import com.app.util.DBConnection;

import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        try(Connection con = db.getConnection()) {
            System.out.println("Connected to database");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
