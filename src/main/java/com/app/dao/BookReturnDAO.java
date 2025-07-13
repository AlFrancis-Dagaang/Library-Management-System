package com.app.dao;

import com.app.model.BookReturnStatus;
import com.app.util.DBConnection;
import com.app.util.LocalDateUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class BookReturnDAO {
    private DBConnection db;
    public BookReturnDAO(BookAgreementDAO bookAgreementDAO) {
        db = new DBConnection();
    }


    public BookReturnStatus createBookReturnStatus(BookReturnStatus bookReturnStatus) {
        String sql = "INSERT INTO book_return_status (transaction_id, agreement_id, return_type, return_date, book_condition," +
                "return_timeline, penalty_amount, refund_amount, librarian_notes) VALUES (?,?,?,?,?,?,?,?,?)";

        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bookReturnStatus.getTransactionId());
            ps.setInt(2, bookReturnStatus.getAgreementId());
            ps.setString(3, bookReturnStatus.getReturnType());
            ps.setDate(4, java.sql.Date.valueOf(bookReturnStatus.getReturnDate()));
            ps.setString(5, bookReturnStatus.getBookCondition());
            ps.setString(6, bookReturnStatus.getReturnTimeline());
            ps.setBigDecimal(7, bookReturnStatus.getPenaltyAmount());
            ps.setBigDecimal(8, bookReturnStatus.getRefundAmount());
            ps.setString(9, bookReturnStatus.getLibrarianNotes());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int returnId = rs.getInt(1);
                return getBookReturnStatus(returnId);
            }else{
                throw new SQLException("Failed to create book return status");
            }
        }catch (SQLException e){
            System.err.println("SQLException caught in createBookReturnStatus(): " + e.getMessage());
            throw new RuntimeException("SQLException caught in createBookReturnStatus(): " + e.getMessage());
        }
    }
    public BookReturnStatus getBookReturnStatus(int returnId) {
        String sql = "SELECT * FROM book_return_status WHERE return_status_id=?";

        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, returnId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int returnStatusId = rs.getInt("return_status_id");
                int transactionId = rs.getInt("transaction_id");
                int agreementId = rs.getInt("agreement_id");
                String returnType = rs.getString("return_type");
                LocalDate returnDate = LocalDateUtil.getNullableLocalDate(rs, "return_date");
                String bookCondition = rs.getString("book_condition");
                String returnTimeline = rs.getString("return_timeline");
                BigDecimal penaltyAmount = rs.getBigDecimal("penalty_amount");
                BigDecimal refundAmount = rs.getBigDecimal("refund_amount");
                String librarianNotes = rs.getString("librarian_notes");

                return new BookReturnStatus(returnStatusId, transactionId, agreementId, returnType, returnDate, bookCondition, returnTimeline, penaltyAmount, refundAmount, librarianNotes);
            }


        }catch (SQLException e){
            System.err.println("SQLException caught in getBookReturnStatus(): " + e.getMessage());
            throw new RuntimeException("SQLException caught in getBookReturnStatus(): " + e.getMessage());
        }
        return null;
    }


}
