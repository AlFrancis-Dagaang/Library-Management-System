package com.app.dao;

import com.app.model.BookTransaction;
import com.app.model.Member;
import com.app.util.DBConnection;
import com.app.util.LocalDateUtil;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MemberDAO {

    private final DBConnection db;

    public MemberDAO(DBConnection dbConnection) {
        this.db = dbConnection;
    }

    public Member getMemberByID(int id) {
        String sql = "SELECT *FROM members where member_id = ?";
        try(Connection conn = this.db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int memberID = rs.getInt("member_id");
                String type = rs.getString("type");
                int numberBookIssued = rs.getInt("number_book_issued");
                LocalDate dateOfMembership = LocalDateUtil.getNullableLocalDate(rs,"date_of_membership");
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
        String sql = "INSERT INTO members (type, date_of_membership, number_book_issued,  max_book_limit, name, address, phone_number)" +
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
        List<Member> members = new ArrayList<>();

        String sql = "SELECT * FROM members";

        try(Connection con = db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int memberID = rs.getInt("member_id");
                String type = rs.getString("type");
                int numberBookIssued = rs.getInt("number_book_issued");
                LocalDate dateOfMembership = LocalDateUtil.getNullableLocalDate(rs,"date_of_membership");
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
            System.err.println("SQLException in getAllMembers: " + e.getMessage());
            throw new RuntimeException("Database error in getAllMembers()");
        }
    }
    public Member updateMember(Member member, int id) {
        String sql = "UPDATE members SET name = ?, address = ?, phone_number = ?, type=?, date_of_membership= ?," +
                "number_book_issued=?, max_book_limit=? WHERE member_id = ?";

        try(Connection conn = this.db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, member.getName());
            ps.setString(2, member.getAddress());
            ps.setLong(3, member.getPhoneNumber());
            ps.setString(4, member.getType());
            ps.setDate(5,java.sql.Date.valueOf(member.getDateOfMembership()));
            ps.setInt(6, member.getNumberOfBookIssued());
            ps.setInt(7, member.getMaxBookLimit());
            ps.setInt(8, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return getMemberByID(id);
            }else{
                throw new SQLException("Failed to update member with id " + id);
            }
        }catch (SQLException e) {
            System.err.println("SQLException in updateMember: " + e.getMessage());
            throw new RuntimeException("Database error in updateMember()");
        }
    }

    public boolean deleteMemberById(int id) {
        String sql = "DELETE FROM members WHERE member_id = ?";
        try(Connection conn = this.db.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }catch (SQLException e) {
            System.err.println("SQLException in deleteMemberById: " + e.getMessage());
            throw new RuntimeException("Database error in deleteMember: " + e.getMessage());
        }

    }

    public List<Member> filterMembers(String type) {
        String sql = "SELECT * FROM members where type= ?";
        List<Member>members = new ArrayList<>();
        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int memberID = rs.getInt("id");
                String memberType = rs.getString("type");
                int numberBookIssued = rs.getInt("number_book_issued");
                LocalDate dateOfMembership = LocalDateUtil.getNullableLocalDate(rs,"date_of_membership");
                int maxBookLimit = rs.getInt("max_book_limit");
                String name = rs.getString("name");
                String address = rs.getString("address");
                long phoneNumber = rs.getLong("phone_number");
                Member getMember = new Member (name, address, phoneNumber, memberType );
                getMember.setMaxBookLimit(maxBookLimit);
                getMember.setNumberOfBookIssued(numberBookIssued);
                getMember.setMemberId(memberID);
                getMember.setDateOfMembership(dateOfMembership);
                members.add(getMember);
            }
            return members;
        }catch (SQLException e) {
            System.err.println("SQLException in sortMembersStudent: " + e.getMessage());
            throw new RuntimeException("Database error in sortMembersStudent()");
        }
    }

    public List<BookTransaction> getAllMemberTransactions(int memberId){
        String sql = "SELECT * FROM book_transactions WHERE member_id = ?";
        List <BookTransaction> memberTransactions = new ArrayList<>();

        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, memberId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                int issueId = rs.getInt("transaction_id");
                int id = rs.getInt("member_id");
                int bookId = rs.getInt("book_id");
                LocalDate dateOfIssue = LocalDateUtil.getNullableLocalDate(rs, "date_of_issue");
                LocalDate dueDate = LocalDateUtil.getNullableLocalDate(rs, "due_date");
                LocalDate returnDate = LocalDateUtil.getNullableLocalDate(rs, "return_date");
                String status = rs.getString("status");
                String bookType = rs.getString("book_type");

                memberTransactions.add(new  BookTransaction(id,memberId, bookId, dateOfIssue, dueDate, returnDate, status, bookType));
            }

            return memberTransactions;
        }catch (SQLException e) {
            System.err.println("SQLException in getAllMemberTransactions: " + e.getMessage());
            throw new RuntimeException("Database error in getAllMemberTransactions()");
        }

    }









}
