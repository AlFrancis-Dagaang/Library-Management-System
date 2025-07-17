package com.app.service;

import com.app.dao.MemberDAO;
import com.app.exception.MemberNotFoundException;
import com.app.model.BookTransaction;
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
        Member tempMember = this.memberDAO.updateMember(member,id);
        if(tempMember == null){
            throw new MemberNotFoundException("Member not found");
        }
        return tempMember;
    }

    public void deleteMember(int id){
        boolean deletedSuccessfully = this.memberDAO.deleteMemberById(id);
        if(!deletedSuccessfully){
            throw new MemberNotFoundException("Member to be deleted not found");
        }

    }

    public List<Member> getFilterMembers(String type){
        List <Member> memberList = this.memberDAO.filterMembers(type);
        if(!memberList.isEmpty()){
            return memberList;
        }else{
            throw new MemberNotFoundException("There is no member with the given:"+type+" type");
        }
    }

    public List<BookTransaction> getAllMemberTransactions(int memberId){
        List<BookTransaction> bookTransactionList = this.memberDAO.getAllMemberTransactions(memberId);

        if(bookTransactionList.isEmpty()){
            throw new MemberNotFoundException("Member transactions not found");
        }
        return bookTransactionList;
    }


}
