package com.app.dao;

import com.app.model.Book;
import com.app.util.DBConnection;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private DBConnection dbConnection;

    public BookDAO() {}
    public BookDAO(DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public Book addBook(Book book){
        String sql ="INSERT INTO books (author, title, price, years_of_publication, isAvailable, type) VALUES (?,?,?,?,?,?)";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book.getAuthor());
            ps.setString(2, book.getTitle());
            ps.setBigDecimal(3, book.getPrice());
            ps.setInt(4, book.getYearOfPublication());
            ps.setBoolean(5, book.isAvailable());
            ps.setString(6, book.getType());
            int rows = ps.executeUpdate();

            if(rows > 0){
                return book;
            }

        }catch (SQLException e) {
            System.out.println("addBookSQLException Error:" + e.getMessage());
        }
        return null;
    }

    public Book getBookById(int id){
        String sql ="SELECT * FROM books WHERE book_id = ?";
        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                int bookId = rs.getInt("book_id");
                String author = rs.getString("author");
                String title = rs.getString("title");
                BigDecimal price = rs.getBigDecimal("price");
                int yearOfPublication = rs.getInt("years_of_publication");
                boolean available = rs.getBoolean("isAvailable");
                String type = rs.getString("type");

                Book getBook = new Book(bookId, author, title, price, yearOfPublication, available, type);
                return getBook;
            }

        }catch (SQLException e) {
            System.out.println("getBookByIdSQLException Error:" + e.getMessage());
        }
        return null;
    }

    public List<Book> getAllBooks(){
        String sql ="SELECT * FROM books";
        List<Book> books = new ArrayList<Book>();
        try(Connection conn = this.dbConnection.getConnection()){
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int bookId = rs.getInt("book_id");
                String author = rs.getString("author");
                String title = rs.getString("title");
                BigDecimal price = rs.getBigDecimal("price");
                int yearOfPublication = rs.getInt("years_of_publication");
                boolean available = rs.getBoolean("isAvailable");
                String type = rs.getString("type");
                Book listBook = new Book(bookId, author, title, price, yearOfPublication, available, type);
                books.add(listBook);
            }
            return books;

        }catch (SQLException e) {
            System.out.println("getAllBooksSQLException Error:" + e.getMessage());
        }
        return null;
    }

    public Book updateBook(Book book, int id){
        String sql = "UPDATE books SET author=?, title = ?, price=?, years_of_publication=?, isAvailable=?, type=? WHERE book_id = ?";
        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, book.getAuthor());
            ps.setString(2, book.getTitle());
            ps.setBigDecimal(3, book.getPrice());
            ps.setInt(4, book.getYearOfPublication());
            ps.setBoolean(5, book.isAvailable());
            ps.setString(6, book.getType());
            ps.setInt(7, id);
            int rows = ps.executeUpdate();
            if(rows > 0){
                return getBookById(id);
            }
        }catch (SQLException e) {
            System.out.println("updateBookSQLException Error:" + e.getMessage());
        }
        return null;
    }

    public boolean deleteBook(int id){
        String sql = "DELETE FROM books WHERE book_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }catch (SQLException e) {
            System.out.println("deleteBookSQLException Error:" + e.getMessage());
            return false;
        }
    }




}
