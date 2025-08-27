package com.example.logintodo.controllers;

import com.example.logintodo.model.Task;
import com.example.logintodo.repositories.TaskRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public String getTasks(Model model){
        model.addAttribute("tasks",taskRepository.findAll());
        return "tasks";
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateStatus(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        String newStatus = payload.get("status");

        task.setStatus("complete".equalsIgnoreCase(newStatus));
        taskRepository.save(task);

        return ResponseEntity.ok(task);
    }

    @PostMapping("/add")
    public Task addTask(@RequestBody Task task) {
        return taskRepository.save(task);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            if (!taskRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            taskRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();  // Log the real cause
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
