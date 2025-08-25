package com.example.logintodo.controllers;

import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String loginPage(HttpServletRequest request, Model model) {
        // Check if there is a logout message in the session
        HttpSession session = request.getSession(false);
        if (session != null) {
            String logoutMessage = (String) session.getAttribute("logoutMessage");
            if (logoutMessage != null) {
                model.addAttribute("logoutMessage", logoutMessage);
                session.removeAttribute("logoutMessage"); // remove so it doesn't show again
            }
        }
        return "login"; // your login.html
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
