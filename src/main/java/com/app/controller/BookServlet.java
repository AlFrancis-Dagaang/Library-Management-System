package com.app.controller;

import com.app.config.AppConfig;
import com.app.dto.BookDTO;
import com.app.exception.BookNotFoundException;
import com.app.exception.ErrorException;
import com.app.exception.MemberNotFoundException;
import com.app.exception.ResourceCreationException;
import com.app.model.Book;
import com.app.model.Member;
import com.app.service.BookService;
import com.google.gson.Gson;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;

@WebServlet ("/v1/books/*")
public class BookServlet extends HttpServlet {
    private BookService bookService;

    public void init() {
        this.bookService = AppConfig.getBookService();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        Book book = gson.fromJson(reader, Book.class);
        String json;

        if(path.equals("/createBook")) {
            try{
                Book newBook = this.bookService.addBook(book);
                json = gson.toJson(newBook);
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setContentType("application/json");
                response.getWriter().write(json);
            }catch (ResourceCreationException e){
                ErrorException error = new ErrorException(e.getMessage(), 400);
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                json = gson.toJson(error);
                response.getWriter().print(json);
            }
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String getPath = request.getPathInfo();
        Gson gson = new Gson();
        if(getPath == null|| getPath.equals("/")){
            try{
                List<Book> books = this.bookService.getAllBooks();
                String json = gson.toJson(books);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType("application/json");
                response.getWriter().write(json);

            }catch (BookNotFoundException e){
                ErrorException error = new ErrorException(e.getMessage(), 404);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.setContentType("application/json");
                response.getWriter().print(gson.toJson(error));
            }

        }else{
            int id;
            String idStr = getPath.substring(1);
            id = Integer.parseInt(idStr);
            try{
                Book book = bookService.getBookById(id);
                String json = gson.toJson(book);
                response.setContentType("application/json");
                response.getWriter().write(json);

            }catch (BookNotFoundException e){
                ErrorException error = new ErrorException(e.getMessage(), 404);
                String errorJson = gson.toJson(error);
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write(errorJson);
            }
        }
    }

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        Gson gson = new Gson();
        Book book = gson.fromJson(reader, Book.class);
        String getId = request.getPathInfo().substring(1);
        int id = Integer.parseInt(getId);

        try{
            Book updateBook = this.bookService.updateBook(book, id);
            String json = gson.toJson(updateBook);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write(json);

        }catch (BookNotFoundException e){
            ErrorException error = new ErrorException(e.getMessage(), 404);
            String errorJson = gson.toJson(error);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write(errorJson);
        }

    }

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo();
        String idStr = path.substring(1);
        int id = Integer.parseInt(idStr);
        Gson gson = new Gson();

        try{
            this.bookService.deleteBook(id);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            response.getWriter().write("Successfully deleted the book");
        }catch (BookNotFoundException e){
            ErrorException error = new ErrorException(e.getMessage(), 404);
            String errorJson = gson.toJson(error);
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            response.getWriter().write(errorJson);
        }
    }

}
