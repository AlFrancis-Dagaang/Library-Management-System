package com.app.model;

import java.math.BigDecimal;

public class Book {
    private int bookId;
    private String author;
    private String title;
    private BigDecimal price;
    private int yearOfPublication;
    private boolean isAvailable;
    private String type;
    public Book() {}
    //Sending to the client with Id for book bank
    public Book(int bookId, String author, String title, BigDecimal price, int yearOfPublication, boolean isAvailable, String type) {
        this.bookId = bookId;
        this.author = author;
        this.title = title;
        this.price = price;
        this.yearOfPublication = yearOfPublication;
        this.isAvailable = isAvailable;
        this.type = type;
    }
    //Contructor for saving json without id for Book bank
    public Book(String author, String title, BigDecimal price, int yearOfPublication, boolean isAvailable, String type) {
        this.author = author;
        this.title = title;
        this.price = price;
        this.yearOfPublication = yearOfPublication;
        this.isAvailable = isAvailable;
        this.type = type;
    }

    //Sending to the client with Id for general and reference book
    public Book(int id,String author, String title, int yearOfPublication, boolean isAvailable, String type){
        this.bookId = id;
        this.author = author;
        this.title = title;
        this.yearOfPublication = yearOfPublication;
        this.isAvailable = isAvailable;
        this.type = type;
    }
    //Contructor for saving json without id for general and reference book
    public Book(String author, String title, int yearOfPublication, boolean isAvailable, String type){
        this.author = author;
        this.title = title;
        this.yearOfPublication = yearOfPublication;
        this.isAvailable = isAvailable;
        this.type = type;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYearOfPublication() {
        return yearOfPublication;
    }

    public void setYearOfPublication(int yearOfPublication) {
        this.yearOfPublication = yearOfPublication;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString(){
        return "Book{" +
                "bookId=" + bookId +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", type='" + type + '\'' +
                ", yearOfPublication=" + yearOfPublication +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
