package com.app.model;

import java.time.LocalDate;
import java.util.Date;

public class Transaction {
    private int transactionId;
    private int memberId;
    private int bookId;
    private LocalDate dateOfIssue;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;

    public Transaction() {}
    public Transaction(int transactionId, int memberId, int bookId, LocalDate dateOfIssue, LocalDate dateOfDue, LocalDate returnDate, String status) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.dateOfIssue = dateOfIssue;
        this.returnDate = returnDate;
        this.dueDate = dateOfDue;
        this.status = status;
    }
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(LocalDate dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
