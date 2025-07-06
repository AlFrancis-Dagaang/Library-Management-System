package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ErrorException;
import com.app.exception.ResourceNotFound;
import com.app.exception.TransactionNotFoundException;
import com.app.model.Admin;
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
import java.util.List;

@WebServlet("/v1/lms/transactions/*")
public class TransactionServlet extends HttpServlet {
    private TransactionService transactionService;

    public void init(){
        this.transactionService  = AppConfig.getTransactionService();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        try{
            if(path == null || path.isEmpty()){
                Transaction transaction = JsonUtil.parse(req, Transaction.class);
                transaction = this.transactionService.createTransaction(transaction);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_CREATED, transaction);
            }
        }catch (IllegalArgumentException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String[] pathParts  = path.split("/");
        Gson gson = new Gson();

        if(path.equals("/protected")){
            HttpSession session = request.getSession(false);
            if (session != null) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println("authorized: proceed");

            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().println("unauthorized");
            }
        }else if(pathParts.length == 3 && pathParts[2].equals("details")){ // GET /{id}/details "Get a transaction details"
            int id = Integer.parseInt(pathParts[1]);
            try{
                TransactionDetailsDTO transactionDetailsDTO = this.transactionService.getTransactionDetails(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(gson.toJson(transactionDetailsDTO));
            }catch (ResourceNotFound e){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                ErrorException errorException = new ErrorException(e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println(gson.toJson(errorException));
            }

        } else if (pathParts.length == 4 && pathParts[1].equals("member")&& pathParts[3].equals("transactions")) {// GET /member/{id}/details "get all transaction made from a member";
            int id = Integer.parseInt(pathParts[2]);
            try{
                List<Transaction> transactions = this.transactionService.getAllTransactions(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(gson.toJson(transactions));

            }catch (TransactionNotFoundException e){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                ErrorException errorException = new ErrorException(e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println(gson.toJson(errorException));
            }
        }  else if(pathParts.length == 2){ // GET /{id} "Get transaction by id"
            int id = Integer.parseInt(pathParts[1]);
            try{
                Transaction transaction = this.transactionService.getTransactionById(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(gson.toJson(transaction));
            }catch (TransactionNotFoundException e){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                ErrorException errorException = new ErrorException("transaction not found", HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println(gson.toJson(errorException));
            }
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String idStr = path.substring(1);
        int id = Integer.parseInt(idStr);
        Gson gson = new Gson();
        BufferedReader reader = request.getReader();
        Transaction transaction = gson.fromJson(reader, Transaction.class);

        try{
            Transaction tempTransaction = this.transactionService.updateTransaction(transaction, id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().println(gson.toJson(tempTransaction));
        }catch (TransactionNotFoundException e){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            ErrorException errorException = new ErrorException(e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println(gson.toJson(errorException));
        }
    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String idStr = path.substring(1);
        int id = Integer.parseInt(idStr);
        Gson gson = new Gson();
        try{
            if(this.transactionService.deleteTransaction(id)){
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(gson.toJson("transaction deleted successfully"));
            }
        }catch (TransactionNotFoundException e){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            ErrorException errorException = new ErrorException(e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println(gson.toJson(errorException));
        }
    }

}
