package com.example.Library_SpringBoot.controllers;

import com.example.Library_SpringBoot.models.Person;
import com.example.Library_SpringBoot.services.PersonService;
import com.example.Library_SpringBoot.utils.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/people")
public class PeopleController {

    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public PeopleController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String getPeople(Model model) {
        model.addAttribute("people", personService.readPeople());
        return "person/people";
    }

    @GetMapping("/new")
    public String addPerson(@ModelAttribute("person") Person person) {
        return "person/addPerson";
    }

    @PostMapping()
    public String addPerson(@ModelAttribute("person") @Valid Person person,
                            BindingResult bindingResult) {
        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "person/addPerson";
        }
        personService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String updatePerson(Model model, @PathVariable int id) {
        model.addAttribute("person", personService.findById(id).get());
        return "person/editPerson";
    }

    @PostMapping("/{id}")
    public String updatePerson(@ModelAttribute("person") @Valid Person person,
                               BindingResult bindingResult,
                               @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "person/editPerson";
        }
        personService.update(id, person);
        return "redirect:/people";
    }

    @GetMapping("/{id}")
    public String showPerson(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", personService.findById(id).get());
        model.addAttribute("books", personService.findPersonBooks(id));
        return "person/showPerson";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        if (personService.findPersonBooks(id).isEmpty()) {
            personService.delete(id);
            return "redirect:/people";
        } else {
            return "person/personError";
        }
    }
}
