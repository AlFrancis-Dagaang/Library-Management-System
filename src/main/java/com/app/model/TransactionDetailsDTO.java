package com.app.model;

import java.math.BigDecimal;
import java.util.Date;

public class TransactionDetailsDTO {
    private int transactionId;
    private String memberName;
    private String bookTitle;
    private BigDecimal bookPrice;
    private Date dateOfIssue;
    private Date dueDate;
    private Date returnDate;
    private String status;

    public TransactionDetailsDTO(){}
    public TransactionDetailsDTO(int transactionId, String memberName, BigDecimal bookPrice, String bookTitle, Date dateOfIssue, Date dueDate, Date returnDate, String status) {
        this.transactionId = transactionId;
        this.memberName = memberName;
        this.bookPrice = bookPrice;
        this.bookTitle = bookTitle;
        this.dateOfIssue = dateOfIssue;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(BigDecimal bookPrice) {
        this.bookPrice = bookPrice;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}


