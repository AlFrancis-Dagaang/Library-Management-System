package com.app.config;

import com.app.dao.AuthDAO;
import com.app.dao.BookDAO;
import com.app.dao.TransactionDAO;
import com.app.dao.MemberDAO;
import com.app.service.AuthService;
import com.app.service.BookService;
import com.app.service.TransactionService;
import com.app.service.MemberService;
import com.app.util.DBConnection;

public class AppConfig {
    private static final DBConnection dbConnection = new DBConnection();

    public static MemberService getMemberService(){
        return new MemberService(new MemberDAO(dbConnection));
    }

    public static BookService getBookService(){
        return new BookService(new BookDAO(dbConnection));
    }

    public static TransactionService getTransactionService(){
        return new TransactionService(new TransactionDAO(dbConnection), new BookDAO(dbConnection), new MemberDAO(dbConnection));
    }
    public static AuthService getAuthService(){
        return new AuthService(new AuthDAO(dbConnection));
    }
}
