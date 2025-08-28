package com.example.logintodo.controllers;

import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController{
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(PersonRepository personRepository,PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder=passwordEncoder;
    }

    @GetMapping("/register")
    public String showRegisterForm() {
        return "register";
    }

    @PostMapping("/register")
    public String processRegister(@RequestParam String username,
                                  @RequestParam String email,
                                  @RequestParam String password,
                                  @RequestParam String confirmPassword,
                                  Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match!");
            return "register";
        }
        if (personRepository.findByUserName(username).isPresent()) {
            model.addAttribute("error", "Username already exists!");
            return "register";
        }
        if (personRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already in use!");
            return "register";
        }

        Person newPerson = new Person();
        newPerson.setUserName(username);
        newPerson.setEmail(email);
        newPerson.setPassword(passwordEncoder.encode(password));

        personRepository.save(newPerson);

        model.addAttribute("success", "Registration successful! Please log in.");
        return "login";
    }
}
