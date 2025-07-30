package com.example.logintodo.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    private  int id;
    private String firstname;
    private String lastname;
    private String email;
    private Set<Todo> todo= new HashSet<>();


}
