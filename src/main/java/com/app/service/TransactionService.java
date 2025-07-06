package com.app.service;

import com.app.config.LoanPolicyConfig;
import com.app.dao.BillDAO;
import com.app.dao.BookDAO;
import com.app.dao.TransactionDAO;
import com.app.dao.MemberDAO;
import com.app.exception.TransactionNotFoundException;
import com.app.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class TransactionService {
    private final TransactionDAO transactionDAO;
    private final MemberDAO memberDAO;
    private final BookDAO bookDAO;
    private final BillDAO billDAO;
    public TransactionService(TransactionDAO librarianDAO, BookDAO bookDAO, MemberDAO memberDAO, BillDAO billDAO) {
        this.transactionDAO = librarianDAO;
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
        this.billDAO = billDAO;
    }

    public Transaction createTransaction(Transaction transaction){
        Book book = this.bookDAO.getBookById(transaction.getBookId());
        Member member = this.memberDAO.getMemberByID(transaction.getMemberId());

        if(!book.isAvailable()){
            throw new IllegalArgumentException("Book is not available");
        }

        if(member.getNumberOfBookIssued() > member.getMaxBookLimit()){
            throw new IllegalArgumentException("Member has enough book limit");
        }

        try{
            LocalDate localDate = LocalDate.now();
            LocalDate dueDate = LoanPolicyConfig.calculateDueDate(book);
            transaction.setDateOfIssue(java.sql.Date.valueOf(localDate));
            transaction.setDueDate(java.sql.Date.valueOf(dueDate));
            transaction.setStatus("issued");
            member.addBookIssued();
            book.setAvailable(false);

            Member memberUpdate = memberDAO.updateMember(member, transaction.getMemberId());
            Book bookUpdate = bookDAO.updateBook(book, transaction.getBookId());

            if(memberUpdate != null &&  bookUpdate!=null){
                return this.transactionDAO.createTransaction(transaction);
            }else{
                throw new RuntimeException("Could not update the member and book after creating transaction");
            }

        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Transaction getTransactionById(int transactionId) {
        Transaction transaction = this.transactionDAO.getTransactionById(transactionId);

        if(transaction != null) {
            return transaction;
        }else{
            throw new TransactionNotFoundException("Transaction not found");
        }
    }

    public TransactionDetailsDTO getTransactionDetails(int transactionId) {
        TransactionDetailsDTO transactionDetailsDTO = this.transactionDAO.getTransactionDetailsById(transactionId);
        if(transactionDetailsDTO != null) {
            return transactionDetailsDTO;
        }else{
            throw new TransactionNotFoundException("Transaction not found");
        }

    }

    public Transaction updateTransaction(Transaction transaction, int transactionId) {
        Transaction tempTransaction = this.transactionDAO.updateTransaction(transaction, transactionId);
        if(tempTransaction != null) {
            return tempTransaction;
        }else{
            throw new TransactionNotFoundException("Transaction not found");
        }
    }

    public void deleteTransaction(int transactionId) {
        if(!this.transactionDAO.deleteTransactionById(transactionId)) {
            throw new TransactionNotFoundException("Transaction not found");
        }
    }

    public List<Transaction> getAllTransactionsByStatus(String status) {
        List<Transaction> transactions = this.transactionDAO.getTransactionsByStatus(status);
        if(!transactions.isEmpty()) {
            return transactions;
        }else {
            throw new TransactionNotFoundException("Transactions not found");
        }
    }

    public Bill returnBook(int transaction_id) {
        Transaction transaction = this.transactionDAO.getTransactionById(transaction_id);
        Book book = this.bookDAO.getBookById(transaction.getBookId());
        Date date = new Date();

        Transaction updatedTransaction;
        Book updatedBook;

        if(book.getType().equals("General Book")) {
            transaction.setStatus("returned");
            transaction.setReturnDate(date);
            book.setAvailable(true);

            updatedTransaction = this.transactionDAO.updateTransaction(transaction, transaction.getTransactionId());
            updatedBook = this.bookDAO.updateBook(book, book.getBookId());

            if(updatedBook != null && updatedTransaction!=null) {
                return null;
            }else{
                throw new RuntimeException("Updating failed in General Book");
            }

        }else if (book.getPrice() != null && this.billDAO.getBillByTransactionId(transaction.getTransactionId()) == null && book.getType().equals("Book Bank")) {
            transaction.setStatus("returned");
            transaction.setReturnDate(date);
            book.setAvailable(true);
            BigDecimal amount = book.getPrice();
            String status = "unpaid";

            updatedTransaction = this.transactionDAO.updateTransaction(transaction, transaction.getTransactionId());
            updatedBook = this.bookDAO.updateBook(book, book.getBookId());
            Bill createdBill = new Bill(date, transaction.getMemberId(), transaction.getTransactionId(), amount, status);

            if(updatedBook != null && updatedTransaction!=null) {
                return this.billDAO.createBil(createdBill);
            }else{
                throw new RuntimeException("Updating failed in Book Bank");
            }
        }
        throw new IllegalArgumentException("Bill is already created for this transaction");
    }

}
