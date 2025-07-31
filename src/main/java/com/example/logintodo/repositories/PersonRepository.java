package com.example.logintodo.repositories;

import com.example.logintodo.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person,Integer> {


}
