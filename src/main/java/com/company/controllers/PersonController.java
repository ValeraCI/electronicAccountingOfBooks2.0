package com.company.controllers;


import com.company.models.Person;
import com.company.services.PersonService;
import com.company.util.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

//TODO сделать так, что бы невозможно было добавить двух одинаковых людей

@Controller
@RequestMapping("/people")
public class PersonController {
    private final PersonService personService;
    private final PersonValidator personValidator;

    @Autowired
    public PersonController(PersonService personService, PersonValidator personValidator) {
        this.personService = personService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(@RequestParam(value = "receive", required = false) String receive,
                        @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
                        Model model){

        List<Person> people;

        if(receive == null) receive = "any";
        if(pageNumber == null || pageNumber < 0) pageNumber = 0;

        while (true) {
            if (receive != null && receive.equals("free")) {
                model.addAttribute("titleText", "Лиди без книг:");
                people = personService.getWithoutBooks(pageNumber);
            } else if (receive != null && receive.equals("busy")) {
                model.addAttribute("titleText", "Люди с книгами:");
                people = personService.getWithBooks(pageNumber);
            } else {
                model.addAttribute("titleText", "Все люди:");
                people = personService.getAll(pageNumber);
            }
            if(people.size() != 0 || pageNumber == 0) break;
            else pageNumber--;
        }
        model.addAttribute("receive", receive);
        model.addAttribute("next", pageNumber+1);
        model.addAttribute("prev", pageNumber-1);
        model.addAttribute("people", people);
        return "person/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable int id, Model model){
        Person person = personService.getFromId(id);
        model.addAttribute("person", person);
        model.addAttribute("personBooks", person.getRentedBook());
        return "person/show";
    }

    @GetMapping("/{id}/edit")
    public String updateForm(@PathVariable int id, Model model){
        Person person = personService.getFromId(id);
        model.addAttribute("person", person);
        return "person/edit";
    }

    @PatchMapping("/{id}")
    public String edit(@PathVariable int id, @ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult, Model model){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("person", person);
            return "person/edit";
        }
        personService.edit(id, person);
        return "redirect:/people";
    }

    @GetMapping("/new")
    public String addForm(Model model){
        model.addAttribute("person", new Person());
        return "person/add";
    }

    @PostMapping()
    public String save(@ModelAttribute("person") @Valid Person person,
                       BindingResult bindingResult, Model model){
        personValidator.validate(person, bindingResult);
        if(bindingResult.hasErrors()){
            model.addAttribute("person", person);
            return "person/add";
        }
        personService.add(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        personService.delete(id);
        return "redirect:/people";
    }
}
