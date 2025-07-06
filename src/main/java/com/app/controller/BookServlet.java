package com.app.controller;

import com.app.config.AppConfig;
import com.app.exception.*;
import com.app.model.Book;
import com.app.service.BookService;
import com.app.util.JsonUtil;
import com.app.util.PathUtil;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet ("/v1/lms/books/*")
public class BookServlet extends HttpServlet {
    private BookService bookService;

    public void init() {
        this.bookService = AppConfig.getBookService();
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);
        Book book = JsonUtil.parse(req, Book.class);

        try{
            if(path == null || path.isEmpty()) { //-----------> /v1/lms/books "Create book"
                Book temp = this.bookService.addBook(book);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_CREATED, temp);
            }
        }catch (RuntimeException e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);
        String typeParam = req.getParameter("type");
        String isAvailableParam = req.getParameter("isAvailable");
        String priceParam = req.getParameter("isPriceIsNull");

        try{
            if(path == null || path.isEmpty()) {//-----------> /v1/lms/books "Get all book"
                List<Book> books = this.bookService.getAllBooks();
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, books);
            }else if(paths.length==2 && PathUtil.isNumeric(paths[1])) {//-----------> /{id} "Get book by id"
                int bookId = Integer.parseInt(paths[1]);
                Book book = this.bookService.getBookById(bookId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, book);
            }else if(paths.length==2 && paths[1].equals("sort") && typeParam != null) {//-----------> /sort?type="General Book, Book Bank, Reference Book"  "Get book by id"
                List<Book> books = this.bookService.getBooksByType(typeParam);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, books);
            }else if(paths.length==2 && paths[1].equals("sort") && isAvailableParam != null) {//-----------> /sort?isAvailable="true/false"  "Get book by availability"
                boolean isBookAvailable = Boolean.parseBoolean(isAvailableParam);
                List<Book> books = this.bookService.getBooksByAvailability(isBookAvailable);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, books);
            }else if(paths.length==2 && paths[1].equals("sort") && priceParam != null) {//-----------> /sort?isPriceNull="true/false"  "Get book by price is null"
                boolean isPriceIsNull = Boolean.parseBoolean(priceParam);
                List<Book> books = this.bookService.getBooksByPriceIsNull(isPriceIsNull);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, books);
            }
        }catch (ResourceNotFound e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }catch (RuntimeException e){
        }
    }

    public void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        String path = req.getPathInfo();
        Book updateBook = JsonUtil.parse(req, Book.class);
        String [] paths = PathUtil.getPaths(path);

        try{
            if(paths.length==2 && PathUtil.isNumeric(paths[1])){ //-----------> /{id} "Update book by id"
                int bookId = Integer.parseInt(paths[1]);
                Book book = this.bookService.updateBook(updateBook, bookId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, book);
            }
        }catch (ResourceNotFound e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (RuntimeException e) {
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String path = req.getPathInfo();
        String [] paths = PathUtil.getPaths(path);

        try{
            if(paths.length==2 && PathUtil.isNumeric(paths[1])){ //-----------> /{id} "Delete book by id"
                int bookId = Integer.parseInt(paths[1]);
                this.bookService.deleteBook(bookId);
                JsonUtil.writeOk(resp, HttpServletResponse.SC_OK, "Deleted Successfully");
            }
        }catch (ResourceNotFound e){
            JsonUtil.writeError(resp, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (RuntimeException e) {
            JsonUtil.writeError(resp, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}
