package com.app.dao;

import com.app.model.Admin;
import com.app.model.Book;
import com.app.model.Transaction;
import com.app.model.TransactionDetailsDTO;
import com.app.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LibrarianDAO {
    private DBConnection db;
    public LibrarianDAO(DBConnection db) {
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

    public Transaction createTransaction (Transaction transaction){
        String sql = "insert into transactions(member_id, book_id, date_of_issue, due_date, return_date, status)  values(?,?,?,?,?,?)";

        try (Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, transaction.getMemberId());
            ps.setInt(2, transaction.getBookId());
            ps.setDate(3, new java.sql.Date(transaction.getDateOfIssue().getTime()));
            ps.setDate(4, new java.sql.Date(transaction.getDueDate().getTime()));

            if (transaction.getReturnDate() != null) {
                ps.setDate(5, new java.sql.Date(transaction.getReturnDate().getTime()));
            } else {
                ps.setNull(5, java.sql.Types.DATE);
            }

            ps.setString(6, transaction.getStatus());

            if (ps.executeUpdate() > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    int generatedId = rs.getInt(1); // First column of the first (and only) row
                    return getTransactionById(generatedId);
                }
            }

        }catch (SQLException e){
            System.out.println("createTransactionSQLException Error:" + e.getMessage());
        }
        return null;
    }

    public Transaction getTransactionById(int id){
        String sql = "select * from transactions where transaction_id=?";

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
            System.out.println("getTransactionByIdSQLException Error:" + e.getMessage());
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
            System.out.println("getTransactionDetailsByIdSQLException Error:" + e.getMessage());
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
            System.out.println("updateTransactionSQLException Error:" + e.getMessage());
        }
        return null;

    }

    public boolean deleteTransactionById(int id){
        String sql = "delete from transactions where transaction_id=?";
        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            if(ps.executeUpdate()>0){
                return true;
            }

        }catch (SQLException e){
            System.out.println("deleteTransactionByIdSQLException Error:" + e.getMessage());
        }
        return false;
    }

    public List<Transaction> getAllTransactionByMemberId(int id){
        String sql = "select * from transactions where member_id=?";
        List<Transaction> transactions = new ArrayList<>();

        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int transactionId = rs.getInt("transaction_id");
                int bookId = rs.getInt("book_id");
                int memberId = rs.getInt("member_id");
                Date dateOfIssue = rs.getDate("date_of_issue");
                Date dueDate = rs.getDate("due_date");
                Date returnDate = rs.getDate("return_date");
                String status = rs.getString("status");

                transactions.add(new Transaction(transactionId, memberId, bookId, dateOfIssue, dueDate, returnDate, status));
            }
            return transactions;
        }catch (SQLException e){
            System.out.println("getAllTransactionByMemberIdSQLException Error:" + e.getMessage());
        }
        return null;
    }






}
