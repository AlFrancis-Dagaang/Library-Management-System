package com.app.dao;

import com.app.model.Book;
import com.app.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
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
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, book.getAuthor());
            ps.setString(2, book.getTitle());
            ps.setBigDecimal(3, book.getPrice());
            ps.setInt(4, book.getYearOfPublication());
            ps.setBoolean(5, book.isAvailable());
            ps.setString(6, book.getType());
            int rows = ps.executeUpdate();

            if(rows > 0){
                ResultSet rs = ps.getGeneratedKeys();
                return getBookById(rs.getInt(1));
            }else{
                throw new SQLException("Book could not be added");
            }

        }catch (SQLException e) {
            System.err.println("SQLException in addBook: " + e.getMessage());
            throw new RuntimeException("Database error in addBook()");
        }
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
                return new Book(bookId, author, title, price, yearOfPublication, available, type);
            }

        }catch (SQLException e) {
            System.err.println("SQLException in getBookById: " + e.getMessage());
            throw new RuntimeException("Database error in getBookById()");
        }
        return null;
    }

    public List<Book> getAllBooks(){
        String sql ="SELECT * FROM books";
        List<Book> books = new ArrayList<>();
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

                books.add(new Book(bookId, author, title, price, yearOfPublication, available, type));
            }
            return books;
        }catch (SQLException e) {
            System.err.println("SQLException in getAllBooks(): " + e.getMessage());
            System.out.println("Database error in getAllBooks()");
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

            return rows > 0 ? getBookById(id) : null;

        }catch (SQLException e) {
            System.err.println("SQLException in updateBook: " + e.getMessage());
            throw new RuntimeException("Database error in updateBook()");
        }

    }

    public boolean deleteBook(int id){
        String sql = "DELETE FROM books WHERE book_id = ?";

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        }catch (SQLException e) {
            System.err.println("SQLException in deleteBook: " + e.getMessage());
            throw new RuntimeException("Database error in deleteBook()");
        }
    }

    public  List<Book> getBooksByType(String type){
        String sql ="SELECT * FROM books WHERE type = ?";
        List<Book> books = new ArrayList<>();

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, type);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int bookId = rs.getInt("book_id");
                String author = rs.getString("author");
                String title = rs.getString("title");
                BigDecimal price = rs.getBigDecimal("price");
                int yearOfPublication = rs.getInt("years_of_publication");
                String bookType = rs.getString("type");
                boolean available = rs.getBoolean("isAvailable");
                books.add(new Book(bookId, author, title, price, yearOfPublication, available, bookType));
            }
            return books;
        }catch (SQLException e) {
            System.err.println("SQLException in getBooksByType: " + e.getMessage());
            throw new RuntimeException("Database error in getBooksByType()");
        }
    }
    public  List<Book> getBooksByAvailable(boolean isBookAvailable){
        String sql ="SELECT * FROM books WHERE isAvailable = ?";
        List<Book> books = new ArrayList<>();

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setBoolean(1, isBookAvailable);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int bookId = rs.getInt("book_id");
                String author = rs.getString("author");
                String title = rs.getString("title");
                BigDecimal price = rs.getBigDecimal("price");
                int yearOfPublication = rs.getInt("years_of_publication");
                String bookType = rs.getString("type");
                boolean available = rs.getBoolean("isAvailable");
                books.add(new Book(bookId, author, title, price, yearOfPublication, available, bookType));
            }
            return books;
        }catch (SQLException e) {
            System.err.println("SQLException in getBooksByAvailable: " + e.getMessage());
            throw new RuntimeException("Database error in getBooksByAvailable()");
        }
    }
    public  List<Book> getBooksByPriceIsNull(boolean priceIsNull){

        String sql;

        if(priceIsNull){
            sql= "SELECT * FROM books WHERE price IS NULL";
        }else{
            sql= "SELECT * FROM books WHERE price IS NOT NULL ";
        }

        List<Book> books = new ArrayList<>();

        try(Connection con = this.dbConnection.getConnection()){
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                int bookId = rs.getInt("book_id");
                String author = rs.getString("author");
                String title = rs.getString("title");
                BigDecimal price = rs.getBigDecimal("price");
                int yearOfPublication = rs.getInt("years_of_publication");
                String bookType = rs.getString("type");
                boolean available = rs.getBoolean("isAvailable");
                books.add(new Book(bookId, author, title, price, yearOfPublication, available, bookType));
            }
            return books;
        }catch (SQLException e) {
            System.err.println("SQLException in getBooksByPriceNull: " + e.getMessage());
            throw new RuntimeException("Database error in getBooksByPriceNull()");
        }
    }








}
