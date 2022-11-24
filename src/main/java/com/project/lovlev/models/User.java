package com.project.lovlev.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.lovlev.enums.Sex;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
//    @Pattern(regexp = "^{5,30}$", message = "Email should be valid")
    @Column(name = "email", unique = true)
    private String email;

    @Pattern(regexp = "^[a-zA-Z0-9]{3,15}$",
            message = "Username can only contain letters, numbers and must be between 4-8 characters")
    @Column(name = "username",unique = true)
    private String username;

//    @Min(value = 4, message = "password must be between 4-20 characters, no spaces allowed")
//    @Max(value = 20, message = "password must be between 4-20 characters, no spaces allowed")
    @Column(name = "password")
    private String password;

    @Pattern(regexp = "^ROLE_[A-Z]{1,10}$", message = "role must only contain uppercase letters of th following format: ROLE_[A-Z]")
    @Column(name = "roles")
    private String roles;

    @Column(name = "sex")
    @Enumerated(EnumType.ORDINAL)
    private Sex sex;

    @JsonIgnore
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Partner> partners = new HashSet<>();
}
