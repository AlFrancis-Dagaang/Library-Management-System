package com.app.dao;

import com.app.model.Member;
import com.app.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                getMember.setDateOfMembership(dateOfMembership);

                return getMember;
            }
        } catch (SQLException e) {
            System.err.println("SQLException in getMemberByID: " + e.getMessage());
            throw new RuntimeException("Database error in getMemberByID()");
        }
        return null;
    }
    public Member addMember(Member member) {
        String sql = "INSERT INTO member (type, date_of_membership, number_book_issued,  max_book_limit, name, address, phone_number)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection conn = this.db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, member.getType());
            ps.setDate(2, new java.sql.Date(new Date().getTime()));
            ps.setInt(3, member.getNumberOfBookIssued());
            ps.setInt(4, member.getMaxBookLimit());
            ps.setString(5, member.getName());
            ps.setString(6, member.getAddress());
            ps.setLong(7, member.getPhoneNumber());

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int memberId;
            if (rs.next()) {
                memberId = rs.getInt(1);
                return getMemberByID(memberId);
            }else {
                throw new SQLException("Failed to retrieve generated member ID.");
            }
        } catch (SQLException e) {
            System.err.println("SQLException in addMember: " + e.getMessage());
            throw new RuntimeException("Database error in addMember()");
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<Member>();

        String sql = "SELECT * FROM member";

        try(Connection con = db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
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
                getMember.setDateOfMembership(dateOfMembership);
                members.add(getMember);
            }
            return members;
        }catch (SQLException e) {
            throw new RuntimeException("getAllMembersSQLException Error: " + e.getMessage());
        }
    }
    public Member udpateMember(Member member, int id) {
        String sql = "UPDATE member SET name = ?, address = ?, phone_number = ?, type=?, date_of_membership= ?," +
                "number_book_issued=?, max_book_limit=? WHERE id = ?";

        try(Connection conn = this.db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getAddress());
            ps.setLong(3, member.getPhoneNumber());
            ps.setString(4, member.getType());
            ps.setDate(5,new java.sql.Date(member.getDateOfMembership().getTime()));
            ps.setInt(6, member.getNumberOfBookIssued());
            ps.setInt(7, member.getMaxBookLimit());
            ps.setInt(8, id);
            int rowsAffected = ps.executeUpdate();
            if (rowsAffected > 0) {
                return getMemberByID(id);
            }else{
                return null;
            }
        }catch (SQLException e) {
            throw new RuntimeException("udpateMemberSQLException Error: " + e.getMessage());
        }
    }

    public boolean deleteMember(int id) {
        String sql = "DELETE FROM member WHERE id = ?";
        try(Connection conn = this.db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException e) {
            throw new RuntimeException("deleteMemberSQLException Error: " + e.getMessage());
        }


    }



}
