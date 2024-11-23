package com.project.junitrestapiapplication.service.impl;

import com.project.junitrestapiapplication.entity.Book;
import com.project.junitrestapiapplication.entity.BookDTO;
import com.project.junitrestapiapplication.exceptions.ResourceNotFoundException;
import com.project.junitrestapiapplication.repo.BookRepository;
import com.project.junitrestapiapplication.service.BookService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository, ModelMapper modelMapper) {
        this.bookRepository = bookRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(book -> modelMapper.map(book, BookDTO.class)).collect(Collectors.toList());
    }

    @Override
    public BookDTO getBook(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));
        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public BookDTO addBook(BookDTO bookDTO) {
        Book book = modelMapper.map(bookDTO, Book.class);
        Book save = bookRepository.save(book);
        return modelMapper.map(save, BookDTO.class);
    }

    @Override
    public BookDTO updateBook(BookDTO bookDTO, int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));
        book.setTitle(book.getTitle());
        book.setAuthor(book.getAuthor());
        book.setRating(book.getRating());
        bookRepository.save(book);

        return modelMapper.map(book, BookDTO.class);
    }

    @Override
    public void deleteBook(int id) {
        Book book1 = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "id", String.valueOf(id)));
        bookRepository.delete(book1);
    }
}
