package com.app.dao;

import com.app.model.Admin;
import com.app.model.Transaction;
import com.app.model.TransactionDetailsDTO;
import com.app.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TransactionDAO {
    private DBConnection db;
    public TransactionDAO(DBConnection db) {
        this.db = db;
    }

    public Transaction createTransaction (Transaction transaction){
        String sql = "INSERT INTO transactions(member_id, book_id, date_of_issue, due_date, return_date, status)  VALUES(?,?,?,?,?,?)";

        try (Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, transaction.getMemberId());
            ps.setInt(2, transaction.getBookId());
            ps.setDate(3, new java.sql.Date(transaction.getDateOfIssue().getTime()));
            ps.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));

            if (transaction.getReturnDate() == null) {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setString(6, transaction.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int generatedId = rs.getInt(1); // First column of the first (and only) row
                return getTransactionById(generatedId);
            }else{
                throw new SQLException("Failed to create transaction");
            }

        }catch (SQLException e){
            System.err.println("SQLException in createTransaction: " + e.getMessage());
            throw new RuntimeException("Database error in createTransaction(): " + e.getMessage());
        }
    }

    public Transaction getTransactionById(int id){
        String sql = "SELECT * FROM transactions WHERE transaction_id=?";

        try (Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                int memberId = rs.getInt("member_id");
                int bookId = rs.getInt("book_id");
                Date dateOfIssue = rs.getDate("date_of_issue");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");
                String status = rs.getString("status");

                return new Transaction(transactionId, memberId, bookId, dateOfIssue, dueDate, returnDate, status);
            }

        }catch (SQLException e){
            System.err.println("SQLException in getTransactionById: " + e.getMessage());
            throw new RuntimeException("Database error in getTransactionById(): " + e.getMessage());
        }
        return null;
    }


    public TransactionDetailsDTO getTransactionDetailsById(int id){
        String sql = "SELECT \n" +
                "    t.transaction_id, t.date_of_issue, t.due_date, t.return_date, t.status AS transaction_status,\n" +
                "    m.name AS member_name,\n" +
                "    b.title AS book_title, b.price AS book_price\n" +
                "FROM transactions t\n" +
                "JOIN member m ON t.member_id = m.id\n" +
                "JOIN books b ON t.book_id = b.book_id\n" +
                "WHERE t.transaction_id = ?;\n";
        try (Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int transactionId = rs.getInt("transaction_id");
                String memberName = rs.getString("member_name");
                String bookTitle = rs.getString("book_title");
                BigDecimal bookPrice = rs.getBigDecimal("book_price");
                Date dateOfIssue = rs.getDate("date_of_issue");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");
                String status = rs.getString("transaction_status");

                return new TransactionDetailsDTO(transactionId, memberName, bookPrice, bookTitle, dateOfIssue, dueDate, returnDate, status);
            }

        }catch (SQLException e){
            System.err.println("SQLException in getTransactionDetailsById: " + e.getMessage());
            throw new RuntimeException("Database error in getTransactionDetailsById(): " + e.getMessage());
        }
        return null;
    }

    public Transaction updateTransaction (Transaction transaction, int id){
        String sql = "UPDATE transactions set member_id =? , book_id = ?, date_of_issue = ?, due_date = ?, return_date = ?, status =? where transaction_id = ?  ";

        try (Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, transaction.getMemberId());
            ps.setInt(2, transaction.getBookId());
            ps.setDate(3, new java.sql.Date(transaction.getDateOfIssue().getTime()));
            ps.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));
            if (transaction.getReturnDate() != null) {
                ps.setDate(5, new java.sql.Date(transaction.getReturnDate().getTime()));
            }else{
                ps.setNull(5, java.sql.Types.DATE);
            }
            ps.setString(6, transaction.getStatus());
            ps.setInt(7, id);
            if(ps.executeUpdate()>0){
                return transaction;
            }
        }catch (SQLException e){
            System.err.println("SQLException in updateTransaction: " + e.getMessage());
            throw new RuntimeException("Database error in updateTransaction(): " + e.getMessage());

        }
        return null;
    }

    public boolean deleteTransactionById(int id){
        String sql = "DELETE FROM transactions WHERE transaction_id=?";
        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                return true;
            }
        }catch (SQLException e){
            System.err.println("SQLException in deleteTransactionById: " + e.getMessage());
            throw new RuntimeException("Database error in deleteTransactionById(): " + e.getMessage());
        }
        return false;
    }

    public List<Transaction> getTransactionsByStatus(String byStatus){
        String sql = "SELECT * FROM transactions WHERE status = ?";
        List<Transaction> transactions = new ArrayList<>();

        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, byStatus);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int transactionId = rs.getInt("transaction_id");
                int memberId = rs.getInt("member_id");
                int bookId = rs.getInt("book_id");
                Date dateOfIssue = rs.getDate("date_of_issue");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");
                String status = rs.getString("status");
                transactions.add(new Transaction(transactionId, memberId, bookId, dateOfIssue, dueDate, returnDate, status));
            }
            return transactions;
        }catch (SQLException e){
            System.err.println("SQLException in getTransactionsByStatus: " + e.getMessage());
            throw new RuntimeException("Database error in getTransactionsByStatus(): " + e.getMessage());
        }
    }







}
