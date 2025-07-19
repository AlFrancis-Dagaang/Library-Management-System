package com.app.service;

import com.app.dao.BookReturnBillDAO;
import com.app.dao.BookReturnDAO;
import com.app.dao.PaymentTransactionDAO;
import com.app.exception.ResourceNotFound;
import com.app.model.BookReturnBill;
import com.app.model.BookReturnStatus;
import com.app.model.PaymentTransaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PaymentTransactionService {
    private PaymentTransactionDAO paymentTransactionDAO;
    private BookReturnBillDAO bookReturnBillDAO;
    private BookReturnDAO bookReturnDAO;
    public PaymentTransactionService(PaymentTransactionDAO paymentTransactionDAO, BookReturnBillDAO bookReturnBillDAO, BookReturnDAO bookReturnDAO) {
        this.paymentTransactionDAO = paymentTransactionDAO;
        this.bookReturnBillDAO = bookReturnBillDAO;
        this.bookReturnDAO = bookReturnDAO;
    }

    public PaymentTransaction createPaymentTransaction(PaymentTransaction paymentTransaction) {
        BookReturnBill returnBill = this.bookReturnBillDAO.getBookReturnBillById(paymentTransaction.getBillId());
        BookReturnStatus returnStatus = this.bookReturnDAO.getBookReturnStatus(returnBill.getReturnStatusId());

        if(returnBill.getBillStatus().equals("PENDING")){
            paymentTransaction.setPaymentDate(LocalDate.now());
            paymentTransaction.setStatus("SUCCESS");

            if(returnStatus.getReturnType().equals("GENERAL") && !returnStatus.getBookCondition().equals("Lost")){
                paymentTransaction.setTransactionType("PAYMENT");

                if (returnBill.getTotalPaid().compareTo(returnBill.getPenaltyAmount()) < 0) {

                    BigDecimal totalPaid = returnBill.getTotalPaid();
                    BigDecimal amount = paymentTransaction.getAmount();
                    BigDecimal updatedTotalPaid = totalPaid.add(amount);
                    returnBill.setTotalPaid(updatedTotalPaid);

                    if (returnBill.getTotalPaid().compareTo(returnBill.getPenaltyAmount()) >= 0) {
                        returnBill.setBillStatus("PAID");
                    }
                    this.bookReturnBillDAO.updateBookReturnBill(returnBill);
                    return this.paymentTransactionDAO.createPaymentTransaction(paymentTransaction);
                }
            }else if(returnStatus.getReturnType().equals("BOOK_BANK") && !returnStatus.getBookCondition().equals("Lost")){
                paymentTransaction.setTransactionType("REFUND");

                if (returnBill.getTotalPaid().compareTo(returnBill.getRefundAmount()) < 0) {

                    BigDecimal totalPaid = returnBill.getTotalPaid();
                    BigDecimal amount = paymentTransaction.getAmount();
                    BigDecimal updatedTotalPaid = totalPaid.add(amount);
                    returnBill.setTotalPaid(updatedTotalPaid);

                    if (returnBill.getTotalPaid().compareTo(returnBill.getRefundAmount()) >= 0) {
                        returnBill.setBillStatus("REFUNDED");
                    }

                    this.bookReturnBillDAO.updateBookReturnBill(returnBill);
                    return this.paymentTransactionDAO.createPaymentTransaction(paymentTransaction);
                }

            }else if(returnStatus.getBookCondition().equals("Lost") && returnStatus.getReturnType().equals("BOOK_BANK")){
                paymentTransaction.setTransactionType("FORFEITURE");

                returnBill.setBillStatus("REFUNDED");

                this.bookReturnBillDAO.updateBookReturnBill(returnBill);
                return paymentTransactionDAO.createPaymentTransaction(paymentTransaction);
            }
        }else{
            if(returnStatus.getReturnType().equals("BOOK_BANK")){
                throw new IllegalArgumentException("The Bill is already refunded");
            }else{
                throw new IllegalArgumentException("The Bill is already paid");
            }
        }
        throw new RuntimeException("The Bill didnt exist in the database or status not determined");
    }

    public List<PaymentTransaction> getAllPaymentTransactions(int billId) {
        List<PaymentTransaction> allPaymentTransactions = this.paymentTransactionDAO.getPaymentTransactionsByBillId(billId);

        if(allPaymentTransactions == null){
            throw new ResourceNotFound("The bill is empty");
        }

        return allPaymentTransactions;
    }
}
