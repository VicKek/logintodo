package com.example.logintodo.controllers;

import com.example.logintodo.model.Assignment;
import com.example.logintodo.model.Person;
import com.example.logintodo.model.Task;
import com.example.logintodo.repositories.AssignmentRepository;
import com.example.logintodo.repositories.PersonRepository;
import com.example.logintodo.repositories.TaskRepository;
import org.springframework.boot.actuate.web.exchanges.HttpExchange;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/assignments/ajax")
public class AssignmentAJAXController {

    private final PersonRepository personRepository;
    private final AssignmentRepository assignmentRepository;
    private final TaskRepository taskRepository;

    public AssignmentAJAXController(PersonRepository personRepository,
                                    AssignmentRepository assignmentRepository,
                                    TaskRepository taskRepository) {
        this.personRepository = personRepository;
        this.assignmentRepository = assignmentRepository;
        this.taskRepository = taskRepository;
    }
    @PostMapping("/add")
    public Assignment addAssignmentWithTask(@RequestBody Map<String, String> payload, Principal principal) {
        String taskName = payload.get("taskName");
        String description = payload.get("description");

        Task task = new Task();
        task.setTaskName(taskName);
        task.setDescription(description);
        taskRepository.save(task);

        Person user = personRepository.findByUserName(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Assignment assignment = new Assignment();
        assignment.setTask(task);
        assignment.setPerson(user);
        assignment.setStatus(false);

        return assignmentRepository.save(assignment);
    }


    @PutMapping("/{assignmentId}/toggle")
    public Assignment toggleStatus(@PathVariable Long assignmentId, Principal principal) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getPerson().getUserName().equals(principal.getName())) {
            throw new AccessDeniedException("Cannot modify others' assignments");
        }

        assignment.setStatus(!assignment.isStatus());
        return assignmentRepository.save(assignment);
    }

    @DeleteMapping("/{assignmentId}/delete")
    public void deleteAssignment(@PathVariable Long assignmentId, Principal principal) {
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        if (!assignment.getPerson().getUserName().equals(principal.getName())) {
            throw new AccessDeniedException("Cannot delete others' assignments");
        }

        assignmentRepository.deleteById(assignmentId);
    }

}
