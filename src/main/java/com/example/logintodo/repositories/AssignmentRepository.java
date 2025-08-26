package com.example.logintodo.repositories;

import com.example.logintodo.model.Assignment;
import com.example.logintodo.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AssignmentRepository extends CrudRepository<Assignment,Long> {
    List<Assignment> findByPersonId(Long personId);
}
