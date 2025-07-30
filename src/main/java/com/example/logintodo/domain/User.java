package com.example.logintodo.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {

    int id;
    String firstname;
    String lastname;
    String email;
    List<Todo> todo;

}
