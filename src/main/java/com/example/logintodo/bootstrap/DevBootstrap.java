package com.example.logintodo.bootstrap;

import com.example.logintodo.model.Assignment;
import com.example.logintodo.model.Person;
import com.example.logintodo.model.Task;
import com.example.logintodo.repositories.AssignmentRepository;
import com.example.logintodo.repositories.PersonRepository;
import com.example.logintodo.repositories.TaskRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final PersonRepository personRepository;
    private final TaskRepository taskRepository;
    private final PasswordEncoder passwordEncoder;
    private final AssignmentRepository assignmentRepository;

    public DevBootstrap(PersonRepository personRepository, TaskRepository taskRepository, PasswordEncoder passwordEncoder,AssignmentRepository assignmentRepository){
        this.personRepository=personRepository;
        this.taskRepository=taskRepository;
        this.passwordEncoder = passwordEncoder;
        this.assignmentRepository=assignmentRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData(){
        System.out.println("#### INITIALIZING DATA ####");

        Person p1 = Person.builder()
                .userName("JohnDoe")
                .password(passwordEncoder.encode("pass1"))
                .email("email1@gmail.com")
                .build();

        Person p2 = Person.builder()
                .userName("LeBronJames")
                .password(passwordEncoder.encode("pass1"))
                .email("email2@gmail.com")
                .build();

        Person p3 = Person.builder()
                .userName("VictorKekas")
                .password(passwordEncoder.encode("pass1"))
                .email("email3@gmail.com")
                .build();

        Task t1 = Task.builder()
                .taskName("Go for a walk")
                .description("Walk at least 5 KM")
                .build();

        Task t2 = Task.builder()
                .taskName("Go for a swim")
                .description("Swim at least 2 KM")
                .build();

        Task t3 = Task.builder()
                .taskName("Do the dishes")
                .description("Sink must be clean until mom gets home")
                .build();

        Task t4 = Task.builder()
                .taskName("Water the plants")
                .description("Water all the plants in the garden")
                .build();

        personRepository.saveAll(List.of(p1, p2, p3));
        taskRepository.saveAll(List.of(t1, t2, t3, t4));

        Assignment a1 = Assignment.builder().
                person(p1).
                task(t1).
                status(false).
                build();
        Assignment a2 = Assignment.builder().
                person(p1).
                task(t2).
                status(false).
                build();
        Assignment a3 = Assignment.
                builder().
                person(p1).
                task(t4).
                status(false).
                build();

        Assignment a4 = Assignment.
                builder().
                person(p2).
                task(t2).
                status(false).
                build();
        Assignment a5 = Assignment.
                builder().person(p2).
                task(t3).
                status(false).
                build();
        Assignment a6 = Assignment.builder().
                person(p2).
                task(t4).
                status(false).
                build();

        Assignment a7 = Assignment.builder().
                person(p3).
                task(t1).
                status(false).
                build();
        Assignment a8 = Assignment.builder().
                person(p3).
                task(t4).
                status(false).
                build();

        assignmentRepository.saveAll(List.of(a1,a2,a3,a4,a5,a6,a7,a8));

        System.out.println("#### DATA ADDED FROM DEV BOOTSTRAP ####");
    }

}
