package org.example.repositories;


import org.example.model.Book;
import org.example.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {

}
