package io.github.GreenMushroomer03.todoapp.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@MappedSuperclass
abstract class TaskBased {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int id;
    @JsonProperty("description")
    @NotBlank(message = "Description must not be empty")
    public String description;

    public TaskBased() {

    }

    public int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void updateFrom(TaskBased source) {
       description = source.description;
    }
}
