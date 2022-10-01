package app.services;

import app.model.Book;
import app.model.Person;
import app.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public List<Person> readPeople() {
        return personRepository.findAll();
    }

    @Transactional
    public void save(Person person) {
        personRepository.save(person);
    }

    public Optional<Person> findById(int id) {
        return personRepository.findById(id);
    }

    @Transactional
    public void delete(int id) {
        personRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        personRepository.save(person);
    }

    public List<Book> findPersonBooks(int id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isPresent()) {
            List<Book> books = person.get().getBooks();
            for (Book book : books) {
                LocalDate taken = new java.sql.Date(book
                        .getTakenAt()
                        .getTime())
                        .toLocalDate();
                if (taken.plusDays(10).isBefore(LocalDate.now())) {
                    book.setExpired(true);
                }
            }
            return books;
        }
        return Collections.emptyList();
    }

    public Optional<Person> findPersonByFullName(String fullName) {
        return personRepository.findByFullName(fullName);
    }
}
