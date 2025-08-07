package com.example.logintodo.controllers;

import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController{
    private final PersonRepository personRepository;

    public RegisterController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("person", new Person());
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute Person person, Model model) {
        if (personRepository.existsByUserName(person.getUserName())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        // NOTE: For security, use BCryptPasswordEncoder in real projects
        personRepository.save(person);
        return "redirect:/login";
    }
}
