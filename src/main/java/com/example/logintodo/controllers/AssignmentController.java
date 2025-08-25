package com.example.logintodo.controllers;

import com.example.logintodo.model.Person;
import com.example.logintodo.model.Task;
import com.example.logintodo.repositories.PersonRepository;
import com.example.logintodo.repositories.TaskRepository;
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
    private final TaskRepository taskRepository;

    public AssignmentController(PersonRepository personRepository, TaskRepository taskRepository) {
        this.personRepository = personRepository;
        this.taskRepository = taskRepository;
    }

    @GetMapping("/{id}")
    public String userAssignments(@PathVariable Long id, Model model, Principal principal) {
        Person loggedUser = personRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!loggedUser.getId().equals(id)) {
            throw new AccessDeniedException("You cannot view other users' assignments");
        }

        List<Task> completedTasks = loggedUser.getTasks().stream()
                .filter(Task::isStatus)
                .toList();

        List<Task> pendingTasks = loggedUser.getTasks().stream()
                .filter(t -> !t.isStatus())
                .toList();

        model.addAttribute("completedTasks", completedTasks);
        model.addAttribute("pendingTasks", pendingTasks);
        model.addAttribute("userId", loggedUser.getId());

        return "assignments";
    }

    @PostMapping("/{userId}/toggle-ajax/{taskId}")
    @ResponseBody
    public String toggleTaskStatusAjax(@PathVariable Long userId,
                                       @PathVariable Long taskId,
                                       Principal principal) {
        Person user = personRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getUserName().equals(principal.getName())) {
            throw new AccessDeniedException("Cannot modify others' tasks!");
        }

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(!task.isStatus());
        taskRepository.save(task);

        return "success";
    }
}
