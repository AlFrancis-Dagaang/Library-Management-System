package com.app.service;

import com.app.dao.BillDAO;
import com.app.dao.BookDAO;
import com.app.dao.MemberDAO;
import com.app.dao.TransactionDAO;
import com.app.model.Bill;
import com.app.model.Book;
import com.app.model.Member;
import com.app.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;

public class BillService {
    private final BillDAO billDAO;
    private final BookDAO bookDAO;
    private final TransactionDAO transactionDAO;

    public BillService(BillDAO billDAO, BookDAO bookDAO, TransactionDAO transactionDAO) {
        this.billDAO = billDAO;
        this.bookDAO = bookDAO;
        this.transactionDAO = transactionDAO;
    }
}
