// src/main/java/com/kaiburr/taskmanager/model/Task.java
package com.kaiburr.taskmanager.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "tasks")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Task {

    @Id
    private String id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Owner is mandatory")
    private String owner;

    /**
     * Accepts only safe shell tokens (letters, numbers, dash, underscore, space).
     * Change the regex if you need more flexibility, but keep security in mind.
     */
    @Pattern(
      regexp = "^[a-zA-Z0-9_\\- ]{1,120}$",
      message = "Command contains disallowed characters"
    )
    private String command;

    @Builder.Default
    private List<TaskExecution> taskExecutions = new ArrayList<>();
}
