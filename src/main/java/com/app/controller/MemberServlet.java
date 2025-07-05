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
        String typeParam = req.getParameter("type");

        try{
            if(path.equals("/members") && typeParam ==null){ //--------> /members
                List<Member> members = memberService.getAllMembers();
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, members);

            }else if(paths[1].equals("members") && paths.length == 3){//----------> /members/{id}
                int memberId = Integer.parseInt(paths[2]);

                Member member = memberService.getMemberByID(memberId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, member);

            }else if (paths[1].equals("members") && typeParam != null){  //----------> /members?type={type} "Sorting base on member types"

                List<Member> members = this.memberService.getSortMembers(typeParam);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, members);
            }

        }catch (MemberNotFoundException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
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
        String path = req.getPathInfo();

        Member member = JsonUtil.parse(req, Member.class);
        String [] paths = path.split("/");

        try{
            if(paths[1].equals("members") && paths.length == 3){
                int memberId = Integer.parseInt(paths[2]);
                Member memberTemp = this.memberService.updateMember(member, memberId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, memberTemp);
            }

        }catch (MemberNotFoundException e) {
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String [] paths = path.split("/");

        try{
            if(paths[1].equals("members") && paths.length == 3){// ---------> /members/{id}
                int memberId = Integer.parseInt(paths[2]);
                this.memberService.deleteMember(memberId);
                Member memberTemp = this.memberService.getMemberByID(memberId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, memberTemp);
            }

        }catch (MemberNotFoundException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }

    }




}
