package com.app.config;

import com.app.dao.BookDAO;
import com.app.dao.MemberDAO;
import com.app.service.BookService;
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
}
