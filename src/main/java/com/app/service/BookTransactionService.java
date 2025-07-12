package com.app.service;

import com.app.config.PolicyConfig;
import com.app.dao.BookAgreementDAO;
import com.app.dao.BookDAO;
import com.app.dao.BookTransactionDAO;
import com.app.dao.MemberDAO;
import com.app.exception.ResourceNotFound;
import com.app.model.*;
import com.app.util.BookBankUtil;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookTransactionService {
    private final BookTransactionDAO bookTransactionDAO;
    private final BookAgreementDAO bookAgreementDAO;
    private final BookDAO bookDAO;
    private final MemberDAO memberDAO;

    public BookTransactionService(BookTransactionDAO bookTransactionDAO, BookDAO bookDAO, MemberDAO memberDAO, BookAgreementDAO bookAgreementDAO) {
        this.bookTransactionDAO = bookTransactionDAO;
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
        this.bookAgreementDAO = bookAgreementDAO;
    }

    public Object createTransaction(BookTransaction transaction) {
        Book book = this.bookDAO.getBookById(transaction.getBookId());
        Member member = this.memberDAO.getMemberByID(transaction.getMember_id());

        if("reference book".equalsIgnoreCase(transaction.getBookType())) {
            throw new IllegalArgumentException("Reference Book is not transactional");
        }

        if(!book.isAvailable()){
            throw new IllegalArgumentException("Book is not available");
        }

        if(member.getNumberOfBookIssued() >= member.getMaxBookLimit()){
            throw new IllegalArgumentException("Member book issue limit exceeded");
        }

        book.setAvailable(false);
        member.addBookIssued();

        LocalDate dueDate = PolicyConfig.calculateDueDate(book);

        if("general book".equalsIgnoreCase(book.getType())) {
            transaction.setDueDate(dueDate);
            transaction.setStatus("ISSUED");
            transaction.setBookType(PolicyConfig.getBookType(book));

            BookTransaction createdTransaction = this.bookTransactionDAO.createBookTransaction(transaction);

            if(createdTransaction != null) {
                //update the book availability and number of member issued
                this.bookDAO.updateBook(book, book.getBookId());
                this.memberDAO.updateMember(member, member.getMemberId());
                return createdTransaction;
            }else{
                throw new RuntimeException("Something went wrong while creating transaction for general book");
            }

        }else if ("book bank".equalsIgnoreCase(book.getType())) {
            transaction.setDueDate(dueDate);
            transaction.setStatus("PENDING");
            transaction.setBookType(PolicyConfig.getBookType(book));
            BookTransaction createdTransaction = this.bookTransactionDAO.createBookTransaction(transaction);

            if(createdTransaction != null) {
                //update the book availability and number of member issued
                this.bookDAO.updateBook(book, book.getBookId());
                this.memberDAO.updateMember(member, member.getMemberId());
            }else{
                throw new RuntimeException("Something went wrong while creating transaction for book bank");
            }

            int transactionId = createdTransaction.getIssueId();
            BigDecimal bookPrice = book.getPrice();
            BigDecimal serviceFee = BookBankUtil.calculateServiceFee(bookPrice);
            BigDecimal totalAmount = bookPrice.add(serviceFee);
            LocalDate agreementDate = LocalDate.now();
            boolean active = false;
            BookAgreement bookAgreement = new BookAgreement(transactionId, bookPrice, serviceFee, totalAmount, agreementDate, active );

            return this.bookAgreementDAO.createBookAgreement(bookAgreement);
        }else{
            throw new RuntimeException("Book type is not known");
        }
    }

    public BookTransaction getTransactionById(int transactionId) {
        BookTransaction getTransaction = this.bookTransactionDAO.getIssueTransactionById(transactionId);

        if(getTransaction == null) {
            throw new ResourceNotFound("Transaction not found");
        }
        return getTransaction;
    }

    public List<BookTransaction> getAllTransactions() {
        List<BookTransaction> transactions = this.bookTransactionDAO.getAllBookTransactions();

        if(transactions == null) {
            throw new ResourceNotFound("Transactions are empty");
        }

        return transactions;

    }

    public BookTransactionAgreementDetailsDTO completeBookTransactionAgreement(int transactionId) {

        boolean successCompletingTheAgreement = this.bookTransactionDAO.completeTheBookAgreement(transactionId);

        if(!successCompletingTheAgreement) {
            throw new RuntimeException("Something went wrong while completing the book agreement");
        }
        return this.bookTransactionDAO.getBookTransactionAgreementDetails(transactionId);
    }

    public BookTransactionDetailsDTO getBookTransactionDetails(int transactionId) {
        BookTransactionDetailsDTO transactionDetails = this.bookTransactionDAO.getBookTransactionDetails(transactionId);

        if(transactionDetails == null) {
            throw new ResourceNotFound("Transaction not found");
        }

        return transactionDetails;
    }

    public BookTransactionAgreementDetailsDTO getBookTransactionAgreementDetails(int transactionId) {
        BookTransactionAgreementDetailsDTO agreementDetails = this.bookTransactionDAO.getBookTransactionAgreementDetails(transactionId);
        if(agreementDetails == null) {
            throw new ResourceNotFound("Transaction not found");
        }
        return agreementDetails;
    }

    public BookTransaction cancelBookAgreement(int transactionId, int agreementId) {
        boolean successfullyDeletedAgreement = this.bookAgreementDAO.deleteBookAgreement(agreementId);
        if (!successfullyDeletedAgreement) {
            throw new RuntimeException("Something went wrong while deleting the book agreement");
        }
        return this.bookTransactionDAO.cancelBookTransaction(transactionId);
    }

    public BookAgreement getBookAgreementByTransactionId(int transactionId) {
        BookAgreement agreement = this.bookAgreementDAO.getBookAgreementByTransactionId(transactionId);

        if(agreement == null) {
            throw new ResourceNotFound("Agreement not found");
        }

        return agreement;
    }


}
