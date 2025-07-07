package com.app.dao;

import com.app.model.Bill;
import com.app.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BillDAO {
    private DBConnection dbConnection;
    public BillDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Bill getBillById(int billId){
        String sql = "SELECT * FROM bills WHERE bill_id = ?";
        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int id = rs.getInt("bill_id");
                LocalDate date = rs.getDate("date").toLocalDate();
                int memberId = rs.getInt("member_id");
                int transactionId = rs.getInt("transaction_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                String status = rs.getString("status");

                return new Bill(id, date, memberId, transactionId, amount, status);
            }

        }catch (SQLException e){
            System.err.println("SQLException in getBillById: " + e.getMessage());
            throw new RuntimeException("Database error in getBillById: " + e.getMessage());
        }
        return null;
    }

    public Bill createBil (Bill bill){
        String sql = "INSERT INTO bills (date, member_id, transaction_id, amount, status) VALUES (?, ?, ?, ?, ?)";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(bill.getDate()));
            ps.setInt(2, bill.getMemberId());
            ps.setInt(3, bill.getTransactionId());
            ps.setBigDecimal(4, bill.getAmount());
            ps.setString(5, bill.getStatus());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if(rs.next()){
                int id = rs.getInt(1);
                return getBillById(id);
            }else {
                throw new SQLException("Failed to insert bill");
            }

        }catch (SQLException e){
            System.err.println("SQLException in createBil: " + e.getMessage());
            throw new RuntimeException("Database error in createBil: " + e.getMessage());
        }
    }

    public Bill getBillByTransactionId(int billId) {
        String sql = "SELECT * FROM bills WHERE transaction_id = ?";
        try (Connection con = this.dbConnection.getConnection()) {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("bill_id");
                LocalDate date = rs.getDate("date").toLocalDate();
                int memberId = rs.getInt("member_id");
                int transactionId = rs.getInt("transaction_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                String status = rs.getString("status");

                return new Bill(id, date, memberId, transactionId, amount, status);
            }
        } catch (SQLException e) {
            System.err.println("SQLException in getBillByTransactionId: " + e.getMessage());
            throw new RuntimeException("Database error in getBillByTransactionId: " + e.getMessage());
        }
        return null;
    }

    public Bill payFine(int billId){
        String sql = "UPDATE bills SET status = ? WHERE bill_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "paid");
            ps.setInt(2, billId);

            if(ps.executeUpdate() > 0){
                return getBillById(billId);
            }else{
                throw new SQLException("Failed to pay fines");
            }

        }catch (SQLException e){
            System.err.println("SQLException in payFine: " + e.getMessage());
            throw new RuntimeException("Database error in payFine: " + e.getMessage());
        }

    }

    public List<Bill> getAllBills (){
        String sql = "SELECT * FROM bills";
        List<Bill> billList = new ArrayList<Bill>();

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int id = rs.getInt("bill_id");
                LocalDate date = rs.getDate("date").toLocalDate();
                int memberId = rs.getInt("member_id");
                int transactionId = rs.getInt("transaction_id");
                BigDecimal amount = rs.getBigDecimal("amount");
                String status = rs.getString("status");
                billList.add(new Bill(id, date, memberId, transactionId, amount, status));
            }
            return billList;
        }catch (SQLException e){
            System.err.println("SQLException in getAllBills: " + e.getMessage());
            throw new RuntimeException("Database error in getAllBills: " + e.getMessage());
        }
    }

    public boolean deleteBill(int billId){
        String sql = "DELETE FROM bills WHERE bill_id = ?";
        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, billId);
            return ps.executeUpdate() > 0;
        }catch (SQLException e){
            System.err.println("SQLException in deleteBill: " + e.getMessage());
            throw new RuntimeException("Database error in deleteBill: " + e.getMessage());
        }
    }

}
