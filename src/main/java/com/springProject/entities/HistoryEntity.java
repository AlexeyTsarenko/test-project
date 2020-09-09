package com.springProject.entities;


import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Data
@Table(name = "history")
public class HistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @Column
    Integer entityId;

    @NotNull
    @Column
    String fieldName;

    @NotNull
    @Column
    String oldValue;

    @NotNull
    @Column
    String newValue;
}
