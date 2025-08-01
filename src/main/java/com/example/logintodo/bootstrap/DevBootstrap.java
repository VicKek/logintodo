package com.example.logintodo.bootstrap;

import com.example.logintodo.model.Person;
import com.example.logintodo.model.Task;
import com.example.logintodo.repositories.PersonRepository;
import com.example.logintodo.repositories.TaskRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;

@Component
public class DevBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final PersonRepository personRepository;
    private final TaskRepository taskRepository;

    public DevBootstrap(PersonRepository personRepository,TaskRepository taskRepository){
        this.personRepository=personRepository;
        this.taskRepository=taskRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        initData();
    }

    private void initData(){

        Person p1= Person.builder()
                .userName("JohnDoe")
                .password("pass1")
                .email("email1")
                .tasks(new HashSet<>())
                .build();

        Person p2= Person.builder()
                .userName("Le Bron James")
                .password("pass1")
                .email("email2")
                .tasks(new HashSet<>())
                .build();

        Person p3= Person.builder()
                .userName("Victor Kekas")
                .password("pass1")
                .email("email3")
                .tasks(new HashSet<>())
                .build();

        Task t1= Task.builder()
                .taskName("Go for a walk")
                .description("Walk at least 5 KM")
                .status(false)
                .people(new HashSet<>())
                .build();

        Task t2= Task.builder()
                .taskName("Go for a swim")
                .description("Swim at least 2 KM")
                .status(false)
                .people(new HashSet<>())
                .build();

        Task t3= Task.builder()
                .taskName("Do the dishes")
                .description("Sink must be clean until mom gets home")
                .status(false)
                .people(new HashSet<>())
                .build();

        Task t4= Task.builder()
                .taskName("Water the plants")
                .description("Water all the plants in the garden")
                .status(false)
                .people(new HashSet<>())
                .build();

        System.out.println("#### ADDING DATA TO ");

        p1.addTask(t1);
        p1.addTask(t2);
        p1.addTask(t4);

        p2.addTask(t2);
        p2.addTask(t3);
        p2.addTask(t4);

        p3.addTask(t1);
        p3.addTask(t4);

        personRepository.saveAll(List.of(p1, p2, p3));

        System.out.println("#### DATA ADDED ");

    }

}
