package app.controller;

import app.model.Book;
import app.model.Person;
import app.services.BookService;
import app.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BooksController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String getBooks(Model model,
                           @RequestParam(name = "page", required = false) Integer page,
                           @RequestParam(name = "books_per_page", required = false) Integer books_per_page,
                           @RequestParam(name = "sort_by_year", required = false) Boolean sort_by_year) {
        model.addAttribute("books", bookService.readBooks(page, books_per_page, sort_by_year));
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
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String updateBook(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findById(id).get());
        return "book/editBook";
    }

    @PostMapping("/{id}")
    public String updateBook(@ModelAttribute("book") @Valid Book book,
                             BindingResult bindingResult,
                             @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "book/editBook";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @GetMapping("/{id}")
    public String showBook(@ModelAttribute("person") Person person,
                           Model model,
                           @PathVariable("id") int id) {
        Book book = bookService.findById(id).get();
        if (book.getPerson() != null) {
            model.addAttribute("bookHolder", personService.findById(book.getPerson().getId()).get());
        }
        model.addAttribute("book", book);
        model.addAttribute("people", personService.readPeople());
        return "book/showBook";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookService.deleteById(id);
        return "redirect:/books";
    }

    @PostMapping("/{id}/addBookToPerson")
    public String addBookToPerson(@PathVariable("id") int id,
                                  @ModelAttribute("person") Person person) {
        bookService.addPersonToBook(person.getId(), id);
        return "redirect:/books/" + id;
    }

    @PostMapping("/{id}/free")
    public String freeBook(@PathVariable("id") int id) {
        bookService.freeBook(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search() {
        return "book/search";
    }

    @PostMapping("/search")
    public String search(@RequestBody String to_find) {
        System.out.println(to_find);
        return "redirect:/books/search";
    }
}
