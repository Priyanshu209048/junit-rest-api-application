package com.project.junitrestapiapplication.controller;

import com.project.junitrestapiapplication.entity.BookDTO;
import com.project.junitrestapiapplication.repo.BookRepository;
import com.project.junitrestapiapplication.service.BookService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;
    private final BookRepository bookRepository;

    private Logger logger = LoggerFactory.getLogger(BookController.class);

    @Autowired
    public BookController(BookService bookService, BookRepository bookRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
    }

    @GetMapping()
    public ResponseEntity<List<BookDTO>> getAllBooks(){
        List<BookDTO> books = bookService.getAllBooks();
        logger.info("Books : {}", books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @GetMapping("/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable Integer bookId){
        if (Boolean.TRUE.equals(bookRepository.existsById(bookId))) {
            return new ResponseEntity<>(bookService.getBook(bookId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/")
    public ResponseEntity<?> addBook(@RequestBody @Valid BookDTO bookDTO){
        if (Boolean.TRUE.equals(bookRepository.existsById(bookDTO.getId()))){
            return new ResponseEntity<>("Book Already Exists!", HttpStatus.CONFLICT);
        }
        BookDTO createdBook = bookService.addBook(bookDTO);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<?> updateBook(@RequestBody @Valid BookDTO bookDTO){
        return new ResponseEntity<>(bookService.updateBook(bookDTO, bookDTO.getId()), HttpStatus.OK);
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable Integer bookId){
        if (bookService.getBook(bookId) == null){
            return new ResponseEntity<>("Book Not Exists!", HttpStatus.BAD_REQUEST);
        }
        bookService.deleteBook(bookId);
        return new ResponseEntity<>("Book Successfully Deleted!", HttpStatus.OK);
    }
}
