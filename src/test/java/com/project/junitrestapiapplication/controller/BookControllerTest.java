package com.project.junitrestapiapplication.controller;

import com.project.junitrestapiapplication.entity.BookDTO;
import com.project.junitrestapiapplication.repo.BookRepository;
import com.project.junitrestapiapplication.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {

    private Logger logger = LoggerFactory.getLogger(BookControllerTest.class);

    @InjectMocks
    private BookController bookController;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookService bookService;

    private BookDTO bookDTO1 = new BookDTO();
    private BookDTO bookDTO2 = new BookDTO();
    private BookDTO bookDTO3 = new BookDTO();

    @BeforeEach
    void setUp(){
        bookDTO1 = BookDTO.builder()
                .id(1)
                .title("Book1")
                .author("Priyanshu Baral")
                .rating("7").build();

        bookDTO2 = BookDTO.builder()
                .id(2)
                .title("Book2")
                .author("Ashish Mishra")
                .rating("8").build();

        bookDTO3 = BookDTO.builder()
                .id(3)
                .title("Book3")
                .author("Abhishek Mishra")
                .rating("9").build();
    }

    @Test
    void getAllRecords_success() {
        List<BookDTO> bookList = List.of(bookDTO1, bookDTO2, bookDTO3);
        when(bookService.getAllBooks()).thenReturn(bookList);
        ResponseEntity<?> responseEntity = this.bookController.getAllBooks();

        /*mockMvc.perform(get("/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());*/

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(3, bookList.size());

        assertEquals(bookList, responseEntity.getBody());
        verify(bookService, times(1)).getAllBooks();
        logger.info("Successfully retrieved all books");
    }

    @Test
    void getBookById_success() throws Exception {
        /*lenient().when(bookService.getBook(bookDTO1.getId())).thenReturn(bookDTO1);

        mockMvc.perform(get("/book/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(bookDTO1.getId()));*/

        when(bookService.getBook(bookDTO1.getId())).thenReturn(bookDTO1);
        ResponseEntity<?> responseEntity = this.bookController.getBookById(bookDTO1.getId());

        if(responseEntity.getStatusCode() == HttpStatus.OK){
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals(bookDTO1, responseEntity.getBody());
            logger.info("Successfully retrieved book : {}", responseEntity.getBody());
        } else {
            assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
            logger.error("Book not found");
        }
    }

    @Test
    void getBookById_NotFound() {
        when(bookRepository.existsById(bookDTO1.getId())).thenReturn(false);
        ResponseEntity<?> responseEntity = this.bookController.getBookById(bookDTO1.getId());
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
    }

    @Test
    void createBook_success() {
        lenient().when(bookRepository.existsByTitle(bookDTO1.getTitle())).thenReturn(false);
        lenient().when(bookService.addBook(bookDTO1)).thenReturn(bookDTO1);
        ResponseEntity<?> responseEntity = bookController.addBook(bookDTO1);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(bookDTO1, responseEntity.getBody());
        logger.info("Successfully added book : {}", responseEntity.getBody());
    }

    @Test
    void updateBook_success() {
        BookDTO record = BookDTO.builder()
                .id(1)
                .title("book1")
                .author("Priyanshu Baral")
                .rating("8")
                .build();
        when(bookService.updateBook(record, record.getId())).thenReturn(record);
        ResponseEntity<?> responseEntity = bookController.updateBook(record);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(record, responseEntity.getBody());
        logger.info("Successfully updated book : {}", responseEntity.getBody());
    }

    @Test
    void deleteBook_success() {
        when(bookService.getBook(bookDTO1.getId())).thenReturn(bookDTO1);
        assertNotNull(bookController.getBookById(bookDTO1.getId()));
        ResponseEntity<?> responseEntity = bookController.deleteBook(bookDTO1.getId());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Book Successfully Deleted!", responseEntity.getBody());
        logger.info("Book Successfully Deleted");
    }

}
