package com.example.logintodo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Todo {

    String taskName;
    String taskDescription;
    boolean done = false;

}
