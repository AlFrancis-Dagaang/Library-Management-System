package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ErrorException;
import com.app.exception.ResourceNotFound;
import com.app.exception.TransactionNotFoundException;
import com.app.model.Admin;
import com.app.model.Transaction;
import com.app.model.TransactionDetailsDTO;
import com.app.service.LibrarianService;
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

@WebServlet("/v1/librarian/*")
public class LibrarianServlet extends HttpServlet {
    private LibrarianService librarianService;

    public void init(){
        this.librarianService = AppConfig.getLibrarianService();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

        if(path.equals("/login")){
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            Admin admin = gson.fromJson(reader, Admin.class);
            if(this.librarianService.loginAsAdmin(admin)){
                HttpSession session = request.getSession();
                session.setAttribute("admin", admin);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println("login successful");
            }else{
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().println("username or password incorrect");
            }
        }else if(path.equals("/createTransaction")){
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            Transaction transaction = gson.fromJson(reader, Transaction.class);
            try{
                transaction = this.librarianService.createTransaction(transaction);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(gson.toJson(transaction));
            }catch (IllegalArgumentException e){
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.setContentType("application/json");
                ErrorException errorException = new ErrorException(e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().println(gson.toJson(errorException));
            }
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
        }else if(pathParts.length == 3 && pathParts[2].equals("details")){ // GET /{id}/details "get transaction details"
            int id = Integer.parseInt(pathParts[1]);
            try{
                TransactionDetailsDTO transactionDetailsDTO = this.librarianService.getTransactionDetails(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(gson.toJson(transactionDetailsDTO));
            }catch (ResourceNotFound e){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                ErrorException errorException = new ErrorException(e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println(gson.toJson(errorException));
            }

        } else if (pathParts.length == 4 && pathParts[1].equals("member")&& pathParts[3].equals("transactions")) {// GET /member/{id}/details "get all transaction details from a member";
            int id = Integer.parseInt(pathParts[2]);
            try{
                List<Transaction> transactions = this.librarianService.getAllTransactions(id);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().println(gson.toJson(transactions));

            }catch (TransactionNotFoundException e){
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                ErrorException errorException = new ErrorException(e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().println(gson.toJson(errorException));
            }
        }  else if(pathParts.length == 2){ // GET /{id}
            int id = Integer.parseInt(pathParts[1]);
            try{
                Transaction transaction = this.librarianService.getTransactionById(id);
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
            Transaction tempTransaction = this.librarianService.updateTransaction(transaction, id);
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
            if(this.librarianService.deleteTransaction(id)){
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
