package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookReturnStatus {
    private int returnStatusId;
    private int transactionId;
    private String returnType;
    private LocalDate returnDate;
    private String bookCondition;
    private String returnTimeline;
    private BigDecimal penaltyAmount;
    private BigDecimal refundAmount;
    private String librarianNotes;

    public BookReturnStatus() {}

    public BookReturnStatus(int returnStatusId, int transactionId, String returnType, LocalDate returnDate, String bookCondition, String returnTimeline, BigDecimal penaltyAmount, BigDecimal refundAmount, String librarianNotes) {
        this.returnStatusId = returnStatusId;
        this.transactionId = transactionId;
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

    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public void setReturnStatusId(int returnStatusId) {
        this.returnStatusId = returnStatusId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }


    public void setBookCondition(String bookCondition) {
        this.bookCondition = bookCondition;
    }

    public void setLibrarianNotes(String librarianNotes) {
        this.librarianNotes = librarianNotes;
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
    @Override
    public String toString() {
        return "BookReturnStatus{" +
                "returnStatusId=" + returnStatusId +
                ", transactionId=" + transactionId +
                ", returnType='" + returnType + '\'' +
                ", returnDate=" + returnDate +
                ", bookCondition='" + bookCondition + '\'' +
                ", returnTimeline='" + returnTimeline + '\'' +
                ", penaltyAmount=" + penaltyAmount +
                ", refundAmount=" + refundAmount +
                ", librarianNotes='" + librarianNotes + '\'' +
                '}';
    }

}
