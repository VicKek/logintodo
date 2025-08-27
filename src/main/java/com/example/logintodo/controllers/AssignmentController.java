package com.example.logintodo.controllers;

import com.example.logintodo.model.Assignment;
import com.example.logintodo.model.Person;
import com.example.logintodo.repositories.AssignmentRepository;
import com.example.logintodo.repositories.PersonRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;


@Controller
@RequestMapping("/assignments")
public class AssignmentController {

    private final PersonRepository personRepository;
    private final AssignmentRepository assignmentRepository;

    public AssignmentController(PersonRepository personRepository, AssignmentRepository assignmentRepository) {
        this.personRepository = personRepository;
        this.assignmentRepository = assignmentRepository;
    }

    @GetMapping("/{id}")
    public String userAssignments(@PathVariable Long id, Model model, Principal principal) {
        Person loggedUser = personRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!loggedUser.getId().equals(id)) {
            throw new AccessDeniedException("You cannot view other users' assignments");
        }

        List<Assignment> userAssignments = assignmentRepository.findByPersonId(id);

        List<Assignment> completedAssignments = userAssignments.stream()
                .filter(Assignment::isStatus)
                .toList();

        List<Assignment> pendingAssignments = userAssignments.stream()
                .filter(a -> !a.isStatus())
                .toList();

        model.addAttribute("completedAssignments", completedAssignments);
        model.addAttribute("pendingAssignments", pendingAssignments);
        model.addAttribute("userId", loggedUser.getId());

        return "assignments";
    }

}

