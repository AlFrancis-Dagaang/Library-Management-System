package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ResourceNotFound;
import com.app.model.BookAgreement;
import com.app.model.BookTransaction;
import com.app.model.BookTransactionAgreementDetailsDTO;
import com.app.model.BookTransactionDetailsDTO;
import com.app.service.BookTransactionService;
import com.app.util.JsonUtil;
import com.app.util.PathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

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
            if(path == null || path.isEmpty()){ // "Create Transaction for both General and Book Bank"
                Object transactionalObject = this.bookTransactionService.createTransaction(transaction);
                JsonUtil.writeOk(response, HttpServletResponse.SC_CREATED, "", transactionalObject);
            }else if(paths.length==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("complete-transaction")){ // /{id}/complete-transaction
                int transactionId = Integer.parseInt(paths[1]);
                BookTransactionAgreementDetailsDTO bookTransactionAgreementDetailsDTO = this.bookTransactionService.completeBookTransactionAgreement(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"Success", bookTransactionAgreementDetailsDTO);
            } else if (paths.length==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("cancel-agreement")) { // /{id}/cancel-transaction
                int transactionId = Integer.parseInt(paths[1]);
                BookTransaction bookCancelledTransaction = this.bookTransactionService.cancelBookAgreement(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"Success", bookCancelledTransaction);
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
            if(path == null || path.isEmpty()){
                List<BookTransaction> allTransactions = this.bookTransactionService.getAllTransactions();
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"Success", allTransactions);
            }else if (paths.length==2 && PathUtil.isNumeric(paths[1] ) ){ // /{id}   "Get transaction without details"
                int transactionId = Integer.parseInt(paths[1]);
                BookTransaction getTransaction = this.bookTransactionService.getTransactionById(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"", getTransaction);
            }else if (paths.length==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("details")){ // /{id}/details "Get transaction details without agreement"
                int transactionId = Integer.parseInt(paths[1]);
                BookTransactionDetailsDTO bookTransactionDetailsDTO = this.bookTransactionService.getBookTransactionDetails(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"Success", bookTransactionDetailsDTO);
            }else if(paths.length==4 && PathUtil.isNumeric(paths[1]) && paths[2].equals("details") && paths[3].equals("agreement")){ //{id}/details/agreement "Get transaction details with agreement"
                int transactionId = Integer.parseInt(paths[1]);
                BookTransactionAgreementDetailsDTO agreementDetailsDTO = this.bookTransactionService.getBookTransactionAgreementDetails(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"Success", agreementDetailsDTO);
            }else if(paths.length==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("agreement")){ // /{id}/agreement
                int transactionId = Integer.parseInt(paths[1]);
                BookAgreement agreement = this.bookTransactionService.getBookAgreementByTransactionId(transactionId);
                JsonUtil.writeOk(response, HttpServletResponse.SC_OK,"Success", agreement);
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
