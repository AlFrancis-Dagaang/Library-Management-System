package com.app.model;

import java.time.LocalDate;

public class BookTransaction {
    private int issueId;
    private int memberId;
    private int bookId;
    private LocalDate dateOfIssue;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private String status;
    private String bookType;

    public BookTransaction(int issueId, int member_id, int bookId, LocalDate dateOfIssue, LocalDate dateOfDue, LocalDate returnDate, String status, String bookType) {
        this.issueId = issueId;
        this.memberId = member_id;
        this.bookId = bookId;
        this.dateOfIssue = dateOfIssue;
        this.dueDate = dateOfDue;
        this.returnDate = returnDate;
        this.status = status;
        this.bookType = bookType;
    }

    public int getIssueId() {
        return issueId;
    }

    public void setIssueId(int issueId) {
        this.issueId = issueId;
    }

    public int getMember_id() {
        return memberId;
    }

    public void setMember_id(int member_id) {
        this.memberId = member_id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
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

    public String getBookType() {
        return bookType;
    }

    public void setBookType(String bookType) {
        this.bookType = bookType;
    }

    @Override
    public String toString() {
        return "BookIssueTransaction{" +
                "id=" + this.issueId +
                ", bookId=" + bookId +
                ", member_id=" + memberId +
                ", issueDate=" + (dateOfIssue != null ? dateOfIssue : "null") +
                ", dueDate=" + (dueDate != null ? dueDate : "null") +
                ", returnDate=" + (returnDate != null ? returnDate : "null") +
                ", bookType='" + bookType + '\'' +
                '}';
    }


}
