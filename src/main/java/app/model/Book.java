package app.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class Book {
    private int id;
    private Integer person_id;

    @NotEmpty(message = "Заполните поле!")
    private String name;

    @NotEmpty(message = "Заполните поле!")
    private String author;

    @Min(value = 0, message = "Введите корректный год!")
    @NotNull(message = "Заполните поле!")
    private int year;

    public Book() {}

    public Book(int id, Integer person_id, String name, String author, int year) {
        this.id = id;
        this.person_id = person_id;
        this.name = name;
        this.author = author;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
