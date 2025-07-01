package com.app.dao;

import com.app.model.Member;
import com.app.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class MemberDAO {

    private DBConnection db;

    public MemberDAO(DBConnection dbConnection) {
        this.db = dbConnection;
    }
    public Member getMemberByID(int id) {
        String sql = "SELECT *FROM member where id = ?";
        try(Connection conn = this.db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                Member member = new Member();
                int memberID = rs.getInt("id");
                String type = rs.getString("type");
                int numberBookIssued = rs.getInt("number_book_issued");
                Date dateOfMembership = rs.getDate("date_of_membership");
                int maxBookLimit = rs.getInt("max_book_limit");
                String name = rs.getString("name");
                String address = rs.getString("address");
                long phoneNumber = rs.getLong("phone_number");

                Member getMember = new Member (name, address, phoneNumber, type);
                getMember.setMaxBookLimit(maxBookLimit);
                getMember.setNumberOfBookIssued(numberBookIssued);
                getMember.setMemberId(memberID);
                getMember.setDataOfMembership(dateOfMembership);

                return getMember;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }



}
