package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

public class Bill {
    private int billId;
    private LocalDate date;
    private int memberId;
    private int transactionId;
    private BigDecimal amount;
    private String status;

    public Bill(){}
    public Bill( LocalDate date, int memberId, int transactionId, BigDecimal amount, String status) {
        this.date = date;
        this.memberId = memberId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.status = status;
    }
    public Bill(int billId, LocalDate date, int memberId, int transactionId, BigDecimal amount, String status) {
        this.billId = billId;
        this.date = date;
        this.memberId = memberId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.status = status;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
