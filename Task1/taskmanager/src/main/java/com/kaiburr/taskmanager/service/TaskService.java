// src/main/java/com/kaiburr/taskmanager/service/TaskService.java
package com.kaiburr.taskmanager.service;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.model.TaskExecution;
import com.kaiburr.taskmanager.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repo;

    public List<Task> all() { return repo.findAll(); }

    public Optional<Task> byId(String id) { return repo.findById(id); }

    public List<Task> findByName(String q) { return repo.findByNameContainingIgnoreCase(q); }

    public Task save(Task t) { return repo.save(t); }

    public void delete(String id) { repo.deleteById(id); }

    /** Executes the command locally for Task 1.  
     *  In Task 2 you will replace this with “launch a BusyBox pod and exec”. */
    public TaskExecution run(Task task) throws Exception {
        Instant start = Instant.now();
        Process p = new ProcessBuilder(task.getCommand().split(" ")).start();
        String output;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
            output = br.lines().reduce("", (a, b) -> a + b + System.lineSeparator());
        }
        p.waitFor();
        Instant end = Instant.now();

        TaskExecution exec = new TaskExecution(start, end, output.trim());
        task.getTaskExecutions().add(exec);
        repo.save(task);
        return exec;
    }
}
