package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentTransaction {
    private int paymentId;
    private int billId;
    private String transactionType;
    private BigDecimal amount;
    private String status;
    private String paymentMethod;
    private LocalDate paymentDate;

    public PaymentTransaction(int paymentId, int billId, String transactionType, BigDecimal amount, String status, String paymentMethod, LocalDate paymentDate) {
        this.paymentId = paymentId;
        this.billId = billId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public PaymentTransaction(int billId, String transactionType, BigDecimal amount, String status, String paymentMethod, LocalDate paymentDate) {
        this.billId = billId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.paymentDate = paymentDate;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }
}
