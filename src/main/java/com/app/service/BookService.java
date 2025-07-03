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
    public BookDTO getBookById(int id){
        Book book = bookDAO.getBookById(id);
        ModelMapper modelMapper = MapperUtil.getModelMapper();

        if(book != null){
            return modelMapper.map(book, BookDTO.class);
        }else{
            throw new BookNotFoundException("Book could not be found");
        }
    }

    public List<BookDTO> getAllBooks(){
        List<Book> books = bookDAO.getAllBooks();
        ModelMapper modelMapper = MapperUtil.getModelMapper();
        if(books != null && !books.isEmpty()){
            List<BookDTO> bookDTOs = books.stream().map(book-> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
            return bookDTOs;
        }else{
            throw new BookNotFoundException("No books found in the database.");
        }
    }

    public BookDTO updateBook(Book book, int id){
        Book newBook = bookDAO.updateBook(book, id);

        if(newBook != null){
            return MapperUtil.getModelMapper().map(newBook, BookDTO.class);
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
