package com.app.model;

import com.app.config.Constant;

import java.util.Date;

public class Member {
    private int memberId;
    private String type;
    private Date dateOfMembership;
    private int numberOfBookIssued=0;
    private int maxBookLimit;
    private String name;
    private String address;
    private long phoneNumber;
    public Member() {}

    public Member(String name, String address, long phoneNumber, String type) {
        this.name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.type = type;

    }

    public void initializeDefault(){
        if(type.equals(Constant.MEMBER_TYPE_STUDENT)) {
            this.type = "Student";
            this.maxBookLimit = Constant.MAX_BOOK_STUDENTS;
        }else if(type.equals(Constant.MEMBER_TYPE_TEACHER)) {
            this.type = "Teacher";
            this.maxBookLimit = Constant.MAX_BOOK_TEACHER;
        }
    }


    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxBookLimit() {
        return maxBookLimit;
    }

    public void setMaxBookLimit(int maxBookLimit) {
        this.maxBookLimit = maxBookLimit;
    }

    public int getNumberOfBookIssued() {
        return numberOfBookIssued;
    }

    public void setNumberOfBookIssued(int numberOfBookIssued) {
        this.numberOfBookIssued = numberOfBookIssued;
    }

    public Date getDateOfMembership() {
        return dateOfMembership;
    }

    public void setDateOfMembership(Date dataOfMembership) {
        this.dateOfMembership = dataOfMembership;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberId=" + memberId +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber=" + phoneNumber +
                ", type='" + type + '\'' +
                ", dateOfMembership=" + dateOfMembership +
                ", numberOfBookIssued=" + numberOfBookIssued +
                ", maxBookLimit=" + maxBookLimit +
                '}';
    }



}
