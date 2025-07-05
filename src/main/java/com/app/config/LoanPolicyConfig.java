package com.app.config;

import com.app.model.Book;

import java.time.LocalDate;


public class LoanPolicyConfig {

    public static LocalDate calculateDueDate (Book book){
        if(book.getType().equals("Book Bank")){
            return LocalDate.now().plusMonths(1);
        }else if(book.getType().equals("General Book")){
            return LocalDate.now().plusWeeks(1);
        }else{
            throw new IllegalArgumentException("Reference book cannot be issued");
        }
    }
}
