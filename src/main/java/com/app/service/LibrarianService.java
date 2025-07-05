package com.app.service;

import com.app.config.LoanPolicyConfig;
import com.app.dao.BookDAO;
import com.app.dao.LibrarianDAO;
import com.app.dao.MemberDAO;
import com.app.exception.ResourceNotFound;
import com.app.exception.TransactionNotFoundException;
import com.app.model.*;

import java.time.LocalDate;
import java.util.List;

public class LibrarianService {
    private LibrarianDAO librarianDAO;
    private MemberDAO memberDAO;
    private BookDAO bookDAO;
    public LibrarianService() {}
    public LibrarianService(LibrarianDAO librarianDAO, BookDAO bookDAO, MemberDAO memberDAO) {
        this.librarianDAO = librarianDAO;
        this.bookDAO = bookDAO;
        this.memberDAO = memberDAO;
    }

    public boolean loginAsAdmin(Admin admin) {
        Admin tempAdmin = this.librarianDAO.getAdminByUsername(admin.getUsername());

        if(tempAdmin != null && tempAdmin.getPassword().equals(admin.getPassword())) {
            return true;
        }
        return false;
    }

    public Transaction createTransaction(Transaction transaction){
        Book book = this.bookDAO.getBookById(transaction.getBookId());
        Member member = this.memberDAO.getMemberByID(transaction.getMemberId());

        LocalDate localDate = LocalDate.now();

        if(!book.isAvailable()){
            throw new IllegalArgumentException("Book is not available");
        }

        if(member.getNumberOfBookIssued() > member.getMaxBookLimit()){
            throw new IllegalArgumentException("Member has enough book limit");
        }

        try{
            LocalDate dueDate = LoanPolicyConfig.calculateDueDate(book);
            transaction.setDateOfIssue(java.sql.Date.valueOf(localDate));
            transaction.setDueDate(java.sql.Date.valueOf(dueDate));
            transaction.setStatus("issued");
            member.addBookIssued();
            book.setAvailable(false);

            if(memberDAO.updateMember(member, transaction.getMemberId()) != null && bookDAO.updateBook(book, transaction.getBookId()) !=null){
                return this.librarianDAO.createTransaction(transaction);
            }else{
                return null;
            }

        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    public Transaction getTransactionById(int transactionId) {
        Transaction transaction = this.librarianDAO.getTransactionById(transactionId);

        if(transaction != null) {
            return transaction;
        }else{
            throw new TransactionNotFoundException("Transaction not found");
        }
    }

    public TransactionDetailsDTO getTransactionDetails(int transactionId) {
        TransactionDetailsDTO transactionDetailsDTO = this.librarianDAO.getTransactionDetailsById(transactionId);
        if(transactionDetailsDTO != null) {
            return transactionDetailsDTO;
        }else{
            throw new ResourceNotFound("Transaction details not found");
        }

    }

    public Transaction updateTransaction(Transaction transaction, int transactionId) {
        Transaction tempTransaction = this.librarianDAO.updateTransaction(transaction, transactionId);
        if(tempTransaction != null) {
            return tempTransaction;
        }else{
            throw new TransactionNotFoundException("Update transaction not found");
        }
    }

    public boolean deleteTransaction(int transactionId) {

        if(this.librarianDAO.deleteTransactionById(transactionId)) {
            return true;
        }else{
            throw new TransactionNotFoundException("Transaction not found");
        }

    }

    public List<Transaction> getAllTransactions(int id) {
        List<Transaction> allMemberTransaction = this.librarianDAO.getAllTransactionByMemberId(id);

        if(allMemberTransaction != null) {
            return allMemberTransaction;
        }else{
            throw new TransactionNotFoundException("Member transaction not found or Member transaction is empty");
        }
    }
}
