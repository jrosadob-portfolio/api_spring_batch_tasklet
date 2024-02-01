package com.jrosadob.batchtasklet.entities;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "persons")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personId;
    private String personName;

    @Column(name = "last_name")
    private String personLastName;
    private int age;

    @Column(name = "insertion_date")
    private String insertionDate;
}

