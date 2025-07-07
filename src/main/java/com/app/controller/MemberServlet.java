package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.ErrorException;
import com.app.exception.MemberNotFoundException;
import com.app.model.Bill;
import com.app.model.Member;
import com.app.model.Transaction;
import com.app.service.MemberService;
import com.app.util.JsonUtil;
import com.app.util.PathUtil;
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

@WebServlet ("/v1/lms/members/*")
public class MemberServlet extends HttpServlet {
    private MemberService memberService;

    @Override
    public void init(){
        memberService = AppConfig.getMemberService();
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String[]paths = PathUtil.getPaths(path);
        String typeParam = req.getParameter("type");

        try{
            if(path == null || path.isEmpty()){ //--------> "Get all members
                List<Member> members = memberService.getAllMembers();
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, members);

            }else if(paths.length==2 && PathUtil.isNumeric(paths[1])){//----------> /{id}
                int memberId = Integer.parseInt(paths[1]);
                Member member = memberService.getMemberByID(memberId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, member);

            }else if (paths.length==2 && paths[1].equals("sort") && typeParam != null){  //----------> /sort?type={type} "Sorting base on member types"
                List<Member> members = this.memberService.getSortMembers(typeParam);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, members);
            }else if(paths.length==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("transactions")){  //----------> /{id}/transactions "Get all member transactions"
                int memberId = Integer.parseInt(paths[1]);
                List<Transaction>memberTransactions = this.memberService.getMemberTransactions(memberId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, memberTransactions);
            }else if(paths.length==3 && PathUtil.isNumeric(paths[1]) && paths[2].equals("bills")){  //----------> /{id}/bills "Get all member transactions"
                int memberId = Integer.parseInt(paths[1]);
                List<Bill>memberTransactions = this.memberService.getAllMemberBills(memberId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, memberTransactions);
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
            if(path == null || path.isEmpty() ){ //-----------> /members "Create Member"
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
        String [] paths = PathUtil.getPaths(path);

        try{
            if(paths.length==2 && PathUtil.isNumeric(paths[1])){
                int memberId = Integer.parseInt(paths[1]);
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
        String [] paths = PathUtil.getPaths(path);

        try{
            if(paths.length==2 && PathUtil.isNumeric(paths[1])){// ---------> /members/{id}
                int memberId = Integer.parseInt(paths[1]);
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
