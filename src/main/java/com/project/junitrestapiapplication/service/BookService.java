package com.project.junitrestapiapplication.service;

import com.project.junitrestapiapplication.entity.BookDTO;

import java.util.List;

public interface BookService {

    List<BookDTO> getAllBooks();
    BookDTO getBook(int id);
    BookDTO addBook(BookDTO bookDTO);
    BookDTO updateBook(BookDTO bookDTO, int id);
    void deleteBook(int id);

}
