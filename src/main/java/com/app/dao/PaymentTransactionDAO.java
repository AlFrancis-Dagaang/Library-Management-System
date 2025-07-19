package com.app.dao;

import com.app.model.PaymentTransaction;
import com.app.util.DBConnection;
import com.app.util.LocalDateUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentTransactionDAO {
    private DBConnection dbConnection;

    public PaymentTransactionDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public PaymentTransaction createPaymentTransaction(PaymentTransaction paymentTransaction) {
        String sql = "INSERT INTO payment_transactions (bill_id, transaction_type, amount, status, payment_method, payment_date)" +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, paymentTransaction.getBillId());
            ps.setString(2, paymentTransaction.getTransactionType());
            ps.setBigDecimal(3, paymentTransaction.getAmount());
            ps.setString(4, paymentTransaction.getStatus());
            ps.setString(5, paymentTransaction.getPaymentMethod());
            ps.setDate(6, java.sql.Date.valueOf(paymentTransaction.getPaymentDate()));
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                return getPaymentTransactionById(id);
            }else{
                throw new RuntimeException("Failed to create payment transaction");
            }
        }catch (SQLException e){
            System.err.println("SQLException caught in createPaymentTransaction: " + e.getMessage());
            throw new RuntimeException("SQLException caught in createPaymentTransaction: " + e.getMessage());
        }
    }


    public PaymentTransaction getPaymentTransactionById(int paymentId) {
        String sql = "SELECT * FROM payment_transactions WHERE payment_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, paymentId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("payment_id");
                int billId = rs.getInt("bill_id");
                String transactionType = rs.getString("transaction_type");
                BigDecimal amount = rs.getBigDecimal("amount");
                String status = rs.getString("status");
                String paymentMethod = rs.getString("payment_method");
                LocalDate paymentDate = LocalDateUtil.getNullableLocalDate(rs, "payment_date");

                return new PaymentTransaction(id, billId, transactionType, amount, status, paymentMethod, paymentDate);
            }

        }catch (SQLException e){
            System.err.println("SQLException caught in getPaymentTransactionById: " + e.getMessage());
            throw new RuntimeException("SQLException caught in getPaymentTransactionById: " + e.getMessage());
        }
        return null;
    }

    public List<PaymentTransaction> getPaymentTransactionsByBillId(int billId) {
        String sql = "SELECT * FROM payment_transactions WHERE bill_id = ?";
        List<PaymentTransaction> paymentTransactions = new ArrayList<PaymentTransaction>();
        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("payment_id");
                int billID = rs.getInt("bill_id");
                String transactionType = rs.getString("transaction_type");
                BigDecimal amount = rs.getBigDecimal("amount");
                String status = rs.getString("status");
                String paymentMethod = rs.getString("payment_method");
                LocalDate paymentDate = LocalDateUtil.getNullableLocalDate(rs, "payment_date");

                paymentTransactions.add(new PaymentTransaction(id, billID, transactionType, amount, status, paymentMethod, paymentDate));
            }

            return paymentTransactions;
        }catch (SQLException e){
            System.err.println("SQLException caught in getPaymentTransactionsByBillId: " + e.getMessage());
            throw new RuntimeException("SQLException caught in getPaymentTransactionsByBillId: " + e.getMessage());
        }
    }
}
