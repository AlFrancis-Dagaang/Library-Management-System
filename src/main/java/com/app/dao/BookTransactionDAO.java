package com.app.dao;

import com.app.model.BookAgreement;
import com.app.model.BookTransaction;
import com.app.model.BookTransactionAgreementDetailsDTO;
import com.app.model.BookTransactionDetailsDTO;
import com.app.util.DBConnection;
import com.app.util.LocalDateUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

    public List<BookTransaction> getAllBookTransactions() {
        String sql = "SELECT * FROM book_transactions";
        List<BookTransaction> bookTransactions = new ArrayList<BookTransaction>();

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int issueId = rs.getInt("transaction_id");
                int memberId = rs.getInt("member_id");
                int bookId = rs.getInt("book_id");
                LocalDate dateOfIssue = LocalDateUtil.getNullableLocalDate(rs, "date_of_issue");
                LocalDate dueDate = LocalDateUtil.getNullableLocalDate(rs, "due_date");
                LocalDate returnDate = LocalDateUtil.getNullableLocalDate(rs, "return_date");
                String status = rs.getString("status");
                String bookType = rs.getString("book_type");

                bookTransactions.add(new  BookTransaction(issueId,memberId, bookId, dateOfIssue, dueDate, returnDate, status, bookType));
            }
            return bookTransactions;

        }catch (SQLException e){
            System.err.println("Error getting book transactions: " + e.getMessage());
            throw new RuntimeException("Error getting book transactions: " + e.getMessage());
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

    public boolean completeTheBookAgreement(int transactionId) {
        String transactionSql = "UPDATE book_transactions t\n" +
                "JOIN book_agreement a ON t.transaction_id = a.transaction_id\n" +
                "SET \n" +
                "  t.status = 'ISSUED',\n" +
                "  a.active = TRUE\n" +
                "WHERE t.transaction_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(transactionSql);
            ps.setInt(1, transactionId);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            System.err.println("Error in completeTheBookAgreement: " + e.getMessage());
            throw new RuntimeException("Error in completeTheBookAgreement: " + e.getMessage());
        }
    }

    public BookTransactionDetailsDTO getBookTransactionDetails(int transactionId) {
        String sql = "SELECT t.transaction_id, m.member_id, b.book_id, m.name, m.type AS member_type, b.title, t.date_of_issue, \n" +
                "t.due_date,t.return_date, b.price, b.type AS  book_type, t.status\n" +
                "FROM book_transactions t \n" +
                "JOIN members m ON t.member_id = m.member_id\n" +
                "JOIN books b ON t.book_id = b.book_id\n" +
                "WHERE t.transaction_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int transId = rs.getInt("transaction_id");
                int memberId = rs.getInt("member_id");
                int bookId = rs.getInt("book_id");
                String name = rs.getString("name");
                String memberType = rs.getString("member_type");
                String title = rs.getString("title");
                LocalDate dateOfIssue = LocalDateUtil.getNullableLocalDate(rs, "date_of_issue");
                LocalDate dueDate = LocalDateUtil.getNullableLocalDate(rs, "due_date");
                LocalDate returnDate = LocalDateUtil.getNullableLocalDate(rs, "return_date");
                BigDecimal price = BigDecimal.valueOf(rs.getDouble("price"));
                String bookType = rs.getString("book_type");
                String status = rs.getString("status");

                return new BookTransactionDetailsDTO(transId, memberId, bookId, name, memberType,
                       title, dateOfIssue, dueDate, returnDate, price, bookType, status);

            }

        }catch (SQLException e){
            System.err.println("Error in getBookTransactionDetails: " + e.getMessage());
            throw new RuntimeException("Error in getBookTransactionDetails: " + e.getMessage());
        }

        return null;
    }

    public BookTransactionAgreementDetailsDTO getBookTransactionAgreementDetails(int transactionId) {
        String sql = "SELECT t.transaction_id, m.member_id, b.book_id,a.agreement_id, m.name ,m.type AS member_type, b.title, t.date_of_issue, t.due_date,t.return_date, a.agreement_date, b.price, a.service_fee, a.total_amount, b.type AS book_type, t.status, a.active\n" +
                "FROM book_transactions t \n" +
                "JOIN members m ON t.member_id = m.member_id\n" +
                "JOIN books b ON t.book_id = b.book_id\n" +
                "JOIN book_agreement a ON t.transaction_id = a.transaction_id\n" +
                "WHERE t.transaction_id = ?";

        try (Connection con = this.dbConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int transId = rs.getInt("transaction_id");
                int memberId = rs.getInt("member_id");
                int bookId = rs.getInt("book_id");
                int agreementId = rs.getInt("agreement_id");
                String name = rs.getString("name");
                String memberType = rs.getString("member_type");
                String title = rs.getString("title");
                LocalDate dateOfIssue = LocalDateUtil.getNullableLocalDate(rs, "date_of_issue");
                LocalDate dueDate = LocalDateUtil.getNullableLocalDate(rs, "due_date");
                LocalDate returnDate = LocalDateUtil.getNullableLocalDate(rs, "return_date");
                LocalDate agreementDate = LocalDateUtil.getNullableLocalDate(rs, "agreement_date");
                BigDecimal price = BigDecimal.valueOf(rs.getDouble("price"));
                BigDecimal serviceFee = BigDecimal.valueOf(rs.getDouble("service_fee"));
                BigDecimal totalAmount = BigDecimal.valueOf(rs.getDouble("total_amount"));
                String bookType = rs.getString("book_type");
                String status = rs.getString("status");
                boolean active = rs.getBoolean("active");

                return new BookTransactionAgreementDetailsDTO(transId, memberId, bookId, agreementId, name, memberType, title, dateOfIssue, dueDate, returnDate, agreementDate, price, serviceFee, totalAmount, bookType, status, active);

            }

        } catch (SQLException e) {
            System.err.println("Error in getBookTransactionAgreementDetails: " + e.getMessage());
            throw new RuntimeException("Error in getBookTransactionAgreementDetails: " + e.getMessage());
        }

        return null;
    }

    public BookTransaction cancelBookTransaction(int transactionId) {
        String sql = "UPDATE book_transactions SET status ='CANCELLED' WHERE transaction_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            if(ps.executeUpdate() > 0){
                return this.getIssueTransactionById(transactionId);
            }else{
                throw new RuntimeException("Error in cancelBookTransaction: " + transactionId);
            }
        }catch (SQLException e){
            System.err.println("Error in cancelBookTransaction: " + e.getMessage());
            throw new RuntimeException("Error in cancelBookTransaction: " + e.getMessage());
        }
    }


    public BookTransaction updateBookTransaction (BookTransaction transaction, int transactionId) {
        String sql = "UPDATE book_transactions SET return_date = ?, status = ? WHERE transaction_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setDate(1, java.sql.Date.valueOf(transaction.getReturnDate()));
            ps.setString(2, transaction.getStatus());
            ps.setInt(3, transactionId);
            if(ps.executeUpdate() > 0){
                return this.getIssueTransactionById(transactionId);
            }else {
                throw new RuntimeException("Error in updateBookTransaction: " + transactionId);
            }
        }catch (SQLException e){
            System.err.println("Error in updateBookTransaction: " + e.getMessage());
            throw new RuntimeException("Error in updateBookTransaction: " + e.getMessage());
        }
    }



}
