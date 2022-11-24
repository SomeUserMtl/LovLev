package com.project.lovlev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.lovlev.enums.Sex;
import lombok.Data;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "username",unique = true)
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "roles")
    private String roles;

    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Partner> partners = new ArrayList<>();
}
