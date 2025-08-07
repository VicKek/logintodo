package com.example.logintodo.repositories;

import com.example.logintodo.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person,Integer> {

    Optional<Person> findByUserName(String userName);

    boolean existsByUserName(String userName);

}
