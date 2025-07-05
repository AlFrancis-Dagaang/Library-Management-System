package com.app.service;

import com.app.config.AppConfig;
import com.app.dao.BookDAO;
import com.app.dto.BookDTO;
import com.app.exception.BookNotFoundException;
import com.app.exception.ResourceCreationException;
import com.app.model.Book;
import com.app.util.MapperUtil;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookService {
    private BookDAO bookDAO;

    public BookService() {}
    public BookService(BookDAO bookDAO) {
        this.bookDAO = bookDAO;
    }

    public Book addBook(Book book){
        Book newBook = bookDAO.addBook(book);

        if(newBook != null){
            return newBook;
        }else{
            throw new ResourceCreationException("Book could not be created");
        }

    }
    public Book getBookById(int id){
        Book book = bookDAO.getBookById(id);

        if(book != null){
            return book;
        }else{
            throw new BookNotFoundException("Book could not be found");
        }
    }

    public List<Book> getAllBooks(){
        List<Book> books = bookDAO.getAllBooks();
        if(books != null && !books.isEmpty()){
            return books;
        }else{
            throw new BookNotFoundException("No books found in the database.");
        }
    }

    public Book updateBook(Book book, int id){
        Book newBook = bookDAO.updateBook(book, id);

        if(newBook != null){
            return newBook;
        }else{
            throw new BookNotFoundException("Book could not be found");
        }
    }

    public void deleteBook(int id){
        boolean deletedSuccess = bookDAO.deleteBook(id);

        if(deletedSuccess){
          return;
        }else{
            throw new BookNotFoundException("Book could not be found");
        }

    }
}
