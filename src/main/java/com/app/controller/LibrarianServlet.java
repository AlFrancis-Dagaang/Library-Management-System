package com.app.controller;

import com.app.config.AppConfig;
import com.app.model.Admin;
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
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();

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

        }
    }

}
