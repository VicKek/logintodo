package com.example.logintodo.repositories;

import com.example.logintodo.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task,Integer> {

}
