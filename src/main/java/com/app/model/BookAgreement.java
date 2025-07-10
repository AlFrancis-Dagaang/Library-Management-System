package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookAgreement {
    private int agreementId;
    private int transactionId;
    private BigDecimal bookPrice;
    private BigDecimal serviceFee;
    private BigDecimal totalAmount;
    private LocalDate agreementDate;
    private boolean active;
    public BookAgreement() {}
    public BookAgreement(int agreementId, int transactionId, BigDecimal bookPrice, BigDecimal serviceFee, BigDecimal totalAmount, LocalDate agreementDate, boolean active) {
        this.agreementId = agreementId;
        this.transactionId = transactionId;
        this.bookPrice = bookPrice;
        this.serviceFee = serviceFee;
        this.totalAmount = totalAmount;
        this.agreementDate = agreementDate;
        this.active = active;
    }
    public BookAgreement( int transactionId, BigDecimal bookPrice, BigDecimal serviceFee, BigDecimal totalAmount, LocalDate agreementDate, boolean active) {
        this.transactionId = transactionId;
        this.bookPrice = bookPrice;
        this.serviceFee = serviceFee;
        this.totalAmount = totalAmount;
        this.agreementDate = agreementDate;
        this.active = active;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public void setAgreementId(int agreementId) {
        this.agreementId = agreementId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public LocalDate getAgreementDate() {
        return agreementDate;
    }

    public void setAgreementDate(LocalDate agreementDate) {
        this.agreementDate = agreementDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

