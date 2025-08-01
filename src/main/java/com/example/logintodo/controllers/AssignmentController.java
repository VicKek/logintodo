package com.example.logintodo.controllers;


import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class AssignmentController {

    @Autowired
    private final PersonRepository personRepository;

    public AssignmentController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @RequestMapping("/assignment")
    public String showAssignmentPage(Model model) {
        List<Person> people = (List<Person>) personRepository.findAll();
        model.addAttribute("people", people);
        return "assignment"; // matches assignment.html in templates folder
    }
}
