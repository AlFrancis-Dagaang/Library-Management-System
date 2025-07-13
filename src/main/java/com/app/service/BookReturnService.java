package com.app.service;

import com.app.config.PolicyConfig;
import com.app.dao.*;
import com.app.model.Book;
import com.app.model.BookReturnStatus;
import com.app.model.BookTransaction;
import com.app.model.Member;
import com.app.util.BookBankUtil;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BookReturnService {
    private BookReturnDAO bookReturnDAO;
    private BookDAO bookDAO;
    private BookTransactionDAO bookTransactionDAO;
    private BookAgreementDAO bookAgreementDAO;

    public BookReturnService(BookReturnDAO bookReturnDAO, BookDAO bookDAO, BookTransactionDAO bookTransactionDAO, BookAgreementDAO bookAgreementDAO) {
        this.bookReturnDAO = bookReturnDAO;
        this.bookDAO = bookDAO;
        this.bookTransactionDAO = bookTransactionDAO;
        this.bookAgreementDAO = bookAgreementDAO;
    }

    public BookReturnStatus createBookReturn(BookReturnStatus bookReturn) {
        BookTransaction bookTransaction = this.bookTransactionDAO.getIssueTransactionById(bookReturn.getTransactionId());
        Book book = this.bookDAO.getBookById(bookTransaction.getBookId());

        LocalDate returnDate = LocalDate.now();
        bookReturn.setReturnDate(returnDate);

        bookTransaction.setReturnDate(returnDate); //update return date for BookTransaction

        if(bookTransaction.getDueDate().isBefore(returnDate)) {
            bookReturn.setReturnTimeline("Overdue");
        }else{
            bookReturn.setReturnTimeline("On Time");
        }

        bookReturn.setReturnType(PolicyConfig.getBookType(book));

        String conditionOfBook = bookReturn.getBookCondition();
        String returnType = bookReturn.getReturnType();
        String timeLine = bookReturn.getReturnTimeline();

        BigDecimal penaltyBaseOnCondition= BookBankUtil.calculatePenaltyBaseOnCondition(conditionOfBook, returnType, book.getPrice());
        BigDecimal penaltyBaseOnTimeline = BookBankUtil.calculatePenaltyBaseOnReturnTimeline(timeLine, returnType );
        BigDecimal penaltyAmount = penaltyBaseOnCondition.add(penaltyBaseOnTimeline);
        bookReturn.setPenaltyAmount(penaltyAmount);

        book.setAvailable(true);//update book availability to available


        if("BOOK)_BANK".equals(returnType)) {
            BigDecimal refundAmount = book.getPrice().subtract(penaltyAmount);
            bookReturn.setRefundAmount(refundAmount);

        }

        return null;
    }
}

















