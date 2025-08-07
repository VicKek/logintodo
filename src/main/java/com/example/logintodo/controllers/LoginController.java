package com.example.logintodo.controllers;

import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Controller
public class LoginController {

    private final PersonRepository personRepository;

    public LoginController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("person", new Person());
        return "login";
    }

    @PostMapping("/login")
    public String processLogin(@ModelAttribute Person person, Model model) {
        Optional<Person> existing = personRepository.findByUserName(person.getUserName());

        if (existing.isPresent() && existing.get().getPassword().equals(person.getPassword())) {
            // Save user to session if needed (for now just redirect)
            return "redirect:/dashboard"; // Replace with actual home/dashboard
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "login";
        }
    }

}
