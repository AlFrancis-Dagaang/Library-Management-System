package com.app.dao;

import com.app.model.BookAgreement;
import com.app.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class BookAgreementDAO {
    private DBConnection dbConnection;

    public BookAgreementDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public BookAgreement getBookAgreementById(int bookAgreementId) {
        String sql ="SELECT * FROM book_agreement WHERE agreement_id=?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookAgreementId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int agreementId = rs.getInt("agreement_id");
                int trasactionId = rs.getInt("transaction_id");
                BigDecimal bookPrice = rs.getBigDecimal("book_price");
                BigDecimal serviceFee = rs.getBigDecimal("service_fee");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                LocalDate agreementDate = LocalDate.parse(rs.getString("agreement_date"));
                boolean active  = rs.getBoolean("active");

                return new BookAgreement(agreementId, trasactionId, bookPrice, serviceFee, totalAmount, agreementDate, active);
            }
        }catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
            throw new RuntimeException("SQLException: " + e.getMessage());
        }
        return null;
    }
    public BookAgreement createBookAgreement(BookAgreement bookAgreement) {
        String sql = "INSERT INTO book_agreement (transaction_id, book_price, service_fee, total_amount, agreement_date, active) VALUES (?,?,?,?,?,?)";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, bookAgreement.getTransactionId());
            ps.setBigDecimal(2, bookAgreement.getBookPrice());
            ps.setBigDecimal(3, bookAgreement.getServiceFee());
            ps.setBigDecimal(4, bookAgreement.getTotalAmount());
            ps.setDate(5, java.sql.Date.valueOf(bookAgreement.getAgreementDate()));
            ps.setBoolean(6, bookAgreement.isActive());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()) {
                int id = rs.getInt(1);
                return getBookAgreementById(id);
            }else{
                throw new RuntimeException("Create BookAgreement failed");
            }

        }catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
            throw new RuntimeException("SQLException: " + e.getMessage());
        }
    }

    public boolean deleteBookAgreement(int transactionId) {
        String sql = "DELETE FROM book_agreement WHERE transaction_id=?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
            throw new RuntimeException("SQLException: " + e.getMessage());
        }
    }

    public BookAgreement getBookAgreementByTransactionId(int transactionId) {
        String sql = "SELECT * FROM book_agreement WHERE transaction_id=?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, transactionId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                int agreementId = rs.getInt("agreement_id");
                int transId = rs.getInt("transaction_id");
                BigDecimal bookPrice = rs.getBigDecimal("book_price");
                BigDecimal serviceFee = rs.getBigDecimal("service_fee");
                BigDecimal totalAmount = rs.getBigDecimal("total_amount");
                LocalDate agreementDate = LocalDate.parse(rs.getString("agreement_date"));
                boolean active  = rs.getBoolean("active");

                return new BookAgreement(agreementId, transId, bookPrice, serviceFee, totalAmount, agreementDate, active);
            }
        }catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
            throw new RuntimeException("SQLException: " + e.getMessage());
        }
        return null;
    }

    public BookAgreement updateBookAgreement(BookAgreement bookAgreement, int agreementId) {
        String sql = "UPDATE book_agreement SET active = ? WHERE agreement_id=?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, bookAgreement.isActive());
            ps.setInt(2, agreementId);
            if(ps.executeUpdate() > 0){
                return getBookAgreementById(agreementId);
            }
        }catch (SQLException e){
            System.err.println("SQLException: " + e.getMessage());
            throw new RuntimeException("SQLException: " + e.getMessage());
        }

        return null;
    }
}
