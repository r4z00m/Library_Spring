package app.util;

import app.model.Person;
import app.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        Optional<Person> optional = personService.findPersonByFullName(person.getFullName());
        if (optional.isPresent()) {
            if (person.getFullName().equals(optional.get().getFullName())) {
                errors.rejectValue("fullName", "", "Такой ФИО уже есть в базе!");
            }
        }
    }
}
