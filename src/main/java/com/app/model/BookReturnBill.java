package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookReturnBill {
    private int billId;
    private int returnStatusId;
    private BigDecimal totalPaid;
    private BigDecimal penaltyAmount;
    private BigDecimal refundAmount;
    private String billStatus;
    private LocalDate billDate;

    public BookReturnBill(int billId, int returnStatusId, BigDecimal totalPaid, BigDecimal penaltyAmount, BigDecimal refundAmount, String billStatus, LocalDate billDate) {
        this.billId = billId;
        this.returnStatusId = returnStatusId;
        this.totalPaid = totalPaid;
        this.penaltyAmount = penaltyAmount;
        this.refundAmount = refundAmount;
        this.billStatus = billStatus;
        this.billDate = billDate;
    }

    public BookReturnBill(int returnStatusId, BigDecimal penaltyAmount, BigDecimal refundAmount, String billStatus, LocalDate billDate) {
        this.returnStatusId = returnStatusId;
        this.penaltyAmount = penaltyAmount;
        this.refundAmount = refundAmount;
        this.billStatus = billStatus;
        this.billDate = billDate;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getReturnStatusId() {
        return returnStatusId;
    }

    public void setReturnStatusId(int returnStatusId) {
        this.returnStatusId = returnStatusId;
    }

    public BigDecimal getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(BigDecimal totalPaid) {
        this.totalPaid = totalPaid;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public String getBillStatus() {
        return billStatus;
    }

    public void setBillStatus(String billStatus) {
        this.billStatus = billStatus;
    }

    public LocalDate getBillDate() {
        return billDate;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }
}
