package com.springProject.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@Table(name = "entities")
public class SimpleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @NotNull
    @Column
    Integer amount;

    @NotNull
    @Column
    Integer price;

    @NotNull
    @Column
    Integer totalPrice;

    @NotNull
    @Column
    String status;

    @NotNull
    @Column
    Date releaseDate;


    @Column
    String comment;
}
