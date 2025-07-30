package com.example.logintodo.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String taskName;
    private String taskDescription;
    private boolean done = false;

    @ManyToMany
    @JoinTable(name="people_todo", joinColumns=@JoinColumn(name="todo_id"),
    inverseJoinColumns = @JoinColumn(name = "person_id"))
    private Set<Person> people = new HashSet<>();

}
