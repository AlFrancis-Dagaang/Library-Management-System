package com.app.dao;

import com.app.model.BookReturnBill;
import com.app.util.DBConnection;
import com.app.util.LocalDateUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;

public class BookReturnBillDAO {
    private DBConnection db;
    public BookReturnBillDAO(DBConnection db) {
        this.db = db;
    }

    public BookReturnBill createBookReturnBill(BookReturnBill returnBill) {
        String sql = "INSERT INTO book_return_bill(return_status_id, total_paid," +
                "penalty_amount, refund_amount, status, bill_date) VALUES (?,?,?,?,?,?)";

        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, returnBill.getReturnStatusId());
            ps.setBigDecimal(2, returnBill.getTotalPaid());
            ps.setBigDecimal(3, returnBill.getPenaltyAmount());
            ps.setBigDecimal(4, returnBill.getRefundAmount());
            ps.setString(5, returnBill.getBillStatus());
            ps.setDate(6, java.sql.Date.valueOf(returnBill.getBillDate()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                return getBookReturnBillById(id);
            }else{
                throw new SQLException("Book Return Bill Creation Failed");
            }
        }catch (SQLException e){
            System.err.println("SQLException in BookReturnBillDAO: " + e.getMessage());
            throw new RuntimeException("SQLException in BookReturnBillDAO: " + e.getMessage());
        }

    }

    public BookReturnBill getBookReturnBillById(int id) {
        String sql = "SELECT * FROM book_return_bill WHERE bill_id = ?";

        try(Connection con = this.db.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int billId = rs.getInt("bill_id");
                int returnStatusId = rs.getInt("return_status_id");
                BigDecimal totalPaid = rs.getBigDecimal("total_paid");
                BigDecimal penaltyAmount = rs.getBigDecimal("penalty_amount");
                BigDecimal refundAmount = rs.getBigDecimal("refund_amount");
                String billStatus = rs.getString("status");
                LocalDate billDate = LocalDateUtil.getNullableLocalDate(rs, "bill_date");

                return new BookReturnBill(billId, returnStatusId, totalPaid, penaltyAmount, refundAmount, billStatus, billDate);

            }


        }catch (SQLException e){
            System.err.println("SQLException in BookReturnBillDAO: " + e.getMessage());
            throw new RuntimeException("SQLException in BookReturnBillDAO: " + e.getMessage());
        }

        return null;
    }







}
