package com.app.service;

import com.app.dao.BillDAO;
import com.app.dao.BookDAO;
import com.app.dao.MemberDAO;
import com.app.dao.TransactionDAO;
import com.app.exception.ResourceNotFound;
import com.app.model.Bill;
import com.app.model.Book;
import com.app.model.Member;
import com.app.model.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class BillService {
    private final BillDAO billDAO;


    public BillService(BillDAO billDAO, BookDAO bookDAO, TransactionDAO transactionDAO) {
        this.billDAO = billDAO;
    }

    public Bill payBill(int billId) {
        Bill bill = this.billDAO.getBillById(billId);

        if(bill == null) {
            throw new ResourceNotFound("Bill not found");
        }

        if(bill.getStatus().equals("paid")){
            throw new IllegalArgumentException("Bill is already paid");
        }

        return this.billDAO.payFine(billId);
    }

    public List<Bill> getAllBills(){
        List<Bill> bills = this.billDAO.getAllBills();
        if(bills.isEmpty()) {
            throw new ResourceNotFound("No bills found");
        }else{
            return bills;
        }
    }

    public Bill getBillById(int billId) {
        Bill bill = this.billDAO.getBillById(billId);

        if(bill == null) {
            throw new ResourceNotFound("Bill not found");
        }
        return bill;
    }

    public void deleteBill(int billId) {
        boolean deletedSuccessfully = this.billDAO.deleteBill(billId);

        if(!deletedSuccessfully) {
            throw new ResourceNotFound("Bill not found");
        }

    }



}
