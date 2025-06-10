// src/main/java/com/kaiburr/taskmanager/controller/TaskController.java
package com.kaiburr.taskmanager.controller;

import com.kaiburr.taskmanager.model.Task;
import com.kaiburr.taskmanager.model.TaskExecution;
import com.kaiburr.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;

    /** GET /tasks or /tasks?id=123 */
    @GetMapping
    public ResponseEntity<?> get(@RequestParam(required = false) String id) {
        if (id == null) return ResponseEntity.ok(service.all());
        return service.byId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** GET /tasks/search?q=Print */
    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam String q) {
        List<Task> found = service.findByName(q);
        return found.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(found);
    }

    /** PUT /tasks  (create or overwrite) */
    @PutMapping
    public ResponseEntity<?> put(@Valid @RequestBody Task task) {
        Task saved = service.save(task);
        return ResponseEntity.created(URI.create("/tasks?id=" + saved.getId())).body(saved);
    }

    /** DELETE /tasks/:id */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    /** PUT /tasks/{id}/run  – execute the command */
    @PutMapping("/{id}/run")
    public ResponseEntity<?> run(@PathVariable String id) throws Exception {
        Task task = service.byId(id).orElseThrow();
        TaskExecution exec = service.run(task);
        return ResponseEntity.ok(exec);
    }
}
