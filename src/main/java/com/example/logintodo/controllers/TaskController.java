package com.example.logintodo.controllers;

import com.example.logintodo.repositories.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @RequestMapping("/tasks")
    public String getTasks(Model model){
        model.addAttribute("tasks",taskRepository.findAll());

        return "tasks";
    }
}
