package com.app.controller;

import com.app.config.AppConfig;
import com.app.model.Transaction;
import com.app.service.BillService;
import com.app.util.JsonUtil;
import com.app.util.PathUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/v1/lms/bills/*")
public class BillServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BillService billService;

    @Override
    public void init(){
        this.billService = AppConfig.getBillService();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);
        Transaction transaction = JsonUtil.parse(req, Transaction.class);

        try{

        }catch (IllegalArgumentException e){

        }catch (RuntimeException e){

        }

    }
}
