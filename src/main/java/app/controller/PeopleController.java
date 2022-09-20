package app.controller;

import app.dao.PersonDao;
import app.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonDao personDao;

    @Autowired
    public PeopleController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @GetMapping()
    public String getPeople(Model model) {
        model.addAttribute("people", personDao.readPeople());
        return "person/people";
    }

    @GetMapping("/new")
    public String addPerson(@ModelAttribute("person") Person person) {
        return "person/addPerson";
    }

    @PostMapping()
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "person/addPerson";
        }
        personDao.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String updatePerson(Model model, @PathVariable int id) {
        model.addAttribute("person", personDao.findById(id).get());
        return "person/editPerson";
    }

    @PostMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "person/editPerson";
        }
        personDao.update(id, person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String showPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personDao.findById(id).get());
        model.addAttribute("books", personDao.findPersonBooks(id));
        return "person/showPerson";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        if (personDao.findPersonBooks(id).isEmpty()) {
            personDao.delete(id);
            return "redirect:/people";
        } else {
            return "person/personError";
        }
    }
}
