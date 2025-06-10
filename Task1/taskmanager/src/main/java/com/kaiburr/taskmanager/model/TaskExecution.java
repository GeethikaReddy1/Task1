// src/main/java/com/kaiburr/taskmanager/model/TaskExecution.java
package com.kaiburr.taskmanager.model;

import java.time.Instant;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor
public class TaskExecution {
    private Instant startTime;
    private Instant endTime;
    private String output;
}
