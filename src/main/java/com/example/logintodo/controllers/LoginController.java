package com.example.logintodo.controllers;

import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    private final PersonRepository personRepository;

    public LoginController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/login")
    public String loginPage(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            String logoutMessage = (String) session.getAttribute("logoutMessage");
            if (logoutMessage != null) {
                model.addAttribute("logoutMessage", logoutMessage);
                session.removeAttribute("logoutMessage");
            }
        }
        return "login";
    }
}
