package com.app.dto;

import java.math.BigDecimal;

public class BookDTO {
    private int bookId;
    private String author;
    private String title;
    private BigDecimal price;
    private int yearOfPublication;
    private boolean isAvailable;

    public BookDTO() {}
    public BookDTO(int bookId,String author, String title, BigDecimal price, int yearOfPublication, boolean isAvailable) {
        this.bookId = bookId;
        this.author = author;
        this.title = title;
        this.price = price;
        this.yearOfPublication = yearOfPublication;
        this.isAvailable = isAvailable;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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

    public String toString(){
        return "Book{" +
                ", author='" + author + '\'' +
                ", title='" + title + '\'' +
                ", price=" + price +
                ", yearOfPublication=" + yearOfPublication +
                ", isAvailable=" + isAvailable +
                '}';
    }
}
