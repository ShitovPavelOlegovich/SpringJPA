package org.example.repositories;

import org.example.model.Book;
import org.example.model.Person;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.Min;
import java.util.List;

@Repository
public interface BooksRepository extends JpaRepository<Book, Integer> {

    List<Book> findByOwner(Person owner);

    void findByOwnerAndId(Person owner, int id);

    List<Book> findAllByYearOrderByYearDesc(int year);

    List<Book> findByYearOrderByYear(int year);

    List<Book> findByTitleLike(String title);

    Page<Book> findAll(Pageable pageable);



}
