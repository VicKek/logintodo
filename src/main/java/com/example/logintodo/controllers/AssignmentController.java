package com.example.logintodo.controllers;

import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.PersonRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;

@Controller
public class AssignmentController {

    private final PersonRepository personRepository;

    public AssignmentController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @GetMapping("/assignments/{id}")
    public String userAssignments(@PathVariable Long id, Model model, Principal principal) {
        Person loggedUser = personRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!loggedUser.getId().equals(id)) {
            throw new AccessDeniedException("You cannot view other users' assignments");
        }

        model.addAttribute("user", loggedUser);
        model.addAttribute("tasks", loggedUser.getTasks());
        return "assignments"; // Make sure assignments.html exists under src/main/resources/templates
    }
}
