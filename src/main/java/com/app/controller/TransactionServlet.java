package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ErrorException;
import com.app.exception.ResourceNotFound;
import com.app.exception.TransactionNotFoundException;
import com.app.model.Admin;
import com.app.model.Bill;
import com.app.model.Transaction;
import com.app.model.TransactionDetailsDTO;
import com.app.service.TransactionService;
import com.app.util.JsonUtil;
import com.app.util.PathUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/v1/lms/transactions/*")
public class TransactionServlet extends HttpServlet {
    private TransactionService transactionService;

    public void init(){
        this.transactionService  = AppConfig.getTransactionService();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);

        try{
            if(path == null || path.isEmpty()){
                Transaction transaction = JsonUtil.parse(req, Transaction.class);
                transaction = this.transactionService.createTransaction(transaction);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_CREATED, transaction);
            }else if(paths.length ==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("return")){ //----------> /{id}/return
                int transactionId = Integer.parseInt(paths[1]);
                Bill bill = this.transactionService.returnBook(transactionId);

                if(bill == null){
                    JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, "General Book has no bill");
                }else{
                    JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, bill);
                }
            }
        }catch (IllegalArgumentException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String[] paths = PathUtil.getPaths(path);
        String statusParam = request.getParameter("status");

        try{
            if(paths.length ==2 && PathUtil.isNumeric(paths[1])){// --------/{id} > get transaction by id
                int transactionId = Integer.parseInt(paths[1]);
                Transaction transaction = this.transactionService.getTransactionById(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK, transaction);
            }else if(paths.length ==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("details")){ // ----------/{id}/details > get details of a transaction  by its id
                int transactionId = Integer.parseInt(paths[1]);
                TransactionDetailsDTO transactionDetailsDTO = this.transactionService.getTransactionDetails(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK, transactionDetailsDTO);
            }else if(paths.length ==2 && paths[1].equals("sort")&& statusParam!=null){ // ----------/sort?status=issued,returned, overdue  > sort transaction by status
                List<Transaction> transactions = this.transactionService.getAllTransactionsByStatus(statusParam);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK, transactions);
            }
        }catch (TransactionNotFoundException e){
            JsonUtil.writeOk(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeOk(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String[] paths = PathUtil.getPaths(path);
        Transaction transaction = JsonUtil.parse(req, Transaction.class);

        try{
            if(paths.length ==2 && PathUtil.isNumeric(paths[1])){
                int transactionId = Integer.parseInt(paths[1]);
                Transaction transactionUpdate = this.transactionService.updateTransaction(transaction, transactionId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, transactionUpdate);
            }
        }catch (TransactionNotFoundException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String [] paths = PathUtil.getPaths(path);

        try{
            if(paths.length ==2 && PathUtil.isNumeric(paths[1])){
                int transactionId = Integer.parseInt(paths[1]);
                this.transactionService.deleteTransaction(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK, "Successfully deleted transaction");
            }
        }catch (TransactionNotFoundException e){
            JsonUtil.writeError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }



}
