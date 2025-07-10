package com.app.dao;

import com.app.model.BookTransaction;
import com.app.util.DBConnection;
import com.app.util.LocalDateUtil;

import java.sql.*;
import java.time.LocalDate;

public class BookTransactionDAO {
    private final DBConnection dbConnection;
    public BookTransactionDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public BookTransaction createBookTransaction(BookTransaction bookTransaction) {
        String sql = "INSERT INTO book_transactions (member_id, book_id, date_of_issue, due_date, return_date,status, book_type) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bookTransaction.getMember_id());
            ps.setInt(2, bookTransaction.getBookId());
            ps.setDate(3, java.sql.Date.valueOf(bookTransaction.getDateOfIssue()));
            ps.setDate(4, java.sql.Date.valueOf(bookTransaction.getDueDate()));
            if (bookTransaction.getReturnDate() != null) {
                ps.setDate(5, java.sql.Date.valueOf(bookTransaction.getReturnDate()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }
            ps.setString(6, bookTransaction.getStatus());
            ps.setString(7, bookTransaction.getBookType());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                return getIssueTransactionById(id);
            }else{
                return null;
            }
        }catch (SQLException e){
            System.err.println("Error creating issue transaction: " + e.getMessage());
            throw new RuntimeException("Error creating issue transaction: " + e.getMessage());
        }
    }
    public BookTransaction getIssueTransactionById(int id) {
        String sql = "SELECT * FROM book_transactions WHERE transaction_id = ?";
        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int issueId = rs.getInt("transaction_id");
                int memberId = rs.getInt("member_id");
                int bookId = rs.getInt("book_id");
                LocalDate dateOfIssue = LocalDateUtil.getNullableLocalDate(rs, "date_of_issue");
                LocalDate dueDate = LocalDateUtil.getNullableLocalDate(rs, "due_date");
                LocalDate returnDate = LocalDateUtil.getNullableLocalDate(rs, "return_date");
                String status = rs.getString("status");
                String bookType = rs.getString("book_type");

                return new BookTransaction(issueId,memberId, bookId, dateOfIssue, dueDate, returnDate, status, bookType);
            }

        }catch (SQLException e){
            System.err.println("Error in getIssueTransactionById: " + e.getMessage());
            throw new RuntimeException("Error in getIssueTransactionById: " + e.getMessage());

        }
        return null;
    }
}
