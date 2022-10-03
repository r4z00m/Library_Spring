package com.example.Library_SpringBoot.repositories;

import com.example.Library_SpringBoot.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByNameStartingWith(String startingWith);
}
