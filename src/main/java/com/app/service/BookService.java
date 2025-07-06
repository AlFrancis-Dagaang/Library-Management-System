package com.app.service;

import com.app.config.AppConfig;
import com.app.dao.BookDAO;
import com.app.dto.BookDTO;
import com.app.exception.BookNotFoundException;
import com.app.exception.ResourceCreationException;
import com.app.exception.ResourceNotFound;
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
        return bookDAO.addBook(book);
    }

    public Book getBookById(int id){
        Book book = bookDAO.getBookById(id);
        if(book != null){
            return book;
        }else{
            throw new ResourceNotFound("Book not found");
        }
    }

    public List<Book> getAllBooks(){
        List<Book> books = bookDAO.getAllBooks();
        if(books != null && !books.isEmpty()){
            return books;
        }else{
            throw new ResourceNotFound("No books found");
        }
    }

    public Book updateBook(Book book, int id){
        Book updatedBook = bookDAO.updateBook(book, id);

        if(updatedBook != null){
            return updatedBook;
        }else {
            throw new ResourceNotFound("Book not found");
        }
    }

    public void deleteBook(int id){
        boolean deletedSuccessfully = bookDAO.deleteBook(id);

        if(!deletedSuccessfully){
            throw new ResourceNotFound("Book not found");
        }
    }

    public List<Book> getBooksByType(String type){
        List<Book> books = bookDAO.getBooksByType(type);
        if(books != null && !books.isEmpty()){
            return books;
        }else {
            throw new ResourceNotFound("No books found with type " + type);
        }
    }

    public List<Book> getBooksByAvailability(boolean availability){
        List<Book> books = bookDAO.getBooksByAvailable(availability);
        if(books != null && !books.isEmpty()){
            return books;
        }else {
            if(availability){
                throw new ResourceNotFound("No books found available");
            }else{
                throw new ResourceNotFound("All books are available");
            }
        }
    }

    public List<Book> getBooksByPriceIsNull(boolean isPriceIsNull){
        List<Book> books = bookDAO.getBooksByPriceIsNull(isPriceIsNull);
        if(books != null && !books.isEmpty()){
            return books;
        }else{
            if(isPriceIsNull){
                throw new ResourceNotFound("No books found with price is null");
            }else{
                throw new ResourceNotFound("No book found with price");
            }
        }
    }


}
