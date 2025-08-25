package com.example.logintodo.repositories;

import com.example.logintodo.model.Person;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonRepository extends CrudRepository<Person,Long> {

    Optional<Person> findByUserName(String userName);
    Optional<Person> findByEmail(String Email);

    boolean existsByUserName(String userName);

}
