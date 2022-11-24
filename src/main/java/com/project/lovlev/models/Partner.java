package com.project.lovlev.models;

import com.project.lovlev.enums.Sex;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "partner")
public class Partner{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "age")
    private int age;

    @Column(name = "height")
    private String height;

    @Column(name = "hair_color")
    private String hairColor;

    @Column(name = "eye_color")
    private String eyeColor;

    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

//    @ManyToOne(fetch = FetchType.EAGER)
    private Long user;
}
