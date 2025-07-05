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
        Member tempMember = memberDAO.getMemberByID(id);
         if(tempMember!=null){
             return tempMember;
         }else{
             throw new MemberNotFoundException("member not found");
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

    public void deleteMember(int id){
        if(memberDAO.deleteMember(id)){
            return;
        }else{
            throw new MemberNotFoundException("Deletion failed or member with ID " + id + " not found.");
        }
    }






}
