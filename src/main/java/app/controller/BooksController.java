package app.controller;

import app.dao.BookDao;
import app.dao.PersonDao;
import app.model.Book;
import app.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookDao bookDao;
    private final PersonDao personDao;

    @Autowired
    public BooksController(BookDao bookDao, PersonDao personDao) {
        this.bookDao = bookDao;
        this.personDao = personDao;
    }

    @GetMapping()
    public String getBooks(Model model) {
        model.addAttribute("books", bookDao.readBooks());
        return "book/books";
    }

    @GetMapping("/new")
    public String addBook(@ModelAttribute("book") Book book) {
        return "book/addBook";
    }

    @PostMapping()
    public String addBook(@ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "book/addBook";
        }
        bookDao.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String updateBook(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookDao.findById(id).get());
        return "book/editBook";
    }

    @PostMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "book/editBook";
        }
        bookDao.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@ModelAttribute("person") Person person,
                           Model model,
                           @PathVariable("id") int id) {
        Book book = bookDao.findById(id).get();
        if (book.getPerson_id() != null) {
            model.addAttribute("bookHolder", personDao.findById(book.getPerson_id()).get());
        }
        model.addAttribute("book", book);
        model.addAttribute("people", personDao.readPeople());
        return "book/showBook";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookDao.delete(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/addBookToPerson")
    public String addBookToPerson(@PathVariable("id") int id,
                                  @ModelAttribute("person") Person person) {
        bookDao.addPersonToBook(person.getId(), id);
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        bookDao.freeBook(id);
        return "redirect:/books/" + id;
    }
}
