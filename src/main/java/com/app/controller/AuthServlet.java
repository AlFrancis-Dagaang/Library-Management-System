package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ResourceNotFound;
import com.app.model.Admin;
import com.app.service.AuthService;
import com.app.util.JsonUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet ("/v1/lms/auth/*")
public class AuthServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AuthService authService;

    @Override

    public void init(){
        this.authService = AppConfig.getAuthService();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        try{
            if(path.equals("/login")){
                Admin admin = JsonUtil.parse(req, Admin.class);
                this.authService.loginAsAdmin(admin);
                HttpSession session = req.getSession();
                session.setAttribute("admin", admin);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, "Login successful");
            }
        }catch (ResourceNotFound e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (IllegalArgumentException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


}
