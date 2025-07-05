package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ErrorException;
import com.app.exception.MemberNotFoundException;
import com.app.model.Member;
import com.app.service.MemberService;
import com.app.util.JsonUtil;
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

@WebServlet ("/v1/lms/*")
public class MemberServlet extends HttpServlet {
    private MemberService memberService;

    @Override
    public void init(){
        memberService = AppConfig.getMemberService();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String[]paths = path.split("/");

        if(path.equals("/members")){ //--------> /members
            List<Member> members = memberService.getAllMembers();
            JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, members);

        }else if(paths[1].equals("members") && paths.length == 3){//----------> /members/{id}
            int memberId = Integer.parseInt(paths[2]);

            try{
                Member member = memberService.getMemberByID(memberId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, member);
            }catch (MemberNotFoundException e){
                JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }catch (RuntimeException e){
                JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
            }
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();

        try{
            if(path.equals("/members") ){ //-----------> /members "Create Member"
                Member member = JsonUtil.parse(req, Member.class);
                member.initializeDefault();
                JsonUtil.writeOk(resp, HttpServletResponse.SC_CREATED, this.memberService.addMember(member));
            }
        } catch (RuntimeException e) {
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
