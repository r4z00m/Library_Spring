package app.services;

import app.model.Book;
import app.model.Person;
import app.repositories.BookRepository;
import app.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository bookRepository;
    private final PersonRepository personRepository;

    @Autowired
    public BookService(BookRepository bookRepository, PersonRepository personRepository) {
        this.bookRepository = bookRepository;
        this.personRepository = personRepository;
    }

    public List<Book> readBooks(Integer page, Integer books_per_page, Boolean sort_by_year) {
        if (page != null && books_per_page != null) {
            if (sort_by_year != null) {
                return bookRepository
                        .findAll(PageRequest.of(page, books_per_page, Sort.by("year")))
                        .getContent();
            } else {
                return bookRepository
                        .findAll(PageRequest.of(page, books_per_page))
                        .getContent();
            }
        } else if (sort_by_year != null) {
            return bookRepository.findAll(Sort.by("year"));
        }
        return bookRepository.findAll();
    }

    @Transactional
    public void save(Book book) {
        bookRepository.save(book);
    }

    public Optional<Book> findById(int id) {
        return bookRepository.findById(id);
    }

    @Transactional
    public void update(int id, Book book) {
        Book old = bookRepository.findById(id).get();
        Hibernate.initialize(old.getPerson());
        if (old.getPerson() != null) {
            book.setPerson(old.getPerson());
            book.setTakenAt(old.getTakenAt());
        }
        book.setId(id);
        bookRepository.save(book);
    }

    @Transactional
    public void deleteById(int id) {
        bookRepository.deleteById(id);
    }

    @Transactional
    public void addPersonToBook(int person_id, int book_id) {
        Optional<Book> book = bookRepository.findById(book_id);
        Optional<Person> person = personRepository.findById(person_id);
        if (book.isPresent() && person.isPresent()) {
            book.get().setTakenAt(new Date());
            book.get().setPerson(person.get());
            person.get().getBooks().add(book.get());
        }
    }

    @Transactional
    public void freeBook(int id) {
        Book book = bookRepository.findById(id).get();
        book.setPerson(null);
        book.setTakenAt(null);
        book.setExpired(false);
        bookRepository.save(book);
    }

    public List<Book> findByStart(String to_find) {
        return bookRepository.findAllByNameStartingWith(to_find);
    }
}
