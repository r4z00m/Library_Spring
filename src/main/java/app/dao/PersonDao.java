package app.dao;

import app.model.Book;
import app.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Person> readPeople() {
        return jdbcTemplate
                .query("SELECT * FROM person",
                        new BeanPropertyRowMapper<>(Person.class));
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(fullname, birthyear) VALUES(?, ?)",
                person.getFullName(), person.getBirthYear());
    }

    public Optional<Person> findById(int id) {
        return jdbcTemplate.query("SELECT * FROM person WHERE id=?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public List<Book> findPersonBooks(int id) {
        return jdbcTemplate.query("select * from person join book on person.id = book.person_id where person.id=?",
                new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class));
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE person SET fullname=?, birthyear=? WHERE id=?",
                person.getFullName(), person.getBirthYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE id=?", id);
    }

    public Optional<Person> findPersonByFullName(String fullName) {
        return jdbcTemplate.query("SELECT * FROM person WHERE fullname=?",
                new Object[]{fullName},
                new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }
}
