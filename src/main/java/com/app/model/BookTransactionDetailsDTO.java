package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookTransactionDetailsDTO {
    private int transactionId;
    private int memberId;
    private int bookId;
    private String name;
    private String memberType;
    private String bookTitle;
    private LocalDate dateOfIssue;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private BigDecimal bookPrice;
    private String bookType;
    private String status;

    public BookTransactionDetailsDTO() {}

    public BookTransactionDetailsDTO(int transactionId, int memberId, int bookId, String name,
                                     String memberType, String bookTitle, LocalDate dateOfIssue,
                                     LocalDate dueDate,LocalDate returnDate,  BigDecimal bookPrice,
                                     String bookType, String status) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookId = bookId;
        this.name = name;
        this.dateOfIssue = dateOfIssue;
        this.bookTitle = bookTitle;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.bookPrice = bookPrice;
        this.status = status;
        this.bookType = bookType;
        this.memberType = memberType;
    }

    public LocalDate getReturnDate(){
        return returnDate;
    }

    public String getMemberType() {
        return memberType;
    }

    public int getBookId() {
        return bookId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookType() {
        return bookType;
    }

    public String getStatus() {
        return status;
    }
}
