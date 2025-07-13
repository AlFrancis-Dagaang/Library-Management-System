package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookReturnStatus {
    private int returnStatusId;
    private int transactionId;
    private int agreementId;
    private String returnType;
    private LocalDate returnDate;
    private String bookCondition;
    private String returnTimeline;
    private BigDecimal penaltyAmount;
    private BigDecimal refundAmount;
    private String librarianNotes;

    public BookReturnStatus(int returnStatusId, int transactionId, int agreementId, String returnType, LocalDate returnDate, String bookCondition, String returnTimeline, BigDecimal penaltyAmount, BigDecimal refundAmount, String librarianNotes) {
        this.returnStatusId = returnStatusId;
        this.transactionId = transactionId;
        this.agreementId = agreementId;
        this.returnType = returnType;
        this.returnDate = returnDate;
        this.bookCondition = bookCondition;
        this.returnTimeline = returnTimeline;
        this.penaltyAmount = penaltyAmount;
        this.refundAmount = refundAmount;
        this.librarianNotes = librarianNotes;
    }

    public BookReturnStatus( int transactionId, int agreementId,LocalDate returnDate, String bookCondition, String returnTimeline, String librarianNotes) {
        this.returnDate = returnDate;
        this.bookCondition = bookCondition;
        this.returnTimeline = returnTimeline;
        this.librarianNotes = librarianNotes;
        this.transactionId = transactionId;
        this.agreementId = agreementId;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setReturnTimeline(String returnTimeline) {
        this.returnTimeline = returnTimeline;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public void setPenaltyAmount(BigDecimal penaltyAmount) {
        this.penaltyAmount = penaltyAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public int getReturnStatusId() {
        return returnStatusId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getAgreementId() {
        return agreementId;
    }

    public String getReturnType() {
        return returnType;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public String getBookCondition() {
        return bookCondition;
    }

    public String getReturnTimeline() {
        return returnTimeline;
    }

    public BigDecimal getPenaltyAmount() {
        return penaltyAmount;
    }

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public String getLibrarianNotes() {
        return librarianNotes;
    }
}
