package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ResourceNotFound;
import com.app.model.BookTransaction;
import com.app.service.BookTransactionService;
import com.app.util.JsonUtil;
import com.app.util.PathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/v1/lms/book-transactions/*")
public class BookTransactionServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookTransactionService bookTransactionService;

    @Override
    public void init(){
        this.bookTransactionService = AppConfig.getBookIssueService();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String [] paths = PathUtil.getPaths(path);
        BookTransaction transaction = JsonUtil.parse(request, BookTransaction.class);

        System.out.println(transaction);



        try{
            if(path == null || path.isEmpty()){
                Object transactionalObject = this.bookTransactionService.createTransaction(transaction);
                JsonUtil.writeOk(response, HttpServletResponse.SC_CREATED, "", transactionalObject);
            }
        }catch (IllegalArgumentException e){
            JsonUtil.writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String [] paths = PathUtil.getPaths(path);

        try{
            if (paths.length==2 && PathUtil.isNumeric(paths[1] ) ){ // /{id}
                int transactionId = Integer.parseInt(paths[1]);
                BookTransaction getTransaction = this.bookTransactionService.getTransactionById(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"", getTransaction);
            }

        }catch (IllegalArgumentException e){
            JsonUtil.writeError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (ResourceNotFound e){
            JsonUtil.writeError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
