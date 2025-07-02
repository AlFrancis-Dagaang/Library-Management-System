package com.app.controller;

import com.app.dao.MemberDAO;
import com.app.exception.ErrorException;
import com.app.exception.MemberNotFoundException;
import com.app.model.Member;
import com.app.service.MemberService;
import com.app.util.DBConnection;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet ("/v1/members/*")
public class MemberServlet extends HttpServlet {
    private MemberService memberService;

    @Override
    public void init(){
        DBConnection db = new DBConnection();
        MemberDAO mdao = new MemberDAO(db);
        memberService = new MemberService(mdao);
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String getPath = req.getPathInfo();

        if(getPath == null|| getPath.equals("/")){ //Get All Members
            Gson gson = new Gson();
            List<Member> members = memberService.getAllMembers();
            String json = gson.toJson(members);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        }else{
            int id;
            String idStr = getPath.substring(1);
            id = Integer.parseInt(idStr);
            Gson gson = new Gson();
            try{
                Member member = memberService.getMemberByID(id);
                String json = gson.toJson(member);
                resp.setContentType("application/json");
                resp.getWriter().write(json);
            }catch (MemberNotFoundException e){
                ErrorException error = new ErrorException(e.getMessage(), 404);
                String errorJson = gson.toJson(error);
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                resp.getWriter().write(errorJson);
            }
        }
    }
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();

        if(path.equals("/createMember") ){
            BufferedReader reader = req.getReader();
            Member member = new Gson().fromJson(reader, Member.class);
            member.initializeDefault();
            String json = new Gson().toJson(memberService.addMember(member));
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BufferedReader reader = req.getReader();
        Gson gson = new GsonBuilder()
                .setDateFormat("MMM d, yyyy") // this matches "Jul 1, 2025"
                .create();
        Member member = gson.fromJson(reader, Member.class);
        String path = req.getPathInfo();
        String getId = path.substring(1);
        int id = Integer.parseInt(getId);

        try{
            Member memberTemp = this.memberService.updateMember(member, id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            String json = gson.toJson(memberTemp);
            resp.getWriter().write(json);
        }catch (MemberNotFoundException e){
            ErrorException error = new ErrorException(e.getMessage(), 404);
            String errorJson = gson.toJson(error);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(errorJson);
        }

    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String path = req.getPathInfo();
        String idStr = path.substring(1);
        int id = Integer.parseInt(idStr);
        Gson gson = new Gson();
        try{
            this.memberService.deleteMember(id);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().write("Deleted Successfully");
        }catch (MemberNotFoundException e){
            ErrorException error = new ErrorException(e.getMessage(), 404);
            String errorJson = gson.toJson(error);
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.getWriter().write(errorJson);
        }
    }


}
