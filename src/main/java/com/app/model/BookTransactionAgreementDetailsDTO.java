package com.app.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookTransactionAgreementDetailsDTO {
    private int transactionId;
    private int memberId;
    private int bookId;
    private int agreementId;
    private String name;
    private String memberType;
    private String bookTitle;
    private LocalDate dateOfIssue;
    private LocalDate dueDate;
    private LocalDate returnDate;
    private LocalDate agreementDate;
    private BigDecimal bookPrice;
    private BigDecimal serviceFee;
    private BigDecimal totalAmount;
    private String bookType;
    private String status;
    private boolean active;

    public BookTransactionAgreementDetailsDTO() {}

    public BookTransactionAgreementDetailsDTO(int transactionId,int memberId, int bookId, int agreementId, String name,String memberType, String bookTitle, LocalDate dateOfIssue, LocalDate dueDate,LocalDate returnDate, LocalDate agreementDate, BigDecimal bookPrice, BigDecimal serviceFee, BigDecimal totalAmount, String bookType, String status, boolean active) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.returnDate = returnDate;
        this.transactionId = transactionId;
        this.agreementId = agreementId;
        this.name = name;
        this.memberType = memberType;
        this.bookTitle = bookTitle;
        this.dateOfIssue = dateOfIssue;
        this.dueDate = dueDate;
        this.agreementDate = agreementDate;
        this.bookPrice = bookPrice;
        this.serviceFee = serviceFee;
        this.totalAmount = totalAmount;
        this.bookType = bookType;
        this.status = status;
        this.active = active;
    }
    public LocalDate getReturnDate(){
        return returnDate;
    }

    public int getAgreementId(){
        return agreementId;
    }

    public String getMemberType(){
        return memberType;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public int getMemberId() {
        return memberId;
    }

    public int getBookId() {
        return bookId;
    }

    public String getName() {
        return name;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public LocalDate getDateOfIssue() {
        return dateOfIssue;
    }

    public LocalDate getAgreementDate() {
        return agreementDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public BigDecimal getBookPrice() {
        return bookPrice;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public String getBookType() {
        return bookType;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public boolean getActive() {
        return active;
    }
}

