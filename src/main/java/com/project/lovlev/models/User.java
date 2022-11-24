package com.project.lovlev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.lovlev.enums.Sex;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Pattern;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "users")
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "First name can only contain letters, numbers and spaces")
    @Column(name = "first_name")
    private String firstName;

//    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "Middle name can only contain letters, numbers and spaces")
    @Column(name = "middle_name")
    private String middleName;

//    @Pattern(regexp = "^[a-zA-Z\s]{0,20}$", message = "last name can only contain letters, numbers and spaces")
    @Column(name = "last_name")
    private String lastName;

//    @Email(message = "Email should be valid")
//    @Pattern(regexp = "^{5,30}$", message = "Email should be valid")
    @Column(name = "email", unique = true)
    private String email;

//    @Pattern(regexp = "^[a-zA-Z0-9\s]{4,8}$",
//            message = "Username can only contain letters, numbers and must be between 4-8 characters")
    @Column(name = "username",unique = true)
    private String username;

//    @Pattern(regexp = "^{4,20}$", message = "password must be between 4-20 characters, no spaces allowed")
    @Column(name = "password")
    private String password;

    @Pattern(regexp = "^ROLE_[A-Z]{1,10}$", message = "role can only contain uppercase letters")
    @Column(name = "roles")
    private String roles;

//    @Pattern(regexp = "^[0-9]{1,20}$", message = "sex can only contain numbers")
    @Column(name = "sex")
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Partner> partners = new HashSet<>();
}
