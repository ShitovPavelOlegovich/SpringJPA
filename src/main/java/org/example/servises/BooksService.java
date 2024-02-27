package org.example.servises;

import org.example.model.Book;
import org.example.model.Person;
import org.example.repositories.BooksRepository;
import org.example.repositories.PeopleRepository;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {

    private final BooksRepository booksRepository;

    private final SessionFactory sessionFactory;

    private final JdbcTemplate jdbcTemplate;

    private final PeopleRepository peopleRepository;




    @Autowired
    public BooksService(BooksRepository booksRepository, SessionFactory sessionFactory,
                        JdbcTemplate jdbcTemplate, PeopleRepository peopleRepository) {
        this.booksRepository = booksRepository;
        this.sessionFactory = sessionFactory;
        this.jdbcTemplate = jdbcTemplate;
        this.peopleRepository = peopleRepository;

    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }

    public Book findOne(int id) {
        Optional<Book> book = booksRepository.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    public void update(int id, Book updateBook) {
        updateBook.setId(id);
        booksRepository.save(updateBook);
    }

    @Transactional
    public void delete(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    public List<Book> getBookOwner(Person owner) {
       return booksRepository.findByOwner(owner);
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        LocalDate currentDate = LocalDate.now();
        Optional<Book> optionalBook = booksRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            Optional<Person> optionalPerson = peopleRepository.findById(selectedPerson.getId());
            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                book.setOwner(person);
                book.setDateTakeBook(currentDate);
                booksRepository.save(book);
            }
        }
    }

    @Transactional
    public boolean dateCheck(int id) {
       Optional<Book> optionalBook = booksRepository.findById(id);
       if(optionalBook.isPresent()) {
           Book book = optionalBook.get();
           LocalDate currentDate = LocalDate.now();
           LocalDate dueDate = book.getDateTakeBook().plusDays(10);
           return currentDate.isBefore(dueDate) || currentDate.isEqual(dueDate);
       } else {
           return false;
       }
    }
    @Transactional
    public void release(int id) {
        Optional<Book> optionalBook = booksRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setOwner(null);
        }
    }
    public Optional<Person> getBookOwner(int id) {
        return jdbcTemplate.query("SELECT Person.* FROM Book JOIN Person ON Book.person_id=Person.id WHERE Book.id=?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class)).stream().findAny();
//        Book book = session.get(Book.class, id);
//        return book.getOwner();

//        return peopleRepository.findById(booksRepository.findById(id).getClass().getId());

    }

    public List<Book> search(String title) {
        return booksRepository.findByTitleLike(title);
    }


    public Page<Book> findAll(Pageable pageable) {
        return booksRepository.findAll(pageable);

    }






}
