package com.project.junitrestapiapplication.repo;

import com.project.junitrestapiapplication.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    Boolean existsByTitle(String title);

}
