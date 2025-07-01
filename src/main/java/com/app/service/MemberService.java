package com.app.service;

import com.app.dao.MemberDAO;
import com.app.exception.MemberNotFoundException;
import com.app.model.Member;

import java.util.List;

public class MemberService {
    private MemberDAO memberDAO;
    public MemberService() {}

    public MemberService (MemberDAO memberDao){
        this.memberDAO = memberDao;
    }

    public Member getMemberByID(int id){
         if(memberDAO.getMemberByID(id)!=null){
             return memberDAO.getMemberByID(id);
         }else{
             throw new MemberNotFoundException("Member not found");
         }
    }

    public Member addMember(Member member){
        return this.memberDAO.addMember(member);
    }

    public List<Member> getAllMembers(){
        return this.memberDAO.getAllMembers();
    }

    public Member updateMember(Member member, int id){
        Member temp = this.memberDAO.udpateMember(member,id);
        if( temp == null){
            throw new MemberNotFoundException("Cannot update: Member with ID " + id + " not found.");
        }
        return temp;
    }






}
