package com.app.config;

import com.app.dao.*;
import com.app.service.*;
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
        return new TransactionService(new TransactionDAO(dbConnection), new BookDAO(dbConnection), new MemberDAO(dbConnection), new BillDAO(dbConnection));
    }
    public static AuthService getAuthService(){
        return new AuthService(new AuthDAO(dbConnection));
    }

    public static BillService getBillService(){
        return new BillService(new BillDAO(dbConnection), new BookDAO(dbConnection), new TransactionDAO(dbConnection));
    }
}
